package org.apache.poi.util;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

public class DefaultTempFileCreationStrategy implements TempFileCreationStrategy {
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    public static final String KEEP_FILES = "poi.keep.tmp.files";
    static final String POIFILES = "poifiles";
    private static final SecureRandom random = new SecureRandom();
    private File dir;

    public DefaultTempFileCreationStrategy() {
        this(null);
    }

    public DefaultTempFileCreationStrategy(File dir) {
        this.dir = dir;
    }

    private void createPOIFilesDirectory() throws IOException {
        if (this.dir == null) {
            String tmpDir = System.getProperty("java.io.tmpdir");
            if (tmpDir == null) {
                throw new IOException("Systems temporary directory not defined - set the -Djava.io.tmpdir jvm property!");
            }
            this.dir = new File(tmpDir, POIFILES);
        }
        createTempDirectory(this.dir);
    }

    private void createTempDirectory(File directory) throws IOException {
        if ((!directory.exists() && !directory.mkdirs()) || !directory.isDirectory()) {
            throw new IOException("Could not create temporary directory '" + directory + "'");
        }
    }

    public File createTempFile(String prefix, String suffix) throws IOException {
        createPOIFilesDirectory();
        File newFile = File.createTempFile(prefix, suffix, this.dir);
        if (System.getProperty(KEEP_FILES) == null) {
            newFile.deleteOnExit();
        }
        return newFile;
    }

    public File createTempDirectory(String prefix) throws IOException {
        createPOIFilesDirectory();
        File newDirectory = new File(this.dir, prefix + Long.toString(random.nextLong()));
        createTempDirectory(newDirectory);
        if (System.getProperty(KEEP_FILES) == null) {
            newDirectory.deleteOnExit();
        }
        return newDirectory;
    }
}
