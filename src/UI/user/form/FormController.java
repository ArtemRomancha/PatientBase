package UI.user.form;

/**
 * Created by astures on 22.02.17.
 */

import Logic.controllers.GeneralController;
import Logic.core.*;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField loginTB;

    @FXML
    private JFXTextField fioTB;

    @FXML
    private JFXButton applyBT;

    @FXML
    private JFXButton cancelBT;

    @FXML
    private JFXCheckBox adminCB;

    @FXML
    private JFXButton resetpwBT;

    @FXML
    private StackPane stackPane;

    private static String title;

    private static User user;

    public static void setUser(User value)
    {
        user = value;
    }

    public static void setTitle(String value)
    {
        title = value;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLB.setText(title);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Поле обязательно для заполнения");

        loginTB.getValidators().add(validator);
        loginTB.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) loginTB.validate();
        });
        fioTB.getValidators().add(validator);
        fioTB.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) fioTB.validate();
        });

        if(user != null) {
            applyBT.setText("Сохранить");
            loginTB.setText(user.getLogin().getValue());
            loginTB.setDisable(true);
            fioTB.setText(user.getFio().getValue());
            //fioTB.setDisable(true);
            adminCB.setSelected(user.getAdminAccess().getValue());
            resetpwBT.setVisible(true);
        } else {
            resetpwBT.setVisible(false);
            loginTB.setDisable(false);
            //fioTB.setDisable(false);
        }
    }

    private void showDialog(String header, String text, String dialogType)
    {
        FXMLLoader loader = new FXMLLoader();
        if(dialogType.equals("dialog")) {
            loader.setLocation(getClass().getResource("../../modal/dialog/dialog.fxml"));
            DialogController controller = loader.getController();
            controller.setTitle(header);
            controller.setContent(text);
        }
        if(dialogType.equals("yesNo")) {
            loader.setLocation(getClass().getResource("../../modal/yesNo/yesNo.fxml"));
            yesNoController controller = loader.getController();
            controller.setTitle(header);
            controller.setContent(text);
        }

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

    @FXML
    void resetBTonAction(ActionEvent event) {
        showDialog("Подтверждение", "Вы уверены, что хотите\nсбросить пароль?", "yesNo");
        if(yesNoController.getResult()){
            user.ResetPassword();
        }
    }

    @FXML
    void applyBTonAction(ActionEvent event) {
        if ((loginTB.getText().length() > 0) && (fioTB.getText().length() > 0)) {
            if (user != null) {
                user.setAdminAccess(adminCB.isSelected());
                user.setFio(fioTB.getText());
                user.Update();
                user = null;
                close();
            } else {
                if(User.CheckLogin(loginTB.getText())) {
                    new User(loginTB.getText(), fioTB.getText(), GeneralController.getDefaultPassword(), adminCB.isSelected(), false);
                    close();
                } else {
                    showDialog("Ошибка",  "Невозможно создать пользователя! \nПользователь с таким логином\nуже существует!", "dialog");
                }
            }
        }
    }

    @FXML
    void cancelBTonAction(ActionEvent event) {
        if(user != null) {
            user = null;
        }
        close();
    }

    private void close()
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            adminCB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            fioTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void fioKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            loginTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            adminCB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void adminKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            fioTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            loginTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }
}

