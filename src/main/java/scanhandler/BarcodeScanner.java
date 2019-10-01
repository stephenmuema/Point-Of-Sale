package scanhandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BarcodeScanner {

    private static final long THRESHOLD = 100;
    private static final int MIN_BARCODE_LENGTH = 8;
    private final StringBuffer barcode = new StringBuffer();
    private final List<BarcodeListener> listeners = new CopyOnWriteArrayList<>();
    private long lastEventTimeStamp = 0L;

    public BarcodeScanner() {
// todo implement in code
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_RELEASED) {
                return false;
            }

            if (e.getWhen() - lastEventTimeStamp > THRESHOLD) {
                barcode.delete(0, barcode.length());
            }

            lastEventTimeStamp = e.getWhen();

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (barcode.length() >= MIN_BARCODE_LENGTH) {
                    fireBarcode(barcode.toString());
                }
                barcode.delete(0, barcode.length());
            } else {
                barcode.append(e.getKeyChar());
            }
            return false;
        });

    }

    public static long getTHRESHOLD() {
        return THRESHOLD;
    }

    public static int getMinBarcodeLength() {
        return MIN_BARCODE_LENGTH;
    }

    public StringBuffer getBarcode() {
        return barcode;
    }

    public List<BarcodeListener> getListeners() {
        return listeners;
    }

    public long getLastEventTimeStamp() {
        return lastEventTimeStamp;
    }

    public void setLastEventTimeStamp(long lastEventTimeStamp) {
        this.lastEventTimeStamp = lastEventTimeStamp;
    }

    private void fireBarcode(String barcode) {
        for (BarcodeListener listener : listeners) {
            listener.onBarcodeRead(barcode);
        }
    }

    public void addBarcodeListener(BarcodeListener listener) {
        listeners.add(listener);
    }

    public void removeBarcodeListener(BarcodeListener listener) {
        listeners.remove(listener);
    }

    public interface BarcodeListener {

        void onBarcodeRead(String barcode);
    }
}