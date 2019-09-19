import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static securityandtime.config.networkConnectionMap;

public class NetworkCheck implements Runnable {
    private static void pingGoogle() {
        try {
            URL url = new URL(config.google);
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (IOException e) {
            networkConnectionMap.put("Internet", false);
            networkConnectionMap.put("server", false);
            System.out.println("Internet is not connected");
        }
    }

    /**
     * created by steve muema
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
//        steve muema check time
        while (true) {
            pingGoogle();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

