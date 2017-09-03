package UI.medicalRecord;

import Logic.core.*;
import Logic.utils.connection;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
import UI.medicalRecord.attachment.AttachmentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.sun.java.swing.plaf.motif.MotifEditorPaneUI;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by AstureS on 03.03.2017.
 */
public class MedicalRecordController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label titleLB;

    @FXML
    private JFXComboBox<Surgeon> surgeonCB;

    @FXML
    private JFXComboBox<Anaesthesiologist> anaesthesiologistCB;

    @FXML
    private JFXComboBox<Doctor> doctorCB;

    @FXML
    private JFXComboBox<Mkb> diagnosisCB;

    @FXML
    private JFXDatePicker dataDP;

    @FXML
    private JFXTextArea noticeTA;

    @FXML
    private Label descriptionLB;

    @FXML
    private VBox contentPane;

    @FXML
    private JFXButton attachBT;

    @FXML
    private JFXButton applyBT;

    @FXML
    private JFXButton closeBT;

    private ArrayList<String> permittedFileExtension;
    private List<File> attachedFiles = new ArrayList<>();
    private List<File> lastFiles = new ArrayList<>();
    private static Patient currentPatient;
    private static MedicalRecord currentMedicalRecord;
    private static boolean window = false;

    public static void setCurrentPatient(Patient patient)
    {
        currentPatient = patient;
    }

    public static void setCurrentMedicalRecord(MedicalRecord medicalRecord)
    {
        currentMedicalRecord = medicalRecord;
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

        permittedFileExtension = new ArrayList<>();
        permittedFileExtension.add("bmp");
        permittedFileExtension.add("gif");
        permittedFileExtension.add("jpg");
        permittedFileExtension.add("jpeg");
        permittedFileExtension.add("png");

        String ExtFiles = " (";
        for(String fileExtension : permittedFileExtension) {
            ExtFiles += "*." + fileExtension + ", ";
        }
        descriptionLB.setText(descriptionLB.getText() + ExtFiles.substring(0, ExtFiles.length() - 2) + ")");

        surgeonCB.getItems().addAll(getSurgeon());
        new ComboBoxAutoComplete<>(surgeonCB);
        anaesthesiologistCB.getItems().addAll(getAnaesthesiologist());
        new ComboBoxAutoComplete<>(anaesthesiologistCB);
        doctorCB.getItems().addAll(getDoctor());
        new ComboBoxAutoComplete<>(doctorCB);
        diagnosisCB.getItems().addAll(getDiagnosis());
        new ComboBoxAutoComplete<>(diagnosisCB);

        if(currentMedicalRecord != null) {
            titleLB.setText("Медецинская запись №" + currentMedicalRecord.getId());
            surgeonCB.getSelectionModel().select(FindSurgeon(getSurgeon(), currentMedicalRecord.getSurgeon()));
            anaesthesiologistCB.getSelectionModel().select(FindAnaesthesiologist(getAnaesthesiologist(), currentMedicalRecord.getAnaesthesiologist()));
            doctorCB.getSelectionModel().select(FindDoctor(getDoctor(), currentMedicalRecord.getDoctor()));
            diagnosisCB.getSelectionModel().select(FindDiagnosis(getDiagnosis(), currentMedicalRecord.getDiagnosis()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            dataDP.setValue(LocalDate.parse(currentMedicalRecord.getDateOfCreate(), formatter));
            noticeTA.setText(currentMedicalRecord.getNotice());

            currentMedicalRecord.FillAttachments();
            for (Attachment attachment : currentMedicalRecord.getAttachments()) {
                lastFiles.add(new File(attachment.getFileName()));
                attachedFiles.add(new File(attachment.getFileName()));
            }
            Update();

            applyBT.setText("Сохранить");
        }

        if(window) {
            closeBT.setVisible(true);
        }
    }

    private ArrayList<Surgeon> getSurgeon() {
        ArrayList<Surgeon> surgeons = new ArrayList<>();
        ArrayList<HashMap<String, String>> medics;

        medics = connection.SelectMany(Surgeon.getTableName(), Surgeon.getColumns(), "");
        for (HashMap<String, String> row : medics) {
            surgeons.add(new Surgeon(row));
        }

        return surgeons;
    }

    private ArrayList<Anaesthesiologist> getAnaesthesiologist() {
        ArrayList<Anaesthesiologist> anaesthesiologists = new ArrayList<>();
        ArrayList<HashMap<String, String>> medics;

        medics = connection.SelectMany(Anaesthesiologist.getTableName(), Anaesthesiologist.getColumns(), "");
        for (HashMap<String, String> row : medics) {
            anaesthesiologists.add(new Anaesthesiologist(row));
        }

        return anaesthesiologists;
    }

    private ArrayList<Doctor> getDoctor()
    {
        ArrayList<Doctor> doctors = new ArrayList<>();
        ArrayList<HashMap<String, String>> medics;

                medics = connection.SelectMany(Doctor.getTableName(), Doctor.getColumns(), "");
                for (HashMap<String, String> row : medics) {
                    doctors.add(new Doctor(row));
                }

        return doctors;
    }

    private ArrayList<Mkb> getDiagnosis()
    {
        ArrayList<Mkb> diagnosis = new ArrayList<>();
        ArrayList<HashMap<String, String>> mkb = connection.SelectMany(Mkb.getTableName(), Mkb.getColumns(), "");
        for (HashMap<String, String> row : mkb) {
            diagnosis.add(new Mkb(row));
        }

        return diagnosis;
    }

    private int FindSurgeon(ArrayList<Surgeon> list, Surgeon medico)
    {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == medico.getId()) {
                return i;
            }
        }
        return -1;
    }

    private int FindAnaesthesiologist(ArrayList<Anaesthesiologist> list, Anaesthesiologist medico)
    {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == medico.getId()) {
                return i;
            }
        }
        return -1;
    }

    private int FindDoctor(ArrayList<Doctor> list, Doctor medico)
    {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == medico.getId()) {
                return i;
            }
        }
        return -1;
    }

    private int FindDiagnosis(ArrayList<Mkb> list, Mkb mkb)
    {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == mkb.getId()) {
                return i;
            }
        }
        return -1;
    }

    @FXML
    void applyBTonAction(ActionEvent event)
    {
        if(surgeonCB.getValue() != null && anaesthesiologistCB.getValue() != null && doctorCB != null && diagnosisCB != null && dataDP.getValue() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            if(currentMedicalRecord == null) {
                MedicalRecord record = new MedicalRecord(dataDP.getValue().format(formatter), currentPatient, surgeonCB.getValue(), anaesthesiologistCB.getValue(), doctorCB.getValue(), diagnosisCB.getValue(), noticeTA.getText());
                File path = new File("attachments/" + currentPatient.getId() + "/" + record.getId());
                path.mkdirs();
                for (File file : attachedFiles) {
                    File target = new File(path.getPath() + "/" + file.getName());
                    try {
                        Files.copy(file.toPath(), target.toPath(), REPLACE_EXISTING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    new Attachment(record.getId(), "attachments/" + currentPatient.getId() + "/" + record.getId() + "/" + file.getName());
                }
                showDialog("Успех", "Запись успешно добавлена.", "dialog");
            } else {
                currentMedicalRecord.setPatient(currentPatient);
                currentMedicalRecord.setSurgeon(surgeonCB.getSelectionModel().getSelectedItem());
                currentMedicalRecord.setAnaesthesiologist(anaesthesiologistCB.getSelectionModel().getSelectedItem());
                currentMedicalRecord.setDoctor(doctorCB.getSelectionModel().getSelectedItem());
                currentMedicalRecord.setDiagnosis(diagnosisCB.getSelectionModel().getSelectedItem());
                currentMedicalRecord.setDateOfCreate(dataDP.getValue().format(formatter));
                currentMedicalRecord.setNotice(noticeTA.getText());
                currentMedicalRecord.Update();

                File path = new File("attachments/" + currentPatient.getId() + "/" + currentMedicalRecord.getId());
                path.mkdirs();
                for (File file : attachedFiles) {
                    if(!lastFiles.contains(file)) {
                        File target = new File(path.getPath() + "/" + file.getName());
                        try {
                            Files.copy(file.toPath(), target.toPath(), REPLACE_EXISTING);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        new Attachment(currentMedicalRecord.getId(), "attachments/" + currentPatient.getId() + "/" + currentMedicalRecord.getId() + "/" + file.getName());
                    }
                }

                for (File file : lastFiles) {
                    if(!attachedFiles.contains(file)) {
                        currentMedicalRecord.FindAttachment("attachments/" + currentPatient.getId() + "/" + currentMedicalRecord.getId() + "/" + file.getName()).Delete();
                    }
                }
                currentMedicalRecord = null;
            }

            Parent root = null;
            try {
                if(!window) {
                    root = FXMLLoader.load(getClass().getResource("/UI/patient/actions/actions.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/UI/patient/show/showPatient.fxml"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if(!window) {
                ((Stage)rootPane.getScene().getWindow()).close();
                window = false;
            } else {
                rootPane.getChildren().setAll(root);
            }
        }
    }

    @FXML
    void closeBTOnAction(ActionEvent event) {
        currentMedicalRecord = null;

        Parent root = null;
        try {
            if (!window) {
                root = FXMLLoader.load(getClass().getResource("/UI/patient/actions/actions.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/UI/patient/show/showPatient.fxml"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!window) {
            rootPane.getChildren().setAll(root);
            AnchorPane.setRightAnchor(root, 5.0);
            AnchorPane.setTopAnchor(root, -80.0);
            AnchorPane.setBottomAnchor(root, 5.0);
            AnchorPane.setLeftAnchor(root, 5.0);
            window = false;
        } else {
            rootPane.getChildren().setAll(root);
        }
    }


    @FXML
    void attachBTOnAction(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        String allExtensions = "";
        for(String extension : permittedFileExtension) {
            allExtensions += "*." + extension + "; ";
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Все изображения", allExtensions));
        for(String extension : permittedFileExtension) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.toUpperCase(), "*." + extension));
        }

        List<File> files = fileChooser.showOpenMultipleDialog(rootPane.getScene().getWindow());
        if(files != null) {
            for(File file : files ) {
                DrawAttachedFile(file);
                attachedFiles.add(file);
            }
        }
    }

    @FXML
    void rootPaneOnDragOver(DragEvent event)
    {
        if(event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            for(File file : files) {
                if(!permittedFileExtension.contains(getFileExtension(file))) {
                    event.acceptTransferModes(TransferMode.NONE);
                    return;
                }
            }
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    void rootPaneOnDragDropped(DragEvent event) {

        List<File> files = event.getDragboard().getFiles();

        for(File file : files ) {
            DrawAttachedFile(file);
            attachedFiles.add(file);
        }
    }

    private static String getFileExtension(File file)
    {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private void DrawAttachedFile(File file)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("attachment/attachment.fxml"));
        AttachmentController controller = loader.getController();

        JFXButton button = new JFXButton("Удалить");
        button.setFocusTraversable(false);
        button.getStyleClass().add("delete-button");
        button.setOnAction((e) -> {
            attachedFiles.remove(file);
            Update();
        });
        ImageView deleteIMG = new ImageView();
        deleteIMG.setFitHeight(24);
        deleteIMG.setFitWidth(24);
        button.setGraphic(deleteIMG);
        deleteIMG.setImage(new Image("/img/icons/delete-red.png"));
        button.setOnMouseEntered((e) -> {
            deleteIMG.setImage(new Image("/img/icons/delete-hover.png"));
        });
        button.setOnMouseExited((e) -> {
            deleteIMG.setImage(new Image("/img/icons/delete-red.png"));
        });

        controller.setDeleteBT(button);
        controller.setFile(file);

        try {
            contentPane.getChildren().add(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Update() {
        contentPane.getChildren().clear();
        for (File file : attachedFiles) {
            DrawAttachedFile(file);
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
}
