package com.example.karol.weatherassistant.Helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.karol.weatherassistant.Model.CurrentWeather.CurrentWeather.CurrentWeather;
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

public class CurrentWeatherFetchDataForRiskAssessment extends AsyncTask<String, Void, CurrentWeatherDataForRiskAssessmentWrapper>
{
    String viewPagerSelectedFragment;
    String criteriaType;
    CurrentWeatherDataForRiskAssessmentWrapper wrapper;
    List<List<Integer>> listOfWeatherConditionsRisk;
   /* List<Integer> noRiskConditionsIdsWalking;
    List<Integer> moderateRiskConditionsIdsWalking;
    List<Integer> highRiskConditionsIdsWalking;
    List<Integer> noRiskConditionsIdsCycling;
    List<Integer> moderateRiskConditionsIdsCycling;
    List<Integer> highRiskConditionsIdsCycling; */

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        MainActivity.DownloadProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected CurrentWeatherDataForRiskAssessmentWrapper doInBackground(String... strings)
    {
        viewPagerSelectedFragment = strings[1];
        criteriaType = strings[2];

        wrapper = new CurrentWeatherDataForRiskAssessmentWrapper();
        wrapper.jsonRespones = new String[strings.length - 1];

        for(int i=0; i<strings.length-2; i++)
        {
            wrapper.jsonRespones[i] = fetchData(strings[i]);
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
            if(weatherConditionDescription.equals(""))
                weatherConditionDescription = currentWeathers.get(i).getWeather().get(0).getDescription();

            weatherConditionRiskLevel = getWorstWeatherConditionLevel(weatherConditionRiskLevel, currentWeathers.get(i).getWeather().get(0).getId(), criteriaType);

            if((oldWeatherConditionRiskLevel != weatherConditionRiskLevel) && oldWeatherConditionRiskLevel != -1)
                weatherConditionDescription = currentWeathers.get(i).getWeather().get(0).getDescription();
        }

        if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
        {
            temperatureRiskLevel = getTemperatureRiskLevelForWalking(minTemperature, maxTemperature);
        }

        else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
        {

        }

        //set cloudiness risk

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

    private int getCloudinessRiskLevelForWalkingAndCycling(Double minCloudiness, Double maxCloudiness)
    {
        int riskLevel = -1;

        if(minCloudiness != null && maxCloudiness != null)
        {

            if((minCloudiness <= 59) || (maxCloudiness <= 59))
            {
                if(riskLevel < 0)
                    riskLevel = 0;
            }

            else if((minCloudiness >= 60 && minCloudiness <= 83) || (maxCloudiness >= 60 && maxCloudiness <= 83))
            {
                if(riskLevel < 1)
                    riskLevel = 1;
            }

            else if((minCloudiness >= 84) || (maxCloudiness >= 84))
            {
                if(riskLevel < 2)
                    riskLevel = 2;
            }
        }

        return riskLevel;
    }

    private int getWindSpeedRiskLevelForWalking(Double minWindSpeed, Double maxWindSpeed)
    {
        int riskLevel = -1;

        double minWindSpeedInKilometers = minWindSpeed * 3.6;
        double maxWindSpeedInKilometers = maxWindSpeed * 3.6;

        if((minWindSpeedInKilometers >= 0 && minWindSpeedInKilometers <= 58) ||
                (maxWindSpeedInKilometers >= 0 && maxWindSpeedInKilometers <= 58))
        {
            if(riskLevel < 0)
                riskLevel = 0;
        }

        else if((minWindSpeedInKilometers >= 59 && minWindSpeedInKilometers <= 69) ||
                (maxWindSpeedInKilometers >= 59 && maxWindSpeedInKilometers <= 69))
        {
            if(riskLevel < 1)
                riskLevel = 1;
        }

        else if((minWindSpeedInKilometers >= 70) || (maxWindSpeedInKilometers >= 70))
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

    private int getWorstWeatherConditionLevel(int currentValue, int valueToCheck, String criteriaType)
    {
        if(currentValue == -1)
            return valueToCheck;


            int currentRiskLevel = -1;
           // String currentConditionDescription;
            int toCheckRiskLevel = -1;
           // String toCheckConditionDescription;

            for(List<Integer> list : listOfWeatherConditionsRisk)
            {
                if(list.contains(currentValue))
                {
                    if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
                        currentRiskLevel = list.get(1);

                    else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
                        currentRiskLevel = list.get(2);
                }

                if(list.contains(valueToCheck))
                {
                    if(criteriaType.equals(DirectionsCriteria.PROFILE_WALKING))
                        toCheckRiskLevel = list.get(1);
                    else if(criteriaType.equals(DirectionsCriteria.PROFILE_CYCLING))
                        toCheckRiskLevel = list.get(2);
                }
            }

            if(currentRiskLevel < toCheckRiskLevel)
                return valueToCheck;

            return currentValue;

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
