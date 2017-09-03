package UI.medicalRecord.attachment;

import UI.medicalRecord.attachment.show.ShowAttachmentController;
import UI.medicalRecord.show.ShowMedRecController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Created by aroma on 05.03.2017.
 */

public class AttachmentController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView filePreview;

    @FXML
    private Label fileNameTB;

    @FXML
    private Label dateTB;

    @FXML
    private VBox deleteVB;

    private static JFXButton deleteBT;
    private static File file;

    public static void setDeleteBT(JFXButton button)
    {
        deleteBT = button;
    }

    public static void setFile(File attachedFile)
    {
        file = attachedFile;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        filePreview.setImage(new Image("file:///" + file.getAbsolutePath()));
        fileNameTB.setText(file.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateTB.setText(sdf.format(file.lastModified()));
        if(deleteBT != null) {
            deleteVB.getChildren().add(deleteBT);
        }

        rootPane.setOnMouseEntered((e) -> {
            filePreview.setStyle("-fx-border-color: #FFFFFF;");
            filePreview.setStyle("-fx-border-width: 1;");
            fileNameTB.setStyle("-fx-text-fill: #FFFFFF;");
            dateTB.setStyle("-fx-text-fill: #FFFFFF;");
        });

        rootPane.setOnMouseExited((e) -> {
            filePreview.setStyle("-fx-border-width: 0;");
            fileNameTB.setStyle("-fx-text-fill: #2196F3;");
            dateTB.setStyle("-fx-text-fill: #2196F3;");
        });

        rootPane.setOnMouseClicked((e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    Image img = new Image("file:///" + file.getAbsolutePath());
                    ShowAttachmentController.setImage(img);
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("show/showAttachment.fxml"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    //stage.initStyle(StageStyle.UNDECORATED);
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
