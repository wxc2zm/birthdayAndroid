package com.money.birtyday.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.money.birtyday.R;
import com.money.birtyday.model.User;
import com.money.birtyday.util.HttpUtil;
import com.money.birtyday.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Activity {
	private static final String ACTIVITY_TAG="TestActivity";
	
	private ProgressDialog progressDialog;
	private ListView listView;
	private TextView titleText;
	
	private ArrayAdapter<String> adapter;
	
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
		listView.setAdapter(adapter);
		queryUsers();
	}

	private void queryUsers() {
		if (!new File(xmlPath).exists()) {
			showProgressDialog();
			HttpUtil.sendHttpRequest();
			closeProgressDialog();
			queryUsers();
		}
		try {
			userList = Utility.handleUserResponse(xmlPath);
			Log.i(UserActivity.ACTIVITY_TAG, userList.toString());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (userList.size() > 0) {
			for (User user: userList) {
				dataList.add(user.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setTag("客户管理");
		}
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
