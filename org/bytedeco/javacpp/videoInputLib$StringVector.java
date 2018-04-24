package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Index;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.StdString;
import org.bytedeco.javacpp.annotation.ValueSetter;

@Name({"std::vector<std::string>"})
public class videoInputLib$StringVector extends Pointer {
    private native void allocate();

    private native void allocate(@Cast({"size_t"}) long j);

    @Index
    @StdString
    public native BytePointer get(@Cast({"size_t"}) long j);

    @Index
    @ValueSetter
    public native videoInputLib$StringVector put(@Cast({"size_t"}) long j, @StdString String str);

    public native videoInputLib$StringVector put(@Cast({"size_t"}) long j, BytePointer bytePointer);

    @ByRef
    @Name({"operator="})
    public native videoInputLib$StringVector put(@ByRef videoInputLib$StringVector org_bytedeco_javacpp_videoInputLib_StringVector);

    public native void resize(@Cast({"size_t"}) long j);

    public native long size();

    static {
        Loader.load();
    }

    public videoInputLib$StringVector(Pointer p) {
        super(p);
    }

    public videoInputLib$StringVector(BytePointer... array) {
        this((long) array.length);
        put(array);
    }

    public videoInputLib$StringVector(String... array) {
        this((long) array.length);
        put(array);
    }

    public videoInputLib$StringVector() {
        allocate();
    }

    public videoInputLib$StringVector(long n) {
        allocate(n);
    }

    public videoInputLib$StringVector put(BytePointer... array) {
        if (size() != ((long) array.length)) {
            resize((long) array.length);
        }
        for (int i = 0; i < array.length; i++) {
            put((long) i, array[i]);
        }
        return this;
    }

    public videoInputLib$StringVector put(String... array) {
        if (size() != ((long) array.length)) {
            resize((long) array.length);
        }
        for (int i = 0; i < array.length; i++) {
            put((long) i, array[i]);
        }
        return this;
    }
}
