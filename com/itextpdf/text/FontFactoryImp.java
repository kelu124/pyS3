package com.itextpdf.text;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.BaseFont;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class FontFactoryImp implements FontProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(FontFactoryImp.class);
    private static String[] TTFamilyOrder = new String[]{"3", "1", "1033", "3", "0", "1033", "1", "0", "0", "0", "3", "0"};
    public boolean defaultEmbedding = false;
    public String defaultEncoding = "Cp1252";
    private final Hashtable<String, ArrayList<String>> fontFamilies = new Hashtable();
    private final Hashtable<String, String> trueTypeFonts = new Hashtable();

    public FontFactoryImp() {
        this.trueTypeFonts.put("Courier".toLowerCase(), "Courier");
        this.trueTypeFonts.put("Courier-Bold".toLowerCase(), "Courier-Bold");
        this.trueTypeFonts.put("Courier-Oblique".toLowerCase(), "Courier-Oblique");
        this.trueTypeFonts.put("Courier-BoldOblique".toLowerCase(), "Courier-BoldOblique");
        this.trueTypeFonts.put("Helvetica".toLowerCase(), "Helvetica");
        this.trueTypeFonts.put("Helvetica-Bold".toLowerCase(), "Helvetica-Bold");
        this.trueTypeFonts.put("Helvetica-Oblique".toLowerCase(), "Helvetica-Oblique");
        this.trueTypeFonts.put("Helvetica-BoldOblique".toLowerCase(), "Helvetica-BoldOblique");
        this.trueTypeFonts.put("Symbol".toLowerCase(), "Symbol");
        this.trueTypeFonts.put("Times-Roman".toLowerCase(), "Times-Roman");
        this.trueTypeFonts.put("Times-Bold".toLowerCase(), "Times-Bold");
        this.trueTypeFonts.put("Times-Italic".toLowerCase(), "Times-Italic");
        this.trueTypeFonts.put("Times-BoldItalic".toLowerCase(), "Times-BoldItalic");
        this.trueTypeFonts.put("ZapfDingbats".toLowerCase(), "ZapfDingbats");
        ArrayList<String> tmp = new ArrayList();
        tmp.add("Courier");
        tmp.add("Courier-Bold");
        tmp.add("Courier-Oblique");
        tmp.add("Courier-BoldOblique");
        this.fontFamilies.put("Courier".toLowerCase(), tmp);
        tmp = new ArrayList();
        tmp.add("Helvetica");
        tmp.add("Helvetica-Bold");
        tmp.add("Helvetica-Oblique");
        tmp.add("Helvetica-BoldOblique");
        this.fontFamilies.put("Helvetica".toLowerCase(), tmp);
        tmp = new ArrayList();
        tmp.add("Symbol");
        this.fontFamilies.put("Symbol".toLowerCase(), tmp);
        tmp = new ArrayList();
        tmp.add("Times-Roman");
        tmp.add("Times-Bold");
        tmp.add("Times-Italic");
        tmp.add("Times-BoldItalic");
        this.fontFamilies.put(FontFactory.TIMES.toLowerCase(), tmp);
        this.fontFamilies.put("Times-Roman".toLowerCase(), tmp);
        tmp = new ArrayList();
        tmp.add("ZapfDingbats");
        this.fontFamilies.put("ZapfDingbats".toLowerCase(), tmp);
    }

    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
        return getFont(fontname, encoding, embedded, size, style, color, true);
    }

    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
        if (fontname == null) {
            return new Font(FontFamily.UNDEFINED, size, style, color);
        }
        ArrayList<String> tmp = (ArrayList) this.fontFamilies.get(fontname.toLowerCase());
        if (tmp != null) {
            synchronized (tmp) {
                int s;
                if (style == -1) {
                    s = 0;
                } else {
                    s = style;
                }
                int fs = 0;
                boolean found = false;
                Iterator i$ = tmp.iterator();
                while (i$.hasNext()) {
                    String f = (String) i$.next();
                    String lcf = f.toLowerCase();
                    fs = 0;
                    if (lcf.indexOf(HtmlTags.BOLD) != -1) {
                        fs = 0 | 1;
                    }
                    if (!(lcf.indexOf(HtmlTags.ITALIC) == -1 && lcf.indexOf(HtmlTags.OBLIQUE) == -1)) {
                        fs |= 2;
                    }
                    if ((s & 3) == fs) {
                        fontname = f;
                        found = true;
                        break;
                    }
                }
                if (style != -1 && found) {
                    style &= fs ^ -1;
                }
            }
        }
        try {
            BaseFont basefont = getBaseFont(fontname, encoding, embedded, cached);
            if (basefont == null) {
                return new Font(FontFamily.UNDEFINED, size, style, color);
            }
            return new Font(basefont, size, style, color);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (IOException e) {
            return new Font(FontFamily.UNDEFINED, size, style, color);
        } catch (NullPointerException e2) {
            return new Font(FontFamily.UNDEFINED, size, style, color);
        }
    }

    protected BaseFont getBaseFont(String fontname, String encoding, boolean embedded, boolean cached) throws IOException, DocumentException {
        BaseFont basefont = null;
        try {
            basefont = BaseFont.createFont(fontname, encoding, embedded, cached, null, null, true);
        } catch (DocumentException e) {
        }
        if (basefont != null) {
            return basefont;
        }
        fontname = (String) this.trueTypeFonts.get(fontname.toLowerCase());
        if (fontname != null) {
            return BaseFont.createFont(fontname, encoding, embedded, cached, null, null);
        }
        return basefont;
    }

    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
        return getFont(fontname, encoding, embedded, size, style, null);
    }

    public Font getFont(String fontname, String encoding, boolean embedded, float size) {
        return getFont(fontname, encoding, embedded, size, -1, null);
    }

    public Font getFont(String fontname, String encoding, boolean embedded) {
        return getFont(fontname, encoding, embedded, -1.0f, -1, null);
    }

    public Font getFont(String fontname, String encoding, float size, int style, BaseColor color) {
        return getFont(fontname, encoding, this.defaultEmbedding, size, style, color);
    }

    public Font getFont(String fontname, String encoding, float size, int style) {
        return getFont(fontname, encoding, this.defaultEmbedding, size, style, null);
    }

    public Font getFont(String fontname, String encoding, float size) {
        return getFont(fontname, encoding, this.defaultEmbedding, size, -1, null);
    }

    public Font getFont(String fontname, float size, BaseColor color) {
        return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, -1, color);
    }

    public Font getFont(String fontname, String encoding) {
        return getFont(fontname, encoding, this.defaultEmbedding, -1.0f, -1, null);
    }

    public Font getFont(String fontname, float size, int style, BaseColor color) {
        return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, style, color);
    }

    public Font getFont(String fontname, float size, int style) {
        return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, style, null);
    }

    public Font getFont(String fontname, float size) {
        return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, -1, null);
    }

    public Font getFont(String fontname) {
        return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, -1.0f, -1, null);
    }

    public void registerFamily(String familyName, String fullName, String path) {
        if (path != null) {
            this.trueTypeFonts.put(fullName, path);
        }
        synchronized (this.fontFamilies) {
            ArrayList<String> tmp = (ArrayList) this.fontFamilies.get(familyName);
            if (tmp == null) {
                tmp = new ArrayList();
                this.fontFamilies.put(familyName, tmp);
            }
        }
        synchronized (tmp) {
            if (!tmp.contains(fullName)) {
                int fullNameLength = fullName.length();
                boolean inserted = false;
                for (int j = 0; j < tmp.size(); j++) {
                    if (((String) tmp.get(j)).length() >= fullNameLength) {
                        tmp.add(j, fullName);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    tmp.add(fullName);
                }
            }
        }
    }

    public void register(String path) {
        register(path, null);
    }

    public void register(String path, String alias) {
        try {
            String familyName;
            String fullName;
            if (path.toLowerCase().endsWith(".ttf") || path.toLowerCase().endsWith(".otf") || path.toLowerCase().indexOf(".ttc,") > 0) {
                Object[] allNames = BaseFont.getAllFontNames(path, "Cp1252", null);
                this.trueTypeFonts.put(((String) allNames[0]).toLowerCase(), path);
                if (alias != null) {
                    this.trueTypeFonts.put(alias.toLowerCase(), path);
                }
                for (String[] name : (String[][]) allNames[2]) {
                    this.trueTypeFonts.put(name[3].toLowerCase(), path);
                }
                familyName = null;
                String[][] names = (String[][]) allNames[1];
                int k = 0;
                while (k < TTFamilyOrder.length) {
                    for (String[] name2 : names) {
                        if (TTFamilyOrder[k].equals(name2[0]) && TTFamilyOrder[k + 1].equals(name2[1]) && TTFamilyOrder[k + 2].equals(name2[2])) {
                            familyName = name2[3].toLowerCase();
                            k = TTFamilyOrder.length;
                            break;
                        }
                    }
                    k += 3;
                }
                if (familyName != null) {
                    String lastName = "";
                    for (String[] name22 : (String[][]) allNames[2]) {
                        k = 0;
                        while (k < TTFamilyOrder.length) {
                            if (TTFamilyOrder[k].equals(name22[0]) && TTFamilyOrder[k + 1].equals(name22[1]) && TTFamilyOrder[k + 2].equals(name22[2])) {
                                fullName = name22[3];
                                if (!fullName.equals(lastName)) {
                                    lastName = fullName;
                                    registerFamily(familyName, fullName, null);
                                    break;
                                }
                            }
                            k += 3;
                        }
                    }
                }
            } else if (path.toLowerCase().endsWith(".ttc")) {
                if (alias != null) {
                    LOGGER.error("You can't define an alias for a true type collection.");
                }
                String[] names2 = BaseFont.enumerateTTCNames(path);
                for (int i = 0; i < names2.length; i++) {
                    register(path + "," + i);
                }
            } else if (path.toLowerCase().endsWith(".afm") || path.toLowerCase().endsWith(".pfm")) {
                BaseFont bf = BaseFont.createFont(path, "Cp1252", false);
                fullName = bf.getFullFontName()[0][3].toLowerCase();
                familyName = bf.getFamilyFontName()[0][3].toLowerCase();
                String psName = bf.getPostscriptFontName().toLowerCase();
                registerFamily(familyName, fullName, null);
                this.trueTypeFonts.put(psName, path);
                this.trueTypeFonts.put(fullName, path);
            }
            if (LOGGER.isLogging(Level.TRACE)) {
                LOGGER.trace(String.format("Registered %s", new Object[]{path}));
            }
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    public int registerDirectory(String dir) {
        return registerDirectory(dir, false);
    }

    public int registerDirectory(String dir, boolean scanSubdirectories) {
        if (LOGGER.isLogging(Level.DEBUG)) {
            LOGGER.debug(String.format("Registering directory %s, looking for fonts", new Object[]{dir}));
        }
        int count = 0;
        try {
            File file = new File(dir);
            if (!file.exists() || !file.isDirectory()) {
                return 0;
            }
            String[] files = file.list();
            if (files == null) {
                return 0;
            }
            int k = 0;
            File file2 = file;
            while (k < files.length) {
                try {
                    file = new File(dir, files[k]);
                    try {
                        if (!file.isDirectory()) {
                            String name = file.getPath();
                            String suffix = name.length() < 4 ? null : name.substring(name.length() - 4).toLowerCase();
                            if (".afm".equals(suffix) || ".pfm".equals(suffix)) {
                                if (new File(name.substring(0, name.length() - 4) + ".pfb").exists()) {
                                    register(name, null);
                                    count++;
                                }
                            } else if (".ttf".equals(suffix) || ".otf".equals(suffix) || ".ttc".equals(suffix)) {
                                register(name, null);
                                count++;
                            }
                        } else if (scanSubdirectories) {
                            count += registerDirectory(file.getAbsolutePath(), true);
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e2) {
                    file = file2;
                }
                k++;
                file2 = file;
            }
            return count;
        } catch (Exception e3) {
        }
    }

    public int registerDirectories() {
        int count = 0;
        String windir = System.getenv("windir");
        String fileseparator = System.getProperty("file.separator");
        if (!(windir == null || fileseparator == null)) {
            count = 0 + registerDirectory(windir + fileseparator + "fonts");
        }
        return ((((((count + registerDirectory("/usr/share/X11/fonts", true)) + registerDirectory("/usr/X/lib/X11/fonts", true)) + registerDirectory("/usr/openwin/lib/X11/fonts", true)) + registerDirectory("/usr/share/fonts", true)) + registerDirectory("/usr/X11R6/lib/X11/fonts", true)) + registerDirectory("/Library/Fonts")) + registerDirectory("/System/Library/Fonts");
    }

    public Set<String> getRegisteredFonts() {
        return this.trueTypeFonts.keySet();
    }

    public Set<String> getRegisteredFamilies() {
        return this.fontFamilies.keySet();
    }

    public boolean isRegistered(String fontname) {
        return this.trueTypeFonts.containsKey(fontname.toLowerCase());
    }
}
