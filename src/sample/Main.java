package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Search.fxml"));
        primaryStage.setTitle("Game Search Engine");
        primaryStage.setScene(new Scene(root, 1195, 748));
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
