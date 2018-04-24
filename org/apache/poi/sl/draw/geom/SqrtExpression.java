package org.apache.poi.sl.draw.geom;

import java.util.regex.Matcher;

public class SqrtExpression implements Expression {
    private String arg;

    SqrtExpression(Matcher m) {
        this.arg = m.group(1);
    }

    public double evaluate(Context ctx) {
        return Math.sqrt(ctx.getValue(this.arg));
    }
}
