package complexSignals;

import application.SignalType;
import signals.Point;

import java.util.ArrayList;
import java.util.List;

public class ComplexSignal {



        private double nextNextGaussian;
        private boolean haveNextNextGaussian = false;



        private List<ComplexPoint> points=new ArrayList<>();


        private double amplitude=0.0;                                               //Amplituda
        private double initialTime=0.0;                                             //Czas początkowy
        private double lastTime=0.0;                                                //Czas trwania
        private double basicPeriod=0.0;                                             //Okres podstawowy
        private double signalFrequency=0.0;                                         //Częstotliwość sygnału
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
        private double effectiveValue=0.0;                                          //Wartość skuteczna
        private double variance=0.0;                                                //Wariacja


        public List<ComplexPoint> getPoints() {
            return points;
        }

        public void setPoints(List<ComplexPoint> points) {
            this.points = points;
        }

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

        public double getSignalFrequency() { return signalFrequency; }

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

        public List<Double> getAllX(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(i.getX());
            }
            return list;
        }

        public List<Double> getAllY(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(i.getY());
            }
            return list;
        }

        public List<Double> getAllYI(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(i.getYI());
            }
            return list;
        }

        public List<Double> getAllDiscreteNumbersAbs(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(Math.pow(Math.pow(i.getY(),2)+Math.pow(i.getYI(),2),0.5));
            }
            return list;
        }
    /////////////////////////////////////////////////////////DO ZROBIENIA///////////////////////////////////////
        public List<Double> getAllDiscreteNumbersArg(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(Math.pow(Math.pow(i.getY(),2)+Math.pow(i.getYI(),2),0.5));
            }
            return list;
        }





        //Liczenie czestotliwości gdy podamy okres
        public void countSignalFrequency(double basicPeriod) {
            this.signalFrequency= 1.0/basicPeriod;
        }

        //Liczenie okresu gdy podana czestotliwość
        public void countBasicPeriod(double signalFrequency) {
            this.basicPeriod= 1.0/signalFrequency;
        }



    }
