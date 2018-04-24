package com.itextpdf.text.pdf.events;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IndexEvents extends PdfPageEventHelper {
    private Comparator<Entry> comparator = new C08741();
    private long indexcounter = 0;
    private List<Entry> indexentry = new ArrayList();
    private Map<String, Integer> indextag = new TreeMap();

    class C08741 implements Comparator<Entry> {
        C08741() {
        }

        public int compare(Entry en1, Entry en2) {
            if (en1.getIn1() == null || en2.getIn1() == null) {
                return 0;
            }
            int rt = en1.getIn1().compareToIgnoreCase(en2.getIn1());
            if (rt != 0 || en1.getIn2() == null || en2.getIn2() == null) {
                return rt;
            }
            rt = en1.getIn2().compareToIgnoreCase(en2.getIn2());
            if (rt != 0 || en1.getIn3() == null || en2.getIn3() == null) {
                return rt;
            }
            return en1.getIn3().compareToIgnoreCase(en2.getIn3());
        }
    }

    public class Entry {
        private String in1;
        private String in2;
        private String in3;
        private List<Integer> pagenumbers = new ArrayList();
        private String tag;
        private List<String> tags = new ArrayList();

        public Entry(String aIn1, String aIn2, String aIn3, String aTag) {
            this.in1 = aIn1;
            this.in2 = aIn2;
            this.in3 = aIn3;
            this.tag = aTag;
        }

        public String getIn1() {
            return this.in1;
        }

        public String getIn2() {
            return this.in2;
        }

        public String getIn3() {
            return this.in3;
        }

        public String getTag() {
            return this.tag;
        }

        public int getPageNumber() {
            Integer i = (Integer) IndexEvents.this.indextag.get(this.tag);
            if (i != null) {
                return i.intValue();
            }
            return -1;
        }

        public void addPageNumberAndTag(int number, String tag) {
            this.pagenumbers.add(Integer.valueOf(number));
            this.tags.add(tag);
        }

        public String getKey() {
            return this.in1 + "!" + this.in2 + "!" + this.in3;
        }

        public List<Integer> getPagenumbers() {
            return this.pagenumbers;
        }

        public List<String> getTags() {
            return this.tags;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(this.in1).append(' ');
            buf.append(this.in2).append(' ');
            buf.append(this.in3).append(' ');
            for (int i = 0; i < this.pagenumbers.size(); i++) {
                buf.append(this.pagenumbers.get(i)).append(' ');
            }
            return buf.toString();
        }
    }

    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
        this.indextag.put(text, Integer.valueOf(writer.getPageNumber()));
    }

    public Chunk create(String text, String in1, String in2, String in3) {
        Chunk chunk = new Chunk(text);
        StringBuilder append = new StringBuilder().append("idx_");
        long j = this.indexcounter;
        this.indexcounter = 1 + j;
        String tag = append.append(j).toString();
        chunk.setGenericTag(tag);
        chunk.setLocalDestination(tag);
        this.indexentry.add(new Entry(in1, in2, in3, tag));
        return chunk;
    }

    public Chunk create(String text, String in1) {
        return create(text, in1, "", "");
    }

    public Chunk create(String text, String in1, String in2) {
        return create(text, in1, in2, "");
    }

    public void create(Chunk text, String in1, String in2, String in3) {
        StringBuilder append = new StringBuilder().append("idx_");
        long j = this.indexcounter;
        this.indexcounter = 1 + j;
        String tag = append.append(j).toString();
        text.setGenericTag(tag);
        text.setLocalDestination(tag);
        this.indexentry.add(new Entry(in1, in2, in3, tag));
    }

    public void create(Chunk text, String in1) {
        create(text, in1, "", "");
    }

    public void create(Chunk text, String in1, String in2) {
        create(text, in1, in2, "");
    }

    public void setComparator(Comparator<Entry> aComparator) {
        this.comparator = aComparator;
    }

    public List<Entry> getSortedEntries() {
        Map<String, Entry> grouped = new HashMap();
        for (int i = 0; i < this.indexentry.size(); i++) {
            Entry e = (Entry) this.indexentry.get(i);
            String key = e.getKey();
            Entry master = (Entry) grouped.get(key);
            if (master != null) {
                master.addPageNumberAndTag(e.getPageNumber(), e.getTag());
            } else {
                e.addPageNumberAndTag(e.getPageNumber(), e.getTag());
                grouped.put(key, e);
            }
        }
        List<Entry> sorted = new ArrayList(grouped.values());
        Collections.sort(sorted, this.comparator);
        return sorted;
    }
}
