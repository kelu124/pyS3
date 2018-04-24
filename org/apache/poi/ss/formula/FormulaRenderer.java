package org.apache.poi.ss.formula;

import java.util.Stack;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemErrPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
import org.apache.poi.ss.formula.ptg.Ptg;

public class FormulaRenderer {
    public static String toFormulaString(FormulaRenderingWorkbook book, Ptg[] ptgs) {
        if (ptgs == null || ptgs.length == 0) {
            throw new IllegalArgumentException("ptgs must not be null");
        }
        Stack<String> stack = new Stack();
        for (Ptg ptg : ptgs) {
            if (!((ptg instanceof MemAreaPtg) || (ptg instanceof MemFuncPtg) || (ptg instanceof MemErrPtg))) {
                if (ptg instanceof ParenthesisPtg) {
                    stack.push("(" + ((String) stack.pop()) + ")");
                } else if (ptg instanceof AttrPtg) {
                    AttrPtg attrPtg = (AttrPtg) ptg;
                    if (!(attrPtg.isOptimizedIf() || attrPtg.isOptimizedChoose() || attrPtg.isSkip() || attrPtg.isSpace() || attrPtg.isSemiVolatile())) {
                        if (attrPtg.isSum()) {
                            stack.push(attrPtg.toFormulaString(getOperands(stack, attrPtg.getNumberOfOperands())));
                        } else {
                            throw new RuntimeException("Unexpected tAttr: " + attrPtg.toString());
                        }
                    }
                } else if (ptg instanceof WorkbookDependentFormula) {
                    stack.push(((WorkbookDependentFormula) ptg).toFormulaString(book));
                } else if (ptg instanceof OperationPtg) {
                    OperationPtg o = (OperationPtg) ptg;
                    stack.push(o.toFormulaString(getOperands(stack, o.getNumberOfOperands())));
                } else {
                    stack.push(ptg.toFormulaString());
                }
            }
        }
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack underflow");
        }
        String result = (String) stack.pop();
        if (stack.isEmpty()) {
            return result;
        }
        throw new IllegalStateException("too much stuff left on the stack");
    }

    private static String[] getOperands(Stack<String> stack, int nOperands) {
        String[] operands = new String[nOperands];
        for (int j = nOperands - 1; j >= 0; j--) {
            if (stack.isEmpty()) {
                throw new IllegalStateException("Too few arguments supplied to operation. Expected (" + nOperands + ") operands but got (" + ((nOperands - j) - 1) + ")");
            }
            operands[j] = (String) stack.pop();
        }
        return operands;
    }
}
