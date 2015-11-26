package com.example.sunkun.ibeacondemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class IBeaconActivity extends Activity implements AdapterView.OnItemClickListener {

    private String TAG = "IBeaconActivity";
    private ListView mBeaconList;
    private BluetoothAdapter mBluetoothAdapter;
    private ListAdapter mListAdapter;
    private BluetoothDeviceListAdapter mBluetoothDeviceListAdapter;
    private boolean mBluetoothState = true;//record bluetooth state when launch app
    private IntentFilter mIntentFilter;
    private long mStartTime;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Receive broadcast"+intent.getAction()+":"+mBluetoothAdapter.isEnabled());
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (mBluetoothAdapter.isEnabled()) {
                    Log.i(TAG, "time:" + String.valueOf(System.currentTimeMillis() - mStartTime));
                    //TODO:Thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            scanDevice();
                        }
                    }).start();
                    mStartTime = 0;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon);
        initListView();
        mIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onResume() {
        if (checkVersion() && checkBluetooth())
            scanDevice();
        super.onResume();
    }

    private void initListView() {
        mBeaconList = (ListView) findViewById(R.id.beacon_list);
        mListAdapter = new ListAdapter();
        mBeaconList.setAdapter(mListAdapter);
//        mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter();
//        mBeaconList.setAdapter(mBluetoothDeviceListAdapter);
        mBeaconList.setOnItemClickListener(this);
    }

    @SuppressLint("NewApi")
    private void scanDevice() {
        boolean startLeScan = mBluetoothAdapter.startLeScan(mLeScanCallback);
        Log.i(TAG, "startSacn" + startLeScan);
    }

    @SuppressLint("NewApi")
    private void stopScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    private boolean checkVersion() {
        if (Build.VERSION.SDK_INT < 18) {
            Toast.makeText(this, "Android版本过低", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    private boolean checkBluetooth() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "蓝牙设备不支持", Toast.LENGTH_LONG).show();
            return false;
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙设备不支持", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.i(TAG, "BT not enabled yet");
            mStartTime = System.currentTimeMillis();
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            mBluetoothState = false;
            mBluetoothAdapter.enable();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mBluetoothAdapter != null && mBluetoothState == false)
            mBluetoothAdapter.disable();
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopScan();
        super.onPause();
    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//                    mBluetoothDeviceListAdapter.add(device);
                    Log.i(TAG, "sunkun:" + device.getName());
                    final IBeacon ibeacon = ScanResultAnalysis.formatScanData(device, rssi, scanRecord);
                    if (ibeacon == null)
                        return;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListAdapter.add(ibeacon);
                        }
                    });
                }
            };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick");
        if (mListAdapter == null || mBluetoothAdapter == null)
            return;
        IBeacon iBeacon = (IBeacon) mListAdapter.getItem(position);
        if (iBeacon == null)
            return;
        mBluetoothAdapter.getRemoteDevice(iBeacon.getBluetoothAddress());
        //TODO
    }
}