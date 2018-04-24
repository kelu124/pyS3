package com.itextpdf.text.pdf.parser;

public class FilteredRenderListener implements RenderListener {
    private final RenderListener delegate;
    private final RenderFilter[] filters;

    public FilteredRenderListener(RenderListener delegate, RenderFilter... filters) {
        this.delegate = delegate;
        this.filters = filters;
    }

    public void renderText(TextRenderInfo renderInfo) {
        RenderFilter[] arr$ = this.filters;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].allowText(renderInfo)) {
                i$++;
            } else {
                return;
            }
        }
        this.delegate.renderText(renderInfo);
    }

    public void beginTextBlock() {
        this.delegate.beginTextBlock();
    }

    public void endTextBlock() {
        this.delegate.endTextBlock();
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        RenderFilter[] arr$ = this.filters;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].allowImage(renderInfo)) {
                i$++;
            } else {
                return;
            }
        }
        this.delegate.renderImage(renderInfo);
    }
}
