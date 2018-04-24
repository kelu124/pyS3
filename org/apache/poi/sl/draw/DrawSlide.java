package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import org.apache.poi.sl.usermodel.Background;
import org.apache.poi.sl.usermodel.Slide;

public class DrawSlide extends DrawSheet {
    public DrawSlide(Slide<?, ?> slide) {
        super(slide);
    }

    public void draw(Graphics2D graphics) {
        Background bg = this.sheet.getBackground();
        if (bg != null) {
            DrawFactory.getInstance(graphics).getDrawable(bg).draw(graphics);
        }
        super.draw(graphics);
    }
}
