package com.alex.components;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.TimerTask;

public class XInterval {
    private static javax.swing.Timer t;

    public XInterval(ActionListener e, int milis) {
        t = null;
        t = new Timer(milis, e);

        java.util.Timer tt = new java.util.Timer(false);
        tt.schedule(new TimerTask() {
            @Override
            public void run() {
                t.start();
            }
        }, 0);
    }
}
