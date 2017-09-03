package UI.profile;

import Logic.controllers.GeneralController;
import UI.modal.dialog.DialogController;
import UI.profile.changePassword.ChangePasswordController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by AstureS on 27.02.2017.
 */
public class ProfileController implements Initializable
{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField loginTB;

    @FXML
    private JFXTextField fioTB;

    @FXML
    private JFXButton changePasswordBT;

    @FXML
    private ImageView changePasswordIMG;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        changePasswordIMG.setImage(new Image("/img/icons/alert.png"));
        changePasswordBT.setOnMouseEntered((e) -> changePasswordIMG.setImage(new Image("/img/icons/alert-hover.png")));
        changePasswordBT.setOnMouseExited((e) -> changePasswordIMG.setImage(new Image("/img/icons/alert.png")));

        loginTB.setText(GeneralController.getCurrentUser().getLogin().getValue());
        fioTB.setText(GeneralController.getCurrentUser().getFio().getValue());
    }

    @FXML
    void changePasswordBTonAction(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("changePassword/changePassword.fxml"));

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        changePasswordBT.getScene().getRoot().setEffect(effectPressed);

        stage.showAndWait();

        changePasswordBT.getScene().getRoot().setEffect(null);
    }
}
