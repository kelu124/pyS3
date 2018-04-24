package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

@GwtCompatible
@Beta
public abstract class TreeTraverser<T> {

    private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Queue<T> queue = new ArrayDeque();

        BreadthFirstIterator(T root) {
            this.queue.add(root);
        }

        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        public T peek() {
            return this.queue.element();
        }

        public T next() {
            T result = this.queue.remove();
            Iterables.addAll(this.queue, TreeTraverser.this.children(result));
            return result;
        }
    }

    private final class PostOrderIterator extends AbstractIterator<T> {
        private final ArrayDeque<PostOrderNode<T>> stack = new ArrayDeque();

        PostOrderIterator(T root) {
            this.stack.addLast(expand(root));
        }

        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                PostOrderNode<T> top = (PostOrderNode) this.stack.getLast();
                if (top.childIterator.hasNext()) {
                    this.stack.addLast(expand(top.childIterator.next()));
                } else {
                    this.stack.removeLast();
                    return top.root;
                }
            }
            return endOfData();
        }

        private PostOrderNode<T> expand(T t) {
            return new PostOrderNode(t, TreeTraverser.this.children(t).iterator());
        }
    }

    private static final class PostOrderNode<T> {
        final Iterator<T> childIterator;
        final T root;

        PostOrderNode(T root, Iterator<T> childIterator) {
            this.root = Preconditions.checkNotNull(root);
            this.childIterator = (Iterator) Preconditions.checkNotNull(childIterator);
        }
    }

    private final class PreOrderIterator extends UnmodifiableIterator<T> {
        private final Deque<Iterator<T>> stack = new ArrayDeque();

        PreOrderIterator(T root) {
            this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
        }

        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        public T next() {
            Iterator<T> itr = (Iterator) this.stack.getLast();
            T result = Preconditions.checkNotNull(itr.next());
            if (!itr.hasNext()) {
                this.stack.removeLast();
            }
            Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
            if (childItr.hasNext()) {
                this.stack.addLast(childItr);
            }
            return result;
        }
    }

    public abstract Iterable<T> children(T t);

    public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> nodeToChildrenFunction) {
        Preconditions.checkNotNull(nodeToChildrenFunction);
        return new TreeTraverser<T>() {
            public Iterable<T> children(T root) {
                return (Iterable) nodeToChildrenFunction.apply(root);
            }
        };
    }

    public final FluentIterable<T> preOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.preOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> preOrderIterator(T root) {
        return new PreOrderIterator(root);
    }

    public final FluentIterable<T> postOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.postOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> postOrderIterator(T root) {
        return new PostOrderIterator(root);
    }

    public final FluentIterable<T> breadthFirstTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return new BreadthFirstIterator(root);
            }
        };
    }
}
