package com.example.locule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.locule.adapter.Event;

import java.util.Calendar;

public class AddEventActivity extends Activity {

	private DatePicker dPicker; 
	private TimePicker timePicker;
	private EditText eventName, eventDescription, eventAddress;
	private int year, month, day;
	private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        
        //initializes everything so I dont have to do it later.
		dPicker = (DatePicker) findViewById(R.id.datePicker);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
    	eventName = (EditText) findViewById(R.id.eventName);
    	eventDescription = (EditText) findViewById(R.id.eventDescription);
    	eventAddress = (EditText) findViewById(R.id.eventAddress);
    	db = new DatabaseHandler(this);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		dPicker.init(year, month, day, null);
    	
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void addEvent(View v){
    	Event event;
    	event = new Event(eventName.getText().toString(), eventDescription.getText().toString(), 
    			eventAddress.getText().toString(), dPicker.getYear(), dPicker.getMonth(), 
    			dPicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), this);
    	db.addEvent(event);
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    public void switchMap(View v){
    	Intent intent = new Intent(this, MapActivity.class);
//    	EditText editText = (EditText)findViewById(R.id.map_view);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
    public void switchMain(View v){
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    public void switchAddEvent(View v){
    	Intent intent = new Intent(this, AddEventActivity.class);
    	startActivity(intent);
    }
    
}
