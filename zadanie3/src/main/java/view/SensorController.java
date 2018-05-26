package view;

import application.States;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import signals.Point;
import signals.Sensor;
import signals.Signal;
import signals.Target;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class SensorController implements Initializable {


    Sensor sensor;

    boolean isWorking=false;

    @FXML
    Pane soundingChart;

    @FXML
    Pane reflectedChart;

    @FXML
    Pane corelatedChart;

    @FXML
    Button sensorButton;

    public void createLineChart(Pane pane , Signal signal){

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
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);

    }

    public void handleSensor() throws InterruptedException {

        if(isWorking){
            isWorking=false;
            sensorButton.setLabel("stop");
        } else {
            isWorking=true;
            sensorButton.setLabel("start");
        }
        int t=0;
       Sensor sensor=States.getInstance().getSensor();
        while (isWorking){

            sensor.getTarget().setParameters(t);






            TimeUnit.SECONDS.sleep(1);
            t++;

        }

    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*
        boolean isRunning=true;
        sensor=States.getInstance().getSensor();
        sensor.getTarget().run();
        createLineChart(soundingChart, sensor.getSoundingSignal());
      //  while(isRunning) {
            sensor.setReflectedSignal();
            sensor.distanceSensor();


            createLineChart(reflectedChart, sensor.getReflectedSignal());
            createLineChart(corelatedChart, sensor.getCorelatedSignal());
       // }*/
    }
}
