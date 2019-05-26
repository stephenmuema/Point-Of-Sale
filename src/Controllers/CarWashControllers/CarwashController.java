package Controllers.CarWashControllers;

import Controllers.UserAccountManagementControllers.IdleMonitor;
import MasterClasses.CarWashMaster;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;
import securityandtime.CheckConn;
import securityandtime.config;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static securityandtime.config.*;

public class CarwashController implements Initializable {
    public Label clock;
    public MenuItem logout;
    public Button delete;
    public Button sendtomail;
    public Button printoutaspdf;

    public Button home;
    public Label cash;
    public Tab pastclients;
    public TableColumn<CarWashMaster, String> Name;
    public TableColumn<CarWashMaster, String> reg;
    public TableColumn<CarWashMaster, String> id;
    public TableColumn<CarWashMaster, String> status;
    public TableColumn<CarWashMaster, String> operator;
    public TableColumn<CarWashMaster, String> payout;
    public MenuItem stores;
    public MenuItem stocks;
    public MenuItem logoutMenu;
    public MenuItem exitMenu;
    public MenuItem accountdetailsMenu;
    public MenuItem helpMenu;
    public MenuItem CreatorsMenu;
    private int moneyPaid;
    @FXML
    private TableView<CarWashMaster> tab;
    @FXML
    private VBox parents;
    private ObservableList<CarWashMaster> data;
    private String time;

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
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(parents, Event.ANY);
        time();
        menuclick();
        buttonclick();
        editable();
        cash.setText(String.valueOf(moneyPaid));
    }

    private void editable() {
        tab.setEditable(true);
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection finalConnection = connection;
        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set `ownername`=? where id=?");
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
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set registration=? where id=?");
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
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set idnumber=? where id=?");
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
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            assert finalConnection != null;
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set status=? where id=?");
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
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set washedby=? where id=?");
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
                        ((CarWashMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE carwash set cashpaid=? where id=?");
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
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection finalConnection = connection;
        home.setOnMouseClicked(event -> {
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/panelAdmin.fxml")))));
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
            System.out.println(user.get("user"));
        });
        printoutaspdf.setOnMousePressed(event -> {
            generateAudit();
//         export audit as pdf
        });
    }

    private void generateAudit() {
        pdfGen();
        showAlert(Alert.AlertType.INFORMATION, parents.getScene().getWindow(), "OPERATION SUCCESSFUL", "your pdf was generated successfully");
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public MenuItem getLogout() {
        return logout;
    }

    public void setLogout(MenuItem logout) {
        this.logout = logout;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public Button getSendtomail() {
        return sendtomail;
    }

    public void setSendtomail(Button sendtomail) {
        this.sendtomail = sendtomail;
    }

    public Button getPrintoutaspdf() {
        return printoutaspdf;
    }

    public void setPrintoutaspdf(Button printoutaspdf) {
        this.printoutaspdf = printoutaspdf;
    }

    public MenuItem getStores() {
        return stores;
    }

    public void setStores(MenuItem stores) {
        this.stores = stores;
    }

    public MenuItem getStocks() {
        return stocks;
    }

    public void setStocks(MenuItem stocks) {
        this.stocks = stocks;
    }

    public Button getHome() {
        return home;
    }

    public void setHome(Button home) {
        this.home = home;
    }

    public Label getCash() {
        return cash;
    }

    public void setCash(Label cash) {
        this.cash = cash;
    }

    public Tab getPastclients() {
        return pastclients;
    }

    public void setPastclients(Tab pastclients) {
        this.pastclients = pastclients;
    }

    public TableColumn<CarWashMaster, String> getName() {
        return Name;
    }

    public void setName(TableColumn<CarWashMaster, String> name) {
        Name = name;
    }

    public TableColumn<CarWashMaster, String> getReg() {
        return reg;
    }

    public void setReg(TableColumn<CarWashMaster, String> reg) {
        this.reg = reg;
    }

    public TableColumn<CarWashMaster, String> getId() {
        return id;
    }

    public void setId(TableColumn<CarWashMaster, String> id) {
        this.id = id;
    }

    public TableColumn<CarWashMaster, String> getStatus() {
        return status;
    }

    public void setStatus(TableColumn<CarWashMaster, String> status) {
        this.status = status;
    }

    public TableColumn<CarWashMaster, String> getOperator() {
        return operator;
    }

    public void setOperator(TableColumn<CarWashMaster, String> operator) {
        this.operator = operator;
    }

    public TableColumn<CarWashMaster, String> getPayout() {
        return payout;
    }

    public void setPayout(TableColumn<CarWashMaster, String> payout) {
        this.payout = payout;
    }

    public int getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(int moneyPaid) {
        this.moneyPaid = moneyPaid;
    }

    public TableView<CarWashMaster> getTab() {
        return tab;
    }

    public void setTab(TableView<CarWashMaster> tab) {
        this.tab = tab;
    }

    public VBox getParents() {
        return parents;
    }

    public void setParents(VBox parents) {
        this.parents = parents;
    }

    public ObservableList<CarWashMaster> getData() {
        return data;
    }

    public void setData(ObservableList<CarWashMaster> data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private void pdfGen() {
        Document document = new Document(PageSize.A4_LANDSCAPE, 20, 20, 20, 20);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.getTime() + "CARWASHREPORT.pdf";
        System.out.println(time);
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(time));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
//        cryptography
//        writer.setEncryption("concretepage".getBytes(), "cp123".getBytes(), PdfWriter.ALLOW_COPY, PdfWriter.STANDARD_ENCRYPTION_40);
//        writer.createXmpMetadata();
        document.open();

        Paragraph introtable = new Paragraph("CAR WASH TABLE",

                FontFactory.getFont(FontFactory.HELVETICA,

                        18, Font.BOLDITALIC));
        introtable.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(introtable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Paragraph welcome = new Paragraph("This is the table having all your previous clients");

        try {
            document.add(welcome);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable t = new PdfPTable(5);

        t.setSpacingBefore(25);

        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(new Phrase("REGISTRATION"));

        t.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("NAME"));

        t.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("ID NUMBER"));

        t.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase("CONTACT"));

        t.addCell(c4);

        PdfPCell c5 = new PdfPCell(new Phrase("MONEY"));

        t.addCell(c5);

//        adding headers
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(des[2], "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement statement = null;
        try {
            assert connection != null;
            statement = connection.prepareStatement("SELECT * FROM carwash where status=?");
            statement.setString(1, "COMPLETE");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                assert resultSet != null;
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("registration"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("ownername"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("idnumber"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("contact"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("cashpaid"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            document.add(t);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
//        audits section
        Paragraph employeeaudits = new Paragraph("CAR WASH TABLE",

                FontFactory.getFont(FontFactory.HELVETICA,

                        18, Font.BOLDITALIC));
        employeeaudits.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(employeeaudits);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
//        todo add audits
        Paragraph audits = new Paragraph("This is the table having all your car wash audits");
        try {
            document.add(audits);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    private void populateTable() {
        data = FXCollections.observableArrayList();
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void time() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String mins = null, hrs = null, secs = null, pmam = null;
            try {
                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

                if (hours >= 12) {
//                    hrs= "0"+String.valueOf(hours-12);
                    pmam = "PM";
                } else {
                    pmam = "AM";

                }
                if (minutes > 9) {
                    mins = String.valueOf(minutes);
                } else {
                    mins = "0" + String.valueOf(minutes);

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + String.valueOf(seconds);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) + ":" + (secs) + " " + pmam);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void menuclick() {

        stocks.setOnAction(event -> {
            parents.getChildren().removeAll();
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stores.setOnAction(event -> {
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/addshop.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        helpMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(sitedocs).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        CreatorsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(site).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        exitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(1);
            }
        });
        logoutMenu.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
                System.out.println("logging out");
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                showAlert(Alert.AlertType.INFORMATION, parents.getScene().getWindow(), "sent message successfully", "Sent message to your inbox successfully");

            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }
}
