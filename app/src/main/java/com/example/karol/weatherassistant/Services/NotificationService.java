package com.example.karol.weatherassistant.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.karol.weatherassistant.Services.BurzeDzisService.IWsdl2CodeEvents;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeBurza;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeMiejscowosc;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeOstrzezenia;
import com.example.karol.weatherassistant.Services.BurzeDzisService.serwerSOAPService;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends JobIntentService
{
    static final int JOB_ID = 1000;
    private Timer _timer;
    private TimerTask _timerTask;
    private serwerSOAPService _stormAPI;
    private MyComplexTypeBurza _stormInfo;
    private MyComplexTypeOstrzezenia _warningInfo;
    private MyComplexTypeMiejscowosc _placeInfo;
    private IWsdl2CodeEvents _results;
    private Bundle bundle;



    public static void enqueueWork(Context context, Intent work)
    {
        enqueueWork(context, NotificationService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent)
    {
        bundle = intent.getExtras();
        Log.i("NotificationService", "Executing work: " + intent);
        initializeTimerTask();
        _timer.schedule(_timerTask,0, bundle.getInt("updateFrequency"));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopTimerTask();
        Log.i("NotificationService", "Work complete");
    }

    private void startTimer()
    {
        _timer = new Timer();


    }

    private void initializeTimerTask()
    {
        _timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                _stormAPI = new serwerSOAPService();
                _stormInfo = new MyComplexTypeBurza();
                _warningInfo = new MyComplexTypeOstrzezenia();
                _placeInfo = new MyComplexTypeMiejscowosc();

                _results = new IWsdl2CodeEvents()
                {
                    @Override
                    public void Wsdl2CodeStartedRequest()
                    {

                    }

                    @Override
                    public void Wsdl2CodeFinished(String methodName, Object Data)
                    {
                        switch (methodName)
                        {
                            case "miejscowosc":
                                _placeInfo = (MyComplexTypeMiejscowosc)Data;
                                break;

                            case "ostrzezenia_pogodowe":
                                _warningInfo = (MyComplexTypeOstrzezenia)Data;
                                break;

                            case "szukaj_burzy":
                                _stormInfo = (MyComplexTypeBurza)Data;
                                break;
                        }
                    }

                    @Override
                    public void Wsdl2CodeFinishedWithException(Exception ex)
                    {

                    }

                    @Override
                    public void Wsdl2CodeEndedRequest()
                    {

                    }
                };

                try
                {
                    _stormAPI.miejscowoscAsync(bundle.getString("City"), "3f04fbcac562e34c59d03cc166dc532a9451ded3");

                }
                catch (Exception e)
                {
                    Log.e("miejscowoscAsync", "Exception");
                }
            }
        };
    }

    private void stopTimerTask()
    {
        if(_timer != null)
        {
            _timer.cancel();
            _timer = null;
        }
    }
}
