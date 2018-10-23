package com.example.karol.weatherassistant.Helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karol.weatherassistant.Model.CurrentWeather.Forecast;
import com.example.karol.weatherassistant.R;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>
{
    private List<Forecast> _forecastList;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView day, time, temperature, windSpeed;
        public ImageView icon;

        public ViewHolder(View v)
        {
            super(v);
            day = v.findViewById(R.id.textView_forecast_day);
            time = v.findViewById(R.id.textView_forecast_hour);
            temperature = v.findViewById(R.id.textView_forecast_temperature);
            icon = v.findViewById(R.id.imageView_forecastIcon);
            windSpeed = v.findViewById(R.id.textView_forecast_windSpeed);
        }
    }

    public ForecastAdapter(List<Forecast> forecastList){_forecastList = forecastList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ViewHolder holder, int position)
    {
        Forecast forecast = _forecastList.get(position);
        holder.day.setText(forecast.get_day());
        holder.time.setText(forecast.get_time());
        holder.temperature.setText(forecast.get_temperature());
        holder.icon.setImageResource(forecast.get_icon());
        holder.windSpeed.setText(forecast.get_windSpeed());
    }

    @Override
    public int getItemCount(){return _forecastList.size();}
}
