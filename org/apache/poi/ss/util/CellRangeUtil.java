package org.apache.poi.ss.util;

import java.util.ArrayList;
import java.util.List;

public final class CellRangeUtil {
    public static final int ENCLOSES = 4;
    public static final int INSIDE = 3;
    public static final int NO_INTERSECTION = 1;
    public static final int OVERLAP = 2;

    private CellRangeUtil() {
    }

    public static int intersect(CellRangeAddress crA, CellRangeAddress crB) {
        int firstRow = crB.getFirstRow();
        int lastRow = crB.getLastRow();
        int firstCol = crB.getFirstColumn();
        int lastCol = crB.getLastColumn();
        if (gt(crA.getFirstRow(), lastRow) || lt(crA.getLastRow(), firstRow) || gt(crA.getFirstColumn(), lastCol) || lt(crA.getLastColumn(), firstCol)) {
            return 1;
        }
        if (contains(crA, crB)) {
            return 3;
        }
        if (contains(crB, crA)) {
            return 4;
        }
        return 2;
    }

    public static CellRangeAddress[] mergeCellRanges(CellRangeAddress[] cellRanges) {
        if (cellRanges.length < 1) {
            return new CellRangeAddress[0];
        }
        return toArray(mergeCellRanges(toList(cellRanges)));
    }

    private static List<CellRangeAddress> mergeCellRanges(List<CellRangeAddress> cellRangeList) {
        while (cellRangeList.size() > 1) {
            boolean somethingGotMerged = false;
            for (int i = 0; i < cellRangeList.size(); i++) {
                CellRangeAddress range1 = (CellRangeAddress) cellRangeList.get(i);
                int j = i + 1;
                while (j < cellRangeList.size()) {
                    CellRangeAddress[] mergeResult = mergeRanges(range1, (CellRangeAddress) cellRangeList.get(j));
                    if (mergeResult != null) {
                        somethingGotMerged = true;
                        cellRangeList.set(i, mergeResult[0]);
                        int j2 = j - 1;
                        cellRangeList.remove(j);
                        j = j2;
                        for (int k = 1; k < mergeResult.length; k++) {
                            j++;
                            cellRangeList.add(j, mergeResult[k]);
                        }
                    }
                    j++;
                }
            }
            if (!somethingGotMerged) {
                break;
            }
        }
        return cellRangeList;
    }

    private static CellRangeAddress[] mergeRanges(CellRangeAddress range1, CellRangeAddress range2) {
        int x = intersect(range1, range2);
        switch (x) {
            case 1:
                if (!hasExactSharedBorder(range1, range2)) {
                    return null;
                }
                return new CellRangeAddress[]{createEnclosingCellRange(range1, range2)};
            case 2:
                return null;
            case 3:
                return new CellRangeAddress[]{range1};
            case 4:
                return new CellRangeAddress[]{range2};
            default:
                throw new RuntimeException("unexpected intersection result (" + x + ")");
        }
    }

    private static CellRangeAddress[] toArray(List<CellRangeAddress> temp) {
        CellRangeAddress[] result = new CellRangeAddress[temp.size()];
        temp.toArray(result);
        return result;
    }

    private static List<CellRangeAddress> toList(CellRangeAddress[] temp) {
        List<CellRangeAddress> result = new ArrayList(temp.length);
        for (CellRangeAddress range : temp) {
            result.add(range);
        }
        return result;
    }

    public static boolean contains(CellRangeAddress crA, CellRangeAddress crB) {
        return le(crA.getFirstRow(), crB.getFirstRow()) && ge(crA.getLastRow(), crB.getLastRow()) && le(crA.getFirstColumn(), crB.getFirstColumn()) && ge(crA.getLastColumn(), crB.getLastColumn());
    }

    public static boolean hasExactSharedBorder(CellRangeAddress crA, CellRangeAddress crB) {
        int oFirstRow = crB.getFirstRow();
        int oLastRow = crB.getLastRow();
        int oFirstCol = crB.getFirstColumn();
        int oLastCol = crB.getLastColumn();
        if ((crA.getFirstRow() <= 0 || crA.getFirstRow() - 1 != oLastRow) && (oFirstRow <= 0 || oFirstRow - 1 != crA.getLastRow())) {
            if ((crA.getFirstColumn() <= 0 || crA.getFirstColumn() - 1 != oLastCol) && (oFirstCol <= 0 || crA.getLastColumn() != oFirstCol - 1)) {
                return false;
            }
            if (crA.getFirstRow() == oFirstRow && crA.getLastRow() == oLastRow) {
                return true;
            }
            return false;
        } else if (crA.getFirstColumn() == oFirstCol && crA.getLastColumn() == oLastCol) {
            return true;
        } else {
            return false;
        }
    }

    public static CellRangeAddress createEnclosingCellRange(CellRangeAddress crA, CellRangeAddress crB) {
        if (crB == null) {
            return crA.copy();
        }
        return new CellRangeAddress(lt(crB.getFirstRow(), crA.getFirstRow()) ? crB.getFirstRow() : crA.getFirstRow(), gt(crB.getLastRow(), crA.getLastRow()) ? crB.getLastRow() : crA.getLastRow(), lt(crB.getFirstColumn(), crA.getFirstColumn()) ? crB.getFirstColumn() : crA.getFirstColumn(), gt(crB.getLastColumn(), crA.getLastColumn()) ? crB.getLastColumn() : crA.getLastColumn());
    }

    private static boolean lt(int a, int b) {
        if (a == -1) {
            return false;
        }
        if (b == -1) {
            return true;
        }
        return a < b;
    }

    private static boolean le(int a, int b) {
        return a == b || lt(a, b);
    }

    private static boolean gt(int a, int b) {
        return lt(b, a);
    }

    private static boolean ge(int a, int b) {
        return !lt(a, b);
    }
}
