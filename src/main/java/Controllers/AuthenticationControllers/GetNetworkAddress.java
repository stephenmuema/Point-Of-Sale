package Controllers.AuthenticationControllers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

//made by steve
class GetNetworkAddress {

    static InetAddress GetIpAddress() {
        InetAddress ip = null;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());


        } catch (Exception e) {

            e.printStackTrace();

        }
        assert ip != null;
        return ip;
    }

    static String getMacAddress(InetAddress ip) throws SocketException {
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);

        byte[] mac = network.getHardwareAddress();

        System.out.print("Current MAC address : ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

}
