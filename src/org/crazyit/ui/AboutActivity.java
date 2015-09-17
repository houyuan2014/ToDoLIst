package org.crazyit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AboutActivity extends Activity{
	
	EditText et_feedback, et_userinfo;
	Button bn_feedback;
	String feedback,userinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		et_feedback = (EditText) findViewById(R.id.feedback);
		et_userinfo = (EditText) findViewById(R.id.userinfo);
		bn_feedback = (Button) findViewById(R.id.bn_feedback);

		bn_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				feedback = et_feedback.getText().toString();
				if(!feedback.isEmpty()){
					userinfo = et_userinfo.getText().toString();
					
					
					
					finish();
					}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(AboutActivity.this, "¸ÐÐ»ÄúµÄ·´À¡", Toast.LENGTH_SHORT).show();


	}
	


}
