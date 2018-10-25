package com.example.karol.weatherassistant.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.karol.weatherassistant.R;

public class Settings extends Fragment
{
    private RadioGroup _stormLocationRadioGroup;
    private EditText _city;
    private Spinner _radiusSpinner;
    private Button _confirmButton;


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

        _city.setEnabled(false);

        //radioGroup behavior settings
        _stormLocationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //if "your location" radio button checked, "city" editText should not be enabled
                if(checkedId == 2131296455)
                    _city.setEnabled(false);

                else if(checkedId == 1)
                    _city.setEnabled(true);
            }
        });

        //radius spinner implementation
        _radiusSpinner = (Spinner) view.findViewById(R.id.spinner_settings_radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.radius_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _radiusSpinner.setAdapter(adapter);
        _radiusSpinner.setSelection(4);

        //confirm button implementation
        _confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((int)_stormLocationRadioGroup.getCheckedRadioButtonId() == 1)
                {
                    if(_city.getText().equals(""))
                        Toast.makeText(getContext(), getString(R.string.Settings_alert_cityNotEntered), Toast.LENGTH_SHORT).show();

                    else
                    {

                    }
                }

                else if((int)_stormLocationRadioGroup.getCheckedRadioButtonId() == 2131296455)
                {

                }
            }
        });

        return view;
    }

}
