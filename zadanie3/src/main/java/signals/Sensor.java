package signals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sensor {




    public static Signal spliceCorrelationOfSignals(List<Double> answer, List <Double> input){
        List <Double> listY = new ArrayList<>();
        for(Double i : input){
            listY.add(i);
        }
        Collections.reverse(listY);
        return Filter.spliceOfSignals(answer,input);
    }


    public static Signal directCorrelationOfSignals (List<Double> answer, Signal input){

        Signal corelatedSignal = new Signal();
        int M=answer.size();
        int N=input.getPoints().size();
        double sum;
        for(int n = (int) Math.floor((M+N-1)/(-2)); n<M+N; n++) {
            sum=0.0;
            for (int k =0 ; k < M; k++) {
                if(n-k<0){
                    sum+=0;
                } else{
                    sum+= answer.get(k)*input.getPoints().get(n+k).getY();
                }
            }
            corelatedSignal.getPoints().add(new Point((double) n, sum));
        }
        return corelatedSignal;

    }


    public static void distanceSensor(){




    }


}
