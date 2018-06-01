package signals;

import signals.interfaces.IPoint;

public class ComplexPoint implements IPoint {

    private double x;
    private double xI;
    private double y;
    private double yI;

    public ComplexPoint(double x, double xI, double y, double yI) {
        this.x = x;
        this.xI = xI;
        this.y = y;
        this.yI = yI;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getxI() {
        return xI;
    }

    public void setxI(double xI) {
        this.xI = xI;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getyI() {
        return yI;
    }

    public void setyI(double yI) {
        this.yI = yI;
    }
}
