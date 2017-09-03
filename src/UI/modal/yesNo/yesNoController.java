package UI.modal.yesNo;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by astures on 25.02.17.
 */
public class yesNoController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label titleLB;

    @FXML
    private Label contentLB;

    @FXML
    private JFXButton noBT;

    @FXML
    private JFXButton yesBT;

    private static String title;
    private static String content;
    private static Boolean result = null;

    public static void setTitle(String value)
    {
        title = value;
    }

    public static void setContent(String value)
    {
        content = value;
    }

    public static Boolean getResult()
    {
        return result;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        titleLB.setText(title);
        contentLB.setText(content);
        rootPane.setPrefWidth(contentLB.getPrefWidth());
    }

    @FXML
    void noBTonAction(ActionEvent event) {
        result = false;
        close();
    }

    @FXML
    void yesBTonAction(ActionEvent event) {
        result = true;
        close();
    }

    private void close()
    {
        Stage stage = (Stage)contentLB.getScene().getWindow();
        stage.close();
    }
}
