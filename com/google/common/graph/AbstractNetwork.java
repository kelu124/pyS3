package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractNetwork<N, E> implements Network<N, E> {

    class C07001 extends AbstractGraph<N> {

        class C06991 extends AbstractSet<EndpointPair<N>> {

            class C06981 implements Function<E, EndpointPair<N>> {
                C06981() {
                }

                public EndpointPair<N> apply(E edge) {
                    return AbstractNetwork.this.incidentNodes(edge);
                }
            }

            C06991() {
            }

            public Iterator<EndpointPair<N>> iterator() {
                return Iterators.transform(AbstractNetwork.this.edges().iterator(), new C06981());
            }

            public int size() {
                return AbstractNetwork.this.edges().size();
            }

            public boolean contains(@Nullable Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair<?> endpointPair = (EndpointPair) obj;
                if (C07001.this.isDirected() == endpointPair.isOrdered() && C07001.this.nodes().contains(endpointPair.nodeU()) && C07001.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV())) {
                    return true;
                }
                return false;
            }
        }

        C07001() {
        }

        public Set<N> nodes() {
            return AbstractNetwork.this.nodes();
        }

        public Set<EndpointPair<N>> edges() {
            if (AbstractNetwork.this.allowsParallelEdges()) {
                return super.edges();
            }
            return new C06991();
        }

        public ElementOrder<N> nodeOrder() {
            return AbstractNetwork.this.nodeOrder();
        }

        public boolean isDirected() {
            return AbstractNetwork.this.isDirected();
        }

        public boolean allowsSelfLoops() {
            return AbstractNetwork.this.allowsSelfLoops();
        }

        public Set<N> adjacentNodes(Object node) {
            return AbstractNetwork.this.adjacentNodes(node);
        }

        public Set<N> predecessors(Object node) {
            return AbstractNetwork.this.predecessors(node);
        }

        public Set<N> successors(Object node) {
            return AbstractNetwork.this.successors(node);
        }
    }

    class C07012 implements Function<E, EndpointPair<N>> {
        C07012() {
        }

        public EndpointPair<N> apply(E edge) {
            return AbstractNetwork.this.incidentNodes(edge);
        }
    }

    public Graph<N> asGraph() {
        return new C07001();
    }

    public int degree(Object node) {
        if (isDirected()) {
            return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
        }
        return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
    }

    public int inDegree(Object node) {
        return isDirected() ? inEdges(node).size() : degree(node);
    }

    public int outDegree(Object node) {
        return isDirected() ? outEdges(node).size() : degree(node);
    }

    public Set<E> adjacentEdges(Object edge) {
        EndpointPair<?> endpointPair = incidentNodes(edge);
        return Sets.difference(Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV())), ImmutableSet.of(edge));
    }

    public String toString() {
        String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", new Object[]{Boolean.valueOf(isDirected()), Boolean.valueOf(allowsParallelEdges()), Boolean.valueOf(allowsSelfLoops())});
        return String.format("%s, nodes: %s, edges: %s", new Object[]{propertiesString, nodes(), edgeIncidentNodesMap()});
    }

    private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
        return Maps.asMap(edges(), new C07012());
    }
}
