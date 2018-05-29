package signals;

public class Target {


    private double objectPosition=0.0;                //Położenie obiektu. Położenie pocżatkowe = 0

    private double targetSpeed;                     // Prędkość śledzonego obiektu [M/s]

    public void setParameters(double time) {

        this.objectPosition=time*targetSpeed;
    }

    public double getObjectPosition() {
        return objectPosition;
    }

    public double getTargetSpeed() {
        return targetSpeed;
    }

    public Target(double objectSpeed) {
        this.targetSpeed = objectSpeed;
    }



}
