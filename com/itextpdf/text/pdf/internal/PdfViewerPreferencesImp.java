package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

public class PdfViewerPreferencesImp implements PdfViewerPreferences {
    public static final PdfName[] DIRECTION_PREFERENCES = new PdfName[]{PdfName.L2R, PdfName.R2L};
    public static final PdfName[] DUPLEX_PREFERENCES = new PdfName[]{PdfName.SIMPLEX, PdfName.DUPLEXFLIPSHORTEDGE, PdfName.DUPLEXFLIPLONGEDGE};
    public static final PdfName[] NONFULLSCREENPAGEMODE_PREFERENCES = new PdfName[]{PdfName.USENONE, PdfName.USEOUTLINES, PdfName.USETHUMBS, PdfName.USEOC};
    public static final PdfName[] PAGE_BOUNDARIES = new PdfName[]{PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.BLEEDBOX, PdfName.TRIMBOX, PdfName.ARTBOX};
    public static final PdfName[] PRINTSCALING_PREFERENCES = new PdfName[]{PdfName.APPDEFAULT, PdfName.NONE};
    public static final PdfName[] VIEWER_PREFERENCES = new PdfName[]{PdfName.HIDETOOLBAR, PdfName.HIDEMENUBAR, PdfName.HIDEWINDOWUI, PdfName.FITWINDOW, PdfName.CENTERWINDOW, PdfName.DISPLAYDOCTITLE, PdfName.NONFULLSCREENPAGEMODE, PdfName.DIRECTION, PdfName.VIEWAREA, PdfName.VIEWCLIP, PdfName.PRINTAREA, PdfName.PRINTCLIP, PdfName.PRINTSCALING, PdfName.DUPLEX, PdfName.PICKTRAYBYPDFSIZE, PdfName.PRINTPAGERANGE, PdfName.NUMCOPIES};
    private static final int viewerPreferencesMask = 16773120;
    private int pageLayoutAndMode = 0;
    private PdfDictionary viewerPreferences = new PdfDictionary();

    public int getPageLayoutAndMode() {
        return this.pageLayoutAndMode;
    }

    public PdfDictionary getViewerPreferences() {
        return this.viewerPreferences;
    }

    public void setViewerPreferences(int preferences) {
        this.pageLayoutAndMode |= preferences;
        if ((viewerPreferencesMask & preferences) != 0) {
            this.pageLayoutAndMode = -16773121 & this.pageLayoutAndMode;
            if ((preferences & 4096) != 0) {
                this.viewerPreferences.put(PdfName.HIDETOOLBAR, PdfBoolean.PDFTRUE);
            }
            if ((preferences & 8192) != 0) {
                this.viewerPreferences.put(PdfName.HIDEMENUBAR, PdfBoolean.PDFTRUE);
            }
            if ((preferences & 16384) != 0) {
                this.viewerPreferences.put(PdfName.HIDEWINDOWUI, PdfBoolean.PDFTRUE);
            }
            if ((32768 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
            }
            if ((65536 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.CENTERWINDOW, PdfBoolean.PDFTRUE);
            }
            if ((131072 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
            }
            if ((262144 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USENONE);
            } else if ((524288 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOUTLINES);
            } else if ((1048576 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USETHUMBS);
            } else if ((2097152 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, PdfName.USEOC);
            }
            if ((4194304 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.DIRECTION, PdfName.L2R);
            } else if ((8388608 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.DIRECTION, PdfName.R2L);
            }
            if ((16777216 & preferences) != 0) {
                this.viewerPreferences.put(PdfName.PRINTSCALING, PdfName.NONE);
            }
        }
    }

    private int getIndex(PdfName key) {
        for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
            if (VIEWER_PREFERENCES[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isPossibleValue(PdfName value, PdfName[] accepted) {
        for (PdfName equals : accepted) {
            if (equals.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void addViewerPreference(PdfName key, PdfObject value) {
        switch (getIndex(key)) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 14:
                if (value instanceof PdfBoolean) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 6:
                if ((value instanceof PdfName) && isPossibleValue((PdfName) value, NONFULLSCREENPAGEMODE_PREFERENCES)) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 7:
                if ((value instanceof PdfName) && isPossibleValue((PdfName) value, DIRECTION_PREFERENCES)) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 8:
            case 9:
            case 10:
            case 11:
                if ((value instanceof PdfName) && isPossibleValue((PdfName) value, PAGE_BOUNDARIES)) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 12:
                if ((value instanceof PdfName) && isPossibleValue((PdfName) value, PRINTSCALING_PREFERENCES)) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 13:
                if ((value instanceof PdfName) && isPossibleValue((PdfName) value, DUPLEX_PREFERENCES)) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 15:
                if (value instanceof PdfArray) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            case 16:
                if (value instanceof PdfNumber) {
                    this.viewerPreferences.put(key, value);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void addToCatalog(PdfDictionary catalog) {
        catalog.remove(PdfName.PAGELAYOUT);
        if ((this.pageLayoutAndMode & 1) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.SINGLEPAGE);
        } else if ((this.pageLayoutAndMode & 2) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.ONECOLUMN);
        } else if ((this.pageLayoutAndMode & 4) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNLEFT);
        } else if ((this.pageLayoutAndMode & 8) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.TWOCOLUMNRIGHT);
        } else if ((this.pageLayoutAndMode & 16) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGELEFT);
        } else if ((this.pageLayoutAndMode & 32) != 0) {
            catalog.put(PdfName.PAGELAYOUT, PdfName.TWOPAGERIGHT);
        }
        catalog.remove(PdfName.PAGEMODE);
        if ((this.pageLayoutAndMode & 64) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USENONE);
        } else if ((this.pageLayoutAndMode & 128) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
        } else if ((this.pageLayoutAndMode & 256) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USETHUMBS);
        } else if ((this.pageLayoutAndMode & 512) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.FULLSCREEN);
        } else if ((this.pageLayoutAndMode & 1024) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USEOC);
        } else if ((this.pageLayoutAndMode & 2048) != 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USEATTACHMENTS);
        }
        catalog.remove(PdfName.VIEWERPREFERENCES);
        if (this.viewerPreferences.size() > 0) {
            catalog.put(PdfName.VIEWERPREFERENCES, this.viewerPreferences);
        }
    }

    public static PdfViewerPreferencesImp getViewerPreferences(PdfDictionary catalog) {
        PdfName name;
        PdfViewerPreferencesImp preferences = new PdfViewerPreferencesImp();
        int prefs = 0;
        PdfObject obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGELAYOUT));
        if (obj != null && obj.isName()) {
            name = (PdfName) obj;
            if (name.equals(PdfName.SINGLEPAGE)) {
                prefs = 0 | 1;
            } else if (name.equals(PdfName.ONECOLUMN)) {
                prefs = 0 | 2;
            } else if (name.equals(PdfName.TWOCOLUMNLEFT)) {
                prefs = 0 | 4;
            } else if (name.equals(PdfName.TWOCOLUMNRIGHT)) {
                prefs = 0 | 8;
            } else if (name.equals(PdfName.TWOPAGELEFT)) {
                prefs = 0 | 16;
            } else if (name.equals(PdfName.TWOPAGERIGHT)) {
                prefs = 0 | 32;
            }
        }
        obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGEMODE));
        if (obj != null && obj.isName()) {
            name = (PdfName) obj;
            if (name.equals(PdfName.USENONE)) {
                prefs |= 64;
            } else if (name.equals(PdfName.USEOUTLINES)) {
                prefs |= 128;
            } else if (name.equals(PdfName.USETHUMBS)) {
                prefs |= 256;
            } else if (name.equals(PdfName.FULLSCREEN)) {
                prefs |= 512;
            } else if (name.equals(PdfName.USEOC)) {
                prefs |= 1024;
            } else if (name.equals(PdfName.USEATTACHMENTS)) {
                prefs |= 2048;
            }
        }
        preferences.setViewerPreferences(prefs);
        obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.VIEWERPREFERENCES));
        if (obj != null && obj.isDictionary()) {
            PdfDictionary vp = (PdfDictionary) obj;
            for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
                preferences.addViewerPreference(VIEWER_PREFERENCES[i], PdfReader.getPdfObjectRelease(vp.get(VIEWER_PREFERENCES[i])));
            }
        }
        return preferences;
    }
}
