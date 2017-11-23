package com.baturamobile.bluetoothle;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.baturamobile.bluetoothle.wrapers.BluetoothCharacteristicWrapper;
import com.baturamobile.bluetoothle.wrapers.BluetoothDeviceWrapp;
import com.baturamobile.bluetoothle.wrapers.BluetoothServiceWrapper;
import com.baturamobile.utils.LogStatic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.baturamobile.bluetoothle.BluetoothLEConfig.APPEARANCE;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.DEVICE_NAME_CHARACTERISTICS;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.FIREWARE_REVISION;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.HARDWARE_REVISION;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.MANUFACTURER_NAME;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.MODEL_NUMBER_STRING;
import static com.baturamobile.bluetoothle.BluetoothLEConfig.SERIAL_NUMBER_STRING;


/**
 * Created by unai on 22/08/2016.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BluetoothManagerBase implements BluetoothLEDiscovery.ScanResult,
        BluetoothLEGatt.GattStatusInterface{

    private static final String TAG = BluetoothManagerBase.class.getSimpleName();

    protected Map<String,String> servicesUUIDToSearch;
    protected Map<String,BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead;
    protected String uuidToFilter;
    protected Context appContext;

    protected BluetoothLEDiscovery bluetoothLEDiscovery;
    protected BluetoothLECore bluetoothLECore;

    protected BluetoothLEGatt bluetoothGatt;

    protected Map<String,BluetoothDeviceWrapp> bluetoothScannedDevices;

    private BluetoothGattWrapperFactory bluetoothGattWrapperFactory;

    private BluetoothDeviceWrapp bluetoothDeviceWrapper;



    private Map<String, BluetoothServiceWrapper> mServicesProcessed;

    private boolean initialized = false;

    protected BluetoothManagerBase(){
    }

    protected void setUUIDToSearch(Map<String, BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead, Map<String, String> servicesUUIDToSearch) {
        this.servicesUUIDToSearch = servicesUUIDToSearch;
        this.characteristicsToRead = characteristicsToRead;
        bluetoothGattWrapperFactory
                = new BluetoothGattWrapperFactory(servicesUUIDToSearch,characteristicsToRead);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void initialize() throws BluetoothNotSupportedException {
        bluetoothLECore = new BluetoothLECore(appContext);
        bluetoothLEDiscovery = new BluetoothLEDiscovery(bluetoothLECore);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void discovery(){
        UUID[] uuids = new UUID[]{UUID.fromString(uuidToFilter)};
        bluetoothScannedDevices = new HashMap<>();
        bluetoothLEDiscovery.scanBleDevices(uuids,this);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopDiscovery(){
        bluetoothLEDiscovery.stopScan();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onDeviceFound(BluetoothDeviceWrapp bluetoothDeviceWrapper) {
       if (!bluetoothScannedDevices.containsKey(bluetoothDeviceWrapper.getAddress())){
           bluetoothScannedDevices.put(bluetoothDeviceWrapper.getAddress(),bluetoothDeviceWrapper);
       }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void promtBluetoothDevice(Map<String,BluetoothDeviceWrapp> bluetoothDevices);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void gattProcessed(Map<String,BluetoothServiceWrapper> characteristics);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void characteristicWritten(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper,int status);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void characteristicChanged(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void characteristicRead(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper,int status);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected abstract void descriptorWritten(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper, int status);

    protected abstract void deviceStatusChange();


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected String getServiceName(String uuid){
        return servicesUUIDToSearch.get(uuid);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer getCharacteristicsProcessed(String uuid){
        return characteristicsToRead.get(uuid);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void connectToDevice(final BluetoothDeviceWrapp bluetoothDeviceWrapper){
        /*if (!bluetoothScannedDevices.containsKey(bluetoothDeviceWrapper.getAddress())){
            throw new IllegalArgumentException("bluetoothDevice is not in the list of scannedDevices");


        }*/
        bluetoothLECore.enableAdapter(new BluetoothLECore.BluetoothState() {
            @Override
            public void onBluetoothChangeState(boolean enabled) {

                if (enabled && bluetoothDeviceWrapper.getAddress() != null){
                    BluetoothManagerBase.this.bluetoothDeviceWrapper = bluetoothDeviceWrapper;
                    BluetoothDevice bluetoothDevice =
                            bluetoothLECore.getBluetoothAdapter().getRemoteDevice(bluetoothDeviceWrapper.getAddress());

                    if (bluetoothDevice != null){
                        if (bluetoothGatt == null){
                            bluetoothGatt = new BluetoothLEGatt(bluetoothLECore,bluetoothDevice);
                        }
                        bluetoothGatt.connectToGatt(BluetoothManagerBase.this);

                    }
                }

            }
        });




    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void disconnectDevice(){
        if (bluetoothGatt != null){
            bluetoothGatt.disconnectFromGatt();
        }
    }


    public void onConnectionStatus(boolean connected){
        if (!connected){
            disconnectDevice();
            setInitialized(false);

        }
        LogStatic.logInterface.d(TAG,"OnConnection Status " + connected);
    }

    public void onServicesDiscovered(List<BluetoothGattService> services){
        LogStatic.logInterface.d(TAG,"onServicesDiscovered Services " + services.size());
        bluetoothGattWrapperFactory.addServices(services);
    }

    public void onServicesProcessed(Map<String, Map<String, BluetoothGattCharacteristic>> resultServices){
        LogStatic.logInterface.d(TAG,"services Processed ");
    }
    @Override
    public void onCharacteristicsProcessed(Map<String, Map<String, BluetoothGattCharacteristic>> characteristics) {
        createGattWithWrapper(characteristics);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        String serviceUUID = characteristic.getService().getUuid().toString();
        String characteristicUUID = characteristic.getUuid().toString();

        BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper =
                findCharacteristicWrapper(serviceUUID,characteristicUUID,characteristic);

        characteristicWritten(bluetoothCharacteristicWrapper,status);

    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
        String serviceUUID = characteristic.getService().getUuid().toString();
        String characteristicUUID = characteristic.getUuid().toString();

        BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper =
                findCharacteristicWrapper(serviceUUID,characteristicUUID,characteristic);

        characteristicRead(bluetoothCharacteristicWrapper,status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        String serviceUUID = characteristic.getService().getUuid().toString();
        String characteristicUUID = characteristic.getUuid().toString();

        BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper =
                findCharacteristicWrapper(serviceUUID,characteristicUUID,characteristic);

        characteristicChanged(bluetoothCharacteristicWrapper);
    }
    public void readCharacteristic(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper){
        bluetoothGatt.readCharacteristic(bluetoothCharacteristicWrapper.getBluetoothGattCharacteristic());
    }

    public void writeCharacteristic(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper){
        bluetoothGatt.writeCharacteristic(bluetoothCharacteristicWrapper.getBluetoothGattCharacteristic());
    }
    public void notifyCharacteristic(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper){
        bluetoothGatt.notify(bluetoothCharacteristicWrapper.getBluetoothGattCharacteristic());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status){
        String serviceUUID = descriptor.getCharacteristic().getService().getUuid().toString();
        String characteristicUUID = descriptor.getCharacteristic().getUuid().toString();

        BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper =
                findCharacteristicWrapper(serviceUUID,characteristicUUID,descriptor.getCharacteristic());

        descriptorWritten(bluetoothCharacteristicWrapper,status);
    }




    public void onProblemDiscoverServices(){
        LogStatic.logInterface.d(TAG,"onProblemProcessServices ");
    }

    public void onDiscoveringServices(){
        LogStatic.logInterface.d(TAG,"onDiscovering Services ");
    }



    public void onProblemProcessServices(){
        LogStatic.logInterface.d(TAG,"onProblemProcessServices ");
    }

    @Override
    public void onScanFinish() {
      promtBluetoothDevice(bluetoothScannedDevices);
    }

    @Override
    public void onError(int Error) {
        LogManager.e(TAG,"onScanFailed " + Error);
    }

    public Map<String, BluetoothServiceWrapper> getServicesProcessed() {
        return mServicesProcessed;
    }

    public abstract void onBluetoothDisable();

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        onBluetoothDisable();

                }
            }
        }
    };


    private void createGattWithWrapper(Map<String,Map<String,BluetoothGattCharacteristic>> characteristics){
        bluetoothGattWrapperFactory.addCharacteristics(characteristics);
        mServicesProcessed = bluetoothGattWrapperFactory.build();
        gattProcessed(getServicesProcessed());
        setInitialized(true);
    }
    private BluetoothCharacteristicWrapper findCharacteristicWrapper(String serviceUUID,
                                           String characteristicUUID,
                                           BluetoothGattCharacteristic bluetoothGattCharacteristic){
        BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper = null;

        if (getServicesProcessed().containsKey(serviceUUID)){
            bluetoothCharacteristicWrapper =
                    getServicesProcessed().get(serviceUUID).getCharacteristicByUUID(characteristicUUID);

            if (bluetoothCharacteristicWrapper != null){
                bluetoothCharacteristicWrapper.setBluetoothGattCharacteristic(bluetoothGattCharacteristic);
            }
        }

        return bluetoothCharacteristicWrapper;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
        deviceStatusChange();
    }

    public BluetoothDeviceWrapp getBluetoothDeviceWrapper() {
        return bluetoothDeviceWrapper;
    }


    public static class BluetoothGattWrapperFactory{

        protected Map<String,String> servicesUUIDToSearch;
        protected Map<String,BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead;

        Map<String,BluetoothServiceWrapper> gattMapWrapper;

        private BluetoothGattWrapperFactory(Map<String,String> servicesUUIDToSearch,
                                            Map<String,BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead){
            gattMapWrapper = new LinkedHashMap<>();
            this.servicesUUIDToSearch = servicesUUIDToSearch;
            this.characteristicsToRead = characteristicsToRead;
        }

        public static BluetoothGattWrapperFactory BUILDER(Map<String,String> servicesUUIDToSearch,
                                                          Map<String,BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead){

            return new BluetoothGattWrapperFactory(servicesUUIDToSearch,characteristicsToRead);
        }

        public void addServices(List<BluetoothGattService> services){
            for (int x = 0;services.size() > x ; x++){
                BluetoothGattService bluettothGattService = services.get(x);
                String serviceName = servicesUUIDToSearch.get(bluettothGattService.getUuid().toString());


                BluetoothServiceWrapper bluetoothServiceWrapper =
                        new BluetoothServiceWrapper(bluettothGattService,serviceName);

                gattMapWrapper.put(bluetoothServiceWrapper.getUuid(),bluetoothServiceWrapper);

            }
        }

        public void addCharacteristics(Map<String,Map<String,BluetoothGattCharacteristic>> characteristics){
            extractService(characteristics);
        }

        private void extractService(Map<String,Map<String,BluetoothGattCharacteristic>> characteristics){
            Set<Map.Entry<String,Map<String,BluetoothGattCharacteristic>>> entrySetService =  characteristics.entrySet();

            Iterator<Map.Entry<String,Map<String,BluetoothGattCharacteristic>>> iteratorService =  entrySetService.iterator();


            while (iteratorService.hasNext()) {

                extractCharacteristic(iteratorService.next());

            }
        }

        private void extractCharacteristic(Map.Entry<String,Map<String,BluetoothGattCharacteristic>> serviceEntry){

            Iterator<Map.Entry<String,BluetoothGattCharacteristic>> characteristicIterator =
                    serviceEntry.getValue().entrySet().iterator();

            while (characteristicIterator.hasNext()){
                Map.Entry<String,BluetoothGattCharacteristic> characteristicEntry = characteristicIterator.next();
                String serviceUUID = characteristicEntry.getValue().getService().getUuid().toString();
                if (gattMapWrapper.containsKey(serviceUUID)){

                    BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer characteristicContainer
                            = characteristicsToRead.get(characteristicEntry.getKey());

                    if (characteristicContainer == null){
                        characteristicContainer =
                                new  BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer(characteristicEntry.getKey(),
                                        BluetoothCharacteristicWrapper.FormatValueType.FORMAT_UNKONWN,null);
                    }
                    BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper =
                            new BluetoothCharacteristicWrapper(characteristicContainer,characteristicEntry.getValue());
                    gattMapWrapper.get(serviceUUID).addCharacteristic(bluetoothCharacteristicWrapper);
                }
            }
        }

        public Map<String,BluetoothServiceWrapper> build(){

            return gattMapWrapper;
        }


    }

    public boolean isInitialized() {
        return initialized;
    }


    public static class BluetoothManagerFactory{

        private String setUUIDServiceToFilter;

        private Map<String,String> servicesUUIDToSearch;

        private Map<String,BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer> characteristicsToRead;


        private BluetoothManagerFactory(){
            servicesUUIDToSearch = new HashMap<>();
            characteristicsToRead = new HashMap<>();

            initDefaultServices();
        }

        private void initDefaultServices(){

            addServiceToDefine(BluetoothLEConfig.GENERIC_ACCESS_SERVICE,"Generic Access Service")
                    .addServiceToDefine(BluetoothLEConfig.DEVICE_INFORMATION_SERVICE,"Device Information Service")
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(DEVICE_NAME_CHARACTERISTICS,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"DeviceName"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper
                            .BluetoothCharacteristicsContainer(APPEARANCE,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_UNKONWN,"Appearance"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(MANUFACTURER_NAME,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"Manufacturer Name"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(MODEL_NUMBER_STRING,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"Model Number String"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(SERIAL_NUMBER_STRING,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"Serial Number String"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(HARDWARE_REVISION,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"Hardware Revision"))
                    .addCharacteristicsToDefine(new BluetoothCharacteristicWrapper.
                            BluetoothCharacteristicsContainer(FIREWARE_REVISION,
                            BluetoothCharacteristicWrapper.FormatValueType.FORMAT_STRING,"Fireware Revision"));
        }

        public static BluetoothManagerFactory BUILDER(){
            return new BluetoothManagerFactory();
        }

        public BluetoothManagerFactory setServiceFilterToSearchDevice(String serviceUUID){
            this.setUUIDServiceToFilter = serviceUUID;
            return this;
        }

        public BluetoothManagerFactory addServiceToDefine(String uuid,String name){
            if (!servicesUUIDToSearch.containsKey(uuid)){
                servicesUUIDToSearch.put(uuid,name);
            }

            return this;
        }

        public BluetoothManagerFactory addCharacteristicsToDefine(BluetoothCharacteristicWrapper.BluetoothCharacteristicsContainer container){
            if (!characteristicsToRead.containsKey(container.getUuid())){
                characteristicsToRead.put(container.getUuid(),container);
            }

            return this;
        }


        public <T extends BluetoothManagerBase>T build(Context appContext, Class<T> classBluetoothManager) throws IllegalAccessException, InstantiationException, BluetoothNotSupportedException {

            if (classBluetoothManager == null)throw new IllegalArgumentException("class to Instantiate is mandatory");
            if (appContext == null)throw new IllegalArgumentException("Context is requeired");


            T bluetoothManager = classBluetoothManager.newInstance();
            bluetoothManager.setUUIDToSearch(characteristicsToRead,servicesUUIDToSearch);
            bluetoothManager.uuidToFilter = setUUIDServiceToFilter;
            bluetoothManager.appContext = appContext;
            bluetoothManager.initialize();
            return bluetoothManager;
        }

        public <T extends BluetoothManagerBase>void  build(Context appContext, T bluetoothManager) throws IllegalAccessException, InstantiationException, BluetoothNotSupportedException{
            if (appContext == null)throw new IllegalArgumentException("Context is requeired");


            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            appContext.registerReceiver(bluetoothManager.broadcastReceiver,filter);
            bluetoothManager.setUUIDToSearch(characteristicsToRead,servicesUUIDToSearch);
            bluetoothManager.uuidToFilter = setUUIDServiceToFilter;
            bluetoothManager.appContext = appContext;
            bluetoothManager.initialize();
        }

    }






}
