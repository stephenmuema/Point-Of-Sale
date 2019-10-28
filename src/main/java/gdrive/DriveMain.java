package gdrive;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static securityandtime.config.*;

public class DriveMain {
//
//    public static void main(String[] args) {
////                    DriveMain.driveBackupMain(driveFname, pathToFile);
//    }


    public static void driveBackupMain(String driveFname, String pathToFile) throws IOException {
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());

        // 1: Create CREDENTIALS_FOLDER
        if (!CREDENTIALS_FOLDER.exists()) {
            CREDENTIALS_FOLDER.mkdirs();

            System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
            System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");

            File source = new File(CLIENT_SECRET_FILE_NAME);
            File dest = new File(CREDENTIALS_FULL_PATH);
            if (source.isFile() && source.exists()) {
                try {
                    FileUtils.copyFile(source, dest);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                driveBackupMain(driveFname, pathToFile);
            }
        }


//        List<File> files = DriveSuperClass.getGoogleSubFolders(backUpFolderId);
//        if (files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            DriveSuperClass.driveBackup(driveFname, pathToFile);
//
//        }

        try {
            DriveSuperClass.createGoogleFile(backUpFolderId, "multipart/x-zip", driveFname, new java.io.File(pathToFile));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        System.out.println("back up made.Files available are:");
        DriveSuperClass.getGoogleSubFolders(backUpFolderId);
    }
}