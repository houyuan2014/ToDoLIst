package org.crazyit.ui;

import java.util.Calendar;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity 
{
	String tname,finish_time;
    int pri;
	int year,month,day;
	EditText et;
	TextView datetext;
	RadioButton rbn[];
	Intent intent;
	Bundle bundle;
	String _id;
	String create_time;
	InputMethodManager im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		datetext = (TextView) findViewById(R.id.datetext);
		et = (EditText)findViewById(R.id.edit1);
		rbn = new RadioButton[3];
		rbn[0] = (RadioButton) findViewById(R.id.radio0);				
		rbn[1] = (RadioButton) findViewById(R.id.radio1);
		rbn[2] = (RadioButton) findViewById(R.id.radio2);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		et.setFocusable(true);
		im = (InputMethodManager)EditActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

		intent = getIntent();
		bundle = intent.getExtras();
		_id = bundle.getString("_id");
		pri = Integer.parseInt(bundle.getString("pri"));
		tname = bundle.getString("name");
		finish_time = bundle.getString("finish_time");
		create_time = bundle.getString("create_time");
//		Toast.makeText(this, "create_time"+create_time+" pri"+pri+"id"+_id, Toast.LENGTH_LONG).show();
		
//		Toast toast0 = Toast.makeText(EditActivity.this, " pri = "+pri,
//				Toast.LENGTH_SHORT);
//		toast0.show();	
//		
		if(pri==R.drawable.pri_1){
			rbn[0].setChecked(true);
		}else if(pri==R.drawable.pri_2){
			rbn[1].setChecked(true);
		}else if(pri==R.drawable.pri_3){
			rbn[2].setChecked(true);
		}	
		et.setText(tname);
		datetext.setText(finish_time);
	
		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);		
		// 初始化DatePicker组件，初始化时指定监听器
		datePicker.init(year, month, day, new OnDateChangedListener()
		{

			@Override
			public void onDateChanged(DatePicker arg0, int year
					, int month, int day)
			{
				EditActivity.this.year = year;
				EditActivity.this.month = month+1;
				EditActivity.this.day = day;
				if(month<9&&day<10){
					finish_time = year+"-"+"0"+(month+1)+"-"+"0"+day;
				}else if(month<9){
					finish_time = year+"-"+"0"+(month+1)+"-"+day;
				}else if(day<10){
					finish_time = year+"-"+(month+1)+"-"+"0"+day;
				}else{
					finish_time = year+"-"+(month+1)+"-"+day;
				}
				// 显示当前日期、时间
				datetext.setText(finish_time);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator = new MenuInflater(this);
		// 状态R.menu.context对应的菜单，并添加到menu中
		inflator.inflate(R.menu.task_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// 判断单击的是哪个菜单项，并针对性的作出响应。
		switch (item.getItemId())
		{
			case R.id.cancel:
//				Toast toast1 = Toast.makeText(this, "cancel菜单项",
//						Toast.LENGTH_SHORT);
//				toast1.show();
				intent.putExtra("nname","");
				EditActivity.this.setResult(1, intent);
				
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);

				finish();

				break;
			case R.id.save:
//				Toast toast0 = Toast.makeText(this, "save菜单项",
//						Toast.LENGTH_SHORT);
//				toast0.show();				
				tname = et.getText().toString();
				if(rbn[0].isChecked()){
					pri = 1;
				}
				else if(rbn[1].isChecked()){
					pri = 2;
					}
					else pri = 3;				
				
				intent.putExtra("nname",tname);
				intent.putExtra("npri",pri);
				intent.putExtra("nfinish_time",finish_time);
				intent.putExtra("_id", _id);	
				intent.putExtra("create_time", create_time);
				EditActivity.this.setResult(1, intent);
				
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);

				finish();

				break;


		}
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		intent.putExtra("nname","");
		EditActivity.this.setResult(1, intent);
		finish();
	}


}
