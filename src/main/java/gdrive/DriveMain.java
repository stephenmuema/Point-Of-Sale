package gdrive;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;

import static securityandtime.config.*;

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
            return;
        }


        List<File> files = DriveSuperClass.getGoogleSubFolders(backUpFolderId);
        if (files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            DriveSuperClass.driveBackup(driveFname, pathToFile);

        }


    }
}