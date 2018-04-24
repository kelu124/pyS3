package org.bytedeco.javacpp;

import com.itextpdf.text.pdf.PdfBoolean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.poi.util.TempFile;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.tools.Logger;

public class Loader {
    private static final String PLATFORM;
    static File cacheDir = null;
    static Map<String, String> loadedLibraries = Collections.synchronizedMap(new HashMap());
    private static final Logger logger = Logger.create(Loader.class);
    static WeakHashMap<Class<? extends Pointer>, HashMap<String, Integer>> memberOffsets = new WeakHashMap();
    private static Properties platformProperties = null;
    static File tempDir = null;

    static class C11981 extends SecurityManager {
        C11981() {
        }

        public Class[] getClassContext() {
            return super.getClassContext();
        }
    }

    static {
        String jvmName = System.getProperty("java.vm.name", "").toLowerCase();
        String osName = System.getProperty("os.name", "").toLowerCase();
        String osArch = System.getProperty("os.arch", "").toLowerCase();
        String abiType = System.getProperty("sun.arch.abi", "").toLowerCase();
        String libPath = System.getProperty("sun.boot.library.path", "").toLowerCase();
        if (jvmName.startsWith("dalvik") && osName.startsWith("linux")) {
            osName = "android";
        } else if (jvmName.startsWith("robovm") && osName.startsWith("darwin")) {
            osName = "ios";
            osArch = "arm";
        } else if (osName.startsWith("mac os x") || osName.startsWith("darwin")) {
            osName = "macosx";
        } else {
            int spaceIndex = osName.indexOf(32);
            if (spaceIndex > 0) {
                osName = osName.substring(0, spaceIndex);
            }
        }
        if (osArch.equals("i386") || osArch.equals("i486") || osArch.equals("i586") || osArch.equals("i686")) {
            osArch = "x86";
        } else if (osArch.equals("amd64") || osArch.equals("x86-64") || osArch.equals("x64")) {
            osArch = "x86_64";
        } else if (osArch.startsWith("aarch64") || osArch.startsWith("armv8") || osArch.startsWith("arm64")) {
            osArch = "arm64";
        } else if (osArch.startsWith("arm") && (abiType.equals("gnueabihf") || libPath.contains("openjdk-armhf"))) {
            osArch = "armhf";
        } else if (osArch.startsWith("arm")) {
            osArch = "arm";
        }
        PLATFORM = osName + "-" + osArch;
    }

    public static String getPlatform() {
        return System.getProperty("org.bytedeco.javacpp.platform", PLATFORM);
    }

    public static Properties loadProperties() {
        String name = getPlatform();
        if (platformProperties != null && name.equals(platformProperties.getProperty("platform"))) {
            return platformProperties;
        }
        Properties loadProperties = loadProperties(name, null);
        platformProperties = loadProperties;
        return loadProperties;
    }

    public static Properties loadProperties(String name, String defaults) {
        InputStream is2;
        if (defaults == null) {
            defaults = "generic";
        }
        Properties p = new Properties();
        p.put("platform", name);
        p.put("platform.path.separator", File.pathSeparator);
        String s = System.mapLibraryName("/");
        int i = s.indexOf(47);
        p.put("platform.library.prefix", s.substring(0, i));
        p.put("platform.library.suffix", s.substring(i + 1));
        InputStream is = Loader.class.getResourceAsStream("properties/" + name + ".properties");
        try {
            p.load(new InputStreamReader(is));
        } catch (NoSuchMethodError e) {
            try {
                p.load(is);
            } catch (Exception e2) {
                if (is2 != null) {
                    try {
                        is2.close();
                    } catch (IOException ex) {
                        logger.error("Unable to close resource : " + ex.getMessage());
                    }
                }
            } catch (Exception e3) {
                is2 = Loader.class.getResourceAsStream("properties/" + defaults + ".properties");
                try {
                    p.load(new InputStreamReader(is2));
                } catch (NoSuchMethodError e4) {
                    p.load(is2);
                }
                if (is2 != null) {
                    try {
                        is2.close();
                    } catch (IOException ex2) {
                        logger.error("Unable to close resource : " + ex2.getMessage());
                    }
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex22) {
                        logger.error("Unable to close resource : " + ex22.getMessage());
                    }
                }
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException ex222) {
                logger.error("Unable to close resource : " + ex222.getMessage());
            }
        }
        return p;
        if (is != null) {
            try {
                is.close();
            } catch (IOException ex2222) {
                logger.error("Unable to close resource : " + ex2222.getMessage());
            }
        }
        return p;
    }

    public static Class getEnclosingClass(Class cls) {
        Class<?> c = cls;
        while (c.getDeclaringClass() != null && !c.isAnnotationPresent(org.bytedeco.javacpp.annotation.Properties.class)) {
            if (c.isAnnotationPresent(Platform.class)) {
                Platform p = (Platform) c.getAnnotation(Platform.class);
                if (p.pragma().length > 0 || p.define().length > 0 || p.include().length > 0 || p.cinclude().length > 0 || p.includepath().length > 0 || p.compiler().length > 0 || p.linkpath().length > 0 || p.link().length > 0 || p.frameworkpath().length > 0 || p.framework().length > 0 || p.preloadpath().length > 0 || p.preload().length > 0 || p.library().length() > 0) {
                    break;
                }
            }
            c = c.getDeclaringClass();
        }
        return c;
    }

    public static ClassProperties loadProperties(Class[] cls, Properties properties, boolean inherit) {
        ClassProperties cp = new ClassProperties(properties);
        for (Class c : cls) {
            cp.load(c, inherit);
        }
        return cp;
    }

    public static ClassProperties loadProperties(Class cls, Properties properties, boolean inherit) {
        ClassProperties cp = new ClassProperties(properties);
        cp.load(cls, inherit);
        return cp;
    }

    public static Class getCallerClass(int i) {
        Class[] classContext = null;
        try {
            new C11981().getClassContext();
        } catch (NoSuchMethodError e) {
            logger.error("No definition of this method : " + e.getMessage());
        }
        int j;
        if (classContext != null) {
            for (j = 0; j < classContext.length; j++) {
                if (classContext[j] == Loader.class) {
                    return classContext[i + j];
                }
            }
        } else {
            try {
                StackTraceElement[] classNames = Thread.currentThread().getStackTrace();
                for (j = 0; j < classNames.length; j++) {
                    if (Class.forName(classNames[j].getClassName()) == Loader.class) {
                        return Class.forName(classNames[i + j].getClassName());
                    }
                }
            } catch (ClassNotFoundException e2) {
                logger.error("No definition for the class found : " + e2.getMessage());
            }
        }
        return null;
    }

    public static File cacheResource(String name) throws IOException {
        return cacheResource(getCallerClass(2), name);
    }

    public static File cacheResource(Class cls, String name) throws IOException {
        return cacheResource(cls.getResource(name));
    }

    public static File cacheResource(URL resourceURL) throws IOException {
        return cacheResource(resourceURL, null);
    }

    public static File cacheResource(URL resourceURL, String target) throws IOException {
        long timestamp;
        File urlFile = new File(resourceURL.getPath());
        String name = urlFile.getName();
        File cacheSubdir = getCacheDir().getCanonicalFile();
        URLConnection urlConnection = resourceURL.openConnection();
        long size;
        if (urlConnection instanceof JarURLConnection) {
            JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
            JarEntry jarEntry = ((JarURLConnection) urlConnection).getJarEntry();
            File jarFileFile = new File(jarFile.getName());
            File jarEntryFile = new File(jarEntry.getName());
            size = jarEntry.getSize();
            timestamp = jarEntry.getTime();
            cacheSubdir = new File(cacheSubdir, jarFileFile.getName() + File.separator + jarEntryFile.getParent());
        } else {
            size = urlFile.length();
            timestamp = urlFile.lastModified();
            cacheSubdir = new File(cacheSubdir, name);
        }
        if (resourceURL.getRef() != null) {
            name = resourceURL.getRef();
        }
        File file = new File(cacheSubdir, name);
        if (target != null && target.length() > 0) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating symbolic link to " + target);
                }
                Path path = file.toPath();
                Path targetPath = Paths.get(target, new String[0]);
                if ((file.exists() && Files.isSymbolicLink(path)) || !targetPath.isAbsolute() || targetPath.equals(path)) {
                    return file;
                }
                file.delete();
                Files.createSymbolicLink(path, targetPath, new FileAttribute[0]);
                return file;
            } catch (IOException e) {
                return null;
            }
        } else if (file.exists() && file.length() == size && file.lastModified() == timestamp && cacheSubdir.equals(file.getCanonicalFile().getParentFile())) {
            while (System.currentTimeMillis() - file.lastModified() >= 0 && System.currentTimeMillis() - file.lastModified() < 1000) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
            }
            return file;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Extracting " + resourceURL);
            }
            file.delete();
            extractResource(resourceURL, file, null, null);
            file.setLastModified(timestamp);
            return file;
        }
    }

    public static File extractResource(String name, File directory, String prefix, String suffix) throws IOException {
        return extractResource(getCallerClass(2), name, directory, prefix, suffix);
    }

    public static File extractResource(Class cls, String name, File directory, String prefix, String suffix) throws IOException {
        return extractResource(cls.getResource(name), directory, prefix, suffix);
    }

    public static File extractResource(URL resourceURL, File directoryOrFile, String prefix, String suffix) throws IOException {
        Throwable th;
        InputStream is = resourceURL != null ? resourceURL.openStream() : null;
        OutputStream outputStream = null;
        if (is == null) {
            return null;
        }
        File file = null;
        boolean fileExisted = false;
        if (prefix == null && suffix == null) {
            File directory;
            if (directoryOrFile == null) {
                try {
                    directoryOrFile = new File(System.getProperty(TempFile.JAVA_IO_TMPDIR));
                } catch (IOException e) {
                    e = e;
                    try {
                        IOException e2;
                        file.delete();
                        throw e2;
                    } catch (Throwable th2) {
                        th = th2;
                        is.close();
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        throw th;
                    }
                }
            }
            if (directoryOrFile.isDirectory()) {
                directory = directoryOrFile;
                file = new File(directoryOrFile, new File(resourceURL.getPath()).getName());
            } else {
                directory = directoryOrFile.getParentFile();
                file = directoryOrFile;
            }
            if (directory != null) {
                directory.mkdirs();
            }
            fileExisted = file.exists();
        } else {
            file = File.createTempFile(prefix, suffix, directoryOrFile);
        }
        OutputStream os = new FileOutputStream(file);
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            is.close();
            if (os != null) {
                os.close();
            }
            outputStream = os;
            return file;
        } catch (IOException e3) {
            e2 = e3;
            outputStream = os;
            if (!(file == null || fileExisted)) {
                file.delete();
            }
            throw e2;
        } catch (Throwable th3) {
            th = th3;
            outputStream = os;
            is.close();
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
    }

    public static File getCacheDir() {
        if (cacheDir == null) {
            String dirName = System.getProperty("org.bytedeco.javacpp.cachedir", System.getProperty("user.home") + "/.javacpp/cache/");
            if (dirName != null) {
                File f = new File(dirName);
                if (f.exists() || f.mkdirs()) {
                    cacheDir = f;
                }
            }
        }
        return cacheDir;
    }

    public static File getTempDir() {
        if (tempDir == null) {
            File tmpdir = new File(System.getProperty(TempFile.JAVA_IO_TMPDIR));
            for (int i = 0; i < 1000; i++) {
                File f = new File(tmpdir, "javacpp" + System.nanoTime());
                if (f.mkdir()) {
                    tempDir = f;
                    tempDir.deleteOnExit();
                    break;
                }
            }
        }
        return tempDir;
    }

    public static boolean isLoadLibraries() {
        String s = System.getProperty("org.bytedeco.javacpp.loadlibraries", PdfBoolean.TRUE).toLowerCase();
        return s.equals(PdfBoolean.TRUE) || s.equals("t") || s.equals("");
    }

    public static String load() {
        return load(getCallerClass(2), loadProperties(), false);
    }

    public static String load(boolean pathsFirst) {
        return load(getCallerClass(2), loadProperties(), pathsFirst);
    }

    public static String load(Class cls) {
        return load(cls, loadProperties(), false);
    }

    public static String load(Class cls, Properties properties, boolean pathsFirst) {
        if (!isLoadLibraries() || cls == null) {
            return null;
        }
        cls = getEnclosingClass(cls);
        ClassProperties p = loadProperties(cls, properties, true);
        List<String> targets = p.get("target");
        if (targets.isEmpty()) {
            if (p.getInheritedClasses() != null) {
                for (Class c : p.getInheritedClasses()) {
                    targets.add(c.getName());
                }
            }
            targets.add(cls.getName());
        }
        for (String s : targets) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading class " + s);
                }
                Class.forName(s, true, cls.getClassLoader());
            } catch (ClassNotFoundException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to load class " + s + ": " + ex);
                }
                Error e = new NoClassDefFoundError(ex.toString());
                e.initCause(ex);
                throw e;
            }
        }
        List<String> preloads = new ArrayList();
        preloads.addAll(p.get("platform.preload"));
        preloads.addAll(p.get("platform.link"));
        UnsatisfiedLinkError preloadError = null;
        for (String preload : preloads) {
            try {
                loadLibrary(findLibrary(cls, p, preload, pathsFirst), preload);
            } catch (UnsatisfiedLinkError e2) {
                preloadError = e2;
            }
        }
        try {
            String library = p.getProperty("platform.library");
            return loadLibrary(findLibrary(cls, p, library, pathsFirst), library);
        } catch (UnsatisfiedLinkError e22) {
            if (preloadError != null && e22.getCause() == null) {
                e22.initCause(preloadError);
            }
            throw e22;
        }
    }

    public static URL[] findLibrary(Class cls, ClassProperties properties, String libnameversion, boolean pathsFirst) {
        MalformedURLException ex;
        if (libnameversion.trim().endsWith("#")) {
            return new URL[0];
        }
        int i;
        String[] split = libnameversion.split("#");
        String[] s = split[0].split("@");
        String[] s2 = (split.length > 1 ? split[1] : split[0]).split("@");
        String libname = s[0];
        String libname2 = s2[0];
        String version = s.length > 1 ? s[s.length - 1] : "";
        String version2 = s2.length > 1 ? s2[s2.length - 1] : "";
        String subdir = properties.getProperty("platform") + '/';
        String prefix = properties.getProperty("platform.library.prefix", "");
        String suffix = properties.getProperty("platform.library.suffix", "");
        String[] styles = new String[]{prefix + libname + suffix + version, prefix + libname + version + suffix, prefix + libname + suffix};
        String[] styles2 = new String[]{prefix + libname2 + suffix + version2, prefix + libname2 + version2 + suffix, prefix + libname2 + suffix};
        String[] suffixes = (String[]) properties.get("platform.library.suffix").toArray(new String[0]);
        if (suffixes.length > 1) {
            styles = new String[(suffixes.length * 3)];
            styles2 = new String[(suffixes.length * 3)];
            for (i = 0; i < suffixes.length; i++) {
                styles[i * 3] = prefix + libname + suffixes[i] + version;
                styles[(i * 3) + 1] = prefix + libname + version + suffixes[i];
                styles[(i * 3) + 2] = prefix + libname + suffixes[i];
                styles2[i * 3] = prefix + libname2 + suffixes[i] + version2;
                styles2[(i * 3) + 1] = prefix + libname2 + version2 + suffixes[i];
                styles2[(i * 3) + 2] = prefix + libname2 + suffixes[i];
            }
        }
        List<String> paths = new ArrayList();
        paths.addAll(properties.get("platform.preloadpath"));
        paths.addAll(properties.get("platform.linkpath"));
        String libpath = System.getProperty("java.library.path", "");
        if (libpath.length() > 0) {
            paths.addAll(Arrays.asList(libpath.split(File.pathSeparator)));
        }
        ArrayList<URL> arrayList = new ArrayList(styles.length * (paths.size() + 1));
        i = 0;
        while (cls != null && i < styles.length) {
            URL u = cls.getResource(subdir + styles[i]);
            if (u != null) {
                if (!styles[i].equals(styles2[i])) {
                    try {
                        u = new URL(u + "#" + styles2[i]);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
                arrayList.add(u);
            }
            i++;
        }
        int size = pathsFirst ? 0 : arrayList.size();
        i = 0;
        while (paths.size() > 0 && i < styles.length) {
            for (String path : paths) {
                File file = new File(path, styles[i]);
                if (file.exists()) {
                    try {
                        u = file.toURI().toURL();
                        if (!styles[i].equals(styles2[i])) {
                            u = new URL(u + "#" + styles2[i]);
                        }
                        int k = size + 1;
                        try {
                            arrayList.add(size, u);
                            size = k;
                        } catch (MalformedURLException e2) {
                            ex = e2;
                            size = k;
                        }
                    } catch (MalformedURLException e3) {
                        ex = e3;
                    }
                }
            }
            i++;
        }
        return (URL[]) arrayList.toArray(new URL[arrayList.size()]);
        throw new RuntimeException(ex);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String loadLibrary(java.net.URL[] r18, java.lang.String r19) {
        /*
        r13 = isLoadLibraries();
        if (r13 != 0) goto L_0x0008;
    L_0x0006:
        r5 = 0;
    L_0x0007:
        return r5;
    L_0x0008:
        r13 = "#";
        r0 = r19;
        r10 = r0.split(r13);
        r13 = 0;
        r8 = r10[r13];
        r13 = r10.length;
        r14 = 1;
        if (r13 <= r14) goto L_0x001a;
    L_0x0017:
        r13 = 1;
        r8 = r10[r13];
    L_0x001a:
        r13 = loadedLibraries;
        r5 = r13.get(r8);
        r5 = (java.lang.String) r5;
        r9 = 0;
        r0 = r18;
        r14 = r0.length;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r13 = 0;
    L_0x0027:
        if (r13 >= r14) goto L_0x0117;
    L_0x0029:
        r12 = r18[r13];	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r11 = r12.toURI();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r4 = new java.io.File;	 Catch:{ Exception -> 0x006e }
        r4.<init>(r11);	 Catch:{ Exception -> 0x006e }
    L_0x0034:
        if (r5 != 0) goto L_0x0007;
    L_0x0036:
        if (r4 == 0) goto L_0x0113;
    L_0x0038:
        r15 = r4.exists();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        if (r15 == 0) goto L_0x0113;
    L_0x003e:
        r6 = r4.getAbsolutePath();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15 = r15.isDebugEnabled();	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        if (r15 == 0) goto L_0x0064;
    L_0x004a:
        r15 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16 = new java.lang.StringBuilder;	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16.<init>();	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r17 = "Loading ";
        r16 = r16.append(r17);	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r0 = r16;
        r16 = r0.append(r6);	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16 = r16.toString();	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15.debug(r16);	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
    L_0x0064:
        r15 = loadedLibraries;	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15.put(r8, r6);	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        java.lang.System.load(r6);	 Catch:{ UnsatisfiedLinkError -> 0x00de, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r5 = r6;
        goto L_0x0007;
    L_0x006e:
        r3 = move-exception;
        r4 = new java.io.File;	 Catch:{ Exception -> 0x00dc }
        r15 = r11.getPath();	 Catch:{ Exception -> 0x00dc }
        r4.<init>(r15);	 Catch:{ Exception -> 0x00dc }
        r15 = r4.exists();	 Catch:{ Exception -> 0x00dc }
        if (r15 == 0) goto L_0x00a7;
    L_0x007e:
        r6 = r4.getAbsolutePath();	 Catch:{ Exception -> 0x00dc }
        r15 = logger;	 Catch:{ Exception -> 0x00dc }
        r15 = r15.isDebugEnabled();	 Catch:{ Exception -> 0x00dc }
        if (r15 == 0) goto L_0x00a4;
    L_0x008a:
        r15 = logger;	 Catch:{ Exception -> 0x00dc }
        r16 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00dc }
        r16.<init>();	 Catch:{ Exception -> 0x00dc }
        r17 = "Preloading ";
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x00dc }
        r0 = r16;
        r16 = r0.append(r6);	 Catch:{ Exception -> 0x00dc }
        r16 = r16.toString();	 Catch:{ Exception -> 0x00dc }
        r15.debug(r16);	 Catch:{ Exception -> 0x00dc }
    L_0x00a4:
        java.lang.System.load(r6);	 Catch:{ UnsatisfiedLinkError -> 0x00ac, Exception -> 0x00dc, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
    L_0x00a7:
        r4 = cacheResource(r12, r5);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        goto L_0x0034;
    L_0x00ac:
        r1 = move-exception;
        r15 = logger;	 Catch:{ Exception -> 0x00dc }
        r15 = r15.isDebugEnabled();	 Catch:{ Exception -> 0x00dc }
        if (r15 == 0) goto L_0x00a7;
    L_0x00b5:
        r15 = logger;	 Catch:{ Exception -> 0x00dc }
        r16 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00dc }
        r16.<init>();	 Catch:{ Exception -> 0x00dc }
        r17 = "Failed to preload ";
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x00dc }
        r0 = r16;
        r16 = r0.append(r6);	 Catch:{ Exception -> 0x00dc }
        r17 = ": ";
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x00dc }
        r0 = r16;
        r16 = r0.append(r1);	 Catch:{ Exception -> 0x00dc }
        r16 = r16.toString();	 Catch:{ Exception -> 0x00dc }
        r15.debug(r16);	 Catch:{ Exception -> 0x00dc }
        goto L_0x00a7;
    L_0x00dc:
        r15 = move-exception;
        goto L_0x00a7;
    L_0x00de:
        r1 = move-exception;
        r9 = r1;
        r15 = loadedLibraries;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15.remove(r8);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15 = r15.isDebugEnabled();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        if (r15 == 0) goto L_0x0113;
    L_0x00ed:
        r15 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16 = new java.lang.StringBuilder;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16.<init>();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r17 = "Failed to load ";
        r16 = r16.append(r17);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r0 = r16;
        r16 = r0.append(r6);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r17 = ": ";
        r16 = r16.append(r17);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r0 = r16;
        r16 = r0.append(r1);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r16 = r16.toString();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15.debug(r16);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
    L_0x0113:
        r13 = r13 + 1;
        goto L_0x0027;
    L_0x0117:
        if (r5 != 0) goto L_0x0007;
    L_0x0119:
        r13 = "#";
        r0 = r19;
        r13 = r0.split(r13);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = 0;
        r13 = r13[r14];	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = "@";
        r13 = r13.split(r14);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = 0;
        r7 = r13[r14];	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r13 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r13 = r13.isDebugEnabled();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        if (r13 == 0) goto L_0x014d;
    L_0x0135:
        r13 = logger;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = new java.lang.StringBuilder;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14.<init>();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r15 = "Loading library ";
        r14 = r14.append(r15);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = r14.append(r7);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r14 = r14.toString();	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r13.debug(r14);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
    L_0x014d:
        r13 = loadedLibraries;	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r13.put(r8, r7);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        java.lang.System.loadLibrary(r7);	 Catch:{ UnsatisfiedLinkError -> 0x0158, IOException -> 0x01e0, URISyntaxException -> 0x0196 }
        r5 = r7;
        goto L_0x0007;
    L_0x0158:
        r1 = move-exception;
        r13 = loadedLibraries;
        r13.remove(r8);
        if (r9 == 0) goto L_0x0169;
    L_0x0160:
        r13 = r1.getCause();
        if (r13 != 0) goto L_0x0169;
    L_0x0166:
        r1.initCause(r9);
    L_0x0169:
        r13 = logger;
        r13 = r13.isDebugEnabled();
        if (r13 == 0) goto L_0x0195;
    L_0x0171:
        r13 = logger;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Failed to load for ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r15 = ": ";
        r14 = r14.append(r15);
        r14 = r14.append(r1);
        r14 = r14.toString();
        r13.debug(r14);
    L_0x0195:
        throw r1;
    L_0x0196:
        r2 = move-exception;
    L_0x0197:
        r13 = loadedLibraries;
        r13.remove(r8);
        if (r9 == 0) goto L_0x01a7;
    L_0x019e:
        r13 = r2.getCause();
        if (r13 != 0) goto L_0x01a7;
    L_0x01a4:
        r2.initCause(r9);
    L_0x01a7:
        r1 = new java.lang.UnsatisfiedLinkError;
        r13 = r2.toString();
        r1.<init>(r13);
        r1.initCause(r2);
        r13 = logger;
        r13 = r13.isDebugEnabled();
        if (r13 == 0) goto L_0x01df;
    L_0x01bb:
        r13 = logger;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Failed to extract for ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r15 = ": ";
        r14 = r14.append(r15);
        r14 = r14.append(r1);
        r14 = r14.toString();
        r13.debug(r14);
    L_0x01df:
        throw r1;
    L_0x01e0:
        r2 = move-exception;
        goto L_0x0197;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.Loader.loadLibrary(java.net.URL[], java.lang.String):java.lang.String");
    }

    static Class putMemberOffset(String typeName, String member, int offset) throws ClassNotFoundException {
        Class<?> c = Class.forName(typeName.replace('/', '.'), false, Loader.class.getClassLoader());
        if (member != null) {
            putMemberOffset(c.asSubclass(Pointer.class), member, offset);
        }
        return c;
    }

    static synchronized void putMemberOffset(Class<? extends Pointer> type, String member, int offset) {
        synchronized (Loader.class) {
            HashMap<String, Integer> offsets = (HashMap) memberOffsets.get(type);
            if (offsets == null) {
                WeakHashMap weakHashMap = memberOffsets;
                offsets = new HashMap();
                weakHashMap.put(type, offsets);
            }
            offsets.put(member, Integer.valueOf(offset));
        }
    }

    public static int offsetof(Class<? extends Pointer> type, String member) {
        return ((Integer) ((HashMap) memberOffsets.get(type)).get(member)).intValue();
    }

    public static int sizeof(Class<? extends Pointer> type) {
        return ((Integer) ((HashMap) memberOffsets.get(type)).get("sizeof")).intValue();
    }
}
