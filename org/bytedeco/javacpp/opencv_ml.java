package org.bytedeco.javacpp;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.helper.opencv_ml$AbstractStatModel;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point2d;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_ml extends org.bytedeco.javacpp.helper.opencv_ml {
    public static final int COL_SAMPLE = 1;
    public static final int ROW_SAMPLE = 0;
    public static final int TEST_ERROR = 0;
    public static final int TRAIN_ERROR = 1;
    public static final int VAR_CATEGORICAL = 1;
    public static final int VAR_NUMERICAL = 0;
    public static final int VAR_ORDERED = 0;

    @Namespace("cv::ml")
    public static class StatModel extends opencv_ml$AbstractStatModel {
        public static final int COMPRESSED_INPUT = 2;
        public static final int PREPROCESSED_INPUT = 4;
        public static final int RAW_OUTPUT = 1;
        public static final int UPDATE_MODEL = 1;

        public native float calcError(@Ptr TrainData trainData, @Cast({"bool"}) boolean z, @ByVal Mat mat);

        public native float calcError(@Ptr TrainData trainData, @Cast({"bool"}) boolean z, @ByVal UMat uMat);

        @Cast({"bool"})
        public native boolean empty();

        public native int getVarCount();

        @Cast({"bool"})
        public native boolean isClassifier();

        @Cast({"bool"})
        public native boolean isTrained();

        public native float predict(@ByVal Mat mat);

        public native float predict(@ByVal Mat mat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat2, int i);

        public native float predict(@ByVal UMat uMat);

        public native float predict(@ByVal UMat uMat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat2, int i);

        @Cast({"bool"})
        public native boolean train(@ByVal Mat mat, int i, @ByVal Mat mat2);

        @Cast({"bool"})
        public native boolean train(@ByVal UMat uMat, int i, @ByVal UMat uMat2);

        @Cast({"bool"})
        public native boolean train(@Ptr TrainData trainData);

        @Cast({"bool"})
        public native boolean train(@Ptr TrainData trainData, int i);

        static {
            Loader.load();
        }

        public StatModel(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class ANN_MLP extends StatModel {
        public static final int BACKPROP = 0;
        public static final int GAUSSIAN = 2;
        public static final int IDENTITY = 0;
        public static final int NO_INPUT_SCALE = 2;
        public static final int NO_OUTPUT_SCALE = 4;
        public static final int RPROP = 1;
        public static final int SIGMOID_SYM = 1;
        public static final int UPDATE_WEIGHTS = 1;

        @Ptr
        public static native ANN_MLP create();

        public native double getBackpropMomentumScale();

        public native double getBackpropWeightScale();

        @ByVal
        public native Mat getLayerSizes();

        public native double getRpropDW0();

        public native double getRpropDWMax();

        public native double getRpropDWMin();

        public native double getRpropDWMinus();

        public native double getRpropDWPlus();

        @ByVal
        public native TermCriteria getTermCriteria();

        public native int getTrainMethod();

        @ByVal
        public native Mat getWeights(int i);

        public native void setActivationFunction(int i);

        public native void setActivationFunction(int i, double d, double d2);

        public native void setBackpropMomentumScale(double d);

        public native void setBackpropWeightScale(double d);

        public native void setLayerSizes(@ByVal Mat mat);

        public native void setLayerSizes(@ByVal UMat uMat);

        public native void setRpropDW0(double d);

        public native void setRpropDWMax(double d);

        public native void setRpropDWMin(double d);

        public native void setRpropDWMinus(double d);

        public native void setRpropDWPlus(double d);

        public native void setTermCriteria(@ByVal TermCriteria termCriteria);

        public native void setTrainMethod(int i);

        public native void setTrainMethod(int i, double d, double d2);

        static {
            Loader.load();
        }

        public ANN_MLP(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class DTrees extends StatModel {
        public static final int PREDICT_AUTO = 0;
        public static final int PREDICT_MASK = 768;
        public static final int PREDICT_MAX_VOTE = 512;
        public static final int PREDICT_SUM = 256;

        @NoOffset
        public static class Node extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            public native int classIdx();

            public native Node classIdx(int i);

            public native int defaultDir();

            public native Node defaultDir(int i);

            public native int left();

            public native Node left(int i);

            public native int parent();

            public native Node parent(int i);

            public native int right();

            public native Node right(int i);

            public native int split();

            public native Node split(int i);

            public native double value();

            public native Node value(double d);

            static {
                Loader.load();
            }

            public Node(Pointer p) {
                super(p);
            }

            public Node(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Node position(long position) {
                return (Node) super.position(position);
            }

            public Node() {
                super((Pointer) null);
                allocate();
            }
        }

        @NoOffset
        public static class Split extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            public native float m162c();

            public native Split m163c(float f);

            public native Split inversed(boolean z);

            @Cast({"bool"})
            public native boolean inversed();

            public native int next();

            public native Split next(int i);

            public native float quality();

            public native Split quality(float f);

            public native int subsetOfs();

            public native Split subsetOfs(int i);

            public native int varIdx();

            public native Split varIdx(int i);

            static {
                Loader.load();
            }

            public Split(Pointer p) {
                super(p);
            }

            public Split(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Split position(long position) {
                return (Split) super.position(position);
            }

            public Split() {
                super((Pointer) null);
                allocate();
            }
        }

        @Ptr
        public static native DTrees create();

        public native int getCVFolds();

        public native int getMaxCategories();

        public native int getMaxDepth();

        public native int getMinSampleCount();

        @StdVector
        public native Node getNodes();

        @ByVal
        public native Mat getPriors();

        public native float getRegressionAccuracy();

        @StdVector
        public native IntPointer getRoots();

        @StdVector
        public native Split getSplits();

        @StdVector
        public native IntPointer getSubsets();

        @Cast({"bool"})
        public native boolean getTruncatePrunedTree();

        @Cast({"bool"})
        public native boolean getUse1SERule();

        @Cast({"bool"})
        public native boolean getUseSurrogates();

        public native void setCVFolds(int i);

        public native void setMaxCategories(int i);

        public native void setMaxDepth(int i);

        public native void setMinSampleCount(int i);

        public native void setPriors(@ByRef @Const Mat mat);

        public native void setRegressionAccuracy(float f);

        public native void setTruncatePrunedTree(@Cast({"bool"}) boolean z);

        public native void setUse1SERule(@Cast({"bool"}) boolean z);

        public native void setUseSurrogates(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public DTrees(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class Boost extends DTrees {
        public static final int DISCRETE = 0;
        public static final int GENTLE = 3;
        public static final int LOGIT = 2;
        public static final int REAL = 1;

        @Ptr
        public static native Boost create();

        public native int getBoostType();

        public native int getWeakCount();

        public native double getWeightTrimRate();

        public native void setBoostType(int i);

        public native void setWeakCount(int i);

        public native void setWeightTrimRate(double d);

        static {
            Loader.load();
        }

        public Boost(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class EM extends StatModel {
        public static final int COV_MAT_DEFAULT = 1;
        public static final int COV_MAT_DIAGONAL = 1;
        public static final int COV_MAT_GENERIC = 2;
        public static final int COV_MAT_SPHERICAL = 0;
        public static final int DEFAULT_MAX_ITERS = 100;
        public static final int DEFAULT_NCLUSTERS = 5;
        public static final int START_AUTO_STEP = 0;
        public static final int START_E_STEP = 1;
        public static final int START_M_STEP = 2;

        @Ptr
        public static native EM create();

        public native int getClustersNumber();

        public native int getCovarianceMatrixType();

        public native void getCovs(@ByRef MatVector matVector);

        @ByVal
        public native Mat getMeans();

        @ByVal
        public native TermCriteria getTermCriteria();

        @ByVal
        public native Mat getWeights();

        @ByVal
        public native Point2d predict2(@ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Point2d predict2(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void setClustersNumber(int i);

        public native void setCovarianceMatrixType(int i);

        public native void setTermCriteria(@ByRef @Const TermCriteria termCriteria);

        @Cast({"bool"})
        public native boolean trainE(@ByVal Mat mat, @ByVal Mat mat2);

        @Cast({"bool"})
        public native boolean trainE(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7);

        @Cast({"bool"})
        public native boolean trainE(@ByVal UMat uMat, @ByVal UMat uMat2);

        @Cast({"bool"})
        public native boolean trainE(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7);

        @Cast({"bool"})
        public native boolean trainEM(@ByVal Mat mat);

        @Cast({"bool"})
        public native boolean trainEM(@ByVal Mat mat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4);

        @Cast({"bool"})
        public native boolean trainEM(@ByVal UMat uMat);

        @Cast({"bool"})
        public native boolean trainEM(@ByVal UMat uMat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4);

        @Cast({"bool"})
        public native boolean trainM(@ByVal Mat mat, @ByVal Mat mat2);

        @Cast({"bool"})
        public native boolean trainM(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat5);

        @Cast({"bool"})
        public native boolean trainM(@ByVal UMat uMat, @ByVal UMat uMat2);

        @Cast({"bool"})
        public native boolean trainM(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat5);

        static {
            Loader.load();
        }

        public EM(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class KNearest extends StatModel {
        public static final int BRUTE_FORCE = 1;
        public static final int KDTREE = 2;

        @Ptr
        public static native KNearest create();

        public native float findNearest(@ByVal Mat mat, int i, @ByVal Mat mat2);

        public native float findNearest(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4);

        public native float findNearest(@ByVal UMat uMat, int i, @ByVal UMat uMat2);

        public native float findNearest(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4);

        public native int getAlgorithmType();

        public native int getDefaultK();

        public native int getEmax();

        @Cast({"bool"})
        public native boolean getIsClassifier();

        public native void setAlgorithmType(int i);

        public native void setDefaultK(int i);

        public native void setEmax(int i);

        public native void setIsClassifier(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public KNearest(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class LogisticRegression extends StatModel {
        public static final int BATCH = 0;
        public static final int MINI_BATCH = 1;
        public static final int REG_DISABLE = -1;
        public static final int REG_L1 = 0;
        public static final int REG_L2 = 1;

        @Ptr
        public static native LogisticRegression create();

        public native int getIterations();

        public native double getLearningRate();

        public native int getMiniBatchSize();

        public native int getRegularization();

        @ByVal
        public native TermCriteria getTermCriteria();

        public native int getTrainMethod();

        @ByVal
        public native Mat get_learnt_thetas();

        public native float predict(@ByVal Mat mat);

        public native float predict(@ByVal Mat mat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat2, int i);

        public native float predict(@ByVal UMat uMat);

        public native float predict(@ByVal UMat uMat, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat2, int i);

        public native void setIterations(int i);

        public native void setLearningRate(double d);

        public native void setMiniBatchSize(int i);

        public native void setRegularization(int i);

        public native void setTermCriteria(@ByVal TermCriteria termCriteria);

        public native void setTrainMethod(int i);

        static {
            Loader.load();
        }

        public LogisticRegression(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class NormalBayesClassifier extends StatModel {
        @Ptr
        public static native NormalBayesClassifier create();

        public native float predictProb(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native float predictProb(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

        public native float predictProb(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native float predictProb(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

        static {
            Loader.load();
        }

        public NormalBayesClassifier(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    @NoOffset
    public static class ParamGrid extends Pointer {
        private native void allocate();

        private native void allocate(double d, double d2, double d3);

        private native void allocateArray(long j);

        public native double logStep();

        public native ParamGrid logStep(double d);

        public native double maxVal();

        public native ParamGrid maxVal(double d);

        public native double minVal();

        public native ParamGrid minVal(double d);

        static {
            Loader.load();
        }

        public ParamGrid(Pointer p) {
            super(p);
        }

        public ParamGrid(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ParamGrid position(long position) {
            return (ParamGrid) super.position(position);
        }

        public ParamGrid() {
            super((Pointer) null);
            allocate();
        }

        public ParamGrid(double _minVal, double _maxVal, double _logStep) {
            super((Pointer) null);
            allocate(_minVal, _maxVal, _logStep);
        }
    }

    @Namespace("cv::ml")
    public static class RTrees extends DTrees {
        @Ptr
        public static native RTrees create();

        public native int getActiveVarCount();

        @Cast({"bool"})
        public native boolean getCalculateVarImportance();

        @ByVal
        public native TermCriteria getTermCriteria();

        @ByVal
        public native Mat getVarImportance();

        public native void setActiveVarCount(int i);

        public native void setCalculateVarImportance(@Cast({"bool"}) boolean z);

        public native void setTermCriteria(@ByRef @Const TermCriteria termCriteria);

        static {
            Loader.load();
        }

        public RTrees(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class SVM extends StatModel {
        public static final int f66C = 0;
        public static final int CHI2 = 4;
        public static final int COEF = 4;
        public static final int CUSTOM = -1;
        public static final int C_SVC = 100;
        public static final int DEGREE = 5;
        public static final int EPS_SVR = 103;
        public static final int GAMMA = 1;
        public static final int INTER = 5;
        public static final int LINEAR = 0;
        public static final int NU = 3;
        public static final int NU_SVC = 101;
        public static final int NU_SVR = 104;
        public static final int ONE_CLASS = 102;
        public static final int f67P = 2;
        public static final int POLY = 1;
        public static final int RBF = 2;
        public static final int SIGMOID = 3;

        @Ptr
        public static native SVM create();

        @ByVal
        public static native ParamGrid getDefaultGrid(int i);

        public native double getC();

        @ByVal
        public native Mat getClassWeights();

        public native double getCoef0();

        public native double getDecisionFunction(int i, @ByVal Mat mat, @ByVal Mat mat2);

        public native double getDecisionFunction(int i, @ByVal UMat uMat, @ByVal UMat uMat2);

        public native double getDegree();

        public native double getGamma();

        public native int getKernelType();

        public native double getNu();

        public native double getP();

        @ByVal
        public native Mat getSupportVectors();

        @ByVal
        public native TermCriteria getTermCriteria();

        public native int getType();

        @ByVal
        public native Mat getUncompressedSupportVectors();

        public native void setC(double d);

        public native void setClassWeights(@ByRef @Const Mat mat);

        public native void setCoef0(double d);

        public native void setCustomKernel(@Ptr Kernel kernel);

        public native void setDegree(double d);

        public native void setGamma(double d);

        public native void setKernel(int i);

        public native void setNu(double d);

        public native void setP(double d);

        public native void setTermCriteria(@ByRef @Const TermCriteria termCriteria);

        public native void setType(int i);

        @Cast({"bool"})
        public native boolean trainAuto(@Ptr TrainData trainData);

        @Cast({"bool"})
        public native boolean trainAuto(@Ptr TrainData trainData, int i, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::C))") ParamGrid paramGrid, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::GAMMA))") ParamGrid paramGrid2, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::P))") ParamGrid paramGrid3, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::NU))") ParamGrid paramGrid4, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::COEF))") ParamGrid paramGrid5, @ByVal(nullValue = "cv::ml::ParamGrid(cv::ml::SVM::getDefaultGrid(cv::ml::SVM::DEGREE))") ParamGrid paramGrid6, @Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public SVM(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static class TrainData extends Pointer {
        @Ptr
        public static native TrainData create(@ByVal Mat mat, int i, @ByVal Mat mat2);

        @Ptr
        public static native TrainData create(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat5, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat6);

        @Ptr
        public static native TrainData create(@ByVal UMat uMat, int i, @ByVal UMat uMat2);

        @Ptr
        public static native TrainData create(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat5, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat6);

        @ByVal
        public static native Mat getSubVector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

        @Ptr
        public static native TrainData loadFromCSV(@Str String str, int i);

        @Ptr
        public static native TrainData loadFromCSV(@Str String str, int i, int i2, int i3, @Str String str2, @Cast({"char"}) byte b, @Cast({"char"}) byte b2);

        @Ptr
        public static native TrainData loadFromCSV(@Str BytePointer bytePointer, int i);

        @Ptr
        public static native TrainData loadFromCSV(@Str BytePointer bytePointer, int i, int i2, int i3, @Str BytePointer bytePointer2, @Cast({"char"}) byte b, @Cast({"char"}) byte b2);

        public static native float missingValue();

        public native int getCatCount(int i);

        @ByVal
        public native Mat getCatMap();

        @ByVal
        public native Mat getCatOfs();

        @ByVal
        public native Mat getClassLabels();

        @ByVal
        public native Mat getDefaultSubstValues();

        public native int getLayout();

        @ByVal
        public native Mat getMissing();

        public native int getNAllVars();

        public native int getNSamples();

        public native int getNTestSamples();

        public native int getNTrainSamples();

        public native int getNVars();

        @ByVal
        public native Mat getNormCatResponses();

        public native void getNormCatValues(int i, @ByVal Mat mat, IntBuffer intBuffer);

        public native void getNormCatValues(int i, @ByVal Mat mat, IntPointer intPointer);

        public native void getNormCatValues(int i, @ByVal UMat uMat, IntPointer intPointer);

        public native void getNormCatValues(int i, @ByVal UMat uMat, int[] iArr);

        public native int getResponseType();

        @ByVal
        public native Mat getResponses();

        public native void getSample(@ByVal Mat mat, int i, FloatBuffer floatBuffer);

        public native void getSample(@ByVal Mat mat, int i, FloatPointer floatPointer);

        public native void getSample(@ByVal UMat uMat, int i, FloatPointer floatPointer);

        public native void getSample(@ByVal UMat uMat, int i, float[] fArr);

        @ByVal
        public native Mat getSampleWeights();

        @ByVal
        public native Mat getSamples();

        @ByVal
        public native Mat getTestNormCatResponses();

        @ByVal
        public native Mat getTestResponses();

        @ByVal
        public native Mat getTestSampleIdx();

        @ByVal
        public native Mat getTestSampleWeights();

        @ByVal
        public native Mat getTrainNormCatResponses();

        @ByVal
        public native Mat getTrainResponses();

        @ByVal
        public native Mat getTrainSampleIdx();

        @ByVal
        public native Mat getTrainSampleWeights();

        @ByVal
        public native Mat getTrainSamples();

        @ByVal
        public native Mat getTrainSamples(int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

        public native void getValues(int i, @ByVal Mat mat, FloatBuffer floatBuffer);

        public native void getValues(int i, @ByVal Mat mat, FloatPointer floatPointer);

        public native void getValues(int i, @ByVal UMat uMat, FloatPointer floatPointer);

        public native void getValues(int i, @ByVal UMat uMat, float[] fArr);

        @ByVal
        public native Mat getVarIdx();

        @ByVal
        public native Mat getVarType();

        public native void setTrainTestSplit(int i);

        public native void setTrainTestSplit(int i, @Cast({"bool"}) boolean z);

        public native void setTrainTestSplitRatio(double d);

        public native void setTrainTestSplitRatio(double d, @Cast({"bool"}) boolean z);

        public native void shuffleTrainTest();

        static {
            Loader.load();
        }

        public TrainData(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ml")
    public static native void createConcentricSpheresTestSet(int i, int i2, int i3, @ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::ml")
    public static native void createConcentricSpheresTestSet(int i, int i2, int i3, @ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::ml")
    public static native void randMVNormal(@ByVal Mat mat, @ByVal Mat mat2, int i, @ByVal Mat mat3);

    @Namespace("cv::ml")
    public static native void randMVNormal(@ByVal UMat uMat, @ByVal UMat uMat2, int i, @ByVal UMat uMat3);

    static {
        Loader.load();
    }
}
