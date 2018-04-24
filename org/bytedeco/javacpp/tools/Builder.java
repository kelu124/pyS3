package org.bytedeco.javacpp.tools;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.pdf.PdfBoolean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.Loader;

public class Builder {
    ClassScanner classScanner;
    boolean compile;
    Collection<String> compilerOptions;
    boolean copyLibs;
    boolean deleteJniFiles;
    Map<String, String> environmentVariables;
    boolean header;
    String jarPrefix;
    final Logger logger;
    File outputDirectory;
    String outputName;
    Properties properties;

    File parse(String[] classPath, Class cls) throws IOException, ParserException {
        return new Parser(this.logger, this.properties).parse(this.outputDirectory, classPath, cls);
    }

    void includeJavaPaths(ClassProperties properties, boolean header) {
        Exception e;
        if (!properties.getProperty("platform", "").startsWith("android")) {
            String platform = Loader.getPlatform();
            final String jvmlink = properties.getProperty("platform.link.prefix", "") + "jvm" + properties.getProperty("platform.link.suffix", "");
            final String jvmlib = properties.getProperty("platform.library.prefix", "") + "jvm" + properties.getProperty("platform.library.suffix", "");
            final String[] jnipath = new String[2];
            final String[] jvmpath = new String[2];
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (new File(dir, "jni.h").exists()) {
                        jnipath[0] = dir.getAbsolutePath();
                    }
                    if (new File(dir, "jni_md.h").exists()) {
                        jnipath[1] = dir.getAbsolutePath();
                    }
                    if (new File(dir, jvmlink).exists()) {
                        jvmpath[0] = dir.getAbsolutePath();
                    }
                    if (new File(dir, jvmlib).exists()) {
                        jvmpath[1] = dir.getAbsolutePath();
                    }
                    return new File(dir, name).isDirectory();
                }
            };
            try {
                ArrayList<File> dirs = new ArrayList(Arrays.asList(new File(System.getProperty("java.home")).getParentFile().getCanonicalFile().listFiles(filter)));
                while (!dirs.isEmpty()) {
                    File d = (File) dirs.remove(dirs.size() - 1);
                    String dpath = d.getPath();
                    File[] files = d.listFiles(filter);
                    if (!(dpath == null || files == null)) {
                        for (File f : files) {
                            File f2;
                            try {
                                f2 = f2.getCanonicalFile();
                            } catch (IOException e2) {
                            }
                            if (!dpath.startsWith(f2.getPath())) {
                                dirs.add(f2);
                            }
                        }
                    }
                }
                if (jnipath[0] != null && jnipath[0].equals(jnipath[1])) {
                    jnipath[1] = null;
                } else if (jnipath[0] == null) {
                    String macpath = "/System/Library/Frameworks/JavaVM.framework/Headers/";
                    if (new File(macpath).isDirectory()) {
                        jnipath[0] = macpath;
                    }
                }
                if (jvmpath[0] != null && jvmpath[0].equals(jvmpath[1])) {
                    jvmpath[1] = null;
                }
                properties.addAll("platform.includepath", jnipath);
                if (platform.equals(properties.getProperty("platform", platform))) {
                    if (header) {
                        properties.get("platform.link").add(0, "jvm");
                        properties.addAll("platform.linkpath", jvmpath);
                    }
                    if (platform.startsWith("macosx")) {
                        properties.addAll("platform.framework", "JavaVM");
                    }
                }
            } catch (IOException e3) {
                e = e3;
                this.logger.warn("Could not include header files from java.home:" + e);
            } catch (NullPointerException e4) {
                e = e4;
                this.logger.warn("Could not include header files from java.home:" + e);
            }
        }
    }

    int compile(String sourceFilename, String outputFilename, ClassProperties properties, File workingDirectory) throws IOException, InterruptedException {
        String s;
        ArrayList<String> command = new ArrayList();
        includeJavaPaths(properties, this.header);
        String platform = Loader.getPlatform();
        command.add(properties.getProperty("platform.compiler"));
        String p = properties.getProperty("platform.sysroot.prefix", "");
        for (String s2 : properties.get("platform.sysroot")) {
            if (new File(s2).isDirectory()) {
                if (p.endsWith(" ")) {
                    command.add(p.trim());
                    command.add(s2);
                } else {
                    command.add(p + s2);
                }
            }
        }
        p = properties.getProperty("platform.includepath.prefix", "");
        for (String s22 : properties.get("platform.includepath")) {
            if (new File(s22).isDirectory()) {
                if (p.endsWith(" ")) {
                    command.add(p.trim());
                    command.add(s22);
                } else {
                    command.add(p + s22);
                }
            }
        }
        command.add(sourceFilename);
        List<String> allOptions = properties.get("platform.compiler.*");
        if (!(allOptions.contains("!default") || allOptions.contains("default"))) {
            allOptions.add(0, "default");
        }
        for (String s222 : allOptions) {
            if (!(s222 == null || s222.length() == 0)) {
                p = "platform.compiler." + s222;
                String options = properties.getProperty(p);
                if (options != null && options.length() > 0) {
                    command.addAll(Arrays.asList(options.split(" ")));
                } else if (!("!default".equals(s222) || "default".equals(s222))) {
                    this.logger.warn("Could not get the property named \"" + p + "\"");
                }
            }
        }
        command.addAll(this.compilerOptions);
        String output = properties.getProperty("platform.compiler.output");
        int i = 1;
        while (true) {
            if (i >= 2 && output == null) {
                break;
            }
            if (output != null && output.length() > 0) {
                command.addAll(Arrays.asList(output.split(" ")));
            }
            if (output == null || output.length() == 0 || output.endsWith(" ")) {
                command.add(outputFilename);
            } else {
                command.add(((String) command.remove(command.size() - 1)) + outputFilename);
            }
            i++;
            output = properties.getProperty("platform.compiler.output" + i);
        }
        p = properties.getProperty("platform.linkpath.prefix", "");
        String p2 = properties.getProperty("platform.linkpath.prefix2");
        for (String s2222 : properties.get("platform.linkpath")) {
            if (new File(s2222).isDirectory()) {
                if (p.endsWith(" ")) {
                    command.add(p.trim());
                    command.add(s2222);
                } else {
                    command.add(p + s2222);
                }
                if (p2 != null) {
                    if (p2.endsWith(" ")) {
                        command.add(p2.trim());
                        command.add(s2222);
                    } else {
                        command.add(p2 + s2222);
                    }
                }
            }
        }
        p = properties.getProperty("platform.link.prefix", "");
        String x = properties.getProperty("platform.link.suffix", "");
        i = command.size();
        for (String s22222 : properties.get("platform.link")) {
            String[] libnameversion = s22222.split("#")[0].split("@");
            if (libnameversion.length == 3 && libnameversion[1].length() == 0) {
                s22222 = libnameversion[0] + libnameversion[2];
            } else {
                s22222 = libnameversion[0];
            }
            if (p.endsWith(" ") && x.startsWith(" ")) {
                command.add(i, p.trim());
                command.add(i + 1, s22222);
                command.add(i + 2, x.trim());
            } else if (p.endsWith(" ")) {
                command.add(i, p.trim());
                command.add(i + 1, s22222 + x);
            } else if (x.startsWith(" ")) {
                command.add(i, p + s22222);
                command.add(i + 1, x.trim());
            } else {
                command.add(i, p + s22222 + x);
            }
        }
        p = properties.getProperty("platform.frameworkpath.prefix", "");
        for (String s222222 : properties.get("platform.frameworkpath")) {
            if (new File(s222222).isDirectory()) {
                if (p.endsWith(" ")) {
                    command.add(p.trim());
                    command.add(s222222);
                } else {
                    command.add(p + s222222);
                }
            }
        }
        p = properties.getProperty("platform.framework.prefix", "");
        x = properties.getProperty("platform.framework.suffix", "");
        for (String s2222222 : properties.get("platform.framework")) {
            if (p.endsWith(" ") && x.startsWith(" ")) {
                command.add(p.trim());
                command.add(s2222222);
                command.add(x.trim());
            } else if (p.endsWith(" ")) {
                command.add(p.trim());
                command.add(s2222222 + x);
            } else if (x.startsWith(" ")) {
                command.add(p + s2222222);
                command.add(x.trim());
            } else {
                command.add(p + s2222222 + x);
            }
        }
        String text = "";
        boolean windows = platform.startsWith("windows");
        Iterator it = command.iterator();
        while (it.hasNext()) {
            s2222222 = (String) it.next();
            boolean hasSpaces = s2222222.indexOf(" ") > 0;
            if (hasSpaces) {
                text = text + (windows ? "\"" : "'");
            }
            text = text + s2222222;
            if (hasSpaces) {
                text = text + (windows ? "\"" : "'");
            }
            text = text + " ";
        }
        this.logger.info(text);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDirectory);
        if (this.environmentVariables != null) {
            pb.environment().putAll(this.environmentVariables);
        }
        return pb.inheritIO().start().waitFor();
    }

    File generateAndCompile(Class[] classes, String outputName) throws IOException, InterruptedException {
        String classPath;
        URISyntaxException e;
        IllegalArgumentException e2;
        File outputPath = this.outputDirectory;
        ClassProperties p = Loader.loadProperties(classes, this.properties, true);
        String platform = p.getProperty("platform");
        String sourcePrefix = new File(outputPath, outputName).getPath();
        String sourceSuffix = p.getProperty("platform.source.suffix", ".cpp");
        String libraryPath = p.getProperty("platform.library.path", "");
        String libraryName = p.getProperty("platform.library.prefix", "") + outputName + p.getProperty("platform.library.suffix", "");
        if (outputPath == null) {
            URI uri = null;
            try {
                String resourceName = '/' + classes[classes.length - 1].getName().replace('.', '/') + ".class";
                String resourceURL = classes[classes.length - 1].getResource(resourceName).toString();
                URI uri2 = new URI(resourceURL.substring(0, resourceURL.lastIndexOf(47) + 1));
                try {
                    File packageDir;
                    File targetDir;
                    boolean isFile = Annotation.FILE.equals(uri2.getScheme());
                    classPath = this.classScanner.getClassLoader().getPaths()[0];
                    if (isFile) {
                        packageDir = new File(uri2);
                    } else {
                        packageDir = new File(classPath, resourceName.substring(0, resourceName.lastIndexOf(47) + 1));
                    }
                    URI uri3 = new URI(resourceURL.substring(0, (resourceURL.length() - resourceName.length()) + 1));
                    File file;
                    if (libraryPath.length() <= 0) {
                        file = new File(packageDir, platform);
                    } else if (isFile) {
                        targetDir = new File(uri3);
                    } else {
                        file = new File(classPath);
                    }
                    File outputPath2 = new File(targetDir, libraryPath);
                    try {
                        sourcePrefix = new File(packageDir, outputName).getPath();
                        outputPath = outputPath2;
                    } catch (URISyntaxException e3) {
                        e = e3;
                        outputPath = outputPath2;
                        throw new RuntimeException(e);
                    } catch (IllegalArgumentException e4) {
                        e2 = e4;
                        outputPath = outputPath2;
                        throw new RuntimeException("URI: " + uri, e2);
                    }
                } catch (URISyntaxException e5) {
                    e = e5;
                    uri = uri2;
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e6) {
                    e2 = e6;
                    uri = uri2;
                    throw new RuntimeException("URI: " + uri, e2);
                }
            } catch (URISyntaxException e7) {
                e = e7;
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e8) {
                e2 = e8;
                throw new RuntimeException("URI: " + uri, e2);
            }
        }
        if (!outputPath.exists()) {
            outputPath.mkdirs();
        }
        Generator generator = new Generator(this.logger, p);
        String sourceFilename = sourcePrefix + sourceSuffix;
        String headerFilename = this.header ? sourcePrefix + ".h" : null;
        classPath = System.getProperty("java.class.path");
        for (String s : this.classScanner.getClassLoader().getPaths()) {
            classPath = classPath + File.pathSeparator + s;
        }
        this.logger.info("Generating " + sourceFilename);
        if (generator.generate(sourceFilename, headerFilename, classPath, classes)) {
            generator.close();
            if (!this.compile) {
                return new File(sourceFilename);
            }
            this.logger.info("Compiling " + outputPath.getPath() + File.separator + libraryName);
            int exitValue = compile(sourceFilename, libraryName, p, outputPath);
            if (exitValue == 0) {
                if (this.deleteJniFiles) {
                    this.logger.info("Deleting " + sourceFilename);
                    new File(sourceFilename).delete();
                } else {
                    this.logger.info("Keeping " + sourceFilename);
                }
                return new File(outputPath, libraryName);
            }
            System.exit(exitValue);
            return null;
        }
        this.logger.info("Nothing generated for " + sourceFilename);
        return null;
    }

    void createJar(File jarFile, String[] classPath, File... files) throws IOException {
        this.logger.info("Creating " + jarFile);
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        for (File f : files) {
            String name = f.getPath();
            if (classPath != null) {
                int i;
                String[] names = new String[classPath.length];
                for (i = 0; i < classPath.length; i++) {
                    String path = new File(classPath[i]).getCanonicalPath();
                    if (name.startsWith(path)) {
                        names[i] = name.substring(path.length() + 1);
                    }
                }
                i = 0;
                while (i < names.length) {
                    if (names[i] != null && names[i].length() < name.length()) {
                        name = names[i];
                    }
                    i++;
                }
            }
            ZipEntry e = new ZipEntry(name.replace(File.separatorChar, '/'));
            e.setTime(f.lastModified());
            jos.putNextEntry(e);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fis.read(buffer);
                if (length == -1) {
                    break;
                }
                jos.write(buffer, 0, length);
            }
            fis.close();
            jos.closeEntry();
        }
        jos.close();
    }

    public Builder() {
        this(Logger.create(Builder.class));
    }

    public Builder(Logger logger) {
        this.outputDirectory = null;
        this.outputName = null;
        this.jarPrefix = null;
        this.compile = true;
        this.deleteJniFiles = true;
        this.header = false;
        this.copyLibs = false;
        this.properties = null;
        this.classScanner = null;
        this.environmentVariables = null;
        this.compilerOptions = null;
        this.logger = logger;
        System.setProperty("org.bytedeco.javacpp.loadlibraries", PdfBoolean.FALSE);
        this.properties = Loader.loadProperties();
        this.classScanner = new ClassScanner(logger, new ArrayList(), new UserClassLoader(Thread.currentThread().getContextClassLoader()));
        this.compilerOptions = new ArrayList();
    }

    public Builder classPaths(String classPaths) {
        classPaths(classPaths == null ? null : classPaths.split(File.pathSeparator));
        return this;
    }

    public Builder classPaths(String... classPaths) {
        this.classScanner.getClassLoader().addPaths(classPaths);
        return this;
    }

    public Builder outputDirectory(String outputDirectory) {
        outputDirectory(outputDirectory == null ? null : new File(outputDirectory));
        return this;
    }

    public Builder outputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public Builder compile(boolean compile) {
        this.compile = compile;
        return this;
    }

    public Builder deleteJniFiles(boolean deleteJniFiles) {
        this.deleteJniFiles = deleteJniFiles;
        return this;
    }

    public Builder header(boolean header) {
        this.header = header;
        return this;
    }

    public Builder copyLibs(boolean copyLibs) {
        this.copyLibs = copyLibs;
        return this;
    }

    public Builder outputName(String outputName) {
        this.outputName = outputName;
        return this;
    }

    public Builder jarPrefix(String jarPrefix) {
        this.jarPrefix = jarPrefix;
        return this;
    }

    public Builder properties(String platform) {
        if (platform != null) {
            this.properties = Loader.loadProperties(platform, null);
        }
        return this;
    }

    public Builder properties(Properties properties) {
        if (properties != null) {
            for (Entry e : properties.entrySet()) {
                property((String) e.getKey(), (String) e.getValue());
            }
        }
        return this;
    }

    public Builder propertyFile(String filename) throws IOException {
        propertyFile(filename == null ? null : new File(filename));
        return this;
    }

    public Builder propertyFile(File propertyFile) throws IOException {
        if (propertyFile != null) {
            FileInputStream fis = new FileInputStream(propertyFile);
            this.properties = new Properties();
            try {
                this.properties.load(new InputStreamReader(fis));
            } catch (NoSuchMethodError e) {
                this.properties.load(fis);
            }
            fis.close();
        }
        return this;
    }

    public Builder property(String keyValue) {
        int equalIndex = keyValue.indexOf(61);
        if (equalIndex < 0) {
            equalIndex = keyValue.indexOf(58);
        }
        property(keyValue.substring(2, equalIndex), keyValue.substring(equalIndex + 1));
        return this;
    }

    public Builder property(String key, String value) {
        if (key.length() > 0 && value.length() > 0) {
            this.properties.put(key, value);
        }
        return this;
    }

    public Builder classesOrPackages(String... classesOrPackages) throws IOException, ClassNotFoundException, NoClassDefFoundError {
        if (classesOrPackages == null) {
            this.classScanner.addPackage(null, true);
        } else {
            for (String s : classesOrPackages) {
                this.classScanner.addClassOrPackage(s);
            }
        }
        return this;
    }

    public Builder environmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
        return this;
    }

    public Builder compilerOptions(String... options) {
        if (options != null) {
            this.compilerOptions.addAll(Arrays.asList(options));
        }
        return this;
    }

    public File[] build() throws IOException, InterruptedException, ParserException {
        if (this.classScanner.getClasses().isEmpty()) {
            return null;
        }
        ClassProperties p;
        List<File> outputFiles = new ArrayList();
        Map<String, LinkedHashSet<Class>> map = new LinkedHashMap();
        for (Class c : this.classScanner.getClasses()) {
            String libraryName;
            File f;
            if (Loader.getEnclosingClass(c) == c) {
                p = Loader.loadProperties(c, this.properties, false);
                if (p.isLoaded()) {
                    String target = p.getProperty("target");
                    if (target == null || c.getName().equals(target)) {
                        libraryName = this.outputName != null ? this.outputName : p.getProperty("platform.library", "");
                        if (libraryName.length() != 0) {
                            LinkedHashSet<Class> classList = (LinkedHashSet) map.get(libraryName);
                            if (classList == null) {
                                classList = new LinkedHashSet();
                                map.put(libraryName, classList);
                            }
                            classList.addAll(p.getEffectiveClasses());
                        }
                    } else {
                        f = parse(this.classScanner.getClassLoader().getPaths(), c);
                        if (f != null) {
                            outputFiles.add(f);
                        }
                    }
                } else {
                    this.logger.warn("Could not load platform properties for " + c);
                }
            }
        }
        for (String libraryName2 : map.keySet()) {
            File file;
            LinkedHashSet<Class> classSet = (LinkedHashSet) map.get(libraryName2);
            Class[] classArray = (Class[]) classSet.toArray(new Class[classSet.size()]);
            f = generateAndCompile(classArray, libraryName2);
            if (f != null) {
                outputFiles.add(f);
                if (this.copyLibs) {
                    p = Loader.loadProperties(classArray, this.properties, false);
                    List<String> preloads = new ArrayList();
                    preloads.addAll(p.get("platform.preload"));
                    preloads.addAll(p.get("platform.link"));
                    p = Loader.loadProperties(classArray, this.properties, true);
                    File directory = f.getParentFile();
                    for (String s : preloads) {
                        try {
                            File fi = new File(Loader.findLibrary(null, p, s, true)[0].toURI());
                            file = new File(directory, fi.getName());
                            if (fi.exists() && !outputFiles.contains(file)) {
                                this.logger.info("Copying " + fi);
                                FileInputStream fis = new FileInputStream(fi);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                while (true) {
                                    int length = fis.read(buffer);
                                    if (length == -1) {
                                        break;
                                    }
                                    fileOutputStream.write(buffer, 0, length);
                                }
                                fileOutputStream.close();
                                fis.close();
                                outputFiles.add(file);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        File[] files = (File[]) outputFiles.toArray(new File[outputFiles.size()]);
        if (this.jarPrefix != null && files.length > 0) {
            file = new File(this.jarPrefix + "-" + this.properties.get("platform") + ".jar");
            File d = file.getParentFile();
            if (!(d == null || d.exists())) {
                d.mkdir();
            }
            createJar(file, this.outputDirectory == null ? this.classScanner.getClassLoader().getPaths() : null, files);
        }
        System.setProperty("org.bytedeco.javacpp.loadlibraries", PdfBoolean.TRUE);
        return files;
    }

    public static void printHelp() {
        String version = Builder.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown";
        }
        System.out.println("JavaCPP version " + version + "\nCopyright (C) 2011-2016 Samuel Audet <samuel.audet@gmail.com>\nProject site: https://github.com/bytedeco/javacpp");
        System.out.println();
        System.out.println("Usage: java -jar javacpp.jar [options] [class or package (suffixed with .* or .**)]");
        System.out.println();
        System.out.println("where options include:");
        System.out.println();
        System.out.println("    -classpath <path>      Load user classes from path");
        System.out.println("    -d <directory>         Output all generated files to directory");
        System.out.println("    -o <name>              Output everything in a file named after given name");
        System.out.println("    -nocompile             Do not compile or delete the generated source files");
        System.out.println("    -nodelete              Do not delete generated C++ JNI files after compilation");
        System.out.println("    -header                Generate header file with declarations of callbacks functions");
        System.out.println("    -copylibs              Copy to output directory dependent libraries (link and preload)");
        System.out.println("    -jarprefix <prefix>    Also create a JAR file named \"<prefix>-<platform>.jar\"");
        System.out.println("    -properties <resource> Load all properties from resource");
        System.out.println("    -propertyfile <file>   Load all properties from file");
        System.out.println("    -D<property>=<value>   Set property to value");
        System.out.println("    -Xcompiler <option>    Pass option directly to compiler");
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        boolean addedClasses = false;
        Builder builder = new Builder();
        int i = 0;
        while (i < args.length) {
            if ("-help".equals(args[i]) || "--help".equals(args[i])) {
                printHelp();
                System.exit(0);
            } else if ("-classpath".equals(args[i]) || "-cp".equals(args[i]) || "-lib".equals(args[i])) {
                i++;
                builder.classPaths(args[i]);
            } else if ("-d".equals(args[i])) {
                i++;
                builder.outputDirectory(args[i]);
            } else if ("-o".equals(args[i])) {
                i++;
                builder.outputName(args[i]);
            } else if ("-cpp".equals(args[i]) || "-nocompile".equals(args[i])) {
                builder.compile(false);
            } else if ("-nodelete".equals(args[i])) {
                builder.deleteJniFiles(false);
            } else if ("-header".equals(args[i])) {
                builder.header(true);
            } else if ("-copylibs".equals(args[i])) {
                builder.copyLibs(true);
            } else if ("-jarprefix".equals(args[i])) {
                i++;
                builder.jarPrefix(args[i]);
            } else if ("-properties".equals(args[i])) {
                i++;
                builder.properties(args[i]);
            } else if ("-propertyfile".equals(args[i])) {
                i++;
                builder.propertyFile(args[i]);
            } else if (args[i].startsWith("-D")) {
                builder.property(args[i]);
            } else if ("-Xcompiler".equals(args[i])) {
                String[] strArr = new String[1];
                i++;
                strArr[0] = args[i];
                builder.compilerOptions(strArr);
            } else if (args[i].startsWith("-")) {
                builder.logger.error("Invalid option \"" + args[i] + "\"");
                printHelp();
                System.exit(1);
            } else {
                builder.classesOrPackages(args[i]);
                addedClasses = true;
            }
            i++;
        }
        if (!addedClasses) {
            builder.classesOrPackages((String[]) null);
        }
        builder.build();
    }
}
