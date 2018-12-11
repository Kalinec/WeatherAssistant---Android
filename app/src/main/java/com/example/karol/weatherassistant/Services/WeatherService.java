package com.example.karol.weatherassistant.Services;

import com.example.karol.weatherassistant.Helpers.CurrentWeatherFetchData;
import com.example.karol.weatherassistant.Helpers.CurrentWeatherFetchDataForRiskAssessment;
import com.example.karol.weatherassistant.Helpers.ForecastWeatherFetchData;

import java.util.ArrayList;

public class WeatherService
{
    //Singleton class implementation
    private static WeatherService INSTANCE = null;

    private WeatherService() {};

    public static synchronized WeatherService getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new WeatherService();

        return(INSTANCE);
    }

    //rest of class
    //private CurrentWeatherFetchData _fetchData = new CurrentWeatherFetchData();
    private final String _APIID = "ac545ebcce565f9c91956383c030f848";
    private String _units = "metric";
    private String _language = "en";

    public void setUnits(String units)
    {
        _units = units;
    }

    public void setLanguage(String language)
    {
        _language = language;
    }


    public void getCurrentWeatherByCityName(String city, String routeCriteria)
    {
        CurrentWeatherFetchData CurrentWeatherFetchData = new CurrentWeatherFetchData();
        String query = buildQueryByCityName(city, false);
        CurrentWeatherFetchData.execute(query, routeCriteria);
    }

    public void getCurrentWeatherByCoordinate(double latitude, double longitude, String routeCriteria)
    {
        CurrentWeatherFetchData currentWeatherFetchData = new CurrentWeatherFetchData();
        String query = buildQueryByCoordinate(latitude, longitude, false);
        currentWeatherFetchData.execute(query, routeCriteria);
    }

    public void getForecastWeatherByCityName(String city, String routeCriteria)
    {
        ForecastWeatherFetchData forecastWeatherFetchData = new ForecastWeatherFetchData();
        String query = buildQueryByCityName(city, true);
        forecastWeatherFetchData.execute(query, routeCriteria);
    }

    public void getForecastWeatherByCoordinate(double latitude, double longitude, String routeCriteria)
    {
        ForecastWeatherFetchData forecastWeatherFetchData = new ForecastWeatherFetchData();
        String query = buildQueryByCoordinate(latitude, longitude, true);
        forecastWeatherFetchData.execute(query, routeCriteria);
    }

    public void getMultipleCurrentWeatherByCoordinate(ArrayList<ArrayList<Double>> listOfCoordinates,  String routeCriteria)
    {
        CurrentWeatherFetchDataForRiskAssessment currentWeatherFetchDataForRiskAssessment = new CurrentWeatherFetchDataForRiskAssessment();
        ArrayList<String> queryList = new ArrayList<>();

        for(ArrayList<Double> doubleList : listOfCoordinates)
        {
            Double latitude = null;
            Double longitude = null;
            String query = null;

            for(int i = 0; i < doubleList.size(); i++)
            {
                if(i == 0)
                    latitude = doubleList.get(i);
                else
                    longitude = doubleList.get(i);
            }
            if(latitude != null && longitude != null)
                query = buildQueryByCoordinate(latitude, longitude, false);
            queryList.add(query);
        }

       /* for(int i = 0; i < coordinates.length; i++)
        {
            Double latitude = null;
            Double longitude = null;
            String query = null;

            for(int j = 0; j < coordinates[i].length; j++)
            {
                    if(j == 0)
                        latitude = coordinates[i][j];
                    else
                        longitude = coordinates[i][j];
            }

            if(latitude != null && longitude != null)
                query = buildQueryByCoordinate(latitude, longitude, false);
            queryList.add(query);
        } */

        queryList.add(routeCriteria);
        currentWeatherFetchDataForRiskAssessment.execute(queryList);
    }

    //queryType: false - currentWeather; true - forecastWeather
    private String buildQueryByCityName(String city, boolean queryType)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(queryType == false)
            stringBuilder.append("http://api.openweathermap.org/data/2.5/weather?q=");
        else
            stringBuilder.append("http://api.openweathermap.org/data/2.5/forecast?q=");
        stringBuilder.append(city);
        stringBuilder.append("&appid=");
        stringBuilder.append(_APIID);
        stringBuilder.append("&units=");
        stringBuilder.append(_units);
        stringBuilder.append("&lang=");
        stringBuilder.append(_language);

        return stringBuilder.toString();
    }

    private String buildQueryByCoordinate(double latitude, double longitude, boolean queryType)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(queryType == false)
            stringBuilder.append("http://api.openweathermap.org/data/2.5/weather?lat=");
        else
            stringBuilder.append("http://api.openweathermap.org/data/2.5/forecast?lat=");
        stringBuilder.append(latitude);
        stringBuilder.append("&lon=");
        stringBuilder.append(longitude);
        stringBuilder.append("&appid=");
        stringBuilder.append(_APIID);
        stringBuilder.append("&units=");
        stringBuilder.append(_units);
        stringBuilder.append("&lang=");
        stringBuilder.append(_language);

        return stringBuilder.toString();
    }
}
