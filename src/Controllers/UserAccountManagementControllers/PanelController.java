package Controllers.UserAccountManagementControllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.CheckConn;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.site;

public class PanelController implements Initializable {
    public MenuItem logout;
    public Button carwash;
    public Button shop;
    public ImageView logoimage;
    //    public Button chat; todo v1.4
    public Button accountb;
    public Hyperlink link;
    @FXML
    Button logoutButton;
    @FXML
    private VBox panel;
    @FXML

    private Label clock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        buttonHandlers();
        menuhandlers();
        time();
        getLogo();
        link.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(panel, Event.ANY);
    }

    private void getLogo() {
//        File file = new File("resourcefiles/images/banner_hardware.png");
        //            FileInputStream fileInputStream=new FileInputStream("resourcefiles/images/banner_hardware.png");
        Image image = new Image("resourcefiles/images/banner_hardware.png");
        logoimage.setImage(image);

    }

    private void buttonHandlers() {
//        chat.setOnMouseClicked(event -> {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("resourcefiles/ChatView.fxml"));
//            try {
//                Parent parent = (Parent) fxmlLoader.load();
//                Stage stage = new Stage();
//                stage.setScene(new Scene(parent));
//                stage.initStyle(StageStyle.UTILITY);
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
        accountb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/userDetails.fxml"));
                try {
                    Parent parent = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        carwash.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/carwashFiles/carwashSales.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        shop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/shop.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        logoutButton.setOnAction(event -> {
            config.login.put("loggedout", true);
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void time() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String mins = null, hrs = null, secs = null, pmam = null;
            try {
                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

                if (hours >= 12) {
//                    hrs= "0"+String.valueOf(hours-12);
                    pmam = "PM";
                } else {
                    pmam = "AM";

                }
                if (minutes > 9) {
                    mins = String.valueOf(minutes);
                } else {
                    mins = "0" + String.valueOf(minutes);

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + String.valueOf(seconds);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) + ":" + (secs) + " " + pmam);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void menuhandlers() {
        logout.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public MenuItem getLogout() {
        return logout;
    }

    public void setLogout(MenuItem logout) {
        this.logout = logout;
    }

    public Button getCarwash() {
        return carwash;
    }

    public void setCarwash(Button carwash) {
        this.carwash = carwash;
    }

    public Button getShop() {
        return shop;
    }

    public void setShop(Button shop) {
        this.shop = shop;
    }

    public ImageView getLogoimage() {
        return logoimage;
    }

    public void setLogoimage(ImageView logoimage) {
        this.logoimage = logoimage;
    }

//    public Button getChat() {
//        return chat;
//    }
//
//    public void setChat(Button chat) {
//        this.chat = chat;
//    }

    public Button getAccountb() {
        return accountb;
    }

    public void setAccountb(Button accountb) {
        this.accountb = accountb;
    }

    public Hyperlink getLink() {
        return link;
    }

    public void setLink(Hyperlink link) {
        this.link = link;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void setLogoutButton(Button logoutButton) {
        this.logoutButton = logoutButton;
    }

    public VBox getPanel() {
        return panel;
    }

    public void setPanel(VBox panel) {
        this.panel = panel;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }
}
