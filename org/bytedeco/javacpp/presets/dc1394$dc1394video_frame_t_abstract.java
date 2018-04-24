package org.bytedeco.javacpp.presets;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

public abstract class dc1394$dc1394video_frame_t_abstract extends Pointer {
    public abstract BytePointer image();

    public abstract long total_bytes();

    public dc1394$dc1394video_frame_t_abstract(Pointer p) {
        super(p);
    }

    public ByteBuffer getByteBuffer() {
        return image().capacity((long) ((int) total_bytes())).asByteBuffer();
    }
}
