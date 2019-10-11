package Controllers.AuthenticationControllers;

import Controllers.SuperClass;
import Controllers.UtilityClass;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import logging.LogClass;
import securityandtime.AesCrypto;
import securityandtime.Security;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.*;

//end of imports
//made by steve
public class LoginController extends UtilityClass implements Initializable {

    public Button refresh;
    public TextField shiftid;
    @FXML
    private Label clock;
    private String emailSubmit, pass;
    @FXML
    private MenuItem menuQuit;
    @FXML
    private MenuItem abtMenu;
    @FXML
    private MenuItem termsMenu;
    @FXML
    private MenuItem checkUpdatesMenu;
    @FXML
    private MenuItem documentationMenu;
    @FXML
    private MenuItem reachUsMenu;
    @FXML
    private MenuItem menuShutDown;
    @FXML
    private MenuItem menuRestart;
    @FXML
    Hyperlink link;
    @FXML
    Button login, signup;
    @FXML
    PasswordField password;
    @FXML
    TextField email;
    @FXML
    private
    ImageView imageView;
    @FXML
    private
    Button exit;
    @FXML
    private AnchorPane panel;
    @FXML
    private Label message;
    private Connection connection = getConnection();
    private ResultSet resultSet;
    private String shiftNbr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (throwables.containsKey("INVALID KEY")) {
//            showAlert(Alert.AlertType.ERROR,);
        }
        main();
    }

    private void main() {
        menuClick();
        if (config.login.containsKey("loggedout")) {
            message.setText("YOU ARE LOGGED OUT ");
//            destroy session variables
            config.user.clear();
            config.login.clear();
        } else {
            message.setText("SIGN INTO YOUR ACCOUNT");

        }
//        go to nanotechsoftwares website
        link.setOnMousePressed(event -> {
            try {
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        buttonClick();
        timeMain(clock);
        enterpressed();
        shiftSet();
    }

    private void shiftSet() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DAYS WHERE completed=?");
            preparedStatement.setString(1, "incomplete");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    shiftNbr = resultSet.getString("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (shiftNbr == null) {
            shiftid.setText("null");

        } else {
            shiftid.setText(shiftNbr);
        }
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
    }


    private void enterpressed() {
        email.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });
        password.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });
    }

    private void buttonClick() {
        refresh.setOnAction(__ -> {
            panel.getChildren().removeAll();
            try {
                panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        exit.setOnAction(event -> exit());
        signup.setOnMousePressed(new EventHandler<MouseEvent>() {
            //            got to sign up page
            @Override
            public void handle(MouseEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI here.
                        panel.getChildren().removeAll();
                        try {
                            panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/signup.fxml"))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        login.setOnMousePressed(event -> {
//            login and check if fields are empty
            UtilityClass utilityClass = new UtilityClass();
            connection = utilityClass.getConnection();
            loginValidation();
        });
    }

    private void loginValidation() {
        if (email.getText().isEmpty() || password.getText().isEmpty()) {
            LogClass.getLogger().log(Level.SEVERE, " PLEASE FILL ALL FIELDS");
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {
            login();

        }
    }

    //login method
    private void login() {
//        get input text
        emailSubmit = email.getText();
        pass = password.getText();
        try {
//            create a connection
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=? OR employeename=?");
            statement.setString(1, emailSubmit);
            statement.setString(2, emailSubmit);
            resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    if (!resultSet.getString("status").equalsIgnoreCase("active")) {
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                                "YUR ACCOUNT IS SUSPENDED", "PLEASE INFORM the ADMINISTRATOR TO ACTIVATE YOUR ACCOUNT");

                    } else {
                        if (resultSet.getBoolean("activated")) {
                            //if account exists and password matches hashed password
                            if ((resultSet.getString("password").equals(Security.hashPassword(pass)))) {
                                if (resultSet.getBoolean("admin")) {
//
//if user account is admin
//                                    parent.getChildren().removeAll();
                                    try {
//                                    go to admin panel
                                        panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml"))));
                                        assert false;
//                                    work as sessions and hold user session data
                                        config.login.put("loggedinasadmin", true);
                                        config.user.put("userName", resultSet.getString("employeename"));

                                        config.user.put("user", resultSet.getString("email"));
                                        config.key.put("key", resultSet.getString("subscribername"));
                                        config.user.put("backupemail", resultSet.getString("backupemail"));
                                        config.user.put("backupemailpassword", AesCrypto.decrypt(encryptionkey, resultSet.getString("backupemailpassword")));

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
//                                user is not admin go to normal panel

                                    Connection connection = new SuperClass().getConnection();
                                    Statement statements = connection.createStatement();
                                    ResultSet res = statements.executeQuery("SELECT * FROM DAYS WHERE end_time IS NULL ORDER BY id DESC LIMIT 1");
                                    if (!res.isBeforeFirst()) {
//make everything not clickable
                                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "INACTIVE SYSTEM", "YOU HAVE AN INACTIVE SYSTEM!!THE ADMIN SHOULD START A DAY FOR ANY CASHIERS TO LOGIN");

                                        res.close();
                                        statements.close();
                                    } else {
                                        panel.getChildren().removeAll();
                                        try {
                                            panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml"))));
                                            assert false;
                                            //                                    work as sessions and hold user session data
                                            config.user.put("userName", resultSet.getString("employeename"));

                                            config.user.put("user", resultSet.getString("email"));
                                            config.login.put("loggedinasemployee", true);

//                                    new ShopController().setTransID(String.valueOf(new Random().nextGaussian()));
                                            //                                    create a new transaction id for local sqlite cart

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
//                            if passwords do not match
                                LogClass.getLogger().log(Level.SEVERE, " passwords do not match");
                                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                                        "WRONG PASSWORD!!", "ENTER THE CORRECT PASSWORD");

                            }
                        } else {
                            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                                    "Activate your license/account !!", "PLEASE ACTIVATE YOUR ACCOUNT OR INFORM THE EMPLOYER TO RENEW THE LICENSE");

                        }
                    }
                }

            } else {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                        "WRONG NAME/EMAIL !!", "PLEASE RE-ENTER A VALID USER NAME OR EMAIL");
//                LogClass.getLogger().log(Level.SEVERE, " LOGIN ERROR");
//name or email does not exist
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "CONNECTION ERROR", "CHECK YOUR CONNECTION TO THE SERVER");
            message.setText("CHECK YOUR CONNECTION!!");
            message.setTextFill(Paint.valueOf("RED"));
        }
        try {
            resultSet.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    //


    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public String getEmailSubmit() {
        return emailSubmit;
    }

    public void setEmailSubmit(String emailSubmit) {
        this.emailSubmit = emailSubmit;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Hyperlink getLink() {
        return link;
    }

    public void setLink(Hyperlink link) {
        this.link = link;
    }

    public Button getLogin() {
        return login;
    }

    public void setLogin(Button login) {
        this.login = login;
    }

    public Button getSignup() {
        return signup;
    }

    public void setSignup(Button signup) {
        this.signup = signup;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public AnchorPane getParent() {
        return panel;
    }

    public void setParent(AnchorPane parent) {
        this.panel = parent;
    }

    public Label getMessage() {
        return message;
    }

    public void setMessage(Label message) {
        this.message = message;
    }

}
