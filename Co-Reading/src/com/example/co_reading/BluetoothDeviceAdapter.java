package com.example.co_reading;

import java.util.Collections;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothDeviceAdapter extends BaseAdapter {
	
	private String TAG = BluetoothDeviceAdapter.class.getSimpleName();
	
	private List<BluetoothDevice> m_btDeviceList = Collections.emptyList();
	
	private final Context m_context;
	
	public BluetoothDeviceAdapter(Context context) {
		m_context = context;
	}
	
	public void updateBtDevices(List<BluetoothDevice> btDeviceList) {
		m_btDeviceList = btDeviceList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return m_btDeviceList.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		return m_btDeviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class ViewHolder {
		public final ImageView m_btDeviceImageView;
		public final TextView m_btDeviceNameView;
		public final TextView m_btDeviceAddressView;
		public final TextView m_btDeivceStatusView;
		
		public ViewHolder(ImageView imageView, TextView nameView, TextView addrView, TextView statusView) {
			m_btDeviceImageView = imageView;
			m_btDeviceNameView = nameView;
			m_btDeviceAddressView = addrView;
			m_btDeivceStatusView = statusView;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView btDeviceImageView;
		TextView btDeviceNameView;
		TextView btDeviceAddressView;
		TextView btDeviceStatusView;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(m_context)
					.inflate(R.layout.btdevice_list, parent, false);

			btDeviceImageView = (ImageView)convertView.findViewById(R.id.btDevice_image);
			btDeviceNameView = (TextView)convertView.findViewById(R.id.btDevice_name);
			btDeviceAddressView = (TextView)convertView.findViewById(R.id.btDevice_address);
			btDeviceStatusView = (TextView)convertView.findViewById(R.id.btDevice_status);
			
			convertView.setTag(new ViewHolder(btDeviceImageView, btDeviceNameView, btDeviceAddressView, btDeviceStatusView));
		} else {
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			btDeviceImageView = viewHolder.m_btDeviceImageView;
			btDeviceNameView = viewHolder.m_btDeviceNameView;
			btDeviceAddressView = viewHolder.m_btDeviceAddressView;
			btDeviceStatusView = viewHolder.m_btDeivceStatusView; 
		}
		
		BluetoothDevice btDevice = getItem(position);
		// btDeviceImageView.setImageResource(resId);
		btDeviceNameView.setText(btDevice.getName());
		btDeviceAddressView.setText(btDevice.getAddress());
		
		String mStatus = "";
		switch (btDevice.getBondState()) {
		case BluetoothDevice.BOND_BONDED:
			mStatus = "Paired";
			break;
		case BluetoothDevice.BOND_BONDING:
			mStatus = "Pairing...";
			break;
		}

		btDeviceStatusView.setText(mStatus);

		return convertView;
	}
}
