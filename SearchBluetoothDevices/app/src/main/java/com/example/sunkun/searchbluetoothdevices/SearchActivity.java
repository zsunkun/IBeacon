package com.example.sunkun.searchbluetoothdevices;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends Activity implements View.OnClickListener {

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Receive broadcast" + intent.getAction());
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                mFoundDevice = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str;
                if (mFoundDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    str = "未配对|" + mFoundDevice.getName() + "|"
                            + mFoundDevice.getAddress();
                } else {
                    str = "已配对|" + mFoundDevice.getName() + "|"
                            + mFoundDevice.getAddress();
                }
                if (mListDevices.indexOf(str) == -1)// 防止重复添加
                    mListDevices.add(str);
                mListAdapter.notifyDataSetChanged();

            }
        }
    };

    private static final String TAG = "SearchActivity";
    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView mDevicesListView;
    List<String> mListDevices = new ArrayList<String>();
    private BaseAdapter mListAdapter;
    private BluetoothDevice mFoundDevice;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initBluetooth();
        initListView();
        registerReceiver();
        mSearchButton = (Button) findViewById(R.id.button_search);
        mSearchButton.setOnClickListener(this);
    }

    private void registerReceiver() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver, intent);
    }

    private void initListView() {
        mDevicesListView = (ListView) this.findViewById(R.id.listview_devices);
        mListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListDevices);
        mDevicesListView.setAdapter(mListAdapter);
    }

    private void initBluetooth() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "蓝牙设备不支持", Toast.LENGTH_LONG).show();
            return;
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙设备不支持", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(this, "蓝牙已经开启，可以搜索设备了", Toast.LENGTH_SHORT).show();
            setTitle("本机蓝牙地址：" + mBluetoothAdapter.getAddress());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启，可以搜索设备了", Toast.LENGTH_SHORT).show();
                setTitle("本机蓝牙地址：" + mBluetoothAdapter.getAddress());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_search) {
            mListDevices.clear();
            mListAdapter.notifyDataSetChanged();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                return;
            }
            if (mBluetoothAdapter.isDiscovering())
                mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
