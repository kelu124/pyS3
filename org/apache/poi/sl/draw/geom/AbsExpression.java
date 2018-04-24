package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class AbsExpression implements Expression {
    private String arg;

    AbsExpression(Matcher m) {
        this.arg = m.group(1);
    }

    public double evaluate(Context ctx) {
        return Math.abs(ctx.getValue(this.arg));
    }
}
