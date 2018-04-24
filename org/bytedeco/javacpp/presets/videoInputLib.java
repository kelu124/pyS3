package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.videoInputLib", value = {@Platform(include = {"<videoInput.h>", "<videoInput.cpp>"}, link = {"ole32", "oleaut32", "amstrmid", "strmiids", "uuid"})})
public class videoInputLib implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("videoInput.cpp").skip()).put(new Info("_WIN32_WINNT").cppTypes(new String[0]).define(false)).put(new Info("std::vector<std::string>").pointerTypes("StringVector").define()).put(new Info("GUID").cast().pointerTypes("Pointer")).put(new Info("long", "unsigned long").cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
    }
}
