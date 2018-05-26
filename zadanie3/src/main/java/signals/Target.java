package signals;

public class Target {


    private double objectPosition=0;                //Położenie obiektu. Położenie pocżatkowe = 0

    private double targetSpeed;                     // Prędkość śledzonego obiektu [M/s]

    private double time=0.0;


    public double getTime() {
        return time;
    }

    public void setParameters(double time) {
        this.time = time;
        this.objectPosition+=time*targetSpeed;
    }

    public double getObjectPosition() {
        return objectPosition;
    }

    public void setObjectPosition(double objectPosition) {
        this.objectPosition = objectPosition;
    }

    public double getTargetSpeed() {
        return targetSpeed;
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public Target(double objectSpeed) {
        this.targetSpeed = objectSpeed;
    }



}
