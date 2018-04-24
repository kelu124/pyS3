package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Opaque;

@Namespace("libfreenect2")
@Opaque
public class freenect2$DepthPacketProcessor extends Pointer {
    public freenect2$DepthPacketProcessor() {
        super((Pointer) null);
    }

    public freenect2$DepthPacketProcessor(Pointer p) {
        super(p);
    }
}
