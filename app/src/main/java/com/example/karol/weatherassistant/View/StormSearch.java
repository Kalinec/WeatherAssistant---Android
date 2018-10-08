package com.example.karol.weatherassistant.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.karol.weatherassistant.Helpers.WarningAdapter;
import com.example.karol.weatherassistant.Model.CurrentWeather.Warning;
import com.example.karol.weatherassistant.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class StormSearch extends Fragment {

   /* public IWsdl2CodeEvents _eventsHandler = new IWsdl2CodeEvents() {
        @Override
        public void Wsdl2CodeStartedRequest() {

        }

        @Override
        public void Wsdl2CodeFinished(String methodName, Object Data) {
            StormSearch._time.setText(methodName + ": " + Data.toString());
        }

        @Override
        public void Wsdl2CodeFinishedWithException(Exception ex) {
            //Log.e(TAG, "Explanation of what was being attempted with this shit", ex);
        }

        @Override
        public void Wsdl2CodeEndedRequest() {

        }
    };
    private serwerSOAPService _service = new serwerSOAPService(_eventsHandler,"https://burze.dzis.net/soap.php");
    private MyComplexTypeBurza _burzatest = new MyComplexTypeBurza(); */

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
    public static List<Warning> warningList;
    public static WarningAdapter warningAdapter;


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

        warningList = new ArrayList<>();
        warningAdapter = new WarningAdapter(warningList);
        _recyclerView.setAdapter(warningAdapter);
        warningAdapter.notifyDataSetChanged();


        //radius spinner implementation
        RadiusSpinner = (Spinner) view.findViewById(R.id.radius_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.radius_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RadiusSpinner.setAdapter(adapter);
        //szukajBurzy();
        return view;
    }

    private void szukajBurzy()
    {
        try {
          // _service.szukaj_burzyAsync();
        }
        catch (Exception  e){
            Log.e(TAG, "Explanation of what was being attempted", e);
        }

    }

}
