package com.lee.ultrascan.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;

public class FileUtils {
    private FileUtils() {
        throw new AssertionError();
    }

    public static File getExternalStorageDir() {
        if (isExternalStorageAvailable() || !Environment.isExternalStorageRemovable()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    public static File getApplicationDir(Context context, String subDirName) {
        String path;
        File file;
        if (!isExternalStorageAvailable() && Environment.isExternalStorageRemovable()) {
            path = context.getFilesDir().getPath();
        } else if (context.getExternalFilesDir(null) == null) {
            return null;
        } else {
            path = context.getExternalFilesDir(null).getPath();
        }
        if (TextUtils.isEmpty(subDirName)) {
            file = new File(path + File.separator + subDirName);
        } else {
            file = new File(path);
        }
        if (file.isDirectory() || file.mkdirs()) {
            return file;
        }
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getExternalStorageSize() {
        if (!isExternalStorageAvailable()) {
            return 0;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    public static long getExternalStorageAvailableSize() {
        if (!isExternalStorageAvailable()) {
            return 0;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    public static long getInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    public static long getInternalStorageAvailableSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    public static boolean isFileExists(String fullPath) {
        return new File(fullPath).exists();
    }

    public static boolean isDirectory(String fullPath) {
        return new File(fullPath).isDirectory();
    }

    public static File openFile(File dir, String filename) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
