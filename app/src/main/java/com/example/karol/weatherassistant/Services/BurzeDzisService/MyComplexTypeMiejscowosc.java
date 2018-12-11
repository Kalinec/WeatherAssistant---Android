package com.example.karol.weatherassistant.Services.BurzeDzisService;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class MyComplexTypeMiejscowosc implements KvmSerializable {

    public float y;
    public float x;

    public MyComplexTypeMiejscowosc(){}

    public MyComplexTypeMiejscowosc(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("y"))
        {
            Object obj = soapObject.getProperty("y");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                y = Float.parseFloat(j.toString());
            }else if (obj!= null && obj instanceof Number){
                y = (Integer) obj;
            }
        }
        if (soapObject.hasProperty("x"))
        {
            Object obj = soapObject.getProperty("x");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                x = Float.parseFloat(j.toString());
            }else if (obj!= null && obj instanceof Number){
                x = (Integer) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return y;
            case 1:
                return x;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = Float.class;
                info.name = "y";
                break;
            case 1:
                info.type = Float.class;
                info.name = "x";
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


