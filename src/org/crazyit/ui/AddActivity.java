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

public class AddActivity extends Activity 
{
	String tname;
    String finish_time;
    int pri;
	int year,month,day;
	EditText et;
	TextView datetext;
	//RadioButton rbn0,rbn1,rbn2;
	RadioButton rbn[];
	Intent intent;
	Bundle bundle;
	String op;
	int _id;
	InputMethodManager im;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		datetext = (TextView) findViewById(R.id.datetext);
		et = (EditText)findViewById(R.id.edit1);
		
		et.setFocusable(true);
		im = (InputMethodManager)AddActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

		
		rbn = new RadioButton[3];
		rbn[0] = (RadioButton) findViewById(R.id.radio0);				
		rbn[1] = (RadioButton) findViewById(R.id.radio1);
		rbn[2] = (RadioButton) findViewById(R.id.radio2);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		pri = 2;
		if(month<9 && day<10){
			finish_time = year+"-"+"0"+(month+1)+"-"+"0"+day;
		}else if(month<9){
			finish_time = year+"-"+"0"+(month+1)+"-"+day;
		}else if(day<10){
			finish_time = year+"-"+(month+1)+"-"+"0"+day;
		}else{
			finish_time = year+"-"+(month+1)+"-"+day;
		}
		
		datetext.setText(finish_time);	
		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);		
		// ��ʼ��DatePicker�������ʼ��ʱָ��������
		datePicker.init(year, month, day, new OnDateChangedListener()
		{

			@Override
			public void onDateChanged(DatePicker arg0, int year
					, int month, int day)
			{
				AddActivity.this.year = year;
				AddActivity.this.month = month+1;
				AddActivity.this.day = day;
				if(month<9 && day<10){
					finish_time = year+"-"+"0"+(month+1)+"-"+"0"+day;
				}else if(month<9){
					finish_time = year+"-"+"0"+(month+1)+"-"+day;
				}else if(day<10){
					finish_time = year+"-"+(month+1)+"-"+"0"+day;
				}else{
					finish_time = year+"-"+(month+1)+"-"+day;
				}
				// ��ʾ��ǰ���ڡ�ʱ��
				datetext.setText(finish_time);

			}
		});
		

		
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator = new MenuInflater(this);
		// ״̬R.menu.context��Ӧ�Ĳ˵�������ӵ�menu��
		inflator.inflate(R.menu.task_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// �жϵ��������ĸ��˵��������Ե�������Ӧ��
		switch (item.getItemId())
		{
			
			case R.id.cancel:
				intent = getIntent();
				intent.putExtra("name","");
				AddActivity.this.setResult(0, intent);
				
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);
				
				finish();

				break;
			case R.id.save:			
				tname = et.getText().toString();
				if(rbn[0].isChecked()){
					pri = 1;
				}
				else if(rbn[1].isChecked()){
					pri = 2;
					}
					else pri = 3;
				//����һ��Bundle����
				intent = getIntent();
				intent.putExtra("name",tname);
				intent.putExtra("pri",String.valueOf(pri));
				intent.putExtra("finish_time",finish_time);
				AddActivity.this.setResult(0, intent);
				
				im.hideSoftInputFromWindow(et.getWindowToken(), 0);

				finish();

				break;

		}
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		intent = getIntent();
		intent.putExtra("name","");
		AddActivity.this.setResult(0, intent);
		finish();
	}

}
