package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class CosineArcTanExpression implements Expression {
    private String arg1;
    private String arg2;
    private String arg3;

    CosineArcTanExpression(Matcher m) {
        this.arg1 = m.group(1);
        this.arg2 = m.group(2);
        this.arg3 = m.group(3);
    }

    public double evaluate(Context ctx) {
        return Math.cos(Math.atan(ctx.getValue(this.arg3) / ctx.getValue(this.arg2))) * ctx.getValue(this.arg1);
    }
}
