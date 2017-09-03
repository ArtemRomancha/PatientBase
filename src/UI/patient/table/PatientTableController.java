package UI.patient.table;

/**
 * Created by astures on 22.02.17.
 */
import Logic.core.*;
import Logic.utils.connection;
import UI.modal.yesNo.yesNoController;
import UI.patient.form.FormController;
import UI.patient.show.ShowPatientController;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PatientTableController implements Initializable
{
    @FXML
    private AnchorPane patientAP;

    @FXML
    private JFXTreeTableView<Patient> patientTV;

    @FXML
    private JFXTextField filterTB;

    private JFXButton actionBT;
    private JFXButton showBT;
    private JFXButton editBT;
    private JFXButton deleteBT;
    private JFXButton newBT;
    private JFXNodesList buttonsList;
    private TreeItem<Patient> root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), patientAP);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        Tooltip filterBTToolTip = new Tooltip();
        filterBTToolTip.setText("Начните вводить информацию и таблица будет отфильтрована");
        filterTB.setTooltip(filterBTToolTip);

        filterTB.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                patientTV.setPredicate(new Predicate<TreeItem<Patient>>() {
                    @Override
                    public boolean test(TreeItem<Patient> patient) {
                        boolean flag = false;
                        String patientInfo = patient.toString();
                        String[] subNewValue = newValue.split(" ");
                        for(String string : subNewValue) {
                            if(patientInfo.toLowerCase().contains(string.toLowerCase())) {
                                flag = true;
                            } else {
                                flag = false;
                                break;
                            }
                        }
                        return flag;
                    }
                });
            }
        });

        addButtons();

        JFXTreeTableColumn<Patient, String> medRecord = new JFXTreeTableColumn<>("№ карты");
        medRecord.setCellValueFactory(cellData -> cellData.getValue().getValue().getMedicalRecordNo());

        JFXTreeTableColumn<Patient, String> firstName = new JFXTreeTableColumn<>("Имя");
        firstName.setCellValueFactory(cellData -> cellData.getValue().getValue().getFirstName());

        JFXTreeTableColumn<Patient, String> middleName = new JFXTreeTableColumn<>("Отчество");
        middleName.setCellValueFactory(cellData -> cellData.getValue().getValue().getMiddleName());

        JFXTreeTableColumn<Patient, String> lastName = new JFXTreeTableColumn<>("Фамилия");
        lastName.setCellValueFactory(cellData -> cellData.getValue().getValue().getLastName());

        JFXTreeTableColumn<Patient, String> phone = new JFXTreeTableColumn<>("Телефон");
        phone.setCellValueFactory(cellData -> cellData.getValue().getValue().getPhoneNumber());

        JFXTreeTableColumn<Patient, String> data = new JFXTreeTableColumn<>("Дата рождения");
        data.setCellValueFactory(cellData -> cellData.getValue().getValue().getDateOfBirth());

        root = new RecursiveTreeItem<>(getPatients(), RecursiveTreeObject::getChildren);
        patientTV.getColumns().setAll(medRecord, firstName, middleName, lastName, data, phone);

        patientTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateButtons(newValue != null));

        patientTV.setOnMouseClicked((e) -> {
            if(patientTV.getSelectionModel().getSelectedIndex() != -1) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (e.getClickCount() == 2) {
                        showBT.fire();
                    }
                }
            }
        });

        patientTV.setRoot(root);
        patientTV.setShowRoot(false);
    }

    private ObservableList<Patient> getPatients()
    {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        ArrayList<HashMap<String, String>> temp = connection.SelectMany(Patient.getTableName(), Patient.getColumns(), "");
        for (HashMap<String, String> row : temp) {
            patients.add(new Patient(row));
        }

        return patients;
    }

    public void UpdateList()
    {
        root = new RecursiveTreeItem<>(getPatients(), RecursiveTreeObject::getChildren);
        patientTV.setRoot(root);
    }

    private void addButtons()
    {
        actionBT = new JFXButton("");
        actionBT.setButtonType(JFXButton.ButtonType.RAISED);
        actionBT.getStyleClass().addAll("animated-button", "animated-action-button");
        Tooltip aBTToolTip = new Tooltip();
        aBTToolTip.setText("Панель действий");
        actionBT.setTooltip(aBTToolTip);

        ImageView actionIMG = new ImageView();
        actionBT.setGraphic(actionIMG);
        actionIMG.setImage(new Image("/img/icons/animation-hover.png"));
        actionBT.setOnMouseEntered((e) -> actionIMG.setImage(new Image("/img/icons/animation.png")));
        actionBT.setOnMouseExited((e) -> actionIMG.setImage(new Image("/img/icons/animation-hover.png")));

        showBT = new JFXButton("");
        showBT.setButtonType(JFXButton.ButtonType.RAISED);
        showBT.getStyleClass().addAll("animated-button", "animated-show-button");
        showBT.setOnAction((e) -> {
            int selectedIndex = patientTV.getSelectionModel().getSelectedIndex();
            showPatient(patientTV.getTreeItem(selectedIndex).getValue());
        });
        Tooltip sBTToolTip = new Tooltip();
        sBTToolTip.setText("Показать профиль пациента");
        showBT.setTooltip(sBTToolTip);

        ImageView showIMG = new ImageView();
        showBT.setGraphic(showIMG);
        showIMG.setImage(new Image("/img/icons/eye-hover.png"));
        showBT.setOnMouseEntered((e) -> showIMG.setImage(new Image("/img/icons/eye-cyan.png")));
        showBT.setOnMouseExited((e) -> showIMG.setImage(new Image("/img/icons/eye-hover.png")));

        editBT = new JFXButton("");
        editBT.setButtonType(JFXButton.ButtonType.RAISED);
        editBT.getStyleClass().addAll("animated-button", "animated-edit-button");
        editBT.setOnAction((e) -> {
            int selectedIndex = patientTV.getSelectionModel().getSelectedIndex();
            editPatient(patientTV.getTreeItem(selectedIndex).getValue());
            UpdateList();
        });
        Tooltip eBTToolTip = new Tooltip();
        eBTToolTip.setText("Редактировать");
        editBT.setTooltip(eBTToolTip);

        ImageView editIMG = new ImageView();
        editBT.setGraphic(editIMG);
        editIMG.setImage(new Image("/img/icons/pencil-hover.png"));
        editBT.setOnMouseEntered((e) -> editIMG.setImage(new Image("/img/icons/pencil-orange.png")));
        editBT.setOnMouseExited((e) -> editIMG.setImage(new Image("/img/icons/pencil-hover.png")));

        deleteBT = new JFXButton("");
        deleteBT.setButtonType(JFXButton.ButtonType.RAISED);
        deleteBT.getStyleClass().addAll("animated-button", "animated-delete-button");
        deleteBT.setOnAction((e) -> {
            int selectedIndex = patientTV.getSelectionModel().getSelectedIndex();
            Dialog("Подтверждение", "Вы уверены, что хотите\nудалить пациента?");
            if(yesNoController.getResult()) {
                patientTV.getTreeItem(selectedIndex).getValue().Delete();
                UpdateList();
            }
        });
        Tooltip dBTToolTip = new Tooltip();
        dBTToolTip.setText("Удалить");
        deleteBT.setTooltip(dBTToolTip);

        ImageView deleteIMG = new ImageView();
        deleteBT.setGraphic(deleteIMG);
        deleteIMG.setImage(new Image("/img/icons/delete-hover.png"));
        deleteBT.setOnMouseEntered((e) -> deleteIMG.setImage(new Image("/img/icons/delete-red.png")));
        deleteBT.setOnMouseExited((e) -> deleteIMG.setImage(new Image("/img/icons/delete-hover.png")));

        newBT = new JFXButton("");
        newBT.setButtonType(JFXButton.ButtonType.RAISED);
        newBT.getStyleClass().addAll("animated-button", "animated-form-button");
        newBT.setOnAction((e) -> newPatient());
        Tooltip nBTToolTip = new Tooltip();
        nBTToolTip.setText("Добавить");
        newBT.setTooltip(nBTToolTip);

        ImageView newIMG = new ImageView();
        newBT.setGraphic(newIMG);
        newIMG.setImage(new Image("/img/icons/plus-outline-hover.png"));
        newBT.setOnMouseEntered((e) -> newIMG.setImage(new Image("/img/icons/plus-outline-green.png")));
        newBT.setOnMouseExited((e) -> newIMG.setImage(new Image("/img/icons/plus-outline-hover.png")));

        buttonsList = new JFXNodesList();
        buttonsList.addAnimatedNode(actionBT);
        buttonsList.addAnimatedNode(showBT);
        buttonsList.addAnimatedNode(editBT);
        buttonsList.addAnimatedNode(deleteBT);
        buttonsList.addAnimatedNode(newBT);
        buttonsList.setSpacing(15);
        buttonsList.setRotate(90);

        AnchorPane.setRightAnchor(buttonsList, 15.0);
        AnchorPane.setBottomAnchor(buttonsList, 15.0);
        patientAP.getChildren().add(buttonsList);

        showBT.setDisable(true);
        editBT.setDisable(true);
        deleteBT.setDisable(true);
        newBT.setDisable(false);
    }

    private void updateButtons(boolean isItemSelected)
    {
        if(isItemSelected) {
            showBT.setDisable(false);
            editBT.setDisable(false);
            deleteBT.setDisable(false);
            newBT.setDisable(false);
        } else {
            showBT.setDisable(true);
            editBT.setDisable(true);
            deleteBT.setDisable(true);
            newBT.setDisable(false);
        }
    }

    private void showPatient(Patient patient)
    {
        ShowPatientController.setPatient(patient);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/patient/show/showPatient.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showDialog(root, "Профиль пациента");
    }

    private void newPatient()
    {
        FormController.setWindow(false);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/patient/form/form.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showDialog(root, "Новый пациент");
    }

    private void editPatient(Patient patient)
    {
        FormController.setPatient(patient);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UI/patient/form/form.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showDialog(root, "Редактирование пациента");
    }

    private void showDialog(Parent root, String title)
    {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setHeight(650);
        stage.setTitle(title);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(patientAP.getScene().getWindow());
        stage.setOnCloseRequest((e) -> UpdateList());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        patientAP.getScene().getRoot().setEffect(effectPressed);
        stage.showAndWait();
        patientAP.getScene().getRoot().setEffect(null);

        UpdateList();
    }

    private void Dialog(String header, String text) {
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
        stage.initOwner(patientAP.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        patientAP.getScene().getRoot().setEffect(effectPressed);
        stage.showAndWait();
        patientAP.getScene().getRoot().setEffect(null);
    }
}

