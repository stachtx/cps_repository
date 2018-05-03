package signals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignalsCalculator {

    private static final Double DELTA = 0.000000001;

    public SignalsCalculator(Signal firstSignal,Signal secondSignal) {

       this.firstSignal=firstSignal;
       this.secondSignal=secondSignal;

    }

    private Signal calculatedSignal;
    private Signal reconstructedSignal;
    private Signal sampledSignal;
    public double mse;
    public double snr;
    public double psnr;
    public double md;
    public double enob;

    private Signal firstSignal=new Signal();
    private Signal secondSignal=new Signal();



    public Signal getCalculatedSignal(){ return this.calculatedSignal;}


    /*public void addListToXAndY(List<Double> listOfX, List<Double> listOfY) {
        clearSignalsList();
        listX.add(listOfX);

        listY.add(listOfY);
    }*/


    public void addSignals(){

        calculatedSignal=new Signal();
        List<Integer> tempList = new ArrayList<>();
        int count=0;
        int count2=0;


        for(int i=0; i<firstSignal.getX().size(); i++){

            for (int j=0;j<secondSignal.getX().size();j++){

                if(Objects.equals(firstSignal.getX().get(i),secondSignal.getX().get(j))){
                    calculatedSignal.getX().add(firstSignal.getX().get(i));
                    calculatedSignal.getY().add(firstSignal.getY().get(i)+secondSignal.getY().get(j));
                        
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getX().add(firstSignal.getX().get(i));
                calculatedSignal.getY().add(firstSignal.getY().get(i));
             }
            count = 0;
        }

        for (int k=0; k<secondSignal.getX().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getX().add(secondSignal.getX().get(k));
                calculatedSignal.getY().add(secondSignal.getY().get(k));
            }
            count2 = 0;
            calculatedSignal.setLastTime(calculatedSignal.getX().get(k));
        }
        //if(firstSignal.getType()==secondSignal.getType())
        calculatedSignal.setType(firstSignal.getType());
        calculatedSignal.setFrequency(firstSignal.getFrequency());
        calculatedSignal.setSignalFrequency(firstSignal.getSignalFrequency());
        calculatedSignal.setBasicPeriod(1.0/calculatedSignal.getSignalFrequency());
        calculatedSignal.setAmplitude(firstSignal.getAmplitude()+secondSignal.getAmplitude());
        calculatedSignal.setInitialTime(calculatedSignal.getX().get(0));
    }

    public void  subtractSignals(){

        calculatedSignal=new Signal();
        List<Integer> tempList = new ArrayList<>();
        int count = 0;
        int count2 = 0;

        for(int i=0; i<firstSignal.getX().size(); i++){

            for (int j=0;j<secondSignal.getX().size();j++){

                if(Objects.equals(firstSignal.getX().get(i),secondSignal.getX().get(j))){
                    calculatedSignal.getX().add(firstSignal.getX().get(i));
                    calculatedSignal.getY().add(firstSignal.getY().get(i)-secondSignal.getY().get(j));
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getX().add(firstSignal.getX().get(i));
                calculatedSignal.getY().add(firstSignal.getY().get(i));
            }
            count = 0;
        }

        for (int k=0; k<secondSignal.getX().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                //tempSygnalPoOdjeciu.add(new Pair(dane2.get(k).getKey(), -dane2.get(k).getValue()));
                calculatedSignal.getX().add(secondSignal.getX().get(k));
                calculatedSignal.getY().add(-secondSignal.getY().get(k));
            }
            count2 = 0;
        }
        calculatedSignal.setFrequency(firstSignal.getFrequency());
    }

    public void multiplySignals(){

        calculatedSignal=new Signal();
        List<Integer> tempList = new ArrayList<>();
        int count = 0;
        int count2 = 0;

        for(int i=0; i<firstSignal.getX().size(); i++){

            for (int j=0;j<secondSignal.getX().size();j++){

                if(Objects.equals(firstSignal.getX().get(i),secondSignal.getX().get(j))){
                    calculatedSignal.getX().add(firstSignal.getX().get(i));
                    calculatedSignal.getY().add(firstSignal.getY().get(i)*secondSignal.getY().get(j));
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getX().add(firstSignal.getX().get(i));
                calculatedSignal.getY().add(0.0);
            }
            count = 0;
        }
        for (int k=0; k<secondSignal.getX().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getX().add(secondSignal.getX().get(k));
                calculatedSignal.getY().add(0.0);
            }
            count2 = 0;
        }
        calculatedSignal.setFrequency(firstSignal.getFrequency());
    }

    public void divideSignals(){

        calculatedSignal=new Signal();
        List<Integer> tempList = new ArrayList<>();

        int count = 0;
        int count2 = 0;

        for (int i=0; i<firstSignal.getX().size(); i++){

            for (int j=0; j<secondSignal.getX().size(); j++){
                //System.out.print(firstSignal.getY().get(j)+" ... ");
                //System.out.print(secondSignal.getY().get(j)+" ... ");
                if (Objects.equals(firstSignal.getX().get(i),secondSignal.getX().get(j))){
                    if (secondSignal.getY().get(j) == 0){
                        //calculatedSignal.getX().add(secondSignal.getX().get(i));
                        //calculatedSignal.getY().add(0.0);

                    }
                    else
                    {
                        calculatedSignal.getX().add(firstSignal.getX().get(i));
                        calculatedSignal.getY().add(firstSignal.getY().get(i)/secondSignal.getY().get(j));
                    }
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getX().add(firstSignal.getX().get(i));
                calculatedSignal.getY().add(0.0);
            }
            count = 0;
        }
       for (int k=0; k<secondSignal.getX().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getX().add(secondSignal.getX().get(k));
                calculatedSignal.getY().add(0.0);
            }
            count2 = 0;
        }
        calculatedSignal.setFrequency(firstSignal.getFrequency());

    }

}
