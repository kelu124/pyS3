package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

abstract class AbstractDirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> inEdgeMap;
    protected final Map<E, N> outEdgeMap;
    private int selfLoopCount;

    class C06961 extends AbstractSet<E> {
        C06961() {
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.unmodifiableIterator((AbstractDirectedNetworkConnections.this.selfLoopCount == 0 ? Iterables.concat(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : Sets.union(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet())).iterator());
        }

        public int size() {
            return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
        }

        public boolean contains(@Nullable Object obj) {
            return AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj);
        }
    }

    protected AbstractDirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount) {
        this.inEdgeMap = (Map) Preconditions.checkNotNull(inEdgeMap);
        this.outEdgeMap = (Map) Preconditions.checkNotNull(outEdgeMap);
        this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
        boolean z = selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size();
        Preconditions.checkState(z);
    }

    public Set<N> adjacentNodes() {
        return Sets.union(predecessors(), successors());
    }

    public Set<E> incidentEdges() {
        return new C06961();
    }

    public Set<E> inEdges() {
        return Collections.unmodifiableSet(this.inEdgeMap.keySet());
    }

    public Set<E> outEdges() {
        return Collections.unmodifiableSet(this.outEdgeMap.keySet());
    }

    public N oppositeNode(Object edge) {
        return Preconditions.checkNotNull(this.outEdgeMap.get(edge));
    }

    public N removeInEdge(Object edge, boolean isSelfLoop) {
        if (isSelfLoop) {
            int i = this.selfLoopCount - 1;
            this.selfLoopCount = i;
            Graphs.checkNonNegative(i);
        }
        return Preconditions.checkNotNull(this.inEdgeMap.remove(edge));
    }

    public N removeOutEdge(Object edge) {
        return Preconditions.checkNotNull(this.outEdgeMap.remove(edge));
    }

    public void addInEdge(E edge, N node, boolean isSelfLoop) {
        if (isSelfLoop) {
            int i = this.selfLoopCount + 1;
            this.selfLoopCount = i;
            Graphs.checkPositive(i);
        }
        Preconditions.checkState(this.inEdgeMap.put(edge, node) == null);
    }

    public void addOutEdge(E edge, N node) {
        Preconditions.checkState(this.outEdgeMap.put(edge, node) == null);
    }
}
