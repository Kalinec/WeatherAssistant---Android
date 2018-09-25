package com.example.karol.weatherassistant.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.Services.WeatherService;

import org.w3c.dom.Text;


public class WeatherForecast extends Fragment {

    private SearchView _searchViewCity;
    public static TextView City;
    public static TextView Country;
    public static TextView Latitude;
    public static TextView Longitude;
    public static TextView Temperature;
    public static TextView Condition;
    public static TextView Description;
    public static TextView Humidity;
    public static TextView WindSpeed;
    public static TextView Pressure;

    public WeatherForecast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        _searchViewCity = (SearchView) view.findViewById(R.id.searchView);
        City = (TextView) view.findViewById(R.id.textView_City);
        Country = (TextView) view.findViewById(R.id.textView_Country);
        Latitude = (TextView) view.findViewById(R.id.textView_LatitudeValue);
        Longitude = (TextView) view.findViewById(R.id.textView_LongitudeValue);
        Temperature = (TextView) view.findViewById(R.id.textView_temperature);
        Condition = (TextView) view.findViewById(R.id.textView_ConditionValue);
        Description = (TextView) view.findViewById(R.id.textView_descriptionValue);
        Humidity = (TextView) view.findViewById(R.id.textView_humidityValue);
        WindSpeed = (TextView) view.findViewById(R.id.textView_windSpeedValue);
        Pressure = (TextView) view.findViewById(R.id.textView_pressureValue);

        _searchViewCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                WeatherService.getInstance().getCurrentWeatherByCityName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //proba pobierania json
       /* try
        {
            downloadJson();
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"nie pykło",Toast.LENGTH_SHORT).show();
        }
*/
        return view;
    }

    private void downloadJson()
    {
        //WeatherService.getInstance().getCurrentWeatherByCityName(_searchViewCity.get);
    }

}
