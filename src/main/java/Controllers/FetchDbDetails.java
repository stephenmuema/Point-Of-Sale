package Controllers;

import securityandtime.AesCrypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FetchDbDetails {
    private static String dbName = "";
    private static String dbPass = "";
    private static String dbUser = "";
    private static String dbPort = "";
    private static String dbHost = "";
    private static Map<String, String> connectionMap = new LinkedHashMap<>();
    private static String[] dbdetails;
    private static String[] des;
    FetchDbDetails() throws IOException {
////        System.out.println(readFile());
        InputStream is = new FileInputStream(securityandtime.config.pathToDbSettings);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String str = "";
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();

        try {
            str = new AesCrypto().decrypt(fileAsString);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        setDbUser(str.split("::")[2]);
//        System.out.println(getDbUser());
        setDbPass(str.split("::")[1]);
//        System.out.println(getDbPass());
        setDbPort(str.split("::")[3]);
//        System.out.println(getDbPort());
        setDbHost(str.split("::")[0]);
//        System.out.println(getDbHost());
        setDbName(readDbFile());
//        System.out.println(getDbName());
        dbdetails = new String[]{dbHost + "/", dbUser, dbPass, "jdbc:mysql://", dbName + "?zeroDateTimeBehavior=convertToNull", dbPort};
        des = new String[]{dbdetails[1], dbdetails[2], dbdetails[3] + dbdetails[0] + dbdetails[4]};

    }


    private String readDbFile() throws IOException {
        InputStream is = new FileInputStream(securityandtime.config.pathToPOSSettings);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String str = "";
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line);
            line = buf.readLine();
        }

        String fileAsString = sb.toString();


        return fileAsString;
    }

    private String readFile() throws IOException {
        InputStream is = new FileInputStream(securityandtime.config.pathToDbSettings);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String str = "";
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();

        try {
            str = new AesCrypto().decrypt(fileAsString);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        setDbUser(str.split("::")[2]);
//        System.out.println(getDbUser());
        setDbPass(str.split("::")[1]);
//        System.out.println(getDbPass());
        setDbPort(str.split("::")[3]);
//        System.out.println(getDbPort());
        setDbHost(str.split("::")[0]);
//        System.out.println(getDbHost());
        setDbName(readDbFile());
//        System.out.println(getDbName());
        return str;
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
