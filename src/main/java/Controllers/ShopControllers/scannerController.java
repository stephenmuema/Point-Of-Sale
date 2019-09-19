package Controllers.ShopControllers;

import Controllers.BarCodeScanner;
import Controllers.UserAccountManagementControllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.StockMaster;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import logging.LogClass;
import securityandtime.config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.*;

public class scannerController extends UtilityClass implements Initializable {
    public MenuItem logout;
    public Label clock;
    public Font x1;
    public TabPane tabpane;
    public Tab existingstockdtab;
    public TableView<StockMaster> table;
    public TableColumn<StockMaster, String> name;
    public TableColumn<StockMaster, String> price;
    public TableColumn<StockMaster, String> category;
    public TableColumn<StockMaster, String> barcode;
    public TableColumn<StockMaster, String> quantity;
    public Button delete;
    public AnchorPane parentsstocks;
    public TextField itemname;
    public TextField itemprice;
    public TextField itemcategory;
    //todo continue from here by adding menu item functionality
    public TextField itemcode;
    public Button addmanually;
    public Button usescanner;
    public MenuItem employees;
    public TextField amount;
    public Button home;
    public Button image;
    public MenuItem aboutsoftware;
    public MenuItem creators;
    public MenuItem timeline;
    public MenuItem previoussales;
    public MenuItem history;
    private File file;
    private int length;
    private BufferedImage bufferedImage;
    private ObservableList<StockMaster> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
        menuclick();
        buttonclick();
        config.panel.put("panel", parentsstocks);

        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(9000),
                () -> {
                    try {

                        config.login.put("loggedout", true);
                        parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(parentsstocks, Event.ANY);
    }


    private void buttonclick() {
        image.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");

            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");

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
//                    storeimageselected.setImage(image);
            } catch (IOException ex) {
                LogClass.getLogger().log(Level.SEVERE, "image input error");
                showAlert(Alert.AlertType.WARNING, parentsstocks.getScene().getWindow(), "ERROR!!", "IMAGE INPUT ERROR");
            }
        });
        data = FXCollections.observableArrayList();
        Connection connection = getConnection();
        home.setOnAction(event -> {
            try {
                parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        addmanually.setOnAction(event -> {
            try {
                parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        usescanner.setOnMousePressed(event -> {
            //        todo add amount input
            System.out.println("clicked");
            BarCodeScanner scanner = new BarCodeScanner();
            itemcode.setText(scanner.launchScanner());
            String name, barcode, price, quantity, category;
            name = itemname.getText().toUpperCase();
            price = itemprice.getText().toUpperCase();
            barcode = itemcode.getText().toUpperCase();
            category = itemcategory.getText().toUpperCase();
            quantity = amount.getText().toUpperCase();
            PreparedStatement preparedStatement = null;

            if (!name.isEmpty() && !price.isEmpty() && !barcode.isEmpty() && !category.isEmpty() && !quantity.isEmpty()) {
                try {
                    assert connection != null;
                    preparedStatement = connection.prepareStatement("INSERT INTO stocks(name,itemcode,amount,category,price,path)VALUES(?,?,?,?,?,?)");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(1, name.toUpperCase());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(2, barcode.toUpperCase());
                        //                System.out.println("user name=="+user);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(3, quantity.toUpperCase());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(4, category.toUpperCase());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(5, price.toUpperCase());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//            System.getProperty("user.home")+"\\nanotechsoftwaresPOS\\"+
                File f = new File(fileSavePath + System.currentTimeMillis() + file.getName());
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(6, f.getAbsolutePath());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    //executequery
                    if (preparedStatement != null) {
                        int rows = preparedStatement.executeUpdate();
                        if (rows > 0) {
                            //System.out.println(rows);

                            showAlert(Alert.AlertType.INFORMATION, parentsstocks.getScene().getWindow(), "SUCCESS ", "YOUR ITEM WAS ADDED SUCCESSFULLY");
                            itemcode.clear();
                            itemname.clear();
                            itemprice.clear();
                            itemcategory.clear();
                            amount.clear();
                        } else {
                            showAlert(Alert.AlertType.WARNING, parentsstocks.getScene().getWindow(), "  FAILURE", "ERROR WHEN INSERTING ITEMS");

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, parentsstocks.getScene().getWindow(), "ERROR", "ALL FIELDS SHOULD BE FILLED");
            }
        });
        usescanner.setOnMousePressed(event -> {

//todo using scanner
        });
        employees.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parentsstocks.getChildren().removeAll();
                try {
                    parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    //System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        });
        delete.setOnAction(event -> {
            StockMaster store = table.getSelectionModel().getSelectedItem();

            try {
                assert connection != null;
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM stocks WHERE id=?");

                preparedStatement.setInt(1, store.getId());
                int updated = preparedStatement.executeUpdate();
                if (updated > 0) {
//                                updated
                    data = FXCollections.observableArrayList();

                    try {
                        if (connection != null) {
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE owner=?");
                            statement.setString(1, String.valueOf(user));
                            ResultSet resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                StockMaster stockMaster = new StockMaster();
                                stockMaster.setName(resultSet.getString("name"));
                                stockMaster.setAmount(resultSet.getString("amount"));
                                stockMaster.setBarcode(resultSet.getString("itemcode"));
                                stockMaster.setCategory(resultSet.getString("category"));
                                stockMaster.setPrice(resultSet.getString("price"));
                                stockMaster.setId(resultSet.getInt("id"));
                                data.add(stockMaster);
                                data.add(stockMaster);
                            }
                            table.setItems(data);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    assert table != null : "fx:id=\"table\" was not injected: check your FXML ";
                    name.setCellValueFactory(
                            new PropertyValueFactory<StockMaster, String>("Name"));
                    price.setCellValueFactory(
                            new PropertyValueFactory<StockMaster, String>("Price"));
                    category.setCellValueFactory(
                            new PropertyValueFactory<StockMaster, String>("Category"));
                    quantity.setCellValueFactory(new PropertyValueFactory<StockMaster, String>("Amount"));
                    fetchItems();
                    table.refresh();
                    showAlert(Alert.AlertType.INFORMATION, parentsstocks.getScene().getWindow(), "ITEM REMOVED SUCCESSFULLY", "THE ITEM HAS BEEN REMOVED SUCCESSFULLY");

                } else {
//                                not updated
                    showAlert(Alert.AlertType.WARNING, parentsstocks.getScene().getWindow(), "ITEM COULDN'T BE REMOVED SUCCESSFULLY", "THE ITEM HAS NOT BEEN REMOVED SUCCESSFULLY");

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        });
        existingstockdtab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                fetchItems();
            }
        });
        fetchItems();

    }

    private void fetchItems() {

        data = FXCollections.observableArrayList();
        Connection connection = getConnection();
        Connection finalConnection = connection;
        connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stocks order by id ASC ");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    StockMaster storemaster = new StockMaster();
                    storemaster.setName(resultSet.getString("name"));
                    storemaster.setAmount(resultSet.getString("amount"));
                    storemaster.setBarcode(resultSet.getString("itemcode"));
                    storemaster.setCategory(resultSet.getString("category"));
                    storemaster.setPrice(resultSet.getString("price"));
                    storemaster.setId(resultSet.getInt("id"));
                    data.add(storemaster);
                }
                table.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert table != null : "fx:id=\"table\" was not injected: check your FXML ";
        name.setCellValueFactory(
                new PropertyValueFactory<StockMaster, String>("Name"));
        price.setCellValueFactory(
                new PropertyValueFactory<StockMaster, String>("Price"));
        category.setCellValueFactory(
                new PropertyValueFactory<StockMaster, String>("Category"));
        quantity.setCellValueFactory(new PropertyValueFactory<StockMaster, String>("Amount"));
        barcode.setCellValueFactory(new PropertyValueFactory<StockMaster, String>("Barcode"));
        table.refresh();

        table.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StockMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StockMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue().toUpperCase();
                        PreparedStatement preparedStatement = null;
                        try {
                            StockMaster store = table.getSelectionModel().getSelectedItem();
                            String id = store.getId().toString();
                            preparedStatement = finalConnection.prepareStatement("UPDATE stocks set name=? where id=?");
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
        price.setCellFactory(TextFieldTableCell.forTableColumn());
        price.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StockMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StockMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            StockMaster store = table.getSelectionModel().getSelectedItem();
                            String id = store.getId().toString();
                            assert finalConnection != null;
                            preparedStatement = finalConnection.prepareStatement("UPDATE stocks set price=? where id=?");
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
        barcode.setCellFactory(TextFieldTableCell.forTableColumn());
        barcode.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StockMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StockMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            StockMaster store = table.getSelectionModel().getSelectedItem();
                            String id = store.getId().toString();
                            preparedStatement = finalConnection.prepareStatement("UPDATE stocks set itemcode=? where id=?");
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
        quantity.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StockMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StockMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            StockMaster store = table.getSelectionModel().getSelectedItem();
                            String id = store.getId().toString();
                            assert finalConnection != null;
                            preparedStatement = finalConnection.prepareStatement("UPDATE stocks set amount=? where id=?");
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
        category.setCellFactory(TextFieldTableCell.forTableColumn());
        category.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StockMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StockMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            StockMaster store = table.getSelectionModel().getSelectedItem();
                            String id = store.getId().toString();
                            assert finalConnection != null;
                            preparedStatement = finalConnection.prepareStatement("UPDATE stocks set category=? where id=?");
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

    private void menuclick() {
        logout.setOnAction(event -> {
            logout(parentsstocks);

        });
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
        Platform.exit();
    }

    public MenuItem getLogout() {
        return logout;
    }

    public scannerController setLogout(MenuItem logout) {
        this.logout = logout;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public scannerController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public Font getX1() {
        return x1;
    }

    public scannerController setX1(Font x1) {
        this.x1 = x1;
        return this;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public scannerController setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
        return this;
    }

    public Tab getExistingstockdtab() {
        return existingstockdtab;
    }

    public scannerController setExistingstockdtab(Tab existingstockdtab) {
        this.existingstockdtab = existingstockdtab;
        return this;
    }

    public TableView<StockMaster> getTable() {
        return table;
    }

    public scannerController setTable(TableView<StockMaster> table) {
        this.table = table;
        return this;
    }

    public TableColumn<StockMaster, String> getName() {
        return name;
    }

    public scannerController setName(TableColumn<StockMaster, String> name) {
        this.name = name;
        return this;
    }

    public TableColumn<StockMaster, String> getPrice() {
        return price;
    }

    public scannerController setPrice(TableColumn<StockMaster, String> price) {
        this.price = price;
        return this;
    }

    public TableColumn<StockMaster, String> getCategory() {
        return category;
    }

    public scannerController setCategory(TableColumn<StockMaster, String> category) {
        this.category = category;
        return this;
    }

    public TableColumn<StockMaster, String> getBarcode() {
        return barcode;
    }

    public scannerController setBarcode(TableColumn<StockMaster, String> barcode) {
        this.barcode = barcode;
        return this;
    }

    public TableColumn<StockMaster, String> getQuantity() {
        return quantity;
    }

    public scannerController setQuantity(TableColumn<StockMaster, String> quantity) {
        this.quantity = quantity;
        return this;
    }

    public Button getDelete() {
        return delete;
    }

    public scannerController setDelete(Button delete) {
        this.delete = delete;
        return this;
    }

    public AnchorPane getParentsstocks() {
        return parentsstocks;
    }

    public scannerController setParentsstocks(AnchorPane parentsstocks) {
        this.parentsstocks = parentsstocks;
        return this;
    }

    public TextField getItemname() {
        return itemname;
    }

    public scannerController setItemname(TextField itemname) {
        this.itemname = itemname;
        return this;
    }

    public TextField getItemprice() {
        return itemprice;
    }

    public scannerController setItemprice(TextField itemprice) {
        this.itemprice = itemprice;
        return this;
    }

    public TextField getItemcategory() {
        return itemcategory;
    }

    public scannerController setItemcategory(TextField itemcategory) {
        this.itemcategory = itemcategory;
        return this;
    }

    public TextField getItemcode() {
        return itemcode;
    }

    public scannerController setItemcode(TextField itemcode) {
        this.itemcode = itemcode;
        return this;
    }

    public Button getAddmanually() {
        return addmanually;
    }

    public scannerController setAddmanually(Button addmanually) {
        this.addmanually = addmanually;
        return this;
    }

    public Button getUsescanner() {
        return usescanner;
    }

    public scannerController setUsescanner(Button usescanner) {
        this.usescanner = usescanner;
        return this;
    }

    public MenuItem getEmployees() {
        return employees;
    }

    public scannerController setEmployees(MenuItem employees) {
        this.employees = employees;
        return this;
    }

    public TextField getAmount() {
        return amount;
    }

    public scannerController setAmount(TextField amount) {
        this.amount = amount;
        return this;
    }

    public Button getHome() {
        return home;
    }

    public scannerController setHome(Button home) {
        this.home = home;
        return this;
    }

    public Button getImage() {
        return image;
    }

    public scannerController setImage(Button image) {
        this.image = image;
        return this;
    }

    public File getFile() {
        return file;
    }

    public scannerController setFile(File file) {
        this.file = file;
        return this;
    }

    public int getLength() {
        return length;
    }

    public scannerController setLength(int length) {
        this.length = length;
        return this;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public scannerController setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        return this;
    }

    public ObservableList<StockMaster> getData() {
        return data;
    }

    public scannerController setData(ObservableList<StockMaster> data) {
        this.data = data;
        return this;
    }
}
