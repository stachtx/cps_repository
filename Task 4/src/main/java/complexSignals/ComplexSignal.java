package complexSignals;

import java.util.ArrayList;
import java.util.List;

public class ComplexSignal {

        private List<ComplexPoint> points=new ArrayList<>();

        public List<ComplexPoint> getPoints() {
            return points;
        }

        public void setPoints(List<ComplexPoint> points) {
            this.points = points;
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

        public List<Double> getAllComplexNumbersAbs(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(getComplexNumberAbs(i));
            }
            return list;
        }

        public double getComplexNumberAbs(ComplexPoint i){
            return Math.pow(Math.pow(i.getY(),2)+Math.pow(i.getYI(),2),0.5);
        }

        public List<Double> getAllComplexNumbersArg(){
            List <Double> list = new ArrayList<>();
            for(ComplexPoint i : points){
                list.add(getComplexNumberArg(i));
            }
            return list;
        }

        public double getComplexNumberArg(ComplexPoint i){
            double argument=0.0;
            double sin=i.getY()/getComplexNumberAbs(i);
            double cos=i.getYI()/getComplexNumberAbs(i);
            if(sin>0&&cos>0){
                argument= Math.asin(sin);
            }
            else if(sin<0&&cos>0){
                argument= Math.asin(sin)+90;
            }
            else if(sin<0 && cos<0){
                argument= Math.asin(sin)+180;
            } else if (sin>0 && cos<0) {
                argument= Math.asin(sin)+240;
            }
            return argument;
        }

    }
