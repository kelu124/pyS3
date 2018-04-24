package org.apache.poi.ss.formula.udf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.poi.ss.formula.atp.AnalysisToolPak;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

public class AggregatingUDFFinder implements UDFFinder {
    public static final UDFFinder DEFAULT = new AggregatingUDFFinder(AnalysisToolPak.instance);
    private final Collection<UDFFinder> _usedToolPacks;

    public AggregatingUDFFinder(UDFFinder... usedToolPacks) {
        this._usedToolPacks = new ArrayList(usedToolPacks.length);
        this._usedToolPacks.addAll(Arrays.asList(usedToolPacks));
    }

    public FreeRefFunction findFunction(String name) {
        for (UDFFinder pack : this._usedToolPacks) {
            FreeRefFunction evaluatorForFunction = pack.findFunction(name);
            if (evaluatorForFunction != null) {
                return evaluatorForFunction;
            }
        }
        return null;
    }

    public void add(UDFFinder toolPack) {
        this._usedToolPacks.add(toolPack);
    }
}
