package com.example.karol.weatherassistant.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.Services.BurzeDzisService.IWsdl2CodeEvents;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeBurza;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeMiejscowosc;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeOstrzezenia;
import com.example.karol.weatherassistant.Services.BurzeDzisService.serwerSOAPService;
import com.example.karol.weatherassistant.View.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.app.NotificationCompat.DEFAULT_LIGHTS;
import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class NotificationService extends JobIntentService {
    static final int JOB_ID = 1000;
    private serwerSOAPService _stormAPI;
    private MyComplexTypeBurza _stormInfo;
    private MyComplexTypeOstrzezenia _warningInfo;
    private MyComplexTypeMiejscowosc _placeInfo;
    private IWsdl2CodeEvents _results;
    private Bundle bundle;
    private final String CHANNEL_ID_STORMS = "STORMS";
    private final String CHANNEL_ID_WARNINGS = "WARNINGS";
    private NotificationManagerCompat _notificationManager;

   /* //GPS FIELDS
    private LocationListener _locationListener;
    private Location _location;
    private Criteria _criteria;
    private LocationManager _locationManager;
    private final Looper looper = null;
    */

    //GPS FIELDS newer
    private FusedLocationProviderClient _fusedLocationProviderClient;
    private LocationRequest _locationRequest;
    private LocationCallback _locationCallback;

    Handler handler;
    @Override
    public void onCreate() {
        // Handler will get associated with the current thread,
        // which is the main thread.
        handler = new Handler();
        super.onCreate();
    }

   private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        bundle = intent.getExtras();
        Log.i("NotificationService", "Executing work: " + intent);
        createNotificationChannel();
        _notificationManager = NotificationManagerCompat.from(getApplicationContext());


        Log.i("NotificationService", "Started run method");
        _results = new IWsdl2CodeEvents() {
            @Override
            public void Wsdl2CodeStartedRequest() {

            }

            @Override
            public void Wsdl2CodeFinished(String methodName, Object Data) {
                switch (methodName) {
                    case "miejscowosc":
                        _placeInfo = (MyComplexTypeMiejscowosc) Data;

                        try
                        {
                            _stormAPI.szukaj_burzyAsync(
                                    String.valueOf(_placeInfo.y),
                                    String.valueOf(_placeInfo.x),
                                    Integer.valueOf(bundle.getInt("Radius")),
                                    "3f04fbcac562e34c59d03cc166dc532a9451ded3");


                            _stormAPI.ostrzezenia_pogodoweAsync(
                                    _placeInfo.y,
                                    _placeInfo.x,
                                    "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                        }
                        catch (Exception e)
                        {
                            Log.e("Notification Service : ", "Trying resolve szukaj_burzyAsync and ostrzezenia_pogodoweAsync methods", e);
                        }


                        break;

                    case "ostrzezenia_pogodowe":
                        _warningInfo = (MyComplexTypeOstrzezenia) Data;
                        int[] choosedWeatherWarningsToTrack = bundle.getIntArray("WeatherWarnings");


                        //frost
                        if(choosedWeatherWarningsToTrack[0] != 0)
                        {
                            if(_warningInfo.mroz != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Mrozy, zagrożenie ");
                                contentTitle.append(_warningInfo.mroz);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.mroz == 1)
                                    contentText.append(getString(R.string.warning_frost_description1));
                                else if(_warningInfo.mroz == 2)
                                    contentText.append(getString(R.string.warning_frost_description2));
                                else
                                    contentText.append(getString(R.string.warning_frost_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.ic_weather_warning_frost)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(0, _builderWarningNotification.build());
                            }
                        }

                        if(choosedWeatherWarningsToTrack[1] != 0)
                        {
                            if(_warningInfo.upal != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Upały, zagrożenie ");
                                contentTitle.append(_warningInfo.upal);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.upal == 1)
                                    contentText.append(getString(R.string.warning_heat_description1));
                                else if(_warningInfo.upal == 2)
                                    contentText.append(getString(R.string.warning_heat_description2));
                                else
                                    contentText.append(getString(R.string.warning_heat_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.ic_weather_warning_heat)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(1, _builderWarningNotification.build());
                            }

                        }

                        if(choosedWeatherWarningsToTrack[2] != 0)
                        {
                            if(_warningInfo.wiatr != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Silny wiatr, zagrożenie ");
                                contentTitle.append(_warningInfo.wiatr);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.wiatr == 1)
                                    contentText.append(getString(R.string.warning_wind_description1));
                                else if(_warningInfo.wiatr == 2)
                                    contentText.append(getString(R.string.warning_wind_description2));
                                else
                                    contentText.append(getString(R.string.warning_wind_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.if_weather_warning_wind)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(2, _builderWarningNotification.build());
                            }
                        }

                        if(choosedWeatherWarningsToTrack[3] != 0)
                        {
                            if(_warningInfo.opad != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Duże opady, zagrożenie ");
                                contentTitle.append(_warningInfo.opad);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.opad == 1)
                                    contentText.append(getString(R.string.warning_rain_description1));
                                else if(_warningInfo.opad == 2)
                                    contentText.append(getString(R.string.warning_rain_description2));
                                else
                                    contentText.append(getString(R.string.warning_rain_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.if_weather_warning_rain)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(3, _builderWarningNotification.build());
                            }
                        }

                        if(choosedWeatherWarningsToTrack[4] != 0)
                        {
                            if(_warningInfo.burza != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Silne burze, zagrożenie ");
                                contentTitle.append(_warningInfo.burza);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.burza == 1)
                                    contentText.append(getString(R.string.warning_storm_description1));
                                else if(_warningInfo.burza == 2)
                                    contentText.append(getString(R.string.warning_storm_description2));
                                else
                                    contentText.append(getString(R.string.warning_storm_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.if_weather_warning_storm)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(4, _builderWarningNotification.build());
                            }
                        }

                        if(choosedWeatherWarningsToTrack[5] != 0)
                        {
                            if(_warningInfo.traba != 0)
                            {
                                StringBuilder contentTitle = new StringBuilder();
                                contentTitle.append("Trąby powietrzne, zagrożenie ");
                                contentTitle.append(_warningInfo.traba);
                                contentTitle.append(" stopnia!");

                                StringBuilder contentText = new StringBuilder();
                                if(_warningInfo.traba == 1)
                                    contentText.append(getString(R.string.warning_tornado_description1));
                                else if(_warningInfo.traba == 2)
                                    contentText.append(getString(R.string.warning_tornado_description2));
                                else
                                    contentText.append(getString(R.string.warning_tornado_description3));


                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.if_weather_warning_tornado)
                                        .setContentTitle(contentTitle)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(contentText))
                                        .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_MAX)
                                        .setVisibility(VISIBILITY_PUBLIC);

                                _notificationManager.notify(5, _builderWarningNotification.build());
                            }
                        }

                        break;

                    case "szukaj_burzy":
                        _stormInfo = (MyComplexTypeBurza) Data;

                        if (_stormInfo.liczba != 0)
                        {
                            NotificationCompat.Builder _builderStormNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_STORMS)
                                    .setSmallIcon(R.drawable.if_thunderbolt)
                                    .setContentTitle("Wykryto wyładowanie atmosferyczne!")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Czas: " + _stormInfo.okres + "min" +
                                                    "\nLiczba: " + _stormInfo.liczba +
                                                    "\nDystans: " + _stormInfo.odleglosc +
                                                    " km/h\nKierunek: " + _stormInfo.kierunek))
                                    .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setVisibility(VISIBILITY_PUBLIC);

                            _notificationManager.notify(6, _builderStormNotification.build());
                        }

                        break;
                }
            }

            @Override
            public void Wsdl2CodeFinishedWithException(Exception ex) {

            }

            @Override
            public void Wsdl2CodeEndedRequest() {

            }
        };
        _stormAPI = new serwerSOAPService(_results, "https://burze.dzis.net/soap.php");
        _stormInfo = new MyComplexTypeBurza();
        _warningInfo = new MyComplexTypeOstrzezenia();
        _placeInfo = new MyComplexTypeMiejscowosc();

        if(bundle.containsKey("City"))
        {
            try
            {
                _stormAPI.miejscowoscAsync(bundle.getString("City"), "3f04fbcac562e34c59d03cc166dc532a9451ded3");

            } catch (Exception e)
            {
                Log.e("miejscowoscAsync", "Exception" + e.getMessage());
            }
        }

        else
        {
            _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            _locationRequest = new LocationRequest();
            _locationRequest.setInterval(1000);
            _locationRequest.setFastestInterval(0);
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
                        String convertedLatitude = MainActivity.decimalToDM(location.getLatitude());
                        String convertedLongitude = MainActivity.decimalToDM(location.getLongitude());
                        try
                        {
                            _stormAPI.szukaj_burzyAsync(
                                    convertedLatitude,
                                    convertedLongitude,
                                    Integer.valueOf(bundle.getInt("Radius")),
                                    "3f04fbcac562e34c59d03cc166dc532a9451ded3");

                            _stormAPI.ostrzezenia_pogodoweAsync(
                                    Float.valueOf(convertedLatitude),
                                    Float.valueOf(convertedLongitude),
                                    "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                        }
                        catch (Exception e)
                        {
                            Log.e("Notification Service : ", "Trying resolve szukaj_burzyAsync and ostrzezenia_pogodoweAsync methods", e);
                        }
                    }
                }
            };

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        _fusedLocationProviderClient.requestLocationUpdates(_locationRequest, _locationCallback, null);
                    }
                    catch (SecurityException e)
                    {
                        Log.e("GPS SECURITY EXCEPTION", e.getMessage());
                    }
                }
            });
        }

        Log.i("NotificationService", "Ended run method");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NotificationService", "Work complete");
    }

    private void createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //Storms channel
            NotificationChannel channelStorms = new NotificationChannel(CHANNEL_ID_STORMS, "Storms", NotificationManager.IMPORTANCE_HIGH);
            channelStorms.setDescription("Information about detected storms");
            NotificationManager stormsNotificationManager = getSystemService(NotificationManager.class);
            stormsNotificationManager.createNotificationChannel(channelStorms);

            //Warnings channel
            NotificationChannel channelWarnings = new NotificationChannel(CHANNEL_ID_WARNINGS, "Warnings", NotificationManager.IMPORTANCE_HIGH);
            channelWarnings.setDescription("Information about detected warnings");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelWarnings);
        }
    }

   /* private void initializeLocalizer()
    {
        _criteria = new Criteria();
        setCriteria();
        _locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                _location = location;
                Log.d("Location changes", location.toString());

                try
                {
                    _stormAPI.szukaj_burzyAsync(
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),
                            Integer.valueOf(bundle.getInt("Radius")),
                            "3f04fbcac562e34c59d03cc166dc532a9451ded3");

                    _stormAPI.ostrzezenia_pogodoweAsync(
                            (float) location.getLatitude(),
                            (float) location.getLongitude(),
                            "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                }
                catch (Exception e)
                {
                    Log.e("Notification Service : ", "Trying resolve szukaj_burzyAsync and ostrzezenia_pogodoweAsync methods", e);
                }

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
        };

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    _locationManager.requestSingleUpdate(_criteria, _locationListener, looper);
                }
                catch (SecurityException e)
                {
                    Log.e("GPS SECURITY EXCEPTION", e.getMessage());
                }
            }
        });

    } */

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
