package MasterClasses;

import java.awt.*;
import java.awt.print.*;
import java.sql.*;

import static securityandtime.config.localCartDb;

public class ReceiptMasterClass {

    private Connection connectionDbLocal;

    {
        try {
            connectionDbLocal = DriverManager.getConnection(localCartDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Statement statementLocal;

    {
        try {
            assert false;
            statementLocal = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet resultSet;

    {
        try {
            resultSet = statementLocal.executeQuery("SELECT itemname,itemprice,amount,cumulativeprice FROM cartItems");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReceiptMasterClass() {
        StringBuffer billBuffer = new StringBuffer();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                billBuffer.append(resultSet.getString("itemname")).append("\t\t    ").
                        append(resultSet.getString("itemprice")).append("\t\t    ").
                        append(resultSet.getString("amount")).append("\t\t    ").
                        append(resultSet.getString("cumulativeprice")).append("\n\n");
                billBuffer.append(System.getProperty("line.separator"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        printCard(billBuffer.toString());
    }

    public boolean printCard(final String bill) {
        final PrinterJob job = PrinterJob.getPrinterJob();


        Printable contentToPrint = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {


                Graphics2D g2d = (Graphics2D) graphics;

                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Algerian", Font.BOLD, 10));

                String[] billz = bill.split(";");
                int y = 15;
                //draw each String in a separate line
                for (String s : billz) {
                    graphics.drawString(s, 5, y);
                    y = y + 15;
                }

                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                } //Only one page


                return PAGE_EXISTS;
            }


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
        return true;
    }

}
