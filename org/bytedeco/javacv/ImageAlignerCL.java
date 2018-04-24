package org.bytedeco.javacv;

import com.jogamp.opencl.CLImage2d;

public interface ImageAlignerCL extends ImageAligner {
    CLImage2d[] getImagesCL();

    CLImage2d getMaskImageCL();

    CLImage2d getResidualImageCL();

    CLImage2d getTargetImageCL();

    CLImage2d getTemplateImageCL();

    CLImage2d getTransformedImageCL();

    void setTargetImageCL(CLImage2d cLImage2d);

    void setTemplateImageCL(CLImage2d cLImage2d, double[] dArr);
}
