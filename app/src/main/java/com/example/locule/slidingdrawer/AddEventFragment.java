package com.example.locule.slidingdrawer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.example.locule.DatabaseHandler;
import com.example.locule.R;
import com.example.locule.adapter.Event;

import java.util.Calendar;

public class AddEventFragment extends Fragment {
	private DatePicker dPicker; 
	private TimePicker timePicker;
	private EditText eventName, eventDescription, eventAddress;
	private int year, month, day;
	private DatabaseHandler db;
	public AddEventFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        
        RelativeLayout rl = (RelativeLayout)inflater.inflate(R.layout.fragment_add_event, container, false);

        dPicker = (DatePicker) rl.findViewById(R.id.datePicker);
		timePicker = (TimePicker) rl.findViewById(R.id.timePicker);
    	eventName = (EditText) rl.findViewById(R.id.eventName);
    	eventDescription = (EditText) rl.findViewById(R.id.eventDescription);
    	eventAddress = (EditText) rl.findViewById(R.id.eventAddress);
    	db = new DatabaseHandler(getActivity());
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		dPicker.init(year, month, day, null); 
        
    
		
		Button b = (Button) rl.findViewById(R.id.addEventBtn);
        b.setOnClickListener(new Button.OnClickListener() {

           public void onClick(View v) {
        	   Event event;
   	    	event = new Event(eventName.getText().toString(), eventDescription.getText().toString(), 
   	    			eventAddress.getText().toString(), dPicker.getYear(), dPicker.getMonth(), 
   	    			dPicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), getActivity());
   	    	db.addEvent(event);
   	    	FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
   			fragmentManager.beginTransaction()
   					.replace(R.id.frame_container, new HomeFragment()).commit();
           }
        });
        return rl;
    }
	
	 public void addEvent(View v){
	    	Event event;
	    	event = new Event(eventName.getText().toString(), eventDescription.getText().toString(), 
	    			eventAddress.getText().toString(), dPicker.getYear(), dPicker.getMonth(), 
	    			dPicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), getActivity());
	    	db.addEvent(event);
	    	FragmentManager fragmentManager = getChildFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, new HomeFragment()).commit();
	    }
}
