package org.apache.poi.sl.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Rectangle2D;
import org.apache.poi.sl.usermodel.GroupShape;
import org.apache.poi.sl.usermodel.StrokeStyle;
import org.apache.poi.sl.usermodel.StrokeStyle.LineCompound;
import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TableShape;
import org.apache.poi.util.Internal;

public class DrawTableShape extends DrawShape {
    @Internal
    public static final int borderSize = 2;

    public DrawTableShape(TableShape<?, ?> shape) {
        super(shape);
    }

    protected Drawable getGroupShape(Graphics2D graphics) {
        if (this.shape instanceof GroupShape) {
            return DrawFactory.getInstance(graphics).getDrawable((GroupShape) this.shape);
        }
        return null;
    }

    public void applyTransform(Graphics2D graphics) {
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.applyTransform(graphics);
        } else {
            super.applyTransform(graphics);
        }
    }

    public void draw(Graphics2D graphics) {
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.draw(graphics);
            return;
        }
        TableShape<?, ?> ts = getShape();
        DrawPaint drawPaint = DrawFactory.getInstance(graphics).getPaint(ts);
        int rows = ts.getNumberOfRows();
        int cols = ts.getNumberOfColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TableCell<?, ?> tc = ts.getCell(row, col);
                if (!(tc == null || tc.isMerged())) {
                    graphics.setPaint(drawPaint.getPaint(graphics, tc.getFillStyle().getPaint()));
                    Rectangle2D cellAnc = tc.getAnchor();
                    graphics.fill(cellAnc);
                    for (BorderEdge edge : BorderEdge.values()) {
                        StrokeStyle stroke = tc.getBorderStyle(edge);
                        if (stroke != null) {
                            Line2D line;
                            graphics.setStroke(getStroke(stroke));
                            graphics.setPaint(drawPaint.getPaint(graphics, stroke.getPaint()));
                            double x = cellAnc.getX();
                            double y = cellAnc.getY();
                            double w = cellAnc.getWidth();
                            double h = cellAnc.getHeight();
                            switch (1.$SwitchMap$org$apache$poi$sl$usermodel$TableCell$BorderEdge[edge.ordinal()]) {
                                case 2:
                                    line = new Double(x, y, x, (y + h) + 2.0d);
                                    break;
                                case 3:
                                    line = new Double(x + w, y, x + w, 2.0d + (y + h));
                                    break;
                                case 4:
                                    line = new Double(x - 2.0d, y, 2.0d + (x + w), y);
                                    break;
                                default:
                                    line = new Double(x - 2.0d, y + h, (x + w) + 2.0d, y + h);
                                    break;
                            }
                            graphics.draw(line);
                        }
                    }
                }
            }
        }
        drawContent(graphics);
    }

    public void drawContent(Graphics2D graphics) {
        Drawable d = getGroupShape(graphics);
        if (d != null) {
            d.drawContent(graphics);
            return;
        }
        TableShape<?, ?> ts = getShape();
        DrawFactory df = DrawFactory.getInstance(graphics);
        int rows = ts.getNumberOfRows();
        int cols = ts.getNumberOfColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                df.getDrawable(ts.getCell(row, col)).drawContent(graphics);
            }
        }
    }

    protected TableShape<?, ?> getShape() {
        return (TableShape) this.shape;
    }

    public void setAllBorders(Object... args) {
        TableShape<?, ?> table = getShape();
        int rows = table.getNumberOfRows();
        int cols = table.getNumberOfColumns();
        BorderEdge[] edges = new BorderEdge[]{BorderEdge.top, BorderEdge.left, null, null};
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BorderEdge borderEdge;
                if (col == cols - 1) {
                    borderEdge = BorderEdge.right;
                } else {
                    borderEdge = null;
                }
                edges[2] = borderEdge;
                if (row == rows - 1) {
                    borderEdge = BorderEdge.bottom;
                } else {
                    borderEdge = null;
                }
                edges[3] = borderEdge;
                setEdges(table.getCell(row, col), edges, args);
            }
        }
    }

    public void setOutsideBorders(Object... args) {
        if (args.length != 0) {
            TableShape<?, ?> table = getShape();
            int rows = table.getNumberOfRows();
            int cols = table.getNumberOfColumns();
            BorderEdge[] edges = new BorderEdge[4];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    BorderEdge borderEdge;
                    if (col == 0) {
                        borderEdge = BorderEdge.left;
                    } else {
                        borderEdge = null;
                    }
                    edges[0] = borderEdge;
                    if (col == cols - 1) {
                        borderEdge = BorderEdge.right;
                    } else {
                        borderEdge = null;
                    }
                    edges[1] = borderEdge;
                    if (row == 0) {
                        borderEdge = BorderEdge.top;
                    } else {
                        borderEdge = null;
                    }
                    edges[2] = borderEdge;
                    if (row == rows - 1) {
                        borderEdge = BorderEdge.bottom;
                    } else {
                        borderEdge = null;
                    }
                    edges[3] = borderEdge;
                    setEdges(table.getCell(row, col), edges, args);
                }
            }
        }
    }

    public void setInsideBorders(Object... args) {
        if (args.length != 0) {
            TableShape<?, ?> table = getShape();
            int rows = table.getNumberOfRows();
            int cols = table.getNumberOfColumns();
            BorderEdge[] edges = new BorderEdge[2];
            int row = 0;
            while (row < rows) {
                int col = 0;
                while (col < cols) {
                    BorderEdge borderEdge;
                    if (col <= 0 || col >= cols - 1) {
                        borderEdge = null;
                    } else {
                        borderEdge = BorderEdge.right;
                    }
                    edges[0] = borderEdge;
                    if (row <= 0 || row >= rows - 1) {
                        borderEdge = null;
                    } else {
                        borderEdge = BorderEdge.bottom;
                    }
                    edges[1] = borderEdge;
                    setEdges(table.getCell(row, col), edges, args);
                    col++;
                }
                row++;
            }
        }
    }

    private static void setEdges(TableCell<?, ?> cell, BorderEdge[] edges, Object... args) {
        for (BorderEdge be : edges) {
            if (be != null) {
                if (args.length == 0) {
                    cell.removeBorder(be);
                } else {
                    for (Object o : args) {
                        if (o instanceof Double) {
                            cell.setBorderWidth(be, ((Double) o).doubleValue());
                        } else if (o instanceof Color) {
                            cell.setBorderColor(be, (Color) o);
                        } else if (o instanceof LineDash) {
                            cell.setBorderDash(be, (LineDash) o);
                        } else if (o instanceof LineCompound) {
                            cell.setBorderCompound(be, (LineCompound) o);
                        }
                    }
                }
            }
        }
    }
}
