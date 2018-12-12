package com.example.karol.weatherassistant.Helpers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.CurrentWeather;
import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.Weather;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.View.MainActivity;
import com.example.karol.weatherassistant.View.PlanTheTrip;
import com.example.karol.weatherassistant.View.WeatherForecast;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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

        //switch (viewPagerSelectedFragment)
       // {
         //   case "0":
                updateWeatherForecastFragment();
          //      break;

          /*  case "3":

                //set cloudiness risk
                try
                {
                    Double cloudiness = currentWeather.getClouds().getAll();
                    if(cloudiness <= 59)
                    {
                        PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_cloudiness_walking_and_cycle));
                        PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_not);

                    }
                    else if(cloudiness >= 60 && cloudiness <= 83)
                    {
                        PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_cloudiness_walking_and_cycle));
                        PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_moderate);
                    }
                    else if(cloudiness >= 84)
                    {
                        PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_cloudiness_walking_and_cycle));
                        PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_high);
                    }

                    PlanTheTrip._imageCloudinessRisk.setVisibility(View.VISIBLE);
                    PlanTheTrip._textCloudinessRisk.setVisibility(View.VISIBLE);
                }
                catch (NullPointerException e)
                {
                    Log.i("CurrentWeatherFetchData", "getClouds is null");
                    PlanTheTrip._imageCloudinessRisk.setVisibility(View.GONE);
                    PlanTheTrip._textCloudinessRisk.setVisibility(View.GONE);
                }


                if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
                {
                    //set temperature risk
                    Double temperature = currentWeather.getMain().getTemp();
                    if(temperature >= -5 && temperature <= 29)
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_temperature_walking_and_cycle) + " " + currentWeather.getMain().getTemp());
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_not);
                    }
                    else if((temperature >= -15 && temperature < -5) || (temperature >= 30 && temperature < 34 ))
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_temperature_walking_and_cycle));
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_moderate);
                    }
                    else if(temperature >= 34)
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_temperature_walking_and_cycle));
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_high);
                    }

                    //set wind speed risk
                    Double windSpeed = currentWeather.getWind().getSpeed() * 3.6;
                    if(windSpeed >= 0 && windSpeed <= 58)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_not);
                    }
                    else if(windSpeed >= 59 && windSpeed <= 69)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_moderate);
                    }

                    else if(windSpeed >= 70)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_high);
                    }

                    try
                    {
                        Double visibility = currentWeather.getVisibility();
                        if(visibility > 200)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_not);
                        }
                        else if(visibility >= 50 && visibility <= 200)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_moderate);
                        }
                        else if(visibility < 50)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_high);
                        }

                        PlanTheTrip._textVisibilityRisk.setVisibility(View.VISIBLE);
                        PlanTheTrip._imageVisibilityRisk.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Log.d("Visibility", "Not exist");
                        PlanTheTrip._textVisibilityRisk.setVisibility(View.GONE);
                        PlanTheTrip._imageVisibilityRisk.setVisibility(View.GONE);
                    }

                }
                else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
                {
                    //set temperature risk
                    Double temperature = currentWeather.getMain().getTemp();
                    if(temperature > 0 && temperature <= 29)
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_temperature_walking_and_cycle) + " " + currentWeather.getMain().getTemp());
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_not);
                    }
                    else if(temperature >= 30 && temperature <= 34)
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_temperature_walking_and_cycle));
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_moderate);
                    }
                    else if((temperature <= 0) || (temperature > 34))
                    {
                        PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_temperature_walking_and_cycle));
                        PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_high);
                    }

                    //set wind speed risk
                    Double windSpeed = currentWeather.getWind().getSpeed() * 3.6; // m/s to km/h
                    if(windSpeed >= 0 && windSpeed <= 48)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_not);
                    }
                    else if(windSpeed >= 49 && windSpeed <= 59)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_moderate);
                    }
                    else if(windSpeed >= 60)
                    {
                        PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_wind_speed_walking_and_cycle));
                        PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_high);
                    }

                    try
                    {
                        Double visibility = currentWeather.getVisibility();
                        if(visibility > 1000)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_not);
                        }
                        else if(visibility >= 200 && visibility <= 1000)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_moderate);
                        }
                        else if(visibility < 200)
                        {
                            PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_visibility_walking_and_cycle));
                            PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_high);
                        }

                        PlanTheTrip._textVisibilityRisk.setVisibility(View.VISIBLE);
                        PlanTheTrip._imageVisibilityRisk.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Log.d("Visibility", "Not exist");
                        PlanTheTrip._textVisibilityRisk.setVisibility(View.GONE);
                        PlanTheTrip._imageVisibilityRisk.setVisibility(View.GONE);
                    }
                }
                break; */
        MainActivity.DownloadProgressBar.setVisibility(View.GONE);
    }

    private void updateWeatherForecastFragment()
    {
        //update GUI
        WeatherForecast.City.setText(currentWeather.getName());
        WeatherForecast.Country.setText(currentWeather.getSys().getCountry());
//        WeatherForecast.Latitude.setText(currentWeather.getCoord().getLat().toString());
        //  WeatherForecast.Longitude.setText(currentWeather.getCoord().getLon().toString());

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
        // WeatherForecast.WindSpeed.setText(String.valueOf(currentWeather.getWind().getSpeed()*3.6));
        WeatherForecast.Pressure.setText(currentWeather.getMain().getPressure().toString());

        try
        {
            WeatherForecast.WindDirection.setText(degressToDirection(currentWeather.getWind().getDeg()));
        } catch (NullPointerException e)
        {
            WeatherForecast.WindDirection.setText(" - ");
        }

        try
        {
            Double visibilityKilometers = currentWeather.getVisibility() / 1000;
            WeatherForecast.Visibility.setText(String.format("%.2f", visibilityKilometers));
            WeatherForecast.VisibilityInfo.setVisibility(View.VISIBLE);
        } catch (Exception e) {
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
                        MainActivity.resources.getString(R.string.coord_N),
                        MainActivity.resources.getString(R.string.coord_NE),
                        MainActivity.resources.getString(R.string.coord_E),
                        MainActivity.resources.getString(R.string.coord_SE),
                        MainActivity.resources.getString(R.string.coord_S),
                        MainActivity.resources.getString(R.string.coord_SW),
                        MainActivity.resources.getString(R.string.coord_W),
                        MainActivity.resources.getString(R.string.coord_NW),
                        MainActivity.resources.getString(R.string.coord_N),

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
