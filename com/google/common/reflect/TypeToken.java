package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Primitives;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
    private final Type runtimeType;
    private transient TypeResolver typeResolver;

    class C07743 extends TypeVisitor {
        C07743() {
        }

        void visitTypeVariable(TypeVariable<?> typeVariable) {
            throw new IllegalArgumentException(TypeToken.this.runtimeType + "contains a type variable and is not safe for the operation");
        }

        void visitWildcardType(WildcardType type) {
            visit(type.getLowerBounds());
            visit(type.getUpperBounds());
        }

        void visitParameterizedType(ParameterizedType type) {
            visit(type.getActualTypeArguments());
            visit(type.getOwnerType());
        }

        void visitGenericArrayType(GenericArrayType type) {
            visit(type.getGenericComponentType());
        }
    }

    private static class Bounds {
        private final Type[] bounds;
        private final boolean target;

        Bounds(Type[] bounds, boolean target) {
            this.bounds = bounds;
            this.target = target;
        }

        boolean isSubtypeOf(Type supertype) {
            for (Type bound : this.bounds) {
                if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
                    return this.target;
                }
            }
            return !this.target;
        }

        boolean isSupertypeOf(Type subtype) {
            TypeToken<?> type = TypeToken.of(subtype);
            for (Type bound : this.bounds) {
                if (type.isSubtypeOf(bound) == this.target) {
                    return this.target;
                }
            }
            return !this.target;
        }
    }

    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
        private static final long serialVersionUID = 0;
        private transient ImmutableSet<TypeToken<? super T>> types;

        TypeSet() {
        }

        public TypeSet interfaces() {
            return new InterfaceSet(this);
        }

        public TypeSet classes() {
            return new ClassSet();
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set<TypeToken<? super T>> set = this.types;
            if (set != null) {
                return set;
            }
            Set toSet = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            this.types = toSet;
            return toSet;
        }

        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getRawTypes()));
        }
    }

    private final class ClassSet extends TypeSet {
        private static final long serialVersionUID = 0;
        private transient ImmutableSet<TypeToken<? super T>> classes;

        private ClassSet() {
            super();
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set<TypeToken<? super T>> set = this.classes;
            if (set != null) {
                return set;
            }
            Set toSet = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            this.classes = toSet;
            return toSet;
        }

        public TypeSet classes() {
            return this;
        }

        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.this.getRawTypes()));
        }

        public TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().classes();
        }
    }

    private final class InterfaceSet extends TypeSet {
        private static final long serialVersionUID = 0;
        private final transient TypeSet allTypes;
        private transient ImmutableSet<TypeToken<? super T>> interfaces;

        class C07761 implements Predicate<Class<?>> {
            C07761() {
            }

            public boolean apply(Class<?> type) {
                return type.isInterface();
            }
        }

        InterfaceSet(TypeSet allTypes) {
            super();
            this.allTypes = allTypes;
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set<TypeToken<? super T>> set = this.interfaces;
            if (set != null) {
                return set;
            }
            Set toSet = FluentIterable.from(this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toSet();
            this.interfaces = toSet;
            return toSet;
        }

        public TypeSet interfaces() {
            return this;
        }

        public Set<Class<? super T>> rawTypes() {
            return FluentIterable.from(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getRawTypes())).filter(new C07761()).toSet();
        }

        public TypeSet classes() {
            throw new UnsupportedOperationException("interfaces().classes() not supported.");
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().interfaces();
        }
    }

    private static final class SimpleTypeToken<T> extends TypeToken<T> {
        private static final long serialVersionUID = 0;

        SimpleTypeToken(Type type) {
            super(type);
        }
    }

    private static abstract class TypeCollector<K> {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new C07771();
        static final TypeCollector<Class<?>> FOR_RAW_TYPE = new C07782();

        static class C07771 extends TypeCollector<TypeToken<?>> {
            C07771() {
                super();
            }

            Class<?> getRawType(TypeToken<?> type) {
                return type.getRawType();
            }

            Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
                return type.getGenericInterfaces();
            }

            @Nullable
            TypeToken<?> getSuperclass(TypeToken<?> type) {
                return type.getGenericSuperclass();
            }
        }

        static class C07782 extends TypeCollector<Class<?>> {
            C07782() {
                super();
            }

            Class<?> getRawType(Class<?> type) {
                return type;
            }

            Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
                return Arrays.asList(type.getInterfaces());
            }

            @Nullable
            Class<?> getSuperclass(Class<?> type) {
                return type.getSuperclass();
            }
        }

        private static class ForwardingTypeCollector<K> extends TypeCollector<K> {
            private final TypeCollector<K> delegate;

            ForwardingTypeCollector(TypeCollector<K> delegate) {
                super();
                this.delegate = delegate;
            }

            Class<?> getRawType(K type) {
                return this.delegate.getRawType(type);
            }

            Iterable<? extends K> getInterfaces(K type) {
                return this.delegate.getInterfaces(type);
            }

            K getSuperclass(K type) {
                return this.delegate.getSuperclass(type);
            }
        }

        abstract Iterable<? extends K> getInterfaces(K k);

        abstract Class<?> getRawType(K k);

        @Nullable
        abstract K getSuperclass(K k);

        private TypeCollector() {
        }

        final TypeCollector<K> classesOnly() {
            return new ForwardingTypeCollector<K>(this) {
                Iterable<? extends K> getInterfaces(K k) {
                    return ImmutableSet.of();
                }

                ImmutableList<K> collectTypes(Iterable<? extends K> types) {
                    Builder<K> builder = ImmutableList.builder();
                    for (Object type : types) {
                        if (!getRawType(type).isInterface()) {
                            builder.add(type);
                        }
                    }
                    return super.collectTypes(builder.build());
                }
            };
        }

        final ImmutableList<K> collectTypes(K type) {
            return collectTypes(ImmutableList.of(type));
        }

        ImmutableList<K> collectTypes(Iterable<? extends K> types) {
            Map<K, Integer> map = Maps.newHashMap();
            for (K type : types) {
                collectTypes(type, map);
            }
            return sortKeysByValue(map, Ordering.natural().reverse());
        }

        @CanIgnoreReturnValue
        private int collectTypes(K type, Map<? super K, Integer> map) {
            Integer existing = (Integer) map.get(type);
            if (existing != null) {
                return existing.intValue();
            }
            int aboveMe = getRawType(type).isInterface() ? 1 : 0;
            for (K interfaceType : getInterfaces(type)) {
                aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
            }
            K superclass = getSuperclass(type);
            if (superclass != null) {
                aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
            }
            map.put(type, Integer.valueOf(aboveMe + 1));
            return aboveMe + 1;
        }

        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
            return new Ordering<K>() {
                public int compare(K left, K right) {
                    return valueComparator.compare(map.get(left), map.get(right));
                }
            }.immutableSortedCopy(map.keySet());
        }
    }

    private enum TypeFilter implements Predicate<TypeToken<?>> {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD {
            public boolean apply(TypeToken<?> type) {
                return ((type.runtimeType instanceof TypeVariable) || (type.runtimeType instanceof WildcardType)) ? false : true;
            }
        },
        INTERFACE_ONLY {
            public boolean apply(TypeToken<?> type) {
                return type.getRawType().isInterface();
            }
        }
    }

    protected TypeToken() {
        this.runtimeType = capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    protected TypeToken(Class<?> declaringClass) {
        Type captured = super.capture();
        if (captured instanceof Class) {
            this.runtimeType = captured;
        } else {
            this.runtimeType = of((Class) declaringClass).resolveType(captured).runtimeType;
        }
    }

    private TypeToken(Type type) {
        this.runtimeType = (Type) Preconditions.checkNotNull(type);
    }

    public static <T> TypeToken<T> of(Class<T> type) {
        return new SimpleTypeToken(type);
    }

    public static TypeToken<?> of(Type type) {
        return new SimpleTypeToken(type);
    }

    public final Class<? super T> getRawType() {
        return (Class) getRawTypes().iterator().next();
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
        return new SimpleTypeToken(new TypeResolver().where(ImmutableMap.of(new TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType)).resolveType(this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
        return where((TypeParameter) typeParam, of((Class) typeArg));
    }

    public final TypeToken<?> resolveType(Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver resolver = this.typeResolver;
        if (resolver == null) {
            resolver = TypeResolver.accordingTo(this.runtimeType);
            this.typeResolver = resolver;
        }
        return of(resolver.resolveType(type));
    }

    private Type[] resolveInPlace(Type[] types) {
        for (int i = 0; i < types.length; i++) {
            types[i] = resolveType(types[i]).getType();
        }
        return types;
    }

    private TypeToken<?> resolveSupertype(Type type) {
        TypeToken<?> supertype = resolveType(type);
        supertype.typeResolver = this.typeResolver;
        return supertype;
    }

    @Nullable
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundAsSuperclass(((TypeVariable) this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundAsSuperclass(((WildcardType) this.runtimeType).getUpperBounds()[0]);
        }
        Type superclass = getRawType().getGenericSuperclass();
        if (superclass == null) {
            return null;
        }
        return resolveSupertype(superclass);
    }

    @Nullable
    private TypeToken<? super T> boundAsSuperclass(Type bound) {
        TypeToken<?> token = of(bound);
        if (token.getRawType().isInterface()) {
            return null;
        }
        return token;
    }

    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundsAsInterfaces(((TypeVariable) this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundsAsInterfaces(((WildcardType) this.runtimeType).getUpperBounds());
        }
        Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (Type interfaceType : getRawType().getGenericInterfaces()) {
            builder.add(resolveSupertype(interfaceType));
        }
        return builder.build();
    }

    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
        Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (Type bound : bounds) {
            Object boundType = of(bound);
            if (boundType.getRawType().isInterface()) {
                builder.add(boundType);
            }
        }
        return builder.build();
    }

    public final TypeSet getTypes() {
        return new TypeSet();
    }

    public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
        Preconditions.checkArgument(someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", (Object) superclass, (Object) this);
        if (this.runtimeType instanceof TypeVariable) {
            return getSupertypeFromUpperBounds(superclass, ((TypeVariable) this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return getSupertypeFromUpperBounds(superclass, ((WildcardType) this.runtimeType).getUpperBounds());
        }
        if (superclass.isArray()) {
            return getArraySupertype(superclass);
        }
        return resolveSupertype(toGenericType(superclass).runtimeType);
    }

    public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", (Object) this);
        if (this.runtimeType instanceof WildcardType) {
            return getSubtypeFromLowerBounds(subclass, ((WildcardType) this.runtimeType).getLowerBounds());
        }
        if (isArray()) {
            return getArraySubtype(subclass);
        }
        Preconditions.checkArgument(getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", (Object) subclass, (Object) this);
        return of(resolveTypeArgsForSubclass(subclass));
    }

    public final boolean isSupertypeOf(TypeToken<?> type) {
        return type.isSubtypeOf(getType());
    }

    public final boolean isSupertypeOf(Type type) {
        return of(type).isSubtypeOf(getType());
    }

    public final boolean isSubtypeOf(TypeToken<?> type) {
        return isSubtypeOf(type.getType());
    }

    public final boolean isSubtypeOf(Type supertype) {
        Preconditions.checkNotNull(supertype);
        if (supertype instanceof WildcardType) {
            return any(((WildcardType) supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
        }
        if (this.runtimeType instanceof WildcardType) {
            return any(((WildcardType) this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
        }
        if (this.runtimeType instanceof TypeVariable) {
            return this.runtimeType.equals(supertype) || any(((TypeVariable) this.runtimeType).getBounds()).isSubtypeOf(supertype);
        } else {
            if (this.runtimeType instanceof GenericArrayType) {
                return of(supertype).isSupertypeOfArray((GenericArrayType) this.runtimeType);
            }
            if (supertype instanceof Class) {
                return someRawTypeIsSubclassOf((Class) supertype);
            }
            if (supertype instanceof ParameterizedType) {
                return isSubtypeOfParameterizedType((ParameterizedType) supertype);
            }
            return supertype instanceof GenericArrayType ? isSubtypeOfArrayType((GenericArrayType) supertype) : false;
        }
    }

    public final boolean isArray() {
        return getComponentType() != null;
    }

    public final boolean isPrimitive() {
        return (this.runtimeType instanceof Class) && ((Class) this.runtimeType).isPrimitive();
    }

    public final TypeToken<T> wrap() {
        if (isPrimitive()) {
            return of(Primitives.wrap(this.runtimeType));
        }
        return this;
    }

    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }

    public final TypeToken<T> unwrap() {
        if (isWrapper()) {
            return of(Primitives.unwrap(this.runtimeType));
        }
        return this;
    }

    @Nullable
    public final TypeToken<?> getComponentType() {
        Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return of(componentType);
    }

    public final Invokable<T, Object> method(Method method) {
        Preconditions.checkArgument(someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", (Object) method, (Object) this);
        return new MethodInvokable<T>(method) {
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }

            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }

            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }

            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }

            public String toString() {
                return getOwnerType() + "." + super.toString();
            }
        };
    }

    public final Invokable<T, T> constructor(Constructor<?> constructor) {
        Preconditions.checkArgument(constructor.getDeclaringClass() == getRawType(), "%s not declared by %s", (Object) constructor, getRawType());
        return new ConstructorInvokable<T>(constructor) {
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }

            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }

            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }

            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }

            public String toString() {
                return getOwnerType() + "(" + Joiner.on(", ").join(getGenericParameterTypes()) + ")";
            }
        };
    }

    public boolean equals(@Nullable Object o) {
        if (!(o instanceof TypeToken)) {
            return false;
        }
        return this.runtimeType.equals(((TypeToken) o).runtimeType);
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }

    @CanIgnoreReturnValue
    final TypeToken<T> rejectTypeVariables() {
        new C07743().visit(this.runtimeType);
        return this;
    }

    private boolean someRawTypeIsSubclassOf(Class<?> superclass) {
        Iterator i$ = getRawTypes().iterator();
        while (i$.hasNext()) {
            if (superclass.isAssignableFrom((Class) i$.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isSubtypeOfParameterizedType(ParameterizedType supertype) {
        Class<?> matchedClass = of((Type) supertype).getRawType();
        if (!someRawTypeIsSubclassOf(matchedClass)) {
            return false;
        }
        Type[] typeParams = matchedClass.getTypeParameters();
        Type[] toTypeArgs = supertype.getActualTypeArguments();
        for (int i = 0; i < typeParams.length; i++) {
            if (!resolveType(typeParams[i]).is(toTypeArgs[i])) {
                return false;
            }
        }
        boolean z = Modifier.isStatic(((Class) supertype.getRawType()).getModifiers()) || supertype.getOwnerType() == null || isOwnedBySubtypeOf(supertype.getOwnerType());
        return z;
    }

    private boolean isSubtypeOfArrayType(GenericArrayType supertype) {
        if (this.runtimeType instanceof Class) {
            Class<?> fromClass = this.runtimeType;
            if (fromClass.isArray()) {
                return of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
            }
            return false;
        } else if (this.runtimeType instanceof GenericArrayType) {
            return of(this.runtimeType.getGenericComponentType()).isSubtypeOf(supertype.getGenericComponentType());
        } else {
            return false;
        }
    }

    private boolean isSupertypeOfArray(GenericArrayType subtype) {
        if (this.runtimeType instanceof Class) {
            Class<?> thisClass = this.runtimeType;
            if (thisClass.isArray()) {
                return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
            }
            return thisClass.isAssignableFrom(Object[].class);
        } else if (this.runtimeType instanceof GenericArrayType) {
            return of(subtype.getGenericComponentType()).isSubtypeOf(((GenericArrayType) this.runtimeType).getGenericComponentType());
        } else {
            return false;
        }
    }

    private boolean is(Type formalType) {
        if (this.runtimeType.equals(formalType)) {
            return true;
        }
        if (!(formalType instanceof WildcardType)) {
            return false;
        }
        boolean z = every(((WildcardType) formalType).getUpperBounds()).isSupertypeOf(this.runtimeType) && every(((WildcardType) formalType).getLowerBounds()).isSubtypeOf(this.runtimeType);
        return z;
    }

    private static Bounds every(Type[] bounds) {
        return new Bounds(bounds, false);
    }

    private static Bounds any(Type[] bounds) {
        return new Bounds(bounds, true);
    }

    private ImmutableSet<Class<? super T>> getRawTypes() {
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        new TypeVisitor() {
            void visitTypeVariable(TypeVariable<?> t) {
                visit(t.getBounds());
            }

            void visitWildcardType(WildcardType t) {
                visit(t.getUpperBounds());
            }

            void visitParameterizedType(ParameterizedType t) {
                builder.add((Class) t.getRawType());
            }

            void visitClass(Class<?> t) {
                builder.add((Object) t);
            }

            void visitGenericArrayType(GenericArrayType t) {
                builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
            }
        }.visit(this.runtimeType);
        return builder.build();
    }

    private boolean isOwnedBySubtypeOf(Type supertype) {
        Iterator i$ = getTypes().iterator();
        while (i$.hasNext()) {
            Type ownerType = ((TypeToken) i$.next()).getOwnerTypeIfPresent();
            if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Type getOwnerTypeIfPresent() {
        if (this.runtimeType instanceof ParameterizedType) {
            return ((ParameterizedType) this.runtimeType).getOwnerType();
        }
        if (this.runtimeType instanceof Class) {
            return ((Class) this.runtimeType).getEnclosingClass();
        }
        return null;
    }

    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
        if (cls.isArray()) {
            return of(Types.newArrayType(toGenericType(cls.getComponentType()).runtimeType));
        }
        TypeVariable<Class<T>>[] typeParams = cls.getTypeParameters();
        Type ownerType = (!cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) ? null : toGenericType(cls.getEnclosingClass()).runtimeType;
        if (typeParams.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
            return of(Types.newParameterizedTypeWithOwner(ownerType, cls, typeParams));
        }
        return of((Class) cls);
    }

    private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
        for (Type upperBound : upperBounds) {
            TypeToken<? super T> bound = of(upperBound);
            if (bound.isSubtypeOf((Type) supertype)) {
                return bound.getSupertype(supertype);
            }
        }
        throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
    }

    private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
        Type[] arr$ = lowerBounds;
        if (0 < arr$.length) {
            return of(arr$[0]).getSubtype(subclass);
        }
        throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
    }

    private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
        return of(newArrayClassOrGenericArrayType(((TypeToken) Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", (Object) supertype, (Object) this)).getSupertype(supertype.getComponentType()).runtimeType));
    }

    private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
        return of(newArrayClassOrGenericArrayType(getComponentType().getSubtype(subclass.getComponentType()).runtimeType));
    }

    private Type resolveTypeArgsForSubclass(Class<?> subclass) {
        if ((this.runtimeType instanceof Class) && (subclass.getTypeParameters().length == 0 || getRawType().getTypeParameters().length != 0)) {
            return subclass;
        }
        TypeToken<?> genericSubtype = toGenericType(subclass);
        return new TypeResolver().where(genericSubtype.getSupertype(getRawType()).runtimeType, this.runtimeType).resolveType(genericSubtype.runtimeType);
    }

    private static Type newArrayClassOrGenericArrayType(Type componentType) {
        return JavaVersion.JAVA7.newArrayType(componentType);
    }
}
