package org.bytedeco.javacpp.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bytedeco.javacpp.Loader;

@Mojo(defaultPhase = LifecyclePhase.PROCESS_CLASSES, name = "build")
public class BuildMojo extends AbstractMojo {
    @Parameter(property = "javacpp.classOrPackageName")
    String classOrPackageName = null;
    @Parameter(property = "javacpp.classOrPackageNames")
    String[] classOrPackageNames = null;
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "javacpp.classPath")
    String classPath = null;
    @Parameter(property = "javacpp.classPaths")
    String[] classPaths = null;
    @Parameter(defaultValue = "true", property = "javacpp.compile")
    boolean compile = true;
    @Parameter(property = "javacpp.compilerOptions")
    String[] compilerOptions = null;
    @Parameter(defaultValue = "false", property = "javacpp.copyLibs")
    boolean copyLibs = false;
    @Parameter(defaultValue = "true", property = "javacpp.deleteJniFiles")
    boolean deleteJniFiles = true;
    @Parameter(property = "javacpp.environmentVariables")
    Map<String, String> environmentVariables = null;
    @Parameter(defaultValue = "false", property = "javacpp.header")
    boolean header = false;
    @Parameter(property = "javacpp.includePath")
    String includePath = null;
    @Parameter(property = "javacpp.includePaths")
    String[] includePaths = null;
    @Parameter(property = "javacpp.jarPrefix")
    String jarPrefix = null;
    @Parameter(property = "javacpp.linkPath")
    String linkPath = null;
    @Parameter(property = "javacpp.linkPaths")
    String[] linkPaths = null;
    @Parameter(property = "javacpp.outputDirectory")
    File outputDirectory = null;
    @Parameter(property = "javacpp.outputName")
    String outputName = null;
    @Parameter(property = "javacpp.preloadPath")
    String preloadPath = null;
    @Parameter(property = "javacpp.preloadPaths")
    String[] preloadPaths = null;
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;
    @Parameter(property = "javacpp.properties")
    String properties = null;
    @Parameter(property = "javacpp.propertyFile")
    File propertyFile = null;
    @Parameter(property = "javacpp.propertyKeysAndValues")
    Properties propertyKeysAndValues = null;
    @Parameter(defaultValue = "false", property = "javacpp.skip")
    boolean skip = false;

    String[] merge(String[] ss, String s) {
        if (ss != null && s != null) {
            ss = (String[]) Arrays.copyOf(ss, ss.length + 1);
            ss[ss.length - 1] = s;
        } else if (s != null) {
            ss = new String[]{s};
        }
        return ss != null ? ss : new String[0];
    }

    public void execute() throws MojoExecutionException {
        Throwable e;
        final Log log = getLog();
        try {
            if (log.isDebugEnabled()) {
                log.debug("classPath: " + this.classPath);
                log.debug("classPaths: " + Arrays.deepToString(this.classPaths));
                log.debug("includePath: " + this.includePath);
                log.debug("includePaths: " + Arrays.deepToString(this.includePaths));
                log.debug("linkPath: " + this.linkPath);
                log.debug("linkPaths: " + Arrays.deepToString(this.linkPaths));
                log.debug("preloadPath: " + this.preloadPath);
                log.debug("preloadPaths: " + Arrays.deepToString(this.preloadPaths));
                log.debug("outputDirectory: " + this.outputDirectory);
                log.debug("outputName: " + this.outputName);
                log.debug("compile: " + this.compile);
                log.debug("deleteJniFiles: " + this.deleteJniFiles);
                log.debug("header: " + this.header);
                log.debug("copyLibs: " + this.copyLibs);
                log.debug("jarPrefix: " + this.jarPrefix);
                log.debug("properties: " + this.properties);
                log.debug("propertyFile: " + this.propertyFile);
                log.debug("propertyKeysAndValues: " + this.propertyKeysAndValues);
                log.debug("classOrPackageName: " + this.classOrPackageName);
                log.debug("classOrPackageNames: " + Arrays.deepToString(this.classOrPackageNames));
                log.debug("environmentVariables: " + this.environmentVariables);
                log.debug("compilerOptions: " + Arrays.deepToString(this.compilerOptions));
                log.debug("skip: " + this.skip);
            }
            if (this.skip) {
                log.info("Skipping execution of JavaCPP Builder");
                return;
            }
            String v;
            String str;
            String str2;
            this.classPaths = merge(this.classPaths, this.classPath);
            this.classOrPackageNames = merge(this.classOrPackageNames, this.classOrPackageName);
            Builder builder = new Builder(new Logger() {
                public void debug(String s) {
                    log.debug(s);
                }

                public void info(String s) {
                    log.info(s);
                }

                public void warn(String s) {
                    log.warn(s);
                }

                public void error(String s) {
                    log.error(s);
                }
            }).classPaths(this.classPaths).outputDirectory(this.outputDirectory).outputName(this.outputName).compile(this.compile).deleteJniFiles(this.deleteJniFiles).header(this.header).copyLibs(this.copyLibs).jarPrefix(this.jarPrefix).properties(this.properties).propertyFile(this.propertyFile).properties(this.propertyKeysAndValues).classesOrPackages(this.classOrPackageNames).environmentVariables(this.environmentVariables).compilerOptions(this.compilerOptions);
            Properties properties = builder.properties;
            log.info("Detected platform \"" + Loader.getPlatform() + "\"");
            log.info("Building for platform \"" + properties.get("platform") + "\"");
            String separator = properties.getProperty("platform.path.separator");
            for (String s : merge(this.includePaths, this.includePath)) {
                v = properties.getProperty("platform.includepath", "");
                str = "platform.includepath";
                str2 = (v.length() == 0 || v.endsWith(separator)) ? v + s : v + separator + s;
                properties.setProperty(str, str2);
            }
            for (String s2 : merge(this.linkPaths, this.linkPath)) {
                v = properties.getProperty("platform.linkpath", "");
                str = "platform.linkpath";
                str2 = (v.length() == 0 || v.endsWith(separator)) ? v + s2 : v + separator + s2;
                properties.setProperty(str, str2);
            }
            for (String s22 : merge(this.preloadPaths, this.preloadPath)) {
                v = properties.getProperty("platform.preloadpath", "");
                str = "platform.preloadpath";
                str2 = (v.length() == 0 || v.endsWith(separator)) ? v + s22 : v + separator + s22;
                properties.setProperty(str, str2);
            }
            Properties projectProperties = this.project.getProperties();
            for (String key : properties.stringPropertyNames()) {
                projectProperties.setProperty("javacpp." + key, properties.getProperty(key));
            }
            File[] outputFiles = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("outputFiles: " + Arrays.deepToString(outputFiles));
            }
        } catch (IOException e2) {
            e = e2;
            log.error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        } catch (ClassNotFoundException e3) {
            e = e3;
            log.error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        } catch (NoClassDefFoundError e4) {
            e = e4;
            log.error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        } catch (InterruptedException e5) {
            e = e5;
            log.error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        } catch (ParserException e6) {
            e = e6;
            log.error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        }
    }
}
