package Controllers.UserAccountManagementControllers;
//made by steve

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.AesCrypto;
import securityandtime.config;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static securityandtime.config.*;

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
    private UtilityClass utilityClass = new UtilityClass();
    private Connection connection = utilityClass.getConnection();
    @FXML
    private MenuItem backupMenu;
    @FXML
    private MenuItem startDayMenu;
    @FXML
    private MenuItem endDayMenu;
    @FXML
    private MenuItem reportIssuesMenu;
    @FXML
    private MenuItem restartServerMenu;
    @FXML
    private MenuItem troubleShootMenu;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem abtMenu;
    @FXML
    private MenuItem termsMenu;
    @FXML
    private MenuItem checkUpdatesMenu;
    @FXML
    private MenuItem reachUsMenu;
    @FXML
    private MenuItem generateReportsMenu;
    @FXML
    private MenuItem documentationMenu;
    private String timePassedAccordingToDbVAlues;
    private String path = fileSavePath + "backups";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config.panel.put("panel", AdminPanel);
        menuClick();
        buttonClick();
        time(clock);
        config.panel.put("panel", AdminPanel);
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
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

    void refresh() throws SQLException {
        ResultSet resultSet;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
        statement.setString(1, config.user.get("user"));
        resultSet = statement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                config.login.put("loggedinasadmin", true);
                config.user.put("userName", resultSet.getString("employeename"));

                config.user.put("user", resultSet.getString("email"));
                config.key.put("key", resultSet.getString("subscribername"));
                config.user.put("backupemail", resultSet.getString("backupemail"));
                config.user.put("backupemailpassword", AesCrypto.decrypt(encryptionkey, resultSet.getString("backupemailpassword")));
            }
        }
    }


    private void buttonClick() {

        backup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String timePassedAccordingToDbVAlues = null;

                PreparedStatement prep = null;

                try {
                    prep = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
                    prep.setString(1, "backupLocation");
                    ResultSet resultSet = prep.executeQuery();
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            path = resultSet.getString("value");

                        }
                    }
                    resultSet.close();
                    prep.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users where email=?");
                    preparedStatement.setString(1, user.get("user"));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        if (resultSet.getString("backupemailPassword") == null || resultSet.getString("backupemail") == null) {
                            showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "SET UP EMAIL FOR BACKUPS", "YOU NEED TO CREATE A BACK UP EMAIL AND A PASSWORD FOR THAT EMAIL FOR ONLINE BACKUPS TO TAKE PLACE IN ACCOUNT SECTION");
                        } else {
                            try {
                                URL url = new URL("https://www.google.com/");
                                URLConnection connection = url.openConnection();
                                connection.connect();

                                String filepath = backup(path);
                                showAlert(Alert.AlertType.INFORMATION, backup.getScene().getWindow(), "SUCCESS", "BACK UP HAS BEEN COMPLETED SUCCESSFULLY TO " + filepath);
                            } catch (IOException e) {
                                showAlert(Alert.AlertType.ERROR, AdminPanel.getScene().getWindow(), "ERROR", "YOU NEED AN ACTIVE INTERNET CONNECTION TO CARRY OUT A BACK UP");
                            } catch (Exception ignored) {

                            }
                        }
                    }
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }


            //todo continue from backing up database
            private String backup(String path) throws Exception {
                Properties properties = new Properties();
                properties.setProperty(MysqlExportService.DB_NAME, "nanotechsoftwarespos");
                properties.setProperty(MysqlExportService.DB_USERNAME, "root");
                properties.setProperty(MysqlExportService.DB_PASSWORD, "");
//properties relating to email configurations
                properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.gmail.com");
                properties.setProperty(MysqlExportService.EMAIL_PORT, "587");
                properties.setProperty(MysqlExportService.EMAIL_USERNAME, user.get("backupemail"));
                properties.setProperty(MysqlExportService.EMAIL_PASSWORD, user.get("backupemailpassword"));
                properties.setProperty(MysqlExportService.EMAIL_FROM, user.get("backupemail"));
                properties.setProperty(MysqlExportService.EMAIL_TO, backupmail);
                properties.setProperty(MysqlExportService.EMAIL_TO, backupmail1);
                properties.setProperty(MysqlExportService.EMAIL_SUBJECT, "BACK UP FOR POS");
                File zip;
                if (path == null) {
                    path = fileSavePath + "backups";
                    zip = new File(fileSavePath + "backups");

                } else {
                    zip = new File(path);

                }
                properties.setProperty(MysqlExportService.TEMP_DIR, zip.getPath());
                properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
                MysqlExportService mysqlExportService = new MysqlExportService(properties);


                File file = mysqlExportService.getGeneratedZipFile();
                //add path to back up table
                mysqlExportService.clearTempFiles(false);
                mysqlExportService.export();

                return zip.getPath();
            }


        });
        audits.setOnMouseClicked(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/audits.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        visitSuppliers.setOnMouseClicked(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Desktop.getDesktop().browse(new URL(supplierSite).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        carwashpanel.setOnAction(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            AdminPanel.getChildren().removeAll();
            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        stockspanel.setOnMousePressed(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        employees.setOnMousePressed(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            showAlert(Alert.AlertType.INFORMATION, AdminPanel.getScene().getWindow(), "coming soon", "Feature not yet supported");
        });

    }

    private void localBackup() {
        try {
            PreparedStatement prep;
            prep = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
            prep.setString(1, "backup");
            ResultSet rs = prep.executeQuery();
            if (!rs.isBeforeFirst()) {
//does not exist
                timePassedAccordingToDbVAlues = null;

            } else {
                while (rs.next()) {
                    timePassedAccordingToDbVAlues = rs.getString("value");
                }
            }
            if (timePassedAccordingToDbVAlues == null) {
//                    4 day backup
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + 4 * 24 * 60 * 60);

            } else if (timePassedAccordingToDbVAlues.equalsIgnoreCase("STARTUP BACKUP")) {
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + 24 * 60 * 60);

            } else if (timePassedAccordingToDbVAlues.equalsIgnoreCase("END DAY BACK UP")) {
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + 10 * 24 * 60 * 60);

            } else {
                //4 day backup
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + 4 * 24 * 60 * 60);

            }
            PreparedStatement insertBackup = connection.prepareStatement("INSERT INTO backups(path ,nextBackup) VALUES (?,?)");
            insertBackup.setString(1, path);
            insertBackup.setString(2, timePassedAccordingToDbVAlues);
            insertBackup.executeUpdate();
            insertBackup.close();
            rs.close();
            prep.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private UtilityClass getUtilityClass() {
        return utilityClass;
    }

    private AdminPanelController setUtilityClass(UtilityClass utilityClass) {
        this.utilityClass = utilityClass;
        return this;
    }


    private MenuItem getBackupMenu() {
        return backupMenu;
    }

    private AdminPanelController setBackupMenu(MenuItem backupMenu) {
        this.backupMenu = backupMenu;
        return this;
    }

    private MenuItem getStartDayMenu() {
        return startDayMenu;
    }

    private AdminPanelController setStartDayMenu(MenuItem startDayMenu) {
        this.startDayMenu = startDayMenu;
        return this;
    }

    private MenuItem getEndDayMenu() {
        return endDayMenu;
    }

    private AdminPanelController setEndDayMenu(MenuItem endDayMenu) {
        this.endDayMenu = endDayMenu;
        return this;
    }

    private MenuItem getReportIssuesMenu() {
        return reportIssuesMenu;
    }

    private AdminPanelController setReportIssuesMenu(MenuItem reportIssuesMenu) {
        this.reportIssuesMenu = reportIssuesMenu;
        return this;
    }

    private MenuItem getRestartServerMenu() {
        return restartServerMenu;
    }

    private AdminPanelController setRestartServerMenu(MenuItem restartServerMenu) {
        this.restartServerMenu = restartServerMenu;
        return this;
    }

    private MenuItem getTroubleShootMenu() {
        return troubleShootMenu;
    }

    private AdminPanelController setTroubleShootMenu(MenuItem troubleShootMenu) {
        this.troubleShootMenu = troubleShootMenu;
        return this;
    }

    private Menu getHelpMenu() {
        return helpMenu;
    }

    private AdminPanelController setHelpMenu(Menu helpMenu) {
        this.helpMenu = helpMenu;
        return this;
    }

    private MenuItem getAbtMenu() {
        return abtMenu;
    }

    private AdminPanelController setAbtMenu(MenuItem abtMenu) {
        this.abtMenu = abtMenu;
        return this;
    }

    private MenuItem getTermsMenu() {
        return termsMenu;
    }

    private AdminPanelController setTermsMenu(MenuItem termsMenu) {
        this.termsMenu = termsMenu;
        return this;
    }

    private MenuItem getCheckUpdatesMenu() {
        return checkUpdatesMenu;
    }

    private AdminPanelController setCheckUpdatesMenu(MenuItem checkUpdatesMenu) {
        this.checkUpdatesMenu = checkUpdatesMenu;
        return this;
    }

    private MenuItem getReachUsMenu() {
        return reachUsMenu;
    }

    private AdminPanelController setReachUsMenu(MenuItem reachUsMenu) {
        this.reachUsMenu = reachUsMenu;
        return this;
    }

    private MenuItem getGenerateReportsMenu() {
        return generateReportsMenu;
    }

    private AdminPanelController setGenerateReportsMenu(MenuItem generateReportsMenu) {
        this.generateReportsMenu = generateReportsMenu;
        return this;
    }

    private MenuItem getDocumentationMenu() {
        return documentationMenu;
    }

    private AdminPanelController setDocumentationMenu(MenuItem documentationMenu) {
        this.documentationMenu = documentationMenu;
        return this;
    }
}