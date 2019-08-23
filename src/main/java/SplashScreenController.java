import Controllers.AuthenticationControllers.AnimationGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static securityandtime.config.license;

public class SplashScreenController implements Initializable {

    public Label owner;
    public Label expiry;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Date date = new Date();


            if (Objects.equals(license.get("name"), "Trial license")) {
                date.setTime(Long.parseLong(license.get("time")) + System.currentTimeMillis());

                owner.setText(license.get("name") + " " + ".Expires on " + new SimpleDateFormat("yyyy/MM/dd").format(date));
            } else {
                date.setTime(Long.parseLong(license.get("time")) + System.currentTimeMillis());

                String s2 = new SimpleDateFormat("yyyy/MM/dd").format(date);

                owner.setText("Registered to " + license.get("name"));
                expiry.setText(s2);
            }
//            System.out.println(license.get("time"));


//
//todo  set text later
//            go to login page
            Parent fxml = FXMLLoader.load(getClass().getResource("AuthenticationFiles/Login.fxml"));
            makeStageDrageable();
            AnimationGenerator animationGenerator = new AnimationGenerator();
//            animationGenerator.applyFadeAnimationOn01(parent, 4000, 1, 0.7, 1, new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent e) {
            animationGenerator.applyFadeAnimationOn02(parent, 4000, 1, 1, 1, e2 -> {
                parent.getChildren().removeAll();
                parent.getChildren().setAll(fxml);
            });
//                }
//            });
        } catch (IOException ex) {
            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeStageDrageable() {
        parent.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        parent.setOnMouseDragged(event -> {
            Launcher.stage.setX(event.getScreenX() - xOffset);
            Launcher.stage.setY(event.getScreenY() - yOffset);
            Launcher.stage.setOpacity(0.5f);
        });
        parent.setOnDragDone((e) -> Launcher.stage.setOpacity(1.0f));
        parent.setOnMouseReleased((e) -> Launcher.stage.setOpacity(1.0f));
    }

}
