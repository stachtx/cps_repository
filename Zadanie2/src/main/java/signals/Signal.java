package signals;

import application.SignalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Signal {

    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;
    private List<Double> x=new ArrayList<>();
    private List<Double> y=new ArrayList<>();


    private SignalType type= SignalType.noiseUniDis;                            //typ sygnału
    private double amplitude=0.0;                                               //Amplituda
    private double initialTime=0.0;                                             //Czas początkowy
    private double lastTime=0.0;                                                //Czas trwania
    private double basicPeriod=0.0;                                             //Okres podstawowy
    private double fillFactor=0.0;                                              //Współczynnik wypełnienia
    private double entityChange=0.0;                                            //Skok jednostkowy
    private int firstSampleNr=0;                                                //Numer pierwszej próbki
    private int lastSampleNr=0;                                                 //Numer ostatniej próbki
    private int changeForSample=0;                                              //Skok dla próbki
    private double frequency=0.0;                                               //Czestotliwość próbkowania
    private double probability=0.0;                                             //Prawdopodobieństwo
    private double average=0.0;                                                 //średnia
    private double absoluteMean=0.0;                                            //średnia bezwzględna
    private double  averagePower=0.0;                                           //Moc średnia

    public double getAmplitude() {
        return amplitude;
    }

    public double getInitialTime() {
        return initialTime;
    }

    public double getLastTime() {
        return lastTime;
    }

    public double getBasicPeriod() {
        return basicPeriod;
    }

    public double getFillFactor() {
        return fillFactor;
    }

    public double getEntityChange() {
        return entityChange;
    }

    public int getFirstSampleNr() {
        return firstSampleNr;
    }

    public int getLastSampleNr() {
        return lastSampleNr;
    }

    public int getChangeForSample() {
        return changeForSample;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getProbability() {
        return probability;
    }

    private double effectiveValue=0.0;                                          //Wartość skuteczna
    private double variance=0.0;                                                //Wariacja


    public List<Double> getX() {
        return x;
    }

    public List<Double> getY() {
        return y;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public SignalType getType() {
        return type;
    }

    public void setType(SignalType type) {
        this.type = type;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public void setInitialTime(double initialTime) {
        this.initialTime = initialTime;
    }

    public void setLastTime(double lastTime) {
        this.lastTime = lastTime;
    }

    public void setBasicPeriod(double basicPeriod) {
        this.basicPeriod = basicPeriod;
    }

    public void setFillFactor(double fillFactor) {
        this.fillFactor = fillFactor;
    }

    public void setEntityChange(double entityChange) {
        this.entityChange = entityChange;
    }

    public void setFirstSampleNr(int firstSampleNr) {
        this.firstSampleNr = firstSampleNr;
    }

    public void setLastSampleNr(int lastSampleNr) {
        this.lastSampleNr = lastSampleNr;
    }

    public void setChangeForSample(int changeForSample) {
        this.changeForSample = changeForSample;
    }

    public void setFrequency(double frequency) {

       // if (frequency)

        this.frequency = frequency;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getAverage() {
        return average;
    }

    public double getAbsoluteMean() {
        return absoluteMean;
    }

    public double getAveragePower() {
        return averagePower;
    }

    public double getEffectiveValue() {
        return effectiveValue;
    }

    public double getVariance() {
        return variance;
    }


    //Generuje odpowiedni sygnał
    public void generateSignal(){
        y.clear();
        switch (type){

            case noiseUniDis:
                noiseUniDis();
                break;

            case noiseGauss:
                noiseGauss();
                break;

            case sin:
                sin();
                break;

            case sinOneHalf:
                sinOneHalf();
                break;

            case sinTwoHalf:
                sinTwoHalf();
                break;

            case rec:
                rec();
                break;

            case recSym:
                recSym();
                break;

            case tri:
                tri();
                break;
            case entityChange:
                entityChange();
                break;
            case entityImpulse:
                entityImpulse();
                break;
            case noiseImpulse:
                noiseImpulse();
                break;
        }
    }

    //Szum jednostajny
    public void noiseUniDis(){
        Random rand=new Random();
        int sampleAmount= (int) ((lastTime)*frequency);
        for(int i =firstSampleNr;i<=sampleAmount;i++){
            double t=i/frequency+initialTime;
            x.add(t);
            y.add(rand.nextDouble()*(-amplitude-amplitude)+amplitude);
        }
    }

    //Szum gaussowski
    public void noiseGauss(){
        Random rand=new Random();

        int sampleAmount= (int) ((lastTime)*frequency);
        for(int i =firstSampleNr;i<=sampleAmount;i++){
            double t=i/frequency+initialTime;
            x.add(t);
            if (haveNextNextGaussian) {
                haveNextNextGaussian = false;
                y.add(nextNextGaussian);
            } else {
                double v1, v2, s;
                do {
                    v1 = 2 * rand.nextDouble() - 1;   // between -1.0 and 1.0
                    v2 = 2 * rand.nextDouble() - 1;   // between -1.0 and 1.0
                    s = v1 * v1 + v2 * v2;
                } while (s >= 1 || s == 0);
                double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
                nextNextGaussian = v2 * multiplier;
                haveNextNextGaussian = true;
                y.add(v1 * multiplier) ;
            }
        }

    }

    //Sygnał sinusoidalny
    public void sin(){
        int sampleAmount= (int) ((lastTime)*frequency);
    for(int i =firstSampleNr;i<=sampleAmount;i++){
        double t=i/frequency+initialTime;
        x.add(t);
        y.add(amplitude* Math.sin((2*Math.PI/basicPeriod)*(t-initialTime)));
    }

   }

    //Sygnał sinusoidalny wyprostowany jednopołówkowo
    public void sinOneHalf(){
       int sampleAmount= (int) ((lastTime)*frequency);
       for(int i =firstSampleNr;i<=sampleAmount;i++){
           double t=i/frequency+initialTime;
           x.add(t);
           y.add(0.5*amplitude* (Math.sin((2*Math.PI/basicPeriod)*(t-initialTime))+ Math.abs(Math.sin((2*Math.PI/basicPeriod)*(t-initialTime)))));
       }
   }

   //Sygnał sinusoidalny wyprostowany dwupołówkowo
    public void sinTwoHalf(){
       int sampleAmount= (int) ((lastTime)*frequency);
       for(int i =firstSampleNr;i<=sampleAmount;i++){
           double t=i/frequency+initialTime;
           x.add(t);
           y.add(amplitude* Math.abs(Math.sin((2*Math.PI/basicPeriod)*(t-initialTime))));
       }
   }

   //Sygnał prostokątny
    public void rec(){
       int sampleAmount= (int) ((lastTime)*frequency);
       int k=0;
       for(int i =firstSampleNr;i<=sampleAmount;i++){
           double t=i/frequency+initialTime;
           x.add(t);
           if(t>=basicPeriod*(k+1)+initialTime){ k++;}
           if(((t)>= (k*basicPeriod+initialTime)) &&((t)<(fillFactor*basicPeriod+k*basicPeriod+initialTime ))){
               y.add(amplitude);
           } else if(((t)>=(fillFactor*basicPeriod+k*basicPeriod+initialTime )) || ((t)< (k*basicPeriod+initialTime))){
               y.add(0.0);
           }
       }
   }
    //Sygnał prostokątny symetryczny
    public void recSym(){
       int sampleAmount= (int) ((lastTime)*frequency);
       int k=0;
       for(int i =firstSampleNr;i<=sampleAmount;i++){
           double t=i/frequency+initialTime;
           x.add(t);
           if(t>=basicPeriod*(k+1)+initialTime){ k++;}
           if(((t)>= (k*basicPeriod+initialTime)) &&((t)<(fillFactor*basicPeriod+k*basicPeriod+initialTime ))){
               y.add(amplitude);
           } else if(((t)>=(fillFactor*basicPeriod+k*basicPeriod+initialTime )) || ((t)< (k*basicPeriod+initialTime))){
               y.add(-amplitude);
           }
       }
   }

    public void tri() {
        int sampleAmount= (int) ((lastTime)*frequency);
        int k=0;
        for(int i =firstSampleNr;i<=sampleAmount;i++){
            double t=i/frequency+initialTime;
            x.add(t);
            if(t>=basicPeriod*(k+1)+initialTime){ k++;}
            if((t>= (k*basicPeriod+initialTime)) &&(t<(fillFactor*basicPeriod+k*basicPeriod+initialTime ))){
                y.add(amplitude/(fillFactor*basicPeriod)*(t-k*basicPeriod-initialTime));
            } else if((t>=(fillFactor*basicPeriod+k*basicPeriod+initialTime )) || (t<(basicPeriod+k*basicPeriod+initialTime))){
                y.add(-amplitude/(basicPeriod*(1-fillFactor))*(t-k*basicPeriod-initialTime)+amplitude/(1-fillFactor));
            }
        }
    }

    //Skok jednostkowy
    public void entityChange(){
       int sampleAmount= (int) ((lastTime)*frequency);
       for(int i =firstSampleNr;i<sampleAmount;i++){
           double t=i/frequency+initialTime;
           x.add(t);
           if((t> entityChange)){
               y.add(amplitude);
           } else if((t== entityChange)){
               y.add(0.5);
           } else if ((t< entityChange)){
               y.add(0.0);
           }
       }
   }

    //Impuls jednostkowy
    public void entityImpulse(){

       for(int i =firstSampleNr;i<=lastSampleNr;i++){
           x.add((double) i);
           if((i==changeForSample)){
               y.add(1.0);
           } else{
               y.add(0.0);
           }
       }
   }

    //Szum impulsowy
    public void noiseImpulse(){
        Random rand=new Random();

        for(int i =firstSampleNr;i<=lastSampleNr;i++){
            x.add((double) i);
            if(rand.nextDouble()<=probability){
                y.add(1.0);
            } else{
                y.add(0.0);
            }
        }
    }

    //średnia
    public void average(){

        double suma=0;
        for(Double i : y){
            suma+=i;
        }
        this.average= suma* (1/(lastTime+1));
    }

    //średnia bezwzględna
    public void absoluteMean(){

        double suma=0;
        for(Double i : y){
            suma+=Math.abs(i);
        }
        this.absoluteMean= suma* (1/(lastTime+1));
    }

    //średnia moc
    public void averagePower(){
        double suma=0;
        for(Double i : y){
            suma+=Math.pow(i,2);
        }
        this.averagePower= suma* (1/(lastTime+1));

    }

    //
    public void effectiveValue(){

        double suma=0;
        for(Double i : y){
            suma+=Math.pow(i-this.average,2);
        }
        this.effectiveValue= suma* (1/(lastTime+1));

    }

    //Wariacja
    public void variance(){

        double suma=0;
        for(Double i : y){
            suma+=Math.pow(i,2);
        }
        this.variance= Math.sqrt(suma* (1/(lastTime+1)));

    }



}


