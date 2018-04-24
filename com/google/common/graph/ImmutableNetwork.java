package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

@Beta
public final class ImmutableNetwork<N, E> extends ConfigurableNetwork<N, E> {
    public /* bridge */ /* synthetic */ Set adjacentNodes(Object x0) {
        return super.adjacentNodes(x0);
    }

    public /* bridge */ /* synthetic */ boolean allowsParallelEdges() {
        return super.allowsParallelEdges();
    }

    public /* bridge */ /* synthetic */ boolean allowsSelfLoops() {
        return super.allowsSelfLoops();
    }

    public /* bridge */ /* synthetic */ ElementOrder edgeOrder() {
        return super.edgeOrder();
    }

    public /* bridge */ /* synthetic */ Set edges() {
        return super.edges();
    }

    public /* bridge */ /* synthetic */ Set edgesConnecting(Object x0, Object x1) {
        return super.edgesConnecting(x0, x1);
    }

    public /* bridge */ /* synthetic */ Set inEdges(Object x0) {
        return super.inEdges(x0);
    }

    public /* bridge */ /* synthetic */ Set incidentEdges(Object x0) {
        return super.incidentEdges(x0);
    }

    public /* bridge */ /* synthetic */ EndpointPair incidentNodes(Object x0) {
        return super.incidentNodes(x0);
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

    public /* bridge */ /* synthetic */ Set outEdges(Object x0) {
        return super.outEdges(x0);
    }

    public /* bridge */ /* synthetic */ Set predecessors(Object x0) {
        return super.predecessors(x0);
    }

    public /* bridge */ /* synthetic */ Set successors(Object x0) {
        return super.successors(x0);
    }

    private ImmutableNetwork(Network<N, E> network) {
        super(NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
    }

    public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
        return network instanceof ImmutableNetwork ? (ImmutableNetwork) network : new ImmutableNetwork(network);
    }

    @Deprecated
    public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
        return (ImmutableNetwork) Preconditions.checkNotNull(network);
    }

    public ImmutableGraph<N> asGraph() {
        final Graph<N> asGraph = super.asGraph();
        return new ImmutableGraph<N>() {
            protected Graph<N> delegate() {
                return asGraph;
            }
        };
    }

    private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
        Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
        for (N node : network.nodes()) {
            nodeConnections.put(node, connectionsOf(network, node));
        }
        return nodeConnections.build();
    }

    private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
        Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
        for (E edge : network.edges()) {
            edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
        }
        return edgeToReferenceNode.build();
    }

    private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
        if (network.isDirected()) {
            Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
            Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
            int selfLoopCount = network.edgesConnecting(node, node).size();
            return network.allowsParallelEdges() ? DirectedMultiNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : DirectedNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
        } else {
            Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
            return network.allowsParallelEdges() ? UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap) : UndirectedNetworkConnections.ofImmutable(incidentEdgeMap);
        }
    }

    private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            public N apply(E edge) {
                return network.incidentNodes(edge).source();
            }
        };
    }

    private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            public N apply(E edge) {
                return network.incidentNodes(edge).target();
            }
        };
    }

    private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
        return new Function<E, N>() {
            public N apply(E edge) {
                return network.incidentNodes(edge).adjacentNode(node);
            }
        };
    }
}
