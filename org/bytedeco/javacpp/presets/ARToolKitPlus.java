package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.ARToolKitPlus", value = {@Platform(define = {"AR_STATIC"}, include = {"ARToolKitPlus_plus.h", "<ARToolKitPlus/ARToolKitPlus.h>", "<ARToolKitPlus/config.h>", "<ARToolKitPlus/ar.h>", "<ARToolKitPlus/arMulti.h>", "<ARToolKitPlus/matrix.h>", "<ARToolKitPlus/vector.h>", "<ARToolKitPlus/Camera.h>", "<ARToolKitPlus/extra/BCH.h>", "<ARToolKitPlus/extra/Hull.h>", "<ARToolKitPlus/extra/rpp.h>", "<ARToolKitPlus/Tracker.h>", "<ARToolKitPlus/TrackerMultiMarker.h>", "<ARToolKitPlus/TrackerSingleMarker.h>", "<ARToolKitPlus/arBitFieldPattern.h>", "<ARToolKitPlus/arGetInitRot2Sub.h>"}, link = {"ARToolKitPlus"}), @Platform(includepath = {"C:/Program Files (x86)/ARToolKitPlus/include/"}, linkpath = {"C:/Program Files (x86)/ARToolKitPlus/lib/"}, value = {"windows-x86"}), @Platform(includepath = {"C:/Program Files/ARToolKitPlus/include/"}, linkpath = {"C:/Program Files/ARToolKitPlus/lib/"}, value = {"windows-x86_64"})})
public class ARToolKitPlus implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("AR_EXPORT").cppTypes(new String[0]).annotations(new String[0])).put(new Info("defined(_MSC_VER) || defined(_WIN32_WCE)").define(false)).put(new Info("ARToolKitPlus::IDPATTERN").cast().valueTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]")).put(new Info("ARFloat").cast().valueTypes("float").pointerTypes("FloatPointer", "FloatBuffer", "float[]")).put(new Info("ARToolKitPlus::_64bits").cast().valueTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]")).put(new Info("rpp_vec").cast().valueTypes("DoublePointer").pointerTypes("PointerPointer")).put(new Info("rpp_mat").valueTypes("@Cast(\"double(*)[3]\") DoublePointer").pointerTypes("@Cast(\"double(*)[3][3]\") PointerPointer"));
    }
}
