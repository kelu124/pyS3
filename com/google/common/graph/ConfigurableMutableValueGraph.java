package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

final class ConfigurableMutableValueGraph<N, V> extends ConfigurableValueGraph<N, V> implements MutableValueGraph<N, V> {
    ConfigurableMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
        super(builder);
    }

    @CanIgnoreReturnValue
    public boolean addNode(N node) {
        Preconditions.checkNotNull(node, "node");
        if (containsNode(node)) {
            return false;
        }
        addNodeInternal(node);
        return true;
    }

    @CanIgnoreReturnValue
    private GraphConnections<N, V> addNodeInternal(N node) {
        GraphConnections<N, V> connections = newConnections();
        Preconditions.checkState(this.nodeConnections.put(node, connections) == null);
        return connections;
    }

    @CanIgnoreReturnValue
    public V putEdgeValue(N nodeU, N nodeV, V value) {
        Preconditions.checkNotNull(nodeU, "nodeU");
        Preconditions.checkNotNull(nodeV, "nodeV");
        Preconditions.checkNotNull(value, "value");
        if (!allowsSelfLoops()) {
            Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", (Object) nodeU);
        }
        GraphConnections<N, V> connectionsU = (GraphConnections) this.nodeConnections.get(nodeU);
        if (connectionsU == null) {
            connectionsU = addNodeInternal(nodeU);
        }
        V previousValue = connectionsU.addSuccessor(nodeV, value);
        GraphConnections<N, V> connectionsV = (GraphConnections) this.nodeConnections.get(nodeV);
        if (connectionsV == null) {
            connectionsV = addNodeInternal(nodeV);
        }
        connectionsV.addPredecessor(nodeU, value);
        if (previousValue == null) {
            long j = this.edgeCount + 1;
            this.edgeCount = j;
            Graphs.checkPositive(j);
        }
        return previousValue;
    }

    @CanIgnoreReturnValue
    public boolean removeNode(Object node) {
        Preconditions.checkNotNull(node, "node");
        GraphConnections<N, V> connections = (GraphConnections) this.nodeConnections.get(node);
        if (connections == null) {
            return false;
        }
        if (allowsSelfLoops() && connections.removeSuccessor(node) != null) {
            connections.removePredecessor(node);
            this.edgeCount--;
        }
        for (N successor : connections.successors()) {
            ((GraphConnections) this.nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
            this.edgeCount--;
        }
        if (isDirected()) {
            for (N predecessor : connections.predecessors()) {
                boolean z;
                if (((GraphConnections) this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null) {
                    z = true;
                } else {
                    z = false;
                }
                Preconditions.checkState(z);
                this.edgeCount--;
            }
        }
        this.nodeConnections.remove(node);
        Graphs.checkNonNegative(this.edgeCount);
        return true;
    }

    @CanIgnoreReturnValue
    public V removeEdge(Object nodeU, Object nodeV) {
        Preconditions.checkNotNull(nodeU, "nodeU");
        Preconditions.checkNotNull(nodeV, "nodeV");
        GraphConnections<N, V> connectionsU = (GraphConnections) this.nodeConnections.get(nodeU);
        GraphConnections<N, V> connectionsV = (GraphConnections) this.nodeConnections.get(nodeV);
        if (connectionsU == null || connectionsV == null) {
            return null;
        }
        V previousValue = connectionsU.removeSuccessor(nodeV);
        if (previousValue == null) {
            return previousValue;
        }
        connectionsV.removePredecessor(nodeU);
        long j = this.edgeCount - 1;
        this.edgeCount = j;
        Graphs.checkNonNegative(j);
        return previousValue;
    }

    private GraphConnections<N, V> newConnections() {
        return isDirected() ? DirectedGraphConnections.of() : UndirectedGraphConnections.of();
    }
}
