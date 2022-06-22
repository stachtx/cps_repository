/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import signals.Sensor;
import signals.Signal;

import java.util.ResourceBundle;


public class States {
    
    private static final States instance = new States();
    private ResourceBundle bundle;

    private Signal signal;
    private Signal secondSignal;
    private Sensor sensor;


    public static States getInstance() {
        return instance;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(final Signal signal) {
        this.signal=signal;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Signal getSecondSignal() {
        return secondSignal;
    }

    public void setSecondSignal(Signal secondSignal) {
        this.secondSignal = secondSignal;
    }

}
