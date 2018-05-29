
package signals;

import application.SensorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static signals.Filter.*;

public class Sensor {

    //PARAMETRY
    private int    bufferLength;                        // Wielkość buffora
    private double samplingFrequency=0.0;               // Częstotliwość próbkowania sygnałów
    private double raportPeriod;                        // Okres raportowania
    private double signalSpeed;                         // Prędkość rozchodzenia sygnału

    //SYGNAŁY
    private Signal soundingSignal;                      // Sygnał sondujący
    private Signal reflectedSignal;                     // Sygnał odbity
    private Signal corelatedSignal;                     // Sygnał po przeprowadzonej korelacji
    private Signal firstSubSignal;                      // Pierwszy podsygnał
    private Signal secondSubSignal;                     // Drugi podsygnał
    private SensorType type;                            // Typ sensora
    private Target target;                              // Oddalający sięobiekt
    private double distance;                            // Obliczona odległość


    public Sensor(int bufferLength, double samplingFrequency, double raportPeriod, double signalSpeed, SensorType type, Target target,Signal first, Signal second) {
        this.bufferLength = bufferLength;
        this.samplingFrequency = samplingFrequency;
        this.raportPeriod = raportPeriod;
        this.signalSpeed = signalSpeed;
        this.type = type;
        this.target = target;
        this.firstSubSignal=first;
        this.secondSubSignal=second;
        this.convertToSoundingSignal(first, second);
    }

    public double getRaportPeriod() {
        return raportPeriod;
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

    public Target getTarget() {
        return target;
    }

    public double getDistance() {
        return distance;
    }

    public  void spliceCorrelationOfSignals(List <Double> first, List<Double> second){
        List<Double> firstSignalY=first;
        Collections.reverse(firstSignalY);
        corelatedSignal= spliceOfSignals(firstSignalY,second);
    }

    public void directCorrelationOfSignals (List<Double> first,List<Double> second){

        corelatedSignal=new Signal();
        for (int i = -(second.size()-1), j = 0; i < first.size(); i++, j++) {
            double sum = 0.0;

            for (int k = 0; k < first.size(); k++) {
                try {
                    sum += first.get(k) * second.get(k-i);
                } catch (IndexOutOfBoundsException e) { }
            }
            corelatedSignal.getPoints().add(new Point((double) j, sum));
        }

    }

    public  void choose(List <Double> first, List<Double> second){

        switch (type){

            case spliceCorrelation:
                 spliceCorrelationOfSignals(first,second);

            case directCorrelation:
                 directCorrelationOfSignals(first,second);
        }

    }

    public void convertToSoundingSignal ( Signal first, Signal second){
        SignalsCalculator sc=new SignalsCalculator(first,second);
        sc.addSignals();
        soundingSignal=sc.getCalculatedSignal();
    }

    public void  generateReflectedSignal(){
        double delay =target.getObjectPosition()/signalSpeed;
        firstSubSignal.setFirstSampleNr((int) (delay*samplingFrequency));
        secondSubSignal.setFirstSampleNr((int) (delay*samplingFrequency));
        firstSubSignal.getPoints().clear();
        secondSubSignal.getPoints().clear();
        firstSubSignal.generateSignal();
        secondSubSignal.generateSignal();
        SignalsCalculator sc=new SignalsCalculator(firstSubSignal,secondSubSignal);
        sc.addSignals();
        reflectedSignal=sc.getCalculatedSignal();
    }

    public void distanceSensor(){

        this.generateReflectedSignal();
        this.choose(soundingSignal.getAllY().subList(0,bufferLength),reflectedSignal.getAllY().subList(0,bufferLength));
        Point maxValueSample = new Point(0.0, -Double.MAX_VALUE);
        for (int i = corelatedSignal.getPoints().size() / 2; i < corelatedSignal.getPoints().size(); i++) {
            if (corelatedSignal.getPoints().get(i).getY() > maxValueSample.getY()) {
                maxValueSample = corelatedSignal.getPoints().get(i);
            }
        }
        int maxValue = corelatedSignal.getPoints().indexOf(maxValueSample);
        int centerValue=corelatedSignal.getPoints().size()/2;
        double shift= Math.abs(maxValue-centerValue)/samplingFrequency;
        this.distance= shift *signalSpeed;
    }


}

