package com.baturamobile.bluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by unai on 19/08/2016.
 */

public class BluetoothLECore {

    private static final String TAG = BluetoothLECore.class.getSimpleName();

    private Context appContext;

    private BluetoothAdapter mBluetoothAdapter;
    final BluetoothManager mBluetoothManager;

    private BluetoothState mBluetoothState;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothLECore(Context context) throws BluetoothNotSupportedException {


        this.appContext = context;

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);


        if (BluetoothLEHelper.hasBluetoothLEAvailable(context)){
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }else{
            LogManager.e(TAG,"This devices does not support BluetoothLe");
            throw new BluetoothNotSupportedException();
        }

    }




    protected void enableAdapter(BluetoothState bluetoothState){
        mBluetoothState = bluetoothState;
        if (!getBluetoothAdapter().isEnabled()){
            LogManager.d(TAG,"on EnableAdapter | enable Adapter");
            //TODO this is not android like change in the future
            getBluetoothAdapter().enable();
        }else{
            LogManager.d(TAG,"on EnableAdapter | adapter is already enabled");
            mBluetoothState.onBluetoothChangeState(true);
        }

    }

    protected void disableAdapter(BluetoothState bluetoothState){
        if (getBluetoothAdapter().isEnabled())
            mBluetoothState = bluetoothState;
            LogManager.d(TAG,"on DisabelAdapter | disable Adapter");
            getBluetoothAdapter().disable();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if (mBluetoothState != null){
                            mBluetoothState.onBluetoothChangeState(false);
                        }
                        break;
                    case BluetoothAdapter.STATE_ON:
                        if (mBluetoothState != null){
                            mBluetoothState.onBluetoothChangeState(true);
                        }
                        break;
                }
            }
        }
    };


    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public Context getAppContext() {
        return appContext;
    }

    public interface BluetoothState{
        void onBluetoothChangeState(boolean enabled);
    }
}
