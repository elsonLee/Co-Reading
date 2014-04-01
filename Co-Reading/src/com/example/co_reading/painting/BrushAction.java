package com.example.co_reading.painting;
import com.example.co_reading.R;

import android.content.Context;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class BrushAction extends ActionProvider {
	private Context mContext;
	private final String TAG = "BrushAction";
	public BrushAction(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		Log.i(TAG, "onCreateActionView");

		return null;
	}
	
	@Override  
	public void onPrepareSubMenu(SubMenu subMenu) {  
		subMenu.clear();
		String[] menus = new String[] {
				"color", "emboss", "blur", "srcatop", "erase"			
		};

		for (int i = 0; i < menus.length; i++) {
			String s = menus[i];
			subMenu.add(0, i, 0, s).setIcon(R.drawable.ic_launcher).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {  
			@Override  
			public boolean onMenuItemClick(MenuItem item) {
		        Paint pt = Brush.getPaint();
		        Brush brush = Brush.getInstance();

		        pt.setXfermode(null);
		        pt.setAlpha(0xFF);
		        Log.i(TAG, "on sub menu click, id:" + item.getItemId());
		    	switch (item.getItemId()) {
		    	case 0:
		        	new ColorPickerDialog(mContext, brush, pt.getColor()).show();
		        	break;
		        case 1:
		            MaskFilter embossFilter = Brush.getEmbossFilter();
		            if (pt.getMaskFilter() != embossFilter)
		                pt.setMaskFilter(embossFilter);
		            else
		                pt.setMaskFilter(null);
		            break;
		        case 2:
		            MaskFilter blurFilter = Brush.getBlurFilter();
		            if (pt.getMaskFilter() != blurFilter)
		                pt.setMaskFilter(blurFilter);
		            else
		                pt.setMaskFilter(null);
		            break;
		        case 3:
		            pt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
		            pt.setAlpha(0x80);
		            break;
		        case 4:
		        	pt.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		            break;
		        default:
		            break;
		    	}
				return true;  
			}  
			});
		}  
	}
	
	@Override  
	public boolean hasSubMenu() {  
		return true;  
	}  

}
