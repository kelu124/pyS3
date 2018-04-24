package org.apache.poi.ss.util;

import com.itextpdf.text.html.HtmlTags;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class DateFormatConverter {
    private static Map<String, String> localePrefixes = prepareLocalePrefixes();
    private static POILogger logger = POILogFactory.getLogger(DateFormatConverter.class);
    private static Map<String, String> tokenConversions = prepareTokenConversions();

    public static class DateFormatTokenizer {
        String format;
        int pos;

        public DateFormatTokenizer(String format) {
            this.format = format;
        }

        public String getNextToken() {
            if (this.pos >= this.format.length()) {
                return null;
            }
            int subStart = this.pos;
            char curChar = this.format.charAt(this.pos);
            this.pos++;
            if (curChar == '\'') {
                while (this.pos < this.format.length() && this.format.charAt(this.pos) != '\'') {
                    this.pos++;
                }
                if (this.pos < this.format.length()) {
                    this.pos++;
                }
            } else {
                char activeChar = curChar;
                while (this.pos < this.format.length() && this.format.charAt(this.pos) == activeChar) {
                    this.pos++;
                }
            }
            return this.format.substring(subStart, this.pos);
        }

        public static String[] tokenize(String format) {
            List<String> result = new ArrayList();
            DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
            while (true) {
                String token = tokenizer.getNextToken();
                if (token == null) {
                    return (String[]) result.toArray(new String[0]);
                }
                result.add(token);
            }
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            DateFormatTokenizer tokenizer = new DateFormatTokenizer(this.format);
            while (true) {
                String token = tokenizer.getNextToken();
                if (token == null) {
                    return result.toString();
                }
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append("[").append(token).append("]");
            }
        }
    }

    private static Map<String, String> prepareTokenConversions() {
        Map<String, String> result = new HashMap();
        result.put("EEEE", "dddd");
        result.put("EEE", "ddd");
        result.put("EE", "ddd");
        result.put("E", "d");
        result.put("Z", "");
        result.put("z", "");
        result.put(HtmlTags.f32A, "am/pm");
        result.put("A", "AM/PM");
        result.put("K", "H");
        result.put("KK", "HH");
        result.put("k", "h");
        result.put("kk", "hh");
        result.put("S", "0");
        result.put("SS", "00");
        result.put("SSS", "000");
        return result;
    }

    private static Map<String, String> prepareLocalePrefixes() {
        Map<String, String> result = new HashMap();
        result.put("af", "[$-0436]");
        result.put("am", "[$-45E]");
        result.put("ar_ae", "[$-3801]");
        result.put("ar_bh", "[$-3C01]");
        result.put("ar_dz", "[$-1401]");
        result.put("ar_eg", "[$-C01]");
        result.put("ar_iq", "[$-0801]");
        result.put("ar_jo", "[$-2C01]");
        result.put("ar_kw", "[$-3401]");
        result.put("ar_lb", "[$-3001]");
        result.put("ar_ly", "[$-1001]");
        result.put("ar_ma", "[$-1801]");
        result.put("ar_om", "[$-2001]");
        result.put("ar_qa", "[$-4001]");
        result.put("ar_sa", "[$-0401]");
        result.put("ar_sy", "[$-2801]");
        result.put("ar_tn", "[$-1C01]");
        result.put("ar_ye", "[$-2401]");
        result.put("as", "[$-44D]");
        result.put("az_az", "[$-82C]");
        result.put("az_az", "[$-42C]");
        result.put("be", "[$-0423]");
        result.put("bg", "[$-0402]");
        result.put("bn", "[$-0845]");
        result.put("bn", "[$-0445]");
        result.put("bo", "[$-0451]");
        result.put("bs", "[$-141A]");
        result.put("ca", "[$-0403]");
        result.put("cs", "[$-0405]");
        result.put("cy", "[$-0452]");
        result.put("da", "[$-0406]");
        result.put("de_at", "[$-C07]");
        result.put("de_ch", "[$-0807]");
        result.put("de_de", "[$-0407]");
        result.put("de_li", "[$-1407]");
        result.put("de_lu", "[$-1007]");
        result.put("dv", "[$-0465]");
        result.put("el", "[$-0408]");
        result.put("en_au", "[$-C09]");
        result.put("en_bz", "[$-2809]");
        result.put("en_ca", "[$-1009]");
        result.put("en_cb", "[$-2409]");
        result.put("en_gb", "[$-0809]");
        result.put("en_ie", "[$-1809]");
        result.put("en_in", "[$-4009]");
        result.put("en_jm", "[$-2009]");
        result.put("en_nz", "[$-1409]");
        result.put("en_ph", "[$-3409]");
        result.put("en_tt", "[$-2C09]");
        result.put("en_us", "[$-0409]");
        result.put("en_za", "[$-1C09]");
        result.put("es_ar", "[$-2C0A]");
        result.put("es_bo", "[$-400A]");
        result.put("es_cl", "[$-340A]");
        result.put("es_co", "[$-240A]");
        result.put("es_cr", "[$-140A]");
        result.put("es_do", "[$-1C0A]");
        result.put("es_ec", "[$-300A]");
        result.put("es_es", "[$-40A]");
        result.put("es_gt", "[$-100A]");
        result.put("es_hn", "[$-480A]");
        result.put("es_mx", "[$-80A]");
        result.put("es_ni", "[$-4C0A]");
        result.put("es_pa", "[$-180A]");
        result.put("es_pe", "[$-280A]");
        result.put("es_pr", "[$-500A]");
        result.put("es_py", "[$-3C0A]");
        result.put("es_sv", "[$-440A]");
        result.put("es_uy", "[$-380A]");
        result.put("es_ve", "[$-200A]");
        result.put("et", "[$-0425]");
        result.put("eu", "[$-42D]");
        result.put("fa", "[$-0429]");
        result.put("fi", "[$-40B]");
        result.put("fo", "[$-0438]");
        result.put("fr_be", "[$-80C]");
        result.put("fr_ca", "[$-C0C]");
        result.put("fr_ch", "[$-100C]");
        result.put("fr_fr", "[$-40C]");
        result.put("fr_lu", "[$-140C]");
        result.put("gd", "[$-43C]");
        result.put("gd_ie", "[$-83C]");
        result.put("gn", "[$-0474]");
        result.put("gu", "[$-0447]");
        result.put("he", "[$-40D]");
        result.put("hi", "[$-0439]");
        result.put(HtmlTags.HR, "[$-41A]");
        result.put("hu", "[$-40E]");
        result.put("hy", "[$-42B]");
        result.put("id", "[$-0421]");
        result.put("is", "[$-40F]");
        result.put("it_ch", "[$-0810]");
        result.put("it_it", "[$-0410]");
        result.put("ja", "[$-0411]");
        result.put("kk", "[$-43F]");
        result.put("km", "[$-0453]");
        result.put("kn", "[$-44B]");
        result.put("ko", "[$-0412]");
        result.put("ks", "[$-0460]");
        result.put("la", "[$-0476]");
        result.put("lo", "[$-0454]");
        result.put("lt", "[$-0427]");
        result.put("lv", "[$-0426]");
        result.put("mi", "[$-0481]");
        result.put("mk", "[$-42F]");
        result.put("ml", "[$-44C]");
        result.put("mn", "[$-0850]");
        result.put("mn", "[$-0450]");
        result.put("mr", "[$-44E]");
        result.put("ms_bn", "[$-83E]");
        result.put("ms_my", "[$-43E]");
        result.put("mt", "[$-43A]");
        result.put("my", "[$-0455]");
        result.put("ne", "[$-0461]");
        result.put("nl_be", "[$-0813]");
        result.put("nl_nl", "[$-0413]");
        result.put("no_no", "[$-0814]");
        result.put("or", "[$-0448]");
        result.put("pa", "[$-0446]");
        result.put("pl", "[$-0415]");
        result.put("pt_br", "[$-0416]");
        result.put("pt_pt", "[$-0816]");
        result.put("rm", "[$-0417]");
        result.put("ro", "[$-0418]");
        result.put("ro_mo", "[$-0818]");
        result.put("ru", "[$-0419]");
        result.put("ru_mo", "[$-0819]");
        result.put("sa", "[$-44F]");
        result.put("sb", "[$-42E]");
        result.put("sd", "[$-0459]");
        result.put("si", "[$-45B]");
        result.put("sk", "[$-41B]");
        result.put("sl", "[$-0424]");
        result.put("so", "[$-0477]");
        result.put("sq", "[$-41C]");
        result.put("sr_sp", "[$-C1A]");
        result.put("sr_sp", "[$-81A]");
        result.put("sv_fi", "[$-81D]");
        result.put("sv_se", "[$-41D]");
        result.put("sw", "[$-0441]");
        result.put("ta", "[$-0449]");
        result.put("te", "[$-44A]");
        result.put("tg", "[$-0428]");
        result.put(HtmlTags.TH, "[$-41E]");
        result.put("tk", "[$-0442]");
        result.put("tn", "[$-0432]");
        result.put(HtmlTags.TR, "[$-41F]");
        result.put("ts", "[$-0431]");
        result.put("tt", "[$-0444]");
        result.put("uk", "[$-0422]");
        result.put("ur", "[$-0420]");
        result.put("UTF_8", "[$-0000]");
        result.put("uz_uz", "[$-0843]");
        result.put("uz_uz", "[$-0443]");
        result.put("vi", "[$-42A]");
        result.put("xh", "[$-0434]");
        result.put("yi", "[$-43D]");
        result.put("zh_cn", "[$-0804]");
        result.put("zh_hk", "[$-C04]");
        result.put("zh_mo", "[$-1404]");
        result.put("zh_sg", "[$-1004]");
        result.put("zh_tw", "[$-0404]");
        result.put("zu", "[$-0435]");
        result.put("ar", "[$-0401]");
        result.put("bn", "[$-0845]");
        result.put("de", "[$-0407]");
        result.put("en", "[$-0409]");
        result.put("es", "[$-40A]");
        result.put("fr", "[$-40C]");
        result.put("it", "[$-0410]");
        result.put("ms", "[$-43E]");
        result.put("nl", "[$-0413]");
        result.put("nn", "[$-0814]");
        result.put("no", "[$-0414]");
        result.put("pt", "[$-0816]");
        result.put("sr", "[$-C1A]");
        result.put("sv", "[$-41D]");
        result.put("uz", "[$-0843]");
        result.put("zh", "[$-0804]");
        result.put("ga", "[$-43C]");
        result.put("ga_ie", "[$-83C]");
        result.put("in", "[$-0421]");
        result.put("iw", "[$-40D]");
        result.put("", "[$-0409]");
        return result;
    }

    public static String getPrefixForLocale(Locale locale) {
        String localeString = locale.toString().toLowerCase(locale);
        String result = (String) localePrefixes.get(localeString);
        if (result == null) {
            result = (String) localePrefixes.get(localeString.substring(0, 2));
            if (result == null) {
                Locale parentLocale = new Locale(localeString.substring(0, 2));
                logger.log(7, new Object[]{"Unable to find prefix for " + locale + "(" + locale.getDisplayName(Locale.ROOT) + ") or " + localeString.substring(0, 2) + "(" + parentLocale.getDisplayName(Locale.ROOT) + ")"});
                return "";
            }
        }
        return result;
    }

    public static String convert(Locale locale, DateFormat df) {
        return convert(locale, ((SimpleDateFormat) df).toPattern());
    }

    public static String convert(Locale locale, String format) {
        StringBuilder result = new StringBuilder();
        result.append(getPrefixForLocale(locale));
        DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
        while (true) {
            String token = tokenizer.getNextToken();
            if (token == null) {
                result.append(";@");
                return result.toString().trim();
            } else if (token.startsWith("'")) {
                result.append(token.replaceAll("'", "\""));
            } else if (Character.isLetter(token.charAt(0))) {
                String mappedToken = (String) tokenConversions.get(token);
                if (mappedToken != null) {
                    token = mappedToken;
                }
                result.append(token);
            } else {
                result.append(token);
            }
        }
    }

    public static String getJavaDatePattern(int style, Locale locale) {
        DateFormat df = DateFormat.getDateInstance(style, locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        switch (style) {
            case 0:
                return "dddd, MMMM d, yyyy";
            case 1:
                return "MMMM d, yyyy";
            case 2:
                return "MMM d, yyyy";
            case 3:
                return "d/MM/yy";
            default:
                return "MMM d, yyyy";
        }
    }

    public static String getJavaTimePattern(int style, Locale locale) {
        DateFormat df = DateFormat.getTimeInstance(style, locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        switch (style) {
            case 0:
                return "h:mm:ss a";
            case 1:
                return "h:mm:ss a";
            case 2:
                return "h:mm:ss a";
            case 3:
                return "h:mm a";
            default:
                return "h:mm:ss a";
        }
    }

    public static String getJavaDateTimePattern(int style, Locale locale) {
        DateFormat df = DateFormat.getDateTimeInstance(style, style, locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        switch (style) {
            case 0:
                return "dddd, MMMM d, yyyy h:mm:ss a";
            case 1:
                return "MMMM d, yyyy h:mm:ss a";
            case 2:
                return "MMM d, yyyy h:mm:ss a";
            case 3:
                return "M/d/yy h:mm a";
            default:
                return "MMM d, yyyy h:mm:ss a";
        }
    }
}
