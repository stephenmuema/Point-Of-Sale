package MasterClasses;

import java.awt.*;
import java.awt.print.*;
import java.sql.*;

import static securityandtime.config.localCartDb;

public class ReceiptMasterClass {

    ResultSet resultSet;
    private Connection connectionDbLocal;
    private Statement statementLocal;

    {
        try {
            connectionDbLocal = DriverManager.getConnection(localCartDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            assert false;
            statementLocal = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            resultSet = statementLocal.executeQuery("SELECT itemname,itemprice,amount,cumulativeprice FROM cartItems");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReceiptMasterClass() {
        StringBuilder billStringBuilder = new StringBuilder();
        billStringBuilder.append("NAME").append("\t\t    ").
                append("PRICE").append("\t\t    ").
                append("AMOUNT").append("\t\t    ").
                append("PRICE").append("\n\n");
//        billStringBuilder.append(System.getProperty("line.separator"));
        billStringBuilder.append("\n");
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                billStringBuilder.append(resultSet.getString("itemname")).append("\t\t    ").
                        append(resultSet.getString("itemprice")).append("\t\t    ").
                        append(resultSet.getString("amount")).append("\t\t    ").
                        append(resultSet.getString("cumulativeprice")).append("\n\n");
                billStringBuilder.append(System.getProperty("line.separator"));
                billStringBuilder.append("\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        printCard(billStringBuilder.toString());
    }

    private void printCard(final String bill) {
        final PrinterJob job = PrinterJob.getPrinterJob();


        Printable contentToPrint = (graphics, pageFormat, pageIndex) -> {


            Graphics2D g2d = (Graphics2D) graphics;

            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(new Font("Algerian", Font.BOLD, 10));

            String[] billz = bill.split("\n");
            int y = 15;
            //draw each String in a separate line
            for (String s : billz) {
                graphics.drawString(s, 5, y);
                y = y + 15;
            }

            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            } //Only one page


            return Printable.PAGE_EXISTS;
        };
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        Paper pPaper = pageFormat.getPaper();

        pPaper.setSize(8000, 10000000);
        pPaper.setImageableArea(0, 0, pPaper.getWidth(), pPaper.getHeight() - 2);
        pageFormat.setPaper(pPaper);

        job.setPrintable(contentToPrint, pageFormat);


        try {
            job.print();

        } catch (PrinterException e) {
            System.err.println(e.getMessage());
        }
    }

}
