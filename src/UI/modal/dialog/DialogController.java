package UI.modal.dialog;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by astures on 25.02.17.
 */
public class DialogController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private HBox titleHBox;

    @FXML
    private Label titleLB;

    @FXML
    private Label contentLB;

    @FXML
    private JFXButton okBT;

    private static String title;
    private static String content;
    private static boolean error = true;

    public static void setTitle(String value)
    {
        title = value;
    }

    public static void setContent(String value)
    {
        content = value;
    }

    public static void setError(boolean value)
    {
        error = value;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if(error) {
            titleHBox.setStyle("-fx-background-color: #D50000;");
        } else {
            titleHBox.setStyle("-fx-background-color: #2196F3;");
        }
        titleLB.setText(title);
        contentLB.setText(content);
        rootPane.setPrefWidth(contentLB.getPrefWidth());
    }

    @FXML
    void okBTonAction(ActionEvent event) {
        Stage stage = (Stage)contentLB.getScene().getWindow();
        stage.close();
    }


}
