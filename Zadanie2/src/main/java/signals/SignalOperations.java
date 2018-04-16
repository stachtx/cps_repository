package signals;

import application.ReconstructionType;
import com.google.common.collect.Ordering;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public   class SignalOperations {


public static Signal sampling(Signal signal, double frequency){

    Signal sampledSignal=copy(signal);
            sampledSignal.setFrequency(frequency);
            sampledSignal.generateSignal();
    return  sampledSignal;
}

//jeszcze nie działa
public static Signal quantize(Signal signal,int bits){

    Signal quantizedSignal=copy(signal);
    List<Double> levels =new ArrayList<Double>();
    double q=signal.getAmplitude()*2.0/Math.pow(2,bits);
    for(int i=0; i<Math.pow(2,bits);i++){
        levels.add(-signal.getAmplitude()+i*q);
    }

    for(int i=0;i<signal.getX().size();i++){
        quantizedSignal.getX().add(signal.getX().get(i));

            final int ii=i;
            Collections.sort(levels, Ordering.natural().onResultOf(p-> Math.abs(signal.getY().get(ii)-p)));
            quantizedSignal.getY().add(levels.get(0));

    }

return quantizedSignal;
}

public static Signal reconstruct(Signal signal, ReconstructionType type){

    switch(type){
        case sinc:
            return sincReconstruction(signal);

        case zeroExploration:
            return zeroExploration();

        default:
            return null;
    }


}
public static double sinc(double t){

    if(t==0){
        return 1.0;
    }
    else {
        return Math.sin(t)/t;
    }

}

private static Signal sincReconstruction(Signal signal){
    Signal reconstructedSignal= new Signal();
    double sincSum;
    for (Double x: signal.getX()){
        sincSum=0.0;
        for(Double y: signal.getY()){


            sincSum+=y*sinc(x*signal.getFrequency()-signal.getY().indexOf(y));
        }

        reconstructedSignal.getY().add(sincSum);

    }


    return new Signal();
}

private static Signal zeroExploration(){

    return new Signal();
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
}
