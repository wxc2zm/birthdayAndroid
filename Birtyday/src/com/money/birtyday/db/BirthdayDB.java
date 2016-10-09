package com.money.birtyday.db;

import java.util.ArrayList;
import java.util.List;

import com.money.birtyday.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BirthdayDB {
	
	/**
	 * 数据库名
	 */
	private static final String DB_NAME = "birthday";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	private static BirthdayDB birthdayDB;
	
	private SQLiteDatabase db;
	
	/**
	 * 将构造方法私有化
	 */
	private BirthdayDB(Context context) {
		BirthdayOpenHelper dbHelper = new BirthdayOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 获取CoolWeatherDB的实例。
	 */
	public static BirthdayDB getInstance(Context context) {
		if (birthdayDB == null) {
			birthdayDB = new BirthdayDB(context);
		}
		return birthdayDB;
	}
	
	/**
	 * 将User实例存储到数据库。
	 */
	public void saveUser(User user) {
		if (user != null) {
			ContentValues values = new ContentValues();
			values.put("id", user.getId());
			values.put("name", user.getName());
			values.put("gender", user.getGender());
			values.put("mobile", user.getMobile());
			values.put("birthday", user.getBirthday());
			values.put("address", user.getAddress());
			values.put("memo", "无");
			db.insert("User", null, values);
		}
	}
	
	/**
	 * 从数据库中读取
	 * @return
	 */
	public List<User> loadUser() {
		List<User> list = new ArrayList<User>();
		Cursor cursor = db.query("User", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				User user = new User();
				user.setId(cursor.getString(cursor.getColumnIndex("id")));
				user.setName(cursor.getString(cursor.getColumnIndex("name")));
				user.setGender(cursor.getString(cursor.getColumnIndex("gender")));
				user.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
				user.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
				user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				user.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
			} while (cursor.moveToNext());
		}
		return list;
	}
}
