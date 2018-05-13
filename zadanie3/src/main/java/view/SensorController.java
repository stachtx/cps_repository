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

import java.net.URL;
import java.util.ResourceBundle;

public class SensorController implements Initializable {




    Signal soundingSignal;

    Signal reflectedSignal;

    Signal corelatedSignal;


    @FXML
    Pane soundingChart;

    @FXML
    Pane reflectedChart;

    @FXML
    Pane corelatedChart;


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





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        soundingSignal= States.getInstance().getSoundingSignal();
        reflectedSignal=States.getInstance().getReflectedsignal();
        corelatedSignal=States.getInstance().getCorelatedSignal();


        createLineChart(soundingChart,soundingSignal);
        createLineChart(reflectedChart,reflectedSignal);
        createLineChart(corelatedChart,corelatedSignal);
    }
}
