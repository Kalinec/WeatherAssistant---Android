package com.example.karol.weatherassistant.Services.BurzeDzisService;

public interface IWsdl2CodeEvents {
    public void Wsdl2CodeStartedRequest();
    public void Wsdl2CodeFinished(String methodName,Object Data);
    public void Wsdl2CodeFinishedWithException(Exception ex);
    public void Wsdl2CodeEndedRequest();
}