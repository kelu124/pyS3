package com.google.zxing.oned;

import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.util.CodePageUtil;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

final class EANManufacturerOrgSupport {
    private final List<String> countryIdentifiers = new ArrayList();
    private final List<int[]> ranges = new ArrayList();

    EANManufacturerOrgSupport() {
    }

    String lookupCountryIdentifier(String productCode) {
        initIfNeeded();
        int prefix = Integer.parseInt(productCode.substring(0, 3));
        int max = this.ranges.size();
        for (int i = 0; i < max; i++) {
            int[] range = (int[]) this.ranges.get(i);
            int start = range[0];
            if (prefix < start) {
                return null;
            }
            if (prefix <= (range.length == 1 ? start : range[1])) {
                return (String) this.countryIdentifiers.get(i);
            }
        }
        return null;
    }

    private void add(int[] range, String id) {
        this.ranges.add(range);
        this.countryIdentifiers.add(id);
    }

    private synchronized void initIfNeeded() {
        if (this.ranges.isEmpty()) {
            int[] iArr = new int[2];
            iArr[1] = 19;
            add(iArr, "US/CA");
            add(new int[]{30, 39}, "US");
            add(new int[]{60, 139}, "US/CA");
            add(new int[]{300, 379}, "FR");
            add(new int[]{380}, "BG");
            add(new int[]{383}, "SI");
            add(new int[]{dc1394.DC1394_TRIGGER_MODE_1}, "HR");
            add(new int[]{dc1394.DC1394_TRIGGER_MODE_3}, "BA");
            add(new int[]{400, opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y}, "DE");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_WB_KB, opencv_videoio.CV_CAP_PROP_XI_LIMIT_BANDWIDTH}, "JP");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_SENSOR_DATA_BIT_DEPTH, opencv_videoio.CV_CAP_PROP_XI_HOUS_TEMP}, "RU");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_APPLY_CMS}, "TW");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_IMAGE_IS_COLOR}, "EE");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_COLOR_FILTER_ARRAY}, "LV");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_GAMMAY}, "AZ");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_GAMMAC}, "LT");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_SHARPNESS}, "UZ");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_00}, "LK");
            add(new int[]{480}, "PH");
            add(new int[]{481}, "BY");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_03}, "UA");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_11}, "MD");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_12}, "AM");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_13}, "GE");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_20}, "KZ");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_22}, "HK");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_23, opencv_videoio.CV_CAP_PROP_XI_ACQ_FRAME_BURST_COUNT}, "JP");
            add(new int[]{500, opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_T1}, "GB");
            add(new int[]{TIFFConstants.TIFFTAG_JPEGDCTABLES}, "GR");
            add(new int[]{528}, ExpandedProductParsedResult.POUND);
            add(new int[]{529}, "CY");
            add(new int[]{531}, "MK");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_FRAMERATE}, "MT");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_AVAILABLE_BANDWIDTH}, "IE");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_BUFFER_POLICY, 549}, "BE/LU");
            add(new int[]{opencv_videoio.CV_CAP_PROP_XI_HDR_KNEEPOINT_COUNT}, "PT");
            add(new int[]{569}, "IS");
            add(new int[]{570, dc1394.DC1394_TRIGGER_SOURCE_3}, "DK");
            add(new int[]{590}, "PL");
            add(new int[]{594}, "RO");
            add(new int[]{599}, "HU");
            add(new int[]{600, 601}, "ZA");
            add(new int[]{603}, "GH");
            add(new int[]{608}, "BH");
            add(new int[]{dc1394.DC1394_POWER_CLASS_PROV_MIN_15W}, "MU");
            add(new int[]{dc1394.DC1394_POWER_CLASS_PROV_MIN_45W}, "MA");
            add(new int[]{dc1394.DC1394_POWER_CLASS_USES_MAX_3W}, "DZ");
            add(new int[]{616}, "KE");
            add(new int[]{618}, "CI");
            add(new int[]{619}, "TN");
            add(new int[]{621}, "SY");
            add(new int[]{622}, "EG");
            add(new int[]{624}, "LY");
            add(new int[]{625}, "JO");
            add(new int[]{626}, "IR");
            add(new int[]{627}, "KW");
            add(new int[]{628}, "SA");
            add(new int[]{629}, "AE");
            add(new int[]{640, 649}, "FI");
            add(new int[]{690, 695}, "CN");
            add(new int[]{700, 709}, "NO");
            add(new int[]{729}, "IL");
            add(new int[]{730, 739}, "SE");
            add(new int[]{740}, "GT");
            add(new int[]{741}, "SV");
            add(new int[]{742}, "HN");
            add(new int[]{743}, "NI");
            add(new int[]{744}, "CR");
            add(new int[]{745}, "PA");
            add(new int[]{746}, "DO");
            add(new int[]{750}, "MX");
            add(new int[]{754, 755}, "CA");
            add(new int[]{759}, "VE");
            add(new int[]{760, dc1394.DC1394_LOG_WARNING}, "CH");
            add(new int[]{770}, "CO");
            add(new int[]{773}, "UY");
            add(new int[]{775}, "PE");
            add(new int[]{777}, "BO");
            add(new int[]{779}, "AR");
            add(new int[]{780}, "CL");
            add(new int[]{784}, "PY");
            add(new int[]{785}, "PE");
            add(new int[]{786}, "EC");
            add(new int[]{789, 790}, "BR");
            add(new int[]{800, 839}, "IT");
            add(new int[]{840, 849}, "ES");
            add(new int[]{850}, "CU");
            add(new int[]{858}, "SK");
            add(new int[]{859}, "CZ");
            add(new int[]{860}, "YU");
            add(new int[]{865}, "MN");
            add(new int[]{867}, "KP");
            add(new int[]{868, 869}, "TR");
            add(new int[]{870, 879}, "NL");
            add(new int[]{880}, "KR");
            add(new int[]{885}, "TH");
            add(new int[]{888}, "SG");
            add(new int[]{890}, "IN");
            add(new int[]{893}, "VN");
            add(new int[]{896}, "PK");
            add(new int[]{899}, "ID");
            add(new int[]{900, 919}, "AT");
            add(new int[]{930, 939}, "AU");
            add(new int[]{940, CodePageUtil.CP_MS949}, "AZ");
            add(new int[]{955}, "MY");
            add(new int[]{958}, "MO");
        }
    }
}
