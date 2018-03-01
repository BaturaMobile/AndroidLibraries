package com.baturamobile.bluetoothle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.baturamobile.utils.log.LogStatic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by unai on 19/08/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLEGatt {

    private static final String TAG = BluetoothLEGatt.class.getSimpleName();

    private BluetoothLECore bluetoothLECore;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mBluetoothDevice;

    private GattStatusInterface mBluetoothGattStatus;





    private int mConnectionState = BluetoothProfile.STATE_DISCONNECTED;


    private Map<String,BluetoothGattService> mBluetoothGattServiceMap = new LinkedHashMap<>();
    private Map<String,Map<String,BluetoothGattCharacteristic>> mBluetoothGattCharacteristicsMap = new LinkedHashMap<>();

    private Deque<BluetoothGattCharacteristic> characteristicsStack = new ArrayDeque<>();

    public BluetoothLEGatt(BluetoothLECore bluetoothLECore, BluetoothDevice bluetoothDevice){
        this.bluetoothLECore = bluetoothLECore;
        this.mBluetoothDevice = bluetoothDevice;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void connectToGatt(GattStatusInterface gattStatus){


        LogStatic.logInterface.d(TAG,"connectGatt");
        mBluetoothGatt = mBluetoothDevice.connectGatt(bluetoothLECore.getAppContext(), false, bluetoothGattCallback);

        mBluetoothGatt.connect();

        mBluetoothGattStatus = gattStatus;
    }



    public void disconnectFromGatt(){
        if (mBluetoothGatt != null){
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            mBluetoothGattStatus = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        mBluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void notify(BluetoothGattCharacteristic characteristic){
        mBluetoothGatt.setCharacteristicNotification(characteristic,true);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }


    private BluetoothGattCallback bluetoothGattCallback;

    {
        bluetoothGattCallback = new BluetoothGattCallback() {

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                if (mBluetoothGattStatus != null) {
                    mBluetoothGattStatus.onDescriptorWrite(gatt, descriptor, status);
                }
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);

                mBluetoothGattStatus.onServicesDiscovered(gatt.getServices());
                setServices(gatt);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                if (mBluetoothGattStatus != null) {
                    mBluetoothGattStatus.onCharacteristicRead(gatt, characteristic, status);
                }

            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                setConnectionState(status, newState);


            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (mBluetoothGattStatus != null) {
                    mBluetoothGattStatus.onCharacteristicWrite(gatt, characteristic, status);
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                if (mBluetoothGattStatus != null) {
                    mBluetoothGattStatus.onCharacteristicChanged(gatt, characteristic);
                }
            }
        };
    }

    private void setServices(BluetoothGatt bluetoothGatt){
        mBluetoothGatt = bluetoothGatt;
        if (mBluetoothGattStatus == null){
            LogManager.e(TAG,"on GetService | BluetoothGatt is not initialized or GattStatusInterface not initiated");
            return;
        }

        processServices(mBluetoothGatt.getServices());
    }

    private void setConnectionState(int status, int newState){
        if (mBluetoothGatt == null || mBluetoothGattStatus == null){
            LogManager.e(TAG,"on GetService | BluetoothGatt is not initialized or GattStatusInterface not initiated");

            return;
        }
        LogStatic.logInterface.d(TAG,"OnConnectionStateChange | OldStatus " + status + " | NewStatus " + newState);
        switch (newState){
            case BluetoothProfile.STATE_CONNECTED:
                mBluetoothGattStatus.onConnectionStatus(true);
                mConnectionState = newState;

                discoverServices();

                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                mBluetoothGattStatus.onConnectionStatus(false);
                mConnectionState = newState;
                break;
        }
    }

    private void discoverServices(){
        boolean discoverServices = mBluetoothGatt.discoverServices();
        if (!discoverServices){
            mBluetoothGattStatus.onProblemDiscoverServices();
        }else{
            mBluetoothGattStatus.onDiscoveringServices();
        }
    }

    private void processServices(List<BluetoothGattService> services){
        getBluetoothGattServiceMap().clear();
        getBluetoothGattCharacteristicsMap().clear();

        int servicesSize = services.size();

        for (int x = 0 ; x<servicesSize; x++){

            BluetoothGattService bluetoothGattService = services.get(x);
            //Put a service into the first map
            getBluetoothGattServiceMap().put(bluetoothGattService.getUuid().toString(),bluetoothGattService);

            //Get the characteristics of current service
            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics =
                    bluetoothGattService.getCharacteristics();

            int characteristicsSize = bluetoothGattCharacteristics.size();

            //Create a Map to insert the characteristics of current service
            Map<String,BluetoothGattCharacteristic> bluetoothGattCharacteristicMap = new LinkedHashMap<>();

            for (int y = 0; y <characteristicsSize ; y ++){

                BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattCharacteristics.get(y);

                characteristicsStack.add(bluetoothGattCharacteristic);

                bluetoothGattCharacteristicMap.put(bluetoothGattCharacteristic.getUuid().toString(),bluetoothGattCharacteristic);
            }

            //Put the information of current service into the map
            getBluetoothGattCharacteristicsMap().put(bluetoothGattService.getUuid().toString(),bluetoothGattCharacteristicMap);


        }
        if (mBluetoothGattStatus != null){
            mBluetoothGattStatus.onServicesProcessed(mBluetoothGattCharacteristicsMap);
            mBluetoothGattStatus.onCharacteristicsProcessed(mBluetoothGattCharacteristicsMap);
        }
    }



    public Map<String, BluetoothGattService> getBluetoothGattServiceMap() {
        return mBluetoothGattServiceMap;
    }

    public Map<String, Map<String, BluetoothGattCharacteristic>> getBluetoothGattCharacteristicsMap() {
        return mBluetoothGattCharacteristicsMap;
    }



    public interface GattStatusInterface {
        void onConnectionStatus(boolean connected);

        void onServicesDiscovered(List<BluetoothGattService> services);

        void onCharacteristicsProcessed(Map<String, Map<String, BluetoothGattCharacteristic>> characteristics);

        void onProblemDiscoverServices();

        void onDiscoveringServices();

        void onServicesProcessed(Map<String, Map<String, BluetoothGattCharacteristic>> resultServices);

        void onProblemProcessServices();

        void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

        void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

        void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

        void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);
    }
}
