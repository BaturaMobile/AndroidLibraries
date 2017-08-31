package com.baturamobile.bluetoothle.wrapers;

import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by unai on 22/08/2016.
 */

public class BluetoothServiceWrapper {

    BluetoothGattService bluetoothGattService;

    Map<String, BluetoothCharacteristicWrapper> uuidBluetoothCharacteristicWrapperMap ;

    String serviceName;

    private String uuid;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothServiceWrapper(BluetoothGattService bluetoothGattService,
                                   String serviceName){
        this.bluetoothGattService = bluetoothGattService;
        uuid = bluetoothGattService.getUuid().toString();
        uuidBluetoothCharacteristicWrapperMap = new LinkedHashMap<>();
        this.serviceName = serviceName;

    }


    public BluetoothCharacteristicWrapper getCharacteristicByUUID(UUID uuid){
        return uuidBluetoothCharacteristicWrapperMap.get(uuid.toString());
    }
    public BluetoothCharacteristicWrapper getCharacteristicByUUID(String uuid){
        return uuidBluetoothCharacteristicWrapperMap.get(uuid);
    }

    public void addCharacteristic(BluetoothCharacteristicWrapper characteristicWrapper){
        uuidBluetoothCharacteristicWrapperMap.put(characteristicWrapper.getUUID().toString(),
                characteristicWrapper);
    }


    public String getUuid() {
        return uuid;
    }
}
