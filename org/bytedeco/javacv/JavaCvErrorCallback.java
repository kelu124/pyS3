package org.bytedeco.javacv;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvErrorCallback;

public class JavaCvErrorCallback extends CvErrorCallback {
    private long lastErrorTime;
    private Component parent;
    private int rc;
    private boolean showDialog;

    public JavaCvErrorCallback() {
        this(false);
    }

    public JavaCvErrorCallback(boolean showDialog) {
        this(showDialog, null);
    }

    public JavaCvErrorCallback(boolean showDialog, Component parent) {
        this(showDialog, parent, 0);
    }

    public JavaCvErrorCallback(boolean showDialog, Component parent, int rc) {
        this.lastErrorTime = 0;
        this.parent = parent;
        this.showDialog = showDialog;
        this.rc = rc;
    }

    public int call(int status, BytePointer func_name, BytePointer err_msg, BytePointer file_name, int line, Pointer userdata) {
        String title = "OpenCV Error";
        final String message = opencv_core.cvErrorStr(status) + " (" + err_msg.getString() + ")\nin function " + func_name.getString() + ", " + file_name.getString() + "(" + line + ")";
        Logger.getLogger(JavaCvErrorCallback.class.getName()).log(Level.SEVERE, "OpenCV Error: " + message, new Exception("Strack trace"));
        if (this.showDialog) {
            if (System.currentTimeMillis() - this.lastErrorTime > 1000) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(JavaCvErrorCallback.this.parent, message, "OpenCV Error", 0);
                    }
                });
            }
            this.lastErrorTime = System.currentTimeMillis();
        }
        return this.rc;
    }
}
