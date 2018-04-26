package application;

public enum ReconstructionType {


    zeroExploration("Ekstrapolacja zerowego rzędu"),
    sinc("Rekonstrukcja w oparciu o funkcję sinc");

    private String label;

    ReconstructionType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
