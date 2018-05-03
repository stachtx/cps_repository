package signals;

import application.FilterType;

import java.util.ArrayList;
import java.util.List;

public class Filter {


    public static Signal spliceOfSignals (List<Double> answer, Signal input){
        Signal splicedSignal = new Signal();
        int M=answer.size();
        int N=input.getPoints().size();
        double sum;
        for(int n=0;n<M+N;n++) {
            sum=0.0;
            for (int k = 0; k < M; k++) {
                if(n-k<0){
                    sum+=0;
                } else{
                    sum+= answer.get(k)*input.getPoints().get(n-k).getY();
                }
            }
            splicedSignal.getPoints().add(new Point((double) n, sum));
        }
        return splicedSignal;
    }
    public static Double  hannignFunction (int n,int M){

        return 0.5*0.5*Math.cos(2*Math.PI*n/M);
    }

    public static List<Double> impulseAnswerBottomHan(int M, Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;n++){
            if(n==0){
                points.add(2/K*hannignFunction(n,M));
            } else {
                points.add(Math.sin(2*Math.PI*n/K)/(Math.PI*n)*hannignFunction(n,M));
            }

        }
         return points;
    }

    public static List<Double> impulseAnswerBottomRec (int M,Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;n++){
            if(n==0){
                points.add(2/K);
            } else {
                points.add(Math.sin(2*Math.PI*n/K)/Math.PI*n);
            }

        }
        return points;
    }

    public static List <Double> impulseAnswerTop(int M,Double K, List<Double> bottom ){
        List <Double> points= new ArrayList<>();

        for(int n=0; n<bottom.size();n++){
            points.add(bottom.get(n)*Math.pow(-1,n));
        }

        return points;
    }

    public static Signal filtrateSignal (FilterType type,Signal signal, int M, Double K){

        List <Double> tmp = new ArrayList<>();

        switch(type){

             case bottomRec:
                   tmp=impulseAnswerBottomRec(M,K);
                 break;

             case topRec:
                    tmp=impulseAnswerTop(M,K,impulseAnswerBottomRec(M,K));
                 break;

             case bottomHanning:
                    tmp=impulseAnswerBottomHan(M,K);
                 break;

             case topHanning:
                 tmp=impulseAnswerTop(M,K,impulseAnswerBottomHan(M,K));
                 break;

         }

        return spliceOfSignals(tmp,signal);
    }
}
