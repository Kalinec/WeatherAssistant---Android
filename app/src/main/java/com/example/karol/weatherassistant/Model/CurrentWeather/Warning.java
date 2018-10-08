package com.example.karol.weatherassistant.Model.CurrentWeather;

public class Warning
{
    private String _type;
    private String _degree;
    private String _description;
    private String _from;
    private String _to;
    private int _icon;

    public Warning(String type, String degree, String description, String from, String to, int icon)
    {
        _type = type;
        _degree = degree;
        _description = description;
        _from = from;
        _to = to;
        _icon = icon;
    }


    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_degree() {
        return _degree;
    }

    public void set_degree(String _degree) {
        this._degree = _degree;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_from() {
        return _from;
    }

    public void set_from(String _from) {
        this._from = _from;
    }

    public String get_to() {
        return _to;
    }

    public void set_to(String _to) {
        this._to = _to;
    }

    public int get_icon() {
        return _icon;
    }

    public void set_icon(int _icon) {
        this._icon = _icon;
    }
}
