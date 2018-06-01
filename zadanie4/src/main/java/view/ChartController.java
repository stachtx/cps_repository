package view;

import application.ReconstructionType;
import application.SignalType;
import application.States;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import signals.Point;
import signals.Signal;
import signals.SignalOperations;

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
    TextField quantText;
    @FXML
    Pane samp;
    @FXML
    Pane rec;
    @FXML
    Pane quant;
    @FXML
    GridPane aaa;
    @FXML
    Tab quantisationTab;
    @FXML
    ChoiceBox menu;
    @FXML
    Label mseText;
    @FXML
    Label snrText;
    @FXML
    Label psnrText;
    @FXML
    Label mdText;
    @FXML
    Label enobText;

    private ReconstructionType type= ReconstructionType.sinc;
    private  double binWidth=0;


    public Map<Integer, Integer> calculateWeights() {

        List <Double> listY = new ArrayList<>();
        for(Point i : signal.getPoints()){
            listY.add(i.getY());
        }
        return listY.stream().collect(
                Collectors.groupingBy(
                        (x) -> findBin(x),
                        Collectors.mapping(i -> 1.0,
                                Collectors.summingInt(s -> s.intValue()
                                ))
                ));

    }

    public Integer findBin(Double datum){

        int nBins=10;
        List <Double> listY = new ArrayList<>();
        for(Point i : signal.getPoints()){
            listY.add(i.getY());
        }
        double min=Collections.min(listY);

        double max=Collections.max(listY);

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
        for(Point i : signal.getPoints()){
            series.getData().add(new XYChart.Data(i.getX(), i.getY()));
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
        for(Point i : signal.getPoints()){
            series.getData().add(new XYChart.Data(i.getX(), i.getY()));
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
        List <Double> listY = new ArrayList<>();
        for(Point i : signal.getPoints()){
            listY.add(i.getY());
        }
        double min=Collections.min(listY);
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

        Signal sampledSignal= SignalOperations.sampling(signal, Double.valueOf(samplingFrequencyText.getText()));
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
        XYChart.Series secSeries= new XYChart.Series();
        //populating the series with data
        for(Point i : signal.getPoints()){
            series.getData().add(new XYChart.Data(i.getX(), i.getY()));
        }
        for(Point j : sampledSignal.getPoints()){
            secSeries.getData().add(new XYChart.Data(j.getX(), j.getY()));
        }
        lineChart.getData().add(series);
        lineChart.getData().add(secSeries);

        lineChart.prefWidthProperty().bind(samp.widthProperty());
        lineChart.prefHeightProperty().bind(samp.heightProperty());
        samp.getChildren().clear();
        samp.getChildren().add(lineChart);
    }

    public void createQuantisationChart(){
        Signal quantisedSignal=SignalOperations.quantize(signal, Integer.parseInt(quantText.getText()));
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
        XYChart.Series secSeries= new XYChart.Series();
        //populating the series with data
        for(Point i : signal.getPoints()){
            series.getData().add(new XYChart.Data(i.getX(), i.getY()));
        }
        for(Point j : quantisedSignal.getPoints()){
            secSeries.getData().add(new XYChart.Data(j.getX(), j.getY()));
        }
        lineChart.getData().add(series);
        lineChart.getData().add(secSeries);

        lineChart.prefWidthProperty().bind(quant.widthProperty());
        lineChart.prefHeightProperty().bind(quant.heightProperty());
        quant.getChildren().clear();
        quant.getChildren().add(lineChart);
    }

    public void createReconstructionChart(){
    Signal reconstructedSignal= SignalOperations.reconstruct(signal, type);
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
    XYChart.Series secSeries= new XYChart.Series();
    //populating the series with data
    for(Point i : signal.getPoints()){
        series.getData().add(new XYChart.Data(i.getX(), i.getY()));
    }
   for(Point j : reconstructedSignal.getPoints()){
        secSeries.getData().add(new XYChart.Data(j.getX(), j.getY()));
    }
    lineChart.getData().add(series);
    lineChart.getData().add(secSeries);
    lineChart.prefWidthProperty().bind(rec.widthProperty());
    lineChart.prefHeightProperty().bind(rec.heightProperty());
    mseText.setText(String.valueOf(SignalOperations.MSE(signal,reconstructedSignal)));
    snrText.setText(String.valueOf(SignalOperations.SNR(signal,reconstructedSignal)));
    psnrText.setText(String.valueOf(SignalOperations.PSNR(signal,reconstructedSignal)));
    mdText.setText(String.valueOf(SignalOperations.MD(signal,reconstructedSignal)));
    enobText.setText(String.valueOf(SignalOperations.ENOB(signal,reconstructedSignal)));
    rec.getChildren().clear();
    rec.getChildren().add(lineChart);

}

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

       signal= States.getInstance().getSignal();
       if(signal.getType()== SignalType.noiseImpulse || signal.getType()==SignalType.entityImpulse)
            createDotChart(chart);

       else{
           createLineChart();
           lineChartPane(samp);
           lineChartPane(rec);
           lineChartPane(quant);
           Tab tab=new Tab("Sygnał Dyskretny");
            tabPane.getTabs().add(1,tab);
            createDotChart(tab);
       }
       createHistogram();



        samplingFrequencyText.setText(String.valueOf(1));
        quantText.setText(String.valueOf(8));

        menu.setItems( FXCollections.observableArrayList( ReconstructionType.values()));
       // menu.setValue(ReconstructionType.sinc);
//        createReconstructionChart();
        menu.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            this.type=(ReconstructionType) menu.getValue();
            createReconstructionChart();
        });

    }

    public void lineChartPane(Pane p){
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
        for(Point i : signal.getPoints()){
            series.getData().add(new XYChart.Data(i.getX(), i.getY()));
        }

        lineChart.getData().add(series);
        lineChart.prefWidthProperty().bind(p.widthProperty());
        lineChart.prefHeightProperty().bind(p.heightProperty());
        p.getChildren().add(lineChart);
    }
}




