package com.example.locule.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.locule.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
	Context context;
    public EventAdapter(Context context, List<Event> objects) {
        super(context, R.layout.list_event, objects);
        this.context = context;
    }
   private class ViewHolder
   {
	        TextView txtMenuTitle;
	        TextView txtMenuDesc;
	        TextView txtTime;
            TextView txtTravelTime;
   }
   public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
       Event event = getItem(position);

       LayoutInflater mInflater = (LayoutInflater) context
               .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
       if (convertView == null) {
           convertView = mInflater.inflate(R.layout.list_event, null);
           holder = new ViewHolder();
           holder.txtMenuTitle = (TextView) convertView.findViewById(R.id.title);
           holder.txtMenuDesc = (TextView) convertView.findViewById(R.id.description);
           holder.txtTime = (TextView) convertView.findViewById(R.id.time);
           holder.txtTravelTime=(TextView)convertView.findViewById(R.id.travel_time);
           convertView.setTag(holder);
       } else
           holder = (ViewHolder) convertView.getTag();

       holder.txtMenuDesc.setText(event.getDescription());
       holder.txtMenuTitle.setText(event.getName());
       holder.txtTime.setText("Due in " + String.valueOf(event.getTimeString()));
       holder.txtTravelTime.setText("Travel Time " + event.getTravelTime());


       return convertView;
   }


	
}
