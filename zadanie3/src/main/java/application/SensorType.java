package application;

public enum SensorType {

    spliceCorrelation("Korelacja z wykorzystaniem splotu"),
    directCorrelation("Korelacja bezpośrednia");

    private String label;

    SensorType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }


}
