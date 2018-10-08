package com.example.karol.weatherassistant.Helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karol.weatherassistant.Model.CurrentWeather.Warning;
import com.example.karol.weatherassistant.R;

import org.w3c.dom.Text;

import java.util.List;

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder>
{
    private List<Warning> _warningList;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView type, degree, description, from, to;
        public ImageView icon;

        public ViewHolder(View v)
        {
            super(v);
            type = (TextView)v.findViewById(R.id.textView_warningInfo);
            degree = (TextView)v.findViewById(R.id.textView_warningInfo_degreeValue);
            description = (TextView)v.findViewById(R.id.textView_warningInfo_description);
            from = (TextView)v.findViewById(R.id.textView_warningStatement_fromValue);
            to = (TextView)v.findViewById(R.id.textView_warningStatement_toValue);
            icon = (ImageView)v.findViewById(R.id.imageView_warningInfo);

        }
    }

    public WarningAdapter(List<Warning> warningList)
    {
        _warningList = warningList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.warning_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(WarningAdapter.ViewHolder holder, int position)
    {
        Warning warning = _warningList.get(position);
        holder.type.setText(warning.get_type());
        holder.degree.setText(warning.get_degree());
        holder.description.setText(warning.get_description());
        holder.from.setText(warning.get_from());
        holder.to.setText(warning.get_to());
        holder.icon.setImageResource(warning.get_icon());
    }

    @Override
    public int getItemCount()
    {
        return _warningList.size();
    }
}
