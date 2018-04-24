package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.itextpdf.text.xml.xmp.DublinCoreProperties;

@Beta
public class DeadEvent {
    private final Object event;
    private final Object source;

    public DeadEvent(Object source, Object event) {
        this.source = Preconditions.checkNotNull(source);
        this.event = Preconditions.checkNotNull(event);
    }

    public Object getSource() {
        return this.source;
    }

    public Object getEvent() {
        return this.event;
    }

    public String toString() {
        return MoreObjects.toStringHelper((Object) this).add(DublinCoreProperties.SOURCE, this.source).add("event", this.event).toString();
    }
}
