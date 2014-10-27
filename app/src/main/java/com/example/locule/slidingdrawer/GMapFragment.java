package com.example.locule.slidingdrawer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import com.example.locule.AddEventActivity;
import com.example.locule.DatabaseHandler;
import com.example.locule.MainActivity;
import com.example.locule.MapActivity;
import com.example.locule.R;
import com.example.locule.adapter.Event;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
public class GMapFragment extends Fragment {
	 
	MapView mapView;
	GoogleMap map;
	LatLng myPosition;
	DatabaseHandler db;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map, container, false);
 
 		// Gets the MapView from the XML layout and creates it
		mapView = (MapView) v.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
 
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setMyLocationEnabled(true);
 
		MapsInitializer.initialize(this.getActivity());
		
		// Updates the location and zoom opf the MapView

	    map.setMyLocationEnabled(true);
	    centerMapOnMyLocation();
	    displayMarkers();
		return v;
	}
	 private void centerMapOnMyLocation() {
	       LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);

	         
	        double lat = location.getLatitude();
	        double lng = location.getLongitude();
	        LatLng latLng = new LatLng(lat, lng);
	        myPosition = new LatLng(lat, lng);
	                
	                LatLng coordinate = new LatLng(lat, lng);
	                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
	                map.animateCamera(yourLocation);
		}
	 public void displayMarkers()
	 {
		 db = new DatabaseHandler(getActivity());
		 List<Event> events = db.getAllEvents();
		 for(int a = 0; a < events.size(); a++)
		 {
	        Address temp = stringToAddress(events.get(a).getAddress());
	        double latitude = temp.getLatitude();
	        double longitude = temp.getLongitude();
	        // create marker
	        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(events.get(a).getName());
	        // adding marker
	        map.addMarker(marker); 
		 }
	 }
	 public Address stringToAddress(String strAddress){
	 Geocoder coder = new Geocoder(getActivity());
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
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
 
}