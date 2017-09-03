package UI.log;

import Logic.core.Log;
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

public class LogController implements Initializable {
    @FXML
    private AnchorPane logAP;

    @FXML
    private JFXTreeTableView<Log> logTV;

    @FXML
    private JFXTextField filterTB;
    private TreeItem<Log> root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), logAP);
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
                logTV.setPredicate(new Predicate<TreeItem<Log>>() {
                    @Override
                    public boolean test(TreeItem<Log> logNote) {
                        return logNote.toString().toLowerCase().contains(newValue.toLowerCase());
                    }
                });
            }
        });

        JFXTreeTableColumn<Log, String> user = new JFXTreeTableColumn<>("Пользователь");
        user.setCellValueFactory(cellData -> cellData.getValue().getValue().getUser());

        JFXTreeTableColumn<Log, String> date = new JFXTreeTableColumn<>("Дата");
        date.setCellValueFactory(cellData -> cellData.getValue().getValue().getDate());

        JFXTreeTableColumn<Log, String> event = new JFXTreeTableColumn<>("Событие");
        event.setCellValueFactory(cellData -> cellData.getValue().getValue().getEvent());

        root = new RecursiveTreeItem<>(getAllLogNotes(), RecursiveTreeObject::getChildren);
        logTV.getColumns().setAll(user, date, event);

        logTV.setRoot(root);
        logTV.setShowRoot(false);
    }

    private ObservableList<Log> getAllLogNotes() {
        ObservableList<Log> logs = FXCollections.observableArrayList();

        ArrayList<HashMap<String, String>> log = connection.SelectMany(Log.getTableName(), Log.getColumns(), "");
        for (HashMap<String, String> row : log) {
            logs.add(new Log(row));
        }
        return logs;
    }
}
