package org.crazyit.ui;



import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.crazyit.utils.HttpUtil;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	String email,pass;
	EditText et_email, et_pass;
	Button bn_logup,bn_login,bn_loginoff;
	boolean offline = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//初始化控件
		et_email = (EditText) findViewById(R.id.email);
		et_pass = (EditText) findViewById(R.id.pass);
		bn_logup = (Button) findViewById(R.id.logup);
		bn_login = (Button) findViewById(R.id.login);
		bn_loginoff = (Button) findViewById(R.id.loginoff);
		
		bn_loginoff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email = et_email.getText().toString();
				pass = et_pass.getText().toString();
				login(email,pass);
			}
		});
		//未注册按钮添加监听器
		bn_logup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email = et_email.getText().toString();
				pass = et_pass.getText().toString();
				if (validate(email,pass)){
					//服务器注册处理
					if(logupPro(email,pass)){
						//本地登陆
						login(email,pass);
					}
				}  

			}




		});
		//为登陆按钮添加监听器
		bn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email = et_email.getText().toString();
				pass = et_pass.getText().toString();
				// 执行输入校验
				if (validate(email,pass))  
				{
					// 服务器验证
					if (loginPro(email,pass)) 
					{
						//跳转到主Activity
						login(email,pass);
						
					}
				}
			}
		});
		
	}
	
	private boolean validate(String em,String pa)
	{
		if (em.equals(""))
		{
			Toast.makeText(this, "用户名为必填项",Toast.LENGTH_SHORT).show();
			return false;
		}
		if (pa.equals(""))
		{
			Toast.makeText(this, "用户口令是必填项",Toast.LENGTH_SHORT).show();
			return false;
		}

		String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		if(!matcher.matches()){
			Toast.makeText(this, "邮箱格式不正确",Toast.LENGTH_SHORT).show();
			return false;		
			}
		return true;
	}
	
	private void login(String em, String pa) {
		// TODO Auto-generated method stub
		// 启动Main Activity
		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
		intent.putExtra("email",email);
		intent.putExtra("pass", pass);
		intent.putExtra("status", "1");
		intent.putExtra("offline", offline);
		LoginActivity.this.startActivity(intent);
		// 结束该Activity
		finish();
	}
	
	private boolean logupPro(String email,String pass) {
		// TODO Auto-generated method stub
			try
			{
				Map<String,String> map = new HashMap<String, String>();
				map.put("email", email);
				map.put("pass", pass);
				String url = HttpUtil.BASE_URL+"JSON_Register.ashx";
				
				//Log.i("url", url);
				//Log.i("map", map.toString());
							
				JSONObject jsonObject = new JSONObject(HttpUtil.postRequest(url, map));
				
				Log.i("logup", jsonObject.getString("register"));

				if (jsonObject.getString("register").equals("success"))
				{
					return true;
				}else if(jsonObject.getString("register").equals("fail")){
					Toast.makeText(this, "用户已存在",Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			catch (Exception e)
			{		
				Toast.makeText(this, "服务器响应异常，离线使用",Toast.LENGTH_SHORT).show();
				setOffline();
				e.printStackTrace();
			}
		
 		return false;

	}
	private boolean loginPro(String email,String pass)
	{
		try
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("email", email);
			map.put("pass", pass);
			String url = HttpUtil.BASE_URL+"JSON_Login.ashx";
			JSONObject jsonObject = new JSONObject(HttpUtil.postRequest(url, map));
			
			Log.i("login", jsonObject.toString());
			
			if (jsonObject.getString("login").equals("success"))
			{
				return true;
			}else if(jsonObject.getString("login").equals("fail")){
				Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
				return false;
			}
			
		}
		catch (Exception e)
		{
			Toast.makeText(this, "服务器响应异常，离线使用",Toast.LENGTH_SHORT).show();
			setOffline();
			e.printStackTrace();
		}
		return false;
	}
	
private void setOffline(){
		et_email.setText("test@qq.com");
		et_pass.setText("123");
		bn_logup.setVisibility(View.INVISIBLE);
		bn_login.setVisibility(View.INVISIBLE);
		bn_loginoff.setVisibility(View.VISIBLE);
		offline = true;
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}



}
