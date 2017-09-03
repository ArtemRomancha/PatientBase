package UI.nav;

import Logic.controllers.GeneralController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by astures on 22.02.17.
 */


public class NavController implements Initializable
{
    @FXML
    private VBox rootBox;

    @FXML
    private JFXButton patientBT;

    @FXML
    private ImageView patientIMG;

    @FXML
    private JFXButton surgeonBT;

    @FXML
    private ImageView surgeonIMG;

    @FXML
    private JFXButton anaestBT;

    @FXML
    private ImageView anaestIMG;

    @FXML
    private JFXButton doctorBT;

    @FXML
    private ImageView doctorIMG;

    @FXML
    private JFXButton diagnosisBT;

    @FXML
    private ImageView diagnosisIMG;

    @FXML
    private JFXButton usersBT;

    @FXML
    private ImageView usersIMG;

    @FXML
    private JFXButton profileBT;

    @FXML
    private ImageView profileIMG;

    @FXML
    private JFXButton logsBT;

    @FXML
    private ImageView logsIMG;

    @FXML
    private JFXButton closeBT;

    @FXML
    private ImageView closeIMG;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        patientIMG.setImage(new Image("/img/icons/account-card-details.png"));
        patientBT.setOnMouseEntered((e) -> patientIMG.setImage(new Image("/img/icons/account-card-details-hover.png")));
        patientBT.setOnMouseExited((e) -> patientIMG.setImage(new Image("/img/icons/account-card-details.png")));

        if(GeneralController.getCurrentUser().getAdminAccess().getValue() || GeneralController.getCurrentUser().getSuperAdminAccess()) {
            surgeonIMG.setImage(new Image("/img/icons/medical-bag.png"));
            surgeonBT.setOnMouseEntered((e) -> surgeonIMG.setImage(new Image("/img/icons/medical-bag-hover.png")));
            surgeonBT.setOnMouseExited((e) -> surgeonIMG.setImage(new Image("/img/icons/medical-bag.png")));

            anaestIMG.setImage(new Image("/img/icons/pharmacy.png"));
            anaestBT.setOnMouseEntered((e) -> anaestIMG.setImage(new Image("/img/icons/pharmacy-hover.png")));
            anaestBT.setOnMouseExited((e) -> anaestIMG.setImage(new Image("/img/icons/pharmacy.png")));

            doctorIMG.setImage(new Image("/img/icons/hospital.png"));
            doctorBT.setOnMouseEntered((e) -> doctorIMG.setImage(new Image("/img/icons/hospital-hover.png")));
            doctorBT.setOnMouseExited((e) -> doctorIMG.setImage(new Image("/img/icons/hospital.png")));

            diagnosisIMG.setImage(new Image("/img/icons/biohazard.png"));
            diagnosisBT.setOnMouseEntered((e) -> diagnosisIMG.setImage(new Image("/img/icons/biohazard-hover.png")));
            diagnosisBT.setOnMouseExited((e) -> diagnosisIMG.setImage(new Image("/img/icons/biohazard.png")));
        }

        profileIMG.setImage(new Image("/img/icons/account-circle.png"));
        profileBT.setOnMouseEntered((e) -> profileIMG.setImage(new Image("/img/icons/account-circle.png")));
        profileBT.setOnMouseExited((e) -> profileIMG.setImage(new Image("/img/icons/account-circle.png")));

        if(GeneralController.getCurrentUser().getSuperAdminAccess()) {
            usersIMG.setImage(new Image("/img/icons/account-multiple.png"));
            usersBT.setOnMouseEntered((e) -> usersIMG.setImage(new Image("/img/icons/account-multiple-hover.png")));
            usersBT.setOnMouseExited((e) -> usersIMG.setImage(new Image("/img/icons/account-multiple.png")));

            logsIMG.setImage(new Image("/img/icons/history.png"));
            logsBT.setOnMouseEntered((e) -> logsIMG.setImage(new Image("/img/icons/history-hover.png")));
            logsBT.setOnMouseExited((e) -> logsIMG.setImage(new Image("/img/icons/history.png")));
        }

        closeIMG.setImage(new Image("/img/icons/exit-to-app.png"));
        closeBT.setOnMouseEntered((e) -> closeIMG.setImage(new Image("/img/icons/exit-to-app-hover.png")));
        closeBT.setOnMouseExited((e) -> closeIMG.setImage(new Image("/img/icons/exit-to-app.png")));
    }
}
