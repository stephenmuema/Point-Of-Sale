package Controllers.UserAccountManagementControllers;

import Controllers.UtilityClass;
import com.smattme.MysqlExportService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static securityandtime.config.fileSavePath;
import static securityandtime.config.supplierSite;

public class AdminPanelController extends UtilityClass implements Initializable {
    public MenuItem menulogout;
    public MenuItem details;
    public MenuItem license;
    public Label clock;
    public Button employees;
    public Button stockspanel;
    public Button carwashpanel;
    public Button visitSuppliers;
    public Button backup;
    public Button audits;
    @FXML
    private AnchorPane AdminPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuClick();
        buttonClick();
        time(clock);


        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                () -> {
                    try {
                        config.login.put("loggedout", true);

                        AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(AdminPanel, Event.ANY);
    }


    private void menuClick() {
        details.setOnAction(event -> {
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
        });
        menulogout.setOnAction(event -> {
            logout(AdminPanel);

        });
    }



    private void buttonClick() {

        backup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    URL url = new URL("https://www.google.com/");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    System.out.println(backup());

                    showAlert(Alert.AlertType.ERROR, AdminPanel.getScene().getWindow(), "ERROR", "YOU NEED AN ACTIVE INTERNET CONNECTION TO CARRY OUT A BACK UP");
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, AdminPanel.getScene().getWindow(), "ERROR", "YOU NEED AN ACTIVE INTERNET CONNECTION TO CARRY OUT A BACK UP");
                } catch (Exception ignored) {

                }


            }

            //todo continue from backing up database
            private String backup() throws Exception {
                //required properties for exporting of db
                Properties properties = new Properties();
                properties.setProperty(MysqlExportService.DB_NAME, "nanotechsoftwarespos");
                properties.setProperty(MysqlExportService.DB_USERNAME, "root");
                properties.setProperty(MysqlExportService.DB_PASSWORD, "");

//properties relating to email config
                properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.gmail.com");
                properties.setProperty(MysqlExportService.EMAIL_PORT, "587");
                properties.setProperty(MysqlExportService.EMAIL_USERNAME, "muemasnyamai@gmail.com");
                properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "tpgkhylqyxiypqld");
                properties.setProperty(MysqlExportService.EMAIL_FROM, "muemasnyamai@gmail.com");
                properties.setProperty(MysqlExportService.EMAIL_TO, "muemasnyamai@gmail.com");

//set the outputs temp dir
                File zip = new File(fileSavePath + "backups");
                properties.setProperty(MysqlExportService.TEMP_DIR, zip.getPath());
                properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
                MysqlExportService mysqlExportService = new MysqlExportService(properties);
                File file = mysqlExportService.getGeneratedZipFile();
                mysqlExportService.clearTempFiles(false);
//                System.out.println(mysqlExportService.getGeneratedSql());
                mysqlExportService.export();

//add to db
                return mysqlExportService.getGeneratedSql();
            }

        });
        audits.setOnMouseClicked(event -> {
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/audits.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        visitSuppliers.setOnMouseClicked(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(supplierSite).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        carwashpanel.setOnAction(event -> {
            AdminPanel.getChildren().removeAll();
            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        stockspanel.setOnMousePressed(event -> {

            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        employees.setOnMousePressed(event -> {
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            showAlert(Alert.AlertType.INFORMATION, AdminPanel.getScene().getWindow(), "coming soon", "Feature not yet supported");
        });
//        addshop.setOnMousePressed(event -> {
//            try {
//                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("addshop.fxml")))));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
    }


    public MenuItem getMenulogout() {
        return menulogout;
    }

    public void setMenulogout(MenuItem menulogout) {
        this.menulogout = menulogout;
    }


    public MenuItem getDetails() {
        return details;
    }

    public void setDetails(MenuItem details) {
        this.details = details;
    }


    public MenuItem getLicense() {
        return license;
    }

    public void setLicense(MenuItem license) {
        this.license = license;
    }



    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
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

    public AnchorPane getAdminPanel() {
        return AdminPanel;
    }

    public void setAdminPanel(AnchorPane adminPanel) {
        AdminPanel = adminPanel;
    }

}