import Controllers.UtilityClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockAlertCheck implements Runnable {
    private static void checkStocks() {
        try {
//            adding new stock alerts
//            System.out.println("run bitch!!");
            String id = null;
            Connection connection = new UtilityClass().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM stocks WHERE amount < ?");
            preparedStatement.setInt(1, 50);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {

                while (resultSet.next()) {
                    id = resultSet.getString("id");
                }
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM stockalerts where itemid=?");
            preparedStatement.setString(1, id);
            if (checkId(Integer.parseInt(id))) {
                System.out.println("exists");
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO stockalerts (itemid) VALUES (?)");
                preparedStatement.setString(1, id);
//                System.out.println("run bitch");

                preparedStatement.executeUpdate();
            }
//            removing items that have not reached alert level
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
            resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    if (resultSet.getInt("amount") >= 50) {
                        preparedStatement = connection.prepareStatement("DELETE FROM stockalerts WHERE itemid=? OR itemid is null ");
                        preparedStatement.setString(1, id);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkId(int itemid) throws IOException, SQLException {
        String sql = "Select 1 from stockalerts where itemid = ?";

        Connection connection = new UtilityClass().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(itemid));
        ResultSet rs = ps.executeQuery();

        return rs.next();
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
