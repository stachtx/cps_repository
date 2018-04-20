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

    //jeszcze nie działa
    public static Signal quantize(Signal signal, int bits) {

        Signal quantizedSignal = copy(signal);
        List<Double> levels = new ArrayList<Double>();
        double q = signal.getAmplitude() * 2.0 / Math.pow(2, bits);
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

    public static Signal reconstruct(Signal signal, ReconstructionType type/*, double frequency*/) {
        double frequency=200;
        switch (type) {
            case sinc:
                return sincReconstruction(signal);

            case zeroExploration:
                return zeroExploration(signal,frequency);

            default:
                return null;
        }


    }

    public static double sinc(double t) {

        if (t == 0) {
            return 1.0;
        } else {
            return Math.sin(t) / t;
        }

    }

    private static Signal sincReconstruction(Signal signal) {
        Signal reconstructedSignal = new Signal();
        double sincSum;
        for (Double x : signal.getX()) {
            sincSum = 0.0;
            for (Double y : signal.getY()) {


                sincSum += y * sinc(x * signal.getFrequency() - signal.getY().indexOf(y));
            }

            reconstructedSignal.getY().add(sincSum);

        }


        return new Signal();
    }

    private static Signal zeroExploration(Signal signal, double frequency) {

        Signal reconstructedSignal= signal;
        Signal sampledSignal =sampling(signal,frequency);
        List<Double>listOfX= new ArrayList<>();
        List<Double>listOfY= new ArrayList<>();

        for (int i=0; i<signal.getX().size(); i++) {
            for(int j=0; j<sampledSignal.getX().size(); j++) {
                if (signal.getX().get(i)==sampledSignal.getX().get(j))
                    while(signal.getX().get(i)<sampledSignal.getX().get(j+1)) {
                        listOfX.add(signal.getX().get(i));
                        listOfY.add(signal.getY().get(i - 1));
                    }
            }
        }
        reconstructedSignal.setX(listOfX);
        reconstructedSignal.setY(listOfY);

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


    private static double MSE(Signal signal, Signal reconstructedSignal) {
        double meanSquareError = 0.0;
        for (int i=0; i<signal.getX().size(); i++){
            meanSquareError+=Math.pow(signal.getY().get(i)-reconstructedSignal.getY().get(i),2);
        }
        return meanSquareError/signal.getX().size();
    }

    private double SNR(Signal signal, Signal reconstructedSignal) {
        double signalNoiseRatio=0.0;
        double sum=0.0;
        double meanSquareError=MSE(signal, reconstructedSignal);
        for(int i=0; i<signal.getX().size(); i++) {
            sum += Math.pow(signal.getY().get(i),2);
        }
        signalNoiseRatio=10* Math.log10(sum/meanSquareError);

        return signalNoiseRatio;
    }

    private double PSNR(Signal signal, Signal reconstructedSignal) {
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

    private double MD(Signal signal, Signal reconstructedSignal) {
        double maximumDifference=0.0;
        List<Double> listofValues= new ArrayList<>();

        for (int i=0; i<signal.getX().size(); i++){
            listofValues.add(Math.abs(signal.getY().get(i)-reconstructedSignal.getY().get(i)));
        }

        return maximumDifference;
    }

    private double efektywnaLiczbaBitowENOB(Signal signal, Signal reconstructedSignal) {
        double effectiveNumberOfBits= 0.0;
        double signalNoiseRatio = SNR(signal, reconstructedSignal);

        effectiveNumberOfBits= (signalNoiseRatio - 1.76)/6.02;

        return effectiveNumberOfBits;
    }
}