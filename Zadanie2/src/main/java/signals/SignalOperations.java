package signals;

import application.ReconstructionType;
import application.SignalType;

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
    quantizedSignal.generateSignal();
    double q=quantizedSignal.getLastTime()/Math.pow(2,bits);
    double r=quantizedSignal.getInitialTime()+quantizedSignal.getLastTime();
    double sum=0.0;
    int amount=0;
    double avg;

    for(double i=0;i<r;i=i+q){
        for(int j=0; quantizedSignal.getX().get(j)<i;j++){
            sum+=quantizedSignal.getY().get(j);
            amount++;
        }
        avg=sum/amount;
        for(int j=0; quantizedSignal.getX().get(j)<i;j++){

            quantizedSignal.getY().set(j,avg);
        }
    }

return quantizedSignal;
}

public static Signal reconstruct(Signal signal, ReconstructionType type){

    switch(type){





    }

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