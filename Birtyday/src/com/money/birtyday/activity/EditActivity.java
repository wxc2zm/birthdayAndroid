package com.money.birtyday.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;

import com.money.birtyday.R;
import com.money.birtyday.model.User;
import com.money.birtyday.util.HttpCallbackListener;
import com.money.birtyday.util.HttpUtil;
import com.money.birtyday.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity {
	
	private static final String TAG = "EditActivity";
	
	private EditText editName;
	private EditText editGender;
	private EditText editMobile;
	private EditText editBirthday;
	private EditText editAddress;
	private EditText editMemo;
	
	private Button buttonSave;
	private Button buttonDelete;
	
	private List<User> list;
	
	private User user = new User();
	
	private String xmlPath = Environment.getExternalStorageDirectory() + "/birthday/xml/user.xml";
	
	private Handler myHandler = new Handler() {
		public void handlerMessage(Message message) {
			if (message.what == 001) {
				Intent intent = new Intent(EditActivity.this, UserActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(EditActivity.this, "保存失败", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_user);
		
		editName = (EditText) findViewById(R.id.name);
		editGender = (EditText) findViewById(R.id.gender);
		editMobile = (EditText) findViewById(R.id.mobile);
		editBirthday = (EditText) findViewById(R.id.birthday);
		editAddress = (EditText) findViewById(R.id.address);
		editMemo = (EditText) findViewById(R.id.memo);
		
		buttonSave = (Button) findViewById(R.id.save);
		buttonDelete = (Button) findViewById(R.id.delete);
		
		try {
			Intent intent = getIntent();
			String id = intent.getStringExtra("id");
			
			list = Utility.handleUserResponse(xmlPath);
			for (User user: list) {
				if (user.getId().equals(id)) {
					editName.setText(user.getName());
					if (user.isGender())
						editGender.setText("男");
					else {
						editGender.setText("女");
					}
					editMobile.setText(user.getMobile());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					editBirthday.setText(sdf.format(user.getBirthday()));
					editAddress.setText(user.getAddress());
					editMemo.setText(user.getMemo());
					break;
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		buttonSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(EditActivity.TAG, editName.getText().toString());
				Log.i(EditActivity.TAG, editGender.getText().toString());
				Log.i(EditActivity.TAG, "11111111111111111");
				user.setName(editName.getText().toString());
				Log.i(EditActivity.TAG, editGender.getText().toString());
				if (editGender.getText().toString().equals("男")) {
					user.setGender(true);
				} else {
					user.setGender(false);
				}
				Log.i(EditActivity.TAG, editMobile.getText().toString());	
				user.setMobile(editMobile.getText().toString());
				Log.i(EditActivity.TAG, editBirthday.getText().toString());
				try {
					user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(editBirthday.getText().toString()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.i(EditActivity.TAG, editAddress.getText().toString());
				user.setAddress(editAddress.getText().toString());
				user.setMemo("无");
				
				Object[] data = {user};
				//Log.i(EditActivity.TAG, data.toString());
				
				HttpUtil.webService(data, "update", new HttpCallbackListener() {
					
					@Override
					public void onSuccess(String respose) {
						Log.i(EditActivity.TAG, respose);
						Message message = new Message();
						message.what = Utility.resolveResult(respose);
						myHandler.sendMessage(message);
					}
					
					@Override
					public void onError(Exception e) {
						Log.i(EditActivity.TAG, e.toString());
					}
				});
			}
		});
	}
}
