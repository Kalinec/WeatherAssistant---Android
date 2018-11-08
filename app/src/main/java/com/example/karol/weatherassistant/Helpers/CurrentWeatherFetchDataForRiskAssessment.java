package com.example.karol.weatherassistant.Helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.CurrentWeather;
import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.Main;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.View.MainActivity;
import com.example.karol.weatherassistant.View.PlanTheTrip;
import com.google.gson.Gson;
import com.mapbox.api.directions.v5.DirectionsCriteria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrentWeatherFetchDataForRiskAssessment extends AsyncTask<ArrayList<String>, Void, CurrentWeatherDataForRiskAssessmentWrapper>
{
    String criteriaType;
    CurrentWeatherDataForRiskAssessmentWrapper wrapper;
    List<List<Integer>> listOfWeatherConditionsRisk;

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        MainActivity.DownloadProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected CurrentWeatherDataForRiskAssessmentWrapper doInBackground(ArrayList<String>... strings)
    {
        ArrayList<String> parameters = strings[0];
        criteriaType = parameters.get(parameters.size()-1);

        wrapper = new CurrentWeatherDataForRiskAssessmentWrapper();
        wrapper.jsonRespones = new String[parameters.size()-1];

        for(int i=0; i<parameters.size() - 1; i++)
        {
            wrapper.jsonRespones[i] = fetchData(parameters.get(i));
        }

        return wrapper;
    }

    @Override
    protected void onPostExecute(CurrentWeatherDataForRiskAssessmentWrapper w)
    {
        Gson gson = new Gson();
        ArrayList<CurrentWeather> currentWeathers = new ArrayList<>();
        initializeListOfWeatherConditionsRisk();

        //risk level: 0-none 1-moderate 2-high
        int temperatureRiskLevel = -1;
        Double minTemperature = null;
        Double maxTemperature = null;

        int weatherConditionRiskLevel = -1;
        String weatherConditionDescription = "";

        int cloudinessRiskLevel = -1;
        Double minCloudiness = null;
        Double maxCloudiness = null;

        int windSpeedRiskLevel = -1;
        Double minWindSpeed = null;
        Double maxWindSpeed = null;

        int visibilityRiskLevel = -1;
        Double minVisibility = null;
        Double maxVisibility = null;

        for(int i=0; i<w.jsonRespones.length; i++)
        {
            currentWeathers.add(gson.fromJson(w.jsonRespones[i], CurrentWeather.class));

            minTemperature = getMinimumValue(minTemperature, currentWeathers.get(i).getMain().getTemp());
            maxTemperature = getMaximumValue(maxTemperature, currentWeathers.get(i).getMain().getTemp());

            try
            {
                minCloudiness = getMinimumValue(minCloudiness, currentWeathers.get(i).getClouds().getAll());
                maxCloudiness = getMaximumValue(maxCloudiness, currentWeathers.get(i).getClouds().getAll());
            }
            catch (NullPointerException e)
            {
                Log.i("FetchDataRiskAssessment","Cloudiness value is null");
            }

            minWindSpeed = getMinimumValue(minWindSpeed, currentWeathers.get(i).getWind().getSpeed());
            maxWindSpeed = getMaximumValue(maxWindSpeed, currentWeathers.get(i).getWind().getSpeed());

            try
            {
                minVisibility = getMinimumValue(minVisibility, currentWeathers.get(i).getVisibility());
                maxVisibility = getMaximumValue(maxVisibility, currentWeathers.get(i).getVisibility());
            }
            catch (NullPointerException e)
            {
                Log.i("FetchDataRiskAssessment","Visibility value is null");
            }

            int oldWeatherConditionRiskLevel =  weatherConditionRiskLevel;
            //if(weatherConditionDescription.equals(""))
            //    weatherConditionDescription = currentWeathers.get(i).getWeather().get(0).getDescription();

            weatherConditionRiskLevel = getWorstWeatherConditionLevel(weatherConditionRiskLevel, currentWeathers.get(i).getWeather().get(0).getId(), criteriaType);

            //if((oldWeatherConditionRiskLevel != weatherConditionRiskLevel) && oldWeatherConditionRiskLevel != -1)
            if((oldWeatherConditionRiskLevel != weatherConditionRiskLevel))
                    weatherConditionDescription = currentWeathers.get(i).getWeather().get(0).getDescription();
        }

        if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
        {
            temperatureRiskLevel = getTemperatureRiskLevelForWalking(minTemperature, maxTemperature);
            cloudinessRiskLevel = getCloudinessRiskLevelForWalkingAndCycling(maxCloudiness);
            windSpeedRiskLevel = getWindSpeedRiskLevelForWalking(maxWindSpeed);
            visibilityRiskLevel = getVisibilityRiskLevelForWalking(maxVisibility);
        }

        else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
        {
            temperatureRiskLevel = getTemperatureRiskLevelForCycling(minTemperature, maxTemperature);
            cloudinessRiskLevel = getCloudinessRiskLevelForWalkingAndCycling(maxCloudiness);
            windSpeedRiskLevel = getWindSpeedRiskLevelForCycling(maxWindSpeed);
            visibilityRiskLevel = getVisibilityRiskLevelForCycling(maxVisibility);
        }

        PlanTheTrip._imageWindSpeedRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._textWindSpeedRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._imageWindSpeedRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._textWindSpeedRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._imageVisibilityRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._textVisibilityRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._textCloudinessRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._imageCloudinessRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._imageWeatherConditionRisk.setVisibility(View.VISIBLE);
        PlanTheTrip._textWeatherConditionRisk.setVisibility(View.VISIBLE);

        switch (temperatureRiskLevel)
        {
            case 0:
                PlanTheTrip._textTemperatureRisk.setText(
                        MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_temperature_walking_and_cycle) +
                                " " +
                                minTemperature +
                                "-" +
                                maxTemperature +
                                "°C");
                PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_not);
                break;

            case 1:
                PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_temperature_walking_and_cycle) +
                        " " +
                        minTemperature +
                        "-" +
                        maxTemperature +
                        "°C");
                PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_moderate);
                break;

            case 2:
                PlanTheTrip._textTemperatureRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_temperature_walking_and_cycle) +
                        " " +
                        minTemperature +
                        "-" +
                        maxTemperature +
                        "°C");
                PlanTheTrip._imageTemperatureRisk.setImageResource(R.drawable.if_risk_high);
                break;

            case -1:
                PlanTheTrip._textTemperatureRisk.setVisibility(View.GONE);
                PlanTheTrip._imageTemperatureRisk.setVisibility(View.GONE);

        }

        switch (windSpeedRiskLevel)
        {
            case 0:
                PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_wind_speed_walking_and_cycle) +
                " " +
                minWindSpeed * 3.6 +
                "-" +
                maxWindSpeed * 3.6 +
                "km/h");
                PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_not);
                break;

            case 1:
                PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_wind_speed_walking_and_cycle)+
                        " " +
                        minWindSpeed * 3.6 +
                        "-" +
                        maxWindSpeed * 3.6 +
                        "km/h");
                PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_moderate);
                break;

            case 2:
                PlanTheTrip._textWindSpeedRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_wind_speed_walking_and_cycle)+
                        " " +
                        minWindSpeed * 3.6 +
                        "-" +
                        maxWindSpeed * 3.6 +
                        "km/h");
                PlanTheTrip._imageWindSpeedRisk.setImageResource(R.drawable.if_risk_high);
                break;

            case -1:
                PlanTheTrip._imageWindSpeedRisk.setVisibility(View.GONE);
                PlanTheTrip._textWindSpeedRisk.setVisibility(View.GONE);
        }

        switch (visibilityRiskLevel)
        {
            case 0:
                PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_visibility_walking_and_cycle) +
                " " +
                minVisibility / 1000 +
                "-" +
                maxVisibility / 1000 +
                "km");
                PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_not);
                break;

            case 1:
                PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_visibility_walking_and_cycle)+
                        " " +
                        minVisibility / 1000 +
                        "-" +
                        maxVisibility / 1000 +
                        "km");
                PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_moderate);
                break;

            case 2:
                PlanTheTrip._textVisibilityRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_visibility_walking_and_cycle)+
                        " " +
                        minVisibility / 1000 +
                        "-" +
                        maxVisibility / 1000 +
                        "km");
                PlanTheTrip._imageVisibilityRisk.setImageResource(R.drawable.if_risk_high);
                break;

            case -1:
                PlanTheTrip._imageVisibilityRisk.setVisibility(View.GONE);
                PlanTheTrip._textVisibilityRisk.setVisibility(View.GONE);
        }

        switch (cloudinessRiskLevel)
        {
            case 0:
                PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_none_cloudiness_walking_and_cycle)+
                        " " +
                        minCloudiness +
                        "-" +
                        maxCloudiness +
                        "%");
                PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_not);
                break;
            case 1:
                PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_moderate_cloudiness_walking_and_cycle)+
                        " " +
                        minCloudiness +
                        "-" +
                        maxCloudiness +
                        "%");
                PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_moderate);
                break;
            case 2:
                PlanTheTrip._textCloudinessRisk.setText(MainActivity.resources.getString(R.string.PlanTheTrip_risk_high_cloudiness_walking_and_cycle)+
                        " " +
                        minCloudiness +
                        "-" +
                        maxCloudiness +
                        "%");
                PlanTheTrip._imageCloudinessRisk.setImageResource(R.drawable.if_risk_high);
                break;
            case -1:
                PlanTheTrip._textCloudinessRisk.setVisibility(View.GONE);
                PlanTheTrip._imageCloudinessRisk.setVisibility(View.GONE);
                break;
        }

        switch (weatherConditionRiskLevel)
        {
            case 0:
                PlanTheTrip._imageWeatherConditionRisk.setImageResource(R.drawable.if_risk_not);
                PlanTheTrip._textWeatherConditionRisk.setText(weatherConditionDescription);
                break;

            case 1:
                PlanTheTrip._imageWeatherConditionRisk.setImageResource(R.drawable.if_risk_moderate);
                PlanTheTrip._textWeatherConditionRisk.setText(weatherConditionDescription);
                break;

            case 2:
                PlanTheTrip._imageWeatherConditionRisk.setImageResource(R.drawable.if_risk_high);
                PlanTheTrip._textWeatherConditionRisk.setText(weatherConditionDescription);
                break;

            case -1:
                PlanTheTrip._imageWeatherConditionRisk.setVisibility(View.GONE);
                PlanTheTrip._textWeatherConditionRisk.setVisibility(View.GONE);
                break;
        }

        MainActivity.DownloadProgressBar.setVisibility(View.GONE);
    }

    private String fetchData(String Url)
    {
        String data = "";
        try {
            URL url = new URL(Url);
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

    private int getTemperatureRiskLevelForWalking(Double minTemperature, Double maxTemperature)
    {
        int riskLevel = -1;

        if((minTemperature >= -5 && minTemperature <= 29) || (maxTemperature >= -5 && maxTemperature <= 29))
        {
            if(riskLevel < 0)
                riskLevel = 0;
        }
        else if(((minTemperature >= -15 && minTemperature < -5) || (minTemperature >= 30 && minTemperature < 34 )) ||
                ((maxTemperature >= -15 && maxTemperature < -5) || (maxTemperature >= 30 && maxTemperature < 34 )))
        {
            if(riskLevel < 1)
                riskLevel = 1;
        }
        else if((minTemperature >= 34) || (maxTemperature >= 34))
        {
            riskLevel = 2;
        }

        return riskLevel;
    }

    private int getTemperatureRiskLevelForCycling(Double minTemperature, Double maxTemperature)
    {
        int riskLevel = -1;

        if((minTemperature >= 0 && minTemperature <= 29) || (maxTemperature >= 0 && maxTemperature <= 29))
        {
            if(riskLevel < 0)
                riskLevel = 0;
        }
        else if(((minTemperature >= 30 && minTemperature <= 34) || (maxTemperature >= 30 && maxTemperature <= 34 )))
        {
            if(riskLevel < 1)
                riskLevel = 1;
        }
        else if(((minTemperature <= 0) || (minTemperature > 34)) || ((maxTemperature <= 0) || (maxTemperature > 34)))
        {
            riskLevel = 2;
        }

        return riskLevel;
    }

    private int getCloudinessRiskLevelForWalkingAndCycling(Double maxCloudiness)
    {
        if(maxCloudiness != null)
        {
            if(maxCloudiness >= 84)
                return 2;

            else if(maxCloudiness >= 60 && maxCloudiness <= 83)
                return 1;

            else if(maxCloudiness <= 59)
                return 0;
        }
        return -1;
    }

    private int getWindSpeedRiskLevelForWalking(Double maxWindSpeed)
    {
        double maxWindSpeedInKilometers = maxWindSpeed * 3.6;

        if (maxWindSpeedInKilometers <= 58)
            return 0;

        else if(maxWindSpeedInKilometers >= 59 && maxWindSpeedInKilometers <= 69)
            return 1;

        else if(maxWindSpeedInKilometers >= 70)
            return 2;

        return -1;
    }

    private int getWindSpeedRiskLevelForCycling(Double maxWindSpeed)
    {
        double maxWindSpeedInKilometers = maxWindSpeed * 3.6;

        if (maxWindSpeedInKilometers <= 48)
            return 0;

        else if(maxWindSpeedInKilometers >= 49 && maxWindSpeedInKilometers <= 59)
            return 1;

        else if(maxWindSpeedInKilometers >= 60)
            return 2;

        return -1;
    }

    private int getVisibilityRiskLevelForWalking(Double maxVisibility)
    {
        if(maxVisibility != null)
        {
            if(maxVisibility < 50)
                return 2;

            else if(maxVisibility >= 50 && maxVisibility <= 200)
                return 1;

            else if(maxVisibility > 200)
                return 0;
        }
        return -1;
    }

    private int getVisibilityRiskLevelForCycling(Double maxVisibility)
    {
        if(maxVisibility != null)
        {
            if(maxVisibility < 50)
                return 2;

            else if(maxVisibility >= 50 && maxVisibility <= 200)
                return 1;

            else if(maxVisibility > 200)
                return 0;
        }
        return -1;
    }

    private double getMinimumValue(Double currentValue, double valueToCheck)
    {
        if(currentValue == null || currentValue > valueToCheck)
            return valueToCheck;
        return currentValue;
    }

    private double getMaximumValue(Double currentValue, double valueToCheck)
    {
        if(currentValue == null || currentValue < valueToCheck)
            return valueToCheck;
        return currentValue;
    }

    private int getWorstWeatherConditionLevel(int currentRiskValue, int idValueToCheck, String criteriaType)
    {
       // if(currentRiskValue == -1)
        //    return idValueToCheck;


          //  int currentRiskLevel = -1;
           // String currentConditionDescription;
            int toCheckRiskLevel = -1;
           // String toCheckConditionDescription;

            for(List<Integer> list : listOfWeatherConditionsRisk)
            {
              /*  if(list.contains(currentRiskValue))
                {
                    if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
                        currentRiskLevel = list.get(1);

                    else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
                        currentRiskLevel = list.get(2);
                } */

                if(list.contains(idValueToCheck))
                {
                    if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
                        toCheckRiskLevel = list.get(1);
                    else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
                        toCheckRiskLevel = list.get(2);
                }
            }

            if(currentRiskValue < toCheckRiskLevel)
                return toCheckRiskLevel;

            return currentRiskValue;

    }

    private void initializeListOfWeatherConditionsRisk()
    {
        //first column - Condition ID; second column - walking risk level; third column - cycling risk level
        // 0 - none risk level; 1 - moderate risk level; 2 - high risk level

        ///Group 2xx: Thunderstorm
        listOfWeatherConditionsRisk = new ArrayList<List<Integer>>();
        listOfWeatherConditionsRisk.add(Arrays.asList(200, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(201, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(202, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(210, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(211, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(212, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(221, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(230, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(231, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(232, 2, 2));

        ///Group 3xx: Drizzle
        listOfWeatherConditionsRisk.add(Arrays.asList(300, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(301, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(302, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(310, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(311, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(312, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(313, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(314, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(321, 2, 2));

        ///Group 5xx: Rain
        listOfWeatherConditionsRisk.add(Arrays.asList(500, 0, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(501, 1, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(502, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(503, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(504, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(511, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(520, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(521, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(522, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(531, 2, 2));

        ///Group 6xx: Snow
        listOfWeatherConditionsRisk.add(Arrays.asList(600, 1, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(601, 1, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(602, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(611, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(612, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(615, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(616, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(620, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(621, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(622, 2, 2));

        ///Group 7xx: Atmosphere
        listOfWeatherConditionsRisk.add(Arrays.asList(701, 0, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(711, 0, 0));
        listOfWeatherConditionsRisk.add(Arrays.asList(721, 1, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(731, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(741, 1, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(751, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(761, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(762, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(771, 2, 2));
        listOfWeatherConditionsRisk.add(Arrays.asList(781, 2, 2));

        ///Group 800: Clear
        listOfWeatherConditionsRisk.add(Arrays.asList(800, 0, 0));

        ///Group 80x: Clouds
        listOfWeatherConditionsRisk.add(Arrays.asList(801, 0, 0));
        listOfWeatherConditionsRisk.add(Arrays.asList(802, 0, 0));
        listOfWeatherConditionsRisk.add(Arrays.asList(803, 1, 1));
        listOfWeatherConditionsRisk.add(Arrays.asList(804, 2, 2));
    }
}
