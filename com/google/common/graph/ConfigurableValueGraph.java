package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

class ConfigurableValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean allowsSelfLoops;
    protected long edgeCount;
    private final boolean isDirected;
    protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder) {
        this(builder, builder.nodeOrder.createMap(((Integer) builder.expectedNodeCount.or(Integer.valueOf(10))).intValue()), 0);
    }

    ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
        this.isDirected = builder.directed;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.cast();
        this.nodeConnections = nodeConnections instanceof TreeMap ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections);
        this.edgeCount = Graphs.checkNonNegative(edgeCount);
    }

    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    public boolean isDirected() {
        return this.isDirected;
    }

    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    public Set<N> adjacentNodes(Object node) {
        return checkedConnections(node).adjacentNodes();
    }

    public Set<N> predecessors(Object node) {
        return checkedConnections(node).predecessors();
    }

    public Set<N> successors(Object node) {
        return checkedConnections(node).successors();
    }

    public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
        GraphConnections<N, V> connectionsU = (GraphConnections) this.nodeConnections.get(nodeU);
        if (connectionsU == null) {
            return defaultValue;
        }
        V value = connectionsU.value(nodeV);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    protected long edgeCount() {
        return this.edgeCount;
    }

    protected final GraphConnections<N, V> checkedConnections(Object node) {
        GraphConnections<N, V> connections = (GraphConnections) this.nodeConnections.get(node);
        if (connections != null) {
            return connections;
        }
        Preconditions.checkNotNull(node);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[]{node}));
    }

    protected final boolean containsNode(@Nullable Object node) {
        return this.nodeConnections.containsKey(node);
    }
}
