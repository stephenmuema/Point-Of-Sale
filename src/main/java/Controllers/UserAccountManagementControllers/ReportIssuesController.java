package Controllers.UserAccountManagementControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.AesCrypto;
import securityandtime.config;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.adminEmail;
import static securityandtime.config.encryptionkey;

public class ReportIssuesController extends UtilityClass implements Initializable {
    @FXML
    private Label contacts;
    @FXML
    private Label message;
    @FXML
    private Button backtomainpanel;
    @FXML
    private TextArea description;
    @FXML
    private RadioButton support;
    @FXML
    private RadioButton bug;
    @FXML
    private RadioButton newFeature;
    @FXML
    private Button submit;
    @FXML
    private TextField name;
    @FXML
    private AnchorPane panel;
    @FXML
    private MenuItem details;
    @FXML
    private MenuItem menulogout;
    @FXML
    private MenuItem license;

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
    private String subject;
    private String fromEmail, fromPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup tg = new ToggleGroup();
        bug.setToggleGroup(tg);
        support.setToggleGroup(tg);
        newFeature.setToggleGroup(tg);
        menuClick();
        try {
            buttonPress();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        newFeature.setSelected(true);
        tg.selectedToggleProperty().addListener((ob, o, n) -> {
            RadioButton rb = (RadioButton) tg.getSelectedToggle();

            if (rb != null) {
                subject = rb.getText();

            }
        });

    }

    private void buttonPress() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
        preparedStatement.setString(1, config.user.get("user"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            if (resultSet.getString("backupemail") == null) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "SET UP YOUR BACKUP EMAIL ADDRESS", "THIS SOFTWARE USES THE BACKUP EMAIL TO SEND EMAILS TO THE DEVELOPERS.ADD YOUR BACKUP EMAIL ANS SET ITS PASSWORD IN SETTINGS");
                submit.setDisable(true);

            } else if (resultSet.getString("backupemail").isEmpty()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "SET UP YOUR BACKUP EMAIL ADDRESS", "THIS SOFTWARE USES THE BACKUP EMAIL TO SEND EMAILS TO THE DEVELOPERS.ADD YOUR BACKUP EMAIL ANS SET ITS PASSWORD IN SETTINGS");
                submit.setDisable(true);

            }
            if (resultSet.getString("backupemailPassword") == null) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "SET UP YOUR BACKUP EMAIL ADDRESS PASSWORD", "THIS SOFTWARE USES THE BACKUP EMAIL TO SEND EMAILS TO THE DEVELOPERS.ADD YOUR BACKUP EMAIL ANS SET ITS PASSWORD IN SETTINGS");
                submit.setDisable(true);

            } else if (resultSet.getString("backupemailPassword").isEmpty()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "SET UP YOUR BACKUP EMAIL ADDRESS PASSWORD", "THIS SOFTWARE USES THE BACKUP EMAIL TO SEND EMAILS TO THE DEVELOPERS.ADD YOUR BACKUP EMAIL ANS SET ITS PASSWORD IN SETTINGS");
                submit.setDisable(true);

            }
            if (resultSet.getString("backupemail") != null && !
                    resultSet.getString("backupemail").isEmpty() &&
                    resultSet.getString("backupemailPassword") != null &&
                    !resultSet.getString("backupemailPassword").isEmpty()) {
                fromEmail = resultSet.getString("backupemail");
                fromPassword = resultSet.getString("backupemailPassword");
            }


        }

        submit.setOnAction(event -> {
            String nameStr = name.getText();
            String desc = description.getText();
            String sub = subject;
            if (sub.isEmpty() || desc.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "ALL REQUIRED FIELDS SHOULD BE FILLED");
            } else {
                try {
                    mailSend(desc, sub, adminEmail, fromEmail, "text/plain", AesCrypto.decrypt(encryptionkey, fromPassword));
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "MESSAGE HAS BEEN SENT TO THE ADMIN");
                } catch (MessagingException e) {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "INVALID EMAIL OR PASSWORD", "THE EMAIL YOU PROVIDED COULD NOT BE USED.CHECK IF IT HAS THE CORRECT PASSWORD OR IF IT EXISTS");

                }

            }
        });
    }

    private void menuClick() {
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
        menulogout.setOnAction(event -> logout(panel));

    }

}
