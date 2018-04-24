package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Opaque;

@Namespace("libfreenect2")
@Opaque
public class freenect2$DataCallback extends Pointer {
    public freenect2$DataCallback() {
        super((Pointer) null);
    }

    public freenect2$DataCallback(Pointer p) {
        super(p);
    }
}
