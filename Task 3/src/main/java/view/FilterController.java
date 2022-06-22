package view;

import application.States;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import signals.Point;
import signals.Signal;

import java.net.URL;
import java.util.ResourceBundle;

public class FilterController implements Initializable {

    @FXML
    Tab before;
    @FXML
    Tab after;


    public void createLineChart(Tab tab, Signal signal){

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
        tab.setContent(lineChart);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createLineChart(before, States.getInstance().getSignal());
        createLineChart(after,States.getInstance().getSecondSignal());
    }
}
