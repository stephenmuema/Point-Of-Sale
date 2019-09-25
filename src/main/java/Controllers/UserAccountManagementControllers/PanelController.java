package Controllers.UserAccountManagementControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.site;

public class PanelController extends UtilityClass implements Initializable {
    @FXML
    private MenuItem logout;
    @FXML
    private Button carwash;
    @FXML
    private Button shop;
    //    public Button chat; todo v1.4
    @FXML
    private Button accountb;
    @FXML
    private Hyperlink link;
    @FXML
    private Button refresh;
    @FXML
    private Button shiftStart;
    @FXML
    private Button restartApps;
    @FXML
    private Button troubleshoot;
    @FXML
    private Button endShift;
    @FXML
    private Button help;
    @FXML
    private Button reportIssue;
    @FXML
    private Button chatBox;
    @FXML
    private Button callAdmin;
    @FXML
    private Label stationname;
    @FXML
    private MenuItem endShiftMenu;
    @FXML
    private MenuItem startShiftMenu;
    @FXML
    private MenuItem quit;
    @FXML
    private MenuItem details;
    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane panel;
    @FXML

    private Label clock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        buttonHandlers();
        menuhandlers();
        config.panel.put("panel", panel);
        stationname.setText(getComputerName());
        time(clock);
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
                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(panel, Event.ANY);


    }


    private void buttonHandlers() {
        refresh.setOnAction(event -> {
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        accountb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("UserAccountManagementFiles/Settings.fxml"));
                try {
                    Parent parent = fxmlLoader.load();
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
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwashSales.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        shop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/shop.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        logoutButton.setOnAction(event -> {
            config.login.put("loggedout", true);
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


    private void menuhandlers() {
        logout.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
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

    public AnchorPane getPanel() {
        return panel;
    }

    public void setPanel(AnchorPane panel) {
        this.panel = panel;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }
}
