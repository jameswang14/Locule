package com.example.locule.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.locule.distance.Route;
import com.example.locule.distance.Routing;
import com.example.locule.distance.RoutingListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Event implements RoutingListener {

	long id;
    Context _context;
	String _name;
	String _description;
	String _address;
	GregorianCalendar dateTime;
    String travelTime;
    Routing routing;



	public Event(){
		
	}
	public Event(int i, String name, String desc, String address, int year, int month, int day, int hour, int min, Context context){
		id = i;
        _context = context;
		_name = name; 
		_description = desc;
		_address = address;
		dateTime = new GregorianCalendar(year, month, day, hour, min);
        //proccessLength();
	}
	public Event(String name, String desc, String address, int year, int month, int day, int hour, int min, Context context){
        _context = context;
		_name = name; 
		_description = desc;
		_address = address;
		dateTime = new GregorianCalendar(year, month, day, hour, min);
        //proccessLength();
	}
	public Event(int i, String name, String desc, String Address, Date d, Context context){
        _context = context;
		id = i;
		_name = name;
		_description = desc;
		_address = Address;
		dateTime = new GregorianCalendar();
        dateTime.setTime(d);
        //proccessLength();
	}
	public Event(String name, String desc, String Address, Date d, Context context){
        _context = context;
		_name = name;
		_description = desc;
		_address = Address;
		dateTime = new GregorianCalendar();
        dateTime.setTime(d);
        //proccessLength();
	}
	public long getID(){
		return id;
	}
	public void setID(long i){
		id = i;
	}
	public String getName(){
		return _name;
	}
	public void setName(String name){
		_name = name;
	}
	public String getDescription(){
		return _description;
	}
	public void setDescription(String desc){
		_description = desc;
	}
	public void setAddress(String address){
		_address = address;
	}
	public String getAddress(){
		return _address;
	}
	public Date getDate(){
		return (Date) dateTime.getTime();
	}
	public void setDate(int year, int month, int day, int hour, int min){
		dateTime.set(year,  month, day, hour, min);
	}
    public String getTravelTime()
    {
        return travelTime;
    }
	public void setDate(Date d){
		dateTime.setTime(d);
	}


	public String getTimeString(){
		Date d1 = dateTime.getTime();
		Date d2 = new Date();
   
		// Get msec from each, and subtract.
		long diff = d1.getTime() - d2.getTime();
		long diffSeconds = diff / 1000 % 60;  
		long diffMinutes = diff / (60 * 1000) % 60;       
		long diffHours = diff / (60 * 60 * 1000)%24;                      
		long diffDays = diff / (24*60*60*1000);
		if(diff >0)
			return String.valueOf(diffDays) + " Days " + String.valueOf(diffHours) + " Hours " + String.valueOf(diffMinutes) + " Min " + String.valueOf(diffSeconds) + " Sec ";
		return "Overdue";
	}
	 public double stringToLat(Context context){
	 Geocoder coder = new Geocoder(context);
	 List<Address> address = null;
			try {
				address = coder.getFromLocationName(_address,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     if (address == null) 
	         return -1;
	     Address location = address.get(0);
	     return location.getLatitude();
	 }
	 public double stringToLong(Context context){
	 Geocoder coder = new Geocoder(context);
	 List<Address> address = null;
			try {
				address = coder.getFromLocationName(_address,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     if (address == null) 
	         return -1;
	     Address location = address.get(0);
	     return location.getLongitude();

	 }

    public void proccessLength()
    {
        LocationManager lm = (LocationManager)_context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        LatLng start = new LatLng(lat,lng);
        LatLng end = new LatLng(stringToLat(_context), stringToLong(_context));

        //Change later for other travel options
        Routing routing = new Routing(Routing.TravelMode.WALKING);
        routing.registerListener(this);
        routing.execute(start,end);

    }
    public void onRoutingFailure()
    {
        Log.e("Distance", "Error calculating distance");
    }
    public void onRoutingStart()
    {

    }
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route)
    {
        travelTime = route.getDurationText();
        Log.d("Distance", "Length is " + travelTime);
    }
	
}
