
package signals;

import application.SensorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sensor {

    private int    bufferLength;                        // Wielkość buffora
    private double samplingFrequency=0.0;           // Częstotliwość próbkowania sygnałów
    private double raportPeriod;                    // Okres raportowania
    private double sondagePeriod;                   // Okres sygnału sondującego
    private double signalSpeed;                     // Prędkość rozchodzenia sygnału

    private Signal soundingSignal;                  // Sygnał sondujący
    private Signal reflectedSignal;                 // Sygnał odbity
    private Signal corelatedSignal;                 // Sygnał po przeprowadzonej korelacji
    private SensorType type;                        // typ sensoa
    private Target target;
    private double distance;



    public void setBufferLength(int bufferLength) {
        this.bufferLength = bufferLength;
    }

    public void setSamplingFrequency(double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
    }

    public void setRaportPeriod(double raportPeriod) {
        this.raportPeriod = raportPeriod;
    }

    public void setSondagePeriod(double sondagePeriod) {
        this.sondagePeriod = sondagePeriod;
    }

    public void setSignalSpeed(double signalSpeed) {
        this.signalSpeed = signalSpeed;
    }

    public Signal getSoundingSignal() {
        return soundingSignal;
    }

    public Signal getReflectedSignal() {
        return reflectedSignal;
    }

    public Signal getCorelatedSignal() {
        return corelatedSignal;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
/*
    public  Signal spliceCorrelationOfSignals(List<Double> first, Signal second){
*/
/*      List <Double> listY = new ArrayList<>();
        for(Double i : second){
            listY.add(i);
        }
        Collections.reverse(listY);
        return Filter.spliceOfSignals(first,second);*//*
 return null;
    }

    public Signal directCorrelationOfSignals (Signal first, Signal second){
*/
/*
        Signal corelatedSignal = new Signal();
        int M=first.size();
        int N=input.size();
        double sum;
        for(int n = (int) Math.floor((M+N-1)/(-2)); n<M+N; n++) {
            sum=0.0;
            for (int k =0 ; k < M; k++) {
                if(n-k<0){
                    sum+=0;
                } else{
                    sum+= first.get(k)*input.get(n+k);
                }
            }
            corelatedSignal.getPoints().add(new Point((double) n, sum));
        }
        return corelatedSignal;
*//*
 return null;
    }

    public  Signal choose(SensorType type, Signal first, Signal second){

        switch (type){

            case spliceCorrelation:
                return spliceCorrelationOfSignals(first,second);

            case directCorrelation:
                return directCorrelationOfSignals(first,second);
            default:
                return null;

        }

    }

    public void setSoundingSignal ( Signal first, Signal second){
        SignalsCalculator sc=new SignalsCalculator(first,second);
        sc.addSignals();
        soundingSignal=sc.getCalculatedSignal();
    }

    public void  setReflectedSignal(){
        reflectedSignal= SignalOperations.copy(soundingSignal);
        Double delay = target.getObjectPosition()/signalSpeed;
        for(Point i : reflectedSignal.getPoints()){

            i.setX(i.getX()*samplingFrequency-delay);
        }
    }

    public Double distanceSensor(){


        this.choose(type,soundingSignal,reflectedSignal);

        Double maxValue = Collections.max(corelatedSignal.getAllY());

        Double centerValue=corelatedSignal.getPoints().get(Math.round(corelatedSignal.getPoints().size()/2)).getX();

        Double shift= Math.abs(maxValue-centerValue);

        return shift *signalSpeed;
    }
*/
    }

