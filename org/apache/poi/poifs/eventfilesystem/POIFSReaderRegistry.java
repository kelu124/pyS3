package org.apache.poi.poifs.eventfilesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.poi.poifs.filesystem.DocumentDescriptor;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;

class POIFSReaderRegistry {
    private Map<DocumentDescriptor, Set<POIFSReaderListener>> chosenDocumentDescriptors = new HashMap();
    private Set<POIFSReaderListener> omnivorousListeners = new HashSet();
    private Map<POIFSReaderListener, Set<DocumentDescriptor>> selectiveListeners = new HashMap();

    POIFSReaderRegistry() {
    }

    void registerListener(POIFSReaderListener listener, POIFSDocumentPath path, String documentName) {
        if (!this.omnivorousListeners.contains(listener)) {
            Set<DocumentDescriptor> descriptors = (Set) this.selectiveListeners.get(listener);
            if (descriptors == null) {
                descriptors = new HashSet();
                this.selectiveListeners.put(listener, descriptors);
            }
            DocumentDescriptor descriptor = new DocumentDescriptor(path, documentName);
            if (descriptors.add(descriptor)) {
                Set<POIFSReaderListener> listeners = (Set) this.chosenDocumentDescriptors.get(descriptor);
                if (listeners == null) {
                    listeners = new HashSet();
                    this.chosenDocumentDescriptors.put(descriptor, listeners);
                }
                listeners.add(listener);
            }
        }
    }

    void registerListener(POIFSReaderListener listener) {
        if (!this.omnivorousListeners.contains(listener)) {
            removeSelectiveListener(listener);
            this.omnivorousListeners.add(listener);
        }
    }

    Iterator<POIFSReaderListener> getListeners(POIFSDocumentPath path, String name) {
        Set<POIFSReaderListener> rval = new HashSet(this.omnivorousListeners);
        Set<POIFSReaderListener> selectiveListenersInner = (Set) this.chosenDocumentDescriptors.get(new DocumentDescriptor(path, name));
        if (selectiveListenersInner != null) {
            rval.addAll(selectiveListenersInner);
        }
        return rval.iterator();
    }

    private void removeSelectiveListener(POIFSReaderListener listener) {
        Set<DocumentDescriptor> selectedDescriptors = (Set) this.selectiveListeners.remove(listener);
        if (selectedDescriptors != null) {
            for (DocumentDescriptor dropDocument : selectedDescriptors) {
                dropDocument(listener, dropDocument);
            }
        }
    }

    private void dropDocument(POIFSReaderListener listener, DocumentDescriptor descriptor) {
        Set<POIFSReaderListener> listeners = (Set) this.chosenDocumentDescriptors.get(descriptor);
        listeners.remove(listener);
        if (listeners.size() == 0) {
            this.chosenDocumentDescriptors.remove(descriptor);
        }
    }
}
