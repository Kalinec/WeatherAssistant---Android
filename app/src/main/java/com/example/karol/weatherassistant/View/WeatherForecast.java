package com.example.karol.weatherassistant.View;


import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.Services.WeatherService;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.w3c.dom.Text;


public class WeatherForecast extends Fragment {

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

        Humidity.setBottomText(getString(R.string.word_humidity));
        Humidity.setBottomTextSize(40);
        Humidity.setFinishedStrokeColor(Color.parseColor("#4A65A6"));
        Humidity.setUnfinishedStrokeColor(Color.parseColor("#E0E0E0"));
        Cloudiness.setBottomText(getString(R.string.word_cloudiness));
        Cloudiness.setBottomTextSize(40);
        Cloudiness.setFinishedStrokeColor(Color.parseColor("#4A65A6"));
        Cloudiness.setUnfinishedStrokeColor(Color.parseColor("#E0E0E0"));

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
