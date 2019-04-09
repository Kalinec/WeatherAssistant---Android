package com.example.karol.weatherassistant.View;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;


import com.example.karol.weatherassistant.Helpers.Permissions;
import com.example.karol.weatherassistant.Models.CurrentWeather.Warning;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.SectionsStatePagerAdapter;
import com.example.karol.weatherassistant.Services.BurzeDzisService.IWsdl2CodeEvents;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeBurza;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeMiejscowosc;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeOstrzezenia;
import com.example.karol.weatherassistant.Services.BurzeDzisService.serwerSOAPService;
import com.example.karol.weatherassistant.Services.LocationService;
import com.example.karol.weatherassistant.Services.WeatherService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
{

    public static ProgressBar DownloadProgressBar;
    public static ImageButton LocalizerButton;
    private DrawerLayout _drawerLayout;
    private SectionsStatePagerAdapter _sectionsStatePagerAdapter;
    public static ViewPager ViewPager;
    public static SearchView SearchView;
    private serwerSOAPService _stormService;
    public IWsdl2CodeEvents EventsHandler;
    private MyComplexTypeMiejscowosc _stormLocation;
    private MyComplexTypeBurza _stormInfo;
    private MyComplexTypeOstrzezenia _warningInfo;
    public static Resources Resources;
    private Context _context;

    //GPS field
    private LocationService _locationService;
    private LocationCallback _locationCallback;
    //GPS Fields
    public static LocationManager LocationManager;
    public static FusedLocationProviderClient FusedLocationProviderClient;
    private LocationRequest _locationRequest;


    private View _aboutView;
    private PopupWindow _popupAbout;
    private ImageButton _popupCloseButton;
    private RelativeLayout _relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this;

        checkRequiredPermissions();

        EventsHandler = new IWsdl2CodeEvents() {
            @Override
            public void Wsdl2CodeStartedRequest() {
                DownloadProgressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void Wsdl2CodeFinished(String methodName, Object Data) {
                switch (methodName)
                {
                    case "KeyAPI":
                        break;
                    case "miejscowosc":
                        _stormLocation = (MyComplexTypeMiejscowosc)Data;
                        try
                        {
                            _stormService.szukaj_burzyAsync(
                                    String.valueOf(_stormLocation.y),
                                    String.valueOf(_stormLocation.x),
                                    Integer.valueOf(StormSearch.RadiusSpinner.getSelectedItem().toString())
                                    , getString(R.string.burzedzisnet_access_token));

                            _stormService.ostrzezenia_pogodoweAsync(
                                    _stormLocation.y,
                                    _stormLocation.x,
                                    getString(R.string.burzedzisnet_access_token));
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "Explanation of what was being attempted from onPostExecute", e);
                        }
                        StormSearch.City.setText(SearchView.getQuery());
                        StormSearch.Latitude.setText(String.valueOf(_stormLocation.y));
                        StormSearch.Longitude.setText(String.valueOf(_stormLocation.x));
                        break;
                    case "ostrzezenia_pogodowe":
                        StormSearch.WarningList.clear();
                        _warningInfo = (MyComplexTypeOstrzezenia)Data;

                        //if(_warningInfo.wiatr == 0 && _warningInfo.upal == 0 && _warningInfo.traba == 0 && _warningInfo.opad == 0 && _warningInfo.mroz == 0 && _warningInfo.burza == 0)


                        if(_warningInfo.burza != 0)
                        {
                            if(_warningInfo.burza == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_storms),
                                        String.valueOf(_warningInfo.burza),
                                        getString(R.string.warning_storm_description1),
                                        utcToLocalTime(_warningInfo.burza_od_dnia),
                                        utcToLocalTime(_warningInfo.burza_do_dnia),
                                        R.drawable.if_weather_warning_storm));
                            else if(_warningInfo.burza == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_storms), String.valueOf(_warningInfo.burza), getString(R.string.warning_storm_description2), utcToLocalTime(_warningInfo.burza_od_dnia), utcToLocalTime(_warningInfo.burza_do_dnia), R.drawable.if_weather_warning_storm));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_storms), String.valueOf(_warningInfo.burza), getString(R.string.warning_storm_description3), utcToLocalTime(_warningInfo.burza_od_dnia), utcToLocalTime(_warningInfo.burza_do_dnia), R.drawable.if_weather_warning_storm));
                        }

                        if(_warningInfo.mroz != 0)
                        {
                            if(_warningInfo.mroz == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description1), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                            else if(_warningInfo.mroz == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description2), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description3), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                        }

                        if(_warningInfo.opad != 0)
                        {
                            if(_warningInfo.opad == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description1), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                            else if(_warningInfo.opad == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description2), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description3), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                        }

                        if(_warningInfo.traba != 0)
                        {
                            if(_warningInfo.traba == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description1), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                            else if(_warningInfo.traba == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description2), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description3), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                        }

                        if(_warningInfo.upal != 0)
                        {
                            if(_warningInfo.upal == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description1), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                            else if(_warningInfo.upal == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description2), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description3), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                        }

                        if(_warningInfo.wiatr != 0)
                        {
                            if(_warningInfo.wiatr == 1)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description1), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                            else if(_warningInfo.wiatr == 2)
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description2), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                            else
                                StormSearch.WarningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description3), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                        }

                        StormSearch.WarningAdapter.notifyDataSetChanged();
                        break;
                    case "szukaj_burzy":
                        _stormInfo = (MyComplexTypeBurza)Data;
                        StormSearch.Time.setText(String.valueOf(_stormInfo.okres) + " min");
                        if(_stormInfo.kierunek.isEmpty())
                            StormSearch.Direction.setText("-");
                        else
                        StormSearch.Direction.setText(String.valueOf(_stormInfo.kierunek));
                        StormSearch.Number.setText(String.valueOf(_stormInfo.liczba));
                        StormSearch.Distance.setText(String.valueOf(_stormInfo.odleglosc) + " km");
                        break;
                    case "miejscowosci_lista":
                        break;
                }

            }

            @Override
            public void Wsdl2CodeFinishedWithException(Exception ex) {
                DownloadProgressBar.setVisibility(View.GONE);
                Log.e(TAG, ex.getMessage());

            }

            @Override
            public void Wsdl2CodeEndedRequest() {
                DownloadProgressBar.setVisibility(View.GONE);

            }
        };
        _stormService = new serwerSOAPService(EventsHandler, "https://burze.dzis.net/soap.php");
        _stormLocation = new MyComplexTypeMiejscowosc();
        _stormInfo = new MyComplexTypeBurza();
        _warningInfo = new MyComplexTypeOstrzezenia();
        Resources = getResources();

        //configuration menu button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        _drawerLayout = findViewById(R.id.drawer_layout);
        SearchView = findViewById(R.id.searchCity);
        DownloadProgressBar = findViewById(R.id.progressBar_download);
        LocalizerButton = findViewById(R.id.imageButton_my_location);

        //gps
        _locationService = new LocationService(new LocationRequest(), getApplicationContext());
        //gps
        //FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //_locationRequest = new LocationRequest();
        //_locationRequest.setNumUpdates(1);
        //_locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        _locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                switch (ViewPager.getCurrentItem())
                {
                    case 0:
                        WeatherService.getInstance().getCurrentWeatherByCoordinate(location.getLatitude(), location.getLongitude(), null);
                        WeatherService.getInstance().getForecastWeatherByCoordinate(location.getLatitude(), location.getLongitude(), null);
                        break;

                    case 2:
                        String convertedLatitude = decimalToDM(location.getLatitude());
                        String convertedLongitude = decimalToDM(location.getLongitude());

                        try
                        {
                            _stormService.szukaj_burzyAsync(convertedLatitude, convertedLongitude, Integer.valueOf(StormSearch.RadiusSpinner.getSelectedItem().toString()),getString(R.string.burzedzisnet_access_token));
                            _stormService.ostrzezenia_pogodoweAsync(Float.valueOf(convertedLatitude), Float.valueOf(convertedLongitude), getString(R.string.burzedzisnet_access_token));
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "Explanation of what was being attempted", e);
                        }

                        StormSearch.City.setText("-");
                        StormSearch.Latitude.setText(convertedLatitude);
                        StormSearch.Longitude.setText(convertedLongitude);
                        break;

                    case 4:
                        break;
                    }
            }
        };

        //set language response from WeatherAPI the same as the system language
        WeatherService.getInstance().setLanguage(Locale.getDefault().getLanguage());

        //handle navigation click events
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                item.setChecked(true);

                int id = item.getItemId();

                if(id == R.id.nav_WeatherForecast)
                {
                    ViewPager.setCurrentItem(0);
                    SearchView.setVisibility(View.VISIBLE);
                    LocalizerButton.setVisibility(View.VISIBLE);

                }


                else if(id == R.id.nav_StormyMap)
                {
                    ViewPager.setCurrentItem(1);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }


                else if(id == R.id.nav_StormSearch)
                {
                    ViewPager.setCurrentItem(2);
                    SearchView.setVisibility(View.VISIBLE);
                    LocalizerButton.setVisibility(View.VISIBLE);

                }


                else if(id == R.id.nav_PlanTheTrip)
                {
                    ViewPager.setCurrentItem(3);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }

                else if(id == R.id.nav_Settings)
                {
                    ViewPager.setCurrentItem(4);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }

                else if(id == R.id.nav_About)
                {
                    //popup initalize
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    _aboutView = inflater.inflate(R.layout.popup_about, null);
                    _popupAbout = new PopupWindow(
                            _aboutView,
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );

                    if(Build.VERSION.SDK_INT >= 21)
                    {
                        _popupAbout.setElevation(5.0f);
                    }

                    _popupCloseButton = (ImageButton) _aboutView.findViewById(R.id.imageButton_about_exit);

                    _popupCloseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _popupAbout.dismiss();
                        }
                    });
                    _relativeLayout = findViewById(R.id.content_frame);
                    _popupAbout.showAtLocation(_relativeLayout, Gravity.CENTER, 0, 0);
                }


                _drawerLayout.closeDrawers();

                //ViewPager.setCurrentItem(item.getItemId());

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });


        //fragment pager
        _sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        ViewPager = (ViewPager) findViewById(R.id.container);

        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 5) {}

                else if(position == 0 || position == 2)
                {
                    SearchView.setVisibility(View.VISIBLE);
                    LocalizerButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //setup the pager
        setupViewPager(ViewPager);


        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (ViewPager.getCurrentItem())
                {
                    case 0:
                        WeatherService.getInstance().getCurrentWeatherByCityName(query,  null);
                        WeatherService.getInstance().getForecastWeatherByCityName(query, null);
                        hideKeyboardFrom(getApplicationContext(),getCurrentFocus());
                        break;
                    case 2:
                        try
                        {
                            if(query.equals("test"))
                                fakeStormSearchAndWarning();
                            else
                                _stormService.miejscowoscAsync(query,getString(R.string.burzedzisnet_access_token));
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "Explanation of what was being attempted", e);
                        }
                        break;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        LocalizerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DownloadProgressBar.setVisibility(View.VISIBLE);
                _locationService.requestLocation(_locationCallback);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                _drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherForecast(), "WeatherForecast");
        adapter.addFragment(new StormyMap(), "StormyMap");
        adapter.addFragment(new StormSearch(), "StormySearch");
        adapter.addFragment(new PlanTheTrip(), "PlanTheTrip");
        adapter.addFragment(new Settings(), "Settings");
        viewPager.setAdapter(adapter);
    }

    private void fakeStormSearchAndWarning()
    {
        StormSearch.WarningList.clear();

        StormSearch.City.setText("Lublin");
        StormSearch.Latitude.setText("22.34");
        StormSearch.Longitude.setText("51.14");
        StormSearch.Time.setText(String.valueOf(15));
        StormSearch.Number.setText(String.valueOf(12));
        StormSearch.Distance.setText(String.valueOf("5.72km"));
        StormSearch.Direction.setText("W");
        StormSearch.WarningList.add(new Warning(getString(R.string.word_storms), "1", getString(R.string.warning_storm_description1), "15.05.2018", "16.05.2018", R.drawable.if_weather_warning_storm));
        StormSearch.WarningList.add(new Warning(getString(R.string.word_frost), "2", getString(R.string.warning_frost_description2), "16.05.2018", "17.05.2018", R.drawable.ic_weather_warning_frost));
        StormSearch.WarningList.add(new Warning(getString(R.string.word_rain), "3", getString(R.string.warning_rain_description3), "17.05.2018", "18.05.2018", R.drawable.if_weather_warning_rain));
        StormSearch.WarningList.add(new Warning(getString(R.string.word_tornado), "1", getString(R.string.warning_tornado_description1), "18.05.2018", "19.05.2018", R.drawable.if_weather_warning_tornado));
        StormSearch.WarningList.add(new Warning(getString(R.string.word_heat), "2", getString(R.string.warning_heat_description2), "19.05.2018", "20.05.2018", R.drawable.ic_weather_warning_heat));
        StormSearch.WarningList.add(new Warning(getString(R.string.word_wind),"3", getString(R.string.warning_wind_description3), "20.05.2018", "21.05.2018", R.drawable.if_weather_warning_wind));

        StormSearch.WarningAdapter.notifyDataSetChanged();
    }

    private String utcToLocalTime(String utcTime)
    {
        if(utcTime.equals("0"))
            return null;

        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try
        {
            date = simpleDateFormat.parse(utcTime);
        }
        catch (ParseException e)
        {
            Log.v("Exception", e.getLocalizedMessage());
            date = null;
        }
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void checkRequiredPermissions()
    {
        if(!Permissions.Check_STORAGE(this))
            Permissions.Request_STORAGE(this,0);

        if(!Permissions.Check_COARSE_LOCATION(this))
            Permissions.Request_COARSE_LOCATION(this,1);

        if(!Permissions.Check_FINE_LOCATION(this))
            Permissions.Request_FINE_LOCATION(this, 2);

        if(!Permissions.Check_INTERNET(this))
            Permissions.Request_INTERNET(this, 3);

        if(!Permissions.Check_ACCESS_NETWORK_STATE(this))
            Permissions.Request_ACCESS_NETWORK_STATE(this, 4);

        if(!Permissions.Check_WAKE_LOCK(this))
            Permissions.Request_WAKE_LOCK(this, 5);

    }

    public static boolean CheckGpsStatus(Context context){

        LocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String decimalToDM(double coord) {
        String output, degrees, minutes;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod := -79.982195 % 1 == 0.982195
        //
        // next get the integer part of the coord. On other words the whole number part.
        // e.g. intPart := -79

        double mod = coord % 1;
        int intPart = (int)coord;

        //set degrees to the value of intPart
        //e.g. degrees := "-79"

        degrees = String.valueOf(intPart);

        // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal point.
        // e.g. coord :=  0.982195 * 60 == 58.9317
        //	mod   := 58.9317 % 1    == 0.9317
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart := 58

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int)coord;
        if (intPart < 0) {
            // Convert number to positive if it's negative.
            intPart *= -1;
        }

        // set minutes to the value of intPart.
        // e.g. minutes = "58"
        minutes = String.valueOf(intPart);
        // I used this format for android but you can change it
        // to return in whatever format you like
        // e.g. output = "-79/1,58/1,56/1"
        output = degrees + "." + minutes;

        //Standard output of D°M′S″
        //output = degrees + "°" + minutes + "'" + seconds + "\"";

        return output;
    }

    public static void buildAlertMessageNoGps(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Resources.getString(R.string.gps_disabled_alert))
                .setCancelable(false)
                .setPositiveButton(Resources.getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(Resources.getString(R.string.word_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("WEATHERFORECAST_CITY", WeatherForecast.City.getText().toString());
        editor.commit();
    }
}
