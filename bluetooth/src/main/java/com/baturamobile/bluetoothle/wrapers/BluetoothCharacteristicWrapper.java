package com.baturamobile.bluetoothle.wrapers;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

/**
 * Created by unai on 22/08/2016.
 */

public class BluetoothCharacteristicWrapper {

    public String getCharacteristicName() {
        return characteristicName;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return mBluetoothGattCharacteristic;
    }

    public String getCharacteristicUUID(){
        return mBluetoothGattCharacteristic.getUuid().toString();
    }

    public void setBluetoothGattCharacteristic(BluetoothGattCharacteristic mBluetoothGattCharacteristic) {
        this.mBluetoothGattCharacteristic = mBluetoothGattCharacteristic;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public enum FormatValueType{

        FORMAT_UNKONWN(0),
        FORMAT_UINT8(BluetoothGattCharacteristic.FORMAT_SINT8),
        FORMAT_UINT16(BluetoothGattCharacteristic.FORMAT_UINT16),
        FORMAT_UINT32(BluetoothGattCharacteristic.FORMAT_UINT32),
        FORMAT_SINT8(BluetoothGattCharacteristic.FORMAT_SINT8),
        FORMAT_SINT16(BluetoothGattCharacteristic.FORMAT_SINT16),
        FORMAT_SINT32(BluetoothGattCharacteristic.FORMAT_SINT32),
        FORMAT_SFLOAT(BluetoothGattCharacteristic.FORMAT_SFLOAT),
        FORMAT_FLOAT(BluetoothGattCharacteristic.FORMAT_FLOAT),
        FORMAT_STRING(0x200);
        private final int value;

        private FormatValueType(int value) {
            this.value = value;
        }

    }
    FormatValueType formatType;

    private boolean write;
    private boolean read;
    private boolean notify;

    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;

    private String characteristicName;


    public BluetoothCharacteristicWrapper(BluetoothCharacteristicsContainer characteristicsContainer,
                                          BluetoothGattCharacteristic bluetoothGattCharacteristic){
        this.characteristicName = characteristicsContainer.getName();
        this.formatType = characteristicsContainer.getFormatValueType();
        this.setBluetoothGattCharacteristic(bluetoothGattCharacteristic);

    }

    public FormatValueType getType(){
        return formatType;
    }

    public int getIntValue(){
       return getBluetoothGattCharacteristic().getIntValue(formatType.value,0);
    }
    public float getFloatValue(){
        return getBluetoothGattCharacteristic().getFloatValue(formatType.value,0);
    }
    public  String getStringValue(){
        return  getBluetoothGattCharacteristic().getStringValue(0);
    }
    public void setStringValue(String value){
        write = true;
        getBluetoothGattCharacteristic().setValue(value);

    }
    public void setBytesvalue(byte[] bytesvalue){
        getBluetoothGattCharacteristic().setValue(bytesvalue);
    }

    public boolean isWriteable(){
        return (getBluetoothGattCharacteristic().getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    public boolean isReadable(){
        return (getBluetoothGattCharacteristic().getProperties() & (BluetoothGattCharacteristic.PROPERTY_READ)) != 0;
    }

    public boolean isNotificable(){
        return (getBluetoothGattCharacteristic().getProperties() & (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) != 0;
    }

    public UUID getUUID(){
       return getBluetoothGattCharacteristic().getUuid();
    }


    public static class BluetoothCharacteristicsContainer{

        private String uuid;

        private FormatValueType formatValueType;

        private String name;

        public BluetoothCharacteristicsContainer(String uuid,FormatValueType formatValueType,String name){
            this.uuid = uuid;
            this.formatValueType = formatValueType;
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public FormatValueType getFormatValueType() {
            return formatValueType;
        }

        public String getName() {
            return name;
        }
    }
}
