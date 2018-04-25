package signals;

import application.ReconstructionType;
import com.google.common.collect.Ordering;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleToIntFunction;

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

        for (int i = 0; i < signal.getX().size(); i++) {
            quantizedSignal.getX().add(signal.getX().get(i));

            final int ii = i;
            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(signal.getY().get(ii) - p)));
            quantizedSignal.getY().add(levels.get(0));

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

        if (t == 0.0) {
            return 1.0;
        } else {
            return Math.sin(t*Math.PI) /(t*Math.PI);
        }

    }

    public static Signal sincReconstruction(Signal signal) {
        Signal reconstructedSignal = sampling(signal,signal.getFrequency()+10);
        reconstructedSignal.getY().clear();
        double sincSum;
        for(Double t: reconstructedSignal.getX()) {
            sincSum=0.0;
            for(int i=0;i<signal.getX().size();i++){
                    sincSum += signal.getY().get(i) * sinc(t -signal.getX().get(i));
                }
                reconstructedSignal.getY().add(sincSum/signal.getFrequency());
        }

        return reconstructedSignal;
    }

    public static Signal zeroExploration(Signal signal) {

        Signal reconstructedSignal = sampling(signal,signal.getFrequency()*2);
       for(int i=0;i<signal.getY().size()-1;i++)
        for(int j =0;j<reconstructedSignal.getY().size();j++){
            if(reconstructedSignal.getY().get(j)>=signal.getY().get(i) && reconstructedSignal.getY().get(j)<signal.getY().get(i+1) ){
                reconstructedSignal.getY().set(j,signal.getY().get(i));
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
        for (int i=0; i<signal.getX().size(); i++){
            meanSquareError+=Math.pow(signal.getY().get(i)-reconstructedSignal.getY().get(i),2);
        }
        return meanSquareError/signal.getX().size();
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

        return maximumDifference;
    }

    public double efektywnaLiczbaBitowENOB(Signal signal, Signal reconstructedSignal) {
        double effectiveNumberOfBits= 0.0;
        double signalNoiseRatio = SNR(signal, reconstructedSignal);

        effectiveNumberOfBits= (signalNoiseRatio - 1.76)/6.02;

        return effectiveNumberOfBits;
    }
}