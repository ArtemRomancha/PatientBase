package UI.diagnosis.form;

import Logic.core.Mkb;
import UI.medico.MedicoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by astures on 23.02.17.
 */
public class FormController implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField codeTB;

    @FXML
    private JFXTextField descriptionTB;

    @FXML
    private JFXButton applyBT;

    @FXML
    private JFXButton cancelBT;

    private static String title;

    private static Mkb mkb;

    public static void setMkb(Mkb value) {
        mkb = value;
    }

    public static void setTitle(String value) {
        title = value;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLB.setText(title);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Поле обязательно для заполнения");

        codeTB.getValidators().add(validator);
        codeTB.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) codeTB.validate();
        });
        descriptionTB.getValidators().add(validator);
        descriptionTB.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) descriptionTB.validate();
        });

        if (mkb != null) {
            applyBT.setText("Сохранить");
            codeTB.setText(mkb.getMkbCode().getValue());
            descriptionTB.setText(mkb.getDescription().getValue());
        }
    }

    @FXML
    void applyBTonAction(ActionEvent event) {
        if ((codeTB.getText().length() > 0) && (descriptionTB.getText().length() > 0)) {
            if (mkb != null) {
                mkb.setMkbCode(codeTB.getText());
                mkb.setDescription(descriptionTB.getText());
                mkb.Update();
                mkb = null;
                close();
            } else {
                new Mkb(codeTB.getText(), descriptionTB.getText());
                close();
            }
        }
    }

    @FXML
    void cancelBTonAction(ActionEvent event) {
        if (mkb != null) {
            mkb = null;
        }
        close();
    }

    private void close() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void codeKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            descriptionTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            descriptionTB.requestFocus();
        }
    }

    @FXML
    void descriptionKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            codeTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            codeTB.requestFocus();
        }
    }
}



