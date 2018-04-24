package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.itextpdf.text.pdf.BaseField;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private static final Object PRED = new Object();
    private final Map<N, Object> adjacentNodeValues;
    private int predecessorCount;
    private int successorCount;

    class C07041 extends AbstractSet<N> {
        C07041() {
        }

        public UnmodifiableIterator<N> iterator() {
            final Iterator<Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
            return new AbstractIterator<N>() {
                protected N computeNext() {
                    while (entries.hasNext()) {
                        Entry<N, Object> entry = (Entry) entries.next();
                        if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
                            return entry.getKey();
                        }
                    }
                    return endOfData();
                }
            };
        }

        public int size() {
            return DirectedGraphConnections.this.predecessorCount;
        }

        public boolean contains(@Nullable Object obj) {
            return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
        }
    }

    class C07062 extends AbstractSet<N> {
        C07062() {
        }

        public UnmodifiableIterator<N> iterator() {
            final Iterator<Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
            return new AbstractIterator<N>() {
                protected N computeNext() {
                    while (entries.hasNext()) {
                        Entry<N, Object> entry = (Entry) entries.next();
                        if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
                            return entry.getKey();
                        }
                    }
                    return endOfData();
                }
            };
        }

        public int size() {
            return DirectedGraphConnections.this.successorCount;
        }

        public boolean contains(@Nullable Object obj) {
            return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
        }
    }

    private static final class PredAndSucc {
        private final Object successorValue;

        PredAndSucc(Object successorValue) {
            this.successorValue = successorValue;
        }
    }

    private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, int predecessorCount, int successorCount) {
        this.adjacentNodeValues = (Map) Preconditions.checkNotNull(adjacentNodeValues);
        this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
        this.successorCount = Graphs.checkNonNegative(successorCount);
        boolean z = predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size();
        Preconditions.checkState(z);
    }

    static <N, V> DirectedGraphConnections<N, V> of() {
        return new DirectedGraphConnections(new HashMap(4, BaseField.BORDER_WIDTH_THIN), 0, 0);
    }

    static <N, V> DirectedGraphConnections<N, V> ofImmutable(Set<N> predecessors, Map<N, V> successorValues) {
        Map adjacentNodeValues = new HashMap();
        adjacentNodeValues.putAll(successorValues);
        for (N predecessor : predecessors) {
            Object value = adjacentNodeValues.put(predecessor, PRED);
            if (value != null) {
                adjacentNodeValues.put(predecessor, new PredAndSucc(value));
            }
        }
        return new DirectedGraphConnections(ImmutableMap.copyOf(adjacentNodeValues), predecessors.size(), successorValues.size());
    }

    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
    }

    public Set<N> predecessors() {
        return new C07041();
    }

    public Set<N> successors() {
        return new C07062();
    }

    public V value(Object node) {
        Object value = this.adjacentNodeValues.get(node);
        if (value == PRED) {
            return null;
        }
        if (value instanceof PredAndSucc) {
            return ((PredAndSucc) value).successorValue;
        }
        return value;
    }

    public void removePredecessor(Object node) {
        Object previousValue = this.adjacentNodeValues.get(node);
        int i;
        if (previousValue == PRED) {
            this.adjacentNodeValues.remove(node);
            i = this.predecessorCount - 1;
            this.predecessorCount = i;
            Graphs.checkNonNegative(i);
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, ((PredAndSucc) previousValue).successorValue);
            i = this.predecessorCount - 1;
            this.predecessorCount = i;
            Graphs.checkNonNegative(i);
        }
    }

    public V removeSuccessor(Object node) {
        Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == null || previousValue == PRED) {
            return null;
        }
        if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, PRED);
            int i = this.successorCount - 1;
            this.successorCount = i;
            Graphs.checkNonNegative(i);
            return ((PredAndSucc) previousValue).successorValue;
        }
        this.adjacentNodeValues.remove(node);
        i = this.successorCount - 1;
        this.successorCount = i;
        Graphs.checkNonNegative(i);
        return previousValue;
    }

    public void addPredecessor(N node, V v) {
        Object previousValue = this.adjacentNodeValues.put(node, PRED);
        int i;
        if (previousValue == null) {
            i = this.predecessorCount + 1;
            this.predecessorCount = i;
            Graphs.checkPositive(i);
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, previousValue);
        } else if (previousValue != PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
            i = this.predecessorCount + 1;
            this.predecessorCount = i;
            Graphs.checkPositive(i);
        }
    }

    public V addSuccessor(N node, V value) {
        Object previousValue = this.adjacentNodeValues.put(node, value);
        int i;
        if (previousValue == null) {
            i = this.successorCount + 1;
            this.successorCount = i;
            Graphs.checkPositive(i);
            return null;
        } else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            return ((PredAndSucc) previousValue).successorValue;
        } else if (previousValue != PRED) {
            return previousValue;
        } else {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            i = this.successorCount + 1;
            this.successorCount = i;
            Graphs.checkPositive(i);
            return null;
        }
    }

    private static boolean isPredecessor(@Nullable Object value) {
        return value == PRED || (value instanceof PredAndSucc);
    }

    private static boolean isSuccessor(@Nullable Object value) {
        return (value == PRED || value == null) ? false : true;
    }
}
