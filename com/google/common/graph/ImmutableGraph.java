package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.util.Set;

@Beta
public abstract class ImmutableGraph<N> extends ForwardingGraph<N> {

    static class ValueBackedImpl<N, V> extends ImmutableGraph<N> {
        protected final ValueGraph<N, V> backingValueGraph;

        ValueBackedImpl(AbstractGraphBuilder<? super N> builder, ImmutableMap<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
            this.backingValueGraph = new ConfigurableValueGraph(builder, nodeConnections, edgeCount);
        }

        protected Graph<N> delegate() {
            return this.backingValueGraph;
        }
    }

    public /* bridge */ /* synthetic */ Set adjacentNodes(Object x0) {
        return super.adjacentNodes(x0);
    }

    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    public /* bridge */ /* synthetic */ int degree(Object x0) {
        return super.degree(x0);
    }

    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    public /* bridge */ /* synthetic */ int inDegree(Object x0) {
        return super.inDegree(x0);
    }

    public /* bridge */ /* synthetic */ boolean isDirected() {
        return super.isDirected();
    }

    public /* bridge */ /* synthetic */ ElementOrder nodeOrder() {
        return super.nodeOrder();
    }

    public /* bridge */ /* synthetic */ Set nodes() {
        return super.nodes();
    }

    public /* bridge */ /* synthetic */ int outDegree(Object x0) {
        return super.outDegree(x0);
    }

    public /* bridge */ /* synthetic */ Set predecessors(Object x0) {
        return super.predecessors(x0);
    }

    public /* bridge */ /* synthetic */ Set successors(Object x0) {
        return super.successors(x0);
    }

    ImmutableGraph() {
    }

    public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
        return graph instanceof ImmutableGraph ? (ImmutableGraph) graph : new ValueBackedImpl(GraphBuilder.from(graph), getNodeConnections(graph), (long) graph.edges().size());
    }

    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
        return (ImmutableGraph) Preconditions.checkNotNull(graph);
    }

    private static <N> ImmutableMap<N, GraphConnections<N, Presence>> getNodeConnections(Graph<N> graph) {
        Builder<N, GraphConnections<N, Presence>> nodeConnections = ImmutableMap.builder();
        for (N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }

    private static <N> GraphConnections<N, Presence> connectionsOf(Graph<N> graph, N node) {
        Function edgeValueFn = Functions.constant(Presence.EDGE_EXISTS);
        return graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), Maps.asMap(graph.successors(node), edgeValueFn)) : UndirectedGraphConnections.ofImmutable(Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
    }
}
