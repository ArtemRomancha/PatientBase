package UI.splash;

/**
 * Created by astures on 21.02.17.
 */

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SplashController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread
    {
        @Override
        public void run()
        {
            try {
                Thread.sleep(2000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), rootPane);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);
                        fadeOut.setCycleCount(1);
                        fadeOut.play();
                        fadeOut.setOnFinished((e) -> showMain());
                    }
                });
            } catch (InterruptedException ex) {
                System.out.print(ex);
            }
        }
    }

    void showMain() {
        AnchorPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/main/main.fxml"));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        Stage primaryStage = (Stage) rootPane.getScene().getWindow();
        primaryStage.setTitle("Учет данных пациентов");
        primaryStage.setResizable(true);
        primaryStage.getScene().setRoot(root);
    }
}
