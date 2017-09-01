package com.baturamobile.bluetoothle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.test.suitebuilder.annotation.Suppress;

import com.baturamobile.bluetoothle.wrapers.BluetoothDeviceWrapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by unai on 19/08/2016.
 */

public class BluetoothLEDiscovery {

    private static final String TAG = BluetoothLEDiscovery.class.getSimpleName();

    BluetoothLECore mBluetoothLECore;

    private BluetoothLeScanner mBluetoothScanner;

    private ScanResult mScanResult;
    private Handler mStopScanningHandler;

    public BluetoothLEDiscovery(BluetoothLECore bluetoothLECore){
        this.mBluetoothLECore = bluetoothLECore;
        mStopScanningHandler = new Handler();
    }

    @Suppress
    public void scanBleDevices(final UUID[] servicesUUID,
                                  final ScanResult scanResult) {
        mBluetoothLECore.enableAdapter(new BluetoothLECore.BluetoothState() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onBluetoothChangeState(boolean enabled) {

                if (enabled){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        LogManager.d(TAG, "on ScanBleDevices | LolliPOP or greater detected");
                        BluetoothLEDiscovery.this.scanBleDevicesApi21Above(createScanFilterWithUUID(servicesUUID));
                    }else{
                        LogManager.d(TAG,"on ScanBleDevices | PRE-LOLLIPOP");
                        scanBleDevicesLegacy(servicesUUID);
                    }

                    BluetoothLEDiscovery.this.mScanResult = scanResult;
                    mStopScanningHandler.postDelayed(stopScanningHandler, BluetoothLEConfig.SCAN_PERIOD_IN_MS);
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopScan(){

        mStopScanningHandler.removeCallbacks(stopScanningHandler);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            mBluetoothLECore.getBluetoothAdapter().stopLeScan(leScanCallback);
        }else{
            mBluetoothScanner.stopScan(scanCallbackApi21);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<ScanFilter> createScanFilterWithUUID(UUID[] servicesUUID){
        if (servicesUUID == null) return null;

        List<ScanFilter> scanFilters = new ArrayList<>();
        for (int x = 0; x< servicesUUID.length;x++){
            ScanFilter.Builder builder = new ScanFilter.Builder();
            builder.setServiceUuid(new ParcelUuid(servicesUUID[x]));
            scanFilters.add(builder.build());
        }
        return scanFilters;

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scanBleDevicesApi21Above(@Nullable List<ScanFilter> scanFilters){

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build();

        mBluetoothScanner = mBluetoothLECore.getBluetoothAdapter().getBluetoothLeScanner();

        mBluetoothScanner.startScan(scanFilters,scanSettings,scanCallbackApi21);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanBleDevicesLegacy(UUID[] uuids){
        mBluetoothLECore.getBluetoothAdapter().startLeScan(uuids,leScanCallback);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback scanCallbackApi21 = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            LogManager.e(TAG,"onScanFailed " + errorCode);
            mScanResult.onError(errorCode);
            super.onScanFailed(errorCode);
        }

        @Override
        public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
            LogManager.d(TAG,"onScanResult " + result.getDevice().getAddress());
            super.onScanResult(callbackType, result);

            mScanResult.onDeviceFound(new BluetoothDeviceWrapp(result.getDevice(),
                    ScanRecordLega
                            .parseFromBytes(result.getScanRecord().getBytes())));
        }
    };



    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            LogManager.d(TAG,"onScanResult " + device.getAddress());

            mScanResult.onDeviceFound(new BluetoothDeviceWrapp(device,
                    ScanRecordLega.parseFromBytes(scanRecord)));
        }
    };



    private Runnable stopScanningHandler = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            LogManager.d(TAG,"StopScan");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                mBluetoothLECore.getBluetoothAdapter().stopLeScan(leScanCallback);
            }else{
                mBluetoothScanner.stopScan(scanCallbackApi21);
            }
            mScanResult.onScanFinish();
            /*mBluetoothLECore.disableAdapter(new BluetoothLECore.BluetoothState() {
                @Override
                public void onBluetoothChangeState(boolean enabled) {

                }
            });*/
        }
    };

    public interface ScanResult{
        void onDeviceFound(BluetoothDeviceWrapp bluetoothDeviceWrapper);
        void onScanFinish();

        void onError(int Error);
    }
}
