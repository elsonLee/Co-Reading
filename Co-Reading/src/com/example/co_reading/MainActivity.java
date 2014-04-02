//Copyright (C) 2014  ElsonLee & WenPin Cui
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.
package com.example.co_reading;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.co_reading.connection.ITransceiverOps;
import com.example.co_reading.connection.bluetooth.BlueToothManager;
import com.example.co_reading.painting.Brush;
import com.example.co_reading.painting.ColorPickerDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private final String TAG = MainActivity.class.getSimpleName();

	private RetainedFragment mRetainedFragment = null;

	private OnRestoreData mRestoreData = null;

	private boolean mDrawMode = false;

	private Uri mUri;

	static {
		System.loadLibrary("gen_pipe");
	}

	// first: read, second: write
	public native FileDescriptor[] createpipe();

	public native void closefd(FileDescriptor fdesc);

	private ITransceiverOps mTransceiverManager = null;

	public class OnRestoreData {
		List<ActionBar.Tab> m_tabList = new ArrayList<ActionBar.Tab>();
		int m_curTabPos;

		void addToTabList(ActionBar.Tab tab) {
			m_tabList.add(tab);
		}

		void restoreNavigationTab(ActionBar actionBar) {

			if (m_tabList.isEmpty() == false) {
				for (ActionBar.Tab mtab : m_tabList) {
					actionBar.addTab(mtab);
				}
			}

			actionBar.setSelectedNavigationItem(m_curTabPos);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		Log.i(TAG, "MainActivity create");

		SlidingMenu menu = new SlidingMenu(this);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setFadeDegree(0.35f);
		menu.setFadeEnabled(true);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setMenu(R.layout.slide_menu);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		FragmentManager fm = getFragmentManager();
		mRetainedFragment = (RetainedFragment) fm
				.findFragmentByTag("restoreData");

		if (mRetainedFragment == null) {
			mRetainedFragment = new RetainedFragment();
			fm.beginTransaction().add(mRetainedFragment, "restoreData")
					.commit();
			if (mRestoreData == null)
				mRestoreData = new OnRestoreData();
			mRetainedFragment.setData(mRestoreData);
		} else {
			mRestoreData = mRetainedFragment.getData();
			ActionBar mactionBar = getActionBar();
			mRestoreData.restoreNavigationTab(mactionBar);
		}

		if (savedInstanceState != null)
			mUri = savedInstanceState.getParcelable("uri"); 
		if (mUri == null) {
			mUri = getIntent().getData();
			setIntent(null);
			addTab();
		}

		/* test for network */
		/*
		 * FileDescriptor[] fd1 = createpipe(); FileDescriptor[] fd2 =
		 * createpipe();
		 * 
		 * Log.d(TAG, "file: "+fd1[0]+" "+fd1[1]); Log.d(TAG,
		 * "file: "+fd2[0]+" "+fd2[1]);
		 * 
		 * FileInputStream fileReadStream1 = new FileInputStream(fd1[0]);
		 * FileOutputStream fileWriteStream1 = new FileOutputStream(fd1[1]);
		 * FileInputStream fileReadStream2 = new FileInputStream(fd2[0]);
		 * FileOutputStream fileWriteStream2 = new FileOutputStream(fd2[1]);
		 * 
		 * Client client = null; Server server = null; try { client = new
		 * PipeClient(); client.Initialize(fileReadStream1, fileWriteStream2);
		 * client.addListener(new INetworkListener() {
		 * 
		 * @Override public void onNetworkConnected() { // TODO Auto-generated
		 * method stub
		 * 
		 * }
		 * 
		 * @Override public void onNetworkDisconnected() { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onNetworkReceivedObj(Object object) { if
		 * (object instanceof Packet) { Packet pack = (Packet) object;
		 * Log.d(TAG, "Received obj: mId="+ pack.mId); } } });
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 * 
		 * try { server = new PipeServer(); server.Initialize(fileReadStream2,
		 * fileWriteStream1); } catch (IOException e) { e.printStackTrace(); }
		 * 
		 * client.start(); server.start();
		 * 
		 * server.send(new Packet(2)); client.send(new Packet(1));
		 * server.send(new Packet(20)); client.send(new Packet(10));
		 * client.send(new Packet(100));
		 */

		/*
		 * closefd(fd1[0]); closefd(fd1[1]); closefd(fd2[0]); closefd(fd2[1]);
		 */
	}

	@Override
	protected void onDestroy() {
		ActionBar actionBar = getActionBar();
		mRestoreData.m_curTabPos = actionBar.getSelectedNavigationIndex();
		mRetainedFragment.setData(mRestoreData);

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_actionbar_menu, menu);

		super.onCreateOptionsMenu(menu);

		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("uri", mUri);
	}
	

	protected void addTab() {
		ActionBar.Tab newTab = null;
		ActionBar actionBar = getActionBar();

		PdfFragment fragment = new PdfFragment();

		if (mUri != null) {
			Bundle bd = new Bundle();
			bd.putString("uri", mUri.getPath());
			fragment.setArguments(bd);
		}
		fragment.setDrawMode(mDrawMode);

		newTab = actionBar.newTab().setText("newTab");
		newTab.setTabListener(new TabListener(fragment));
		newTab.setTag(fragment);

		actionBar.addTab(newTab);
		mRestoreData.addToTabList(newTab);

		if (actionBar.getNavigationItemCount() > 1)
			actionBar.setSelectedNavigationItem(actionBar
					.getNavigationItemCount() - 1);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Toast.makeText(this, "Selected Item: " + item.getTitle(),
		// Toast.LENGTH_SHORT).show();

		switch (item.getItemId()) {

		case R.id.action_addtab:
			addTab();
			return true;

		case R.id.action_painter:
			ActionBar ab = getActionBar();
			mDrawMode = !mDrawMode;
			String msg = mDrawMode ? "Draw Mode" : "Read  Mode";
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			for (int index = 0; index < ab.getTabCount(); index++) {
				PdfFragment frag = (PdfFragment) (ab.getTabAt(index).getTag());
				frag.setDrawMode(mDrawMode);
			}
			return true;

		case R.id.action_search:
			// wenpin: don't add code here, it's an action view
			return true;

		case R.id.action_settings:
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onSlideMenuClick(View v) {
		Paint pt = Brush.getPaint();
		Brush brush = Brush.getInstance();

		pt.setXfermode(null);
		pt.setAlpha(0xFF);
		switch (v.getId()) {
		case R.id.pick_color:
			new ColorPickerDialog(this, brush, pt.getColor()).show();
			break;
		case R.id.emboss:
			MaskFilter embossFilter = Brush.getEmbossFilter();
			if (pt.getMaskFilter() != embossFilter)
				pt.setMaskFilter(embossFilter);
			else
				pt.setMaskFilter(null);
			break;
		case R.id.blur:
			MaskFilter blurFilter = Brush.getBlurFilter();
			if (pt.getMaskFilter() != blurFilter)
				pt.setMaskFilter(blurFilter);
			else
				pt.setMaskFilter(null);
			break;
		case R.id.erase:
			pt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			break;
		case R.id.srcatop:
			pt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			pt.setAlpha(0x80);
			break;
		default:
			break;
		}

		return;
	}

	public void onSort(MenuItem item) {
		// invalidateOptionsMenu();

		switch (item.getItemId()) {

		/** Bluetooth connect */
		case R.id.bt_connect:
			mTransceiverManager = BlueToothManager.getInstance();

			// Intent intent = new
			// Intent(BluetoothDiscoveryDialog.ACTION_BTDIALOG);
			Intent intent = new Intent(
					this,
					com.example.co_reading.connection.bluetooth.ui.BtDialogFragContainer.class);
			if (intent.resolveActivity(getPackageManager()) == null) {
				Log.e(TAG, "has no corresponding intent");
				return;
			}
			startActivity(intent);

			break;

		default:
			break;
		}
	}

	public void onChangeDrawMode(View view) {
		boolean on = ((Switch) view).isChecked();
		ActionBar ab = getActionBar();
		mDrawMode = on;
		Log.i(TAG, on ? "Draw Mode" : "Read  Mode");
		for (int index = 0; index < ab.getTabCount(); index++) {
			PdfFragment frag = (PdfFragment) (ab.getTabAt(index).getTag());
			frag.setDrawMode(on);
		}
	}
}
