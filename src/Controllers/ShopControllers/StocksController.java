package Controllers.ShopControllers;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import logging.LogClass;
import org.apache.commons.io.FileUtils;
import securityandtime.config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.des;
import static securityandtime.config.user;

public class StocksController extends UtilityClass implements Initializable {
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
    public VBox parentsstocks;
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
    private File file;
    private int length;
    private BufferedImage bufferedImage;
    private ObservableList<StockMaster> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
        menuclick();
        buttonclick();
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(9000),
                () -> {
                    try {

                        config.login.put("loggedout", true);
                        parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(parentsstocks, Event.ANY);
    }

    public MenuItem getLogout() {
        return logout;
    }

    public void setLogout(MenuItem logout) {
        this.logout = logout;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public Font getX1() {
        return x1;
    }

    public void setX1(Font x1) {
        this.x1 = x1;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public void setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
    }

    public Tab getExistingstockdtab() {
        return existingstockdtab;
    }

    public void setExistingstockdtab(Tab existingstockdtab) {
        this.existingstockdtab = existingstockdtab;
    }

    public TableView<StockMaster> getTable() {
        return table;
    }

    public void setTable(TableView<StockMaster> table) {
        this.table = table;
    }

    public TableColumn<StockMaster, String> getName() {
        return name;
    }

    public void setName(TableColumn<StockMaster, String> name) {
        this.name = name;
    }

    public TableColumn<StockMaster, String> getPrice() {
        return price;
    }

    public void setPrice(TableColumn<StockMaster, String> price) {
        this.price = price;
    }

    public TableColumn<StockMaster, String> getCategory() {
        return category;
    }

    public void setCategory(TableColumn<StockMaster, String> category) {
        this.category = category;
    }

    public TableColumn<StockMaster, String> getBarcode() {
        return barcode;
    }

    public void setBarcode(TableColumn<StockMaster, String> barcode) {
        this.barcode = barcode;
    }

    public TableColumn<StockMaster, String> getQuantity() {
        return quantity;
    }

    public void setQuantity(TableColumn<StockMaster, String> quantity) {
        this.quantity = quantity;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public VBox getParentsstocks() {
        return parentsstocks;
    }

    public void setParentsstocks(VBox parentsstocks) {
        this.parentsstocks = parentsstocks;
    }

    public TextField getItemname() {
        return itemname;
    }

    public void setItemname(TextField itemname) {
        this.itemname = itemname;
    }

    public TextField getItemprice() {
        return itemprice;
    }

    public void setItemprice(TextField itemprice) {
        this.itemprice = itemprice;
    }

    public TextField getItemcategory() {
        return itemcategory;
    }

    public void setItemcategory(TextField itemcategory) {
        this.itemcategory = itemcategory;
    }

    public TextField getItemcode() {
        return itemcode;
    }

    public void setItemcode(TextField itemcode) {
        this.itemcode = itemcode;
    }

    public Button getAddmanually() {
        return addmanually;
    }

    public void setAddmanually(Button addmanually) {
        this.addmanually = addmanually;
    }

    public Button getUsescanner() {
        return usescanner;
    }

    public void setUsescanner(Button usescanner) {
        this.usescanner = usescanner;
    }

    public MenuItem getEmployees() {
        return employees;
    }

    public void setEmployees(MenuItem employees) {
        this.employees = employees;
    }

    public TextField getAmount() {
        return amount;
    }

    public void setAmount(TextField amount) {
        this.amount = amount;
    }

    public Button getHome() {
        return home;
    }

    public void setHome(Button home) {
        this.home = home;
    }

    public Button getImage() {
        return image;
    }

    public void setImage(Button image) {
        this.image = image;
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

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public ObservableList<StockMaster> getData() {
        return data;
    }

    public void setData(ObservableList<StockMaster> data) {
        this.data = data;
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
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection finalConnection = connection;
        home.setOnAction(event -> {
            try {
                parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/panelAdmin.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        addmanually.setOnMousePressed(event -> {
            //        todo add amount input
            String name, barcode, price, quantity, category;
            name = itemname.getText().toUpperCase();
            price = itemprice.getText().toUpperCase();
            barcode = itemcode.getText().toUpperCase();
            category = itemcategory.getText().toUpperCase();
            quantity = amount.getText().toUpperCase();
            PreparedStatement preparedStatement = null;

            try {
                assert finalConnection != null;
                preparedStatement = finalConnection.prepareStatement("INSERT INTO stocks(name,itemcode,amount,category,price,image)VALUES(?,?,?,?,?,?)");
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
            try {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.setBinaryStream(6, FileUtils.openInputStream(file), length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        });
        usescanner.setOnMousePressed(event -> {

//todo using scanner
        });
        employees.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parentsstocks.getChildren().removeAll();
                try {
                    parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                }
            }
        });
        delete.setOnAction(event -> {
            StockMaster store = table.getSelectionModel().getSelectedItem();

            try {
                assert finalConnection != null;
                PreparedStatement preparedStatement = finalConnection.prepareStatement("DELETE FROM stocks WHERE id=?");

                preparedStatement.setInt(1, store.getId());
                int updated = preparedStatement.executeUpdate();
                if (updated > 0) {
//                                updated
                    data = FXCollections.observableArrayList();

                    try {
                        if (finalConnection != null) {
                            PreparedStatement statement = finalConnection.prepareStatement("SELECT * FROM stores WHERE owner=?");
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
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        ((StockMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
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
                        ((StockMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
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
                        ((StockMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
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
                        ((StockMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
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
                        ((StockMaster) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
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
            config.login.put("loggedout", true);

            try {
                parentsstocks.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
        Platform.exit();
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }



}
