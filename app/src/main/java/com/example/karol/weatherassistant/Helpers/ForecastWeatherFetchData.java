package com.example.karol.weatherassistant.Helpers;

import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;

import com.example.karol.weatherassistant.Model.CurrentWeather.Forecast;
import com.example.karol.weatherassistant.Model.CurrentWeather.ForecastWeather.ForecastWeather;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.View.WeatherForecast;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastWeatherFetchData extends AsyncTask<String, Void, String>
{

    @Override
    protected String doInBackground(String... strings)
    {
        String data = "";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();

                if (line != null)
                    data = data + line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String aVoid)
    {
        Gson gson = new Gson();
        ForecastWeather forecastWeather = gson.fromJson(aVoid, ForecastWeather.class);

        //update GUI
        for(int i=0; i<forecastWeather.getList().size(); i++)
        {
            Date date = new Date(forecastWeather.getList().get(i).getDt()*1000);
            SimpleDateFormat dayFormatter = new SimpleDateFormat("dd-MM");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            WeatherForecast.forecastList.add(new Forecast(dayFormatter.format(date), timeFormatter.format(date), R.drawable.ic_weather_cloudy, forecastWeather.getList().get(i).getMain().getTemp().toString()));

        }
        WeatherForecast.forecastAdapter.notifyDataSetChanged();
    }
}
