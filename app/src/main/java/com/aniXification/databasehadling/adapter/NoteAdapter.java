/**
 * 
 */
package com.aniXification.databasehadling.adapter;

import java.sql.SQLException;
import java.text.Format;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.aniXification.databasehadling.R;
import com.aniXification.databasehadling.dto.Note;
import com.aniXification.databasehadling.model.NoteDatasource;

/**
 * @author aniXification
 * Jul 23, 2014 2014
 * NoteAdapter.java
 */
public class NoteAdapter extends ArrayAdapter<Note>{

	private Context context;
	private List<Note> notesList;
	Format formatter;
	String timeStamp;
	
	public NoteAdapter(Context context, int textViewResourceId, List<Note> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.notesList = items;
	}

	public int getCount() {
		return notesList.size();
	}

	public Note getItem(int position) {
		return notesList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		
		final Note note = notesList.get(position);
		final NoteDatasource noteDatasource;
		noteDatasource = new NoteDatasource(context);
		
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
        }
        
        
        if (note != null) {
			TextView tt = (TextView) convertView.findViewById(R.id.toptext);
			TextView bt = (TextView) convertView.findViewById(R.id.bottomtext);
			CheckBox cb  = (CheckBox) convertView.findViewById(R.id.cb_fav);
			ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);
			
			if (tt != null) {
				tt.setText(note.getName());
			}
			if (bt != null) {			
				bt.setText(note.getDescription() );
			}
			
			//SET the NOTE or REMINDER icon
			if(note.getType().equals("0")){ //note
				iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_note_icon));
			} else {
				iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reminder_icon));
			}
			
			//check if the item is fav from the database
			if(note.getIsFav().equalsIgnoreCase("1")){
				cb.setChecked(true);
			}
			
			//set the checkbox listener
			cb.setOnClickListener(new OnClickListener() {

			      @Override
			      public void onClick(View v) {
			        if (((CheckBox) v).isChecked()) {
			        	try {
							noteDatasource.open();
							note.setIsFav("1");
				        	boolean updated =  noteDatasource.updateNote(note);
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							noteDatasource.close();
						}
						
			        } else
						try {
							noteDatasource.open();
							note.setIsFav("0");
				        	boolean updated =  noteDatasource.updateNote(note);
						} catch (SQLException e) {
							e.printStackTrace();
						} finally {
							noteDatasource.close();
						}
			        	
			      }
			    });
			
		}
		return convertView;
       
	}

	public void remove(Note record) {
		notesList.remove(record);
	}
	
	public void removeAll() {
		notesList.removeAll(notesList);
	}
}
