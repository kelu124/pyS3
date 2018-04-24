package org.apache.poi.hpsf;

import java.util.Date;
import org.apache.poi.hpsf.wellknown.PropertyIDMap;

public final class SummaryInformation extends SpecialPropertySet {
    public static final String DEFAULT_STREAM_NAME = "\u0005SummaryInformation";

    public PropertyIDMap getPropertySetIDMap() {
        return PropertyIDMap.getSummaryInformationProperties();
    }

    public SummaryInformation(PropertySet ps) throws UnexpectedPropertySetTypeException {
        super(ps);
        if (!isSummaryInformation()) {
            throw new UnexpectedPropertySetTypeException("Not a " + getClass().getName());
        }
    }

    public String getTitle() {
        return getPropertyStringValue(2);
    }

    public void setTitle(String title) {
        ((MutableSection) getFirstSection()).setProperty(2, title);
    }

    public void removeTitle() {
        ((MutableSection) getFirstSection()).removeProperty(2);
    }

    public String getSubject() {
        return getPropertyStringValue(3);
    }

    public void setSubject(String subject) {
        ((MutableSection) getFirstSection()).setProperty(3, subject);
    }

    public void removeSubject() {
        ((MutableSection) getFirstSection()).removeProperty(3);
    }

    public String getAuthor() {
        return getPropertyStringValue(4);
    }

    public void setAuthor(String author) {
        ((MutableSection) getFirstSection()).setProperty(4, author);
    }

    public void removeAuthor() {
        ((MutableSection) getFirstSection()).removeProperty(4);
    }

    public String getKeywords() {
        return getPropertyStringValue(5);
    }

    public void setKeywords(String keywords) {
        ((MutableSection) getFirstSection()).setProperty(5, keywords);
    }

    public void removeKeywords() {
        ((MutableSection) getFirstSection()).removeProperty(5);
    }

    public String getComments() {
        return getPropertyStringValue(6);
    }

    public void setComments(String comments) {
        ((MutableSection) getFirstSection()).setProperty(6, comments);
    }

    public void removeComments() {
        ((MutableSection) getFirstSection()).removeProperty(6);
    }

    public String getTemplate() {
        return getPropertyStringValue(7);
    }

    public void setTemplate(String template) {
        ((MutableSection) getFirstSection()).setProperty(7, template);
    }

    public void removeTemplate() {
        ((MutableSection) getFirstSection()).removeProperty(7);
    }

    public String getLastAuthor() {
        return getPropertyStringValue(8);
    }

    public void setLastAuthor(String lastAuthor) {
        ((MutableSection) getFirstSection()).setProperty(8, lastAuthor);
    }

    public void removeLastAuthor() {
        ((MutableSection) getFirstSection()).removeProperty(8);
    }

    public String getRevNumber() {
        return getPropertyStringValue(9);
    }

    public void setRevNumber(String revNumber) {
        ((MutableSection) getFirstSection()).setProperty(9, revNumber);
    }

    public void removeRevNumber() {
        ((MutableSection) getFirstSection()).removeProperty(9);
    }

    public long getEditTime() {
        Date d = (Date) getProperty(10);
        if (d == null) {
            return 0;
        }
        return Util.dateToFileTime(d);
    }

    public void setEditTime(long time) {
        ((MutableSection) getFirstSection()).setProperty(10, 64, Util.filetimeToDate(time));
    }

    public void removeEditTime() {
        ((MutableSection) getFirstSection()).removeProperty(10);
    }

    public Date getLastPrinted() {
        return (Date) getProperty(11);
    }

    public void setLastPrinted(Date lastPrinted) {
        ((MutableSection) getFirstSection()).setProperty(11, 64, lastPrinted);
    }

    public void removeLastPrinted() {
        ((MutableSection) getFirstSection()).removeProperty(11);
    }

    public Date getCreateDateTime() {
        return (Date) getProperty(12);
    }

    public void setCreateDateTime(Date createDateTime) {
        ((MutableSection) getFirstSection()).setProperty(12, 64, createDateTime);
    }

    public void removeCreateDateTime() {
        ((MutableSection) getFirstSection()).removeProperty(12);
    }

    public Date getLastSaveDateTime() {
        return (Date) getProperty(13);
    }

    public void setLastSaveDateTime(Date time) {
        ((MutableSection) getFirstSection()).setProperty(13, 64, time);
    }

    public void removeLastSaveDateTime() {
        ((MutableSection) getFirstSection()).removeProperty(13);
    }

    public int getPageCount() {
        return getPropertyIntValue(14);
    }

    public void setPageCount(int pageCount) {
        ((MutableSection) getFirstSection()).setProperty(14, pageCount);
    }

    public void removePageCount() {
        ((MutableSection) getFirstSection()).removeProperty(14);
    }

    public int getWordCount() {
        return getPropertyIntValue(15);
    }

    public void setWordCount(int wordCount) {
        ((MutableSection) getFirstSection()).setProperty(15, wordCount);
    }

    public void removeWordCount() {
        ((MutableSection) getFirstSection()).removeProperty(15);
    }

    public int getCharCount() {
        return getPropertyIntValue(16);
    }

    public void setCharCount(int charCount) {
        ((MutableSection) getFirstSection()).setProperty(16, charCount);
    }

    public void removeCharCount() {
        ((MutableSection) getFirstSection()).removeProperty(16);
    }

    public byte[] getThumbnail() {
        return (byte[]) getProperty(17);
    }

    public Thumbnail getThumbnailThumbnail() {
        byte[] data = getThumbnail();
        if (data == null) {
            return null;
        }
        return new Thumbnail(data);
    }

    public void setThumbnail(byte[] thumbnail) {
        ((MutableSection) getFirstSection()).setProperty(17, 30, thumbnail);
    }

    public void removeThumbnail() {
        ((MutableSection) getFirstSection()).removeProperty(17);
    }

    public String getApplicationName() {
        return getPropertyStringValue(18);
    }

    public void setApplicationName(String applicationName) {
        ((MutableSection) getFirstSection()).setProperty(18, applicationName);
    }

    public void removeApplicationName() {
        ((MutableSection) getFirstSection()).removeProperty(18);
    }

    public int getSecurity() {
        return getPropertyIntValue(19);
    }

    public void setSecurity(int security) {
        ((MutableSection) getFirstSection()).setProperty(19, security);
    }

    public void removeSecurity() {
        ((MutableSection) getFirstSection()).removeProperty(19);
    }
}
