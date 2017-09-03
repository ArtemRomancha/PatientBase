package UI.patient.actions;

import UI.patient.form.FormController;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by AstureS on 01.03.2017.
 */

public class ActionsController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton addBT;

    @FXML
    private ImageView addIMG;

    @FXML
    private JFXButton findBT;

    @FXML
    private ImageView findIMG;

    @FXML
    private JFXButton listBT;

    @FXML
    private ImageView listIMG;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        addIMG.setImage(new Image("/img/icons/account-plus.png"));
        addBT.setOnMouseEntered((e) -> addIMG.setImage(new Image("/img/icons/account-plus-hover.png")));
        addBT.setOnMouseExited((e) -> addIMG.setImage(new Image("/img/icons/account-plus.png")));

        findIMG.setImage(new Image("/img/icons/account-search.png"));
        findBT.setOnMouseEntered((e) -> findIMG.setImage(new Image("/img/icons/account-search-hover.png")));
        findBT.setOnMouseExited((e) -> findIMG.setImage(new Image("/img/icons/account-search.png")));

        listIMG.setImage(new Image("/img/icons/script.png"));
        listBT.setOnMouseEntered((e) -> listIMG.setImage(new Image("/img/icons/script-hover.png")));
        listBT.setOnMouseExited((e) -> listIMG.setImage(new Image("/img/icons/script.png")));
    }

    @FXML
    void addBTonAction(ActionEvent event)
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

    @FXML
    void findBTonAction(ActionEvent event)
    {

    }

    @FXML
    void listBTonAction(ActionEvent event)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UI/patient/table/patientTable.fxml"));
            rootPane.getChildren().setAll(root);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, -75.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog(Parent root, String title)
    {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());

        ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        rootPane.getScene().getRoot().setEffect(effectPressed);
        stage.showAndWait();
        rootPane.getScene().getRoot().setEffect(null);
    }
}
