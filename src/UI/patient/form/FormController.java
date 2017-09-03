package UI.patient.form;

import Logic.core.*;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
import UI.medicalRecord.MedicalRecordController;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by AstureS on 01.03.2017.
 */
public class FormController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField lnameTB;

    @FXML
    private JFXTextField nameTB;

    @FXML
    private JFXTextField mnameTB;

    @FXML
    private JFXComboBox<Label> sexCB;

    @FXML
    private JFXDatePicker dobDP;

    @FXML
    private JFXTextField medRecTB;

    @FXML
    private JFXTextField phoneTB;

    @FXML
    private JFXTextField regionTB;

    @FXML
    private JFXTextField cityTB;

    @FXML
    private JFXTextField streetTB;

    @FXML
    private JFXTextField houseNumTB;

    @FXML
    private JFXTextField flatNumTB;

    @FXML
    private JFXTextArea noticeTA;

    @FXML
    private JFXButton applyBT;

    private static Patient patient;
    private static boolean window = false;

    public static void setPatient(Patient editPatient)
    {
        patient = editPatient;
    }

    public static void setWindow(boolean value)
    {
        window = value;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        if(patient != null) {
            titleLB.setText("Редактирование пациента " + patient.getId());
            applyBT.setText("Сохранить");
            nameTB.setText(patient.getFirstName().getValue());
            mnameTB.setText(patient.getMiddleName().getValue());
            lnameTB.setText(patient.getLastName().getValue());
            medRecTB.setText(patient.getMedicalRecordNo().getValue());
            phoneTB.setText(patient.getPhoneNumber().getValue());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            dobDP.setValue(LocalDate.parse(patient.getDateOfBirth().getValue(), formatter));
            sexCB.setValue(sexCB.getItems().get(patient.getSex().getValue().equals("мужчина") ? 0 : 1));
            noticeTA.setText(patient.getNotice());

            regionTB.setText(patient.getAdress().getRegion());
            cityTB.setText(patient.getAdress().getCity());
            streetTB.setText(patient.getAdress().getStreet());
            houseNumTB.setText(patient.getAdress().getHouseNumber());
            flatNumTB.setText(patient.getAdress().getFlatNumber() == -1 ? "" : Integer.toString(patient.getAdress().getFlatNumber()));
        }
    }

    @FXML
    void applyBTonAction(ActionEvent event) {
        if (lnameTB.getLength() * nameTB.getLength() * mnameTB.getLength() * medRecTB.getLength() * phoneTB.getLength() * regionTB.getLength() * cityTB.getLength() * streetTB.getLength() * houseNumTB.getLength() > 0 && sexCB.getValue() != null && dobDP.getValue() != null) { //Если хоть одно из полей будет пусто, то произведение будет равно 0
            Parent root = null;

            int flat = -1;
            if (flatNumTB.getLength() > 0) {
                flat = Integer.parseInt(flatNumTB.getText());
            }

            if (patient == null) {
                Adres adres = new Adres(regionTB.getText(), cityTB.getText(), streetTB.getText(), houseNumTB.getText(), flat);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Patient patient = new Patient(medRecTB.getText(), nameTB.getText(), mnameTB.getText(), lnameTB.getText(), sexCB.getValue().getText(), dobDP.getValue().format(formatter), adres, phoneTB.getText(), noticeTA.getText());
                showDialog("Успех", "Пациент успешно добавлен. \nПерейти к добавлению медецинской записи?", "yesNo");

                try {
                    if (yesNoController.getResult()) {
                        MedicalRecordController.setCurrentPatient(patient);
                        MedicalRecordController.setWindow(false);
                        root = FXMLLoader.load(getClass().getResource("/UI/medicalRecord/medicalRecord.fxml"));
                    } else {
                        ((Stage) rootPane.getScene().getWindow()).close();
                        return;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Adres adres = patient.getAdress();
                adres.setRegion(regionTB.getText());
                adres.setCity(cityTB.getText());
                adres.setStreet(streetTB.getText());
                adres.setHouseNumber(houseNumTB.getText());
                adres.setFlatNumber(flat);
                adres.Update();

                patient.setFirstName(nameTB.getText());
                patient.setMiddleName(mnameTB.getText());
                patient.setLastName(lnameTB.getText());
                patient.setSex(sexCB.getValue().getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                patient.setdateOfBirth(dobDP.getValue().format(formatter));
                patient.setMedicalRecordNo(medRecTB.getText());
                patient.setPhoneNumber(phoneTB.getText());
                patient.setNotice(noticeTA.getText());
                patient.Update();

                patient = null;
                try {
                    if(!window) {
                        ((Stage)rootPane.getScene().getWindow()).close();
                        return;
                    } else {
                        root = FXMLLoader.load(getClass().getResource("/UI/patient/show/showPatient.fxml"));
                        window = false;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            rootPane.getChildren().setAll(root);
            AnchorPane.setRightAnchor(root, 5.0);
            AnchorPane.setTopAnchor(root, 5.0);
            AnchorPane.setBottomAnchor(root, 5.0);
            AnchorPane.setLeftAnchor(root, 5.0);
        } else {
            showDialog("Недостаточно данных", "Заполните, пожалуйста, все поля!", "dialog");
        }

    }

    private void showDialog(String header, String text, String dialogType)
    {
        FXMLLoader loader = new FXMLLoader();
        if(dialogType.equals("dialog")) {
            loader.setLocation(getClass().getResource("/UI/modal/dialog/dialog.fxml"));
            DialogController controller = loader.getController();
            controller.setTitle(header);
            controller.setContent(text);
        }
        if(dialogType.equals("yesNo")) {
            loader.setLocation(getClass().getResource("/UI/modal/yesNo/yesNo.fxml"));
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

        Parent rootParent = rootPane.getScene().getRoot();
        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        rootParent.setEffect(effectPressed);

        stage.showAndWait();

        rootParent.setEffect(null);
    }

    @FXML
    void cityTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            regionTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            streetTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void dobDPKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            sexCB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            medRecTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void flatNumTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            houseNumTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            noticeTA.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void noticeTAOnKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            flatNumTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            lnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }


    @FXML
    void houseNumTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            streetTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            flatNumTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void lnameTBonKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            flatNumTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            nameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void medRecTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            dobDP.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            phoneTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void mnameTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            nameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            sexCB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void nameTBonKeyPressed(KeyEvent event)
    {
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
    void phoneTNKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            medRecTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            regionTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void regionTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            phoneTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            cityTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void sexCBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            mnameTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            dobDP.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }

    @FXML
    void streetTBKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.TAB) && event.isShiftDown()) {
            cityTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.TAB) && !event.isShiftDown()) {
            houseNumTB.requestFocus();
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            applyBTonAction(new ActionEvent());
        }
    }
}
