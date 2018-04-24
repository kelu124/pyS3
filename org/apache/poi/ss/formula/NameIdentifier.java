package org.apache.poi.ss.formula;

public class NameIdentifier {
    private final boolean _isQuoted;
    private final String _name;

    public NameIdentifier(String name, boolean isQuoted) {
        this._name = name;
        this._isQuoted = isQuoted;
    }

    public String getName() {
        return this._name;
    }

    public boolean isQuoted() {
        return this._isQuoted;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        if (this._isQuoted) {
            sb.append("'").append(this._name).append("'");
        } else {
            sb.append(this._name);
        }
        sb.append("]");
        return sb.toString();
    }
}
