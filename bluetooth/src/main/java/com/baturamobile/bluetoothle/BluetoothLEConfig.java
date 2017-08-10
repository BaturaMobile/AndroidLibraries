package com.baturamobile.bluetoothle;

/**
 * Created by unai on 19/08/2016.
 */

public class BluetoothLEConfig {

    public static final int SCAN_PERIOD_IN_MS = 5000;
    public static final int PROCESS_CHARACTERISTICS_TIMEOUT = 800;

    public static final String GENERIC_ACCESS_SERVICE = "00001800-0000-1000-8000-00805f9b34fb";
    public static final String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";

    public static final String DEVICE_NAME_CHARACTERISTICS = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final String APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb";

    public static final String MANUFACTURER_NAME = "00002a29-0000-1000-8000-00805f9b34fb";
    public static final String MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
    public static final String SERIAL_NUMBER_STRING= "00002a25-0000-1000-8000-00805f9b34fb";
    public static final String HARDWARE_REVISION = "00002a27-0000-1000-8000-00805f9b34fb";
    public static final String FIREWARE_REVISION = "00002a26-0000-1000-8000-00805f9b34fb";
}
