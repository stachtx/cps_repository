package signals;

import application.FilterType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Filter {


    public static Signal spliceOfSignals(List<Double> answer, List <Double> input){
        Signal splicedSignal = new Signal();
        int M=answer.size();
        int N=input.size();

        for (int i = 0; i < M+N-1; i++) {
            double sum = 0.0;

            for (int k = 0; k < M; k++) {
                try {
                    sum+=answer.get(k)*input.get(i-k);
                } catch (IndexOutOfBoundsException e) { }
            }
            splicedSignal.getPoints().add(new Point((double) i, sum));
        }
        return splicedSignal;
    }

    public static Signal spliceOfSignals (List<Double> answer, List <Double> input,double lastTime){
        Signal splicedSignal = new Signal();
        int M=answer.size();
        int N=input.size();

        double T=lastTime/(M+N-1);
        for(int n=0;n<M+N-1;++n) {
            double sum=0.0;
            for (int k = 0; k < M; ++k) {
                if(k<M && (n-k) >= 0 && (n-k)<N ){
                    sum+=answer.get(k)*input.get(n-k);
                }
            }

             splicedSignal.getPoints().add(new Point((double) n*T, sum));
        }
        return splicedSignal;
    } // Powinno być ok

    public static double rectangleWindow(){
        return 1.0;
    } //Powinno być ok

    public static double hannignWindow(int n, double M){

        return 0.5-0.5*Math.cos(2*Math.PI*(double)n/M);
    } // Powinno być ok

   public static List<Double> impulseAnswerBottomHan(int M, Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;n++){
            if(n == (M - 1.0) / 2.0){
                points.add(2/K* hannignWindow(n,M));
            } else {
                points.add(Math.sin(2.0 * Math.PI * ((double) n - (M - 1.0) / 2.0) / K) / (Math.PI * ((double) n - (M - 1.0) / 2.0))* hannignWindow(n,M));
            }

        }
         return points;
    }

    public static List<Double> impulseAnswerBottomRec (int M,Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;++n){
            if(n == (M - 1.0) / 2.0){
                points.add(2/K);
            } else {
                points.add(Math.sin(2.0 * Math.PI * ((double) n - (M - 1.0) / 2.0) / K) / (Math.PI * ((double) n - (M - 1.0) / 2.0))*rectangleWindow());
            }
        }
        return points;
    }

    public static List <Double> impulseAnswerTop(List<Double> bottom ){
        List <Double> points= new ArrayList<>();

        for(int n=0; n<bottom.size();n++){
            points.add(bottom.get(n)*Math.pow(-1,n));
        }

        return points;
    }

    public static Signal filtrateSignal (FilterType type,Signal signal, int M,double cutFrequency){

        List <Double> tmp = new ArrayList<>();

        Double K=signal.getSignalFrequency()/cutFrequency;
        switch(type){

             case bottomRec:
                   tmp=impulseAnswerBottomRec(M,K);
                 break;

             case topRec:
                    K=signal.getSignalFrequency()/(signal.getFrequency()/2-cutFrequency);
                    tmp=impulseAnswerTop(impulseAnswerBottomRec(M,K));
                 break;

             case bottomHanning:
                    tmp=impulseAnswerBottomHan(M,K);
                 break;

             case topHanning:
                 K=signal.getSignalFrequency()/(signal.getFrequency()/2-cutFrequency);
                 tmp=impulseAnswerTop(impulseAnswerBottomHan(M,K));
                 break;

         }


        return spliceOfSignals(tmp,signal.getAllY(),signal.getLastTime());
    }
}
