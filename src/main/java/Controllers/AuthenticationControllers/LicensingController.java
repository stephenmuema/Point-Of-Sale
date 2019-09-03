package Controllers.AuthenticationControllers;

import Controllers.UtilityClass;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import securityandtime.AesCipher;
import securityandtime.BoardListener;

import java.awt.*;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.*;

public class LicensingController extends UtilityClass implements Initializable {
    public AnchorPane panel;
    public Button getlicensebutton;
    public TextArea licensearea;
    public Button confirm;
    public AnchorPane draggablepane;
    public Button otherproducts;
    public Hyperlink link;
    private String decryptedString;
    private String initial;
    private Connection connectionDbLocal;
    private Statement statementLocal;

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
        licensearea.setStyle("-fx-text-inner-color: #0d1dff;");
        buttonListeners();
        utilities();

//        radioGroupManager();
    }

//    private void radioGroupManager(){
//        ToggleGroup group=new ToggleGroup();
//        radioactivate.setToggleGroup(group);
//        radioactivate.setSelected(true);
//        radiotrial.setToggleGroup(group);
//        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            @Override
//            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
//                if (group.getSelectedToggle() != null) {
//                    RadioButton button = (RadioButton) group.getSelectedToggle();
//                    System.out.println("Button: " + button.getText());
////                labelInfo.setText("You are " + button.getText());
//                }
//            }
//        });
//
//    }


    private void utilities() {
        link.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URL(siteLicensing).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent e) {

                System.out.println("ClipBoard UPDATED: " + e.getSource() + " " + e.toString());
                BoardListener boardListener = new BoardListener();
                boardListener.start();//latest changein the code
                initial = boardListener.getClipboardContents();
                licensearea.setText(initial);
//                confirmed();

            }
        });


        licensearea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode.equals(KeyCode.ENTER)) {
                    confirmed();
                }
            }
        });
        licensearea.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            }
        });

        licensearea.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
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
//                    confirmed();
                    success = true;
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
    }


    private void buttonListeners() {

        otherproducts.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL("http://localhost/licensing/").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        getlicensebutton.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL("http://localhost/licensing/").toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                confirmed();

            }
        });
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

    public TextArea getLicensearea() {
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

    public String getDecryptedString() {
        return decryptedString;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

//    public Connection getConnectionDbLocal() {
//        return connectionDbLocal;
//    }


    public Statement getStatementLocal() {
        return statementLocal;
    }

    public void setStatementLocal(Statement statementLocal) {
        this.statementLocal = statementLocal;
    }

    private void confirmed() {
        String license = licensearea.getText();
        if (license.length() <= 50000) {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "INVALID LICENSE FILE");
        } else {
            String key = "26kozQaKwRuNJ24t26kozQaKwRuNJ24t";
            setDecryptedString(AesCipher.decrypt(key, license.substring(0, license.length() - 50000)).getData());
            System.out.println("Key:" + key);
            try {
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(licensepath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    assert fileOutputStream != null;
                    fileOutputStream.write(decryptedString.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS!!", "lICENSE ACTIVATION WAS SUCCESSFULL");
                if (getDecryptedString().split(":::")[0].equals("Trial license")) {
                    boolean check = statementLocal.execute("INSERT INTO settings(owner, expirydate,creationdate,type) VALUES ('" + decryptedString.split(":::")[0] + "###" + decryptedString.split(":::")[1] + "','" + Integer.parseInt(decryptedString.split(":::")[2]) + "','" + decryptedString.split(":::")[3] + "','Trial license')");
                    if (throwables.size() > 0) {
//            showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");
                        throwables.clear();
                    }
                    getLicensearea().clear();
                    //System.out.println(decryptedString.split(":::")[0]);
                    //System.out.println(decryptedString.split(":::")[1]);
                    //System.out.println(decryptedString.split(":::")[2]);//expiry
                    //System.out.println(decryptedString.split(":::")[3]);
                    Platform.exit();
                    System.exit(1);

                } else {
                    boolean check = statementLocal.execute("INSERT INTO settings(owner, expirydate,creationdate,type) VALUES ('" + decryptedString.split(":::")[0] + "###" + decryptedString.split(":::")[1] + "','" + Integer.parseInt(decryptedString.split(":::")[2]) + "','" + decryptedString.split(":::")[3] + "','Annual license')");

                    if (throwables.size() > 0) {
//            showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");
                        throwables.clear();
                    }
                    licensearea.clear();
                    //System.out.println(decryptedString.split(":::")[0]);
                    //System.out.println(decryptedString.split(":::")[1]);
                    //System.out.println(decryptedString.split(":::")[2]);//expiry
                    //System.out.println(decryptedString.split(":::")[3]);
                    Platform.exit();
                    System.exit(1);
                }

//            todo check if a viable license has been created
                //if(check){
//    showAlert(Alert.AlertType.INFORMATION,panel.getScene().getWindow(),"SUCCESS","LICENSE REGISTRATION WAS SUCCESSFULL.RESTART THE APPLICATION FOR IT TO TAKE EFFECT");
//
//
//}
//else {
//    showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");
//
//}

            } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"ERROR","PLEASE CHECK YOUR LICENSE");

                e.printStackTrace();
            }
        }

    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }



}
