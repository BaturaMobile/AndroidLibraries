package com.baturamobile.bluetoothle.wrapers;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import com.baturamobile.bluetoothle.ScanRecordLega;

import java.io.Serializable;

public class BluetoothDeviceWrapp implements Serializable {
    private String UUID;
    private String addres;

    public BluetoothDeviceWrapp(BluetoothDevice bluetoothDevice, ScanRecordLega scanRecordLegacy) {
        this.addres = bluetoothDevice.getAddress();
        this.UUID = scanRecordLegacy.getServiceUuids().get(0).getUuid().toString();
    }

    public String getAddress() {
        return this.addres;
    }

    public String getUUID() {
        return this.UUID;
    }
}
