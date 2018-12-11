package com.example.karol.weatherassistant.Services.BurzeDzisService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class MyComplexTypeBurza implements KvmSerializable {

    public int liczba;
    public float odleglosc;
    public String kierunek;
    public int okres;

    public MyComplexTypeBurza(){}

    public MyComplexTypeBurza(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("liczba"))
        {
            Object obj = soapObject.getProperty("liczba");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                liczba = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                liczba = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("odleglosc"))
        {
            Object obj = soapObject.getProperty("odleglosc");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                odleglosc = Float.parseFloat(j.toString());
            }else if (obj!= null && obj instanceof Number){
                odleglosc = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("kierunek"))
        {
            Object obj = soapObject.getProperty("kierunek");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                kierunek = j.toString();
            }else if (obj!= null && obj instanceof String){
                kierunek = (String) obj;
            }
        }
        if (soapObject.hasProperty("okres"))
        {
            Object obj = soapObject.getProperty("okres");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                okres = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                okres = (Integer) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return liczba;
            case 1:
                return odleglosc;
            case 2:
                return kierunek;
            case 3:
                return okres;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "liczba";
                break;
            case 1:
                info.type = Float.class;
                info.name = "odleglosc";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "kierunek";
                break;
            case 3:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "okres";
                break;
        }
    }

    public String getInnerText() {
        return null;
    }


    public void setInnerText(String s) {
    }


    @Override
    public void setProperty(int arg0, Object arg1) {
    }

}
