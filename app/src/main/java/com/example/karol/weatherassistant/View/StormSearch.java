package com.example.karol.weatherassistant.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.karol.weatherassistant.Helpers.WarningAdapter;
import com.example.karol.weatherassistant.Models.CurrentWeather.Warning;
import com.example.karol.weatherassistant.R;

import java.util.ArrayList;
import java.util.List;

public class StormSearch extends Fragment {

    public static TextView Time;
    public static TextView Number;
    public static TextView Distance;
    public static TextView Direction;
    public static TextView City;
    public static TextView Latitude;
    public static TextView Longitude;
    public static Spinner RadiusSpinner;

    private RecyclerView _recyclerView;
    private RecyclerView.LayoutManager _layoutManager;
    public static List<Warning> WarningList;
    public static WarningAdapter WarningAdapter;


    public StormSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storm_search, container, false);
        City = (TextView) view.findViewById(R.id.textView_CityValue);
        Latitude = (TextView) view.findViewById(R.id.textView_LatitudeValue);
        Longitude = (TextView) view.findViewById(R.id.textView_LongitudeValue);
        Time = (TextView) view.findViewById(R.id.textView_TimeValue);
        Number = (TextView) view.findViewById(R.id.textView_NumberValue);
        Distance = (TextView) view.findViewById(R.id.textView_DistanceValue);
        Direction = (TextView) view.findViewById(R.id.textView_DirectionValue);
        _recyclerView = (RecyclerView) view.findViewById(R.id.warningInfo_recyclerView);
        _layoutManager = new LinearLayoutManager(getActivity());
        _recyclerView.setLayoutManager(_layoutManager);

        if(_recyclerView != null)
            _recyclerView.setHasFixedSize(true);

        WarningList = new ArrayList<>();
        WarningAdapter = new WarningAdapter(WarningList);
        _recyclerView.setAdapter(WarningAdapter);
        WarningAdapter.notifyDataSetChanged();


        //radius spinner implementation
        RadiusSpinner = (Spinner) view.findViewById(R.id.radius_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.radius_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RadiusSpinner.setAdapter(adapter);
        return view;
    }
}
