package com.example.karol.weatherassistant.Services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import com.example.karol.weatherassistant.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class LocationService
{
    private LocationManager _locationManager;
    private FusedLocationProviderClient _fusedLocationProviderClient;
    private LocationRequest _locationRequest;
    private Context _context;

    public LocationService(LocationRequest locationRequest, Context context)
    {
        _context = context;
        _locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        _locationRequest = locationRequest;
        _locationRequest.setNumUpdates(1);
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public boolean checkGpsStatus()
    {
        return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void requestLocation(LocationCallback locationCallback)
    {
        if(!checkGpsStatus())
        {
            buildAlertMessageNoGps();
            return;
        }

        try
        {
            _fusedLocationProviderClient.requestLocationUpdates(_locationRequest, locationCallback, null);
        }
        catch (SecurityException e)
        {
            Toast.makeText(_context, _context.getString(R.string.error_gps_update_location), Toast.LENGTH_SHORT).show();
            Log.e("GpsLocationUpdateError", e.getMessage());
        }
    }

    public void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(_context.getString(R.string.gps_disabled_alert))
                .setCancelable(false)
                .setPositiveButton(_context.getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        _context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(_context.getString(R.string.word_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
