package UI.profile.changePassword;

import Logic.controllers.GeneralController;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
import UI.user.UserController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by aroma on 09.03.2017.
 */

public class ChangePasswordController
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField oldPassTB;

    @FXML
    private JFXTextField newPassTB;

    @FXML
    private JFXTextField renewPassTB;

    @FXML
    private JFXButton changeBT;

    @FXML
    private JFXButton closeBT;

    @FXML
    void closeBTOnAction(ActionEvent event)
    {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    void changeBTOnAction(ActionEvent event)
    {
        if (oldPassTB.getText().length() * newPassTB.getText().length() * renewPassTB.getText().length() != 0) {
            if(GeneralController.getCurrentUser().ValidatePassword(oldPassTB.getText())) {
                if(newPassTB.getText().equals(renewPassTB.getText())) {
                    GeneralController.getCurrentUser().ChangePassword(newPassTB.getText());
                    ((Stage)rootPane.getScene().getWindow()).close();
                } else {
                    showDialog("Ошибка", "Поля нового пароля не совпадают");
                }
            } else {
                showDialog("Ошибка", "Указан не правильный пароль");
            }
        } else {
            showDialog("Ошибка", "Все поля обязательны для заполнения");
        }
    }

    private void showDialog(String header, String text)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../modal/dialog/dialog.fxml"));
        DialogController controller = loader.getController();
        controller.setTitle(header);
        controller.setContent(text);

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        stage.setTitle(header);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        rootPane.setEffect(effectPressed);

        stage.showAndWait();

        rootPane.setEffect(null);
    }
}
