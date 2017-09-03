package UI.diagnosis;

import Logic.core.*;
import Logic.utils.connection;
import UI.diagnosis.form.FormController;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Created by astures on 23.02.17.
 */
public class DiagnosisController implements Initializable {
    @FXML
    private AnchorPane diagnosisAP;

    @FXML
    private JFXTreeTableView<Mkb> diagnosisTV;

    @FXML
    private JFXTextField filterTB;

    private JFXButton actionBT;
    private JFXButton editBT;
    private JFXButton deleteBT;
    private JFXButton newBT;
    private JFXNodesList buttonsList;
    private TreeItem<Mkb> root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), diagnosisAP);
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
                diagnosisTV.setPredicate(new Predicate<TreeItem<Mkb>>() {
                    @Override
                    public boolean test(TreeItem<Mkb> diagnosis) {
                        return diagnosis.getValue().toString().toLowerCase().contains(newValue.toLowerCase());
                    }
                });
            }
        });

        addButtons();

        JFXTreeTableColumn<Mkb, String> code = new JFXTreeTableColumn<>("Код МКБ 10");
        code.setCellValueFactory(cellData -> cellData.getValue().getValue().getMkbCode());

        JFXTreeTableColumn<Mkb, String> des = new JFXTreeTableColumn<>("Описание");
        des.setCellValueFactory(cellData -> cellData.getValue().getValue().getDescription());

        root = new RecursiveTreeItem<>(getAllDiagnosis(), RecursiveTreeObject::getChildren);
        diagnosisTV.getColumns().setAll(code, des);

        diagnosisTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateButtons(newValue != null)
        );

        diagnosisTV.setOnMouseClicked((e) -> {
            if(diagnosisTV.getSelectionModel().getSelectedIndex() != -1) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (e.getClickCount() == 2) {
                        editBT.fire();
                    }
                }
            }
        });

        diagnosisTV.setRoot(root);
        diagnosisTV.setShowRoot(false);
    }

    private ObservableList<Mkb> getAllDiagnosis() {
        ObservableList<Mkb> mkbs = FXCollections.observableArrayList();

        ArrayList<HashMap<String, String>> mkb = connection.SelectMany(Mkb.getTableName(), Mkb.getColumns(), "");
        for (HashMap<String, String> row : mkb) {
            mkbs.add(new Mkb(row));
        }

        return mkbs;
    }

    public void UpdateList() {
        root = new RecursiveTreeItem<>(getAllDiagnosis(), RecursiveTreeObject::getChildren);
        diagnosisTV.setRoot(root);
    }

    private void addButtons() {
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
            int selectedIndex = diagnosisTV.getSelectionModel().getSelectedIndex();
            editDiagnosis(diagnosisTV.getTreeItem(selectedIndex).getValue());
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
            int selectedIndex = diagnosisTV.getSelectionModel().getSelectedIndex();
            Dialog("Подтверждение", "Вы уверены, что хотите\nудалить диагноз?");
            if(yesNoController.getResult()) {
                diagnosisTV.getTreeItem(selectedIndex).getValue().Delete();
                UpdateList();
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
        newBT.setOnAction((e) -> newDiagnosis());
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
        diagnosisAP.getChildren().add(buttonsList);

        editBT.setDisable(true);
        deleteBT.setDisable(true);
        newBT.setDisable(false);
    }

    private void updateButtons(boolean isItemSelected) {
        if (isItemSelected) {
            editBT.setDisable(false);
            deleteBT.setDisable(false);
            newBT.setDisable(false);
        } else {
            editBT.setDisable(true);
            deleteBT.setDisable(true);
            newBT.setDisable(false);
        }
    }

    private void newDiagnosis() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("form/form.fxml"));
        FormController controller = loader.getController();
        controller.setTitle("Новый диагноз");
        try {
            showDialog(loader.load(), "Новый диагноз");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void editDiagnosis(Mkb mkb) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("form/form.fxml"));
        FormController controller = loader.getController();
        controller.setMkb(mkb);
        controller.setTitle("Редактирование");
        try {
            showDialog(loader.load(), "Редактирование");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog(Parent root, String title) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        diagnosisAP.getScene().getRoot().setEffect(effectPressed);

        stage.initOwner(diagnosisAP.getScene().getWindow());
        stage.setOnCloseRequest((e) -> UpdateList());
        stage.showAndWait();
        diagnosisAP.getScene().getRoot().setEffect(null);

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
        stage.initOwner(diagnosisTV.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        diagnosisTV.getScene().getRoot().setEffect(effectPressed);

        stage.showAndWait();

        diagnosisTV.getScene().getRoot().setEffect(null);
    }
}



