package org.apache.poi.ss.formula;

import org.apache.poi.ss.formula.ptg.ArrayPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.Ptg;

final class ParseNode {
    public static final ParseNode[] EMPTY_ARRAY = new ParseNode[0];
    private final ParseNode[] _children;
    private boolean _isIf;
    private final Ptg _token;
    private final int _tokenCount;

    private static final class TokenCollector {
        private int _offset = 0;
        private final Ptg[] _ptgs;

        public TokenCollector(int tokenCount) {
            this._ptgs = new Ptg[tokenCount];
        }

        public int sumTokenSizes(int fromIx, int toIx) {
            int result = 0;
            for (int i = fromIx; i < toIx; i++) {
                result += this._ptgs[i].getSize();
            }
            return result;
        }

        public int createPlaceholder() {
            int i = this._offset;
            this._offset = i + 1;
            return i;
        }

        public void add(Ptg token) {
            if (token == null) {
                throw new IllegalArgumentException("token must not be null");
            }
            this._ptgs[this._offset] = token;
            this._offset++;
        }

        public void setPlaceholder(int index, Ptg token) {
            if (this._ptgs[index] != null) {
                throw new IllegalStateException("Invalid placeholder index (" + index + ")");
            }
            this._ptgs[index] = token;
        }

        public Ptg[] getResult() {
            return this._ptgs;
        }
    }

    public ParseNode(Ptg token, ParseNode[] children) {
        if (token == null) {
            throw new IllegalArgumentException("token must not be null");
        }
        this._token = token;
        this._children = (ParseNode[]) children.clone();
        this._isIf = isIf(token);
        int tokenCount = 1;
        for (ParseNode tokenCount2 : children) {
            tokenCount += tokenCount2.getTokenCount();
        }
        if (this._isIf) {
            tokenCount += children.length;
        }
        this._tokenCount = tokenCount;
    }

    public ParseNode(Ptg token) {
        this(token, EMPTY_ARRAY);
    }

    public ParseNode(Ptg token, ParseNode child0) {
        this(token, new ParseNode[]{child0});
    }

    public ParseNode(Ptg token, ParseNode child0, ParseNode child1) {
        this(token, new ParseNode[]{child0, child1});
    }

    private int getTokenCount() {
        return this._tokenCount;
    }

    public int getEncodedSize() {
        int result = this._token instanceof ArrayPtg ? 8 : this._token.getSize();
        for (ParseNode encodedSize : this._children) {
            result += encodedSize.getEncodedSize();
        }
        return result;
    }

    public static Ptg[] toTokenArray(ParseNode rootNode) {
        TokenCollector temp = new TokenCollector(rootNode.getTokenCount());
        rootNode.collectPtgs(temp);
        return temp.getResult();
    }

    private void collectPtgs(TokenCollector temp) {
        if (isIf(this._token)) {
            collectIfPtgs(temp);
            return;
        }
        boolean isPreFixOperator = (this._token instanceof MemFuncPtg) || (this._token instanceof MemAreaPtg);
        if (isPreFixOperator) {
            temp.add(this._token);
        }
        for (ParseNode collectPtgs : getChildren()) {
            collectPtgs.collectPtgs(temp);
        }
        if (!isPreFixOperator) {
            temp.add(this._token);
        }
    }

    private void collectIfPtgs(TokenCollector temp) {
        getChildren()[0].collectPtgs(temp);
        int ifAttrIndex = temp.createPlaceholder();
        getChildren()[1].collectPtgs(temp);
        int skipAfterTrueParamIndex = temp.createPlaceholder();
        AttrPtg attrIf = AttrPtg.createIf(temp.sumTokenSizes(ifAttrIndex + 1, skipAfterTrueParamIndex) + 4);
        AttrPtg attrSkipAfterTrue;
        if (getChildren().length > 2) {
            getChildren()[2].collectPtgs(temp);
            int skipAfterFalseParamIndex = temp.createPlaceholder();
            attrSkipAfterTrue = AttrPtg.createSkip(((temp.sumTokenSizes(skipAfterTrueParamIndex + 1, skipAfterFalseParamIndex) + 4) + 4) - 1);
            AttrPtg attrSkipAfterFalse = AttrPtg.createSkip(3);
            temp.setPlaceholder(ifAttrIndex, attrIf);
            temp.setPlaceholder(skipAfterTrueParamIndex, attrSkipAfterTrue);
            temp.setPlaceholder(skipAfterFalseParamIndex, attrSkipAfterFalse);
        } else {
            attrSkipAfterTrue = AttrPtg.createSkip(3);
            temp.setPlaceholder(ifAttrIndex, attrIf);
            temp.setPlaceholder(skipAfterTrueParamIndex, attrSkipAfterTrue);
        }
        temp.add(this._token);
    }

    private static boolean isIf(Ptg token) {
        if ((token instanceof FuncVarPtg) && "IF".equals(((FuncVarPtg) token).getName())) {
            return true;
        }
        return false;
    }

    public Ptg getToken() {
        return this._token;
    }

    public ParseNode[] getChildren() {
        return this._children;
    }
}
