package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class DrawTextFragment implements Drawable {
    final TextLayout layout;
    final AttributedString str;
    double f174x;
    double f175y;

    public DrawTextFragment(TextLayout layout, AttributedString str) {
        this.layout = layout;
        this.str = str;
    }

    public void setPosition(double x, double y) {
        this.f174x = x;
        this.f175y = y;
    }

    public void draw(Graphics2D graphics) {
        if (this.str != null) {
            double yBaseline = this.f175y + ((double) this.layout.getAscent());
            Integer textMode = (Integer) graphics.getRenderingHint(Drawable.TEXT_RENDERING_MODE);
            if (textMode == null || textMode.intValue() != 2) {
                graphics.drawString(this.str.getIterator(), (float) this.f174x, (float) yBaseline);
            } else {
                this.layout.draw(graphics, (float) this.f174x, (float) yBaseline);
            }
        }
    }

    public void applyTransform(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D graphics) {
    }

    public TextLayout getLayout() {
        return this.layout;
    }

    public AttributedString getAttributedString() {
        return this.str;
    }

    public float getHeight() {
        return (float) ((Math.ceil((double) this.layout.getAscent()) + Math.ceil((double) this.layout.getDescent())) + ((double) this.layout.getLeading()));
    }

    public float getWidth() {
        return this.layout.getAdvance();
    }

    public String getString() {
        if (this.str == null) {
            return "";
        }
        AttributedCharacterIterator it = this.str.getIterator();
        StringBuilder buf = new StringBuilder();
        for (char c = it.first(); c != '￿'; c = it.next()) {
            buf.append(c);
        }
        return buf.toString();
    }

    public String toString() {
        return "[" + getClass().getSimpleName() + "] " + getString();
    }
}
