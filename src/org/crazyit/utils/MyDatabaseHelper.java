package org.crazyit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	final String CREATE_TABLE_USER =
			"CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR, pass VARCHAR, status INTEGER, last_sync, show_all_tag)";
	final String CREATE_TABLE_TASK =
			"CREATE TABLE task(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, pri , finish_time , edit_time , create_time, ischeckoff, status INTEGER, email VARCHAR)";

	public MyDatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_TASK);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int  oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
			+ oldVersion + "--->" + newVersion);
		
	}

}
