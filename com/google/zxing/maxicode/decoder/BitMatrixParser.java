package com.google.zxing.maxicode.decoder;

import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

final class BitMatrixParser {
    private static final int[][] BITNR;
    private final BitMatrix bitMatrix;

    static {
        r0 = new int[33][];
        int[] iArr = new int[]{481, 480, opencv_videoio.CV_CAP_PROP_XI_COLOR_FILTER_ARRAY, opencv_videoio.CV_CAP_PROP_XI_IMAGE_IS_COLOR, opencv_videoio.CV_CAP_PROP_XI_HOUS_TEMP, opencv_videoio.CV_CAP_PROP_XI_CHIP_TEMP, 48, -2, 30, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 53, 52, opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_PACKING, opencv_videoio.CV_CAP_PROP_XI_IMAGE_DATA_BIT_DEPTH, 457, 456, opencv_videoio.CV_CAP_PROP_XI_WIDTH, opencv_videoio.CV_CAP_PROP_XI_WB_KB, 837, -3, iArr};
        r0[16] = new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_10, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_03, opencv_videoio.CV_CAP_PROP_XI_GAMMAC, opencv_videoio.CV_CAP_PROP_XI_GAMMAY, opencv_videoio.CV_CAP_PROP_XI_APPLY_CMS, opencv_videoio.CV_CAP_PROP_XI_CMS, 49, -1, -2, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -2, -1, opencv_videoio.CV_CAP_PROP_XI_IS_COOLED, opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_PACKING_TYPE, opencv_videoio.CV_CAP_PROP_XI_LIMIT_BANDWIDTH, 458, 453, opencv_videoio.CV_CAP_PROP_XI_HEIGHT, 839, 838};
        r0[17] = new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_12, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_11, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_00, opencv_videoio.CV_CAP_PROP_XI_SHARPNESS, 473, 472, 51, 50, 31, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 1, -2, 42, opencv_videoio.CV_CAP_PROP_XI_TARGET_TEMP, opencv_videoio.CV_CAP_PROP_XI_COOLING, opencv_videoio.CV_CAP_PROP_XI_OUTPUT_DATA_BIT_DEPTH, opencv_videoio.CV_CAP_PROP_XI_SENSOR_DATA_BIT_DEPTH, 455, 454, 840, -3};
        r0[18] = new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_20, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_13, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_32, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_31, opencv_videoio.CV_CAP_PROP_XI_ACQ_FRAME_BURST_COUNT, opencv_videoio.CV_CAP_PROP_XI_TRG_SELECTOR, 97, 96, 61, 60, -3, -3, -3, -3, -3, -3, -3, -3, -3, 26, 91, 90, MetaDo.META_CREATEPATTERNBRUSH, 504, 511, opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_POL, 517, opencv_videoio.CV_CAP_PROP_XI_LENS_FOCAL_LENGTH, 842, 841};
        r0[19] = new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_22, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_21, opencv_videoio.CV_CAP_PROP_XI_DEFAULT_CC_MATRIX, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_33, 501, 500, 99, 98, 63, 62, -3, -3, -3, -3, -3, -3, -3, -3, 28, 27, 93, 92, opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_EN, 506, 513, 512, TIFFConstants.TIFFTAG_JPEGQTABLES, 518, 843, -3};
        r0[20] = new int[]{opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_30, opencv_videoio.CV_CAP_PROP_XI_CC_MATRIX_23, 497, MetaDo.META_DELETEOBJECT, 503, 502, 101, 100, 65, 64, 17, -3, -3, -3, -3, -3, -3, -3, 18, 29, 95, 94, opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_T1, opencv_videoio.CV_CAP_PROP_XI_DEBOUNCE_T0, 515, 514, 521, TIFFConstants.TIFFTAG_JPEGDCTABLES, 845, 844};
        r0[21] = new int[]{opencv_videoio.CV_CAP_PROP_XI_HDR, opencv_videoio.CV_CAP_PROP_XI_SENSOR_MODE, 553, 552, 547, 546, opencv_videoio.CV_CAP_PROP_XI_LUT_EN, opencv_videoio.CV_CAP_PROP_XI_BUFFER_POLICY, 73, 72, 32, -3, -3, -3, -3, -3, -3, 10, 67, 66, 115, 114, opencv_videoio.CV_CAP_PROP_XI_FRAMERATE, opencv_videoio.CV_CAP_PROP_XI_SENSOR_OUTPUT_CHANNEL_COUNT, 529, 528, MetaDo.META_SETWINDOWORG, 522, 846, -3};
        r0[22] = new int[]{561, opencv_videoio.CV_CAP_PROP_XI_HDR_KNEEPOINT_COUNT, 555, 554, 549, 548, opencv_videoio.CV_CAP_PROP_XI_LUT_VALUE, opencv_videoio.CV_CAP_PROP_XI_LUT_INDEX, 75, 74, -2, -1, 7, 6, 35, 34, 11, -2, 69, 68, 117, 116, opencv_videoio.CV_CAP_PROP_XI_COUNTER_VALUE, opencv_videoio.CV_CAP_PROP_XI_COUNTER_SELECTOR, 531, 530, MetaDo.META_SETVIEWPORTORG, MetaDo.META_SETWINDOWEXT, 848, 847};
        r0[23] = new int[]{opencv_videoio.CV_CAP_PROP_XI_KNEEPOINT1, opencv_videoio.CV_CAP_PROP_XI_HDR_T2, 557, 556, 551, 550, 545, 544, 77, 76, -2, 33, 9, 8, 25, 24, -1, -2, 71, 70, 119, 118, opencv_videoio.CV_CAP_PROP_XI_AVAILABLE_BANDWIDTH, opencv_videoio.CV_CAP_PROP_XI_ACQ_TIMING_MODE, opencv_videoio.CV_CAP_PROP_XI_SENSOR_CLOCK_FREQ_INDEX, 532, MetaDo.META_OFFSETWINDOWORG, MetaDo.META_SETVIEWPORTEXT, 849, -3};
        r0[24] = new int[]{opencv_videoio.CV_CAP_PROP_XI_IMAGE_BLACK_LEVEL, 564, opencv_videoio.CV_CAP_PROP_XI_HW_REVISION, 570, dc1394.DC1394_TRIGGER_SOURCE_1, 576, opencv_videoio.CV_CAP_PROP_XI_FFS_ACCESS_KEY, opencv_videoio.CV_CAP_PROP_XI_USED_FFS_SIZE, 589, 588, 595, 594, 601, 600, 607, 606, dc1394.DC1394_POWER_CLASS_USES_MAX_3W, dc1394.DC1394_POWER_CLASS_USES_MAX_1W, 619, 618, 625, 624, 631, 630, 637, 636, dc1394.DC1394_PHY_DELAY_UNKNOWN_2, dc1394.DC1394_PHY_DELAY_UNKNOWN_1, 851, 850};
        r0[25] = new int[]{567, 566, opencv_videoio.CV_CAP_PROP_XI_AUTO_BANDWIDTH_CALCULATION, opencv_videoio.CV_CAP_PROP_XI_DEBUG_LEVEL, dc1394.DC1394_TRIGGER_SOURCE_3, 578, opencv_videoio.CV_CAP_PROP_XI_SENSOR_FEATURE_SELECTOR, 584, 591, 590, 597, 596, 603, 602, dc1394.DC1394_POWER_CLASS_PROV_MIN_15W, 608, 615, dc1394.DC1394_POWER_CLASS_USES_MAX_6W, 621, 620, 627, 626, 633, 632, 639, 638, 645, 644, 852, -3};
        r0[26] = new int[]{569, 568, 575, 574, opencv_videoio.CV_CAP_PROP_XI_FREE_FFS_SIZE, 580, 587, opencv_videoio.CV_CAP_PROP_XI_SENSOR_FEATURE_VALUE, 593, 592, 599, 598, 605, 604, dc1394.DC1394_POWER_CLASS_PROV_MIN_45W, dc1394.DC1394_POWER_CLASS_PROV_MIN_30W, 617, 616, 623, 622, 629, 628, 635, 634, 641, 640, 647, 646, 854, 853};
        r0[27] = new int[]{727, 726, 721, 720, 715, 714, 709, 708, 703, 702, 697, 696, 691, 690, 685, 684, 679, 678, 673, 672, 667, Element.WRITABLE_DIRECT, 661, 660, 655, 654, 649, 648, 855, -3};
        r0[28] = new int[]{729, 728, 723, 722, 717, 716, 711, 710, 705, 704, 699, 698, 693, 692, 687, 686, 681, 680, 675, 674, 669, 668, 663, 662, 657, 656, 651, 650, 857, 856};
        r0[29] = new int[]{731, 730, 725, 724, 719, 718, 713, 712, 707, 706, 701, 700, 695, 694, 689, 688, 683, 682, 677, 676, 671, 670, 665, 664, 659, 658, 653, 652, 858, -3};
        r0[30] = new int[]{733, 732, 739, 738, 745, 744, 751, 750, 757, 756, MetaDo.META_CREATEFONTINDIRECT, MetaDo.META_CREATEPENINDIRECT, dc1394.DC1394_LOG_WARNING, 768, 775, 774, 781, 780, 787, 786, 793, 792, 799, 798, MetaDo.META_POLYLINE, MetaDo.META_POLYGON, 811, 810, 860, 859};
        r0[31] = new int[]{735, 734, 741, 740, 747, 746, 753, 752, 759, 758, 765, MetaDo.META_CREATEBRUSHINDIRECT, 771, 770, 777, 776, 783, 782, 789, 788, 795, 794, 801, 800, 807, 806, 813, 812, 861, -3};
        r0[32] = new int[]{dc1394.DC1394_FEATURE_MODE_AUTO, 736, 743, 742, 749, 748, 755, 754, 761, 760, 767, 766, 773, 772, 779, 778, 785, 784, 791, 790, 797, 796, 803, 802, 809, 808, 815, 814, 863, 862};
        BITNR = r0;
    }

    BitMatrixParser(BitMatrix bitMatrix) {
        this.bitMatrix = bitMatrix;
    }

    byte[] readCodewords() {
        byte[] result = new byte[144];
        int height = this.bitMatrix.getHeight();
        int width = this.bitMatrix.getWidth();
        int y = 0;
        while (y < height) {
            int[] bitnrRow = BITNR[y];
            int x = 0;
            while (x < width) {
                int bit = bitnrRow[x];
                if (bit >= 0 && this.bitMatrix.get(x, y)) {
                    int i = bit / 6;
                    result[i] = (byte) (result[i] | ((byte) (1 << (5 - (bit % 6)))));
                }
                x++;
            }
            y++;
        }
        return result;
    }
}
