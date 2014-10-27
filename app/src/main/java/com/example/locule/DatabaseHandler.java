package com.example.locule;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.locule.adapter.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DatabaseHandler extends SQLiteOpenHelper {

	  // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "eventsManager.db";
 
    // Contacts table name
    private static final String TABLE_EVENTS = "events";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date";
    Context context;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE events ( id INTEGER PRIMARY KEY autoincrement, name TEXT, description TEXT, " + KEY_ADDRESS +  " TEXT, date TEXT)";
        db.execSQL(CREATE_EVENTS_TABLE);
        Log.v("Database", "DB Created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
 
        // Create tables again
        onCreate(db);
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName()); 
        values.put(KEY_DESC, event.getDescription()); 
        values.put(KEY_ADDRESS, event.getAddress());
        values.put(KEY_DATE, dateToString(event.getDate()));

        db.insert(TABLE_EVENTS, null, values);
        Log.v("Database", "Event Added");
    }
    public Event getEvent(long id){
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_EVENTS, new String[] {
                KEY_NAME, KEY_DESC, KEY_ADDRESS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Event event = new Event(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), stringToDate(cursor.getString(4)),context);
        // return contact
        return event;
    }    
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)),cursor.getString(1), 
                		cursor.getString(2), cursor.getString(3), stringToDate(cursor.getString(4)),context);
                // Adding contact to list
               eventList.add(event);
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return eventList;
    }
    public int getEventCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        cursor.close();
 
        // return count
        return temp;
    }
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, event.getName());
        values.put(KEY_DESC, event.getDescription());
        values.put(KEY_ADDRESS, event.getAddress());
        values.put(KEY_DATE, dateToString(event.getDate()));
     
        // updating row
        return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getID()) });
    }
    public boolean deleteEvent	(Event e) {
    	
    	boolean result = false;
    	
    	String query = "Select * FROM " + TABLE_EVENTS + " WHERE " + KEY_NAME + " =  \"" + e.getName() + "\"";

    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	Cursor cursor = db.rawQuery(query, null);
    	
    	Event event = new Event();
    	
    	if (cursor.moveToFirst()) {
    		event.setID(Integer.parseInt(cursor.getString(0)));
    		db.delete(TABLE_EVENTS, KEY_ID + " = ?",
    	            new String[] { String.valueOf(event.getID()) });
    		cursor.close();
    		result = true;
    	}
            db.close();
    	return result;
    }
    
    public static String dateToString(Date d) {
    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	String reportDate = df.format(d);
        return reportDate;
    }
    public static Date stringToDate(String s){
    	Date date;
		try {
			date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH).parse(s);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public Event cursorToEvent(Cursor cursor){
    	Event event = new Event();
        event.setID(cursor.getLong(0));
        event.setName(cursor.getString(1));
        event.setDescription(cursor.getString(2));
        event.setAddress(cursor.getString(3));
        event.setDate(stringToDate(cursor.getString(4)));
        return event;
    	
    }

}
