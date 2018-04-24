package org.bytedeco.javacv;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.avutil$LogCallback;
import org.bytedeco.javacpp.tools.Logger;

public class FFmpegLogCallback extends avutil$LogCallback {
    static final /* synthetic */ boolean $assertionsDisabled = (!FFmpegLogCallback.class.desiredAssertionStatus());
    static final FFmpegLogCallback instance = new FFmpegLogCallback();
    private static final Logger logger = Logger.create(FFmpegLogCallback.class);

    public static FFmpegLogCallback getInstance() {
        return instance;
    }

    public static void set() {
        avutil.setLogCallback(getInstance());
    }

    public void call(int level, BytePointer msg) {
        switch (level) {
            case 0:
            case 8:
            case 16:
                logger.error(msg.getString());
                return;
            case 24:
                logger.warn(msg.getString());
                return;
            case 32:
                logger.info(msg.getString());
                break;
            case 40:
            case 48:
            case 56:
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
        logger.debug(msg.getString());
    }
}
