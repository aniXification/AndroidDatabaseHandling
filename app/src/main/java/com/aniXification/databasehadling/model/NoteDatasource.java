package com.aniXification.databasehadling.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aniXification.databasehadling.dto.Note;

/**
 * @author aniXification
 * Jul 23, 2014 2014
 * NoteDatasource.java
 */
public class NoteDatasource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, 
			MySQLiteHelper.COLUMN_NAME, 
			MySQLiteHelper.COLUMN_DESCRIPTION, 
			MySQLiteHelper.COLUMN_IS_FAV , 
			MySQLiteHelper.COLUMN_TYPE, 
			MySQLiteHelper.COLUMN_REMINDER_TIMESTAMP , 
			MySQLiteHelper.COLUMN_TIMESPAMP};

	public NoteDatasource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public long insertNote(Note note){
		
		ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_NAME, note.getName());
	    values.put(MySQLiteHelper.COLUMN_DESCRIPTION, note.getDescription());
	    values.put(MySQLiteHelper.COLUMN_IS_FAV, note.getIsFav());
	    values.put(MySQLiteHelper.COLUMN_TYPE, note.getType());
	    values.put(MySQLiteHelper.COLUMN_REMINDER_TIMESTAMP, note.getReminderTimestamp());
	    values.put(MySQLiteHelper.COLUMN_TIMESPAMP, note.getTimestamp());
		
	    long insertId;
		try {
			insertId = database.insert(MySQLiteHelper.TABLE_NOTE, null, values);
		} catch (Exception x) {
			insertId = 0;
		}
		
		database.close();
		
		return insertId;
	}
	
	public boolean updateNote(Note note) {
		
		ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_NAME, note.getName());
	    values.put(MySQLiteHelper.COLUMN_DESCRIPTION, note.getDescription());
	    values.put(MySQLiteHelper.COLUMN_IS_FAV, note.getIsFav());
	    values.put(MySQLiteHelper.COLUMN_TYPE, note.getType());
	    values.put(MySQLiteHelper.COLUMN_REMINDER_TIMESTAMP, note.getReminderTimestamp());
	    values.put(MySQLiteHelper.COLUMN_TIMESPAMP, note.getTimestamp());
	    
	    try {
	    	database.update(MySQLiteHelper.TABLE_NOTE, values, "_id = ?", new String[] {note.getId().toString()});
	    	database.close();
	    	return true;
		} catch (Exception e) {
			database.close();
			return false;
		}
	    
	    
	  }
	
	  public void deleteNote(Note note) {
		  
		  long id = note.getId();
		  
		  database.delete(MySQLiteHelper.TABLE_NOTE, MySQLiteHelper.COLUMN_ID + " = " + id, null);
		  database.close();
	  }
	  
	  public void deletAllNotes() {		  
		  
		  database.delete(MySQLiteHelper.TABLE_NOTE, null, null);
		  database.close();
	  }
	  
	  public List<Note> getAllNotes(){
		  List<Note> notes = new ArrayList<Note>();
		  
		  Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTE, allColumns, null, null, null, null, MySQLiteHelper.COLUMN_TIMESPAMP + " DESC");
		  
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  Note record = cursorToAccount(cursor);			  
			  notes.add(record);
			  cursor.moveToNext();
		  }
				  
		  cursor.close();
		  database.close();
		  return notes;
	  }
	
	private Note cursorToAccount(Cursor cursor) {
		
		  Note note = new Note();
		  
		  note.setId(cursor.getLong(0));
		  note.setName(cursor.getString(1));
		  note.setDescription(cursor.getString(2));
		  note.setIsFav(cursor.getString(3));
		  note.setType(cursor.getString(4));
		  note.setReminderTimestamp(cursor.getLong(5));
		  note.setTimestamp(cursor.getLong(6));
		  
		  return note;
	  }
	
}
