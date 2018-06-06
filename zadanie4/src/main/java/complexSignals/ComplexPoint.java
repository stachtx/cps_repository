package complexSignals;



public class ComplexPoint  {

    private double x;
    private double y;
    private double yI;

    public ComplexPoint(double x, double y, double yI) {
        this.x = x;

        this.y = y;
        this.yI = yI;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYI() {
        return yI;
    }

    public void setYI(double yI) {
        this.yI = yI;
    }
}
