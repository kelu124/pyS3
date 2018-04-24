package org.bytedeco.javacv;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_ProfileRGB;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class CanvasFrame extends JFrame {
    public static final long DEFAULT_LATENCY = 200;
    public static CanvasFrame global = null;
    private BufferedImage buffer;
    protected Canvas canvas;
    private Color color;
    private Java2DFrameConverter converter;
    private Image image;
    protected double initialScale;
    protected double inverseGamma;
    private KeyEvent keyEvent;
    private KeyEventDispatcher keyEventDispatch;
    private long latency;
    protected boolean needInitialResize;

    class C12422 extends Canvas {
        C12422() {
        }

        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            try {
                if (CanvasFrame.this.canvas.getWidth() > 0 && CanvasFrame.this.canvas.getHeight() > 0) {
                    BufferStrategy strategy = CanvasFrame.this.canvas.getBufferStrategy();
                    while (true) {
                        g = strategy.getDrawGraphics();
                        if (CanvasFrame.this.color != null) {
                            g.setColor(CanvasFrame.this.color);
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                        if (CanvasFrame.this.image != null) {
                            g.drawImage(CanvasFrame.this.image, 0, 0, getWidth(), getHeight(), null);
                        }
                        if (CanvasFrame.this.buffer != null) {
                            g.drawImage(CanvasFrame.this.buffer, 0, 0, getWidth(), getHeight(), null);
                        }
                        g.dispose();
                        if (!strategy.contentsRestored()) {
                            strategy.show();
                            if (!strategy.contentsLost()) {
                                return;
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
            } catch (IllegalStateException e2) {
            }
        }
    }

    class C12433 implements KeyEventDispatcher {
        C12433() {
        }

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == 401) {
                synchronized (CanvasFrame.this) {
                    CanvasFrame.this.keyEvent = e;
                    CanvasFrame.this.notify();
                }
            }
            return false;
        }
    }

    public static class Exception extends Exception {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String[] getScreenDescriptions() {
        GraphicsDevice[] screens = getScreenDevices();
        String[] descriptions = new String[screens.length];
        for (int i = 0; i < screens.length; i++) {
            descriptions[i] = screens[i].getIDstring();
        }
        return descriptions;
    }

    public static DisplayMode getDisplayMode(int screenNumber) {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber < 0 || screenNumber >= screens.length) {
            return null;
        }
        return screens[screenNumber].getDisplayMode();
    }

    public static double getGamma(int screenNumber) {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber < 0 || screenNumber >= screens.length) {
            return 0.0d;
        }
        return getGamma(screens[screenNumber]);
    }

    public static double getDefaultGamma() {
        return getGamma(getDefaultScreenDevice());
    }

    public static double getGamma(GraphicsDevice screen) {
        ColorSpace cs = screen.getDefaultConfiguration().getColorModel().getColorSpace();
        if (cs.isCS_sRGB()) {
            return 2.2d;
        }
        try {
            return (double) ((ICC_ProfileRGB) ((ICC_ColorSpace) cs).getProfile()).getGamma(0);
        } catch (RuntimeException e) {
            return 0.0d;
        }
    }

    public static GraphicsDevice getScreenDevice(int screenNumber) throws Exception {
        GraphicsDevice[] screens = getScreenDevices();
        if (screenNumber < screens.length) {
            return screens[screenNumber];
        }
        throw new Exception("CanvasFrame Error: Screen number " + screenNumber + " not found. There are only " + screens.length + " screens.");
    }

    public static GraphicsDevice[] getScreenDevices() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    }

    public static GraphicsDevice getDefaultScreenDevice() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public CanvasFrame(String title) {
        this(title, 0.0d);
    }

    public CanvasFrame(String title, double gamma) {
        super(title);
        this.latency = 200;
        this.keyEvent = null;
        this.keyEventDispatch = new C12433();
        this.canvas = null;
        this.needInitialResize = false;
        this.initialScale = 1.0d;
        this.inverseGamma = 1.0d;
        this.color = null;
        this.image = null;
        this.buffer = null;
        this.converter = new Java2DFrameConverter();
        init(false, null, gamma);
    }

    public CanvasFrame(String title, GraphicsConfiguration gc) {
        this(title, gc, 0.0d);
    }

    public CanvasFrame(String title, GraphicsConfiguration gc, double gamma) {
        super(title, gc);
        this.latency = 200;
        this.keyEvent = null;
        this.keyEventDispatch = new C12433();
        this.canvas = null;
        this.needInitialResize = false;
        this.initialScale = 1.0d;
        this.inverseGamma = 1.0d;
        this.color = null;
        this.image = null;
        this.buffer = null;
        this.converter = new Java2DFrameConverter();
        init(false, null, gamma);
    }

    public CanvasFrame(String title, int screenNumber, DisplayMode displayMode) throws Exception {
        this(title, screenNumber, displayMode, 0.0d);
    }

    public CanvasFrame(String title, int screenNumber, DisplayMode displayMode, double gamma) throws Exception {
        super(title, getScreenDevice(screenNumber).getDefaultConfiguration());
        this.latency = 200;
        this.keyEvent = null;
        this.keyEventDispatch = new C12433();
        this.canvas = null;
        this.needInitialResize = false;
        this.initialScale = 1.0d;
        this.inverseGamma = 1.0d;
        this.color = null;
        this.image = null;
        this.buffer = null;
        this.converter = new Java2DFrameConverter();
        init(true, displayMode, gamma);
    }

    private void init(boolean fullScreen, DisplayMode displayMode, double gamma) {
        final DisplayMode displayMode2 = displayMode;
        final boolean z = fullScreen;
        final double d = gamma;
        Runnable r = new Runnable() {
            public void run() {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(CanvasFrame.this.keyEventDispatch);
                GraphicsDevice gd = CanvasFrame.this.getGraphicsConfiguration().getDevice();
                DisplayMode d = gd.getDisplayMode();
                DisplayMode d2 = null;
                if (!(displayMode2 == null || d == null)) {
                    int w = displayMode2.getWidth();
                    int h = displayMode2.getHeight();
                    int b = displayMode2.getBitDepth();
                    int r = displayMode2.getRefreshRate();
                    if (w <= 0) {
                        w = d.getWidth();
                    }
                    if (h <= 0) {
                        h = d.getHeight();
                    }
                    if (b <= 0) {
                        b = d.getBitDepth();
                    }
                    if (r <= 0) {
                        r = d.getRefreshRate();
                    }
                    d2 = new DisplayMode(w, h, b, r);
                }
                if (z) {
                    CanvasFrame.this.setUndecorated(true);
                    CanvasFrame.this.getRootPane().setWindowDecorationStyle(0);
                    CanvasFrame.this.setResizable(false);
                    gd.setFullScreenWindow(CanvasFrame.this);
                } else {
                    CanvasFrame.this.setLocationByPlatform(true);
                }
                if (!(d2 == null || d2.equals(d))) {
                    gd.setDisplayMode(d2);
                }
                double g = d == 0.0d ? CanvasFrame.getGamma(gd) : d;
                CanvasFrame.this.inverseGamma = g == 0.0d ? 1.0d : 1.0d / g;
                CanvasFrame.this.setVisible(true);
                CanvasFrame.this.initCanvas(z, displayMode2, d);
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
        this.canvas = new C12422();
        if (fullScreen) {
            this.canvas.setSize(getSize());
            this.needInitialResize = false;
        } else {
            this.canvas.setSize(10, 10);
            this.needInitialResize = true;
        }
        getContentPane().add(this.canvas);
        this.canvas.setVisible(true);
        this.canvas.createBufferStrategy(2);
    }

    public long getLatency() {
        return this.latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public void waitLatency() throws InterruptedException {
        Thread.sleep(getLatency());
    }

    public KeyEvent waitKey() throws InterruptedException {
        return waitKey(0);
    }

    public synchronized KeyEvent waitKey(int delay) throws InterruptedException {
        KeyEvent e;
        if (delay >= 0) {
            this.keyEvent = null;
            wait((long) delay);
        }
        e = this.keyEvent;
        this.keyEvent = null;
        return e;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public Dimension getCanvasSize() {
        return this.canvas.getSize();
    }

    public void setCanvasSize(final int width, final int height) {
        Dimension d = getCanvasSize();
        if (d.width != width || d.height != height) {
            Runnable r = new Runnable() {
                public void run() {
                    CanvasFrame.this.setExtendedState(0);
                    CanvasFrame.this.canvas.setSize(width, height);
                    CanvasFrame.this.pack();
                    CanvasFrame.this.canvas.setSize(width + 1, height + 1);
                    CanvasFrame.this.canvas.setSize(width, height);
                    CanvasFrame.this.needInitialResize = false;
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
    }

    public double getCanvasScale() {
        return this.initialScale;
    }

    public void setCanvasScale(double initialScale) {
        this.initialScale = initialScale;
        this.needInitialResize = true;
    }

    public Graphics2D createGraphics() {
        if (!(this.buffer != null && this.buffer.getWidth() == this.canvas.getWidth() && this.buffer.getHeight() == this.canvas.getHeight())) {
            BufferedImage newbuffer = this.canvas.getGraphicsConfiguration().createCompatibleImage(this.canvas.getWidth(), this.canvas.getHeight(), 3);
            if (this.buffer != null) {
                Graphics g = newbuffer.getGraphics();
                g.drawImage(this.buffer, 0, 0, null);
                g.dispose();
            }
            this.buffer = newbuffer;
        }
        return this.buffer.createGraphics();
    }

    public void releaseGraphics(Graphics2D g) {
        g.dispose();
        this.canvas.paint(null);
    }

    public void showColor(Color color) {
        this.color = color;
        this.image = null;
        this.canvas.paint(null);
    }

    public void showImage(Frame image) {
        showImage(image, false);
    }

    public void showImage(Frame image, boolean flipChannels) {
        Java2DFrameConverter java2DFrameConverter = this.converter;
        Java2DFrameConverter java2DFrameConverter2 = this.converter;
        showImage(java2DFrameConverter.getBufferedImage(image, Java2DFrameConverter.getBufferedImageType(image) == 0 ? 1.0d : this.inverseGamma, flipChannels, null));
    }

    public void showImage(Image image) {
        if (image != null) {
            if (isResizable() && this.needInitialResize) {
                setCanvasSize((int) Math.round(((double) image.getWidth(null)) * this.initialScale), (int) Math.round(((double) image.getHeight(null)) * this.initialScale));
            }
            this.color = null;
            this.image = image;
            this.canvas.paint(null);
        }
    }

    public static void tile(final CanvasFrame[] frames) {
        final AnonymousClass1MovedListener movedListener = new ComponentAdapter() {
            boolean moved = false;

            public void componentMoved(ComponentEvent e) {
                this.moved = true;
                Component c = e.getComponent();
                synchronized (c) {
                    c.notify();
                }
            }
        };
        int canvasCols = (int) Math.round(Math.sqrt((double) frames.length));
        if (canvasCols * canvasCols < frames.length) {
            canvasCols++;
        }
        int canvasX = 0;
        int canvasY = 0;
        int canvasMaxY = 0;
        for (int i = 0; i < frames.length; i++) {
            final int n = i;
            final int x = canvasX;
            final int y = canvasY;
            try {
                movedListener.moved = false;
                final CanvasFrame[] canvasFrameArr = frames;
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        canvasFrameArr[n].addComponentListener(movedListener);
                        canvasFrameArr[n].setLocation(x, y);
                    }
                });
                int count = 0;
                while (!movedListener.moved && count < 5) {
                    synchronized (frames[n]) {
                        frames[n].wait(100);
                    }
                    count++;
                }
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        frames[n].removeComponentListener(movedListener);
                    }
                });
            } catch (Exception e) {
            }
            canvasX = frames[i].getX() + frames[i].getWidth();
            canvasMaxY = Math.max(canvasMaxY, frames[i].getY() + frames[i].getHeight());
            if ((i + 1) % canvasCols == 0) {
                canvasX = 0;
                canvasY = canvasMaxY;
            }
        }
    }
}
