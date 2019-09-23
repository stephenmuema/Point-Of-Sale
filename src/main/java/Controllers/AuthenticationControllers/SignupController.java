package Controllers.AuthenticationControllers;

import Controllers.UtilityClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logging.LogClass;
import securityandtime.Security;
import securityandtime.config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.site;

//made by steve muema
//end of imports
public class SignupController extends UtilityClass implements Initializable {
    public Label clock;
    @FXML
    private PasswordField password, passwordconfirmation;
    @FXML
    private TextField email, name, IDNUMBER;
    @FXML
    private Hyperlink link;
    @FXML
    private Button login, signup;
    @FXML
    private AnchorPane panel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
//
        config.panel.put("panel", panel);

        timeMain(clock);
        link.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        listenEnter();
        login.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                panel.getChildren().removeAll();
                try {
                    panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        signup.setOnMousePressed(event -> registerUser());
    }

    private void listenEnter() {
        password.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        passwordconfirmation.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        email.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        name.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        IDNUMBER.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        if (password.getText().isEmpty() ||
                passwordconfirmation.getText().isEmpty() ||
                email.getText().isEmpty() || name.getText().isEmpty()
                || IDNUMBER.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                    "Fill all the fields", "Please fill all the fields");
        } else {
            if (!password.getText().equals(passwordconfirmation.getText())) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                        "Your passwords don't match", "Please enter matching passwords");
            } else {
//                Connection snm = connectiondb.getConnect();
                try {
                    try {
                        insertion(email, name, IDNUMBER, password, passwordconfirmation);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                } catch (SQLException e) {
                    LogClass.getLogger().log(Level.SEVERE, " Registration not successful");
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                            "Transaction unsuccessfull!!", "Registration not successful");
                    e.printStackTrace();
                }

            }
        }
    }

    private void insertion(TextField email, TextField name, TextField IDNUMBER, PasswordField password, PasswordField passwordconfirmation) throws SQLException, NoSuchAlgorithmException {
        String str = email.getText() + name.getText() + password.getText() + IDNUMBER.getText() + new java.util.Date().toString();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(str.getBytes(), 0, str.length());
        String hash = new BigInteger(1, messageDigest.digest()).toString(16);
        System.out.println("MD5: " + hash);
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        PreparedStatement statementemail = connection.prepareStatement("SELECT * FROM users WHERE email=? ");
        statementemail.setString(1, email.getText());
        ResultSet resultSetemail = statementemail.executeQuery();
        if (resultSetemail.isBeforeFirst()) {
            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                    "EMAIL IN USE", "EMAIL IS IN USE");
            LogClass.getLogger().log(Level.SEVERE, " EMAIL IS IN USE");

        } else {

            PreparedStatement statementname = connection.prepareStatement("SELECT * FROM users WHERE employeename=? ");
            statementname.setString(1, name.getText());
            ResultSet resultSetname = statementname.executeQuery();
            if (resultSetname.isBeforeFirst()) {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                        "NAME IN USE", "NAME IS IN USE");
                LogClass.getLogger().log(Level.SEVERE, " NAME IS IN USE");

            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(employeename,email,password, employeeid,activated,hash,admin,status)VALUES(?,?,?,?,?,?,?,?)");

                preparedStatement.setString(1, name.getText());
                preparedStatement.setString(2, email.getText());
                preparedStatement.setString(3, Security.hashPassword(password.getText()));
                preparedStatement.setString(4, IDNUMBER.getText());
                preparedStatement.setBoolean(5, true);
                preparedStatement.setString(6, hash);
                if (name.getText().equalsIgnoreCase("admin")) {
                    preparedStatement.setBoolean(7, true);
                    preparedStatement.setString(8, "active");

                } else {
                    preparedStatement.setBoolean(7, false);
                    preparedStatement.setString(8, "pending");


                }
                if (preparedStatement.executeUpdate() > 0) {
                    LogClass.getLogger().log(Level.CONFIG, " Registration successful");
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                            "Transaction successfull!!", "Registration  successful");
                    panel.getChildren().removeAll();
                    try {
                        panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogClass.getLogger().log(Level.SEVERE, " Registration not successful");
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                            "Transaction unsuccessfull!!", "Registration not successful");
                }

            }
        }

    }


    public Label getClock() {
        return clock;
    }

    public SignupController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public PasswordField getPassword() {
        return password;
    }

    public SignupController setPassword(PasswordField password) {
        this.password = password;
        return this;
    }

    public PasswordField getPasswordconfirmation() {
        return passwordconfirmation;
    }

    public SignupController setPasswordconfirmation(PasswordField passwordconfirmation) {
        this.passwordconfirmation = passwordconfirmation;
        return this;
    }

    public TextField getEmail() {
        return email;
    }

    public SignupController setEmail(TextField email) {
        this.email = email;
        return this;
    }

    public TextField getName() {
        return name;
    }

    public SignupController setName(TextField name) {
        this.name = name;
        return this;
    }

    public TextField getIDNUMBER() {
        return IDNUMBER;
    }

    public SignupController setIDNUMBER(TextField IDNUMBER) {
        this.IDNUMBER = IDNUMBER;
        return this;
    }


    public Button getLogin() {
        return login;
    }

    public Hyperlink getLink() {
        return link;
    }

    public SignupController setLink(Hyperlink link) {
        this.link = link;
        return this;
    }

    public SignupController setLogin(Button login) {
        this.login = login;
        return this;
    }

    public Button getSignup() {
        return signup;
    }

    public SignupController setSignup(Button signup) {
        this.signup = signup;
        return this;
    }

    public AnchorPane getParent() {
        return panel;
    }

    class SendMail {


        public SendMail() {
            final String username = "muemasn@outlook.com";  // like yourname@outlook.com
            final String password = "Adm10735";   // password here

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            session.setDebug(true);

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email.getText()));   // like inzi769@gmail.com
                message.setSubject("NANOTECH POS ACCOUNT ACTIVATION");
                message.setContent("<html><head></head><body>" +
                        "<P>" +
                        "</P>" +
                        "</body></html>", "text/html");

                Transport.send(message);

                System.out.println("Done");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public SignupController setParent(AnchorPane parent) {
        this.panel = parent;
        return this;
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }


}

