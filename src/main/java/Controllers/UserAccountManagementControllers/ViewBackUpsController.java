package Controllers.UserAccountManagementControllers;

import MasterClasses.BackUpFilesMaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static securityandtime.config.sysconfig;

public class ViewBackUpsController implements Initializable {

    @FXML
    private TableView<BackUpFilesMaster> backupFiles;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileName;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileSize;
    @FXML
    private TableColumn<BackUpFilesMaster, String> fileCreated;
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

    private ObservableList<BackUpFilesMaster> data;

    private static String getFileSizeMegaBytes(File file) {
        DecimalFormat df2 = new DecimalFormat("#.####");

        return df2.format((double) file.length() / (1024 * 1024)) + " mb";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        menuClicked();
        buttonPressed();
    }

    private void init() {
        data = FXCollections.observableArrayList();

        gegtFiles((String) sysconfig.get("backUpLoc"));
    }

    private void buttonPressed() {

    }

    private void menuClicked() {
    }

    private void gegtFiles(String dir) {
        File directory = new File(dir);
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        if (files != null) {
            for (File file : files) {
                BasicFileAttributes attrs = null;
                try {
                    attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileTime time = attrs.creationTime();

                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String lastModified = simpleDateFormat.format(new Date(time.toMillis()));
                String name = file.getName();
                String path = file.getAbsolutePath();
                String size = getFileSizeMegaBytes(file);
                BackUpFilesMaster backUpFilesMaster = new BackUpFilesMaster();
                backUpFilesMaster.setDate(lastModified);
                backUpFilesMaster.setName(name);
                backUpFilesMaster.setSize(size);
                backUpFilesMaster.setPath(path);
                data.add(backUpFilesMaster);
            }
            backupFiles.setItems(data);
        }
        fileCreated.setCellValueFactory(new PropertyValueFactory<>("date"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fileSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        backupFiles.refresh();

    }

}
