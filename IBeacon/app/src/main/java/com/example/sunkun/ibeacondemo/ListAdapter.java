package com.example.sunkun.ibeacondemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkun on 2015/9/10.
 */
public class ListAdapter extends BaseAdapter {
    private List<IBeacon> mData = new ArrayList<IBeacon>();
    private LayoutInflater mInflater;

    public ListAdapter() {
    }

    public void add(IBeacon iBeacon) {
        if (iBeacon == null)
            return;

        for (int i = 0; i < mData.size(); i++) {
            String btAddress = mData.get(i).bluetoothAddress;
            if (btAddress.equals(iBeacon.bluetoothAddress)) {
                mData.add(i + 1, iBeacon);
                mData.remove(i);
                return;
            }
        }
        mData.add(iBeacon);
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
        IBeacon iBeacon = mData.get(position);
        if (iBeacon == null)
            return convertView;
        holder.name.setText("name:" + iBeacon.getName());
        holder.proximityUuid.setText("proximityUuid:" + iBeacon.getProximityUuid());
        holder.bluetoothAddress.setText("bluetoothAddress" + iBeacon.getBluetoothAddress());
        holder.major.setText("major:" + iBeacon.getMajor());
        holder.minor.setText("minor:" + iBeacon.getMinor());
        holder.txPower.setText("txPower:" + iBeacon.getTxPower());
        holder.rssi.setText("rssi:" + iBeacon.getRssi());
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
