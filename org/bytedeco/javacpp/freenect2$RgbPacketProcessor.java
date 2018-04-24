package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Opaque;

@Namespace("libfreenect2")
@Opaque
public class freenect2$RgbPacketProcessor extends Pointer {
    public freenect2$RgbPacketProcessor() {
        super((Pointer) null);
    }

    public freenect2$RgbPacketProcessor(Pointer p) {
        super(p);
    }
}
