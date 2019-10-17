package gdrive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;

import static securityandtime.config.*;

public class DriveMain {

    public static void main(String[] args) {
        //            DriveMain.driveBackupMain(driveFname, pathToFile);
    }


    public static void driveBackupMain(String driveFname, String pathToFile) throws IOException {
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());

        // 1: Create CREDENTIALS_FOLDER
        if (!CREDENTIALS_FOLDER.exists()) {
            CREDENTIALS_FOLDER.mkdirs();

            System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
            System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
            return;
        }


        List<File> files = DriveSuperClass.getGoogleSubFolders(backUpFolderId);
        if (files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                File folder = DriveSuperClass.createGoogleFolder(backUpFolderId, "EXAMPLE COMPANY NAME");
                System.out.println("Created folder with id= " + folder.getId());
                //insert folder id to settings
                System.out.println("name= " + folder.getName());
                boolean folderExists = false;
                if (file.getId().equals("id from table")) {
                    DriveSuperClass.driveBackup(driveFname, pathToFile);
                    folderExists = true;
                }
                if (!folderExists) {
                    DriveSuperClass.createGoogleFolder(backUpFolderId, " company name from table");
                    driveBackupMain(driveFname, pathToFile);
                }
                //perform file backup


            }
        }


    }
}