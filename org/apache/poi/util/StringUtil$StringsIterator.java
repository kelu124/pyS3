package org.apache.poi.util;

import java.util.Iterator;

public class StringUtil$StringsIterator implements Iterator<String> {
    private int position = 0;
    private String[] strings = new String[0];

    public StringUtil$StringsIterator(String[] strings) {
        if (strings != null) {
            this.strings = (String[]) strings.clone();
        }
    }

    public boolean hasNext() {
        return this.position < this.strings.length;
    }

    public String next() {
        int ourPos = this.position;
        this.position = ourPos + 1;
        if (ourPos < this.strings.length) {
            return this.strings[ourPos];
        }
        throw new ArrayIndexOutOfBoundsException(ourPos);
    }

    public void remove() {
    }
}
