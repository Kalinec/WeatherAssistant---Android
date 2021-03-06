package com.example.karol.weatherassistant.Models.CurrentWeather;

public class Forecast
{
    private String _day;
    private String _time;
    private int _icon;
    private String _temperature;
    private String _windSpeed;

    public Forecast(String day, String time, int icon, String temperature, String windSpeed)
    {
        _day = day;
        _time = time;
        _icon = icon;
        _temperature = temperature;
        _windSpeed = windSpeed;
    }


    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public int get_icon() {
        return _icon;
    }

    public void set_icon(int _icon) {
        this._icon = _icon;
    }

    public String get_temperature() {
        return _temperature;
    }

    public void set_temperature(String _temperature) {
        this._temperature = _temperature;
    }

    public String get_windSpeed() {
        return _windSpeed;
    }

    public void set_windSpeed(String _windSpeed) {
        this._windSpeed = _windSpeed;
    }
}
