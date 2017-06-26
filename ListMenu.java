package com.in.sight.android;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListMenu extends Activity {
	/** Called when the activity is first created. */

	public static String[] array;
	public static int cur_y = 0, pre_y = 0, diff_y = 0;
	public static int temp, rowid = 0,top;
	public static ListView lview ;
	public static int row_size,scrolling;
	public static List<ResolveInfo> appList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_list);
		Log.i("in", "list menu");
		Preview.list_started = true;
		PackageManager pm = this.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.addCategory(Intent.CATEGORY_DEFAULT);

		lview = (ListView) findViewById(R.id.android_R_id_list);
		lview.setFocusable(true);
		appList = pm.queryIntentActivities(mainIntent, 0);
		Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));

		array = new String[appList.size()];

		for (int i = 0; i < appList.size(); i++) {
			array[i] = appList.get(i).loadLabel(pm).toString();

		}
		Log.i("rows",appList.size()+"" );
		lview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array));
		row_size=90;
		lview.setFocusable(true);
		lview.setFocusableInTouchMode(true);
		lview.requestFocus();
		lview.setKeepScreenOn(true);
		//lview.setFastScrollEnabled(true);
		lview.setSmoothScrollbarEnabled(true);
		lview.setSelector(R.drawable.focused);
		lview.setSelection(0);
		
	}
		public static void change(){
		
		cur_y = (int) (Preview.y);
		//lview.requestFocus();
		
		if (pre_y != cur_y) {
			diff_y = cur_y - pre_y;
			if (temp != diff_y)
				Log.i("diff", diff_y + "");
			scrolling=diff_y*row_size*appList.size()/320;
			if (diff_y>0){
			
				rowid = rowid + Math.abs(scrolling/row_size)+1;
				if(rowid>=appList.size()-1)rowid=appList.size()-1;
			}
			else if(diff_y<0) {
				
				rowid = rowid -Math.abs(scrolling/row_size)-1;
				if(rowid<0)rowid=0;
			}
			
			int position=lview.getFirstVisiblePosition();
			Log.i("scroll row view", scrolling+" "+rowid+" "+position);
			lview.requestFocusFromTouch();
			//lview.scrollBy(0, scrolling);
			
			lview.setSelection(rowid);
			lview.scrollTo(0, scrolling);
			Log.i("row",""+lview.pointToRowId(0, pre_y+scrolling));
			//Log.i("blink"," "+Preview.blink_stage);
			Log.i("class",lview.getSelectedItem()+"");
			/*if(Preview.blink_stage==3){
				Log.i("blink",lview.getSelectedItem().getClass().toString());
			}*/
			//lview.setItemChecked(rowid, true);
			pre_y = cur_y;
			temp = diff_y;
		}
		else{
			cur_y=0;
		}
		
		
		}
		public boolean onKeyDown(int arg0, KeyEvent arg1) {
			// TODO Auto-generated method stub
			System.exit(0);
			return true;
		}
}