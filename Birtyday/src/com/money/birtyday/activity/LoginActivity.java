package com.money.birtyday.activity;

import com.money.birtyday.R;
import com.money.birtyday.util.HttpCallbackListener;
import com.money.birtyday.util.HttpUtil;
import com.money.birtyday.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";
	private EditText editText1;
	private EditText editText2;
	private Button button;
	private String username;
	private String password;
	
	
	private Handler myHandler = new Handler() {
		public void handleMessage(Message message) {
			if (message.what == 01) {
				Intent intent = new Intent(LoginActivity.this, UserActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_admin);
		editText1 = (EditText) findViewById(R.id.username);
		editText2 = (EditText) findViewById(R.id.password);
		button = (Button) findViewById(R.id.login);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = editText1.getText().toString();
				password = editText2.getText().toString();
				Object[] data = {username, password};
				HttpUtil.webService(data, "guestLogin", new HttpCallbackListener() {
					@Override
					public void onSuccess(String respose) {
						Message message = new Message();
						message.what = Utility.resolveResult(respose);
						myHandler.sendMessage(message);
					}

					@Override
					public void onError(Exception e) {
						Log.i(LoginActivity.TAG, "22222");
						Log.i(LoginActivity.TAG, e.toString());
					}
				});
			}
		});
	}

}
