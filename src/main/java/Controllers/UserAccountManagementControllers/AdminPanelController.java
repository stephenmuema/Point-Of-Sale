package Controllers.UserAccountManagementControllers;
//made by steve

import Controllers.FetchDbDetails;
import Controllers.IdleMonitor;
import Controllers.SevenZ;
import Controllers.UtilityClass;
import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import gdrive.DriveMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import securityandtime.AesCrypto;
import securityandtime.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.*;

import static securityandtime.config.*;

public class AdminPanelController extends UtilityClass implements Initializable, AdminInterface {

    public MenuItem license;
    public ImageView logoImage;
    @FXML
    private Label clock;
    @FXML
    private Button employees;
    @FXML
    private Button stockspanel;
    @FXML
    private Button carwashpanel;
    @FXML
    private Button visitSuppliers;
    @FXML
    private Button backup;
    @FXML
    private Button audits;
    //start of the menu
    @FXML
    private MenuItem menulogout;
    @FXML
    private MenuItem details;
    @FXML
    private Button checkUpdates;
    @FXML
    private Label message;
    @FXML
    private AnchorPane panel;
    @FXML
    private Button refresh;
    private UtilityClass utilityClass = new UtilityClass();
    private Connection connection = utilityClass.getConnection();
    private String timePassedAccordingToDbVAlues;
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
    @FXML
    private MenuItem menuQuit;
    @FXML
    private MenuItem viewBackupsMenu;
    @FXML
    private MenuItem retrieveBackupMenu;
    @FXML
    private MenuItem staffMenu;
    @FXML
    private MenuItem carWashMenu;
    @FXML
    private MenuItem inventoryMenu;
    @FXML
    private MenuItem mrMenu;
    @FXML
    private MenuItem auditsMenu;
    @FXML
    private MenuItem menuShutDown;
    @FXML
    private MenuItem menuRestart;
    @FXML
    private Button viewShiftInformation;
    @FXML
    private Button troubleShootSystem;
    @FXML
    private Button reportIssues;
    @FXML
    private Button restartServer;
    @FXML
    private Button startDay;
    @FXML
    private Button endDay;
    @FXML
    private Button syncDb;
    @FXML
    private Button licenseManager;
    @FXML
    private Button logoutButton;

    @FXML
    private Menu acctMenu;
    @FXML
    private Menu dbMenu;
    @FXML
    private Menu shiftsMenu;
    @FXML
    private Menu systemMenu;
    @FXML
    private Menu helpAndSupport;
    @FXML
    private Menu navigation;
    @FXML
    private Menu inventory;


    private String companyName;

    public AdminPanelController() throws IOException {
    }

    private static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        assert files != null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        assert choice != null;
        System.out.println(choice.getAbsolutePath());
        return choice;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        config.panel.put("panel", panel);
        try {
            checkEmailAndPassword();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            initModules();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ContextMenu cm = new ContextMenu();
        cm.getItems().add(acctMenu);
        cm.getItems().add(dbMenu);
        cm.getItems().add(shiftsMenu);
        cm.getItems().add(systemMenu);
        cm.getItems().add(helpAndSupport);
        cm.getItems().add(navigation);
        cm.getItems().add(inventory);
        panel.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY) {
                cm.show(panel, t.getScreenX(), t.getScreenY());
            }
        });
        menuClick();
        buttonClick();
        time(clock);

        config.panel.put("panel", panel);
        IdleMonitor idleMonitor = null;
        try {
            idleMonitor = new IdleMonitor(Duration.seconds(3600),
                    () -> {
                        try {
                            config.login.put("loggedout", true);

                            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        idleMonitor.register(panel, Event.ANY);

        if (environment != "development") {
            viewShiftInformation.setVisible(false);
            troubleShootSystem.setVisible(false);
            reportIssues.setVisible(false);
            restartServer.setVisible(false);
            startDay.setVisible(false);
            endDay.setVisible(false);
            syncDb.setVisible(false);
            licenseManager.setVisible(false);
            logoutButton.setVisible(false);
            employees.setVisible(false);
            stockspanel.setVisible(false);
            carwashpanel.setVisible(false);
            visitSuppliers.setVisible(false);
            backup.setVisible(false);
            audits.setVisible(false);
            checkUpdates.setVisible(false);
            logoImage.setImage(companyLogoImageObj);

        } else {
            logoImage.setVisible(false);
        }
    }

    private void menuClick() {
        viewBackupsMenu.setOnAction(event -> {
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/viewBackups.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        retrieveBackupMenu.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();

            //Set extension filter

            FileChooser.ExtensionFilter extFilterSQL = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
            FileChooser.ExtensionFilter extFilterZip = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
            FileChooser.ExtensionFilter both = new FileChooser.ExtensionFilter("ZIP files (*.zip) SQL files (*.sql)", "*.zip", "*.sql");

            fileChooser.getExtensionFilters().addAll(extFilterSQL, extFilterZip);
            fileChooser.setTitle("SELECT YOUR SQL BACKUP FILE");
            //Show open file dialog
            fileChooser.setInitialDirectory(new File((String) sysconfig.get("backUpLoc")));
            File file = fileChooser.showOpenDialog(null);

            int length = (int) file.length();


            String sql = null;
            if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("sql")) {
                try {
                    sql = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
//                System.out.println(sql);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("zip")) {
                SevenZ sevenZ = null;
                try {
                    sevenZ = new SevenZ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String fname = file.getAbsolutePath();
                sevenZ.unzipBak(fname, sysconfig.get("backUpLoc") + "\\unzippedFiles");
                File directory = new File(sysconfig.get("backUpLoc") + "\\unzippedFiles");
                File[] files = directory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile();
                    }
                });
                try {
                    assert files != null;
                    sql = new String(Files.readAllBytes(Paths.get(files[0].getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            boolean res = false;
            try {
                res = MysqlImportService.builder()
                        .setDatabase(FetchDbDetails.getDbName())
                        .setSqlString(sql)
                        .setUsername(FetchDbDetails.getDbUser())
                        .setPassword(FetchDbDetails.getDbPass())
                        .setDeleteExisting(true)
                        .setDropExisting(false)
                        .importDatabase();


            } catch (SQLException | ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR!!", "YOUR IMPORT FAILED.CONTACT THE ADMINISTRATOR");
            }
            if (res) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "YOUR IMPORT WAS SUCCCESSFULL");
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR!!", "YOUR IMPORT FAILED.CONTACT THE ADMINISTRATOR");

            }

        });
        carWashMenu.setOnAction(event -> {
            goToCarwash(panel);
        });
        auditsMenu.setOnAction(event -> {
            gotoAudits(panel);
        });
        staffMenu.setOnAction(event -> {
            goToStaff(panel);
        });
        inventoryMenu.setOnAction(event -> {
            goToStocks(panel);
        });

        backupMenu.setOnAction(event -> backingUpMainMethod());
        menuShutDown.setOnAction(event -> {
            try {
                shutdown();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuRestart.setOnAction(event -> {
            try {
                restart();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuQuit.setOnAction(event -> exit());
        details.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("UserAccountManagementFiles/adminSettings.fxml"));
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
        menulogout.setOnAction(event -> logout(panel));
        startDayMenu.setOnAction(event -> {
            try {
                if (readBackupInfo().equals("STARTUP BACKUP")) {
                    backingUpMainMethod();//back up on start day
                }
                startDayMethod();
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "DAY STARTED", "DAY STARTED SUCCESSFULLY");
                reload();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        endDayMenu.setOnAction(event -> {
            try {
                endDayMethod();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void initModules() throws SQLException {

        Connection connection = null;
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("SELECT * FROM company ORDER BY id DESC LIMIT 1");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.isBeforeFirst()) {
                //admin has set company name
                while (rs.next()) {
                    companyName = AesCrypto.decrypt(encryptionkey, rs.getString("companyname"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            checkEmailAndPassword();
        } catch (NullPointerException e) {
//            reportIssues.setDisable(true);
//            reportIssuesMenu.setVisible(false);
//            reportIssues.setText("NO EMAIL CONFIGURED");
//            startDay.setDisable(true);
//            startDayMenu.setDisable(true);
//            endDay.setDisable(true);
//            endDayMenu.setDisable(true);
//            backup.setDisable(true);
//            backupMenu.setDisable(true);
//            showAlert(Alert.AlertType.WARNING,panel.getScene().getWindow(),"EMAIL NOT CONFIGURED","YOUR BACK UP EMAIL IS NOT CONFIGURED");

        }
        checkIfPreviousDayEnded();

        boolean checkTable = checkBackUpPeriodic();
        boolean backupdata = false;

        if (checkTable) {
            Statement preparedStatement = connection.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM backups ORDER BY id DESC LIMIT 1");
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    if (new Timestamp(Long.parseLong(resultSet.getString("nextBackup"))).before(new Timestamp(new Date().getTime()))) {
                        backupdata = true;
                    }
                }
                if (backupdata) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "AUTOMATIC BACKUP", "BACK UP RUNNING");

                        backingUpMainMethod();
                    });
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "INITIAL BACKUP TESTING", "THIS IS YOUR FIRST BACK UP ON THE NEW BACKUOP LOCATION.wE HAVE TO TEST IT TO CHECK IF THE BACKUP FUNCTIONALITY WORKS");
                backingUpMainMethod();

            }

        }
    }


    private void checkIfPreviousDayEnded() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id FROM DAYS WHERE end_time IS NULL ORDER BY id DESC LIMIT 1");
        if (resultSet.isBeforeFirst()) {
            startDayMenu.setDisable(true);
            startDay.setVisible(false);
            endDay.setVisible(true);
            endDayMenu.setDisable(false);
        }
    }


    private boolean checkBackUpPeriodic() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
        preparedStatement.setString(1, "backup");
        ResultSet resultSet = preparedStatement.executeQuery();
        String choice = null;
        boolean backup;
        if (!resultSet.isBeforeFirst()) {
            systemSettingsBackUp();
            checkBackUpPeriodic();
            choice = "not periodic";
        } else {
            while (resultSet.next()) {
                choice = resultSet.getString("value");
            }
        }
        assert choice != null;
        backup = choice.equalsIgnoreCase("periodic");
        preparedStatement.close();
        resultSet.close();
        return backup;
    }


    private void buttonClick() {
        refresh.setOnAction(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        reportIssues.setOnAction(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                panel.getChildren().removeAll();
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/reportIssues.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        startDay.setOnAction(event -> {
            try {
                if (readBackupInfo().equals("STARTUP BACKUP")) {
                    backingUpMainMethod();//back up on start day
                }
                startDayMethod();
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "DAY STARTED", "DAY STARTED SUCCESSFULLY");
                reload();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

        });
        endDay.setOnAction(event -> {
            try {
                endDayMethod();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        logoutButton.setOnAction(event -> logout(panel));
        backup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backingUpMainMethod();
            }


        });
        audits.setOnMouseClicked(event -> {
            gotoAudits(panel);

        });
        visitSuppliers.setOnMouseClicked(event -> {
            goToSuppliers(panel);
        });
        carwashpanel.setOnAction(event -> {
            goToCarwash(panel);


        });

        stockspanel.setOnMousePressed(event -> {
            goToStocks(panel);

        });


        employees.setOnMousePressed(event -> {
            goToStaff(panel);
//            showAlert(Alert.AlertType.INFORMATION, AdminPanel.getScene().getWindow(), "coming soon", "Feature not yet supported");
        });

    }


    private void reload() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void endDayMethod() throws SQLException {
        //send reports to manager
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("END DAY PROCEDURE");
        alert1.setHeaderText(null);
        alert1.setContentText("ENDING A DAY MAY AFFECT YOUR REPORTS NEGATIVELY IF IT IS DONE AT THE WRONG TIME BECAUSE IT IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO END DAY?");
        alert1.initOwner(config.panel.get("panel").getScene().getWindow());
        Optional<ButtonType> option = alert1.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            if (readBackupInfo().equals("END DAY BACK UP")) {
                backingUpMainMethod();//back up on end day
            }
            Thread backUpThread = new Thread(new BackUp());
            backUpThread.start();
        }
    }

    private String backup(String path) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, FetchDbDetails.getDbName());
        properties.setProperty(MysqlExportService.DB_USERNAME, FetchDbDetails.getDbUser());
        properties.setProperty(MysqlExportService.DB_PASSWORD, FetchDbDetails.getDbPass());
        properties.setProperty(MysqlExportService.EMAIL_HOST, host);
        properties.setProperty(MysqlExportService.EMAIL_PORT, "587");
        properties.setProperty(MysqlExportService.EMAIL_USERNAME, user.get("backupemail"));
        properties.setProperty(MysqlExportService.EMAIL_PASSWORD, user.get("backupemailpassword"));
        properties.setProperty(MysqlExportService.EMAIL_FROM, user.get("backupemail"));
        properties.setProperty(MysqlExportService.EMAIL_TO, alternativeBackupEmail);

        properties.setProperty(MysqlExportService.EMAIL_SUBJECT, "BACK UP FOR POS");
        File zip;
        if (path == null) {
            zip = new File(fileSavePath + "backups");
        } else {
            Path dirs = Paths.get(path);

            if (!Files.exists(dirs)) {

                try {
                    Files.createDirectories(dirs);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

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

    private void startDayMethod() {
        try {
            LocalDate myObj = LocalDate.now(); // Create a date object
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO days(start_time)VALUES(?)");
            preparedStatement.setString(1, myObj.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String readBackupInfo() throws SQLException {
        String backuptime = null;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
        preparedStatement.setString(1, "backup");
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                backuptime = rs.getString("value");
            }
        } else {
            PreparedStatement preparedStatement1;
            preparedStatement1 = connection.prepareStatement("INSERT INTO systemsettings (name,type,value)VALUES (?,?,?)");
            preparedStatement1.setString(1, "backup");
            preparedStatement1.setString(2, "security");
            preparedStatement1.setString(3, "STARTUP BACKUP");
            preparedStatement1.executeUpdate();
//        preparedStatement1.close();
            readBackupInfo();
        }
        return backuptime;
    }

    private void backingUpMainMethod() {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String timePassedAccordingToDbVAlues = null;

        PreparedStatement prep;

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
//                        message.setText("backing up data...");
                        URL url = new URL(google);
                        URLConnection connection = url.openConnection();
                        connection.connect();

                        String filepath = backup(path);


                        showAlert(Alert.AlertType.INFORMATION, backup.getScene().getWindow(), "SUCCESS", "BACK UP HAS BEEN COMPLETED SUCCESSFULLY TO " + filepath);
                        File lastFileModified = lastFileModified(path);
                        if (!companyName.isEmpty() && companyName != null) {
                            DriveMain.driveBackupMain(companyName + "__file__" + lastFileModified.getName(), lastFileModified.getAbsolutePath());
//                            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
//                            fileMetadata.setName(companyName + " __ file__" + lastFileModified.getName());
//                            java.io.File filePath = new java.io.File(lastFileModified.getAbsolutePath());
//                            FileContent mediaContent = new FileContent("multipart/x-zip", filePath);
//                            com.google.api.services.drive.model.File file = new DriveSuperClass().getService().files().create(fileMetadata, mediaContent)
//                                    .setFields("id")
//                                    .execute();
//                            System.out.println("File ID: " + file.getId());

                        }
                        localBackup(lastFileModified.getAbsolutePath());
//                        message.setText("DONE BACKING UP DATA...");
                    } catch (IOException e) {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "YOU NEED AN ACTIVE INTERNET CONNECTION TO CARRY OUT A BACK UP");
                    } catch (Exception ignored) {

                    }
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void localBackup(String path) {
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
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + (4 * 24 * 60 * 60 * 1000));

            } else if (timePassedAccordingToDbVAlues.equalsIgnoreCase("STARTUP BACKUP")) {
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + (24 * 60 * 60 * 1000));

            } else if (timePassedAccordingToDbVAlues.equalsIgnoreCase("END DAY BACK UP")) {
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + (24 * 60 * 60 * 1000));

            } else {
                //4 day backup
                timePassedAccordingToDbVAlues = String.valueOf(new Date().getTime() + 4 * 24 * 60 * 60 * 1000);

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
        return panel;
    }

    public void setAdminPanel(AnchorPane adminPanel) {
        panel = adminPanel;
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

    private Button getRefresh() {
        return refresh;
    }

    private AdminPanelController setRefresh(Button refresh) {
        this.refresh = refresh;
        return this;
    }

    private String getTimePassedAccordingToDbVAlues() {
        return timePassedAccordingToDbVAlues;
    }

    private AdminPanelController setTimePassedAccordingToDbVAlues(String timePassedAccordingToDbVAlues) {
        this.timePassedAccordingToDbVAlues = timePassedAccordingToDbVAlues;
        return this;
    }

    private String getPath() {
        return path;
    }


    private Button getViewShiftInformation() {
        return viewShiftInformation;
    }

    private AdminPanelController setViewShiftInformation(Button viewShiftInformation) {
        this.viewShiftInformation = viewShiftInformation;
        return this;
    }

    private Button getTroubleShootSystem() {
        return troubleShootSystem;
    }

    private AdminPanelController setTroubleShootSystem(Button troubleShootSystem) {
        this.troubleShootSystem = troubleShootSystem;
        return this;
    }

    private Button getReportIssues() {
        return reportIssues;
    }

    private AdminPanelController setReportIssues(Button reportIssues) {
        this.reportIssues = reportIssues;
        return this;
    }

    private Button getRestartServer() {
        return restartServer;
    }

    private AdminPanelController setRestartServer(Button restartServer) {
        this.restartServer = restartServer;
        return this;
    }

    private Button getStartDay() {
        return startDay;
    }

    private AdminPanelController setStartDay(Button startDay) {
        this.startDay = startDay;
        return this;
    }

    private Button getEndDay() {
        return endDay;
    }

    private AdminPanelController setEndDay(Button endDay) {
        this.endDay = endDay;
        return this;
    }

    private Button getSyncDb() {
        return syncDb;
    }

    private AdminPanelController setSyncDb(Button syncDb) {
        this.syncDb = syncDb;
        return this;
    }

    private Button getLicenseManager() {
        return licenseManager;
    }

    private AdminPanelController setLicenseManager(Button licenseManager) {
        this.licenseManager = licenseManager;
        return this;
    }

    private Button getLogoutButton() {
        return logoutButton;
    }

    private AdminPanelController setLogoutButton(Button logoutButton) {
        this.logoutButton = logoutButton;
        return this;
    }

    private MenuItem getMenuQuit() {
        return menuQuit;
    }

    private AdminPanelController setMenuQuit(MenuItem menuQuit) {
        this.menuQuit = menuQuit;
        return this;
    }

    private MenuItem getViewBackupsMenu() {
        return viewBackupsMenu;
    }

    private AdminPanelController setViewBackupsMenu(MenuItem viewBackupsMenu) {
        this.viewBackupsMenu = viewBackupsMenu;
        return this;
    }

    private MenuItem getRetrieveBackupMenu() {
        return retrieveBackupMenu;
    }

    private AdminPanelController setRetrieveBackupMenu(MenuItem retrieveBackupMenu) {
        this.retrieveBackupMenu = retrieveBackupMenu;
        return this;
    }

    private MenuItem getStaffMenu() {
        return staffMenu;
    }

    private AdminPanelController setStaffMenu(MenuItem staffMenu) {
        this.staffMenu = staffMenu;
        return this;
    }

    private MenuItem getCarWashMenu() {
        return carWashMenu;
    }

    private AdminPanelController setCarWashMenu(MenuItem carWashMenu) {
        this.carWashMenu = carWashMenu;
        return this;
    }

    private MenuItem getInventoryMenu() {
        return inventoryMenu;
    }

    private AdminPanelController setInventoryMenu(MenuItem inventoryMenu) {
        this.inventoryMenu = inventoryMenu;
        return this;
    }

    private MenuItem getMrMenu() {
        return mrMenu;
    }

    private AdminPanelController setMrMenu(MenuItem mrMenu) {
        this.mrMenu = mrMenu;
        return this;
    }

    private MenuItem getAuditsMenu() {
        return auditsMenu;
    }

    private AdminPanelController setAuditsMenu(MenuItem auditsMenu) {
        this.auditsMenu = auditsMenu;
        return this;
    }

    private MenuItem getMenuShutDown() {
        return menuShutDown;
    }

    private AdminPanelController setMenuShutDown(MenuItem menuShutDown) {
        this.menuShutDown = menuShutDown;
        return this;
    }

    private MenuItem getMenuRestart() {
        return menuRestart;
    }

    private AdminPanelController setMenuRestart(MenuItem menuRestart) {
        this.menuRestart = menuRestart;
        return this;
    }

    class BackUp implements Runnable {

        @Override
        public void run() {
            try {
                LocalDate myObj = LocalDate.now(); // Create a date object
                String time = String.valueOf(myObj);
                String id = null;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM DAYS WHERE end_time IS NULL ORDER BY id DESC LIMIT 1");
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        if (resultSet.getString("start_time").equalsIgnoreCase(myObj.toString())) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "ENDING DAY", "DAY HAS BEEN ENDED SUCCESSFULLY");
                                }
                            });
                        }
                        id = resultSet.getString("id");
                    }
                }
                resultSet.close();
                statement.close();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE days SET end_time=? , completed=? WHERE id=?");
                preparedStatement.setString(1, time);
                preparedStatement.setString(2, "complete");
                preparedStatement.setString(3, id);
                if (preparedStatement.executeUpdate() > 0) {
                    reload();
                    endDayMenu.setDisable(true);
                    endDay.setVisible(false);
                    startDayMenu.setDisable(false);
                    startDay.setVisible(true);
                } else {
                    System.out.println("errrr");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}