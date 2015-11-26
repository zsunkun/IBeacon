package com.example.sunkun.ibeacondemo;

/**
 * Created by sunkun on 2015/9/10.
 */
public class IBeacon {
    public String name;
    public String proximityUuid;
    public String bluetoothAddress;
    public int major;
    public int minor;
    public int txPower;
    public int rssi;

//    public void setRssi(int rssi) {
//        this.rssi = rssi;
//    }
//
//    public void setProximityUuid(String proximityUuid) {
//        this.proximityUuid = proximityUuid;
//    }
//
//    public void setBluetoothAddress(String bluetoothAddress) {
//        this.bluetoothAddress = bluetoothAddress;
//    }
//
//    public void setMajor(int major) {
//        this.major = major;
//    }
//
//    public void setMinor(int minor) {
//        this.minor = minor;
//    }
//
//    public void setTxPower(int txPower) {
//        this.txPower = txPower;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getName() {
        return name;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getTxPower() {
        return txPower;
    }

    public int getRssi() {
        return rssi;
    }
}
