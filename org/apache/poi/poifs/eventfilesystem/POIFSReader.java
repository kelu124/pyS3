package org.apache.poi.poifs.eventfilesystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.OPOIFSDocument;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.Property;
import org.apache.poi.poifs.property.PropertyTable;
import org.apache.poi.poifs.storage.BlockAllocationTableReader;
import org.apache.poi.poifs.storage.BlockList;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.poifs.storage.RawDataBlockList;
import org.apache.poi.poifs.storage.SmallBlockTableReader;

public class POIFSReader {
    private final POIFSReaderRegistry registry = new POIFSReaderRegistry();
    private boolean registryClosed = false;

    private static class SampleListener implements POIFSReaderListener {
        SampleListener() {
        }

        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
            DocumentInputStream istream = event.getStream();
            POIFSDocumentPath path = event.getPath();
            String name = event.getName();
            try {
                byte[] data = new byte[istream.available()];
                istream.read(data);
                int pathLength = path.length();
                for (int k = 0; k < pathLength; k++) {
                    System.out.print("/" + path.getComponent(k));
                }
                System.out.println("/" + name + ": " + data.length + " bytes read");
            } catch (IOException e) {
            }
        }
    }

    public void read(InputStream stream) throws IOException {
        this.registryClosed = true;
        HeaderBlock header_block = new HeaderBlock(stream);
        RawDataBlockList data_blocks = new RawDataBlockList(stream, header_block.getBigBlockSize());
        BlockAllocationTableReader blockAllocationTableReader = new BlockAllocationTableReader(header_block.getBigBlockSize(), header_block.getBATCount(), header_block.getBATArray(), header_block.getXBATCount(), header_block.getXBATIndex(), data_blocks);
        PropertyTable properties = new PropertyTable(header_block, data_blocks);
        processProperties(SmallBlockTableReader.getSmallDocumentBlocks(header_block.getBigBlockSize(), data_blocks, properties.getRoot(), header_block.getSBATStart()), data_blocks, properties.getRoot().getChildren(), new POIFSDocumentPath());
    }

    public void registerListener(POIFSReaderListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        } else if (this.registryClosed) {
            throw new IllegalStateException();
        } else {
            this.registry.registerListener(listener);
        }
    }

    public void registerListener(POIFSReaderListener listener, String name) {
        registerListener(listener, null, name);
    }

    public void registerListener(POIFSReaderListener listener, POIFSDocumentPath path, String name) {
        if (listener == null || name == null || name.length() == 0) {
            throw new NullPointerException();
        } else if (this.registryClosed) {
            throw new IllegalStateException();
        } else {
            POIFSReaderRegistry pOIFSReaderRegistry = this.registry;
            if (path == null) {
                path = new POIFSDocumentPath();
            }
            pOIFSReaderRegistry.registerListener(listener, path, name);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("at least one argument required: input filename(s)");
            System.exit(1);
        }
        for (int j = 0; j < args.length; j++) {
            POIFSReader reader = new POIFSReader();
            reader.registerListener(new SampleListener());
            System.out.println("reading " + args[j]);
            FileInputStream istream = new FileInputStream(args[j]);
            reader.read(istream);
            istream.close();
        }
    }

    private void processProperties(BlockList small_blocks, BlockList big_blocks, Iterator<Property> properties, POIFSDocumentPath path) throws IOException {
        while (properties.hasNext()) {
            Property property = (Property) properties.next();
            String name = property.getName();
            if (property.isDirectory()) {
                processProperties(small_blocks, big_blocks, ((DirectoryProperty) property).getChildren(), new POIFSDocumentPath(path, new String[]{name}));
            } else {
                int startBlock = property.getStartBlock();
                Iterator<POIFSReaderListener> listeners = this.registry.getListeners(path, name);
                if (listeners.hasNext()) {
                    OPOIFSDocument document;
                    int size = property.getSize();
                    if (property.shouldUseSmallBlocks()) {
                        document = new OPOIFSDocument(name, small_blocks.fetchBlocks(startBlock, -1), size);
                    } else {
                        document = new OPOIFSDocument(name, big_blocks.fetchBlocks(startBlock, -1), size);
                    }
                    while (listeners.hasNext()) {
                        ((POIFSReaderListener) listeners.next()).processPOIFSReaderEvent(new POIFSReaderEvent(new DocumentInputStream(document), path, name));
                    }
                } else if (property.shouldUseSmallBlocks()) {
                    small_blocks.fetchBlocks(startBlock, -1);
                } else {
                    big_blocks.fetchBlocks(startBlock, -1);
                }
            }
        }
    }
}
