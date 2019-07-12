package Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import securityandtime.CheckConn;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilityClass {
    public void time(Label clock) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String mins = null, hrs = null, secs = null, pmam = null;
            try {
                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

                if (hours >= 12) {
//                    hrs= "0"+String.valueOf(hours-12);
                    pmam = "PM";
                } else {
                    pmam = "AM";

                }
                if (minutes > 9) {
                    mins = String.valueOf(minutes);
                } else {
                    mins = "0" + String.valueOf(minutes);

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + String.valueOf(seconds);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) + ":" + (secs) + " " + pmam);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    protected void timeMain(Label clock) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            //                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
//                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
//                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

            //                    hrs= "0"+String.valueOf(hours-12);
            //            display time
            clock.setText(String.valueOf(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a").format(Calendar.getInstance().getTime())));
            //                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) +":" + (secs)+ " " + pmam);
        }),
                new KeyFrame(Duration.seconds(1))
        );
//        refresh every one second
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
