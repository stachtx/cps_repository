package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import signals.Signal;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("menu.fxml"));
        stage.setTitle("Signals generator");
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }



}
