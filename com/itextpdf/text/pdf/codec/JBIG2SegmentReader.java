package com.itextpdf.text.pdf.codec;

import com.google.common.base.Ascii;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class JBIG2SegmentReader {
    public static final int END_OF_FILE = 51;
    public static final int END_OF_PAGE = 49;
    public static final int END_OF_STRIPE = 50;
    public static final int EXTENSION = 62;
    public static final int IMMEDIATE_GENERIC_REFINEMENT_REGION = 42;
    public static final int IMMEDIATE_GENERIC_REGION = 38;
    public static final int IMMEDIATE_HALFTONE_REGION = 22;
    public static final int IMMEDIATE_LOSSLESS_GENERIC_REFINEMENT_REGION = 43;
    public static final int IMMEDIATE_LOSSLESS_GENERIC_REGION = 39;
    public static final int IMMEDIATE_LOSSLESS_HALFTONE_REGION = 23;
    public static final int IMMEDIATE_LOSSLESS_TEXT_REGION = 7;
    public static final int IMMEDIATE_TEXT_REGION = 6;
    public static final int INTERMEDIATE_GENERIC_REFINEMENT_REGION = 40;
    public static final int INTERMEDIATE_GENERIC_REGION = 36;
    public static final int INTERMEDIATE_HALFTONE_REGION = 20;
    public static final int INTERMEDIATE_TEXT_REGION = 4;
    public static final int PAGE_INFORMATION = 48;
    public static final int PATTERN_DICTIONARY = 16;
    public static final int PROFILES = 52;
    public static final int SYMBOL_DICTIONARY = 0;
    public static final int TABLES = 53;
    private final SortedSet<JBIG2Segment> globals = new TreeSet();
    private int number_of_pages = -1;
    private boolean number_of_pages_known;
    private final SortedMap<Integer, JBIG2Page> pages = new TreeMap();
    private RandomAccessFileOrArray ra;
    private boolean read = false;
    private final SortedMap<Integer, JBIG2Segment> segments = new TreeMap();
    private boolean sequential;

    public static class JBIG2Page {
        public final int page;
        public int pageBitmapHeight = -1;
        public int pageBitmapWidth = -1;
        private final SortedMap<Integer, JBIG2Segment> segs = new TreeMap();
        private final JBIG2SegmentReader sr;

        public JBIG2Page(int page, JBIG2SegmentReader sr) {
            this.page = page;
            this.sr = sr;
        }

        public byte[] getData(boolean for_embedding) throws IOException {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (Integer sn : this.segs.keySet()) {
                JBIG2Segment s = (JBIG2Segment) this.segs.get(sn);
                if (!(for_embedding && (s.type == 51 || s.type == 49))) {
                    if (for_embedding) {
                        byte[] headerData_emb = JBIG2SegmentReader.copyByteArray(s.headerData);
                        if (s.page_association_size) {
                            headerData_emb[s.page_association_offset] = (byte) 0;
                            headerData_emb[s.page_association_offset + 1] = (byte) 0;
                            headerData_emb[s.page_association_offset + 2] = (byte) 0;
                            headerData_emb[s.page_association_offset + 3] = (byte) 1;
                        } else {
                            headerData_emb[s.page_association_offset] = (byte) 1;
                        }
                        os.write(headerData_emb);
                    } else {
                        os.write(s.headerData);
                    }
                    os.write(s.data);
                }
            }
            os.close();
            return os.toByteArray();
        }

        public void addSegment(JBIG2Segment s) {
            this.segs.put(Integer.valueOf(s.segmentNumber), s);
        }
    }

    public static class JBIG2Segment implements Comparable<JBIG2Segment> {
        public int countOfReferredToSegments = -1;
        public byte[] data = null;
        public long dataLength = -1;
        public boolean deferredNonRetain = false;
        public byte[] headerData = null;
        public int page = -1;
        public int page_association_offset = -1;
        public boolean page_association_size = false;
        public int[] referredToSegmentNumbers = null;
        public final int segmentNumber;
        public boolean[] segmentRetentionFlags = null;
        public int type = -1;

        public JBIG2Segment(int segment_number) {
            this.segmentNumber = segment_number;
        }

        public int compareTo(JBIG2Segment s) {
            return this.segmentNumber - s.segmentNumber;
        }
    }

    public JBIG2SegmentReader(RandomAccessFileOrArray ra) throws IOException {
        this.ra = ra;
    }

    public static byte[] copyByteArray(byte[] b) {
        byte[] bc = new byte[b.length];
        System.arraycopy(b, 0, bc, 0, b.length);
        return bc;
    }

    public void read() throws IOException {
        if (this.read) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("already.attempted.a.read.on.this.jbig2.file", new Object[0]));
        }
        this.read = true;
        readFileHeader();
        if (this.sequential) {
            do {
                JBIG2Segment tmp = readHeader();
                readSegment(tmp);
                this.segments.put(Integer.valueOf(tmp.segmentNumber), tmp);
            } while (this.ra.getFilePointer() < this.ra.length());
            return;
        }
        do {
            tmp = readHeader();
            this.segments.put(Integer.valueOf(tmp.segmentNumber), tmp);
        } while (tmp.type != 51);
        for (Object obj : this.segments.keySet()) {
            readSegment((JBIG2Segment) this.segments.get(obj));
        }
    }

    void readSegment(JBIG2Segment s) throws IOException {
        int ptr = (int) this.ra.getFilePointer();
        if (s.dataLength != 4294967295L) {
            byte[] data = new byte[((int) s.dataLength)];
            this.ra.read(data);
            s.data = data;
            if (s.type == 48) {
                int last = (int) this.ra.getFilePointer();
                this.ra.seek((long) ptr);
                int page_bitmap_width = this.ra.readInt();
                int page_bitmap_height = this.ra.readInt();
                this.ra.seek((long) last);
                JBIG2Page p = (JBIG2Page) this.pages.get(Integer.valueOf(s.page));
                if (p == null) {
                    throw new IllegalStateException(MessageLocalization.getComposedMessage("referring.to.widht.height.of.page.we.havent.seen.yet.1", s.page));
                }
                p.pageBitmapWidth = page_bitmap_width;
                p.pageBitmapHeight = page_bitmap_height;
            }
        }
    }

    JBIG2Segment readHeader() throws IOException {
        int i;
        int segment_page_association;
        int ptr = (int) this.ra.getFilePointer();
        int segment_number = this.ra.readInt();
        JBIG2Segment jBIG2Segment = new JBIG2Segment(segment_number);
        int segment_header_flags = this.ra.read();
        jBIG2Segment.deferredNonRetain = (segment_header_flags & 128) == 128;
        boolean page_association_size = (segment_header_flags & 64) == 64;
        jBIG2Segment.type = segment_header_flags & 63;
        int referred_to_byte0 = this.ra.read();
        int count_of_referred_to_segments = (referred_to_byte0 & 224) >> 5;
        boolean[] segment_retention_flags = null;
        if (count_of_referred_to_segments == 7) {
            this.ra.seek(this.ra.getFilePointer() - 1);
            count_of_referred_to_segments = this.ra.readInt() & 536870911;
            segment_retention_flags = new boolean[(count_of_referred_to_segments + 1)];
            i = 0;
            int referred_to_current_byte = 0;
            do {
                int j = i % 8;
                if (j == 0) {
                    referred_to_current_byte = this.ra.read();
                }
                segment_retention_flags[i] = (((1 << j) & referred_to_current_byte) >> j) == 1;
                i++;
            } while (i <= count_of_referred_to_segments);
        } else if (count_of_referred_to_segments <= 4) {
            segment_retention_flags = new boolean[(count_of_referred_to_segments + 1)];
            referred_to_byte0 &= 31;
            for (i = 0; i <= count_of_referred_to_segments; i++) {
                segment_retention_flags[i] = (((1 << i) & referred_to_byte0) >> i) == 1;
            }
        } else if (count_of_referred_to_segments == 5 || count_of_referred_to_segments == 6) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("count.of.referred.to.segments.had.bad.value.in.header.for.segment.1.starting.at.2", String.valueOf(segment_number), String.valueOf(ptr)));
        }
        jBIG2Segment.segmentRetentionFlags = segment_retention_flags;
        jBIG2Segment.countOfReferredToSegments = count_of_referred_to_segments;
        int[] referred_to_segment_numbers = new int[(count_of_referred_to_segments + 1)];
        for (i = 1; i <= count_of_referred_to_segments; i++) {
            if (segment_number <= 256) {
                referred_to_segment_numbers[i] = this.ra.read();
            } else if (segment_number <= 65536) {
                referred_to_segment_numbers[i] = this.ra.readUnsignedShort();
            } else {
                referred_to_segment_numbers[i] = (int) this.ra.readUnsignedInt();
            }
        }
        jBIG2Segment.referredToSegmentNumbers = referred_to_segment_numbers;
        int page_association_offset = ((int) this.ra.getFilePointer()) - ptr;
        if (page_association_size) {
            segment_page_association = this.ra.readInt();
        } else {
            segment_page_association = this.ra.read();
        }
        if (segment_page_association < 0) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("page.1.invalid.for.segment.2.starting.at.3", String.valueOf(segment_page_association), String.valueOf(segment_number), String.valueOf(ptr)));
        }
        jBIG2Segment.page = segment_page_association;
        jBIG2Segment.page_association_size = page_association_size;
        jBIG2Segment.page_association_offset = page_association_offset;
        if (segment_page_association > 0 && !this.pages.containsKey(Integer.valueOf(segment_page_association))) {
            this.pages.put(Integer.valueOf(segment_page_association), new JBIG2Page(segment_page_association, this));
        }
        if (segment_page_association > 0) {
            ((JBIG2Page) this.pages.get(Integer.valueOf(segment_page_association))).addSegment(jBIG2Segment);
        } else {
            this.globals.add(jBIG2Segment);
        }
        jBIG2Segment.dataLength = this.ra.readUnsignedInt();
        int end_ptr = (int) this.ra.getFilePointer();
        this.ra.seek((long) ptr);
        byte[] header_data = new byte[(end_ptr - ptr)];
        this.ra.read(header_data);
        jBIG2Segment.headerData = header_data;
        return jBIG2Segment;
    }

    void readFileHeader() throws IOException {
        boolean z;
        boolean z2 = true;
        this.ra.seek(0);
        byte[] idstring = new byte[8];
        this.ra.read(idstring);
        byte[] refidstring = new byte[]{(byte) -105, (byte) 74, (byte) 66, (byte) 50, (byte) 13, (byte) 10, Ascii.SUB, (byte) 10};
        for (int i = 0; i < idstring.length; i++) {
            if (idstring[i] != refidstring[i]) {
                throw new IllegalStateException(MessageLocalization.getComposedMessage("file.header.idstring.not.good.at.byte.1", i));
            }
        }
        int fileheaderflags = this.ra.read();
        if ((fileheaderflags & 1) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.sequential = z;
        if ((fileheaderflags & 2) != 0) {
            z2 = false;
        }
        this.number_of_pages_known = z2;
        if ((fileheaderflags & 252) != 0) {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("file.header.flags.bits.2.7.not.0", new Object[0]));
        } else if (this.number_of_pages_known) {
            this.number_of_pages = this.ra.readInt();
        }
    }

    public int numberOfPages() {
        return this.pages.size();
    }

    public int getPageHeight(int i) {
        return ((JBIG2Page) this.pages.get(Integer.valueOf(i))).pageBitmapHeight;
    }

    public int getPageWidth(int i) {
        return ((JBIG2Page) this.pages.get(Integer.valueOf(i))).pageBitmapWidth;
    }

    public JBIG2Page getPage(int page) {
        return (JBIG2Page) this.pages.get(Integer.valueOf(page));
    }

    public byte[] getGlobal(boolean for_embedding) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            for (JBIG2Segment s : this.globals) {
                if (!(for_embedding && (s.type == 51 || s.type == 49))) {
                    os.write(s.headerData);
                    os.write(s.data);
                }
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (os.size() <= 0) {
            return null;
        }
        return os.toByteArray();
    }

    public String toString() {
        if (this.read) {
            return "Jbig2SegmentReader: number of pages: " + numberOfPages();
        }
        return "Jbig2SegmentReader in indeterminate state.";
    }
}
