package com.itextpdf.awt.geom.gl;

import com.itextpdf.awt.geom.PathIterator;
import com.itextpdf.awt.geom.Shape;

public class Crossing {
    public static final int CROSSING = 255;
    static final double DELTA = 1.0E-5d;
    static final double ROOT_DELTA = 1.0E-10d;
    static final int UNKNOWN = 254;

    public static class CubicCurve {
        double Ax = ((this.ax - this.Bx) - this.Cx);
        double Ax3 = ((this.Ax + this.Ax) + this.Ax);
        double Ay = ((this.ay - this.By) - this.Cy);
        double Bx = ((((this.cx + this.cx) + this.cx) - this.Cx) - this.Cx);
        double Bx2 = (this.Bx + this.Bx);
        double By = ((((this.cy + this.cy) + this.cy) - this.Cy) - this.Cy);
        double Cx = ((this.bx + this.bx) + this.bx);
        double Cy = ((this.by + this.by) + this.by);
        double ax;
        double ay;
        double bx;
        double by;
        double cx;
        double cy;

        public CubicCurve(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
            this.ax = x2 - x1;
            this.ay = y2 - y1;
            this.bx = cx1 - x1;
            this.by = cy1 - y1;
            this.cx = cx2 - x1;
            this.cy = cy2 - y1;
        }

        int cross(double[] res, int rc, double py1, double py2) {
            int cross = 0;
            for (int i = 0; i < rc; i++) {
                double t = res[i];
                if (t >= -1.0E-5d && t <= 1.00001d) {
                    double d;
                    if (t < Crossing.DELTA) {
                        if (py1 < 0.0d) {
                            d = this.bx != 0.0d ? this.bx : this.cx != this.bx ? this.cx - this.bx : this.ax - this.cx;
                            if (d < 0.0d) {
                                cross--;
                            }
                        }
                    } else if (t > 0.99999d) {
                        if (py1 < this.ay) {
                            d = this.ax != this.cx ? this.ax - this.cx : this.cx != this.bx ? this.cx - this.bx : this.bx;
                            if (d > 0.0d) {
                                cross++;
                            }
                        }
                    } else if (t * ((((this.Ay * t) + this.By) * t) + this.Cy) > py2) {
                        double rxt = (((this.Ax3 * t) + this.Bx2) * t) + this.Cx;
                        if (rxt > -1.0E-5d && rxt < Crossing.DELTA) {
                            rxt = ((this.Ax3 + this.Ax3) * t) + this.Bx2;
                            if (rxt >= -1.0E-5d && rxt <= Crossing.DELTA) {
                                rxt = this.ax;
                            }
                        }
                        cross += rxt > 0.0d ? 1 : -1;
                    }
                }
            }
            return cross;
        }

        int solvePoint(double[] res, double px) {
            return Crossing.solveCubic(new double[]{-px, this.Cx, this.Bx, this.Ax}, res);
        }

        int solveExtremX(double[] res) {
            return Crossing.solveQuad(new double[]{this.Cx, this.Bx2, this.Ax3}, res);
        }

        int solveExtremY(double[] res) {
            return Crossing.solveQuad(new double[]{this.Cy, this.By + this.By, (this.Ay + this.Ay) + this.Ay}, res);
        }

        int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
            int i = 0;
            int bc2 = bc;
            while (i < rc) {
                double t = res[i];
                if (t > -1.0E-5d && t < 1.00001d) {
                    double rx = t * ((((this.Ax * t) + this.Bx) * t) + this.Cx);
                    if (minX <= rx && rx <= maxX) {
                        bc = bc2 + 1;
                        bound[bc2] = t;
                        bc2 = bc + 1;
                        bound[bc] = rx;
                        bc = bc2 + 1;
                        bound[bc2] = ((((this.Ay * t) + this.By) * t) + this.Cy) * t;
                        bc2 = bc + 1;
                        bound[bc] = (double) id;
                        if (changeId) {
                            id++;
                            bc = bc2;
                            i++;
                            bc2 = bc;
                        }
                    }
                }
                bc = bc2;
                i++;
                bc2 = bc;
            }
            return bc2;
        }
    }

    public static class QuadCurve {
        double Ax = (this.ax - this.Bx);
        double Ay = (this.ay - this.By);
        double Bx = (this.bx + this.bx);
        double By = (this.by + this.by);
        double ax;
        double ay;
        double bx;
        double by;

        public QuadCurve(double x1, double y1, double cx, double cy, double x2, double y2) {
            this.ax = x2 - x1;
            this.ay = y2 - y1;
            this.bx = cx - x1;
            this.by = cy - y1;
        }

        int cross(double[] res, int rc, double py1, double py2) {
            int cross = 0;
            for (int i = 0; i < rc; i++) {
                double t = res[i];
                if (t >= -1.0E-5d && t <= 1.00001d) {
                    if (t < Crossing.DELTA) {
                        if (py1 < 0.0d) {
                            if ((this.bx != 0.0d ? this.bx : this.ax - this.bx) < 0.0d) {
                                cross--;
                            }
                        }
                    } else if (t > 0.99999d) {
                        if (py1 < this.ay) {
                            if ((this.ax != this.bx ? this.ax - this.bx : this.bx) > 0.0d) {
                                cross++;
                            }
                        }
                    } else if (t * ((this.Ay * t) + this.By) > py2) {
                        double rxt = (this.Ax * t) + this.bx;
                        if (rxt <= -1.0E-5d || rxt >= Crossing.DELTA) {
                            cross += rxt > 0.0d ? 1 : -1;
                        }
                    }
                }
            }
            return cross;
        }

        int solvePoint(double[] res, double px) {
            return Crossing.solveQuad(new double[]{-px, this.Bx, this.Ax}, res);
        }

        int solveExtrem(double[] res) {
            int rc = 0;
            if (this.Ax != 0.0d) {
                int rc2 = 0 + 1;
                res[0] = (-this.Bx) / (this.Ax + this.Ax);
                rc = rc2;
            }
            if (this.Ay == 0.0d) {
                return rc;
            }
            rc2 = rc + 1;
            res[rc] = (-this.By) / (this.Ay + this.Ay);
            return rc2;
        }

        int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
            int i = 0;
            int bc2 = bc;
            while (i < rc) {
                double t = res[i];
                if (t > -1.0E-5d && t < 1.00001d) {
                    double rx = t * ((this.Ax * t) + this.Bx);
                    if (minX <= rx && rx <= maxX) {
                        bc = bc2 + 1;
                        bound[bc2] = t;
                        bc2 = bc + 1;
                        bound[bc] = rx;
                        bc = bc2 + 1;
                        bound[bc2] = ((this.Ay * t) + this.By) * t;
                        bc2 = bc + 1;
                        bound[bc] = (double) id;
                        if (changeId) {
                            id++;
                            bc = bc2;
                            i++;
                            bc2 = bc;
                        }
                    }
                }
                bc = bc2;
                i++;
                bc2 = bc;
            }
            return bc2;
        }
    }

    public static int solveQuad(double[] eqn, double[] res) {
        int rc;
        double a = eqn[2];
        double b = eqn[1];
        double c = eqn[0];
        int rc2;
        if (a != 0.0d) {
            double d = (b * b) - ((4.0d * a) * c);
            if (d < 0.0d) {
                return 0;
            }
            d = Math.sqrt(d);
            rc2 = 0 + 1;
            res[0] = ((-b) + d) / (2.0d * a);
            if (d != 0.0d) {
                rc = rc2 + 1;
                res[rc2] = ((-b) - d) / (2.0d * a);
            } else {
                rc = rc2;
            }
        } else if (b == 0.0d) {
            return -1;
        } else {
            rc2 = 0 + 1;
            res[0] = (-c) / b;
            rc = rc2;
        }
        return fixRoots(res, rc);
    }

    public static int solveCubic(double[] eqn, double[] res) {
        double d = eqn[3];
        if (d == 0.0d) {
            return solveQuad(eqn, res);
        }
        int i;
        double a = eqn[2] / d;
        double b = eqn[1] / d;
        double Q = ((a * a) - (3.0d * b)) / 9.0d;
        double R = (((((2.0d * a) * a) * a) - ((9.0d * a) * b)) + (27.0d * (eqn[0] / d))) / 54.0d;
        double Q3 = (Q * Q) * Q;
        double R2 = R * R;
        double n = (-a) / 3.0d;
        int i2;
        if (R2 < Q3) {
            double t = Math.acos(R / Math.sqrt(Q3)) / 3.0d;
            double m = -2.0d * Math.sqrt(Q);
            i2 = 0 + 1;
            res[0] = (Math.cos(t) * m) + n;
            i = i2 + 1;
            res[i2] = (Math.cos(t + 2.0943951023931953d) * m) + n;
            i2 = i + 1;
            res[i] = (Math.cos(t - 2.0943951023931953d) * m) + n;
            i = i2;
        } else {
            double A = Math.pow(Math.abs(R) + Math.sqrt(R2 - Q3), 0.3333333333333333d);
            if (R > 0.0d) {
                A = -A;
            }
            if (-1.0E-10d >= A || A >= ROOT_DELTA) {
                double B = Q / A;
                i2 = 0 + 1;
                res[0] = (A + B) + n;
                double delta = R2 - Q3;
                if (-1.0E-10d >= delta || delta >= ROOT_DELTA) {
                    i = i2;
                } else {
                    i = i2 + 1;
                    res[i2] = ((-(A + B)) / 2.0d) + n;
                }
            } else {
                i2 = 0 + 1;
                res[0] = n;
                i = i2;
            }
        }
        return fixRoots(res, i);
    }

    static int fixRoots(double[] res, int rc) {
        int i = 0;
        int tc = 0;
        while (i < rc) {
            int tc2;
            for (int j = i + 1; j < rc; j++) {
                if (isZero(res[i] - res[j])) {
                    tc2 = tc;
                    break;
                }
            }
            tc2 = tc + 1;
            res[tc] = res[i];
            i++;
            tc = tc2;
        }
        return tc;
    }

    public static int crossLine(double x1, double y1, double x2, double y2, double x, double y) {
        if ((x < x1 && x < x2) || ((x > x1 && x > x2) || ((y > y1 && y > y2) || x1 == x2))) {
            return 0;
        }
        if ((y >= y1 || y >= y2) && ((y2 - y1) * (x - x1)) / (x2 - x1) <= y - y1) {
            return 0;
        }
        if (x != x1) {
            return x == x2 ? x1 < x2 ? 1 : 0 : x1 < x2 ? 1 : -1;
        } else {
            if (x1 < x2) {
                return 0;
            }
            return -1;
        }
    }

    public static int crossQuad(double x1, double y1, double cx, double cy, double x2, double y2, double x, double y) {
        if ((x < x1 && x < cx && x < x2) || ((x > x1 && x > cx && x > x2) || ((y > y1 && y > cy && y > y2) || (x1 == cx && cx == x2)))) {
            return 0;
        }
        if (y < y1 && y < cy && y < y2 && x != x1 && x != x2) {
            return x1 < x2 ? (x1 >= x || x >= x2) ? 0 : 1 : (x2 >= x || x >= x1) ? 0 : -1;
        } else {
            QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
            double py = y - y1;
            double[] res = new double[3];
            return c.cross(res, c.solvePoint(res, x - x1), py, py);
        }
    }

    public static int crossCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double x, double y) {
        if ((x < x1 && x < cx1 && x < cx2 && x < x2) || ((x > x1 && x > cx1 && x > cx2 && x > x2) || ((y > y1 && y > cy1 && y > cy2 && y > y2) || (x1 == cx1 && cx1 == cx2 && cx2 == x2)))) {
            return 0;
        }
        if (y < y1 && y < cy1 && y < cy2 && y < y2 && x != x1 && x != x2) {
            return x1 < x2 ? (x1 >= x || x >= x2) ? 0 : 1 : (x2 >= x || x >= x1) ? 0 : -1;
        } else {
            CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
            double py = y - y1;
            double[] res = new double[3];
            return c.cross(res, c.solvePoint(res, x - x1), py, py);
        }
    }

    public static int crossPath(PathIterator p, double x, double y) {
        int cross = 0;
        double cy = 0.0d;
        double cx = 0.0d;
        double my = 0.0d;
        double mx = 0.0d;
        double[] coords = new double[6];
        while (!p.isDone()) {
            double cx2;
            double cy2;
            double d;
            double d2;
            switch (p.currentSegment(coords)) {
                case 0:
                    if (!(cx == mx && cy == my)) {
                        cross += crossLine(cx, cy, mx, my, x, y);
                    }
                    cx = coords[0];
                    mx = cx;
                    cy = coords[1];
                    my = cy;
                    break;
                case 1:
                    cx2 = coords[0];
                    cy2 = coords[1];
                    cross += crossLine(cx, cy, cx2, cy2, x, y);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 2:
                    d = coords[0];
                    d2 = coords[1];
                    cx2 = coords[2];
                    cy2 = coords[3];
                    cross += crossQuad(cx, cy, d, d2, cx2, cy2, x, y);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 3:
                    d = coords[0];
                    d2 = coords[1];
                    double d3 = coords[2];
                    double d4 = coords[3];
                    cx2 = coords[4];
                    cy2 = coords[5];
                    cross += crossCubic(cx, cy, d, d2, d3, d4, cx2, cy2, x, y);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 4:
                    if (!(cy == my && cx == mx)) {
                        cross += crossLine(cx, cy, mx, my, x, y);
                        cy = my;
                        cx = mx;
                        break;
                    }
            }
            if (x == cx && y == cy) {
                cross = 0;
                cy = my;
                if (cy == my) {
                    return cross + crossLine(cx, cy, mx, my, x, y);
                }
                return cross;
            }
            p.next();
        }
        if (cy == my) {
            return cross;
        }
        return cross + crossLine(cx, cy, mx, my, x, y);
    }

    public static int crossShape(Shape s, double x, double y) {
        if (s.getBounds2D().contains(x, y)) {
            return crossPath(s.getPathIterator(null), x, y);
        }
        return 0;
    }

    public static boolean isZero(double val) {
        return -1.0E-5d < val && val < DELTA;
    }

    static void sortBound(double[] bound, int bc) {
        for (int i = 0; i < bc - 4; i += 4) {
            int k = i;
            for (int j = i + 4; j < bc; j += 4) {
                if (bound[k] > bound[j]) {
                    k = j;
                }
            }
            if (k != i) {
                double tmp = bound[i];
                bound[i] = bound[k];
                bound[k] = tmp;
                tmp = bound[i + 1];
                bound[i + 1] = bound[k + 1];
                bound[k + 1] = tmp;
                tmp = bound[i + 2];
                bound[i + 2] = bound[k + 2];
                bound[k + 2] = tmp;
                tmp = bound[i + 3];
                bound[i + 3] = bound[k + 3];
                bound[k + 3] = tmp;
            }
        }
    }

    static int crossBound(double[] bound, int bc, double py1, double py2) {
        if (bc == 0) {
            return 0;
        }
        int i;
        int up = 0;
        int down = 0;
        for (i = 2; i < bc; i += 4) {
            if (bound[i] < py1) {
                up++;
            } else if (bound[i] <= py2) {
                return 255;
            } else {
                down++;
            }
        }
        if (down == 0) {
            return 0;
        }
        if (up != 0) {
            sortBound(bound, bc);
            boolean sign = bound[2] > py2;
            i = 6;
            while (i < bc) {
                boolean sign2 = bound[i] > py2;
                if (sign != sign2 && bound[i + 1] != bound[i - 3]) {
                    return 255;
                }
                sign = sign2;
                i += 4;
            }
        }
        return 254;
    }

    public static int intersectLine(double x1, double y1, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        if ((rx2 < x1 && rx2 < x2) || ((rx1 > x1 && rx1 > x2) || (ry1 > y1 && ry1 > y2))) {
            return 0;
        }
        if (ry2 >= y1 || ry2 >= y2) {
            if (x1 == x2) {
                return 255;
            }
            double bx1;
            double bx2;
            if (x1 < x2) {
                if (x1 < rx1) {
                    bx1 = rx1;
                } else {
                    bx1 = x1;
                }
                if (x2 < rx2) {
                    bx2 = x2;
                } else {
                    bx2 = rx2;
                }
            } else {
                if (x2 < rx1) {
                    bx1 = rx1;
                } else {
                    bx1 = x2;
                }
                if (x1 < rx2) {
                    bx2 = x1;
                } else {
                    bx2 = rx2;
                }
            }
            double k = (y2 - y1) / (x2 - x1);
            double by1 = ((bx1 - x1) * k) + y1;
            double by2 = ((bx2 - x1) * k) + y1;
            if (by1 < ry1 && by2 < ry1) {
                return 0;
            }
            if (by1 <= ry2 || by2 <= ry2) {
                return 255;
            }
        }
        if (x1 == x2) {
            return 0;
        }
        return rx1 == x1 ? x1 < x2 ? 0 : -1 : rx1 == x2 ? x1 < x2 ? 1 : 0 : x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
    }

    public static int intersectQuad(double x1, double y1, double cx, double cy, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        if ((rx2 < x1 && rx2 < cx && rx2 < x2) || ((rx1 > x1 && rx1 > cx && rx1 > x2) || (ry1 > y1 && ry1 > cy && ry1 > y2))) {
            return 0;
        }
        if (ry2 < y1 && ry2 < cy && ry2 < y2 && rx1 != x1 && rx1 != x2) {
            return x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
        } else {
            QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
            double px1 = rx1 - x1;
            double py1 = ry1 - y1;
            double px2 = rx2 - x1;
            double py2 = ry2 - y1;
            double[] res1 = new double[3];
            double[] res2 = new double[3];
            int rc1 = c.solvePoint(res1, px1);
            int rc2 = c.solvePoint(res2, px2);
            if (rc1 == 0 && rc2 == 0) {
                return 0;
            }
            int i;
            double minX = px1 - DELTA;
            double maxX = px2 + DELTA;
            double[] bound = new double[28];
            int bc = c.addBound(bound, c.addBound(bound, c.addBound(bound, 0, res1, rc1, minX, maxX, false, 0), res2, rc2, minX, maxX, false, 1), res2, c.solveExtrem(res2), minX, maxX, true, 2);
            if (rx1 < x1 && x1 < rx2) {
                i = bc + 1;
                bound[bc] = 0.0d;
                bc = i + 1;
                bound[i] = 0.0d;
                i = bc + 1;
                bound[bc] = 0.0d;
                bc = i + 1;
                bound[i] = 4.0d;
            }
            i = bc;
            if (rx1 < x2 && x2 < rx2) {
                bc = i + 1;
                bound[i] = 1.0d;
                i = bc + 1;
                bound[bc] = c.ax;
                bc = i + 1;
                bound[i] = c.ay;
                i = bc + 1;
                bound[bc] = 5.0d;
            }
            int cross = crossBound(bound, i, py1, py2);
            if (cross != 254) {
                return cross;
            }
            return c.cross(res1, rc1, py1, py2);
        }
    }

    public static int intersectCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
        if ((rx2 < x1 && rx2 < cx1 && rx2 < cx2 && rx2 < x2) || ((rx1 > x1 && rx1 > cx1 && rx1 > cx2 && rx1 > x2) || (ry1 > y1 && ry1 > cy1 && ry1 > cy2 && ry1 > y2))) {
            return 0;
        }
        if (ry2 < y1 && ry2 < cy1 && ry2 < cy2 && ry2 < y2 && rx1 != x1 && rx1 != x2) {
            return x1 < x2 ? (x1 >= rx1 || rx1 >= x2) ? 0 : 1 : (x2 >= rx1 || rx1 >= x1) ? 0 : -1;
        } else {
            CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
            double px1 = rx1 - x1;
            double py1 = ry1 - y1;
            double px2 = rx2 - x1;
            double py2 = ry2 - y1;
            double[] res1 = new double[3];
            double[] res2 = new double[3];
            int rc1 = c.solvePoint(res1, px1);
            int rc2 = c.solvePoint(res2, px2);
            if (rc1 == 0 && rc2 == 0) {
                return 0;
            }
            int i;
            double minX = px1 - DELTA;
            double maxX = px2 + DELTA;
            double[] bound = new double[40];
            int bc = c.addBound(bound, c.addBound(bound, c.addBound(bound, c.addBound(bound, 0, res1, rc1, minX, maxX, false, 0), res2, rc2, minX, maxX, false, 1), res2, c.solveExtremX(res2), minX, maxX, true, 2), res2, c.solveExtremY(res2), minX, maxX, true, 4);
            if (rx1 < x1 && x1 < rx2) {
                i = bc + 1;
                bound[bc] = 0.0d;
                bc = i + 1;
                bound[i] = 0.0d;
                i = bc + 1;
                bound[bc] = 0.0d;
                bc = i + 1;
                bound[i] = 6.0d;
            }
            i = bc;
            if (rx1 < x2 && x2 < rx2) {
                bc = i + 1;
                bound[i] = 1.0d;
                i = bc + 1;
                bound[bc] = c.ax;
                bc = i + 1;
                bound[i] = c.ay;
                i = bc + 1;
                bound[bc] = 7.0d;
            }
            int cross = crossBound(bound, i, py1, py2);
            if (cross != 254) {
                return cross;
            }
            return c.cross(res1, rc1, py1, py2);
        }
    }

    public static int intersectPath(PathIterator p, double x, double y, double w, double h) {
        int cross = 0;
        double cy = 0.0d;
        double cx = 0.0d;
        double my = 0.0d;
        double mx = 0.0d;
        double[] coords = new double[6];
        double rx1 = x;
        double ry1 = y;
        double rx2 = x + w;
        double ry2 = y + h;
        while (!p.isDone()) {
            int count = 0;
            double cx2;
            double cy2;
            double d;
            double d2;
            switch (p.currentSegment(coords)) {
                case 0:
                    if (!(cx == mx && cy == my)) {
                        count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
                    }
                    cx = coords[0];
                    mx = cx;
                    cy = coords[1];
                    my = cy;
                    break;
                case 1:
                    cx2 = coords[0];
                    cy2 = coords[1];
                    count = intersectLine(cx, cy, cx2, cy2, rx1, ry1, rx2, ry2);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 2:
                    d = coords[0];
                    d2 = coords[1];
                    cx2 = coords[2];
                    cy2 = coords[3];
                    count = intersectQuad(cx, cy, d, d2, cx2, cy2, rx1, ry1, rx2, ry2);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 3:
                    d = coords[0];
                    d2 = coords[1];
                    double d3 = coords[2];
                    double d4 = coords[3];
                    cx2 = coords[4];
                    cy2 = coords[5];
                    count = intersectCubic(cx, cy, d, d2, d3, d4, cx2, cy2, rx1, ry1, rx2, ry2);
                    cy = cy2;
                    cx = cx2;
                    break;
                case 4:
                    if (!(cy == my && cx == mx)) {
                        count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
                    }
                    cx = mx;
                    cy = my;
                    break;
            }
            if (count == 255) {
                return 255;
            }
            cross += count;
            p.next();
        }
        if (cy != my) {
            count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
            if (count == 255) {
                return 255;
            }
            cross += count;
        }
        return cross;
    }

    public static int intersectShape(Shape s, double x, double y, double w, double h) {
        if (s.getBounds2D().intersects(x, y, w, h)) {
            return intersectPath(s.getPathIterator(null), x, y, w, h);
        }
        return 0;
    }

    public static boolean isInsideNonZero(int cross) {
        return cross != 0;
    }

    public static boolean isInsideEvenOdd(int cross) {
        return (cross & 1) != 0;
    }
}
