package com.money.birtyday.activity;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.money.birtyday.R;
import com.money.birtyday.db.BirthdayDB;
import com.money.birtyday.model.User;
import com.money.birtyday.util.HttpCallbackListener;
import com.money.birtyday.util.HttpUtil;
import com.money.birtyday.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
	
	private ProgressDialog progressDialog;
	private BirthdayDB birthdayDB;
	private ListView listView;
	private TextView titleText;
	
	private ArrayAdapter<String> adapter;
	
	private List<String> dataList = new ArrayList<String>();
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
		listView.setAdapter(adapter);
		birthdayDB = BirthdayDB.getInstance(this);
		/*
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				
			}
		});*/
		queryUsers();
	}
	
	
	private void queryUsers() {
		userList = birthdayDB.loadUser();
		if (userList.size() > 0) {
			userList.clear();
			for (User user: userList) {
				dataList.add(user.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setTag("客户管理");
			
		} else {
			queryFromServer();
		}
	}
	private void queryFromServer() {
		String address = "http://119.10.54.179:8080/etour/zndl/110000/resource_package/1alpkgy/user.xml";
		showProgressDialog();
		//Toast.makeText(TestActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				try {
					
					result = Utility.handleUserResponse(birthdayDB, response);
					Toast.makeText(TestActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (result) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							closeProgressDialog();
							queryUsers();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(TestActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
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
