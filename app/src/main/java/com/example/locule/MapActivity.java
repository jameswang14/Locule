package com.example.locule;

import java.io.IOException;
import java.util.List;

import com.example.locule.R;
import com.example.locule.adapter.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends Activity {
	private GoogleMap googleMap;
    private DatabaseHandler db = new DatabaseHandler(this);
	private LatLng myPosition;

	 @Override
	 
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map_view);

	        try {
          // Loading map
          initilizeMap();

      } catch (Exception e) {
          e.printStackTrace();
      }

	     // latitude and longitude
	     displayMarkers();
	     centerMapOnMyLocation();
	    }
	 private void centerMapOnMyLocation() {
	       LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);

	         
	        double lat = location.getLatitude();
	        double lng = location.getLongitude();
	        LatLng latLng = new LatLng(lat, lng);
	        myPosition = new LatLng(lat, lng);
	                
	                LatLng coordinate = new LatLng(lat, lng);
	                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
	                googleMap.animateCamera(yourLocation);
		}
	 public void displayMarkers()
	 {
		 List<Event> events = db.getAllEvents();
		 for(int a = 0; a < events.size(); a++)
		 {
	        Address temp = stringToAddress(events.get(a).getAddress());
	        double latitude = temp.getLatitude();
	        double longitude = temp.getLongitude();
	        // create marker
	        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(events.get(a).getName());
	        // adding marker
	        googleMap.addMarker(marker); 
		 }
	 }
	 public Address stringToAddress(String strAddress){
	 Geocoder coder = new Geocoder(this);
	 List<Address> address = null;

			try {
				address = coder.getFromLocationName(strAddress,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	     if (address == null) 
	         return null;
	     Address location = address.get(0);
	     location.getLatitude();
	     location.getLongitude();

	     
	      return location;
	 }
	    public void switchMain(View v){
	    	Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
	    }
	    public void switchMap(View v){
	    	Intent intent = new Intent(this, MapActivity.class);
	    	startActivity(intent);
	    }
	    public void switchAddEvent(View v){
	    	Intent intent = new Intent(this, AddEventActivity.class);
	    	startActivity(intent);
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.map, menu);
	        return true;
	    }

	    
	    private void initilizeMap() {
      if (googleMap == null) {
          googleMap = ((MapFragment)getFragmentManager().findFragmentById(
                  R.id.gMap)).getMap();

          // check if map is created successfully or not
          if (googleMap == null) {
              Toast.makeText(getApplicationContext(),
                      "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                      .show();
          }
      }
  }
  @Override
  protected void onResume() {
      super.onResume();
      initilizeMap();
  }
}
