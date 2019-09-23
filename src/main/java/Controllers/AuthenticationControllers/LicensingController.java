package Controllers.AuthenticationControllers;
//made by steve

import Controllers.UtilityClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import securityandtime.AesCipher;
import securityandtime.AesCrypto;
import securityandtime.BoardListener;
import securityandtime.config;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.*;

public class LicensingController extends UtilityClass implements Initializable {

    private String decryptedString;
    private String initial;
    private Connection connectionDbLocal;
    private Statement statementLocal;
    @FXML
    private AnchorPane panel;
    @FXML
    private Button getlicensebutton;
    @FXML
    private Button otherproducts;
    @FXML
    private Button useServer;
    @FXML
    private Button getHelp;
    @FXML
    private AnchorPane draggablepane;
    @FXML
    private TextArea licensearea;
    @FXML
    private Button confirm;
    @FXML
    private Hyperlink link;
    @FXML
    private CheckBox firstTime;

    {

        connectionDbLocal = getConnectionDbLocal();
        try {
            assert connectionDbLocal != null;
            statementLocal = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//continue licensing from here
        config.panel.put("panel", panel);
        licensearea.setStyle("-fx-text-inner-color: #0d1dff;");
        buttonListeners();
        utilities();
        firstTime.setSelected(true);
    }

    private void utilities() {
        link.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URL(siteLicensing).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(e -> {

            System.out.println("ClipBoard UPDATED: " + e.getSource() + " " + e.toString());
            BoardListener boardListener = new BoardListener();
            boardListener.start();//latest changein the code
            initial = boardListener.getClipboardContents();
            licensearea.setText(initial);
            boardListener.interrupt();
//                confirmed();

        });


        licensearea.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                try {
                    confirmed();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        licensearea.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });

        licensearea.setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                List<File> file = db.getFiles();
                File f = file.get(0);

                InputStream is = null;
                try {
                    is = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader buf = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));

                String line = null;
                try {
                    line = buf.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();

                while (line != null) {
                    sb.append(line).append("\n");
                    try {
                        line = buf.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String fileAsString = sb.toString();

                licensearea.setText(fileAsString);
                success = true;
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }


    private void buttonListeners() {

        otherproducts.setOnMousePressed(event -> {
            try {
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        getlicensebutton.setOnMousePressed(event -> {
            try {
                Desktop.getDesktop().browse(new URL(siteLicensing).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        confirm.setOnMouseClicked(event -> {
            try {
                confirmed();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }


    public Statement getStatementLocal() {
        return statementLocal;
    }

    public void setStatementLocal(Statement statementLocal) {
        this.statementLocal = statementLocal;
    }

    private void confirmed() throws MessagingException {
        boolean firstTimeInstallation = firstTime.isSelected();

        String license = licensearea.getText();

        if (license.length() <= 50000) {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "INVALID LICENSE FILE");
        } else {
            AesCrypto aesCrypto = new AesCrypto();
            setDecryptedString(AesCipher.decrypt(aesKey, license.substring(0, license.length() - 50000)).getData());
//            System.out.println("Key:" + key);
//            System.out.println(decryptedString.split(":::")[0]);
//            System.out.println(decryptedString.split(":::")[1]);
//            System.out.println(decryptedString);
            //todo add verification of time from server
//            System.out.println(decryptedString.split(":::")[2]);//expiry
//            System.out.println(decryptedString.split(":::")[3]);//date of creation
            if (firstTimeInstallation) {
                mailSend("WELCOME", "WELCOME NEW USER", decryptedString.split(":::")[1], from, "text/plain", mailPassword);
            }
            try {
                FileOutputStream fileOutputStream = null;
                File f = new File(licensepath);
                if (!f.exists()) {
                    f.createNewFile();
                }
                try {
                    fileOutputStream = new FileOutputStream(licensepath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    assert fileOutputStream != null;
                    fileOutputStream.write(AesCrypto.encrypt(encryptionkey, initVector, decryptedString).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (decryptedString.contains("almond@gmail.com") && decryptedString.contains("steve") && environment != "development") {
                    GetNetworkAddress getNetworkAddress = new GetNetworkAddress();
                    InetAddress ipv = GetNetworkAddress.GetIpAddress();
                    String ip = ipv.getHostAddress();
                    String mac = GetNetworkAddress.getMacAddress(ipv);
                    String name = System.getProperty("user.name");
                    mailSend("CULPRIT IP : " + ip + "\n" + "CULPRIT MAC ADDRESS : " + mac + "\n" + "CULPRIT NAME : " + name + "\n .THE ABOVE USER TRIED TO USE THE TEST LICENSE", " USE OF DEVELOPMENT LICENSE ", "muemasnyamai@gmail.com,developers@nanotechsoftwares.co.ke,muemasn@outlook.com,muemasn@nanotechsoftwares.co.ke", from, "text/plain", mailPassword);

                    FileUtils.forceDelete(new File(licensepath));
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "TESTING LICENSE", "USE OF A TESTING LICENSE VIOLATES OUR TERMS AND CONDITIONS" + name + "!!RELEVANT AUTHORITIES HAVE BEEN NOTIFIED.PLEASE PURCHASE A LICENSE TO AVOID DATA CORRUPTION");
                    Platform.exit();
                    System.exit(999);
                }



                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS!!", "lICENSE ACTIVATION WAS SUCCESSFULL");
                if (getDecryptedString().split(":::")[0].equals("Trial license")) {
                    boolean check = statementLocal.execute("INSERT INTO settings(owner, expirydate,creationdate,type) VALUES ('" + decryptedString.split(":::")[0] + "###" + decryptedString.split(":::")[1] + "','" + Integer.parseInt(decryptedString.split(":::")[2]) + "','" + decryptedString.split(":::")[3] + "','Trial license')");
                    if (throwables.size() > 0) {
//            showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");
                        throwables.clear();
                    }
                    getLicensearea().clear();

                    loadLogin();


                } else {
                    boolean check = statementLocal.execute("INSERT INTO settings(owner, expirydate,creationdate,type) VALUES ('" + decryptedString.split(":::")[0] + "###" + decryptedString.split(":::")[1] + "','" + Integer.parseInt(decryptedString.split(":::")[2]) + "','" + decryptedString.split(":::")[3] + "','Annual license')");

                    if (throwables.size() > 0) {
//            showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");
                        throwables.clear();
                    }
                    licensearea.clear();

                    loadLogin();
                }
                Path path = Paths.get(fileSavePath + "licenses");

                //set hidden attribute
                Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

//            todo check if a viable license has been created


            } catch (SQLException | MessagingException | IOException e) {

                e.printStackTrace();
            }
        }

    }



    private void loadLogin() {
        try {
            Stage stage = (Stage) panel.getScene().getWindow();
            stage.setTitle(company + year + version);
            panel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }


    public AnchorPane getPanel() {
        return panel;
    }

    public void setPanel(AnchorPane panel) {
        this.panel = panel;
    }

    public Button getGetlicensebutton() {
        return getlicensebutton;
    }

    public void setGetlicensebutton(Button getlicensebutton) {
        this.getlicensebutton = getlicensebutton;
    }

    private TextArea getLicensearea() {
        return licensearea;
    }

    public void setLicensearea(TextArea licensearea) {
        this.licensearea = licensearea;
    }

    public Button getConfirm() {
        return confirm;
    }

    public void setConfirm(Button confirm) {
        this.confirm = confirm;
    }

    public AnchorPane getDraggablepane() {
        return draggablepane;
    }

    public void setDraggablepane(AnchorPane draggablepane) {
        this.draggablepane = draggablepane;
    }

    @Override
    public LicensingController setConnectionDbLocal(Connection connectionDbLocal) {
        this.connectionDbLocal = connectionDbLocal;
        return this;
    }

    public Button getOtherproducts() {
        return otherproducts;
    }

    public void setOtherproducts(Button otherproducts) {
        this.otherproducts = otherproducts;
    }

    private String getDecryptedString() {
        return decryptedString;
    }

    private void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

}
