package com.itextpdf.text.error_messages;

import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public final class MessageLocalization {
    private static final String BASE_PATH = "com/itextpdf/text/l10n/error/";
    private static HashMap<String, String> currentLanguage;
    private static HashMap<String, String> defaultLanguage;

    static {
        defaultLanguage = new HashMap();
        try {
            defaultLanguage = getLanguageMessages("en", null);
        } catch (Exception e) {
        }
        if (defaultLanguage == null) {
            defaultLanguage = new HashMap();
        }
    }

    private MessageLocalization() {
    }

    public static String getMessage(String key) {
        return getMessage(key, true);
    }

    public static String getMessage(String key, boolean useDefaultLanguageIfMessageNotFound) {
        String val;
        HashMap<String, String> cl = currentLanguage;
        if (cl != null) {
            val = (String) cl.get(key);
            if (val != null) {
                return val;
            }
        }
        if (useDefaultLanguageIfMessageNotFound) {
            val = (String) defaultLanguage.get(key);
            if (val != null) {
                return val;
            }
        }
        return "No message found for " + key;
    }

    public static String getComposedMessage(String key, int p1) {
        return getComposedMessage(key, String.valueOf(p1), null, null, null);
    }

    public static String getComposedMessage(String key, Object... param) {
        String msg = getMessage(key);
        if (param != null) {
            int i = 1;
            for (Object o : param) {
                if (o != null) {
                    msg = msg.replace("{" + i + "}", o.toString());
                }
                i++;
            }
        }
        return msg;
    }

    public static boolean setLanguage(String language, String country) throws IOException {
        HashMap<String, String> lang = getLanguageMessages(language, country);
        if (lang == null) {
            return false;
        }
        currentLanguage = lang;
        return true;
    }

    public static void setMessages(Reader r) throws IOException {
        currentLanguage = readLanguageStream(r);
    }

    private static HashMap<String, String> getLanguageMessages(String language, String country) throws IOException {
        HashMap<String, String> hashMap = null;
        if (language == null) {
            throw new IllegalArgumentException("The language cannot be null.");
        }
        String file;
        InputStream is = null;
        if (country != null) {
            try {
                file = language + "_" + country + ".lng";
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            file = language + ".lng";
        }
        is = StreamUtil.getResourceStream(BASE_PATH + file, new MessageLocalization().getClass().getClassLoader());
        if (is != null) {
            hashMap = readLanguageStream(is);
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                }
            }
        } else if (country != null) {
            is = StreamUtil.getResourceStream(BASE_PATH + (language + ".lng"), new MessageLocalization().getClass().getClassLoader());
            if (is != null) {
                hashMap = readLanguageStream(is);
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e3) {
                    }
                }
            } else if (is != null) {
                try {
                    is.close();
                } catch (Exception e4) {
                }
            }
        } else if (is != null) {
            try {
                is.close();
            } catch (Exception e5) {
            }
        }
        return hashMap;
    }

    private static HashMap<String, String> readLanguageStream(InputStream is) throws IOException {
        return readLanguageStream(new InputStreamReader(is, XmpWriter.UTF8));
    }

    private static HashMap<String, String> readLanguageStream(Reader r) throws IOException {
        HashMap<String, String> lang = new HashMap();
        BufferedReader br = new BufferedReader(r);
        while (true) {
            String line = br.readLine();
            if (line == null) {
                return lang;
            }
            int idxeq = line.indexOf(61);
            if (idxeq >= 0) {
                String key = line.substring(0, idxeq).trim();
                if (!key.startsWith("#")) {
                    lang.put(key, line.substring(idxeq + 1));
                }
            }
        }
    }
}
