package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V> {
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ boolean containsEntry(Object x0, Object x1) {
        return super.containsEntry(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean containsKey(Object x0) {
        return super.containsKey(x0);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object x0) {
        return super.containsValue(x0);
    }

    public /* bridge */ /* synthetic */ Set entries() {
        return super.entries();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    public /* bridge */ /* synthetic */ boolean put(Object x0, Object x1) {
        return super.put(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Multimap x0) {
        return super.putAll(x0);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Object x0, Iterable x1) {
        return super.putAll(x0, x1);
    }

    public /* bridge */ /* synthetic */ boolean remove(Object x0, Object x1) {
        return super.remove(x0, x1);
    }

    public /* bridge */ /* synthetic */ SortedSet removeAll(Object x0) {
        return super.removeAll(x0);
    }

    public /* bridge */ /* synthetic */ SortedSet replaceValues(Object x0, Iterable x1) {
        return super.replaceValues(x0, x1);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public /* bridge */ /* synthetic */ Collection values() {
        return super.values();
    }

    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
        return new TreeMultimap(Ordering.natural(), Ordering.natural());
    }

    public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
        return new TreeMultimap((Comparator) Preconditions.checkNotNull(keyComparator), (Comparator) Preconditions.checkNotNull(valueComparator));
    }

    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new TreeMultimap(Ordering.natural(), Ordering.natural(), multimap);
    }

    TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
        super(new TreeMap(keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }

    private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
        this(keyComparator, valueComparator);
        putAll(multimap);
    }

    SortedSet<V> createCollection() {
        return new TreeSet(this.valueComparator);
    }

    Collection<V> createCollection(@Nullable K key) {
        if (key == null) {
            keyComparator().compare(key, key);
        }
        return super.createCollection(key);
    }

    public Comparator<? super K> keyComparator() {
        return this.keyComparator;
    }

    public Comparator<? super V> valueComparator() {
        return this.valueComparator;
    }

    @GwtIncompatible
    NavigableMap<K, Collection<V>> backingMap() {
        return (NavigableMap) super.backingMap();
    }

    @GwtIncompatible
    public NavigableSet<V> get(@Nullable K key) {
        return (NavigableSet) super.get((Object) key);
    }

    @GwtIncompatible
    Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        return Sets.unmodifiableNavigableSet((NavigableSet) collection);
    }

    @GwtIncompatible
    Collection<V> wrapCollection(K key, Collection<V> collection) {
        return new WrappedNavigableSet(key, (NavigableSet) collection, null);
    }

    @GwtIncompatible
    public NavigableSet<K> keySet() {
        return (NavigableSet) super.keySet();
    }

    @GwtIncompatible
    NavigableSet<K> createKeySet() {
        return new NavigableKeySet(backingMap());
    }

    @GwtIncompatible
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap) super.asMap();
    }

    @GwtIncompatible
    NavigableMap<K, Collection<V>> createAsMap() {
        return new NavigableAsMap(backingMap());
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(keyComparator());
        stream.writeObject(valueComparator());
        Serialization.writeMultimap(this, stream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyComparator = (Comparator) Preconditions.checkNotNull((Comparator) stream.readObject());
        this.valueComparator = (Comparator) Preconditions.checkNotNull((Comparator) stream.readObject());
        setMap(new TreeMap(this.keyComparator));
        Serialization.populateMultimap(this, stream);
    }
}
