package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

class ConfigurableNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<E> edgeOrder;
    protected final MapIteratorCache<E, N> edgeToReferenceNode;
    private final boolean isDirected;
    protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    private final ElementOrder<N> nodeOrder;

    ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder) {
        this(builder, builder.nodeOrder.createMap(((Integer) builder.expectedNodeCount.or(Integer.valueOf(10))).intValue()), builder.edgeOrder.createMap(((Integer) builder.expectedEdgeCount.or(Integer.valueOf(20))).intValue()));
    }

    ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder, Map<N, NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode) {
        this.isDirected = builder.directed;
        this.allowsParallelEdges = builder.allowsParallelEdges;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.cast();
        this.edgeOrder = builder.edgeOrder.cast();
        this.nodeConnections = nodeConnections instanceof TreeMap ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections);
        this.edgeToReferenceNode = new MapIteratorCache(edgeToReferenceNode);
    }

    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }

    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }

    public boolean isDirected() {
        return this.isDirected;
    }

    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }

    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }

    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }

    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }

    public Set<E> incidentEdges(Object node) {
        return checkedConnections(node).incidentEdges();
    }

    public EndpointPair<N> incidentNodes(Object edge) {
        Object nodeU = checkedReferenceNode(edge);
        return EndpointPair.of((Network) this, nodeU, ((NetworkConnections) this.nodeConnections.get(nodeU)).oppositeNode(edge));
    }

    public Set<N> adjacentNodes(Object node) {
        return checkedConnections(node).adjacentNodes();
    }

    public Set<E> edgesConnecting(Object nodeU, Object nodeV) {
        NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
        if (!this.allowsSelfLoops && nodeU == nodeV) {
            return ImmutableSet.of();
        }
        Preconditions.checkArgument(containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
        return connectionsU.edgesConnecting(nodeV);
    }

    public Set<E> inEdges(Object node) {
        return checkedConnections(node).inEdges();
    }

    public Set<E> outEdges(Object node) {
        return checkedConnections(node).outEdges();
    }

    public Set<N> predecessors(Object node) {
        return checkedConnections(node).predecessors();
    }

    public Set<N> successors(Object node) {
        return checkedConnections(node).successors();
    }

    protected final NetworkConnections<N, E> checkedConnections(Object node) {
        NetworkConnections<N, E> connections = (NetworkConnections) this.nodeConnections.get(node);
        if (connections != null) {
            return connections;
        }
        Preconditions.checkNotNull(node);
        throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[]{node}));
    }

    protected final N checkedReferenceNode(Object edge) {
        N referenceNode = this.edgeToReferenceNode.get(edge);
        if (referenceNode != null) {
            return referenceNode;
        }
        Preconditions.checkNotNull(edge);
        throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[]{edge}));
    }

    protected final boolean containsNode(@Nullable Object node) {
        return this.nodeConnections.containsKey(node);
    }

    protected final boolean containsEdge(@Nullable Object edge) {
        return this.edgeToReferenceNode.containsKey(edge);
    }
}
