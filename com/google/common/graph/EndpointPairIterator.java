package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;

abstract class EndpointPairIterator<N> extends AbstractIterator<EndpointPair<N>> {
    private final Graph<N> graph;
    protected N node;
    private final Iterator<N> nodeIterator;
    protected Iterator<N> successorIterator;

    private static final class Directed<N> extends EndpointPairIterator<N> {
        private Directed(Graph<N> graph) {
            super(graph);
        }

        protected EndpointPair<N> computeNext() {
            while (!this.successorIterator.hasNext()) {
                if (!advance()) {
                    return (EndpointPair) endOfData();
                }
            }
            return EndpointPair.ordered(this.node, this.successorIterator.next());
        }
    }

    private static final class Undirected<N> extends EndpointPairIterator<N> {
        private Set<N> visitedNodes;

        private Undirected(Graph<N> graph) {
            super(graph);
            this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size());
        }

        protected EndpointPair<N> computeNext() {
            while (true) {
                if (this.successorIterator.hasNext()) {
                    N otherNode = this.successorIterator.next();
                    if (!this.visitedNodes.contains(otherNode)) {
                        return EndpointPair.unordered(this.node, otherNode);
                    }
                } else {
                    this.visitedNodes.add(this.node);
                    if (!advance()) {
                        this.visitedNodes = null;
                        return (EndpointPair) endOfData();
                    }
                }
            }
        }
    }

    static <N> EndpointPairIterator<N> of(Graph<N> graph) {
        return graph.isDirected() ? new Directed(graph) : new Undirected(graph);
    }

    private EndpointPairIterator(Graph<N> graph) {
        this.node = null;
        this.successorIterator = ImmutableSet.of().iterator();
        this.graph = graph;
        this.nodeIterator = graph.nodes().iterator();
    }

    protected final boolean advance() {
        Preconditions.checkState(!this.successorIterator.hasNext());
        if (!this.nodeIterator.hasNext()) {
            return false;
        }
        this.node = this.nodeIterator.next();
        this.successorIterator = this.graph.successors(this.node).iterator();
        return true;
    }
}
