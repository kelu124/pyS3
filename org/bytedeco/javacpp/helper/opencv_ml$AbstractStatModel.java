package org.bytedeco.javacpp.helper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_ml.ANN_MLP;
import org.bytedeco.javacpp.opencv_ml.Boost;
import org.bytedeco.javacpp.opencv_ml.DTrees;
import org.bytedeco.javacpp.opencv_ml.EM;
import org.bytedeco.javacpp.opencv_ml.KNearest;
import org.bytedeco.javacpp.opencv_ml.LogisticRegression;
import org.bytedeco.javacpp.opencv_ml.NormalBayesClassifier;
import org.bytedeco.javacpp.opencv_ml.RTrees;
import org.bytedeco.javacpp.opencv_ml.SVM;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

@Name({"cv::ml::StatModel"})
public abstract class opencv_ml$AbstractStatModel extends Algorithm {
    @Ptr
    @Name({"load<cv::ml::ANN_MLP>"})
    public static native ANN_MLP loadANN_MLP(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::ANN_MLP>"})
    public static native ANN_MLP loadANN_MLP(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::Boost>"})
    public static native Boost loadBoost(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::Boost>"})
    public static native Boost loadBoost(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::DTrees>"})
    public static native DTrees loadDTrees(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::DTrees>"})
    public static native DTrees loadDTrees(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::EM>"})
    public static native EM loadEM(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::EM>"})
    public static native EM loadEM(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::KNearest>"})
    public static native KNearest loadKNearest(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::KNearest>"})
    public static native KNearest loadKNearest(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::LogisticRegression>"})
    public static native LogisticRegression loadLogisticRegression(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::LogisticRegression>"})
    public static native LogisticRegression loadLogisticRegression(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::NormalBayesClassifier>"})
    public static native NormalBayesClassifier loadNormalBayesClassifier(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::NormalBayesClassifier>"})
    public static native NormalBayesClassifier loadNormalBayesClassifier(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::RTrees>"})
    public static native RTrees loadRTrees(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::RTrees>"})
    public static native RTrees loadRTrees(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Ptr
    @Name({"load<cv::ml::SVM>"})
    public static native SVM loadSVM(@Str String str, @Str String str2);

    @Ptr
    @Name({"load<cv::ml::SVM>"})
    public static native SVM loadSVM(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    public opencv_ml$AbstractStatModel(Pointer p) {
        super(p);
    }
}
