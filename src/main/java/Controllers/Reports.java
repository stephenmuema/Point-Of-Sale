package Controllers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static securityandtime.config.fileSavePath;

public class Reports {
    ArrayList<File> reports = new ArrayList<>();
    String filesDest = fileSavePath + "files\\";

    public ArrayList<File> getReports() {
        return reports;
    }

    public void setReports(ArrayList<File> reports) {
        this.reports = reports;
    }

    public File staffReport() {
        File f = null;
        reports.add(f);
        return f;
    }

    public File staffSalesReport() {
        File f = null;
        reports.add(f);
        return f;
    }

    public File salesReport() {
        File f = null;
        reports.add(f);
        return f;
    }

    public File TopSalesReport() {
        File f = null;
        reports.add(f);
        return f;
    }

    public File PatientReports() {
        File f = null;
        reports.add(f);
        return f;
    }

    public File PharmacyAcquisitionReport() {
        File f = null;
        reports.add(f);
        return f;
    }

    public void sendReportsToMail(String subject, String to, String from, String type, String password) throws MessagingException {
        LocalDate myObj = LocalDate.now();
        String time = String.valueOf(myObj);
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        // creates a new e-mail message
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        InternetAddress[] toAddresses = {new InternetAddress(to)};
        message.setRecipients(Message.RecipientType.TO, toAddresses);
        message.setSubject(subject);
        message.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (reports != null && reports.size() > 0) {
            for (File filePath : reports) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath.getAbsolutePath());
                    filePath.renameTo(new File(filesDest + new Date() + filePath.getName()));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // sets the multi-part as e-mail's content
        message.setContent(multipart);
        message.setContent(multipart);
        Transport.send(message);

    }

    public boolean saveReports() throws IOException {
        boolean val = false;

        if (reports != null && reports.size() > 0) {
            for (File filePath : reports) {
                filePath.renameTo(new File(filesDest + new Date() + filePath.getName()));


            }
            val = true;
        }
        return val;
    }
}

