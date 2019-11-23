import Controllers.UtilityClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockAlertCheck implements Runnable {
    public static void checkStocks() {
        try {
//            adding new stock alerts
            System.out.println("run bitch!!");
            String id = null;
            Connection connection = new UtilityClass().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM stocks WHERE amount < ?");
            preparedStatement.setString(1, "500");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                }
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM stockalerts where itemid=?");
            preparedStatement.setString(1, id);
            if (resultSet.isBeforeFirst()) {
                preparedStatement = connection.prepareStatement("INSERT INTO stockalerts (itemid) VALUES (?)");
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
            }

//            removing items tat have not reached alert level
            //            adding new stock alerts
            preparedStatement = connection.prepareStatement("SELECT *FROM stockalerts");
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    id = resultSet.getString("itemid");
                }
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM stocks where id=?");
            preparedStatement.setString(1, id);
            if (resultSet.isBeforeFirst()) {
                if (Integer.parseInt(resultSet.getString("amount")) >= 500) {
                    preparedStatement = connection.prepareStatement("DELETE FROM stockalerts WHERE itemid=?");
                    preparedStatement.setString(1, id);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
//        steve muema check time
        while (true) {
            checkStocks();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
