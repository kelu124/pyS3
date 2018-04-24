package com.lee.ultrascan;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import com.lee.ultrascan.utils.FileUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.opencv.android.OpenCVLoader;

public class UltraScanApplication extends MultiDexApplication implements UncaughtExceptionHandler {
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        return ((UltraScanApplication) context.getApplicationContext()).refWatcher;
    }

    public void onCreate() {
        super.onCreate();
        this.refWatcher = LeakCanary.install(this);
        PreferenceManager.setDefaultValues(this, C0796R.xml.preferences, false);
        if (OpenCVLoader.initDebug()) {
            ZXingLibrary.initDisplayOpinion(this);
            return;
        }
        throw new RuntimeException("init opencv failed!");
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        writeErrorLog(ex);
        exit();
    }

    protected void writeErrorLog(Throwable ex) {
        Exception e;
        String info;
        FileOutputStream fileOutputStream;
        Throwable th;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                PrintStream printStream2 = new PrintStream(baos2);
                try {
                    ex.printStackTrace(printStream2);
                    String info2 = new String(baos2.toByteArray());
                    if (printStream2 != null) {
                        try {
                            printStream2.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            printStream = printStream2;
                            baos = baos2;
                            info = info2;
                        }
                    }
                    if (baos2 != null) {
                        baos2.close();
                    }
                    printStream = printStream2;
                    baos = baos2;
                    info = info2;
                } catch (Exception e3) {
                    e2 = e3;
                    printStream = printStream2;
                    baos = baos2;
                    try {
                        info = "";
                        e2.printStackTrace();
                        if (printStream != null) {
                            try {
                                printStream.close();
                            } catch (Exception e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        if ("mounted".equals(Environment.getExternalStorageState())) {
                            try {
                                fileOutputStream = new FileOutputStream(new File(FileUtils.getApplicationDir(this, "crash_log"), "crash_log_" + getCurrentDateString()), true);
                                fileOutputStream.write(info.getBytes());
                                fileOutputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (printStream != null) {
                            try {
                                printStream.close();
                            } catch (Exception e222) {
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    printStream = printStream2;
                    baos = baos2;
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (baos != null) {
                        baos.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e222 = e5;
                baos = baos2;
                info = "";
                e222.printStackTrace();
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
                if ("mounted".equals(Environment.getExternalStorageState())) {
                    fileOutputStream = new FileOutputStream(new File(FileUtils.getApplicationDir(this, "crash_log"), "crash_log_" + getCurrentDateString()), true);
                    fileOutputStream.write(info.getBytes());
                    fileOutputStream.close();
                }
            } catch (Throwable th4) {
                th = th4;
                baos = baos2;
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e222 = e6;
            info = "";
            e222.printStackTrace();
            if (printStream != null) {
                printStream.close();
            }
            if (baos != null) {
                baos.close();
            }
            if ("mounted".equals(Environment.getExternalStorageState())) {
                fileOutputStream = new FileOutputStream(new File(FileUtils.getApplicationDir(this, "crash_log"), "crash_log_" + getCurrentDateString()), true);
                fileOutputStream.write(info.getBytes());
                fileOutputStream.close();
            }
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            fileOutputStream = new FileOutputStream(new File(FileUtils.getApplicationDir(this, "crash_log"), "crash_log_" + getCurrentDateString()), true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        }
    }

    public void exit() {
        Process.killProcess(Process.myPid());
    }

    private static String getCurrentDateString() {
        return new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.getDefault()).format(new Date());
    }
}
