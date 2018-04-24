package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class CosExpression implements Expression {
    private String arg1;
    private String arg2;

    CosExpression(Matcher m) {
        this.arg1 = m.group(1);
        this.arg2 = m.group(2);
    }

    public double evaluate(Context ctx) {
        return Math.cos(Math.toRadians(ctx.getValue(this.arg2) / 60000.0d)) * ctx.getValue(this.arg1);
    }
}
