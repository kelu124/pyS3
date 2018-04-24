package org.apache.poi.sl.draw.geom;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    private static final Map<String, ExpressionEntry> impls = new HashMap();

    private static class ExpressionEntry {
        final Constructor<? extends Expression> con;
        final Pattern regex;

        ExpressionEntry(String regex, Class<? extends Expression> cls) throws SecurityException, NoSuchMethodException {
            this.regex = Pattern.compile(regex);
            this.con = cls.getDeclaredConstructor(new Class[]{Matcher.class});
            ExpressionParser.impls.put(ExpressionParser.op(regex), this);
        }
    }

    static {
        try {
            ExpressionEntry expressionEntry = new ExpressionEntry("\\*/ +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", MultiplyDivideExpression.class);
            expressionEntry = new ExpressionEntry("\\+- +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)( 0)?", AddSubtractExpression.class);
            expressionEntry = new ExpressionEntry("\\+/ +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", AddDivideExpression.class);
            expressionEntry = new ExpressionEntry("\\?: +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", IfElseExpression.class);
            expressionEntry = new ExpressionEntry("val +([\\-\\w]+)", LiteralValueExpression.class);
            expressionEntry = new ExpressionEntry("abs +([\\-\\w]+)", AbsExpression.class);
            expressionEntry = new ExpressionEntry("sqrt +([\\-\\w]+)", SqrtExpression.class);
            expressionEntry = new ExpressionEntry("max +([\\-\\w]+) +([\\-\\w]+)", MaxExpression.class);
            expressionEntry = new ExpressionEntry("min +([\\-\\w]+) +([\\-\\w]+)", MinExpression.class);
            expressionEntry = new ExpressionEntry("at2 +([\\-\\w]+) +([\\-\\w]+)", ArcTanExpression.class);
            expressionEntry = new ExpressionEntry("sin +([\\-\\w]+) +([\\-\\w]+)", SinExpression.class);
            expressionEntry = new ExpressionEntry("cos +([\\-\\w]+) +([\\-\\w]+)", CosExpression.class);
            expressionEntry = new ExpressionEntry("tan +([\\-\\w]+) +([\\-\\w]+)", TanExpression.class);
            expressionEntry = new ExpressionEntry("cat2 +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", CosineArcTanExpression.class);
            expressionEntry = new ExpressionEntry("sat2 +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", SinArcTanExpression.class);
            expressionEntry = new ExpressionEntry("pin +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", PinExpression.class);
            expressionEntry = new ExpressionEntry("mod +([\\-\\w]+) +([\\-\\w]+) +([\\-\\w]+)", ModExpression.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String op(String str) {
        return (str == null || !str.contains(" ")) ? "" : str.substring(0, str.indexOf(" ")).replace("\\", "");
    }

    public static Expression parse(String str) {
        ExpressionEntry ee = (ExpressionEntry) impls.get(op(str));
        Matcher m = ee == null ? null : ee.regex.matcher(str);
        if (m == null || !m.matches()) {
            throw new RuntimeException("Unsupported formula: " + str);
        }
        try {
            return (Expression) ee.con.newInstance(new Object[]{m});
        } catch (Exception e) {
            throw new RuntimeException("Unsupported formula: " + str, e);
        }
    }
}
