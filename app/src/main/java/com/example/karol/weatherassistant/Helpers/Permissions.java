package com.example.karol.weatherassistant.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permissions
{
    //Request Permisson
    public static void Request_INTERNET(Activity act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.INTERNET},code);
    }

    public static void Request_ACCESS_NETWORK_STATE(Activity act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.ACCESS_NETWORK_STATE}, code);
    }

    public static void Request_WAKE_LOCK(Activity act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.WAKE_LOCK}, code);
    }

    public static void Request_STORAGE(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},code);
    }

    public static void Request_COARSE_LOCATION(Activity act, int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, code);
    }
    public static void Request_FINE_LOCATION(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
    }
    public static void Request_READ_SMS(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_SMS},code);
    }
    public static void Request_READ_CONTACTS(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_CONTACTS},code);
    }
    public static void Request_READ_CALENDAR(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_CALENDAR},code);
    }
    public static void Request_RECORD_AUDIO(Activity act,int code)
    {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.RECORD_AUDIO},code);
    }

    //Check Permisson
    public static boolean Check_INTERNET(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.INTERNET);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_ACCESS_NETWORK_STATE(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_NETWORK_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_WAKE_LOCK(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.WAKE_LOCK);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_STORAGE(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_CAMERA(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_COARSE_LOCATION(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_FINE_LOCATION(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_READ_SMS(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_READ_CONTACTS(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_READ_CALENDAR(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CALENDAR);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean Check_RECORD_AUDIO(Activity act)
    {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
