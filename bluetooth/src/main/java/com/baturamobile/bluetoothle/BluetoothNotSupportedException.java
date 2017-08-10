package com.baturamobile.bluetoothle;

/**
 * Created by unai on 19/08/2016.
 */

public class BluetoothNotSupportedException extends Exception {

    public BluetoothNotSupportedException(){
        super("Device not supported BluetoothLE");
    }
}
