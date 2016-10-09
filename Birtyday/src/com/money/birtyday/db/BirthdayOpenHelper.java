package com.money.birtyday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BirthdayOpenHelper extends SQLiteOpenHelper {

	private static final String CREATE_USER = "create table User ("
			+ "id integer primary key autoincrement, "
			+ "name text, "
			+ "gender text, "
			+ "mobile text, "
			+ "birthday text, "
			+ "address text, "
			+ "memo text)";
	
	public BirthdayOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table User");
		db.execSQL(CREATE_USER);
	}

}
