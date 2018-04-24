package org.apache.poi.sl.draw.geom;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Context {
    final Rectangle2D _anchor;
    final Map<String, Double> _ctx = new HashMap();
    final IAdjustableShape _props;

    public Context(CustomGeometry geom, Rectangle2D anchor, IAdjustableShape props) {
        this._props = props;
        this._anchor = anchor;
        for (Guide gd : geom.adjusts) {
            evaluate(gd);
        }
        for (Guide gd2 : geom.guides) {
            evaluate(gd2);
        }
    }

    public Rectangle2D getShapeAnchor() {
        return this._anchor;
    }

    public Guide getAdjustValue(String name) {
        return this._props.getAdjustValue(name);
    }

    public double getValue(String key) {
        if (key.matches("(\\+|-)?\\d+")) {
            return Double.parseDouble(key);
        }
        Formula builtIn = (Formula) Formula.builtInFormulas.get(key);
        if (builtIn != null) {
            return builtIn.evaluate(this);
        }
        if (this._ctx.containsKey(key)) {
            return ((Double) this._ctx.get(key)).doubleValue();
        }
        throw new RuntimeException("undefined variable: " + key);
    }

    public double evaluate(Formula fmla) {
        double result = fmla.evaluate(this);
        String key = fmla.getName();
        if (key != null) {
            this._ctx.put(key, Double.valueOf(result));
        }
        return result;
    }
}
