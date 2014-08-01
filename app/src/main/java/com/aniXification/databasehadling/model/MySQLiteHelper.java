package com.aniXification.databasehadling.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "NOTES";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NOTE = "NOTE";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_IS_FAV = "is_fav";
	public static final String COLUMN_TYPE = "type";  // 0 = note, 1 = reminder
	public static final String COLUMN_REMINDER_TIMESTAMP = "reminder_timestamp"; //set if the TYPE = 1 [reminder]
	public static final String COLUMN_TIMESPAMP = "timestamp";
	
	private static final String CREATE_TABLE_NOTE = "create table "
			+ TABLE_NOTE + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_NAME + " text, "
			+ COLUMN_DESCRIPTION + " text, " 
			+ COLUMN_IS_FAV + " text, "
			+ COLUMN_TYPE+ " text, "
			+ COLUMN_REMINDER_TIMESTAMP + " text, "
			+ COLUMN_TIMESPAMP + " text, " 
			+ "UNIQUE (name)" + ");";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLE_NOTE);
		insertData(db);
	}

	private void insertData(SQLiteDatabase db) {
		
		ContentValues cvNote1 = new ContentValues();
		
		cvNote1.put(COLUMN_NAME, "Note1 title");
		cvNote1.put(COLUMN_DESCRIPTION, "Note1 Description");
		cvNote1.put(COLUMN_IS_FAV, "0");
		cvNote1.put(COLUMN_TYPE , "0");
		cvNote1.put(COLUMN_REMINDER_TIMESTAMP , "");
		cvNote1.put(COLUMN_TIMESPAMP, "1406105381759");
		
		db.insert(TABLE_NOTE, null, cvNote1);
		
		ContentValues cvNote2 = new ContentValues();
		
		cvNote2.put(COLUMN_NAME, "Note2 title");
		cvNote2.put(COLUMN_DESCRIPTION, "Note2 Description");
		cvNote2.put(COLUMN_IS_FAV, "1");
		cvNote2.put(COLUMN_TYPE , "0");
		cvNote2.put(COLUMN_REMINDER_TIMESTAMP , "");
		cvNote2.put(COLUMN_TIMESPAMP, "1406105429780");
		
		db.insert(TABLE_NOTE, null, cvNote2);
		
		//ADD REMINDER
		ContentValues cvReminder1 = new ContentValues();
		
		cvReminder1.put(COLUMN_NAME, "Reminder1 title");
		cvReminder1.put(COLUMN_DESCRIPTION, "Remunder2  Description");
		cvReminder1.put(COLUMN_IS_FAV, "1");
		cvReminder1.put(COLUMN_TYPE , "1");
		cvReminder1.put(COLUMN_REMINDER_TIMESTAMP , "1406105429780");
		cvReminder1.put(COLUMN_TIMESPAMP, "1406105429780");
		
		db.insert(TABLE_NOTE, null, cvReminder1);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
	}

}
