package org.apache.poi.ss.formula;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment.WorkbookNotFoundException;
import org.apache.poi.ss.formula.atp.AnalysisToolPak;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.ExternalNameEval;
import org.apache.poi.ss.formula.eval.FunctionEval;
import org.apache.poi.ss.formula.eval.FunctionNameEval;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.Choose;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.formula.functions.IfFunc;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.ControlPtg;
import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.ExpPtg;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemErrPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.MissingArgPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.UnionPtg;
import org.apache.poi.ss.formula.ptg.UnknownPtg;
import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class WorkbookEvaluator {
    private static final POILogger LOG = POILogFactory.getLogger(WorkbookEvaluator.class);
    private final POILogger EVAL_LOG;
    private EvaluationCache _cache;
    private CollaboratingWorkbooksEnvironment _collaboratingWorkbookEnvironment;
    private final IEvaluationListener _evaluationListener;
    private boolean _ignoreMissingWorkbooks;
    private final Map<String, Integer> _sheetIndexesByName;
    private final Map<EvaluationSheet, Integer> _sheetIndexesBySheet;
    private final IStabilityClassifier _stabilityClassifier;
    private final AggregatingUDFFinder _udfFinder;
    private final EvaluationWorkbook _workbook;
    private int _workbookIx;
    private boolean dbgEvaluationOutputForNextEval;
    private int dbgEvaluationOutputIndent;

    public WorkbookEvaluator(EvaluationWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this(workbook, null, stabilityClassifier, udfFinder);
    }

    WorkbookEvaluator(EvaluationWorkbook workbook, IEvaluationListener evaluationListener, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
        this._ignoreMissingWorkbooks = false;
        this.dbgEvaluationOutputForNextEval = false;
        this.EVAL_LOG = POILogFactory.getLogger("POI.FormulaEval");
        this.dbgEvaluationOutputIndent = -1;
        this._workbook = workbook;
        this._evaluationListener = evaluationListener;
        this._cache = new EvaluationCache(evaluationListener);
        this._sheetIndexesBySheet = new IdentityHashMap();
        this._sheetIndexesByName = new IdentityHashMap();
        this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
        this._workbookIx = 0;
        this._stabilityClassifier = stabilityClassifier;
        AggregatingUDFFinder defaultToolkit = workbook == null ? null : (AggregatingUDFFinder) workbook.getUDFFinder();
        if (!(defaultToolkit == null || udfFinder == null)) {
            defaultToolkit.add(udfFinder);
        }
        this._udfFinder = defaultToolkit;
    }

    String getSheetName(int sheetIndex) {
        return this._workbook.getSheetName(sheetIndex);
    }

    EvaluationSheet getSheet(int sheetIndex) {
        return this._workbook.getSheet(sheetIndex);
    }

    EvaluationWorkbook getWorkbook() {
        return this._workbook;
    }

    EvaluationName getName(String name, int sheetIndex) {
        return this._workbook.getName(name, sheetIndex);
    }

    private static boolean isDebugLogEnabled() {
        return LOG.check(1);
    }

    private static boolean isInfoLogEnabled() {
        return LOG.check(3);
    }

    private static void logDebug(String s) {
        if (isDebugLogEnabled()) {
            LOG.log(1, s);
        }
    }

    private static void logInfo(String s) {
        if (isInfoLogEnabled()) {
            LOG.log(3, s);
        }
    }

    void attachToEnvironment(CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment, EvaluationCache cache, int workbookIx) {
        this._collaboratingWorkbookEnvironment = collaboratingWorkbooksEnvironment;
        this._cache = cache;
        this._workbookIx = workbookIx;
    }

    CollaboratingWorkbooksEnvironment getEnvironment() {
        return this._collaboratingWorkbookEnvironment;
    }

    void detachFromEnvironment() {
        this._collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
        this._cache = new EvaluationCache(this._evaluationListener);
        this._workbookIx = 0;
    }

    WorkbookEvaluator getOtherWorkbookEvaluator(String workbookName) throws WorkbookNotFoundException {
        return this._collaboratingWorkbookEnvironment.getWorkbookEvaluator(workbookName);
    }

    IEvaluationListener getEvaluationListener() {
        return this._evaluationListener;
    }

    public void clearAllCachedResultValues() {
        this._cache.clear();
        this._sheetIndexesBySheet.clear();
        this._workbook.clearAllCachedResultValues();
    }

    public void notifyUpdateCell(EvaluationCell cell) {
        this._cache.notifyUpdateCell(this._workbookIx, getSheetIndex(cell.getSheet()), cell);
    }

    public void notifyDeleteCell(EvaluationCell cell) {
        this._cache.notifyDeleteCell(this._workbookIx, getSheetIndex(cell.getSheet()), cell);
    }

    private int getSheetIndex(EvaluationSheet sheet) {
        Integer result = (Integer) this._sheetIndexesBySheet.get(sheet);
        if (result == null) {
            int sheetIndex = this._workbook.getSheetIndex(sheet);
            if (sheetIndex < 0) {
                throw new RuntimeException("Specified sheet from a different book");
            }
            result = Integer.valueOf(sheetIndex);
            this._sheetIndexesBySheet.put(sheet, result);
        }
        return result.intValue();
    }

    public ValueEval evaluate(EvaluationCell srcCell) {
        return evaluateAny(srcCell, getSheetIndex(srcCell.getSheet()), srcCell.getRowIndex(), srcCell.getColumnIndex(), new EvaluationTracker(this._cache));
    }

    int getSheetIndex(String sheetName) {
        Integer result = (Integer) this._sheetIndexesByName.get(sheetName);
        if (result == null) {
            int sheetIndex = this._workbook.getSheetIndex(sheetName);
            if (sheetIndex < 0) {
                return -1;
            }
            result = Integer.valueOf(sheetIndex);
            this._sheetIndexesByName.put(sheetName, result);
        }
        return result.intValue();
    }

    int getSheetIndexByExternIndex(int externSheetIndex) {
        return this._workbook.convertFromExternSheetIndex(externSheetIndex);
    }

    private ValueEval evaluateAny(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker) {
        boolean shouldCellDependencyBeRecorded = this._stabilityClassifier == null ? true : !this._stabilityClassifier.isCellFinal(sheetIndex, rowIndex, columnIndex);
        ValueEval result;
        if (srcCell == null || srcCell.getCellTypeEnum() != CellType.FORMULA) {
            result = getValueFromNonFormulaCell(srcCell);
            if (!shouldCellDependencyBeRecorded) {
                return result;
            }
            tracker.acceptPlainValueDependency(this._workbookIx, sheetIndex, rowIndex, columnIndex, result);
            return result;
        }
        CellCacheEntry cce = this._cache.getOrCreateFormulaCellEntry(srcCell);
        if (shouldCellDependencyBeRecorded || cce.isInputSensitive()) {
            tracker.acceptFormulaDependency(cce);
        }
        IEvaluationListener evalListener = this._evaluationListener;
        if (cce.getValue() != null) {
            if (evalListener != null) {
                evalListener.onCacheHit(sheetIndex, rowIndex, columnIndex, cce.getValue());
            }
            return cce.getValue();
        } else if (!tracker.startEvaluate(cce)) {
            return ErrorEval.CIRCULAR_REF_ERROR;
        } else {
            OperationEvaluationContext ec = new OperationEvaluationContext(this, this._workbook, sheetIndex, rowIndex, columnIndex, tracker);
            try {
                Ptg[] ptgs = this._workbook.getFormulaTokens(srcCell);
                if (evalListener == null) {
                    result = evaluateFormula(ec, ptgs);
                } else {
                    evalListener.onStartEvaluate(srcCell, cce);
                    result = evaluateFormula(ec, ptgs);
                    evalListener.onEndEvaluate(cce, result);
                }
                tracker.updateCacheResult(result);
                tracker.endEvaluate(cce);
            } catch (NotImplementedException e) {
                throw addExceptionInfo(e, sheetIndex, rowIndex, columnIndex);
            } catch (RuntimeException re) {
                if ((re.getCause() instanceof WorkbookNotFoundException) && this._ignoreMissingWorkbooks) {
                    logInfo(re.getCause().getMessage() + " - Continuing with cached value!");
                    switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[srcCell.getCachedFormulaResultTypeEnum().ordinal()]) {
                        case 1:
                            result = new NumberEval(srcCell.getNumericCellValue());
                            break;
                        case 2:
                            result = new StringEval(srcCell.getStringCellValue());
                            break;
                        case 3:
                            result = BlankEval.instance;
                            break;
                        case 4:
                            result = BoolEval.valueOf(srcCell.getBooleanCellValue());
                            break;
                        case 5:
                            result = ErrorEval.valueOf(srcCell.getErrorCellValue());
                            break;
                        default:
                            throw new RuntimeException("Unexpected cell type '" + srcCell.getCellTypeEnum() + "' found!");
                    }
                    tracker.endEvaluate(cce);
                } else {
                    throw re;
                }
            } catch (Throwable th) {
                tracker.endEvaluate(cce);
            }
            if (!isDebugLogEnabled()) {
                return result;
            }
            logDebug("Evaluated " + getSheetName(sheetIndex) + "!" + new CellReference(rowIndex, columnIndex).formatAsString() + " to " + result.toString());
            return result;
        }
    }

    private NotImplementedException addExceptionInfo(NotImplementedException inner, int sheetIndex, int rowIndex, int columnIndex) {
        try {
            return new NotImplementedException("Error evaluating cell " + new CellReference(this._workbook.getSheetName(sheetIndex), rowIndex, columnIndex, false, false).formatAsString(), inner);
        } catch (Exception e) {
            LOG.log(7, "Can't add exception info", e);
            return inner;
        }
    }

    static ValueEval getValueFromNonFormulaCell(EvaluationCell cell) {
        if (cell == null) {
            return BlankEval.instance;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()]) {
            case 1:
                return new NumberEval(cell.getNumericCellValue());
            case 2:
                return new StringEval(cell.getStringCellValue());
            case 3:
                return BlankEval.instance;
            case 4:
                return BoolEval.valueOf(cell.getBooleanCellValue());
            case 5:
                return ErrorEval.valueOf(cell.getErrorCellValue());
            default:
                throw new RuntimeException("Unexpected cell type (" + cellType + ")");
        }
    }

    @Internal
    ValueEval evaluateFormula(OperationEvaluationContext ec, Ptg[] ptgs) {
        String dbgIndentStr = "";
        if (this.dbgEvaluationOutputForNextEval) {
            this.dbgEvaluationOutputIndent = 1;
            this.dbgEvaluationOutputForNextEval = false;
        }
        if (this.dbgEvaluationOutputIndent > 0) {
            dbgIndentStr = "                                                                                                    ";
            dbgIndentStr = dbgIndentStr.substring(0, Math.min(dbgIndentStr.length(), this.dbgEvaluationOutputIndent * 2));
            this.EVAL_LOG.log(5, dbgIndentStr + "- evaluateFormula('" + ec.getRefEvaluatorForCurrentSheet().getSheetNameRange() + "'/" + new CellReference(ec.getRowIndex(), ec.getColumnIndex()).formatAsString() + "): " + Arrays.toString(ptgs).replaceAll("\\Qorg.apache.poi.ss.formula.ptg.\\E", ""));
            this.dbgEvaluationOutputIndent++;
        }
        Stack<ValueEval> stack = new Stack();
        int i = 0;
        int iSize = ptgs.length;
        while (i < iSize) {
            Ptg ptg = ptgs[i];
            if (this.dbgEvaluationOutputIndent > 0) {
                this.EVAL_LOG.log(3, dbgIndentStr + "  * ptg " + i + ": " + ptg);
            }
            if (ptg instanceof AttrPtg) {
                AttrPtg attrPtg = (AttrPtg) ptg;
                if (attrPtg.isSum()) {
                    ptg = FuncVarPtg.SUM;
                }
                if (attrPtg.isOptimizedChoose()) {
                    ValueEval arg0 = (ValueEval) stack.pop();
                    int[] jumpTable = attrPtg.getJumpTable();
                    int nChoices = jumpTable.length;
                    int dist;
                    try {
                        int switchIndex = Choose.evaluateFirstArg(arg0, ec.getRowIndex(), ec.getColumnIndex());
                        if (switchIndex < 1 || switchIndex > nChoices) {
                            stack.push(ErrorEval.VALUE_INVALID);
                            dist = attrPtg.getChooseFuncOffset() + 4;
                            i += countTokensToBeSkipped(ptgs, i, dist - ((nChoices * 2) + 2));
                        } else {
                            dist = jumpTable[switchIndex - 1];
                            i += countTokensToBeSkipped(ptgs, i, dist - ((nChoices * 2) + 2));
                        }
                    } catch (EvaluationException e) {
                        stack.push(e.getErrorEval());
                        dist = attrPtg.getChooseFuncOffset() + 4;
                    }
                } else if (attrPtg.isOptimizedIf()) {
                    try {
                        if (!IfFunc.evaluateFirstArg((ValueEval) stack.pop(), ec.getRowIndex(), ec.getColumnIndex())) {
                            i += countTokensToBeSkipped(ptgs, i, attrPtg.getData());
                            Ptg nextPtg = ptgs[i + 1];
                            if ((ptgs[i] instanceof AttrPtg) && (nextPtg instanceof FuncVarPtg) && ((FuncVarPtg) nextPtg).getFunctionIndex() == (short) 1) {
                                i++;
                                stack.push(BoolEval.FALSE);
                            }
                        }
                    } catch (EvaluationException e2) {
                        stack.push(e2.getErrorEval());
                        i += countTokensToBeSkipped(ptgs, i, attrPtg.getData());
                        i += countTokensToBeSkipped(ptgs, i, ptgs[i].getData() + 1);
                    }
                } else if (attrPtg.isSkip()) {
                    i += countTokensToBeSkipped(ptgs, i, attrPtg.getData() + 1);
                    if (stack.peek() == MissingArgEval.instance) {
                        stack.pop();
                        stack.push(BlankEval.instance);
                    }
                }
                i++;
            }
            if (!((ptg instanceof ControlPtg) || (ptg instanceof MemFuncPtg) || (ptg instanceof MemAreaPtg) || (ptg instanceof MemErrPtg))) {
                ValueEval opResult;
                if (ptg instanceof OperationPtg) {
                    OperationPtg optg = (OperationPtg) ptg;
                    if (optg instanceof UnionPtg) {
                        continue;
                    } else {
                        int numops = optg.getNumberOfOperands();
                        ValueEval[] ops = new ValueEval[numops];
                        for (int j = numops - 1; j >= 0; j--) {
                            ops[j] = (ValueEval) stack.pop();
                        }
                        opResult = OperationEvaluatorFactory.evaluate(optg, ops, ec);
                    }
                } else {
                    opResult = getEvalForPtg(ptg, ec);
                }
                if (opResult == null) {
                    throw new RuntimeException("Evaluation result must not be null");
                }
                stack.push(opResult);
                if (this.dbgEvaluationOutputIndent > 0) {
                    this.EVAL_LOG.log(3, dbgIndentStr + "    = " + opResult);
                }
            }
            i++;
        }
        ValueEval value = (ValueEval) stack.pop();
        if (stack.isEmpty()) {
            ValueEval result = dereferenceResult(value, ec.getRowIndex(), ec.getColumnIndex());
            if (this.dbgEvaluationOutputIndent > 0) {
                this.EVAL_LOG.log(3, dbgIndentStr + "finshed eval of " + new CellReference(ec.getRowIndex(), ec.getColumnIndex()).formatAsString() + ": " + result);
                this.dbgEvaluationOutputIndent--;
                if (this.dbgEvaluationOutputIndent == 1) {
                    this.dbgEvaluationOutputIndent = -1;
                }
            }
            return result;
        }
        throw new IllegalStateException("evaluation stack not empty");
    }

    private static int countTokensToBeSkipped(Ptg[] ptgs, int startIndex, int distInBytes) {
        int remBytes = distInBytes;
        int index = startIndex;
        while (remBytes != 0) {
            index++;
            remBytes -= ptgs[index].getSize();
            if (remBytes < 0) {
                throw new RuntimeException("Bad skip distance (wrong token size calculation).");
            } else if (index >= ptgs.length) {
                throw new RuntimeException("Skip distance too far (ran out of formula tokens).");
            }
        }
        return index - startIndex;
    }

    public static ValueEval dereferenceResult(ValueEval evaluationResult, int srcRowNum, int srcColNum) {
        try {
            ValueEval value = OperandResolver.getSingleValue(evaluationResult, srcRowNum, srcColNum);
            if (value == BlankEval.instance) {
                return NumberEval.ZERO;
            }
            return value;
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private ValueEval getEvalForPtg(Ptg ptg, OperationEvaluationContext ec) {
        if (ptg instanceof NamePtg) {
            return getEvalForNameRecord(this._workbook.getName((NamePtg) ptg), ec);
        } else if (ptg instanceof NameXPtg) {
            return processNameEval(ec.getNameXEval((NameXPtg) ptg), ec);
        } else {
            if (ptg instanceof NameXPxg) {
                return processNameEval(ec.getNameXEval((NameXPxg) ptg), ec);
            }
            if (ptg instanceof IntPtg) {
                return new NumberEval((double) ((IntPtg) ptg).getValue());
            }
            if (ptg instanceof NumberPtg) {
                return new NumberEval(((NumberPtg) ptg).getValue());
            }
            if (ptg instanceof StringPtg) {
                return new StringEval(((StringPtg) ptg).getValue());
            }
            if (ptg instanceof BoolPtg) {
                return BoolEval.valueOf(((BoolPtg) ptg).getValue());
            }
            if (ptg instanceof ErrPtg) {
                return ErrorEval.valueOf(((ErrPtg) ptg).getErrorCode());
            }
            if (ptg instanceof MissingArgPtg) {
                return MissingArgEval.instance;
            }
            if ((ptg instanceof AreaErrPtg) || (ptg instanceof RefErrorPtg) || (ptg instanceof DeletedArea3DPtg) || (ptg instanceof DeletedRef3DPtg)) {
                return ErrorEval.REF_INVALID;
            }
            if (ptg instanceof Ref3DPtg) {
                return ec.getRef3DEval((Ref3DPtg) ptg);
            }
            if (ptg instanceof Ref3DPxg) {
                return ec.getRef3DEval((Ref3DPxg) ptg);
            }
            if (ptg instanceof Area3DPtg) {
                return ec.getArea3DEval((Area3DPtg) ptg);
            }
            if (ptg instanceof Area3DPxg) {
                return ec.getArea3DEval((Area3DPxg) ptg);
            }
            if (ptg instanceof RefPtg) {
                RefPtg rptg = (RefPtg) ptg;
                return ec.getRefEval(rptg.getRow(), rptg.getColumn());
            } else if (ptg instanceof AreaPtg) {
                AreaPtg aptg = (AreaPtg) ptg;
                return ec.getAreaEval(aptg.getFirstRow(), aptg.getFirstColumn(), aptg.getLastRow(), aptg.getLastColumn());
            } else if (ptg instanceof UnknownPtg) {
                throw new RuntimeException("UnknownPtg not allowed");
            } else if (ptg instanceof ExpPtg) {
                throw new RuntimeException("ExpPtg currently not supported");
            } else {
                throw new RuntimeException("Unexpected ptg class (" + ptg.getClass().getName() + ")");
            }
        }
    }

    private ValueEval processNameEval(ValueEval eval, OperationEvaluationContext ec) {
        if (eval instanceof ExternalNameEval) {
            return getEvalForNameRecord(((ExternalNameEval) eval).getName(), ec);
        }
        return eval;
    }

    private ValueEval getEvalForNameRecord(EvaluationName nameRecord, OperationEvaluationContext ec) {
        if (nameRecord.isFunctionName()) {
            return new FunctionNameEval(nameRecord.getNameText());
        }
        if (nameRecord.hasFormula()) {
            return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
        }
        throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
    }

    ValueEval evaluateNameFormula(Ptg[] ptgs, OperationEvaluationContext ec) {
        if (ptgs.length == 1) {
            return getEvalForPtg(ptgs[0], ec);
        }
        return evaluateFormula(ec, ptgs);
    }

    ValueEval evaluateReference(EvaluationSheet sheet, int sheetIndex, int rowIndex, int columnIndex, EvaluationTracker tracker) {
        return evaluateAny(sheet.getCell(rowIndex, columnIndex), sheetIndex, rowIndex, columnIndex, tracker);
    }

    public FreeRefFunction findUserDefinedFunction(String functionName) {
        return this._udfFinder.findFunction(functionName);
    }

    public void setIgnoreMissingWorkbooks(boolean ignore) {
        this._ignoreMissingWorkbooks = ignore;
    }

    public boolean isIgnoreMissingWorkbooks() {
        return this._ignoreMissingWorkbooks;
    }

    public static Collection<String> getSupportedFunctionNames() {
        Collection<String> lst = new TreeSet();
        lst.addAll(FunctionEval.getSupportedFunctionNames());
        lst.addAll(AnalysisToolPak.getSupportedFunctionNames());
        return Collections.unmodifiableCollection(lst);
    }

    public static Collection<String> getNotSupportedFunctionNames() {
        Collection<String> lst = new TreeSet();
        lst.addAll(FunctionEval.getNotSupportedFunctionNames());
        lst.addAll(AnalysisToolPak.getNotSupportedFunctionNames());
        return Collections.unmodifiableCollection(lst);
    }

    public static void registerFunction(String name, FreeRefFunction func) {
        AnalysisToolPak.registerFunction(name, func);
    }

    public static void registerFunction(String name, Function func) {
        FunctionEval.registerFunction(name, func);
    }

    public void setDebugEvaluationOutputForNextEval(boolean value) {
        this.dbgEvaluationOutputForNextEval = value;
    }

    public boolean isDebugEvaluationOutputForNextEval() {
        return this.dbgEvaluationOutputForNextEval;
    }
}
