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


        for(int i=0; i<firstSignal.getPoints().size(); i++){

            for (int j=0;j<secondSignal.getPoints().size();j++){

                if(Objects.equals(firstSignal.getPoints().get(i).getX(),secondSignal.getPoints().get(j).getX())){

                    calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()+secondSignal.getPoints().get(j).getY()));
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()));
             }
            count = 0;
        }

        for (int k=0; k<secondSignal.getPoints().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getPoints().add(new Point(secondSignal.getPoints().get(k).getX(),secondSignal.getPoints().get(k).getY()));
            }
            count2 = 0;
            calculatedSignal.setLastTime(calculatedSignal.getPoints().get(k).getX());
        }
        //if(firstSignal.getType()==secondSignal.getType())
        calculatedSignal.setType(firstSignal.getType());
        calculatedSignal.setFrequency(firstSignal.getFrequency());
        calculatedSignal.setSignalFrequency(firstSignal.getSignalFrequency());
        calculatedSignal.setBasicPeriod(1.0/calculatedSignal.getSignalFrequency());
        calculatedSignal.setAmplitude(firstSignal.getAmplitude()+secondSignal.getAmplitude());
        calculatedSignal.setInitialTime(calculatedSignal.getPoints().get(0).getX());
    }

    public void  subtractSignals(){

        calculatedSignal=new Signal();
        List<Integer> tempList = new ArrayList<>();
        int count = 0;
        int count2 = 0;

        for(int i=0; i<firstSignal.getPoints().size(); i++){

            for (int j=0;j<secondSignal.getPoints().size();j++){

                if(Objects.equals(firstSignal.getPoints().get(i).getX(),secondSignal.getPoints().get(j).getX())){
                      calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()-secondSignal.getPoints().get(j).getY()));
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()));
            }
            count = 0;
        }

        for (int k=0; k<secondSignal.getPoints().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                //tempSygnalPoOdjeciu.add(new Pair(dane2.get(k).getKey(), -dane2.get(k).getValue()));
                calculatedSignal.getPoints().add(new Point(secondSignal.getPoints().get(k).getX(),-secondSignal.getPoints().get(k).getY()));
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

        for(int i=0; i<firstSignal.getPoints().size(); i++){

            for (int j=0;j<secondSignal.getPoints().size();j++){

                if(Objects.equals(firstSignal.getPoints().get(i).getX(),secondSignal.getPoints().get(j).getX())){
                    calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()*secondSignal.getPoints().get(j).getY()));
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),0.0));
            }
            count = 0;
        }
        for (int k=0; k<secondSignal.getPoints().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getPoints().add(new Point(secondSignal.getPoints().get(k).getX(),0.0));
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

        for (int i=0; i<firstSignal.getPoints().size(); i++){

            for (int j=0; j<secondSignal.getPoints().size(); j++){
                //System.out.print(firstSignal.getY().get(j)+" ... ");
                //System.out.print(secondSignal.getY().get(j)+" ... ");
                if (Objects.equals(firstSignal.getPoints().get(i).getX(),secondSignal.getPoints().get(j).getX())){
                    if (secondSignal.getPoints().get(j).getY() == 0){
                        //calculatedSignal.getX().add(secondSignal.getX().get(i));
                        //calculatedSignal.getY().add(0.0);

                    }
                    else
                    {
                        calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),firstSignal.getPoints().get(i).getY()/secondSignal.getPoints().get(j).getY()));
                    }
                    count++;
                    tempList.add(j);
                }
            }
            if (count == 0){
                calculatedSignal.getPoints().add(new Point(firstSignal.getPoints().get(i).getX(),0.0));
            }
            count = 0;
        }
       for (int k=0; k<secondSignal.getPoints().size(); k++){
            for (int l=0; l<tempList.size(); l++){
                if (k==tempList.get(l)){
                    count2++;
                }
            }
            if (count2 == 0){
                calculatedSignal.getPoints().add(new Point(secondSignal.getPoints().get(k).getX(),0.0));

            }
            count2 = 0;
        }
        calculatedSignal.setFrequency(firstSignal.getFrequency());

    }

}
