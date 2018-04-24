package com.itextpdf.text;

import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.draw.DrawInterface;

public class TabStop {
    protected Alignment alignment;
    protected char anchorChar;
    protected DrawInterface leader;
    protected float position;

    public enum Alignment {
        LEFT,
        RIGHT,
        CENTER,
        ANCHOR
    }

    public static TabStop newInstance(float currentPosition, float tabInterval) {
        currentPosition = ((float) Math.round(currentPosition * 1000.0f)) / 1000.0f;
        tabInterval = ((float) Math.round(tabInterval * 1000.0f)) / 1000.0f;
        return new TabStop((currentPosition + tabInterval) - (currentPosition % tabInterval));
    }

    public TabStop(float position) {
        this(position, Alignment.LEFT);
    }

    public TabStop(float position, DrawInterface leader) {
        this(position, leader, Alignment.LEFT);
    }

    public TabStop(float position, Alignment alignment) {
        this(position, null, alignment);
    }

    public TabStop(float position, Alignment alignment, char anchorChar) {
        this(position, null, alignment, anchorChar);
    }

    public TabStop(float position, DrawInterface leader, Alignment alignment) {
        this(position, leader, alignment, '.');
    }

    public TabStop(float position, DrawInterface leader, Alignment alignment, char anchorChar) {
        this.alignment = Alignment.LEFT;
        this.anchorChar = '.';
        this.position = position;
        this.leader = leader;
        this.alignment = alignment;
        this.anchorChar = anchorChar;
    }

    public TabStop(TabStop tabStop) {
        this(tabStop.getPosition(), tabStop.getLeader(), tabStop.getAlignment(), tabStop.getAnchorChar());
    }

    public float getPosition() {
        return this.position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public DrawInterface getLeader() {
        return this.leader;
    }

    public void setLeader(DrawInterface leader) {
        this.leader = leader;
    }

    public char getAnchorChar() {
        return this.anchorChar;
    }

    public void setAnchorChar(char anchorChar) {
        this.anchorChar = anchorChar;
    }

    public float getPosition(float tabPosition, float currentPosition, float anchorPosition) {
        float newPosition = this.position;
        float textWidth = currentPosition - tabPosition;
        switch (this.alignment) {
            case RIGHT:
                if (tabPosition + textWidth < this.position) {
                    return this.position - textWidth;
                }
                return tabPosition;
            case CENTER:
                if ((textWidth / BaseField.BORDER_WIDTH_MEDIUM) + tabPosition < this.position) {
                    return this.position - (textWidth / BaseField.BORDER_WIDTH_MEDIUM);
                }
                return tabPosition;
            case ANCHOR:
                if (Float.isNaN(anchorPosition)) {
                    if (tabPosition + textWidth < this.position) {
                        return this.position - textWidth;
                    }
                    return tabPosition;
                } else if (anchorPosition < this.position) {
                    return this.position - (anchorPosition - tabPosition);
                } else {
                    return tabPosition;
                }
            default:
                return newPosition;
        }
    }
}
