package com.example.karol.weatherassistant.Services.BurzeDzisService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class MyComplexTypeOstrzezenia implements KvmSerializable {

    public String od_dnia;
    public String do_dnia;
    public int mroz;
    public String mroz_od_dnia;
    public String mroz_do_dnia;
    public int upal;
    public String upal_od_dnia;
    public String upal_do_dnia;
    public int wiatr;
    public String wiatr_od_dnia;
    public String wiatr_do_dnia;
    public int opad;
    public String opad_od_dnia;
    public String opad_do_dnia;
    public int burza;
    public String burza_od_dnia;
    public String burza_do_dnia;
    public int traba;
    public String traba_od_dnia;
    public String traba_do_dnia;

    public MyComplexTypeOstrzezenia(){}

    public MyComplexTypeOstrzezenia(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("od_dnia"))
        {
            Object obj = soapObject.getProperty("od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("do_dnia"))
        {
            Object obj = soapObject.getProperty("do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("mroz"))
        {
            Object obj = soapObject.getProperty("mroz");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                mroz = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                mroz = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("mroz_od_dnia"))
        {
            Object obj = soapObject.getProperty("mroz_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                mroz_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                mroz_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("mroz_do_dnia"))
        {
            Object obj = soapObject.getProperty("mroz_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                mroz_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                mroz_do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("upal"))
        {
            Object obj = soapObject.getProperty("upal");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                upal = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                upal = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("upal_od_dnia"))
        {
            Object obj = soapObject.getProperty("upal_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                upal_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                upal_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("upal_do_dnia"))
        {
            Object obj = soapObject.getProperty("upal_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                upal_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                upal_do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("wiatr"))
        {
            Object obj = soapObject.getProperty("wiatr");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                wiatr = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                wiatr = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("wiatr_od_dnia"))
        {
            Object obj = soapObject.getProperty("wiatr_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                wiatr_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                wiatr_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("wiatr_do_dnia"))
        {
            Object obj = soapObject.getProperty("wiatr_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                wiatr_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                wiatr_do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("opad"))
        {
            Object obj = soapObject.getProperty("opad");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                opad = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                opad = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("opad_od_dnia"))
        {
            Object obj = soapObject.getProperty("opad_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                opad_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                opad_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("opad_do_dnia"))
        {
            Object obj = soapObject.getProperty("opad_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                opad_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                opad_do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("burza"))
        {
            Object obj = soapObject.getProperty("burza");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                burza = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                burza = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("burza_od_dnia"))
        {
            Object obj = soapObject.getProperty("burza_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                burza_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                burza_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("burza_do_dnia"))
        {
            Object obj = soapObject.getProperty("burza_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                burza_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                burza_do_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("traba"))
        {
            Object obj = soapObject.getProperty("traba");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                traba = Integer.parseInt(j.toString());
            }else if (obj!= null && obj instanceof Number){
                traba = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("traba_od_dnia"))
        {
            Object obj = soapObject.getProperty("traba_od_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                traba_od_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                traba_od_dnia = (String) obj;
            }
        }
        if (soapObject.hasProperty("traba_do_dnia"))
        {
            Object obj = soapObject.getProperty("traba_do_dnia");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                traba_do_dnia = j.toString();
            }else if (obj!= null && obj instanceof String){
                traba_do_dnia = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return od_dnia;
            case 1:
                return do_dnia;
            case 2:
                return mroz;
            case 3:
                return mroz_od_dnia;
            case 4:
                return mroz_do_dnia;
            case 5:
                return upal;
            case 6:
                return upal_od_dnia;
            case 7:
                return upal_do_dnia;
            case 8:
                return wiatr;
            case 9:
                return wiatr_od_dnia;
            case 10:
                return wiatr_do_dnia;
            case 11:
                return opad;
            case 12:
                return opad_od_dnia;
            case 13:
                return opad_do_dnia;
            case 14:
                return burza;
            case 15:
                return burza_od_dnia;
            case 16:
                return burza_do_dnia;
            case 17:
                return traba;
            case 18:
                return traba_od_dnia;
            case 19:
                return traba_do_dnia;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 20;
    }

    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "od_dnia";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "do_dnia";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "mroz";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mroz_od_dnia";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "mroz_do_dnia";
                break;
            case 5:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "upal";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "upal_od_dnia";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "upal_do_dnia";
                break;
            case 8:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "wiatr";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "wiatr_od_dnia";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "wiatr_do_dnia";
                break;
            case 11:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "opad";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "opad_od_dnia";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "opad_do_dnia";
                break;
            case 14:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "burza";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "burza_od_dnia";
                break;
            case 16:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "burza_do_dnia";
                break;
            case 17:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "traba";
                break;
            case 18:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "traba_od_dnia";
                break;
            case 19:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "traba_do_dnia";
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
