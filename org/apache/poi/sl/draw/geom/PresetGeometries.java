package org.apache.poi.sl.draw.geom;

import java.io.InputStream;
import java.util.LinkedHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.poi.sl.draw.binding.CTCustomGeometry2D;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class PresetGeometries extends LinkedHashMap<String, CustomGeometry> {
    protected static final String BINDING_PACKAGE = "org.apache.poi.sl.draw.binding";
    private static final POILogger LOG = POILogFactory.getLogger(PresetGeometries.class);
    protected static PresetGeometries _inst;

    class C10951 implements EventFilter {
        C10951() {
        }

        public boolean accept(XMLEvent event) {
            return event.isStartElement();
        }
    }

    protected PresetGeometries() {
    }

    public void init(InputStream is) throws XMLStreamException, JAXBException {
        EventFilter startElementFilter = new C10951();
        XMLInputFactory staxFactory = XMLInputFactory.newFactory();
        XMLEventReader staxReader = staxFactory.createXMLEventReader(is);
        XMLEventReader staxFiltRd = staxFactory.createFilteredReader(staxReader, startElementFilter);
        staxFiltRd.nextEvent();
        Unmarshaller unmarshaller = JAXBContext.newInstance(BINDING_PACKAGE).createUnmarshaller();
        long cntElem = 0;
        while (staxFiltRd.peek() != null) {
            String name = ((StartElement) staxFiltRd.peek()).getName().getLocalPart();
            CTCustomGeometry2D cus = (CTCustomGeometry2D) unmarshaller.unmarshal(staxReader, CTCustomGeometry2D.class).getValue();
            cntElem++;
            if (containsKey(name)) {
                LOG.log(5, new Object[]{"Duplicate definition of " + name});
            }
            put(name, new CustomGeometry(cus));
        }
    }

    public static CustomGeometry convertCustomGeometry(XMLStreamReader staxReader) {
        try {
            return new CustomGeometry((CTCustomGeometry2D) JAXBContext.newInstance(BINDING_PACKAGE).createUnmarshaller().unmarshal(staxReader, CTCustomGeometry2D.class).getValue());
        } catch (JAXBException e) {
            LOG.log(7, new Object[]{"Unable to parse single custom geometry", e});
            return null;
        }
    }

    public static synchronized PresetGeometries getInstance() {
        PresetGeometries presetGeometries;
        synchronized (PresetGeometries.class) {
            if (_inst == null) {
                PresetGeometries lInst = new PresetGeometries();
                InputStream is;
                try {
                    is = PresetGeometries.class.getResourceAsStream("presetShapeDefinitions.xml");
                    lInst.init(is);
                    is.close();
                    _inst = lInst;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    is.close();
                }
            }
            presetGeometries = _inst;
        }
        return presetGeometries;
    }
}
