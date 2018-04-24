package org.bytedeco.javacpp;

import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Convention;
import org.bytedeco.javacpp.annotation.Index;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar4i;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.StringVector;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_dnn extends org.bytedeco.javacpp.presets.opencv_dnn {

    @Namespace("cv::dnn")
    @NoOffset
    public static class Blob extends Pointer {
        private native void allocate();

        private native void allocate(@ByVal Mat mat);

        private native void allocate(@ByVal Mat mat, int i);

        private native void allocate(@ByVal UMat uMat);

        private native void allocate(@ByVal UMat uMat, int i);

        private native void allocate(@ByRef @Const BlobShape blobShape);

        private native void allocate(@ByRef @Const BlobShape blobShape, int i);

        private native void allocateArray(long j);

        @Name({"fill"})
        public native void _fill(@ByRef @Const BlobShape blobShape, int i, Pointer pointer);

        @Name({"fill"})
        public native void _fill(@ByRef @Const BlobShape blobShape, int i, Pointer pointer, @Cast({"bool"}) boolean z);

        public native int canonicalAxis(int i);

        public native int channels();

        public native int cols();

        public native void create(@ByRef @Const BlobShape blobShape);

        public native void create(@ByRef @Const BlobShape blobShape, int i);

        public native int dims();

        @Cast({"bool"})
        public native boolean equalShape(@ByRef @Const Blob blob);

        @ByVal
        public native Mat getPlane(int i, int i2);

        @ByRef
        public native Mat matRef();

        @ByRef
        @Const
        public native Mat matRefConst();

        public native int num();

        @Cast({"size_t"})
        public native long offset();

        @Cast({"size_t"})
        public native long offset(int i, int i2, int i3, int i4);

        @Cast({"uchar*"})
        public native BytePointer ptr();

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, int i3, int i4);

        public native FloatPointer ptrf();

        public native FloatPointer ptrf(int i, int i2, int i3, int i4);

        @ByRef
        public native Blob reshape(@ByRef @Const BlobShape blobShape);

        public native int rows();

        @ByVal
        public native BlobShape shape();

        @ByVal
        public native Scalar4i shape4();

        @ByRef
        public native Blob shareFrom(@ByRef @Const Blob blob);

        public native int size(int i);

        @ByVal
        public native Size size2();

        @Cast({"size_t"})
        public native long total();

        @Cast({"size_t"})
        public native long total(int i, int i2);

        public native int type();

        @ByRef
        public native UMat umatRef();

        @ByRef
        @Const
        public native UMat umatRefConst();

        public native int xsize(int i);

        static {
            Loader.load();
        }

        public Blob(Pointer p) {
            super(p);
        }

        public Blob(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Blob position(long position) {
            return (Blob) super.position(position);
        }

        public Blob() {
            super((Pointer) null);
            allocate();
        }

        public Blob(@ByRef @Const BlobShape shape, int type) {
            super((Pointer) null);
            allocate(shape, type);
        }

        public Blob(@ByRef @Const BlobShape shape) {
            super((Pointer) null);
            allocate(shape);
        }

        public Blob(@ByVal Mat image, int dstCn) {
            super((Pointer) null);
            allocate(image, dstCn);
        }

        public Blob(@ByVal Mat image) {
            super((Pointer) null);
            allocate(image);
        }

        public Blob(@ByVal UMat image, int dstCn) {
            super((Pointer) null);
            allocate(image, dstCn);
        }

        public Blob(@ByVal UMat image) {
            super((Pointer) null);
            allocate(image);
        }
    }

    @Name({"std::vector<cv::dnn::Blob*>"})
    public static class BlobPointerVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        public native Blob get(@Cast({"size_t"}) long j);

        public native BlobPointerVector put(@Cast({"size_t"}) long j, Blob blob);

        @ByRef
        @Name({"operator="})
        public native BlobPointerVector put(@ByRef BlobPointerVector blobPointerVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public BlobPointerVector(Pointer p) {
            super(p);
        }

        public BlobPointerVector(Blob... array) {
            this((long) array.length);
            put(array);
        }

        public BlobPointerVector() {
            allocate();
        }

        public BlobPointerVector(long n) {
            allocate(n);
        }

        public BlobPointerVector put(Blob... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class BlobShape extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4);

        private native void allocate(int i, @Const IntBuffer intBuffer);

        private native void allocate(int i, @Const IntPointer intPointer);

        private native void allocate(int i, @Const int[] iArr);

        private native void allocate(@StdVector IntBuffer intBuffer);

        private native void allocate(@StdVector IntPointer intPointer);

        private native void allocate(@StdVector int[] iArr);

        private native void allocateArray(long j);

        public native int dims();

        @Cast({"bool"})
        public native boolean equal(@ByRef @Const BlobShape blobShape);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const BlobShape blobShape);

        @ByRef
        @Name({"operator []"})
        public native IntPointer get(int i);

        @Const
        public native IntPointer ptr();

        @ByRef
        public native IntPointer size(int i);

        @Cast({"ptrdiff_t"})
        public native long total();

        public native int xsize(int i);

        static {
            Loader.load();
        }

        public BlobShape(Pointer p) {
            super(p);
        }

        public BlobShape(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BlobShape position(long position) {
            return (BlobShape) super.position(position);
        }

        public BlobShape(int ndims, int fill) {
            super((Pointer) null);
            allocate(ndims, fill);
        }

        public BlobShape() {
            super((Pointer) null);
            allocate();
        }

        public BlobShape(int num, int cn, int rows, int cols) {
            super((Pointer) null);
            allocate(num, cn, rows, cols);
        }

        public BlobShape(int ndims, @Const IntPointer sizes) {
            super((Pointer) null);
            allocate(ndims, sizes);
        }

        public BlobShape(int ndims, @Const IntBuffer sizes) {
            super((Pointer) null);
            allocate(ndims, sizes);
        }

        public BlobShape(int ndims, @Const int[] sizes) {
            super((Pointer) null);
            allocate(ndims, sizes);
        }

        public BlobShape(@StdVector IntPointer sizes) {
            super((Pointer) null);
            allocate(sizes);
        }

        public BlobShape(@StdVector IntBuffer sizes) {
            super((Pointer) null);
            allocate(sizes);
        }

        public BlobShape(@StdVector int[] sizes) {
            super((Pointer) null);
            allocate(sizes);
        }
    }

    @Name({"std::vector<cv::dnn::Blob>"})
    public static class BlobVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Blob get(@Cast({"size_t"}) long j);

        public native BlobVector put(@Cast({"size_t"}) long j, Blob blob);

        @ByRef
        @Name({"operator="})
        public native BlobVector put(@ByRef BlobVector blobVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public BlobVector(Pointer p) {
            super(p);
        }

        public BlobVector(Blob... array) {
            this((long) array.length);
            put(array);
        }

        public BlobVector() {
            allocate();
        }

        public BlobVector(long n) {
            allocate(n);
        }

        public BlobVector put(Blob... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv::dnn")
    public static class Dict extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        @Const
        public native DictValue get(@Str String str);

        @ByRef
        @Const
        public native DictValue get(@Str BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean has(@Str String str);

        @Cast({"bool"})
        public native boolean has(@Str BytePointer bytePointer);

        public native DictValue ptr(@Str String str);

        public native DictValue ptr(@Str BytePointer bytePointer);

        static {
            Loader.load();
        }

        public Dict() {
            super((Pointer) null);
            allocate();
        }

        public Dict(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Dict(Pointer p) {
            super(p);
        }

        public Dict position(long position) {
            return (Dict) super.position(position);
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class DictValue extends Pointer {
        private native void allocate();

        private native void allocate(double d);

        private native void allocate(int i);

        private native void allocate(@Str String str);

        private native void allocate(@Str BytePointer bytePointer);

        private native void allocate(@ByRef @Const DictValue dictValue);

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean isInt();

        @Cast({"bool"})
        public native boolean isReal();

        @Cast({"bool"})
        public native boolean isString();

        @ByRef
        @Name({"operator ="})
        public native DictValue put(@ByRef @Const DictValue dictValue);

        public native int size();

        static {
            Loader.load();
        }

        public DictValue(Pointer p) {
            super(p);
        }

        public DictValue(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DictValue position(long position) {
            return (DictValue) super.position(position);
        }

        public DictValue(@ByRef @Const DictValue r) {
            super((Pointer) null);
            allocate(r);
        }

        public DictValue(int p) {
            super((Pointer) null);
            allocate(p);
        }

        public DictValue() {
            super((Pointer) null);
            allocate();
        }

        public DictValue(double p) {
            super((Pointer) null);
            allocate(p);
        }

        public DictValue(@Str BytePointer p) {
            super((Pointer) null);
            allocate(p);
        }

        public DictValue(@Str String p) {
            super((Pointer) null);
            allocate(p);
        }
    }

    @Namespace("cv::dnn")
    public static class Importer extends Pointer {
        public native void populateNet(@ByVal Net net);

        static {
            Loader.load();
        }

        public Importer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class Layer extends Pointer {
        @Name({"allocate"})
        public native void _allocate(@ByRef @Const BlobPointerVector blobPointerVector, @ByRef BlobVector blobVector);

        @ByRef
        public native BlobVector blobs();

        public native Layer blobs(BlobVector blobVector);

        public native void forward(@ByRef BlobPointerVector blobPointerVector, @ByRef BlobVector blobVector);

        public native int inputNameToIndex(@Str String str);

        public native int inputNameToIndex(@Str BytePointer bytePointer);

        @Str
        public native BytePointer name();

        public native Layer name(BytePointer bytePointer);

        public native int outputNameToIndex(@Str String str);

        public native int outputNameToIndex(@Str BytePointer bytePointer);

        @Str
        public native BytePointer type();

        public native Layer type(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public Layer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::dnn")
    public static class LayerFactory extends Pointer {

        @Convention(extern = "C++", value = "")
        public static class Constuctor extends FunctionPointer {
            private native void allocate();

            @Ptr
            public native Layer call(@ByRef LayerParams layerParams);

            static {
                Loader.load();
            }

            public Constuctor(Pointer p) {
                super(p);
            }

            protected Constuctor() {
                allocate();
            }
        }

        @Ptr
        public static native Layer createLayerInstance(@Str String str, @ByRef LayerParams layerParams);

        @Ptr
        public static native Layer createLayerInstance(@Str BytePointer bytePointer, @ByRef LayerParams layerParams);

        public static native void registerLayer(@Str String str, Constuctor constuctor);

        public static native void registerLayer(@Str BytePointer bytePointer, Constuctor constuctor);

        public static native void unregisterLayer(@Str String str);

        public static native void unregisterLayer(@Str BytePointer bytePointer);

        static {
            Loader.load();
        }

        public LayerFactory(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class LayerParams extends Dict {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        public native BlobVector blobs();

        public native LayerParams blobs(BlobVector blobVector);

        @Str
        public native BytePointer name();

        public native LayerParams name(BytePointer bytePointer);

        @Str
        public native BytePointer type();

        public native LayerParams type(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public LayerParams() {
            super((Pointer) null);
            allocate();
        }

        public LayerParams(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public LayerParams(Pointer p) {
            super(p);
        }

        public LayerParams position(long position) {
            return (LayerParams) super.position(position);
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class Net extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int addLayer(@Str String str, @Str String str2, @ByRef LayerParams layerParams);

        public native int addLayer(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, @ByRef LayerParams layerParams);

        public native int addLayerToPrev(@Str String str, @Str String str2, @ByRef LayerParams layerParams);

        public native int addLayerToPrev(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, @ByRef LayerParams layerParams);

        public native void connect(int i, int i2, int i3, int i4);

        public native void connect(@Str String str, @Str String str2);

        public native void connect(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

        public native void deleteLayer(@Cast({"cv::dnn::Net::LayerId*"}) @ByVal DictValue dictValue);

        public native void forward();

        public native void forward(@Cast({"cv::dnn::Net::LayerId*"}) @ByVal DictValue dictValue);

        @ByVal
        public native Blob getBlob(@Str String str);

        @ByVal
        public native Blob getBlob(@Str BytePointer bytePointer);

        public native int getLayerId(@Str String str);

        public native int getLayerId(@Str BytePointer bytePointer);

        @ByVal
        public native Blob getParam(@Cast({"cv::dnn::Net::LayerId*"}) @ByVal DictValue dictValue);

        @ByVal
        public native Blob getParam(@Cast({"cv::dnn::Net::LayerId*"}) @ByVal DictValue dictValue, int i);

        public native void setBlob(@Str String str, @ByRef @Const Blob blob);

        public native void setBlob(@Str BytePointer bytePointer, @ByRef @Const Blob blob);

        public native void setNetInputs(@ByRef @Const StringVector stringVector);

        static {
            Loader.load();
        }

        public Net(Pointer p) {
            super(p);
        }

        public Net(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Net position(long position) {
            return (Net) super.position(position);
        }

        public Net() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::dnn")
    @NoOffset
    public static class _LayerStaticRegisterer extends Pointer {
        private native void allocate(@Str String str, Constuctor constuctor);

        private native void allocate(@Str BytePointer bytePointer, Constuctor constuctor);

        @Str
        public native BytePointer type();

        public native _LayerStaticRegisterer type(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public _LayerStaticRegisterer(Pointer p) {
            super(p);
        }

        public _LayerStaticRegisterer(@Str BytePointer type, Constuctor constuctor) {
            super((Pointer) null);
            allocate(type, constuctor);
        }

        public _LayerStaticRegisterer(@Str String type, Constuctor constuctor) {
            super((Pointer) null);
            allocate(type, constuctor);
        }
    }

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createCaffeImporter(@Str String str);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createCaffeImporter(@Str String str, @Str String str2);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createCaffeImporter(@Str BytePointer bytePointer);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createCaffeImporter(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createTorchImporter(@Str String str);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createTorchImporter(@Str String str, @Cast({"bool"}) boolean z);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createTorchImporter(@Str BytePointer bytePointer);

    @Namespace("cv::dnn")
    @Ptr
    public static native Importer createTorchImporter(@Str BytePointer bytePointer, @Cast({"bool"}) boolean z);

    @Namespace("cv::dnn")
    public static native void initModule();

    static {
        Loader.load();
    }
}
