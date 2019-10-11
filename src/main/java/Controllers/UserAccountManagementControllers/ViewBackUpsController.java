package Controllers.UserAccountManagementControllers;

import Controllers.SevenZ;
import Controllers.UtilityClass;
import MasterClasses.BackUpFilesMaster;
import com.smattme.MysqlImportService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import securityandtime.AesCrypto;
import securityandtime.config;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.*;


public class ViewBackUpsController extends UtilityClass implements Initializable {

    public Menu navigation;
    public MenuItem details;
    public Button refreshB;
    public MenuItem panelMenu;
    @FXML
    private Button importDb;
    @FXML
    private AnchorPane panel;
    @FXML
    private Button sendToReports;
    @FXML
    private Button delete;
    @FXML
    private Button mailSendBtn;
    @FXML
    private TableView<BackUpFilesMaster> backupFiles;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileName;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileSize;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileCreated;
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

    private ObservableList<BackUpFilesMaster> data;

    private static String getFileSizeMegaBytes(File file) {
        DecimalFormat df2 = new DecimalFormat("#.####");

        return df2.format((double) file.length() / (1024 * 1024)) + " mb";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuClicked();
        buttonPressed();
        config.panel.put("panel", panel);

    }

    private boolean deleteFile(BackUpFilesMaster backUpFilesMaster) {
        return FileUtils.deleteQuietly(new File(backUpFilesMaster.getPath()));

    }

    private void mailBackupSend(String to, String from, String password) throws MessagingException {
        BackUpFilesMaster backUpFilesMaster = backupFiles.getSelectionModel().getSelectedItem();
        if (backUpFilesMaster != null) {

            File file = new File(backUpFilesMaster.getPath());

            Session session = Session.getDefaultInstance(mailProp,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    });
            session.setDebug(true);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            System.out.println("FROM " + from);

            try {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            } catch (AddressException e) {
                e.printStackTrace();
            }

            message.setSubject("BACK UP FILE NOTIFICATION");
            {
                //send with attachment
                message.addHeader("Content-Type", "multipart/mixed; charset=UTF-8");
                BodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText("BELOW IS ATTACHED FILE FOR YOUR BACKUP");

                // Create a multipart message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = file.getAbsolutePath();
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(new File(filename).getName());
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);

                // Send message
                Transport.send(message);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "MAIL SENT", "YOUR EMAIL HAS BEEN SENT SUCCESSFULLY");
                    }
                });

            }
        }
    }

    private void init() throws SQLException {
        data = FXCollections.observableArrayList();
        data.clear();
        checkEmailAndPassword();
        gegtFiles((String) sysconfig.get("backUpLoc"));
        backupFiles.setRowFactory(tv -> {
            TableRow<BackUpFilesMaster> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    BackUpFilesMaster rowData = row.getItem();
//                    System.out.println("Double click on: "+rowData.getName());
                    importDbFile(rowData);

                }
            });
            return row;
        });
        ContextMenu cm = new ContextMenu();
        MenuItem contextImport = new MenuItem("IMPORT BACKUP");
        cm.getItems().add(contextImport);
        MenuItem contextDelete = new MenuItem("DELETE BACKUP FILE");
        cm.getItems().add(contextDelete);
        MenuItem contextSend = new MenuItem("SEND FILE TO MY BACKUP EMAIL");
        cm.getItems().add(contextSend);
        MenuItem contextReport = new MenuItem("SEND FILE TO REPORTS");
        cm.getItems().add(contextReport);
        cm.getItems().add(navigation);
        if (mailProp.isEmpty()) {
            contextSend.setDisable(true);
            mailSendBtn.setDisable(true);
        }
        contextImport.setOnAction(event -> {
            BackUpFilesMaster backUpFilesMaster = backupFiles.getSelectionModel().getSelectedItem();
            if (backUpFilesMaster != null) {
                importDbFile(backUpFilesMaster);

            }
        });
        contextSend.setOnAction(event -> {
            sendBackup();
        });
        backupFiles.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY) {
                cm.show(backupFiles, t.getScreenX(), t.getScreenY());
            }
        });
    }

    private void refresh() throws SQLException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                panel.getChildren().removeAll();
                try {

                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/viewBackups.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ResultSet resultSet;
        PreparedStatement statement = new UtilityClass().getConnection().prepareStatement("SELECT * FROM users WHERE email=?");
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

    private void buttonPressed() {
        delete.setOnAction(event -> {
            BackUpFilesMaster backUpFilesMaster = backupFiles.getSelectionModel().getSelectedItem();
            if (backUpFilesMaster != null) {
                deleteFile(backUpFilesMaster);

            }
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        refreshB.setOnAction(event -> {
            try {
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        importDb.setOnAction(event -> {
            BackUpFilesMaster backUpFilesMaster = backupFiles.getSelectionModel().getSelectedItem();
            if (backUpFilesMaster != null) {
                importDbFile(backUpFilesMaster);

            }

        });
        mailSendBtn.setOnAction(event -> {
            sendBackup();
        });
    }

    private void sendBackup() {
        try {
            mailBackupSend(user.get("backupemail"), backupmail, mailPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void importDbFile(BackUpFilesMaster backUpFilesMaster) {
        File file;
        if (backUpFilesMaster != null) {
            String fname = backUpFilesMaster.getPath();
            if (FilenameUtils.getExtension(fname).equalsIgnoreCase("zip")) {
                SevenZ sevenZ = new SevenZ();
                sevenZ.unzipBak(fname, sysconfig.get("backUpLoc") + "\\unzippedFiles");

                File directory = new File(sysconfig.get("backUpLoc") + "\\unzippedFiles");
                File[] files = directory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile();
                    }
                });
                file = files[0];
            } else {
                file = new File(fname);
            }
            int length = (int) file.length();


            String sql = null;
            try {
                sql = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
////                System.out.println(sql);
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean res = false;
            try {
                res = MysqlImportService.builder()
                        .setDatabase(dbName)
                        .setSqlString(sql)
                        .setUsername(dbUser)
                        .setPassword(dbPass)
                        .setDeleteExisting(true)
                        .setDropExisting(false)
                        .importDatabase();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (res) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "YOUR IMPORT WAS SUCCCESSFULL");
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR!!", "YOUR IMPORT FAILED.CONTACT THE ADMINISTRATOR");

            }

        }
    }

    private void menuClicked() {
        panelMenu.setOnAction(event -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    panel.getChildren().removeAll();
                    try {

                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        });
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

    }

    private void gegtFiles(String dir) {
        File directory = new File(dir);
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        if (files != null) {
            for (File file : files) {
//                System.out.println(file.getAbsolutePath());
                if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("zip") || FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("sql")) {
                    BasicFileAttributes attrs = null;
                    try {
                        attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileTime time = attrs.creationTime();

                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String lastModified = simpleDateFormat.format(new Date(time.toMillis()));
                    String name = file.getName();
                    String path = file.getAbsolutePath();
                    String size = getFileSizeMegaBytes(file);
                    BackUpFilesMaster backUpFilesMaster = new BackUpFilesMaster();
                    backUpFilesMaster.setDate(lastModified);
                    backUpFilesMaster.setName(name);
                    backUpFilesMaster.setSize(size);
                    backUpFilesMaster.setPath(path);
                    data.add(backUpFilesMaster);
                }
            }
            backupFiles.setItems(data);
        }
        fileCreated.setCellValueFactory(new PropertyValueFactory<>("date"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        backupFiles.refresh();

    }

    public void goToStaff(AnchorPane panel) {

        try {
            panel.getChildren().removeAll();
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToStocks(AnchorPane panel) {

        try {
            panel.getChildren().removeAll();
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCarwash(AnchorPane panel) {

        panel.getChildren().removeAll();
        try {
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToSuppliers(AnchorPane panel) {

        try {
            Desktop.getDesktop().browse(new URL(supplierSite).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void gotoAudits(AnchorPane panel) {

        try {
            panel.getChildren().removeAll();
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/audits.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Button getImportDb() {
        return importDb;
    }

    public ViewBackUpsController setImportDb(Button importDb) {
        this.importDb = importDb;
        return this;
    }

    public AnchorPane getPanel() {
        return panel;
    }

    public ViewBackUpsController setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public Button getSendToReports() {
        return sendToReports;
    }

    public ViewBackUpsController setSendToReports(Button sendToReports) {
        this.sendToReports = sendToReports;
        return this;
    }

    public Button getDelete() {
        return delete;
    }

    public ViewBackUpsController setDelete(Button delete) {
        this.delete = delete;
        return this;
    }

    public Button getMailSendBtn() {
        return mailSendBtn;
    }

    public ViewBackUpsController setMailSendBtn(Button mailSendBtn) {
        this.mailSendBtn = mailSendBtn;
        return this;
    }

    public TableView<BackUpFilesMaster> getBackupFiles() {
        return backupFiles;
    }

    public ViewBackUpsController setBackupFiles(TableView<BackUpFilesMaster> backupFiles) {
        this.backupFiles = backupFiles;
        return this;
    }

    public TableColumn<BackUpFilesMaster, String> getFileName() {
        return fileName;
    }

    public ViewBackUpsController setFileName(TableColumn<BackUpFilesMaster, String> fileName) {
        this.fileName = fileName;
        return this;
    }

    public TableColumn<BackUpFilesMaster, String> getFileSize() {
        return fileSize;
    }

    public ViewBackUpsController setFileSize(TableColumn<BackUpFilesMaster, String> fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public TableColumn<BackUpFilesMaster, String> getFileCreated() {
        return fileCreated;
    }

    public ViewBackUpsController setFileCreated(TableColumn<BackUpFilesMaster, String> fileCreated) {
        this.fileCreated = fileCreated;
        return this;
    }

    public MenuItem getAbtMenu() {
        return abtMenu;
    }

    public ViewBackUpsController setAbtMenu(MenuItem abtMenu) {
        this.abtMenu = abtMenu;
        return this;
    }

    public MenuItem getTermsMenu() {
        return termsMenu;
    }

    public ViewBackUpsController setTermsMenu(MenuItem termsMenu) {
        this.termsMenu = termsMenu;
        return this;
    }

    public MenuItem getCheckUpdatesMenu() {
        return checkUpdatesMenu;
    }

    public ViewBackUpsController setCheckUpdatesMenu(MenuItem checkUpdatesMenu) {
        this.checkUpdatesMenu = checkUpdatesMenu;
        return this;
    }

    public MenuItem getReachUsMenu() {
        return reachUsMenu;
    }

    public ViewBackUpsController setReachUsMenu(MenuItem reachUsMenu) {
        this.reachUsMenu = reachUsMenu;
        return this;
    }

    public MenuItem getGenerateReportsMenu() {
        return generateReportsMenu;
    }

    public ViewBackUpsController setGenerateReportsMenu(MenuItem generateReportsMenu) {
        this.generateReportsMenu = generateReportsMenu;
        return this;
    }

    public MenuItem getDocumentationMenu() {
        return documentationMenu;
    }

    public ViewBackUpsController setDocumentationMenu(MenuItem documentationMenu) {
        this.documentationMenu = documentationMenu;
        return this;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }

    public ViewBackUpsController setMenuQuit(MenuItem menuQuit) {
        this.menuQuit = menuQuit;
        return this;
    }

    public MenuItem getStaffMenu() {
        return staffMenu;
    }

    public ViewBackUpsController setStaffMenu(MenuItem staffMenu) {
        this.staffMenu = staffMenu;
        return this;
    }

    public MenuItem getCarWashMenu() {
        return carWashMenu;
    }

    public ViewBackUpsController setCarWashMenu(MenuItem carWashMenu) {
        this.carWashMenu = carWashMenu;
        return this;
    }

    public MenuItem getInventoryMenu() {
        return inventoryMenu;
    }

    public ViewBackUpsController setInventoryMenu(MenuItem inventoryMenu) {
        this.inventoryMenu = inventoryMenu;
        return this;
    }

    public MenuItem getMrMenu() {
        return mrMenu;
    }

    public ViewBackUpsController setMrMenu(MenuItem mrMenu) {
        this.mrMenu = mrMenu;
        return this;
    }

    public MenuItem getAuditsMenu() {
        return auditsMenu;
    }

    public ViewBackUpsController setAuditsMenu(MenuItem auditsMenu) {
        this.auditsMenu = auditsMenu;
        return this;
    }

    public MenuItem getMenuShutDown() {
        return menuShutDown;
    }

    public ViewBackUpsController setMenuShutDown(MenuItem menuShutDown) {
        this.menuShutDown = menuShutDown;
        return this;
    }

    public MenuItem getMenuRestart() {
        return menuRestart;
    }

    public ViewBackUpsController setMenuRestart(MenuItem menuRestart) {
        this.menuRestart = menuRestart;
        return this;
    }

    public ObservableList<BackUpFilesMaster> getData() {
        return data;
    }

    public ViewBackUpsController setData(ObservableList<BackUpFilesMaster> data) {
        this.data = data;
        return this;
    }
}
