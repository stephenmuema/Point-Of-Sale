package Controllers;

import securityandtime.AesCrypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static securityandtime.config.dbConnFile;
import static securityandtime.config.encryptionkey;

public class FetchDbDetails {
    static String dbName = "nanotech_pos";
    static String dbPass = "nanotech";
    static String dbUser = "root";
    static String dbPort = "3306";
    static String dbHost = "127.0.0.1";
    static Map<String, String> connectionMap = new LinkedHashMap<>();
    static String[] dbdetails = {dbHost + "/", dbUser, dbPass, "jdbc:mysql://", dbName + "?zeroDateTimeBehavior=convertToNull", dbPort};
    static String[] des = {dbdetails[1], dbdetails[2], dbdetails[3] + dbdetails[0] + dbdetails[4]};


    FetchDbDetails() throws IOException {
        File file = new File(dbConnFile);
        boolean exists = file.exists();
        if (file.exists()) {

            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] fileContent = new byte[(int) file.length()];

            int i = fileInputStream.read(fileContent);
//            System.out.println("bytes read are " + i);
            StringBuilder builderEnc = new StringBuilder();
            StringBuilder builder = new StringBuilder();

            for (byte b : fileContent
            ) {
                builderEnc.append((char) b);
            }
            String decrypt = AesCrypto.decrypt(encryptionkey, builderEnc.toString());
        }
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        FetchDbDetails.dbName = dbName;
    }

    public static String getDbPass() {
        return dbPass;
    }

    public static void setDbPass(String dbPass) {
        FetchDbDetails.dbPass = dbPass;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static void setDbUser(String dbUser) {
        FetchDbDetails.dbUser = dbUser;
    }

    public static String getDbPort() {
        return dbPort;
    }

    public static void setDbPort(String dbPort) {
        FetchDbDetails.dbPort = dbPort;
    }

    public static String getDbHost() {
        return dbHost;
    }

    public static void setDbHost(String dbHost) {
        FetchDbDetails.dbHost = dbHost;
    }

    public static Map<String, String> getConnectionMap() {
        return connectionMap;
    }

    public static void setConnectionMap(Map<String, String> connectionMap) {
        FetchDbDetails.connectionMap = connectionMap;
    }

    public static String[] getDbdetails() {
        return dbdetails;
    }

    public static void setDbdetails(String[] dbdetails) {
        FetchDbDetails.dbdetails = dbdetails;
    }

    public static String[] getDes() {
        return des;
    }

    public static void setDes(String[] des) {
        FetchDbDetails.des = des;
    }
}
