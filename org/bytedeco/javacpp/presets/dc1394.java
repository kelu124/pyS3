package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.dc1394", value = {@Platform(include = {"<poll.h>", "<dc1394/dc1394.h>", "<dc1394/types.h>", "<dc1394/log.h>", "<dc1394/camera.h>", "<dc1394/control.h>", "<dc1394/capture.h>", "<dc1394/conversions.h>", "<dc1394/format7.h>", "<dc1394/iso.h>", "<dc1394/register.h>", "<dc1394/video.h>", "<dc1394/utils.h>"}, link = {"dc1394@.22"}, not = {"android"}, preload = {"usb-1.0"}, preloadpath = {"/usr/local/lib/"}), @Platform(include = {"<dc1394/dc1394.h>", "<dc1394/types.h>", "<dc1394/log.h>", "<dc1394/camera.h>", "<dc1394/control.h>", "<dc1394/capture.h>", "<dc1394/conversions.h>", "<dc1394/format7.h>", "<dc1394/iso.h>", "<dc1394/register.h>", "<dc1394/video.h>", "<dc1394/utils.h>"}, preload = {"libdc1394-22", "libusb-1.0"}, value = {"windows"})})
public class dc1394 implements InfoMapper {
    public static final short POLLERR = (short) 8;
    public static final short POLLHUP = (short) 16;
    public static final short POLLIN = (short) 1;
    public static final short POLLMSG = (short) 1024;
    public static final short POLLNVAL = (short) 32;
    public static final short POLLOUT = (short) 4;
    public static final short POLLPRI = (short) 2;
    public static final short POLLRDHUP = (short) 8192;
    public static final short POLLREMOVE = (short) 4096;

    @Platform(not = {"windows"})
    public static class pollfd extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native pollfd events(short s);

        public native short events();

        public native int fd();

        public native pollfd fd(int i);

        public native pollfd revents(short s);

        public native short revents();

        static {
            Loader.load();
        }

        public pollfd() {
            allocate();
        }

        public pollfd(long size) {
            allocateArray(size);
        }

        public pollfd(Pointer p) {
            super(p);
        }

        public pollfd position(long position) {
            return (pollfd) super.position(position);
        }
    }

    @Platform(not = {"windows"})
    public static native int poll(pollfd org_bytedeco_javacpp_presets_dc1394_pollfd, @Cast({"nfds_t"}) long j, int i);

    public void map(InfoMap infoMap) {
        infoMap.put(new Info("poll.h").skip()).put(new Info("restrict").cppTypes(new String[0])).put(new Info("YUV2RGB", "RGB2YUV").cppTypes("void", "int", "int", "int", "int&", "int&", "int&")).put(new Info("dc1394video_frame_t").base("dc1394video_frame_t_abstract"));
    }
}
