package signals;

public class Target implements Runnable{


    private double objectPosition=0;                //Położenie obiektu. Położenie pocżatkowe = 0

    private double objectSpeed;                     // Prędkość śledzonego obiektu [M/s]


    public double getObjectPosition() {
        return objectPosition;
    }

    public void setObjectPosition(double objectPosition) {
        this.objectPosition = objectPosition;
    }

    public double getObjectSpeed() {
        return objectSpeed;
    }

    public void setObjectSpeed(double objectSpeed) {
        this.objectSpeed = objectSpeed;
    }

    public Target(double objectSpeed) {
        this.objectSpeed = objectSpeed;
    }

    @Override
    public void run() {

        double t=0.0;
        while(true) {

            try {
                //usypiamy wątek na 100 milisekund
                Thread.sleep((long) (1000));
                this.objectPosition+=objectSpeed*t;
                t++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
