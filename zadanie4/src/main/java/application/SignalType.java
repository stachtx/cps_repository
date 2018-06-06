package application;

public enum  SignalType {

    noiseUniDis ("szum o rozkładzie jednostajnym"),
    noiseGauss("szum gaussowski"),
    sin("sygnał sinusoidalny"),
    sinOneHalf(" sygnał sinusoidalny wyprostowany jednopołówkowo"),
    sinTwoHalf(" sygnał sinusoidalny wyprostowany dwupołówkowo"),
    rec(" sygnał prostokątny"),
    recSym(" sygnał prostokątny symetryczny"),
    tri("sygnał trójkątny"),
    entityChange("skok jednostkowy"),
    entityImpulse(" impuls jednostkowy"),
    noiseImpulse(" szum impulsowy"),
    s2 (" wariant s2");

    private String label;

    SignalType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
