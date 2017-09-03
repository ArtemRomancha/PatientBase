package UI.medicalRecord.show;

import Logic.core.Attachment;
import Logic.core.MedicalRecord;
import UI.medicalRecord.attachment.AttachmentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by AstureS on 06.03.2017.
 */
public class ShowMedRecController implements Initializable
{
    @FXML
    private Label titleLB;

    @FXML
    private JFXTextField surgeonTB;

    @FXML
    private JFXTextField anaesthesiologistTB;

    @FXML
    private JFXTextField doctorTB;

    @FXML
    private JFXTextField diagnosisTB;

    @FXML
    private JFXTextArea noticeTA;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentPane;

    @FXML
    private JFXButton closeBT;

    private static MedicalRecord medicalRecord;

    public static void setMedRec(MedicalRecord showMedRec)
    {
        medicalRecord = showMedRec;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        titleLB.setText(titleLB.getText() + " " + medicalRecord.getDateOfCreate());

        surgeonTB.setText(medicalRecord.getSurgeon().getFio());
        anaesthesiologistTB.setText(medicalRecord.getAnaesthesiologist().getFio());
        doctorTB.setText(medicalRecord.getDoctor().getFio());
        diagnosisTB.setText(medicalRecord.getDiagnosis().toString());
        noticeTA.setText(medicalRecord.getNotice());

        medicalRecord.FillAttachments();
        Update();
    }

    @FXML
    void closeBTOnAction(ActionEvent event)
    {
        ((Stage)contentPane.getScene().getWindow()).close();
    }

    private void DrawAttachedFile(Attachment file)
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/UI/medicalRecord/attachment/attachment.fxml"));
        AttachmentController.setDeleteBT(null);
        AttachmentController.setFile(new File(file.getFileName()));

        try {
            contentPane.getChildren().add(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Update() {
        contentPane.getChildren().clear();
        for (Attachment attachment : medicalRecord.getAttachments()) {
            DrawAttachedFile(attachment);
        }
    }
}
