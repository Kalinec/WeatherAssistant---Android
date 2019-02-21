package com.example.karol.weatherassistant.Helpers;

import android.os.AsyncTask;
import android.view.View;

import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.CurrentWeather;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CurrentWeatherFetchData extends AsyncTask<String, Void, String> {
    //String viewPagerSelectedFragment;
    CurrentWeather currentWeather;

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        MainActivity.DownloadProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
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
    protected void onPostExecute(String aVoid) {
        Gson gson = new Gson();
        currentWeather = gson.fromJson(aVoid, CurrentWeather.class);
        updateWeatherForecastFragment();
        MainActivity.DownloadProgressBar.setVisibility(View.GONE);
    }

    private void updateWeatherForecastFragment()
    {
        //update GUI
        WeatherForecast.City.setText(currentWeather.getName());
        WeatherForecast.Country.setText(currentWeather.getSys().getCountry());

        switch (currentWeather.getWeather().get(0).getIcon()) {
            case "01d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_sunny);
                break;

            case "01n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_night);
                break;

            case "02d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_day);
                break;

            case "02n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_night);
                break;

            case "03d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy);
                break;

            case "03n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy);
                break;

            case "04d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy);
                break;

            case "04n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy);
                break;

            case "09d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_rain);
                break;

            case "09n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_rain);
                break;

            case "10d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_rainy_day);
                break;

            case "10n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_rainy_night);
                break;

            case "11d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_stormy_day);
                break;

            case "11n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_stormy_night);
                break;

            case "13d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_snowy_day);
                break;

            case "13n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_cloudy_snowy_night);
                break;

            case "50d":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_windy_mist);
                break;

            case "50n":
                WeatherForecast.WeatherIcon.setImageResource(R.drawable.if_windy_mist);
                break;
        }

        WeatherForecast.Time.setText(new SimpleDateFormat("HH.mm").format(Calendar.getInstance().getTime()));
        WeatherForecast.Temperature.setText(String.valueOf(currentWeather.getMain().getTemp().intValue()));
        if(Locale.getDefault().getLanguage().equals("en"))
            WeatherForecast.Condition.setText(currentWeather.getWeather().get(0).getMain());
        else if(Locale.getDefault().getLanguage().equals("pl"))
            WeatherForecast.Condition.setText(englishToPolishCondition(currentWeather.getWeather().get(0).getMain()));
        WeatherForecast.Description.setText(currentWeather.getWeather().get(0).getDescription());
        WeatherForecast.Humidity.setProgress(currentWeather.getMain().getHumidity().intValue());
        WeatherForecast.Cloudiness.setProgress(currentWeather.getClouds().getAll().intValue());
        WeatherForecast.WindSpeed.setText(String.format("%.0f", currentWeather.getWind().getSpeed()*3.6));
        WeatherForecast.Pressure.setText(String.valueOf(currentWeather.getMain().getPressure().intValue()));

        try
        {
            WeatherForecast.WindDirection.setText(degressToDirection(currentWeather.getWind().getDeg()));
        }
        catch (NullPointerException e)
        {
            WeatherForecast.WindDirection.setText(" - ");
        }

        try
        {
            Double visibilityKilometers = currentWeather.getVisibility() / 1000;
            WeatherForecast.Visibility.setText(String.format("%.2f", visibilityKilometers));
            WeatherForecast.VisibilityInfo.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            WeatherForecast.VisibilityInfo.setVisibility(View.INVISIBLE);
        }

        WeatherForecast.Sunrise.setText(getDateFromSeconds(currentWeather.getSys().getSunrise()));
        WeatherForecast.Sunset.setText(getDateFromSeconds(currentWeather.getSys().getSunset()));
    }

    private String getDateFromSeconds(long seconds)
    {
        Date date = new Date(seconds*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    private String degressToDirection(double x)
    {
        String directions[] =
                {
                        MainActivity.Resources.getString(R.string.coord_N),
                        MainActivity.Resources.getString(R.string.coord_NE),
                        MainActivity.Resources.getString(R.string.coord_E),
                        MainActivity.Resources.getString(R.string.coord_SE),
                        MainActivity.Resources.getString(R.string.coord_S),
                        MainActivity.Resources.getString(R.string.coord_SW),
                        MainActivity.Resources.getString(R.string.coord_W),
                        MainActivity.Resources.getString(R.string.coord_NW),
                        MainActivity.Resources.getString(R.string.coord_N),

                };
        return directions[ (int)Math.round((  ((double)x % 360) / 45)) ];
    }

    private String englishToPolishCondition(String condition)
    {
        switch (condition)
        {
            case "Thunderstorm":
                return "Burza";

            case "Drizzle":
                return "Mżawka";

            case "Rain":
                return "Deszcz";

            case "Snow":
                return "Śnieg";

            case "Atmosphere":
                return "Aura";

            case "Clear":
                return "Czyste niebo";

            case "Clouds":
                return "Chmury";

            case "Mist":
                return "Mglisto";

            case "Fog":
                return "Mglisto";

            default:
                return "zmienili Ci API KAROL";
        }
    }
}
