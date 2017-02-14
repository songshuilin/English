package net.naucu.englishxianshi.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.app.myTool.ActionTitleBarWidget;
import com.app.myTool.ActionTitleBarWidget.ClickListener;

import net.naucu.englishxianshi.R;
import net.naucu.englishxianshi.ui.base.BaseActivity;
import net.naucu.englishxianshi.util.SharedPreTool;
/**
 * 
* 类名: ReadingsetActivity.java
* 描述: TODO 正音朗读
* 作者: youyou_pc
* 时间: 2015年12月1日  下午3:28:57
*
 */
public class ReadingsetActivity extends BaseActivity {
	private ActionTitleBarWidget titlebar;//初始化标题控
	private RadioGroup rg_eadlanguage;
	private RadioButton rb_eadlanguage_slow;
	private RadioButton rb_eadlanguage_the;
	private RadioButton rb_eadlanguage_fast;
	
	private RadioGroup rg_readcharacters;
	private RadioButton rb_readcharacters_male;
	private RadioButton rb_readcharacters_female;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readingset);
		initView();
		initTitleBar();
		initEvent();
		initDate();
	}
	private void initEvent() {
		titlebar.OnTitleBarClickListener(clickListener);
	}
	private void initView() {
		titlebar=(ActionTitleBarWidget) findViewById(R.id.titlebar);
		rg_eadlanguage=(RadioGroup) findViewById(R.id.rg_eadlanguage);//朗读语度
		rb_eadlanguage_slow=(RadioButton) findViewById(R.id.rb_eadlanguage_slow);
		rb_eadlanguage_the=(RadioButton) findViewById(R.id.rb_eadlanguage_the);
		rb_eadlanguage_fast=(RadioButton) findViewById(R.id.rb_eadlanguage_fast);
		rg_readcharacters=(RadioGroup) findViewById(R.id.rg_readcharacters);//朗读角色
		rb_readcharacters_male=(RadioButton) findViewById(R.id.rb_readcharacters_male);
		rb_readcharacters_female=(RadioButton) findViewById(R.id.rb_readcharacters_female);
	}
	private void initDate() {
		switch (SharedPreTool.getSharedPreDateInt(ReadingsetActivity.this,"EadLanguageShared",50)) {
		case 0:
			rb_eadlanguage_slow.setChecked(true);
			break;
		case 50:
			rb_eadlanguage_the.setChecked(true);
			break;
		case 100:
			rb_eadlanguage_fast.setChecked(true);
			break;
		default:
			rb_eadlanguage_the.setChecked(true);
			break;
		}
		
		String temp=SharedPreTool.getSharedPreDateString(ReadingsetActivity.this,"ReadCharactersShared","henry");
		if(temp.equals("Vimary")){
			rb_readcharacters_female.setChecked(true);
		}else if(temp.equals("henry")){
			rb_readcharacters_male.setChecked(true);
		}else{
			rb_readcharacters_female.setChecked(true);
		}
		rg_eadlanguage.setOnCheckedChangeListener(checkedChangeListener);
		rg_readcharacters.setOnCheckedChangeListener(checkedChangeListener);
	}
	private OnCheckedChangeListener checkedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getId()) {
			case R.id.rg_eadlanguage:
				switch (checkedId) {
				case R.id.rb_eadlanguage_slow:
					SharedPreTool.setSharedPreDateInt(ReadingsetActivity.this,"EadLanguageShared",0);
					break;
				case R.id.rb_eadlanguage_the:
					SharedPreTool.setSharedPreDateInt(ReadingsetActivity.this,"EadLanguageShared",50);
					break;
				case R.id.rb_eadlanguage_fast:
					SharedPreTool.setSharedPreDateInt(ReadingsetActivity.this,"EadLanguageShared",100);
					break;
				}
				break;
			case R.id.rg_readcharacters:
				switch (checkedId) {
				case R.id.rb_readcharacters_male:
					SharedPreTool.setSharedPreDateString(ReadingsetActivity.this,"ReadCharactersShared","henry");
					break;
				case R.id.rb_readcharacters_female:
					SharedPreTool.setSharedPreDateString(ReadingsetActivity.this,"ReadCharactersShared","Vimary");
					break;
				}
				break;
			}
		}
	};
	/**
	 * 初始化TitleBar
	 */
	private void initTitleBar() {
		titlebar.setTitleBackgroundResource(R.drawable.menubutton_select,0);
		titlebar.setLeftGravity(Gravity.CENTER);
		titlebar.setLeftIco(R.drawable.back);
		titlebar.setCenterGravity(Gravity.CENTER);
		titlebar.setCenterText(getString(R.string.tx_readingset),17,Color.BLACK);
	}
	private ClickListener clickListener=new ClickListener() {
		
		@Override
		public void onright(View v) {
		}
		@Override
		public void onleft(View v) {
			finish();
		}
		@Override
		public void oncenter(View arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
