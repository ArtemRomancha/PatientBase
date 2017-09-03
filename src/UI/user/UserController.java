package UI.user;

import Logic.core.User;
import Logic.utils.connection;
import UI.modal.yesNo.yesNoController;
import UI.user.form.FormController;
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

/**
 * Created by astures on 24.02.17.
 */

public class UserController implements Initializable {
    @FXML
    private AnchorPane userAP;

    @FXML
    private JFXTreeTableView<User> userTV;

    @FXML
    private JFXTextField filterTB;

    private JFXButton actionBT;
    private JFXButton editBT;
    private JFXButton deleteBT;
    private JFXButton newBT;
    private JFXNodesList buttonsList;
    private TreeItem<User> root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), userAP);
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
                userTV.setPredicate(new Predicate<TreeItem<User>>() {
                    @Override
                    public boolean test(TreeItem<User> user) {
                        return user.toString().toLowerCase().contains(newValue.toLowerCase());
                    }
                });
            }
        });

        addButtons();

        JFXTreeTableColumn<User, String> login = new JFXTreeTableColumn<>("Логин");
        login.setCellValueFactory(cellData -> cellData.getValue().getValue().getLogin());

        JFXTreeTableColumn<User, String> fio = new JFXTreeTableColumn<>("ФИО");
        fio.setCellValueFactory(cellData -> cellData.getValue().getValue().getFio());

        JFXTreeTableColumn<User, Boolean> access = new JFXTreeTableColumn<>("Доступ администратора");
        access.setCellValueFactory(cellData -> cellData.getValue().getValue().getAdminAccess());

        root = new RecursiveTreeItem<>(getAllUser(), RecursiveTreeObject::getChildren);
        userTV.getColumns().setAll(login, fio, access);

        userTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateButtons(newValue != null);
                });

        userTV.setOnMouseClicked((e) -> {
            if(userTV.getSelectionModel().getSelectedIndex() != -1) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (e.getClickCount() == 2) {
                        editBT.fire();
                    }
                }
            }
        });

        userTV.setRoot(root);
        userTV.setShowRoot(false);
    }

    private ObservableList<User> getAllUser() {
        ObservableList<User> users = FXCollections.observableArrayList();

        ArrayList<HashMap<String, String>> usr = connection.SelectMany(User.getTableName(), User.getColumns(), "WHERE super_admin_access != 1");
        for (HashMap<String, String> row : usr) {
            users.add(new User(row));
        }
        return users;
    }

    public void UpdateList() {
        root = new RecursiveTreeItem<>(getAllUser(), RecursiveTreeObject::getChildren);
        userTV.setRoot(root);
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
        actionBT.setOnMouseEntered((e) -> actionIMG.setImage(new Image("/img/icons/animation.png")));
        actionBT.setOnMouseExited((e) -> actionIMG.setImage(new Image("/img/icons/animation-hover.png")));

        editBT = new JFXButton("");
        editBT.setButtonType(JFXButton.ButtonType.RAISED);
        editBT.getStyleClass().addAll("animated-button", "animated-edit-button");
        editBT.setOnAction((e) -> {
            int selectedIndex = userTV.getSelectionModel().getSelectedIndex();
            editUser(userTV.getTreeItem(selectedIndex).getValue());
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
            int selectedIndex = userTV.getSelectionModel().getSelectedIndex();
            Dialog("Подтверждение", "Вы уверены, что хотите\nудалить пользователя?");
            if(yesNoController.getResult()) {
                userTV.getTreeItem(selectedIndex).getValue().Delete();
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
        newBT.setOnAction((e) -> newUser());
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
        buttonsList.addAnimatedNode(editBT);
        buttonsList.addAnimatedNode(deleteBT);
        buttonsList.addAnimatedNode(newBT);
        buttonsList.setSpacing(15);
        buttonsList.setRotate(90);

        AnchorPane.setRightAnchor(buttonsList, 15.0);
        AnchorPane.setBottomAnchor(buttonsList, 15.0);
        userAP.getChildren().add(buttonsList);

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

    private void newUser() {
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

    private void editUser(User user) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("form/form.fxml"));
        FormController controller = loader.getController();
        controller.setUser(user);
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
        stage.initOwner(userAP.getScene().getWindow());

        Parent rootParent = userAP.getScene().getRoot();
        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        rootParent.setEffect(effectPressed);

        stage.showAndWait();

        rootParent.setEffect(null);

        UpdateList();
    }

    private void Dialog(String header, String text) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("../modal/yesNo/yesNo.fxml"));
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
        stage.initOwner(userAP.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        userAP.getScene().getRoot().setEffect(effectPressed);

        stage.showAndWait();

        userAP.getScene().getRoot().setEffect(null);
    }
}
