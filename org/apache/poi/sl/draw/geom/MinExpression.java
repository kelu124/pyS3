package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class MinExpression implements Expression {
    private String arg1;
    private String arg2;

    MinExpression(Matcher m) {
        this.arg1 = m.group(1);
        this.arg2 = m.group(2);
    }

    public double evaluate(Context ctx) {
        return Math.min(ctx.getValue(this.arg1), ctx.getValue(this.arg2));
    }
}
