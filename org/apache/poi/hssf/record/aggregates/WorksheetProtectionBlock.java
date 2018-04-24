package org.apache.poi.hssf.record.aggregates;

import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.ObjectProtectRecord;
import org.apache.poi.hssf.record.PasswordRecord;
import org.apache.poi.hssf.record.ProtectRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.ScenarioProtectRecord;
import org.apache.poi.hssf.record.aggregates.RecordAggregate.RecordVisitor;
import org.apache.poi.poifs.crypt.CryptoFunctions;

public final class WorksheetProtectionBlock extends RecordAggregate {
    private ObjectProtectRecord _objectProtectRecord;
    private PasswordRecord _passwordRecord;
    private ProtectRecord _protectRecord;
    private ScenarioProtectRecord _scenarioProtectRecord;

    public static boolean isComponentRecord(int sid) {
        switch (sid) {
            case 18:
            case 19:
            case 99:
            case 221:
                return true;
            default:
                return false;
        }
    }

    private boolean readARecord(RecordStream rs) {
        switch (rs.peekNextSid()) {
            case 18:
                checkNotPresent(this._protectRecord);
                this._protectRecord = (ProtectRecord) rs.getNext();
                break;
            case 19:
                checkNotPresent(this._passwordRecord);
                this._passwordRecord = (PasswordRecord) rs.getNext();
                break;
            case 99:
                checkNotPresent(this._objectProtectRecord);
                this._objectProtectRecord = (ObjectProtectRecord) rs.getNext();
                break;
            case 221:
                checkNotPresent(this._scenarioProtectRecord);
                this._scenarioProtectRecord = (ScenarioProtectRecord) rs.getNext();
                break;
            default:
                return false;
        }
        return true;
    }

    private void checkNotPresent(Record rec) {
        if (rec != null) {
            throw new RecordFormatException("Duplicate PageSettingsBlock record (sid=0x" + Integer.toHexString(rec.getSid()) + ")");
        }
    }

    public void visitContainedRecords(RecordVisitor rv) {
        visitIfPresent(this._protectRecord, rv);
        visitIfPresent(this._objectProtectRecord, rv);
        visitIfPresent(this._scenarioProtectRecord, rv);
        visitIfPresent(this._passwordRecord, rv);
    }

    private static void visitIfPresent(Record r, RecordVisitor rv) {
        if (r != null) {
            rv.visitRecord(r);
        }
    }

    public PasswordRecord getPasswordRecord() {
        return this._passwordRecord;
    }

    public ScenarioProtectRecord getHCenter() {
        return this._scenarioProtectRecord;
    }

    public void addRecords(RecordStream rs) {
        do {
        } while (readARecord(rs));
    }

    private ProtectRecord getProtect() {
        if (this._protectRecord == null) {
            this._protectRecord = new ProtectRecord(false);
        }
        return this._protectRecord;
    }

    private PasswordRecord getPassword() {
        if (this._passwordRecord == null) {
            this._passwordRecord = createPassword();
        }
        return this._passwordRecord;
    }

    public void protectSheet(String password, boolean shouldProtectObjects, boolean shouldProtectScenarios) {
        if (password == null) {
            this._passwordRecord = null;
            this._protectRecord = null;
            this._objectProtectRecord = null;
            this._scenarioProtectRecord = null;
            return;
        }
        ProtectRecord prec = getProtect();
        PasswordRecord pass = getPassword();
        prec.setProtect(true);
        pass.setPassword((short) CryptoFunctions.createXorVerifier1(password));
        if (this._objectProtectRecord == null && shouldProtectObjects) {
            ObjectProtectRecord rec = createObjectProtect();
            rec.setProtect(true);
            this._objectProtectRecord = rec;
        }
        if (this._scenarioProtectRecord == null && shouldProtectScenarios) {
            ScenarioProtectRecord srec = createScenarioProtect();
            srec.setProtect(true);
            this._scenarioProtectRecord = srec;
        }
    }

    public boolean isSheetProtected() {
        return this._protectRecord != null && this._protectRecord.getProtect();
    }

    public boolean isObjectProtected() {
        return this._objectProtectRecord != null && this._objectProtectRecord.getProtect();
    }

    public boolean isScenarioProtected() {
        return this._scenarioProtectRecord != null && this._scenarioProtectRecord.getProtect();
    }

    private static ObjectProtectRecord createObjectProtect() {
        ObjectProtectRecord retval = new ObjectProtectRecord();
        retval.setProtect(false);
        return retval;
    }

    private static ScenarioProtectRecord createScenarioProtect() {
        ScenarioProtectRecord retval = new ScenarioProtectRecord();
        retval.setProtect(false);
        return retval;
    }

    private static PasswordRecord createPassword() {
        return new PasswordRecord(0);
    }

    public int getPasswordHash() {
        if (this._passwordRecord == null) {
            return 0;
        }
        return this._passwordRecord.getPassword();
    }
}
