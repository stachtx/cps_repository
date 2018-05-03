package application;

public enum FilterType {

    bottomRec("Filtracja dolnoprzepustowa z oknem prostokątnym"),
    topRec("Filtracja górnoprzepustowa z oknem prostokątnym"),
    bottomHanning("Filtracja dolnoprzepustowa z oknem Hanninga"),
    topHanning("Filtracja górnoprzepustowa z oknem Hanninga");

    private String label;

    FilterType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
