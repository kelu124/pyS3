package com.itextpdf.text.io;

import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;

public final class RandomAccessSourceFactory {
    private boolean exclusivelyLockFile = false;
    private boolean forceRead = false;
    private boolean usePlainRandomAccess = false;

    public RandomAccessSourceFactory setForceRead(boolean forceRead) {
        this.forceRead = forceRead;
        return this;
    }

    public RandomAccessSourceFactory setUsePlainRandomAccess(boolean usePlainRandomAccess) {
        this.usePlainRandomAccess = usePlainRandomAccess;
        return this;
    }

    public RandomAccessSourceFactory setExclusivelyLockFile(boolean exclusivelyLockFile) {
        this.exclusivelyLockFile = exclusivelyLockFile;
        return this;
    }

    public RandomAccessSource createSource(byte[] data) {
        return new ArrayRandomAccessSource(data);
    }

    public RandomAccessSource createSource(RandomAccessFile raf) throws IOException {
        return new RAFRandomAccessSource(raf);
    }

    public RandomAccessSource createSource(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            RandomAccessSource createSource = createSource(is);
            return createSource;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public RandomAccessSource createSource(InputStream is) throws IOException {
        try {
            RandomAccessSource createSource = createSource(StreamUtil.inputStreamToArray(is));
            return createSource;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public RandomAccessSource createBestSource(String filename) throws IOException {
        File file = new File(filename);
        if (file.canRead()) {
            if (this.forceRead) {
                return createByReadingToMemory(new FileInputStream(filename));
            }
            RandomAccessFile raf = new RandomAccessFile(file, this.exclusivelyLockFile ? "rw" : "r");
            if (this.exclusivelyLockFile) {
                raf.getChannel().lock();
            }
            try {
                return createBestSource(raf);
            } catch (IOException e) {
                try {
                    raf.close();
                } catch (IOException e2) {
                }
                throw e;
            } catch (RuntimeException e3) {
                try {
                    raf.close();
                } catch (IOException e4) {
                }
                throw e3;
            }
        } else if (filename.startsWith("file:/") || filename.startsWith("http://") || filename.startsWith("https://") || filename.startsWith("jar:") || filename.startsWith("wsjar:") || filename.startsWith("wsjar:") || filename.startsWith("vfszip:")) {
            return createSource(new URL(filename));
        } else {
            return createByReadingToMemory(filename);
        }
    }

    public RandomAccessSource createBestSource(RandomAccessFile raf) throws IOException {
        if (this.usePlainRandomAccess) {
            return new RAFRandomAccessSource(raf);
        }
        if (raf.length() <= 0) {
            return new RAFRandomAccessSource(raf);
        }
        try {
            return createBestSource(raf.getChannel());
        } catch (MapFailedException e) {
            return new RAFRandomAccessSource(raf);
        }
    }

    public RandomAccessSource createBestSource(FileChannel channel) throws IOException {
        if (channel.size() <= 67108864) {
            return new GetBufferedRandomAccessSource(new FileChannelRandomAccessSource(channel));
        }
        return new GetBufferedRandomAccessSource(new PagedChannelRandomAccessSource(channel));
    }

    public RandomAccessSource createRanged(RandomAccessSource source, long[] ranges) throws IOException {
        RandomAccessSource[] sources = new RandomAccessSource[(ranges.length / 2)];
        for (int i = 0; i < ranges.length; i += 2) {
            sources[i / 2] = new WindowRandomAccessSource(source, ranges[i], ranges[i + 1]);
        }
        return new GroupedRandomAccessSource(sources);
    }

    private RandomAccessSource createByReadingToMemory(String filename) throws IOException {
        InputStream is = StreamUtil.getResourceStream(filename);
        if (is != null) {
            return createByReadingToMemory(is);
        }
        throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", filename));
    }

    private RandomAccessSource createByReadingToMemory(InputStream is) throws IOException {
        try {
            RandomAccessSource arrayRandomAccessSource = new ArrayRandomAccessSource(StreamUtil.inputStreamToArray(is));
            return arrayRandomAccessSource;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }
}
