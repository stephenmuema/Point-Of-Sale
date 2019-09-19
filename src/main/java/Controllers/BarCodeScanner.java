package Controllers;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class BarCodeScanner extends Thread {
    private String barcode;


    private static SerialPort serialPort;


    private static String scan() {
        AtomicReference<String> code = new AtomicReference<>("");
        System.out.println("hello");
        serialPort = new SerialPort("COM5");
        try {
            if (serialPort.isOpened()) {
                serialPort.closePort();
            }
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(
                    new PortReader(buffer -> Platform.runLater(() -> {
//                        System.out.println(buffer);
                        code.set(buffer);
                    })),
                    SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            ex.getMessage();
        }
        return code.get();
    }

    public String launchScanner() {
        Runnable target;
        Thread thread = new Thread(() -> {
            while (true) {
                barcode = scan();
                System.out.println(barcode);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return barcode;
    }

    static class PortReader implements SerialPortEventListener {
        private final Consumer<String> textHandler;

        PortReader(Consumer<String> textHandler) {
            this.textHandler = textHandler;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                try {
                    String buffer = serialPort.readString();
                    textHandler.accept(buffer);
                } catch (SerialPortException ex) {
                    ex.getMessage();
                }
            }
        }
    }
}

