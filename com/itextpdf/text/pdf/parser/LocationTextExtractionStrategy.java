package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.BaseField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationTextExtractionStrategy implements TextExtractionStrategy {
    static boolean DUMP_STATE = false;
    private final List<TextChunk> locationalResult = new ArrayList();

    public static class TextChunk implements Comparable<TextChunk> {
        private final float charSpaceWidth;
        private final float distParallelEnd;
        private final float distParallelStart;
        private final int distPerpendicular;
        private final Vector endLocation;
        private final int orientationMagnitude;
        private final Vector orientationVector;
        private final Vector startLocation;
        private final String text;

        public TextChunk(String string, Vector startLocation, Vector endLocation, float charSpaceWidth) {
            this.text = string;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.charSpaceWidth = charSpaceWidth;
            Vector oVector = endLocation.subtract(startLocation);
            if (oVector.length() == 0.0f) {
                oVector = new Vector(BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f);
            }
            this.orientationVector = oVector.normalize();
            this.orientationMagnitude = (int) (Math.atan2((double) this.orientationVector.get(1), (double) this.orientationVector.get(0)) * 1000.0d);
            this.distPerpendicular = (int) startLocation.subtract(new Vector(0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN)).cross(this.orientationVector).get(2);
            this.distParallelStart = this.orientationVector.dot(startLocation);
            this.distParallelEnd = this.orientationVector.dot(endLocation);
        }

        public Vector getStartLocation() {
            return this.startLocation;
        }

        public Vector getEndLocation() {
            return this.endLocation;
        }

        public String getText() {
            return this.text;
        }

        public float getCharSpaceWidth() {
            return this.charSpaceWidth;
        }

        private void printDiagnostics() {
            System.out.println("Text (@" + this.startLocation + " -> " + this.endLocation + "): " + this.text);
            System.out.println("orientationMagnitude: " + this.orientationMagnitude);
            System.out.println("distPerpendicular: " + this.distPerpendicular);
            System.out.println("distParallel: " + this.distParallelStart);
        }

        public boolean sameLine(TextChunk as) {
            if (this.orientationMagnitude == as.orientationMagnitude && this.distPerpendicular == as.distPerpendicular) {
                return true;
            }
            return false;
        }

        public float distanceFromEndOf(TextChunk other) {
            return this.distParallelStart - other.distParallelEnd;
        }

        public int compareTo(TextChunk rhs) {
            if (this == rhs) {
                return 0;
            }
            int rslt = compareInts(this.orientationMagnitude, rhs.orientationMagnitude);
            if (rslt != 0) {
                return rslt;
            }
            rslt = compareInts(this.distPerpendicular, rhs.distPerpendicular);
            return rslt == 0 ? Float.compare(this.distParallelStart, rhs.distParallelStart) : rslt;
        }

        private static int compareInts(int int1, int int2) {
            if (int1 == int2) {
                return 0;
            }
            return int1 < int2 ? -1 : 1;
        }
    }

    public interface TextChunkFilter {
        boolean accept(TextChunk textChunk);
    }

    public void beginTextBlock() {
    }

    public void endTextBlock() {
    }

    private boolean startsWithSpace(String str) {
        if (str.length() != 0 && str.charAt(0) == ' ') {
            return true;
        }
        return false;
    }

    private boolean endsWithSpace(String str) {
        if (str.length() != 0 && str.charAt(str.length() - 1) == ' ') {
            return true;
        }
        return false;
    }

    private List<TextChunk> filterTextChunks(List<TextChunk> textChunks, TextChunkFilter filter) {
        if (filter == null) {
            return textChunks;
        }
        List<TextChunk> filtered = new ArrayList();
        for (TextChunk textChunk : textChunks) {
            if (filter.accept(textChunk)) {
                filtered.add(textChunk);
            }
        }
        return filtered;
    }

    protected boolean isChunkAtWordBoundary(TextChunk chunk, TextChunk previousChunk) {
        float dist = chunk.distanceFromEndOf(previousChunk);
        if (dist < (-chunk.getCharSpaceWidth()) || dist > chunk.getCharSpaceWidth() / BaseField.BORDER_WIDTH_MEDIUM) {
            return true;
        }
        return false;
    }

    public String getResultantText(TextChunkFilter chunkFilter) {
        if (DUMP_STATE) {
            dumpState();
        }
        List<TextChunk> filteredTextChunks = filterTextChunks(this.locationalResult, chunkFilter);
        Collections.sort(filteredTextChunks);
        StringBuffer sb = new StringBuffer();
        TextChunk lastChunk = null;
        for (TextChunk chunk : filteredTextChunks) {
            if (lastChunk == null) {
                sb.append(chunk.text);
            } else if (chunk.sameLine(lastChunk)) {
                if (!(!isChunkAtWordBoundary(chunk, lastChunk) || startsWithSpace(chunk.text) || endsWithSpace(lastChunk.text))) {
                    sb.append(' ');
                }
                sb.append(chunk.text);
            } else {
                sb.append('\n');
                sb.append(chunk.text);
            }
            lastChunk = chunk;
        }
        return sb.toString();
    }

    public String getResultantText() {
        return getResultantText(null);
    }

    private void dumpState() {
        for (TextChunk location : this.locationalResult) {
            location.printDiagnostics();
            System.out.println();
        }
    }

    public void renderText(TextRenderInfo renderInfo) {
        LineSegment segment = renderInfo.getBaseline();
        if (renderInfo.getRise() != 0.0f) {
            segment = segment.transformBy(new Matrix(0.0f, -renderInfo.getRise()));
        }
        this.locationalResult.add(new TextChunk(renderInfo.getText(), segment.getStartPoint(), segment.getEndPoint(), renderInfo.getSingleSpaceWidth()));
    }

    public void renderImage(ImageRenderInfo renderInfo) {
    }
}
