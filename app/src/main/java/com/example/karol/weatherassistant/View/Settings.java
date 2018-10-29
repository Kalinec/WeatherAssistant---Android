package com.example.karol.weatherassistant.View;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
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
                if((int)_stormLocationRadioGroup.getCheckedRadioButtonId() == 1)
                {
                    if(_city.getText().equals(""))
                        Toast.makeText(getContext(), getString(R.string.Settings_alert_cityNotEntered), Toast.LENGTH_SHORT).show();

                    else
                    {
                        intent.putExtra("City", _city.getText().toString());
                        intent.putExtra("Radius", Integer.parseInt(_radiusSpinner.getSelectedItem().toString()));
                        for(int i=0; i<6; i++)
                        {
                            if(_weatherWarnings[i].isChecked())
                                _weatherWarningsValues[i] = 1;
                            else
                                _weatherWarningsValues[i] = 0;
                        }
                        intent.putExtra("WeatherWarnings", _weatherWarningsValues);
                        intent.putExtra("updateFrequency", Integer.parseInt(_updatesFrequencySpinner.getSelectedItem().toString()));

                        alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 15000, pendingIntent);
                        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*1000, pendingIntent);

                        //NotificationService.enqueueWork(getContext(), intent);
                    }
                }

                else
                {
                    //NotificationService.enqueueWork(getContext(), intent);
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
}
