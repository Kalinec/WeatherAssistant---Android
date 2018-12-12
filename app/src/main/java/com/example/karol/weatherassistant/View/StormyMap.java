package com.example.karol.weatherassistant.View;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.example.karol.weatherassistant.R;
import com.koushikdutta.ion.Ion;


public class StormyMap extends Fragment {

    private ImageView _mapView;
    private ImageButton _displayMapSettings;
    private ImageButton _exitMapSettings;
    private CardView _mapSettingsCardView;
    private CheckBox _showDirectionAndSpeed;
    private CheckBox _showGroupsAndStormsNumber;
    private ToggleButton _selectedMap;
    private RadioButton _staticMap;
    private RadioButton _animatedMap;
    private Spinner _mapMagnification;
    private ArrayAdapter<CharSequence> adapter;

    public StormyMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_stormy_map, container, false);
        _mapView = view.findViewById(R.id.photoView_stormyMap_mapView);
        _displayMapSettings = view.findViewById(R.id.imageButton_stormyMap_displayMapSettings);
        _exitMapSettings = view.findViewById(R.id.imageButton_stormyMap_options_exit);
        _mapSettingsCardView = view.findViewById(R.id.cardView_stormyMap_mapSettings);
        _showDirectionAndSpeed = view.findViewById(R.id.checkBox_stormyMap_showDirectionAndSpeed);
        _showGroupsAndStormsNumber = view.findViewById(R.id.checkBox_stormyMap_showGroupsAndStormsNumber);
        _selectedMap = view.findViewById(R.id.toggleButton_stormyMap_mapChange);
        _staticMap = view.findViewById(R.id.radioButton_stormyMap_staticMap);
        _animatedMap = view.findViewById(R.id.radioButton_stormyMap_animatedMap);
        _mapMagnification = view.findViewById(R.id.spinner_stormyMap_magnification);
        _staticMap.setChecked(true);

        adapter = ArrayAdapter.createFromResource(getContext(),R.array.magnification_polish_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _mapMagnification.setAdapter(adapter);

        _mapMagnification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _staticMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeMap();
            }
        });
        _selectedMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeSpinnerList();
                changeMap();
            }
        });
        _displayMapSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_mapSettingsCardView.getVisibility() == View.VISIBLE)
                    _mapSettingsCardView.setVisibility(View.GONE);
                else
                    _mapSettingsCardView.setVisibility(View.VISIBLE);
            }
        });
        _exitMapSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mapSettingsCardView.setVisibility(View.GONE);
            }
        });
        _showDirectionAndSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _showGroupsAndStormsNumber.setChecked(false);
                changeMap();
            }
        });
        _showGroupsAndStormsNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    _showDirectionAndSpeed.setChecked(false);
                changeMap();
            }
        });
        return view;
    }

    private void changeSpinnerList()
    {
        if(_selectedMap.isChecked())
            adapter = ArrayAdapter.createFromResource(getContext(),R.array.magnification_europe_array, android.R.layout.simple_spinner_item);
        else
            adapter = ArrayAdapter.createFromResource(getContext(),R.array.magnification_polish_array, android.R.layout.simple_spinner_item);

        _mapMagnification.setAdapter(adapter);
    }
    private void changeMap()
    {
        String url = null;
        if (!_selectedMap.isChecked())
        {
            if (_staticMap.isChecked())
            {
                if (_showDirectionAndSpeed.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_wektory.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_wektory_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_wektory_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_wektory_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_wektory_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_wektory_polska_se.gif";
                            break;
                    }
                }

                else if (_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_grupy.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_grupy_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_grupy_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_grupy_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_grupy_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_grupy_polska_se.gif";
                            break;
                    }
                }

                else if (!_showDirectionAndSpeed.isChecked() &&
                        !_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_polska_se.gif";
                            break;
                    }
                }

            }

            else
            {
                if (_showDirectionAndSpeed.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_anim_wektory.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_anim_wektory_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_anim_wektory_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_anim_wektory_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_anim_wektory_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_anim_wektory_polska_se.gif";
                            break;
                    }
                }

                else if (_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_anim_grupy.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_anim_grupy_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_anim_grupy_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_anim_grupy_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_anim_grupy_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_anim_grupy_polska_se.gif";
                            break;
                    }
                }

                else if (!_showDirectionAndSpeed.isChecked() &&
                        !_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_anim.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/burze_anim_polska_nw.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/burze_anim_polska_ne.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/burze_anim_polska_c.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/burze_anim_polska_sw.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/burze_anim_polska_se.gif";
                            break;
                    }
                }

            }
        }

        else
        {
            if (_staticMap.isChecked())
            {
                if (_showDirectionAndSpeed.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa_wektory.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria_direction.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg_direction.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia_direction.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika_vektory.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france_direction.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece_direction.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal_direction.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland_vektoren.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary_direction.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein_direction.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine_vettori.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles_direction.gif";
                            break;
                    }
                }

                else if (_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa_grupy.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria_grup.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg_grup.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia_grup.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika_skupiny.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france_grup.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece_grup.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal_grup.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland_gruppen.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary_grup.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein_grup.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine_gruppi.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles_grup.gif";
                            break;
                    }
                }

                else if (!_showDirectionAndSpeed.isChecked() &&
                        !_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles.gif";
                            break;
                    }
                }

            }

            else
            {
                if (_showDirectionAndSpeed.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa_anim_wektory.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria_animation_direction.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg_animation_direction.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia_animation_direction.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika_animace_vektory.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france_animation_direction.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece_animation_direction.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal_animation_direction.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland_lebhaft_vektoren.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary_animation_direction.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein_animation_direction.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine_lebhaft_vettori.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles_animation_direction.gif";
                            break;
                    }
                }

                else if (_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa_anim_grupy.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria_animation_grup.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg_animation_grup.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia_animation_grup.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika_animace_skupiny.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france_animation_grup.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece_animation_grup.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal_animation_grup.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland_lebhaft_gruppen.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary_animation_grup.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein_animation_grup.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine_lebhaft_gruppi.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles_animation_grup.gif";
                            break;
                    }
                }

                else if (!_showDirectionAndSpeed.isChecked() &&
                        !_showGroupsAndStormsNumber.isChecked())
                {
                    switch (_mapMagnification.getSelectedItemPosition())
                    {
                        case 0:
                            url = "https://burze.dzis.net/burze_europa_anim.gif";
                            break;
                        case 1:
                            url = "https://burze.dzis.net/storm_austria_animation.gif";
                            break;
                        case 2:
                            url = "https://burze.dzis.net/storm_belgium_netherlands_luxembourg_animation.gif";
                            break;
                        case 3:
                            url = "https://burze.dzis.net/storm_bosnia_and_herzegovina_croatia_slovenia_animation.gif";
                            break;
                        case 4:
                            url = "https://burze.dzis.net/stormy_mapa_ceska_republika_animace.gif";
                            break;
                        case 5:
                            url = "https://burze.dzis.net/storm_france_animation.gif";
                            break;
                        case 6:
                            url = "https://burze.dzis.net/storm_greece_animation.gif";
                            break;
                        case 7:
                            url = "https://burze.dzis.net/storm_spain_and_portugal_animation.gif";
                            break;
                        case 8:
                            url = "https://burze.dzis.net/sturm_deutschland_lebhaft.gif";
                            break;
                        case 9:
                            url = "https://burze.dzis.net/storm_slovenia_hungary_animation.gif";
                            break;
                        case 10:
                            url = "https://burze.dzis.net/storm_switzerland_and_liechtenstein_animation.gif";
                            break;
                        case 11:
                            url = "https://burze.dzis.net/italia_fulmine_lebhaft.gif";
                            break;
                        case 12:
                            url = "https://burze.dzis.net/storm_british_isles_animation.gif";
                            break;
                    }
                }
            }
        }
        Ion.with(this)
                .load(url)
                .setLogging("DeepZoom", Log.VERBOSE)
                .withBitmap()
                .deepZoom()
                .intoImageView(_mapView);
    }
}
