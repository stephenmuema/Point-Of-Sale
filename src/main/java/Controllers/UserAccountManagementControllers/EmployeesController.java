package Controllers.UserAccountManagementControllers;
//made by steve
import Controllers.UtilityClass;
import MasterClasses.EmployeeMaster;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

public class EmployeesController extends UtilityClass implements Initializable {
    public Button delete;
    @FXML
    private Label clock;
    @FXML
    private MenuItem details;
    @FXML
    private MenuItem license;
    @FXML
    private MenuItem menulogout;
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
    private Button sendtomail;
    @FXML
    private Button printoutaspdf;
    @FXML
    private TableColumn<EmployeeMaster, String> Name;
    @FXML
    private TableColumn<EmployeeMaster, String> email;
    @FXML
    private TableColumn<EmployeeMaster, String> id;
    @FXML
    private TableColumn<EmployeeMaster, String> status;

    @FXML
    private Tab existingemptab;
    @FXML
    private Button home;
    @FXML
    private Button suspend;
    @FXML
    private TableView<EmployeeMaster> tab;
    @FXML
    private AnchorPane panel;
    private ObservableList<EmployeeMaster> data;
    private String time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
        menuclick();
        buttonclick();
        editable();
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
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        delete.setOnMousePressed(event -> {
            EmployeeMaster selectedEmployee = tab.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "DO YOU WANT TO DELETE THE USER?THIS OP CANNOT BE UNDONE", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("WARNING");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
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
                                    PreparedStatement statement = connection1.prepareStatement("SELECT * FROM users where admin=?");
                                    statement.setBoolean(1, false);
                                    ResultSet resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        EmployeeMaster employeeMaster = new EmployeeMaster();
                                        employeeMaster.setName(resultSet.getString("employeename"));
                                        employeeMaster.setEmail(resultSet.getString("email"));
                                        employeeMaster.setId(resultSet.getString("id"));
                                        employeeMaster.setStatus(resultSet.getString("status"));
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
                            status.setCellValueFactory(
                                    new PropertyValueFactory<>("status"));
                            email.setCellValueFactory(new PropertyValueFactory<>("email"));
                            tab.refresh();
                        }

                    } else {
//                                not updated
                        showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ITEM COULDN'T BE REMOVED SUCCESSFULLY", "THE ITEM HAS NOT BEEN REMOVED SUCCESSFULLY");

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("I GUESS YOU STILL HAVE TO KEEP " + selectedEmployee.getName());

            }

        });
        suspend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EmployeeMaster selectedEmployee = tab.getSelectionModel().getSelectedItem();
                String newStatus;
                if (selectedEmployee.getStatus().equalsIgnoreCase("active")) {
                    newStatus = "suspended";
                } else {
                    newStatus = "active";

                }
                try {

                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET status=? WHERE id=?");

                    preparedStatement.setString(1, newStatus);
                    preparedStatement.setString(2, selectedEmployee.getId());
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
                                    PreparedStatement statement = connection1.prepareStatement("SELECT * FROM users where admin=?");
                                    statement.setBoolean(1, false);
                                    ResultSet resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        EmployeeMaster employeeMaster = new EmployeeMaster();
                                        employeeMaster.setName(resultSet.getString("employeename"));
                                        employeeMaster.setEmail(resultSet.getString("email"));
                                        employeeMaster.setId(resultSet.getString("id"));
                                        employeeMaster.setStatus(resultSet.getString("status"));

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
                            status.setCellValueFactory(
                                    new PropertyValueFactory<>("status"));
                            email.setCellValueFactory(new PropertyValueFactory<>("email"));
                            tab.refresh();
                        }

                    } else {
//                                not updated
                        showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ITEM COULDN'T BE REMOVED SUCCESSFULLY", "THE ITEM HAS NOT BEEN REMOVED SUCCESSFULLY");

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users where admin=?");
//                            WHERE subscriberkey=? and admin=?
                            statement.setBoolean(1, false);
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                EmployeeMaster employeeMaster = new EmployeeMaster();
                                employeeMaster.setName(resultSet.getString("employeename"));
                                employeeMaster.setEmail(resultSet.getString("email"));
                                employeeMaster.setId(resultSet.getString("id"));
                                employeeMaster.setStatus(resultSet.getString("status"));
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
                    status.setCellValueFactory(
                            new PropertyValueFactory<>("status"));
                    email.setCellValueFactory(new PropertyValueFactory<>("email"));
                    tab.refresh();
                }
            }
        });

        sendtomail.setOnMousePressed(event -> {
//         send audit to email
            generateAudit();

        });
        printoutaspdf.setOnMousePressed(event -> {
            generateAudit();
//         export audit as pdf
        });
    }

    private void generateAudit() {
        pdfGen();
        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "pdf created successfully", "your pdf was generated successfully");
    }

    private void pdfGen() {
        Document document = new Document(PageSize.A4_LANDSCAPE, 20, 20, 20, 20);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.getTime() + "employeesreport.pdf";
        //System.out.println(time);
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
            statement = connection.prepareStatement("SELECT * FROM users  where admin=?");
//            WHERE subscriberkey=? and admin=?
            statement.setBoolean(1, false);
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


    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
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

    public TableColumn<EmployeeMaster, String> getStatus() {
        return status;
    }

    public EmployeesController setStatus(TableColumn<EmployeeMaster, String> status) {
        this.status = status;
        return this;
    }

    public Button getSuspend() {
        return suspend;
    }

    public EmployeesController setSuspend(Button suspend) {
        this.suspend = suspend;
        return this;
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
        return panel;
    }

    public void setParents(AnchorPane parents) {
        this.panel = parents;
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

}
