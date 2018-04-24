package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
final class TopKSelector<T> {
    private final T[] buffer;
    private int bufferSize;
    private final Comparator<? super T> comparator;
    private final int f25k;
    private T threshold;

    public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
        return least(k, Ordering.natural());
    }

    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
        return greatest(k, Ordering.natural());
    }

    public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
        return new TopKSelector(comparator, k);
    }

    public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
        return new TopKSelector(Ordering.from((Comparator) comparator).reverse(), k);
    }

    private TopKSelector(Comparator<? super T> comparator, int k) {
        boolean z;
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator, "comparator");
        this.f25k = k;
        if (k >= 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "k must be nonnegative, was %s", k);
        this.buffer = new Object[(k * 2)];
        this.bufferSize = 0;
        this.threshold = null;
    }

    public void offer(@Nullable T elem) {
        if (this.f25k != 0) {
            if (this.bufferSize == 0) {
                this.buffer[0] = elem;
                this.threshold = elem;
                this.bufferSize = 1;
            } else if (this.bufferSize < this.f25k) {
                r0 = this.buffer;
                r1 = this.bufferSize;
                this.bufferSize = r1 + 1;
                r0[r1] = elem;
                if (this.comparator.compare(elem, this.threshold) > 0) {
                    this.threshold = elem;
                }
            } else if (this.comparator.compare(elem, this.threshold) < 0) {
                r0 = this.buffer;
                r1 = this.bufferSize;
                this.bufferSize = r1 + 1;
                r0[r1] = elem;
                if (this.bufferSize == this.f25k * 2) {
                    trim();
                }
            }
        }
    }

    private void trim() {
        int left = 0;
        int right = (this.f25k * 2) - 1;
        int minThresholdPosition = 0;
        int iterations = 0;
        int maxIterations = IntMath.log2(right - 0, RoundingMode.CEILING) * 3;
        while (left < right) {
            int pivotNewIndex = partition(left, right, ((left + right) + 1) >>> 1);
            if (pivotNewIndex <= this.f25k) {
                if (pivotNewIndex >= this.f25k) {
                    break;
                }
                left = Math.max(pivotNewIndex, left + 1);
                minThresholdPosition = pivotNewIndex;
            } else {
                right = pivotNewIndex - 1;
            }
            iterations++;
            if (iterations >= maxIterations) {
                Arrays.sort(this.buffer, left, right, this.comparator);
                break;
            }
        }
        this.bufferSize = this.f25k;
        this.threshold = this.buffer[minThresholdPosition];
        for (int i = minThresholdPosition + 1; i < this.f25k; i++) {
            if (this.comparator.compare(this.buffer[i], this.threshold) > 0) {
                this.threshold = this.buffer[i];
            }
        }
    }

    private int partition(int left, int right, int pivotIndex) {
        T pivotValue = this.buffer[pivotIndex];
        this.buffer[pivotIndex] = this.buffer[right];
        int pivotNewIndex = left;
        for (int i = left; i < right; i++) {
            if (this.comparator.compare(this.buffer[i], pivotValue) < 0) {
                swap(pivotNewIndex, i);
                pivotNewIndex++;
            }
        }
        this.buffer[right] = this.buffer[pivotNewIndex];
        this.buffer[pivotNewIndex] = pivotValue;
        return pivotNewIndex;
    }

    private void swap(int i, int j) {
        T tmp = this.buffer[i];
        this.buffer[i] = this.buffer[j];
        this.buffer[j] = tmp;
    }

    public void offerAll(Iterable<? extends T> elements) {
        offerAll(elements.iterator());
    }

    public void offerAll(Iterator<? extends T> elements) {
        while (elements.hasNext()) {
            offer(elements.next());
        }
    }

    public List<T> topK() {
        Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
        if (this.bufferSize > this.f25k) {
            Arrays.fill(this.buffer, this.f25k, this.buffer.length, null);
            this.bufferSize = this.f25k;
            this.threshold = this.buffer[this.f25k - 1];
        }
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
    }
}
