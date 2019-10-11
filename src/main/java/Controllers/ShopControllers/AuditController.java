package Controllers.ShopControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.EmployeeMaster;
import MasterClasses.SalesMaster;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.site;

//made by steve
public class AuditController extends UtilityClass implements Initializable {
    public TabPane maintabpane;//maintabpane
    public Tab tabemployeeaudits;//employee audits tab

    //table of employees
    public TableView<EmployeeMaster> tableemployeelist;
    public TableColumn<EmployeeMaster, String> employeeid;
    public TableColumn<EmployeeMaster, String> employeename;
    public TableColumn<EmployeeMaster, String> employeeemail;
    //done
    //endof table
    //get search parameters
//    public TextField tf1period;
    public Button querytf1;
    //end search
//    table of sales for each selected employee
    public TableView<SalesMaster> tableemployeesales;
    public TableColumn<SalesMaster, String> employeetransid;
    public TableColumn<SalesMaster, String> transprice;
    public TableColumn<SalesMaster, String> transpaid;
    public TableColumn<SalesMaster, String> transmethod;
    public TableColumn<SalesMaster, String> transbalance;
    public TableColumn<SalesMaster, String> transcompletion;
    //
//done
//    end of table of sales per employee

    public Tab tabsalesaudits;//tab of sales
    //creation date
//    future plans tab// todo v1.2
//    public Tab tabfutureplans;// todo v1.2
//    public TextField tabfutureplansname;// todo v1.2
//    //get nameof plan// todo v1.2
//    public TextArea tabfutureplansdescription;// todo v1.2
//    //get description of plan
//    public Button tabfutureplanssubmit;// todo v1.2
//    //submit plan
//    public DatePicker tabfutureplansdateofimplementation;// todo v1.2
    //get date of implementation of plan(to be used in reminder)
    public Tab tabstockalerts;
    //tab of alerts
    public TableView stockalerttable;

    //search
//    public TextField tf2period;//get period
//    public Button querytf2;//search period todo v1.2
//        endof search
//    table for sales per category
//    public TableView <CategorySalesMasterClass> categorysalestable;
//    public TableColumn <CategorySalesMasterClass,String> categorysalesname;
//    public TableColumn <CategorySalesMasterClass,String> categorysalesid;
//    public TableColumn <CategorySalesMasterClass,String> categorysalespayout;
//    public TableColumn <CategorySalesMasterClass,String> categorysalessalesperday;
//    private ObservableList<CategorySalesMasterClass> categorySalesMasterClassObservableList = FXCollections.observableArrayList();
//todo use in v1.2
//    tableofsales of specific items
//    public TableView <ItemStocksMaster>itemsalestable;
//    public TableColumn <ItemStocksMaster,String> itemsalesid;
//    public TableColumn <ItemStocksMaster,String> itemsalesname;
//    public TableColumn <ItemStocksMaster,String> itemsalespayout;
//    public TableColumn <ItemStocksMaster,Double> itemsalessalesperday;
//    public TableColumn <ItemStocksMaster,String> itemsalessalesremainingstock;
//    private ObservableList<ItemStocksMaster> itemStocksMasterObservableList = FXCollections.observableArrayList();
//todo use in v1.2
// todo v1.2
//    end of table for sales per category
//    public Tab tabcapandcost;//tab of capital and costs incurred// todo v1.2
//    public TabPane tapaneinnercapitalandcost;// todo v1.2
//    public Tab pastcosts;//past costs// todo v1.2
//    //past costs table// todo v1.2
//    public TableView pastcoststable;// todo v1.2
//    public TableColumn pastcoststableid;// todo v1.2
//    public TableColumn pastcoststablename;// todo v1.2
//    public TableColumn pastcoststabledateadded;// todo v1.2
//    public TableColumn pastcoststableamount;// todo v1.2
//    public TableColumn pastcoststableactiveinactivestatus;// todo v1.2
//    end of past costs table
//    new costs tab
//    public Tab newcosts;// todo v1.2
//    public Button newcostssubmit;// todo v1.2
//    //insert cost
//    public TextField newcostsamount;// todo v1.2
//    //amount
//    public TextField newcostsname;// todo v1.2
//    //name
//    public TextField newcostsactiveinactivestatus;// todo v1.2
//    //status
//    public DatePicker newcostsdatecreated;// todo v1.2
    //table of alerts
    public TableColumn stockalerttableid;
    public TableColumn stockalerttablename;
    public TableColumn stockalerttabledate;
    public TableColumn stockalerttablemarkasread;
    public Tab taballaudits;
    //tab of all audits for exports or graphical viewing
    public Button exportfullreport;
    public Button exportcategoryreport;
    public Button exportemployeereport;
    public Button getdetailedgraph;
    public Button getcateegorygraph;
    public Button getemployeegraph;
    public Label clock;
    public AnchorPane panel;
    public Button topanelbutton;
    public Button tocarwashbutton;
    public Button toemployeesbutton;
    public Button logoutbutton;
    public Button tosupplierbutton;
    //db connection
    private Connection connection = getConnection();
    private ObservableList<EmployeeMaster> employeeMasterObservableList = FXCollections.observableArrayList();
    private ObservableList<SalesMaster> salesMasterObservableList = FXCollections.observableArrayList();


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
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                () -> {
                    try {
                        config.login.put("loggedout", true);

                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        config.panel.put("panel", panel);

        menuListeners();
        buttonListeners();
        navigatoryButtonListeners();
        loadTables();
        time(clock);
    }

    private void navigatoryButtonListeners() {
        topanelbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        tocarwashbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        toemployeesbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logoutbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logout();
            }
        });
        tosupplierbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    //todo change link to supplier site
                    Desktop.getDesktop().browse(new URL(site).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void menuListeners() {

//        getdocumentation.setOnAction(event -> {
//            try {
//                //todo change link to documentation page
//                Desktop.getDesktop().browse(new URL(site).toURI());
//            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void logout() {
        logout(panel);
    }

    private void buttonListeners() {

    }

    private void loadTables() {
        loadCashiersTable();
        loadCashierSalesTable();
//        loadSpecificItemsTable();// todo v1.2
//        loadCategoricalSalesTable();// todo v1.2
//        costsTableAndInput();// todo v1.2
    }


    // todo v1.2

//    private void costsTableAndInput() {
////        public TextField newcostsamount;
////    //amount
////    public TextField newcostsname;
////    //name
////    public TextField newcostsactiveinactivestatus;
////    //status
////    public DatePicker newcostsdatecreated;
//newcostssubmit.setOnMouseClicked(event -> {
//    if(newcostsamount.getText().isEmpty()||newcostsname.getText().isEmpty()||newcostsactiveinactivestatus.getText().isEmpty()||newcostsdatecreated.getConverter().toString().isEmpty()){
//        showAlert(Alert.AlertType.ERROR,panel.getScene().getWindow(),"FILL ALL FIELDS","FILL ALL FIELDS TO PROCEED");
//    }
//    else{
//        String cost=newcostsamount.getText().toUpperCase();
//        String name=newcostsname.getText().toUpperCase();
//        String status=newcostsactiveinactivestatus.getText().toUpperCase();
//        String date=newcostsdatecreated.getConverter().toString();
//        newcostsname.clear();


//        newcostsamount.clear();
//        newcostsactiveinactivestatus.clear();
//
//        if(!status.equals("active".toUpperCase())||!status.equals("inactive".toUpperCase())){
//            showAlert(Alert.AlertType.INFORMATION,panel.getScene().getWindow()
//                    ,"WRONG INPUT","STATUS CAN ONLY BE ACTIVE OR INACTIVE");
//
//        }
//        else{
//            try{
//                PreparedStatement insertCosts=connection.prepareStatement("INSERT INTO costs(name,amount,date,status)VALUES (?,?,?,?)");
//                insertCosts.setString(1,name);
//                insertCosts.setString(2,cost);
//                insertCosts.setString(3,date);
//                insertCosts.setString(4,status);
//
//                insertCosts.executeUpdate();
//            }
//            catch (SQLException e){
//                e.printStackTrace();
//            }
//        }
//
//    }
//});
//
//    }

//    private void loadSpecificItemsTable() {
////  public TableView <ItemStocksMaster>itemsalestable;
////    public TableColumn <ItemStocksMaster,String> itemsalesid;
////    public TableColumn <ItemStocksMaster,String> itemsalesname;
////    public TableColumn <ItemStocksMaster,String> itemsalespayout;
////    public TableColumn <ItemStocksMaster,Double> itemsalessalesperday;
////    public TableColumn <ItemStocksMaster,String> itemsalessalesremainingstock;
////    private ObservableList<ItemStocksMaster> itemStocksMasterObservableList = FXCollections.observableArrayList();
//
//        try {
//            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM solditems");
//            ResultSet rs=preparedStatement.executeQuery();
//            HashMap<String,Integer>exists=new HashMap<>();
//            HashMap<String,Integer>cash=new HashMap<>();
//            while(rs.next()){
//String keys=rs.getString("name");
//Integer count= Integer.valueOf(rs.getString("quantitysold"));
//Integer cashperunit= Integer.valueOf(rs.getString("price"));
//if(exists.containsKey(keys)){
//    exists.replace(keys,exists.get(keys),exists.get(keys)+count);
//    cash.replace(keys,cash.get(keys),cash.get(keys)+(count*cashperunit));
//
//}else{
//    exists.put(keys,count);
//    cash.put(keys,cashperunit*count);
//}
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        itemsalestable.setItems(itemStocksMasterObservableList);
//        assert itemsalestable != null : "fx:id=\"tab\" was not injected: check your FXML ";
//        itemsalesname.setCellValueFactory(
//                new PropertyValueFactory<>("Name"));
//        itemsalesid.setCellValueFactory(
//                new PropertyValueFactory<>("Id"));
//        itemsalespayout.setCellValueFactory(new PropertyValueFactory<>("payout"));
//        itemsalessalesperday.setCellValueFactory(new PropertyValueFactory<>("salesperday"));
//        itemsalessalesremainingstock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//
//        itemsalestable.refresh();
//    }
// todo v1.2

    //    private void loadCategoricalSalesTable() {
//
///*
//         public TableView <CategorySalesMasterClass> categorysalestable;
//    public TableColumn <CategorySalesMasterClass,String> categorysalesname;
//    public TableColumn <CategorySalesMasterClass,String> categorysalesid;
//    public TableColumn <CategorySalesMasterClass,String> categorysalespayout;
//    public TableColumn <CategorySalesMasterClass,String> categorysalessalesperday;
// done with these
//fetch from sold,group by category,display by category
//use a map for category name and sring name
//use a map for category id and sring id
//use a map for category payout and sring payout
//use a map for category avgsalesperday and sring sales/day value
//hold these map objects in a list
//*/
////first insert into tableofcategoricalsales
//
//
//        try {
//            if (connection != null) {
//                PreparedStatement statement = connection.prepareStatement("SELECT * FROM solditems group by category");
////                                statement.setString(1, String.valueOf(key.get("key")));
////                                statement.setBoolean(2, false);
//                ResultSet resultSet = statement.executeQuery();
//                while (resultSet.next()) {
//                    CategorySalesMasterClass selectedCategoricalSale = new CategorySalesMasterClass();
////                    todo continue tomorrow
////                    selectedCategoricalSale.setName(resultSet.getString("employeename"));
////                    selectedCategoricalSale.setEmail(resultSet.getString("email"));
////                    selectedCategoricalSale.setId(resultSet.getString("id"));
//                    categorySalesMasterClassObservableList.add(selectedCategoricalSale);
//                }
//                categorysalestable.setItems(categorySalesMasterClassObservableList);
//
//
//
//                assert categorysalestable != null : "fx:id=\"categorysalestable\" was not injected: check your FXML ";
//                categorysalesname.setCellValueFactory(
//                        new PropertyValueFactory<>("categorysalesname"));
//                categorysalesid.setCellValueFactory(
//                        new PropertyValueFactory<>("categorysalesid"));
//                categorysalespayout.setCellValueFactory(new PropertyValueFactory<>("categorysalespayout"));
//                categorysalessalesperday.setCellValueFactory(new PropertyValueFactory<>("categorysalessalesperday"));
//                categorysalestable.refresh();
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//todo add v1.2// todo v1.2
    private void loadCashiersTable() {
        try {
//                        DISPLAYING EMPLOYEES
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE admin=? and status=? and activated=?");

                statement.setBoolean(1, false);
                statement.setString(2, "active");
                statement.setInt(3, 1);
//                                statement.setBoolean(2, false);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    EmployeeMaster employeeMaster = new EmployeeMaster();
                    employeeMaster.setName(resultSet.getString("employeename"));
                    employeeMaster.setEmail(resultSet.getString("email"));
                    employeeMaster.setId(resultSet.getString("id"));
                    employeeMasterObservableList.add(employeeMaster);
                }
                tableemployeelist.setItems(employeeMasterObservableList);


                assert tableemployeelist != null : "fx:id=\"tab\" was not injected: check your FXML ";
                employeename.setCellValueFactory(
                        new PropertyValueFactory<>("Name"));
                employeeid.setCellValueFactory(
                        new PropertyValueFactory<>("Id"));
                employeeemail.setCellValueFactory(new PropertyValueFactory<>("email"));
                tableemployeelist.refresh();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCashierSalesTable() {
        tableemployeelist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                salesMasterObservableList.clear();
                EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=?");
                    preparedStatement.setString(1, selectedEmployee.getEmail());
                    ResultSet salesResultSet = preparedStatement.executeQuery();
                    while (salesResultSet.next()) {


                        SalesMaster salesMaster = new SalesMaster();
                        salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                        salesMaster.setTransbalance(salesResultSet.getString("balance"));
                        salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                        salesMaster.setTransmethod(salesResultSet.getString("method"));
                        salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                        salesMaster.setTransprice(salesResultSet.getString("cash"));
                        salesMasterObservableList.add(salesMaster);
                    }
                    tableemployeesales.setItems(salesMasterObservableList);

                    assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                    transprice.setCellValueFactory(
                            new PropertyValueFactory<>("transprice"));
                    employeetransid.setCellValueFactory(
                            new PropertyValueFactory<>("employeetransid"));
                    transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                    transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                    transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                    transcompletion.setCellValueFactory(new PropertyValueFactory<>("transcompletion"));
                    tableemployeelist.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        });


    }

    //    fixme template pdf generator
    private void pdfGenTemplate() {
        Document document = new Document(PageSize.A4_LANDSCAPE, 20, 20, 20, 20);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = timestamp.getTime() + "employeesreport.pdf";
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
            statement = connection.prepareStatement("SELECT * FROM users  ");
//            WHERE subscriberkey=? and admin=?
//            statement.setString(1, key.get("key"));
//            statement.setBoolean(2, false);
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


    public TabPane getMaintabpane() {
        return maintabpane;
    }

    public void setMaintabpane(TabPane maintabpane) {
        this.maintabpane = maintabpane;
    }

    public Tab getTabemployeeaudits() {
        return tabemployeeaudits;
    }

    public void setTabemployeeaudits(Tab tabemployeeaudits) {
        this.tabemployeeaudits = tabemployeeaudits;
    }

    public TableView<EmployeeMaster> getTableemployeelist() {
        return tableemployeelist;
    }

    public void setTableemployeelist(TableView<EmployeeMaster> tableemployeelist) {
        this.tableemployeelist = tableemployeelist;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(TableColumn<EmployeeMaster, String> employeeid) {
        this.employeeid = employeeid;
    }

    public TableColumn<EmployeeMaster, String> getEmployeename() {
        return employeename;
    }

    public void setEmployeename(TableColumn<EmployeeMaster, String> employeename) {
        this.employeename = employeename;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeemail() {
        return employeeemail;
    }

    public void setEmployeeemail(TableColumn<EmployeeMaster, String> employeeemail) {
        this.employeeemail = employeeemail;
    }

    public ObservableList<EmployeeMaster> getEmployeeMasterObservableList() {
        return employeeMasterObservableList;
    }

    public void setEmployeeMasterObservableList(ObservableList<EmployeeMaster> employeeMasterObservableList) {
        this.employeeMasterObservableList = employeeMasterObservableList;
    }


    public Button getQuerytf1() {
        return querytf1;
    }

    public void setQuerytf1(Button querytf1) {
        this.querytf1 = querytf1;
    }

    public TableView<SalesMaster> getTableemployeesales() {
        return tableemployeesales;
    }

    public void setTableemployeesales(TableView<SalesMaster> tableemployeesales) {
        this.tableemployeesales = tableemployeesales;
    }

    public TableColumn<SalesMaster, String> getEmployeetransid() {
        return employeetransid;
    }

    public void setEmployeetransid(TableColumn<SalesMaster, String> employeetransid) {
        this.employeetransid = employeetransid;
    }

    public TableColumn<SalesMaster, String> getTransprice() {
        return transprice;
    }

    public void setTransprice(TableColumn<SalesMaster, String> transprice) {
        this.transprice = transprice;
    }

    public TableColumn<SalesMaster, String> getTranspaid() {
        return transpaid;
    }

    public void setTranspaid(TableColumn<SalesMaster, String> transpaid) {
        this.transpaid = transpaid;
    }

    public TableColumn<SalesMaster, String> getTransmethod() {
        return transmethod;
    }

    public void setTransmethod(TableColumn<SalesMaster, String> transmethod) {
        this.transmethod = transmethod;
    }

    public TableColumn<SalesMaster, String> getTransbalance() {
        return transbalance;
    }

    public void setTransbalance(TableColumn<SalesMaster, String> transbalance) {
        this.transbalance = transbalance;
    }

    public TableColumn<SalesMaster, String> getTranscompletion() {
        return transcompletion;
    }

    public void setTranscompletion(TableColumn<SalesMaster, String> transcompletion) {
        this.transcompletion = transcompletion;
    }

    public ObservableList<SalesMaster> getSalesMasterObservableList() {
        return salesMasterObservableList;
    }

    public void setSalesMasterObservableList(ObservableList<SalesMaster> salesMasterObservableList) {
        this.salesMasterObservableList = salesMasterObservableList;
    }


    public Tab getTabsalesaudits() {
        return tabsalesaudits;
    }

    public void setTabsalesaudits(Tab tabsalesaudits) {
        this.tabsalesaudits = tabsalesaudits;
    }

    public Tab getTabstockalerts() {
        return tabstockalerts;
    }

    public void setTabstockalerts(Tab tabstockalerts) {
        this.tabstockalerts = tabstockalerts;
    }

    public TableView getStockalerttable() {
        return stockalerttable;
    }

    public void setStockalerttable(TableView stockalerttable) {
        this.stockalerttable = stockalerttable;
    }

    public TableColumn getStockalerttableid() {
        return stockalerttableid;
    }

    public void setStockalerttableid(TableColumn stockalerttableid) {
        this.stockalerttableid = stockalerttableid;
    }

    public TableColumn getStockalerttablename() {
        return stockalerttablename;
    }

    public void setStockalerttablename(TableColumn stockalerttablename) {
        this.stockalerttablename = stockalerttablename;
    }

    public TableColumn getStockalerttabledate() {
        return stockalerttabledate;
    }

    public void setStockalerttabledate(TableColumn stockalerttabledate) {
        this.stockalerttabledate = stockalerttabledate;
    }

    public TableColumn getStockalerttablemarkasread() {
        return stockalerttablemarkasread;
    }

    public void setStockalerttablemarkasread(TableColumn stockalerttablemarkasread) {
        this.stockalerttablemarkasread = stockalerttablemarkasread;
    }

    public Tab getTaballaudits() {
        return taballaudits;
    }

    public void setTaballaudits(Tab taballaudits) {
        this.taballaudits = taballaudits;
    }

    public Button getExportfullreport() {
        return exportfullreport;
    }

    public void setExportfullreport(Button exportfullreport) {
        this.exportfullreport = exportfullreport;
    }

    public Button getExportcategoryreport() {
        return exportcategoryreport;
    }

    public void setExportcategoryreport(Button exportcategoryreport) {
        this.exportcategoryreport = exportcategoryreport;
    }

    public Button getExportemployeereport() {
        return exportemployeereport;
    }

    public void setExportemployeereport(Button exportemployeereport) {
        this.exportemployeereport = exportemployeereport;
    }

    public Button getGetdetailedgraph() {
        return getdetailedgraph;
    }

    public void setGetdetailedgraph(Button getdetailedgraph) {
        this.getdetailedgraph = getdetailedgraph;
    }

    public Button getGetcateegorygraph() {
        return getcateegorygraph;
    }

    public void setGetcateegorygraph(Button getcateegorygraph) {
        this.getcateegorygraph = getcateegorygraph;
    }

    public Button getGetemployeegraph() {
        return getemployeegraph;
    }

    public void setGetemployeegraph(Button getemployeegraph) {
        this.getemployeegraph = getemployeegraph;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }


    public AnchorPane getPanel() {
        return panel;
    }

    public void setPanel(AnchorPane panel) {
        this.panel = panel;
    }

    public Button getTopanelbutton() {
        return topanelbutton;
    }

    public void setTopanelbutton(Button topanelbutton) {
        this.topanelbutton = topanelbutton;
    }

    public Button getTocarwashbutton() {
        return tocarwashbutton;
    }

    public void setTocarwashbutton(Button tocarwashbutton) {
        this.tocarwashbutton = tocarwashbutton;
    }

    public Button getToemployeesbutton() {
        return toemployeesbutton;
    }

    public void setToemployeesbutton(Button toemployeesbutton) {
        this.toemployeesbutton = toemployeesbutton;
    }

    public Button getLogoutbutton() {
        return logoutbutton;
    }

    public void setLogoutbutton(Button logoutbutton) {
        this.logoutbutton = logoutbutton;
    }

    public Button getTosupplierbutton() {
        return tosupplierbutton;
    }

    public void setTosupplierbutton(Button tosupplierbutton) {
        this.tosupplierbutton = tosupplierbutton;
    }


    @Override
    public AuditController setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }
}
