package org.apache.poi.sl.usermodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.OldFileFormatException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.IOUtils;

public class SlideShowFactory {
    public static SlideShow<?, ?> create(NPOIFSFileSystem fs) throws IOException {
        return create(fs, null);
    }

    public static SlideShow<?, ?> create(NPOIFSFileSystem fs, String password) throws IOException {
        SlideShow<?, ?> createXSLFSlideShow;
        if (fs.getRoot().hasEntry(Decryptor.DEFAULT_POIFS_ENTRY)) {
            InputStream inputStream = null;
            try {
                inputStream = DocumentFactoryHelper.getDecryptedStream(fs, password);
                createXSLFSlideShow = createXSLFSlideShow(inputStream);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            if (password != null) {
                Biff8EncryptionKey.setCurrentUserPassword(password);
            }
            try {
                createXSLFSlideShow = createHSLFSlideShow(fs);
            } finally {
                Biff8EncryptionKey.setCurrentUserPassword(null);
            }
        }
        return createXSLFSlideShow;
    }

    public static SlideShow<?, ?> create(InputStream inp) throws IOException, EncryptedDocumentException {
        return create(inp, null);
    }

    public static SlideShow<?, ?> create(InputStream inp, String password) throws IOException, EncryptedDocumentException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (NPOIFSFileSystem.hasPOIFSHeader(IOUtils.peekFirst8Bytes(inp))) {
            return create(new NPOIFSFileSystem(inp), password);
        }
        if (DocumentFactoryHelper.hasOOXMLHeader(inp)) {
            return createXSLFSlideShow(inp);
        }
        throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
    }

    public static SlideShow<?, ?> create(File file) throws IOException, EncryptedDocumentException {
        return create(file, null);
    }

    public static SlideShow<?, ?> create(File file, String password) throws IOException, EncryptedDocumentException {
        return create(file, password, false);
    }

    public static SlideShow<?, ?> create(File file, String password, boolean readOnly) throws IOException, EncryptedDocumentException {
        RuntimeException e;
        if (file.exists()) {
            NPOIFSFileSystem fs = null;
            try {
                NPOIFSFileSystem fs2 = new NPOIFSFileSystem(file, readOnly);
                try {
                    fs = fs2;
                    return create(fs2, password);
                } catch (OfficeXmlFileException e2) {
                    fs = fs2;
                    if (fs != null) {
                        fs.close();
                    }
                    return createXSLFSlideShow(file, Boolean.valueOf(readOnly));
                } catch (RuntimeException e3) {
                    e = e3;
                    fs = fs2;
                    if (fs != null) {
                        fs.close();
                    }
                    throw e;
                }
            } catch (OfficeXmlFileException e4) {
                if (fs != null) {
                    fs.close();
                }
                return createXSLFSlideShow(file, Boolean.valueOf(readOnly));
            } catch (RuntimeException e5) {
                e = e5;
                if (fs != null) {
                    fs.close();
                }
                throw e;
            }
        }
        throw new FileNotFoundException(file.toString());
    }

    protected static SlideShow<?, ?> createHSLFSlideShow(Object... args) throws IOException, EncryptedDocumentException {
        return createSlideShow("org.apache.poi.hslf.usermodel.HSLFSlideShowFactory", args);
    }

    protected static SlideShow<?, ?> createXSLFSlideShow(Object... args) throws IOException, EncryptedDocumentException {
        return createSlideShow("org.apache.poi.xslf.usermodel.XSLFSlideShowFactory", args);
    }

    protected static SlideShow<?, ?> createSlideShow(String factoryClass, Object[] args) throws IOException, EncryptedDocumentException {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(factoryClass);
            Class<?>[] argsClz = new Class[args.length];
            Object[] arr$ = args;
            int len$ = arr$.length;
            int i$ = 0;
            int i = 0;
            while (i$ < len$) {
                Class<?> c = arr$[i$].getClass();
                if (Boolean.class.isAssignableFrom(c)) {
                    c = Boolean.TYPE;
                } else if (InputStream.class.isAssignableFrom(c)) {
                    c = InputStream.class;
                }
                int i2 = i + 1;
                argsClz[i] = c;
                i$++;
                i = i2;
            }
            return (SlideShow) clazz.getMethod("createSlideShow", argsClz).invoke(null, args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof IOException) {
                throw ((IOException) t);
            } else if (t instanceof EncryptedDocumentException) {
                throw ((EncryptedDocumentException) t);
            } else if (t instanceof OldFileFormatException) {
                throw ((OldFileFormatException) t);
            } else {
                throw new IOException(t);
            }
        } catch (Exception e2) {
            throw new IOException(e2);
        }
    }
}
