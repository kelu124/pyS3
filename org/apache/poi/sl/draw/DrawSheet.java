package org.apache.poi.sl.draw;

import com.itextpdf.text.pdf.BaseField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.apache.poi.sl.usermodel.MasterSheet;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.Sheet;

public class DrawSheet implements Drawable {
    protected final Sheet<?, ?> sheet;

    public DrawSheet(Sheet<?, ?> sheet) {
        this.sheet = sheet;
    }

    public void draw(Graphics2D graphics) {
        Dimension dim = this.sheet.getSlideShow().getPageSize();
        graphics.setColor(new Color(BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, 0.0f));
        graphics.fillRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
        DrawFactory drawFact = DrawFactory.getInstance(graphics);
        MasterSheet master = this.sheet.getMasterSheet();
        if (this.sheet.getFollowMasterGraphics() && master != null) {
            drawFact.getDrawable(master).draw(graphics);
        }
        graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, new AffineTransform());
        for (Shape shape : this.sheet.getShapes()) {
            if (canDraw(shape)) {
                AffineTransform at = graphics.getTransform();
                graphics.setRenderingHint(Drawable.GSAVE, Boolean.valueOf(true));
                Drawable drawer = drawFact.getDrawable(shape);
                drawer.applyTransform(graphics);
                drawer.draw(graphics);
                graphics.setTransform(at);
                graphics.setRenderingHint(Drawable.GRESTORE, Boolean.valueOf(true));
            }
        }
    }

    public void applyTransform(Graphics2D context) {
    }

    public void drawContent(Graphics2D context) {
    }

    protected boolean canDraw(Shape<?, ?> shape) {
        return true;
    }
}
