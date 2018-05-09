package signals;

import application.ReconstructionType;
import com.google.common.collect.Ordering;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleToIntFunction;

public   class SignalOperations {

    private static int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<0.5){
            return d<0 ? -i : i;
        }else{
            return d<0 ? -(i+1) : i+1;
        }
    }



    public static Signal sampling(Signal signal, double samplingFrequency /*ile próbek zostanie pobrane w danej sekundzie z sygnału*/) {

        Signal sampledSignal= new Signal();

        int whichNextSample=0;
        int newSignalSize=(int)((signal.getLastTime()-signal.getInitialTime())*samplingFrequency);
        if(signal.getFrequency()>=samplingFrequency){
            whichNextSample= round(signal.getFrequency()/samplingFrequency);
        }else
            whichNextSample = 1;
        for(int i=0; i <=newSignalSize; i++){
            if(i*whichNextSample<=signal.getX().size()){
                sampledSignal.getX().add(signal.getX().get(i*whichNextSample));
                sampledSignal.getY().add(signal.getY().get(i*whichNextSample));
            }
        }
        for(int i=0;i<=newSignalSize;i++){
            System.out.println(sampledSignal.getX().get(i) + ", " + sampledSignal.getY().get(i));
        }
        System.out.println(sampledSignal.getX().size());

        return sampledSignal;

    }



    //przerobić funkcję żeby przekazywana była liczba poziomów, bo z liczby bitów można wyliczyć też liczbę poziomów
    public static Signal quantizeBits(Signal signal, int bits) {

        Signal quantizedSignal = copy(signal);
        List<Double> levels = new ArrayList<Double>();
        double q = signal.getAmplitude() * 2.0 / (Math.pow(2, bits)-1);
        for (int i = 0; i < Math.pow(2, bits); i++) {
            levels.add(-signal.getAmplitude() + i * q);
        }

        for (int i = 0; i < signal.getX().size(); i++) {
            quantizedSignal.getX().add(signal.getX().get(i));

            final int ii = i;
            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(signal.getY().get(ii) - p)));
            quantizedSignal.getY().add(levels.get(0));

        }

        return quantizedSignal;
    }
    public static Signal quantizeLevels(Signal signal, int numberOfLevels) {

        double step= signal.getAmplitude()*2.0 / (numberOfLevels-1);
        Signal quantizedSignal = copy(signal);
        List<Double> levels = new ArrayList<Double>();

        for (int i = 0; i < numberOfLevels; i++) {
            levels.add(-signal.getAmplitude() + i * step);
        }

        for (int i = 0; i < signal.getX().size(); i++) {
            quantizedSignal.getX().add(signal.getX().get(i));

            final int ii = i;
            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(signal.getY().get(ii) - p)));
            quantizedSignal.getY().add(levels.get(0));

        }

        return quantizedSignal;
    }

    public static Signal reconstruct(Signal signal, ReconstructionType type, double reconstructionFrequency) {

        switch (type) {
            case sinc:
                return sincReconstruction(signal, reconstructionFrequency);

            case zeroExploration:
                return zeroExploration(signal, reconstructionFrequency);

            default:
                return null;
        }


    }
    public static double rec(double t){
        if(Math.abs(t)>0.5){
            return 0;
        } else if(Math.abs(t)<0.5){
            return 1;
        }
        else {
            return 0.5;
        }
    }

    public static double sinc(double t) {

        if (Math.abs(t-0.0)<0.0001) {
            return 1.0;
        } else {
            return Math.sin(t*Math.PI) /(t*Math.PI);
        }

    }

    public static Signal sincReconstruction(Signal signal, double reconstructionFrequency) {

        double step=1/signal.getFrequency();
        //double sampF=50;
        Signal sampledSignal = sampling(signal,reconstructionFrequency);
        Signal reconstructedSignal = new Signal();
        double sincSum;
        for(Double i=sampledSignal.getX().get(0); i<=sampledSignal.getX().get(sampledSignal.getX().size()-1); i+=step) {
            sincSum=0.0;
            for(int j=0; j<sampledSignal.getX().size(); j++){
                sincSum += sampledSignal.getY().get(j) * sinc(i/ (1/reconstructionFrequency) - j);
            }
            reconstructedSignal.getX().add(i);
            reconstructedSignal.getY().add(sincSum);
        }

        return reconstructedSignal;
    }

    public static Signal zeroExploration(Signal signal, double reconstructionFrequency) {
        double step=1/signal.getFrequency();
        //double sampF=50;
        int nextIndex=1;
        double lastValue;
        Signal sampledSignal = sampling(signal,reconstructionFrequency);
        Signal reconstructedSignal = new Signal();
        lastValue=sampledSignal.getY().get(0);

        for(Double i=sampledSignal.getX().get(0); i<=sampledSignal.getX().get(sampledSignal.getX().size()-1); i+=step) {
            if(nextIndex<sampledSignal.getX().size() && sampledSignal.getX().get(nextIndex)<=i){
                lastValue=sampledSignal.getY().get(nextIndex);
                nextIndex++;
            }
            reconstructedSignal.getX().add(i);
            reconstructedSignal.getY().add(lastValue);
        }


        System.out.println("To co wazne");
        System.out.println(step);
        System.out.println(reconstructedSignal.getX().size());
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
        for (int i=0; i<signal.getX().size(); i++){
            meanSquareError+=Math.pow(signal.getY().get(i)-reconstructedSignal.getY().get(i),2);
        }
        return meanSquareError/signal.getX().size();
        //return meanSquareError;
    }

    public static double SNR(Signal signal, Signal reconstructedSignal) {
        double signalNoiseRatio=0.0;
        double sum=0.0;
        double meanSquareError=MSE(signal, reconstructedSignal);
        for(int i=0; i<signal.getX().size(); i++) {
            sum += Math.pow(signal.getY().get(i),2);
        }
        signalNoiseRatio=10* Math.log10(sum/meanSquareError);

        return signalNoiseRatio;
    }

    public static double PSNR(Signal signal, Signal reconstructedSignal) {
        double peakSignalToNoiseRatio=0.0;
        List<Double> listofValues= new ArrayList<>();
        double up=0.0;
        double meanSquareError=MSE(signal, reconstructedSignal);

        for (int i=0; i<signal.getX().size(); i++){
            listofValues.add(signal.getY().get(i));
        }
        up=Collections.max(listofValues);
        peakSignalToNoiseRatio=10*Math.log10(up/meanSquareError);
        return peakSignalToNoiseRatio;
    }

    public static double MD(Signal signal, Signal reconstructedSignal) {
        double maximumDifference=0.0;
        List<Double> listofValues= new ArrayList<>();

        for (int i=0; i<signal.getX().size(); i++){
            listofValues.add(Math.abs(signal.getY().get(i)-reconstructedSignal.getY().get(i)));
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
}