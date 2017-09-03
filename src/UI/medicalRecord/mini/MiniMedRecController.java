package UI.medicalRecord.mini;

import Logic.core.MedicalRecord;
import UI.medicalRecord.show.ShowMedRecController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseButton;
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
public class MiniMedRecController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label dateTB;

    @FXML
    private Label diagnosisLB;

    @FXML
    private VBox actionVB;

    private static JFXButton deleteBT;
    private static JFXButton editBT;
    private static MedicalRecord medicalRecord;
    private MedicalRecord record;

    public static void setDeleteBT(JFXButton button)
    {
        deleteBT = button;
    }

    public static void setEditBT(JFXButton button)
    {
        editBT = button;
    }

    public static void setMedRec(MedicalRecord medRec)
    {
        medicalRecord = medRec;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        record = medicalRecord;
        diagnosisLB.setText(medicalRecord.getDiagnosis().getDescription().getValue());
        dateTB.setText(medicalRecord.getDateOfCreate());
        actionVB.getChildren().add(editBT);
        actionVB.getChildren().add(deleteBT);

        rootPane.setOnMouseEntered((e) -> {
            diagnosisLB.setStyle(" -fx-text-fill: #FFFFFF;");
            dateTB.setStyle("-fx-text-fill: #FFFFFF;");
        });

        rootPane.setOnMouseExited((e) -> {
            diagnosisLB.setStyle("-fx-text-fill: #2196F3;");
            dateTB.setStyle(" -fx-text-fill: #2196F3;");
        });

        rootPane.setOnMouseClicked((e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    ShowMedRecController.setMedRec(record);
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/UI/medicalRecord/show/showMedRec.fxml"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
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
        });
    }
}
