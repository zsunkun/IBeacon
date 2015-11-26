package com.example.sunkun.ibeacondemo;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkun on 2015/9/16.
 */
public class BluetoothDeviceListAdapter extends BaseAdapter {
        private List<BluetoothDevice> mData = new ArrayList<BluetoothDevice>();
        private LayoutInflater mInflater;

        public BluetoothDeviceListAdapter() {
        }

        public void add(BluetoothDevice device) {
            if (device == null)
                return;

            for (int i = 0; i < mData.size(); i++) {
                String btAddress = mData.get(i).getAddress();
                if (btAddress.equals(device.getAddress())) {
                    mData.add(i + 1, device);
                    mData.remove(i);
                    return;
                }
            }
            mData.add(device);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mData == null)
                return convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                mInflater = LayoutInflater.from(parent.getContext());
                convertView = mInflater.inflate(R.layout.layout_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.proximityUuid = (TextView) convertView.findViewById(R.id.proximityUuid);
                holder.bluetoothAddress = (TextView) convertView.findViewById(R.id.bluetoothAddress);
                holder.major = (TextView) convertView.findViewById(R.id.major);
                holder.minor = (TextView) convertView.findViewById(R.id.minor);
                holder.txPower = (TextView) convertView.findViewById(R.id.txPower);
                holder.rssi = (TextView) convertView.findViewById(R.id.rssi);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BluetoothDevice device = mData.get(position);
            if (device == null)
                return convertView;
            holder.name.setText("name:" + device.getName());
            holder.bluetoothAddress.setText("bluetoothAddress" + device.getAddress());
            return convertView;
        }

        static class ViewHolder {
            public TextView name;
            public TextView proximityUuid;
            public TextView bluetoothAddress;
            public TextView major;
            public TextView minor;
            public TextView txPower;
            public TextView rssi;
        }
}
