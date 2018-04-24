package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class TypeResolver {
    private final TypeTable typeTable;

    private static final class TypeMappingIntrospector extends TypeVisitor {
        private static final WildcardCapturer wildcardCapturer = new WildcardCapturer();
        private final Map<TypeVariableKey, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(Type contextType) {
            TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.visit(wildcardCapturer.capture(contextType));
            return ImmutableMap.copyOf(introspector.mappings);
        }

        void visitClass(Class<?> clazz) {
            visit(clazz.getGenericSuperclass());
            visit(clazz.getGenericInterfaces());
        }

        void visitParameterizedType(ParameterizedType parameterizedType) {
            boolean z;
            TypeVariable<?>[] vars = ((Class) parameterizedType.getRawType()).getTypeParameters();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (vars.length == typeArgs.length) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z);
            for (int i = 0; i < vars.length; i++) {
                map(new TypeVariableKey(vars[i]), typeArgs[i]);
            }
            visit(rawClass);
            visit(parameterizedType.getOwnerType());
        }

        void visitTypeVariable(TypeVariable<?> t) {
            visit(t.getBounds());
        }

        void visitWildcardType(WildcardType t) {
            visit(t.getUpperBounds());
        }

        private void map(TypeVariableKey var, Type arg) {
            if (!this.mappings.containsKey(var)) {
                Type t = arg;
                while (t != null) {
                    if (var.equalsType(t)) {
                        Type x = arg;
                        while (x != null) {
                            x = (Type) this.mappings.remove(TypeVariableKey.forLookup(x));
                        }
                        return;
                    }
                    t = (Type) this.mappings.get(TypeVariableKey.forLookup(t));
                }
                this.mappings.put(var, arg);
            }
        }
    }

    private static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;

        TypeTable() {
            this.map = ImmutableMap.of();
        }

        private TypeTable(ImmutableMap<TypeVariableKey, Type> map) {
            this.map = map;
        }

        final TypeTable where(Map<TypeVariableKey, ? extends Type> mappings) {
            Builder<TypeVariableKey, Type> builder = ImmutableMap.builder();
            builder.putAll(this.map);
            for (Entry<TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
                Object variable = (TypeVariableKey) mapping.getKey();
                Type type = (Type) mapping.getValue();
                Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
                builder.put(variable, type);
            }
            return new TypeTable(builder.build());
        }

        final Type resolve(final TypeVariable<?> var) {
            final TypeTable unguarded = this;
            return resolveInternal(var, new TypeTable() {
                public Type resolveInternal(TypeVariable<?> intermediateVar, TypeTable forDependent) {
                    return intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration()) ? intermediateVar : unguarded.resolveInternal(intermediateVar, forDependent);
                }
            });
        }

        Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
            Type type = (Type) this.map.get(new TypeVariableKey(var));
            if (type != null) {
                return new TypeResolver(forDependants).resolveType(type);
            }
            Type[] bounds = var.getBounds();
            if (bounds.length == 0) {
                return var;
            }
            Type[] resolvedBounds = new TypeResolver(forDependants).resolveTypes(bounds);
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, resolvedBounds)) {
                return var;
            }
            return Types.newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
        }
    }

    static final class TypeVariableKey {
        private final TypeVariable<?> var;

        TypeVariableKey(TypeVariable<?> var) {
            this.var = (TypeVariable) Preconditions.checkNotNull(var);
        }

        public int hashCode() {
            return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
        }

        public boolean equals(Object obj) {
            if (obj instanceof TypeVariableKey) {
                return equalsTypeVariable(((TypeVariableKey) obj).var);
            }
            return false;
        }

        public String toString() {
            return this.var.toString();
        }

        static TypeVariableKey forLookup(Type t) {
            if (t instanceof TypeVariable) {
                return new TypeVariableKey((TypeVariable) t);
            }
            return null;
        }

        boolean equalsType(Type type) {
            if (type instanceof TypeVariable) {
                return equalsTypeVariable((TypeVariable) type);
            }
            return false;
        }

        private boolean equalsTypeVariable(TypeVariable<?> that) {
            return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
        }
    }

    private static final class WildcardCapturer {
        private final AtomicInteger id;

        private WildcardCapturer() {
            this.id = new AtomicInteger();
        }

        Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if ((type instanceof Class) || (type instanceof TypeVariable)) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                return Types.newArrayType(capture(((GenericArrayType) type).getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class) parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                if (wildcardType.getLowerBounds().length != 0) {
                    return type;
                }
                return Types.newArtificialTypeVariable(WildcardCapturer.class, "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(wildcardType.getUpperBounds()), wildcardType.getUpperBounds());
            } else {
                throw new AssertionError("must have been one of the known types");
            }
        }

        private Type captureNullable(@Nullable Type type) {
            if (type == null) {
                return null;
            }
            return capture(type);
        }

        private Type[] capture(Type[] types) {
            Type[] result = new Type[types.length];
            for (int i = 0; i < types.length; i++) {
                result[i] = capture(types[i]);
            }
            return result;
        }
    }

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    public TypeResolver where(Type formal, Type actual) {
        Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
        populateTypeMappings(mappings, (Type) Preconditions.checkNotNull(formal), (Type) Preconditions.checkNotNull(actual));
        return where(mappings);
    }

    TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
        return new TypeResolver(this.typeTable.where(mappings));
    }

    private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
        if (!from.equals(to)) {
            new TypeVisitor() {
                void visitTypeVariable(TypeVariable<?> typeVariable) {
                    mappings.put(new TypeVariableKey(typeVariable), to);
                }

                void visitWildcardType(WildcardType fromWildcardType) {
                    if (to instanceof WildcardType) {
                        int i;
                        WildcardType toWildcardType = to;
                        Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                        Type[] toUpperBounds = toWildcardType.getUpperBounds();
                        Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                        Type[] toLowerBounds = toWildcardType.getLowerBounds();
                        boolean z = fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length;
                        Preconditions.checkArgument(z, "Incompatible type: %s vs. %s", (Object) fromWildcardType, to);
                        for (i = 0; i < fromUpperBounds.length; i++) {
                            TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                        }
                        for (i = 0; i < fromLowerBounds.length; i++) {
                            TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
                        }
                    }
                }

                void visitParameterizedType(ParameterizedType fromParameterizedType) {
                    if (!(to instanceof WildcardType)) {
                        Object toParameterizedType = (ParameterizedType) TypeResolver.expectArgument(ParameterizedType.class, to);
                        if (!(fromParameterizedType.getOwnerType() == null || toParameterizedType.getOwnerType() == null)) {
                            TypeResolver.populateTypeMappings(mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
                        }
                        Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", (Object) fromParameterizedType, to);
                        Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                        Type[] toArgs = toParameterizedType.getActualTypeArguments();
                        Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", (Object) fromParameterizedType, toParameterizedType);
                        for (int i = 0; i < fromArgs.length; i++) {
                            TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                        }
                    }
                }

                void visitGenericArrayType(GenericArrayType fromArrayType) {
                    if (!(to instanceof WildcardType)) {
                        Type componentType = Types.getComponentType(to);
                        Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
                        TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
                    }
                }

                void visitClass(Class<?> fromClass) {
                    if (!(to instanceof WildcardType)) {
                        throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to);
                    }
                }
            }.visit(from);
        }
    }

    public Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        if (type instanceof WildcardType) {
            return resolveWildcardType((WildcardType) type);
        }
        return type;
    }

    private Type[] resolveTypes(Type[] types) {
        Type[] result = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            result[i] = resolveType(types[i]);
        }
        return result;
    }

    private WildcardType resolveWildcardType(WildcardType type) {
        return new WildcardTypeImpl(resolveTypes(type.getLowerBounds()), resolveTypes(type.getUpperBounds()));
    }

    private Type resolveGenericArrayType(GenericArrayType type) {
        return Types.newArrayType(resolveType(type.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType type) {
        Type owner = type.getOwnerType();
        return Types.newParameterizedTypeWithOwner(owner == null ? null : resolveType(owner), (Class) resolveType(type.getRawType()), resolveTypes(type.getActualTypeArguments()));
    }

    private static <T> T expectArgument(Class<T> type, Object arg) {
        try {
            return type.cast(arg);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
        }
    }
}
