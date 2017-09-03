package UI.medico.form;

/**
 * Created by astures on 22.02.17.
 */

import Logic.core.Anaesthesiologist;
import Logic.core.Doctor;
import Logic.core.Medico;
import Logic.core.Surgeon;
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

public class FormController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField fnameTB;

    @FXML
    private JFXTextField mnameTB;

    @FXML
    private JFXTextField lnameTB;

    @FXML
    private JFXButton applyBT;

    @FXML
    private JFXButton cancelBT;

    private static String title;

    private static Medico medico;

    public static void setMedico(Medico value)
    {
        medico = value;
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

        fnameTB.getValidators().add(validator);
        fnameTB.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) fnameTB.validate();
        });
        mnameTB.getValidators().add(validator);
        mnameTB.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) mnameTB.validate();
        });
        lnameTB.getValidators().add(validator);
        lnameTB.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal) lnameTB.validate();
        });

        if(medico != null) {
            applyBT.setText("Сохранить");
            fnameTB.setText(medico.getFirstName().getValue());
            mnameTB.setText(medico.getMiddleName().getValue());
            lnameTB.setText(medico.getLastName().getValue());
        }
    }

    @FXML
    void applyBTonAction(ActionEvent event) {
        if((fnameTB.getText().length() > 0) && (mnameTB.getText().length() > 0) && (lnameTB.getText().length() > 0)) {
            if (medico != null) {
                medico.setFirstName(fnameTB.getText());
                medico.setMiddleName(mnameTB.getText());
                medico.setLastName(lnameTB.getText());
                medico.Update();
                medico = null;
                close();
            } else {
                switch (MedicoController.medico) {
                    case "surgeon":
                        new Surgeon(fnameTB.getText(), mnameTB.getText(), lnameTB.getText());
                        close();
                        break;
                    case "anaesthesiologist":
                        new Anaesthesiologist(fnameTB.getText(), mnameTB.getText(), lnameTB.getText());
                        close();
                        break;
                    case "doctor":
                        new Doctor(fnameTB.getText(), mnameTB.getText(), lnameTB.getText());
                        close();
                        break;
                }
            }
        }
    }

    @FXML
    void cancelBTonAction(ActionEvent event) {
        if(medico != null) {
            medico = null;
        }
        close();
    }

    private void close()
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void fnameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            lnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            mnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void lnameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            mnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            fnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void mnameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            fnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            lnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }
}

