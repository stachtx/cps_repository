package signals;

import application.SensorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sensor {

    private int bufferLength;                        // Wielkość buffora
    private double samplingFrequency=0.0;           // Częstotliwość próbkowania sygnałów
    private double raportPeriod;                    // Okres raportowania
    private double sondagePeriod;                   // Okres sygnału sondującego
    private double signalSpeed;                     // Prędkość rozchodzenia sygnału



    private Signal soundingSignal;                  // Sygnał sondujący
    private Signal reflectedSignal;                 // Sygnał odbity
    private Signal corelatedSignal;                 // Sygnał po przeprowadzonej korelacji
    private Target target;                          //Oddalający się obiekt


    public  Signal spliceCorrelationOfSignals(List<Double> first, List <Double> second){
        List <Double> listY = new ArrayList<>();
        for(Double i : second){
            listY.add(i);
        }
        Collections.reverse(listY);
        return Filter.spliceOfSignals(first,second);
    }

    public Signal directCorrelationOfSignals (List<Double> first, List<Double> input){

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

    }

    public  Signal choose(SensorType type, List <Double> first,List <Double>  second){

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

    public void  setReflectedSignal(Target target){
        reflectedSignal= SignalOperations.copy(soundingSignal);
        Double delay = target.getObjectPosition()/signalSpeed;
        for(Point i : reflectedSignal.getPoints()){

            i.setX(i.getX()*samplingFrequency-delay);
        }
    }

    public Double distanceSensor(SensorType type, Target target){

        target.run();
        List <Double> soundingSignalBuffer = new ArrayList<>();
        List <Double> reflectedSignalBuffer = new ArrayList<>();

        for (int i=0;i<bufferLength; i++){
            soundingSignalBuffer.add(soundingSignal.getPoints().get(i).getX());
            reflectedSignalBuffer.add(reflectedSignal.getPoints().get(i).getX());
        }
        this.choose(type,soundingSignalBuffer,reflectedSignalBuffer);

        Double maxValue = Collections.max(corelatedSignal.getAllY());

        Double centerValue=corelatedSignal.getPoints().get(Math.round(corelatedSignal.getPoints().size()/2)).getX();

        Double shift= Math.abs(maxValue-centerValue);

        return shift *signalSpeed;
    }


}
