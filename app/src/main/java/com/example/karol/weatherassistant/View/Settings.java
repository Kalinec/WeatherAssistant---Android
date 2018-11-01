package com.example.karol.weatherassistant.View;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.karol.weatherassistant.Helpers.NotificationReceiver;
import com.example.karol.weatherassistant.Helpers.Permissions;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.Services.NotificationService;

import java.util.HashMap;
import java.util.Map;

public class Settings extends Fragment
{
    //private Intent intent;
    private RadioGroup _stormLocationRadioGroup;
    private EditText _city;
    private Spinner _radiusSpinner;
    private Button _confirmButton;
    private Button _cancelButton;
    private Spinner _updatesFrequencySpinner;
    private CheckBox[] _weatherWarnings;
    private int[] _weatherWarningsValues;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

  /*  //GPS FIELDS
    private LocationListener _locationListener;
    private Location _location;
    private Criteria _criteria;
    private LocationManager _locationManager;
    private final Looper looper = null; */

    public Settings()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        _stormLocationRadioGroup = view.findViewById(R.id.radioGroup_settings_storm);
        _city = view.findViewById(R.id.editText_settings_city);
        _radiusSpinner = view.findViewById(R.id.spinner_settings_radius);
        _confirmButton = view.findViewById(R.id.button_settings_confirm);
        _cancelButton = view.findViewById(R.id.button_settings_cancel);
        _updatesFrequencySpinner = view.findViewById(R.id.spinner_settings_updatesFrequency);
        _weatherWarnings = new CheckBox[]
                {
                        view.findViewById(R.id.checkBox_settings_frost),
                        view.findViewById(R.id.checkBox_settings_heat),
                        view.findViewById(R.id.checkBox_settings_wind),
                        view.findViewById(R.id.checkBox_settings_rain),
                        view.findViewById(R.id.checkBox_settings_storm),
                        view.findViewById(R.id.checkBox_settings_tornado)
                };
        _weatherWarningsValues = new int[6];
        _city.setEnabled(false);

      /*  _criteria = new Criteria();
        setCriteria();
        _locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                _location = location;
                Log.d("Location changes", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.d("Status Changed", String.valueOf(status));
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.d("Provider Enabled", provider);
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.d("Provider Disabled", provider);
            }
        }; */

        //radioGroup behavior settings
        _stormLocationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //if "your location" radio button checked, "city" editText should not be enabled
                if(checkedId == 1)
                    _city.setEnabled(true);

                else
                    _city.setEnabled(false);
            }
        });

        //radius spinner implementation
        _radiusSpinner = (Spinner) view.findViewById(R.id.spinner_settings_radius);
        ArrayAdapter<CharSequence> radiusAdapter = ArrayAdapter.createFromResource(getContext(),R.array.radius_array, android.R.layout.simple_spinner_item);
        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _radiusSpinner.setAdapter(radiusAdapter);
        _radiusSpinner.setSelection(4);


        _updatesFrequencySpinner = (Spinner) view.findViewById(R.id.spinner_settings_updatesFrequency);
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(getContext(),R.array.frequency_array, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _updatesFrequencySpinner.setAdapter(frequencyAdapter);

        //confirm button implementation
        _confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), NotificationReceiver.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


                intent.putExtra("Radius", Integer.parseInt(_radiusSpinner.getSelectedItem().toString()));
                //intent.putExtra("WeatherWarnings", _weatherWarnings);
                for(int i=0; i<6; i++)
                {
                    if(_weatherWarnings[i].isChecked())
                        _weatherWarningsValues[i] = 1;
                    else
                        _weatherWarningsValues[i] = 0;
                }
                intent.putExtra("WeatherWarnings", _weatherWarningsValues);

                if((int)_stormLocationRadioGroup.getCheckedRadioButtonId() == 1)
                {
                    if(_city.getText().equals(""))
                        Toast.makeText(getContext(), getString(R.string.Settings_alert_cityNotEntered), Toast.LENGTH_SHORT).show();

                    else
                    {
                        intent.putExtra("City", _city.getText().toString());
                        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 500, pendingIntent);

                        /*
                        alarmManager.setInexactRepeating(
                                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime(),
                                1000 * 60 * Integer.parseInt(_updatesFrequencySpinner.getSelectedItem().toString()),
                                pendingIntent); */

                    }
                }

                else
                {
                    //NotificationService.enqueueWork(getContext(), intent);
                    if(!Permissions.Check_FINE_LOCATION(getActivity()))
                        Permissions.Request_FINE_LOCATION(getActivity(), 2);


                    if(MainActivity.CheckGpsStatus(getContext()))
                    {
                        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 15000, pendingIntent);

                        /*
                        alarmManager.setInexactRepeating(
                                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime(),
                                1000 * 60 * Integer.parseInt(_updatesFrequencySpinner.getSelectedItem().toString()),
                                pendingIntent); */
                       /* try
                        {
                            _locationManager.requestSingleUpdate(_criteria, _locationListener, looper);
                        }
                        catch (SecurityException e)
                        {
                            Log.e("GPS SECURITY EXCEPTION", e.getMessage());
                        } */

                    }

                    else
                        Toast.makeText(getActivity(), "Włącz moduł GPS!", Toast.LENGTH_SHORT).show();
                }

                _confirmButton.setEnabled(false);
                _cancelButton.setVisibility(View.VISIBLE);
            }

        });

        _cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               if(alarmManager != null)
                   alarmManager.cancel(pendingIntent);

               _cancelButton.setVisibility(View.GONE);
               _confirmButton.setEnabled(true);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("NOTIFICATION_TYPE", _stormLocationRadioGroup.getCheckedRadioButtonId());
        outState.putBoolean("CITY_ENABLED", _city.isEnabled());
        outState.putString("CITY", _city.getText().toString());
        outState.putInt("RADIUS", _radiusSpinner.getSelectedItemPosition());
        outState.putBoolean("FROST", _weatherWarnings[0].isChecked());
        outState.putBoolean("HEAT", _weatherWarnings[1].isChecked());
        outState.putBoolean("WIND", _weatherWarnings[2].isChecked());
        outState.putBoolean("RAIN", _weatherWarnings[3].isChecked());
        outState.putBoolean("STORM", _weatherWarnings[4].isChecked());
        outState.putBoolean("TORNADO", _weatherWarnings[5].isChecked());
        outState.putBoolean("CONFIRM_ENABLED", _confirmButton.isEnabled());
        outState.putInt("CANCEL_VISIBILITY", _cancelButton.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
        {
            _stormLocationRadioGroup.check(savedInstanceState.getInt("NOTIFICATION_TYPE"));
            _city.setEnabled(savedInstanceState.getBoolean("CITY_ENABLED"));
            _city.setText(savedInstanceState.getString("CITY"));
            _radiusSpinner.setSelection(savedInstanceState.getInt("RADIUS"));
            _weatherWarnings[0].setChecked(savedInstanceState.getBoolean("FROST"));
            _weatherWarnings[1].setChecked(savedInstanceState.getBoolean("HEAT"));
            _weatherWarnings[2].setChecked(savedInstanceState.getBoolean("WIND"));
            _weatherWarnings[3].setChecked(savedInstanceState.getBoolean("RAIN"));
            _weatherWarnings[4].setChecked(savedInstanceState.getBoolean("STORM"));
            _weatherWarnings[5].setChecked(savedInstanceState.getBoolean("TORNADO"));
            _confirmButton.setEnabled(savedInstanceState.getBoolean("CONFIRM_ENABLED"));
            _cancelButton.setVisibility(savedInstanceState.getInt("CANCEL_VISIBILITY"));
        }

    }

   /* private void setCriteria()
    {
        // this is done to save the battery life of the device
        _criteria.setAccuracy(Criteria.ACCURACY_FINE);
        _criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        _criteria.setAltitudeRequired(false);
        _criteria.setBearingRequired(false);
        _criteria.setSpeedRequired(false);
        _criteria.setCostAllowed(true);
        _criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        _criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
    } */
}
