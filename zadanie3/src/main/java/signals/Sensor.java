
package signals;

import application.SensorType;
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
        int M=first.size();
        int N=second.size();
        double sum;
        for(int n = (int) Math.floor((M+N-1)/(-2)); n<M+N; n++) {
            sum=0.0;
            for (int k =0 ; k < M; k++) {
                if(k<M && (n+k) >= 0 && (n+k)<N ){
                    sum+= first.get(k)*second.get(n+k);
                }
            }
            corelatedSignal.getPoints().add(new Point((double) n, sum));
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
        reflectedSignal= this.copy(soundingSignal);
        double delay =2.0*target.getObjectPosition()/signalSpeed;
        for(Point i : reflectedSignal.getPoints()){

            i.setX(i.getX()*samplingFrequency-delay);
        }
    }

    public void distanceSensor(){

        this.generateReflectedSignal();
        this.choose(soundingSignal.getAllY().subList(0,bufferLength),reflectedSignal.getAllY().subList(0,bufferLength));
        double maxValue = Collections.max(corelatedSignal.getAllY());
        double centerValue=corelatedSignal.getPoints().get(Math.round(corelatedSignal.getPoints().size()/2)).getX();
        double shift= Math.abs(maxValue-centerValue);
        this.distance= shift *signalSpeed;
    }

    public static Signal copy(Signal signal) {
        Signal copySignal = new Signal();
        copySignal.setPoints(signal.getPoints());
        copySignal.setType(signal.getType());
        copySignal.setAmplitude(signal.getAmplitude());
        copySignal.setInitialTime(signal.getInitialTime());
        copySignal.setLastTime(signal.getLastTime());
        copySignal.setBasicPeriod(signal.getBasicPeriod());
        copySignal.setFillFactor(signal.getFillFactor());
        copySignal.setEntityChange(signal.getEntityChange());
        copySignal.setFirstSampleNr(signal.getFirstSampleNr());
        copySignal.setLastSampleNr(signal.getLastSampleNr());
        copySignal.setChangeForSample(signal.getChangeForSample());
        copySignal.setFrequency(signal.getFrequency());
        copySignal.setProbability(signal.getProbability());
        return copySignal;

    }

}

