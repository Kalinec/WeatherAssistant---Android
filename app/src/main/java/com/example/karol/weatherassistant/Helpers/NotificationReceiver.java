package com.example.karol.weatherassistant.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.karol.weatherassistant.Services.NotificationService;

public class NotificationReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationService.enqueueWork(context,intent);
    }
}
