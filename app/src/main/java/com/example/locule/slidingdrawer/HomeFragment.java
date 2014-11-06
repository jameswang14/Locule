package com.example.locule.slidingdrawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.locule.DatabaseHandler;
import com.example.locule.R;
import com.example.locule.adapter.DirectionsJSONParser;
import com.example.locule.adapter.Event;
import com.example.locule.adapter.EventAdapter;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class HomeFragment extends ListFragment {
	
	public HomeFragment(){}
    private ListView listView;
    private ArrayList<Event> eventItems;
    private EventAdapter mAdapter;
    private DatabaseHandler db;
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	db = new DatabaseHandler(getActivity());
    	mAdapter = new EventAdapter(getActivity(),db.getAllEvents());
    }
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        
        return rootView;
    }
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

        List<Event> l = db.getAllEvents();
        for(Event e: l)
            e.proccessLength();
		listView = getListView();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            	Fragment fragment = new EventFragment();
    			FragmentManager fragmentManager = getFragmentManager();
    			fragmentManager.beginTransaction()
    					.replace(R.id.frame_container, fragment).commit();

    		        
            }
        });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener(){
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		final int positionToRemove = position;
        		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        		builder.setTitle("Select an Action");
        		builder.setItems(R.array.event_actions, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch(which)
						{
						case 0:
							break;
						case 1:
							break;
						case 2:
				            db.deleteEvent((Event)listView.getItemAtPosition(positionToRemove));
				            mAdapter.remove((Event)listView.getItemAtPosition(positionToRemove));
				            mAdapter.notifyDataSetChanged();
				            break;
				        default:
				            break;
						}
						
					}

				});
				AlertDialog alert = builder.create();
				alert.show();
        		return true;
        	}
        });}
	   /**    LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);

	        double lat = location.getLatitude();
	        double lng = location.getLongitude();
		// Getting URL to the Google Directions API
		 List<Event> events = db.getAllEvents();
	       	Event temp = events.get(0);
	        double latitude = temp.stringToLat(getActivity());
	        double longitude = temp.stringToLong(getActivity());
		String link = getDirectionsUrl(lat, lng, latitude, longitude);				
		
		DownloadTask downloadTask = new DownloadTask();
		
		// Start downloading json data from Google Directions API
		downloadTask.execute(link); 
        }
	private String getDirectionsUrl(double startLat, double startLong, double endLat, double endLong){
		
		// Origin of route
		String str_origin = "origin="+startLat+","+startLong;
		
		// Destination of route
		String str_dest = "destination="+endLat+","+endLong;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			//PolylineOptions lineOptions = null;
			//MarkerOptions markerOptions = new MarkerOptions();
			String distance = "";
			String duration = "";
			
			
			
			if(result.size()<1){
				Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
				
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				//points = new ArrayList<LatLng>();
				//lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	
					
					if(j==0){	// Get distance from the list
						distance = (String)point.get("distance");						
						continue;
					}else if(j==1){ // Get duration from the list
						duration = (String)point.get("duration");
						continue;
					}
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				//lineOptions.addAll(points);
				//lineOptions.width(2);
				//lineOptions.color(Color.RED);	
				
			}
			
			Log.d("Test", "\n\n" + duration + "\n\n");

			
			// Drawing polyline in the Google Map for the i-th route
			//map.addPolyline(lineOptions);			
		}			
    }   
}
    
    
	

