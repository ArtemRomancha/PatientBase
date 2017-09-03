package UI.login;

/**
 * Created by astures on 21.02.17.
 */

import Logic.controllers.GeneralController;
import Logic.core.User;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    void loginPressBTN(ActionEvent event)
    {
        if(username.getText().length() != 0 && password.getText().length() != 0) {
            User curr = User.FindUser(username.getText());

            boolean wrongData = false;

            if (!curr.isNull()) {
                if (curr.ValidatePassword(password.getText())) {
                    GeneralController.setCurrenUser(curr);

                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), rootPane);
                    fadeOut.setFromValue(1);
                    fadeOut.setToValue(0);
                    fadeOut.setCycleCount(1);

                    fadeOut.play();
                    fadeOut.setOnFinished((e) -> {
                        showSplash();
                    });
                } else {
                    wrongData = true;
                }
            } else {
                wrongData = true;
            }

            if(wrongData) {
                showDialog("\tОшибочные данные", "Указанные ИМЯ ПОЛЬЗОВАТЕЛЯ \n" +
                        "И/ИЛИ ПАРОЛЬ ошибочны");
            }
        } else  {
            showDialog("\tЕсть пустые поля", "Заполните, пожалуйста, все поля");
        }
    }

    @FXML
    void loginBTKeyPressed(KeyEvent e)
    {
        if(e.getCode().equals(KeyCode.TAB)) {
            username.requestFocus();
        }
        if(e.getCode().equals(KeyCode.ENTER)) {
            loginPressBTN(new ActionEvent());
        }
    }

    @FXML
    void usernameTFKeyPressed(KeyEvent e)
    {
        if(e.getCode().equals(KeyCode.TAB)) {
            password.requestFocus();
        }
        if(e.getCode().equals(KeyCode.ENTER)) {
            loginPressBTN(new ActionEvent());
        }
    }

    @FXML
    void passwordTFKeyPressed(KeyEvent e)
    {
        if(e.getCode().equals(KeyCode.TAB)) {
            username.requestFocus();
        }
        if(e.getCode().equals(KeyCode.ENTER)) {
            loginPressBTN(new ActionEvent());
        }
    }

    void showDialog(String header, String text)
    {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        content.setBody(new Text(text));

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("ОК");
        button.setOnAction((e) -> dialog.close());

        content.setActions(button);
        dialog.show();
    }

    void showSplash()
    {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/splash/splash.fxml"));
        } catch (IOException ex) {
            System.out.println(ex.fillInStackTrace());
        }

        rootPane.getChildren().setAll(root);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }


}
