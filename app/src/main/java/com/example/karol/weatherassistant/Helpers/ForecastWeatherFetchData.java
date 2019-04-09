package com.example.karol.weatherassistant.Helpers;

import android.os.AsyncTask;
import android.view.View;

import com.example.karol.weatherassistant.Models.CurrentWeather.Forecast;
import com.example.karol.weatherassistant.Models.CurrentWeather.ForecastWeather.ForecastWeather;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.View.MainActivity;
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
    private ForecastWeather _forecastWeather;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.DownloadProgressBar.setVisibility(View.VISIBLE);
    }

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
        _forecastWeather = gson.fromJson(aVoid, ForecastWeather.class);

        WeatherForecast.ForecastList.clear();
        //update GUI - List of weathers
        for(int i=0; i<_forecastWeather.getList().size(); i++)
        {
            Date date = new Date(_forecastWeather.getList().get(i).getDt()*1000);
            SimpleDateFormat dayFormatter = new SimpleDateFormat("EEE");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
            WeatherForecast.ForecastList.add(
                    new Forecast(
                            dayFormatter.format(date),
                            timeFormatter.format(date),
                            setWeatherIcon(_forecastWeather.getList().get(i).getWeather().get(0).getIcon()),
                            String.valueOf(_forecastWeather.getList().get(i).getMain().getTemp().intValue()),
                            String.format("%.2f",_forecastWeather.getList().get(i).getWind().getSpeed()*3.6)));
        }
        WeatherForecast.ForecastAdapter.notifyDataSetChanged();
        MainActivity.DownloadProgressBar.setVisibility(View.GONE);
    }

    private int setWeatherIcon(String icon)
    {
        switch (icon)
        {
            case "01d":
                return R.drawable.if_sunny;

            case "01n":
                return R.drawable.if_night;

            case "02d":
                return R.drawable.if_cloudy_day;

            case "02n":
                return R.drawable.if_cloudy_night;

            case "03d":
                return R.drawable.if_cloudy;

            case "03n":
                return R.drawable.if_cloudy;

            case "04d":
                return R.drawable.if_cloudy;

            case "04n":
                return R.drawable.if_cloudy;

            case "09d":
                return R.drawable.if_cloudy_rain;

            case "09n":
                return R.drawable.if_cloudy_rain;

            case "10d":
                return R.drawable.if_cloudy_rainy_day;

            case "10n":
                return R.drawable.if_cloudy_rainy_night;

            case "11d":
                return R.drawable.if_cloudy_stormy_day;

            case "11n":
                return R.drawable.if_cloudy_stormy_night;

            case "13d":
                return R.drawable.if_cloudy_snowy_day;

            case "13n":
                return R.drawable.if_cloudy_snowy_night;

            case "50d":
                return R.drawable.if_windy_mist;

            case "50n":
                return R.drawable.if_windy_mist;
        }

        return -1;
    }



}
