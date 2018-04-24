package org.apache.poi.poifs.macros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.RLEDecompressingInputStream;
import org.apache.poi.util.StringUtil;

public class VBAMacroReader implements Closeable {
    private static final int EOF = -1;
    private static final int MODULEOFFSET = 49;
    private static final int MODULETYPE_DOCUMENT_CLASS_OR_DESIGNER = 34;
    private static final int MODULETYPE_PROCEDURAL = 33;
    private static final int PROJECTCODEPAGE = 3;
    private static final int PROJECTLCID = 2;
    private static final int PROJECTVERSION = 9;
    private static final int STREAMNAME = 26;
    protected static final String VBA_PROJECT_OOXML = "vbaProject.bin";
    protected static final String VBA_PROJECT_POIFS = "VBA";
    private static final int VERSION_DEPENDENT_TERMINATOR = 43;
    private static final int VERSION_INDEPENDENT_TERMINATOR = 16;
    private NPOIFSFileSystem fs;

    protected static class Module {
        byte[] buf;
        Integer offset;

        protected Module() {
        }

        void read(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            out.close();
            this.buf = out.toByteArray();
        }
    }

    protected static class ModuleMap extends HashMap<String, Module> {
        Charset charset = Charset.forName("Cp1252");

        protected ModuleMap() {
        }
    }

    public VBAMacroReader(InputStream rstream) throws IOException {
        PushbackInputStream stream = new PushbackInputStream(rstream, 8);
        if (NPOIFSFileSystem.hasPOIFSHeader(IOUtils.peekFirst8Bytes(stream))) {
            this.fs = new NPOIFSFileSystem(stream);
        } else {
            openOOXML(stream);
        }
    }

    public VBAMacroReader(File file) throws IOException {
        try {
            this.fs = new NPOIFSFileSystem(file);
        } catch (OfficeXmlFileException e) {
            openOOXML(new FileInputStream(file));
        }
    }

    public VBAMacroReader(NPOIFSFileSystem fs) {
        this.fs = fs;
    }

    private void openOOXML(InputStream zipFile) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFile);
        ZipEntry zipEntry;
        do {
            zipEntry = zis.getNextEntry();
            if (zipEntry == null) {
                zis.close();
                throw new IllegalArgumentException("No VBA project found");
            }
        } while (!StringUtil.endsWithIgnoreCase(zipEntry.getName(), VBA_PROJECT_OOXML));
        try {
            this.fs = new NPOIFSFileSystem(zis);
        } catch (IOException e) {
            zis.close();
            throw e;
        }
    }

    public void close() throws IOException {
        this.fs.close();
        this.fs = null;
    }

    public Map<String, String> readMacros() throws IOException {
        ModuleMap modules = new ModuleMap();
        findMacros(this.fs.getRoot(), modules);
        Map<String, String> moduleSources = new HashMap();
        for (Entry<String, Module> entry : modules.entrySet()) {
            Module module = (Module) entry.getValue();
            if (module.buf != null && module.buf.length > 0) {
                moduleSources.put(entry.getKey(), new String(module.buf, modules.charset));
            }
        }
        return moduleSources;
    }

    protected void findMacros(DirectoryNode dir, ModuleMap modules) throws IOException {
        if (VBA_PROJECT_POIFS.equalsIgnoreCase(dir.getName())) {
            readMacros(dir, modules);
            return;
        }
        Iterator i$ = dir.iterator();
        while (i$.hasNext()) {
            org.apache.poi.poifs.filesystem.Entry child = (org.apache.poi.poifs.filesystem.Entry) i$.next();
            if (child instanceof DirectoryNode) {
                findMacros((DirectoryNode) child, modules);
            }
        }
    }

    private static String readString(InputStream stream, int length, Charset charset) throws IOException {
        byte[] buffer = new byte[length];
        return new String(buffer, 0, stream.read(buffer), charset);
    }

    private static void readModule(RLEDecompressingInputStream in, String streamName, ModuleMap modules) throws IOException {
        int moduleOffset = in.readInt();
        Module module = (Module) modules.get(streamName);
        if (module == null) {
            module = new Module();
            module.offset = Integer.valueOf(moduleOffset);
            modules.put(streamName, module);
            return;
        }
        InputStream stream = new RLEDecompressingInputStream(new ByteArrayInputStream(module.buf, moduleOffset, module.buf.length - moduleOffset));
        module.read(stream);
        stream.close();
    }

    private static void readModule(DocumentInputStream dis, String name, ModuleMap modules) throws IOException {
        Module module = (Module) modules.get(name);
        if (module == null) {
            module = new Module();
            modules.put(name, module);
            module.read(dis);
        } else if (module.offset == null) {
            throw new IOException("Module offset for '" + name + "' was never read.");
        } else {
            long skippedBytes = dis.skip((long) module.offset.intValue());
            if (skippedBytes != ((long) module.offset.intValue())) {
                throw new IOException("tried to skip " + module.offset + " bytes, but actually skipped " + skippedBytes + " bytes");
            }
            InputStream stream = new RLEDecompressingInputStream(dis);
            module.read(stream);
            stream.close();
        }
    }

    private static void trySkip(InputStream in, long n) throws IOException {
        long skippedBytes = in.skip(n);
        if (skippedBytes == n) {
            return;
        }
        if (skippedBytes < 0) {
            throw new IOException("Tried skipping " + n + " bytes, but no bytes were skipped. " + "The end of the stream has been reached or the stream is closed.");
        }
        throw new IOException("Tried skipping " + n + " bytes, but only " + skippedBytes + " bytes were skipped. " + "This should never happen.");
    }

    protected void readMacros(DirectoryNode macroDir, ModuleMap modules) throws IOException {
        Iterator i$ = macroDir.iterator();
        while (i$.hasNext()) {
            org.apache.poi.poifs.filesystem.Entry entry = (org.apache.poi.poifs.filesystem.Entry) i$.next();
            if (entry instanceof DocumentNode) {
                String name = entry.getName();
                DocumentInputStream dis = new DocumentInputStream((DocumentNode) entry);
                try {
                    if ("dir".equalsIgnoreCase(name)) {
                        RLEDecompressingInputStream in = new RLEDecompressingInputStream(dis);
                        String streamName = null;
                        int recordId = 0;
                        while (true) {
                            recordId = in.readShort();
                            if (-1 == recordId || 16 == recordId) {
                                in.close();
                            } else {
                                try {
                                    int recordLength = in.readInt();
                                    switch (recordId) {
                                        case 3:
                                            modules.charset = Charset.forName("Cp" + in.readShort());
                                            break;
                                        case 9:
                                            trySkip(in, 6);
                                            break;
                                        case 26:
                                            streamName = readString(in, recordLength, modules.charset);
                                            break;
                                        case 49:
                                            readModule(in, streamName, modules);
                                            break;
                                        default:
                                            trySkip(in, (long) recordLength);
                                            break;
                                    }
                                } catch (IOException e) {
                                    throw new IOException("Error occurred while reading macros at section id " + recordId + " (" + HexDump.shortToHex(recordId) + ")", e);
                                } catch (Throwable th) {
                                    in.close();
                                }
                            }
                        }
                    } else if (!(StringUtil.startsWithIgnoreCase(name, "__SRP") || StringUtil.startsWithIgnoreCase(name, "_VBA_PROJECT"))) {
                        readModule(dis, name, modules);
                    }
                    dis.close();
                } catch (Throwable th2) {
                    dis.close();
                }
            }
        }
    }
}
