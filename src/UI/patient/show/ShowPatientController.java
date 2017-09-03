package UI.patient.show;

import Logic.core.MedicalRecord;
import Logic.core.Patient;
import UI.medicalRecord.MedicalRecordController;
import UI.medicalRecord.mini.MiniMedRecController;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
import UI.patient.form.FormController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by AstureS on 06.03.2017.
 */
public class ShowPatientController implements Initializable
{
    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField lnameTB;

    @FXML
    private JFXTextField nameTB;

    @FXML
    private JFXTextField mnameTB;

    @FXML
    private JFXTextField sexTB;

    @FXML
    private JFXTextField dobTB;

    @FXML
    private JFXTextField medRecTB;

    @FXML
    private JFXTextField phoneTB;

    @FXML
    private JFXTextField adresTB;

    @FXML
    private JFXTextArea noticeTB;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentPane;

    @FXML
    private JFXButton editBT;

    @FXML
    private JFXButton closeBT;

    private static Patient patient;

    public static void setPatient(Patient showPatient)
    {
        patient = showPatient;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lnameTB.setText(patient.getLastName().getValue());
        nameTB.setText(patient.getFirstName().getValue());
        mnameTB.setText(patient.getMiddleName().getValue());
        sexTB.setText(patient.getSex().getValue());
        dobTB.setText(patient.getDateOfBirth().getValue());
        medRecTB.setText(patient.getMedicalRecordNo().getValue());
        phoneTB.setText(patient.getPhoneNumber().getValue());
        adresTB.setText(patient.getAdress().getFullAdress());
        noticeTB.setText(patient.getNotice());

        Update();
    }

    @FXML
    void closeBTOnAction(ActionEvent event)
    {
        ((Stage)contentPane.getScene().getWindow()).close();
    }

    @FXML
    void editBTOnAction(ActionEvent event)
    {
        FormController.setPatient(patient);
        FormController.setWindow(true);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/patient/form/form.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        contentPane.getScene().setRoot(root);
    }

    private void DrawMedRec(MedicalRecord medicalRecord)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/UI/medicalRecord/mini/miniMedRec.fxml"));
        MiniMedRecController.setMedRec(medicalRecord);

        JFXButton deleteBT = new JFXButton("Удалить");
        deleteBT.setFocusTraversable(false);
        deleteBT.getStyleClass().add("delete-button");
        deleteBT.setOnAction((e) -> {
            showDialog("Подтверждение", "Вы уверены, что хотите\nудалить запись?");
            if(yesNoController.getResult()) {
                medicalRecord.Delete();
                Update();
            }
        });

        ImageView deleteIMG = new ImageView();
        deleteIMG.setFitHeight(24);
        deleteIMG.setFitWidth(24);
        deleteBT.setGraphic(deleteIMG);
        deleteIMG.setImage(new Image("/img/icons/delete-red.png"));
        deleteBT.setOnMouseEntered((e) -> deleteIMG.setImage(new Image("/img/icons/delete-hover.png")));
        deleteBT.setOnMouseExited((e) -> deleteIMG.setImage(new Image("/img/icons/delete-red.png")));

        JFXButton editBT = new JFXButton("Изменить");
        editBT.setFocusTraversable(false);
        editBT.getStyleClass().add("edit-button");
        editBT.setOnAction((e) -> {
            MedicalRecordController.setCurrentPatient(patient);
            MedicalRecordController.setCurrentMedicalRecord(medicalRecord);
            MedicalRecordController.setWindow(true);
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/UI/medicalRecord/medicalRecord.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            contentPane.getScene().setRoot(root);

            Update();
        });

        ImageView editIMG = new ImageView();
        editIMG.setFitHeight(24);
        editIMG.setFitWidth(24);
        editBT.setGraphic(editIMG);
        editIMG.setImage(new Image("/img/icons/pencil-green.png"));
        editBT.setOnMouseEntered((e) -> editIMG.setImage(new Image("/img/icons/pencil-hover.png")));
        editBT.setOnMouseExited((e) -> editIMG.setImage(new Image("/img/icons/pencil-green.png")));

        MiniMedRecController.setDeleteBT(deleteBT);
        MiniMedRecController.setEditBT(editBT);
        try {
            contentPane.getChildren().add(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Update() {
        contentPane.getChildren().clear();
        patient.FillMedicalRecords();
        for (MedicalRecord medicalRecord : patient.getMedicalRecords()) {
            DrawMedRec(medicalRecord);
        }

        AnchorPane AP = new AnchorPane();
        JFXButton newMedRec = new JFXButton("Добавить");
        newMedRec.setPrefHeight(75);
        AnchorPane.setBottomAnchor(newMedRec, 0.0);
        AnchorPane.setLeftAnchor(newMedRec, 0.0);
        AnchorPane.setTopAnchor(newMedRec, 0.0);
        AnchorPane.setRightAnchor(newMedRec, 0.0);
        AP.setPadding(new Insets(5,5,5,5));

        newMedRec.setFocusTraversable(false);
        newMedRec.getStyleClass().add("edit-button");
        newMedRec.setOnAction((e) -> {
            MedicalRecordController.setCurrentPatient(patient);
            Parent root = null;
            try {
                MedicalRecordController.setWindow(true);
                root = FXMLLoader.load(getClass().getResource("/UI/medicalRecord/medicalRecord.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            contentPane.getScene().setRoot(root);

            Update();
        });
        AP.getChildren().add(newMedRec);
        contentPane.getChildren().add(AP);
    }

    private void showDialog(String header, String text) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/UI/modal/yesNo/yesNo.fxml"));
        yesNoController controller = loader.getController();
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
        stage.initOwner(titleLB.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        titleLB.getScene().getRoot().setEffect(effectPressed);

        stage.showAndWait();

        titleLB.getScene().getRoot().setEffect(null);
    }
}
