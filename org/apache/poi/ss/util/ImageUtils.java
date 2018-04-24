package org.apache.poi.ss.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Units;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImageUtils {
    public static final int PIXEL_DPI = 96;
    private static final POILogger logger = POILogFactory.getLogger(ImageUtils.class);

    public static Dimension getImageDimension(InputStream is, int type) {
        Dimension size = new Dimension();
        switch (type) {
            case 5:
            case 6:
            case 7:
                try {
                    ImageInputStream iis = ImageIO.createImageInputStream(is);
                    ImageReader r;
                    try {
                        r = (ImageReader) ImageIO.getImageReaders(iis).next();
                        r.setInput(iis);
                        BufferedImage img = r.read(0);
                        int[] dpi = getResolution(r);
                        if (dpi[0] == 0) {
                            dpi[0] = 96;
                        }
                        if (dpi[1] == 0) {
                            dpi[1] = 96;
                        }
                        size.width = (img.getWidth() * 96) / dpi[0];
                        size.height = (img.getHeight() * 96) / dpi[1];
                        r.dispose();
                        iis.close();
                        break;
                    } catch (Throwable th) {
                        iis.close();
                    }
                } catch (IOException e) {
                    logger.log(5, new Object[]{e});
                    break;
                }
            default:
                logger.log(5, new Object[]{"Only JPEG, PNG and DIB pictures can be automatically sized"});
                break;
        }
        return size;
    }

    public static int[] getResolution(ImageReader r) throws IOException {
        int hdpi = 96;
        int vdpi = 96;
        Element node = (Element) r.getImageMetadata(0).getAsTree("javax_imageio_1.0");
        NodeList lst = node.getElementsByTagName("HorizontalPixelSize");
        if (lst != null && lst.getLength() == 1) {
            hdpi = (int) (25.4d / ((double) Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))));
        }
        lst = node.getElementsByTagName("VerticalPixelSize");
        if (lst != null && lst.getLength() == 1) {
            vdpi = (int) (25.4d / ((double) Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))));
        }
        return new int[]{hdpi, vdpi};
    }

    public static Dimension setPreferredSize(Picture picture, double scaleX, double scaleY) {
        double scaledHeight;
        int col2;
        double delta;
        ClientAnchor anchor = picture.getClientAnchor();
        boolean isHSSF = anchor instanceof HSSFClientAnchor;
        PictureData data = picture.getPictureData();
        Sheet sheet = picture.getSheet();
        Dimension imgSize = getImageDimension(new ByteArrayInputStream(data.getData()), data.getPictureType());
        Dimension anchorSize = getDimensionFromAnchor(picture);
        double scaledWidth = scaleX == Double.MAX_VALUE ? imgSize.getWidth() : (anchorSize.getWidth() / 9525.0d) * scaleX;
        if (scaleY == Double.MAX_VALUE) {
            scaledHeight = imgSize.getHeight();
        } else {
            scaledHeight = (anchorSize.getHeight() / 9525.0d) * scaleY;
        }
        short col1 = anchor.getCol1();
        int dx2 = 0;
        int col22 = col1 + 1;
        double w = (double) sheet.getColumnWidthInPixels(col1);
        if (isHSSF) {
            w *= 1.0d - (((double) anchor.getDx1()) / 1024.0d);
        } else {
            w -= ((double) anchor.getDx1()) / 9525.0d;
        }
        while (w < scaledWidth) {
            w += (double) sheet.getColumnWidthInPixels(col22);
            col22++;
        }
        if (w > scaledWidth) {
            col2 = col22 - 1;
            double cw = (double) sheet.getColumnWidthInPixels(col2);
            delta = w - scaledWidth;
            if (isHSSF) {
                dx2 = (int) (((cw - delta) / cw) * 1024.0d);
            } else {
                dx2 = (int) ((cw - delta) * 9525.0d);
            }
            if (dx2 < 0) {
                dx2 = 0;
            }
        } else {
            col2 = col22;
        }
        anchor.setCol2(col2);
        anchor.setDx2(dx2);
        int row1 = anchor.getRow1();
        int dy2 = 0;
        int row2 = row1 + 1;
        double h = getRowHeightInPixels(sheet, row1);
        if (isHSSF) {
            h *= 1.0d - (((double) anchor.getDy1()) / 256.0d);
        } else {
            h -= ((double) anchor.getDy1()) / 9525.0d;
        }
        while (h < scaledHeight) {
            h += getRowHeightInPixels(sheet, row2);
            row2++;
        }
        if (h > scaledHeight) {
            row1 = row2 - 1;
            double ch = getRowHeightInPixels(sheet, row1);
            delta = h - scaledHeight;
            if (isHSSF) {
                dy2 = (int) (((ch - delta) / ch) * 256.0d);
            } else {
                dy2 = (int) ((ch - delta) * 9525.0d);
            }
            if (dy2 < 0) {
                dy2 = 0;
            }
        } else {
            row1 = row2;
        }
        anchor.setRow2(row1);
        anchor.setDy2(dy2);
        return new Dimension((int) Math.round(9525.0d * scaledWidth), (int) Math.round(9525.0d * scaledHeight));
    }

    public static Dimension getDimensionFromAnchor(Picture picture) {
        short col2;
        ClientAnchor anchor = picture.getClientAnchor();
        boolean isHSSF = anchor instanceof HSSFClientAnchor;
        Sheet sheet = picture.getSheet();
        int col22 = anchor.getCol1();
        short col23 = col22 + 1;
        double w = (double) sheet.getColumnWidthInPixels(col22);
        if (isHSSF) {
            w *= 1.0d - (((double) anchor.getDx1()) / 1024.0d);
            col2 = col23;
        } else {
            w -= ((double) anchor.getDx1()) / 9525.0d;
            col2 = col23;
        }
        while (col2 < anchor.getCol2()) {
            w += (double) sheet.getColumnWidthInPixels(col2);
            col2++;
        }
        if (isHSSF) {
            w += ((double) (sheet.getColumnWidthInPixels(col2) * ((float) anchor.getDx2()))) / 1024.0d;
        } else {
            w += ((double) anchor.getDx2()) / 9525.0d;
        }
        int row2 = anchor.getRow1();
        int row22 = row2 + 1;
        double h = getRowHeightInPixels(sheet, row2);
        if (isHSSF) {
            h *= 1.0d - (((double) anchor.getDy1()) / 256.0d);
            row2 = row22;
        } else {
            h -= ((double) anchor.getDy1()) / 9525.0d;
            row2 = row22;
        }
        while (row2 < anchor.getRow2()) {
            h += getRowHeightInPixels(sheet, row2);
            row2++;
        }
        if (isHSSF) {
            h += (getRowHeightInPixels(sheet, row2) * ((double) anchor.getDy2())) / 256.0d;
        } else {
            h += ((double) anchor.getDy2()) / 9525.0d;
        }
        return new Dimension((int) Math.rint(w * 9525.0d), (int) Math.rint(h * 9525.0d));
    }

    private static double getRowHeightInPixels(Sheet sheet, int rowNum) {
        Row r = sheet.getRow(rowNum);
        return ((double) Units.toEMU(r == null ? (double) sheet.getDefaultRowHeightInPoints() : (double) r.getHeightInPoints())) / 9525.0d;
    }
}
