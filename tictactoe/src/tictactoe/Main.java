package tictactoe;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vbox;
        MyController controller;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("check.fxml"));
            vbox = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene s = new Scene(vbox);

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setResizable(false);
        primaryStage.setScene(s);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("xo.png").toString()));
        primaryStage.show();

        controller.initConfig();
        controller.initializeGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
