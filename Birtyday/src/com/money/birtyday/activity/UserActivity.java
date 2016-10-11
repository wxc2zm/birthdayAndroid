package com.money.birtyday.activity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.money.birtyday.R;
import com.money.birtyday.model.User;
import com.money.birtyday.util.HttpCallbackListener;
import com.money.birtyday.util.HttpUtil;
import com.money.birtyday.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Activity {
	private static final String TAG="UserActivity";
	
	private ProgressDialog progressDialog;
	private ListView listView;
	private TextView titleText;
	
	private ArrayAdapter<String> adapter;
	
	private User user;
	
	private List<String> dataList = new ArrayList<String>();
	private String xmlPath = Environment.getExternalStorageDirectory() + "/birthday/xml/user.xml";
	/**
	 * 客户列表
	 */
	private List<User> userList = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_user);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setTag("客户管理");
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				user = userList.get(position);
				Intent intent = new Intent(UserActivity.this, EditActivity.class);
				intent.putExtra("id", user.getId());
				startActivity(intent);
			}
			
		});
		queryUsers();
	}

	private void queryUsers() {
		long clientFile = new File(xmlPath).length();
		Object[] data = {clientFile};
		HttpUtil.webService(data, "compareFile", new HttpCallbackListener() {
			
			@Override
			public void onSuccess(String respose) {
				Log.i(UserActivity.TAG, respose);
				int number = Utility.resolveResult(respose);
				if(number == 0002) {
					HttpUtil.sendHttpRequest();
				}
				try {
					userList = Utility.handleUserResponse(xmlPath);
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (userList.size() > 0) {
					for (User user: userList) {
						dataList.add(user.getName());
					}
					
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				
			}
		});
		
	}
	
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	
}
