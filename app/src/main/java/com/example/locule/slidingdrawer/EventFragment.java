package com.example.locule.slidingdrawer;

import com.example.locule.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventFragment extends Fragment {

    public EventFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        //TextView label = (TextView)getView().findViewById(R.id.txtLabel);
        return rootView;
    }
}
