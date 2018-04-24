package org.bytedeco.javacv;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avcodec.AVCodec;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avcodec.AVPicture;
import org.bytedeco.javacpp.avdevice;
import org.bytedeco.javacpp.avformat;
import org.bytedeco.javacpp.avformat$Read_packet_Pointer_BytePointer_int;
import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.avformat.AVIOContext;
import org.bytedeco.javacpp.avformat.AVInputFormat;
import org.bytedeco.javacpp.avformat.AVStream;
import org.bytedeco.javacpp.avformat.Seek_Pointer_long_int;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.avutil$AVRational;
import org.bytedeco.javacpp.avutil.AVDictionary;
import org.bytedeco.javacpp.avutil.AVDictionaryEntry;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.bytedeco.javacpp.swresample;
import org.bytedeco.javacpp.swscale;
import org.bytedeco.javacpp.swscale.SwsContext;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber.ImageMode;

public class FFmpegFrameGrabber extends FrameGrabber {
    static final /* synthetic */ boolean $assertionsDisabled = (!FFmpegFrameGrabber.class.desiredAssertionStatus());
    static HashMap<Pointer, InputStream> inputStreams = new HashMap();
    private static Exception loadingException = null;
    static ReadCallback readCallback;
    static SeekCallback seekCallback;
    private AVCodecContext audio_c;
    private AVStream audio_st;
    private AVIOContext avio;
    private String filename;
    private Frame frame;
    private boolean frameGrabbed;
    private int[] got_frame;
    private Buffer[] image_buf;
    private BytePointer[] image_ptr;
    private SwsContext img_convert_ctx;
    private InputStream inputStream;
    private AVFormatContext oc;
    private AVFrame picture;
    private AVFrame picture_rgb;
    private AVPacket pkt;
    private AVPacket pkt2;
    private Buffer[] samples_buf;
    private AVFrame samples_frame;
    private BytePointer[] samples_ptr;
    private int sizeof_pkt;
    private AVCodecContext video_c;
    private AVStream video_st;

    static class ReadCallback extends avformat$Read_packet_Pointer_BytePointer_int {
        ReadCallback() {
        }

        public int call(Pointer opaque, BytePointer buf, int buf_size) {
            try {
                byte[] b = new byte[buf_size];
                int size = ((InputStream) FFmpegFrameGrabber.inputStreams.get(opaque)).read(b, 0, buf_size);
                if (size < 0) {
                    return 0;
                }
                buf.put(b, 0, size);
                return size;
            } catch (Throwable t) {
                System.err.println("Error on InputStream.read(): " + t);
                return -1;
            }
        }
    }

    static class SeekCallback extends Seek_Pointer_long_int {
        SeekCallback() {
        }

        public long call(Pointer opaque, long offset, int whence) {
            try {
                InputStream is = (InputStream) FFmpegFrameGrabber.inputStreams.get(opaque);
                switch (whence) {
                    case 0:
                        is.reset();
                        break;
                    case 1:
                        break;
                    default:
                        return -1;
                }
                is.skip(offset);
                return 0;
            } catch (Throwable t) {
                System.err.println("Error on InputStream.reset() or skip(): " + t);
                return -1;
            }
        }
    }

    static {
        try {
            tryLoad();
        } catch (Exception e) {
        }
    }

    public static String[] getDeviceDescriptions() throws Exception {
        tryLoad();
        throw new UnsupportedOperationException("Device enumeration not support by FFmpeg.");
    }

    public static FFmpegFrameGrabber createDefault(File deviceFile) throws Exception {
        return new FFmpegFrameGrabber(deviceFile);
    }

    public static FFmpegFrameGrabber createDefault(String devicePath) throws Exception {
        return new FFmpegFrameGrabber(devicePath);
    }

    public static FFmpegFrameGrabber createDefault(int deviceNumber) throws Exception {
        throw new Exception(FFmpegFrameGrabber.class + " does not support device numbers.");
    }

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        }
        try {
            Loader.load(avutil.class);
            Loader.load(swresample.class);
            Loader.load(avcodec.class);
            Loader.load(avformat.class);
            Loader.load(swscale.class);
            avcodec.avcodec_register_all();
            avformat.av_register_all();
            avformat.avformat_network_init();
            Loader.load(avdevice.class);
            avdevice.avdevice_register_all();
        } catch (Throwable t) {
            if (t instanceof Exception) {
                loadingException = (Exception) t;
            } else {
                loadingException = new Exception("Failed to load " + FFmpegFrameGrabber.class, t);
            }
        }
    }

    public FFmpegFrameGrabber(File file) {
        this(file.getAbsolutePath());
    }

    public FFmpegFrameGrabber(String filename) {
        this.filename = filename;
    }

    public FFmpegFrameGrabber(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void release() throws Exception {
        synchronized (avcodec.class) {
            releaseUnsafe();
        }
    }

    void releaseUnsafe() throws Exception {
        if (!(this.pkt == null || this.pkt2 == null)) {
            if (this.pkt2.size() > 0) {
                avcodec.av_free_packet(this.pkt);
            }
            this.pkt2 = null;
            this.pkt = null;
        }
        if (this.image_ptr != null) {
            for (Pointer av_free : this.image_ptr) {
                avutil.av_free(av_free);
            }
            this.image_ptr = null;
        }
        if (this.picture_rgb != null) {
            avutil.av_frame_free(this.picture_rgb);
            this.picture_rgb = null;
        }
        if (this.picture != null) {
            avutil.av_frame_free(this.picture);
            this.picture = null;
        }
        if (this.video_c != null) {
            avcodec.avcodec_close(this.video_c);
            this.video_c = null;
        }
        if (this.samples_frame != null) {
            avutil.av_frame_free(this.samples_frame);
            this.samples_frame = null;
        }
        if (this.audio_c != null) {
            avcodec.avcodec_close(this.audio_c);
            this.audio_c = null;
        }
        if (!(this.inputStream != null || this.oc == null || this.oc.isNull())) {
            avformat.avformat_close_input(this.oc);
            this.oc = null;
        }
        if (this.img_convert_ctx != null) {
            swscale.sws_freeContext(this.img_convert_ctx);
            this.img_convert_ctx = null;
        }
        this.got_frame = null;
        this.frameGrabbed = false;
        this.frame = null;
        this.timestamp = 0;
        this.frameNumber = 0;
        this.filename = null;
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
                this.inputStream = null;
                inputStreams.remove(this.oc);
                if (this.avio != null) {
                    if (this.avio.buffer() != null) {
                        avutil.av_free(this.avio.buffer());
                        this.avio.buffer(null);
                    }
                    avutil.av_free(this.avio);
                    this.avio = null;
                }
                if (this.oc != null) {
                    avformat.avformat_free_context(this.oc);
                    this.oc = null;
                }
            } catch (IOException ex) {
                throw new Exception("Error on InputStream.close(): ", ex);
            } catch (Throwable th) {
                this.inputStream = null;
                inputStreams.remove(this.oc);
                if (this.avio != null) {
                    if (this.avio.buffer() != null) {
                        avutil.av_free(this.avio.buffer());
                        this.avio.buffer(null);
                    }
                    avutil.av_free(this.avio);
                    this.avio = null;
                }
                if (this.oc != null) {
                    avformat.avformat_free_context(this.oc);
                    this.oc = null;
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public double getGamma() {
        if (this.gamma == 0.0d) {
            return 2.2d;
        }
        return this.gamma;
    }

    public String getFormat() {
        if (this.oc == null) {
            return super.getFormat();
        }
        return this.oc.iformat().name().getString();
    }

    public int getImageWidth() {
        return (this.imageWidth > 0 || this.video_c == null) ? super.getImageWidth() : this.video_c.width();
    }

    public int getImageHeight() {
        return (this.imageHeight > 0 || this.video_c == null) ? super.getImageHeight() : this.video_c.height();
    }

    public int getAudioChannels() {
        return this.audio_c == null ? super.getAudioChannels() : this.audio_c.channels();
    }

    public int getPixelFormat() {
        if (this.imageMode == ImageMode.COLOR || this.imageMode == ImageMode.GRAY) {
            if (this.pixelFormat == -1) {
                return this.imageMode == ImageMode.COLOR ? 3 : 8;
            } else {
                return this.pixelFormat;
            }
        } else if (this.video_c != null) {
            return this.video_c.pix_fmt();
        } else {
            return super.getPixelFormat();
        }
    }

    public int getVideoCodec() {
        return this.video_c == null ? super.getVideoCodec() : this.video_c.codec_id();
    }

    public int getVideoBitrate() {
        return this.video_c == null ? super.getVideoBitrate() : (int) this.video_c.bit_rate();
    }

    public double getAspectRatio() {
        if (this.video_st == null) {
            return super.getAspectRatio();
        }
        avutil$AVRational r = avformat.av_guess_sample_aspect_ratio(this.oc, this.video_st, this.picture);
        double a = ((double) r.num()) / ((double) r.den());
        return a == 0.0d ? 1.0d : a;
    }

    public double getFrameRate() {
        if (this.video_st == null) {
            return super.getFrameRate();
        }
        avutil$AVRational r = this.video_st.avg_frame_rate();
        if (r.num() == 0 && r.den() == 0) {
            r = this.video_st.r_frame_rate();
        }
        return ((double) r.num()) / ((double) r.den());
    }

    public int getAudioCodec() {
        return this.audio_c == null ? super.getAudioCodec() : this.audio_c.codec_id();
    }

    public int getAudioBitrate() {
        return this.audio_c == null ? super.getAudioBitrate() : (int) this.audio_c.bit_rate();
    }

    public int getSampleFormat() {
        return this.audio_c == null ? super.getSampleFormat() : this.audio_c.sample_fmt();
    }

    public int getSampleRate() {
        return this.audio_c == null ? super.getSampleRate() : this.audio_c.sample_rate();
    }

    public String getMetadata(String key) {
        if (this.oc == null) {
            return super.getMetadata(key);
        }
        AVDictionaryEntry entry = avutil.av_dict_get(this.oc.metadata(), key, null, 0);
        if (entry == null || entry.value() == null) {
            return null;
        }
        return entry.value().getString();
    }

    public String getVideoMetadata(String key) {
        if (this.video_st == null) {
            return super.getVideoMetadata(key);
        }
        AVDictionaryEntry entry = avutil.av_dict_get(this.video_st.metadata(), key, null, 0);
        if (entry == null || entry.value() == null) {
            return null;
        }
        return entry.value().getString();
    }

    public String getAudioMetadata(String key) {
        if (this.audio_st == null) {
            return super.getAudioMetadata(key);
        }
        AVDictionaryEntry entry = avutil.av_dict_get(this.audio_st.metadata(), key, null, 0);
        if (entry == null || entry.value() == null) {
            return null;
        }
        return entry.value().getString();
    }

    public void setFrameNumber(int frameNumber) throws Exception {
        setTimestamp(Math.round(((double) (1000000 * ((long) frameNumber))) / getFrameRate()));
    }

    public void setTimestamp(long timestamp) throws Exception {
        if (this.oc == null) {
            super.setTimestamp(timestamp);
            return;
        }
        timestamp = (1000000 * timestamp) / 1000000;
        if (this.oc.start_time() != avutil.AV_NOPTS_VALUE) {
            timestamp += this.oc.start_time();
        }
        int ret = avformat.avformat_seek_file(this.oc, -1, Long.MIN_VALUE, timestamp, Long.MAX_VALUE, 1);
        if (ret < 0) {
            throw new Exception("avformat_seek_file() error " + ret + ": Could not seek file to timestamp " + timestamp + ".");
        }
        if (this.video_c != null) {
            avcodec.avcodec_flush_buffers(this.video_c);
        }
        if (this.audio_c != null) {
            avcodec.avcodec_flush_buffers(this.audio_c);
        }
        if (this.pkt2.size() > 0) {
            this.pkt2.size(0);
            avcodec.av_free_packet(this.pkt);
        }
        while (this.timestamp > 1 + timestamp) {
            if (grabFrame(false, true, false, false) == null) {
                break;
            }
        }
        while (this.timestamp < timestamp - 1) {
            if (grabFrame(false, true, false, false) == null) {
                break;
            }
        }
        if (this.video_c != null) {
            this.frameGrabbed = true;
        }
    }

    public int getLengthInFrames() {
        return (int) ((((double) getLengthInTime()) * getFrameRate()) / 1000000.0d);
    }

    public long getLengthInTime() {
        return (this.oc.duration() * 1000000) / 1000000;
    }

    public AVFormatContext getFormatContext() {
        return this.oc;
    }

    public void start() throws Exception {
        synchronized (avcodec.class) {
            startUnsafe();
        }
    }

    void startUnsafe() throws Exception {
        int ret;
        this.img_convert_ctx = null;
        this.oc = new AVFormatContext(null);
        this.video_c = null;
        this.audio_c = null;
        this.pkt = new AVPacket();
        this.pkt2 = new AVPacket();
        this.sizeof_pkt = this.pkt.sizeof();
        this.got_frame = new int[1];
        this.frameGrabbed = false;
        this.frame = new Frame();
        this.timestamp = 0;
        this.frameNumber = 0;
        this.pkt2.size(0);
        AVInputFormat f = null;
        if (this.format != null && this.format.length() > 0) {
            f = avformat.av_find_input_format(this.format);
            if (f == null) {
                throw new Exception("av_find_input_format() error: Could not find input format \"" + this.format + "\".");
            }
        }
        AVDictionary aVDictionary = new AVDictionary(null);
        if (this.frameRate > 0.0d) {
            avutil$AVRational r = avutil.av_d2q(this.frameRate, 1001000);
            avutil.av_dict_set(aVDictionary, "framerate", r.num() + "/" + r.den(), 0);
        }
        if (this.pixelFormat >= 0) {
            avutil.av_dict_set(aVDictionary, "pixel_format", avutil.av_get_pix_fmt_name(this.pixelFormat).getString(), 0);
        } else if (this.imageMode != ImageMode.RAW) {
            avutil.av_dict_set(aVDictionary, "pixel_format", this.imageMode == ImageMode.COLOR ? "bgr24" : "gray8", 0);
        }
        if (this.imageWidth > 0 && this.imageHeight > 0) {
            avutil.av_dict_set(aVDictionary, "video_size", this.imageWidth + "x" + this.imageHeight, 0);
        }
        if (this.sampleRate > 0) {
            avutil.av_dict_set(aVDictionary, "sample_rate", "" + this.sampleRate, 0);
        }
        if (this.audioChannels > 0) {
            avutil.av_dict_set(aVDictionary, "channels", "" + this.audioChannels, 0);
        }
        for (Entry<String, String> e : this.options.entrySet()) {
            avutil.av_dict_set(aVDictionary, (String) e.getKey(), (String) e.getValue(), 0);
        }
        if (this.inputStream != null) {
            if (readCallback == null) {
                readCallback = new ReadCallback();
            }
            if (seekCallback == null) {
                seekCallback = new SeekCallback();
            }
            if (!this.inputStream.markSupported()) {
                this.inputStream = new BufferedInputStream(this.inputStream, 1048576);
            }
            this.inputStream.mark(1048576);
            this.oc = avformat.avformat_alloc_context();
            this.avio = avformat.avio_alloc_context(new BytePointer(avutil.av_malloc(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM)), 4096, 0, this.oc, readCallback, null, seekCallback);
            this.oc.pb(this.avio);
            this.filename = this.inputStream.toString();
            inputStreams.put(this.oc, this.inputStream);
        }
        if (avformat.avformat_open_input(this.oc, this.filename, f, aVDictionary) < 0) {
            avutil.av_dict_set(aVDictionary, "pixel_format", null, 0);
            ret = avformat.avformat_open_input(this.oc, this.filename, f, aVDictionary);
            if (ret < 0) {
                throw new Exception("avformat_open_input() error " + ret + ": Could not open input \"" + this.filename + "\". (Has setFormat() been called?)");
            }
        }
        avutil.av_dict_free(aVDictionary);
        ret = avformat.avformat_find_stream_info(this.oc, (PointerPointer) null);
        if (ret < 0) {
            throw new Exception("avformat_find_stream_info() error " + ret + ": Could not find stream information.");
        }
        avformat.av_dump_format(this.oc, 0, this.filename, 0);
        this.audio_st = null;
        this.video_st = null;
        int nb_streams = this.oc.nb_streams();
        int i = 0;
        while (i < nb_streams) {
            AVStream st = this.oc.streams(i);
            AVCodecContext c = st.codec();
            if (this.video_st == null && c.codec_type() == 0 && (this.videoStream < 0 || this.videoStream == i)) {
                this.video_st = st;
                this.video_c = c;
            } else if (this.audio_st == null && c.codec_type() == 1 && (this.audioStream < 0 || this.audioStream == i)) {
                this.audio_st = st;
                this.audio_c = c;
            }
            i++;
        }
        if (this.video_st == null && this.audio_st == null) {
            throw new Exception("Did not find a video or audio stream inside \"" + this.filename + "\" for videoStream == " + this.videoStream + " and audioStream == " + this.audioStream + ".");
        }
        AVCodec codec;
        AVFrame av_frame_alloc;
        if (this.video_st != null) {
            codec = avcodec.avcodec_find_decoder(this.video_c.codec_id());
            if (codec != null) {
                aVDictionary = new AVDictionary(null);
                for (Entry<String, String> e2 : this.videoOptions.entrySet()) {
                    avutil.av_dict_set(aVDictionary, (String) e2.getKey(), (String) e2.getValue(), 0);
                }
                ret = avcodec.avcodec_open2(this.video_c, codec, aVDictionary);
                if (ret >= 0) {
                    avutil.av_dict_free(aVDictionary);
                    if (this.video_c.time_base().num() > 1000 && this.video_c.time_base().den() == 1) {
                        this.video_c.time_base().den(1000);
                    }
                    av_frame_alloc = avutil.av_frame_alloc();
                    this.picture = av_frame_alloc;
                    if (av_frame_alloc != null) {
                        av_frame_alloc = avutil.av_frame_alloc();
                        this.picture_rgb = av_frame_alloc;
                        if (av_frame_alloc != null) {
                            int width = this.imageWidth > 0 ? this.imageWidth : this.video_c.width();
                            int height = this.imageHeight > 0 ? this.imageHeight : this.video_c.height();
                            switch (this.imageMode) {
                                case COLOR:
                                case GRAY:
                                    int fmt = getPixelFormat();
                                    int size = avcodec.avpicture_get_size(fmt, width, height);
                                    this.image_ptr = new BytePointer[]{new BytePointer(avutil.av_malloc((long) size)).capacity((long) size)};
                                    this.image_buf = new Buffer[]{this.image_ptr[0].asBuffer()};
                                    avcodec.avpicture_fill(new AVPicture(this.picture_rgb), this.image_ptr[0], fmt, width, height);
                                    this.picture_rgb.format(fmt);
                                    this.picture_rgb.width(width);
                                    this.picture_rgb.height(height);
                                    break;
                                case RAW:
                                    this.image_ptr = new BytePointer[]{null};
                                    this.image_buf = new Buffer[]{null};
                                    break;
                                default:
                                    if (!$assertionsDisabled) {
                                        throw new AssertionError();
                                    }
                                    break;
                            }
                        }
                        throw new Exception("av_frame_alloc() error: Could not allocate RGB picture frame.");
                    }
                    throw new Exception("av_frame_alloc() error: Could not allocate raw picture frame.");
                }
                throw new Exception("avcodec_open2() error " + ret + ": Could not open video codec.");
            }
            throw new Exception("avcodec_find_decoder() error: Unsupported video format or codec not found: " + this.video_c.codec_id() + ".");
        }
        if (this.audio_st != null) {
            codec = avcodec.avcodec_find_decoder(this.audio_c.codec_id());
            if (codec == null) {
                throw new Exception("avcodec_find_decoder() error: Unsupported audio format or codec not found: " + this.audio_c.codec_id() + ".");
            }
            aVDictionary = new AVDictionary(null);
            for (Entry<String, String> e22 : this.audioOptions.entrySet()) {
                avutil.av_dict_set(aVDictionary, (String) e22.getKey(), (String) e22.getValue(), 0);
            }
            ret = avcodec.avcodec_open2(this.audio_c, codec, aVDictionary);
            if (ret < 0) {
                throw new Exception("avcodec_open2() error " + ret + ": Could not open audio codec.");
            }
            avutil.av_dict_free(aVDictionary);
            av_frame_alloc = avutil.av_frame_alloc();
            this.samples_frame = av_frame_alloc;
            if (av_frame_alloc == null) {
                throw new Exception("av_frame_alloc() error: Could not allocate audio frame.");
            }
        }
    }

    public void stop() throws Exception {
        release();
    }

    public void trigger() throws Exception {
        if (this.oc == null || this.oc.isNull()) {
            throw new Exception("Could not trigger: No AVFormatContext. (Has start() been called?)");
        }
        if (this.pkt2.size() > 0) {
            this.pkt2.size(0);
            avcodec.av_free_packet(this.pkt);
        }
        for (int i = 0; i < this.numBuffers + 1 && avformat.av_read_frame(this.oc, this.pkt) >= 0; i++) {
            avcodec.av_free_packet(this.pkt);
        }
    }

    private void processImage() throws Exception {
        this.frame.imageWidth = this.imageWidth > 0 ? this.imageWidth : this.video_c.width();
        this.frame.imageHeight = this.imageHeight > 0 ? this.imageHeight : this.video_c.height();
        this.frame.imageDepth = 8;
        switch (this.imageMode) {
            case COLOR:
            case GRAY:
                if (!this.deinterlace) {
                    this.img_convert_ctx = swscale.sws_getCachedContext(this.img_convert_ctx, this.video_c.width(), this.video_c.height(), this.video_c.pix_fmt(), this.frame.imageWidth, this.frame.imageHeight, getPixelFormat(), 2, null, null, (DoublePointer) null);
                    if (this.img_convert_ctx != null) {
                        swscale.sws_scale(this.img_convert_ctx, new PointerPointer(this.picture), this.picture.linesize(), 0, this.video_c.height(), new PointerPointer(this.picture_rgb), this.picture_rgb.linesize());
                        this.frame.imageStride = this.picture_rgb.linesize(0);
                        this.frame.image = this.image_buf;
                        break;
                    }
                    throw new Exception("sws_getCachedContext() error: Cannot initialize the conversion context.");
                }
                throw new Exception("Cannot deinterlace: Functionality moved to FFmpegFrameFilter.");
            case RAW:
                this.frame.imageStride = this.picture.linesize(0);
                BytePointer ptr = this.picture.data(0);
                if (!(ptr == null || ptr.equals(this.image_ptr[0]))) {
                    this.image_ptr[0] = ptr.capacity((long) (this.frame.imageHeight * this.frame.imageStride));
                    this.image_buf[0] = ptr.asBuffer();
                }
                this.frame.image = this.image_buf;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        this.frame.image[0].limit(this.frame.imageHeight * this.frame.imageStride);
        this.frame.imageChannels = this.frame.imageStride / this.frame.imageWidth;
    }

    public Frame grab() throws Exception {
        return grabFrame(true, true, true, false);
    }

    public Frame grabImage() throws Exception {
        return grabFrame(false, true, true, false);
    }

    public Frame grabSamples() throws Exception {
        return grabFrame(true, false, true, false);
    }

    public Frame grabKeyFrame() throws Exception {
        return grabFrame(false, true, true, true);
    }

    public Frame grabFrame(boolean doAudio, boolean doVideo, boolean processImage, boolean keyFrames) throws Exception {
        if (this.oc == null || this.oc.isNull()) {
            throw new Exception("Could not grab: No AVFormatContext. (Has start() been called?)");
        } else if ((!doVideo || this.video_st == null) && (!doAudio || this.audio_st == null)) {
            return null;
        } else {
            this.frame.keyFrame = false;
            this.frame.imageWidth = 0;
            this.frame.imageHeight = 0;
            this.frame.imageDepth = 0;
            this.frame.imageChannels = 0;
            this.frame.imageStride = 0;
            this.frame.image = null;
            this.frame.sampleRate = 0;
            this.frame.audioChannels = 0;
            this.frame.samples = null;
            this.frame.opaque = null;
            if (doVideo && this.frameGrabbed) {
                this.frameGrabbed = false;
                if (processImage) {
                    processImage();
                }
                this.frame.keyFrame = this.picture.key_frame() != 0;
                this.frame.image = this.image_buf;
                this.frame.opaque = this.picture;
                return this.frame;
            }
            boolean done = false;
            while (!done) {
                if (this.pkt2.size() <= 0 && avformat.av_read_frame(this.oc, this.pkt) < 0) {
                    if (!doVideo || this.video_st == null) {
                        return null;
                    }
                    this.pkt.stream_index(this.video_st.index());
                    this.pkt.flags(1);
                    this.pkt.data(null);
                    this.pkt.size(0);
                }
                long pts;
                avutil$AVRational time_base;
                if (!doVideo || this.video_st == null || this.pkt.stream_index() != this.video_st.index() || (keyFrames && this.pkt.flags() != 1)) {
                    if (doAudio && this.audio_st != null && this.pkt.stream_index() == this.audio_st.index()) {
                        if (this.pkt2.size() <= 0) {
                            BytePointer.memcpy(this.pkt2, this.pkt, (long) this.sizeof_pkt);
                        }
                        avutil.av_frame_unref(this.samples_frame);
                        int len = avcodec.avcodec_decode_audio4(this.audio_c, this.samples_frame, this.got_frame, this.pkt2);
                        if (len <= 0) {
                            this.pkt2.size(0);
                        } else {
                            this.pkt2.data(this.pkt2.data().position((long) len));
                            this.pkt2.size(this.pkt2.size() - len);
                            if (this.got_frame[0] != 0) {
                                pts = avutil.av_frame_get_best_effort_timestamp(this.samples_frame);
                                time_base = this.audio_st.time_base();
                                this.timestamp = ((1000000 * pts) * ((long) time_base.num())) / ((long) time_base.den());
                                done = true;
                                int sample_format = this.samples_frame.format();
                                int planes = avutil.av_sample_fmt_is_planar(sample_format) != 0 ? this.samples_frame.channels() : 1;
                                int data_size = avutil.av_samples_get_buffer_size((IntPointer) null, this.audio_c.channels(), this.samples_frame.nb_samples(), this.audio_c.sample_fmt(), 1) / planes;
                                if (this.samples_buf == null || this.samples_buf.length != planes) {
                                    this.samples_ptr = new BytePointer[planes];
                                    this.samples_buf = new Buffer[planes];
                                }
                                this.frame.keyFrame = this.samples_frame.key_frame() != 0;
                                this.frame.sampleRate = this.audio_c.sample_rate();
                                this.frame.audioChannels = this.audio_c.channels();
                                this.frame.samples = this.samples_buf;
                                this.frame.opaque = this.samples_frame;
                                int sample_size = data_size / avutil.av_get_bytes_per_sample(sample_format);
                                int i = 0;
                                while (i < planes) {
                                    BytePointer p = this.samples_frame.data(i);
                                    if (!p.equals(this.samples_ptr[i]) || this.samples_ptr[i].capacity() < ((long) data_size)) {
                                        this.samples_ptr[i] = p.capacity((long) data_size);
                                        ByteBuffer b = p.asBuffer();
                                        switch (sample_format) {
                                            case 0:
                                            case 5:
                                                this.samples_buf[i] = b;
                                                break;
                                            case 1:
                                            case 6:
                                                this.samples_buf[i] = b.asShortBuffer();
                                                break;
                                            case 2:
                                            case 7:
                                                this.samples_buf[i] = b.asIntBuffer();
                                                break;
                                            case 3:
                                            case 8:
                                                this.samples_buf[i] = b.asFloatBuffer();
                                                break;
                                            case 4:
                                            case 9:
                                                this.samples_buf[i] = b.asDoubleBuffer();
                                                break;
                                            default:
                                                if ($assertionsDisabled) {
                                                    break;
                                                }
                                                throw new AssertionError();
                                        }
                                    }
                                    this.samples_buf[i].position(0).limit(sample_size);
                                    i++;
                                }
                            }
                        }
                    }
                } else if (avcodec.avcodec_decode_video2(this.video_c, this.picture, this.got_frame, this.pkt) >= 0 && this.got_frame[0] != 0 && (!keyFrames || this.picture.pict_type() == 1)) {
                    pts = avutil.av_frame_get_best_effort_timestamp(this.picture);
                    time_base = this.video_st.time_base();
                    this.timestamp = ((1000000 * pts) * ((long) time_base.num())) / ((long) time_base.den());
                    this.frameNumber = (int) ((((double) this.timestamp) * getFrameRate()) / 1000000.0d);
                    if (processImage) {
                        processImage();
                    }
                    done = true;
                    this.frame.keyFrame = this.picture.key_frame() != 0;
                    this.frame.image = this.image_buf;
                    this.frame.opaque = this.picture;
                } else if (this.pkt.data() == null && this.pkt.size() == 0) {
                    return null;
                }
                if (this.pkt2.size() <= 0) {
                    avcodec.av_free_packet(this.pkt);
                }
            }
            return this.frame;
        }
    }

    public AVPacket grabPacket() throws Exception {
        if (this.oc == null || this.oc.isNull()) {
            throw new Exception("Could not trigger: No AVFormatContext. (Has start() been called?)");
        } else if (avformat.av_read_frame(this.oc, this.pkt) < 0) {
            return null;
        } else {
            return this.pkt;
        }
    }
}
