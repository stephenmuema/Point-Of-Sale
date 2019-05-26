package Controllers;

import Controllers.AuthenticationControllers.AnimationGenerator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SleepingController implements Initializable {

    AnimationGenerator animationGenerator = null;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private
    AnchorPane sleeping;
    @FXML
    private Button resume;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            assert false;
            Parent fxml;
            if (config.login.containsKey("logged in as admin")) {
                fxml = FXMLLoader.load(getClass().getResource("resourcefiles/panelAdmin.fxml"));
            } else {
                fxml = FXMLLoader.load(getClass().getResource("resourcefiles/panel.fxml"));
            }
            resume.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    sleeping.getChildren().removeAll();
                    sleeping.getChildren().setAll(fxml);
                }
            });
//                }
//            });
        } catch (IOException ex) {
            Logger.getLogger(SleepingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public AnimationGenerator getAnimationGenerator() {
        return animationGenerator;
    }

    public void setAnimationGenerator(AnimationGenerator animationGenerator) {
        this.animationGenerator = animationGenerator;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public AnchorPane getSleeping() {
        return sleeping;
    }

    public void setSleeping(AnchorPane sleeping) {
        this.sleeping = sleeping;
    }

    public Button getResume() {
        return resume;
    }

    public void setResume(Button resume) {
        this.resume = resume;
    }
}
