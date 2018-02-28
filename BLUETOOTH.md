Esta es una capa de abstracción más en la librería de Bluettoth 4.0 en android ayudando a encapsular los servicios para que sepamos de una manera mas humana los datos que son devueltos y de que servicio se trata.

Para crear una instancia tenemos que crear una clase que herede de BluetoothManagerBase

Zona en Contruccion

```java
public class ExampleBluetoothManager extends BluetoothManagerBase {
    /***
     * This functions return the devices is being discovered for the bluetooth device

     * @param bluetoothDevices devices discovered
     */
    @Override
    protected void promtBluetoothDevice(Map<String, BluetoothDeviceWrapp> bluetoothDevices) {}
    /**
     * When the Gatt services are discovered and the library assign the services with the config this function will be call
     * Contains a Map of {@link BluetoothServiceWrapper}
     *
     * @param characteristics
     */
    @Override
    protected void gattProcessed(Map<String, BluetoothServiceWrapper> characteristics) {}
    /**
     * When the bluetooth client response the write request
     *
     * @param bluetoothCharacteristicWrapper {@link BluetoothCharacteristicWrapper}
     * @param status                         @See <a href="http://allmydroids.blogspot.com.es/2015/06/android-ble-error-status-codes-explained.html">BLE Codes</a>
     */
    @Override
    protected void characteristicWritten(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper, int status) {}
    /**
     * When you are subscriber to a characteristics this function is call when the client send a changes of the characteristic value
     *
     * @param bluetoothCharacteristicWrapper {@link BluetoothCharacteristicWrapper}
     */
    @Override
    protected void characteristicChanged(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper) {}
    /**
     * When the bluetooth client response the read request
     *
     * @param bluetoothCharacteristicWrapper {@link BluetoothCharacteristicWrapper}
     * @param status                         @See <a href="http://allmydroids.blogspot.com.es/2015/06/android-ble-error-status-codes-explained.html">BLE Codes</a>
     */
    @Override
    protected void characteristicRead(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper, int status) {}
    /**
     * When the bluetooth client response the write request in a characteristic desciptor
     *
     * @param bluetoothCharacteristicWrapper {@link BluetoothCharacteristicWrapper}
     * @param status                         @See <a href="http://allmydroids.blogspot.com.es/2015/06/android-ble-error-status-codes-explained.html">BLE Codes</a>
     */
    @Override
    protected void descriptorWritten(BluetoothCharacteristicWrapper bluetoothCharacteristicWrapper, int status) {}
    /**
     * This function is being called when the client change the status of connection
     */
    @Override
    protected void deviceStatusChange() {}
    /***
     * This function is being call when the bluetooth change for enable to disable
     */
    @Override
    public void onBluetoothDisable() {}
}
```
