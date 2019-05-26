package Controllers.UserAccountManagementControllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Window;
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

public class AdminPanelController implements Initializable {
    public MenuItem menulogout;
    public Button addshop;
    public MenuItem details;
    public Menu branches;
    public MenuItem license;
    public MenuItem feedback;
    public Label clock;
    public Font x1;
    public Button employees;
    public Button stockspanel;
    public Button carwashpanel;
    public Button visitSuppliers;
    public Button backup;
    public Button audits;
    @FXML
    private VBox AdminPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuClick();
        buttonClick();
        time();


        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                () -> {
                    try {
                        config.login.put("loggedout", true);

                        AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(AdminPanel, Event.ANY);
    }


    public void menuClick() {
        menulogout.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    private void buttonClick() {
        audits.setOnMouseClicked(event -> {
            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/audits.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        backup.setOnMouseClicked(event -> {
            //todo back up all sql database code now to the licensing server db of the user account
        });
        visitSuppliers.setOnMouseClicked(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL("http://localhost/licensing/suppliers/").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        carwashpanel.setOnAction(event -> {
            AdminPanel.getChildren().removeAll();
            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/carwashFiles/carwash.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        stockspanel.setOnMousePressed(event -> {

            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        employees.setOnMousePressed(event -> {
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/employees.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            showAlert(Alert.AlertType.INFORMATION, AdminPanel.getScene().getWindow(), "coming soon", "Feature not yet supported");
        });
//        addshop.setOnMousePressed(event -> {
//            try {
//                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/addshop.fxml")))));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
    }

    private void time() {
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

    public MenuItem getMenulogout() {
        return menulogout;
    }

    public void setMenulogout(MenuItem menulogout) {
        this.menulogout = menulogout;
    }

    public Button getAddshop() {
        return addshop;
    }

    public void setAddshop(Button addshop) {
        this.addshop = addshop;
    }

    public MenuItem getDetails() {
        return details;
    }

    public void setDetails(MenuItem details) {
        this.details = details;
    }

    public Menu getBranches() {
        return branches;
    }

    public void setBranches(Menu branches) {
        this.branches = branches;
    }

    public MenuItem getLicense() {
        return license;
    }

    public void setLicense(MenuItem license) {
        this.license = license;
    }

    public MenuItem getFeedback() {
        return feedback;
    }

    public void setFeedback(MenuItem feedback) {
        this.feedback = feedback;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public Font getX1() {
        return x1;
    }

    public void setX1(Font x1) {
        this.x1 = x1;
    }

    public Button getEmployees() {
        return employees;
    }

    public void setEmployees(Button employees) {
        this.employees = employees;
    }

    public Button getStockspanel() {
        return stockspanel;
    }

    public void setStockspanel(Button stockspanel) {
        this.stockspanel = stockspanel;
    }

    public Button getCarwashpanel() {
        return carwashpanel;
    }

    public void setCarwashpanel(Button carwashpanel) {
        this.carwashpanel = carwashpanel;
    }

    public Button getVisitSuppliers() {
        return visitSuppliers;
    }

    public void setVisitSuppliers(Button visitSuppliers) {
        this.visitSuppliers = visitSuppliers;
    }

    public Button getBackup() {
        return backup;
    }

    public void setBackup(Button backup) {
        this.backup = backup;
    }

    public Button getAudits() {
        return audits;
    }

    public void setAudits(Button audits) {
        this.audits = audits;
    }

    public VBox getAdminPanel() {
        return AdminPanel;
    }

    public void setAdminPanel(VBox adminPanel) {
        AdminPanel = adminPanel;
    }

}