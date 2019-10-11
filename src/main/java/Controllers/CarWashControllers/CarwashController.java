package Controllers.CarWashControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.CarWashMaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static securityandtime.config.host;
import static securityandtime.config.user;

public class CarwashController extends UtilityClass implements Initializable {
    @FXML
    public Label clock;
    @FXML
    public Button delete;
    @FXML
    public Button sendtomail;
    @FXML
    public Button printoutaspdf;

    public Button home;
    @FXML
    public Label cash;
    @FXML
    public Tab pastclients;
    @FXML
    public TableColumn<CarWashMaster, String> Name;
    @FXML
    public TableColumn<CarWashMaster, String> reg;
    @FXML
    public TableColumn<CarWashMaster, String> id;
    @FXML
    public TableColumn<CarWashMaster, String> status;
    @FXML
    public TableColumn<CarWashMaster, String> operator;
    @FXML
    public TableColumn<CarWashMaster, String> payout;


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
    private int moneyPaid;
    @FXML
    private TableView<CarWashMaster> tab;
    @FXML
    private AnchorPane panel;
    private ObservableList<CarWashMaster> data;
    private String time;

    /**
     * made by steve
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config.panel.put("panel", panel);

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
        time(clock);
        menuclick();
        buttonclick();
        editable();
        cash.setText(String.valueOf(moneyPaid));
    }

    private void editable() {
        tab.setEditable(true);
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();

        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set `ownername`=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        reg.setCellFactory(TextFieldTableCell.forTableColumn());
        reg.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set registration=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set idnumber=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            assert connection != null;
                            preparedStatement = connection.prepareStatement("UPDATE carwash set status=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        operator.setCellFactory(TextFieldTableCell.forTableColumn());
        operator.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set washedby=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );

        payout.setCellFactory(TextFieldTableCell.forTableColumn());
        payout.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            UtilityClass utilityClass = new UtilityClass();
                            Connection connection = utilityClass.getConnection();
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set cashpaid=? where id=?");
                            preparedStatement.setString(1, newval.toUpperCase());
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
    }

    private void buttonclick() {
        data = FXCollections.observableArrayList();

        home.setOnMouseClicked(event -> {
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//        todo delete
            }
        });
        pastclients.setOnSelectionChanged(event -> {
            data = FXCollections.observableArrayList();
            if (pastclients.isSelected()) {
                populateTable();
            }
        });
        sendtomail.setOnMousePressed(event -> {
//         send audit to email
            generateAudit();
            SendEmail sendEmail = new SendEmail(user.get("user"), "nanotechsoftwarespos@nanotechsoftwares.co.ke", host, time);
            //System.out.println(user.get("user"));
        });
        printoutaspdf.setOnMousePressed(event -> {
            generateAudit();
//         export audit as pdf
        });
    }

    private void generateAudit() {
        pdfGen();
        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "OPERATION SUCCESSFUL", "your pdf was generated successfully");
    }


    private void pdfGen() {

    }


    private void populateTable() {
        data = FXCollections.observableArrayList();
        Connection connection = getConnection();
        try {
//                        DISPLAYING CLIENTS
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM carwash where status=?");
                statement.setString(1, "COMPLETED");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    CarWashMaster carWashMaster1 = new CarWashMaster();
                    System.out.println("name is" + resultSet.getString("ownername"));
                    carWashMaster1.Name.set(resultSet.getString("ownername"));
                    carWashMaster1.regno.set(resultSet.getString("registration"));
                    carWashMaster1.Id.set(resultSet.getString("id"));
                    carWashMaster1.idnum.set(resultSet.getString("idnumber"));
                    carWashMaster1.status.set(resultSet.getString("status"));
                    carWashMaster1.operator.set(resultSet.getString("washedby"));
                    carWashMaster1.cash.set(resultSet.getString("cashpaid"));
                    carWashMaster1.setContact(resultSet.getString("contact"));
//                    int tempmoneyPaid = 0;
//                    tempmoneyPaid += Integer.parseInt(resultSet.getString("cashpaid"));
//                    moneyPaid = tempmoneyPaid;
                    data.add(carWashMaster1);
                }
                tab.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
        Name.setCellValueFactory(
                new PropertyValueFactory<>("Name"));
        id.setCellValueFactory(
                new PropertyValueFactory<>("idnum"));
        reg.setCellValueFactory(new PropertyValueFactory<>("regno"));
        status.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        operator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        payout.setCellValueFactory(new PropertyValueFactory<>("cash"));

        tab.refresh();
    }


    private void menuclick() {
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
    }

    public AnchorPane getParents() {
        return panel;
    }

    public CarwashController setParents(AnchorPane parents) {
        this.panel = parents;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public CarwashController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public Button getDelete() {
        return delete;
    }

    public CarwashController setDelete(Button delete) {
        this.delete = delete;
        return this;
    }

    public Button getSendtomail() {
        return sendtomail;
    }

    public CarwashController setSendtomail(Button sendtomail) {
        this.sendtomail = sendtomail;
        return this;
    }

    public Button getPrintoutaspdf() {
        return printoutaspdf;
    }

    public CarwashController setPrintoutaspdf(Button printoutaspdf) {
        this.printoutaspdf = printoutaspdf;
        return this;
    }

    public Button getHome() {
        return home;
    }

    public CarwashController setHome(Button home) {
        this.home = home;
        return this;
    }

    public Label getCash() {
        return cash;
    }

    public CarwashController setCash(Label cash) {
        this.cash = cash;
        return this;
    }

    public Tab getPastclients() {
        return pastclients;
    }

    public CarwashController setPastclients(Tab pastclients) {
        this.pastclients = pastclients;
        return this;
    }

    public TableColumn<CarWashMaster, String> getName() {
        return Name;
    }

    public CarwashController setName(TableColumn<CarWashMaster, String> name) {
        Name = name;
        return this;
    }

    public TableColumn<CarWashMaster, String> getReg() {
        return reg;
    }

    public CarwashController setReg(TableColumn<CarWashMaster, String> reg) {
        this.reg = reg;
        return this;
    }

    public TableColumn<CarWashMaster, String> getId() {
        return id;
    }

    public CarwashController setId(TableColumn<CarWashMaster, String> id) {
        this.id = id;
        return this;
    }

    public TableColumn<CarWashMaster, String> getStatus() {
        return status;
    }

    public CarwashController setStatus(TableColumn<CarWashMaster, String> status) {
        this.status = status;
        return this;
    }

    public TableColumn<CarWashMaster, String> getOperator() {
        return operator;
    }

    public CarwashController setOperator(TableColumn<CarWashMaster, String> operator) {
        this.operator = operator;
        return this;
    }

    public TableColumn<CarWashMaster, String> getPayout() {
        return payout;
    }

    public CarwashController setPayout(TableColumn<CarWashMaster, String> payout) {
        this.payout = payout;
        return this;
    }

    public MenuItem getHelpMenu() {
        return helpMenu;
    }

    public CarwashController setHelpMenu(Menu helpMenu) {
        this.helpMenu = helpMenu;
        return this;
    }

    public int getMoneyPaid() {
        return moneyPaid;
    }

    public CarwashController setMoneyPaid(int moneyPaid) {
        this.moneyPaid = moneyPaid;
        return this;
    }

    public TableView<CarWashMaster> getTab() {
        return tab;
    }

    public CarwashController setTab(TableView<CarWashMaster> tab) {
        this.tab = tab;
        return this;
    }

    public ObservableList<CarWashMaster> getData() {
        return data;
    }

    public CarwashController setData(ObservableList<CarWashMaster> data) {
        this.data = data;
        return this;
    }

    public String getTime() {
        return time;
    }

    public CarwashController setTime(String time) {
        this.time = time;
        return this;
    }

    public MenuItem getDetails() {
        return details;
    }

    public CarwashController setDetails(MenuItem details) {
        this.details = details;
        return this;
    }

    public MenuItem getMenulogout() {
        return menulogout;
    }

    public CarwashController setMenulogout(MenuItem menulogout) {
        this.menulogout = menulogout;
        return this;
    }

    public MenuItem getLicense() {
        return license;
    }

    public CarwashController setLicense(MenuItem license) {
        this.license = license;
        return this;
    }

    public MenuItem getBackupMenu() {
        return backupMenu;
    }

    public CarwashController setBackupMenu(MenuItem backupMenu) {
        this.backupMenu = backupMenu;
        return this;
    }

    public MenuItem getStartDayMenu() {
        return startDayMenu;
    }

    public CarwashController setStartDayMenu(MenuItem startDayMenu) {
        this.startDayMenu = startDayMenu;
        return this;
    }

    public MenuItem getEndDayMenu() {
        return endDayMenu;
    }

    public CarwashController setEndDayMenu(MenuItem endDayMenu) {
        this.endDayMenu = endDayMenu;
        return this;
    }

    public MenuItem getReportIssuesMenu() {
        return reportIssuesMenu;
    }

    public CarwashController setReportIssuesMenu(MenuItem reportIssuesMenu) {
        this.reportIssuesMenu = reportIssuesMenu;
        return this;
    }

    public MenuItem getRestartServerMenu() {
        return restartServerMenu;
    }

    public CarwashController setRestartServerMenu(MenuItem restartServerMenu) {
        this.restartServerMenu = restartServerMenu;
        return this;
    }

    public MenuItem getTroubleShootMenu() {
        return troubleShootMenu;
    }

    public CarwashController setTroubleShootMenu(MenuItem troubleShootMenu) {
        this.troubleShootMenu = troubleShootMenu;
        return this;
    }

    public MenuItem getAbtMenu() {
        return abtMenu;
    }

    public CarwashController setAbtMenu(MenuItem abtMenu) {
        this.abtMenu = abtMenu;
        return this;
    }

    public MenuItem getTermsMenu() {
        return termsMenu;
    }

    public CarwashController setTermsMenu(MenuItem termsMenu) {
        this.termsMenu = termsMenu;
        return this;
    }

    public MenuItem getCheckUpdatesMenu() {
        return checkUpdatesMenu;
    }

    public CarwashController setCheckUpdatesMenu(MenuItem checkUpdatesMenu) {
        this.checkUpdatesMenu = checkUpdatesMenu;
        return this;
    }

    public MenuItem getReachUsMenu() {
        return reachUsMenu;
    }

    public CarwashController setReachUsMenu(MenuItem reachUsMenu) {
        this.reachUsMenu = reachUsMenu;
        return this;
    }

    public MenuItem getGenerateReportsMenu() {
        return generateReportsMenu;
    }

    public CarwashController setGenerateReportsMenu(MenuItem generateReportsMenu) {
        this.generateReportsMenu = generateReportsMenu;
        return this;
    }

    public MenuItem getDocumentationMenu() {
        return documentationMenu;
    }

    public CarwashController setDocumentationMenu(MenuItem documentationMenu) {
        this.documentationMenu = documentationMenu;
        return this;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }

    public CarwashController setMenuQuit(MenuItem menuQuit) {
        this.menuQuit = menuQuit;
        return this;
    }

    public MenuItem getViewBackupsMenu() {
        return viewBackupsMenu;
    }

    public CarwashController setViewBackupsMenu(MenuItem viewBackupsMenu) {
        this.viewBackupsMenu = viewBackupsMenu;
        return this;
    }

    public MenuItem getRetrieveBackupMenu() {
        return retrieveBackupMenu;
    }

    public CarwashController setRetrieveBackupMenu(MenuItem retrieveBackupMenu) {
        this.retrieveBackupMenu = retrieveBackupMenu;
        return this;
    }

    public MenuItem getStaffMenu() {
        return staffMenu;
    }

    public CarwashController setStaffMenu(MenuItem staffMenu) {
        this.staffMenu = staffMenu;
        return this;
    }

    public MenuItem getCarWashMenu() {
        return carWashMenu;
    }

    public CarwashController setCarWashMenu(MenuItem carWashMenu) {
        this.carWashMenu = carWashMenu;
        return this;
    }

    public MenuItem getInventoryMenu() {
        return inventoryMenu;
    }

    public CarwashController setInventoryMenu(MenuItem inventoryMenu) {
        this.inventoryMenu = inventoryMenu;
        return this;
    }

    public MenuItem getMrMenu() {
        return mrMenu;
    }

    public CarwashController setMrMenu(MenuItem mrMenu) {
        this.mrMenu = mrMenu;
        return this;
    }

    public MenuItem getAuditsMenu() {
        return auditsMenu;
    }

    public CarwashController setAuditsMenu(MenuItem auditsMenu) {
        this.auditsMenu = auditsMenu;
        return this;
    }

    public MenuItem getMenuShutDown() {
        return menuShutDown;
    }

    public CarwashController setMenuShutDown(MenuItem menuShutDown) {
        this.menuShutDown = menuShutDown;
        return this;
    }

    public MenuItem getMenuRestart() {
        return menuRestart;
    }

    public CarwashController setMenuRestart(MenuItem menuRestart) {
        this.menuRestart = menuRestart;
        return this;
    }

    public AnchorPane getPanel() {
        return panel;
    }

    public CarwashController setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    class SendEmail {
        SendEmail(String to, String from, String host, String file) {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);

            // Get the default Session object.
            Session session = Session.getDefaultInstance(properties);

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("NANOTECH SOFTWARES POS SYSTEM EMPLOYEE REPORT");

                // Now set the actual message
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText("WE AT NANOTECH SOFTWARES VALUE OUR CUSTOMERS");

                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);


                // Send message
                Transport.send(message);
//                System.out.println("Sent message successfully....");
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "sent message successfully", "Sent message to your inbox successfully");

            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }
}
