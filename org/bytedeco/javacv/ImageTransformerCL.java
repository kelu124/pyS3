package org.bytedeco.javacv;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLImage2d;
import com.jogamp.opencl.CLMemory.Mem;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.bytedeco.javacv.ImageTransformer.Parameters;

public interface ImageTransformerCL extends ImageTransformer {

    public static class InputData {
        boolean autoWrite;
        CLBuffer<ByteBuffer> buffer;
        public double outlierThreshold;
        public int pyramidLevel;
        public int roiHeight;
        public int roiWidth;
        public int roiX;
        public int roiY;
        public double zeroThreshold;

        public InputData() {
            this(true);
        }

        public InputData(boolean autoWrite) {
            this.pyramidLevel = 0;
            this.roiX = 0;
            this.roiY = 0;
            this.roiWidth = 0;
            this.roiHeight = 0;
            this.zeroThreshold = 0.0d;
            this.outlierThreshold = 0.0d;
            this.buffer = null;
            this.autoWrite = true;
            this.autoWrite = autoWrite;
        }

        CLBuffer<ByteBuffer> getBuffer(JavaCVCL context) {
            if (this.buffer == null || this.buffer.getCLSize() < ((long) 16)) {
                if (this.buffer != null) {
                    this.buffer.release();
                }
                this.buffer = context.getCLContext().createByteBuffer(16, new Mem[]{Mem.READ_ONLY});
            }
            return this.buffer;
        }

        public CLBuffer<ByteBuffer> writeBuffer(JavaCVCL context) {
            getBuffer(context);
            ((ByteBuffer) ((ByteBuffer) this.buffer.getBuffer()).rewind()).putInt(this.roiY).putInt(this.roiHeight).putFloat((float) this.zeroThreshold).putFloat((float) this.outlierThreshold).rewind();
            context.writeBuffer(this.buffer, false);
            return this.buffer;
        }
    }

    public static class OutputData {
        boolean autoRead;
        CLBuffer<ByteBuffer> buffer;
        public int dstCount;
        public int dstCountOutlier;
        public int dstCountZero;
        public FloatBuffer dstDstDot;
        public FloatBuffer srcDstDot;

        public OutputData() {
            this(true);
        }

        public OutputData(boolean autoRead) {
            this.dstCount = 0;
            this.dstCountZero = 0;
            this.dstCountOutlier = 0;
            this.srcDstDot = null;
            this.dstDstDot = null;
            this.buffer = null;
            this.autoRead = true;
            this.autoRead = autoRead;
        }

        CLBuffer<ByteBuffer> getBuffer(JavaCVCL context, int dotSize, int reduceSize) {
            int structSize = ((dotSize + 4) + (dotSize * dotSize)) * 4;
            if (this.buffer == null || this.buffer.getCLSize() < ((long) (structSize * reduceSize))) {
                if (this.buffer != null) {
                    this.buffer.release();
                }
                this.buffer = context.getCLContext().createByteBuffer(structSize * reduceSize, new Mem[0]);
                ByteBuffer byteBuffer = (ByteBuffer) this.buffer.getBuffer();
                byteBuffer.position(16);
                this.srcDstDot = byteBuffer.asFloatBuffer();
                byteBuffer.position((dotSize + 4) * 4);
                this.dstDstDot = byteBuffer.asFloatBuffer();
                byteBuffer.rewind();
            }
            return this.buffer;
        }

        public CLBuffer<ByteBuffer> readBuffer(JavaCVCL context) {
            context.readBuffer(this.buffer, true);
            ByteBuffer byteBuffer = (ByteBuffer) this.buffer.getBuffer();
            this.dstCount = byteBuffer.getInt(4);
            this.dstCountZero = byteBuffer.getInt(8);
            this.dstCountOutlier = byteBuffer.getInt(12);
            return this.buffer;
        }
    }

    JavaCVCL getContext();

    void transform(CLImage2d cLImage2d, CLImage2d cLImage2d2, CLImage2d cLImage2d3, CLImage2d cLImage2d4, CLImage2d cLImage2d5, CLImage2d cLImage2d6, Parameters[] parametersArr, boolean[] zArr, InputData inputData, OutputData outputData);
}
