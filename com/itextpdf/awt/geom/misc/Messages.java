package com.itextpdf.awt.geom.misc;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static ResourceBundle bundle = null;

    public static String getString(String msg) {
        if (bundle == null) {
            return msg;
        }
        try {
            return bundle.getString(msg);
        } catch (MissingResourceException e) {
            return "Missing message: " + msg;
        }
    }

    public static String getString(String msg, Object arg) {
        return getString(msg, new Object[]{arg});
    }

    public static String getString(String msg, int arg) {
        return getString(msg, new Object[]{Integer.toString(arg)});
    }

    public static String getString(String msg, char arg) {
        return getString(msg, new Object[]{String.valueOf(arg)});
    }

    public static String getString(String msg, Object arg1, Object arg2) {
        return getString(msg, new Object[]{arg1, arg2});
    }

    public static String getString(String msg, Object[] args) {
        String format = msg;
        if (bundle != null) {
            try {
                format = bundle.getString(msg);
            } catch (MissingResourceException e) {
            }
        }
        return format(format, args);
    }

    public static String format(String format, Object[] args) {
        int i;
        StringBuilder answer = new StringBuilder(format.length() + (args.length * 20));
        String[] argStrings = new String[args.length];
        for (i = 0; i < args.length; i++) {
            if (args[i] == null) {
                argStrings[i] = "<null>";
            } else {
                argStrings[i] = args[i].toString();
            }
        }
        int lastI = 0;
        i = format.indexOf(123, 0);
        while (i >= 0) {
            if (i != 0 && format.charAt(i - 1) == '\\') {
                if (i != 1) {
                    answer.append(format.substring(lastI, i - 1));
                }
                answer.append('{');
                lastI = i + 1;
            } else if (i > format.length() - 3) {
                answer.append(format.substring(lastI, format.length()));
                lastI = format.length();
            } else {
                int argnum = (byte) Character.digit(format.charAt(i + 1), 10);
                if (argnum < 0 || format.charAt(i + 2) != '}') {
                    answer.append(format.substring(lastI, i + 1));
                    lastI = i + 1;
                } else {
                    answer.append(format.substring(lastI, i));
                    if (argnum >= argStrings.length) {
                        answer.append("<missing argument>");
                    } else {
                        answer.append(argStrings[argnum]);
                    }
                    lastI = i + 3;
                }
            }
            i = format.indexOf(123, lastI);
        }
        if (lastI < format.length()) {
            answer.append(format.substring(lastI, format.length()));
        }
        return answer.toString();
    }
}
