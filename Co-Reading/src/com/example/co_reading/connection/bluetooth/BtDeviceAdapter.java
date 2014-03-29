/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	
package com.example.co_reading.connection.bluetooth;

import java.util.Collections;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.co_reading.R;

public class BtDeviceAdapter extends BaseAdapter {
	
	private String TAG = BtDeviceAdapter.class.getSimpleName();
	
	private List<BluetoothDevice> mBtDeviceList = Collections.emptyList();
	
	private final Context mContext;
	
	public BtDeviceAdapter(Context context) {
		mContext = context;
	}
	
	public void updateBtDevices(List<BluetoothDevice> btDeviceList) {
		mBtDeviceList = btDeviceList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mBtDeviceList.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		return mBtDeviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class ViewHolder {
		public final ImageView mBtDeviceImageView;
		public final TextView mBtDeviceNameView;
		public final TextView mBtDeviceAddressView;
		public final TextView mBtDeivceStatusView;
		
		public ViewHolder(ImageView imageView, TextView nameView, TextView addrView, TextView statusView) {
			mBtDeviceImageView = imageView;
			mBtDeviceNameView = nameView;
			mBtDeviceAddressView = addrView;
			mBtDeivceStatusView = statusView;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView btDeviceImageView;
		TextView btDeviceNameView;
		TextView btDeviceAddressView;
		TextView btDeviceStatusView;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.bt_dialog_listview_row, parent, false);

			btDeviceImageView = (ImageView)convertView.findViewById(R.id.btDevice_image);
			btDeviceNameView = (TextView)convertView.findViewById(R.id.btDevice_name);
			btDeviceAddressView = (TextView)convertView.findViewById(R.id.btDevice_address);
			btDeviceStatusView = (TextView)convertView.findViewById(R.id.btDevice_status);
			
			convertView.setTag(new ViewHolder(btDeviceImageView, btDeviceNameView, btDeviceAddressView, btDeviceStatusView));
		} else {
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			btDeviceImageView = viewHolder.mBtDeviceImageView;
			btDeviceNameView = viewHolder.mBtDeviceNameView;
			btDeviceAddressView = viewHolder.mBtDeviceAddressView;
			btDeviceStatusView = viewHolder.mBtDeivceStatusView; 
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