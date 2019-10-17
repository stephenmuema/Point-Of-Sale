package gdrive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static securityandtime.config.CLIENT_SECRET_FILE_NAME;
import static securityandtime.config.CREDENTIALS_FOLDER;

public class DriveMain {

    public static void main(String[] args) {
//                    DriveMain.driveBackupMain(driveFname, pathToFile);
    }


    public static void driveBackupMain(String driveFname, String pathToFile) throws IOException {
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());

        // 1: Create CREDENTIALS_FOLDER
        if (!CREDENTIALS_FOLDER.exists()) {
            CREDENTIALS_FOLDER.mkdirs();

            System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
            System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
            driveBackupMain(driveFname, pathToFile);
        }


//        List<File> files = DriveSuperClass.getGoogleSubFolders(backUpFolderId);
//        if (files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            DriveSuperClass.driveBackup(driveFname, pathToFile);
//
//        }

        try {
            DriveSuperClass.createGoogleFile(null, "multipart/x-zip", driveFname, new java.io.File(pathToFile));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }
}