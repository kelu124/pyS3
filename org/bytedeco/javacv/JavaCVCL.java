package org.bytedeco.javacv;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLDevice;
import com.jogamp.opencl.CLEventList;
import com.jogamp.opencl.CLImage2d;
import com.jogamp.opencl.CLImageFormat;
import com.jogamp.opencl.CLImageFormat.ChannelOrder;
import com.jogamp.opencl.CLImageFormat.ChannelType;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLMemory.Map;
import com.jogamp.opencl.CLMemory.Mem;
import com.jogamp.opencl.CLObject;
import com.jogamp.opencl.CLPlatform;
import com.jogamp.opencl.CLProgram;
import com.jogamp.opencl.gl.CLGLContext;
import com.jogamp.opencl.gl.CLGLImage2d;
import com.jogamp.opencl.gl.CLGLObject;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.logging.Logger;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core$IplROI;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.videoInputLib;

public class JavaCVCL {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String fastCompilerOptions = "-cl-fast-relaxed-math -cl-mad-enable";
    private static final Logger logger = Logger.getLogger(JavaCVCL.class.getName());
    private final CLCommandQueue commandQueue;
    private final CLContext context;
    private final GLU glu;
    private final CLKernel pyrDownKernel;
    private final CLKernel remapBayerKernel;
    private final CLKernel remapKernel;

    static /* synthetic */ class C12571 {
        static final /* synthetic */ int[] $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder = new int[ChannelOrder.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType = new int[ChannelType.values().length];

        static {
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.SIGNED_INT8.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.SNORM_INT8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNSIGNED_INT8.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNORM_INT8.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.SIGNED_INT16.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.SNORM_INT16.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNSIGNED_INT16.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNORM_INT16.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNSIGNED_INT32.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.SIGNED_INT32.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.FLOAT.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.HALF_FLOAT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNORM_SHORT_565.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNORM_SHORT_555.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[ChannelType.UNORM_INT_101010.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.R.ordinal()] = 1;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.A.ordinal()] = 2;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.INTENSITY.ordinal()] = 3;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.LUMINANCE.ordinal()] = 4;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.Rx.ordinal()] = 5;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RG.ordinal()] = 6;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RA.ordinal()] = 7;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RGx.ordinal()] = 8;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RGB.ordinal()] = 9;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RGBx.ordinal()] = 10;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.RGBA.ordinal()] = 11;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.ARGB.ordinal()] = 12;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[ChannelOrder.BGRA.ordinal()] = 13;
            } catch (NoSuchFieldError e28) {
            }
        }
    }

    class PinnedIplImage extends IplImage {
        final CLBuffer pinnedBuffer;

        PinnedIplImage(int width, int height, int depth, int channels) {
            super(opencv_core.cvCreateImageHeader(new CvSize().width(width).height(height), depth, channels));
            this.pinnedBuffer = JavaCVCL.this.createPinnedBuffer(imageSize());
            imageData(new BytePointer(getByteBuffer()));
        }

        public CLBuffer getCLBuffer() {
            return this.pinnedBuffer;
        }

        public ByteBuffer getByteBuffer() {
            return (ByteBuffer) this.pinnedBuffer.getBuffer();
        }

        public void release() {
            JavaCVCL.this.commandQueue.putUnmapMemory(this.pinnedBuffer, getByteBuffer());
            this.pinnedBuffer.release();
            opencv_core.cvReleaseImageHeader(this);
        }
    }

    static {
        boolean z;
        if (JavaCVCL.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public JavaCVCL(CLContext context) {
        this(context, context.getDevices()[0]);
    }

    public JavaCVCL(CLContext context, CLDevice device) {
        this.context = context;
        this.glu = context instanceof CLGLContext ? new GLU() : null;
        this.commandQueue = device.createCommandQueue();
        CLKernel[] kernels = buildKernels(fastCompilerOptions, "JavaCV.cl", "pyrDown", "remap", "remapBayer");
        this.pyrDownKernel = kernels[0];
        this.remapKernel = kernels[1];
        this.remapBayerKernel = kernels[2];
    }

    public static GLCapabilities getDefaultGLCapabilities(GLProfile profile) {
        if (profile == null) {
            profile = GLProfile.getDefault();
        }
        GLCapabilities caps = new GLCapabilities(profile);
        caps.setDoubleBuffered(false);
        return caps;
    }

    public JavaCVCL() {
        this(false);
    }

    public JavaCVCL(boolean createPbuffer) {
        GLCapabilitiesImmutable defaultGLCapabilities;
        if (createPbuffer) {
            defaultGLCapabilities = getDefaultGLCapabilities(null);
        } else {
            defaultGLCapabilities = null;
        }
        this(defaultGLCapabilities, null, null);
    }

    public JavaCVCL(GLContext shareWith) {
        GLProfile gLProfile;
        if (shareWith == null) {
            gLProfile = null;
        } else {
            gLProfile = shareWith.getGLDrawable().getGLProfile();
        }
        this(getDefaultGLCapabilities(gLProfile), shareWith, null);
    }

    public JavaCVCL(GLCapabilitiesImmutable caps, GLContext shareWith, CLDevice device) {
        GLContext glContext = GLContext.getCurrent();
        if (device == null && glContext != null) {
            for (CLDevice d : CLPlatform.getDefault().listCLDevices()) {
                if (d.isGLMemorySharingSupported()) {
                    device = d;
                    break;
                }
            }
        }
        if (glContext != null && device != null) {
            this.context = CLGLContext.create(glContext, new CLDevice[]{device});
            this.glu = GLU.createGLU();
        } else if (device != null) {
            this.context = CLContext.create(new CLDevice[]{device});
            this.glu = null;
        } else {
            this.context = CLContext.create();
            device = this.context.getDevices()[0];
            this.glu = null;
        }
        this.commandQueue = device.createCommandQueue();
        CLKernel[] kernels = buildKernels(fastCompilerOptions, "JavaCV.cl", "pyrDown", "remap", "remapBayer");
        this.pyrDownKernel = kernels[0];
        this.remapKernel = kernels[1];
        this.remapBayerKernel = kernels[2];
    }

    public void release() {
        if (!this.context.isReleased()) {
            this.context.release();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public CLContext getCLContext() {
        return this.context;
    }

    public CLCommandQueue getCLCommandQueue() {
        return this.commandQueue;
    }

    public CLGLContext getCLGLContext() {
        return this.context instanceof CLGLContext ? (CLGLContext) this.context : null;
    }

    public GLContext getGLContext() {
        return this.context instanceof CLGLContext ? ((CLGLContext) this.context).getGLContext() : null;
    }

    public GL getGL() {
        GLContext glContext = getGLContext();
        return glContext != null ? glContext.getGL() : null;
    }

    public GL2 getGL2() {
        GL gl = getGL();
        return gl != null ? gl.getGL2() : null;
    }

    public GLU getGLU() {
        return this.glu;
    }

    public CLKernel buildKernel(String resourceNames, String kernelName) {
        return buildKernels(fastCompilerOptions, Loader.getCallerClass(2), resourceNames, kernelName)[0];
    }

    public CLKernel buildKernel(String compilerOptions, String resourceNames, String kernelName) {
        return buildKernels(compilerOptions, Loader.getCallerClass(2), resourceNames, kernelName)[0];
    }

    public CLKernel[] buildKernels(String compilerOptions, String resourceNames, String... kernelNames) {
        return buildKernels(compilerOptions, Loader.getCallerClass(2), resourceNames, kernelNames);
    }

    public CLKernel[] buildKernels(String compilerOptions, Class resourceClass, String resourceNames, String... kernelNames) {
        try {
            InputStream s;
            String[] a = resourceNames.split(":");
            if (a.length == 1) {
                s = resourceClass.getResourceAsStream(a[0]);
            } else {
                Vector<InputStream> vs = new Vector(a.length);
                for (String name : a) {
                    vs.addElement(resourceClass.getResourceAsStream(name));
                }
                s = new SequenceInputStream(vs.elements());
            }
            CLProgram program = this.context.createProgram(s);
            program.build(compilerOptions);
            if ($assertionsDisabled || program.isExecutable()) {
                CLKernel[] kernels = new CLKernel[kernelNames.length];
                for (int i = 0; i < kernelNames.length; i++) {
                    kernels[i] = program.createCLKernel(kernelNames[i]);
                }
                return kernels;
            }
            throw new AssertionError();
        } catch (IOException ex) {
            throw ((Error) new LinkageError(ex.toString()).initCause(ex));
        }
    }

    public CLImage2d createCLImageFrom(IplImage image, Mem... flags) {
        int width = image.width();
        int height = image.height();
        int pitch = image.widthStep();
        ByteBuffer buffer = image.getByteBuffer();
        ChannelOrder order = null;
        ChannelType type = null;
        int size = 0;
        switch (image.depth()) {
            case -2147483640:
                type = ChannelType.SNORM_INT8;
                size = 1;
                break;
            case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                type = ChannelType.SNORM_INT16;
                size = 2;
                break;
            case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                type = ChannelType.SIGNED_INT32;
                size = 4;
                break;
            case 8:
                type = ChannelType.UNORM_INT8;
                size = 1;
                break;
            case 16:
                type = ChannelType.UNORM_INT16;
                size = 2;
                break;
            case 32:
                type = ChannelType.FLOAT;
                size = 4;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        switch (image.nChannels()) {
            case 1:
                order = ChannelOrder.LUMINANCE;
                break;
            case 2:
                order = ChannelOrder.RG;
                size *= 2;
                break;
            case 3:
                order = ChannelOrder.RGB;
                size *= 3;
                break;
            case 4:
                order = ChannelOrder.RGBA;
                size *= 4;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        if (width != pitch / size) {
            width = pitch / size;
        }
        return this.context.createImage2d(buffer, width, height, new CLImageFormat(order, type), flags);
    }

    public CLGLImage2d createCLGLImageFrom(IplImage image, Mem... flags) {
        GL2 gl = getGL2();
        if (gl == null) {
            return null;
        }
        int width = image.width();
        int height = image.height();
        int pitch = image.widthStep();
        int format = 0;
        int size = 0;
        switch (image.nChannels()) {
            case 1:
                switch (image.depth()) {
                    case -2147483640:
                        format = 36885;
                        size = 1;
                        break;
                    case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                        format = 36889;
                        size = 2;
                        break;
                    case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                        format = 36230;
                        size = 4;
                        break;
                    case 8:
                        format = 32832;
                        size = 1;
                        break;
                    case 16:
                        format = 32834;
                        size = 2;
                        break;
                    case 32:
                        format = 34840;
                        size = 4;
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
            case 2:
                switch (image.depth()) {
                    case -2147483640:
                        format = 36757;
                        size = 2;
                        break;
                    case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                        format = 36761;
                        size = 4;
                        break;
                    case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                        format = 33339;
                        size = 8;
                        break;
                    case 8:
                        format = 33323;
                        size = 2;
                        break;
                    case 16:
                        format = 33324;
                        size = 4;
                        break;
                    case 32:
                        format = 33328;
                        size = 8;
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
            case 3:
                switch (image.depth()) {
                    case -2147483640:
                        format = 36758;
                        size = 3;
                        break;
                    case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                        format = 36762;
                        size = 6;
                        break;
                    case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                        format = 36227;
                        size = 12;
                        break;
                    case 8:
                        format = 32849;
                        size = 3;
                        break;
                    case 16:
                        format = 32852;
                        size = 6;
                        break;
                    case 32:
                        format = 34837;
                        size = 12;
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
            case 4:
                switch (image.depth()) {
                    case -2147483640:
                        format = 36759;
                        size = 4;
                        break;
                    case opencv_core.IPL_DEPTH_16S /*-2147483632*/:
                        format = 36763;
                        size = 8;
                        break;
                    case opencv_core.IPL_DEPTH_32S /*-2147483616*/:
                        format = 36226;
                        size = 16;
                        break;
                    case 8:
                        format = 32856;
                        size = 4;
                        break;
                    case 16:
                        format = 32859;
                        size = 8;
                        break;
                    case 32:
                        format = 34836;
                        size = 16;
                        break;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                }
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        if (width != pitch / size) {
            width = pitch / size;
        }
        int[] renderBuffer = new int[1];
        gl.glGenRenderbuffers(1, renderBuffer, 0);
        gl.glBindRenderbuffer(36161, renderBuffer[0]);
        gl.glRenderbufferStorage(36161, format, width, height);
        return getCLGLContext().createFromGLRenderbuffer(renderBuffer[0], flags);
    }

    public void releaseCLGLImage(CLGLImage2d image) {
        image.release();
        getGL2().glDeleteRenderbuffers(1, new int[]{image.getGLObjectID()}, 0);
    }

    public CLBuffer createPinnedBuffer(int size) {
        CLBuffer pinnedBuffer = this.context.createBuffer(size, new Mem[]{Mem.ALLOCATE_BUFFER});
        pinnedBuffer.use(this.commandQueue.putMapBuffer(pinnedBuffer, Map.READ_WRITE, true));
        return pinnedBuffer;
    }

    public IplImage createPinnedIplImage(int width, int height, int depth, int channels) {
        return new PinnedIplImage(width, height, depth, channels);
    }

    public IplImage createIplImageFrom(CLImage2d image) {
        int width = image.width;
        int height = image.height;
        CLImageFormat format = image.getFormat();
        ChannelOrder order = format.getImageChannelOrder();
        ChannelType type = format.getImageChannelDataType();
        int depth = 0;
        int channels = 0;
        switch (C12571.$SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelOrder[order.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
                channels = 1;
                break;
            case 5:
            case 6:
            case 7:
                channels = 2;
                break;
            case 8:
            case 9:
                channels = 3;
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                channels = 4;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        switch (C12571.$SwitchMap$com$jogamp$opencl$CLImageFormat$ChannelType[type.ordinal()]) {
            case 1:
            case 2:
                depth = -2147483640;
                break;
            case 3:
            case 4:
                depth = 8;
                break;
            case 5:
            case 6:
                depth = opencv_core.IPL_DEPTH_16S;
                break;
            case 7:
            case 8:
                depth = 16;
                break;
            case 9:
            case 10:
                depth = opencv_core.IPL_DEPTH_32S;
                break;
            case 11:
                depth = 32;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return IplImage.create(width, height, depth, channels);
    }

    public IplImage readImage(CLImage2d clImg, IplImage iplImage, boolean blocking) {
        if (iplImage == null) {
            iplImage = createIplImageFrom(clImg);
        }
        int x = 0;
        int y = 0;
        int width = clImg.width;
        int height = clImg.height;
        int pitch = iplImage.widthStep();
        ByteBuffer buffer = iplImage.getByteBuffer();
        opencv_core$IplROI roi = iplImage.roi();
        if (roi != null) {
            x = roi.xOffset();
            y = roi.yOffset();
            width = roi.width();
            height = roi.height();
            buffer = iplImage.getByteBuffer((y * pitch) + (x * (iplImage.nChannels() * ((iplImage.depth() & Integer.MAX_VALUE) / 8))));
        }
        clImg.use(buffer);
        this.commandQueue.putReadImage(clImg, pitch, x, y, width, height, blocking);
        return iplImage;
    }

    public CLImage2d writeImage(CLImage2d clImg, IplImage iplImage, boolean blocking) {
        if (clImg == null) {
            clImg = createCLImageFrom(iplImage, new Mem[0]);
        }
        int x = 0;
        int y = 0;
        int width = iplImage.width();
        int height = iplImage.height();
        int pitch = iplImage.widthStep();
        ByteBuffer buffer = iplImage.getByteBuffer();
        opencv_core$IplROI roi = iplImage.roi();
        if (roi != null) {
            x = roi.xOffset();
            y = roi.yOffset();
            width = roi.width();
            height = roi.height();
            buffer = iplImage.getByteBuffer((y * pitch) + (x * (iplImage.nChannels() * ((iplImage.depth() & Integer.MAX_VALUE) / 8))));
        }
        clImg.use(buffer);
        this.commandQueue.putWriteImage(clImg, pitch, x, y, width, height, blocking);
        return clImg;
    }

    public void acquireGLObject(CLObject object) {
        if (object instanceof CLGLObject) {
            this.commandQueue.putAcquireGLObject((CLGLObject) object);
        }
    }

    public void releaseGLObject(CLObject object) {
        if (object instanceof CLGLObject) {
            this.commandQueue.putReleaseGLObject((CLGLObject) object);
        }
    }

    public void readBuffer(CLBuffer<?> buffer, boolean blocking) {
        this.commandQueue.putReadBuffer(buffer, blocking);
    }

    public void writeBuffer(CLBuffer<?> buffer, boolean blocking) {
        this.commandQueue.putWriteBuffer(buffer, blocking);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkSizeX, long localWorkSizeX) {
        this.commandQueue.put1DRangeKernel(kernel, globalWorkOffsetX, globalWorkSizeX, localWorkSizeX);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkSizeX, long localWorkSizeX, CLEventList events) {
        this.commandQueue.put1DRangeKernel(kernel, globalWorkOffsetX, globalWorkSizeX, localWorkSizeX, events);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkSizeX, long localWorkSizeX, CLEventList condition, CLEventList events) {
        this.commandQueue.put1DRangeKernel(kernel, globalWorkOffsetX, globalWorkSizeX, localWorkSizeX, condition, events);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkSizeX, long globalWorkSizeY, long localWorkSizeX, long localWorkSizeY) {
        this.commandQueue.put2DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkSizeX, globalWorkSizeY, localWorkSizeX, localWorkSizeY);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkSizeX, long globalWorkSizeY, long localWorkSizeX, long localWorkSizeY, CLEventList events) {
        this.commandQueue.put2DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkSizeX, globalWorkSizeY, localWorkSizeX, localWorkSizeY, events);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkSizeX, long globalWorkSizeY, long localWorkSizeX, long localWorkSizeY, CLEventList condition, CLEventList events) {
        this.commandQueue.put2DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkSizeX, globalWorkSizeY, localWorkSizeX, localWorkSizeY, condition, events);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkOffsetZ, long globalWorkSizeX, long globalWorkSizeY, long globalWorkSizeZ, long localWorkSizeX, long localWorkSizeY, long localWorkSizeZ) {
        this.commandQueue.put3DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkOffsetZ, globalWorkSizeX, globalWorkSizeY, globalWorkSizeZ, localWorkSizeX, localWorkSizeY, localWorkSizeZ);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkOffsetZ, long globalWorkSizeX, long globalWorkSizeY, long globalWorkSizeZ, long localWorkSizeX, long localWorkSizeY, long localWorkSizeZ, CLEventList events) {
        this.commandQueue.put3DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkOffsetZ, globalWorkSizeX, globalWorkSizeY, globalWorkSizeZ, localWorkSizeX, localWorkSizeY, localWorkSizeZ, events);
    }

    public void executeKernel(CLKernel kernel, long globalWorkOffsetX, long globalWorkOffsetY, long globalWorkOffsetZ, long globalWorkSizeX, long globalWorkSizeY, long globalWorkSizeZ, long localWorkSizeX, long localWorkSizeY, long localWorkSizeZ, CLEventList condition, CLEventList events) {
        this.commandQueue.put3DRangeKernel(kernel, globalWorkOffsetX, globalWorkOffsetY, globalWorkOffsetZ, globalWorkSizeX, globalWorkSizeY, globalWorkSizeZ, localWorkSizeX, localWorkSizeY, localWorkSizeZ, condition, events);
    }

    public void finish() {
        this.commandQueue.finish();
    }

    public void flush() {
        this.commandQueue.flush();
    }

    public static int alignCeil(int x, int n) {
        return (((x + n) - 1) / n) * n;
    }

    public static int alignFloor(int x, int n) {
        return (x / n) * n;
    }

    public void pyrDown(CLImage2d srcImg, CLImage2d dstImg) {
        this.pyrDownKernel.putArg(srcImg).putArg(dstImg).rewind();
        executeKernel(this.pyrDownKernel, 0, 0, (long) alignCeil(dstImg.width, 2), (long) alignCeil(dstImg.height, 64), 2, 64, null);
    }

    public void remap(CLImage2d srcImg, CLImage2d dstImg, CLImage2d mapxImg, CLImage2d mapyImg) {
        remap(srcImg, dstImg, mapxImg, mapyImg, -1);
    }

    public void remap(CLImage2d srcImg, CLImage2d dstImg, CLImage2d mapxImg, CLImage2d mapyImg, long sensorPattern) {
        CLKernel kernel;
        if (sensorPattern != -1) {
            kernel = this.remapBayerKernel.putArg(srcImg).putArg(dstImg).putArg(mapxImg).putArg(mapyImg).putArg(sensorPattern).rewind();
        } else {
            kernel = this.remapKernel.putArg(srcImg).putArg(dstImg).putArg(mapxImg).putArg(mapyImg).rewind();
        }
        executeKernel(kernel, 0, 0, (long) alignCeil(dstImg.width, 2), (long) alignCeil(dstImg.height, 64), 2, 64, null);
    }

    public static void main(String[] args) {
        JavaCVCL context = new JavaCVCL();
        for (CLImageFormat f : context.getCLContext().getSupportedImage2dFormats(new Mem[0])) {
            System.out.println(f);
        }
        CameraDevice camera = new CameraDevice("Camera");
        camera.imageWidth = 1280;
        camera.imageHeight = 960;
        camera.cameraMatrix = CvMat.create(3, 3);
        double f2 = ((double) camera.imageWidth) * 2.5d;
        camera.cameraMatrix.put(new double[]{f2, 0.0d, (double) (camera.imageWidth / 2), 0.0d, f2, (double) (camera.imageHeight / 2), 0.0d, 0.0d, 1.0d});
        camera.R = CvMat.create(3, 3);
        opencv_core.cvSetIdentity(camera.R);
        camera.T = CvMat.create(3, 1);
        opencv_core.cvSetZero(camera.T);
        camera.distortionCoeffs = CvMat.create(1, 4);
        opencv_core.cvSetZero(camera.distortionCoeffs);
        camera.distortionCoeffs.put(new double[]{videoInputLib.VI_VERSION});
        camera.colorMixingMatrix = CvMat.create(3, 3);
        opencv_core.cvSetIdentity(camera.colorMixingMatrix);
        IplImage srcImg = opencv_imgcodecs.cvLoadImageRGBA(args[0]);
        IplImage downDst = IplImage.create(srcImg.width() / 2, srcImg.height() / 2, 8, 4);
        camera.setFixedPointMaps(false);
        camera.setMapsPyramidLevel(1);
        IplImage mapxImg = camera.getUndistortMap1();
        IplImage mapyImg = camera.getUndistortMap2();
        long start = System.nanoTime();
        opencv_imgproc.cvRemap(srcImg, downDst, mapxImg, mapyImg, 9, CvScalar.ZERO);
        System.out.println("cvRemap: " + (((double) (System.nanoTime() - start)) / 1000000.0d));
        opencv_imgcodecs.cvSaveImage("/tmp/opencv.png", downDst);
        CLImage2d src = context.createCLImageFrom(srcImg, new Mem[0]);
        CLImage2d dst = context.createCLImageFrom(downDst, new Mem[0]);
        CLImage2d mapx = context.createCLImageFrom(mapxImg, new Mem[0]);
        CLImage2d mapy = context.createCLImageFrom(mapyImg, new Mem[0]);
        context.writeImage(src, srcImg, false);
        context.writeImage(mapx, mapxImg, false);
        context.writeImage(mapy, mapyImg, false);
        context.remap(src, dst, mapx, mapy);
        context.readImage(dst, downDst, true);
        opencv_imgcodecs.cvSaveImage("/tmp/javacvcl.png", downDst);
        context.release();
        System.exit(0);
    }
}
