package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;

@Beta
public final class ImmutableValueGraph<N, V> extends ValueBackedImpl<N, V> implements ValueGraph<N, V> {
    private ImmutableValueGraph(ValueGraph<N, V> graph) {
        super(ValueGraphBuilder.from(graph), getNodeConnections(graph), (long) graph.edges().size());
    }

    public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
        return graph instanceof ImmutableValueGraph ? (ImmutableValueGraph) graph : new ImmutableValueGraph(graph);
    }

    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
        return (ImmutableValueGraph) Preconditions.checkNotNull(graph);
    }

    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
        Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
        for (N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }

    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
        Function successorNodeToValueFn = new Function<N, V>() {
            public V apply(N successorNode) {
                return graph.edgeValue(node, successorNode);
            }
        };
        return graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), Maps.asMap(graph.successors(node), successorNodeToValueFn)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
    }

    public V edgeValue(Object nodeU, Object nodeV) {
        return this.backingValueGraph.edgeValue(nodeU, nodeV);
    }

    public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
        return this.backingValueGraph.edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }

    public String toString() {
        return this.backingValueGraph.toString();
    }
}
