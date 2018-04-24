package org.bytedeco.javacv;

import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfBoolean;
import com.jogamp.opencl.CLMemory.Mem;
import com.jogamp.opencl.gl.CLGLImage2d;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Gamma;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacv.CanvasFrame.Exception;

public class GLCanvasFrame extends CanvasFrame {
    static final /* synthetic */ boolean $assertionsDisabled = (!GLCanvasFrame.class.desiredAssertionStatus());
    private static GLCanvasFrame canvasFrame;
    private Buffer buffer;
    private Color color;
    private GLEventListener eventListener;
    private int format;
    private int frameBuffer;
    private int height;
    private int[] params;
    private int renderBuffer;
    private int type;
    private int width;

    class C12502 implements GLEventListener {
        static final /* synthetic */ boolean $assertionsDisabled = (!GLCanvasFrame.class.desiredAssertionStatus());

        C12502() {
        }

        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            gl.setSwapInterval(1);
            if (GLCanvasFrame.this.inverseGamma != 1.0d) {
                Gamma.setDisplayGamma(drawable, (float) GLCanvasFrame.this.inverseGamma, 0.0f, BaseField.BORDER_WIDTH_THIN);
            }
            gl.glGenFramebuffers(1, GLCanvasFrame.this.params, 0);
            GLCanvasFrame.this.frameBuffer = GLCanvasFrame.this.params[0];
        }

        public void dispose(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            GLCanvasFrame.this.params[0] = GLCanvasFrame.this.frameBuffer;
            gl.glDeleteFramebuffers(1, GLCanvasFrame.this.params, 0);
            if (GLCanvasFrame.this.inverseGamma != 1.0d) {
                Gamma.resetDisplayGamma(drawable);
            }
        }

        public void display(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();
            if (GLCanvasFrame.this.color != null) {
                gl.glClearColor(((float) GLCanvasFrame.this.color.getRed()) / 255.0f, ((float) GLCanvasFrame.this.color.getGreen()) / 255.0f, ((float) GLCanvasFrame.this.color.getBlue()) / 255.0f, BaseField.BORDER_WIDTH_THIN);
                gl.glClear(16384);
            } else if (GLCanvasFrame.this.buffer != null) {
                if (GLCanvasFrame.this.isResizable() && GLCanvasFrame.this.needInitialResize) {
                    GLCanvasFrame.this.setCanvasSize((int) Math.round(((double) GLCanvasFrame.this.width) * GLCanvasFrame.this.initialScale), (int) Math.round(((double) GLCanvasFrame.this.height) * GLCanvasFrame.this.initialScale));
                }
                gl.glWindowPos2i(0, GLCanvasFrame.this.canvas.getHeight());
                gl.glPixelZoom(((float) GLCanvasFrame.this.canvas.getWidth()) / ((float) GLCanvasFrame.this.width), (-((float) GLCanvasFrame.this.canvas.getHeight())) / ((float) GLCanvasFrame.this.height));
                gl.glDrawPixels(GLCanvasFrame.this.width, GLCanvasFrame.this.height, GLCanvasFrame.this.format, GLCanvasFrame.this.type, GLCanvasFrame.this.buffer);
            } else if (GLCanvasFrame.this.renderBuffer > 0) {
                gl.glBindRenderbuffer(36161, GLCanvasFrame.this.renderBuffer);
                gl.glGetRenderbufferParameteriv(36161, 36162, GLCanvasFrame.this.params, 0);
                gl.glGetRenderbufferParameteriv(36161, 36163, GLCanvasFrame.this.params, 1);
                if (GLCanvasFrame.this.isResizable() && GLCanvasFrame.this.needInitialResize) {
                    GLCanvasFrame.this.setCanvasSize((int) Math.round(((double) GLCanvasFrame.this.params[0]) * GLCanvasFrame.this.initialScale), (int) Math.round(((double) GLCanvasFrame.this.params[1]) * GLCanvasFrame.this.initialScale));
                }
                gl.glBindFramebuffer(36008, GLCanvasFrame.this.frameBuffer);
                gl.glFramebufferRenderbuffer(36008, 36064, 36161, GLCanvasFrame.this.renderBuffer);
                if ($assertionsDisabled || gl.glCheckFramebufferStatus(36008) == 36053) {
                    gl.glBlitFramebuffer(0, 0, GLCanvasFrame.this.params[0], GLCanvasFrame.this.params[1], 0, GLCanvasFrame.this.canvas.getHeight(), GLCanvasFrame.this.canvas.getWidth(), 0, 16384, 9729);
                    return;
                }
                throw new AssertionError();
            }
        }

        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        }
    }

    static class C12513 implements Runnable {
        C12513() {
        }

        public void run() {
            try {
                GLCanvasFrame.canvasFrame = new GLCanvasFrame("Some Title");
                GLCanvasFrame.canvasFrame.setDefaultCloseOperation(3);
                GLCanvasFrame.canvasFrame.showColor(Color.BLUE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public GLCanvasFrame(String title) {
        this(title, 0.0d);
    }

    public GLCanvasFrame(String title, double gamma) {
        super(title, gamma);
        this.params = new int[2];
        this.color = null;
        this.buffer = null;
        this.frameBuffer = 0;
        this.renderBuffer = 0;
        this.eventListener = new C12502();
        init(false, null, null);
    }

    public GLCanvasFrame(String title, GraphicsConfiguration gc, GLCapabilitiesImmutable caps, GLContext shareWith) {
        this(title, gc, caps, shareWith, 0.0d);
    }

    public GLCanvasFrame(String title, GraphicsConfiguration gc, GLCapabilitiesImmutable caps, GLContext shareWith, double gamma) {
        super(title, gc, gamma);
        this.params = new int[2];
        this.color = null;
        this.buffer = null;
        this.frameBuffer = 0;
        this.renderBuffer = 0;
        this.eventListener = new C12502();
        init(false, caps, shareWith);
    }

    public GLCanvasFrame(String title, int screenNumber, DisplayMode displayMode) throws Exception {
        this(title, screenNumber, displayMode, 0.0d);
    }

    public GLCanvasFrame(String title, int screenNumber, DisplayMode displayMode, double gamma) throws Exception {
        super(title, screenNumber, displayMode, gamma);
        this.params = new int[2];
        this.color = null;
        this.buffer = null;
        this.frameBuffer = 0;
        this.renderBuffer = 0;
        this.eventListener = new C12502();
        init(true, null, null);
    }

    public GLCanvasFrame(String title, int screenNumber, DisplayMode displayMode, GLCapabilitiesImmutable caps, GLContext shareWith) throws Exception {
        this(title, screenNumber, displayMode, caps, shareWith, 0.0d);
    }

    public GLCanvasFrame(String title, int screenNumber, DisplayMode displayMode, GLCapabilitiesImmutable caps, GLContext shareWith, double gamma) throws Exception {
        super(title, screenNumber, displayMode, gamma);
        this.params = new int[2];
        this.color = null;
        this.buffer = null;
        this.frameBuffer = 0;
        this.renderBuffer = 0;
        this.eventListener = new C12502();
        init(true, caps, shareWith);
    }

    private void init(final boolean fullScreen, final GLCapabilitiesImmutable caps, final GLContext shareWith) {
        Runnable r = new Runnable() {
            public void run() {
                String wasErase = System.setProperty("sun.awt.noerasebackground", PdfBoolean.TRUE);
                GLCanvasFrame.this.canvas = new GLCanvas(caps);
                if (shareWith != null) {
                    ((GLCanvas) GLCanvasFrame.this.canvas).setSharedContext(shareWith);
                }
                ((GLCanvas) GLCanvasFrame.this.canvas).addGLEventListener(GLCanvasFrame.this.eventListener);
                if (fullScreen) {
                    GLCanvasFrame.this.canvas.setSize(GLCanvasFrame.this.getSize());
                    GLCanvasFrame.this.needInitialResize = false;
                } else {
                    GLCanvasFrame.this.canvas.setSize(1, 1);
                    GLCanvasFrame.this.needInitialResize = true;
                }
                GLCanvasFrame.this.getContentPane().add(GLCanvasFrame.this.canvas);
                GLCanvasFrame.this.canvas.setVisible(true);
                if (wasErase != null) {
                    System.setProperty("sun.awt.noerasebackground", wasErase);
                } else {
                    System.clearProperty("sun.awt.noerasebackground");
                }
            }
        };
        if (EventQueue.isDispatchThread()) {
            r.run();
            return;
        }
        try {
            EventQueue.invokeAndWait(r);
        } catch (Exception e) {
        }
    }

    protected void initCanvas(boolean fullScreen, DisplayMode displayMode, double gamma) {
    }

    public GLCanvas getGLCanvas() {
        return (GLCanvas) this.canvas;
    }

    public void showColor(Color color) {
        this.color = color;
        this.buffer = null;
        getGLCanvas().display();
    }

    public void showImage(Frame frame) {
        showImage(frame, false);
    }

    public void showImage(Frame frame, boolean flipChannels) {
        if (flipChannels) {
            throw new RuntimeException("GLCanvasFrame does not support channel flipping.");
        } else if (frame != null) {
            this.color = null;
            this.width = frame.imageWidth;
            this.height = frame.imageHeight;
            this.buffer = frame.image[0];
            switch (frame.imageDepth) {
                case -32:
                    this.type = 5124;
                    break;
                case -16:
                    this.type = 5122;
                    break;
                case -8:
                    this.type = 5120;
                    break;
                case 8:
                    this.type = 5121;
                    break;
                case 16:
                    this.type = 5123;
                    break;
                case 32:
                    this.type = 5126;
                    break;
                case 64:
                    this.type = 5130;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            switch (frame.imageChannels) {
                case 1:
                    this.format = 6409;
                    break;
                case 2:
                    this.format = 33319;
                    break;
                case 3:
                    this.format = 6407;
                    break;
                case 4:
                    this.format = 6408;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            getGLCanvas().display();
        }
    }

    public void showImage(Image image) {
        if (image instanceof BufferedImage) {
            showImage((BufferedImage) image);
            return;
        }
        throw new RuntimeException("GLCanvasFrame does not support " + image + ", BufferedImage required.");
    }

    public void showImage(BufferedImage image) {
        if (image != null) {
            this.color = null;
            this.width = image.getWidth();
            this.height = image.getHeight();
            DataBuffer buffer = image.getRaster().getDataBuffer();
            if (buffer instanceof DataBufferByte) {
                this.buffer = ByteBuffer.wrap(((DataBufferByte) buffer).getData());
                this.type = 5121;
            } else if (buffer instanceof DataBufferDouble) {
                this.buffer = DoubleBuffer.wrap(((DataBufferDouble) buffer).getData());
                this.type = 5130;
            } else if (buffer instanceof DataBufferFloat) {
                this.buffer = FloatBuffer.wrap(((DataBufferFloat) buffer).getData());
                this.type = 5126;
            } else if (buffer instanceof DataBufferInt) {
                this.buffer = IntBuffer.wrap(((DataBufferInt) buffer).getData());
                this.type = 5124;
            } else if (buffer instanceof DataBufferShort) {
                this.buffer = ShortBuffer.wrap(((DataBufferShort) buffer).getData());
                this.type = 5122;
            } else if (buffer instanceof DataBufferUShort) {
                this.buffer = ShortBuffer.wrap(((DataBufferUShort) buffer).getData());
                this.type = 5123;
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            switch (image.getSampleModel().getNumBands()) {
                case 1:
                    this.format = 6409;
                    break;
                case 2:
                    this.format = 33319;
                    break;
                case 3:
                    this.format = 6407;
                    break;
                case 4:
                    this.format = 6408;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            getGLCanvas().display();
        }
    }

    public void showImage(int renderBuffer) {
        if (renderBuffer > 0) {
            this.color = null;
            this.buffer = null;
            this.renderBuffer = renderBuffer;
            getGLCanvas().display();
        }
    }

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(new C12513());
        JavaCVCL context = new JavaCVCL(canvasFrame.getGLCanvas().getContext());
        IplImage image = opencv_imgcodecs.cvLoadImageBGRA("/usr/share/opencv/samples/c/lena.jpg");
        CLGLImage2d imageCLGL = context.createCLGLImageFrom(image, new Mem[0]);
        context.acquireGLObject(imageCLGL);
        context.writeImage(imageCLGL, image, true);
        context.releaseGLObject(imageCLGL);
        canvasFrame.setCanvasScale(0.5d);
        for (int i = 0; i < 1000; i++) {
            canvasFrame.showImage(imageCLGL.getGLObjectID());
            Thread.sleep(10);
            canvasFrame.showColor(Color.RED);
            Thread.sleep(10);
        }
        canvasFrame.waitKey();
        context.release();
        System.exit(0);
    }
}
