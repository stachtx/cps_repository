package complexSignals;

import signals.Point;
import signals.Signal;

public class ComplexOperations {


    public static ComplexSignal transformToComplexSignal(Signal signal){

        ComplexSignal complexSignal=new ComplexSignal();

        for(Point p : signal.getPoints()){
            complexSignal.getPoints().add(new ComplexPoint(p.getX(),p.getY(),0.0));
        }
        return complexSignal;
    }

    public static ComplexSignal computeDft(ComplexSignal input) {
        ComplexSignal complexSignal=new ComplexSignal();
        int n = input.getPoints().size();
        for (int k = 0; k < n; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * k / n;
                sumreal +=  input.getPoints().get(t).getY() * Math.cos(angle) + input.getPoints().get(t).getYI() * Math.sin(angle);
                sumimag += -input.getPoints().get(t).getY() * Math.sin(angle) + input.getPoints().get(t).getYI() * Math.cos(angle);
            }
            complexSignal.getPoints().add(new ComplexPoint(k,sumreal,sumimag));
        }
        return complexSignal;
    }

}
