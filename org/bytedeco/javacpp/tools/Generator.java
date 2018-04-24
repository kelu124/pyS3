package org.bytedeco.javacpp.tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bytedeco.javacpp.BoolPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.CLongPointer;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.FunctionPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.ShortPointer;
import org.bytedeco.javacpp.SizeTPointer;
import org.bytedeco.javacpp.annotation.Adapter;
import org.bytedeco.javacpp.annotation.Allocator;
import org.bytedeco.javacpp.annotation.ArrayAllocator;
import org.bytedeco.javacpp.annotation.ByPtr;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByPtrRef;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Convention;
import org.bytedeco.javacpp.annotation.Function;
import org.bytedeco.javacpp.annotation.Index;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.MemberSetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoDeallocator;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.annotation.ValueGetter;
import org.bytedeco.javacpp.annotation.ValueSetter;
import org.bytedeco.javacpp.annotation.Virtual;

public class Generator implements Closeable {
    static final String JNI_VERSION = "JNI_VERSION_1_4";
    static final List<Class> baseClasses = Arrays.asList(new Class[]{Pointer.class, BytePointer.class, ShortPointer.class, IntPointer.class, LongPointer.class, FloatPointer.class, DoublePointer.class, CharPointer.class, PointerPointer.class, BoolPointer.class, CLongPointer.class, SizeTPointer.class});
    Map<Method, MethodInformation> annotationCache;
    IndexedSet<Class> arrayDeallocators;
    IndexedSet<String> callbacks;
    IndexedSet<Class> deallocators;
    IndexedSet<Class> functions;
    IndexedSet<Class> jclasses;
    final Logger logger;
    boolean mayThrowExceptions;
    Map<Class, Set<String>> members;
    PrintWriter out;
    PrintWriter out2;
    boolean passesStrings;
    final ClassProperties properties;
    boolean usesAdapters;
    Map<Class, Set<String>> virtualFunctions;
    Map<Class, Set<String>> virtualMembers;

    class C12351 extends Writer {
        C12351() {
        }

        public void write(char[] cbuf, int off, int len) {
        }

        public void flush() {
        }

        public void close() {
        }
    }

    public Generator(Logger logger, ClassProperties properties) {
        this.logger = logger;
        this.properties = properties;
    }

    public boolean generate(String sourceFilename, String headerFilename, String classPath, Class<?>... classes) throws FileNotFoundException {
        this.out = new PrintWriter(new C12351());
        this.out2 = null;
        this.callbacks = new IndexedSet();
        this.functions = new IndexedSet();
        this.deallocators = new IndexedSet();
        this.arrayDeallocators = new IndexedSet();
        this.jclasses = new IndexedSet();
        this.members = new HashMap();
        this.virtualFunctions = new HashMap();
        this.virtualMembers = new HashMap();
        this.annotationCache = new HashMap();
        this.mayThrowExceptions = false;
        this.usesAdapters = false;
        this.passesStrings = false;
        for (Class<?> cls : baseClasses) {
            this.jclasses.index(cls);
        }
        if (!classes(true, true, true, classPath, classes)) {
            return false;
        }
        File sourceFile = new File(sourceFilename);
        File sourceDir = sourceFile.getParentFile();
        if (sourceDir != null) {
            sourceDir.mkdirs();
        }
        this.out = new PrintWriter(sourceFile);
        if (headerFilename != null) {
            File headerFile = new File(headerFilename);
            File headerDir = headerFile.getParentFile();
            if (headerDir != null) {
                headerDir.mkdirs();
            }
            this.out2 = new PrintWriter(headerFile);
        }
        return classes(this.mayThrowExceptions, this.usesAdapters, this.passesStrings, classPath, classes);
    }

    public void close() {
        if (this.out != null) {
            this.out.close();
        }
        if (this.out2 != null) {
            this.out2.close();
        }
    }

    boolean classes(boolean handleExceptions, boolean defineAdapters, boolean convertStrings, String classPath, Class<?>... classes) {
        Iterator it;
        String version = Generator.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown";
        }
        String warning = "// Generated by JavaCPP version " + version + ": DO NOT EDIT THIS FILE";
        this.out.println(warning);
        this.out.println();
        if (this.out2 != null) {
            this.out2.println(warning);
            this.out2.println();
        }
        for (String s : this.properties.get("platform.pragma")) {
            this.out.println("#pragma " + s);
        }
        for (String s2 : this.properties.get("platform.define")) {
            this.out.println("#define " + s2);
        }
        this.out.println();
        this.out.println("#ifdef _WIN32");
        this.out.println("    #define _JAVASOFT_JNI_MD_H_");
        this.out.println();
        this.out.println("    #define JNIEXPORT __declspec(dllexport)");
        this.out.println("    #define JNIIMPORT __declspec(dllimport)");
        this.out.println("    #define JNICALL __stdcall");
        this.out.println();
        this.out.println("    typedef int jint;");
        this.out.println("    typedef long long jlong;");
        this.out.println("    typedef signed char jbyte;");
        this.out.println("#elif defined(__GNUC__)");
        this.out.println("    #define _JAVASOFT_JNI_MD_H_");
        this.out.println();
        this.out.println("    #define JNIEXPORT __attribute__((visibility(\"default\")))");
        this.out.println("    #define JNIIMPORT");
        this.out.println("    #define JNICALL");
        this.out.println();
        this.out.println("    typedef int jint;");
        this.out.println("    typedef long long jlong;");
        this.out.println("    typedef signed char jbyte;");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#include <jni.h>");
        if (this.out2 != null) {
            this.out2.println("#include <jni.h>");
        }
        this.out.println();
        this.out.println("#ifdef __ANDROID__");
        this.out.println("    #include <android/log.h>");
        this.out.println("#elif defined(__APPLE__) && defined(__OBJC__)");
        this.out.println("    #include <TargetConditionals.h>");
        this.out.println("    #include <Foundation/Foundation.h>");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#ifdef __linux__");
        this.out.println("    #include <sys/types.h>");
        this.out.println("    #include <sys/stat.h>");
        this.out.println("    #include <fcntl.h>");
        this.out.println("    #include <unistd.h>");
        this.out.println("#elif defined(__APPLE__)");
        this.out.println("    #include <mach/mach_init.h>");
        this.out.println("    #include <mach/task.h>");
        this.out.println("#elif defined(_WIN32)");
        this.out.println("    #define NOMINMAX");
        this.out.println("    #include <windows.h>");
        this.out.println("    #include <psapi.h>");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#if defined(__ANDROID__) || TARGET_OS_IPHONE");
        this.out.println("    #define NewWeakGlobalRef(obj) NewGlobalRef(obj)");
        this.out.println("    #define DeleteWeakGlobalRef(obj) DeleteGlobalRef(obj)");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#include <limits.h>");
        this.out.println("#include <stddef.h>");
        this.out.println("#ifndef _WIN32");
        this.out.println("    #include <stdint.h>");
        this.out.println("#endif");
        this.out.println("#include <stdio.h>");
        this.out.println("#include <stdlib.h>");
        this.out.println("#include <string.h>");
        this.out.println("#include <exception>");
        this.out.println("#include <memory>");
        this.out.println("#include <new>");
        this.out.println();
        this.out.println("#if defined(NATIVE_ALLOCATOR) && defined(NATIVE_DEALLOCATOR)");
        this.out.println("    void* operator new(std::size_t size, const std::nothrow_t&) throw() {");
        this.out.println("        return NATIVE_ALLOCATOR(size);");
        this.out.println("    }");
        this.out.println("    void* operator new[](std::size_t size, const std::nothrow_t&) throw() {");
        this.out.println("        return NATIVE_ALLOCATOR(size);");
        this.out.println("    }");
        this.out.println("    void* operator new(std::size_t size) throw(std::bad_alloc) {");
        this.out.println("        return NATIVE_ALLOCATOR(size);");
        this.out.println("    }");
        this.out.println("    void* operator new[](std::size_t size) throw(std::bad_alloc) {");
        this.out.println("        return NATIVE_ALLOCATOR(size);");
        this.out.println("    }");
        this.out.println("    void operator delete(void* ptr) throw() {");
        this.out.println("        NATIVE_DEALLOCATOR(ptr);");
        this.out.println("    }");
        this.out.println("    void operator delete[](void* ptr) throw() {");
        this.out.println("        NATIVE_DEALLOCATOR(ptr);");
        this.out.println("    }");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#define jlong_to_ptr(a) ((void*)(uintptr_t)(a))");
        this.out.println("#define ptr_to_jlong(a) ((jlong)(uintptr_t)(a))");
        this.out.println();
        this.out.println("#if defined(_MSC_VER)");
        this.out.println("    #define JavaCPP_noinline __declspec(noinline)");
        this.out.println("    #define JavaCPP_hidden /* hidden by default */");
        this.out.println("#elif defined(__GNUC__)");
        this.out.println("    #define JavaCPP_noinline __attribute__((noinline))");
        this.out.println("    #define JavaCPP_hidden   __attribute__((visibility(\"hidden\")))");
        this.out.println("#else");
        this.out.println("    #define JavaCPP_noinline");
        this.out.println("    #define JavaCPP_hidden");
        this.out.println("#endif");
        this.out.println();
        List[] include = new List[]{this.properties.get("platform.include"), this.properties.get("platform.cinclude")};
        int i = 0;
        while (i < include.length) {
            if (include[i] != null && include[i].size() > 0) {
                if (i == 1) {
                    this.out.println("extern \"C\" {");
                    if (this.out2 != null) {
                        this.out2.println("#ifdef __cplusplus");
                        this.out2.println("extern \"C\" {");
                        this.out2.println("#endif");
                    }
                }
                for (String s22 : include[i]) {
                    String line = "#include ";
                    if (!(s22.startsWith("<") || s22.startsWith("\""))) {
                        line = line + '\"';
                    }
                    line = line + s22;
                    if (!(s22.endsWith(">") || s22.endsWith("\""))) {
                        line = line + '\"';
                    }
                    this.out.println(line);
                    if (this.out2 != null) {
                        this.out2.println(line);
                    }
                }
                if (i == 1) {
                    this.out.println("}");
                    if (this.out2 != null) {
                        this.out2.println("#ifdef __cplusplus");
                        this.out2.println("}");
                        this.out2.println("#endif");
                    }
                }
                this.out.println();
            }
            i++;
        }
        this.out.println("static JavaVM* JavaCPP_vm = NULL;");
        this.out.println("static bool JavaCPP_haveAllocObject = false;");
        this.out.println("static bool JavaCPP_haveNonvirtual = false;");
        this.out.println("static const char* JavaCPP_classNames[" + this.jclasses.size() + "] = {");
        Iterator<Class> classIterator = this.jclasses.iterator();
        int maxMemberSize = 0;
        while (classIterator.hasNext()) {
            Class c = (Class) classIterator.next();
            this.out.print("        \"" + c.getName().replace('.', '/') + "\"");
            if (classIterator.hasNext()) {
                this.out.println(",");
            }
            Set<String> m = (Set) this.members.get(c);
            if (m != null && m.size() > maxMemberSize) {
                maxMemberSize = m.size();
            }
        }
        this.out.println(" };");
        this.out.println("static jclass JavaCPP_classes[" + this.jclasses.size() + "] = { NULL };");
        this.out.println("static jfieldID JavaCPP_addressFID = NULL;");
        this.out.println("static jfieldID JavaCPP_positionFID = NULL;");
        this.out.println("static jfieldID JavaCPP_limitFID = NULL;");
        this.out.println("static jfieldID JavaCPP_capacityFID = NULL;");
        this.out.println("static jfieldID JavaCPP_deallocatorFID = NULL;");
        this.out.println("static jfieldID JavaCPP_ownerAddressFID = NULL;");
        this.out.println("static jmethodID JavaCPP_initMID = NULL;");
        this.out.println("static jmethodID JavaCPP_arrayMID = NULL;");
        this.out.println("static jmethodID JavaCPP_stringMID = NULL;");
        this.out.println("static jmethodID JavaCPP_getBytesMID = NULL;");
        this.out.println("static jmethodID JavaCPP_toStringMID = NULL;");
        this.out.println();
        this.out.println("static inline void JavaCPP_log(const char* fmt, ...) {");
        this.out.println("    va_list ap;");
        this.out.println("    va_start(ap, fmt);");
        this.out.println("#ifdef __ANDROID__");
        this.out.println("    __android_log_vprint(ANDROID_LOG_ERROR, \"javacpp\", fmt, ap);");
        this.out.println("#elif defined(__APPLE__) && defined(__OBJC__)");
        this.out.println("    NSLogv([NSString stringWithUTF8String:fmt], ap);");
        this.out.println("#else");
        this.out.println("    vfprintf(stderr, fmt, ap);");
        this.out.println("    fprintf(stderr, \"\\n\");");
        this.out.println("#endif");
        this.out.println("    va_end(ap);");
        this.out.println("}");
        this.out.println();
        this.out.println("static inline jlong JavaCPP_physicalBytes() {");
        this.out.println("    jlong size = 0;");
        this.out.println("#ifdef __linux__");
        this.out.println("    static int fd = open(\"/proc/self/statm\", O_RDONLY, 0);");
        this.out.println("    if (fd >= 0) {");
        this.out.println("        char line[256];");
        this.out.println("        char* space;");
        this.out.println("        lseek(fd, 0, SEEK_SET);");
        this.out.println("        if (read(fd, line, sizeof(line)) > 0 && (space = strchr(line, ' ')) != NULL) {");
        this.out.println("            size = (jlong)(atoll(space + 1) * getpagesize());");
        this.out.println("        }");
        this.out.println("        // no close(fd);");
        this.out.println("    }");
        this.out.println("#elif defined(__APPLE__)");
        this.out.println("    task_basic_info info = {};");
        this.out.println("    mach_msg_type_number_t count = TASK_BASIC_INFO_COUNT;");
        this.out.println("    task_info(current_task(), TASK_BASIC_INFO, (task_info_t)&info, &count);");
        this.out.println("    size = (jlong)info.resident_size;");
        this.out.println("#elif defined(_WIN32)");
        this.out.println("    PROCESS_MEMORY_COUNTERS counters;");
        this.out.println("    if (GetProcessMemoryInfo(GetCurrentProcess(), &counters, sizeof(counters))) {");
        this.out.println("        size = (jlong)counters.WorkingSetSize;");
        this.out.println("    }");
        this.out.println("#endif");
        this.out.println("    return size;");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jclass JavaCPP_getClass(JNIEnv* env, int i) {");
        this.out.println("    if (JavaCPP_classes[i] == NULL && env->PushLocalFrame(1) == 0) {");
        this.out.println("        jclass cls = env->FindClass(JavaCPP_classNames[i]);");
        this.out.println("        if (cls == NULL || env->ExceptionCheck()) {");
        this.out.println("            JavaCPP_log(\"Error loading class %s.\", JavaCPP_classNames[i]);");
        this.out.println("            return NULL;");
        this.out.println("        }");
        this.out.println("        JavaCPP_classes[i] = (jclass)env->NewWeakGlobalRef(cls);");
        this.out.println("        if (JavaCPP_classes[i] == NULL || env->ExceptionCheck()) {");
        this.out.println("            JavaCPP_log(\"Error creating global reference of class %s.\", JavaCPP_classNames[i]);");
        this.out.println("            return NULL;");
        this.out.println("        }");
        this.out.println("        env->PopLocalFrame(NULL);");
        this.out.println("    }");
        this.out.println("    return JavaCPP_classes[i];");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jfieldID JavaCPP_getFieldID(JNIEnv* env, int i, const char* name, const char* sig) {");
        this.out.println("    jclass cls = JavaCPP_getClass(env, i);");
        this.out.println("    if (cls == NULL) {");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    jfieldID fid = env->GetFieldID(cls, name, sig);");
        this.out.println("    if (fid == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting field ID of %s/%s\", JavaCPP_classNames[i], name);");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    return fid;");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jmethodID JavaCPP_getMethodID(JNIEnv* env, int i, const char* name, const char* sig) {");
        this.out.println("    jclass cls = JavaCPP_getClass(env, i);");
        this.out.println("    if (cls == NULL) {");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    jmethodID mid = env->GetMethodID(cls, name, sig);");
        this.out.println("    if (mid == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting method ID of %s/%s\", JavaCPP_classNames[i], name);");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    return mid;");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jmethodID JavaCPP_getStaticMethodID(JNIEnv* env, int i, const char* name, const char* sig) {");
        this.out.println("    jclass cls = JavaCPP_getClass(env, i);");
        this.out.println("    if (cls == NULL) {");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    jmethodID mid = env->GetStaticMethodID(cls, name, sig);");
        this.out.println("    if (mid == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting static method ID of %s/%s\", JavaCPP_classNames[i], name);");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    return mid;");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jobject JavaCPP_createPointer(JNIEnv* env, int i, jclass cls = NULL) {");
        this.out.println("    if (cls == NULL && (cls = JavaCPP_getClass(env, i)) == NULL) {");
        this.out.println("        return NULL;");
        this.out.println("    }");
        this.out.println("    if (JavaCPP_haveAllocObject) {");
        this.out.println("        return env->AllocObject(cls);");
        this.out.println("    } else {");
        this.out.println("        jmethodID mid = env->GetMethodID(cls, \"<init>\", \"(Lorg/bytedeco/javacpp/Pointer;)V\");");
        this.out.println("        if (mid == NULL || env->ExceptionCheck()) {");
        this.out.println("            JavaCPP_log(\"Error getting Pointer constructor of %s, while VM does not support AllocObject()\", JavaCPP_classNames[i]);");
        this.out.println("            return NULL;");
        this.out.println("        }");
        this.out.println("        return env->NewObject(cls, mid, NULL);");
        this.out.println("    }");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline void JavaCPP_initPointer(JNIEnv* env, jobject obj, const void* ptr, jlong size, void* owner, void (*deallocator)(void*)) {");
        this.out.println("    if (deallocator != NULL) {");
        this.out.println("        jvalue args[4];");
        this.out.println("        args[0].j = ptr_to_jlong(ptr);");
        this.out.println("        args[1].j = size;");
        this.out.println("        args[2].j = ptr_to_jlong(owner);");
        this.out.println("        args[3].j = ptr_to_jlong(deallocator);");
        this.out.println("        if (JavaCPP_haveNonvirtual) {");
        this.out.println("            env->CallNonvirtualVoidMethodA(obj, JavaCPP_getClass(env, " + this.jclasses.index(Pointer.class) + "), JavaCPP_initMID, args);");
        this.out.println("        } else {");
        this.out.println("            env->CallVoidMethodA(obj, JavaCPP_initMID, args);");
        this.out.println("        }");
        this.out.println("    } else {");
        this.out.println("        env->SetLongField(obj, JavaCPP_addressFID, ptr_to_jlong(ptr));");
        this.out.println("        env->SetLongField(obj, JavaCPP_limitFID, (jlong)size);");
        this.out.println("        env->SetLongField(obj, JavaCPP_capacityFID, (jlong)size);");
        this.out.println("    }");
        this.out.println("}");
        this.out.println();
        if (handleExceptions || convertStrings) {
            this.out.println("static JavaCPP_noinline jstring JavaCPP_createString(JNIEnv* env, const char* ptr) {");
            this.out.println("    if (ptr == NULL) {");
            this.out.println("        return NULL;");
            this.out.println("    }");
            this.out.println("#ifdef MODIFIED_UTF8_STRING");
            this.out.println("    return env->NewStringUTF(ptr);");
            this.out.println("#else");
            this.out.println("    size_t length = strlen(ptr);");
            this.out.println("    jbyteArray bytes = env->NewByteArray(length < INT_MAX ? length : INT_MAX);");
            this.out.println("    env->SetByteArrayRegion(bytes, 0, length < INT_MAX ? length : INT_MAX, (signed char*)ptr);");
            this.out.println("    return (jstring)env->NewObject(JavaCPP_getClass(env, " + this.jclasses.index(String.class) + "), JavaCPP_stringMID, bytes);");
            this.out.println("#endif");
            this.out.println("}");
            this.out.println();
        }
        if (convertStrings) {
            this.out.println("static JavaCPP_noinline const char* JavaCPP_getStringBytes(JNIEnv* env, jstring str) {");
            this.out.println("    if (str == NULL) {");
            this.out.println("        return NULL;");
            this.out.println("    }");
            this.out.println("#ifdef MODIFIED_UTF8_STRING");
            this.out.println("    return env->GetStringUTFChars(str, NULL);");
            this.out.println("#else");
            this.out.println("    jbyteArray bytes = (jbyteArray)env->CallObjectMethod(str, JavaCPP_getBytesMID);");
            this.out.println("    if (bytes == NULL || env->ExceptionCheck()) {");
            this.out.println("        JavaCPP_log(\"Error getting bytes from string.\");");
            this.out.println("        return NULL;");
            this.out.println("    }");
            this.out.println("    jsize length = env->GetArrayLength(bytes);");
            this.out.println("    signed char* ptr = new (std::nothrow) signed char[length + 1];");
            this.out.println("    if (ptr != NULL) {");
            this.out.println("        env->GetByteArrayRegion(bytes, 0, length, ptr);");
            this.out.println("        ptr[length] = 0;");
            this.out.println("    }");
            this.out.println("    return (const char*)ptr;");
            this.out.println("#endif");
            this.out.println("}");
            this.out.println();
            this.out.println("static JavaCPP_noinline void JavaCPP_releaseStringBytes(JNIEnv* env, jstring str, const char* ptr) {");
            this.out.println("#ifdef MODIFIED_UTF8_STRING");
            this.out.println("    if (str != NULL) {");
            this.out.println("        env->ReleaseStringUTFChars(str, ptr);");
            this.out.println("    }");
            this.out.println("#else");
            this.out.println("    delete[] ptr;");
            this.out.println("#endif");
            this.out.println("}");
            this.out.println();
        }
        this.out.println("class JavaCPP_hidden JavaCPP_exception : public std::exception {");
        this.out.println("public:");
        this.out.println("    JavaCPP_exception(const char* str) throw() {");
        this.out.println("        if (str == NULL) {");
        this.out.println("            strcpy(msg, \"Unknown exception.\");");
        this.out.println("        } else {");
        this.out.println("            strncpy(msg, str, sizeof(msg));");
        this.out.println("            msg[sizeof(msg) - 1] = 0;");
        this.out.println("        }");
        this.out.println("    }");
        this.out.println("    virtual const char* what() const throw() { return msg; }");
        this.out.println("    char msg[1024];");
        this.out.println("};");
        this.out.println();
        if (handleExceptions) {
            this.out.println("#ifndef GENERIC_EXCEPTION_CLASS");
            this.out.println("#define GENERIC_EXCEPTION_CLASS std::exception");
            this.out.println("#endif");
            this.out.println("static JavaCPP_noinline jthrowable JavaCPP_handleException(JNIEnv* env, int i) {");
            this.out.println("    jstring str = NULL;");
            this.out.println("    try {");
            this.out.println("        throw;");
            this.out.println("    } catch (GENERIC_EXCEPTION_CLASS& e) {");
            this.out.println("        str = JavaCPP_createString(env, e.what());");
            this.out.println("    } catch (...) {");
            this.out.println("        str = JavaCPP_createString(env, \"Unknown exception.\");");
            this.out.println("    }");
            this.out.println("    jmethodID mid = JavaCPP_getMethodID(env, i, \"<init>\", \"(Ljava/lang/String;)V\");");
            this.out.println("    if (mid == NULL) {");
            this.out.println("        return NULL;");
            this.out.println("    }");
            this.out.println("    return (jthrowable)env->NewObject(JavaCPP_getClass(env, i), mid, str);");
            this.out.println("}");
            this.out.println();
        }
        try {
            String[] typeName;
            String valueTypeName;
            String subType;
            Iterator<String> memberIterator;
            Class<?> cls;
            Class deallocator = Class.forName(Pointer.class.getName() + "$Deallocator", false, Pointer.class.getClassLoader());
            Class nativeDeallocator = Class.forName(Pointer.class.getName() + "$NativeDeallocator", false, Pointer.class.getClassLoader());
            if (defineAdapters) {
                this.out.println("static JavaCPP_noinline void* JavaCPP_getPointerOwner(JNIEnv* env, jobject obj) {");
                this.out.println("    if (obj != NULL) {");
                this.out.println("        jobject deallocator = env->GetObjectField(obj, JavaCPP_deallocatorFID);");
                this.out.println("        if (deallocator != NULL && env->IsInstanceOf(deallocator, JavaCPP_getClass(env, " + this.jclasses.index(nativeDeallocator) + "))) {");
                this.out.println("            return jlong_to_ptr(env->GetLongField(deallocator, JavaCPP_ownerAddressFID));");
                this.out.println("        }");
                this.out.println("    }");
                this.out.println("    return NULL;");
                this.out.println("}");
                this.out.println();
                this.out.println("#include <vector>");
                this.out.println("template<typename P, typename T = P> class JavaCPP_hidden VectorAdapter {");
                this.out.println("public:");
                this.out.println("    VectorAdapter(const P* ptr, typename std::vector<T>::size_type size, void* owner) : ptr((P*)ptr), size(size), owner(owner),");
                this.out.println("        vec2(ptr ? std::vector<T>((P*)ptr, (P*)ptr + size) : std::vector<T>()), vec(vec2) { }");
                this.out.println("    VectorAdapter(const std::vector<T>& vec) : ptr(0), size(0), owner(0), vec2(vec), vec(vec2) { }");
                this.out.println("    VectorAdapter(      std::vector<T>& vec) : ptr(0), size(0), owner(0), vec(vec) { }");
                this.out.println("    VectorAdapter(const std::vector<T>* vec) : ptr(0), size(0), owner(0), vec(*(std::vector<T>*)vec) { }");
                this.out.println("    void assign(P* ptr, typename std::vector<T>::size_type size, void* owner) {");
                this.out.println("        this->ptr = ptr;");
                this.out.println("        this->size = size;");
                this.out.println("        this->owner = owner;");
                this.out.println("        vec.assign(ptr, ptr + size);");
                this.out.println("    }");
                this.out.println("    static void deallocate(void* owner) { operator delete(owner); }");
                this.out.println("    operator P*() {");
                this.out.println("        if (vec.size() > size) {");
                this.out.println("            ptr = (P*)(operator new(sizeof(P) * vec.size(), std::nothrow_t()));");
                this.out.println("        }");
                this.out.println("        if (ptr) {");
                this.out.println("            std::copy(vec.begin(), vec.end(), ptr);");
                this.out.println("        }");
                this.out.println("        size = vec.size();");
                this.out.println("        owner = ptr;");
                this.out.println("        return ptr;");
                this.out.println("    }");
                this.out.println("    operator const P*()        { return &vec[0]; }");
                this.out.println("    operator std::vector<T>&() { return vec; }");
                this.out.println("    operator std::vector<T>*() { return ptr ? &vec : 0; }");
                this.out.println("    P* ptr;");
                this.out.println("    typename std::vector<T>::size_type size;");
                this.out.println("    void* owner;");
                this.out.println("    std::vector<T> vec2;");
                this.out.println("    std::vector<T>& vec;");
                this.out.println("};");
                this.out.println();
                this.out.println("#include <string>");
                this.out.println("class JavaCPP_hidden StringAdapter {");
                this.out.println("public:");
                this.out.println("    StringAdapter(const          char* ptr, size_t size, void* owner) : ptr((char*)ptr), size(size), owner(owner),");
                this.out.println("        str2(ptr ? (char*)ptr : \"\", ptr ? (size > 0 ? size : strlen((char*)ptr)) : 0), str(str2) { }");
                this.out.println("    StringAdapter(const signed   char* ptr, size_t size, void* owner) : ptr((char*)ptr), size(size), owner(owner),");
                this.out.println("        str2(ptr ? (char*)ptr : \"\", ptr ? (size > 0 ? size : strlen((char*)ptr)) : 0), str(str2) { }");
                this.out.println("    StringAdapter(const unsigned char* ptr, size_t size, void* owner) : ptr((char*)ptr), size(size), owner(owner),");
                this.out.println("        str2(ptr ? (char*)ptr : \"\", ptr ? (size > 0 ? size : strlen((char*)ptr)) : 0), str(str2) { }");
                this.out.println("    StringAdapter(const std::string& str) : ptr(0), size(0), owner(0), str2(str), str(str2) { }");
                this.out.println("    StringAdapter(      std::string& str) : ptr(0), size(0), owner(0), str(str) { }");
                this.out.println("    StringAdapter(const std::string* str) : ptr(0), size(0), owner(0), str(*(std::string*)str) { }");
                this.out.println("    void assign(char* ptr, size_t size, void* owner) {");
                this.out.println("        this->ptr = ptr;");
                this.out.println("        this->size = size;");
                this.out.println("        this->owner = owner;");
                this.out.println("        str.assign(ptr ? ptr : \"\", ptr ? (size > 0 ? size : strlen((char*)ptr)) : 0);");
                this.out.println("    }");
                this.out.println("    void assign(const          char* ptr, size_t size, void* owner) { assign((char*)ptr, size, owner); }");
                this.out.println("    void assign(const signed   char* ptr, size_t size, void* owner) { assign((char*)ptr, size, owner); }");
                this.out.println("    void assign(const unsigned char* ptr, size_t size, void* owner) { assign((char*)ptr, size, owner); }");
                this.out.println("    static void deallocate(void* owner) { delete[] (char*)owner; }");
                this.out.println("    operator char*() {");
                this.out.println("        const char* data = str.data();");
                this.out.println("        if (str.size() > size) {");
                this.out.println("            ptr = new (std::nothrow) char[str.size()+1];");
                this.out.println("            if (ptr) memset(ptr, 0, str.size()+1);");
                this.out.println("        }");
                this.out.println("        if (ptr && memcmp(ptr, data, str.size()) != 0) {");
                this.out.println("            memcpy(ptr, data, str.size());");
                this.out.println("            if (size > str.size()) ptr[str.size()] = 0;");
                this.out.println("        }");
                this.out.println("        size = str.size();");
                this.out.println("        owner = ptr;");
                this.out.println("        return ptr;");
                this.out.println("    }");
                this.out.println("    operator       signed   char*() { return (signed   char*)(operator char*)(); }");
                this.out.println("    operator       unsigned char*() { return (unsigned char*)(operator char*)(); }");
                this.out.println("    operator const          char*() { return                 str.c_str(); }");
                this.out.println("    operator const signed   char*() { return (signed   char*)str.c_str(); }");
                this.out.println("    operator const unsigned char*() { return (unsigned char*)str.c_str(); }");
                this.out.println("    operator         std::string&() { return str; }");
                this.out.println("    operator         std::string*() { return ptr ? &str : 0; }");
                this.out.println("    char* ptr;");
                this.out.println("    size_t size;");
                this.out.println("    void* owner;");
                this.out.println("    std::string str2;");
                this.out.println("    std::string& str;");
                this.out.println("};");
                this.out.println();
                this.out.println("#ifdef SHARED_PTR_NAMESPACE");
                this.out.println("template<class T> class SharedPtrAdapter {");
                this.out.println("public:");
                this.out.println("    typedef SHARED_PTR_NAMESPACE::shared_ptr<T> S;");
                this.out.println("    SharedPtrAdapter(const T* ptr, size_t size, void* owner) : ptr((T*)ptr), size(size), owner(owner),");
                this.out.println("            sharedPtr2(owner != NULL && owner != ptr ? *(S*)owner : S((T*)ptr)), sharedPtr(sharedPtr2) { }");
                this.out.println("    SharedPtrAdapter(const S& sharedPtr) : ptr(0), size(0), owner(0), sharedPtr2(sharedPtr), sharedPtr(sharedPtr2) { }");
                this.out.println("    SharedPtrAdapter(      S& sharedPtr) : ptr(0), size(0), owner(0), sharedPtr(sharedPtr) { }");
                this.out.println("    SharedPtrAdapter(const S* sharedPtr) : ptr(0), size(0), owner(0), sharedPtr(*(S*)sharedPtr) { }");
                this.out.println("    void assign(T* ptr, size_t size, S* owner) {");
                this.out.println("        this->ptr = ptr;");
                this.out.println("        this->size = size;");
                this.out.println("        this->owner = owner;");
                this.out.println("        this->sharedPtr = owner != NULL && owner != ptr ? *(S*)owner : S((T*)ptr);");
                this.out.println("    }");
                this.out.println("    static void deallocate(void* owner) { delete (S*)owner; }");
                this.out.println("    operator T*() {");
                this.out.println("        ptr = sharedPtr.get();");
                this.out.println("        if (owner == NULL || owner == ptr) {");
                this.out.println("            owner = new S(sharedPtr);");
                this.out.println("        }");
                this.out.println("        return ptr;");
                this.out.println("    }");
                this.out.println("    operator const T*() { return sharedPtr.get(); }");
                this.out.println("    operator       S&() { return sharedPtr; }");
                this.out.println("    operator       S*() { return &sharedPtr; }");
                this.out.println("    T* ptr;");
                this.out.println("    size_t size;");
                this.out.println("    void* owner;");
                this.out.println("    S sharedPtr2;");
                this.out.println("    S& sharedPtr;");
                this.out.println("};");
                this.out.println("#endif");
                this.out.println();
                this.out.println("#ifdef UNIQUE_PTR_NAMESPACE");
                this.out.println("template<class T> class UniquePtrAdapter {");
                this.out.println("public:");
                this.out.println("    typedef UNIQUE_PTR_NAMESPACE::unique_ptr<T> U;");
                this.out.println("    UniquePtrAdapter(const T* ptr, size_t size, void* owner) : ptr((T*)ptr), size(size), owner(owner),");
                this.out.println("            uniquePtr2(owner != NULL && owner != ptr ? U() : U((T*)ptr)),");
                this.out.println("            uniquePtr(owner != NULL && owner != ptr ? *(U*)owner : uniquePtr2) { }");
                this.out.println("    UniquePtrAdapter(const U& uniquePtr) : ptr(0), size(0), owner(0), uniquePtr((U&)uniquePtr) { }");
                this.out.println("    UniquePtrAdapter(      U& uniquePtr) : ptr(0), size(0), owner(0), uniquePtr(uniquePtr) { }");
                this.out.println("    UniquePtrAdapter(const U* uniquePtr) : ptr(0), size(0), owner(0), uniquePtr(*(U*)uniquePtr) { }");
                this.out.println("    void assign(T* ptr, size_t size, U* owner) {");
                this.out.println("        this->ptr = ptr;");
                this.out.println("        this->size = size;");
                this.out.println("        this->owner = owner;");
                this.out.println("        this->uniquePtr = owner != NULL && owner != ptr ? *(U*)owner : U((T*)ptr);");
                this.out.println("    }");
                this.out.println("    static void deallocate(void* owner) { delete (U*)owner; }");
                this.out.println("    operator T*() {");
                this.out.println("        ptr = uniquePtr.get();");
                this.out.println("        if (owner == NULL || owner == ptr) {");
                this.out.println("            owner = new U(UNIQUE_PTR_NAMESPACE::move(uniquePtr));");
                this.out.println("        }");
                this.out.println("        return ptr;");
                this.out.println("    }");
                this.out.println("    operator const T*() { return uniquePtr.get(); }");
                this.out.println("    operator       U&() { return uniquePtr; }");
                this.out.println("    operator       U*() { return &uniquePtr; }");
                this.out.println("    T* ptr;");
                this.out.println("    size_t size;");
                this.out.println("    void* owner;");
                this.out.println("    U uniquePtr2;");
                this.out.println("    U& uniquePtr;");
                this.out.println("};");
                this.out.println("#endif");
                this.out.println();
            }
            if (!(this.functions.isEmpty() && this.virtualFunctions.isEmpty())) {
                this.out.println("static JavaCPP_noinline void JavaCPP_detach(bool detach) {");
                this.out.println("#ifndef NO_JNI_DETACH_THREAD");
                this.out.println("    if (detach && JavaCPP_vm->DetachCurrentThread() != JNI_OK) {");
                this.out.println("        JavaCPP_log(\"Could not detach the JavaVM from the current thread.\");");
                this.out.println("    }");
                this.out.println("#endif");
                this.out.println("}");
                this.out.println();
                this.out.println("static JavaCPP_noinline bool JavaCPP_getEnv(JNIEnv** env) {");
                this.out.println("    bool attached = false;");
                this.out.println("    JavaVM *vm = JavaCPP_vm;");
                this.out.println("    if (vm == NULL) {");
                if (this.out2 != null) {
                    this.out.println("#if !defined(__ANDROID__) && !TARGET_OS_IPHONE");
                    this.out.println("        int size = 1;");
                    this.out.println("        if (JNI_GetCreatedJavaVMs(&vm, 1, &size) != JNI_OK || size == 0) {");
                    this.out.println("#endif");
                }
                this.out.println("            JavaCPP_log(\"Could not get any created JavaVM.\");");
                this.out.println("            *env = NULL;");
                this.out.println("            return false;");
                if (this.out2 != null) {
                    this.out.println("#if !defined(__ANDROID__) && !TARGET_OS_IPHONE");
                    this.out.println("        }");
                    this.out.println("#endif");
                }
                this.out.println("    }");
                this.out.println("    if (vm->GetEnv((void**)env, JNI_VERSION_1_4) != JNI_OK) {");
                this.out.println("        struct {");
                this.out.println("            JNIEnv **env;");
                this.out.println("            operator JNIEnv**() { return env; } // Android JNI");
                this.out.println("            operator void**() { return (void**)env; } // standard JNI");
                this.out.println("        } env2 = { env };");
                this.out.println("        if (vm->AttachCurrentThread(env2, NULL) != JNI_OK) {");
                this.out.println("            JavaCPP_log(\"Could not attach the JavaVM to the current thread.\");");
                this.out.println("            *env = NULL;");
                this.out.println("            return false;");
                this.out.println("        }");
                this.out.println("        attached = true;");
                this.out.println("    }");
                this.out.println("    if (JavaCPP_vm == NULL) {");
                this.out.println("        if (JNI_OnLoad(vm, NULL) < 0) {");
                this.out.println("            JavaCPP_detach(attached);");
                this.out.println("            *env = NULL;");
                this.out.println("            return false;");
                this.out.println("        }");
                this.out.println("    }");
                this.out.println("    return attached;");
                this.out.println("}");
                this.out.println();
            }
            it = this.functions.iterator();
            while (it.hasNext()) {
                c = (Class) it.next();
                typeName = cppTypeName(c);
                String[] returnConvention = typeName[0].split("\\(");
                returnConvention[1] = constValueTypeName(returnConvention[1]);
                String parameterDeclaration = typeName[1].substring(1);
                String instanceTypeName = functionClassName(c);
                this.out.println("struct JavaCPP_hidden " + instanceTypeName + " {");
                this.out.println("    " + instanceTypeName + "() : ptr(NULL), obj(NULL) { }");
                if (parameterDeclaration != null && parameterDeclaration.length() > 0) {
                    this.out.println("    " + returnConvention[0] + "operator()" + parameterDeclaration + ";");
                }
                this.out.println("    " + typeName[0] + "ptr" + typeName[1] + ";");
                this.out.println("    jobject obj; static jmethodID mid;");
                this.out.println("};");
                this.out.println("jmethodID " + instanceTypeName + "::mid = NULL;");
            }
            this.out.println();
            it = this.jclasses.iterator();
            while (it.hasNext()) {
                c = (Class) it.next();
                Set<String> functionList = (Set) this.virtualFunctions.get(c);
                if (functionList != null) {
                    Set<String> memberList = (Set) this.virtualMembers.get(c);
                    valueTypeName = valueTypeName(cppTypeName(c));
                    subType = "JavaCPP_" + mangle(valueTypeName);
                    this.out.println("class JavaCPP_hidden " + subType + " : public " + valueTypeName + " {");
                    this.out.println("public:");
                    this.out.println("    jobject obj;");
                    for (String s222 : functionList) {
                        this.out.println("    static jmethodID " + s222 + ";");
                    }
                    this.out.println();
                    for (String s2222 : memberList) {
                        this.out.println(s2222);
                    }
                    this.out.println("};");
                    for (String s22222 : functionList) {
                        this.out.println("jmethodID " + subType + "::" + s22222 + " = NULL;");
                    }
                }
            }
            this.out.println();
            it = this.callbacks.iterator();
            while (it.hasNext()) {
                this.out.println((String) it.next());
            }
            this.out.println();
            it = this.deallocators.iterator();
            while (it.hasNext()) {
                c = (Class) it.next();
                this.out.print("static void " + ("JavaCPP_" + mangle(c.getName())) + "_deallocate(void *p) { ");
                if (FunctionPointer.class.isAssignableFrom(c)) {
                    String typeName2 = functionClassName(c) + "*";
                    this.out.println("JNIEnv *e; bool a = JavaCPP_getEnv(&e); if (e != NULL) e->DeleteWeakGlobalRef((jweak)((" + typeName2 + ")p)->obj); delete (" + typeName2 + ")p; JavaCPP_detach(a); }");
                } else if (this.virtualFunctions.containsKey(c)) {
                    subType = "JavaCPP_" + mangle(valueTypeName(cppTypeName(c)));
                    this.out.println("JNIEnv *e; bool a = JavaCPP_getEnv(&e); if (e != NULL) e->DeleteWeakGlobalRef((jweak)((" + subType + "*)p)->obj); delete (" + subType + "*)p; JavaCPP_detach(a); }");
                } else {
                    typeName = cppTypeName(c);
                    this.out.println("delete (" + typeName[0] + typeName[1] + ")p; }");
                }
            }
            it = this.arrayDeallocators.iterator();
            while (it.hasNext()) {
                c = (Class) it.next();
                String name = "JavaCPP_" + mangle(c.getName());
                typeName = cppTypeName(c);
                this.out.println("static void " + name + "_deallocateArray(void* p) { delete[] (" + typeName[0] + typeName[1] + ")p; }");
            }
            this.out.println();
            this.out.println("extern \"C\" {");
            if (this.out2 != null) {
                this.out2.println();
                this.out2.println("#ifdef __cplusplus");
                this.out2.println("extern \"C\" {");
                this.out2.println("#endif");
                this.out2.println("JNIIMPORT int JavaCPP_init(int argc, const char *argv[]);");
                this.out.println();
                this.out.println("JNIEXPORT int JavaCPP_init(int argc, const char *argv[]) {");
                this.out.println("#if defined(__ANDROID__) || TARGET_OS_IPHONE");
                this.out.println("    return JNI_OK;");
                this.out.println("#else");
                this.out.println("    if (JavaCPP_vm != NULL) {");
                this.out.println("        return JNI_OK;");
                this.out.println("    }");
                this.out.println("    int err;");
                this.out.println("    JavaVM *vm;");
                this.out.println("    JNIEnv *env;");
                this.out.println("    int nOptions = 1 + (argc > 255 ? 255 : argc);");
                this.out.println("    JavaVMOption options[256] = { { NULL } };");
                this.out.println("    options[0].optionString = (char*)\"-Djava.class.path=" + classPath.replace('\\', '/') + "\";");
                this.out.println("    for (int i = 1; i < nOptions && argv != NULL; i++) {");
                this.out.println("        options[i].optionString = (char*)argv[i - 1];");
                this.out.println("    }");
                this.out.println("    JavaVMInitArgs vm_args = { JNI_VERSION_1_4, nOptions, options };");
                this.out.println("    return (err = JNI_CreateJavaVM(&vm, (void**)&env, &vm_args)) == JNI_OK && vm != NULL && (err = JNI_OnLoad(vm, NULL)) >= 0 ? JNI_OK : err;");
                this.out.println("#endif");
                this.out.println("}");
            }
            this.out.println();
            this.out.println("JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {");
            this.out.println("    JNIEnv* env;");
            this.out.println("    if (vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK) {");
            this.out.println("        JavaCPP_log(\"Could not get JNIEnv for JNI_VERSION_1_4 inside JNI_OnLoad().\");");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    if (JavaCPP_vm == vm) {");
            this.out.println("        return env->GetVersion();");
            this.out.println("    }");
            this.out.println("    JavaCPP_vm = vm;");
            this.out.println("    JavaCPP_haveAllocObject = env->functions->AllocObject != NULL;");
            this.out.println("    JavaCPP_haveNonvirtual = env->functions->CallNonvirtualVoidMethodA != NULL;");
            this.out.println("    const char* members[" + this.jclasses.size() + "][" + maxMemberSize + "] = {");
            classIterator = this.jclasses.iterator();
            while (classIterator.hasNext()) {
                this.out.print("            { ");
                m = (Set) this.members.get(classIterator.next());
                memberIterator = m == null ? null : m.iterator();
                if (memberIterator == null) {
                    this.out.print("NULL");
                } else {
                    while (memberIterator.hasNext()) {
                        this.out.print("\"" + ((String) memberIterator.next()) + "\"");
                        if (memberIterator.hasNext()) {
                            this.out.print(", ");
                        }
                    }
                }
                this.out.print(" }");
                if (classIterator.hasNext()) {
                    this.out.println(",");
                }
            }
            this.out.println(" };");
            this.out.println("    int offsets[" + this.jclasses.size() + "][" + maxMemberSize + "] = {");
            classIterator = this.jclasses.iterator();
            while (classIterator.hasNext()) {
                this.out.print("            { ");
                c = (Class) classIterator.next();
                m = (Set) this.members.get(c);
                memberIterator = m == null ? null : m.iterator();
                if (memberIterator == null) {
                    this.out.print("-1");
                } else {
                    while (memberIterator.hasNext()) {
                        valueTypeName = valueTypeName(cppTypeName(c));
                        String memberName = (String) memberIterator.next();
                        if ("sizeof".equals(memberName)) {
                            if ("void".equals(valueTypeName)) {
                                valueTypeName = "void*";
                            }
                            this.out.print("sizeof(" + valueTypeName + ")");
                        } else {
                            this.out.print("offsetof(" + valueTypeName + ", " + memberName + ")");
                        }
                        if (memberIterator.hasNext()) {
                            this.out.print(", ");
                        }
                    }
                }
                this.out.print(" }");
                if (classIterator.hasNext()) {
                    this.out.println(",");
                }
            }
            this.out.println(" };");
            this.out.print("    int memberOffsetSizes[" + this.jclasses.size() + "] = { ");
            classIterator = this.jclasses.iterator();
            while (classIterator.hasNext()) {
                m = (Set) this.members.get(classIterator.next());
                this.out.print(m == null ? 1 : m.size());
                if (classIterator.hasNext()) {
                    this.out.print(", ");
                }
            }
            this.out.println(" };");
            this.out.println("    jmethodID putMemberOffsetMID = JavaCPP_getStaticMethodID(env, " + this.jclasses.index(Loader.class) + ", \"putMemberOffset\", \"(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/Class;\");");
            this.out.println("    if (putMemberOffsetMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    for (int i = 0; i < " + this.jclasses.size() + " && !env->ExceptionCheck(); i++) {");
            this.out.println("        for (int j = 0; j < memberOffsetSizes[i] && !env->ExceptionCheck(); j++) {");
            this.out.println("            if (env->PushLocalFrame(3) == 0) {");
            this.out.println("                jvalue args[3];");
            this.out.println("                args[0].l = env->NewStringUTF(JavaCPP_classNames[i]);");
            this.out.println("                args[1].l = members[i][j] == NULL ? NULL : env->NewStringUTF(members[i][j]);");
            this.out.println("                args[2].i = offsets[i][j];");
            this.out.println("                jclass cls = (jclass)env->CallStaticObjectMethodA(JavaCPP_getClass(env, " + this.jclasses.index(Loader.class) + "), putMemberOffsetMID, args);");
            this.out.println("                if (cls == NULL || env->ExceptionCheck()) {");
            this.out.println("                    JavaCPP_log(\"Error putting member offsets for class %s.\", JavaCPP_classNames[i]);");
            this.out.println("                    return JNI_ERR;");
            this.out.println("                }");
            this.out.println("                JavaCPP_classes[i] = (jclass)env->NewWeakGlobalRef(cls);");
            this.out.println("                if (JavaCPP_classes[i] == NULL || env->ExceptionCheck()) {");
            this.out.println("                    JavaCPP_log(\"Error creating global reference of class %s.\", JavaCPP_classNames[i]);");
            this.out.println("                    return JNI_ERR;");
            this.out.println("                }");
            this.out.println("                env->PopLocalFrame(NULL);");
            this.out.println("            }");
            this.out.println("        }");
            this.out.println("    }");
            this.out.println("    JavaCPP_addressFID = JavaCPP_getFieldID(env, " + this.jclasses.index(Pointer.class) + ", \"address\", \"J\");");
            this.out.println("    if (JavaCPP_addressFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_positionFID = JavaCPP_getFieldID(env, " + this.jclasses.index(Pointer.class) + ", \"position\", \"J\");");
            this.out.println("    if (JavaCPP_positionFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_limitFID = JavaCPP_getFieldID(env, " + this.jclasses.index(Pointer.class) + ", \"limit\", \"J\");");
            this.out.println("    if (JavaCPP_limitFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_capacityFID = JavaCPP_getFieldID(env, " + this.jclasses.index(Pointer.class) + ", \"capacity\", \"J\");");
            this.out.println("    if (JavaCPP_capacityFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_deallocatorFID = JavaCPP_getFieldID(env, " + this.jclasses.index(Pointer.class) + ", \"deallocator\", \"" + signature(deallocator) + "\");");
            this.out.println("    if (JavaCPP_deallocatorFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_ownerAddressFID = JavaCPP_getFieldID(env, " + this.jclasses.index(nativeDeallocator) + ", \"ownerAddress\", \"J\");");
            this.out.println("    if (JavaCPP_ownerAddressFID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_initMID = JavaCPP_getMethodID(env, " + this.jclasses.index(Pointer.class) + ", \"init\", \"(JJJJ)V\");");
            this.out.println("    if (JavaCPP_initMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_arrayMID = JavaCPP_getMethodID(env, " + this.jclasses.index(Buffer.class) + ", \"array\", \"()Ljava/lang/Object;\");");
            this.out.println("    if (JavaCPP_arrayMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_stringMID = JavaCPP_getMethodID(env, " + this.jclasses.index(String.class) + ", \"<init>\", \"([B)V\");");
            this.out.println("    if (JavaCPP_stringMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_getBytesMID = JavaCPP_getMethodID(env, " + this.jclasses.index(String.class) + ", \"getBytes\", \"()[B\");");
            this.out.println("    if (JavaCPP_getBytesMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    JavaCPP_toStringMID = JavaCPP_getMethodID(env, " + this.jclasses.index(Object.class) + ", \"toString\", \"()Ljava/lang/String;\");");
            this.out.println("    if (JavaCPP_toStringMID == NULL) {");
            this.out.println("        return JNI_ERR;");
            this.out.println("    }");
            this.out.println("    return env->GetVersion();");
            this.out.println("}");
            this.out.println();
            if (this.out2 != null) {
                this.out2.println("JNIIMPORT int JavaCPP_uninit();");
                this.out2.println();
                this.out.println("JNIEXPORT int JavaCPP_uninit() {");
                this.out.println("#if defined(__ANDROID__) || TARGET_OS_IPHONE");
                this.out.println("    return JNI_OK;");
                this.out.println("#else");
                this.out.println("    JavaVM *vm = JavaCPP_vm;");
                this.out.println("    JNI_OnUnload(JavaCPP_vm, NULL);");
                this.out.println("    return vm->DestroyJavaVM();");
                this.out.println("#endif");
                this.out.println("}");
            }
            this.out.println();
            this.out.println("JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {");
            this.out.println("    JNIEnv* env;");
            this.out.println("    if (vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK) {");
            this.out.println("        JavaCPP_log(\"Could not get JNIEnv for JNI_VERSION_1_4 inside JNI_OnUnLoad().\");");
            this.out.println("        return;");
            this.out.println("    }");
            this.out.println("    for (int i = 0; i < " + this.jclasses.size() + "; i++) {");
            this.out.println("        env->DeleteWeakGlobalRef((jweak)JavaCPP_classes[i]);");
            this.out.println("        JavaCPP_classes[i] = NULL;");
            this.out.println("    }");
            this.out.println("    JavaCPP_vm = NULL;");
            this.out.println("}");
            this.out.println();
            LinkedHashSet<Class> allClasses = new LinkedHashSet();
            allClasses.addAll(baseClasses);
            allClasses.addAll(Arrays.asList(classes));
            boolean supportedPlatform = false;
            for (Class<?> cls2 : classes) {
                supportedPlatform |= checkPlatform(cls2);
            }
            boolean didSomethingUseful = false;
            it = allClasses.iterator();
            while (it.hasNext()) {
                cls2 = (Class) it.next();
                try {
                    didSomethingUseful |= methods(cls2);
                } catch (NoClassDefFoundError e) {
                    this.logger.warn("Could not generate code for class " + cls2.getCanonicalName() + ": " + e);
                }
            }
            this.out.println("}");
            this.out.println();
            if (this.out2 != null) {
                this.out2.println("#ifdef __cplusplus");
                this.out2.println("}");
                this.out2.println("#endif");
            }
            return supportedPlatform && didSomethingUseful;
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    boolean methods(Class<?> cls) {
        if (!checkPlatform(cls)) {
            return false;
        }
        int i;
        Set<String> memberList = (Set) this.members.get(cls);
        if (!(cls.isAnnotationPresent(Opaque.class) || FunctionPointer.class.isAssignableFrom(cls) || cls.getEnclosingClass() == Pointer.class)) {
            if (memberList == null) {
                Map map = this.members;
                memberList = new LinkedHashSet();
                map.put(cls, memberList);
            }
            if (!memberList.contains("sizeof")) {
                memberList.add("sizeof");
            }
        }
        boolean didSomething = false;
        for (Class<?> c : cls.getDeclaredClasses()) {
            if (Pointer.class.isAssignableFrom(c) || Pointer.class.isAssignableFrom(c.getEnclosingClass())) {
                didSomething |= methods(c);
            }
        }
        Method[] methods = cls.getDeclaredMethods();
        MethodInformation[] methodInfos = new MethodInformation[methods.length];
        for (i = 0; i < methods.length; i++) {
            methodInfos[i] = methodInformation(methods[i]);
        }
        Class<?> c2 = cls.getSuperclass();
        while (c2 != null && c2 != Object.class) {
            for (Method m : c2.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Virtual.class)) {
                    boolean found = false;
                    String name = m.getName();
                    Class<?>[] types = m.getParameterTypes();
                    for (Method m2 : methods) {
                        if (name.equals(m2.getName())) {
                            if (Arrays.equals(types, m2.getParameterTypes())) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        methods = (Method[]) Arrays.copyOf(methods, methods.length + 1);
                        methods[methods.length - 1] = m;
                        methodInfos = (MethodInformation[]) Arrays.copyOf(methodInfos, methodInfos.length + 1);
                        methodInfos[methods.length - 1] = methodInformation(m);
                        methodInfos[methods.length - 1].cls = cls;
                    }
                }
            }
            c2 = c2.getSuperclass();
        }
        boolean[] callbackAllocators = new boolean[methods.length];
        Method[] functionMethods = functionMethods(cls, callbackAllocators);
        boolean firstCallback = true;
        i = 0;
        while (i < methods.length) {
            if (checkPlatform((Platform) methods[i].getAnnotation(Platform.class), null)) {
                MethodInformation methodInfo = methodInfos[i];
                String nativeName = mangle(cls.getName()) + "_" + mangle(methods[i].getName());
                String callbackName = (!callbackAllocators[i] || methodInfo.parameterTypes.length <= 0) ? "JavaCPP_" + nativeName + "_callback" : null;
                if (callbackAllocators[i] && functionMethods == null) {
                    this.logger.warn("No callback method call() or apply() has been not declared in \"" + cls.getCanonicalName() + "\". No code will be generated for callback allocator.");
                } else {
                    if (functionMethods != null) {
                        for (Method functionMethod : functionMethods) {
                            if ((functionMethod != null && callbackAllocators[i]) || (methods[i].equals(functionMethod) && !Modifier.isNative(methods[i].getModifiers()))) {
                                this.functions.index(cls);
                                Name name2 = (Name) methods[i].getAnnotation(Name.class);
                                if (name2 != null && name2.value().length > 0 && name2.value()[0].length() > 0) {
                                    callbackName = name2.value()[0];
                                }
                                callback(cls, functionMethod, callbackName, firstCallback, null);
                                firstCallback = false;
                                didSomething = true;
                            }
                        }
                    }
                    if (!((!Modifier.isNative(methods[i].getModifiers()) && !Modifier.isAbstract(methods[i].getModifiers())) || methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter || cls.isInterface() || (!methods[i].isAnnotationPresent(Virtual.class) && !methodInfo.allocator))) {
                        callback(cls, methods[i], methodInfo.memberName[0], !methodInfo.allocator, methodInfo);
                    }
                    if (Modifier.isNative(methods[i].getModifiers())) {
                        if (!((!methodInfo.memberGetter && !methodInfo.memberSetter) || methodInfo.noOffset || memberList == null || Modifier.isStatic(methodInfo.modifiers))) {
                            if (!memberList.contains(methodInfo.memberName[0])) {
                                memberList.add(methodInfo.memberName[0]);
                            }
                        }
                        didSomething = true;
                        this.out.print("JNIEXPORT " + jniTypeName(methodInfo.returnType) + " JNICALL Java_" + nativeName);
                        if (methodInfo.overloaded) {
                            this.out.print("__" + mangle(signature(methodInfo.parameterTypes)));
                        }
                        if (Modifier.isStatic(methodInfo.modifiers)) {
                            this.out.print("(JNIEnv* env, jclass cls");
                        } else {
                            this.out.print("(JNIEnv* env, jobject obj");
                        }
                        for (int j = 0; j < methodInfo.parameterTypes.length; j++) {
                            this.out.print(", " + jniTypeName(methodInfo.parameterTypes[j]) + " arg" + j);
                        }
                        this.out.println(") {");
                        if (callbackAllocators[i]) {
                            callbackAllocator(cls, callbackName);
                        } else {
                            if (!(Modifier.isStatic(methodInfo.modifiers) || methodInfo.allocator || methodInfo.arrayAllocator || methodInfo.deallocator)) {
                                String[] typeName = cppTypeName(cls);
                                if ("void*".equals(typeName[0])) {
                                    if (!cls.isAnnotationPresent(Opaque.class)) {
                                        typeName[0] = "char*";
                                        this.out.println("    " + typeName[0] + " ptr" + typeName[1] + " = (" + typeName[0] + typeName[1] + ")jlong_to_ptr(env->GetLongField(obj, JavaCPP_addressFID));");
                                        this.out.println("    if (ptr == NULL) {");
                                        this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"This pointer address is NULL.\");");
                                        this.out.println("        return" + (methodInfo.returnType != Void.TYPE ? ";" : " 0;"));
                                        this.out.println("    }");
                                        if (FunctionPointer.class.isAssignableFrom(cls)) {
                                            this.out.println("    if (ptr->ptr == NULL) {");
                                            this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"This function pointer address is NULL.\");");
                                            this.out.println("        return" + (methodInfo.returnType != Void.TYPE ? ";" : " 0;"));
                                            this.out.println("    }");
                                        }
                                        if (!cls.isAnnotationPresent(Opaque.class)) {
                                            this.out.println("    jlong position = env->GetLongField(obj, JavaCPP_positionFID);");
                                            this.out.println("    ptr += position;");
                                            if (methodInfo.bufferGetter) {
                                                this.out.println("    jlong size = env->GetLongField(obj, JavaCPP_limitFID);");
                                                this.out.println("    size -= position;");
                                            }
                                        }
                                    }
                                }
                                if (FunctionPointer.class.isAssignableFrom(cls)) {
                                    this.functions.index(cls);
                                    typeName[0] = functionClassName(cls) + "*";
                                    typeName[1] = "";
                                }
                                this.out.println("    " + typeName[0] + " ptr" + typeName[1] + " = (" + typeName[0] + typeName[1] + ")jlong_to_ptr(env->GetLongField(obj, JavaCPP_addressFID));");
                                this.out.println("    if (ptr == NULL) {");
                                this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"This pointer address is NULL.\");");
                                if (methodInfo.returnType != Void.TYPE) {
                                }
                                this.out.println("        return" + (methodInfo.returnType != Void.TYPE ? ";" : " 0;"));
                                this.out.println("    }");
                                if (FunctionPointer.class.isAssignableFrom(cls)) {
                                    this.out.println("    if (ptr->ptr == NULL) {");
                                    this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"This function pointer address is NULL.\");");
                                    if (methodInfo.returnType != Void.TYPE) {
                                    }
                                    this.out.println("        return" + (methodInfo.returnType != Void.TYPE ? ";" : " 0;"));
                                    this.out.println("    }");
                                }
                                if (cls.isAnnotationPresent(Opaque.class)) {
                                    this.out.println("    jlong position = env->GetLongField(obj, JavaCPP_positionFID);");
                                    this.out.println("    ptr += position;");
                                    if (methodInfo.bufferGetter) {
                                        this.out.println("    jlong size = env->GetLongField(obj, JavaCPP_limitFID);");
                                        this.out.println("    size -= position;");
                                    }
                                }
                            }
                            parametersBefore(methodInfo);
                            call(methodInfo, returnBefore(methodInfo), false);
                            returnAfter(methodInfo);
                            parametersAfter(methodInfo);
                            if (methodInfo.throwsException != null) {
                                this.out.println("    if (exc != NULL) {");
                                this.out.println("        env->Throw(exc);");
                                this.out.println("    }");
                            }
                            if (methodInfo.returnType != Void.TYPE) {
                                this.out.println("    return rarg;");
                            }
                            this.out.println("}");
                        }
                    }
                }
            }
            i++;
        }
        this.out.println();
        return didSomething;
    }

    void parametersBefore(MethodInformation methodInfo) {
        String adapterLine = "";
        AdapterInformation prevAdapterInfo = null;
        int skipParameters = (methodInfo.parameterTypes.length <= 0 || methodInfo.parameterTypes[0] != Class.class) ? 0 : 1;
        int j = skipParameters;
        while (j < methodInfo.parameterTypes.length) {
            if (!methodInfo.parameterTypes[j].isPrimitive()) {
                AdapterInformation adapterInfo;
                Annotation passBy = by(methodInfo, j);
                String cast = cast(methodInfo, j);
                String[] typeName = methodInfo.parameterRaw[j] ? new String[]{""} : cppTypeName(methodInfo.parameterTypes[j]);
                if (methodInfo.parameterRaw[j]) {
                    adapterInfo = null;
                } else {
                    adapterInfo = adapterInformation(false, methodInfo, j);
                }
                if (FunctionPointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                    this.functions.index(methodInfo.parameterTypes[j]);
                    if (methodInfo.parameterTypes[j] == FunctionPointer.class) {
                        this.logger.warn("Method \"" + methodInfo.method + "\" has an abstract FunctionPointer parameter, but a concrete subclass is required. Compilation will most likely fail.");
                    }
                    typeName[0] = functionClassName(methodInfo.parameterTypes[j]) + "*";
                    typeName[1] = "";
                }
                if (typeName[0].length() == 0 || methodInfo.parameterRaw[j]) {
                    methodInfo.parameterRaw[j] = true;
                    typeName[0] = jniTypeName(methodInfo.parameterTypes[j]);
                    this.out.println("    " + typeName[0] + " ptr" + j + " = arg" + j + ";");
                } else {
                    if ("void*".equals(typeName[0]) && !methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                        typeName[0] = "char*";
                    }
                    this.out.print("    " + typeName[0] + " ptr" + j + typeName[1] + " = ");
                    if (Pointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                        this.out.println("arg" + j + " == NULL ? NULL : (" + typeName[0] + typeName[1] + ")jlong_to_ptr(env->GetLongField(arg" + j + ", JavaCPP_addressFID));");
                        if ((j == 0 && FunctionPointer.class.isAssignableFrom(methodInfo.cls) && methodInfo.cls.isAnnotationPresent(Namespace.class)) || (((passBy instanceof ByVal) && ((ByVal) passBy).nullValue().length() == 0) || ((passBy instanceof ByRef) && ((ByRef) passBy).nullValue().length() == 0))) {
                            this.out.println("    if (ptr" + j + " == NULL) {");
                            this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"Pointer address of argument " + j + " is NULL.\");");
                            this.out.println("        return" + (methodInfo.returnType == Void.TYPE ? ";" : " 0;"));
                            this.out.println("    }");
                        }
                        if (!(adapterInfo == null && prevAdapterInfo == null)) {
                            this.out.println("    jlong size" + j + " = arg" + j + " == NULL ? 0 : env->GetLongField(arg" + j + ", JavaCPP_limitFID);");
                            this.out.println("    void* owner" + j + " = JavaCPP_getPointerOwner(env, arg" + j + ");");
                        }
                        if (!methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    jlong position" + j + " = arg" + j + " == NULL ? 0 : env->GetLongField(arg" + j + ", JavaCPP_positionFID);");
                            this.out.println("    ptr" + j + " += position" + j + ";");
                            if (!(adapterInfo == null && prevAdapterInfo == null)) {
                                this.out.println("    size" + j + " -= position" + j + ";");
                            }
                        }
                    } else if (methodInfo.parameterTypes[j] == String.class) {
                        this.passesStrings = true;
                        this.out.println("JavaCPP_getStringBytes(env, arg" + j + ");");
                        if (!(adapterInfo == null && prevAdapterInfo == null)) {
                            this.out.println("    jlong size" + j + " = 0;");
                            this.out.println("    void* owner" + j + " = (void*)ptr" + j + ";");
                        }
                    } else if (methodInfo.parameterTypes[j].isArray() && methodInfo.parameterTypes[j].getComponentType().isPrimitive()) {
                        this.out.print("arg" + j + " == NULL ? NULL : ");
                        String s = methodInfo.parameterTypes[j].getComponentType().getName();
                        if (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter) {
                            this.out.println("(j" + s + "*)env->GetPrimitiveArrayCritical(arg" + j + ", NULL);");
                        } else {
                            this.out.println("env->Get" + (Character.toUpperCase(s.charAt(0)) + s.substring(1)) + "ArrayElements(arg" + j + ", NULL);");
                        }
                        if (!(adapterInfo == null && prevAdapterInfo == null)) {
                            this.out.println("    jlong size" + j + " = arg" + j + " == NULL ? 0 : env->GetArrayLength(arg" + j + ");");
                            this.out.println("    void* owner" + j + " = (void*)ptr" + j + ";");
                        }
                    } else if (Buffer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                        this.out.println("arg" + j + " == NULL ? NULL : (" + typeName[0] + typeName[1] + ")env->GetDirectBufferAddress(arg" + j + ");");
                        if (!(adapterInfo == null && prevAdapterInfo == null)) {
                            this.out.println("    jlong size" + j + " = arg" + j + " == NULL ? 0 : env->GetDirectBufferCapacity(arg" + j + ");");
                            this.out.println("    void* owner" + j + " = (void*)ptr" + j + ";");
                        }
                        if (methodInfo.parameterTypes[j] != Buffer.class) {
                            String paramName = methodInfo.parameterTypes[j].getSimpleName();
                            paramName = paramName.substring(0, paramName.length() - 6);
                            String paramNameLowerCase = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
                            this.out.println("    j" + paramNameLowerCase + "Array arr" + j + " = NULL;");
                            this.out.println("    if (arg" + j + " != NULL && ptr" + j + " == NULL) {");
                            this.out.println("        arr" + j + " = (j" + paramNameLowerCase + "Array)env->CallObjectMethod(arg" + j + ", JavaCPP_arrayMID);");
                            this.out.println("        if (env->ExceptionOccurred() != NULL) {");
                            this.out.println("            env->ExceptionClear();");
                            this.out.println("        } else {");
                            this.out.println("            ptr" + j + " = arr" + j + " == NULL ? NULL : env->Get" + paramName + "ArrayElements(arr" + j + ", NULL);");
                            if (!(adapterInfo == null && prevAdapterInfo == null)) {
                                this.out.println("            size" + j + " = env->GetArrayLength(arr" + j + ");");
                            }
                            this.out.println("        }");
                            this.out.println("    }");
                        }
                    } else {
                        this.out.println("arg" + j + ";");
                        this.logger.warn("Method \"" + methodInfo.method + "\" has an unsupported parameter of type \"" + methodInfo.parameterTypes[j].getCanonicalName() + "\". Compilation will most likely fail.");
                    }
                    if (adapterInfo != null) {
                        this.usesAdapters = true;
                        adapterLine = "    " + adapterInfo.name + " adapter" + j + "(";
                        prevAdapterInfo = adapterInfo;
                    }
                    if (prevAdapterInfo != null) {
                        if (!FunctionPointer.class.isAssignableFrom(methodInfo.cls)) {
                            adapterLine = adapterLine + cast;
                        }
                        adapterLine = adapterLine + "ptr" + j + ", size" + j + ", owner" + j;
                        int i = prevAdapterInfo.argc - 1;
                        prevAdapterInfo.argc = i;
                        if (i > 0) {
                            adapterLine = adapterLine + ", ";
                        }
                    }
                    if (prevAdapterInfo != null && prevAdapterInfo.argc <= 0) {
                        this.out.println(adapterLine + ");");
                        prevAdapterInfo = null;
                    }
                }
            }
            j++;
        }
    }

    String returnBefore(MethodInformation methodInfo) {
        String returnPrefix = "";
        String[] typeName;
        if (methodInfo.returnType != Void.TYPE) {
            String cast = cast(methodInfo.returnType, methodInfo.annotations);
            typeName = methodInfo.returnRaw ? new String[]{""} : cppCastTypeName(methodInfo.returnType, methodInfo.annotations);
            if (methodInfo.valueSetter || methodInfo.memberSetter || methodInfo.noReturnGetter) {
                this.out.println("    jobject rarg = obj;");
            } else if (methodInfo.returnType.isPrimitive()) {
                this.out.println("    " + jniTypeName(methodInfo.returnType) + " rarg = 0;");
                returnPrefix = typeName[0] + " rvalue" + typeName[1] + " = " + cast;
            } else {
                Annotation returnBy = by(methodInfo.annotations);
                String valueTypeName = valueTypeName(typeName);
                returnPrefix = "rptr = " + cast;
                if (typeName[0].length() == 0 || methodInfo.returnRaw) {
                    methodInfo.returnRaw = true;
                    typeName[0] = jniTypeName(methodInfo.returnType);
                    this.out.println("    " + typeName[0] + " rarg = NULL;");
                    this.out.println("    " + typeName[0] + " rptr;");
                } else if (Pointer.class.isAssignableFrom(methodInfo.returnType) || Buffer.class.isAssignableFrom(methodInfo.returnType) || (methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive())) {
                    if (FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                        this.functions.index(methodInfo.returnType);
                        returnPrefix = "if (rptr != NULL) rptr->ptr = ";
                        if (methodInfo.method.isAnnotationPresent(Virtual.class)) {
                            returnPrefix = returnPrefix + "(" + typeName[0] + typeName[1] + ")&";
                        }
                        typeName[0] = functionClassName(methodInfo.returnType) + "*";
                        typeName[1] = "";
                        valueTypeName = valueTypeName(typeName);
                    }
                    if (returnBy instanceof ByVal) {
                        returnPrefix = returnPrefix + (noException(methodInfo.returnType, methodInfo.method) ? "new (std::nothrow) " : "new ") + valueTypeName + typeName[1] + "(";
                    } else if (returnBy instanceof ByRef) {
                        returnPrefix = returnPrefix + "&";
                    } else if (returnBy instanceof ByPtrPtr) {
                        if (cast.length() > 0) {
                            typeName[0] = typeName[0].substring(0, typeName[0].length() - 1);
                        }
                        returnPrefix = "rptr = NULL; " + typeName[0] + "* rptrptr" + typeName[1] + " = " + cast;
                    }
                    if (methodInfo.bufferGetter) {
                        this.out.println("    jobject rarg = NULL;");
                        this.out.println("    char* rptr;");
                    } else {
                        this.out.println("    " + jniTypeName(methodInfo.returnType) + " rarg = NULL;");
                        this.out.println("    " + typeName[0] + " rptr" + typeName[1] + ";");
                    }
                    if (FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                        this.out.println("    rptr = new (std::nothrow) " + valueTypeName + ";");
                    }
                } else if (methodInfo.returnType == String.class) {
                    this.out.println("    jstring rarg = NULL;");
                    this.out.println("    const char* rptr;");
                    returnPrefix = returnBy instanceof ByRef ? "std::string rstr(" : returnPrefix + "(const char*)";
                } else {
                    this.logger.warn("Method \"" + methodInfo.method + "\" has unsupported return type \"" + methodInfo.returnType.getCanonicalName() + "\". Compilation will most likely fail.");
                }
                AdapterInformation adapterInfo = adapterInformation(false, valueTypeName, methodInfo.annotations);
                if (adapterInfo != null) {
                    this.usesAdapters = true;
                    returnPrefix = adapterInfo.name + " radapter(";
                }
            }
        } else if (methodInfo.allocator || methodInfo.arrayAllocator) {
            this.jclasses.index(methodInfo.cls);
            typeName = cppTypeName(methodInfo.cls);
            returnPrefix = typeName[0] + " rptr" + typeName[1] + " = ";
        }
        if (methodInfo.throwsException != null) {
            this.out.println("    jthrowable exc = NULL;");
            this.out.println("    try {");
        }
        return returnPrefix;
    }

    void call(MethodInformation methodInfo, String returnPrefix, boolean secondCall) {
        boolean needSecondCall = false;
        String indent = secondCall ? "" : methodInfo.throwsException != null ? "        " : "    ";
        String prefix = "(";
        String suffix = ")";
        int skipParameters = (methodInfo.parameterTypes.length <= 0 || methodInfo.parameterTypes[0] != Class.class) ? 0 : 1;
        boolean index = methodInfo.method.isAnnotationPresent(Index.class) || (methodInfo.pairedMethod != null && methodInfo.pairedMethod.isAnnotationPresent(Index.class));
        if (methodInfo.deallocator) {
            this.out.println(indent + "void* allocatedAddress = jlong_to_ptr(arg0);");
            this.out.println(indent + "void (*deallocatorAddress)(void*) = (void(*)(void*))jlong_to_ptr(arg1);");
            this.out.println(indent + "if (deallocatorAddress != NULL && allocatedAddress != NULL) {");
            this.out.println(indent + "    (*deallocatorAddress)(allocatedAddress);");
            this.out.println(indent + "}");
            return;
        }
        if (!FunctionPointer.class.isAssignableFrom(methodInfo.cls) && (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter)) {
            boolean wantsPointer = false;
            int k = methodInfo.parameterTypes.length - 1;
            if ((methodInfo.valueSetter || methodInfo.memberSetter) && !(by(methodInfo, k) instanceof ByRef) && adapterInformation(false, methodInfo, k) == null && methodInfo.parameterTypes[k] == String.class) {
                this.out.print(indent + "strcpy((char*)");
                wantsPointer = true;
                prefix = ", ";
            } else if (k >= 1 && methodInfo.parameterTypes[0].isArray() && methodInfo.parameterTypes[0].getComponentType().isPrimitive() && (methodInfo.parameterTypes[1] == Integer.TYPE || methodInfo.parameterTypes[1] == Long.TYPE)) {
                this.out.print(indent + "memcpy(");
                wantsPointer = true;
                prefix = ", ";
                if (methodInfo.memberGetter || methodInfo.valueGetter) {
                    this.out.print("ptr0 + arg1, ");
                } else {
                    prefix = prefix + "ptr0 + arg1, ";
                }
                skipParameters = 2;
                suffix = " * sizeof(*ptr0)" + suffix;
            } else {
                this.out.print(indent + returnPrefix);
                prefix = (methodInfo.valueGetter || methodInfo.memberGetter) ? "" : " = ";
                suffix = "";
            }
            if (Modifier.isStatic(methodInfo.modifiers)) {
                this.out.print(cppScopeName(methodInfo));
            } else if (!methodInfo.memberGetter && !methodInfo.memberSetter) {
                PrintWriter printWriter = this.out;
                String str = index ? "(*ptr)" : (methodInfo.dim > 0 || wantsPointer) ? "ptr" : "*ptr";
                printWriter.print(str);
            } else if (index) {
                this.out.print("(*ptr)");
                prefix = "." + methodInfo.memberName[0] + prefix;
            } else {
                this.out.print("ptr->" + methodInfo.memberName[0]);
            }
        } else if (methodInfo.bufferGetter) {
            this.out.print(indent + returnPrefix + "ptr");
            prefix = "";
            suffix = "";
        } else {
            this.out.print(indent + returnPrefix);
            if (FunctionPointer.class.isAssignableFrom(methodInfo.cls)) {
                if (methodInfo.cls.isAnnotationPresent(Namespace.class)) {
                    this.out.print("(ptr0->*(ptr->ptr))");
                    skipParameters = 1;
                    if (methodInfo.valueGetter || methodInfo.valueSetter) {
                        prefix = methodInfo.valueGetter ? "" : " = ";
                        suffix = "";
                    }
                } else {
                    this.out.print("(*ptr->ptr)");
                }
            } else if (methodInfo.allocator) {
                String[] typeName = cppTypeName(methodInfo.cls);
                valueTypeName = valueTypeName(typeName);
                if (this.virtualFunctions.containsKey(methodInfo.cls)) {
                    valueTypeName = "JavaCPP_" + mangle(valueTypeName);
                }
                if (methodInfo.cls == Pointer.class) {
                    prefix = "";
                    suffix = "";
                } else {
                    this.out.print((noException(methodInfo.cls, methodInfo.method) ? "new (std::nothrow) " : "new ") + valueTypeName + typeName[1]);
                    if (methodInfo.arrayAllocator) {
                        prefix = "[";
                        suffix = "]";
                    }
                }
            } else if (Modifier.isStatic(methodInfo.modifiers)) {
                this.out.print(cppScopeName(methodInfo));
            } else {
                String name = methodInfo.memberName[0];
                valueTypeName = valueTypeName(cppTypeName(methodInfo.cls));
                if (this.virtualFunctions.containsKey(methodInfo.cls) && !secondCall) {
                    String subType = "JavaCPP_" + mangle(valueTypeName);
                    if (Modifier.isPublic(methodInfo.method.getModifiers())) {
                        this.out.print("(dynamic_cast<" + subType + "*>(ptr) != NULL ? ");
                        needSecondCall = true;
                    }
                    if (methodInfo.method.isAnnotationPresent(Virtual.class)) {
                        name = "super_" + name;
                    }
                    this.out.print("((" + subType + "*)ptr)->" + name);
                } else if (index) {
                    this.out.print("(*ptr)");
                    prefix = "." + name + prefix;
                } else {
                    String op = name.startsWith("operator") ? name.substring(8).trim() : "";
                    if (methodInfo.parameterTypes.length <= 0 || !(op.equals("=") || op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%") || op.equals("==") || op.equals("!=") || op.equals("<") || op.equals(">") || op.equals("<=") || op.equals(">="))) {
                        this.out.print("ptr->" + name);
                    } else {
                        this.out.print("((*ptr)");
                        prefix = op + prefix;
                        suffix = suffix + ")";
                    }
                }
            }
        }
        int j = skipParameters;
        while (j <= methodInfo.parameterTypes.length) {
            if (j == methodInfo.dim + skipParameters) {
                if (methodInfo.memberName.length > 1) {
                    this.out.print(methodInfo.memberName[1]);
                }
                this.out.print(prefix);
                if (methodInfo.withEnv) {
                    this.out.print(Modifier.isStatic(methodInfo.modifiers) ? "env, cls" : "env, obj");
                    if ((methodInfo.parameterTypes.length - skipParameters) - methodInfo.dim > 0) {
                        this.out.print(", ");
                    }
                }
            }
            if (j == methodInfo.parameterTypes.length) {
                break;
            }
            AdapterInformation adapterInfo;
            if (j < methodInfo.dim + skipParameters) {
                this.out.print("[");
            }
            Annotation passBy = by(methodInfo, j);
            String cast = cast(methodInfo, j);
            if (methodInfo.parameterRaw[j]) {
                adapterInfo = null;
            } else {
                adapterInfo = adapterInformation(false, methodInfo, j);
            }
            if (("(void*)".equals(cast) || "(void *)".equals(cast)) && methodInfo.parameterTypes[j] == Long.TYPE) {
                this.out.print("jlong_to_ptr(arg" + j + ")");
            } else if (methodInfo.parameterTypes[j].isPrimitive()) {
                this.out.print(cast + "arg" + j);
            } else if (adapterInfo != null) {
                cast = adapterInfo.cast.trim();
                if (!(cast.length() <= 0 || cast.startsWith("(") || cast.endsWith(")"))) {
                    cast = "(" + cast + ")";
                }
                String cast2 = adapterInfo.cast2.trim();
                if (!(cast2.length() <= 0 || cast2.startsWith("(") || cast2.endsWith(")"))) {
                    cast2 = "(" + cast2 + ")";
                }
                this.out.print(cast + cast2 + "adapter" + j);
                j += adapterInfo.argc - 1;
            } else if (!FunctionPointer.class.isAssignableFrom(methodInfo.parameterTypes[j]) || (passBy instanceof ByVal) || (passBy instanceof ByRef)) {
                if ((passBy instanceof ByVal) || ((passBy instanceof ByRef) && methodInfo.parameterTypes[j] != String.class)) {
                    String nullValue = passBy instanceof ByVal ? ((ByVal) passBy).nullValue() : passBy instanceof ByRef ? ((ByRef) passBy).nullValue() : "";
                    this.out.print((nullValue.length() > 0 ? "ptr" + j + " == NULL ? " + nullValue + " : " : "") + "*" + cast + "ptr" + j);
                } else if (passBy instanceof ByPtrPtr) {
                    this.out.print(cast + "(arg" + j + " == NULL ? NULL : &ptr" + j + ")");
                } else {
                    this.out.print(cast + "ptr" + j);
                }
            } else if (passBy instanceof ByPtrRef) {
                this.out.print(cast + "(ptr" + j + "->ptr)");
            } else {
                this.out.print(cast + "(ptr" + j + " == NULL ? NULL : " + (passBy instanceof ByPtrPtr ? "&ptr" : "ptr") + j + "->ptr)");
            }
            if (j < methodInfo.dim + skipParameters) {
                this.out.print("]");
            } else if (j < methodInfo.parameterTypes.length - 1) {
                this.out.print(", ");
            }
            j++;
        }
        this.out.print(suffix);
        if (methodInfo.memberName.length > 2) {
            this.out.print(methodInfo.memberName[2]);
        }
        if ((by(methodInfo.annotations) instanceof ByRef) && methodInfo.returnType == String.class) {
            this.out.print(");\n" + indent + "rptr = rstr.c_str()");
        }
        if (needSecondCall) {
            call(methodInfo, " : ", true);
            this.out.print(")");
        }
    }

    void returnAfter(MethodInformation methodInfo) {
        String indent = methodInfo.throwsException != null ? "        " : "    ";
        String[] typeName = methodInfo.returnRaw ? new String[]{""} : cppCastTypeName(methodInfo.returnType, methodInfo.annotations);
        Annotation returnBy = by(methodInfo.annotations);
        AdapterInformation adapterInfo = adapterInformation(false, valueTypeName(typeName), methodInfo.annotations);
        String suffix = methodInfo.deallocator ? "" : ";";
        if (!(methodInfo.returnType.isPrimitive() || adapterInfo == null)) {
            suffix = ")" + suffix;
        }
        if (Pointer.class.isAssignableFrom(methodInfo.returnType) || ((methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive()) || Buffer.class.isAssignableFrom(methodInfo.returnType))) {
            if ((returnBy instanceof ByVal) && adapterInfo == null) {
                suffix = ")" + suffix;
            } else if (returnBy instanceof ByPtrPtr) {
                this.out.println(suffix);
                suffix = "";
                this.out.println(indent + "if (rptrptr == NULL) {");
                this.out.println(indent + "    env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.index(NullPointerException.class) + "), \"Return pointer address is NULL.\");");
                this.out.println(indent + "} else {");
                this.out.println(indent + "    rptr = *rptrptr;");
                this.out.println(indent + "}");
            }
        }
        this.out.println(suffix);
        if (methodInfo.returnType == Void.TYPE) {
            if (methodInfo.allocator || methodInfo.arrayAllocator) {
                this.out.println(indent + "jlong rcapacity = " + (methodInfo.arrayAllocator ? "arg0;" : "1;"));
                boolean noDeallocator = methodInfo.cls == Pointer.class || methodInfo.cls.isAnnotationPresent(NoDeallocator.class) || methodInfo.method.isAnnotationPresent(NoDeallocator.class);
                this.out.print(indent + "JavaCPP_initPointer(env, obj, rptr, rcapacity, rptr, ");
                if (noDeallocator) {
                    this.out.println("NULL);");
                } else if (methodInfo.arrayAllocator) {
                    this.out.println("&JavaCPP_" + mangle(methodInfo.cls.getName()) + "_deallocateArray);");
                    this.arrayDeallocators.index(methodInfo.cls);
                } else {
                    this.out.println("&JavaCPP_" + mangle(methodInfo.cls.getName()) + "_deallocate);");
                    this.deallocators.index(methodInfo.cls);
                }
                if (this.virtualFunctions.containsKey(methodInfo.cls)) {
                    this.out.println(indent + "((" + ("JavaCPP_" + mangle(valueTypeName(cppTypeName(methodInfo.cls)))) + "*)rptr)->obj = env->NewWeakGlobalRef(obj);");
                }
            }
        } else if (!methodInfo.valueSetter && !methodInfo.memberSetter && !methodInfo.noReturnGetter) {
            if (methodInfo.returnType.isPrimitive()) {
                this.out.println(indent + "rarg = (" + jniTypeName(methodInfo.returnType) + ")rvalue;");
            } else if (methodInfo.returnRaw) {
                this.out.println(indent + "rarg = rptr;");
            } else {
                boolean needInit = false;
                if (adapterInfo != null) {
                    this.out.println(indent + "rptr = radapter;");
                    if (methodInfo.returnType != String.class) {
                        this.out.println(indent + "jlong rcapacity = (jlong)radapter.size;");
                        if (Pointer.class.isAssignableFrom(methodInfo.returnType)) {
                            this.out.println(indent + "void* rowner = radapter.owner;");
                        }
                        this.out.println(indent + "void (*deallocator)(void*) = " + (adapterInfo.constant ? "NULL;" : "&" + adapterInfo.name + "::deallocate;"));
                    }
                    needInit = true;
                } else if ((returnBy instanceof ByVal) || FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                    this.out.println(indent + "jlong rcapacity = 1;");
                    this.out.println(indent + "void* rowner = (void*)rptr;");
                    this.out.println(indent + "void (*deallocator)(void*) = &JavaCPP_" + mangle(methodInfo.returnType.getName()) + "_deallocate;");
                    this.deallocators.index(methodInfo.returnType);
                    needInit = true;
                }
                if (Pointer.class.isAssignableFrom(methodInfo.returnType)) {
                    this.out.print(indent);
                    if (!(returnBy instanceof ByVal)) {
                        if (Modifier.isStatic(methodInfo.modifiers) && methodInfo.parameterTypes.length > 0) {
                            int i = 0;
                            while (i < methodInfo.parameterTypes.length) {
                                String cast = cast(methodInfo, i);
                                if (Arrays.equals(methodInfo.parameterAnnotations[i], methodInfo.annotations) && methodInfo.parameterTypes[i] == methodInfo.returnType) {
                                    this.out.println("if (rptr == " + cast + "ptr" + i + ") {");
                                    this.out.println(indent + "    rarg = arg" + i + ";");
                                    this.out.print(indent + "} else ");
                                }
                                i++;
                            }
                        } else if (!Modifier.isStatic(methodInfo.modifiers) && methodInfo.cls == methodInfo.returnType) {
                            this.out.println("if (rptr == ptr) {");
                            this.out.println(indent + "    rarg = obj;");
                            this.out.print(indent + "} else ");
                        }
                    }
                    this.out.println("if (rptr != NULL) {");
                    PrintWriter printWriter = this.out;
                    StringBuilder append = new StringBuilder().append(indent).append("    rarg = JavaCPP_createPointer(env, ").append(this.jclasses.index(methodInfo.returnType));
                    String str = (methodInfo.parameterTypes.length <= 0 || methodInfo.parameterTypes[0] != Class.class) ? ");" : ", arg0);";
                    printWriter.println(append.append(str).toString());
                    this.out.println(indent + "    if (rarg != NULL) {");
                    if (needInit) {
                        this.out.println(indent + "        JavaCPP_initPointer(env, rarg, rptr, rcapacity, rowner, deallocator);");
                    } else {
                        this.out.println(indent + "        env->SetLongField(rarg, JavaCPP_addressFID, ptr_to_jlong(rptr));");
                    }
                    this.out.println(indent + "    }");
                    this.out.println(indent + "}");
                } else if (methodInfo.returnType == String.class) {
                    this.passesStrings = true;
                    this.out.println(indent + "if (rptr != NULL) {");
                    this.out.println(indent + "    rarg = JavaCPP_createString(env, rptr);");
                    this.out.println(indent + "}");
                } else if (methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive()) {
                    if (adapterInfo == null && !(returnBy instanceof ByVal)) {
                        this.out.println(indent + "jlong rcapacity = rptr != NULL ? 1 : 0;");
                    }
                    String componentName = methodInfo.returnType.getComponentType().getName();
                    String componentNameUpperCase = Character.toUpperCase(componentName.charAt(0)) + componentName.substring(1);
                    this.out.println(indent + "if (rptr != NULL) {");
                    this.out.println(indent + "    rarg = env->New" + componentNameUpperCase + "Array(rcapacity < INT_MAX ? rcapacity : INT_MAX);");
                    this.out.println(indent + "    env->Set" + componentNameUpperCase + "ArrayRegion(rarg, 0, rcapacity < INT_MAX ? rcapacity : INT_MAX, (j" + componentName + "*)rptr);");
                    this.out.println(indent + "}");
                    if (adapterInfo != null) {
                        this.out.println(indent + "if (deallocator != 0 && rptr != NULL) {");
                        this.out.println(indent + "    (*(void(*)(void*))jlong_to_ptr(deallocator))((void*)rptr);");
                        this.out.println(indent + "}");
                    }
                } else if (Buffer.class.isAssignableFrom(methodInfo.returnType)) {
                    if (methodInfo.bufferGetter) {
                        this.out.println(indent + "jlong rcapacity = size;");
                    } else if (adapterInfo == null && !(returnBy instanceof ByVal)) {
                        this.out.println(indent + "jlong rcapacity = rptr != NULL ? 1 : 0;");
                    }
                    this.out.println(indent + "if (rptr != NULL) {");
                    this.out.println(indent + "    jlong rcapacityptr = rcapacity * sizeof(rptr[0]);");
                    this.out.println(indent + "    rarg = env->NewDirectByteBuffer((void*)rptr, rcapacityptr < INT_MAX ? rcapacityptr : INT_MAX);");
                    this.out.println(indent + "}");
                }
            }
        }
    }

    void parametersAfter(MethodInformation methodInfo) {
        if (methodInfo.throwsException != null) {
            this.mayThrowExceptions = true;
            this.out.println("    } catch (...) {");
            this.out.println("        exc = JavaCPP_handleException(env, " + this.jclasses.index(methodInfo.throwsException) + ");");
            this.out.println("    }");
            this.out.println();
        }
        int skipParameters = (methodInfo.parameterTypes.length <= 0 || methodInfo.parameterTypes[0] != Class.class) ? 0 : 1;
        int j = skipParameters;
        while (j < methodInfo.parameterTypes.length) {
            if (!methodInfo.parameterRaw[j]) {
                String releaseArrayFlag;
                Annotation passBy = by(methodInfo, j);
                String cast = cast(methodInfo, j);
                String[] typeName = cppCastTypeName(methodInfo.parameterTypes[j], methodInfo.parameterAnnotations[j]);
                AdapterInformation adapterInfo = adapterInformation(true, methodInfo, j);
                if ("void*".equals(typeName[0]) && !methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                    typeName[0] = "char*";
                }
                if (cast.contains(" const *") || cast.startsWith("(const ")) {
                    releaseArrayFlag = "JNI_ABORT";
                } else {
                    releaseArrayFlag = "0";
                }
                int k;
                if (Pointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                    if (adapterInfo != null) {
                        k = 0;
                        while (k < adapterInfo.argc) {
                            this.out.println("    " + typeName[0] + " rptr" + (j + k) + typeName[1] + " = " + cast + "adapter" + j + ";");
                            this.out.println("    jlong rsize" + (j + k) + " = (jlong)adapter" + j + ".size" + (k > 0 ? (k + 1) + ";" : ";"));
                            this.out.println("    void* rowner" + (j + k) + " = adapter" + j + ".owner" + (k > 0 ? (k + 1) + ";" : ";"));
                            this.out.println("    if (rptr" + (j + k) + " != " + cast + "ptr" + (j + k) + ") {");
                            this.out.println("        JavaCPP_initPointer(env, arg" + j + ", rptr" + (j + k) + ", rsize" + (j + k) + ", rowner" + (j + k) + ", &" + adapterInfo.name + "::deallocate);");
                            this.out.println("    } else {");
                            this.out.println("        env->SetLongField(arg" + j + ", JavaCPP_limitFID, rsize" + (j + k) + " + position" + (j + k) + ");");
                            this.out.println("    }");
                            k++;
                        }
                    } else if (!((!(passBy instanceof ByPtrPtr) && !(passBy instanceof ByPtrRef)) || methodInfo.valueSetter || methodInfo.memberSetter)) {
                        if (!methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    ptr" + j + " -= position" + j + ";");
                        }
                        this.out.println("    if (arg" + j + " != NULL) env->SetLongField(arg" + j + ", JavaCPP_addressFID, ptr_to_jlong(ptr" + j + "));");
                    }
                } else if (methodInfo.parameterTypes[j] == String.class) {
                    this.out.println("    JavaCPP_releaseStringBytes(env, arg" + j + ", ptr" + j + ");");
                } else if (methodInfo.parameterTypes[j].isArray() && methodInfo.parameterTypes[j].getComponentType().isPrimitive()) {
                    k = 0;
                    while (adapterInfo != null && k < adapterInfo.argc) {
                        this.out.println("    " + typeName[0] + " rptr" + (j + k) + typeName[1] + " = " + cast + "adapter" + j + ";");
                        this.out.println("    void* rowner" + (j + k) + " = adapter" + j + ".owner" + (k > 0 ? (k + 1) + ";" : ";"));
                        this.out.println("    if (rptr" + (j + k) + " != " + cast + "ptr" + (j + k) + ") {");
                        this.out.println("        " + adapterInfo.name + "::deallocate(rowner" + (j + k) + ");");
                        this.out.println("    }");
                        k++;
                    }
                    this.out.print("    if (arg" + j + " != NULL) ");
                    if (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter) {
                        this.out.println("env->ReleasePrimitiveArrayCritical(arg" + j + ", ptr" + j + ", " + releaseArrayFlag + ");");
                    } else {
                        String componentType = methodInfo.parameterTypes[j].getComponentType().getName();
                        this.out.println("env->Release" + (Character.toUpperCase(componentType.charAt(0)) + componentType.substring(1)) + "ArrayElements(arg" + j + ", (j" + componentType + "*)ptr" + j + ", " + releaseArrayFlag + ");");
                    }
                } else if (Buffer.class.isAssignableFrom(methodInfo.parameterTypes[j]) && methodInfo.parameterTypes[j] != Buffer.class) {
                    k = 0;
                    while (adapterInfo != null && k < adapterInfo.argc) {
                        this.out.println("    " + typeName[0] + " rptr" + (j + k) + typeName[1] + " = " + cast + "adapter" + j + ";");
                        this.out.println("    void* rowner" + (j + k) + " = adapter" + j + ".owner" + (k > 0 ? (k + 1) + ";" : ";"));
                        this.out.println("    if (rptr" + (j + k) + " != " + cast + "ptr" + (j + k) + ") {");
                        this.out.println("        " + adapterInfo.name + "::deallocate(rowner" + (j + k) + ");");
                        this.out.println("    }");
                        k++;
                    }
                    this.out.print("    if (arr" + j + " != NULL) ");
                    String parameterSimpleName = methodInfo.parameterTypes[j].getSimpleName();
                    parameterSimpleName = parameterSimpleName.substring(0, parameterSimpleName.length() - 6);
                    this.out.println("env->Release" + parameterSimpleName + "ArrayElements(arr" + j + ", (j" + (Character.toLowerCase(parameterSimpleName.charAt(0)) + parameterSimpleName.substring(1)) + "*)ptr" + j + ", " + releaseArrayFlag + ");");
                }
            }
            j++;
        }
    }

    void callback(Class<?> cls, Method callbackMethod, String callbackName, boolean needDefinition, MethodInformation methodInfo) {
        String valueTypeName;
        int j;
        Class callbackReturnType = callbackMethod.getReturnType();
        Class<?>[] callbackParameterTypes = callbackMethod.getParameterTypes();
        Annotation[] callbackAnnotations = callbackMethod.getAnnotations();
        Annotation[][] callbackParameterAnnotations = callbackMethod.getParameterAnnotations();
        String instanceTypeName = functionClassName(cls);
        String[] callbackTypeName = cppFunctionTypeName(callbackMethod);
        String[] returnConvention = callbackTypeName[0].split("\\(");
        returnConvention[1] = constValueTypeName(returnConvention[1]);
        String parameterDeclaration = callbackTypeName[1].substring(1);
        String fieldName = mangle(callbackMethod.getName()) + "__" + mangle(signature(callbackMethod.getParameterTypes()));
        String firstLine = "";
        if (methodInfo != null) {
            Map map;
            String nonconstParamDeclaration = parameterDeclaration.endsWith(" const") ? parameterDeclaration.substring(0, parameterDeclaration.length() - 6) : parameterDeclaration;
            valueTypeName = valueTypeName(methodInfo.returnRaw ? new String[]{""} : cppTypeName(methodInfo.cls));
            String subType = "JavaCPP_" + mangle(valueTypeName);
            Set<String> memberList = (Set) this.virtualMembers.get(cls);
            if (memberList == null) {
                map = this.virtualMembers;
                memberList = new LinkedHashSet();
                map.put(cls, memberList);
            }
            String member = "    ";
            if (!methodInfo.arrayAllocator) {
                if (methodInfo.allocator) {
                    member = member + subType + nonconstParamDeclaration + " : " + valueTypeName + "(";
                    for (j = 0; j < callbackParameterTypes.length; j++) {
                        member = member + "arg" + j;
                        if (j < callbackParameterTypes.length - 1) {
                            member = member + ", ";
                        }
                    }
                    member = member + "), obj(NULL) { }";
                } else {
                    Set<String> functionList = (Set) this.virtualFunctions.get(cls);
                    if (functionList == null) {
                        map = this.virtualFunctions;
                        functionList = new LinkedHashSet();
                        map.put(cls, functionList);
                    }
                    member = member + "using " + valueTypeName + "::" + methodInfo.memberName[0] + ";\n    virtual " + returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + methodInfo.memberName[0] + parameterDeclaration + ";\n    " + returnConvention[0] + "super_" + methodInfo.memberName[0] + nonconstParamDeclaration + " { ";
                    if (((Virtual) methodInfo.method.getAnnotation(Virtual.class)).value()) {
                        member = member + "throw JavaCPP_exception(\"Cannot call a pure virtual function.\"); }";
                    } else {
                        member = member + (callbackReturnType != Void.TYPE ? "return " : "") + valueTypeName + "::" + methodInfo.memberName[0] + "(";
                        for (j = 0; j < callbackParameterTypes.length; j++) {
                            member = member + "arg" + j;
                            if (j < callbackParameterTypes.length - 1) {
                                member = member + ", ";
                            }
                        }
                        member = member + "); }";
                    }
                    firstLine = returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + subType + "::" + methodInfo.memberName[0] + parameterDeclaration + " {";
                    functionList.add(fieldName);
                }
                memberList.add(member);
            } else {
                return;
            }
        } else if (callbackName != null) {
            this.callbacks.index("static " + instanceTypeName + " " + callbackName + "_instance;");
            Convention convention = (Convention) cls.getAnnotation(Convention.class);
            if (!(convention == null || convention.extern().equals("C"))) {
                this.out.println("extern \"" + convention.extern() + "\" {");
                if (this.out2 != null) {
                    this.out2.println("extern \"" + convention.extern() + "\" {");
                }
            }
            if (this.out2 != null) {
                this.out2.println("JNIIMPORT " + returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + callbackName + parameterDeclaration + ";");
            }
            this.out.println("JNIEXPORT " + returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + callbackName + parameterDeclaration + " {");
            this.out.print((callbackReturnType != Void.TYPE ? "    return " : "    ") + callbackName + "_instance(");
            for (j = 0; j < callbackParameterTypes.length; j++) {
                this.out.print("arg" + j);
                if (j < callbackParameterTypes.length - 1) {
                    this.out.print(", ");
                }
            }
            this.out.println(");");
            this.out.println("}");
            if (!(convention == null || convention.extern().equals("C"))) {
                this.out.println("}");
                if (this.out2 != null) {
                    this.out2.println("}");
                }
            }
            firstLine = returnConvention[0] + instanceTypeName + "::operator()" + parameterDeclaration + " {";
        }
        if (needDefinition) {
            Annotation passBy;
            String[] typeName;
            AdapterInformation adapterInfo;
            String cast;
            String s;
            this.out.println(firstLine);
            String returnPrefix = "";
            if (callbackReturnType != Void.TYPE) {
                this.out.println("    " + jniTypeName(callbackReturnType) + " rarg = 0;");
                returnPrefix = "rarg = ";
                if (callbackReturnType == String.class) {
                    returnPrefix = returnPrefix + "(jstring)";
                }
            }
            String callbackReturnCast = cast(callbackReturnType, callbackAnnotations);
            Annotation returnBy = by(callbackAnnotations);
            String[] returnTypeName = cppTypeName(callbackReturnType);
            AdapterInformation returnAdapterInfo = adapterInformation(false, valueTypeName(returnTypeName), callbackAnnotations);
            this.out.println("    jthrowable exc = NULL;");
            this.out.println("    JNIEnv* env;");
            this.out.println("    bool attached = JavaCPP_getEnv(&env);");
            this.out.println("    if (env == NULL) {");
            this.out.println("        goto end;");
            this.out.println("    }");
            this.out.println("{");
            if (callbackParameterTypes.length > 0) {
                this.out.println("    jvalue args[" + callbackParameterTypes.length + "];");
                j = 0;
                while (j < callbackParameterTypes.length) {
                    if (callbackParameterTypes[j].isPrimitive()) {
                        this.out.println("    args[" + j + "]." + signature(callbackParameterTypes[j]).toLowerCase() + " = (" + jniTypeName(callbackParameterTypes[j]) + ")arg" + j + ";");
                    } else {
                        passBy = by(callbackParameterAnnotations[j]);
                        typeName = cppTypeName(callbackParameterTypes[j]);
                        valueTypeName = valueTypeName(typeName);
                        adapterInfo = adapterInformation(false, valueTypeName, callbackParameterAnnotations[j]);
                        if (adapterInfo != null) {
                            this.usesAdapters = true;
                            this.out.println("    " + adapterInfo.name + " adapter" + j + "(arg" + j + ");");
                        }
                        if (Pointer.class.isAssignableFrom(callbackParameterTypes[j]) || Buffer.class.isAssignableFrom(callbackParameterTypes[j]) || (callbackParameterTypes[j].isArray() && callbackParameterTypes[j].getComponentType().isPrimitive())) {
                            cast = "(" + typeName[0] + typeName[1] + ")";
                            if (FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j])) {
                                this.functions.index(callbackParameterTypes[j]);
                                typeName[0] = functionClassName(callbackParameterTypes[j]) + "*";
                                typeName[1] = "";
                                valueTypeName = valueTypeName(typeName);
                            } else if (this.virtualFunctions.containsKey(callbackParameterTypes[j])) {
                                valueTypeName = "JavaCPP_" + mangle(valueTypeName);
                            }
                            this.out.println("    " + jniTypeName(callbackParameterTypes[j]) + " obj" + j + " = NULL;");
                            this.out.println("    " + typeName[0] + " ptr" + j + typeName[1] + " = NULL;");
                            if (FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j])) {
                                this.out.println("    ptr" + j + " = new (std::nothrow) " + valueTypeName + ";");
                                this.out.println("    if (ptr" + j + " != NULL) {");
                                this.out.println("        ptr" + j + "->ptr = " + cast + "&arg" + j + ";");
                                this.out.println("    }");
                            } else if (adapterInfo != null) {
                                this.out.println("    ptr" + j + " = adapter" + j + ";");
                            } else if ((passBy instanceof ByVal) && callbackParameterTypes[j] != Pointer.class) {
                                this.out.println("    ptr" + j + (noException(callbackParameterTypes[j], callbackMethod) ? " = new (std::nothrow) " : " = new ") + valueTypeName + typeName[1] + "(*" + cast + "&arg" + j + ");");
                            } else if ((passBy instanceof ByVal) || (passBy instanceof ByRef)) {
                                this.out.println("    ptr" + j + " = " + cast + "&arg" + j + ";");
                            } else if (passBy instanceof ByPtrPtr) {
                                this.out.println("    if (arg" + j + " == NULL) {");
                                this.out.println("        JavaCPP_log(\"Pointer address of argument " + j + " is NULL in callback for " + cls.getCanonicalName() + ".\");");
                                this.out.println("    } else {");
                                this.out.println("        ptr" + j + " = " + cast + "*arg" + j + ";");
                                this.out.println("    }");
                            } else {
                                this.out.println("    ptr" + j + " = " + cast + "arg" + j + ";");
                            }
                        }
                        boolean needInit = false;
                        if (adapterInfo != null) {
                            if (callbackParameterTypes[j] != String.class) {
                                this.out.println("    jlong size" + j + " = (jlong)adapter" + j + ".size;");
                                this.out.println("    void* owner" + j + " = adapter" + j + ".owner;");
                                this.out.println("    void (*deallocator" + j + ")(void*) = &" + adapterInfo.name + "::deallocate;");
                            }
                            needInit = true;
                        } else if (((passBy instanceof ByVal) && callbackParameterTypes[j] != Pointer.class) || FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j])) {
                            this.out.println("    jlong size" + j + " = 1;");
                            this.out.println("    void* owner" + j + " = ptr" + j + ";");
                            this.out.println("    void (*deallocator" + j + ")(void*) = &JavaCPP_" + mangle(callbackParameterTypes[j].getName()) + "_deallocate;");
                            this.deallocators.index(callbackParameterTypes[j]);
                            needInit = true;
                        }
                        if (Pointer.class.isAssignableFrom(callbackParameterTypes[j])) {
                            s = "    obj" + j + " = JavaCPP_createPointer(env, " + this.jclasses.index(callbackParameterTypes[j]) + ");";
                            if (adapterInformation(true, valueTypeName, callbackParameterAnnotations[j]) != null || (passBy instanceof ByPtrPtr) || (passBy instanceof ByPtrRef)) {
                                this.out.println(s);
                            } else {
                                this.out.println("    if (ptr" + j + " != NULL) { ");
                                this.out.println("    " + s);
                                this.out.println("    }");
                            }
                            this.out.println("    if (obj" + j + " != NULL) { ");
                            if (needInit) {
                                this.out.println("        JavaCPP_initPointer(env, obj" + j + ", ptr" + j + ", size" + j + ", owner" + j + ", deallocator" + j + ");");
                            } else {
                                this.out.println("        env->SetLongField(obj" + j + ", JavaCPP_addressFID, ptr_to_jlong(ptr" + j + "));");
                            }
                            this.out.println("    }");
                            this.out.println("    args[" + j + "].l = obj" + j + ";");
                        } else if (callbackParameterTypes[j] == String.class) {
                            this.passesStrings = true;
                            this.out.println("    jstring obj" + j + " = JavaCPP_createString(env, (const char*)" + (adapterInfo != null ? "adapter" : "arg") + j + ");");
                            this.out.println("    args[" + j + "].l = obj" + j + ";");
                        } else if (callbackParameterTypes[j].isArray() && callbackParameterTypes[j].getComponentType().isPrimitive()) {
                            if (adapterInfo == null) {
                                this.out.println("    jlong size" + j + " = ptr" + j + " != NULL ? 1 : 0;");
                            }
                            String componentType = callbackParameterTypes[j].getComponentType().getName();
                            String S = Character.toUpperCase(componentType.charAt(0)) + componentType.substring(1);
                            this.out.println("    if (ptr" + j + " != NULL) {");
                            this.out.println("        obj" + j + " = env->New" + S + "Array(size" + j + " < INT_MAX ? size" + j + " : INT_MAX);");
                            this.out.println("        env->Set" + S + "ArrayRegion(obj" + j + ", 0, size" + j + " < INT_MAX ? size" + j + " : INT_MAX, (j" + componentType + "*)ptr" + j + ");");
                            this.out.println("    }");
                            if (adapterInfo != null) {
                                this.out.println("    if (deallocator" + j + " != 0 && ptr" + j + " != NULL) {");
                                this.out.println("        (*(void(*)(void*))jlong_to_ptr(deallocator" + j + "))((void*)ptr" + j + ");");
                                this.out.println("    }");
                            }
                        } else if (Buffer.class.isAssignableFrom(callbackParameterTypes[j])) {
                            if (adapterInfo == null) {
                                this.out.println("    jlong size" + j + " = ptr" + j + " != NULL ? 1 : 0;");
                            }
                            this.out.println("    if (ptr" + j + " != NULL) {");
                            this.out.println("        jlong sizeptr = size" + j + " * sizeof(ptr" + j + "[0]);");
                            this.out.println("        obj" + j + " = env->NewDirectByteBuffer((void*)ptr" + j + ", sizeptr < INT_MAX ? sizeptr : INT_MAX);");
                            this.out.println("    }");
                        } else {
                            this.logger.warn("Callback \"" + callbackMethod + "\" has unsupported parameter type \"" + callbackParameterTypes[j].getCanonicalName() + "\". Compilation will most likely fail.");
                        }
                    }
                    j++;
                }
            }
            if (methodInfo != null) {
                this.out.println("    if (" + fieldName + " == NULL) {");
                this.out.println("        " + fieldName + " = JavaCPP_getMethodID(env, " + this.jclasses.index(cls) + ", \"" + methodInfo.method.getName() + "\", \"(" + signature(methodInfo.method.getParameterTypes()) + ")" + signature(methodInfo.method.getReturnType()) + "\");");
                this.out.println("    }");
                this.out.println("    jmethodID mid = " + fieldName + ";");
            } else if (callbackName != null) {
                this.out.println("    if (obj == NULL) {");
                this.out.println("        obj = JavaCPP_createPointer(env, " + this.jclasses.index(cls) + ");");
                this.out.println("        obj = obj == NULL ? NULL : env->NewGlobalRef(obj);");
                this.out.println("        if (obj == NULL) {");
                this.out.println("            JavaCPP_log(\"Error creating global reference of " + cls.getCanonicalName() + " instance for callback.\");");
                this.out.println("        } else {");
                this.out.println("            env->SetLongField(obj, JavaCPP_addressFID, ptr_to_jlong(this));");
                this.out.println("        }");
                this.out.println("        ptr = &" + callbackName + ";");
                this.out.println("    }");
                this.out.println("    if (mid == NULL) {");
                this.out.println("        mid = JavaCPP_getMethodID(env, " + this.jclasses.index(cls) + ", \"" + callbackMethod.getName() + "\", \"(" + signature(callbackMethod.getParameterTypes()) + ")" + signature(callbackMethod.getReturnType()) + "\");");
                this.out.println("    }");
            }
            this.out.println("    if (env->IsSameObject(obj, NULL)) {");
            this.out.println("        JavaCPP_log(\"Function pointer object is NULL in callback for " + cls.getCanonicalName() + ".\");");
            this.out.println("    } else if (mid == NULL) {");
            this.out.println("        JavaCPP_log(\"Error getting method ID of function caller \\\"" + callbackMethod + "\\\" for callback.\");");
            this.out.println("    } else {");
            s = "Object";
            if (callbackReturnType.isPrimitive()) {
                s = callbackReturnType.getName();
                s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            }
            this.out.println("        " + returnPrefix + "env->Call" + s + "MethodA(obj, mid, " + (callbackParameterTypes.length == 0 ? "NULL);" : "args);"));
            this.out.println("        if ((exc = env->ExceptionOccurred()) != NULL) {");
            this.out.println("            env->ExceptionClear();");
            this.out.println("        }");
            this.out.println("    }");
            j = 0;
            while (j < callbackParameterTypes.length) {
                if (Pointer.class.isAssignableFrom(callbackParameterTypes[j])) {
                    typeName = cppTypeName(callbackParameterTypes[j]);
                    passBy = by(callbackParameterAnnotations[j]);
                    cast = cast((Class) callbackParameterTypes[j], callbackParameterAnnotations[j]);
                    adapterInfo = adapterInformation(true, valueTypeName(typeName), callbackParameterAnnotations[j]);
                    if ("void*".equals(typeName[0]) && !callbackParameterTypes[j].isAnnotationPresent(Opaque.class)) {
                        typeName[0] = "char*";
                    }
                    if (adapterInfo != null || (passBy instanceof ByPtrPtr) || (passBy instanceof ByPtrRef)) {
                        this.out.println("    " + typeName[0] + " rptr" + j + typeName[1] + " = (" + typeName[0] + typeName[1] + ")jlong_to_ptr(env->GetLongField(obj" + j + ", JavaCPP_addressFID));");
                        if (adapterInfo != null) {
                            this.out.println("    jlong rsize" + j + " = env->GetLongField(obj" + j + ", JavaCPP_limitFID);");
                            this.out.println("    void* rowner" + j + " = JavaCPP_getPointerOwner(env, obj" + j + ");");
                        }
                        if (!callbackParameterTypes[j].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    jlong rposition" + j + " = env->GetLongField(obj" + j + ", JavaCPP_positionFID);");
                            this.out.println("    rptr" + j + " += rposition" + j + ";");
                            if (adapterInfo != null) {
                                this.out.println("    rsize" + j + " -= rposition" + j + ";");
                            }
                        }
                        if (adapterInfo != null) {
                            this.out.println("    adapter" + j + ".assign(rptr" + j + ", rsize" + j + ", rowner" + j + ");");
                        } else if (passBy instanceof ByPtrPtr) {
                            this.out.println("    if (arg" + j + " != NULL) {");
                            this.out.println("        *arg" + j + " = *" + cast + "&rptr" + j + ";");
                            this.out.println("    }");
                        } else if (passBy instanceof ByPtrRef) {
                            this.out.println("    arg" + j + " = " + cast + "rptr" + j + ";");
                        }
                    }
                }
                if (!callbackParameterTypes[j].isPrimitive()) {
                    this.out.println("    env->DeleteLocalRef(obj" + j + ");");
                }
                j++;
            }
            this.out.println("}");
            this.out.println("end:");
            if (callbackReturnType != Void.TYPE) {
                if ("void*".equals(returnTypeName[0]) && !callbackReturnType.isAnnotationPresent(Opaque.class)) {
                    returnTypeName[0] = "char*";
                }
                if (Pointer.class.isAssignableFrom(callbackReturnType)) {
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = rarg == NULL ? NULL : (" + returnTypeName[0] + returnTypeName[1] + ")jlong_to_ptr(env->GetLongField(rarg, JavaCPP_addressFID));");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jlong rsize = rarg == NULL ? 0 : env->GetLongField(rarg, JavaCPP_limitFID);");
                        this.out.println("    void* rowner = JavaCPP_getPointerOwner(env, rarg);");
                    }
                    if (!callbackReturnType.isAnnotationPresent(Opaque.class)) {
                        this.out.println("    jlong rposition = rarg == NULL ? 0 : env->GetLongField(rarg, JavaCPP_positionFID);");
                        this.out.println("    rptr += rposition;");
                        if (returnAdapterInfo != null) {
                            this.out.println("    rsize -= rposition;");
                        }
                    }
                } else if (callbackReturnType == String.class) {
                    this.passesStrings = true;
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = JavaCPP_getStringBytes(env, rarg);");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jlong rsize = 0;");
                        this.out.println("    void* rowner = (void*)rptr");
                    }
                } else if (Buffer.class.isAssignableFrom(callbackReturnType)) {
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = rarg == NULL ? NULL : env->GetDirectBufferAddress(rarg);");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jlong rsize = rarg == NULL ? 0 : env->GetDirectBufferCapacity(rarg);");
                        this.out.println("    void* rowner = (void*)rptr;");
                    }
                } else if (!callbackReturnType.isPrimitive()) {
                    this.logger.warn("Callback \"" + callbackMethod + "\" has unsupported return type \"" + callbackReturnType.getCanonicalName() + "\". Compilation will most likely fail.");
                }
            }
            this.passesStrings = true;
            this.out.println("    if (exc != NULL) {");
            this.out.println("        jstring str = (jstring)env->CallObjectMethod(exc, JavaCPP_toStringMID);");
            this.out.println("        env->DeleteLocalRef(exc);");
            this.out.println("        const char *msg = JavaCPP_getStringBytes(env, str);");
            this.out.println("        JavaCPP_exception e(msg);");
            this.out.println("        JavaCPP_releaseStringBytes(env, str, msg);");
            this.out.println("        env->DeleteLocalRef(str);");
            this.out.println("        JavaCPP_detach(attached);");
            this.out.println("        throw e;");
            this.out.println("    } else {");
            this.out.println("        JavaCPP_detach(attached);");
            this.out.println("    }");
            if (callbackReturnType != Void.TYPE) {
                if (callbackReturnType.isPrimitive()) {
                    this.out.println("    return " + callbackReturnCast + "rarg;");
                } else if (returnAdapterInfo != null) {
                    this.usesAdapters = true;
                    this.out.println("    return " + returnAdapterInfo.name + "(" + callbackReturnCast + "rptr, rsize, rowner);");
                } else if (FunctionPointer.class.isAssignableFrom(callbackReturnType)) {
                    this.functions.index(callbackReturnType);
                    this.out.println("    return " + callbackReturnCast + "(rptr == NULL ? NULL : rptr->ptr);");
                } else if ((returnBy instanceof ByVal) || (returnBy instanceof ByRef)) {
                    this.out.println("    if (rptr == NULL) {");
                    this.out.println("        JavaCPP_log(\"Return pointer address is NULL in callback for " + cls.getCanonicalName() + ".\");");
                    this.out.println("        static " + returnConvention[0] + " empty" + returnTypeName[1] + ";");
                    this.out.println("        return empty;");
                    this.out.println("    } else {");
                    this.out.println("        return *" + callbackReturnCast + "rptr;");
                    this.out.println("    }");
                } else if (returnBy instanceof ByPtrPtr) {
                    this.out.println("    return " + callbackReturnCast + "&rptr;");
                } else {
                    this.out.println("    return " + callbackReturnCast + "rptr;");
                }
            }
            this.out.println("}");
        }
    }

    void callbackAllocator(Class cls, String callbackName) {
        String[] typeName = cppTypeName(cls);
        String instanceTypeName = functionClassName(cls);
        this.out.println("    obj = env->NewWeakGlobalRef(obj);");
        this.out.println("    if (obj == NULL) {");
        this.out.println("        JavaCPP_log(\"Error creating global reference of " + cls.getCanonicalName() + " instance for callback.\");");
        this.out.println("        return;");
        this.out.println("    }");
        this.out.println("    " + instanceTypeName + "* rptr = new (std::nothrow) " + instanceTypeName + ";");
        this.out.println("    if (rptr != NULL) {");
        this.out.println("        rptr->ptr = " + (callbackName == null ? "(" + typeName[0] + typeName[1] + ")jlong_to_ptr(arg0)" : "&" + callbackName) + ";");
        this.out.println("        rptr->obj = obj;");
        this.out.println("        JavaCPP_initPointer(env, obj, rptr, 1, rptr, &JavaCPP_" + mangle(cls.getName()) + "_deallocate);");
        this.deallocators.index(cls);
        if (callbackName != null) {
            this.out.println("        " + callbackName + "_instance = *rptr;");
        }
        this.out.println("    }");
        this.out.println("}");
    }

    boolean checkPlatform(Class<?> cls) {
        Class<?> enclosingClass = Loader.getEnclosingClass(cls);
        while (!cls.isAnnotationPresent(Properties.class) && !cls.isAnnotationPresent(Platform.class) && cls.getSuperclass() != null) {
            if (enclosingClass == null || cls.getSuperclass() != Object.class) {
                cls = cls.getSuperclass();
            } else {
                cls = enclosingClass;
                enclosingClass = null;
            }
        }
        Properties classProperties = (Properties) cls.getAnnotation(Properties.class);
        if (classProperties != null) {
            Class[] classes = classProperties.inherit();
            String[] defaultNames = classProperties.names();
            Deque<Class> queue = new ArrayDeque(Arrays.asList(classes));
            while (queue.size() > 0 && (defaultNames == null || defaultNames.length == 0)) {
                Properties p = (Properties) ((Class) queue.removeFirst()).getAnnotation(Properties.class);
                if (p != null) {
                    defaultNames = p.names();
                    queue.addAll(Arrays.asList(p.inherit()));
                }
            }
            Platform[] platforms = classProperties.value();
            if (platforms != null) {
                for (Platform p2 : platforms) {
                    if (checkPlatform(p2, defaultNames)) {
                        return true;
                    }
                }
            } else if (classes != null) {
                for (Class c : classes) {
                    if (checkPlatform(c)) {
                        return true;
                    }
                }
            }
        } else if (checkPlatform((Platform) cls.getAnnotation(Platform.class), null)) {
            return true;
        }
        return false;
    }

    boolean checkPlatform(Platform platform, String[] defaultNames) {
        if (platform == null) {
            return true;
        }
        String[] value;
        if (defaultNames == null) {
            defaultNames = new String[0];
        }
        String platform2 = this.properties.getProperty("platform");
        String[][] names = new String[2][];
        if (platform.value().length > 0) {
            value = platform.value();
        } else {
            value = defaultNames;
        }
        names[0] = value;
        names[1] = platform.not();
        boolean[] matches = new boolean[]{false, false};
        for (int i = 0; i < names.length; i++) {
            for (String s : names[i]) {
                if (platform2.startsWith(s)) {
                    matches[i] = true;
                    break;
                }
            }
        }
        if ((names[0].length == 0 || matches[0]) && (names[1].length == 0 || !matches[1])) {
            return true;
        }
        return false;
    }

    static String functionClassName(Class<?> cls) {
        Name name = (Name) cls.getAnnotation(Name.class);
        return name != null ? name.value()[0] : "JavaCPP_" + mangle(cls.getName());
    }

    static Method[] functionMethods(Class<?> cls, boolean[] callbackAllocators) {
        if (!FunctionPointer.class.isAssignableFrom(cls)) {
            return null;
        }
        Method[] methods = cls.getDeclaredMethods();
        Method[] functionMethods = new Method[3];
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            int modifiers = methods[i].getModifiers();
            Class[] parameterTypes = methods[i].getParameterTypes();
            Class returnType = methods[i].getReturnType();
            if (!Modifier.isStatic(modifiers)) {
                if (callbackAllocators != null && methodName.startsWith("allocate") && Modifier.isNative(modifiers) && returnType == Void.TYPE && (parameterTypes.length == 0 || (parameterTypes.length == 1 && (parameterTypes[0] == Integer.TYPE || parameterTypes[0] == Long.TYPE)))) {
                    callbackAllocators[i] = true;
                } else if (methodName.startsWith("call") || methodName.startsWith("apply")) {
                    functionMethods[0] = methods[i];
                } else if (methodName.startsWith("get") && Modifier.isNative(modifiers)) {
                    functionMethods[1] = methods[i];
                } else if (methodName.startsWith("put") && Modifier.isNative(modifiers)) {
                    functionMethods[2] = methods[i];
                }
            }
        }
        if (functionMethods[0] == null && functionMethods[1] == null && functionMethods[2] == null) {
            functionMethods = null;
        }
        return functionMethods;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    org.bytedeco.javacpp.tools.MethodInformation methodInformation(java.lang.reflect.Method r37) {
        /*
        r36 = this;
        r14 = new org.bytedeco.javacpp.tools.MethodInformation;
        r14.<init>();
        r30 = r37.getDeclaringClass();
        r0 = r30;
        r14.cls = r0;
        r0 = r37;
        r14.method = r0;
        r30 = r37.getAnnotations();
        r0 = r30;
        r14.annotations = r0;
        r30 = r37.getModifiers();
        r0 = r30;
        r14.modifiers = r0;
        r30 = r37.getReturnType();
        r0 = r30;
        r14.returnType = r0;
        r30 = r37.getName();
        r0 = r30;
        r14.name = r0;
        r30 = org.bytedeco.javacpp.annotation.Name.class;
        r0 = r37;
        r1 = r30;
        r20 = r0.getAnnotation(r1);
        r20 = (org.bytedeco.javacpp.annotation.Name) r20;
        if (r20 == 0) goto L_0x00fc;
    L_0x003f:
        r30 = r20.value();
    L_0x0043:
        r0 = r30;
        r14.memberName = r0;
        r30 = org.bytedeco.javacpp.annotation.Index.class;
        r0 = r37;
        r1 = r30;
        r13 = r0.getAnnotation(r1);
        r13 = (org.bytedeco.javacpp.annotation.Index) r13;
        if (r13 == 0) goto L_0x010e;
    L_0x0055:
        r30 = r13.value();
    L_0x0059:
        r0 = r30;
        r14.dim = r0;
        r30 = r37.getParameterTypes();
        r0 = r30;
        r14.parameterTypes = r0;
        r30 = r37.getParameterAnnotations();
        r0 = r30;
        r14.parameterAnnotations = r0;
        r30 = org.bytedeco.javacpp.annotation.Raw.class;
        r0 = r37;
        r1 = r30;
        r30 = r0.isAnnotationPresent(r1);
        r0 = r30;
        r14.returnRaw = r0;
        r0 = r14.returnRaw;
        r30 = r0;
        if (r30 == 0) goto L_0x0112;
    L_0x0081:
        r30 = org.bytedeco.javacpp.annotation.Raw.class;
        r0 = r37;
        r1 = r30;
        r30 = r0.getAnnotation(r1);
        r30 = (org.bytedeco.javacpp.annotation.Raw) r30;
        r30 = r30.withEnv();
    L_0x0091:
        r0 = r30;
        r14.withEnv = r0;
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r0 = r30;
        r0 = new boolean[r0];
        r30 = r0;
        r0 = r30;
        r14.parameterRaw = r0;
        r12 = 0;
    L_0x00a9:
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r0 = r30;
        if (r12 >= r0) goto L_0x0119;
    L_0x00b6:
        r16 = 0;
    L_0x00b8:
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r30 = r30[r12];
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r0 = r16;
        r1 = r30;
        if (r0 >= r1) goto L_0x0116;
    L_0x00c9:
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r30 = r30[r12];
        r30 = r30[r16];
        r0 = r30;
        r0 = r0 instanceof org.bytedeco.javacpp.annotation.Raw;
        r30 = r0;
        if (r30 == 0) goto L_0x00f9;
    L_0x00d9:
        r0 = r14.parameterRaw;
        r30 = r0;
        r31 = 1;
        r30[r12] = r31;
        r0 = r14.withEnv;
        r31 = r0;
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r30 = r30[r12];
        r30 = r30[r16];
        r30 = (org.bytedeco.javacpp.annotation.Raw) r30;
        r30 = r30.withEnv();
        r30 = r30 | r31;
        r0 = r30;
        r14.withEnv = r0;
    L_0x00f9:
        r16 = r16 + 1;
        goto L_0x00b8;
    L_0x00fc:
        r30 = 1;
        r0 = r30;
        r0 = new java.lang.String[r0];
        r30 = r0;
        r31 = 0;
        r0 = r14.name;
        r32 = r0;
        r30[r31] = r32;
        goto L_0x0043;
    L_0x010e:
        r30 = 0;
        goto L_0x0059;
    L_0x0112:
        r30 = 0;
        goto L_0x0091;
    L_0x0116:
        r12 = r12 + 1;
        goto L_0x00a9;
    L_0x0119:
        r0 = r14.returnType;
        r30 = r0;
        r31 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0150;
    L_0x0125:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x028f;
    L_0x0130:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r30 = r30.isArray();
        if (r30 == 0) goto L_0x028f;
    L_0x013e:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r30 = r30.getComponentType();
        r30 = r30.isPrimitive();
        if (r30 == 0) goto L_0x028f;
    L_0x0150:
        r5 = 1;
    L_0x0151:
        r0 = r14.returnType;
        r30 = r0;
        r31 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 == r1) goto L_0x016b;
    L_0x015d:
        r0 = r14.returnType;
        r30 = r0;
        r0 = r14.cls;
        r31 = r0;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0292;
    L_0x016b:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x0292;
    L_0x0176:
        r8 = 1;
    L_0x0177:
        r0 = r14.modifiers;
        r30 = r0;
        r30 = java.lang.reflect.Modifier.isStatic(r30);
        if (r30 != 0) goto L_0x0295;
    L_0x0181:
        r0 = r14.returnType;
        r30 = r0;
        r31 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0295;
    L_0x018d:
        r3 = 1;
    L_0x018e:
        if (r3 == 0) goto L_0x0298;
    L_0x0190:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r31 = 1;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0298;
    L_0x01a1:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r31 = java.lang.Integer.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 == r1) goto L_0x01c1;
    L_0x01b1:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r31 = java.lang.Long.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0298;
    L_0x01c1:
        r4 = 1;
    L_0x01c2:
        r28 = 0;
        r29 = 0;
        r17 = 0;
        r18 = 0;
        r21 = 0;
        r22 = 0;
        r0 = r14.cls;
        r30 = r0;
        r32 = r30.getDeclaredMethods();
        r0 = r32;
        r0 = r0.length;
        r33 = r0;
        r30 = 0;
        r31 = r30;
    L_0x01df:
        r0 = r31;
        r1 = r33;
        if (r0 >= r1) goto L_0x0570;
    L_0x01e5:
        r19 = r32[r31];
        r0 = r36;
        r0 = r0.annotationCache;
        r30 = r0;
        r0 = r30;
        r1 = r19;
        r15 = r0.get(r1);
        r15 = (org.bytedeco.javacpp.tools.MethodInformation) r15;
        if (r15 != 0) goto L_0x023b;
    L_0x01f9:
        r0 = r36;
        r0 = r0.annotationCache;
        r30 = r0;
        r15 = new org.bytedeco.javacpp.tools.MethodInformation;
        r15.<init>();
        r0 = r30;
        r1 = r19;
        r0.put(r1, r15);
        r30 = r19.getModifiers();
        r0 = r30;
        r15.modifiers = r0;
        r30 = r19.getReturnType();
        r0 = r30;
        r15.returnType = r0;
        r30 = r19.getName();
        r0 = r30;
        r15.name = r0;
        r30 = r19.getParameterTypes();
        r0 = r30;
        r15.parameterTypes = r0;
        r30 = r19.getAnnotations();
        r0 = r30;
        r15.annotations = r0;
        r30 = r19.getParameterAnnotations();
        r0 = r30;
        r15.parameterAnnotations = r0;
    L_0x023b:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x029b;
    L_0x0246:
        r0 = r14.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r34 = java.lang.Class.class;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x029b;
    L_0x0256:
        r26 = 1;
    L_0x0258:
        r0 = r15.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x029e;
    L_0x0263:
        r0 = r15.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r34 = java.lang.Class.class;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x029e;
    L_0x0273:
        r27 = 1;
    L_0x0275:
        r0 = r37;
        r1 = r19;
        r30 = r0.equals(r1);
        if (r30 != 0) goto L_0x0289;
    L_0x027f:
        r0 = r15.modifiers;
        r30 = r0;
        r30 = java.lang.reflect.Modifier.isNative(r30);
        if (r30 != 0) goto L_0x02a1;
    L_0x0289:
        r30 = r31 + 1;
        r31 = r30;
        goto L_0x01df;
    L_0x028f:
        r5 = 0;
        goto L_0x0151;
    L_0x0292:
        r8 = 0;
        goto L_0x0177;
    L_0x0295:
        r3 = 0;
        goto L_0x018e;
    L_0x0298:
        r4 = 0;
        goto L_0x01c2;
    L_0x029b:
        r26 = 0;
        goto L_0x0258;
    L_0x029e:
        r27 = 0;
        goto L_0x0275;
    L_0x02a1:
        r9 = 0;
        r10 = 0;
        r6 = 0;
        r7 = 0;
        if (r5 == 0) goto L_0x0309;
    L_0x02a7:
        r30 = "get";
        r0 = r14.name;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = r0.equals(r1);
        if (r30 == 0) goto L_0x0309;
    L_0x02b7:
        r30 = "put";
        r0 = r15.name;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = r0.equals(r1);
        if (r30 == 0) goto L_0x0309;
    L_0x02c7:
        r9 = 1;
    L_0x02c8:
        r25 = 1;
        r16 = 0;
    L_0x02cc:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r30 = r30 - r26;
        r0 = r16;
        r1 = r30;
        if (r0 >= r1) goto L_0x0348;
    L_0x02dd:
        r0 = r15.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r30 = r30 - r27;
        r0 = r16;
        r1 = r30;
        if (r0 >= r1) goto L_0x0348;
    L_0x02ee:
        r0 = r14.parameterTypes;
        r30 = r0;
        r34 = r16 + r26;
        r30 = r30[r34];
        r0 = r15.parameterTypes;
        r34 = r0;
        r35 = r16 + r27;
        r34 = r34[r35];
        r0 = r30;
        r1 = r34;
        if (r0 == r1) goto L_0x0306;
    L_0x0304:
        r25 = 0;
    L_0x0306:
        r16 = r16 + 1;
        goto L_0x02cc;
    L_0x0309:
        if (r8 == 0) goto L_0x032d;
    L_0x030b:
        r30 = "put";
        r0 = r14.name;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = r0.equals(r1);
        if (r30 == 0) goto L_0x032d;
    L_0x031b:
        r30 = "get";
        r0 = r15.name;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = r0.equals(r1);
        if (r30 == 0) goto L_0x032d;
    L_0x032b:
        r10 = 1;
        goto L_0x02c8;
    L_0x032d:
        r0 = r15.name;
        r30 = r0;
        r0 = r14.name;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = r0.equals(r1);
        if (r30 == 0) goto L_0x0289;
    L_0x033f:
        r30 = 1;
        r0 = r30;
        r14.overloaded = r0;
        r6 = r5;
        r7 = r8;
        goto L_0x02c8;
    L_0x0348:
        if (r25 == 0) goto L_0x0289;
    L_0x034a:
        if (r9 == 0) goto L_0x04b0;
    L_0x034c:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x04b0;
    L_0x0357:
        r0 = r14.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r30 = r30.isArray();
        if (r30 == 0) goto L_0x04b0;
    L_0x0365:
        r0 = r14.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r30 = r30.getComponentType();
        r30 = r30.isPrimitive();
        if (r30 == 0) goto L_0x04b0;
    L_0x0377:
        r23 = 1;
    L_0x0379:
        if (r10 == 0) goto L_0x04b4;
    L_0x037b:
        r0 = r15.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x04b4;
    L_0x0386:
        r0 = r15.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r30 = r30.isArray();
        if (r30 == 0) goto L_0x04b4;
    L_0x0394:
        r0 = r15.parameterTypes;
        r30 = r0;
        r34 = 0;
        r30 = r30[r34];
        r30 = r30.getComponentType();
        r30 = r30.isPrimitive();
        if (r30 == 0) goto L_0x04b4;
    L_0x03a6:
        r24 = 1;
    L_0x03a8:
        if (r5 == 0) goto L_0x04c2;
    L_0x03aa:
        r0 = r15.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r34 = r0;
        if (r23 == 0) goto L_0x04b8;
    L_0x03b5:
        r30 = 0;
    L_0x03b7:
        r30 = r34 - r30;
        r0 = r14.parameterTypes;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 - r26;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x04c2;
    L_0x03ca:
        if (r23 == 0) goto L_0x04bc;
    L_0x03cc:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r14.parameterTypes;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
    L_0x03dd:
        r0 = r15.parameterTypes;
        r34 = r0;
        r0 = r15.parameterTypes;
        r35 = r0;
        r0 = r35;
        r0 = r0.length;
        r35 = r0;
        r35 = r35 + -1;
        r34 = r34[r35];
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x04c2;
    L_0x03f4:
        r0 = r15.returnType;
        r30 = r0;
        r34 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r34;
        if (r0 == r1) goto L_0x040e;
    L_0x0400:
        r0 = r15.returnType;
        r30 = r0;
        r0 = r14.cls;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x04c2;
    L_0x040e:
        r0 = r15.parameterAnnotations;
        r30 = r0;
        r0 = r15.parameterAnnotations;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 == 0) goto L_0x0445;
    L_0x0426:
        r0 = r15.parameterAnnotations;
        r30 = r0;
        r0 = r15.parameterAnnotations;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
        r0 = r14.annotations;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = java.util.Arrays.equals(r0, r1);
        if (r30 == 0) goto L_0x04c2;
    L_0x0445:
        r22 = r19;
        r28 = r9;
        r17 = r6;
        r21 = r23;
    L_0x044d:
        if (r17 != 0) goto L_0x0451;
    L_0x044f:
        if (r18 == 0) goto L_0x0289;
    L_0x0451:
        r16 = r26;
    L_0x0453:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r0 = r16;
        r1 = r30;
        if (r0 >= r1) goto L_0x0289;
    L_0x0462:
        r30 = org.bytedeco.javacpp.annotation.Index.class;
        r0 = r37;
        r1 = r30;
        r30 = r0.isAnnotationPresent(r1);
        if (r30 != 0) goto L_0x04ad;
    L_0x046e:
        if (r22 == 0) goto L_0x047c;
    L_0x0470:
        r30 = org.bytedeco.javacpp.annotation.Index.class;
        r0 = r22;
        r1 = r30;
        r30 = r0.isAnnotationPresent(r1);
        if (r30 != 0) goto L_0x04ad;
    L_0x047c:
        r0 = r14.parameterTypes;
        r30 = r0;
        r30 = r30[r16];
        r34 = java.lang.Integer.TYPE;
        r0 = r30;
        r1 = r34;
        if (r0 == r1) goto L_0x04ad;
    L_0x048a:
        r0 = r14.parameterTypes;
        r30 = r0;
        r30 = r30[r16];
        r34 = java.lang.Long.TYPE;
        r0 = r30;
        r1 = r34;
        if (r0 == r1) goto L_0x04ad;
    L_0x0498:
        r17 = 0;
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r30 = r30 + -1;
        r0 = r16;
        r1 = r30;
        if (r0 >= r1) goto L_0x04ad;
    L_0x04ab:
        r18 = 0;
    L_0x04ad:
        r16 = r16 + 1;
        goto L_0x0453;
    L_0x04b0:
        r23 = 0;
        goto L_0x0379;
    L_0x04b4:
        r24 = 0;
        goto L_0x03a8;
    L_0x04b8:
        r30 = 1;
        goto L_0x03b7;
    L_0x04bc:
        r0 = r14.returnType;
        r30 = r0;
        goto L_0x03dd;
    L_0x04c2:
        if (r8 == 0) goto L_0x044d;
    L_0x04c4:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r34 = r0;
        if (r24 == 0) goto L_0x0567;
    L_0x04cf:
        r30 = 0;
    L_0x04d1:
        r30 = r34 - r30;
        r0 = r15.parameterTypes;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 - r27;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x044d;
    L_0x04e4:
        if (r24 == 0) goto L_0x056b;
    L_0x04e6:
        r0 = r15.parameterTypes;
        r30 = r0;
        r0 = r15.parameterTypes;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
    L_0x04f7:
        r0 = r14.parameterTypes;
        r34 = r0;
        r0 = r14.parameterTypes;
        r35 = r0;
        r0 = r35;
        r0 = r0.length;
        r35 = r0;
        r35 = r35 + -1;
        r34 = r34[r35];
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x044d;
    L_0x050e:
        r0 = r14.returnType;
        r30 = r0;
        r34 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r34;
        if (r0 == r1) goto L_0x0528;
    L_0x051a:
        r0 = r14.returnType;
        r30 = r0;
        r0 = r14.cls;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        if (r0 != r1) goto L_0x044d;
    L_0x0528:
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r0 = r14.parameterAnnotations;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 == 0) goto L_0x055f;
    L_0x0540:
        r0 = r14.parameterAnnotations;
        r30 = r0;
        r0 = r14.parameterAnnotations;
        r34 = r0;
        r0 = r34;
        r0 = r0.length;
        r34 = r0;
        r34 = r34 + -1;
        r30 = r30[r34];
        r0 = r15.annotations;
        r34 = r0;
        r0 = r30;
        r1 = r34;
        r30 = java.util.Arrays.equals(r0, r1);
        if (r30 == 0) goto L_0x044d;
    L_0x055f:
        r22 = r19;
        r29 = r10;
        r18 = r7;
        goto L_0x044d;
    L_0x0567:
        r30 = 1;
        goto L_0x04d1;
    L_0x056b:
        r0 = r15.returnType;
        r30 = r0;
        goto L_0x04f7;
    L_0x0570:
        r0 = r14.annotations;
        r30 = r0;
        r0 = r36;
        r1 = r30;
        r2 = r0.behavior(r1);
        if (r5 == 0) goto L_0x06cc;
    L_0x057e:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.ValueGetter;
        r30 = r0;
        if (r30 == 0) goto L_0x06cc;
    L_0x0584:
        r30 = 1;
        r0 = r30;
        r14.valueGetter = r0;
        r0 = r21;
        r14.noReturnGetter = r0;
    L_0x058e:
        if (r20 != 0) goto L_0x05ac;
    L_0x0590:
        r0 = r14.pairedMethod;
        r30 = r0;
        if (r30 == 0) goto L_0x05ac;
    L_0x0596:
        r0 = r14.pairedMethod;
        r30 = r0;
        r31 = org.bytedeco.javacpp.annotation.Name.class;
        r20 = r30.getAnnotation(r31);
        r20 = (org.bytedeco.javacpp.annotation.Name) r20;
        if (r20 == 0) goto L_0x05ac;
    L_0x05a4:
        r30 = r20.value();
        r0 = r30;
        r14.memberName = r0;
    L_0x05ac:
        r0 = r14.cls;
        r30 = r0;
        r31 = org.bytedeco.javacpp.annotation.NoOffset.class;
        r30 = r30.isAnnotationPresent(r31);
        if (r30 != 0) goto L_0x05d0;
    L_0x05b8:
        r30 = org.bytedeco.javacpp.annotation.NoOffset.class;
        r0 = r37;
        r1 = r30;
        r30 = r0.isAnnotationPresent(r1);
        if (r30 != 0) goto L_0x05d0;
    L_0x05c4:
        r30 = org.bytedeco.javacpp.annotation.Index.class;
        r0 = r37;
        r1 = r30;
        r30 = r0.isAnnotationPresent(r1);
        if (r30 == 0) goto L_0x0867;
    L_0x05d0:
        r30 = 1;
    L_0x05d2:
        r0 = r30;
        r14.noOffset = r0;
        r0 = r14.noOffset;
        r30 = r0;
        if (r30 != 0) goto L_0x0600;
    L_0x05dc:
        r0 = r14.pairedMethod;
        r30 = r0;
        if (r30 == 0) goto L_0x0600;
    L_0x05e2:
        r0 = r14.pairedMethod;
        r30 = r0;
        r31 = org.bytedeco.javacpp.annotation.NoOffset.class;
        r30 = r30.isAnnotationPresent(r31);
        if (r30 != 0) goto L_0x05fa;
    L_0x05ee:
        r0 = r14.pairedMethod;
        r30 = r0;
        r31 = org.bytedeco.javacpp.annotation.Index.class;
        r30 = r30.isAnnotationPresent(r31);
        if (r30 == 0) goto L_0x086b;
    L_0x05fa:
        r30 = 1;
    L_0x05fc:
        r0 = r30;
        r14.noOffset = r0;
    L_0x0600:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 == 0) goto L_0x0619;
    L_0x060b:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r30 = r30.isArray();
        if (r30 != 0) goto L_0x0660;
    L_0x0619:
        r0 = r14.valueGetter;
        r30 = r0;
        if (r30 != 0) goto L_0x0625;
    L_0x061f:
        r0 = r14.memberGetter;
        r30 = r0;
        if (r30 == 0) goto L_0x086f;
    L_0x0625:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r0 = r30;
        r14.dim = r0;
    L_0x0632:
        r0 = r14.valueGetter;
        r30 = r0;
        if (r30 != 0) goto L_0x063e;
    L_0x0638:
        r0 = r14.valueSetter;
        r30 = r0;
        if (r30 == 0) goto L_0x0660;
    L_0x063e:
        r30 = org.bytedeco.javacpp.FunctionPointer.class;
        r0 = r14.cls;
        r31 = r0;
        r30 = r30.isAssignableFrom(r31);
        if (r30 == 0) goto L_0x0660;
    L_0x064a:
        r0 = r14.cls;
        r30 = r0;
        r31 = org.bytedeco.javacpp.annotation.Namespace.class;
        r30 = r30.isAnnotationPresent(r31);
        if (r30 == 0) goto L_0x0660;
    L_0x0656:
        r0 = r14.dim;
        r30 = r0;
        r30 = r30 + -1;
        r0 = r30;
        r14.dim = r0;
    L_0x0660:
        r30 = 0;
        r0 = r30;
        r14.throwsException = r0;
        r0 = r14.cls;
        r30 = r0;
        r0 = r30;
        r1 = r37;
        r30 = noException(r0, r1);
        if (r30 != 0) goto L_0x06cb;
    L_0x0674:
        r0 = r14.annotations;
        r30 = r0;
        r0 = r36;
        r1 = r30;
        r30 = r0.by(r1);
        r0 = r30;
        r0 = r0 instanceof org.bytedeco.javacpp.annotation.ByVal;
        r30 = r0;
        if (r30 == 0) goto L_0x0696;
    L_0x0688:
        r0 = r14.returnType;
        r30 = r0;
        r0 = r30;
        r1 = r37;
        r30 = noException(r0, r1);
        if (r30 == 0) goto L_0x06ba;
    L_0x0696:
        r0 = r14.deallocator;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x069c:
        r0 = r14.valueGetter;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x06a2:
        r0 = r14.valueSetter;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x06a8:
        r0 = r14.memberGetter;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x06ae:
        r0 = r14.memberSetter;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x06b4:
        r0 = r14.bufferGetter;
        r30 = r0;
        if (r30 != 0) goto L_0x06cb;
    L_0x06ba:
        r11 = r37.getExceptionTypes();
        r0 = r11.length;
        r30 = r0;
        if (r30 <= 0) goto L_0x088c;
    L_0x06c3:
        r30 = 0;
        r30 = r11[r30];
    L_0x06c7:
        r0 = r30;
        r14.throwsException = r0;
    L_0x06cb:
        return r14;
    L_0x06cc:
        if (r8 == 0) goto L_0x06dc;
    L_0x06ce:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.ValueSetter;
        r30 = r0;
        if (r30 == 0) goto L_0x06dc;
    L_0x06d4:
        r30 = 1;
        r0 = r30;
        r14.valueSetter = r0;
        goto L_0x058e;
    L_0x06dc:
        if (r5 == 0) goto L_0x06f0;
    L_0x06de:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.MemberGetter;
        r30 = r0;
        if (r30 == 0) goto L_0x06f0;
    L_0x06e4:
        r30 = 1;
        r0 = r30;
        r14.memberGetter = r0;
        r0 = r21;
        r14.noReturnGetter = r0;
        goto L_0x058e;
    L_0x06f0:
        if (r8 == 0) goto L_0x0700;
    L_0x06f2:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.MemberSetter;
        r30 = r0;
        if (r30 == 0) goto L_0x0700;
    L_0x06f8:
        r30 = 1;
        r0 = r30;
        r14.memberSetter = r0;
        goto L_0x058e;
    L_0x0700:
        if (r3 == 0) goto L_0x0710;
    L_0x0702:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.Allocator;
        r30 = r0;
        if (r30 == 0) goto L_0x0710;
    L_0x0708:
        r30 = 1;
        r0 = r30;
        r14.allocator = r0;
        goto L_0x058e;
    L_0x0710:
        if (r4 == 0) goto L_0x0724;
    L_0x0712:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.ArrayAllocator;
        r30 = r0;
        if (r30 == 0) goto L_0x0724;
    L_0x0718:
        r30 = 1;
        r0 = r30;
        r14.arrayAllocator = r0;
        r0 = r30;
        r14.allocator = r0;
        goto L_0x058e;
    L_0x0724:
        if (r2 != 0) goto L_0x0826;
    L_0x0726:
        r0 = r14.returnType;
        r30 = r0;
        r31 = java.lang.Void.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0781;
    L_0x0732:
        r30 = "deallocate";
        r0 = r14.name;
        r31 = r0;
        r30 = r30.equals(r31);
        if (r30 == 0) goto L_0x0781;
    L_0x073e:
        r0 = r14.modifiers;
        r30 = r0;
        r30 = java.lang.reflect.Modifier.isStatic(r30);
        if (r30 != 0) goto L_0x0781;
    L_0x0748:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r31 = 2;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0781;
    L_0x0759:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 0;
        r30 = r30[r31];
        r31 = java.lang.Long.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0781;
    L_0x0769:
        r0 = r14.parameterTypes;
        r30 = r0;
        r31 = 1;
        r30 = r30[r31];
        r31 = java.lang.Long.TYPE;
        r0 = r30;
        r1 = r31;
        if (r0 != r1) goto L_0x0781;
    L_0x0779:
        r30 = 1;
        r0 = r30;
        r14.deallocator = r0;
        goto L_0x058e;
    L_0x0781:
        if (r3 == 0) goto L_0x0797;
    L_0x0783:
        r30 = "allocate";
        r0 = r14.name;
        r31 = r0;
        r30 = r30.equals(r31);
        if (r30 == 0) goto L_0x0797;
    L_0x078f:
        r30 = 1;
        r0 = r30;
        r14.allocator = r0;
        goto L_0x058e;
    L_0x0797:
        if (r4 == 0) goto L_0x07b1;
    L_0x0799:
        r30 = "allocateArray";
        r0 = r14.name;
        r31 = r0;
        r30 = r30.equals(r31);
        if (r30 == 0) goto L_0x07b1;
    L_0x07a5:
        r30 = 1;
        r0 = r30;
        r14.arrayAllocator = r0;
        r0 = r30;
        r14.allocator = r0;
        goto L_0x058e;
    L_0x07b1:
        r0 = r14.returnType;
        r30 = r0;
        r31 = java.nio.ByteBuffer.class;
        r30 = r30.isAssignableFrom(r31);
        if (r30 == 0) goto L_0x07e6;
    L_0x07bd:
        r30 = "asDirectBuffer";
        r0 = r14.name;
        r31 = r0;
        r30 = r30.equals(r31);
        if (r30 == 0) goto L_0x07e6;
    L_0x07c9:
        r0 = r14.modifiers;
        r30 = r0;
        r30 = java.lang.reflect.Modifier.isStatic(r30);
        if (r30 != 0) goto L_0x07e6;
    L_0x07d3:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        if (r30 != 0) goto L_0x07e6;
    L_0x07de:
        r30 = 1;
        r0 = r30;
        r14.bufferGetter = r0;
        goto L_0x058e;
    L_0x07e6:
        if (r28 == 0) goto L_0x07f8;
    L_0x07e8:
        r30 = 1;
        r0 = r30;
        r14.valueGetter = r0;
        r0 = r21;
        r14.noReturnGetter = r0;
        r0 = r22;
        r14.pairedMethod = r0;
        goto L_0x058e;
    L_0x07f8:
        if (r29 == 0) goto L_0x0806;
    L_0x07fa:
        r30 = 1;
        r0 = r30;
        r14.valueSetter = r0;
        r0 = r22;
        r14.pairedMethod = r0;
        goto L_0x058e;
    L_0x0806:
        if (r17 == 0) goto L_0x0818;
    L_0x0808:
        r30 = 1;
        r0 = r30;
        r14.memberGetter = r0;
        r0 = r21;
        r14.noReturnGetter = r0;
        r0 = r22;
        r14.pairedMethod = r0;
        goto L_0x058e;
    L_0x0818:
        if (r18 == 0) goto L_0x058e;
    L_0x081a:
        r30 = 1;
        r0 = r30;
        r14.memberSetter = r0;
        r0 = r22;
        r14.pairedMethod = r0;
        goto L_0x058e;
    L_0x0826:
        r0 = r2 instanceof org.bytedeco.javacpp.annotation.Function;
        r30 = r0;
        if (r30 != 0) goto L_0x058e;
    L_0x082c:
        r0 = r36;
        r0 = r0.logger;
        r30 = r0;
        r31 = new java.lang.StringBuilder;
        r31.<init>();
        r32 = "Method \"";
        r31 = r31.append(r32);
        r0 = r31;
        r1 = r37;
        r31 = r0.append(r1);
        r32 = "\" cannot behave like a \"";
        r31 = r31.append(r32);
        r32 = r2.annotationType();
        r32 = r32.getSimpleName();
        r31 = r31.append(r32);
        r32 = "\". No code will be generated.";
        r31 = r31.append(r32);
        r31 = r31.toString();
        r30.warn(r31);
        r14 = 0;
        goto L_0x06cb;
    L_0x0867:
        r30 = 0;
        goto L_0x05d2;
    L_0x086b:
        r30 = 0;
        goto L_0x05fc;
    L_0x086f:
        r0 = r14.memberSetter;
        r30 = r0;
        if (r30 != 0) goto L_0x087b;
    L_0x0875:
        r0 = r14.valueSetter;
        r30 = r0;
        if (r30 == 0) goto L_0x0632;
    L_0x087b:
        r0 = r14.parameterTypes;
        r30 = r0;
        r0 = r30;
        r0 = r0.length;
        r30 = r0;
        r30 = r30 + -1;
        r0 = r30;
        r14.dim = r0;
        goto L_0x0632;
    L_0x088c:
        r30 = java.lang.RuntimeException.class;
        goto L_0x06c7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Generator.methodInformation(java.lang.reflect.Method):org.bytedeco.javacpp.tools.MethodInformation");
    }

    static boolean noException(Class<?> cls, Method method) {
        boolean noException = baseClasses.contains(cls) || method.isAnnotationPresent(NoException.class);
        while (!noException && cls != null) {
            noException = cls.isAnnotationPresent(NoException.class);
            if (noException) {
                break;
            }
            cls = cls.getDeclaringClass();
        }
        return noException;
    }

    AdapterInformation adapterInformation(boolean out, MethodInformation methodInfo, int j) {
        if (out && (methodInfo.parameterTypes[j] == String.class || methodInfo.valueSetter || methodInfo.memberSetter)) {
            return null;
        }
        String typeName = cast(methodInfo, j);
        if (typeName != null && typeName.startsWith("(") && typeName.endsWith(")")) {
            typeName = typeName.substring(1, typeName.length() - 1);
        }
        if (typeName == null || typeName.length() == 0) {
            typeName = cppCastTypeName(methodInfo.parameterTypes[j], methodInfo.parameterAnnotations[j])[0];
        }
        String valueTypeName = valueTypeName(typeName);
        AdapterInformation adapter = adapterInformation(out, valueTypeName, methodInfo.parameterAnnotations[j]);
        if (adapter != null || methodInfo.pairedMethod == null || j != methodInfo.parameterTypes.length - 1) {
            return adapter;
        }
        if (methodInfo.valueSetter || methodInfo.memberSetter) {
            return adapterInformation(out, valueTypeName, methodInfo.pairedMethod.getAnnotations());
        }
        return adapter;
    }

    AdapterInformation adapterInformation(boolean out, String valueTypeName, Annotation... annotations) {
        AdapterInformation adapterInfo = null;
        boolean constant = false;
        String cast = "";
        String cast2 = "";
        for (Annotation a : annotations) {
            if (a instanceof Adapter) {
                Annotation adapter = (Adapter) a;
            } else {
                Object adapter2 = (Adapter) a.annotationType().getAnnotation(Adapter.class);
            }
            Cast c;
            if (adapter != null) {
                adapterInfo = new AdapterInformation();
                adapterInfo.name = adapter.value();
                adapterInfo.argc = adapter.argc();
                if (a != adapter) {
                    try {
                        Class cls = a.annotationType();
                        if (cls.isAnnotationPresent(Const.class)) {
                            constant = true;
                        }
                        try {
                            String value = cls.getDeclaredMethod("value", new Class[0]).invoke(a, new Object[0]).toString();
                            if (value != null && value.length() > 0) {
                                valueTypeName = value;
                            }
                        } catch (NoSuchMethodException e) {
                            valueTypeName = null;
                        }
                        c = (Cast) cls.getAnnotation(Cast.class);
                        if (c != null && cast.length() == 0) {
                            cast = c.value()[0];
                            if (valueTypeName != null) {
                                cast = cast + "< " + valueTypeName + " >";
                            }
                            if (c.value().length > 1) {
                                cast = cast + c.value()[1];
                            }
                            if (c.value().length > 2) {
                                cast2 = c.value()[2];
                            }
                        }
                    } catch (Exception ex) {
                        this.logger.warn("Could not invoke the value() method on annotation \"" + a + "\": " + ex);
                    }
                    if (valueTypeName != null && valueTypeName.length() > 0) {
                        adapterInfo.name += "< " + valueTypeName + " >";
                    }
                }
            } else if (a instanceof Const) {
                constant = true;
            } else if (a instanceof Cast) {
                c = (Cast) a;
                if (c.value().length > 1) {
                    cast = c.value()[1];
                }
                if (c.value().length > 2) {
                    cast2 = c.value()[2];
                }
            }
        }
        if (adapterInfo != null) {
            adapterInfo.cast = cast;
            adapterInfo.cast2 = cast2;
            adapterInfo.constant = constant;
        }
        return (out && constant) ? null : adapterInfo;
    }

    String cast(MethodInformation methodInfo, int j) {
        String cast = cast(methodInfo.parameterTypes[j], methodInfo.parameterAnnotations[j]);
        if ((cast != null && cast.length() != 0) || j != methodInfo.parameterTypes.length - 1) {
            return cast;
        }
        if ((methodInfo.valueSetter || methodInfo.memberSetter) && methodInfo.pairedMethod != null) {
            return cast(methodInfo.pairedMethod.getReturnType(), methodInfo.pairedMethod.getAnnotations());
        }
        return cast;
    }

    String cast(Class<?> type, Annotation... annotations) {
        String[] typeName = null;
        for (Annotation a : annotations) {
            if (((a instanceof Cast) && ((Cast) a).value()[0].length() > 0) || (a instanceof Const)) {
                typeName = cppCastTypeName(type, annotations);
                break;
            }
        }
        if (typeName == null || typeName.length <= 0) {
            return "";
        }
        return "(" + typeName[0] + typeName[1] + ")";
    }

    Annotation by(MethodInformation methodInfo, int j) {
        Annotation passBy = by(methodInfo.parameterAnnotations[j]);
        if (passBy != null || methodInfo.pairedMethod == null) {
            return passBy;
        }
        if (methodInfo.valueSetter || methodInfo.memberSetter) {
            return by(methodInfo.pairedMethod.getAnnotations());
        }
        return passBy;
    }

    Annotation by(Annotation... annotations) {
        Annotation byAnnotation = null;
        for (Annotation a : annotations) {
            if ((a instanceof ByPtr) || (a instanceof ByPtrPtr) || (a instanceof ByPtrRef) || (a instanceof ByRef) || (a instanceof ByVal)) {
                if (byAnnotation != null) {
                    this.logger.warn("\"By\" annotation \"" + byAnnotation + "\" already found. Ignoring superfluous annotation \"" + a + "\".");
                } else {
                    byAnnotation = a;
                }
            }
        }
        return byAnnotation;
    }

    Annotation behavior(Annotation... annotations) {
        Annotation behaviorAnnotation = null;
        for (Annotation a : annotations) {
            if ((a instanceof Function) || (a instanceof Allocator) || (a instanceof ArrayAllocator) || (a instanceof ValueSetter) || (a instanceof ValueGetter) || (a instanceof MemberGetter) || (a instanceof MemberSetter)) {
                if (behaviorAnnotation != null) {
                    this.logger.warn("Behavior annotation \"" + behaviorAnnotation + "\" already found. Ignoring superfluous annotation \"" + a + "\".");
                } else {
                    behaviorAnnotation = a;
                }
            }
        }
        return behaviorAnnotation;
    }

    static String constValueTypeName(String... typeName) {
        String type = typeName[0];
        if (type.endsWith("*") || type.endsWith("&")) {
            return type.substring(0, type.length() - 1);
        }
        return type;
    }

    static String valueTypeName(String... typeName) {
        String type = typeName[0];
        if (type.startsWith("const ")) {
            return type.substring(6, type.length() - 1);
        }
        if (type.endsWith("*") || type.endsWith("&")) {
            return type.substring(0, type.length() - 1);
        }
        return type;
    }

    String[] cppAnnotationTypeName(Class<?> type, Annotation... annotations) {
        String[] typeName = cppCastTypeName(type, annotations);
        String prefix = typeName[0];
        String suffix = typeName[1];
        boolean casted = false;
        for (Annotation a : annotations) {
            if (((a instanceof Cast) && ((Cast) a).value()[0].length() > 0) || (a instanceof Const)) {
                casted = true;
                break;
            }
        }
        Annotation by = by(annotations);
        if (by instanceof ByVal) {
            prefix = constValueTypeName(typeName);
        } else if (by instanceof ByRef) {
            prefix = constValueTypeName(typeName) + "&";
        } else if ((by instanceof ByPtrPtr) && !casted) {
            prefix = prefix + "*";
        } else if (by instanceof ByPtrRef) {
            prefix = prefix + "&";
        }
        typeName[0] = prefix;
        typeName[1] = suffix;
        return typeName;
    }

    String[] cppCastTypeName(Class<?> type, Annotation... annotations) {
        String[] typeName = null;
        boolean warning = false;
        boolean adapter = false;
        for (Annotation a : annotations) {
            if (a instanceof Cast) {
                warning = typeName != null;
                String prefix = ((Cast) a).value()[0];
                String suffix = "";
                int parenthesis = prefix.indexOf(41);
                if (parenthesis > 0) {
                    suffix = prefix.substring(parenthesis).trim();
                    prefix = prefix.substring(0, parenthesis).trim();
                }
                typeName = prefix.length() > 0 ? new String[]{prefix, suffix} : null;
            } else if (a instanceof Const) {
                warning = typeName != null;
                if (!warning) {
                    typeName = cppTypeName(type);
                    boolean[] b = ((Const) a).value();
                    if (b.length > 1 && b[1]) {
                        typeName[0] = valueTypeName(typeName) + " const *";
                    }
                    if (b.length > 0 && b[0]) {
                        typeName[0] = "const " + typeName[0];
                    }
                    Annotation by = by(annotations);
                    if (by instanceof ByPtrPtr) {
                        typeName[0] = typeName[0] + "*";
                    } else if (by instanceof ByPtrRef) {
                        typeName[0] = typeName[0] + "&";
                    }
                }
            } else if ((a instanceof Adapter) || a.annotationType().isAnnotationPresent(Adapter.class)) {
                adapter = true;
            }
        }
        if (warning && !adapter) {
            this.logger.warn("Without \"Adapter\", \"Cast\" and \"Const\" annotations are mutually exclusive.");
        }
        if (typeName == null) {
            return cppTypeName(type);
        }
        return typeName;
    }

    String[] cppTypeName(Class<?> type) {
        String prefix = "";
        String suffix = "";
        if (type == Buffer.class || type == Pointer.class) {
            prefix = "void*";
        } else if (type == byte[].class || type == ByteBuffer.class || type == BytePointer.class) {
            prefix = "signed char*";
        } else if (type == short[].class || type == ShortBuffer.class || type == ShortPointer.class) {
            prefix = "short*";
        } else if (type == int[].class || type == IntBuffer.class || type == IntPointer.class) {
            prefix = "int*";
        } else if (type == long[].class || type == LongBuffer.class || type == LongPointer.class) {
            prefix = "jlong*";
        } else if (type == float[].class || type == FloatBuffer.class || type == FloatPointer.class) {
            prefix = "float*";
        } else if (type == double[].class || type == DoubleBuffer.class || type == DoublePointer.class) {
            prefix = "double*";
        } else if (type == char[].class || type == CharBuffer.class || type == CharPointer.class) {
            prefix = "unsigned short*";
        } else if (type == boolean[].class) {
            prefix = "unsigned char*";
        } else if (type == PointerPointer.class) {
            prefix = "void**";
        } else if (type == String.class) {
            prefix = "const char*";
        } else if (type == Byte.TYPE) {
            prefix = "signed char";
        } else if (type == Long.TYPE) {
            prefix = "jlong";
        } else if (type == Character.TYPE) {
            prefix = "unsigned short";
        } else if (type == Boolean.TYPE) {
            prefix = "unsigned char";
        } else if (type.isPrimitive()) {
            prefix = type.getName();
        } else if (FunctionPointer.class.isAssignableFrom(type)) {
            String[] prefixSuffix = cppFunctionTypeName(functionMethods(type, null));
            if (prefixSuffix != null) {
                return prefixSuffix;
            }
        } else {
            String scopedType = cppScopeName((Class) type);
            if (scopedType.length() > 0) {
                prefix = scopedType + "*";
            } else {
                this.logger.warn("The class " + type.getCanonicalName() + " does not map to any C++ type. Compilation will most likely fail.");
            }
        }
        return new String[]{prefix, suffix};
    }

    String[] cppFunctionTypeName(Method... functionMethods) {
        Method functionMethod = null;
        if (functionMethods != null) {
            for (Method m : functionMethods) {
                if (m != null) {
                    functionMethod = m;
                    break;
                }
            }
        }
        if (functionMethod == null) {
            return null;
        }
        String prefix;
        Class<?> type = functionMethod.getDeclaringClass();
        Convention convention = (Convention) type.getAnnotation(Convention.class);
        String callingConvention = convention == null ? "" : convention.value() + " ";
        Namespace namespace = FunctionPointer.class.isAssignableFrom(type) ? (Namespace) type.getAnnotation(Namespace.class) : null;
        if (namespace != null && namespace.value().length() == 0) {
            namespace = null;
        }
        String spaceName = namespace == null ? "" : namespace.value();
        if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
            spaceName = spaceName + "::";
        }
        Class returnType = functionMethod.getReturnType();
        Class[] parameterTypes = functionMethod.getParameterTypes();
        Annotation[] annotations = functionMethod.getAnnotations();
        Annotation[][] parameterAnnotations = functionMethod.getParameterAnnotations();
        String[] returnTypeName = cppAnnotationTypeName(returnType, annotations);
        AdapterInformation returnAdapterInfo = adapterInformation(false, valueTypeName(returnTypeName), annotations);
        if (returnAdapterInfo == null || returnAdapterInfo.cast.length() <= 0) {
            prefix = returnTypeName[0] + returnTypeName[1];
        } else {
            prefix = returnAdapterInfo.cast;
        }
        prefix = prefix + " (" + callingConvention + spaceName + "*";
        String suffix = ")";
        if (functionMethod == functionMethods[0]) {
            suffix = suffix + "(";
            if (FunctionPointer.class.isAssignableFrom(type) && namespace != null && (parameterTypes.length == 0 || !Pointer.class.isAssignableFrom(parameterTypes[0]))) {
                this.logger.warn("First parameter of caller method call() or apply() for member function pointer " + type.getCanonicalName() + " is not a Pointer. Compilation will most likely fail.");
            }
            int j = namespace == null ? 0 : 1;
            while (j < parameterTypes.length) {
                String[] paramTypeName = cppAnnotationTypeName(parameterTypes[j], parameterAnnotations[j]);
                AdapterInformation paramAdapterInfo = adapterInformation(false, valueTypeName(paramTypeName), parameterAnnotations[j]);
                if (paramAdapterInfo == null || paramAdapterInfo.cast.length() <= 0) {
                    suffix = suffix + paramTypeName[0] + " arg" + j + paramTypeName[1];
                } else {
                    suffix = suffix + paramAdapterInfo.cast + " arg" + j;
                }
                if (j < parameterTypes.length - 1) {
                    suffix = suffix + ", ";
                }
                j++;
            }
            suffix = suffix + ")";
        }
        if (type.isAnnotationPresent(Const.class)) {
            suffix = suffix + " const";
        }
        return new String[]{prefix, suffix};
    }

    static String cppScopeName(MethodInformation methodInfo) {
        String scopeName = cppScopeName(methodInfo.cls);
        if (methodInfo.method.isAnnotationPresent(Virtual.class)) {
            scopeName = "JavaCPP_" + mangle(scopeName);
        }
        Namespace namespace = (Namespace) methodInfo.method.getAnnotation(Namespace.class);
        String spaceName = namespace == null ? "" : namespace.value();
        if ((namespace != null && namespace.value().length() == 0) || spaceName.startsWith("::")) {
            scopeName = "";
        }
        if (scopeName.length() > 0 && !scopeName.endsWith("::")) {
            scopeName = scopeName + "::";
        }
        scopeName = scopeName + spaceName;
        if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
            scopeName = scopeName + "::";
        }
        return scopeName + methodInfo.memberName[0];
    }

    static String cppScopeName(Class<?> type) {
        String scopeName = "";
        while (type != null) {
            Namespace namespace = (Namespace) type.getAnnotation(Namespace.class);
            String spaceName = namespace == null ? "" : namespace.value();
            if (Pointer.class.isAssignableFrom(type) && (!baseClasses.contains(type) || type.isAnnotationPresent(Name.class))) {
                String s;
                Name name = (Name) type.getAnnotation(Name.class);
                if (name == null) {
                    s = type.getName();
                    int i = s.lastIndexOf("$");
                    if (i < 0) {
                        i = s.lastIndexOf(".");
                    }
                    s = s.substring(i + 1);
                } else {
                    s = name.value()[0];
                }
                if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
                    spaceName = spaceName + "::";
                }
                spaceName = spaceName + s;
            }
            if (!(scopeName.length() <= 0 || scopeName.startsWith("class ") || scopeName.startsWith("struct ") || scopeName.startsWith("union ") || spaceName.endsWith("::"))) {
                spaceName = spaceName + "::";
            }
            scopeName = spaceName + scopeName;
            if ((namespace != null && namespace.value().length() == 0) || spaceName.startsWith("::")) {
                break;
            }
            type = type.getDeclaringClass();
        }
        return scopeName;
    }

    static String jniTypeName(Class type) {
        if (type == Byte.TYPE) {
            return "jbyte";
        }
        if (type == Short.TYPE) {
            return "jshort";
        }
        if (type == Integer.TYPE) {
            return "jint";
        }
        if (type == Long.TYPE) {
            return "jlong";
        }
        if (type == Float.TYPE) {
            return "jfloat";
        }
        if (type == Double.TYPE) {
            return "jdouble";
        }
        if (type == Character.TYPE) {
            return "jchar";
        }
        if (type == Boolean.TYPE) {
            return "jboolean";
        }
        if (type == byte[].class) {
            return "jbyteArray";
        }
        if (type == short[].class) {
            return "jshortArray";
        }
        if (type == int[].class) {
            return "jintArray";
        }
        if (type == long[].class) {
            return "jlongArray";
        }
        if (type == float[].class) {
            return "jfloatArray";
        }
        if (type == double[].class) {
            return "jdoubleArray";
        }
        if (type == char[].class) {
            return "jcharArray";
        }
        if (type == boolean[].class) {
            return "jbooleanArray";
        }
        if (type.isArray()) {
            return "jobjectArray";
        }
        if (type == String.class) {
            return "jstring";
        }
        if (type == Class.class) {
            return "jclass";
        }
        if (type == Void.TYPE) {
            return "void";
        }
        return "jobject";
    }

    static String signature(Class... types) {
        StringBuilder signature = new StringBuilder(types.length * 2);
        for (Class type : types) {
            if (type == Byte.TYPE) {
                signature.append("B");
            } else if (type == Short.TYPE) {
                signature.append("S");
            } else if (type == Integer.TYPE) {
                signature.append("I");
            } else if (type == Long.TYPE) {
                signature.append("J");
            } else if (type == Float.TYPE) {
                signature.append("F");
            } else if (type == Double.TYPE) {
                signature.append("D");
            } else if (type == Boolean.TYPE) {
                signature.append("Z");
            } else if (type == Character.TYPE) {
                signature.append("C");
            } else if (type == Void.TYPE) {
                signature.append("V");
            } else if (type.isArray()) {
                signature.append(type.getName().replace('.', '/'));
            } else {
                signature.append("L").append(type.getName().replace('.', '/')).append(";");
            }
        }
        return signature.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String mangle(java.lang.String r5) {
        /*
        r3 = new java.lang.StringBuilder;
        r4 = r5.length();
        r4 = r4 * 2;
        r3.<init>(r4);
        r2 = 0;
    L_0x000c:
        r4 = r5.length();
        if (r2 >= r4) goto L_0x0084;
    L_0x0012:
        r0 = r5.charAt(r2);
        r4 = 48;
        if (r0 < r4) goto L_0x001e;
    L_0x001a:
        r4 = 57;
        if (r0 <= r4) goto L_0x002e;
    L_0x001e:
        r4 = 65;
        if (r0 < r4) goto L_0x0026;
    L_0x0022:
        r4 = 90;
        if (r0 <= r4) goto L_0x002e;
    L_0x0026:
        r4 = 97;
        if (r0 < r4) goto L_0x0034;
    L_0x002a:
        r4 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r0 > r4) goto L_0x0034;
    L_0x002e:
        r3.append(r0);
    L_0x0031:
        r2 = r2 + 1;
        goto L_0x000c;
    L_0x0034:
        r4 = 95;
        if (r0 != r4) goto L_0x003e;
    L_0x0038:
        r4 = "_1";
        r3.append(r4);
        goto L_0x0031;
    L_0x003e:
        r4 = 59;
        if (r0 != r4) goto L_0x0048;
    L_0x0042:
        r4 = "_2";
        r3.append(r4);
        goto L_0x0031;
    L_0x0048:
        r4 = 91;
        if (r0 != r4) goto L_0x0052;
    L_0x004c:
        r4 = "_3";
        r3.append(r4);
        goto L_0x0031;
    L_0x0052:
        r4 = 46;
        if (r0 == r4) goto L_0x005a;
    L_0x0056:
        r4 = 47;
        if (r0 != r4) goto L_0x0060;
    L_0x005a:
        r4 = "_";
        r3.append(r4);
        goto L_0x0031;
    L_0x0060:
        r1 = java.lang.Integer.toHexString(r0);
        r4 = "_0";
        r3.append(r4);
        r4 = r1.length();
        switch(r4) {
            case 1: goto L_0x0074;
            case 2: goto L_0x0079;
            case 3: goto L_0x007e;
            default: goto L_0x0070;
        };
    L_0x0070:
        r3.append(r1);
        goto L_0x0031;
    L_0x0074:
        r4 = "0";
        r3.append(r4);
    L_0x0079:
        r4 = "0";
        r3.append(r4);
    L_0x007e:
        r4 = "0";
        r3.append(r4);
        goto L_0x0070;
    L_0x0084:
        r4 = r3.toString();
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bytedeco.javacpp.tools.Generator.mangle(java.lang.String):java.lang.String");
    }
}
