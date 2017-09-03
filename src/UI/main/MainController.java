package UI.main;


import Logic.controllers.GeneralController;
import UI.medico.MedicoController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by astures on 21.02.17.
 */
public class MainController implements Initializable
{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    private HamburgerBackArrowBasicTransition transition;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            VBox nav;
            if (GeneralController.getCurrentUser().getSuperAdminAccess()) {
                nav = FXMLLoader.load(getClass().getResource("/UI/nav/navMenuSuperAdmin.fxml"));
            } else if (GeneralController.getCurrentUser().getAdminAccess().getValue()) {
                nav = FXMLLoader.load(getClass().getResource("/UI/nav/navMenuAdmin.fxml"));
            } else {
                nav = FXMLLoader.load(getClass().getResource("/UI/nav/navMenu.fxml"));
            }
            AnchorPane drawerAP = new AnchorPane();
            drawerAP.getChildren().setAll(nav);
            drawer.setSidePane(drawerAP);

            for (Node node : nav.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.setOnMouseClicked((e) -> {
                        switch (node.getAccessibleText()) {
                            case "patient":
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/patient/actions/actions.fxml"));
                                    contentPane.getChildren().setAll(root);

                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 70.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "surgeon":
                                try {
                                    MedicoController.medico = "surgeon";
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/medico/medico.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "anaesthesiologist":
                                try {
                                    MedicoController.medico = "anaesthesiologist";
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/medico/medico.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "doctor":
                                try {
                                    MedicoController.medico = "doctor";
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/medico/medico.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "mkb10":
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/diagnosis/diagnosis.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "user":
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/user/user.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "profile":
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/profile/profile.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 120.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "logs":
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("/UI/log/log.fxml"));
                                    contentPane.getChildren().setAll(root);
                                    AnchorPane.setRightAnchor(root, 5.0);
                                    AnchorPane.setTopAnchor(root, 5.0);
                                    AnchorPane.setBottomAnchor(root, 5.0);
                                    AnchorPane.setLeftAnchor(root, 5.0);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "close":
                                Platform.exit();
                                break;
                        }
                        drawerOpenClose();
                    });
                }
            }
            transition = new HamburgerBackArrowBasicTransition(hamburger);
            transition.setRate(1);
            hamburger.setOnMouseClicked((e) -> drawerOpenClose());
        } catch (IOException e) {
            System.out.println(e);
        }

        rootPane.setOnMouseClicked((e) -> {
            if (drawer.isShown()) {
                if (!(e.getSceneX() < drawer.getWidth() && e.getSceneY() < drawer.getHeight())) {
                    drawerOpenClose();
                }
            }
        });
    }

    private void drawerOpenClose()
    {
        if(!drawer.isHidding() && !drawer.isShowing()) {
            if (drawer.isShown()) {
                transition.setRate(-1);
                drawer.close();
                drawer.setOnDrawerClosed((e) -> drawer.toBack());
            } else {
                transition.setRate(1);
                drawer.open();
                drawer.toFront();
            }
            transition.play();
        }
    }
}
