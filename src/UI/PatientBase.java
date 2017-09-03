package UI;

/**
 * Created by AstureS on 20.03.2017.
 */
import Logic.controllers.TablesController;
import Logic.core.User;
import Logic.utils.connection;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.File;

public class PatientBase extends Application
{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        File db = new File("Patients.db");
        if(!db.exists()) {
            createTables();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/UI/login/login.fxml"));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(800);
        primaryStage.getIcons().add(new Image("/img/icon.ico"));

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        primaryStage.setTitle("Вход в систему");
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        fadeIn.play();
    }

    public static void createTables()
    {
        connection.Connect();
        TablesController.CreateAnaesthesiologistTable();
        TablesController.CreateAttachmentTable();
        TablesController.CreateDoctorTable();
        TablesController.CreateMedicRecordTable();
        TablesController.CreateMKBTable();
        TablesController.CreatePatientTable();
        TablesController.CreateSurgeonTable();
        TablesController.CreateAdressTable();
        TablesController.CreateUserTable();
        TablesController.CreateLogTable();
        connection.CloseDB();

        new User("superAdmin", "Super Admin", "superAdmin", true, true);
    }

    public static void clearTables()
    {
        TablesController.ClearAnaesthesiologistTable();
        TablesController.ClearAttachmentTable();
        TablesController.ClearDoctorTable();
        TablesController.ClearMedicRecordTable();
        TablesController.ClearMKBTable();
        TablesController.ClearPatientTable();
        TablesController.ClearSurgeonTable();
        TablesController.ClearAdressTable();
    }
}
