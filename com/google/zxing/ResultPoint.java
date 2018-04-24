package com.google.zxing;

import com.google.zxing.common.detector.MathUtils;

public class ResultPoint {
    private final float f68x;
    private final float f69y;

    public ResultPoint(float x, float y) {
        this.f68x = x;
        this.f69y = y;
    }

    public final float getX() {
        return this.f68x;
    }

    public final float getY() {
        return this.f69y;
    }

    public final boolean equals(Object other) {
        if (!(other instanceof ResultPoint)) {
            return false;
        }
        ResultPoint otherPoint = (ResultPoint) other;
        if (this.f68x == otherPoint.f68x && this.f69y == otherPoint.f69y) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return (Float.floatToIntBits(this.f68x) * 31) + Float.floatToIntBits(this.f69y);
    }

    public final String toString() {
        StringBuilder result = new StringBuilder(25);
        result.append('(');
        result.append(this.f68x);
        result.append(',');
        result.append(this.f69y);
        result.append(')');
        return result.toString();
    }

    public static void orderBestPatterns(ResultPoint[] patterns) {
        ResultPoint pointB;
        ResultPoint pointA;
        ResultPoint pointC;
        float zeroOneDistance = distance(patterns[0], patterns[1]);
        float oneTwoDistance = distance(patterns[1], patterns[2]);
        float zeroTwoDistance = distance(patterns[0], patterns[2]);
        if (oneTwoDistance >= zeroOneDistance && oneTwoDistance >= zeroTwoDistance) {
            pointB = patterns[0];
            pointA = patterns[1];
            pointC = patterns[2];
        } else if (zeroTwoDistance < oneTwoDistance || zeroTwoDistance < zeroOneDistance) {
            pointB = patterns[2];
            pointA = patterns[0];
            pointC = patterns[1];
        } else {
            pointB = patterns[1];
            pointA = patterns[0];
            pointC = patterns[2];
        }
        if (crossProductZ(pointA, pointB, pointC) < 0.0f) {
            ResultPoint temp = pointA;
            pointA = pointC;
            pointC = temp;
        }
        patterns[0] = pointA;
        patterns[1] = pointB;
        patterns[2] = pointC;
    }

    public static float distance(ResultPoint pattern1, ResultPoint pattern2) {
        return MathUtils.distance(pattern1.f68x, pattern1.f69y, pattern2.f68x, pattern2.f69y);
    }

    private static float crossProductZ(ResultPoint pointA, ResultPoint pointB, ResultPoint pointC) {
        float bX = pointB.f68x;
        float bY = pointB.f69y;
        return ((pointC.f68x - bX) * (pointA.f69y - bY)) - ((pointC.f69y - bY) * (pointA.f68x - bX));
    }
}
