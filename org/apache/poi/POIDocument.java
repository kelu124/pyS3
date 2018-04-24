package org.apache.poi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.MutablePropertySet;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public abstract class POIDocument implements Closeable {
    private static final POILogger logger = POILogFactory.getLogger(POIDocument.class);
    protected DirectoryNode directory;
    private DocumentSummaryInformation dsInf;
    private boolean initialized;
    private SummaryInformation sInf;

    public abstract void write() throws IOException;

    public abstract void write(File file) throws IOException;

    public abstract void write(OutputStream outputStream) throws IOException;

    protected POIDocument(DirectoryNode dir) {
        this.initialized = false;
        this.directory = dir;
    }

    protected POIDocument(OPOIFSFileSystem fs) {
        this(fs.getRoot());
    }

    protected POIDocument(NPOIFSFileSystem fs) {
        this(fs.getRoot());
    }

    protected POIDocument(POIFSFileSystem fs) {
        this(fs.getRoot());
    }

    public DocumentSummaryInformation getDocumentSummaryInformation() {
        if (!this.initialized) {
            readProperties();
        }
        return this.dsInf;
    }

    public SummaryInformation getSummaryInformation() {
        if (!this.initialized) {
            readProperties();
        }
        return this.sInf;
    }

    public void createInformationProperties() {
        if (!this.initialized) {
            readProperties();
        }
        if (this.sInf == null) {
            this.sInf = PropertySetFactory.newSummaryInformation();
        }
        if (this.dsInf == null) {
            this.dsInf = PropertySetFactory.newDocumentSummaryInformation();
        }
    }

    protected void readProperties() {
        PropertySet ps = getPropertySet(DocumentSummaryInformation.DEFAULT_STREAM_NAME);
        if (ps instanceof DocumentSummaryInformation) {
            this.dsInf = (DocumentSummaryInformation) ps;
        } else if (ps != null) {
            logger.log(5, "DocumentSummaryInformation property set came back with wrong class - ", ps.getClass());
        } else {
            logger.log(5, "DocumentSummaryInformation property set came back as null");
        }
        ps = getPropertySet(SummaryInformation.DEFAULT_STREAM_NAME);
        if (ps instanceof SummaryInformation) {
            this.sInf = (SummaryInformation) ps;
        } else if (ps != null) {
            logger.log(5, "SummaryInformation property set came back with wrong class - ", ps.getClass());
        } else {
            logger.log(5, "SummaryInformation property set came back as null");
        }
        this.initialized = true;
    }

    protected PropertySet getPropertySet(String setName) {
        return getPropertySet(setName, null);
    }

    protected PropertySet getPropertySet(String setName, EncryptionInfo encryptionInfo) {
        Exception e;
        PropertySet propertySet;
        Throwable th;
        DocumentInputStream dis;
        DirectoryNode dirNode = this.directory;
        NPOIFSFileSystem encPoifs = null;
        String step = "getting";
        if (encryptionInfo != null) {
            try {
                step = "getting encrypted";
                InputStream is = encryptionInfo.getDecryptor().getDataStream(this.directory);
                try {
                    NPOIFSFileSystem encPoifs2 = new NPOIFSFileSystem(is);
                    try {
                        dirNode = encPoifs2.getRoot();
                        try {
                            is.close();
                            encPoifs = encPoifs2;
                        } catch (Exception e2) {
                            e = e2;
                            encPoifs = encPoifs2;
                            try {
                                logger.log(5, "Error " + step + " property set with name " + setName, e);
                                propertySet = null;
                                if (encPoifs != null) {
                                    try {
                                        encPoifs.close();
                                    } catch (IOException e3) {
                                        logger.log(5, "Error closing encrypted property poifs", e3);
                                    }
                                }
                                return propertySet;
                            } catch (Throwable th2) {
                                th = th2;
                                if (encPoifs != null) {
                                    try {
                                        encPoifs.close();
                                    } catch (IOException e32) {
                                        logger.log(5, "Error closing encrypted property poifs", e32);
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            encPoifs = encPoifs2;
                            if (encPoifs != null) {
                                encPoifs.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        encPoifs = encPoifs2;
                        is.close();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    is.close();
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                logger.log(5, "Error " + step + " property set with name " + setName, e);
                propertySet = null;
                if (encPoifs != null) {
                    encPoifs.close();
                }
                return propertySet;
            } catch (Throwable th6) {
                dis.close();
            }
        }
        if (dirNode != null) {
            if (dirNode.hasEntry(setName)) {
                step = "getting";
                dis = dirNode.createDocumentInputStream(dirNode.getEntry(setName));
                step = "creating";
                propertySet = PropertySetFactory.create(dis);
                dis.close();
                if (encPoifs != null) {
                    try {
                        encPoifs.close();
                    } catch (IOException e322) {
                        logger.log(5, "Error closing encrypted property poifs", e322);
                    }
                }
                return propertySet;
            }
        }
        propertySet = null;
        if (encPoifs != null) {
            try {
                encPoifs.close();
            } catch (IOException e3222) {
                logger.log(5, "Error closing encrypted property poifs", e3222);
            }
        }
        return propertySet;
    }

    protected void writeProperties() throws IOException {
        validateInPlaceWritePossible();
        writeProperties(this.directory.getFileSystem(), null);
    }

    protected void writeProperties(NPOIFSFileSystem outFS) throws IOException {
        writeProperties(outFS, null);
    }

    protected void writeProperties(NPOIFSFileSystem outFS, List<String> writtenEntries) throws IOException {
        SummaryInformation si = getSummaryInformation();
        if (si != null) {
            writePropertySet(SummaryInformation.DEFAULT_STREAM_NAME, si, outFS);
            if (writtenEntries != null) {
                writtenEntries.add(SummaryInformation.DEFAULT_STREAM_NAME);
            }
        }
        DocumentSummaryInformation dsi = getDocumentSummaryInformation();
        if (dsi != null) {
            writePropertySet(DocumentSummaryInformation.DEFAULT_STREAM_NAME, dsi, outFS);
            if (writtenEntries != null) {
                writtenEntries.add(DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            }
        }
    }

    protected void writePropertySet(String name, PropertySet set, NPOIFSFileSystem outFS) throws IOException {
        try {
            MutablePropertySet mSet = new MutablePropertySet(set);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            mSet.write(bOut);
            outFS.createOrUpdateDocument(new ByteArrayInputStream(bOut.toByteArray()), name);
            logger.log(3, "Wrote property set " + name + " of size " + data.length);
        } catch (WritingNotSupportedException e) {
            logger.log(7, "Couldn't write property set with name " + name + " as not supported by HPSF yet");
        }
    }

    protected void validateInPlaceWritePossible() throws IllegalStateException {
        if (this.directory == null) {
            throw new IllegalStateException("Newly created Document, cannot save in-place");
        } else if (this.directory.getParent() != null) {
            throw new IllegalStateException("This is not the root Document, cannot save embedded resource in-place");
        } else if (this.directory.getFileSystem() == null || !this.directory.getFileSystem().isInPlaceWriteable()) {
            throw new IllegalStateException("Opened read-only or via an InputStream, a Writeable File is required");
        }
    }

    public void close() throws IOException {
        if (this.directory != null && this.directory.getNFileSystem() != null) {
            this.directory.getNFileSystem().close();
            this.directory = null;
        }
    }

    @Internal
    public DirectoryNode getDirectory() {
        return this.directory;
    }
}
