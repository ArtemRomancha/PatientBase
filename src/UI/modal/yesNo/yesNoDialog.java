package UI.modal.yesNo;

import UI.modal.dialog.DialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by AstureS on 28.02.2017.
 */

public class yesNoDialog {
    /*
    FXMLLoader loader = new FXMLLoader();
        if(dialogType.equals("dialog"))

    {
        loader.setLocation(getClass().getResource("../../modal/dialog/dialog.fxml"));
        DialogController controller = loader.getController();
        controller.setTitle(header);
        controller.setContent(text);
    }
        if(dialogType.equals("yesNo"))

    {
        loader.setLocation(getClass().getResource("../../modal/yesNo/yesNo.fxml"));
        yesNoController controller = loader.getController();
        controller.setTitle(header);
        controller.setContent(text);
    }

    Stage stage = new Stage();

        try

    {
        stage.setScene(new Scene(loader.load()));
    } catch(
    IOException ex)

    {
        ex.printStackTrace();
    }

        stage.setTitle(header);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().

    getWindow());

    ColorAdjust effectPressed = new ColorAdjust();
        effectPressed.setBrightness(-0.5);
        rootPane.setEffect(effectPressed);

        stage.showAndWait();

        rootPane.setEffect(null);*/
}
