package securityandtime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Aes encryption
 */

public class Security {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b) {
            sb.append(Integer.toHexString(b1 & 0xff));
        }
        ////System.out.println(sb.toString());
        return sb.toString();
    }
}