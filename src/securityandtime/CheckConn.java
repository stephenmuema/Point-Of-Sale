package securityandtime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

public class CheckConn {
    String[] time;
    int hour, min, sec;
    String ampm;

    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public static Date timelogin() throws IOException {
//        if(CheckConn.pingHost("google.com", 80, 2000)){
////            System.out.println(CheckConn.pingHost(securityandtime.config.host, 80, 2000));
//            String TIME_SERVER = "time-a.nist.gov";
//            NTPUDPClient timeClient = new NTPUDPClient();
//            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
//            TimeInfo timeInfo = timeClient.getTime(inetAddress);
//            long returnTime = timeInfo.getReturnTime();
//            Date time = new Date(returnTime);
////            System.out.println("Time from " + TIME_SERVER + ": " + time);
//            Date date = new Date();
//            if (time!=date){
//
//// set system time
//            }
//            return time;
//        }
//        else {
//            Date time = new Date();
////            System.out.println(time.toString());
//            return time;
//        }
        Date time = new Date();
//            System.out.println(time.toString());
        return time;
    }
//        public static void main(String args[]) throws IOException {
//
//        }

}
