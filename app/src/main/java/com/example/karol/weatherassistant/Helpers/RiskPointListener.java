package com.example.karol.weatherassistant.Helpers;

public class RiskPointListener
{
    public int riskPoint = 0;
    private onValueChangeListener valueChangeListener;

    public int getValue()
    {
        return riskPoint;
    }

    public void setVariable(int value) {
        riskPoint = value;
        if (valueChangeListener != null) valueChangeListener.onChange();
    }

    public onValueChangeListener getValueChangeListener() {

        return valueChangeListener;
    }

    public void setValueChangeListener(onValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public interface onValueChangeListener {
        void onChange();
    }
}
