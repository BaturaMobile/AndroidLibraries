package com.baturamobile.aioistartcard;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.view.View;

import com.baturamobile.aioistartcard.smarttag.ProgressListener;
import com.baturamobile.utils.PoolManager;

import java.lang.ref.WeakReference;

import static com.baturamobile.aioistartcard.MpHelper.FN_READ_DATA;


/**
 * Created by vssnake on 26/06/2017.
 */

public class SmartTagDelegate implements ProgressListener {

    WeakReference<StartTagDelegateInterface> appCompatActivityWeakReference;

    SmartTagDelegate(StartTagDelegateInterface appCompatActivity){
        appCompatActivityWeakReference = new WeakReference<>(appCompatActivity);
    }

    private NfcAdapter mAdapter = null;
    private SmartTagCommandProcessor smartTagCommandProcessor;

    private int mNfcStatus;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    private SmartTag mSmartTag;

    private static final int INIT_NFC_OK = 0;
    private static final int INIT_NFC_NOTFOUND = 1;
    private static final int INIT_NFC_DISABLED = 2;
    private static final int INIT_NFC_ERROR = 9;


    private volatile boolean mIsBusy = false;


    void onCreate(Intent intent){
        mSmartTag = new SmartTag();
        mSmartTag.setProgressListener(this);
        smartTagCommandProcessor = new SmartTagCommandProcessor(mSmartTag);
        startNfc();
        processCommands(intent);
    }

    void onPause(){
        Common.addLogi("stop adapter");
        try{
            if(mNfcStatus == INIT_NFC_OK){
                mAdapter.disableForegroundDispatch(appCompatActivityWeakReference.get().getActivity());
            }
        }catch(Exception e){
            Common.addLoge(e.getMessage());
        }
    }

    void onResume(){
        Common.addLogi("start adapter");
        if(mNfcStatus == INIT_NFC_OK){
            mAdapter.enableForegroundDispatch(
                    appCompatActivityWeakReference.get().getActivity(), mPendingIntent, mFilters, mTechLists);
        }

    }

    void onNewIntent(Intent intent){
        if(mIsBusy){
            return;
        }


        Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        //mIdView.setText("ID: " + Common.makeHexText(idm));

        mSmartTag.selectTarget(idm, tag);

        if(!mSmartTag.isSmartTag()){
            //   mErrorView.setText(getString(R.string.scanTagPrompt));
            return;
        }

        mIsBusy = true;
        PoolManager.sPoolManager.addToUiQueue(preExecute);
        PoolManager.sPoolManager.addToQueue(startSession);
       // mTagTask.execute();
    }

    /**
     * Starts NFC
     * @return 0:success 1:adapter not found 2:NFC disabled
     */
    private int startNfc(){
        try{
            mAdapter = NfcAdapter.getDefaultAdapter(appCompatActivityWeakReference.get().getActivity());

            if(mAdapter == null){
                return INIT_NFC_NOTFOUND;
            }else{
                if(!mAdapter.isEnabled()){
                    return INIT_NFC_DISABLED;
                }
                mPendingIntent =
                        PendingIntent.getActivity(appCompatActivityWeakReference.get().getActivity()
                                , 0,
                        new Intent(appCompatActivityWeakReference.get().getActivity(),
                                appCompatActivityWeakReference.get().getActivity().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                mFilters = new IntentFilter[]{
                        new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                };
                mTechLists = new String[][]{new String[] { NfcF.class.getName() }};
            }
        }catch(Exception e){
            Common.addLoge(e.toString());
            return INIT_NFC_ERROR;
        }
        return INIT_NFC_OK;
    }



    private void processCommands(Intent intent){
        smartTagCommandProcessor.processCommand(intent);
    }

    @Override
    public void onProgress(final int progress) {
        PoolManager.sPoolManager.addToUiQueue(new Runnable() {
            @Override
            public void run() {
                if (appCompatActivityWeakReference.get() != null) {
                    appCompatActivityWeakReference.get().nfcShowProgress(progress);
                }
            }
        });
    }

    @Override
    public void onMaxChanged(int maximum) {
        if (appCompatActivityWeakReference.get() != null){
            appCompatActivityWeakReference.get().nfcMaxChanged(maximum);
        }
    }


    private Runnable startSession = new Runnable() {
        @Override
        public void run() {
            mSmartTag.startSession();
            PoolManager.sPoolManager.addToUiQueue(postExecute);
        }
    };

    private Runnable preExecute = new Runnable() {
        @Override
        public void run() {
            if (appCompatActivityWeakReference.get() != null){
                appCompatActivityWeakReference.get().nfcVisibleProgress(View.VISIBLE);
                appCompatActivityWeakReference.get()
                        .onProcessing();

                appCompatActivityWeakReference.get().nfcSetIndeterminate(true);
            }
        }
    };



    private Runnable postExecute = new Runnable() {
        @Override
        public void run() {
            if (appCompatActivityWeakReference.get() != null){
                appCompatActivityWeakReference.get().nfcVisibleProgress(View.INVISIBLE);
            }


            Exception error = mSmartTag.getLastError();
            if(error != null){
                if (appCompatActivityWeakReference.get() != null){
                    appCompatActivityWeakReference.get().nfcShowProgress(0);
                    appCompatActivityWeakReference.get()
                            .onNFCError(error);
                }

            }else{
                if (appCompatActivityWeakReference.get() != null){
                    appCompatActivityWeakReference.get()
                            .onNFCSuccess();
                    appCompatActivityWeakReference.get().onNFCSuccess();

                    if (smartTagCommandProcessor.getCommand() == FN_READ_DATA){
                        appCompatActivityWeakReference.get().onNFCDataRead(mSmartTag.getReadText());
                    }
                }

            }
            mIsBusy = false;
        }
    };
}
