package org.apache.poi.sl.draw.geom;

import org.apache.poi.sl.draw.binding.CTGeomGuide;

public class AdjustValue extends Guide {
    public AdjustValue(CTGeomGuide gd) {
        super(gd.getName(), gd.getFmla());
    }

    public double evaluate(Context ctx) {
        Guide adj = ctx.getAdjustValue(getName());
        if (adj != null) {
            return adj.evaluate(ctx);
        }
        return super.evaluate(ctx);
    }
}
