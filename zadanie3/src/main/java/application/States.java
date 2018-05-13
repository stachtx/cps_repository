/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import signals.Signal;

import java.util.ResourceBundle;


public class States {
    
    private static final States instance = new States();
    private ResourceBundle bundle;

    private Signal signal;



    private Signal soundingSignal;
    private Signal reflectedsignal;
    private Signal corelatedSignal;
    public static States getInstance() {
        return instance;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(final Signal signal) {
        this.signal=signal;
    }

    public Signal getSoundingSignal() {
        return soundingSignal;
    }

    public void setSoundingSignal(Signal soundingSignal) {
        this.soundingSignal = soundingSignal;
    }

    public Signal getReflectedsignal() {
        return reflectedsignal;
    }

    public void setReflectedsignal(Signal reflectedsignal) {
        this.reflectedsignal = reflectedsignal;
    }

    public Signal getCorelatedSignal() {
        return corelatedSignal;
    }

    public void setCorelatedSignal(Signal corelatedSignal) {
        this.corelatedSignal = corelatedSignal;
    }

}
