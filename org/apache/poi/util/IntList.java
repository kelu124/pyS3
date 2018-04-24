package org.apache.poi.util;

public class IntList {
    private static final int _default_size = 128;
    private int[] _array;
    private int _limit;
    private int fillval;

    public IntList() {
        this(128);
    }

    public IntList(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public IntList(IntList list) {
        this(list._array.length);
        System.arraycopy(list._array, 0, this._array, 0, this._array.length);
        this._limit = list._limit;
    }

    public IntList(int initialCapacity, int fillvalue) {
        this.fillval = 0;
        this._array = new int[initialCapacity];
        if (this.fillval != 0) {
            this.fillval = fillvalue;
            fillArray(this.fillval, this._array, 0);
        }
        this._limit = 0;
    }

    private void fillArray(int val, int[] array, int index) {
        for (int k = index; k < array.length; k++) {
            array[k] = val;
        }
    }

    public void add(int index, int value) {
        if (index > this._limit) {
            throw new IndexOutOfBoundsException();
        } else if (index == this._limit) {
            add(value);
        } else {
            if (this._limit == this._array.length) {
                growArray(this._limit * 2);
            }
            System.arraycopy(this._array, index, this._array, index + 1, this._limit - index);
            this._array[index] = value;
            this._limit++;
        }
    }

    public boolean add(int value) {
        if (this._limit == this._array.length) {
            growArray(this._limit * 2);
        }
        int[] iArr = this._array;
        int i = this._limit;
        this._limit = i + 1;
        iArr[i] = value;
        return true;
    }

    public boolean addAll(IntList c) {
        if (c._limit != 0) {
            if (this._limit + c._limit > this._array.length) {
                growArray(this._limit + c._limit);
            }
            System.arraycopy(c._array, 0, this._array, this._limit, c._limit);
            this._limit += c._limit;
        }
        return true;
    }

    public boolean addAll(int index, IntList c) {
        if (index > this._limit) {
            throw new IndexOutOfBoundsException();
        }
        if (c._limit != 0) {
            if (this._limit + c._limit > this._array.length) {
                growArray(this._limit + c._limit);
            }
            System.arraycopy(this._array, index, this._array, c._limit + index, this._limit - index);
            System.arraycopy(c._array, 0, this._array, index, c._limit);
            this._limit += c._limit;
        }
        return true;
    }

    public void clear() {
        this._limit = 0;
    }

    public boolean contains(int o) {
        boolean rval = false;
        int j = 0;
        while (!rval && j < this._limit) {
            if (this._array[j] == o) {
                rval = true;
            }
            j++;
        }
        return rval;
    }

    public boolean containsAll(IntList c) {
        boolean rval = true;
        if (this != c) {
            int j = 0;
            while (rval && j < c._limit) {
                if (!contains(c._array[j])) {
                    rval = false;
                }
                j++;
            }
        }
        return rval;
    }

    public boolean equals(Object o) {
        boolean rval;
        if (this == o) {
            rval = true;
        } else {
            rval = false;
        }
        if (!(rval || o == null || o.getClass() != getClass())) {
            IntList other = (IntList) o;
            if (other._limit == this._limit) {
                rval = true;
                int j = 0;
                while (rval && j < this._limit) {
                    if (this._array[j] == other._array[j]) {
                        rval = true;
                    } else {
                        rval = false;
                    }
                    j++;
                }
            }
        }
        return rval;
    }

    public int get(int index) {
        if (index < this._limit) {
            return this._array[index];
        }
        throw new IndexOutOfBoundsException(index + " not accessible in a list of length " + this._limit);
    }

    public int hashCode() {
        int hash = 0;
        for (int j = 0; j < this._limit; j++) {
            hash = (hash * 31) + this._array[j];
        }
        return hash;
    }

    public int indexOf(int o) {
        int rval = 0;
        while (rval < this._limit && o != this._array[rval]) {
            rval++;
        }
        if (rval == this._limit) {
            return -1;
        }
        return rval;
    }

    public boolean isEmpty() {
        return this._limit == 0;
    }

    public int lastIndexOf(int o) {
        int rval = this._limit - 1;
        while (rval >= 0 && o != this._array[rval]) {
            rval--;
        }
        return rval;
    }

    public int remove(int index) {
        if (index >= this._limit) {
            throw new IndexOutOfBoundsException();
        }
        int rval = this._array[index];
        System.arraycopy(this._array, index + 1, this._array, index, this._limit - index);
        this._limit--;
        return rval;
    }

    public boolean removeValue(int o) {
        boolean rval = false;
        int j = 0;
        while (!rval && j < this._limit) {
            if (o == this._array[j]) {
                if (j + 1 < this._limit) {
                    System.arraycopy(this._array, j + 1, this._array, j, this._limit - j);
                }
                this._limit--;
                rval = true;
            }
            j++;
        }
        return rval;
    }

    public boolean removeAll(IntList c) {
        boolean rval = false;
        for (int j = 0; j < c._limit; j++) {
            if (removeValue(c._array[j])) {
                rval = true;
            }
        }
        return rval;
    }

    public boolean retainAll(IntList c) {
        boolean rval = false;
        int j = 0;
        while (j < this._limit) {
            if (c.contains(this._array[j])) {
                j++;
            } else {
                remove(j);
                rval = true;
            }
        }
        return rval;
    }

    public int set(int index, int element) {
        if (index >= this._limit) {
            throw new IndexOutOfBoundsException();
        }
        int rval = this._array[index];
        this._array[index] = element;
        return rval;
    }

    public int size() {
        return this._limit;
    }

    public int[] toArray() {
        int[] rval = new int[this._limit];
        System.arraycopy(this._array, 0, rval, 0, this._limit);
        return rval;
    }

    public int[] toArray(int[] a) {
        if (a.length != this._limit) {
            return toArray();
        }
        System.arraycopy(this._array, 0, a, 0, this._limit);
        return a;
    }

    private void growArray(int new_size) {
        int size;
        if (new_size == this._array.length) {
            size = new_size + 1;
        } else {
            size = new_size;
        }
        int[] new_array = new int[size];
        if (this.fillval != 0) {
            fillArray(this.fillval, new_array, this._array.length);
        }
        System.arraycopy(this._array, 0, new_array, 0, this._limit);
        this._array = new_array;
    }
}
