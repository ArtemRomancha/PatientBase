package UI.medico;

/**
 * Created by astures on 22.02.17.
 */
import Logic.core.*;
import Logic.utils.connection;
import UI.medico.form.FormController;
import UI.modal.dialog.DialogController;
import UI.modal.yesNo.yesNoController;
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

public class MedicoController implements Initializable {

    public static String medico;

    @FXML
    private AnchorPane medicoAP;

    @FXML
    private JFXTreeTableView<Medico> medicoTV;

    @FXML
    private JFXTextField filterTB;

    private JFXButton actionBT;
    private JFXButton editBT;
    private JFXButton deleteBT;
    private JFXButton newBT;
    private JFXNodesList buttonsList;
    private TreeItem<Medico> root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), medicoAP);
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
                medicoTV.setPredicate(new Predicate<TreeItem<Medico>>() {
                    @Override
                    public boolean test(TreeItem<Medico> medico) {
                        return medico.getValue().getFio().toLowerCase().contains(newValue.toLowerCase());
                    }
                });
            }
        });

        addButtons();

        JFXTreeTableColumn<Medico, String> firstName = new JFXTreeTableColumn<>("Имя");
        firstName.setCellValueFactory(cellData -> cellData.getValue().getValue().getFirstName());

        JFXTreeTableColumn<Medico, String> middleName = new JFXTreeTableColumn<>("Отчество");
        middleName.setCellValueFactory(cellData -> cellData.getValue().getValue().getMiddleName());

        JFXTreeTableColumn<Medico, String> lastName = new JFXTreeTableColumn<>("Фамилия");
        lastName.setCellValueFactory(cellData -> cellData.getValue().getValue().getLastName());

        root = new RecursiveTreeItem<Medico>(getAllMedico(), RecursiveTreeObject::getChildren);
        medicoTV.getColumns().setAll(firstName, middleName, lastName);

        medicoTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateButtons(newValue != null);
                });

        medicoTV.setOnMouseClicked((e) -> {
            if(medicoTV.getSelectionModel().getSelectedIndex() != -1) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (e.getClickCount() == 2) {
                        editBT.fire();
                    }
                }
            }
        });

        medicoTV.setRoot(root);
        medicoTV.setShowRoot(false);
    }

    private ObservableList<Medico> getAllMedico()
    {
        ObservableList<Medico> medicos = FXCollections.observableArrayList();

        ArrayList<HashMap<String, String>> medics = new ArrayList<>();

        switch (medico){
            case("surgeon"):
                medics = connection.SelectMany(Surgeon.getTableName(), Surgeon.getColumns(), "");
                for (HashMap<String, String> row: medics) {
                    medicos.add(new Surgeon(row));
                }
                break;
            case("anaesthesiologist"):
                medics = connection.SelectMany(Anaesthesiologist.getTableName(), Anaesthesiologist.getColumns(), "");
                for (HashMap<String, String> row: medics) {
                    medicos.add(new Anaesthesiologist(row));
                }
                break;
            case("doctor"):
                medics = connection.SelectMany(Doctor.getTableName(), Doctor.getColumns(), "");
                for (HashMap<String, String> row: medics) {
                    medicos.add(new Doctor(row));
                }
                break;
        }

        return medicos;
    }

    public void UpdateList()
    {
        root = new RecursiveTreeItem<Medico>(getAllMedico(), RecursiveTreeObject::getChildren);
        medicoTV.setRoot(root);
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
        actionBT.setOnMouseEntered((e) -> {
            actionIMG.setImage(new Image("/img/icons/animation.png"));
        });
        actionBT.setOnMouseExited((e) -> {
            actionIMG.setImage(new Image("/img/icons/animation-hover.png"));
        });

        editBT = new JFXButton("");
        editBT.setButtonType(JFXButton.ButtonType.RAISED);
        editBT.getStyleClass().addAll("animated-button", "animated-edit-button");
        editBT.setOnAction((e) -> {
            int selectedIndex = medicoTV.getSelectionModel().getSelectedIndex();
            editMedico(medicoTV.getTreeItem(selectedIndex).getValue());
            UpdateList();
        });
        Tooltip eBTToolTip = new Tooltip();
        eBTToolTip.setText("Редактировать");
        editBT.setTooltip(eBTToolTip);

        ImageView editIMG = new ImageView();
        editBT.setGraphic(editIMG);
        editIMG.setImage(new Image("/img/icons/pencil-hover.png"));
        editBT.setOnMouseEntered((e) -> {
            editIMG.setImage(new Image("/img/icons/pencil-orange.png"));
        });
        editBT.setOnMouseExited((e) -> {
            editIMG.setImage(new Image("/img/icons/pencil-hover.png"));
        });

        deleteBT = new JFXButton("");
        deleteBT.setButtonType(JFXButton.ButtonType.RAISED);
        deleteBT.getStyleClass().addAll("animated-button", "animated-delete-button");
        deleteBT.setOnAction((e) -> {
            int selectedIndex = medicoTV.getSelectionModel().getSelectedIndex();
            showDialog("Подтверждение", "Вы уверены, что хотите\nудалить сотрудника?", "yesNo");
            if(yesNoController.getResult()) {
                if(medicoTV.getTreeItem(selectedIndex).getValue().CanDelete()) {
                    medicoTV.getTreeItem(selectedIndex).getValue().Delete();
                    UpdateList();
                } else {
                    showDialog("Ошибка", "НЕЛЬЗЯ удалить сотрудника,так как у него\n есть пациенты", "dialog");
                }
            }
        });

        Tooltip dBTToolTip = new Tooltip();
        dBTToolTip.setText("Удалить");
        deleteBT.setTooltip(dBTToolTip);

        ImageView deleteIMG = new ImageView();
        deleteBT.setGraphic(deleteIMG);
        deleteIMG.setImage(new Image("/img/icons/delete-hover.png"));
        deleteBT.setOnMouseEntered((e) -> {
            deleteIMG.setImage(new Image("/img/icons/delete-red.png"));
        });
        deleteBT.setOnMouseExited((e) -> {
            deleteIMG.setImage(new Image("/img/icons/delete-hover.png"));
        });

        newBT = new JFXButton("");
        newBT.setButtonType(JFXButton.ButtonType.RAISED);
        newBT.getStyleClass().addAll("animated-button", "animated-form-button");
        newBT.setOnAction((e) -> newMedico());
        Tooltip nBTToolTip = new Tooltip();
        nBTToolTip.setText("Добавить");
        newBT.setTooltip(nBTToolTip);

        ImageView newIMG = new ImageView();
        newBT.setGraphic(newIMG);
        newIMG.setImage(new Image("/img/icons/plus-outline-hover.png"));
        newBT.setOnMouseEntered((e) -> {
            newIMG.setImage(new Image("/img/icons/plus-outline-green.png"));
        });
        newBT.setOnMouseExited((e) -> {
            newIMG.setImage(new Image("/img/icons/plus-outline-hover.png"));
        });

        buttonsList = new JFXNodesList();
        buttonsList.addAnimatedNode(actionBT);
        buttonsList.addAnimatedNode(editBT);
        buttonsList.addAnimatedNode(deleteBT);
        buttonsList.addAnimatedNode(newBT);
        buttonsList.setSpacing(15);
        buttonsList.setRotate(90);

        AnchorPane.setRightAnchor(buttonsList, 15.0);
        AnchorPane.setBottomAnchor(buttonsList, 15.0);
        medicoAP.getChildren().add(buttonsList);

        editBT.setDisable(true);
        deleteBT.setDisable(true);
        newBT.setDisable(false);
    }

    private void updateButtons(boolean isItemSelected)
    {
        if(isItemSelected) {
            editBT.setDisable(false);
            deleteBT.setDisable(false);
            newBT.setDisable(false);
        } else {
            editBT.setDisable(true);
            deleteBT.setDisable(true);
            newBT.setDisable(false);
        }
    }

    private void newMedico()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("form/form.fxml"));
        FormController controller = loader.getController();
        controller.setTitle("Новый сотрудник");
        try {
            showDialog(loader.load(), "Новый сотрудник");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void editMedico(Medico medico)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("form/form.fxml"));
        FormController controller = loader.getController();
        controller.setMedico(medico);
        controller.setTitle("Редактирование");
        try {
            showDialog(loader.load(), "Редактирование");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog(Parent root, String title)
    {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        medicoTV.getScene().getRoot().setEffect(effectPressed);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(medicoAP.getScene().getWindow());
        stage.setOnCloseRequest((e) -> UpdateList());
        stage.showAndWait();
        medicoTV.getScene().getRoot().setEffect(null);
        UpdateList();
    }

    private void showDialog(String header, String text, String dialogType)
    {
        FXMLLoader loader = new FXMLLoader();
        if(dialogType.equals("dialog")) {
            loader.setLocation(getClass().getResource("../modal/dialog/dialog.fxml"));
            DialogController controller = loader.getController();
            controller.setTitle(header);
            controller.setContent(text);
        }
        if(dialogType.equals("yesNo")) {
            loader.setLocation(getClass().getResource("../modal/yesNo/yesNo.fxml"));
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
        stage.initOwner(medicoTV.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        medicoTV.getScene().getRoot().setEffect(effectPressed);

        stage.showAndWait();

        medicoTV.getScene().getRoot().setEffect(null);
    }
}

