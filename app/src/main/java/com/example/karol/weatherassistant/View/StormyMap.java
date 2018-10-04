package com.example.karol.weatherassistant.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.karol.weatherassistant.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StormyMap extends Fragment {


    public StormyMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stormy_map, container, false);
        return view;
    }

}
