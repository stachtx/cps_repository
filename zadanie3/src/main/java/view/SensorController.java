package view;

import application.States;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import signals.Point;
import signals.Sensor;
import signals.Signal;
import javafx.scene.control.TextField;

import java.util.List;

public class SensorController {


    boolean isWorking=false;

    @FXML
    Pane soundingChart;

    @FXML
    Pane reflectedChart;

    @FXML
    Pane corelatedChart;

    @FXML
    TextField current, calculatedPosition;

    @FXML
    Button button;


    public void createLineChart(Pane pane , Signal signal){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("numer próbki");
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
        lineChart.prefWidthProperty().bind(pane.widthProperty());
        lineChart.prefHeightProperty().bind(pane.heightProperty());
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);

    }

    public void createLineChart(Pane pane , List<Double> listX, List<Double> listY){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("numer próbki");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data
        for(int i=0; i<listX.size();i++){
            series.getData().add(new XYChart.Data(listX.get(i), listY.get(i)));
        }

        lineChart.getData().add(series);
        lineChart.prefWidthProperty().bind(pane.widthProperty());
        lineChart.prefHeightProperty().bind(pane.heightProperty());
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);

    }
    public void handleSensor() throws InterruptedException {
        int time=0;
        if(isWorking){
            isWorking=false;
            button.setText("stop");
        } else {
            isWorking=true;
            button.setText("start");
        }

       Sensor sensor=States.getInstance().getSensor();
        this.createLineChart(soundingChart,sensor.getSoundingSignal());

      //  while (isWorking){

            Thread.sleep((int)sensor.getRaportPeriod()*1000);
            time++;
            sensor.getTarget().setParameters(time*sensor.getRaportPeriod());
            sensor.distanceSensor();
         this.createLineChart(reflectedChart,sensor.getReflectedSignal());
            //sensor.getSoundingSignal().getAllX(),sensor.getSoundingSignal().getAllY());
            //sensor.getReflectedSignal().getAllX(),sensor.getReflectedSignal().getAllY());
            this.createLineChart(corelatedChart,sensor.getCorelatedSignal());
            calculatedPosition.setText(String.valueOf(sensor.getDistance()));
            current.setText(String.valueOf(sensor.getTarget().getObjectPosition()));
     //   }

    }
}
