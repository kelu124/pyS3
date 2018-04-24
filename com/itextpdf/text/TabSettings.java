package com.itextpdf.text;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.DeltaRecord;

public class TabSettings {
    public static final float DEFAULT_TAB_INTERVAL = 36.0f;
    private float tabInterval = DEFAULT_TAB_INTERVAL;
    private List<TabStop> tabStops = new ArrayList();

    public static TabStop getTabStopNewInstance(float currentPosition, TabSettings tabSettings) {
        if (tabSettings != null) {
            return tabSettings.getTabStopNewInstance(currentPosition);
        }
        return TabStop.newInstance(currentPosition, DEFAULT_TAB_INTERVAL);
    }

    public TabSettings(List<TabStop> tabStops) {
        this.tabStops = tabStops;
    }

    public TabSettings(float tabInterval) {
        this.tabInterval = tabInterval;
    }

    public TabSettings(List<TabStop> tabStops, float tabInterval) {
        this.tabStops = tabStops;
        this.tabInterval = tabInterval;
    }

    public List<TabStop> getTabStops() {
        return this.tabStops;
    }

    public void setTabStops(List<TabStop> tabStops) {
        this.tabStops = tabStops;
    }

    public float getTabInterval() {
        return this.tabInterval;
    }

    public void setTabInterval(float tabInterval) {
        this.tabInterval = tabInterval;
    }

    public TabStop getTabStopNewInstance(float currentPosition) {
        TabStop tabStop = null;
        if (this.tabStops != null) {
            for (TabStop currentTabStop : this.tabStops) {
                if (((double) (currentTabStop.getPosition() - currentPosition)) > DeltaRecord.DEFAULT_VALUE) {
                    tabStop = new TabStop(currentTabStop);
                    break;
                }
            }
        }
        if (tabStop == null) {
            return TabStop.newInstance(currentPosition, this.tabInterval);
        }
        return tabStop;
    }
}
