package UI.medicalRecord.attachment.show;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 * Created by aroma on 09.03.2017.
 */
public class ShowAttachmentController implements Initializable
{
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView imageView;

    private static Image image;
    private DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

    public static void setImage(Image value)
    {
        image = value;
    }

    public void initialize(URL url, ResourceBundle rb)
    {
        imageView.setFitHeight(image.getHeight());
        imageView.setFitWidth(image.getWidth());
        scrollPane.setPrefWidth(image.getWidth() + 5);
        scrollPane.setPrefHeight(image.getHeight() + 5);

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });

        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });

        imageView.setImage(image);
        imageView.preserveRatioProperty().set(true);
    }
}
