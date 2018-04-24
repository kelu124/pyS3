package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.poi.util.TempFile;

@GwtIncompatible
@Beta
public final class Files {
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new C07492();
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    static class C07481 implements LineProcessor<List<String>> {
        final List<String> result = Lists.newArrayList();

        C07481() {
        }

        public boolean processLine(String line) {
            this.result.add(line);
            return true;
        }

        public List<String> getResult() {
            return this.result;
        }
    }

    static class C07492 extends TreeTraverser<File> {
        C07492() {
        }

        public Iterable<File> children(File file) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    return Collections.unmodifiableList(Arrays.asList(files));
                }
            }
            return Collections.emptyList();
        }

        public String toString() {
            return "Files.fileTreeTraverser()";
        }
    }

    private static final class FileByteSink extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File file, FileWriteMode... modes) {
            this.file = (File) Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf((Object[]) modes);
        }

        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }

        public String toString() {
            return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
        }
    }

    private static final class FileByteSource extends ByteSource {
        private final File file;

        private FileByteSource(File file) {
            this.file = (File) Preconditions.checkNotNull(file);
        }

        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        public Optional<Long> sizeIfKnown() {
            if (this.file.isFile()) {
                return Optional.of(Long.valueOf(this.file.length()));
            }
            return Optional.absent();
        }

        public long size() throws IOException {
            if (this.file.isFile()) {
                return this.file.length();
            }
            throw new FileNotFoundException(this.file.toString());
        }

        public byte[] read() throws IOException {
            Closer closer = Closer.create();
            try {
                FileInputStream in = (FileInputStream) closer.register(openStream());
                byte[] readFile = Files.readFile(in, in.getChannel().size());
                closer.close();
                return readFile;
            } catch (Throwable th) {
                closer.close();
            }
        }

        public String toString() {
            return "Files.asByteSource(" + this.file + ")";
        }
    }

    private enum FilePredicate implements Predicate<File> {
        IS_DIRECTORY {
            public boolean apply(File file) {
                return file.isDirectory();
            }

            public String toString() {
                return "Files.isDirectory()";
            }
        },
        IS_FILE {
            public boolean apply(File file) {
                return file.isFile();
            }

            public String toString() {
                return "Files.isFile()";
            }
        }
    }

    private Files() {
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static ByteSource asByteSource(File file) {
        return new FileByteSource(file);
    }

    static byte[] readFile(InputStream in, long expectedSize) throws IOException {
        if (expectedSize <= 2147483647L) {
            return expectedSize == 0 ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int) expectedSize);
        } else {
            throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
        }
    }

    public static ByteSink asByteSink(File file, FileWriteMode... modes) {
        return new FileByteSink(file, modes);
    }

    public static CharSource asCharSource(File file, Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }

    public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
        return asByteSink(file, modes).asCharSink(charset);
    }

    private static FileWriteMode[] modes(boolean append) {
        if (!append) {
            return new FileWriteMode[0];
        }
        return new FileWriteMode[]{FileWriteMode.APPEND};
    }

    public static byte[] toByteArray(File file) throws IOException {
        return asByteSource(file).read();
    }

    public static String toString(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }

    public static void write(byte[] from, File to) throws IOException {
        asByteSink(to, new FileWriteMode[0]).write(from);
    }

    public static void copy(File from, OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }

    public static void copy(File from, File to) throws IOException {
        boolean z;
        if (from.equals(to)) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z, "Source %s and destination %s must be different", (Object) from, (Object) to);
        asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
    }

    public static void write(CharSequence from, File to, Charset charset) throws IOException {
        asCharSink(to, charset, new FileWriteMode[0]).write(from);
    }

    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        write(from, to, charset, true);
    }

    private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
        asCharSink(to, charset, modes(append)).write(from);
    }

    public static void copy(File from, Charset charset, Appendable to) throws IOException {
        asCharSource(from, charset).copyTo(to);
    }

    public static boolean equal(File file1, File file2) throws IOException {
        Preconditions.checkNotNull(file1);
        Preconditions.checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        long len1 = file1.length();
        long len2 = file2.length();
        if (len1 == 0 || len2 == 0 || len1 == len2) {
            return asByteSource(file1).contentEquals(asByteSource(file2));
        }
        return false;
    }

    public static File createTempDir() {
        File baseDir = new File(System.getProperty(TempFile.JAVA_IO_TMPDIR));
        String baseName = System.currentTimeMillis() + "-";
        for (int counter = 0; counter < 10000; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + 9999 + ')');
    }

    public static void touch(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    public static void createParentDirs(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File parent = file.getCanonicalFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
            if (!parent.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
    }

    public static void move(File from, File to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", (Object) from, (Object) to);
        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (to.delete()) {
                    throw new IOException("Unable to delete " + from);
                }
                throw new IOException("Unable to delete " + to);
            }
        }
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return (List) readLines(file, charset, new C07481());
    }

    @CanIgnoreReturnValue
    public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
        return asCharSource(file, charset).readLines(callback);
    }

    @CanIgnoreReturnValue
    public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
        return asByteSource(file).read(processor);
    }

    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        Preconditions.checkNotNull(file);
        return map(file, MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, MapMode mode) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        if (file.exists()) {
            return map(file, mode, file.length());
        }
        throw new FileNotFoundException(file.toString());
    }

    public static MappedByteBuffer map(File file, MapMode mode, long size) throws FileNotFoundException, IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        Closer closer = Closer.create();
        try {
            MappedByteBuffer map = map((RandomAccessFile) closer.register(new RandomAccessFile(file, mode == MapMode.READ_ONLY ? "r" : "rw")), mode, size);
            closer.close();
            return map;
        } catch (Throwable th) {
            closer.close();
        }
    }

    private static MappedByteBuffer map(RandomAccessFile raf, MapMode mode, long size) throws IOException {
        Closer closer = Closer.create();
        try {
            MappedByteBuffer map = ((FileChannel) closer.register(raf.getChannel())).map(mode, 0, size);
            closer.close();
            return map;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static String simplifyPath(String pathname) {
        Preconditions.checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
        Iterable path = new ArrayList();
        for (String component : components) {
            if (!component.equals(".")) {
                if (!component.equals("..")) {
                    path.add(component);
                } else if (path.size() <= 0 || ((String) path.get(path.size() - 1)).equals("..")) {
                    path.add("..");
                } else {
                    path.remove(path.size() - 1);
                }
            }
        }
        String result = Joiner.on('/').join(path);
        if (pathname.charAt(0) == '/') {
            result = "/" + result;
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            return "/";
        }
        if ("".equals(result)) {
            return ".";
        }
        return result;
    }

    public static String getFileExtension(String fullName) {
        Preconditions.checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file) {
        Preconditions.checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    public static TreeTraverser<File> fileTreeTraverser() {
        return FILE_TREE_TRAVERSER;
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
}
