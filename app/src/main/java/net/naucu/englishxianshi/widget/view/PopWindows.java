package net.naucu.englishxianshi.widget.view;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.myTool.tool.DensityTool;
import com.app.myTool.tool.DisplayMetricsTool;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.adapter.HomeBooksCategoryAdapter;
import net.naucu.englishxianshi.ui.BooksClassificationActivity;

import java.util.List;

/**
*
* 类名: PopWindows.java
* 描述: 自定义PopWindows
* 作者: youyou_pc
* 时间: 2015年11月18日  下午1:31:44
*
 */
public class PopWindows implements OnClickListener {
	private PopupWindow popupWindow;
	private Context context;
	private View view;
	private int height;
	private int width;
	private int resid;
	private GridView gv_category;
	private LinearLayout li_view1,li_view2;
	private TextView photo_down;
	private TextView open_camera;
	private TextView open_album;
	private HeadPortrait headPortrait;
	private List<String> list;
	private int tab;
	private QueryLevel levels;
	public PopWindows(Context context) {
		this.context = context;
		
	}
	public void showPopupWindow(View v,int gravity,boolean d) {
		LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mLayoutInflater.inflate(R.layout.layout_popwindows, null);
		if(d){
			popupWindow = new PopupWindow(view,width,LayoutParams.MATCH_PARENT);
		}else {
			popupWindow = new PopupWindow(view,width,height);
		}
		ColorDrawable dw = new ColorDrawable(0xcc000000);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setOutsideTouchable(true);
		if(d){
			popupWindow.showAtLocation(v,gravity,0, 0);
		}else{
			popupWindow.showAsDropDown(v);
		}
		initview();
	}
	private void initview() {
		gv_category=(GridView) view.findViewById(R.id.gv_category);
		li_view2=(LinearLayout) view.findViewById(R.id.li_view2);
		li_view1=(LinearLayout) view.findViewById(R.id.li_view1);
		photo_down=(TextView) view.findViewById(R.id.photo_down);
		open_camera=(TextView) view.findViewById(R.id.open_camera);
		open_album=(TextView) view.findViewById(R.id.open_album);
		photo_down.setOnClickListener(this);
		open_camera.setOnClickListener(this);
		open_album.setOnClickListener(this);
		gv_category.setOnItemClickListener(itemClickListener);
	}
	@SuppressWarnings("static-access")
	public void setLayoutParams(double height,int width){
		DisplayMetricsTool displayMetricsTool=new DisplayMetricsTool((Activity) context);
		this.width=displayMetricsTool.getWidth()/width;
		this.height= (int) (displayMetricsTool.getHeigh()/height+13);
	}

	public void setLayoutParams(int height,int width){
		DisplayMetricsTool displayMetricsTool=new DisplayMetricsTool((Activity) context);
		this.width=displayMetricsTool.getWidth()/width;
		this.height=displayMetricsTool.getHeigh()/height+13;
	}

	public void setLayoutParams1(int height,int width){
		DisplayMetricsTool displayMetricsTool=new DisplayMetricsTool((Activity) context);
		this.width=displayMetricsTool.getWidth()/width;
		this.height=displayMetricsTool.getHeigh()/2-280;
	}
	public void setview(int viewid){
		switch (viewid) {
		case 0:
			li_view1.setVisibility(View.VISIBLE);
			li_view1.setPadding(DensityTool.dip2px(context,10),DensityTool.dip2px(context,10),DensityTool.dip2px(context,10),DensityTool.dip2px(context,10));
			break;
		case 1:
			li_view1.setVisibility(View.VISIBLE);
			li_view1.setPadding(0,DensityTool.dip2px(context,10),0,DensityTool.dip2px(context,10));
			gv_category.setNumColumns(1);
			break;
		case 2:
			li_view2.setVisibility(View.VISIBLE);
			view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = li_view2.getTop();
					int y=(int) event.getY();
					if(event.getAction()==MotionEvent.ACTION_UP){
						if(y<height){
							popupWindow.dismiss();
						}
					}
					return true;
				}
			});
			break;
		}
	}
	public void sethomeCategoryList(List<String> list,int tab){
		gv_category.setAdapter(new HomeBooksCategoryAdapter(context, list));
		this.list=list;
		this.tab=tab;
	}
	private OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(tab==1){
				context.startActivity(new Intent(context, BooksClassificationActivity.class).putExtra("TwoCategory",list.get(position).toString()));
			}else if(tab==2){
				levels.level(parent, view, position, id);
			}
			popupWindow.dismiss();
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo_down:
			popupWindow.dismiss();
			break;
		case R.id.open_camera:
			popupWindow.dismiss();
			headPortrait.openCamera();
			break;
		case R.id.open_album:
			popupWindow.dismiss();
			headPortrait.openSlbum();
			break;
		}
	}
	public interface QueryLevel{
		void level(AdapterView<?> parent, View view, int position, long id);
	}
	public void onQueryLevel(QueryLevel queryLevel){
		this.levels=queryLevel;
		
	}
	public interface HeadPortrait{
		void openCamera();
		void openSlbum();
	}
	public void onHeadPortrait(HeadPortrait headPortrait){
		this.headPortrait=headPortrait;
	}
}
