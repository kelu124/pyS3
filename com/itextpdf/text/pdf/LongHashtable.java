package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LongHashtable implements Cloneable {
    private transient int count;
    private float loadFactor;
    private transient Entry[] table;
    private int threshold;

    static class Entry {
        int hash;
        long key;
        Entry next;
        long value;

        protected Entry(int hash, long key, long value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public long getKey() {
            return this.key;
        }

        public long getValue() {
            return this.value;
        }

        protected Object clone() {
            return new Entry(this.hash, this.key, this.value, this.next != null ? (Entry) this.next.clone() : null);
        }
    }

    static class LongHashtableIterator implements Iterator<Entry> {
        Entry entry;
        int index;
        Entry[] table;

        LongHashtableIterator(Entry[] table) {
            this.table = table;
            this.index = table.length;
        }

        public boolean hasNext() {
            if (this.entry != null) {
                return true;
            }
            Entry entry;
            do {
                int i = this.index;
                this.index = i - 1;
                if (i <= 0) {
                    return false;
                }
                entry = this.table[this.index];
                this.entry = entry;
            } while (entry == null);
            return true;
        }

        public Entry next() {
            if (this.entry == null) {
                Entry entry;
                do {
                    int i = this.index;
                    this.index = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    entry = this.table[this.index];
                    this.entry = entry;
                } while (entry == null);
            }
            if (this.entry != null) {
                Entry e = this.entry;
                this.entry = e.next;
                return e;
            }
            throw new NoSuchElementException(MessageLocalization.getComposedMessage("inthashtableiterator", new Object[0]));
        }

        public void remove() {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("remove.not.supported", new Object[0]));
        }
    }

    public LongHashtable() {
        this(150, 0.75f);
    }

    public LongHashtable(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public LongHashtable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.capacity.1", initialCapacity));
        } else if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.load.1", String.valueOf(loadFactor)));
        } else {
            if (initialCapacity == 0) {
                initialCapacity = 1;
            }
            this.loadFactor = loadFactor;
            this.table = new Entry[initialCapacity];
            this.threshold = (int) (((float) initialCapacity) * loadFactor);
        }
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(long value) {
        Entry[] tab = this.table;
        int i = tab.length;
        while (true) {
            int i2 = i - 1;
            if (i <= 0) {
                return false;
            }
            for (Entry e = tab[i2]; e != null; e = e.next) {
                if (e.value == value) {
                    return true;
                }
            }
            i = i2;
        }
    }

    public boolean containsValue(long value) {
        return contains(value);
    }

    public boolean containsKey(long key) {
        Entry[] tab = this.table;
        int hash = (int) ((key >>> 32) ^ key);
        Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length];
        while (e != null) {
            if (e.hash == hash && e.key == key) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    public long get(long key) {
        Entry[] tab = this.table;
        int hash = (int) ((key >>> 32) ^ key);
        Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length];
        while (e != null) {
            if (e.hash == hash && e.key == key) {
                return e.value;
            }
            e = e.next;
        }
        return 0;
    }

    protected void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldMap = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newMap = new Entry[newCapacity];
        this.threshold = (int) (((float) newCapacity) * this.loadFactor);
        this.table = newMap;
        int i = oldCapacity;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                Entry old = oldMap[i2];
                while (old != null) {
                    Entry e = old;
                    old = old.next;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
                i = i2;
            } else {
                return;
            }
        }
    }

    public long put(long key, long value) {
        Entry[] tab = this.table;
        int hash = (int) ((key >>> 32) ^ key);
        int index = (hash & Integer.MAX_VALUE) % tab.length;
        Entry e = tab[index];
        while (e != null) {
            if (e.hash == hash && e.key == key) {
                long old = e.value;
                e.value = value;
                return old;
            }
            e = e.next;
        }
        if (this.count >= this.threshold) {
            rehash();
            tab = this.table;
            index = (hash & Integer.MAX_VALUE) % tab.length;
        }
        tab[index] = new Entry(hash, key, value, tab[index]);
        this.count++;
        return 0;
    }

    public long remove(long key) {
        Entry[] tab = this.table;
        int hash = (int) ((key >>> 32) ^ key);
        int index = (Integer.MAX_VALUE & hash) % tab.length;
        Entry e = tab[index];
        Entry prev = null;
        while (e != null) {
            if (e.hash == hash && e.key == key) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                long oldValue = e.value;
                e.value = 0;
                return oldValue;
            }
            prev = e;
            e = e.next;
        }
        return 0;
    }

    public void clear() {
        Entry[] tab = this.table;
        int index = tab.length;
        while (true) {
            index--;
            if (index >= 0) {
                tab[index] = null;
            } else {
                this.count = 0;
                return;
            }
        }
    }

    public Iterator<Entry> getEntryIterator() {
        return new LongHashtableIterator(this.table);
    }

    public long[] toOrderedKeys() {
        long[] res = getKeys();
        Arrays.sort(res);
        return res;
    }

    public long[] getKeys() {
        long[] res = new long[this.count];
        int index = this.table.length;
        Entry entry = null;
        int ptr = 0;
        while (true) {
            if (entry == null) {
                int index2 = index;
                while (true) {
                    index = index2 - 1;
                    if (index2 <= 0) {
                        break;
                    }
                    entry = this.table[index];
                    if (entry != null) {
                        break;
                    }
                    index2 = index;
                }
            }
            if (entry == null) {
                return res;
            }
            Entry e = entry;
            entry = e.next;
            int ptr2 = ptr + 1;
            res[ptr] = e.key;
            ptr = ptr2;
        }
    }

    public long getOneKey() {
        if (this.count == 0) {
            return 0;
        }
        Entry entry = null;
        int index = this.table.length;
        while (true) {
            int index2 = index - 1;
            if (index <= 0) {
                break;
            }
            entry = this.table[index2];
            if (entry != null) {
                break;
            }
            index = index2;
        }
        if (entry != null) {
            return entry.key;
        }
        return 0;
    }

    public Object clone() {
        try {
            LongHashtable t = (LongHashtable) super.clone();
            t.table = new Entry[this.table.length];
            int i = this.table.length;
            while (true) {
                int i2 = i - 1;
                if (i <= 0) {
                    return t;
                }
                t.table[i2] = this.table[i2] != null ? (Entry) this.table[i2].clone() : null;
                i = i2;
            }
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
