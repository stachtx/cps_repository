package signals;

import application.ReconstructionType;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public   class SignalOperations {

    public static Signal sampling(Signal signal, double frequency) {

        Signal sampledSignal = copy(signal);
        sampledSignal.setFrequency(frequency);
        sampledSignal.generateSignal();
        return sampledSignal;
    }

    public static Signal quantize(Signal signal, int bits) {

        Signal quantizedSignal = copy(signal);
        List<Double> levels = new ArrayList<Double>();
        double q = signal.getAmplitude() * 2.0 / (Math.pow(2, bits)-1);
        for (int i = 0; i < Math.pow(2, bits); i++) {
            levels.add(-signal.getAmplitude() + i * q);
        }

        for (Point i : signal.getPoints()) {
            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(i.getY() - p)));
            quantizedSignal.getPoints().add(new Point(i.getX(),levels.get(0)));
        }

        return quantizedSignal;
    }

    public static Signal reconstruct(Signal signal, ReconstructionType type) {

        switch (type) {
            case sinc:
                return sincReconstruction(signal);

            case zeroExploration:
                return zeroExploration(signal);

            default:
                return null;
        }


    }

    public static double sinc(double t) {

        if (t == 0.0) {
            return 1.0;
        } else {
            return Math.sin(t*Math.PI) /(t*Math.PI);
        }

    }

    public static Signal sincReconstruction(Signal signal) {
        Signal reconstructedSignal = sampling(signal,signal.getFrequency()+10);
        double sincSum;
        for(Point t: reconstructedSignal.getPoints()) {
            sincSum=0.0;
            for(Point i : signal.getPoints()){
                    sincSum += i.getY() * sinc(t.getX() - i.getX());
                }
                t.setY(sincSum/signal.getFrequency());
        }

        return reconstructedSignal;
    }

    public static Signal zeroExploration(Signal signal) {

        Signal reconstructedSignal = sampling(signal,signal.getFrequency()*2);
       for(int i=0;i<signal.getPoints().size()-1;i++)
        for(int j =0;j<reconstructedSignal.getPoints().size();j++){
            if(reconstructedSignal.getPoints().get(j).getY()>=signal.getPoints().get(i).getY() && reconstructedSignal.getPoints().get(j).getY()<signal.getPoints().get(i+1).getY() ){
                reconstructedSignal.getPoints().get(j).setY(signal.getPoints().get(i).getY());
            }
        }
        return reconstructedSignal;

    }

    public static Signal copy(Signal signal) {
        Signal copySignal = new Signal();
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

    public static double MSE(Signal signal, Signal reconstructedSignal) {
        double meanSquareError = 0.0;
        for (int i=0; i<signal.getPoints().size(); i++){
            meanSquareError+=Math.pow(signal.getPoints().get(i).getY()-reconstructedSignal.getPoints().get(i).getY(),2);
        }
        return meanSquareError/signal.getPoints().size();
    }

    public static double SNR(Signal signal, Signal reconstructedSignal) {
        double signalNoiseRatio=0.0;
        double sum=0.0;
        double meanSquareError=MSE(signal, reconstructedSignal);
        for(Point i : signal.getPoints()) {
            sum += Math.pow(i.getY(),2);
        }
        signalNoiseRatio=10* Math.log10(sum/meanSquareError);

        return signalNoiseRatio;
    }

    public static double PSNR(Signal signal, Signal reconstructedSignal) {
        double peakSignalToNoiseRatio=0.0;
        List<Double> listofValues= new ArrayList<>();
        double up=0.0;
        double meanSquareError=MSE(signal, reconstructedSignal);

        for (Point i : signal.getPoints()){
            listofValues.add(i.getY());
        }
        up=Collections.max(listofValues);
        peakSignalToNoiseRatio=10*Math.log10(up/meanSquareError);
        return peakSignalToNoiseRatio;
    }

    public static double MD(Signal signal, Signal reconstructedSignal) {
        double maximumDifference=0.0;
        List<Double> listofValues= new ArrayList<>();

        for (int i=0; i<signal.getPoints().size(); i++){
            listofValues.add(Math.abs(signal.getPoints().get(i).getY()-reconstructedSignal.getPoints().get(i).getY()));
        }

        maximumDifference=Collections.max(listofValues);
        return maximumDifference;
    }

    public static double ENOB(Signal signal, Signal reconstructedSignal) {
        double effectiveNumberOfBits= 0.0;
        double signalNoiseRatio = SNR(signal, reconstructedSignal);

        effectiveNumberOfBits= (signalNoiseRatio - 1.76)/6.02;

        return effectiveNumberOfBits;
    }

    public static Signal spliceOfSignals (Signal firstSignal, Signal secondSignal){
        int M=firstSignal.getPoints().size();
        int N=secondSignal.getPoints().size();

        for(int n=0;n<M+N;n++) {
            for (int k = 0; k < M; k++) {

            }
        }
        return new Signal();
    }

}