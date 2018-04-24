package org.apache.poi.ss.formula.atp;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.ss.formula.functions.Bin2Dec;
import org.apache.poi.ss.formula.functions.Complex;
import org.apache.poi.ss.formula.functions.Countifs;
import org.apache.poi.ss.formula.functions.Dec2Bin;
import org.apache.poi.ss.formula.functions.Dec2Hex;
import org.apache.poi.ss.formula.functions.Delta;
import org.apache.poi.ss.formula.functions.EDate;
import org.apache.poi.ss.formula.functions.EOMonth;
import org.apache.poi.ss.formula.functions.FactDouble;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.functions.Hex2Dec;
import org.apache.poi.ss.formula.functions.ImReal;
import org.apache.poi.ss.formula.functions.Imaginary;
import org.apache.poi.ss.formula.functions.Oct2Dec;
import org.apache.poi.ss.formula.functions.Quotient;
import org.apache.poi.ss.formula.functions.Sumifs;
import org.apache.poi.ss.formula.functions.WeekNum;
import org.apache.poi.ss.formula.udf.UDFFinder;

public final class AnalysisToolPak implements UDFFinder {
    public static final UDFFinder instance = new AnalysisToolPak();
    private final Map<String, FreeRefFunction> _functionsByName = createFunctionsMap();

    private static final class NotImplemented implements FreeRefFunction {
        private final String _functionName;

        public NotImplemented(String functionName) {
            this._functionName = functionName;
        }

        public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
            throw new NotImplementedFunctionException(this._functionName);
        }
    }

    private AnalysisToolPak() {
    }

    public FreeRefFunction findFunction(String name) {
        if (name.startsWith("_xlfn.")) {
            name = name.substring(6);
        }
        return (FreeRefFunction) this._functionsByName.get(name.toUpperCase(Locale.ROOT));
    }

    private Map<String, FreeRefFunction> createFunctionsMap() {
        Map<String, FreeRefFunction> m = new HashMap(108);
        m197r(m, "ACCRINT", null);
        m197r(m, "ACCRINTM", null);
        m197r(m, "AMORDEGRC", null);
        m197r(m, "AMORLINC", null);
        m197r(m, "AVERAGEIF", null);
        m197r(m, "AVERAGEIFS", null);
        m197r(m, "BAHTTEXT", null);
        m197r(m, "BESSELI", null);
        m197r(m, "BESSELJ", null);
        m197r(m, "BESSELK", null);
        m197r(m, "BESSELY", null);
        m197r(m, "BIN2DEC", Bin2Dec.instance);
        m197r(m, "BIN2HEX", null);
        m197r(m, "BIN2OCT", null);
        m197r(m, "COMPLEX", Complex.instance);
        m197r(m, "CONVERT", null);
        m197r(m, "COUNTIFS", null);
        m197r(m, "COUPDAYBS", null);
        m197r(m, "COUPDAYS", null);
        m197r(m, "COUPDAYSNC", null);
        m197r(m, "COUPNCD", null);
        m197r(m, "COUPNUM", null);
        m197r(m, "COUPPCD", null);
        m197r(m, "CUBEKPIMEMBER", null);
        m197r(m, "CUBEMEMBER", null);
        m197r(m, "CUBEMEMBERPROPERTY", null);
        m197r(m, "CUBERANKEDMEMBER", null);
        m197r(m, "CUBESET", null);
        m197r(m, "CUBESETCOUNT", null);
        m197r(m, "CUBEVALUE", null);
        m197r(m, "CUMIPMT", null);
        m197r(m, "CUMPRINC", null);
        m197r(m, "DEC2BIN", Dec2Bin.instance);
        m197r(m, "DEC2HEX", Dec2Hex.instance);
        m197r(m, "DEC2OCT", null);
        m197r(m, "DELTA", Delta.instance);
        m197r(m, "DISC", null);
        m197r(m, "DOLLARDE", null);
        m197r(m, "DOLLARFR", null);
        m197r(m, "DURATION", null);
        m197r(m, "EDATE", EDate.instance);
        m197r(m, "EFFECT", null);
        m197r(m, "EOMONTH", EOMonth.instance);
        m197r(m, "ERF", null);
        m197r(m, "ERFC", null);
        m197r(m, "FACTDOUBLE", FactDouble.instance);
        m197r(m, "FVSCHEDULE", null);
        m197r(m, "GCD", null);
        m197r(m, "GESTEP", null);
        m197r(m, "HEX2BIN", null);
        m197r(m, "HEX2DEC", Hex2Dec.instance);
        m197r(m, "HEX2OCT", null);
        m197r(m, "IFERROR", IfError.instance);
        m197r(m, "IMABS", null);
        m197r(m, "IMAGINARY", Imaginary.instance);
        m197r(m, "IMARGUMENT", null);
        m197r(m, "IMCONJUGATE", null);
        m197r(m, "IMCOS", null);
        m197r(m, "IMDIV", null);
        m197r(m, "IMEXP", null);
        m197r(m, "IMLN", null);
        m197r(m, "IMLOG10", null);
        m197r(m, "IMLOG2", null);
        m197r(m, "IMPOWER", null);
        m197r(m, "IMPRODUCT", null);
        m197r(m, "IMREAL", ImReal.instance);
        m197r(m, "IMSIN", null);
        m197r(m, "IMSQRT", null);
        m197r(m, "IMSUB", null);
        m197r(m, "IMSUM", null);
        m197r(m, "INTRATE", null);
        m197r(m, "ISEVEN", ParityFunction.IS_EVEN);
        m197r(m, "ISODD", ParityFunction.IS_ODD);
        m197r(m, "JIS", null);
        m197r(m, "LCM", null);
        m197r(m, "MDURATION", null);
        m197r(m, "MROUND", MRound.instance);
        m197r(m, "MULTINOMIAL", null);
        m197r(m, "NETWORKDAYS", NetworkdaysFunction.instance);
        m197r(m, "NOMINAL", null);
        m197r(m, "OCT2BIN", null);
        m197r(m, "OCT2DEC", Oct2Dec.instance);
        m197r(m, "OCT2HEX", null);
        m197r(m, "ODDFPRICE", null);
        m197r(m, "ODDFYIELD", null);
        m197r(m, "ODDLPRICE", null);
        m197r(m, "ODDLYIELD", null);
        m197r(m, "PRICE", null);
        m197r(m, "PRICEDISC", null);
        m197r(m, "PRICEMAT", null);
        m197r(m, "QUOTIENT", Quotient.instance);
        m197r(m, "RANDBETWEEN", RandBetween.instance);
        m197r(m, "RECEIVED", null);
        m197r(m, "RTD", null);
        m197r(m, "SERIESSUM", null);
        m197r(m, "SQRTPI", null);
        m197r(m, "SUMIFS", Sumifs.instance);
        m197r(m, "TBILLEQ", null);
        m197r(m, "TBILLPRICE", null);
        m197r(m, "TBILLYIELD", null);
        m197r(m, "WEEKNUM", WeekNum.instance);
        m197r(m, "WORKDAY", WorkdayFunction.instance);
        m197r(m, "XIRR", null);
        m197r(m, "XNPV", null);
        m197r(m, "YEARFRAC", YearFrac.instance);
        m197r(m, "YIELD", null);
        m197r(m, "YIELDDISC", null);
        m197r(m, "YIELDMAT", null);
        m197r(m, "COUNTIFS", Countifs.instance);
        return m;
    }

    private static void m197r(Map<String, FreeRefFunction> m, String functionName, FreeRefFunction pFunc) {
        FreeRefFunction func;
        if (pFunc == null) {
            func = new NotImplemented(functionName);
        } else {
            func = pFunc;
        }
        m.put(functionName, func);
    }

    public static boolean isATPFunction(String name) {
        return instance._functionsByName.containsKey(name);
    }

    public static Collection<String> getSupportedFunctionNames() {
        AnalysisToolPak inst = instance;
        Collection<String> lst = new TreeSet();
        for (Entry<String, FreeRefFunction> me : inst._functionsByName.entrySet()) {
            FreeRefFunction func = (FreeRefFunction) me.getValue();
            if (!(func == null || (func instanceof NotImplemented))) {
                lst.add(me.getKey());
            }
        }
        return Collections.unmodifiableCollection(lst);
    }

    public static Collection<String> getNotSupportedFunctionNames() {
        AnalysisToolPak inst = instance;
        Collection<String> lst = new TreeSet();
        for (Entry<String, FreeRefFunction> me : inst._functionsByName.entrySet()) {
            if (((FreeRefFunction) me.getValue()) instanceof NotImplemented) {
                lst.add(me.getKey());
            }
        }
        return Collections.unmodifiableCollection(lst);
    }

    public static void registerFunction(String name, FreeRefFunction func) {
        AnalysisToolPak inst = instance;
        if (isATPFunction(name)) {
            FreeRefFunction f = inst.findFunction(name);
            if (f == null || (f instanceof NotImplemented)) {
                inst._functionsByName.put(name, func);
                return;
            }
            throw new IllegalArgumentException("POI already implememts " + name + ". You cannot override POI's implementations of Excel functions");
        } else if (FunctionMetadataRegistry.getFunctionByName(name) != null) {
            throw new IllegalArgumentException(name + " is a built-in Excel function. " + "Use FunctoinEval.registerFunction(String name, Function func) instead.");
        } else {
            throw new IllegalArgumentException(name + " is not a function from the Excel Analysis Toolpack.");
        }
    }
}
