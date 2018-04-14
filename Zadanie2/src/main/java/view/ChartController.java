package view;

import application.SignalType;
import application.States;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import signals.Signal;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ChartController implements Initializable {


    Signal signal;
    @FXML
    Tab chart;
    @FXML
    Tab histogram;
    @FXML
    TabPane tabPane;
    @FXML
    TextField samplingFrequencyText;
    @FXML
    Pane samp;
    @FXML
    Pane rec;
    @FXML
    GridPane aaa;
    @FXML
    Tab quantisationTab;


    private  double binWidth=0;


    public Map<Integer, Integer> calculateWeights() {

        return signal.getY().stream().collect(
                Collectors.groupingBy(
                        (x) -> findBin(x),
                        Collectors.mapping(i -> 1.0,
                                Collectors.summingInt(s -> s.intValue()
                                ))
                ));

    }

    public Integer findBin(Double datum){

        int nBins=10;
        double min=Collections.min(signal.getY());

        double max=Collections.max(signal.getY());

        binWidth=(max-min)/nBins;
        int ret = 0;
        if (datum == max) {
            ret = nBins-1;
        } else if (datum == min) {
            ret = 0;
        } else {
            ret = (int)Math.floor((datum-min)/binWidth);
        }
        return new Integer(ret);
    }

    public void createDotChart(Tab tab){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final ScatterChart<Number,Number> dotChart = new
                ScatterChart<Number,Number>(xAxis,yAxis);

        dotChart.setLegendVisible(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("Equities");
        for(int i=0;i<signal.getY().size();i++){
            series.getData().add(new XYChart.Data(signal.getX().get(i), signal.getY().get(i)));
        }

        dotChart.getData().add(series);
        tab.setContent(dotChart);

    }

    public void createLineChart(){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("czas");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0;i<signal.getY().size();i++){
            series.getData().add(new XYChart.Data(signal.getX().get(i), signal.getY().get(i)));
        }

        lineChart.getData().add(series);

        chart.setContent(lineChart);
                

    }

    public void createHistogram(){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart =
                new BarChart<>(xAxis,yAxis);
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);

        xAxis.setLabel("Range");
        yAxis.setLabel("Population");

        XYChart.Series series = new XYChart.Series();
        series.setName("Histogram");
        java.text.DecimalFormat df=new java.text.DecimalFormat(); //tworzymy obiekt DecimalFormat
        df.setMaximumFractionDigits(1); //dla df ustawiamy największą ilość miejsc po przecinku
        df.setMinimumFractionDigits(1);
        double min=Collections.min(signal.getY());
        Map<Integer,Integer> weights =calculateWeights();
        for(Map.Entry<Integer,Integer> i: weights.entrySet()){

            double a=min+i.getKey()*binWidth;
            double b=min+(i.getKey()+1)*binWidth;
            series.getData().add(new XYChart.Data("< "+df.format(a)+", "+df.format(b)+" >",i.getValue()));
        }

        barChart.getData().addAll(series);

        histogram.setContent(barChart);
    }

    public void createSamplingChart(){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("czas");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0;i<signal.getY().size();i++){
            series.getData().add(new XYChart.Data(signal.getX().get(i), signal.getY().get(i)));
        }

        lineChart.getData().add(series);

        lineChart.prefWidthProperty().bind(samp.widthProperty());
        lineChart.prefHeightProperty().bind(samp.heightProperty());
        samp.getChildren().add(lineChart);
    }
    public void createQuantisationChart(){
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("czas");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0;i<signal.getY().size();i++){
            series.getData().add(new XYChart.Data(signal.getX().get(i), signal.getY().get(i)));
        }

        lineChart.getData().add(series);

        quantisationTab.setContent(lineChart);
    }

public void createReconstructionChart(){
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("czas");
    //creating the chart
    final LineChart<Number,Number> lineChart =
            new LineChart<Number,Number>(xAxis,yAxis);

    lineChart.setCreateSymbols(false);
    lineChart.setLegendVisible(false);
    //defining a series
    XYChart.Series series = new XYChart.Series();
    //populating the series with data
    for(int i=0;i<signal.getY().size();i++){
        series.getData().add(new XYChart.Data(signal.getX().get(i), signal.getY().get(i)));
    }

    lineChart.getData().add(series);

    lineChart.prefWidthProperty().bind(rec.widthProperty());
    lineChart.prefHeightProperty().bind(rec.heightProperty());
    rec.getChildren().add(lineChart);

}

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

       signal= States.getInstance().getSignal();
       if(signal.getType()== SignalType.noiseImpulse || signal.getType()==SignalType.entityImpulse)
            createDotChart(chart);

       else{
           createLineChart();
           Tab tab=new Tab("Sygnał Dyskretny");
            tabPane.getTabs().add(1,tab);
            createDotChart(tab);
       }
       createHistogram();
       createSamplingChart();
       createQuantisationChart();
        createReconstructionChart();

    }

}




