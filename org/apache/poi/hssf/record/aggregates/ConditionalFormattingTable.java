package org.apache.poi.hssf.record.aggregates;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.CFHeader12Record;
import org.apache.poi.hssf.record.CFHeaderRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.ss.formula.FormulaShifter;

public final class ConditionalFormattingTable extends RecordAggregate {
    private final List<CFRecordsAggregate> _cfHeaders;

    public ConditionalFormattingTable() {
        this._cfHeaders = new ArrayList();
    }

    public ConditionalFormattingTable(RecordStream rs) {
        List<CFRecordsAggregate> temp = new ArrayList();
        while (true) {
            if (rs.peekNextClass() == CFHeaderRecord.class || rs.peekNextClass() == CFHeader12Record.class) {
                temp.add(CFRecordsAggregate.createCFAggregate(rs));
            } else {
                this._cfHeaders = temp;
                return;
            }
        }
    }

    public void visitContainedRecords(RecordVisitor rv) {
        for (CFRecordsAggregate subAgg : this._cfHeaders) {
            subAgg.visitContainedRecords(rv);
        }
    }

    public int add(CFRecordsAggregate cfAggregate) {
        cfAggregate.getHeader().setID(this._cfHeaders.size());
        this._cfHeaders.add(cfAggregate);
        return this._cfHeaders.size() - 1;
    }

    public int size() {
        return this._cfHeaders.size();
    }

    public CFRecordsAggregate get(int index) {
        checkIndex(index);
        return (CFRecordsAggregate) this._cfHeaders.get(index);
    }

    public void remove(int index) {
        checkIndex(index);
        this._cfHeaders.remove(index);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this._cfHeaders.size()) {
            throw new IllegalArgumentException("Specified CF index " + index + " is outside the allowable range (0.." + (this._cfHeaders.size() - 1) + ")");
        }
    }

    public void updateFormulasAfterCellShift(FormulaShifter shifter, int externSheetIndex) {
        int i = 0;
        while (i < this._cfHeaders.size()) {
            if (!((CFRecordsAggregate) this._cfHeaders.get(i)).updateFormulasAfterCellShift(shifter, externSheetIndex)) {
                this._cfHeaders.remove(i);
                i--;
            }
            i++;
        }
    }
}
