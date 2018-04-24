package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class PinExpression implements Expression {
    private String arg1;
    private String arg2;
    private String arg3;

    PinExpression(Matcher m) {
        this.arg1 = m.group(1);
        this.arg2 = m.group(2);
        this.arg3 = m.group(3);
    }

    public double evaluate(Context ctx) {
        double x = ctx.getValue(this.arg1);
        double y = ctx.getValue(this.arg2);
        double z = ctx.getValue(this.arg3);
        if (y < x) {
            return x;
        }
        return y > z ? z : y;
    }
}
