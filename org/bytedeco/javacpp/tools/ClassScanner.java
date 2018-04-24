package org.bytedeco.javacpp.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

class ClassScanner {
    final Collection<Class> classes;
    final UserClassLoader loader;
    final Logger logger;

    ClassScanner(Logger logger, Collection<Class> classes, UserClassLoader loader) {
        this.logger = logger;
        this.classes = classes;
        this.loader = loader;
    }

    public Collection<Class> getClasses() {
        return this.classes;
    }

    public UserClassLoader getClassLoader() {
        return this.loader;
    }

    public void addClass(String className) throws ClassNotFoundException, NoClassDefFoundError {
        if (className != null) {
            if (className.endsWith(".class")) {
                className = className.substring(0, className.length() - 6);
            }
            addClass(Class.forName(className, false, this.loader));
        }
    }

    public void addClass(Class c) {
        if (!this.classes.contains(c)) {
            this.classes.add(c);
        }
    }

    public void addMatchingFile(String filename, String packagePath, boolean recursive) throws ClassNotFoundException, NoClassDefFoundError {
        if (filename != null && filename.endsWith(".class")) {
            if (packagePath == null || ((recursive && filename.startsWith(packagePath)) || filename.regionMatches(0, packagePath, 0, Math.max(filename.lastIndexOf(47), packagePath.lastIndexOf(47))))) {
                addClass(filename.replace('/', '.'));
            }
        }
    }

    public void addMatchingDir(String parentName, File dir, String packagePath, boolean recursive) throws ClassNotFoundException, NoClassDefFoundError {
        File[] files = dir.listFiles();
        Arrays.sort(files);
        for (File f : files) {
            String pathName = parentName == null ? f.getName() : parentName + f.getName();
            if (f.isDirectory()) {
                addMatchingDir(pathName + "/", f, packagePath, recursive);
            } else {
                addMatchingFile(pathName, packagePath, recursive);
            }
        }
    }

    public void addPackage(String packageName, boolean recursive) throws IOException, ClassNotFoundException, NoClassDefFoundError {
        String[] paths = this.loader.getPaths();
        String packagePath = packageName == null ? null : packageName.replace('.', '/') + "/";
        int prevSize = this.classes.size();
        for (String p : paths) {
            File file = new File(p);
            if (file.isDirectory()) {
                addMatchingDir(null, file, packagePath, recursive);
            } else {
                JarInputStream jis = new JarInputStream(new FileInputStream(file));
                for (ZipEntry e = jis.getNextEntry(); e != null; e = jis.getNextEntry()) {
                    addMatchingFile(e.getName(), packagePath, recursive);
                    jis.closeEntry();
                }
                jis.close();
            }
        }
        if (this.classes.size() == 0 && packageName == null) {
            this.logger.warn("No classes found in the unnamed package");
            Builder.printHelp();
        } else if (prevSize == this.classes.size() && packageName != null) {
            this.logger.warn("No classes found in package " + packageName);
        }
    }

    public void addClassOrPackage(String name) throws IOException, ClassNotFoundException, NoClassDefFoundError {
        if (name != null) {
            name = name.replace('/', '.');
            if (name.endsWith(".**")) {
                addPackage(name.substring(0, name.length() - 3), true);
            } else if (name.endsWith(".*")) {
                addPackage(name.substring(0, name.length() - 2), false);
            } else {
                addClass(name);
            }
        }
    }
}
