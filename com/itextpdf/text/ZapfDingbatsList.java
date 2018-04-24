package com.itextpdf.text;

public class ZapfDingbatsList extends List {
    protected int zn;

    public ZapfDingbatsList(int zn) {
        super(true);
        this.zn = zn;
        this.symbol.setFont(FontFactory.getFont("ZapfDingbats", this.symbol.getFont().getSize(), 0));
        this.postSymbol = " ";
    }

    public ZapfDingbatsList(int zn, int symbolIndent) {
        super(true, (float) symbolIndent);
        this.zn = zn;
        this.symbol.setFont(FontFactory.getFont("ZapfDingbats", this.symbol.getFont().getSize(), 0));
        this.postSymbol = " ";
    }

    public ZapfDingbatsList(int zn, int symbolIndent, BaseColor zapfDingbatColor) {
        super(true, (float) symbolIndent);
        this.zn = zn;
        this.symbol.setFont(FontFactory.getFont("ZapfDingbats", this.symbol.getFont().getSize(), 0, zapfDingbatColor));
        this.postSymbol = " ";
    }

    public void setDingbatColor(BaseColor zapfDingbatColor) {
        this.symbol.setFont(FontFactory.getFont("ZapfDingbats", this.symbol.getFont().getSize(), 0, zapfDingbatColor));
    }

    public void setCharNumber(int zn) {
        this.zn = zn;
    }

    public int getCharNumber() {
        return this.zn;
    }

    public boolean add(Element o) {
        if (o instanceof ListItem) {
            ListItem item = (ListItem) o;
            Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
            chunk.setAttributes(this.symbol.getAttributes());
            chunk.append(String.valueOf((char) this.zn));
            chunk.append(this.postSymbol);
            item.setListSymbol(chunk);
            item.setIndentationLeft(this.symbolIndent, this.autoindent);
            item.setIndentationRight(0.0f);
            this.list.add(item);
        } else if (o instanceof List) {
            List nested = (List) o;
            nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
            this.first--;
            return this.list.add(nested);
        }
        return false;
    }
}
