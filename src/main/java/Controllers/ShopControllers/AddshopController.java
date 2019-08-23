package Controllers.ShopControllers;

import Controllers.UserAccountManagementControllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.Storemaster;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import logging.LogClass;
import org.apache.commons.io.FileUtils;
import securityandtime.config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.*;

public class AddshopController extends UtilityClass implements Initializable {


    public TableColumn<Storemaster, String> storeName;
    public TableColumn<Storemaster, String> storeLoc;
    public TableColumn<Storemaster, String> employeeNumber;
    public TableColumn<Storemaster, String> storeId;
    public Button storelistaspdf;
    public MenuItem stocksmanager;
    public Button home;
    public MenuItem logoutMenu;
    public MenuItem exitMenu;
    public MenuItem accountdetailsMenu;
    public MenuItem CreatorsMenu;
    public MenuItem helpMenu;
    BufferedImage bufferedImage;
    File file;
    int length;
    @FXML
    private VBox panel;

    @FXML

    private Label clock;
    @FXML

    private TextField storename;
    @FXML
    private TextField storelocation;
    @FXML
    private TextArea description;
    @FXML
    private TextField employeenum;
    @FXML
    private Button addstore;
    @FXML

    private Font x1;
    @FXML
    private Button storeimageselection;
    @FXML
    private ImageView storeimageselected;
    @FXML
    private Button delete;
    @FXML
    private TabPane tabpane;
    @FXML
    private Tab existingstoredtab;
    private ObservableList<Storemaster> data;
    @FXML
    private TableView<Storemaster> tab;
    private EventHandler<ActionEvent> btnLoadEventListener
            = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent t) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");

            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
            fileChooser.getExtensionFilters().addAll(extFilterALL, extFilterPNG, extFilterJPG, extFilterJPEG);
            fileChooser.setTitle("SELECT STORE IMAGE");
            //Show open file dialog
            file = fileChooser.showOpenDialog(null);
            length = (int) file.length();
            try {
                bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                storeimageselected.setImage(image);
            } catch (IOException ex) {
                LogClass.getLogger().log(Level.SEVERE, "image input error");
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR!!", "IMAGE INPUT ERROR");
            }

        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuclick();
        buttonClick();
        myStores();
        time(clock);
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

    private void menuclick() {


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
//                System.out.println("logging out");
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void myStores() {
        existingstoredtab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (existingstoredtab.isSelected()) {
                    data = FXCollections.observableArrayList();
                    Connection connection = null;
                    try {
                        connection = DriverManager
                                .getConnection(des[2], des[0], des[1]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (connection != null) {
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE owner=?");
                            statement.setString(1, String.valueOf(user.get("user")));
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                Storemaster storemaster = new Storemaster();
                                System.out.println(resultSet.getString("name"));
                                storemaster.storeName.set(resultSet.getString("name"));
                                storemaster.storeLocation.set(resultSet.getString("location"));
                                storemaster.employeeNumber.set(resultSet.getString("employeenumber"));
                                storemaster.storeId.set(resultSet.getString("id"));
                                data.add(storemaster);
                            }
                            tab.setItems(data);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
                    storeName.setCellValueFactory(
                            new PropertyValueFactory<Storemaster, String>("storeName"));
                    storeLoc.setCellValueFactory(
                            new PropertyValueFactory<Storemaster, String>("storeLocation"));
                    employeeNumber.setCellValueFactory(
                            new PropertyValueFactory<Storemaster, String>("employeeNumber"));
                    storeId.setCellValueFactory(new PropertyValueFactory<Storemaster, String>("storeId"));
                    tab.refresh();
                }
            }
        });
        data = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE owner=?");
                statement.setString(1, String.valueOf(user.get("user")));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Storemaster storemaster = new Storemaster();
                    storemaster.storeName.set(resultSet.getString("name"));
                    storemaster.storeLocation.set(resultSet.getString("location"));
                    storemaster.employeeNumber.set(resultSet.getString("employeenumber"));
                    storemaster.storeId.set(resultSet.getString("id"));
                    data.add(storemaster);
                }
                tab.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
        storeName.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("storeName"));
        storeLoc.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("storeLocation"));
        employeeNumber.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("employeeNumber"));
        storeId.setCellValueFactory(new PropertyValueFactory<Storemaster, String>("storeId"));

        tableViewActiononClick();
        tab.setEditable(true);
        storeName.setCellFactory(TextFieldTableCell.forTableColumn());
        Connection finalConnection = connection;
        storeName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Storemaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Storemaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setStoreName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            Storemaster store = tab.getSelectionModel().getSelectedItem();
                            String id = store.getStoreId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE stores set name=? where id=?");
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
        storeLoc.setCellFactory(TextFieldTableCell.forTableColumn());
        storeLoc.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Storemaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Storemaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setStoreName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            Storemaster store = tab.getSelectionModel().getSelectedItem();
                            String id = store.getStoreId();
                            preparedStatement = finalConnection.prepareStatement("UPDATE stores set location=? where id=?");
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
        employeeNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeNumber.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setStoreName(t.getNewValue());
                    String newval = t.getNewValue();
                    PreparedStatement preparedStatement = null;
                    try {
                        Storemaster store = tab.getSelectionModel().getSelectedItem();
                        String id = store.getStoreId();
                        if (finalConnection != null) {
                            preparedStatement = finalConnection.prepareStatement("UPDATE stores set employeenumber=? where id=?");
                        }
                        if (preparedStatement != null) {
                            preparedStatement.setString(1, newval);
                        }
                        if (preparedStatement != null) {
                            preparedStatement.setString(2, id);
                        }
                        if (preparedStatement != null) {
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
        );


    }

    private void tableViewActiononClick() {
        tab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if (tab.getSelectionModel().getSelectedItem() != null) {
//                System.out.println(store.getStoreId());
//                System.out.println("Selected Row is row " + val);
                    buttonClick();
                }
            }
        });
    }

    private void pdfGen() {
        Document document = new Document(PageSize.A4_LANDSCAPE, 20, 20, 20, 20);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = timestamp.getTime() + "storelist.pdf";
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

        Paragraph introtable = new Paragraph("STORE TABLE",

                FontFactory.getFont(FontFactory.HELVETICA,

                        18, com.itextpdf.text.Font.BOLDITALIC));
        introtable.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(introtable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Paragraph welcome = new Paragraph("This is the table having all your stores");

        try {
            document.add(welcome);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable t = new PdfPTable(4);

        t.setSpacingBefore(25);

        t.setSpacingAfter(25);

        PdfPCell c1 = new PdfPCell(new Phrase("ID"));

        t.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase("NAME"));

        t.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("LOCATION"));

        t.addCell(c3);

        PdfPCell c4 = new PdfPCell(new Phrase("EMPLOYEES"));

        t.addCell(c4);
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
            statement = connection.prepareStatement("SELECT * FROM stores where owner=? ");
            statement.setString(1, user.get("user"));

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
                t.addCell(resultSet.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("location"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                t.addCell(resultSet.getString("employeenumber"));
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

                        18, com.itextpdf.text.Font.BOLDITALIC));
        employeeaudits.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(employeeaudits);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Paragraph audits = new Paragraph("This is the table having all your store audits");
        try {
            document.add(audits);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "pdf created successfully", "your pdf was generated successfully");


    }

    //    location,name,incharge,type,date,no of workers
    private void buttonClick() {
        home.setOnAction(event -> {
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        storelistaspdf.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pdfGen();
            }
        });

        delete.setOnAction(event -> {
            Storemaster store = tab.getSelectionModel().getSelectedItem();

            try {
                Connection connection = DriverManager
                        .getConnection(des[2], des[0], des[1]);
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM stores WHERE id=?");

                preparedStatement.setString(1, store.getStoreId());
                int updated = preparedStatement.executeUpdate();
                if (updated > 0) {
                    if (existingstoredtab.isSelected()) {
                        data = FXCollections.observableArrayList();
                        try {
                            connection = DriverManager
                                    .getConnection(des[2], des[0], des[1]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (connection != null) {
                                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE owner=?");
                                statement.setString(1, String.valueOf(user.get("user")));
                                ResultSet resultSet = statement.executeQuery();
                                while (resultSet.next()) {
                                    Storemaster storemaster = new Storemaster();
                                    System.out.println(resultSet.getString("name"));
                                    storemaster.storeName.set(resultSet.getString("name"));
                                    storemaster.storeLocation.set(resultSet.getString("location"));
                                    storemaster.employeeNumber.set(resultSet.getString("employeenumber"));
                                    storemaster.storeId.set(resultSet.getString("id"));
                                    data.add(storemaster);
                                }
                                tab.setItems(data);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
                        storeName.setCellValueFactory(
                                new PropertyValueFactory<Storemaster, String>("storeName"));
                        storeLoc.setCellValueFactory(
                                new PropertyValueFactory<Storemaster, String>("storeLocation"));
                        employeeNumber.setCellValueFactory(
                                new PropertyValueFactory<Storemaster, String>("employeeNumber"));
                        storeId.setCellValueFactory(new PropertyValueFactory<Storemaster, String>("storeId"));
                        tab.refresh();
                    }
                } else {
//                                not updated
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "STORE COULDN'T BE REMOVED SUCCESSFULLY", "THE STORE HAS NOT BEEN REMOVED SUCCESSFULLY");

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        });

        addstore.setOnAction(event -> {

            if (storename.getText().isEmpty() || storelocation.getText().isEmpty() || description.getText().isEmpty()) {
                LogClass.getLogger().log(Level.SEVERE, " PLEASE FILL ALL FIELDS");
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "FILL ALL FIELDS", "FILL ALL FIELDS"
                );

            } else {
                addstore();

            }
        });
        storeimageselection.setOnAction(btnLoadEventListener);
    }

    private void fetch() {
        data = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(des[2], "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE owner=?");
                statement.setString(1, String.valueOf(user));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Storemaster storemaster = new Storemaster();
                    storemaster.storeName.set(resultSet.getString("name"));
                    storemaster.storeLocation.set(resultSet.getString("location"));
                    storemaster.employeeNumber.set(resultSet.getString("employeenumber"));
                    storemaster.storeId.set(resultSet.getString("id"));
                    data.add(storemaster);
                }
                tab.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
        storeName.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("storeName"));
        storeLoc.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("storeLocation"));
        employeeNumber.setCellValueFactory(
                new PropertyValueFactory<Storemaster, String>("employeeNumber"));
        storeId.setCellValueFactory(new PropertyValueFactory<Storemaster, String>("storeId"));
        tab.refresh();
        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "STORE REMOVED SUCCESSFULLY", "THE STORE HAS BEEN REMOVED SUCCESSFULLY");

    }

    /**
     * add store functionality
     */
    private void addstore() {
        String name = storename.getText();
        String location = storelocation.getText();
        String stringdescription = description.getText();
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        try {
            assert connection != null;
            preparedStatement = connection.prepareStatement("INSERT INTO stores(name,owner,description, location,employeenumber,image)VALUES(?,?,?,?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.setString(1, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.setString(2, String.valueOf(user.get("user")));
//                System.out.println("user name=="+user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.setString(3, stringdescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.setString(4, location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String number = employeenum.getText();


        try {
            Objects.requireNonNull(preparedStatement).setString(5, number);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            try {
                Objects.requireNonNull(preparedStatement).setBinaryStream(6, FileUtils.openInputStream(file), length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            //executequery
            if (preparedStatement != null) {
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println(rows);
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS UPLOADING", "YOUR STORE WAS ADDED SUCCESSFULLY");
                    storename.clear();
                    storelocation.clear();
                    employeenum.clear();
                    storeimageselected.setImage(null);
                    description.clear();


                } else {
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), " UPLOADING FAILURE", "ERROR WHEN UPLOADING STORE");

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }


    public TableColumn<Storemaster, String> getStoreName() {
        return storeName;
    }

    public void setStoreName(TableColumn<Storemaster, String> storeName) {
        this.storeName = storeName;
    }

    public TableColumn<Storemaster, String> getStoreLoc() {
        return storeLoc;
    }

    public void setStoreLoc(TableColumn<Storemaster, String> storeLoc) {
        this.storeLoc = storeLoc;
    }

    public TableColumn<Storemaster, String> getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(TableColumn<Storemaster, String> employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public TableColumn<Storemaster, String> getStoreId() {
        return storeId;
    }

    public void setStoreId(TableColumn<Storemaster, String> storeId) {
        this.storeId = storeId;
    }

    public Button getStorelistaspdf() {
        return storelistaspdf;
    }

    public void setStorelistaspdf(Button storelistaspdf) {
        this.storelistaspdf = storelistaspdf;
    }

    public MenuItem getStocksmanager() {
        return stocksmanager;
    }

    public void setStocksmanager(MenuItem stocksmanager) {
        this.stocksmanager = stocksmanager;
    }

    public Button getHome() {
        return home;
    }

    public void setHome(Button home) {
        this.home = home;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public VBox getPanel() {
        return panel;
    }

    public void setPanel(VBox panel) {
        this.panel = panel;
    }


    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public TextField getStorename() {
        return storename;
    }

    public void setStorename(TextField storename) {
        this.storename = storename;
    }

    public TextField getStorelocation() {
        return storelocation;
    }

    public void setStorelocation(TextField storelocation) {
        this.storelocation = storelocation;
    }

    public TextArea getDescription() {
        return description;
    }

    public void setDescription(TextArea description) {
        this.description = description;
    }

    public TextField getEmployeenum() {
        return employeenum;
    }

    public void setEmployeenum(TextField employeenum) {
        this.employeenum = employeenum;
    }

    public Button getAddstore() {
        return addstore;
    }

    public void setAddstore(Button addstore) {
        this.addstore = addstore;
    }


    public Font getX1() {
        return x1;
    }

    public void setX1(Font x1) {
        this.x1 = x1;
    }

    public Button getStoreimageselection() {
        return storeimageselection;
    }

    public void setStoreimageselection(Button storeimageselection) {
        this.storeimageselection = storeimageselection;
    }

    public ImageView getStoreimageselected() {
        return storeimageselected;
    }

    public void setStoreimageselected(ImageView storeimageselected) {
        this.storeimageselected = storeimageselected;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public void setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
    }

    public Tab getExistingstoredtab() {
        return existingstoredtab;
    }

    public void setExistingstoredtab(Tab existingstoredtab) {
        this.existingstoredtab = existingstoredtab;
    }

    public ObservableList<Storemaster> getData() {
        return data;
    }

    public void setData(ObservableList<Storemaster> data) {
        this.data = data;
    }

    public TableView<Storemaster> getTab() {
        return tab;
    }

    public void setTab(TableView<Storemaster> tab) {
        this.tab = tab;
    }

    public EventHandler<ActionEvent> getBtnLoadEventListener() {
        return btnLoadEventListener;
    }

    public void setBtnLoadEventListener(EventHandler<ActionEvent> btnLoadEventListener) {
        this.btnLoadEventListener = btnLoadEventListener;
    }
}
