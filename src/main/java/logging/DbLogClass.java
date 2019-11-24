package logging;

import Controllers.UtilityClass;
import securityandtime.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbLogClass {
    //    user action category exportvalue comment
    public static void systemLogDb(String action, String category, boolean exportvalue, String comment) {
        String user = config.user.get("user");

        Connection connection = null;
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO security_logs(user, action, category, comment,exportvalue) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, action);
            preparedStatement.setString(3, category);
            preparedStatement.setString(4, comment);
            preparedStatement.setBoolean(5, exportvalue);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
