package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
public final class Parameter implements AnnotatedElement {
    private final ImmutableList<Annotation> annotations;
    private final Invokable<?, ?> declaration;
    private final int position;
    private final TypeToken<?> type;

    Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations) {
        this.declaration = declaration;
        this.position = position;
        this.type = type;
        this.annotations = ImmutableList.copyOf((Object[]) annotations);
    }

    public TypeToken<?> getType() {
        return this.type;
    }

    public Invokable<?, ?> getDeclaringInvokable() {
        return this.declaration;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    @Nullable
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        Preconditions.checkNotNull(annotationType);
        Iterator i$ = this.annotations.iterator();
        while (i$.hasNext()) {
            Annotation annotation = (Annotation) i$.next();
            if (annotationType.isInstance(annotation)) {
                return (Annotation) annotationType.cast(annotation);
            }
        }
        return null;
    }

    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        return getDeclaredAnnotationsByType(annotationType);
    }

    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) this.annotations.toArray(new Annotation[this.annotations.size()]);
    }

    @Nullable
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
        Preconditions.checkNotNull(annotationType);
        return (Annotation) FluentIterable.from(this.annotations).filter((Class) annotationType).first().orNull();
    }

    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
        return (Annotation[]) FluentIterable.from(this.annotations).filter((Class) annotationType).toArray(annotationType);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Parameter)) {
            return false;
        }
        Parameter that = (Parameter) obj;
        if (this.position == that.position && this.declaration.equals(that.declaration)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.position;
    }

    public String toString() {
        return this.type + " arg" + this.position;
    }
}
