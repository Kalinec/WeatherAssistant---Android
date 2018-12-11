package com.example.karol.weatherassistant.Services.BurzeDzisService;

import com.example.karol.weatherassistant.Services.BurzeDzisService.WS_Enums.*;
import com.example.karol.weatherassistant.View.StormSearch;

import java.util.List;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.HeaderProperty;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.serialization.MarshalFloat;

import static android.content.ContentValues.TAG;

public class serwerSOAPService {

    public String NAMESPACE ="https://burze.dzis.net/soap.php";
    public String url="";
    public int timeOut = 1000;
    public IWsdl2CodeEvents eventHandler;
    public SoapProtocolVersion soapVersion;

    public serwerSOAPService(){}

    public serwerSOAPService(IWsdl2CodeEvents eventHandler)
    {
        this.eventHandler = eventHandler;
    }
    public serwerSOAPService(IWsdl2CodeEvents eventHandler,String url)
    {
        this.eventHandler = eventHandler;
        this.url = url;
    }
    public serwerSOAPService(IWsdl2CodeEvents eventHandler,String url,int timeOutInSeconds)
    {
        this.eventHandler = eventHandler;
        this.url = url;
        this.setTimeOut(timeOutInSeconds);
    }
    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void KeyAPIAsync(String klucz) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        KeyAPIAsync(klucz, null);
    }

    public void KeyAPIAsync(final String klucz,final List<HeaderProperty> headers) throws Exception{

        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected Boolean doInBackground(Void... params) {
                return KeyAPI(klucz, headers);
            }
            @Override
            protected void onPostExecute(Boolean result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("KeyAPI", result);
                }
            }
        }.execute();
    }

    public boolean KeyAPI(String klucz){
        return KeyAPI(klucz, null);
    }

    public boolean KeyAPI(String klucz,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("https://burze.dzis.net/soap.php","KeyAPI");
        soapReq.addProperty("klucz",klucz);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("https://burze.dzis.net/soap.php/KeyAPI", soapEnvelope,headers);
            }else{
                httpTransport.call("https://burze.dzis.net/soap.php/KeyAPI", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                        SoapPrimitive j =(SoapPrimitive) obj;
                        boolean resultVariable = Boolean.parseBoolean(j.toString());
                        return resultVariable;
                    }else if (obj!= null && obj instanceof Boolean){
                        boolean resultVariable = (Boolean) obj;
                        return resultVariable;
                    }
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return false;
    }

    public void miejscowoscAsync(String nazwa,String klucz) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        miejscowoscAsync(nazwa, klucz, null);
    }

    public void miejscowoscAsync(final String nazwa,final String klucz,final List<HeaderProperty> headers) throws Exception{

        new AsyncTask<Void, Void, MyComplexTypeMiejscowosc>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected MyComplexTypeMiejscowosc doInBackground(Void... params) {
                return miejscowosc(nazwa, klucz, headers);
            }
            @Override
            protected void onPostExecute(MyComplexTypeMiejscowosc result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("miejscowosc", result);
                }
            }
        }.execute();
    }

    public MyComplexTypeMiejscowosc miejscowosc(String nazwa,String klucz){
        return miejscowosc(nazwa, klucz, null);
    }

    public MyComplexTypeMiejscowosc miejscowosc(String nazwa,String klucz,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("https://burze.dzis.net/soap.php","miejscowosc");
        soapReq.addProperty("nazwa",nazwa);
        soapReq.addProperty("klucz",klucz);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("https://burze.dzis.net/soap.php/miejscowosc", soapEnvelope,headers);
            }else{
                httpTransport.call("https://burze.dzis.net/soap.php/miejscowosc", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapObject j = (SoapObject)obj;
                    MyComplexTypeMiejscowosc resultVariable =  new MyComplexTypeMiejscowosc (j);
                    return resultVariable;

                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }

    public void ostrzezenia_pogodoweAsync(float y,float x,String klucz) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        ostrzezenia_pogodoweAsync(y, x, klucz, null);
    }

    public void ostrzezenia_pogodoweAsync(final float y,final float x,final String klucz,final List<HeaderProperty> headers) throws Exception{

        new AsyncTask<Void, Void, MyComplexTypeOstrzezenia>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected MyComplexTypeOstrzezenia doInBackground(Void... params) {
                return ostrzezenia_pogodowe(y, x, klucz, headers);
            }
            @Override
            protected void onPostExecute(MyComplexTypeOstrzezenia result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("ostrzezenia_pogodowe", result);
                }
            }
        }.execute();
    }

    public MyComplexTypeOstrzezenia ostrzezenia_pogodowe(float y,float x,String klucz){
        return ostrzezenia_pogodowe(y, x, klucz, null);
    }

    public MyComplexTypeOstrzezenia ostrzezenia_pogodowe(float y,float x,String klucz,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("https://burze.dzis.net/soap.php","ostrzezenia_pogodowe");
        MarshalFloat marshalFloat = new MarshalFloat();
        marshalFloat.register(soapEnvelope);
        soapReq.addProperty("y",y);
        soapReq.addProperty("x",x);
        soapReq.addProperty("klucz",klucz);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("https://burze.dzis.net/soap.php/ostrzezenia_pogodowe", soapEnvelope,headers);
            }else{
                httpTransport.call("https://burze.dzis.net/soap.php/ostrzezenia_pogodowe", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapObject j = (SoapObject)obj;
                    MyComplexTypeOstrzezenia resultVariable =  new MyComplexTypeOstrzezenia (j);
                    return resultVariable;

                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }

    public void szukaj_burzyAsync(String y,String x,int promien,String klucz) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        szukaj_burzyAsync(y, x, promien, klucz, null);
    }

    public void szukaj_burzyAsync(final String y,final String x,final int promien,final String klucz,final List<HeaderProperty> headers) throws Exception{

        new AsyncTask<Void, Void, MyComplexTypeBurza>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected MyComplexTypeBurza doInBackground(Void... params) {
                return szukaj_burzy(y, x, promien, klucz, headers);
            }
            @Override
            protected void onPostExecute(MyComplexTypeBurza result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("szukaj_burzy", result);
                }
            }
        }.execute();
    }

    public MyComplexTypeBurza szukaj_burzy(String y,String x,int promien,String klucz){
        return szukaj_burzy(y, x, promien, klucz, null);
    }

    public MyComplexTypeBurza szukaj_burzy(String y,String x,int promien,String klucz,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("https://burze.dzis.net/soap.php","szukaj_burzy");
        soapReq.addProperty("y",y);
        soapReq.addProperty("x",x);
        soapReq.addProperty("promien",promien);
        soapReq.addProperty("klucz",klucz);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("https://burze.dzis.net/soap.php/szukaj_burzy", soapEnvelope,headers);
            }else{
                httpTransport.call("https://burze.dzis.net/soap.php/szukaj_burzy", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    SoapObject j = (SoapObject)obj;
                    MyComplexTypeBurza resultVariable =  new MyComplexTypeBurza (j);
                    return resultVariable;
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return null;
    }

    public void miejscowosci_listaAsync(String nazwa,String kraj,String klucz) throws Exception{
        if (this.eventHandler == null)
            throw new Exception("Async Methods Requires IWsdl2CodeEvents");
        miejscowosci_listaAsync(nazwa, kraj, klucz, null);
    }

    public void miejscowosci_listaAsync(final String nazwa,final String kraj,final String klucz,final List<HeaderProperty> headers) throws Exception{

        new AsyncTask<Void, Void, String>(){
            @Override
            protected void onPreExecute() {
                eventHandler.Wsdl2CodeStartedRequest();
            };
            @Override
            protected String doInBackground(Void... params) {
                return miejscowosci_lista(nazwa, kraj, klucz, headers);
            }
            @Override
            protected void onPostExecute(String result)
            {
                eventHandler.Wsdl2CodeEndedRequest();
                if (result != null){
                    eventHandler.Wsdl2CodeFinished("miejscowosci_lista", result);
                }
            }
        }.execute();
    }

    public String miejscowosci_lista(String nazwa,String kraj,String klucz){
        return miejscowosci_lista(nazwa, kraj, klucz, null);
    }

    public String miejscowosci_lista(String nazwa,String kraj,String klucz,List<HeaderProperty> headers){
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("https://burze.dzis.net/soap.php","miejscowosci_lista");
        soapReq.addProperty("nazwa",nazwa);
        soapReq.addProperty("kraj",kraj);
        soapReq.addProperty("klucz",klucz);
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            if (headers!=null){
                httpTransport.call("https://burze.dzis.net/soap.php/miejscowosci_lista", soapEnvelope,headers);
            }else{
                httpTransport.call("https://burze.dzis.net/soap.php/miejscowosci_lista", soapEnvelope);
            }
            Object retObj = soapEnvelope.bodyIn;
            if (retObj instanceof SoapFault){
                SoapFault fault = (SoapFault)retObj;
                Exception ex = new Exception(fault.faultstring);
                if (eventHandler != null)
                    eventHandler.Wsdl2CodeFinishedWithException(ex);
            }else{
                SoapObject result=(SoapObject)retObj;
                if (result.getPropertyCount() > 0){
                    Object obj = result.getProperty(0);
                    if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                        SoapPrimitive j =(SoapPrimitive) obj;
                        String resultVariable = j.toString();
                        return resultVariable;
                    }else if (obj!= null && obj instanceof String){
                        String resultVariable = (String) obj;
                        return resultVariable;
                    }
                }
            }
        }catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }
        return "";
    }

}
