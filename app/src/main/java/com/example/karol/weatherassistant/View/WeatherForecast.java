package com.example.karol.weatherassistant.View;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karol.weatherassistant.Helpers.ForecastAdapter;
import com.example.karol.weatherassistant.Model.CurrentWeather.Forecast;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.Services.WeatherService;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class WeatherForecast extends Fragment
{

    public static ImageView WeatherIcon;
    public static TextView City;
    public static TextView Country;
    public static TextView Latitude;
    public static TextView Longitude;
    public static TextView Temperature;
    public static TextView Condition;
    public static TextView Description;
    //public static TextView Humidity;
    public static ArcProgress Humidity;
    public static ArcProgress Cloudiness;
    public static TextView WindSpeed;
    public static TextView WindDirection;
    public static TextView Pressure;
    public static TextView Visibility;
    public static TextView Time;
    public static LinearLayout VisibilityInfo;
    public static TextView Sunrise;
    public static TextView Sunset;

    private RecyclerView _recyclerView;
    private RecyclerView.LayoutManager _layoutManager;
    public static List<Forecast> forecastList;
    public static ForecastAdapter forecastAdapter;




    public WeatherForecast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        WeatherIcon = (ImageView) view.findViewById(R.id.imageView_weatherIcon);
        City = (TextView) view.findViewById(R.id.textView_City);
        Country = (TextView) view.findViewById(R.id.textView_Country);
        Latitude = (TextView) view.findViewById(R.id.textView_LatitudeValue);
        Longitude = (TextView) view.findViewById(R.id.textView_LongitudeValue);
        Temperature = (TextView) view.findViewById(R.id.textView_temperature);
        Condition = (TextView) view.findViewById(R.id.textView_condition);
        Description = (TextView) view.findViewById(R.id.textView_description);
        Humidity =  view.findViewById(R.id.arcProgress_humidity);
        Cloudiness = view.findViewById(R.id.arcProgress_cloudiness);
        WindSpeed = (TextView) view.findViewById(R.id.textView_windSpeedValue);
        WindDirection = view.findViewById(R.id.textView_windDirectionValue);
        Pressure = (TextView) view.findViewById(R.id.textView_pressureValue);
        Visibility = view.findViewById(R.id.textView_visibilityValue);
        Time = (TextView) view.findViewById(R.id.textView_updateTimeValue);
        VisibilityInfo = view.findViewById(R.id.Layout_visibilityInfo);
        Sunrise = view.findViewById(R.id.textView_sunriseValue);
        Sunset = view.findViewById(R.id.textView_sunsetValue);
        _recyclerView = view.findViewById(R.id.forecastInfo_recyclerView);
        _layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        _recyclerView.setLayoutManager(_layoutManager);

        Humidity.setBottomText(getString(R.string.word_humidity));
        Humidity.setBottomTextSize(40);
        Humidity.setFinishedStrokeColor(Color.parseColor("#4A65A6"));
        Humidity.setUnfinishedStrokeColor(Color.parseColor("#E0E0E0"));
        Cloudiness.setBottomText(getString(R.string.word_cloudiness));
        Cloudiness.setBottomTextSize(40);
        Cloudiness.setFinishedStrokeColor(Color.parseColor("#4A65A6"));
        Cloudiness.setUnfinishedStrokeColor(Color.parseColor("#E0E0E0"));

        if(_recyclerView != null)
            _recyclerView.setHasFixedSize(true);

        forecastList = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastList);
        _recyclerView.setAdapter(forecastAdapter);
        forecastAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("CITY", City.getText().toString());
        outState.putString("COUNTRY", Country.getText().toString());
        outState.putString("TEMPERATURE", Temperature.getText().toString());
        outState.putString("CONDITION", Condition.getText().toString());
        outState.putString("DESCRIPTION", Description.getText().toString());
        outState.putString("TIME", Time.getText().toString());
        outState.putInt("HUMIDITY", Humidity.getProgress());
        outState.putInt("CLOUDINESS", Cloudiness.getProgress());
        outState.putString("PRESSURE", Pressure.getText().toString());
        outState.putString("VISIBILITY", Visibility.getText().toString());
        outState.putString("WINDSPEED", WindSpeed.getText().toString());
        outState.putString("WINDDIRECTION", WindDirection.getText().toString());
        outState.putString("SUNRISE", Sunrise.getText().toString());
        outState.putString("SUNSET", Sunset.getText().toString());

        outState.putParcelable("FORECASTLIST", _recyclerView.getLayoutManager().onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
        {
            City.setText(savedInstanceState.getString("CITY"));
            Country.setText(savedInstanceState.getString("COUNTRY"));
            Temperature.setText(savedInstanceState.getString("TEMPERATURE"));
            Condition.setText(savedInstanceState.getString("CONDITION"));
            Description.setText(savedInstanceState.getString("DESCRIPTION"));
            Time.setText(savedInstanceState.getString("TIME"));
            Humidity.setProgress(savedInstanceState.getInt("HUMIDITY"));
            Cloudiness.setProgress(savedInstanceState.getInt("CLOUDINESS"));
            Pressure.setText(savedInstanceState.getString("PRESSURE"));
            Visibility.setText(savedInstanceState.getString("VISIBILITY"));
            WindSpeed.setText(savedInstanceState.getString("WINDSPEED"));
            WindDirection.setText(savedInstanceState.getString("WINDDIRECTION"));
            Sunrise.setText(savedInstanceState.getString("SUNRISE"));
            Sunset.setText(savedInstanceState.getString("SUNSET"));

            _recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("FORECASTLIST"));
        }

    }

    @Override
    public void onStart()
    {
        String query;
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPreferences.contains("WEATHERFORECAST_CITY"))
            query = sharedPreferences.getString("WEATHERFORECAST_CITY", "");
        else
            query = "Lublin";

        WeatherService.getInstance().getCurrentWeatherByCityName(query,null);
        WeatherService.getInstance().getForecastWeatherByCityName(query, null);
        super.onStart();
    }
}
