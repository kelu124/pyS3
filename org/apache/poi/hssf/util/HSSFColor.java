package org.apache.poi.hssf.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.ss.usermodel.Color;

public class HSSFColor implements Color {
    private static Map<Integer, HSSFColor> indexHash;

    public static final class AQUA extends HSSFColor {
        public static final String hexString = "3333:CCCC:CCCC";
        public static final short index = (short) 49;
        public static final short[] triplet = new short[]{(short) 51, (short) 204, (short) 204};

        public short getIndex() {
            return (short) 49;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class AUTOMATIC extends HSSFColor {
        public static final short index = (short) 64;
        private static HSSFColor instance = new AUTOMATIC();

        public short getIndex() {
            return (short) 64;
        }

        public short[] getTriplet() {
            return BLACK.triplet;
        }

        public String getHexString() {
            return BLACK.hexString;
        }

        public static HSSFColor getInstance() {
            return instance;
        }
    }

    public static final class BLACK extends HSSFColor {
        public static final String hexString = "0:0:0";
        public static final short index = (short) 8;
        public static final short[] triplet = new short[]{(short) 0, (short) 0, (short) 0};

        public short getIndex() {
            return (short) 8;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class BLUE extends HSSFColor {
        public static final String hexString = "0:0:FFFF";
        public static final short index = (short) 12;
        public static final short index2 = (short) 39;
        public static final short[] triplet = new short[]{(short) 0, (short) 0, (short) 255};

        public short getIndex() {
            return (short) 12;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class BLUE_GREY extends HSSFColor {
        public static final String hexString = "6666:6666:9999";
        public static final short index = (short) 54;
        public static final short[] triplet = new short[]{EscherAggregate.ST_CURVEDRIGHTARROW, EscherAggregate.ST_CURVEDRIGHTARROW, EscherAggregate.ST_TEXTCURVEDOWN};

        public short getIndex() {
            return (short) 54;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class BRIGHT_GREEN extends HSSFColor {
        public static final String hexString = "0:FFFF:0";
        public static final short index = (short) 11;
        public static final short index2 = (short) 35;
        public static final short[] triplet = new short[]{(short) 0, (short) 255, (short) 0};

        public short getIndex() {
            return (short) 11;
        }

        public String getHexString() {
            return hexString;
        }

        public short[] getTriplet() {
            return triplet;
        }
    }

    public static final class BROWN extends HSSFColor {
        public static final String hexString = "9999:3333:0";
        public static final short index = (short) 60;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCURVEDOWN, (short) 51, (short) 0};

        public short getIndex() {
            return (short) 60;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class CORAL extends HSSFColor {
        public static final String hexString = "FFFF:8080:8080";
        public static final short index = (short) 29;
        public static final short[] triplet = new short[]{(short) 255, (short) 128, (short) 128};

        public short getIndex() {
            return (short) 29;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class CORNFLOWER_BLUE extends HSSFColor {
        public static final String hexString = "9999:9999:FFFF";
        public static final short index = (short) 24;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCURVEDOWN, EscherAggregate.ST_TEXTCURVEDOWN, (short) 255};

        public short getIndex() {
            return (short) 24;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class DARK_BLUE extends HSSFColor {
        public static final String hexString = "0:0:8080";
        public static final short index = (short) 18;
        public static final short index2 = (short) 32;
        public static final short[] triplet = new short[]{(short) 0, (short) 0, (short) 128};

        public short getIndex() {
            return (short) 18;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class DARK_GREEN extends HSSFColor {
        public static final String hexString = "0:3333:0";
        public static final short index = (short) 58;
        public static final short[] triplet = new short[]{(short) 0, (short) 51, (short) 0};

        public short getIndex() {
            return (short) 58;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class DARK_RED extends HSSFColor {
        public static final String hexString = "8080:0:0";
        public static final short index = (short) 16;
        public static final short index2 = (short) 37;
        public static final short[] triplet = new short[]{(short) 128, (short) 0, (short) 0};

        public short getIndex() {
            return (short) 16;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class DARK_TEAL extends HSSFColor {
        public static final String hexString = "0:3333:6666";
        public static final short index = (short) 56;
        public static final short[] triplet = new short[]{(short) 0, (short) 51, EscherAggregate.ST_CURVEDRIGHTARROW};

        public short getIndex() {
            return (short) 56;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class DARK_YELLOW extends HSSFColor {
        public static final String hexString = "8080:8080:0";
        public static final short index = (short) 19;
        public static final short[] triplet = new short[]{(short) 128, (short) 128, (short) 0};

        public short getIndex() {
            return (short) 19;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GOLD extends HSSFColor {
        public static final String hexString = "FFFF:CCCC:0";
        public static final short index = (short) 51;
        public static final short[] triplet = new short[]{(short) 255, (short) 204, (short) 0};

        public short getIndex() {
            return (short) 51;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GREEN extends HSSFColor {
        public static final String hexString = "0:8080:0";
        public static final short index = (short) 17;
        public static final short[] triplet = new short[]{(short) 0, (short) 128, (short) 0};

        public short getIndex() {
            return (short) 17;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GREY_25_PERCENT extends HSSFColor {
        public static final String hexString = "C0C0:C0C0:C0C0";
        public static final short index = (short) 22;
        public static final short[] triplet = new short[]{(short) 192, (short) 192, (short) 192};

        public short getIndex() {
            return (short) 22;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GREY_40_PERCENT extends HSSFColor {
        public static final String hexString = "9696:9696:9696";
        public static final short index = (short) 55;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCIRCLEPOUR, EscherAggregate.ST_TEXTCIRCLEPOUR, EscherAggregate.ST_TEXTCIRCLEPOUR};

        public short getIndex() {
            return (short) 55;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GREY_50_PERCENT extends HSSFColor {
        public static final String hexString = "8080:8080:8080";
        public static final short index = (short) 23;
        public static final short[] triplet = new short[]{(short) 128, (short) 128, (short) 128};

        public short getIndex() {
            return (short) 23;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class GREY_80_PERCENT extends HSSFColor {
        public static final String hexString = "3333:3333:3333";
        public static final short index = (short) 63;
        public static final short[] triplet = new short[]{(short) 51, (short) 51, (short) 51};

        public short getIndex() {
            return (short) 63;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class INDIGO extends HSSFColor {
        public static final String hexString = "3333:3333:9999";
        public static final short index = (short) 62;
        public static final short[] triplet = new short[]{(short) 51, (short) 51, EscherAggregate.ST_TEXTCURVEDOWN};

        public short getIndex() {
            return (short) 62;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LAVENDER extends HSSFColor {
        public static final String hexString = "CCCC:9999:FFFF";
        public static final short index = (short) 46;
        public static final short[] triplet = new short[]{(short) 204, EscherAggregate.ST_TEXTCURVEDOWN, (short) 255};

        public short getIndex() {
            return (short) 46;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LEMON_CHIFFON extends HSSFColor {
        public static final String hexString = "FFFF:FFFF:CCCC";
        public static final short index = (short) 26;
        public static final short[] triplet = new short[]{(short) 255, (short) 255, (short) 204};

        public short getIndex() {
            return (short) 26;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_BLUE extends HSSFColor {
        public static final String hexString = "3333:6666:FFFF";
        public static final short index = (short) 48;
        public static final short[] triplet = new short[]{(short) 51, EscherAggregate.ST_CURVEDRIGHTARROW, (short) 255};

        public short getIndex() {
            return (short) 48;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_CORNFLOWER_BLUE extends HSSFColor {
        public static final String hexString = "CCCC:CCCC:FFFF";
        public static final short index = (short) 31;
        public static final short[] triplet = new short[]{(short) 204, (short) 204, (short) 255};

        public short getIndex() {
            return (short) 31;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_GREEN extends HSSFColor {
        public static final String hexString = "CCCC:FFFF:CCCC";
        public static final short index = (short) 42;
        public static final short[] triplet = new short[]{(short) 204, (short) 255, (short) 204};

        public short getIndex() {
            return (short) 42;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_ORANGE extends HSSFColor {
        public static final String hexString = "FFFF:9999:0";
        public static final short index = (short) 52;
        public static final short[] triplet = new short[]{(short) 255, EscherAggregate.ST_TEXTCURVEDOWN, (short) 0};

        public short getIndex() {
            return (short) 52;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_TURQUOISE extends HSSFColor {
        public static final String hexString = "CCCC:FFFF:FFFF";
        public static final short index = (short) 41;
        public static final short index2 = (short) 27;
        public static final short[] triplet = new short[]{(short) 204, (short) 255, (short) 255};

        public short getIndex() {
            return (short) 41;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIGHT_YELLOW extends HSSFColor {
        public static final String hexString = "FFFF:FFFF:9999";
        public static final short index = (short) 43;
        public static final short[] triplet = new short[]{(short) 255, (short) 255, EscherAggregate.ST_TEXTCURVEDOWN};

        public short getIndex() {
            return (short) 43;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class LIME extends HSSFColor {
        public static final String hexString = "9999:CCCC:0";
        public static final short index = (short) 50;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCURVEDOWN, (short) 204, (short) 0};

        public short getIndex() {
            return (short) 50;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class MAROON extends HSSFColor {
        public static final String hexString = "8000:0:0";
        public static final short index = (short) 25;
        public static final short[] triplet = new short[]{(short) 127, (short) 0, (short) 0};

        public short getIndex() {
            return (short) 25;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static class OLIVE_GREEN extends HSSFColor {
        public static final String hexString = "3333:3333:0";
        public static final short index = (short) 59;
        public static final short[] triplet = new short[]{(short) 51, (short) 51, (short) 0};

        public short getIndex() {
            return (short) 59;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class ORANGE extends HSSFColor {
        public static final String hexString = "FFFF:6666:0";
        public static final short index = (short) 53;
        public static final short[] triplet = new short[]{(short) 255, EscherAggregate.ST_CURVEDRIGHTARROW, (short) 0};

        public short getIndex() {
            return (short) 53;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class ORCHID extends HSSFColor {
        public static final String hexString = "6666:0:6666";
        public static final short index = (short) 28;
        public static final short[] triplet = new short[]{EscherAggregate.ST_CURVEDRIGHTARROW, (short) 0, EscherAggregate.ST_CURVEDRIGHTARROW};

        public short getIndex() {
            return (short) 28;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class PALE_BLUE extends HSSFColor {
        public static final String hexString = "9999:CCCC:FFFF";
        public static final short index = (short) 44;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCURVEDOWN, (short) 204, (short) 255};

        public short getIndex() {
            return (short) 44;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class PINK extends HSSFColor {
        public static final String hexString = "FFFF:0:FFFF";
        public static final short index = (short) 14;
        public static final short index2 = (short) 33;
        public static final short[] triplet = new short[]{(short) 255, (short) 0, (short) 255};

        public short getIndex() {
            return (short) 14;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class PLUM extends HSSFColor {
        public static final String hexString = "9999:3333:6666";
        public static final short index = (short) 61;
        public static final short index2 = (short) 25;
        public static final short[] triplet = new short[]{EscherAggregate.ST_TEXTCURVEDOWN, (short) 51, EscherAggregate.ST_CURVEDRIGHTARROW};

        public short getIndex() {
            return (short) 61;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class RED extends HSSFColor {
        public static final String hexString = "FFFF:0:0";
        public static final short index = (short) 10;
        public static final short[] triplet = new short[]{(short) 255, (short) 0, (short) 0};

        public short getIndex() {
            return (short) 10;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class ROSE extends HSSFColor {
        public static final String hexString = "FFFF:9999:CCCC";
        public static final short index = (short) 45;
        public static final short[] triplet = new short[]{(short) 255, EscherAggregate.ST_TEXTCURVEDOWN, (short) 204};

        public short getIndex() {
            return (short) 45;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class ROYAL_BLUE extends HSSFColor {
        public static final String hexString = "0:6666:CCCC";
        public static final short index = (short) 30;
        public static final short[] triplet = new short[]{(short) 0, EscherAggregate.ST_CURVEDRIGHTARROW, (short) 204};

        public short getIndex() {
            return (short) 30;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class SEA_GREEN extends HSSFColor {
        public static final String hexString = "3333:9999:6666";
        public static final short index = (short) 57;
        public static final short[] triplet = new short[]{(short) 51, EscherAggregate.ST_TEXTCURVEDOWN, EscherAggregate.ST_CURVEDRIGHTARROW};

        public short getIndex() {
            return (short) 57;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class SKY_BLUE extends HSSFColor {
        public static final String hexString = "0:CCCC:FFFF";
        public static final short index = (short) 40;
        public static final short[] triplet = new short[]{(short) 0, (short) 204, (short) 255};

        public short getIndex() {
            return (short) 40;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class TAN extends HSSFColor {
        public static final String hexString = "FFFF:CCCC:9999";
        public static final short index = (short) 47;
        public static final short[] triplet = new short[]{(short) 255, (short) 204, EscherAggregate.ST_TEXTCURVEDOWN};

        public short getIndex() {
            return (short) 47;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class TEAL extends HSSFColor {
        public static final String hexString = "0:8080:8080";
        public static final short index = (short) 21;
        public static final short index2 = (short) 38;
        public static final short[] triplet = new short[]{(short) 0, (short) 128, (short) 128};

        public short getIndex() {
            return (short) 21;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class TURQUOISE extends HSSFColor {
        public static final String hexString = "0:FFFF:FFFF";
        public static final short index = (short) 15;
        public static final short index2 = (short) 35;
        public static final short[] triplet = new short[]{(short) 0, (short) 255, (short) 255};

        public short getIndex() {
            return (short) 15;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class VIOLET extends HSSFColor {
        public static final String hexString = "8080:0:8080";
        public static final short index = (short) 20;
        public static final short index2 = (short) 36;
        public static final short[] triplet = new short[]{(short) 128, (short) 0, (short) 128};

        public short getIndex() {
            return (short) 20;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class WHITE extends HSSFColor {
        public static final String hexString = "FFFF:FFFF:FFFF";
        public static final short index = (short) 9;
        public static final short[] triplet = new short[]{(short) 255, (short) 255, (short) 255};

        public short getIndex() {
            return (short) 9;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final class YELLOW extends HSSFColor {
        public static final String hexString = "FFFF:FFFF:0";
        public static final short index = (short) 13;
        public static final short index2 = (short) 34;
        public static final short[] triplet = new short[]{(short) 255, (short) 255, (short) 0};

        public short getIndex() {
            return (short) 13;
        }

        public short[] getTriplet() {
            return triplet;
        }

        public String getHexString() {
            return hexString;
        }
    }

    public static final Map<Integer, HSSFColor> getIndexHash() {
        if (indexHash == null) {
            indexHash = Collections.unmodifiableMap(createColorsByIndexMap());
        }
        return indexHash;
    }

    public static final Map<Integer, HSSFColor> getMutableIndexHash() {
        return createColorsByIndexMap();
    }

    private static Map<Integer, HSSFColor> createColorsByIndexMap() {
        HSSFColor[] colors = getAllColors();
        Map<Integer, HSSFColor> result = new HashMap((colors.length * 3) / 2);
        for (HSSFColor color : colors) {
            Integer index1 = Integer.valueOf(color.getIndex());
            if (result.containsKey(index1)) {
                throw new RuntimeException("Dup color index (" + index1 + ") for colors (" + ((HSSFColor) result.get(index1)).getClass().getName() + "),(" + color.getClass().getName() + ")");
            }
            result.put(index1, color);
        }
        for (HSSFColor color2 : colors) {
            Integer index2 = getIndex2(color2);
            if (index2 != null) {
                result.put(index2, color2);
            }
        }
        return result;
    }

    private static Integer getIndex2(HSSFColor color) {
        try {
            try {
                return Integer.valueOf(((Short) color.getClass().getDeclaredField("index2").get(color)).intValue());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        } catch (NoSuchFieldException e3) {
            return null;
        }
    }

    private static HSSFColor[] getAllColors() {
        return new HSSFColor[]{new BLACK(), new BROWN(), new OLIVE_GREEN(), new DARK_GREEN(), new DARK_TEAL(), new DARK_BLUE(), new INDIGO(), new GREY_80_PERCENT(), new ORANGE(), new DARK_YELLOW(), new GREEN(), new TEAL(), new BLUE(), new BLUE_GREY(), new GREY_50_PERCENT(), new RED(), new LIGHT_ORANGE(), new LIME(), new SEA_GREEN(), new AQUA(), new LIGHT_BLUE(), new VIOLET(), new GREY_40_PERCENT(), new PINK(), new GOLD(), new YELLOW(), new BRIGHT_GREEN(), new TURQUOISE(), new DARK_RED(), new SKY_BLUE(), new PLUM(), new GREY_25_PERCENT(), new ROSE(), new LIGHT_YELLOW(), new LIGHT_GREEN(), new LIGHT_TURQUOISE(), new PALE_BLUE(), new LAVENDER(), new WHITE(), new CORNFLOWER_BLUE(), new LEMON_CHIFFON(), new MAROON(), new ORCHID(), new CORAL(), new ROYAL_BLUE(), new LIGHT_CORNFLOWER_BLUE(), new TAN()};
    }

    public static final Map<String, HSSFColor> getTripletHash() {
        return createColorsByHexStringMap();
    }

    private static Map<String, HSSFColor> createColorsByHexStringMap() {
        HSSFColor[] colors = getAllColors();
        Map<String, HSSFColor> result = new HashMap((colors.length * 3) / 2);
        for (HSSFColor color : colors) {
            String hexString = color.getHexString();
            if (result.containsKey(hexString)) {
                throw new RuntimeException("Dup color hexString (" + hexString + ") for color (" + color.getClass().getName() + ") - " + " already taken by (" + ((HSSFColor) result.get(hexString)).getClass().getName() + ")");
            }
            result.put(hexString, color);
        }
        return result;
    }

    public short getIndex() {
        return (short) 8;
    }

    public short[] getTriplet() {
        return BLACK.triplet;
    }

    public String getHexString() {
        return BLACK.hexString;
    }

    public static HSSFColor toHSSFColor(Color color) {
        if (color == null || (color instanceof HSSFColor)) {
            return (HSSFColor) color;
        }
        throw new IllegalArgumentException("Only HSSFColor objects are supported");
    }
}
