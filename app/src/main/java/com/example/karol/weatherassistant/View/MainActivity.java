package com.example.karol.weatherassistant.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.karol.weatherassistant.Helpers.Permissions;
import com.example.karol.weatherassistant.Model.CurrentWeather.Warning;
import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.SectionsStatePagerAdapter;
import com.example.karol.weatherassistant.Services.BurzeDzisService.IWsdl2CodeEvents;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeBurza;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeMiejscowosc;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeOstrzezenia;
import com.example.karol.weatherassistant.Services.BurzeDzisService.serwerSOAPService;
import com.example.karol.weatherassistant.Services.WeatherService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.mapbox.mapboxsdk.storage.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity /* implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener */ {

    public static ProgressBar DownloadProgressBar;
    public static ImageButton LocalizerButton;
    private DrawerLayout _drawerLayout;
    private SectionsStatePagerAdapter _sectionsStatePagerAdapter;
    public static ViewPager _viewPager;
    public static SearchView SearchView;
    private serwerSOAPService _stormService;
    public IWsdl2CodeEvents _eventsHandler;
    private MyComplexTypeMiejscowosc _stormLocation;
    private MyComplexTypeBurza _stormInfo;
    private MyComplexTypeOstrzezenia _warningInfo;
    private boolean _stormLocationAsyncFinished;
    public static Resources resources;
    private Context _context;

    //GPS Fields
    public static LocationManager locationManager;
    public static Location location;

    //GPS FIELDS newer
    public static FusedLocationProviderClient _fusedLocationProviderClient;
    private LocationRequest _locationRequest;
    private LocationCallback _locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this;

        checkRequiredPermissions();

        _eventsHandler = new IWsdl2CodeEvents() {
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
                                    ,"3f04fbcac562e34c59d03cc166dc532a9451ded3");

                            _stormService.ostrzezenia_pogodoweAsync(
                                    _stormLocation.y,
                                    _stormLocation.x,
                                    "3f04fbcac562e34c59d03cc166dc532a9451ded3");
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
                        StormSearch.warningList.clear();
                        _warningInfo = (MyComplexTypeOstrzezenia)Data;

                        //if(_warningInfo.wiatr == 0 && _warningInfo.upal == 0 && _warningInfo.traba == 0 && _warningInfo.opad == 0 && _warningInfo.mroz == 0 && _warningInfo.burza == 0)


                        if(_warningInfo.burza != 0)
                        {
                            if(_warningInfo.burza == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_storms), String.valueOf(_warningInfo.burza), getString(R.string.warning_storm_description1), utcToLocalTime(_warningInfo.burza_od_dnia), utcToLocalTime(_warningInfo.burza_do_dnia), R.drawable.if_weather_warning_storm));
                            else if(_warningInfo.burza == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_storms), String.valueOf(_warningInfo.burza), getString(R.string.warning_storm_description2), utcToLocalTime(_warningInfo.burza_od_dnia), utcToLocalTime(_warningInfo.burza_do_dnia), R.drawable.if_weather_warning_storm));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_storms), String.valueOf(_warningInfo.burza), getString(R.string.warning_storm_description3), utcToLocalTime(_warningInfo.burza_od_dnia), utcToLocalTime(_warningInfo.burza_do_dnia), R.drawable.if_weather_warning_storm));
                        }

                        if(_warningInfo.mroz != 0)
                        {
                            if(_warningInfo.mroz == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description1), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                            else if(_warningInfo.mroz == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description2), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_frost), String.valueOf(_warningInfo.mroz), getString(R.string.warning_frost_description3), utcToLocalTime(_warningInfo.mroz_od_dnia), utcToLocalTime(_warningInfo.mroz_do_dnia), R.drawable.ic_weather_warning_frost));
                        }

                        if(_warningInfo.opad != 0)
                        {
                            if(_warningInfo.opad == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description1), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                            else if(_warningInfo.opad == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description2), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_rain), String.valueOf(_warningInfo.opad), getString(R.string.warning_rain_description3), utcToLocalTime(_warningInfo.opad_od_dnia), utcToLocalTime(_warningInfo.opad_do_dnia), R.drawable.if_weather_warning_rain));
                        }

                        if(_warningInfo.traba != 0)
                        {
                            if(_warningInfo.traba == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description1), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                            else if(_warningInfo.traba == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description2), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_tornado), String.valueOf(_warningInfo.traba), getString(R.string.warning_tornado_description3), utcToLocalTime(_warningInfo.traba_od_dnia), utcToLocalTime(_warningInfo.traba_do_dnia), R.drawable.if_weather_warning_tornado));
                        }

                        if(_warningInfo.upal != 0)
                        {
                            if(_warningInfo.upal == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description1), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                            else if(_warningInfo.upal == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description2), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_heat), String.valueOf(_warningInfo.upal), getString(R.string.warning_heat_description3), utcToLocalTime(_warningInfo.upal_od_dnia), utcToLocalTime(_warningInfo.upal_do_dnia), R.drawable.ic_weather_warning_heat));
                        }

                        if(_warningInfo.wiatr != 0)
                        {
                            if(_warningInfo.wiatr == 1)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description1), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                            else if(_warningInfo.wiatr == 2)
                                StormSearch.warningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description2), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                            else
                                StormSearch.warningList.add(new Warning(getString(R.string.word_wind), String.valueOf(_warningInfo.wiatr), getString(R.string.warning_wind_description3), utcToLocalTime(_warningInfo.wiatr_od_dnia), utcToLocalTime(_warningInfo.wiatr_do_dnia), R.drawable.if_weather_warning_wind));
                        }

                        StormSearch.warningAdapter.notifyDataSetChanged();
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
        _stormService = new serwerSOAPService(_eventsHandler, "https://burze.dzis.net/soap.php");
        _stormLocation = new MyComplexTypeMiejscowosc();
        _stormInfo = new MyComplexTypeBurza();
        _warningInfo = new MyComplexTypeOstrzezenia();
        resources = getResources();

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
        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        _locationRequest = new LocationRequest();
        _locationRequest.setInterval(5000);
        _locationRequest.setFastestInterval(1000);
        _locationRequest.setNumUpdates(1);
        _locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        _locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations())
                {
                    switch (_viewPager.getCurrentItem())
                    {
                        case 0:
                            WeatherService.getInstance().getCurrentWeatherByCoordinate(location.getLatitude(), location.getLongitude(), String.valueOf(_viewPager.getCurrentItem()), null);
                            WeatherService.getInstance().getForecastWeatherByCoordinate(location.getLatitude(), location.getLongitude(), String.valueOf(_viewPager.getCurrentItem()), null);
                            break;

                        case 2:
                            String convertedLatitude = decimalToDM(location.getLatitude());
                            String convertedLongitude = decimalToDM(location.getLongitude());

                            try
                            {
                                _stormService.szukaj_burzyAsync(convertedLatitude, convertedLongitude, Integer.valueOf(StormSearch.RadiusSpinner.getSelectedItem().toString()),"3f04fbcac562e34c59d03cc166dc532a9451ded3");
                                _stormService.ostrzezenia_pogodoweAsync(Float.valueOf(convertedLatitude), Float.valueOf(convertedLongitude), "3f04fbcac562e34c59d03cc166dc532a9451ded3");
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
                    _viewPager.setCurrentItem(0);
                    SearchView.setVisibility(View.VISIBLE);
                    LocalizerButton.setVisibility(View.VISIBLE);

                }


                else if(id == R.id.nav_StormyMap)
                {
                    _viewPager.setCurrentItem(1);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }


                else if(id == R.id.nav_StormSearch)
                {
                    _viewPager.setCurrentItem(2);
                    SearchView.setVisibility(View.VISIBLE);
                    LocalizerButton.setVisibility(View.VISIBLE);

                }


                else if(id == R.id.nav_PlanTheTrip)
                {
                    _viewPager.setCurrentItem(3);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }

                else if(id == R.id.nav_Settings)
                {
                    _viewPager.setCurrentItem(4);
                    SearchView.setVisibility(View.GONE);
                    LocalizerButton.setVisibility(View.GONE);

                }


                _drawerLayout.closeDrawers();

                //_viewPager.setCurrentItem(item.getItemId());

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });


        //fragment pager
        _sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        _viewPager = (ViewPager) findViewById(R.id.container);

        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0 || position == 2)
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
        setupViewPager(_viewPager);


        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (_viewPager.getCurrentItem())
                {
                    case 0:
                        WeatherService.getInstance().getCurrentWeatherByCityName(query, String.valueOf(_viewPager.getCurrentItem()), null);
                        WeatherService.getInstance().getForecastWeatherByCityName(query, String.valueOf(_viewPager.getCurrentItem()),null);
                        hideKeyboardFrom(getApplicationContext(),getCurrentFocus());
                        break;
                    case 2:
                        try
                        {
                            if(query.equals("test"))
                                fakeStormSearchAndWarning();
                            else
                                _stormService.miejscowoscAsync(query,"3f04fbcac562e34c59d03cc166dc532a9451ded3");
                          // _stormService.ostrzezenia_pogodoweAsync(_stormLocation.y,_stormLocation.x, "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                           //_stormService.szukaj_burzyAsync(String.valueOf(_stormLocation.y), String.valueOf(_stormLocation.x), 300, "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                           // _stormService.szukaj_burzyAsync("40.28", "17,14", 300, "3f04fbcac562e34c59d03cc166dc532a9451ded3");
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
                if(!CheckGpsStatus(_context))
                {
                    buildAlertMessageNoGps(_context);
                    return;
                }

                DownloadProgressBar.setVisibility(View.VISIBLE);
                try
                {
                    _fusedLocationProviderClient.requestLocationUpdates(_locationRequest, _locationCallback, null);

                }
                catch (SecurityException e)
                {

                }
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
        StormSearch.warningList.clear();

        StormSearch.City.setText("test");
        StormSearch.Latitude.setText("22.50");
        StormSearch.Longitude.setText("51.20");

        StormSearch.warningList.add(new Warning(getString(R.string.word_storms), "1", getString(R.string.warning_storm_description1), "15.05.2018", "16.05.2018", R.drawable.if_weather_warning_storm));
        StormSearch.warningList.add(new Warning(getString(R.string.word_frost), "2", getString(R.string.warning_frost_description2), "16.05.2018", "17.05.2018", R.drawable.ic_weather_warning_frost));
        StormSearch.warningList.add(new Warning(getString(R.string.word_rain), "3", getString(R.string.warning_rain_description3), "17.05.2018", "18.05.2018", R.drawable.if_weather_warning_rain));
        StormSearch.warningList.add(new Warning(getString(R.string.word_tornado), "1", getString(R.string.warning_tornado_description1), "18.05.2018", "19.05.2018", R.drawable.if_weather_warning_tornado));
        StormSearch.warningList.add(new Warning(getString(R.string.word_heat), "2", getString(R.string.warning_heat_description2), "19.05.2018", "20.05.2018", R.drawable.ic_weather_warning_heat));
        StormSearch.warningList.add(new Warning(getString(R.string.word_wind),"3", getString(R.string.warning_wind_description3), "20.05.2018", "21.05.2018", R.drawable.if_weather_warning_wind));

        StormSearch.warningAdapter.notifyDataSetChanged();
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

        if(!Permissions.Check_FINE_LOCATION(this))
            Permissions.Request_FINE_LOCATION(this, 1);

        if(!Permissions.Check_INTERNET(this))
            Permissions.Request_INTERNET(this, 2);

        if(!Permissions.Check_ACCESS_NETWORK_STATE(this))
            Permissions.Request_ACCESS_NETWORK_STATE(this, 3);

        if(!Permissions.Check_WAKE_LOCK(this))
            Permissions.Request_WAKE_LOCK(this, 4);
    }

    public static boolean CheckGpsStatus(Context context){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Criteria setCriteria()
    {
        // this is done to save the battery life of the device
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        return criteria;
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
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    /*
    // Location Listener start
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    // Location Listener end

    // GoogleApiClient.ConnectionCallbacks Start
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    // GoogleApiClient.ConnectionCallbacks End

    //GoogleApiClient.OnConnectionFailedListener Start
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //GoogleApiClient.OnConnectionFailedListener End
*/
}
