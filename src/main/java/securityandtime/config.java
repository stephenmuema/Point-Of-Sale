package securityandtime;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.*;

public interface config {//host,user,password,des,port
    String environment = "development";
    //    String environment = "production";
    Properties mailProp = new Properties();


    String host = "smtp.gmail.com";
    String from = "muemasnyamai@gmail.com";
    String adminEmail = "muemasn@outlook.com";
    String alternativeBackupEmail = "muemasnyamai@gmail.com";
    HashMap<String, Boolean> login = new HashMap<>();
    HashMap<String, String> user = new HashMap<>();
    String mailPassword = "tpgkhylqyxiypqld";
    IdentityHashMap<String, String> key = new IdentityHashMap<>();
    HashMap<String, String> cartid = new HashMap<String, String>();
    HashMap<String, Integer> pricegot = new HashMap<String, Integer>();
    HashMap<String, Throwable> throwables = new HashMap<>();
    HashMap<String, String> license = new HashMap<>();
    String site = "https://nanotechsoftwares.co.ke";
    String siteLicensing = "https://nanotechsoftwares.co.ke/licensing";
    String sitedocs = "https://nanotechsoftwares.co.ke/docs";
    String supplierSite = "https://nanotechsoftwares.co.ke/suppliers";
    HashMap<String, String> action = new HashMap<>();
    HashMap<String, AnchorPane> panel = new HashMap<>();
    HashMap<String, Object> sysconfig = new HashMap<>();
    String google = "http://www.google.com";
    String encryptionkey = "0123456789abcdef";
    String initVector = "abcdef9876543210";
    String NANOTECHSOFTWARES_POS_SOLUTIONS = "NANOTECHSOFTWARES POS SOLUTIONS";
    String year = " 2019 ";
    String version = " (V 1.1) ";
    String fileSavePath = "D:\\NANOTECHSOFTWARES\\nanotechPOS\\";
    String licensepath = fileSavePath + "licenses\\license.dll";
    String dbConnFile = "nanotechDb.dll";
    String supportFile = "nanotechSupportGroup.dll";
    String localCartDb = "jdbc:sqlite:" + fileSavePath + "files\\shoppingLocal.db";
    String aesKey = "26kozQaKwRuNJ24t26kozQaKwRuNJ24t";
    Map<String, Boolean> networkConnectionMap = new HashMap<>();
    //    File nanotechLogoPos = ;
    Image logoImageNanotechPos = new Image("images/logo.png");

    Image companyLogoImageObj = new Image(new File(fileSavePath + "\\images\\logo.png").toURI().toString());

    //drive settings
    String backUpFolderId = "1KeAMwOXSQiZvWVwFs5DmtcwKgWGZNeEX";
    String APPLICATION_NAME = "Google Drive API Java Quickstart";

    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Directory to store user credentials for this application.
    java.io.File CREDENTIALS_FOLDER = new File(fileSavePath + "credentials");

    String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    //
    // Global instance of the scopes required by this quickstart. If modifying these
    // scopes, delete your previously saved credentials/ folder.
    //
    List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    String credentialFile = CREDENTIALS_FOLDER + "\\StoredCredential";
    String NANOTECHSOFTWARES_SETTINGS = "D:\\NANOTECHSOFTWARES\\settings\\";
    String pathToDbSettings = NANOTECHSOFTWARES_SETTINGS + "DbConf.dll";
    String pathToPOSSettings = NANOTECHSOFTWARES_SETTINGS + "POSConf.dll";
    String pathToMEDICASettings = NANOTECHSOFTWARES_SETTINGS + "MEDICAConf.dll";

}
