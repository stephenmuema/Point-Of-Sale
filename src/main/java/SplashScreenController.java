import Controllers.AuthenticationControllers.AnimationGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static securityandtime.config.*;

public class SplashScreenController implements Initializable {

    public Label owner;
    public Label expiry;
    public Label clientId;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientId.setText(license.get("clientId"));
        try {
            Date date = new Date();
            if (new File(fileSavePath + "\\images\\logo.png").exists()) {
                logo.setImage(companyLogoImageObj);
                System.out.println("logo set");
            }

            if (Objects.equals(license.get("name"), "Trial license")) {
                date.setTime(Long.parseLong(license.get("time")) + System.currentTimeMillis());

                date.setTime(Long.parseLong(license.get("time")) + System.currentTimeMillis());

                String s2 = new SimpleDateFormat("dd/MM/yyyy").format(date);
                String t = "Registered to " + license.get("name");
                owner.setText(t.toUpperCase());
                expiry.setText(s2.toUpperCase());
            } else {
                date.setTime(Long.parseLong(license.get("time")) + System.currentTimeMillis());

                String s2 = new SimpleDateFormat("dd/MM/yyyy").format(date);
                String t = "Registered to " + license.get("name");
                owner.setText(t.toUpperCase());
                expiry.setText(s2.toUpperCase());
            }

            Parent fxml = FXMLLoader.load(getClass().getResource("AuthenticationFiles/Login.fxml"));
            makeStageDrageable();
            AnimationGenerator animationGenerator = new AnimationGenerator();

            animationGenerator.applyFadeAnimationOn02(parent, 4000, 1, 1, 1, e2 -> {
                parent.getChildren().removeAll();
                parent.getChildren().setAll(fxml);
            });

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
            Main.stage.setX(event.getScreenX() - xOffset);
            Main.stage.setY(event.getScreenY() - yOffset);
            Main.stage.setOpacity(0.5f);
        });
        parent.setOnDragDone((e) -> Main.stage.setOpacity(1.0f));
        parent.setOnMouseReleased((e) -> Main.stage.setOpacity(1.0f));
    }

}
