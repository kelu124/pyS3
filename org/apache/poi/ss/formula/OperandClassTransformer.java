package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.ControlPtg;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.IntersectionPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RangePtg;
import org.apache.poi.ss.formula.ptg.UnionPtg;
import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.util.Removal;

final class OperandClassTransformer {
    private final FormulaType _formulaType;

    @Removal(version = "3.17")
    public OperandClassTransformer(int formulaType) {
        this(FormulaType.forInt(formulaType));
    }

    public OperandClassTransformer(FormulaType formulaType) {
        this._formulaType = formulaType;
    }

    public void transformFormula(ParseNode rootNode) {
        byte rootNodeOperandClass;
        switch (1.$SwitchMap$org$apache$poi$ss$formula$FormulaType[this._formulaType.ordinal()]) {
            case 1:
                rootNodeOperandClass = (byte) 32;
                break;
            case 2:
                rootNodeOperandClass = (byte) 64;
                break;
            case 3:
            case 4:
                rootNodeOperandClass = (byte) 0;
                break;
            default:
                throw new RuntimeException("Incomplete code - formula type (" + this._formulaType + ") not supported yet");
        }
        transformNode(rootNode, rootNodeOperandClass, false);
    }

    private void transformNode(ParseNode node, byte desiredOperandClass, boolean callerForceArrayFlag) {
        Ptg token = node.getToken();
        ParseNode[] children = node.getChildren();
        if (isSimpleValueFunction(token)) {
            boolean localForceArray = desiredOperandClass == (byte) 64;
            for (ParseNode transformNode : children) {
                transformNode(transformNode, desiredOperandClass, localForceArray);
            }
            setSimpleValueFuncClass((AbstractFunctionPtg) token, desiredOperandClass, callerForceArrayFlag);
            return;
        }
        if (isSingleArgSum(token)) {
            token = FuncVarPtg.SUM;
        }
        if ((token instanceof ValueOperatorPtg) || (token instanceof ControlPtg) || (token instanceof MemFuncPtg) || (token instanceof MemAreaPtg) || (token instanceof UnionPtg) || (token instanceof IntersectionPtg)) {
            byte localDesiredOperandClass;
            if (desiredOperandClass == (byte) 0) {
                localDesiredOperandClass = (byte) 32;
            } else {
                localDesiredOperandClass = desiredOperandClass;
            }
            for (ParseNode transformNode2 : children) {
                transformNode(transformNode2, localDesiredOperandClass, callerForceArrayFlag);
            }
        } else if (token instanceof AbstractFunctionPtg) {
            transformFunctionNode((AbstractFunctionPtg) token, children, desiredOperandClass, callerForceArrayFlag);
        } else if (children.length > 0) {
            if (token != RangePtg.instance) {
                throw new IllegalStateException("Node should not have any children");
            }
        } else if (!token.isBaseToken()) {
            token.setClass(transformClass(token.getPtgClass(), desiredOperandClass, callerForceArrayFlag));
        }
    }

    private static boolean isSingleArgSum(Ptg token) {
        if (token instanceof AttrPtg) {
            return ((AttrPtg) token).isSum();
        }
        return false;
    }

    private static boolean isSimpleValueFunction(Ptg token) {
        if (!(token instanceof AbstractFunctionPtg)) {
            return false;
        }
        AbstractFunctionPtg aptg = (AbstractFunctionPtg) token;
        if (aptg.getDefaultOperandClass() != (byte) 32) {
            return false;
        }
        for (int i = aptg.getNumberOfOperands() - 1; i >= 0; i--) {
            if (aptg.getParameterClass(i) != (byte) 32) {
                return false;
            }
        }
        return true;
    }

    private byte transformClass(byte currentOperandClass, byte desiredOperandClass, boolean callerForceArrayFlag) {
        switch (desiredOperandClass) {
            case (byte) 0:
                return callerForceArrayFlag ? (byte) 0 : currentOperandClass;
            case (byte) 32:
                if (!callerForceArrayFlag) {
                    return (byte) 32;
                }
                break;
            case (byte) 64:
                break;
            default:
                throw new IllegalStateException("Unexpected operand class (" + desiredOperandClass + ")");
        }
        return (byte) 64;
    }

    private void transformFunctionNode(AbstractFunctionPtg afp, ParseNode[] children, byte desiredOperandClass, boolean callerForceArrayFlag) {
        boolean localForceArrayFlag = false;
        byte defaultReturnOperandClass = afp.getDefaultOperandClass();
        if (callerForceArrayFlag) {
            switch (defaultReturnOperandClass) {
                case (byte) 0:
                    if (desiredOperandClass == (byte) 0) {
                        afp.setClass((byte) 0);
                    } else {
                        afp.setClass((byte) 64);
                    }
                    localForceArrayFlag = false;
                    break;
                case (byte) 32:
                    afp.setClass((byte) 64);
                    localForceArrayFlag = true;
                    break;
                case (byte) 64:
                    afp.setClass((byte) 64);
                    localForceArrayFlag = false;
                    break;
                default:
                    throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
            }
        } else if (defaultReturnOperandClass == desiredOperandClass) {
            localForceArrayFlag = false;
            afp.setClass(defaultReturnOperandClass);
        } else {
            switch (desiredOperandClass) {
                case (byte) 0:
                    switch (defaultReturnOperandClass) {
                        case (byte) 32:
                            afp.setClass((byte) 32);
                            break;
                        case (byte) 64:
                            afp.setClass((byte) 64);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
                    }
                    localForceArrayFlag = false;
                    break;
                case (byte) 32:
                    afp.setClass((byte) 32);
                    localForceArrayFlag = false;
                    break;
                case (byte) 64:
                    switch (defaultReturnOperandClass) {
                        case (byte) 0:
                            afp.setClass((byte) 0);
                            break;
                        case (byte) 32:
                            afp.setClass((byte) 64);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected operand class (" + defaultReturnOperandClass + ")");
                    }
                    if (defaultReturnOperandClass == (byte) 32) {
                        localForceArrayFlag = true;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected operand class (" + desiredOperandClass + ")");
            }
        }
        for (int i = 0; i < children.length; i++) {
            transformNode(children[i], afp.getParameterClass(i), localForceArrayFlag);
        }
    }

    private void setSimpleValueFuncClass(AbstractFunctionPtg afp, byte desiredOperandClass, boolean callerForceArrayFlag) {
        if (callerForceArrayFlag || desiredOperandClass == (byte) 64) {
            afp.setClass((byte) 64);
        } else {
            afp.setClass((byte) 32);
        }
    }
}
