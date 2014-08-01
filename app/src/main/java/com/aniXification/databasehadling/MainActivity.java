package com.aniXification.databasehadling;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aniXification.databasehadling.adapter.NoteAdapter;
import com.aniXification.databasehadling.dto.Note;
import com.aniXification.databasehadling.model.NoteDatasource;

/**
 * @author aniXification
 * Jul 23, 2014 2014
 * MainActivity.java
 */

public class MainActivity extends ListActivity {

	private NoteDatasource noteDatasource;
	private Runnable viewNotes;
	private ProgressDialog mProgressDialog = null;
	private ArrayList<Note> notesArrayList = null;
	private NoteAdapter noteAdapter = null;
	private Note note = null;
	
	private String description="";
	private String name = "";
	private Long noteId = -1L;
	private String noteType = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_display);
		
		notesArrayList = new ArrayList<Note>();
		this.noteAdapter = new NoteAdapter(this, R.layout.row, notesArrayList);
        setListAdapter(noteAdapter);
        
        //Thread
        viewNotes = new Runnable() {
			@Override
			public void run() {
				getNotesAndReminders();
			}
		};

		Thread thread = new Thread(null, viewNotes, "notesthread");
		thread.start();
		mProgressDialog = ProgressDialog.show(MainActivity.this,"Please wait...", "Retreiving notes.", true);
		
		/* onitemlongclick listener */
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int selectedIndex, long id) {

				final Note r = (Note) getListAdapter().getItem(selectedIndex);
				final CharSequence[] items = { "Edit", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Actions");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							showAlertDialog(r);
							
							break;

						case 1:
							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setMessage(
									"Are you sure you want to delete?")
									.setCancelable(false)
									// Prevents user to use "back button"
									.setPositiveButton("Delete",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													deleteNote(r);
												}
											})
									.setNegativeButton("Cancel",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													dialog.cancel();
												}
											});
							builder.show();
							
							break;
						default:
							break;
						}

					}
				});

				AlertDialog alert = builder.create();
				alert.show();
				return false;

			}
		});
		
	}
	
	private void getNotesAndReminders() {
		try {
			noteDatasource = new NoteDatasource(this);
			noteDatasource.open();

			notesArrayList = (ArrayList<Note>) noteDatasource.getAllNotes();

			setListAdapter(noteAdapter);

		} catch (Exception e) {
			e.printStackTrace();
		} 

		runOnUiThread(returnRes);
	}
	
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {	
			
			if (notesArrayList != null && notesArrayList.size() > 0) {
				noteAdapter.notifyDataSetChanged();

				for (Iterator<Note> it = notesArrayList.iterator(); it.hasNext();) {
					noteAdapter.add(it.next());
				}

				mProgressDialog.dismiss();
				noteAdapter.notifyDataSetChanged();
			} else {
				mProgressDialog.dismiss();
			}
		}
	};
	
	private void deleteNote(Note r) {
		try {

			try {
				noteDatasource = new NoteDatasource(this);
				noteDatasource.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			noteDatasource.deleteNote(r); // delete from database
			noteDatasource.close();
			noteAdapter.remove(r); // remove from adapter
			noteAdapter.notifyDataSetChanged(); // notify to UI

			Toast.makeText(this, "note deleted!!", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void showAlertDialog(final Note a){
		
		LayoutInflater inflater = getLayoutInflater();
		View addEditTextsView = inflater.inflate(R.layout.add_note_dialog, null);
		
		final  RadioGroup radioTypeGroup = (RadioGroup)  addEditTextsView.findViewById(R.id.radioType);
		
		final EditText noteTitle = (EditText) addEditTextsView.findViewById(R.id.note_title);
		final EditText noteDescription = (EditText) addEditTextsView.findViewById(R.id.note_description);
		
		//check if the dialog is for adding or editing!!
		if(a == null){
			//do nothing!
		} else {
			noteTitle.setText(a.getName().trim());
			noteDescription.setText(a.getDescription().trim());
		}
		
		/*
		 * radio listerners start
		 */
		
		radioTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioNote:
						Toast.makeText(MainActivity.this, "Note Checked", Toast.LENGTH_SHORT).show();
						noteType = "0";
						
						//MAKE TIME AND DATE PICKER invisible
						
					break;
					
				case R.id.radioReminder:
					Toast.makeText(MainActivity.this, "Reminder Checked", Toast.LENGTH_SHORT).show();
					noteType = "1";
					
					//MAKE TIME AND DATE PICKER visible
					
					break;

				default:
					break;
				}
			}
		});
		
		/*
		 * 
		 * radio listeners end
		 */
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		TextView title = (TextView) addEditTextsView.findViewById(R.id.textview_dialog_title);
		title.setText("Note");
		title.setBackgroundColor(getResources().getColor(R.color.black));
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20);
		builder.setInverseBackgroundForced(true);
		
		builder.setCancelable(false)
				.setView(addEditTextsView)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								name = noteTitle.getText().toString();
								description = noteDescription.getText().toString();
								
								if(name.length() == 0 || description.length() == 0){ //check the fields are entered!!
									Toast.makeText(MainActivity.this, "Don't leave nothing empty!", Toast.LENGTH_SHORT).show();
								} else { //if all fields are entered
									try {
										noteDatasource = new NoteDatasource(getApplicationContext());
										noteDatasource.open();
										
										Long timeStamp_milli = new Date().getTime();
										
										note = new Note();
										
										note.setName(name);
										note.setDescription(description);
										note.setIsFav("1"); //set the layout accordingly!!
										note.setTimestamp(timeStamp_milli);
										note.setType(noteType);
										
										//check if the dialog is for adding or editing!!
										if(a == null){ //insert here!!		
											noteId = noteDatasource.insertNote(note);
											if(noteId > 0){
												note.setId(noteId);
												noteFromAddDialog(note); //pass the record to the main activity for updating adapter!!
											} else {
												Toast.makeText(getApplicationContext(), "Error adding note!", Toast.LENGTH_SHORT).show();
											}
											
											
										} else { //update from here!!
											note.setId(a.getId());
											boolean updated =  noteDatasource.updateNote(note);
											
											if(updated){
												noteAdapter.notifyDataSetChanged();
											} else {
												Toast.makeText(getApplicationContext(), "Error updating note!", Toast.LENGTH_SHORT).show();
											}
										}
										
										noteDatasource.close();
										
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										noteDatasource.close();
									}
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
		builder.show();
	}
	
	private void noteFromAddDialog(Note updateNote){
		note.setName(updateNote.getName());
		note.setDescription(updateNote.getDescription());
		note.setIsFav(updateNote.getIsFav());
		note.setTimestamp(updateNote.getTimestamp());
		note.setType(noteType);
		
		notesArrayList.add(note);
		noteAdapter.add(note);
		
		noteAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
	        case R.id.menu_add:
                showAlertDialog(null);
                break;
                
	        default:
				break;
				
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.add_menu_option, menu);
    	return super.onCreateOptionsMenu(menu);
	}
	
}


