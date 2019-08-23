package Controllers.UserAccountManagementControllers;

import Controllers.UtilityClass;
import MasterClasses.EmployeeMaster;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static securityandtime.config.host;
import static securityandtime.config.user;

public class EmployeesController extends UtilityClass implements Initializable {
    public Label clock;
    public MenuItem logout;
    public Button delete;
    public Button sendtomail;
    public Button printoutaspdf;
    public TableColumn<EmployeeMaster, String> Name;
    public TableColumn<EmployeeMaster, String> email;
    public TableColumn<EmployeeMaster, String> id;
    public Tab existingemptab;
    public MenuItem stores;
    public MenuItem stocks;
    public Button home;
    @FXML
    private TableView<EmployeeMaster> tab;
    @FXML
    private AnchorPane parents;
    private ObservableList<EmployeeMaster> data;
    private String time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
        menuclick();
        buttonclick();
        editable();
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(parents, Event.ANY);
    }

    private void editable() {
        tab.setEditable(true);
        Connection connection = getConnection();
        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmployeeMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmployeeMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            EmployeeMaster employeeMaster = tab.getSelectionModel().getSelectedItem();
                            String id = employeeMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE users set employeename=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EmployeeMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EmployeeMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            EmployeeMaster employeeMaster = tab.getSelectionModel().getSelectedItem();
                            String id = employeeMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE users set email=? where id=?");
                            preparedStatement.setString(1, newval);
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
        Connection connection = getConnection();
        home.setOnMouseClicked(event -> {
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        delete.setOnMousePressed(event -> {
            EmployeeMaster selectedEmployee = tab.getSelectionModel().getSelectedItem();

            try {

                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id=?");

                preparedStatement.setString(1, selectedEmployee.getId());
                int updated = preparedStatement.executeUpdate();
                if (updated > 0) {
//                                updated
                    data = FXCollections.observableArrayList();
                    if (existingemptab.isSelected()) {
                        data = FXCollections.observableArrayList();
                        Connection connection1 = getConnection();
                        try {
//                        DISPLAYING EMPLOYEES
                            if (connection1 != null) {
                                PreparedStatement statement = connection1.prepareStatement("SELECT * FROM users ");
//                                statement.setString(1, String.valueOf(key.get("key")));
//                                statement.setBoolean(2, false);
                                ResultSet resultSet = statement.executeQuery();
                                while (resultSet.next()) {
                                    EmployeeMaster employeeMaster = new EmployeeMaster();
                                    employeeMaster.Name.set(resultSet.getString("employeename"));
                                    employeeMaster.email.set(resultSet.getString("email"));
                                    employeeMaster.Id.set(resultSet.getString("id"));
                                    data.add(employeeMaster);
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
                                new PropertyValueFactory<>("Id"));
                        email.setCellValueFactory(new PropertyValueFactory<>("email"));
                        tab.refresh();
                    }

                } else {
//                                not updated
                    showAlert(Alert.AlertType.WARNING, parents.getScene().getWindow(), "ITEM COULDN'T BE REMOVED SUCCESSFULLY", "THE ITEM HAS NOT BEEN REMOVED SUCCESSFULLY");

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        existingemptab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (existingemptab.isSelected()) {
                    data = FXCollections.observableArrayList();
                    Connection connection = getConnection();
                    try {
//                        DISPLAYING EMPLOYEES
                        if (connection != null) {
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
//                            WHERE subscriberkey=? and admin=?
//                            statement.setString(1, String.valueOf(key.get("key")));
//                            statement.setBoolean(2, false);
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                EmployeeMaster employeeMaster = new EmployeeMaster();
                                employeeMaster.Name.set(resultSet.getString("employeename"));
                                employeeMaster.email.set(resultSet.getString("email"));
                                employeeMaster.Id.set(resultSet.getString("id"));
                                data.add(employeeMaster);
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
                            new PropertyValueFactory<>("Id"));
                    email.setCellValueFactory(new PropertyValueFactory<>("email"));
                    tab.refresh();
                }
            }
        });

        sendtomail.setOnMousePressed(event -> {
//         send audit to email
            generateAudit();
            SendEmail sendEmail = new SendEmail(user.get("user"), "nanotechsoftwarespos@nanotechsoftwares.com", host, time);
            System.out.println(user.get("user"));
        });
        printoutaspdf.setOnMousePressed(event -> {
            generateAudit();
//         export audit as pdf
        });
    }

    private void generateAudit() {
        pdfGen();
        showAlert(Alert.AlertType.INFORMATION, parents.getScene().getWindow(), "pdf created successfully", "your pdf was generated successfully");
    }

    private void pdfGen() {
        Document document = new Document(PageSize.A4_LANDSCAPE, 20, 20, 20, 20);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.getTime() + "employeesreport.pdf";
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

        Paragraph introtable = new Paragraph("EMPLOYEE TABLE",

                FontFactory.getFont(FontFactory.HELVETICA,

                        18, Font.BOLDITALIC));
        introtable.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(introtable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Paragraph welcome = new Paragraph("This is the table having all your employees");

        try {
            document.add(welcome);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable t = new PdfPTable(3);

        t.setSpacingBefore(25);

        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(new Phrase("ID"));

        t.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("NAME"));

        t.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("EMAIL"));

        t.addCell(c3);

//        adding headers
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM users  ");
//            WHERE subscriberkey=? and admin=?
//            statement.setString(1, key.get("key"));
//            statement.setBoolean(2, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = null;
        try {
            assert statement != null;
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
                t.addCell(resultSet.getString("id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("employeename"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("email"));
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
        Paragraph employeeaudits = new Paragraph("EMPLOYEE AUDITS TABLE",

                FontFactory.getFont(FontFactory.HELVETICA,

                        18, Font.BOLDITALIC));
        employeeaudits.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(employeeaudits);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Paragraph audits = new Paragraph("This is the table having all your employees audits");
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

    private void menuclick() {
        stocks.setOnAction(event -> {
            parents.getChildren().removeAll();
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stores.setOnAction(event -> {
            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/addshop.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logout.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
                parents.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    public TableColumn<EmployeeMaster, String> getName() {
        return Name;
    }

    public void setName(TableColumn<EmployeeMaster, String> name) {
        Name = name;
    }

    public TableColumn<EmployeeMaster, String> getEmail() {
        return email;
    }

    public void setEmail(TableColumn<EmployeeMaster, String> email) {
        this.email = email;
    }

    public TableColumn<EmployeeMaster, String> getId() {
        return id;
    }

    public void setId(TableColumn<EmployeeMaster, String> id) {
        this.id = id;
    }

    public Tab getExistingemptab() {
        return existingemptab;
    }

    public void setExistingemptab(Tab existingemptab) {
        this.existingemptab = existingemptab;
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

    public TableView<EmployeeMaster> getTab() {
        return tab;
    }

    public void setTab(TableView<EmployeeMaster> tab) {
        this.tab = tab;
    }

    public AnchorPane getParents() {
        return parents;
    }

    public void setParents(AnchorPane parents) {
        this.parents = parents;
    }

    public ObservableList<EmployeeMaster> getData() {
        return data;
    }

    public void setData(ObservableList<EmployeeMaster> data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
