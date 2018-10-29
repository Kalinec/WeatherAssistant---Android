package com.example.karol.weatherassistant.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
    private Timer _timer;
    private TimerTask _timerTask;
    private serwerSOAPService _stormAPI;
    private MyComplexTypeBurza _stormInfo;
    private MyComplexTypeOstrzezenia _warningInfo;
    private MyComplexTypeMiejscowosc _placeInfo;
    private IWsdl2CodeEvents _results;
    private Bundle bundle;
    private final String CHANNEL_ID_STORMS = "STORMS";
    private final String CHANNEL_ID_WARNINGS = "WARNINGS";
    private NotificationManagerCompat _notificationManager;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        bundle = intent.getExtras();
        Log.i("NotificationService", "Executing work: " + intent);
        createNotificationChannel();
        _notificationManager = NotificationManagerCompat.from(getApplicationContext());
       // startTimer();
       // initializeTimerTask();
       // _timer.schedule(_timerTask, 10, 15);
        //_timer.schedule(_timerTask,0, bundle.getInt("updateFrequency"));


        Log.i("NotificationService", "Started run method");
        //Toast.makeText(getApplicationContext(),"Service zaczal dzialac", Toast.LENGTH_SHORT);
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
                                    Integer.valueOf(bundle.getInt("Radius"))
                                    ,"3f04fbcac562e34c59d03cc166dc532a9451ded3");


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
/*
                                NotificationCompat.Builder _builderWarningNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                        .setSmallIcon(R.drawable.if_cloudy)
                                        .setContentTitle("Wydano ostrzeżenie pogodowe")
                                        .setContentText("Silne wiatry, poziom 2")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                _notificationManager.notify(0, _builderWarningNotification.build());

                               /* for(int i=0; i<6; i++)
                                {
                                    if(bundle.getIntArray("WeatherWarnings")[i] == 1)
                                    {
                                        if(i == 0)
                                        {

                                        }
                                    }
                                } */

                        break;

                    case "szukaj_burzy":
                        _stormInfo = (MyComplexTypeBurza) Data;

                        if (_stormInfo.liczba != 0)
                        {
                            NotificationCompat.Builder _builderStormNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_WARNINGS)
                                    .setSmallIcon(R.drawable.if_cloudy)
                                    .setContentTitle("Wykryto wyładowanie atmosferyczne!")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Czas: " + _stormInfo.okres + "min" + "\nLiczba: " + _stormInfo.liczba + "\nDystans: " + _stormInfo.odleglosc + " km/h\nKierunek: " + _stormInfo.kierunek))
                                    .setDefaults(DEFAULT_LIGHTS | DEFAULT_SOUND | DEFAULT_VIBRATE)
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setVisibility(VISIBILITY_PUBLIC);

                            _notificationManager.notify(1, _builderStormNotification.build());
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

        try {
            _stormAPI.miejscowoscAsync(bundle.getString("City"), "3f04fbcac562e34c59d03cc166dc532a9451ded3");

        } catch (Exception e) {
            Log.e("miejscowoscAsync", "Exception" + e.getMessage());
        }

        Log.i("NotificationService", "Ended run method");
        //Toast.makeText(getApplicationContext(),"Service przestal dzialac", Toast.LENGTH_SHORT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // stopTimerTask();
        Log.i("NotificationService", "Work complete");
    }

   /* private void startTimer() {
        _timer = new Timer();


    } */

    private void initializeTimerTask() {
        _timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
    }

    private void stopTimerTask() {
        if (_timer != null) {
            _timer.cancel();
            _timer = null;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Storms channel
            NotificationChannel channelStorms = new NotificationChannel(CHANNEL_ID_STORMS, "Storms", NotificationManager.IMPORTANCE_HIGH);
            channelStorms.setDescription("Information about detected storms");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager stormsNotificationManager = getSystemService(NotificationManager.class);
            stormsNotificationManager.createNotificationChannel(channelStorms);


            //Warnings channel
            NotificationChannel channelWarnings = new NotificationChannel(CHANNEL_ID_WARNINGS, "Warnings", NotificationManager.IMPORTANCE_HIGH);
            channelWarnings.setDescription("Information about detected warnings");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelWarnings);
        }
    }
}
