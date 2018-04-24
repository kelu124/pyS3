package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;

@GwtIncompatible
@Beta
public abstract class LinearTransformation {

    public static final class LinearTransformationBuilder {
        private final double x1;
        private final double y1;

        private LinearTransformationBuilder(double x1, double y1) {
            this.x1 = x1;
            this.y1 = y1;
        }

        public LinearTransformation and(double x2, double y2) {
            boolean z = true;
            boolean z2 = DoubleUtils.isFinite(x2) && DoubleUtils.isFinite(y2);
            Preconditions.checkArgument(z2);
            if (x2 != this.x1) {
                return withSlope((y2 - this.y1) / (x2 - this.x1));
            }
            if (y2 == this.y1) {
                z = false;
            }
            Preconditions.checkArgument(z);
            return new VerticalLinearTransformation(this.x1);
        }

        public LinearTransformation withSlope(double slope) {
            Preconditions.checkArgument(!Double.isNaN(slope));
            if (DoubleUtils.isFinite(slope)) {
                return new RegularLinearTransformation(slope, this.y1 - (this.x1 * slope));
            }
            return new VerticalLinearTransformation(this.x1);
        }
    }

    private static final class NaNLinearTransformation extends LinearTransformation {
        static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();

        private NaNLinearTransformation() {
        }

        public boolean isVertical() {
            return false;
        }

        public boolean isHorizontal() {
            return false;
        }

        public double slope() {
            return Double.NaN;
        }

        public double transform(double x) {
            return Double.NaN;
        }

        public LinearTransformation inverse() {
            return this;
        }

        public String toString() {
            return "NaN";
        }
    }

    private static final class RegularLinearTransformation extends LinearTransformation {
        @LazyInit
        LinearTransformation inverse;
        final double slope;
        final double yIntercept;

        RegularLinearTransformation(double slope, double yIntercept) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = null;
        }

        RegularLinearTransformation(double slope, double yIntercept, LinearTransformation inverse) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = inverse;
        }

        public boolean isVertical() {
            return false;
        }

        public boolean isHorizontal() {
            return this.slope == 0.0d;
        }

        public double slope() {
            return this.slope;
        }

        public double transform(double x) {
            return (this.slope * x) + this.yIntercept;
        }

        public LinearTransformation inverse() {
            LinearTransformation linearTransformation = this.inverse;
            if (linearTransformation != null) {
                return linearTransformation;
            }
            linearTransformation = createInverse();
            this.inverse = linearTransformation;
            return linearTransformation;
        }

        public String toString() {
            return String.format("y = %g * x + %g", new Object[]{Double.valueOf(this.slope), Double.valueOf(this.yIntercept)});
        }

        private LinearTransformation createInverse() {
            if (this.slope != 0.0d) {
                return new RegularLinearTransformation(1.0d / this.slope, (-1.0d * this.yIntercept) / this.slope, this);
            }
            return new VerticalLinearTransformation(this.yIntercept, this);
        }
    }

    private static final class VerticalLinearTransformation extends LinearTransformation {
        @LazyInit
        LinearTransformation inverse;
        final double f31x;

        VerticalLinearTransformation(double x) {
            this.f31x = x;
            this.inverse = null;
        }

        VerticalLinearTransformation(double x, LinearTransformation inverse) {
            this.f31x = x;
            this.inverse = inverse;
        }

        public boolean isVertical() {
            return true;
        }

        public boolean isHorizontal() {
            return false;
        }

        public double slope() {
            throw new IllegalStateException();
        }

        public double transform(double x) {
            throw new IllegalStateException();
        }

        public LinearTransformation inverse() {
            LinearTransformation linearTransformation = this.inverse;
            if (linearTransformation != null) {
                return linearTransformation;
            }
            linearTransformation = createInverse();
            this.inverse = linearTransformation;
            return linearTransformation;
        }

        public String toString() {
            return String.format("x = %g", new Object[]{Double.valueOf(this.f31x)});
        }

        private LinearTransformation createInverse() {
            return new RegularLinearTransformation(0.0d, this.f31x, this);
        }
    }

    public abstract LinearTransformation inverse();

    public abstract boolean isHorizontal();

    public abstract boolean isVertical();

    public abstract double slope();

    public abstract double transform(double d);

    public static LinearTransformationBuilder mapping(double x1, double y1) {
        boolean z = DoubleUtils.isFinite(x1) && DoubleUtils.isFinite(y1);
        Preconditions.checkArgument(z);
        return new LinearTransformationBuilder(x1, y1);
    }

    public static LinearTransformation vertical(double x) {
        Preconditions.checkArgument(DoubleUtils.isFinite(x));
        return new VerticalLinearTransformation(x);
    }

    public static LinearTransformation horizontal(double y) {
        Preconditions.checkArgument(DoubleUtils.isFinite(y));
        return new RegularLinearTransformation(0.0d, y);
    }

    public static LinearTransformation forNaN() {
        return NaNLinearTransformation.INSTANCE;
    }
}
