package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Convention;
import org.bytedeco.javacpp.annotation.Index;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.annotation.ValueSetter;
import org.bytedeco.javacpp.annotation.Virtual;
import org.bytedeco.javacpp.helper.opencv_core$AbstractCvMat;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvBox2D;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvFileStorage;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvGraph;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvMatND;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvPoint;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvPoint2D32f;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvPoint3D32f;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvRect;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvSeq;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvSet;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvSize;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvSize2D32f;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvSparseMat;
import org.bytedeco.javacpp.helper.opencv_core.AbstractIplImage;
import org.bytedeco.javacpp.helper.opencv_core.AbstractMat;
import org.bytedeco.javacpp.helper.opencv_core.AbstractScalar;
import org.bytedeco.javacpp.helper.opencv_imgproc.AbstractCvHistogram;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_core extends org.bytedeco.javacpp.helper.opencv_core {
    public static final int ACCESS_FAST = 67108864;
    public static final int ACCESS_MASK = 50331648;
    public static final int ACCESS_READ = 16777216;
    public static final int ACCESS_RW = 50331648;
    public static final int ACCESS_WRITE = 33554432;
    public static final int BORDER_CONSTANT = 0;
    public static final int BORDER_DEFAULT = 4;
    public static final int BORDER_ISOLATED = 16;
    public static final int BORDER_REFLECT = 2;
    public static final int BORDER_REFLECT101 = 4;
    public static final int BORDER_REFLECT_101 = 4;
    public static final int BORDER_REPLICATE = 1;
    public static final int BORDER_TRANSPARENT = 5;
    public static final int BORDER_WRAP = 3;
    public static final int BadAlign = -21;
    public static final int BadAlphaChannel = -18;
    public static final int BadCOI = -24;
    public static final int BadCallBack = -22;
    public static final int BadDataPtr = -12;
    public static final int BadDepth = -17;
    public static final int BadImageSize = -10;
    public static final int BadModelOrChSeq = -14;
    public static final int BadNumChannel1U = -16;
    public static final int BadNumChannels = -15;
    public static final int BadOffset = -11;
    public static final int BadOrder = -19;
    public static final int BadOrigin = -20;
    public static final int BadROISize = -25;
    public static final int BadStep = -13;
    public static final int BadTileSize = -23;
    public static final int CMP_EQ = 0;
    public static final int CMP_GE = 2;
    public static final int CMP_GT = 1;
    public static final int CMP_LE = 4;
    public static final int CMP_LT = 3;
    public static final int CMP_NE = 5;
    public static final int COVAR_COLS = 16;
    public static final int COVAR_NORMAL = 1;
    public static final int COVAR_ROWS = 8;
    public static final int COVAR_SCALE = 4;
    public static final int COVAR_SCRAMBLED = 0;
    public static final int COVAR_USE_AVG = 2;
    public static final int CPU_AVX = 10;
    public static final int CPU_AVX2 = 11;
    public static final int CPU_AVX_512BW = 14;
    public static final int CPU_AVX_512CD = 15;
    public static final int CPU_AVX_512DQ = 16;
    public static final int CPU_AVX_512ER = 17;
    public static final int CPU_AVX_512F = 13;
    public static final int CPU_AVX_512IFMA512 = 18;
    public static final int CPU_AVX_512PF = 19;
    public static final int CPU_AVX_512VBMI = 20;
    public static final int CPU_AVX_512VL = 21;
    public static final int CPU_FMA3 = 12;
    public static final int CPU_MMX = 1;
    public static final int CPU_NEON = 100;
    public static final int CPU_POPCNT = 8;
    public static final int CPU_SSE = 2;
    public static final int CPU_SSE2 = 3;
    public static final int CPU_SSE3 = 4;
    public static final int CPU_SSE4_1 = 6;
    public static final int CPU_SSE4_2 = 7;
    public static final int CPU_SSSE3 = 5;
    public static final int CV_16S = 3;
    public static final int CV_16SC1 = CV_MAKETYPE(3, 1);
    public static final int CV_16SC2 = CV_MAKETYPE(3, 2);
    public static final int CV_16SC3 = CV_MAKETYPE(3, 3);
    public static final int CV_16SC4 = CV_MAKETYPE(3, 4);
    public static final int CV_16U = 2;
    public static final int CV_16UC1 = CV_MAKETYPE(2, 1);
    public static final int CV_16UC2 = CV_MAKETYPE(2, 2);
    public static final int CV_16UC3 = CV_MAKETYPE(2, 3);
    public static final int CV_16UC4 = CV_MAKETYPE(2, 4);
    public static final double CV_2PI = 6.283185307179586d;
    public static final int CV_32F = 5;
    public static final int CV_32FC1 = CV_MAKETYPE(5, 1);
    public static final int CV_32FC2 = CV_MAKETYPE(5, 2);
    public static final int CV_32FC3 = CV_MAKETYPE(5, 3);
    public static final int CV_32FC4 = CV_MAKETYPE(5, 4);
    public static final int CV_32S = 4;
    public static final int CV_32SC1 = CV_MAKETYPE(4, 1);
    public static final int CV_32SC2 = CV_MAKETYPE(4, 2);
    public static final int CV_32SC3 = CV_MAKETYPE(4, 3);
    public static final int CV_32SC4 = CV_MAKETYPE(4, 4);
    public static final int CV_64F = 6;
    public static final int CV_64FC1 = CV_MAKETYPE(6, 1);
    public static final int CV_64FC2 = CV_MAKETYPE(6, 2);
    public static final int CV_64FC3 = CV_MAKETYPE(6, 3);
    public static final int CV_64FC4 = CV_MAKETYPE(6, 4);
    public static final int CV_8S = 1;
    public static final int CV_8SC1 = CV_MAKETYPE(1, 1);
    public static final int CV_8SC2 = CV_MAKETYPE(1, 2);
    public static final int CV_8SC3 = CV_MAKETYPE(1, 3);
    public static final int CV_8SC4 = CV_MAKETYPE(1, 4);
    public static final int CV_8U = 0;
    public static final int CV_8UC1 = CV_MAKETYPE(0, 1);
    public static final int CV_8UC2 = CV_MAKETYPE(0, 2);
    public static final int CV_8UC3 = CV_MAKETYPE(0, 3);
    public static final int CV_8UC4 = CV_MAKETYPE(0, 4);
    public static final int CV_AUTOSTEP = Integer.MAX_VALUE;
    public static final int CV_AUTO_STEP = Integer.MAX_VALUE;
    public static final int CV_AVX = 1;
    public static final int CV_AVX2 = 1;
    public static final int CV_AVX_512BW = 0;
    public static final int CV_AVX_512CD = 0;
    public static final int CV_AVX_512DQ = 0;
    public static final int CV_AVX_512ER = 0;
    public static final int CV_AVX_512F = 0;
    public static final int CV_AVX_512IFMA512 = 0;
    public static final int CV_AVX_512PF = 0;
    public static final int CV_AVX_512VBMI = 0;
    public static final int CV_AVX_512VL = 0;
    public static final int CV_BACK = 0;
    public static final int CV_BadAlign = -21;
    public static final int CV_BadAlphaChannel = -18;
    public static final int CV_BadCOI = -24;
    public static final int CV_BadCallBack = -22;
    public static final int CV_BadDataPtr = -12;
    public static final int CV_BadDepth = -17;
    public static final int CV_BadImageSize = -10;
    public static final int CV_BadModelOrChSeq = -14;
    public static final int CV_BadNumChannel1U = -16;
    public static final int CV_BadNumChannels = -15;
    public static final int CV_BadOffset = -11;
    public static final int CV_BadOrder = -19;
    public static final int CV_BadOrigin = -20;
    public static final int CV_BadROISize = -25;
    public static final int CV_BadStep = -13;
    public static final int CV_BadTileSize = -23;
    public static final int CV_C = 1;
    public static final int CV_CHECK_QUIET = 2;
    public static final int CV_CHECK_RANGE = 1;
    public static final int CV_CHOLESKY = 3;
    public static final int CV_CMP_EQ = 0;
    public static final int CV_CMP_GE = 2;
    public static final int CV_CMP_GT = 1;
    public static final int CV_CMP_LE = 4;
    public static final int CV_CMP_LT = 3;
    public static final int CV_CMP_NE = 5;
    public static final int CV_CN_MAX = 512;
    public static final int CV_CN_SHIFT = 3;
    public static final int CV_COVAR_COLS = 16;
    public static final int CV_COVAR_NORMAL = 1;
    public static final int CV_COVAR_ROWS = 8;
    public static final int CV_COVAR_SCALE = 4;
    public static final int CV_COVAR_SCRAMBLED = 0;
    public static final int CV_COVAR_USE_AVG = 2;
    public static final int CV_CPU_AVX = 10;
    public static final int CV_CPU_AVX2 = 11;
    public static final int CV_CPU_AVX_512BW = 14;
    public static final int CV_CPU_AVX_512CD = 15;
    public static final int CV_CPU_AVX_512DQ = 16;
    public static final int CV_CPU_AVX_512ER = 17;
    public static final int CV_CPU_AVX_512F = 13;
    public static final int CV_CPU_AVX_512IFMA512 = 18;
    public static final int CV_CPU_AVX_512PF = 19;
    public static final int CV_CPU_AVX_512VBMI = 20;
    public static final int CV_CPU_AVX_512VL = 21;
    public static final int CV_CPU_FMA3 = 12;
    public static final int CV_CPU_MMX = 1;
    public static final int CV_CPU_NEON = 100;
    public static final int CV_CPU_NONE = 0;
    public static final int CV_CPU_POPCNT = 8;
    public static final int CV_CPU_SSE = 2;
    public static final int CV_CPU_SSE2 = 3;
    public static final int CV_CPU_SSE3 = 4;
    public static final int CV_CPU_SSE4_1 = 6;
    public static final int CV_CPU_SSE4_2 = 7;
    public static final int CV_CPU_SSSE3 = 5;
    public static final int CV_CXX_MOVE_SEMANTICS = 1;
    public static final int CV_DEPTH_MAX = 8;
    public static final int CV_DIFF = 16;
    public static final int CV_DIFF_C = 17;
    public static final int CV_DIFF_L1 = 18;
    public static final int CV_DIFF_L2 = 20;
    public static final int CV_DXT_FORWARD = 0;
    public static final int CV_DXT_INVERSE = 1;
    public static final int CV_DXT_INVERSE_SCALE = 3;
    public static final int CV_DXT_INV_SCALE = 3;
    public static final int CV_DXT_MUL_CONJ = 8;
    public static final int CV_DXT_ROWS = 4;
    public static final int CV_DXT_SCALE = 2;
    public static final int CV_ErrModeLeaf = 0;
    public static final int CV_ErrModeParent = 1;
    public static final int CV_ErrModeSilent = 2;
    public static final int CV_FMA3 = 1;
    public static final int CV_FRONT = 1;
    public static final int CV_GEMM_A_T = 1;
    public static final int CV_GEMM_B_T = 2;
    public static final int CV_GEMM_C_T = 4;
    public static final int CV_GRAPH = 4096;
    public static final int CV_GRAPH_ALL_ITEMS = -1;
    public static final int CV_GRAPH_ANY_EDGE = 30;
    public static final int CV_GRAPH_BACKTRACKING = 64;
    public static final int CV_GRAPH_BACK_EDGE = 4;
    public static final int CV_GRAPH_CROSS_EDGE = 16;
    public static final int CV_GRAPH_FLAG_ORIENTED = 16384;
    public static final int CV_GRAPH_FORWARD_EDGE = 8;
    public static final int CV_GRAPH_FORWARD_EDGE_FLAG = 268435456;
    public static final int CV_GRAPH_ITEM_VISITED_FLAG = 1073741824;
    public static final int CV_GRAPH_NEW_TREE = 32;
    public static final int CV_GRAPH_OVER = -1;
    public static final int CV_GRAPH_SEARCH_TREE_NODE_FLAG = 536870912;
    public static final int CV_GRAPH_TREE_EDGE = 2;
    public static final int CV_GRAPH_VERTEX = 1;
    public static final int CV_GpuApiCallError = -217;
    public static final int CV_GpuNotSupported = -216;
    public static final int CV_HAL_CMP_EQ = 0;
    public static final int CV_HAL_CMP_GE = 2;
    public static final int CV_HAL_CMP_GT = 1;
    public static final int CV_HAL_CMP_LE = 4;
    public static final int CV_HAL_CMP_LT = 3;
    public static final int CV_HAL_CMP_NE = 5;
    public static final int CV_HAL_ERROR_NOT_IMPLEMENTED = 1;
    public static final int CV_HAL_ERROR_OK = 0;
    public static final int CV_HAL_ERROR_UNKNOWN = -1;
    public static final int CV_HARDWARE_MAX_FEATURE = 255;
    public static final int CV_HIST_ARRAY = 0;
    public static final int CV_HIST_MAGIC_VAL = 1111818240;
    public static final int CV_HIST_RANGES_FLAG = 2048;
    public static final int CV_HIST_SPARSE = 1;
    public static final int CV_HIST_TREE = 1;
    public static final int CV_HIST_UNIFORM = 1;
    public static final int CV_HIST_UNIFORM_FLAG = 1024;
    public static final int CV_HeaderIsNull = -9;
    public static final int CV_KMEANS_USE_INITIAL_LABELS = 1;
    public static final int CV_L1 = 2;
    public static final int CV_L2 = 4;
    public static final double CV_LOG2 = 0.6931471805599453d;
    public static final int CV_LU = 0;
    public static final int CV_MAGIC_MASK = -65536;
    public static final int CV_MAJOR_VERSION = 3;
    public static final int CV_MATND_MAGIC_VAL = 1111687168;
    public static final int CV_MAT_CN_MASK = 4088;
    public static final int CV_MAT_CONT_FLAG = 16384;
    public static final int CV_MAT_CONT_FLAG_SHIFT = 14;
    public static final int CV_MAT_DEPTH_MASK = 7;
    public static final int CV_MAT_MAGIC_VAL = 1111621632;
    public static final int CV_MAT_TYPE_MASK = 4095;
    public static final int CV_MAX_ARR = 10;
    public static final int CV_MAX_DIM = 32;
    public static final int CV_MAX_DIM_HEAP = 1024;
    public static final int CV_MINMAX = 32;
    public static final int CV_MINOR_VERSION = 1;
    public static final int CV_MMX = 1;
    public static final int CV_MaskIsTiled = -26;
    public static final int CV_NODE_EMPTY = 32;
    public static final int CV_NODE_FLOAT = 2;
    public static final int CV_NODE_FLOW = 8;
    public static final int CV_NODE_INT = 1;
    public static final int CV_NODE_INTEGER = 1;
    public static final int CV_NODE_MAP = 6;
    public static final int CV_NODE_NAMED = 64;
    public static final int CV_NODE_NONE = 0;
    public static final int CV_NODE_REAL = 2;
    public static final int CV_NODE_REF = 4;
    public static final int CV_NODE_SEQ = 5;
    public static final int CV_NODE_SEQ_SIMPLE = 256;
    public static final int CV_NODE_STR = 3;
    public static final int CV_NODE_STRING = 3;
    public static final int CV_NODE_TYPE_MASK = 7;
    public static final int CV_NODE_USER = 16;
    public static final int CV_NORMAL = 16;
    public static final int CV_NORM_MASK = 7;
    public static final int CV_NO_CN_CHECK = 2;
    public static final int CV_NO_DEPTH_CHECK = 1;
    public static final int CV_NO_SIZE_CHECK = 4;
    public static final int CV_ORIENTED_GRAPH = 20480;
    public static final int CV_OpenCLApiCallError = -220;
    public static final int CV_OpenCLDoubleNotSupported = -221;
    public static final int CV_OpenCLInitError = -222;
    public static final int CV_OpenCLNoAMDBlasFft = -223;
    public static final int CV_OpenGlApiCallError = -219;
    public static final int CV_OpenGlNotSupported = -218;
    public static final int CV_PCA_DATA_AS_COL = 1;
    public static final int CV_PCA_DATA_AS_ROW = 0;
    public static final int CV_PCA_USE_AVG = 2;
    public static final double CV_PI = 3.141592653589793d;
    public static final int CV_POPCNT = 1;
    public static final int CV_QR = 4;
    public static final int CV_RAND_NORMAL = 1;
    public static final int CV_RAND_UNI = 0;
    public static final int CV_REDUCE_AVG = 1;
    public static final int CV_REDUCE_MAX = 2;
    public static final int CV_REDUCE_MIN = 3;
    public static final int CV_REDUCE_SUM = 0;
    public static final int CV_RELATIVE = 8;
    public static final int CV_RELATIVE_C = 9;
    public static final int CV_RELATIVE_L1 = 10;
    public static final int CV_RELATIVE_L2 = 12;
    public static final long CV_RNG_COEFF = 4164903690L;
    public static final int CV_SEQ_CHAIN = (CV_SEQ_ELTYPE_CODE | 4096);
    public static final int CV_SEQ_CHAIN_CONTOUR = (CV_SEQ_CHAIN | 16384);
    public static final int CV_SEQ_CONNECTED_COMP = 0;
    public static final int CV_SEQ_CONTOUR = CV_SEQ_POLYGON;
    public static final int CV_SEQ_ELTYPE_BITS = 12;
    public static final int CV_SEQ_ELTYPE_CODE = CV_8UC1;
    public static final int CV_SEQ_ELTYPE_CONNECTED_COMP = 0;
    public static final int CV_SEQ_ELTYPE_GENERIC = 0;
    public static final int CV_SEQ_ELTYPE_GRAPH_EDGE = 0;
    public static final int CV_SEQ_ELTYPE_GRAPH_VERTEX = 0;
    public static final int CV_SEQ_ELTYPE_INDEX = CV_32SC1;
    public static final int CV_SEQ_ELTYPE_MASK = 4095;
    public static final int CV_SEQ_ELTYPE_POINT = CV_32SC2;
    public static final int CV_SEQ_ELTYPE_POINT3D = CV_32FC3;
    public static final int CV_SEQ_ELTYPE_PPOINT = 7;
    public static final int CV_SEQ_ELTYPE_PTR = 7;
    public static final int CV_SEQ_ELTYPE_TRIAN_ATR = 0;
    public static final int CV_SEQ_FLAG_CLOSED = 16384;
    public static final int CV_SEQ_FLAG_CONVEX = 0;
    public static final int CV_SEQ_FLAG_HOLE = 32768;
    public static final int CV_SEQ_FLAG_SHIFT = 14;
    public static final int CV_SEQ_FLAG_SIMPLE = 0;
    public static final int CV_SEQ_INDEX = (CV_SEQ_ELTYPE_INDEX | 0);
    public static final int CV_SEQ_KIND_BIN_TREE = 8192;
    public static final int CV_SEQ_KIND_BITS = 2;
    public static final int CV_SEQ_KIND_CURVE = 4096;
    public static final int CV_SEQ_KIND_GENERIC = 0;
    public static final int CV_SEQ_KIND_GRAPH = 4096;
    public static final int CV_SEQ_KIND_MASK = 12288;
    public static final int CV_SEQ_KIND_SUBDIV2D = 8192;
    public static final int CV_SEQ_MAGIC_VAL = 1117323264;
    public static final int CV_SEQ_POINT3D_SET = (CV_SEQ_ELTYPE_POINT3D | 0);
    public static final int CV_SEQ_POINT_SET = (CV_SEQ_ELTYPE_POINT | 0);
    public static final int CV_SEQ_POLYGON = (CV_SEQ_POLYLINE | 16384);
    public static final int CV_SEQ_POLYGON_TREE = 8192;
    public static final int CV_SEQ_POLYLINE = (CV_SEQ_ELTYPE_POINT | 4096);
    public static final int CV_SEQ_SIMPLE_POLYGON = (CV_SEQ_POLYGON | 0);
    public static final int CV_SET_ELEM_FREE_FLAG = CV_SET_ELEM_FREE_FLAG();
    public static final int CV_SET_ELEM_IDX_MASK = 67108863;
    public static final int CV_SET_MAGIC_VAL = 1117257728;
    public static final int CV_SORT_ASCENDING = 0;
    public static final int CV_SORT_DESCENDING = 16;
    public static final int CV_SORT_EVERY_COLUMN = 1;
    public static final int CV_SORT_EVERY_ROW = 0;
    public static final int CV_SPARSE_MAT_MAGIC_VAL = 1111752704;
    public static final int CV_SSE = 1;
    public static final int CV_SSE2 = 1;
    public static final int CV_SSE3 = 1;
    public static final int CV_SSE4_1 = 1;
    public static final int CV_SSE4_2 = 1;
    public static final int CV_SSSE3 = 1;
    public static final int CV_STORAGE_APPEND = 2;
    public static final int CV_STORAGE_FORMAT_AUTO = 0;
    public static final int CV_STORAGE_FORMAT_MASK = 56;
    public static final int CV_STORAGE_FORMAT_XML = 8;
    public static final int CV_STORAGE_FORMAT_YAML = 16;
    public static final int CV_STORAGE_MAGIC_VAL = 1116274688;
    public static final int CV_STORAGE_MEMORY = 4;
    public static final int CV_STORAGE_READ = 0;
    public static final int CV_STORAGE_WRITE = 1;
    public static final int CV_STORAGE_WRITE_BINARY = 1;
    public static final int CV_STORAGE_WRITE_TEXT = 1;
    public static final int CV_SUBMAT_FLAG = 32768;
    public static final int CV_SUBMAT_FLAG_SHIFT = 15;
    public static final int CV_SUBMINOR_VERSION = 0;
    public static final int CV_SVD = 1;
    public static final int CV_SVD_MODIFY_A = 1;
    public static final int CV_SVD_SYM = 2;
    public static final int CV_SVD_U_T = 2;
    public static final int CV_SVD_V_T = 4;
    public static final int CV_StsAssert = -215;
    public static final int CV_StsAutoTrace = -8;
    public static final int CV_StsBackTrace = -1;
    public static final int CV_StsBadArg = -5;
    public static final int CV_StsBadFlag = -206;
    public static final int CV_StsBadFunc = -6;
    public static final int CV_StsBadMask = -208;
    public static final int CV_StsBadMemBlock = -214;
    public static final int CV_StsBadPoint = -207;
    public static final int CV_StsBadSize = -201;
    public static final int CV_StsDivByZero = -202;
    public static final int CV_StsError = -2;
    public static final int CV_StsFilterOffsetErr = -31;
    public static final int CV_StsFilterStructContentErr = -29;
    public static final int CV_StsInplaceNotSupported = -203;
    public static final int CV_StsInternal = -3;
    public static final int CV_StsKernelStructContentErr = -30;
    public static final int CV_StsNoConv = -7;
    public static final int CV_StsNoMem = -4;
    public static final int CV_StsNotImplemented = -213;
    public static final int CV_StsNullPtr = -27;
    public static final int CV_StsObjectNotFound = -204;
    public static final int CV_StsOk = 0;
    public static final int CV_StsOutOfRange = -211;
    public static final int CV_StsParseError = -212;
    public static final int CV_StsUnmatchedFormats = -205;
    public static final int CV_StsUnmatchedSizes = -209;
    public static final int CV_StsUnsupportedFormat = -210;
    public static final int CV_StsVecLengthErr = -28;
    public static final int CV_TERMCRIT_EPS = 2;
    public static final int CV_TERMCRIT_ITER = 1;
    public static final int CV_TERMCRIT_NUMBER = 1;
    public static final String CV_TYPE_NAME_GRAPH = "opencv-graph";
    public static final String CV_TYPE_NAME_IMAGE = "opencv-image";
    public static final String CV_TYPE_NAME_MAT = "opencv-matrix";
    public static final String CV_TYPE_NAME_MATND = "opencv-nd-matrix";
    public static final String CV_TYPE_NAME_SEQ = "opencv-sequence";
    public static final String CV_TYPE_NAME_SEQ_TREE = "opencv-sequence-tree";
    public static final String CV_TYPE_NAME_SPARSE_MAT = "opencv-sparse-matrix";
    public static final int CV_USRTYPE1 = 7;
    public static final String CV_VERSION = CV_VERSION();
    public static final int CV_VERSION_MAJOR = 3;
    public static final int CV_VERSION_MINOR = 1;
    public static final int CV_VERSION_REVISION = 0;
    public static final String CV_VERSION_STATUS = "";
    public static final int CV_VFP = 1;
    public static final CvSlice CV_WHOLE_ARR = cvSlice(0, CV_WHOLE_SEQ_END_INDEX);
    public static final CvSlice CV_WHOLE_SEQ = cvSlice(0, CV_WHOLE_SEQ_END_INDEX);
    public static final int CV_WHOLE_SEQ_END_INDEX = 1073741823;
    public static final int DCT_INVERSE = 1;
    public static final int DCT_ROWS = 4;
    public static final int DECOMP_CHOLESKY = 3;
    public static final int DECOMP_EIG = 2;
    public static final int DECOMP_LU = 0;
    public static final int DECOMP_NORMAL = 16;
    public static final int DECOMP_QR = 4;
    public static final int DECOMP_SVD = 1;
    public static final int DFT_COMPLEX_OUTPUT = 16;
    public static final int DFT_INVERSE = 1;
    public static final int DFT_REAL_OUTPUT = 32;
    public static final int DFT_ROWS = 4;
    public static final int DFT_SCALE = 2;
    public static final int FILLED = -1;
    public static final int FONT_HERSHEY_COMPLEX = 3;
    public static final int FONT_HERSHEY_COMPLEX_SMALL = 5;
    public static final int FONT_HERSHEY_DUPLEX = 2;
    public static final int FONT_HERSHEY_PLAIN = 1;
    public static final int FONT_HERSHEY_SCRIPT_COMPLEX = 7;
    public static final int FONT_HERSHEY_SCRIPT_SIMPLEX = 6;
    public static final int FONT_HERSHEY_SIMPLEX = 0;
    public static final int FONT_HERSHEY_TRIPLEX = 4;
    public static final int FONT_ITALIC = 16;
    public static final int GEMM_1_T = 1;
    public static final int GEMM_2_T = 2;
    public static final int GEMM_3_T = 4;
    public static final int GpuApiCallError = -217;
    public static final int GpuNotSupported = -216;
    public static final int HeaderIsNull = -9;
    public static final int IPL_ALIGN_16BYTES = 16;
    public static final int IPL_ALIGN_32BYTES = 32;
    public static final int IPL_ALIGN_4BYTES = 4;
    public static final int IPL_ALIGN_8BYTES = 8;
    public static final int IPL_ALIGN_DWORD = 4;
    public static final int IPL_ALIGN_QWORD = 8;
    public static final int IPL_BORDER_CONSTANT = 0;
    public static final int IPL_BORDER_REFLECT = 2;
    public static final int IPL_BORDER_REFLECT_101 = 4;
    public static final int IPL_BORDER_REPLICATE = 1;
    public static final int IPL_BORDER_TRANSPARENT = 5;
    public static final int IPL_BORDER_WRAP = 3;
    public static final int IPL_DATA_ORDER_PIXEL = 0;
    public static final int IPL_DATA_ORDER_PLANE = 1;
    public static final int IPL_DEPTH_16S = -2147483632;
    public static final int IPL_DEPTH_16U = 16;
    public static final int IPL_DEPTH_1U = 1;
    public static final int IPL_DEPTH_32F = 32;
    public static final int IPL_DEPTH_32S = -2147483616;
    public static final int IPL_DEPTH_64F = 64;
    public static final int IPL_DEPTH_8S = -2147483640;
    public static final int IPL_DEPTH_8U = 8;
    public static final int IPL_DEPTH_SIGN = Integer.MIN_VALUE;
    public static final int IPL_IMAGE_DATA = 2;
    public static final int IPL_IMAGE_HEADER = 1;
    public static final int IPL_IMAGE_MAGIC_VAL = IPL_IMAGE_MAGIC_VAL();
    public static final int IPL_IMAGE_ROI = 4;
    public static final int IPL_ORIGIN_BL = 1;
    public static final int IPL_ORIGIN_TL = 0;
    public static final int KMEANS_PP_CENTERS = 2;
    public static final int KMEANS_RANDOM_CENTERS = 0;
    public static final int KMEANS_USE_INITIAL_LABELS = 1;
    public static final int LINE_4 = 4;
    public static final int LINE_8 = 8;
    public static final int LINE_AA = 16;
    public static final int MaskIsTiled = -26;
    public static final int NORM_HAMMING = 6;
    public static final int NORM_HAMMING2 = 7;
    public static final int NORM_INF = 1;
    public static final int NORM_L1 = 2;
    public static final int NORM_L2 = 4;
    public static final int NORM_L2SQR = 5;
    public static final int NORM_MINMAX = 32;
    public static final int NORM_RELATIVE = 8;
    public static final int NORM_TYPE_MASK = 7;
    public static final int OPENCV_ABI_COMPATIBILITY = 300;
    public static final int OpenCLApiCallError = -220;
    public static final int OpenCLDoubleNotSupported = -221;
    public static final int OpenCLInitError = -222;
    public static final int OpenCLNoAMDBlasFft = -223;
    public static final int OpenGlApiCallError = -219;
    public static final int OpenGlNotSupported = -218;
    public static final int REDUCE_AVG = 1;
    public static final int REDUCE_MAX = 2;
    public static final int REDUCE_MIN = 3;
    public static final int REDUCE_SUM = 0;
    public static final int SOLVELP_MULTI = 1;
    public static final int SOLVELP_SINGLE = 0;
    public static final int SOLVELP_UNBOUNDED = -2;
    public static final int SOLVELP_UNFEASIBLE = -1;
    public static final int SORT_ASCENDING = 0;
    public static final int SORT_DESCENDING = 16;
    public static final int SORT_EVERY_COLUMN = 1;
    public static final int SORT_EVERY_ROW = 0;
    public static final int StsAssert = -215;
    public static final int StsAutoTrace = -8;
    public static final int StsBackTrace = -1;
    public static final int StsBadArg = -5;
    public static final int StsBadFlag = -206;
    public static final int StsBadFunc = -6;
    public static final int StsBadMask = -208;
    public static final int StsBadMemBlock = -214;
    public static final int StsBadPoint = -207;
    public static final int StsBadSize = -201;
    public static final int StsDivByZero = -202;
    public static final int StsError = -2;
    public static final int StsFilterOffsetErr = -31;
    public static final int StsFilterStructContentErr = -29;
    public static final int StsInplaceNotSupported = -203;
    public static final int StsInternal = -3;
    public static final int StsKernelStructContentErr = -30;
    public static final int StsNoConv = -7;
    public static final int StsNoMem = -4;
    public static final int StsNotImplemented = -213;
    public static final int StsNullPtr = -27;
    public static final int StsObjectNotFound = -204;
    public static final int StsOk = 0;
    public static final int StsOutOfRange = -211;
    public static final int StsParseError = -212;
    public static final int StsUnmatchedFormats = -205;
    public static final int StsUnmatchedSizes = -209;
    public static final int StsUnsupportedFormat = -210;
    public static final int StsVecLengthErr = -28;
    public static final int USAGE_ALLOCATE_DEVICE_MEMORY = 2;
    public static final int USAGE_ALLOCATE_HOST_MEMORY = 1;
    public static final int USAGE_ALLOCATE_SHARED_MEMORY = 4;
    public static final int USAGE_DEFAULT = 0;
    public static final int __UMAT_USAGE_FLAGS_32BIT = Integer.MAX_VALUE;
    public static final String cvFuncName = "";

    @Namespace("cv")
    public static class Algorithm extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void clear();

        @Cast({"bool"})
        public native boolean empty();

        @Str
        public native BytePointer getDefaultName();

        public native void read(@ByRef @Const FileNode fileNode);

        public native void save(@Str String str);

        public native void save(@Str BytePointer bytePointer);

        public native void write(@ByRef FileStorage fileStorage);

        static {
            Loader.load();
        }

        public Algorithm(Pointer p) {
            super(p);
        }

        public Algorithm(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Algorithm position(long position) {
            return (Algorithm) super.position(position);
        }

        public Algorithm() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::ogl")
    @Opaque
    public static class Arrays extends Pointer {
        public Arrays() {
            super((Pointer) null);
        }

        public Arrays(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class AutoLock extends Pointer {
        private native void allocate(@ByRef Mutex mutex);

        static {
            Loader.load();
        }

        public AutoLock(Pointer p) {
            super(p);
        }

        public AutoLock(@ByRef Mutex m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    @Namespace("cv::ogl")
    @Opaque
    public static class Buffer extends Pointer {
        public Buffer() {
            super((Pointer) null);
        }

        public Buffer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class BufferPoolController extends Pointer {
        public native void freeAllReservedBuffers();

        @Cast({"size_t"})
        public native long getMaxReservedSize();

        @Cast({"size_t"})
        public native long getReservedSize();

        public native void setMaxReservedSize(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public BufferPoolController(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::vector<char> >"})
    public static class ByteVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        @Cast({"char"})
        public native byte get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native ByteVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, byte b);

        @ByRef
        @Name({"operator="})
        public native ByteVectorVector put(@ByRef ByteVectorVector byteVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public ByteVectorVector(Pointer p) {
            super(p);
        }

        public ByteVectorVector(byte[]... array) {
            this((long) array.length);
            put(array);
        }

        public ByteVectorVector() {
            allocate();
        }

        public ByteVectorVector(long n) {
            allocate(n);
        }

        public ByteVectorVector put(byte[]... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                if (size((long) i) != ((long) array[i].length)) {
                    resize((long) i, (long) array[i].length);
                }
                for (int j = 0; j < array[i].length; j++) {
                    put((long) i, (long) j, array[i][j]);
                }
            }
            return this;
        }
    }

    @Namespace("cv")
    public static class MinProblemSolver extends Algorithm {

        @Const
        public static class Function extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            @Virtual(true)
            public native double calc(@Const DoublePointer doublePointer);

            @Virtual(true)
            public native int getDims();

            @Virtual
            public native void getGradient(@Const DoublePointer doublePointer, DoublePointer doublePointer2);

            @Virtual
            public native double getGradientEps();

            static {
                Loader.load();
            }

            public Function() {
                super((Pointer) null);
                allocate();
            }

            public Function(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Function(Pointer p) {
                super(p);
            }

            public Function position(long position) {
                return (Function) super.position(position);
            }
        }

        @Ptr
        public native Function getFunction();

        @ByVal
        public native TermCriteria getTermCriteria();

        public native double minimize(@ByVal Mat mat);

        public native double minimize(@ByVal UMat uMat);

        public native void setFunction(@Ptr Function function);

        public native void setTermCriteria(@ByRef @Const TermCriteria termCriteria);

        static {
            Loader.load();
        }

        public MinProblemSolver(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class ConjGradSolver extends MinProblemSolver {
        @Ptr
        public static native ConjGradSolver create();

        @Ptr
        public static native ConjGradSolver create(@Ptr Function function, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::MAX_ITER+cv::TermCriteria::EPS,5000,0.000001)") TermCriteria termCriteria);

        static {
            Loader.load();
        }

        public ConjGradSolver(Pointer p) {
            super(p);
        }
    }

    public static class Cv32suf extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m63f();

        public native Cv32suf m64f(float f);

        public native int m65i();

        public native Cv32suf m66i(int i);

        @Cast({"unsigned"})
        public native int m67u();

        public native Cv32suf m68u(int i);

        static {
            Loader.load();
        }

        public Cv32suf() {
            super((Pointer) null);
            allocate();
        }

        public Cv32suf(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Cv32suf(Pointer p) {
            super(p);
        }

        public Cv32suf position(long position) {
            return (Cv32suf) super.position(position);
        }
    }

    public static class Cv64suf extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native double m69f();

        public native Cv64suf m70f(double d);

        @Cast({"int64"})
        public native long m71i();

        public native Cv64suf m72i(long j);

        @Cast({"uint64"})
        public native int m73u();

        public native Cv64suf m74u(int i);

        static {
            Loader.load();
        }

        public Cv64suf() {
            super((Pointer) null);
            allocate();
        }

        public Cv64suf(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Cv64suf(Pointer p) {
            super(p);
        }

        public Cv64suf position(long position) {
            return (Cv64suf) super.position(position);
        }
    }

    public static class CvAttrList extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer attr(int i);

        @MemberGetter
        @Cast({"const char**"})
        public native PointerPointer attr();

        public native CvAttrList next();

        public native CvAttrList next(CvAttrList cvAttrList);

        static {
            Loader.load();
        }

        public CvAttrList() {
            super((Pointer) null);
            allocate();
        }

        public CvAttrList(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvAttrList(Pointer p) {
            super(p);
        }

        public CvAttrList position(long position) {
            return (CvAttrList) super.position(position);
        }
    }

    @NoOffset
    public static class CvBox2D extends AbstractCvBox2D {
        private native void allocate();

        private native void allocate(@Cast({"CvPoint2D32f*"}) @ByVal(nullValue = "CvPoint2D32f()") FloatBuffer floatBuffer, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f cvSize2D32f, float f);

        private native void allocate(@ByVal(nullValue = "CvPoint2D32f()") CvPoint2D32f cvPoint2D32f, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f cvSize2D32f, float f);

        private native void allocate(@ByRef @Const RotatedRect rotatedRect);

        private native void allocate(@Cast({"CvPoint2D32f*"}) @ByVal(nullValue = "CvPoint2D32f()") float[] fArr, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f cvSize2D32f, float f);

        private native void allocateArray(long j);

        public native float angle();

        public native CvBox2D angle(float f);

        @ByVal
        @Name({"operator cv::RotatedRect"})
        public native RotatedRect asRotatedRect();

        public native CvBox2D center(CvPoint2D32f cvPoint2D32f);

        @ByRef
        public native CvPoint2D32f center();

        public native CvBox2D size(CvSize2D32f cvSize2D32f);

        @ByRef
        public native CvSize2D32f size();

        static {
            Loader.load();
        }

        public CvBox2D(Pointer p) {
            super(p);
        }

        public CvBox2D(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvBox2D position(long position) {
            return (CvBox2D) super.position(position);
        }

        public CvBox2D(@ByVal(nullValue = "CvPoint2D32f()") CvPoint2D32f c, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f s, float a) {
            super((Pointer) null);
            allocate(c, s, a);
        }

        public CvBox2D() {
            super((Pointer) null);
            allocate();
        }

        public CvBox2D(@Cast({"CvPoint2D32f*"}) @ByVal(nullValue = "CvPoint2D32f()") FloatBuffer c, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f s, float a) {
            super((Pointer) null);
            allocate(c, s, a);
        }

        public CvBox2D(@Cast({"CvPoint2D32f*"}) @ByVal(nullValue = "CvPoint2D32f()") float[] c, @ByVal(nullValue = "CvSize2D32f()") CvSize2D32f s, float a) {
            super((Pointer) null);
            allocate(c, s, a);
        }

        public CvBox2D(@ByRef @Const RotatedRect rr) {
            super((Pointer) null);
            allocate(rr);
        }
    }

    public static class CvSeq extends AbstractCvSeq {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeq block_max(BytePointer bytePointer);

        public native int delta_elems();

        public native CvSeq delta_elems(int i);

        public native int elem_size();

        public native CvSeq elem_size(int i);

        public native CvSeq first(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock first();

        public native int flags();

        public native CvSeq flags(int i);

        public native CvSeq free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock free_blocks();

        public native CvSeq h_next();

        public native CvSeq h_next(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native CvSeq h_prev(CvSeq cvSeq);

        public native int header_size();

        public native CvSeq header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeq ptr(BytePointer bytePointer);

        public native CvMemStorage storage();

        public native CvSeq storage(CvMemStorage cvMemStorage);

        public native int total();

        public native CvSeq total(int i);

        public native CvSeq v_next();

        public native CvSeq v_next(CvSeq cvSeq);

        public native CvSeq v_prev();

        public native CvSeq v_prev(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSeq() {
            super((Pointer) null);
            allocate();
        }

        public CvSeq(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSeq(Pointer p) {
            super(p);
        }

        public CvSeq position(long position) {
            return (CvSeq) super.position(position);
        }
    }

    public static class CvChain extends CvSeq {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvChain block_max(BytePointer bytePointer);

        public native int delta_elems();

        public native CvChain delta_elems(int i);

        public native int elem_size();

        public native CvChain elem_size(int i);

        public native CvChain first(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock first();

        public native int flags();

        public native CvChain flags(int i);

        public native CvChain free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock free_blocks();

        public native CvChain h_next(CvSeq cvSeq);

        public native CvSeq h_next();

        public native CvChain h_prev(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native int header_size();

        public native CvChain header_size(int i);

        public native CvChain origin(CvPoint cvPoint);

        @ByRef
        public native CvPoint origin();

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvChain ptr(BytePointer bytePointer);

        public native CvChain storage(CvMemStorage cvMemStorage);

        public native CvMemStorage storage();

        public native int total();

        public native CvChain total(int i);

        public native CvChain v_next(CvSeq cvSeq);

        public native CvSeq v_next();

        public native CvChain v_prev(CvSeq cvSeq);

        public native CvSeq v_prev();

        static {
            Loader.load();
        }

        public CvChain() {
            super((Pointer) null);
            allocate();
        }

        public CvChain(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvChain(Pointer p) {
            super(p);
        }

        public CvChain position(long position) {
            return (CvChain) super.position(position);
        }
    }

    @Convention("CV_CDECL")
    public static class CvCloneFunc extends FunctionPointer {
        private native void allocate();

        public native Pointer call(@Const Pointer pointer);

        static {
            Loader.load();
        }

        public CvCloneFunc(Pointer p) {
            super(p);
        }

        protected CvCloneFunc() {
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvCmpFunc extends FunctionPointer {
        private native void allocate();

        public native int call(@Const Pointer pointer, @Const Pointer pointer2, Pointer pointer3);

        static {
            Loader.load();
        }

        public CvCmpFunc(Pointer p) {
            super(p);
        }

        protected CvCmpFunc() {
            allocate();
        }
    }

    public static class CvContour extends CvSeq {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvContour block_max(BytePointer bytePointer);

        public native int color();

        public native CvContour color(int i);

        public native int delta_elems();

        public native CvContour delta_elems(int i);

        public native int elem_size();

        public native CvContour elem_size(int i);

        public native CvContour first(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock first();

        public native int flags();

        public native CvContour flags(int i);

        public native CvContour free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock free_blocks();

        public native CvContour h_next(CvSeq cvSeq);

        public native CvSeq h_next();

        public native CvContour h_prev(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native int header_size();

        public native CvContour header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvContour ptr(BytePointer bytePointer);

        public native CvContour rect(CvRect cvRect);

        @ByRef
        public native CvRect rect();

        public native int reserved(int i);

        @MemberGetter
        public native IntPointer reserved();

        public native CvContour reserved(int i, int i2);

        public native CvContour storage(CvMemStorage cvMemStorage);

        public native CvMemStorage storage();

        public native int total();

        public native CvContour total(int i);

        public native CvContour v_next(CvSeq cvSeq);

        public native CvSeq v_next();

        public native CvContour v_prev(CvSeq cvSeq);

        public native CvSeq v_prev();

        static {
            Loader.load();
        }

        public CvContour() {
            super((Pointer) null);
            allocate();
        }

        public CvContour(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvContour(Pointer p) {
            super(p);
        }

        public CvContour position(long position) {
            return (CvContour) super.position(position);
        }
    }

    @Convention("CV_CDECL")
    public static class CvErrorCallback extends FunctionPointer {
        private native void allocate();

        public native int call(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2, Pointer pointer);

        static {
            Loader.load();
        }

        public CvErrorCallback(Pointer p) {
            super(p);
        }

        protected CvErrorCallback() {
            allocate();
        }
    }

    public static class CvFileNode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Name({"data.f"})
        public native double data_f();

        public native CvFileNode data_f(double d);

        @Name({"data.i"})
        public native int data_i();

        public native CvFileNode data_i(int i);

        public native CvFileNode data_map(CvFileNodeHash cvFileNodeHash);

        @Name({"data.map"})
        public native CvFileNodeHash data_map();

        public native CvFileNode data_seq(CvSeq cvSeq);

        @Name({"data.seq"})
        public native CvSeq data_seq();

        public native CvFileNode data_str(CvString cvString);

        @ByRef
        @Name({"data.str"})
        public native CvString data_str();

        public native CvFileNode info(CvTypeInfo cvTypeInfo);

        public native CvTypeInfo info();

        public native int tag();

        public native CvFileNode tag(int i);

        static {
            Loader.load();
        }

        public CvFileNode() {
            super((Pointer) null);
            allocate();
        }

        public CvFileNode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvFileNode(Pointer p) {
            super(p);
        }

        public CvFileNode position(long position) {
            return (CvFileNode) super.position(position);
        }
    }

    @Opaque
    public static class CvFileNodeHash extends Pointer {
        public CvFileNodeHash() {
            super((Pointer) null);
        }

        public CvFileNodeHash(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class CvFileStorage extends AbstractCvFileStorage {
        public CvFileStorage() {
            super((Pointer) null);
        }

        public CvFileStorage(Pointer p) {
            super(p);
        }
    }

    public static class CvGraph extends AbstractCvGraph {
        private native void allocate();

        private native void allocateArray(long j);

        public native int active_count();

        public native CvGraph active_count(int i);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvGraph block_max(BytePointer bytePointer);

        public native int delta_elems();

        public native CvGraph delta_elems(int i);

        public native CvGraph edges(CvSet cvSet);

        public native CvSet edges();

        public native int elem_size();

        public native CvGraph elem_size(int i);

        public native CvGraph first(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock first();

        public native int flags();

        public native CvGraph flags(int i);

        public native CvGraph free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock free_blocks();

        public native CvGraph free_elems(CvSetElem cvSetElem);

        public native CvSetElem free_elems();

        public native CvGraph h_next(CvSeq cvSeq);

        public native CvSeq h_next();

        public native CvGraph h_prev(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native int header_size();

        public native CvGraph header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvGraph ptr(BytePointer bytePointer);

        public native CvGraph storage(CvMemStorage cvMemStorage);

        public native CvMemStorage storage();

        public native int total();

        public native CvGraph total(int i);

        public native CvGraph v_next(CvSeq cvSeq);

        public native CvSeq v_next();

        public native CvGraph v_prev(CvSeq cvSeq);

        public native CvSeq v_prev();

        static {
            Loader.load();
        }

        public CvGraph() {
            super((Pointer) null);
            allocate();
        }

        public CvGraph(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvGraph(Pointer p) {
            super(p);
        }

        public CvGraph position(long position) {
            return (CvGraph) super.position(position);
        }
    }

    public static class CvGraphEdge extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int flags();

        public native CvGraphEdge flags(int i);

        @MemberGetter
        @Cast({"CvGraphEdge**"})
        public native PointerPointer next();

        public native CvGraphEdge next(int i);

        public native CvGraphEdge next(int i, CvGraphEdge cvGraphEdge);

        @MemberGetter
        @Cast({"CvGraphVtx**"})
        public native PointerPointer vtx();

        public native CvGraphEdge vtx(int i, CvGraphVtx cvGraphVtx);

        public native CvGraphVtx vtx(int i);

        public native float weight();

        public native CvGraphEdge weight(float f);

        static {
            Loader.load();
        }

        public CvGraphEdge() {
            super((Pointer) null);
            allocate();
        }

        public CvGraphEdge(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvGraphEdge(Pointer p) {
            super(p);
        }

        public CvGraphEdge position(long position) {
            return (CvGraphEdge) super.position(position);
        }
    }

    public static class CvHistogram extends AbstractCvHistogram {
        private native void allocate();

        private native void allocateArray(long j);

        public native opencv_core$CvArr bins();

        public native CvHistogram bins(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

        public native CvHistogram mat(CvMatND cvMatND);

        @ByRef
        public native CvMatND mat();

        public native float thresh(int i, int i2);

        @MemberGetter
        @Cast({"float(*)[2]"})
        public native FloatPointer thresh();

        public native CvHistogram thresh(int i, int i2, float f);

        public native FloatPointer thresh2(int i);

        @MemberGetter
        @Cast({"float**"})
        public native PointerPointer thresh2();

        public native CvHistogram thresh2(int i, FloatPointer floatPointer);

        public native int type();

        public native CvHistogram type(int i);

        static {
            Loader.load();
        }

        public CvHistogram() {
            super((Pointer) null);
            allocate();
        }

        public CvHistogram(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvHistogram(Pointer p) {
            super(p);
        }

        public CvHistogram position(long position) {
            return (CvHistogram) super.position(position);
        }
    }

    @Convention("CV_CDECL")
    public static class CvIsInstanceFunc extends FunctionPointer {
        private native void allocate();

        public native int call(@Const Pointer pointer);

        static {
            Loader.load();
        }

        public CvIsInstanceFunc(Pointer p) {
            super(p);
        }

        protected CvIsInstanceFunc() {
            allocate();
        }
    }

    public static class CvLineIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int err();

        public native CvLineIterator err(int i);

        public native int minus_delta();

        public native CvLineIterator minus_delta(int i);

        public native int minus_step();

        public native CvLineIterator minus_step(int i);

        public native int plus_delta();

        public native CvLineIterator plus_delta(int i);

        public native int plus_step();

        public native CvLineIterator plus_step(int i);

        @Cast({"uchar*"})
        public native BytePointer ptr();

        public native CvLineIterator ptr(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public CvLineIterator() {
            super((Pointer) null);
            allocate();
        }

        public CvLineIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvLineIterator(Pointer p) {
            super(p);
        }

        public CvLineIterator position(long position) {
            return (CvLineIterator) super.position(position);
        }
    }

    @NoOffset
    public static class CvMat extends opencv_core$AbstractCvMat {
        private native void allocate();

        private native void allocate(@ByRef @Const CvMat cvMat);

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocateArray(long j);

        public native int cols();

        public native CvMat cols(int i);

        @Name({"data.db"})
        public native DoublePointer data_db();

        public native CvMat data_db(DoublePointer doublePointer);

        @Name({"data.fl"})
        public native FloatPointer data_fl();

        public native CvMat data_fl(FloatPointer floatPointer);

        @Name({"data.i"})
        public native IntPointer data_i();

        public native CvMat data_i(IntPointer intPointer);

        @Cast({"uchar*"})
        @Name({"data.ptr"})
        public native BytePointer data_ptr();

        public native CvMat data_ptr(BytePointer bytePointer);

        @Name({"data.s"})
        public native ShortPointer data_s();

        public native CvMat data_s(ShortPointer shortPointer);

        public native int hdr_refcount();

        public native CvMat hdr_refcount(int i);

        public native int height();

        public native CvMat height(int i);

        public native IntPointer refcount();

        public native CvMat refcount(IntPointer intPointer);

        public native int rows();

        public native CvMat rows(int i);

        public native int step();

        public native CvMat step(int i);

        public native int type();

        public native CvMat type(int i);

        public native int width();

        public native CvMat width(int i);

        static {
            Loader.load();
        }

        public CvMat(Pointer p) {
            super(p);
        }

        public CvMat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvMat position(long position) {
            return (CvMat) super.position(position);
        }

        public CvMat() {
            super((Pointer) null);
            allocate();
        }

        public CvMat(@ByRef @Const CvMat m) {
            super((Pointer) null);
            allocate(m);
        }

        public CvMat(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    @NoOffset
    public static class CvMatND extends AbstractCvMatND {
        private native void allocate();

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocateArray(long j);

        @Name({"data.db"})
        public native DoublePointer data_db();

        public native CvMatND data_db(DoublePointer doublePointer);

        @Name({"data.fl"})
        public native FloatPointer data_fl();

        public native CvMatND data_fl(FloatPointer floatPointer);

        @Name({"data.i"})
        public native IntPointer data_i();

        public native CvMatND data_i(IntPointer intPointer);

        @Cast({"uchar*"})
        @Name({"data.ptr"})
        public native BytePointer data_ptr();

        public native CvMatND data_ptr(BytePointer bytePointer);

        @Name({"data.s"})
        public native ShortPointer data_s();

        public native CvMatND data_s(ShortPointer shortPointer);

        @Name({"dim", ".size"})
        public native int dim_size(int i);

        public native CvMatND dim_size(int i, int i2);

        @Name({"dim", ".step"})
        public native int dim_step(int i);

        public native CvMatND dim_step(int i, int i2);

        public native int dims();

        public native CvMatND dims(int i);

        public native int hdr_refcount();

        public native CvMatND hdr_refcount(int i);

        public native IntPointer refcount();

        public native CvMatND refcount(IntPointer intPointer);

        public native int type();

        public native CvMatND type(int i);

        static {
            Loader.load();
        }

        public CvMatND(Pointer p) {
            super(p);
        }

        public CvMatND(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvMatND position(long position) {
            return (CvMatND) super.position(position);
        }

        public CvMatND() {
            super((Pointer) null);
            allocate();
        }

        public CvMatND(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    public static class CvModuleInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native CvModuleInfo func_tab(CvPluginFuncInfo cvPluginFuncInfo);

        public native CvPluginFuncInfo func_tab();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native CvModuleInfo next();

        public native CvModuleInfo next(CvModuleInfo cvModuleInfo);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer version();

        static {
            Loader.load();
        }

        public CvModuleInfo() {
            super((Pointer) null);
            allocate();
        }

        public CvModuleInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvModuleInfo(Pointer p) {
            super(p);
        }

        public CvModuleInfo position(long position) {
            return (CvModuleInfo) super.position(position);
        }
    }

    public static class CvNArrayIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int count();

        public native CvNArrayIterator count(int i);

        public native int dims();

        public native CvNArrayIterator dims(int i);

        @MemberGetter
        @Cast({"CvMatND**"})
        public native PointerPointer hdr();

        public native CvMatND hdr(int i);

        public native CvNArrayIterator hdr(int i, CvMatND cvMatND);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i);

        @MemberGetter
        @Cast({"uchar**"})
        public native PointerPointer ptr();

        public native CvNArrayIterator ptr(int i, BytePointer bytePointer);

        public native CvNArrayIterator size(CvSize cvSize);

        @ByRef
        public native CvSize size();

        public native int stack(int i);

        @MemberGetter
        public native IntPointer stack();

        public native CvNArrayIterator stack(int i, int i2);

        static {
            Loader.load();
        }

        public CvNArrayIterator() {
            super((Pointer) null);
            allocate();
        }

        public CvNArrayIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvNArrayIterator(Pointer p) {
            super(p);
        }

        public CvNArrayIterator position(long position) {
            return (CvNArrayIterator) super.position(position);
        }
    }

    public static class CvPluginFuncInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native Pointer default_func_addr();

        public native CvPluginFuncInfo default_func_addr(Pointer pointer);

        public native Pointer func_addr(int i);

        @MemberGetter
        @Cast({"void**"})
        public native PointerPointer func_addr();

        public native CvPluginFuncInfo func_addr(int i, Pointer pointer);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer func_names();

        public native int loaded_from();

        public native CvPluginFuncInfo loaded_from(int i);

        public native int search_modules();

        public native CvPluginFuncInfo search_modules(int i);

        static {
            Loader.load();
        }

        public CvPluginFuncInfo() {
            super((Pointer) null);
            allocate();
        }

        public CvPluginFuncInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvPluginFuncInfo(Pointer p) {
            super(p);
        }

        public CvPluginFuncInfo position(long position) {
            return (CvPluginFuncInfo) super.position(position);
        }
    }

    @NoOffset
    public static class CvPoint2D32f extends AbstractCvPoint2D32f {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        public native float m75x();

        public native CvPoint2D32f m76x(float f);

        public native float m77y();

        public native CvPoint2D32f m78y(float f);

        static {
            Loader.load();
        }

        public CvPoint2D32f(Pointer p) {
            super(p);
        }

        public CvPoint2D32f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvPoint2D32f position(long position) {
            return (CvPoint2D32f) super.position(position);
        }

        public CvPoint2D32f(float _x, float _y) {
            super((Pointer) null);
            allocate(_x, _y);
        }

        public CvPoint2D32f() {
            super((Pointer) null);
            allocate();
        }
    }

    @NoOffset
    public static class CvPoint3D32f extends AbstractCvPoint3D32f {
        private native void allocate();

        private native void allocate(float f, float f2, float f3);

        private native void allocateArray(long j);

        public native float m79x();

        public native CvPoint3D32f m80x(float f);

        public native float m81y();

        public native CvPoint3D32f m82y(float f);

        public native float m83z();

        public native CvPoint3D32f m84z(float f);

        static {
            Loader.load();
        }

        public CvPoint3D32f(Pointer p) {
            super(p);
        }

        public CvPoint3D32f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvPoint3D32f position(long position) {
            return (CvPoint3D32f) super.position(position);
        }

        public CvPoint3D32f(float _x, float _y, float _z) {
            super((Pointer) null);
            allocate(_x, _y, _z);
        }

        public CvPoint3D32f() {
            super((Pointer) null);
            allocate();
        }
    }

    @NoOffset
    public static class CvPoint extends AbstractCvPoint {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocateArray(long j);

        public native int m85x();

        public native CvPoint m86x(int i);

        public native int m87y();

        public native CvPoint m88y(int i);

        static {
            Loader.load();
        }

        public CvPoint(Pointer p) {
            super(p);
        }

        public CvPoint(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvPoint position(long position) {
            return (CvPoint) super.position(position);
        }

        public CvPoint(int _x, int _y) {
            super((Pointer) null);
            allocate(_x, _y);
        }

        public CvPoint() {
            super((Pointer) null);
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvReadFunc extends FunctionPointer {
        private native void allocate();

        public native Pointer call(CvFileStorage cvFileStorage, CvFileNode cvFileNode);

        static {
            Loader.load();
        }

        public CvReadFunc(Pointer p) {
            super(p);
        }

        protected CvReadFunc() {
            allocate();
        }
    }

    @NoOffset
    public static class CvRect extends AbstractCvRect {
        private native void allocate();

        private native void allocate(int i, int i2, int i3, int i4);

        private native void allocateArray(long j);

        public native int height();

        public native CvRect height(int i);

        public native int width();

        public native CvRect width(int i);

        public native int m89x();

        public native CvRect m90x(int i);

        public native int m91y();

        public native CvRect m92y(int i);

        static {
            Loader.load();
        }

        public CvRect(Pointer p) {
            super(p);
        }

        public CvRect(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvRect position(long position) {
            return (CvRect) super.position(position);
        }

        public CvRect(int _x, int _y, int w, int h) {
            super((Pointer) null);
            allocate(_x, _y, w, h);
        }

        public CvRect() {
            super((Pointer) null);
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvReleaseFunc extends FunctionPointer {
        private native void allocate();

        public native void call(@ByPtrPtr @Cast({"void**"}) Pointer pointer);

        static {
            Loader.load();
        }

        public CvReleaseFunc(Pointer p) {
            super(p);
        }

        protected CvReleaseFunc() {
            allocate();
        }
    }

    @NoOffset
    public static class CvScalar extends AbstractCvScalar {
        private native void allocate();

        private native void allocate(double d);

        private native void allocate(double d, double d2, double d3, double d4);

        private native void allocateArray(long j);

        public native double val(int i);

        @MemberGetter
        public native DoublePointer val();

        public native CvScalar val(int i, double d);

        static {
            Loader.load();
        }

        public CvScalar(Pointer p) {
            super(p);
        }

        public CvScalar(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvScalar position(long position) {
            return (CvScalar) super.position(position);
        }

        public CvScalar() {
            super((Pointer) null);
            allocate();
        }

        public CvScalar(double d0, double d1, double d2, double d3) {
            super((Pointer) null);
            allocate(d0, d1, d2, d3);
        }

        public CvScalar(double d0) {
            super((Pointer) null);
            allocate(d0);
        }
    }

    public static class CvSeqBlock extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int count();

        public native CvSeqBlock count(int i);

        @Cast({"schar*"})
        public native BytePointer data();

        public native CvSeqBlock data(BytePointer bytePointer);

        public native CvSeqBlock next();

        public native CvSeqBlock next(CvSeqBlock cvSeqBlock);

        public native CvSeqBlock prev();

        public native CvSeqBlock prev(CvSeqBlock cvSeqBlock);

        public native int start_index();

        public native CvSeqBlock start_index(int i);

        static {
            Loader.load();
        }

        public CvSeqBlock() {
            super((Pointer) null);
            allocate();
        }

        public CvSeqBlock(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSeqBlock(Pointer p) {
            super(p);
        }

        public CvSeqBlock position(long position) {
            return (CvSeqBlock) super.position(position);
        }
    }

    public static class CvSeqReader extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native CvSeqBlock block();

        public native CvSeqReader block(CvSeqBlock cvSeqBlock);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeqReader block_max(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer block_min();

        public native CvSeqReader block_min(BytePointer bytePointer);

        public native int delta_index();

        public native CvSeqReader delta_index(int i);

        public native int header_size();

        public native CvSeqReader header_size(int i);

        @Cast({"schar*"})
        public native BytePointer prev_elem();

        public native CvSeqReader prev_elem(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeqReader ptr(BytePointer bytePointer);

        public native CvSeq seq();

        public native CvSeqReader seq(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSeqReader() {
            super((Pointer) null);
            allocate();
        }

        public CvSeqReader(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSeqReader(Pointer p) {
            super(p);
        }

        public CvSeqReader position(long position) {
            return (CvSeqReader) super.position(position);
        }
    }

    public static class CvSeqWriter extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native CvSeqBlock block();

        public native CvSeqWriter block(CvSeqBlock cvSeqBlock);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSeqWriter block_max(BytePointer bytePointer);

        @Cast({"schar*"})
        public native BytePointer block_min();

        public native CvSeqWriter block_min(BytePointer bytePointer);

        public native int header_size();

        public native CvSeqWriter header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSeqWriter ptr(BytePointer bytePointer);

        public native CvSeq seq();

        public native CvSeqWriter seq(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSeqWriter() {
            super((Pointer) null);
            allocate();
        }

        public CvSeqWriter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSeqWriter(Pointer p) {
            super(p);
        }

        public CvSeqWriter position(long position) {
            return (CvSeqWriter) super.position(position);
        }
    }

    public static class CvSet extends AbstractCvSet {
        private native void allocate();

        private native void allocateArray(long j);

        public native int active_count();

        public native CvSet active_count(int i);

        @Cast({"schar*"})
        public native BytePointer block_max();

        public native CvSet block_max(BytePointer bytePointer);

        public native int delta_elems();

        public native CvSet delta_elems(int i);

        public native int elem_size();

        public native CvSet elem_size(int i);

        public native CvSeqBlock first();

        public native CvSet first(CvSeqBlock cvSeqBlock);

        public native int flags();

        public native CvSet flags(int i);

        public native CvSeqBlock free_blocks();

        public native CvSet free_blocks(CvSeqBlock cvSeqBlock);

        public native CvSet free_elems(CvSetElem cvSetElem);

        public native CvSetElem free_elems();

        public native CvSeq h_next();

        public native CvSet h_next(CvSeq cvSeq);

        public native CvSeq h_prev();

        public native CvSet h_prev(CvSeq cvSeq);

        public native int header_size();

        public native CvSet header_size(int i);

        @Cast({"schar*"})
        public native BytePointer ptr();

        public native CvSet ptr(BytePointer bytePointer);

        public native CvMemStorage storage();

        public native CvSet storage(CvMemStorage cvMemStorage);

        public native int total();

        public native CvSet total(int i);

        public native CvSeq v_next();

        public native CvSet v_next(CvSeq cvSeq);

        public native CvSeq v_prev();

        public native CvSet v_prev(CvSeq cvSeq);

        static {
            Loader.load();
        }

        public CvSet() {
            super((Pointer) null);
            allocate();
        }

        public CvSet(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSet(Pointer p) {
            super(p);
        }

        public CvSet position(long position) {
            return (CvSet) super.position(position);
        }
    }

    @NoOffset
    public static class CvSize2D32f extends AbstractCvSize2D32f {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        public native float height();

        public native CvSize2D32f height(float f);

        public native float width();

        public native CvSize2D32f width(float f);

        static {
            Loader.load();
        }

        public CvSize2D32f(Pointer p) {
            super(p);
        }

        public CvSize2D32f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSize2D32f position(long position) {
            return (CvSize2D32f) super.position(position);
        }

        public CvSize2D32f(float w, float h) {
            super((Pointer) null);
            allocate(w, h);
        }

        public CvSize2D32f() {
            super((Pointer) null);
            allocate();
        }
    }

    @NoOffset
    public static class CvSize extends AbstractCvSize {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocateArray(long j);

        public native int height();

        public native CvSize height(int i);

        public native int width();

        public native CvSize width(int i);

        static {
            Loader.load();
        }

        public CvSize(Pointer p) {
            super(p);
        }

        public CvSize(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSize position(long position) {
            return (CvSize) super.position(position);
        }

        public CvSize(int w, int h) {
            super((Pointer) null);
            allocate(w, h);
        }

        public CvSize() {
            super((Pointer) null);
            allocate();
        }
    }

    @NoOffset
    public static class CvSlice extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(@ByRef @Const Range range);

        private native void allocateArray(long j);

        @ByVal
        @Name({"operator cv::Range"})
        public native Range asRange();

        public native int end_index();

        public native CvSlice end_index(int i);

        public native int start_index();

        public native CvSlice start_index(int i);

        static {
            Loader.load();
        }

        public CvSlice(Pointer p) {
            super(p);
        }

        public CvSlice(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSlice position(long position) {
            return (CvSlice) super.position(position);
        }

        public CvSlice(int start, int end) {
            super((Pointer) null);
            allocate(start, end);
        }

        public CvSlice() {
            super((Pointer) null);
            allocate();
        }

        public CvSlice(@ByRef @Const Range r) {
            super((Pointer) null);
            allocate(r);
        }
    }

    public static class CvSparseMat extends AbstractCvSparseMat {
        private native void allocate();

        private native void allocateArray(long j);

        public native void copyToSparseMat(@ByRef SparseMat sparseMat);

        public native int dims();

        public native CvSparseMat dims(int i);

        public native int hashsize();

        public native CvSparseMat hashsize(int i);

        public native Pointer hashtable(int i);

        @MemberGetter
        @Cast({"void**"})
        public native PointerPointer hashtable();

        public native CvSparseMat hashtable(int i, Pointer pointer);

        public native int hdr_refcount();

        public native CvSparseMat hdr_refcount(int i);

        public native CvSet heap();

        public native CvSparseMat heap(CvSet cvSet);

        public native int idxoffset();

        public native CvSparseMat idxoffset(int i);

        public native IntPointer refcount();

        public native CvSparseMat refcount(IntPointer intPointer);

        public native int size(int i);

        @MemberGetter
        public native IntPointer size();

        public native CvSparseMat size(int i, int i2);

        public native int type();

        public native CvSparseMat type(int i);

        public native int valoffset();

        public native CvSparseMat valoffset(int i);

        static {
            Loader.load();
        }

        public CvSparseMat() {
            super((Pointer) null);
            allocate();
        }

        public CvSparseMat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSparseMat(Pointer p) {
            super(p);
        }

        public CvSparseMat position(long position) {
            return (CvSparseMat) super.position(position);
        }
    }

    public static class CvSparseNode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned"})
        public native int hashval();

        public native CvSparseNode hashval(int i);

        public native CvSparseNode next();

        public native CvSparseNode next(CvSparseNode cvSparseNode);

        static {
            Loader.load();
        }

        public CvSparseNode() {
            super((Pointer) null);
            allocate();
        }

        public CvSparseNode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvSparseNode(Pointer p) {
            super(p);
        }

        public CvSparseNode position(long position) {
            return (CvSparseNode) super.position(position);
        }
    }

    public static class CvString extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int len();

        public native CvString len(int i);

        @Cast({"char*"})
        public native BytePointer ptr();

        public native CvString ptr(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public CvString() {
            super((Pointer) null);
            allocate();
        }

        public CvString(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvString(Pointer p) {
            super(p);
        }

        public CvString position(long position) {
            return (CvString) super.position(position);
        }
    }

    public static class CvStringHashNode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned"})
        public native int hashval();

        public native CvStringHashNode hashval(int i);

        public native CvStringHashNode next();

        public native CvStringHashNode next(CvStringHashNode cvStringHashNode);

        @ByRef
        public native CvString str();

        public native CvStringHashNode str(CvString cvString);

        static {
            Loader.load();
        }

        public CvStringHashNode() {
            super((Pointer) null);
            allocate();
        }

        public CvStringHashNode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvStringHashNode(Pointer p) {
            super(p);
        }

        public CvStringHashNode position(long position) {
            return (CvStringHashNode) super.position(position);
        }
    }

    @NoOffset
    public static class CvTermCriteria extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2, double d);

        private native void allocate(@ByRef @Const TermCriteria termCriteria);

        private native void allocateArray(long j);

        @ByVal
        @Name({"operator cv::TermCriteria"})
        public native TermCriteria asTermCriteria();

        public native double epsilon();

        public native CvTermCriteria epsilon(double d);

        public native int max_iter();

        public native CvTermCriteria max_iter(int i);

        public native int type();

        public native CvTermCriteria type(int i);

        static {
            Loader.load();
        }

        public CvTermCriteria(Pointer p) {
            super(p);
        }

        public CvTermCriteria(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvTermCriteria position(long position) {
            return (CvTermCriteria) super.position(position);
        }

        public CvTermCriteria(int _type, int _iter, double _eps) {
            super((Pointer) null);
            allocate(_type, _iter, _eps);
        }

        public CvTermCriteria() {
            super((Pointer) null);
            allocate();
        }

        public CvTermCriteria(@ByRef @Const TermCriteria t) {
            super((Pointer) null);
            allocate(t);
        }
    }

    public static class CvTreeNodeIterator extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int level();

        public native CvTreeNodeIterator level(int i);

        public native int max_level();

        public native CvTreeNodeIterator max_level(int i);

        @MemberGetter
        @Const
        public native Pointer node();

        static {
            Loader.load();
        }

        public CvTreeNodeIterator() {
            super((Pointer) null);
            allocate();
        }

        public CvTreeNodeIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvTreeNodeIterator(Pointer p) {
            super(p);
        }

        public CvTreeNodeIterator position(long position) {
            return (CvTreeNodeIterator) super.position(position);
        }
    }

    @NoOffset
    public static class CvType extends Pointer {
        private native void allocate(String str, CvIsInstanceFunc cvIsInstanceFunc);

        private native void allocate(String str, CvIsInstanceFunc cvIsInstanceFunc, CvReleaseFunc cvReleaseFunc, CvReadFunc cvReadFunc, CvWriteFunc cvWriteFunc, CvCloneFunc cvCloneFunc);

        private native void allocate(@Cast({"const char*"}) BytePointer bytePointer, CvIsInstanceFunc cvIsInstanceFunc);

        private native void allocate(@Cast({"const char*"}) BytePointer bytePointer, CvIsInstanceFunc cvIsInstanceFunc, CvReleaseFunc cvReleaseFunc, CvReadFunc cvReadFunc, CvWriteFunc cvWriteFunc, CvCloneFunc cvCloneFunc);

        public native CvType info(CvTypeInfo cvTypeInfo);

        public native CvTypeInfo info();

        static {
            Loader.load();
        }

        public CvType(Pointer p) {
            super(p);
        }

        public CvType(@Cast({"const char*"}) BytePointer type_name, CvIsInstanceFunc is_instance, CvReleaseFunc release, CvReadFunc read, CvWriteFunc write, CvCloneFunc clone) {
            super((Pointer) null);
            allocate(type_name, is_instance, release, read, write, clone);
        }

        public CvType(@Cast({"const char*"}) BytePointer type_name, CvIsInstanceFunc is_instance) {
            super((Pointer) null);
            allocate(type_name, is_instance);
        }

        public CvType(String type_name, CvIsInstanceFunc is_instance, CvReleaseFunc release, CvReadFunc read, CvWriteFunc write, CvCloneFunc clone) {
            super((Pointer) null);
            allocate(type_name, is_instance, release, read, write, clone);
        }

        public CvType(String type_name, CvIsInstanceFunc is_instance) {
            super((Pointer) null);
            allocate(type_name, is_instance);
        }
    }

    public static class CvTypeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native CvCloneFunc clone();

        public native CvTypeInfo clone(CvCloneFunc cvCloneFunc);

        public native int flags();

        public native CvTypeInfo flags(int i);

        public native int header_size();

        public native CvTypeInfo header_size(int i);

        public native CvIsInstanceFunc is_instance();

        public native CvTypeInfo is_instance(CvIsInstanceFunc cvIsInstanceFunc);

        public native CvTypeInfo next();

        public native CvTypeInfo next(CvTypeInfo cvTypeInfo);

        public native CvTypeInfo prev();

        public native CvTypeInfo prev(CvTypeInfo cvTypeInfo);

        public native CvReadFunc read();

        public native CvTypeInfo read(CvReadFunc cvReadFunc);

        public native CvReleaseFunc release();

        public native CvTypeInfo release(CvReleaseFunc cvReleaseFunc);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer type_name();

        public native CvTypeInfo write(CvWriteFunc cvWriteFunc);

        public native CvWriteFunc write();

        static {
            Loader.load();
        }

        public CvTypeInfo() {
            super((Pointer) null);
            allocate();
        }

        public CvTypeInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvTypeInfo(Pointer p) {
            super(p);
        }

        public CvTypeInfo position(long position) {
            return (CvTypeInfo) super.position(position);
        }
    }

    @Convention("CV_CDECL")
    public static class CvWriteFunc extends FunctionPointer {
        private native void allocate();

        public native void call(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Const Pointer pointer, @ByVal CvAttrList cvAttrList);

        static {
            Loader.load();
        }

        public CvWriteFunc(Pointer p) {
            super(p);
        }

        protected CvWriteFunc() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplAllocateImageData extends FunctionPointer {
        private native void allocate();

        public native void call(IplImage iplImage, int i, int i2);

        static {
            Loader.load();
        }

        public Cv_iplAllocateImageData(Pointer p) {
            super(p);
        }

        protected Cv_iplAllocateImageData() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCloneImage extends FunctionPointer {
        private native void allocate();

        public native IplImage call(@Const IplImage iplImage);

        static {
            Loader.load();
        }

        public Cv_iplCloneImage(Pointer p) {
            super(p);
        }

        protected Cv_iplCloneImage() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCreateImageHeader extends FunctionPointer {
        private native void allocate();

        public native IplImage call(int i, int i2, int i3, @Cast({"char*"}) BytePointer bytePointer, @Cast({"char*"}) BytePointer bytePointer2, int i4, int i5, int i6, int i7, int i8, IplROI iplROI, IplImage iplImage, Pointer pointer, IplTileInfo iplTileInfo);

        static {
            Loader.load();
        }

        public Cv_iplCreateImageHeader(Pointer p) {
            super(p);
        }

        protected Cv_iplCreateImageHeader() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplCreateROI extends FunctionPointer {
        private native void allocate();

        public native IplROI call(int i, int i2, int i3, int i4, int i5);

        static {
            Loader.load();
        }

        public Cv_iplCreateROI(Pointer p) {
            super(p);
        }

        protected Cv_iplCreateROI() {
            allocate();
        }
    }

    @Convention("CV_STDCALL")
    public static class Cv_iplDeallocate extends FunctionPointer {
        private native void allocate();

        public native void call(IplImage iplImage, int i);

        static {
            Loader.load();
        }

        public Cv_iplDeallocate(Pointer p) {
            super(p);
        }

        protected Cv_iplDeallocate() {
            allocate();
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class DMatch extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2, float f);

        private native void allocate(int i, int i2, int i3, float f);

        private native void allocateArray(long j);

        public native float distance();

        public native DMatch distance(float f);

        public native int imgIdx();

        public native DMatch imgIdx(int i);

        @Cast({"bool"})
        @Name({"operator <"})
        public native boolean lessThan(@ByRef @Const DMatch dMatch);

        public native int queryIdx();

        public native DMatch queryIdx(int i);

        public native int trainIdx();

        public native DMatch trainIdx(int i);

        static {
            Loader.load();
        }

        public DMatch(Pointer p) {
            super(p);
        }

        public DMatch(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DMatch position(long position) {
            return (DMatch) super.position(position);
        }

        public DMatch() {
            super((Pointer) null);
            allocate();
        }

        public DMatch(int _queryIdx, int _trainIdx, float _distance) {
            super((Pointer) null);
            allocate(_queryIdx, _trainIdx, _distance);
        }

        public DMatch(int _queryIdx, int _trainIdx, int _imgIdx, float _distance) {
            super((Pointer) null);
            allocate(_queryIdx, _trainIdx, _imgIdx, _distance);
        }
    }

    @Name({"std::vector<cv::DMatch>"})
    public static class DMatchVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native DMatch get(@Cast({"size_t"}) long j);

        public native DMatchVector put(@Cast({"size_t"}) long j, DMatch dMatch);

        @ByRef
        @Name({"operator="})
        public native DMatchVector put(@ByRef DMatchVector dMatchVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public DMatchVector(Pointer p) {
            super(p);
        }

        public DMatchVector(DMatch... array) {
            this((long) array.length);
            put(array);
        }

        public DMatchVector() {
            allocate();
        }

        public DMatchVector(long n) {
            allocate(n);
        }

        public DMatchVector put(DMatch... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::DMatch> >"})
    public static class DMatchVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native DMatchVector get(@Cast({"size_t"}) long j);

        public native DMatchVectorVector put(@Cast({"size_t"}) long j, DMatchVector dMatchVector);

        @ByRef
        @Name({"operator="})
        public native DMatchVectorVector put(@ByRef DMatchVectorVector dMatchVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public DMatchVectorVector(Pointer p) {
            super(p);
        }

        public DMatchVectorVector(DMatchVector... array) {
            this((long) array.length);
            put(array);
        }

        public DMatchVectorVector() {
            allocate();
        }

        public DMatchVectorVector(long n) {
            allocate(n);
        }

        public DMatchVectorVector put(DMatchVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv")
    public static class DownhillSolver extends MinProblemSolver {
        @Ptr
        public static native DownhillSolver create();

        @Ptr
        public static native DownhillSolver create(@Ptr Function function, @ByVal(nullValue = "cv::InputArray(cv::Mat_<double>(1,1,0.0))") Mat mat, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::MAX_ITER+cv::TermCriteria::EPS,5000,0.000001)") TermCriteria termCriteria);

        @Ptr
        public static native DownhillSolver create(@Ptr Function function, @ByVal(nullValue = "cv::InputArray(cv::Mat_<double>(1,1,0.0))") UMat uMat, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::MAX_ITER+cv::TermCriteria::EPS,5000,0.000001)") TermCriteria termCriteria);

        public native void getInitStep(@ByVal Mat mat);

        public native void getInitStep(@ByVal UMat uMat);

        public native void setInitStep(@ByVal Mat mat);

        public native void setInitStep(@ByVal UMat uMat);

        static {
            Loader.load();
        }

        public DownhillSolver(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::cuda")
    @Opaque
    public static class Event extends Pointer {
        public Event() {
            super((Pointer) null);
        }

        public Event(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class FileNode extends Pointer {
        public static final int EMPTY = 32;
        public static final int FLOAT = 2;
        public static final int FLOW = 8;
        public static final int INT = 1;
        public static final int MAP = 6;
        public static final int NAMED = 64;
        public static final int NONE = 0;
        public static final int REAL = 2;
        public static final int REF = 4;
        public static final int SEQ = 5;
        public static final int STR = 3;
        public static final int STRING = 3;
        public static final int TYPE_MASK = 7;
        public static final int USER = 16;

        private native void allocate();

        private native void allocate(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode);

        private native void allocate(@ByRef @Const FileNode fileNode);

        private native void allocateArray(long j);

        @Str
        @Name({"operator cv::String"})
        public native BytePointer asBytePointer();

        @Name({"operator double"})
        public native double asDouble();

        @Name({"operator float"})
        public native float asFloat();

        @Name({"operator int"})
        public native int asInt();

        @ByVal
        public native FileNodeIterator begin();

        @Cast({"bool"})
        public native boolean empty();

        @ByVal
        public native FileNodeIterator end();

        @MemberGetter
        @Const
        public native CvFileStorage fs();

        @ByVal
        @Name({"operator []"})
        public native FileNode get(int i);

        @ByVal
        @Name({"operator []"})
        public native FileNode get(@Str String str);

        @ByVal
        @Name({"operator []"})
        public native FileNode get(@Str BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean isInt();

        @Cast({"bool"})
        public native boolean isMap();

        @Cast({"bool"})
        public native boolean isNamed();

        @Cast({"bool"})
        public native boolean isNone();

        @Cast({"bool"})
        public native boolean isReal();

        @Cast({"bool"})
        public native boolean isSeq();

        @Cast({"bool"})
        public native boolean isString();

        @Name({"operator *"})
        public native CvFileNode multiply();

        @Str
        public native BytePointer name();

        @MemberGetter
        @Const
        public native CvFileNode node();

        public native Pointer readObj();

        public native void readRaw(@Str String str, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        public native void readRaw(@Str String str, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j);

        public native void readRaw(@Str String str, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        public native void readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        public native void readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j);

        public native void readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        @Cast({"size_t"})
        public native long size();

        public native int type();

        static {
            Loader.load();
        }

        public FileNode(Pointer p) {
            super(p);
        }

        public FileNode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FileNode position(long position) {
            return (FileNode) super.position(position);
        }

        public FileNode() {
            super((Pointer) null);
            allocate();
        }

        public FileNode(@Const CvFileStorage fs, @Const CvFileNode node) {
            super((Pointer) null);
            allocate(fs, node);
        }

        public FileNode(@ByRef @Const FileNode node) {
            super((Pointer) null);
            allocate(node);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class FileNodeIterator extends Pointer {

        public static class SeqReader extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            public native Pointer block();

            public native SeqReader block(Pointer pointer);

            @Cast({"schar*"})
            public native BytePointer block_max();

            public native SeqReader block_max(BytePointer bytePointer);

            @Cast({"schar*"})
            public native BytePointer block_min();

            public native SeqReader block_min(BytePointer bytePointer);

            public native int delta_index();

            public native SeqReader delta_index(int i);

            public native int header_size();

            public native SeqReader header_size(int i);

            @Cast({"schar*"})
            public native BytePointer prev_elem();

            public native SeqReader prev_elem(BytePointer bytePointer);

            @Cast({"schar*"})
            public native BytePointer ptr();

            public native SeqReader ptr(BytePointer bytePointer);

            public native Pointer seq();

            public native SeqReader seq(Pointer pointer);

            static {
                Loader.load();
            }

            public SeqReader() {
                super((Pointer) null);
                allocate();
            }

            public SeqReader(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public SeqReader(Pointer p) {
                super(p);
            }

            public SeqReader position(long position) {
                return (SeqReader) super.position(position);
            }
        }

        private native void allocate();

        private native void allocate(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode);

        private native void allocate(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"size_t"}) long j);

        private native void allocate(@ByRef @Const FileNodeIterator fileNodeIterator);

        private native void allocateArray(long j);

        @ByVal
        @Name({"operator ->"})
        public native FileNode access();

        @ByRef
        @Name({"operator +="})
        public native FileNodeIterator addPut(int i);

        @MemberGetter
        @Const
        public native CvFileNode container();

        @ByRef
        @Name({"operator --"})
        public native FileNodeIterator decrement();

        @ByVal
        @Name({"operator --"})
        public native FileNodeIterator decrement(int i);

        @MemberGetter
        @Const
        public native CvFileStorage fs();

        @ByRef
        @Name({"operator ++"})
        public native FileNodeIterator increment();

        @ByVal
        @Name({"operator ++"})
        public native FileNodeIterator increment(int i);

        @ByVal
        @Name({"operator *"})
        public native FileNode multiply();

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) ByteBuffer byteBuffer);

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) BytePointer bytePointer);

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j);

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) byte[] bArr);

        @ByRef
        public native FileNodeIterator readRaw(@Str String str, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) ByteBuffer byteBuffer);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) BytePointer bytePointer2);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) byte[] bArr);

        @ByRef
        public native FileNodeIterator readRaw(@Str BytePointer bytePointer, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        @ByRef
        public native SeqReader reader();

        public native FileNodeIterator reader(SeqReader seqReader);

        @Cast({"size_t"})
        public native long remaining();

        public native FileNodeIterator remaining(long j);

        @ByRef
        @Name({"operator -="})
        public native FileNodeIterator subtractPut(int i);

        static {
            Loader.load();
        }

        public FileNodeIterator(Pointer p) {
            super(p);
        }

        public FileNodeIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FileNodeIterator position(long position) {
            return (FileNodeIterator) super.position(position);
        }

        public FileNodeIterator() {
            super((Pointer) null);
            allocate();
        }

        public FileNodeIterator(@Const CvFileStorage fs, @Const CvFileNode node, @Cast({"size_t"}) long ofs) {
            super((Pointer) null);
            allocate(fs, node, ofs);
        }

        public FileNodeIterator(@Const CvFileStorage fs, @Const CvFileNode node) {
            super((Pointer) null);
            allocate(fs, node);
        }

        public FileNodeIterator(@ByRef @Const FileNodeIterator it) {
            super((Pointer) null);
            allocate(it);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class FileStorage extends Pointer {
        public static final int APPEND = 2;
        public static final int FORMAT_AUTO = 0;
        public static final int FORMAT_MASK = 56;
        public static final int FORMAT_XML = 8;
        public static final int FORMAT_YAML = 16;
        public static final int INSIDE_MAP = 4;
        public static final int MEMORY = 4;
        public static final int NAME_EXPECTED = 2;
        public static final int READ = 0;
        public static final int UNDEFINED = 0;
        public static final int VALUE_EXPECTED = 1;
        public static final int WRITE = 1;

        private native void allocate();

        private native void allocate(@Str String str, int i);

        private native void allocate(@Str String str, int i, @Str String str2);

        private native void allocate(@Str BytePointer bytePointer, int i);

        private native void allocate(@Str BytePointer bytePointer, int i, @Str BytePointer bytePointer2);

        private native void allocate(CvFileStorage cvFileStorage);

        private native void allocate(CvFileStorage cvFileStorage, @Cast({"bool"}) boolean z);

        private native void allocateArray(long j);

        @Str
        public static native String getDefaultObjectName(@Str String str);

        @Str
        public static native BytePointer getDefaultObjectName(@Str BytePointer bytePointer);

        @Str
        public native BytePointer elname();

        public native FileStorage elname(BytePointer bytePointer);

        @Ptr
        public native CvFileStorage fs();

        public native FileStorage fs(CvFileStorage cvFileStorage);

        @ByVal
        @Name({"operator []"})
        public native FileNode get(@Str String str);

        @ByVal
        @Name({"operator []"})
        public native FileNode get(@Str BytePointer bytePointer);

        @ByVal
        public native FileNode getFirstTopLevelNode();

        @Cast({"bool"})
        public native boolean isOpened();

        @Name({"operator *"})
        public native CvFileStorage multiply();

        @Cast({"bool"})
        public native boolean open(@Str String str, int i);

        @Cast({"bool"})
        public native boolean open(@Str String str, int i, @Str String str2);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer, int i);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer, int i, @Str BytePointer bytePointer2);

        public native void release();

        @Str
        public native BytePointer releaseAndGetString();

        @ByVal
        public native FileNode root();

        @ByVal
        public native FileNode root(int i);

        public native int state();

        public native FileStorage state(int i);

        @Cast({"char*"})
        @StdVector
        public native BytePointer structs();

        public native FileStorage structs(BytePointer bytePointer);

        public native void writeObj(@Str String str, @Const Pointer pointer);

        public native void writeObj(@Str BytePointer bytePointer, @Const Pointer pointer);

        public native void writeRaw(@Str String str, @Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        public native void writeRaw(@Str String str, @Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j);

        public native void writeRaw(@Str String str, @Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        public native void writeRaw(@Str BytePointer bytePointer, @Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j);

        public native void writeRaw(@Str BytePointer bytePointer, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j);

        public native void writeRaw(@Str BytePointer bytePointer, @Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public FileStorage(Pointer p) {
            super(p);
        }

        public FileStorage(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FileStorage position(long position) {
            return (FileStorage) super.position(position);
        }

        public FileStorage() {
            super((Pointer) null);
            allocate();
        }

        public FileStorage(@Str BytePointer source, int flags, @Str BytePointer encoding) {
            super((Pointer) null);
            allocate(source, flags, encoding);
        }

        public FileStorage(@Str BytePointer source, int flags) {
            super((Pointer) null);
            allocate(source, flags);
        }

        public FileStorage(@Str String source, int flags, @Str String encoding) {
            super((Pointer) null);
            allocate(source, flags, encoding);
        }

        public FileStorage(@Str String source, int flags) {
            super((Pointer) null);
            allocate(source, flags);
        }

        public FileStorage(CvFileStorage fs, @Cast({"bool"}) boolean owning) {
            super((Pointer) null);
            allocate(fs, owning);
        }

        public FileStorage(CvFileStorage fs) {
            super((Pointer) null);
            allocate(fs);
        }
    }

    @Namespace("cv")
    public static class Formatted extends Pointer {
        @Cast({"const char*"})
        public native BytePointer next();

        public native void reset();

        static {
            Loader.load();
        }

        public Formatted(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class Formatter extends Pointer {
        public static final int FMT_C = 5;
        public static final int FMT_CSV = 2;
        public static final int FMT_DEFAULT = 0;
        public static final int FMT_MATLAB = 1;
        public static final int FMT_NUMPY = 4;
        public static final int FMT_PYTHON = 3;

        @Ptr
        public static native Formatter get();

        @Ptr
        public static native Formatter get(int i);

        @Ptr
        public native Formatted format(@ByRef @Const Mat mat);

        public native void set32fPrecision();

        public native void set32fPrecision(int i);

        public native void set64fPrecision();

        public native void set64fPrecision(int i);

        public native void setMultiline();

        public native void setMultiline(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public Formatter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class Hamming extends Pointer {
        public static final int normType = 6;

        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"cv::Hamming::ResultType"})
        @Name({"operator ()"})
        public native int apply(@Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer2, int i);

        @Cast({"cv::Hamming::ResultType"})
        @Name({"operator ()"})
        public native int apply(@Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"const unsigned char*"}) BytePointer bytePointer2, int i);

        @Cast({"cv::Hamming::ResultType"})
        @Name({"operator ()"})
        public native int apply(@Cast({"const unsigned char*"}) byte[] bArr, @Cast({"const unsigned char*"}) byte[] bArr2, int i);

        static {
            Loader.load();
        }

        public Hamming() {
            super((Pointer) null);
            allocate();
        }

        public Hamming(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Hamming(Pointer p) {
            super(p);
        }

        public Hamming position(long position) {
            return (Hamming) super.position(position);
        }
    }

    @Namespace("cv::cuda")
    @Opaque
    public static class HostMem extends Pointer {
        public HostMem() {
            super((Pointer) null);
        }

        public HostMem(Pointer p) {
            super(p);
        }
    }

    @Name({"std::pair<int,int>"})
    @NoOffset
    public static class IntIntPair extends Pointer {
        private native void allocate();

        @MemberGetter
        public native int first();

        public native IntIntPair first(int i);

        @ByRef
        @Name({"operator="})
        public native IntIntPair put(@ByRef IntIntPair intIntPair);

        @MemberGetter
        public native int second();

        public native IntIntPair second(int i);

        static {
            Loader.load();
        }

        public IntIntPair(Pointer p) {
            super(p);
        }

        public IntIntPair(int firstValue, int secondValue) {
            this();
            put(firstValue, secondValue);
        }

        public IntIntPair() {
            allocate();
        }

        public IntIntPair put(int firstValue, int secondValue) {
            first(firstValue);
            second(secondValue);
            return this;
        }
    }

    @Name({"std::vector<std::pair<int,int> >"})
    public static class IntIntPairVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        public native int first(@Cast({"size_t"}) long j);

        public native IntIntPairVector first(@Cast({"size_t"}) long j, int i);

        @ByRef
        @Name({"operator="})
        public native IntIntPairVector put(@ByRef IntIntPairVector intIntPairVector);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native int second(@Cast({"size_t"}) long j);

        public native IntIntPairVector second(@Cast({"size_t"}) long j, int i);

        public native long size();

        static {
            Loader.load();
        }

        public IntIntPairVector(Pointer p) {
            super(p);
        }

        public IntIntPairVector(int[] firstValue, int[] secondValue) {
            this((long) Math.min(firstValue.length, secondValue.length));
            put(firstValue, secondValue);
        }

        public IntIntPairVector() {
            allocate();
        }

        public IntIntPairVector(long n) {
            allocate(n);
        }

        public IntIntPairVector put(int[] firstValue, int[] secondValue) {
            int i = 0;
            while (i < firstValue.length && i < secondValue.length) {
                first((long) i, firstValue[i]);
                second((long) i, secondValue[i]);
                i++;
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<int> >"})
    public static class IntVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Index
        public native int get(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native IntVectorVector put(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, int i);

        @ByRef
        @Name({"operator="})
        public native IntVectorVector put(@ByRef IntVectorVector intVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native void resize(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2);

        public native long size();

        @Index
        public native long size(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public IntVectorVector(Pointer p) {
            super(p);
        }

        public IntVectorVector(int[]... array) {
            this((long) array.length);
            put(array);
        }

        public IntVectorVector() {
            allocate();
        }

        public IntVectorVector(long n) {
            allocate(n);
        }

        public IntVectorVector put(int[]... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                if (size((long) i) != ((long) array[i].length)) {
                    resize((long) i, (long) array[i].length);
                }
                for (int j = 0; j < array[i].length; j++) {
                    put((long) i, (long) j, array[i][j]);
                }
            }
            return this;
        }
    }

    @NoOffset
    public static class IplImage extends AbstractIplImage {
        private native void allocate();

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocateArray(long j);

        public native int BorderConst(int i);

        @MemberGetter
        public native IntPointer BorderConst();

        public native IplImage BorderConst(int i, int i2);

        public native int BorderMode(int i);

        @MemberGetter
        public native IntPointer BorderMode();

        public native IplImage BorderMode(int i, int i2);

        public native int ID();

        public native IplImage ID(int i);

        public native int align();

        public native IplImage align(int i);

        public native int alphaChannel();

        public native IplImage alphaChannel(int i);

        @Cast({"char"})
        public native byte channelSeq(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer channelSeq();

        public native IplImage channelSeq(int i, byte b);

        @Cast({"char"})
        public native byte colorModel(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer colorModel();

        public native IplImage colorModel(int i, byte b);

        public native int dataOrder();

        public native IplImage dataOrder(int i);

        public native int depth();

        public native IplImage depth(int i);

        public native int height();

        public native IplImage height(int i);

        @Cast({"char*"})
        public native BytePointer imageData();

        public native IplImage imageData(BytePointer bytePointer);

        @Cast({"char*"})
        public native BytePointer imageDataOrigin();

        public native IplImage imageDataOrigin(BytePointer bytePointer);

        public native Pointer imageId();

        public native IplImage imageId(Pointer pointer);

        public native int imageSize();

        public native IplImage imageSize(int i);

        public native IplImage maskROI();

        public native IplImage maskROI(IplImage iplImage);

        public native int nChannels();

        public native IplImage nChannels(int i);

        public native int nSize();

        public native IplImage nSize(int i);

        public native int origin();

        public native IplImage origin(int i);

        public native IplImage roi(IplROI iplROI);

        public native IplROI roi();

        public native IplImage tileInfo(IplTileInfo iplTileInfo);

        public native IplTileInfo tileInfo();

        public native int width();

        public native IplImage width(int i);

        public native int widthStep();

        public native IplImage widthStep(int i);

        static {
            Loader.load();
        }

        public IplImage(Pointer p) {
            super(p);
        }

        public IplImage(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public IplImage position(long position) {
            return (IplImage) super.position(position);
        }

        public IplImage() {
            super((Pointer) null);
            allocate();
        }

        public IplImage(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    @Opaque
    public static class IplTileInfo extends Pointer {
        public IplTileInfo() {
            super((Pointer) null);
        }

        public IplTileInfo(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class KeyPoint extends Pointer {
        private native void allocate();

        private native void allocate(float f, float f2, float f3);

        private native void allocate(float f, float f2, float f3, float f4, float f5, int i, int i2);

        private native void allocate(@ByVal Point2f point2f, float f);

        private native void allocate(@ByVal Point2f point2f, float f, float f2, float f3, int i, int i2);

        private native void allocateArray(long j);

        public static native void convert(@ByRef @Const KeyPointVector keyPointVector, @ByRef Point2fVector point2fVector);

        public static native void convert(@ByRef @Const KeyPointVector keyPointVector, @ByRef Point2fVector point2fVector, @StdVector IntBuffer intBuffer);

        public static native void convert(@ByRef @Const KeyPointVector keyPointVector, @ByRef Point2fVector point2fVector, @StdVector IntPointer intPointer);

        public static native void convert(@ByRef @Const KeyPointVector keyPointVector, @ByRef Point2fVector point2fVector, @StdVector int[] iArr);

        public static native void convert(@ByRef @Const Point2fVector point2fVector, @ByRef KeyPointVector keyPointVector);

        public static native void convert(@ByRef @Const Point2fVector point2fVector, @ByRef KeyPointVector keyPointVector, float f, float f2, int i, int i2);

        public static native float overlap(@ByRef @Const KeyPoint keyPoint, @ByRef @Const KeyPoint keyPoint2);

        public native float angle();

        public native KeyPoint angle(float f);

        public native int class_id();

        public native KeyPoint class_id(int i);

        @Cast({"size_t"})
        public native long hash();

        public native int octave();

        public native KeyPoint octave(int i);

        public native KeyPoint pt(Point2f point2f);

        @ByRef
        public native Point2f pt();

        public native float response();

        public native KeyPoint response(float f);

        public native float size();

        public native KeyPoint size(float f);

        static {
            Loader.load();
        }

        public KeyPoint(Pointer p) {
            super(p);
        }

        public KeyPoint(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public KeyPoint position(long position) {
            return (KeyPoint) super.position(position);
        }

        public KeyPoint() {
            super((Pointer) null);
            allocate();
        }

        public KeyPoint(@ByVal Point2f _pt, float _size, float _angle, float _response, int _octave, int _class_id) {
            super((Pointer) null);
            allocate(_pt, _size, _angle, _response, _octave, _class_id);
        }

        public KeyPoint(@ByVal Point2f _pt, float _size) {
            super((Pointer) null);
            allocate(_pt, _size);
        }

        public KeyPoint(float x, float y, float _size, float _angle, float _response, int _octave, int _class_id) {
            super((Pointer) null);
            allocate(x, y, _size, _angle, _response, _octave, _class_id);
        }

        public KeyPoint(float x, float y, float _size) {
            super((Pointer) null);
            allocate(x, y, _size);
        }
    }

    @Name({"std::vector<cv::KeyPoint>"})
    public static class KeyPointVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native KeyPoint get(@Cast({"size_t"}) long j);

        public native KeyPointVector put(@Cast({"size_t"}) long j, KeyPoint keyPoint);

        @ByRef
        @Name({"operator="})
        public native KeyPointVector put(@ByRef KeyPointVector keyPointVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public KeyPointVector(Pointer p) {
            super(p);
        }

        public KeyPointVector(KeyPoint... array) {
            this((long) array.length);
            put(array);
        }

        public KeyPointVector() {
            allocate();
        }

        public KeyPointVector(long n) {
            allocate(n);
        }

        public KeyPointVector put(KeyPoint... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::KeyPoint> >"})
    public static class KeyPointVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native KeyPointVector get(@Cast({"size_t"}) long j);

        public native KeyPointVectorVector put(@Cast({"size_t"}) long j, KeyPointVector keyPointVector);

        @ByRef
        @Name({"operator="})
        public native KeyPointVectorVector put(@ByRef KeyPointVectorVector keyPointVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public KeyPointVectorVector(Pointer p) {
            super(p);
        }

        public KeyPointVectorVector(KeyPointVector... array) {
            this((long) array.length);
            put(array);
        }

        public KeyPointVectorVector() {
            allocate();
        }

        public KeyPointVectorVector(long n) {
            allocate(n);
        }

        public KeyPointVectorVector put(KeyPointVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class LDA extends Pointer {
        private native void allocate();

        private native void allocate(int i);

        private native void allocate(@ByVal MatVector matVector, @ByVal Mat mat);

        private native void allocate(@ByVal MatVector matVector, @ByVal Mat mat, int i);

        private native void allocate(@ByVal MatVector matVector, @ByVal UMat uMat);

        private native void allocate(@ByVal MatVector matVector, @ByVal UMat uMat, int i);

        private native void allocate(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        private native void allocate(@ByVal UMatVector uMatVector, @ByVal Mat mat, int i);

        private native void allocate(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        private native void allocate(@ByVal UMatVector uMatVector, @ByVal UMat uMat, int i);

        private native void allocateArray(long j);

        @ByVal
        public static native Mat subspaceProject(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        @ByVal
        public static native Mat subspaceProject(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        @ByVal
        public static native Mat subspaceReconstruct(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        @ByVal
        public static native Mat subspaceReconstruct(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native void compute(@ByVal MatVector matVector, @ByVal Mat mat);

        public native void compute(@ByVal MatVector matVector, @ByVal UMat uMat);

        public native void compute(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        public native void compute(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        @ByVal
        public native Mat eigenvalues();

        @ByVal
        public native Mat eigenvectors();

        public native void load(@Str String str);

        public native void load(@Str BytePointer bytePointer);

        public native void load(@ByRef @Const FileStorage fileStorage);

        @ByVal
        public native Mat project(@ByVal Mat mat);

        @ByVal
        public native Mat project(@ByVal UMat uMat);

        @ByVal
        public native Mat reconstruct(@ByVal Mat mat);

        @ByVal
        public native Mat reconstruct(@ByVal UMat uMat);

        public native void save(@Str String str);

        public native void save(@Str BytePointer bytePointer);

        public native void save(@ByRef FileStorage fileStorage);

        static {
            Loader.load();
        }

        public LDA(Pointer p) {
            super(p);
        }

        public LDA(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public LDA position(long position) {
            return (LDA) super.position(position);
        }

        public LDA(int num_components) {
            super((Pointer) null);
            allocate(num_components);
        }

        public LDA() {
            super((Pointer) null);
            allocate();
        }

        public LDA(@ByVal MatVector src, @ByVal Mat labels, int num_components) {
            super((Pointer) null);
            allocate(src, labels, num_components);
        }

        public LDA(@ByVal MatVector src, @ByVal Mat labels) {
            super((Pointer) null);
            allocate(src, labels);
        }

        public LDA(@ByVal UMatVector src, @ByVal Mat labels, int num_components) {
            super((Pointer) null);
            allocate(src, labels, num_components);
        }

        public LDA(@ByVal UMatVector src, @ByVal Mat labels) {
            super((Pointer) null);
            allocate(src, labels);
        }

        public LDA(@ByVal MatVector src, @ByVal UMat labels, int num_components) {
            super((Pointer) null);
            allocate(src, labels, num_components);
        }

        public LDA(@ByVal MatVector src, @ByVal UMat labels) {
            super((Pointer) null);
            allocate(src, labels);
        }

        public LDA(@ByVal UMatVector src, @ByVal UMat labels, int num_components) {
            super((Pointer) null);
            allocate(src, labels, num_components);
        }

        public LDA(@ByVal UMatVector src, @ByVal UMat labels) {
            super((Pointer) null);
            allocate(src, labels);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class Mat extends AbstractMat {
        public static final int AUTO_STEP = 0;
        public static final int CONTINUOUS_FLAG = 16384;
        public static final int DEPTH_MASK = 7;
        public static final int MAGIC_MASK = -65536;
        public static final int MAGIC_VAL = 1124007936;
        public static final int SUBMATRIX_FLAG = 32768;
        public static final int TYPE_MASK = 4095;
        private Pointer data;

        private native void allocate();

        private native void allocate(int i, int i2, int i3);

        private native void allocate(int i, int i2, int i3, Pointer pointer, @Cast({"size_t"}) long j);

        private native void allocate(int i, int i2, int i3, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, Pointer pointer);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, Pointer pointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const IntPointer intPointer, int i2);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, Pointer pointer);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, Pointer pointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const int[] iArr, int i2);

        private native void allocate(int i, @Const int[] iArr, int i2, Pointer pointer);

        private native void allocate(int i, @Const int[] iArr, int i2, Pointer pointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer);

        private native void allocate(int i, @Const int[] iArr, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocate(@ByRef @Const Mat mat, @ByRef @Const Range range);

        private native void allocate(@ByRef @Const Mat mat, @ByRef @Const Range range, @ByRef(nullValue = "cv::Range::all()") @Const Range range2);

        private native void allocate(@ByRef @Const Mat mat, @ByRef @Const Rect rect);

        private native void allocate(@ByVal Size size, int i);

        private native void allocate(@ByVal Size size, int i, Pointer pointer);

        private native void allocate(@ByVal Size size, int i, Pointer pointer, @Cast({"size_t"}) long j);

        private native void allocate(@ByVal Size size, int i, @ByRef @Const Scalar scalar);

        private native void allocateArray(long j);

        @ByVal
        public static native Mat diag(@ByRef @Const Mat mat);

        @ByVal
        public static native MatExpr eye(int i, int i2, int i3);

        @ByVal
        public static native MatExpr eye(@ByVal Size size, int i);

        public static native MatAllocator getDefaultAllocator();

        public static native MatAllocator getStdAllocator();

        @ByVal
        public static native MatExpr ones(int i, int i2, int i3);

        @ByVal
        public static native MatExpr ones(@ByVal Size size, int i);

        public static native void setDefaultAllocator(MatAllocator matAllocator);

        @ByVal
        public static native MatExpr zeros(int i, int i2, int i3);

        @ByVal
        public static native MatExpr zeros(@ByVal Size size, int i);

        @Name({"deallocate"})
        public native void _deallocate();

        public native void addref();

        @ByRef
        public native Mat adjustROI(int i, int i2, int i3, int i4);

        public native Mat allocator(MatAllocator matAllocator);

        public native MatAllocator allocator();

        @ByVal
        @Name({"operator ()"})
        public native Mat apply(@Const Range range);

        @ByVal
        @Name({"operator ()"})
        public native Mat apply(@ByVal Range range, @ByVal Range range2);

        @ByVal
        @Name({"operator ()"})
        public native Mat apply(@ByRef @Const Rect rect);

        public native void assignTo(@ByRef Mat mat);

        public native void assignTo(@ByRef Mat mat, int i);

        public native int channels();

        public native int checkVector(int i);

        public native int checkVector(int i, int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Mat clone();

        @ByVal
        public native Mat col(int i);

        @ByVal
        public native Mat colRange(int i, int i2);

        @ByVal
        public native Mat colRange(@ByRef @Const Range range);

        public native int cols();

        public native Mat cols(int i);

        public native void convertTo(@ByVal Mat mat, int i);

        public native void convertTo(@ByVal Mat mat, int i, double d, double d2);

        public native void convertTo(@ByVal UMat uMat, int i);

        public native void convertTo(@ByVal UMat uMat, int i, double d, double d2);

        public native void copySize(@ByRef @Const Mat mat);

        public native void copyTo(@ByVal Mat mat);

        public native void copyTo(@ByVal Mat mat, @ByVal Mat mat2);

        public native void copyTo(@ByVal UMat uMat);

        public native void copyTo(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void create(int i, int i2, int i3);

        public native void create(int i, @Const IntBuffer intBuffer, int i2);

        public native void create(int i, @Const IntPointer intPointer, int i2);

        public native void create(int i, @Const int[] iArr, int i2);

        public native void create(@ByVal Size size, int i);

        @ByVal
        public native Mat cross(@ByVal Mat mat);

        @ByVal
        public native Mat cross(@ByVal UMat uMat);

        @Cast({"uchar*"})
        public native BytePointer data();

        public native Mat data(BytePointer bytePointer);

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer dataend();

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer datalimit();

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer datastart();

        public native int depth();

        @ByVal
        public native Mat diag();

        @ByVal
        public native Mat diag(int i);

        public native int dims();

        public native Mat dims(int i);

        public native double dot(@ByVal Mat mat);

        public native double dot(@ByVal UMat uMat);

        @Cast({"size_t"})
        public native long elemSize();

        @Cast({"size_t"})
        public native long elemSize1();

        @Cast({"bool"})
        public native boolean empty();

        public native int flags();

        public native Mat flags(int i);

        @ByVal
        public native UMat getUMat(int i);

        @ByVal
        public native UMat getUMat(int i, @Cast({"cv::UMatUsageFlags"}) int i2);

        @ByVal
        public native MatExpr inv();

        @ByVal
        public native MatExpr inv(int i);

        @Cast({"bool"})
        public native boolean isContinuous();

        @Cast({"bool"})
        public native boolean isSubmatrix();

        public native void locateROI(@ByRef Size size, @ByRef Point point);

        @ByVal
        public native MatExpr mul(@ByVal Mat mat);

        @ByVal
        public native MatExpr mul(@ByVal Mat mat, double d);

        @ByVal
        public native MatExpr mul(@ByVal UMat uMat);

        @ByVal
        public native MatExpr mul(@ByVal UMat uMat, double d);

        public native void pop_back();

        public native void pop_back(@Cast({"size_t"}) long j);

        @Cast({"uchar*"})
        public native ByteBuffer ptr(@Const IntBuffer intBuffer);

        @Cast({"uchar*"})
        public native BytePointer ptr();

        @Cast({"uchar*"})
        public native BytePointer ptr(int i);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, int i3);

        @Cast({"uchar*"})
        public native BytePointer ptr(@Const IntPointer intPointer);

        @Cast({"uchar*"})
        public native byte[] ptr(@Const int[] iArr);

        public native void push_back(@ByRef @Const Mat mat);

        public native void push_back_(@Const Pointer pointer);

        @ByRef
        @Name({"operator ="})
        public native Mat put(@ByRef @Const Mat mat);

        @ByRef
        @Name({"operator ="})
        public native Mat put(@ByRef @Const MatExpr matExpr);

        @ByRef
        @Name({"operator ="})
        public native Mat put(@ByRef @Const Scalar scalar);

        public native void release();

        public native void reserve(@Cast({"size_t"}) long j);

        @ByVal
        public native Mat reshape(int i);

        @ByVal
        public native Mat reshape(int i, int i2);

        @ByVal
        public native Mat reshape(int i, int i2, @Const IntBuffer intBuffer);

        @ByVal
        public native Mat reshape(int i, int i2, @Const IntPointer intPointer);

        @ByVal
        public native Mat reshape(int i, int i2, @Const int[] iArr);

        public native void resize(@Cast({"size_t"}) long j);

        public native void resize(@Cast({"size_t"}) long j, @ByRef @Const Scalar scalar);

        @ByVal
        public native Mat row(int i);

        @ByVal
        public native Mat rowRange(int i, int i2);

        @ByVal
        public native Mat rowRange(@ByRef @Const Range range);

        public native int rows();

        public native Mat rows(int i);

        @ByRef
        public native Mat setTo(@ByVal Mat mat);

        @ByRef
        public native Mat setTo(@ByVal Mat mat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

        @ByRef
        public native Mat setTo(@ByVal UMat uMat);

        @ByRef
        public native Mat setTo(@ByVal UMat uMat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

        @MemberGetter
        public native int size(int i);

        @ByVal
        public native Size size();

        @MemberGetter
        public native int step(int i);

        @MemberGetter
        public native long step();

        @Cast({"size_t"})
        public native long step1();

        @Cast({"size_t"})
        public native long step1(int i);

        @ByVal
        public native MatExpr m93t();

        @Cast({"size_t"})
        public native long total();

        public native int type();

        public native Mat m94u(UMatData uMatData);

        public native UMatData m95u();

        static {
            Loader.load();
        }

        public Mat(Pointer p) {
            super(p);
        }

        public Mat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Mat position(long position) {
            return (Mat) super.position(position);
        }

        public Mat() {
            super((Pointer) null);
            allocate();
        }

        public Mat(int rows, int cols, int type) {
            super((Pointer) null);
            allocate(rows, cols, type);
        }

        public Mat(@ByVal Size size, int type) {
            super((Pointer) null);
            allocate(size, type);
        }

        public Mat(int rows, int cols, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(rows, cols, type, s);
        }

        public Mat(@ByVal Size size, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(size, type, s);
        }

        public Mat(int ndims, @Const IntPointer sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public Mat(int ndims, @Const IntBuffer sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public Mat(int ndims, @Const int[] sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public Mat(int ndims, @Const IntPointer sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public Mat(int ndims, @Const IntBuffer sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public Mat(int ndims, @Const int[] sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public Mat(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }

        public Mat(int rows, int cols, int type, Pointer data, @Cast({"size_t"}) long step) {
            super((Pointer) null);
            allocate(rows, cols, type, data, step);
            this.data = data;
        }

        public Mat(int rows, int cols, int type, Pointer data) {
            this(rows, cols, type, data, 0);
        }

        public Mat(opencv_core$CvArr arr) {
            super(opencv_core.cvarrToMat(arr));
            this.data = arr;
        }

        public Mat(byte... b) {
            this(b, false);
        }

        public Mat(byte[] b, boolean signed) {
            this(new BytePointer(b), signed);
        }

        public Mat(short... s) {
            this(s, true);
        }

        public Mat(short[] s, boolean signed) {
            this(new ShortPointer(s), signed);
        }

        public Mat(int... n) {
            this(new IntPointer(n));
        }

        public Mat(double... d) {
            this(new DoublePointer(d));
        }

        public Mat(float... f) {
            this(new FloatPointer(f));
        }

        private Mat(long rows, long cols, int type, Pointer data) {
            this((int) Math.min(rows, 2147483647L), (int) Math.min(cols, 2147483647L), type, data, 0);
        }

        public Mat(BytePointer p) {
            this(p, false);
        }

        public Mat(BytePointer p, boolean signed) {
            this(p.limit - p.position, 1, signed ? opencv_core.CV_8SC1 : opencv_core.CV_8UC1, (Pointer) p);
        }

        public Mat(ShortPointer p) {
            this(p, false);
        }

        public Mat(ShortPointer p, boolean signed) {
            this(p.limit - p.position, 1, signed ? opencv_core.CV_16SC1 : opencv_core.CV_16UC1, (Pointer) p);
        }

        public Mat(IntPointer p) {
            this(p.limit - p.position, 1, opencv_core.CV_32SC1, (Pointer) p);
        }

        public Mat(FloatPointer p) {
            this(p.limit - p.position, 1, opencv_core.CV_32FC1, (Pointer) p);
        }

        public Mat(DoublePointer p) {
            this(p.limit - p.position, 1, opencv_core.CV_64FC1, (Pointer) p);
        }

        public Mat(@ByVal Size size, int type, Pointer data, @Cast({"size_t"}) long step) {
            super((Pointer) null);
            allocate(size, type, data, step);
        }

        public Mat(@ByVal Size size, int type, Pointer data) {
            super((Pointer) null);
            allocate(size, type, data);
        }

        public Mat(int ndims, @Const IntPointer sizes, int type, Pointer data, @Cast({"const size_t*"}) SizeTPointer steps) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data, steps);
        }

        public Mat(int ndims, @Const IntPointer sizes, int type, Pointer data) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data);
        }

        public Mat(int ndims, @Const IntBuffer sizes, int type, Pointer data, @Cast({"const size_t*"}) SizeTPointer steps) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data, steps);
        }

        public Mat(int ndims, @Const IntBuffer sizes, int type, Pointer data) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data);
        }

        public Mat(int ndims, @Const int[] sizes, int type, Pointer data, @Cast({"const size_t*"}) SizeTPointer steps) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data, steps);
        }

        public Mat(int ndims, @Const int[] sizes, int type, Pointer data) {
            super((Pointer) null);
            allocate(ndims, sizes, type, data);
        }

        public Mat(@ByRef @Const Mat m, @ByRef @Const Range rowRange, @ByRef(nullValue = "cv::Range::all()") @Const Range colRange) {
            super((Pointer) null);
            allocate(m, rowRange, colRange);
        }

        public Mat(@ByRef @Const Mat m, @ByRef @Const Range rowRange) {
            super((Pointer) null);
            allocate(m, rowRange);
        }

        public Mat(@ByRef @Const Mat m, @ByRef @Const Rect roi) {
            super((Pointer) null);
            allocate(m, roi);
        }
    }

    @Namespace("cv")
    public static class MatAllocator extends Pointer {
        @Name({"allocate"})
        public native UMatData _allocate(int i, @Const IntBuffer intBuffer, int i2, Pointer pointer, @Cast({"size_t*"}) SizeTPointer sizeTPointer, int i3, @Cast({"cv::UMatUsageFlags"}) int i4);

        @Name({"allocate"})
        public native UMatData _allocate(int i, @Const IntPointer intPointer, int i2, Pointer pointer, @Cast({"size_t*"}) SizeTPointer sizeTPointer, int i3, @Cast({"cv::UMatUsageFlags"}) int i4);

        @Name({"allocate"})
        public native UMatData _allocate(int i, @Const int[] iArr, int i2, Pointer pointer, @Cast({"size_t*"}) SizeTPointer sizeTPointer, int i3, @Cast({"cv::UMatUsageFlags"}) int i4);

        @Cast({"bool"})
        @Name({"allocate"})
        public native boolean _allocate(UMatData uMatData, int i, @Cast({"cv::UMatUsageFlags"}) int i2);

        @Name({"deallocate"})
        public native void _deallocate(UMatData uMatData);

        public native void copy(UMatData uMatData, UMatData uMatData2, int i, @Cast({"const size_t*"}) SizeTPointer sizeTPointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer2, @Cast({"const size_t*"}) SizeTPointer sizeTPointer3, @Cast({"const size_t*"}) SizeTPointer sizeTPointer4, @Cast({"const size_t*"}) SizeTPointer sizeTPointer5, @Cast({"bool"}) boolean z);

        public native void download(UMatData uMatData, Pointer pointer, int i, @Cast({"const size_t*"}) SizeTPointer sizeTPointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer2, @Cast({"const size_t*"}) SizeTPointer sizeTPointer3, @Cast({"const size_t*"}) SizeTPointer sizeTPointer4);

        public native BufferPoolController getBufferPoolController();

        public native BufferPoolController getBufferPoolController(String str);

        public native BufferPoolController getBufferPoolController(@Cast({"const char*"}) BytePointer bytePointer);

        public native void map(UMatData uMatData, int i);

        public native void unmap(UMatData uMatData);

        public native void upload(UMatData uMatData, @Const Pointer pointer, int i, @Cast({"const size_t*"}) SizeTPointer sizeTPointer, @Cast({"const size_t*"}) SizeTPointer sizeTPointer2, @Cast({"const size_t*"}) SizeTPointer sizeTPointer3, @Cast({"const size_t*"}) SizeTPointer sizeTPointer4);

        static {
            Loader.load();
        }

        public MatAllocator(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<std::pair<cv::Mat,uchar> >"})
    public static class MatBytePairVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Mat first(@Cast({"size_t"}) long j);

        public native MatBytePairVector first(@Cast({"size_t"}) long j, Mat mat);

        @ByRef
        @Name({"operator="})
        public native MatBytePairVector put(@ByRef MatBytePairVector matBytePairVector);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native byte second(@Cast({"size_t"}) long j);

        public native MatBytePairVector second(@Cast({"size_t"}) long j, byte b);

        public native long size();

        static {
            Loader.load();
        }

        public MatBytePairVector(Pointer p) {
            super(p);
        }

        public MatBytePairVector(Mat[] firstValue, byte[] secondValue) {
            this((long) Math.min(firstValue.length, secondValue.length));
            put(firstValue, secondValue);
        }

        public MatBytePairVector() {
            allocate();
        }

        public MatBytePairVector(long n) {
            allocate(n);
        }

        public MatBytePairVector put(Mat[] firstValue, byte[] secondValue) {
            int i = 0;
            while (i < firstValue.length && i < secondValue.length) {
                first((long) i, firstValue[i]);
                second((long) i, secondValue[i]);
                i++;
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class MatConstIterator extends Pointer {
        private native void allocate();

        private native void allocate(@Const Mat mat);

        private native void allocate(@Const Mat mat, int i);

        private native void allocate(@Const Mat mat, int i, int i2);

        private native void allocate(@Const Mat mat, @ByVal Point point);

        private native void allocate(@ByRef @Const MatConstIterator matConstIterator);

        private native void allocateArray(long j);

        @ByRef
        @Name({"operator +="})
        public native MatConstIterator addPut(@Cast({"ptrdiff_t"}) long j);

        @ByRef
        @Name({"operator --"})
        public native MatConstIterator decrement();

        @ByVal
        @Name({"operator --"})
        public native MatConstIterator decrement(int i);

        @Cast({"size_t"})
        public native long elemSize();

        public native MatConstIterator elemSize(long j);

        @Cast({"const uchar*"})
        @Name({"operator []"})
        public native BytePointer get(@Cast({"ptrdiff_t"}) long j);

        @ByRef
        @Name({"operator ++"})
        public native MatConstIterator increment();

        @ByVal
        @Name({"operator ++"})
        public native MatConstIterator increment(int i);

        @Cast({"ptrdiff_t"})
        public native long lpos();

        @MemberGetter
        @Const
        public native Mat m96m();

        @Cast({"const uchar*"})
        @Name({"operator *"})
        public native BytePointer multiply();

        @ByVal
        public native Point pos();

        public native void pos(IntBuffer intBuffer);

        public native void pos(IntPointer intPointer);

        public native void pos(int[] iArr);

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer ptr();

        @ByRef
        @Name({"operator ="})
        public native MatConstIterator put(@ByRef @Const MatConstIterator matConstIterator);

        public native void seek(@Cast({"ptrdiff_t"}) long j);

        public native void seek(@Cast({"ptrdiff_t"}) long j, @Cast({"bool"}) boolean z);

        public native void seek(@Const IntBuffer intBuffer);

        public native void seek(@Const IntBuffer intBuffer, @Cast({"bool"}) boolean z);

        public native void seek(@Const IntPointer intPointer);

        public native void seek(@Const IntPointer intPointer, @Cast({"bool"}) boolean z);

        public native void seek(@Const int[] iArr);

        public native void seek(@Const int[] iArr, @Cast({"bool"}) boolean z);

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer sliceEnd();

        @MemberGetter
        @Cast({"const uchar*"})
        public native BytePointer sliceStart();

        @ByRef
        @Name({"operator -="})
        public native MatConstIterator subtractPut(@Cast({"ptrdiff_t"}) long j);

        static {
            Loader.load();
        }

        public MatConstIterator(Pointer p) {
            super(p);
        }

        public MatConstIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MatConstIterator position(long position) {
            return (MatConstIterator) super.position(position);
        }

        public MatConstIterator() {
            super((Pointer) null);
            allocate();
        }

        public MatConstIterator(@Const Mat _m) {
            super((Pointer) null);
            allocate(_m);
        }

        public MatConstIterator(@Const Mat _m, int _row, int _col) {
            super((Pointer) null);
            allocate(_m, _row, _col);
        }

        public MatConstIterator(@Const Mat _m, int _row) {
            super((Pointer) null);
            allocate(_m, _row);
        }

        public MatConstIterator(@Const Mat _m, @ByVal Point _pt) {
            super((Pointer) null);
            allocate(_m, _pt);
        }

        public MatConstIterator(@ByRef @Const MatConstIterator it) {
            super((Pointer) null);
            allocate(it);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class MatExpr extends Pointer {
        private native void allocate();

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocate(@Const MatOp matOp, int i);

        private native void allocate(@Const MatOp matOp, int i, @ByRef(nullValue = "cv::Mat()") @Const Mat mat, @ByRef(nullValue = "cv::Mat()") @Const Mat mat2, @ByRef(nullValue = "cv::Mat()") @Const Mat mat3, double d, double d2, @ByRef(nullValue = "cv::Scalar()") @Const Scalar scalar);

        private native void allocateArray(long j);

        @ByRef
        public native Mat m97a();

        public native MatExpr m98a(Mat mat);

        public native double alpha();

        public native MatExpr alpha(double d);

        @ByVal
        @Name({"operator ()"})
        public native MatExpr apply(@ByRef @Const Range range, @ByRef @Const Range range2);

        @ByVal
        @Name({"operator ()"})
        public native MatExpr apply(@ByRef @Const Rect rect);

        @ByVal
        @Name({"operator cv::Mat"})
        public native Mat asMat();

        @ByRef
        public native Mat m99b();

        public native MatExpr m100b(Mat mat);

        public native double beta();

        public native MatExpr beta(double d);

        @ByRef
        public native Mat m101c();

        public native MatExpr m102c(Mat mat);

        @ByVal
        public native MatExpr col(int i);

        @ByVal
        public native Mat cross(@ByRef @Const Mat mat);

        @ByVal
        public native MatExpr diag();

        @ByVal
        public native MatExpr diag(int i);

        public native double dot(@ByRef @Const Mat mat);

        public native int flags();

        public native MatExpr flags(int i);

        @ByVal
        public native MatExpr inv();

        @ByVal
        public native MatExpr inv(int i);

        @ByVal
        public native MatExpr mul(@ByRef @Const Mat mat);

        @ByVal
        public native MatExpr mul(@ByRef @Const Mat mat, double d);

        @ByVal
        public native MatExpr mul(@ByRef @Const MatExpr matExpr);

        @ByVal
        public native MatExpr mul(@ByRef @Const MatExpr matExpr, double d);

        @MemberGetter
        @Const
        public native MatOp op();

        @ByVal
        public native MatExpr row(int i);

        public native MatExpr m103s(Scalar scalar);

        @ByRef
        public native Scalar m104s();

        @ByVal
        public native Size size();

        @ByVal
        public native MatExpr m105t();

        public native int type();

        static {
            Loader.load();
        }

        public MatExpr(Pointer p) {
            super(p);
        }

        public MatExpr(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MatExpr position(long position) {
            return (MatExpr) super.position(position);
        }

        public MatExpr() {
            super((Pointer) null);
            allocate();
        }

        public MatExpr(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }

        public MatExpr(@Const MatOp _op, int _flags, @ByRef(nullValue = "cv::Mat()") @Const Mat _a, @ByRef(nullValue = "cv::Mat()") @Const Mat _b, @ByRef(nullValue = "cv::Mat()") @Const Mat _c, double _alpha, double _beta, @ByRef(nullValue = "cv::Scalar()") @Const Scalar _s) {
            super((Pointer) null);
            allocate(_op, _flags, _a, _b, _c, _alpha, _beta, _s);
        }

        public MatExpr(@Const MatOp _op, int _flags) {
            super((Pointer) null);
            allocate(_op, _flags);
        }
    }

    @Namespace("cv")
    public static class MatOp extends Pointer {
        public native void abs(@ByRef @Const MatExpr matExpr, @ByRef MatExpr matExpr2);

        public native void add(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3);

        public native void add(@ByRef @Const MatExpr matExpr, @ByRef @Const Scalar scalar, @ByRef MatExpr matExpr2);

        public native void assign(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void assign(@ByRef @Const MatExpr matExpr, @ByRef Mat mat, int i);

        public native void augAssignAdd(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignAnd(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignDivide(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignMultiply(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignOr(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignSubtract(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void augAssignXor(@ByRef @Const MatExpr matExpr, @ByRef Mat mat);

        public native void diag(@ByRef @Const MatExpr matExpr, int i, @ByRef MatExpr matExpr2);

        public native void divide(double d, @ByRef @Const MatExpr matExpr, @ByRef MatExpr matExpr2);

        public native void divide(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3);

        public native void divide(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3, double d);

        @Cast({"bool"})
        public native boolean elementWise(@ByRef @Const MatExpr matExpr);

        public native void invert(@ByRef @Const MatExpr matExpr, int i, @ByRef MatExpr matExpr2);

        public native void matmul(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3);

        public native void multiply(@ByRef @Const MatExpr matExpr, double d, @ByRef MatExpr matExpr2);

        public native void multiply(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3);

        public native void multiply(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3, double d);

        public native void roi(@ByRef @Const MatExpr matExpr, @ByRef @Const Range range, @ByRef @Const Range range2, @ByRef MatExpr matExpr2);

        @ByVal
        public native Size size(@ByRef @Const MatExpr matExpr);

        public native void subtract(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2, @ByRef MatExpr matExpr3);

        public native void subtract(@ByRef @Const Scalar scalar, @ByRef @Const MatExpr matExpr, @ByRef MatExpr matExpr2);

        public native void transpose(@ByRef @Const MatExpr matExpr, @ByRef MatExpr matExpr2);

        public native int type(@ByRef @Const MatExpr matExpr);

        static {
            Loader.load();
        }

        public MatOp(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class MatSize extends Pointer {
        private native void allocate(IntBuffer intBuffer);

        private native void allocate(IntPointer intPointer);

        private native void allocate(int[] iArr);

        @ByVal
        @Name({"operator ()"})
        public native Size apply();

        @Const
        @Name({"operator const int*"})
        public native IntPointer asIntPointer();

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const MatSize matSize);

        @ByRef
        @Name({"operator []"})
        public native IntPointer get(int i);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@ByRef @Const MatSize matSize);

        public native IntPointer m106p();

        public native MatSize m107p(IntPointer intPointer);

        static {
            Loader.load();
        }

        public MatSize(Pointer p) {
            super(p);
        }

        public MatSize(IntPointer _p) {
            super((Pointer) null);
            allocate(_p);
        }

        public MatSize(IntBuffer _p) {
            super((Pointer) null);
            allocate(_p);
        }

        public MatSize(int[] _p) {
            super((Pointer) null);
            allocate(_p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class MatStep extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Name({"operator size_t"})
        public native long asLong();

        @Cast({"size_t"})
        public native long buf(int i);

        @MemberGetter
        @Cast({"size_t*"})
        public native SizeTPointer buf();

        public native MatStep buf(int i, long j);

        @ByRef
        @Cast({"size_t*"})
        @Name({"operator []"})
        public native SizeTPointer get(int i);

        @Cast({"size_t*"})
        public native SizeTPointer m108p();

        public native MatStep m109p(SizeTPointer sizeTPointer);

        @ByRef
        @Name({"operator ="})
        public native MatStep put(@Cast({"size_t"}) long j);

        static {
            Loader.load();
        }

        public MatStep(Pointer p) {
            super(p);
        }

        public MatStep() {
            super((Pointer) null);
            allocate();
        }

        public MatStep(@Cast({"size_t"}) long s) {
            super((Pointer) null);
            allocate(s);
        }
    }

    @Name({"std::vector<cv::Mat>"})
    public static class MatVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Mat get(@Cast({"size_t"}) long j);

        public native MatVector put(@Cast({"size_t"}) long j, Mat mat);

        @ByRef
        @Name({"operator="})
        public native MatVector put(@ByRef MatVector matVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public MatVector(Pointer p) {
            super(p);
        }

        public MatVector(Mat... array) {
            this((long) array.length);
            put(array);
        }

        public MatVector() {
            allocate();
        }

        public MatVector(long n) {
            allocate(n);
        }

        public MatVector put(Mat... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class Moments extends Pointer {
        private native void allocate();

        private native void allocate(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10);

        private native void allocateArray(long j);

        public native double m00();

        public native Moments m00(double d);

        public native double m01();

        public native Moments m01(double d);

        public native double m02();

        public native Moments m02(double d);

        public native double m03();

        public native Moments m03(double d);

        public native double m10();

        public native Moments m10(double d);

        public native double m11();

        public native Moments m11(double d);

        public native double m12();

        public native Moments m12(double d);

        public native double m20();

        public native Moments m20(double d);

        public native double m21();

        public native Moments m21(double d);

        public native double m30();

        public native Moments m30(double d);

        public native double mu02();

        public native Moments mu02(double d);

        public native double mu03();

        public native Moments mu03(double d);

        public native double mu11();

        public native Moments mu11(double d);

        public native double mu12();

        public native Moments mu12(double d);

        public native double mu20();

        public native Moments mu20(double d);

        public native double mu21();

        public native Moments mu21(double d);

        public native double mu30();

        public native Moments mu30(double d);

        public native double nu02();

        public native Moments nu02(double d);

        public native double nu03();

        public native Moments nu03(double d);

        public native double nu11();

        public native Moments nu11(double d);

        public native double nu12();

        public native Moments nu12(double d);

        public native double nu20();

        public native Moments nu20(double d);

        public native double nu21();

        public native Moments nu21(double d);

        public native double nu30();

        public native Moments nu30(double d);

        static {
            Loader.load();
        }

        public Moments(Pointer p) {
            super(p);
        }

        public Moments(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Moments position(long position) {
            return (Moments) super.position(position);
        }

        public Moments() {
            super((Pointer) null);
            allocate();
        }

        public Moments(double m00, double m10, double m01, double m20, double m11, double m02, double m30, double m21, double m12, double m03) {
            super((Pointer) null);
            allocate(m00, m10, m01, m20, m11, m02, m30, m21, m12, m03);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class Mutex extends Pointer {

        @Opaque
        public static class Impl extends Pointer {
            public Impl() {
                super((Pointer) null);
            }

            public Impl(Pointer p) {
                super(p);
            }
        }

        private native void allocate();

        private native void allocate(@ByRef @Const Mutex mutex);

        private native void allocateArray(long j);

        public native void lock();

        @ByRef
        @Name({"operator ="})
        public native Mutex put(@ByRef @Const Mutex mutex);

        @Cast({"bool"})
        public native boolean trylock();

        public native void unlock();

        static {
            Loader.load();
        }

        public Mutex(Pointer p) {
            super(p);
        }

        public Mutex(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Mutex position(long position) {
            return (Mutex) super.position(position);
        }

        public Mutex() {
            super((Pointer) null);
            allocate();
        }

        public Mutex(@ByRef @Const Mutex m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class NAryMatIterator extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"const cv::Mat**"}) PointerPointer pointerPointer, @Cast({"uchar**"}) PointerPointer pointerPointer2, int i);

        private native void allocate(@Cast({"const cv::Mat**"}) PointerPointer pointerPointer, Mat mat, int i);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer, int i);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer, int i);

        private native void allocate(@ByPtrPtr @Const Mat mat, Mat mat2);

        private native void allocate(@ByPtrPtr @Const Mat mat, Mat mat2, int i);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr);

        private native void allocate(@ByPtrPtr @Const Mat mat, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr, int i);

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const cv::Mat**"})
        public native PointerPointer arrays();

        @MemberGetter
        @Const
        public native Mat arrays(int i);

        @ByRef
        @Name({"operator ++"})
        public native NAryMatIterator increment();

        @ByVal
        @Name({"operator ++"})
        public native NAryMatIterator increment(int i);

        public native void init(@Cast({"const cv::Mat**"}) PointerPointer pointerPointer, Mat mat, @Cast({"uchar**"}) PointerPointer pointerPointer2, int i);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer, int i);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer, int i);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr);

        public native void init(@ByPtrPtr @Const Mat mat, Mat mat2, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr, int i);

        public native int narrays();

        public native NAryMatIterator narrays(int i);

        @Cast({"size_t"})
        public native long nplanes();

        public native NAryMatIterator nplanes(long j);

        public native Mat planes();

        public native NAryMatIterator planes(Mat mat);

        @Cast({"uchar*"})
        public native BytePointer ptrs(int i);

        @MemberGetter
        @Cast({"uchar**"})
        public native PointerPointer ptrs();

        public native NAryMatIterator ptrs(int i, BytePointer bytePointer);

        @Cast({"size_t"})
        public native long size();

        public native NAryMatIterator size(long j);

        static {
            Loader.load();
        }

        public NAryMatIterator(Pointer p) {
            super(p);
        }

        public NAryMatIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NAryMatIterator position(long position) {
            return (NAryMatIterator) super.position(position);
        }

        public NAryMatIterator() {
            super((Pointer) null);
            allocate();
        }

        public NAryMatIterator(@Cast({"const cv::Mat**"}) PointerPointer arrays, @Cast({"uchar**"}) PointerPointer ptrs, int narrays) {
            super((Pointer) null);
            allocate(arrays, ptrs, narrays);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) BytePointer ptrs) {
            super((Pointer) null);
            allocate(arrays, ptrs);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) BytePointer ptrs, int narrays) {
            super((Pointer) null);
            allocate(arrays, ptrs, narrays);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer ptrs, int narrays) {
            super((Pointer) null);
            allocate(arrays, ptrs, narrays);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer ptrs) {
            super((Pointer) null);
            allocate(arrays, ptrs);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) byte[] ptrs, int narrays) {
            super((Pointer) null);
            allocate(arrays, ptrs, narrays);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, @ByPtrPtr @Cast({"uchar**"}) byte[] ptrs) {
            super((Pointer) null);
            allocate(arrays, ptrs);
        }

        public NAryMatIterator(@Cast({"const cv::Mat**"}) PointerPointer arrays, Mat planes, int narrays) {
            super((Pointer) null);
            allocate(arrays, planes, narrays);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, Mat planes) {
            super((Pointer) null);
            allocate(arrays, planes);
        }

        public NAryMatIterator(@ByPtrPtr @Const Mat arrays, Mat planes, int narrays) {
            super((Pointer) null);
            allocate(arrays, planes, narrays);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class PCA extends Pointer {
        public static final int DATA_AS_COL = 1;
        public static final int DATA_AS_ROW = 0;
        public static final int USE_AVG = 2;

        private native void allocate();

        private native void allocate(@ByVal Mat mat, @ByVal Mat mat2, int i);

        private native void allocate(@ByVal Mat mat, @ByVal Mat mat2, int i, double d);

        private native void allocate(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

        private native void allocate(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

        private native void allocate(@ByVal UMat uMat, @ByVal UMat uMat2, int i, double d);

        private native void allocate(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

        private native void allocateArray(long j);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal Mat mat, @ByVal Mat mat2, int i);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal Mat mat, @ByVal Mat mat2, int i, double d);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal UMat uMat, @ByVal UMat uMat2, int i, double d);

        @ByRef
        @Name({"operator ()"})
        public native PCA apply(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

        @ByVal
        public native Mat backProject(@ByVal Mat mat);

        @ByVal
        public native Mat backProject(@ByVal UMat uMat);

        public native void backProject(@ByVal Mat mat, @ByVal Mat mat2);

        public native void backProject(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByRef
        public native Mat eigenvalues();

        public native PCA eigenvalues(Mat mat);

        @ByRef
        public native Mat eigenvectors();

        public native PCA eigenvectors(Mat mat);

        @ByRef
        public native Mat mean();

        public native PCA mean(Mat mat);

        @ByVal
        public native Mat project(@ByVal Mat mat);

        @ByVal
        public native Mat project(@ByVal UMat uMat);

        public native void project(@ByVal Mat mat, @ByVal Mat mat2);

        public native void project(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void read(@ByRef @Const FileNode fileNode);

        public native void write(@ByRef FileStorage fileStorage);

        static {
            Loader.load();
        }

        public PCA(Pointer p) {
            super(p);
        }

        public PCA(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PCA position(long position) {
            return (PCA) super.position(position);
        }

        public PCA() {
            super((Pointer) null);
            allocate();
        }

        public PCA(@ByVal Mat data, @ByVal Mat mean, int flags, int maxComponents) {
            super((Pointer) null);
            allocate(data, mean, flags, maxComponents);
        }

        public PCA(@ByVal Mat data, @ByVal Mat mean, int flags) {
            super((Pointer) null);
            allocate(data, mean, flags);
        }

        public PCA(@ByVal UMat data, @ByVal UMat mean, int flags, int maxComponents) {
            super((Pointer) null);
            allocate(data, mean, flags, maxComponents);
        }

        public PCA(@ByVal UMat data, @ByVal UMat mean, int flags) {
            super((Pointer) null);
            allocate(data, mean, flags);
        }

        public PCA(@ByVal Mat data, @ByVal Mat mean, int flags, double retainedVariance) {
            super((Pointer) null);
            allocate(data, mean, flags, retainedVariance);
        }

        public PCA(@ByVal UMat data, @ByVal UMat mean, int flags, double retainedVariance) {
            super((Pointer) null);
            allocate(data, mean, flags, retainedVariance);
        }
    }

    @Namespace("cv")
    public static class ParallelLoopBody extends Pointer {
        @Name({"operator ()"})
        public native void apply(@ByRef @Const Range range);

        static {
            Loader.load();
        }

        public ParallelLoopBody(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class Param extends Pointer {
        public static final int ALGORITHM = 6;
        public static final int BOOLEAN = 1;
        public static final int FLOAT = 7;
        public static final int INT = 0;
        public static final int MAT = 4;
        public static final int MAT_VECTOR = 5;
        public static final int REAL = 2;
        public static final int STRING = 3;
        public static final int UCHAR = 11;
        public static final int UINT64 = 9;
        public static final int UNSIGNED_INT = 8;

        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public Param() {
            super((Pointer) null);
            allocate();
        }

        public Param(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Param(Pointer p) {
            super(p);
        }

        public Param position(long position) {
            return (Param) super.position(position);
        }
    }

    @Name({"cv::Point_<double>"})
    @NoOffset
    public static class Point2d extends DoublePointer {
        private native void allocate();

        private native void allocate(double d, double d2);

        private native void allocate(@ByRef @Const Point2d point2d);

        private native void allocate(@ByRef @Const Size2d size2d);

        private native void allocateArray(long j);

        public native double cross(@ByRef @Const Point2d point2d);

        public native double ddot(@ByRef @Const Point2d point2d);

        public native double dot(@ByRef @Const Point2d point2d);

        @Cast({"bool"})
        public native boolean inside(@ByRef @Const Rectd rectd);

        @ByRef
        @Name({"operator ="})
        public native Point2d put(@ByRef @Const Point2d point2d);

        public native double m110x();

        public native Point2d m111x(double d);

        public native double m112y();

        public native Point2d m113y(double d);

        static {
            Loader.load();
        }

        public Point2d(Pointer p) {
            super(p);
        }

        public Point2d(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point2d position(long position) {
            return (Point2d) super.position(position);
        }

        public Point2d() {
            super((Pointer) null);
            allocate();
        }

        public Point2d(double _x, double _y) {
            super((Pointer) null);
            allocate(_x, _y);
        }

        public Point2d(@ByRef @Const Point2d pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point2d(@ByRef @Const Size2d sz) {
            super((Pointer) null);
            allocate(sz);
        }
    }

    @Name({"std::vector<cv::Point2d>"})
    public static class Point2dVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Point2d get(@Cast({"size_t"}) long j);

        public native Point2dVector put(@Cast({"size_t"}) long j, Point2d point2d);

        @ByRef
        @Name({"operator="})
        public native Point2dVector put(@ByRef Point2dVector point2dVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public Point2dVector(Pointer p) {
            super(p);
        }

        public Point2dVector(Point2d... array) {
            this((long) array.length);
            put(array);
        }

        public Point2dVector() {
            allocate();
        }

        public Point2dVector(long n) {
            allocate(n);
        }

        public Point2dVector put(Point2d... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::Point2d> >"})
    public static class Point2dVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Point2dVector get(@Cast({"size_t"}) long j);

        public native Point2dVectorVector put(@Cast({"size_t"}) long j, Point2dVector point2dVector);

        @ByRef
        @Name({"operator="})
        public native Point2dVectorVector put(@ByRef Point2dVectorVector point2dVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public Point2dVectorVector(Pointer p) {
            super(p);
        }

        public Point2dVectorVector(Point2dVector... array) {
            this((long) array.length);
            put(array);
        }

        public Point2dVectorVector() {
            allocate();
        }

        public Point2dVectorVector(long n) {
            allocate(n);
        }

        public Point2dVectorVector put(Point2dVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"cv::Point_<float>"})
    @NoOffset
    public static class Point2f extends FloatPointer {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocate(@ByRef @Const Point2f point2f);

        private native void allocate(@ByRef @Const Size2f size2f);

        private native void allocateArray(long j);

        public native double cross(@ByRef @Const Point2f point2f);

        public native double ddot(@ByRef @Const Point2f point2f);

        public native float dot(@ByRef @Const Point2f point2f);

        @Cast({"bool"})
        public native boolean inside(@ByRef @Const Rectf rectf);

        @ByRef
        @Name({"operator ="})
        public native Point2f put(@ByRef @Const Point2f point2f);

        public native float m114x();

        public native Point2f m115x(float f);

        public native float m116y();

        public native Point2f m117y(float f);

        static {
            Loader.load();
        }

        public Point2f(Pointer p) {
            super(p);
        }

        public Point2f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point2f position(long position) {
            return (Point2f) super.position(position);
        }

        public Point2f() {
            super((Pointer) null);
            allocate();
        }

        public Point2f(float _x, float _y) {
            super((Pointer) null);
            allocate(_x, _y);
        }

        public Point2f(@ByRef @Const Point2f pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point2f(@ByRef @Const Size2f sz) {
            super((Pointer) null);
            allocate(sz);
        }
    }

    @Name({"std::vector<cv::Point2f>"})
    public static class Point2fVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Point2f get(@Cast({"size_t"}) long j);

        public native Point2fVector put(@Cast({"size_t"}) long j, Point2f point2f);

        @ByRef
        @Name({"operator="})
        public native Point2fVector put(@ByRef Point2fVector point2fVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public Point2fVector(Pointer p) {
            super(p);
        }

        public Point2fVector(Point2f... array) {
            this((long) array.length);
            put(array);
        }

        public Point2fVector() {
            allocate();
        }

        public Point2fVector(long n) {
            allocate(n);
        }

        public Point2fVector put(Point2f... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::Point2f> >"})
    public static class Point2fVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Point2fVector get(@Cast({"size_t"}) long j);

        public native Point2fVectorVector put(@Cast({"size_t"}) long j, Point2fVector point2fVector);

        @ByRef
        @Name({"operator="})
        public native Point2fVectorVector put(@ByRef Point2fVectorVector point2fVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public Point2fVectorVector(Pointer p) {
            super(p);
        }

        public Point2fVectorVector(Point2fVector... array) {
            this((long) array.length);
            put(array);
        }

        public Point2fVectorVector() {
            allocate();
        }

        public Point2fVectorVector(long n) {
            allocate(n);
        }

        public Point2fVectorVector put(Point2fVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"cv::Point3_<double>"})
    @NoOffset
    public static class Point3d extends DoublePointer {
        private native void allocate();

        private native void allocate(double d, double d2, double d3);

        private native void allocate(@ByRef @Const Point2d point2d);

        private native void allocate(@ByRef @Const Point3d point3d);

        private native void allocateArray(long j);

        @ByVal
        public native Point3d cross(@ByRef @Const Point3d point3d);

        public native double ddot(@ByRef @Const Point3d point3d);

        public native double dot(@ByRef @Const Point3d point3d);

        @ByRef
        @Name({"operator ="})
        public native Point3d put(@ByRef @Const Point3d point3d);

        public native double m118x();

        public native Point3d m119x(double d);

        public native double m120y();

        public native Point3d m121y(double d);

        public native double m122z();

        public native Point3d m123z(double d);

        static {
            Loader.load();
        }

        public Point3d(Pointer p) {
            super(p);
        }

        public Point3d(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point3d position(long position) {
            return (Point3d) super.position(position);
        }

        public Point3d() {
            super((Pointer) null);
            allocate();
        }

        public Point3d(double _x, double _y, double _z) {
            super((Pointer) null);
            allocate(_x, _y, _z);
        }

        public Point3d(@ByRef @Const Point3d pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point3d(@ByRef @Const Point2d pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"cv::Point3_<float>"})
    @NoOffset
    public static class Point3f extends FloatPointer {
        private native void allocate();

        private native void allocate(float f, float f2, float f3);

        private native void allocate(@ByRef @Const Point2f point2f);

        private native void allocate(@ByRef @Const Point3f point3f);

        private native void allocateArray(long j);

        @ByVal
        public native Point3f cross(@ByRef @Const Point3f point3f);

        public native double ddot(@ByRef @Const Point3f point3f);

        public native float dot(@ByRef @Const Point3f point3f);

        @ByRef
        @Name({"operator ="})
        public native Point3f put(@ByRef @Const Point3f point3f);

        public native float m124x();

        public native Point3f m125x(float f);

        public native float m126y();

        public native Point3f m127y(float f);

        public native float m128z();

        public native Point3f m129z(float f);

        static {
            Loader.load();
        }

        public Point3f(Pointer p) {
            super(p);
        }

        public Point3f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point3f position(long position) {
            return (Point3f) super.position(position);
        }

        public Point3f() {
            super((Pointer) null);
            allocate();
        }

        public Point3f(float _x, float _y, float _z) {
            super((Pointer) null);
            allocate(_x, _y, _z);
        }

        public Point3f(@ByRef @Const Point3f pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point3f(@ByRef @Const Point2f pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"cv::Point3_<int>"})
    @NoOffset
    public static class Point3i extends IntPointer {
        private native void allocate();

        private native void allocate(int i, int i2, int i3);

        private native void allocate(@ByRef @Const Point3i point3i);

        private native void allocate(@ByRef @Const Point point);

        private native void allocateArray(long j);

        @ByVal
        public native Point3i cross(@ByRef @Const Point3i point3i);

        public native double ddot(@ByRef @Const Point3i point3i);

        public native int dot(@ByRef @Const Point3i point3i);

        @ByRef
        @Name({"operator ="})
        public native Point3i put(@ByRef @Const Point3i point3i);

        public native int m130x();

        public native Point3i m131x(int i);

        public native int m132y();

        public native Point3i m133y(int i);

        public native int m134z();

        public native Point3i m135z(int i);

        static {
            Loader.load();
        }

        public Point3i(Pointer p) {
            super(p);
        }

        public Point3i(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point3i position(long position) {
            return (Point3i) super.position(position);
        }

        public Point3i() {
            super((Pointer) null);
            allocate();
        }

        public Point3i(int _x, int _y, int _z) {
            super((Pointer) null);
            allocate(_x, _y, _z);
        }

        public Point3i(@ByRef @Const Point3i pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point3i(@ByRef @Const Point pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"cv::Point_<int>"})
    @NoOffset
    public static class Point extends IntPointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(@ByRef @Const Point point);

        private native void allocate(@ByRef @Const Size size);

        private native void allocateArray(long j);

        public native double cross(@ByRef @Const Point point);

        public native double ddot(@ByRef @Const Point point);

        public native int dot(@ByRef @Const Point point);

        @Cast({"bool"})
        public native boolean inside(@ByRef @Const Rect rect);

        @ByRef
        @Name({"operator ="})
        public native Point put(@ByRef @Const Point point);

        public native int m136x();

        public native Point m137x(int i);

        public native int m138y();

        public native Point m139y(int i);

        static {
            Loader.load();
        }

        public Point(Pointer p) {
            super(p);
        }

        public Point(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Point position(long position) {
            return (Point) super.position(position);
        }

        public Point() {
            super((Pointer) null);
            allocate();
        }

        public Point(int _x, int _y) {
            super((Pointer) null);
            allocate(_x, _y);
        }

        public Point(@ByRef @Const Point pt) {
            super((Pointer) null);
            allocate(pt);
        }

        public Point(@ByRef @Const Size sz) {
            super((Pointer) null);
            allocate(sz);
        }
    }

    @Name({"std::vector<cv::Point>"})
    public static class PointVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Point get(@Cast({"size_t"}) long j);

        public native PointVector put(@Cast({"size_t"}) long j, Point point);

        @ByRef
        @Name({"operator="})
        public native PointVector put(@ByRef PointVector pointVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public PointVector(Pointer p) {
            super(p);
        }

        public PointVector(Point... array) {
            this((long) array.length);
            put(array);
        }

        public PointVector() {
            allocate();
        }

        public PointVector(long n) {
            allocate(n);
        }

        public PointVector put(Point... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::Point> >"})
    public static class PointVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native PointVector get(@Cast({"size_t"}) long j);

        public native PointVectorVector put(@Cast({"size_t"}) long j, PointVector pointVector);

        @ByRef
        @Name({"operator="})
        public native PointVectorVector put(@ByRef PointVectorVector pointVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public PointVectorVector(Pointer p) {
            super(p);
        }

        public PointVectorVector(PointVector... array) {
            this((long) array.length);
            put(array);
        }

        public PointVectorVector() {
            allocate();
        }

        public PointVectorVector(long n) {
            allocate(n);
        }

        public PointVectorVector put(PointVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv::detail")
    @Opaque
    public static class PtrOwner extends Pointer {
        public PtrOwner() {
            super((Pointer) null);
        }

        public PtrOwner(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class RNG extends Pointer {
        public static final int NORMAL = 1;
        public static final int UNIFORM = 0;

        private native void allocate();

        private native void allocate(@Cast({"uint64"}) int i);

        private native void allocateArray(long j);

        @Name({"fill"})
        public native void _fill(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal Mat mat3);

        @Name({"fill"})
        public native void _fill(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal Mat mat3, @Cast({"bool"}) boolean z);

        @Name({"fill"})
        public native void _fill(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal UMat uMat3);

        @Name({"fill"})
        public native void _fill(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal UMat uMat3, @Cast({"bool"}) boolean z);

        @Cast({"unsigned"})
        @Name({"operator ()"})
        public native int apply();

        @Cast({"unsigned"})
        @Name({"operator ()"})
        public native int apply(@Cast({"unsigned"}) int i);

        @Name({"operator uchar"})
        public native byte asByte();

        @Name({"operator double"})
        public native double asDouble();

        @Name({"operator float"})
        public native float asFloat();

        @Name({"operator unsigned"})
        public native int asInt();

        @Name({"operator ushort"})
        public native short asShort();

        public native double gaussian(double d);

        @Cast({"unsigned"})
        public native int next();

        @Cast({"uint64"})
        public native int state();

        public native RNG state(int i);

        public native double uniform(double d, double d2);

        public native float uniform(float f, float f2);

        public native int uniform(int i, int i2);

        static {
            Loader.load();
        }

        public RNG(Pointer p) {
            super(p);
        }

        public RNG(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public RNG position(long position) {
            return (RNG) super.position(position);
        }

        public RNG() {
            super((Pointer) null);
            allocate();
        }

        public RNG(@Cast({"uint64"}) int state) {
            super((Pointer) null);
            allocate(state);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class RNG_MT19937 extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"unsigned"}) int i);

        private native void allocateArray(long j);

        @Cast({"unsigned"})
        @Name({"operator ()"})
        public native int apply();

        @Cast({"unsigned"})
        @Name({"operator ()"})
        public native int apply(@Cast({"unsigned"}) int i);

        @Name({"operator double"})
        public native double asDouble();

        @Name({"operator float"})
        public native float asFloat();

        @Name({"operator int"})
        public native int asInt();

        @Cast({"unsigned"})
        public native int next();

        public native void seed(@Cast({"unsigned"}) int i);

        public native double uniform(double d, double d2);

        public native float uniform(float f, float f2);

        public native int uniform(int i, int i2);

        static {
            Loader.load();
        }

        public RNG_MT19937(Pointer p) {
            super(p);
        }

        public RNG_MT19937(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public RNG_MT19937 position(long position) {
            return (RNG_MT19937) super.position(position);
        }

        public RNG_MT19937() {
            super((Pointer) null);
            allocate();
        }

        public RNG_MT19937(@Cast({"unsigned"}) int s) {
            super((Pointer) null);
            allocate(s);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class Range extends Pointer {
        @ByVal
        public static native Range all();

        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean empty();

        public native int end();

        public native Range end(int i);

        public native int size();

        public native int start();

        public native Range start(int i);

        static {
            Loader.load();
        }

        public Range(Pointer p) {
            super(p);
        }

        public Range(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Range position(long position) {
            return (Range) super.position(position);
        }

        public Range() {
            super((Pointer) null);
            allocate();
        }

        public Range(int _start, int _end) {
            super((Pointer) null);
            allocate(_start, _end);
        }
    }

    @Name({"cv::Rect_<int>"})
    @NoOffset
    public static class Rect extends IntPointer {
        private native void allocate();

        private native void allocate(int i, int i2, int i3, int i4);

        private native void allocate(@ByRef @Const Point point, @ByRef @Const Point point2);

        private native void allocate(@ByRef @Const Point point, @ByRef @Const Size size);

        private native void allocate(@ByRef @Const Rect rect);

        private native void allocateArray(long j);

        public native int area();

        @ByVal
        public native Point br();

        @Cast({"bool"})
        public native boolean contains(@ByRef @Const Point point);

        public native int height();

        public native Rect height(int i);

        @ByRef
        @Name({"operator ="})
        public native Rect put(@ByRef @Const Rect rect);

        @ByVal
        public native Size size();

        @ByVal
        public native Point tl();

        public native int width();

        public native Rect width(int i);

        public native int m140x();

        public native Rect m141x(int i);

        public native int m142y();

        public native Rect m143y(int i);

        static {
            Loader.load();
        }

        public Rect(Pointer p) {
            super(p);
        }

        public Rect(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Rect position(long position) {
            return (Rect) super.position(position);
        }

        public Rect() {
            super((Pointer) null);
            allocate();
        }

        public Rect(int _x, int _y, int _width, int _height) {
            super((Pointer) null);
            allocate(_x, _y, _width, _height);
        }

        public Rect(@ByRef @Const Rect r) {
            super((Pointer) null);
            allocate(r);
        }

        public Rect(@ByRef @Const Point org, @ByRef @Const Size sz) {
            super((Pointer) null);
            allocate(org, sz);
        }

        public Rect(@ByRef @Const Point pt1, @ByRef @Const Point pt2) {
            super((Pointer) null);
            allocate(pt1, pt2);
        }
    }

    @Name({"std::vector<cv::Rect>"})
    public static class RectVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Rect get(@Cast({"size_t"}) long j);

        public native RectVector put(@Cast({"size_t"}) long j, Rect rect);

        @ByRef
        @Name({"operator="})
        public native RectVector put(@ByRef RectVector rectVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public RectVector(Pointer p) {
            super(p);
        }

        public RectVector(Rect... array) {
            this((long) array.length);
            put(array);
        }

        public RectVector() {
            allocate();
        }

        public RectVector(long n) {
            allocate(n);
        }

        public RectVector put(Rect... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"std::vector<std::vector<cv::Rect> >"})
    public static class RectVectorVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native RectVector get(@Cast({"size_t"}) long j);

        public native RectVectorVector put(@Cast({"size_t"}) long j, RectVector rectVector);

        @ByRef
        @Name({"operator="})
        public native RectVectorVector put(@ByRef RectVectorVector rectVectorVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public RectVectorVector(Pointer p) {
            super(p);
        }

        public RectVectorVector(RectVector... array) {
            this((long) array.length);
            put(array);
        }

        public RectVectorVector() {
            allocate();
        }

        public RectVectorVector(long n) {
            allocate(n);
        }

        public RectVectorVector put(RectVector... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Name({"cv::Rect_<double>"})
    @NoOffset
    public static class Rectd extends DoublePointer {
        private native void allocate();

        private native void allocate(double d, double d2, double d3, double d4);

        private native void allocate(@ByRef @Const Point2d point2d, @ByRef @Const Point2d point2d2);

        private native void allocate(@ByRef @Const Point2d point2d, @ByRef @Const Size2d size2d);

        private native void allocate(@ByRef @Const Rectd rectd);

        private native void allocateArray(long j);

        public native double area();

        @ByVal
        public native Point2d br();

        @Cast({"bool"})
        public native boolean contains(@ByRef @Const Point2d point2d);

        public native double height();

        public native Rectd height(double d);

        @ByRef
        @Name({"operator ="})
        public native Rectd put(@ByRef @Const Rectd rectd);

        @ByVal
        public native Size2d size();

        @ByVal
        public native Point2d tl();

        public native double width();

        public native Rectd width(double d);

        public native double m144x();

        public native Rectd m145x(double d);

        public native double m146y();

        public native Rectd m147y(double d);

        static {
            Loader.load();
        }

        public Rectd(Pointer p) {
            super(p);
        }

        public Rectd(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Rectd position(long position) {
            return (Rectd) super.position(position);
        }

        public Rectd() {
            super((Pointer) null);
            allocate();
        }

        public Rectd(double _x, double _y, double _width, double _height) {
            super((Pointer) null);
            allocate(_x, _y, _width, _height);
        }

        public Rectd(@ByRef @Const Rectd r) {
            super((Pointer) null);
            allocate(r);
        }

        public Rectd(@ByRef @Const Point2d org, @ByRef @Const Size2d sz) {
            super((Pointer) null);
            allocate(org, sz);
        }

        public Rectd(@ByRef @Const Point2d pt1, @ByRef @Const Point2d pt2) {
            super((Pointer) null);
            allocate(pt1, pt2);
        }
    }

    @Name({"cv::Rect_<float>"})
    @NoOffset
    public static class Rectf extends FloatPointer {
        private native void allocate();

        private native void allocate(float f, float f2, float f3, float f4);

        private native void allocate(@ByRef @Const Point2f point2f, @ByRef @Const Point2f point2f2);

        private native void allocate(@ByRef @Const Point2f point2f, @ByRef @Const Size2f size2f);

        private native void allocate(@ByRef @Const Rectf rectf);

        private native void allocateArray(long j);

        public native float area();

        @ByVal
        public native Point2f br();

        @Cast({"bool"})
        public native boolean contains(@ByRef @Const Point2f point2f);

        public native float height();

        public native Rectf height(float f);

        @ByRef
        @Name({"operator ="})
        public native Rectf put(@ByRef @Const Rectf rectf);

        @ByVal
        public native Size2f size();

        @ByVal
        public native Point2f tl();

        public native float width();

        public native Rectf width(float f);

        public native float m148x();

        public native Rectf m149x(float f);

        public native float m150y();

        public native Rectf m151y(float f);

        static {
            Loader.load();
        }

        public Rectf(Pointer p) {
            super(p);
        }

        public Rectf(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Rectf position(long position) {
            return (Rectf) super.position(position);
        }

        public Rectf() {
            super((Pointer) null);
            allocate();
        }

        public Rectf(float _x, float _y, float _width, float _height) {
            super((Pointer) null);
            allocate(_x, _y, _width, _height);
        }

        public Rectf(@ByRef @Const Rectf r) {
            super((Pointer) null);
            allocate(r);
        }

        public Rectf(@ByRef @Const Point2f org, @ByRef @Const Size2f sz) {
            super((Pointer) null);
            allocate(org, sz);
        }

        public Rectf(@ByRef @Const Point2f pt1, @ByRef @Const Point2f pt2) {
            super((Pointer) null);
            allocate(pt1, pt2);
        }
    }

    @Name({"cv::detail::RefOrVoid<void>"})
    public static class RefOrVoid extends Pointer {

        @Opaque
        public static class type extends Pointer {
            public type() {
                super((Pointer) null);
            }

            public type(Pointer p) {
                super(p);
            }
        }

        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public RefOrVoid() {
            super((Pointer) null);
            allocate();
        }

        public RefOrVoid(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public RefOrVoid(Pointer p) {
            super(p);
        }

        public RefOrVoid position(long position) {
            return (RefOrVoid) super.position(position);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class RotatedRect extends FloatPointer {
        private native void allocate();

        private native void allocate(@ByRef @Const Point2f point2f, @ByRef @Const Point2f point2f2, @ByRef @Const Point2f point2f3);

        private native void allocate(@ByRef @Const Point2f point2f, @ByRef @Const Size2f size2f, float f);

        private native void allocateArray(long j);

        public native float angle();

        public native RotatedRect angle(float f);

        @ByVal
        public native Rect boundingRect();

        @ByRef
        public native Point2f center();

        public native RotatedRect center(Point2f point2f);

        public native void points(Point2f point2f);

        public native RotatedRect size(Size2f size2f);

        @ByRef
        public native Size2f size();

        static {
            Loader.load();
        }

        public RotatedRect(Pointer p) {
            super(p);
        }

        public RotatedRect(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public RotatedRect position(long position) {
            return (RotatedRect) super.position(position);
        }

        public RotatedRect() {
            super((Pointer) null);
            allocate();
        }

        public RotatedRect(@ByRef @Const Point2f center, @ByRef @Const Size2f size, float angle) {
            super((Pointer) null);
            allocate(center, size, angle);
        }

        public RotatedRect(@ByRef @Const Point2f point1, @ByRef @Const Point2f point2, @ByRef @Const Point2f point3) {
            super((Pointer) null);
            allocate(point1, point2, point3);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class SVD extends Pointer {
        public static final int FULL_UV = 4;
        public static final int MODIFY_A = 1;
        public static final int NO_UV = 2;

        private native void allocate();

        private native void allocate(@ByVal Mat mat);

        private native void allocate(@ByVal Mat mat, int i);

        private native void allocate(@ByVal UMat uMat);

        private native void allocate(@ByVal UMat uMat, int i);

        private native void allocateArray(long j);

        public static native void backSubst(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

        public static native void backSubst(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

        public static native void compute(@ByVal Mat mat, @ByVal Mat mat2);

        public static native void compute(@ByVal Mat mat, @ByVal Mat mat2, int i);

        public static native void compute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        public static native void compute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, int i);

        public static native void compute(@ByVal UMat uMat, @ByVal UMat uMat2);

        public static native void compute(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

        public static native void compute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        public static native void compute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, int i);

        public static native void solveZ(@ByVal Mat mat, @ByVal Mat mat2);

        public static native void solveZ(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByRef
        @Name({"operator ()"})
        public native SVD apply(@ByVal Mat mat);

        @ByRef
        @Name({"operator ()"})
        public native SVD apply(@ByVal Mat mat, int i);

        @ByRef
        @Name({"operator ()"})
        public native SVD apply(@ByVal UMat uMat);

        @ByRef
        @Name({"operator ()"})
        public native SVD apply(@ByVal UMat uMat, int i);

        public native void backSubst(@ByVal Mat mat, @ByVal Mat mat2);

        public native void backSubst(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByRef
        public native Mat m152u();

        public native SVD m153u(Mat mat);

        @ByRef
        public native Mat vt();

        public native SVD vt(Mat mat);

        @ByRef
        public native Mat m154w();

        public native SVD m155w(Mat mat);

        static {
            Loader.load();
        }

        public SVD(Pointer p) {
            super(p);
        }

        public SVD(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SVD position(long position) {
            return (SVD) super.position(position);
        }

        public SVD() {
            super((Pointer) null);
            allocate();
        }

        public SVD(@ByVal Mat src, int flags) {
            super((Pointer) null);
            allocate(src, flags);
        }

        public SVD(@ByVal Mat src) {
            super((Pointer) null);
            allocate(src);
        }

        public SVD(@ByVal UMat src, int flags) {
            super((Pointer) null);
            allocate(src, flags);
        }

        public SVD(@ByVal UMat src) {
            super((Pointer) null);
            allocate(src);
        }
    }

    @Name({"cv::Scalar_<int>"})
    public static class Scalar4i extends IntPointer {
        @ByVal
        public static native Scalar4i all(int i);

        private native void allocate();

        private native void allocate(int i);

        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4);

        private native void allocateArray(long j);

        @ByVal
        public native Scalar4i conj();

        @Cast({"bool"})
        public native boolean isReal();

        @ByVal
        public native Scalar4i mul(@ByRef @Const Scalar4i scalar4i);

        @ByVal
        public native Scalar4i mul(@ByRef @Const Scalar4i scalar4i, double d);

        static {
            Loader.load();
        }

        public Scalar4i(Pointer p) {
            super(p);
        }

        public Scalar4i(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Scalar4i position(long position) {
            return (Scalar4i) super.position(position);
        }

        public Scalar4i() {
            super((Pointer) null);
            allocate();
        }

        public Scalar4i(int v0, int v1, int v2, int v3) {
            super((Pointer) null);
            allocate(v0, v1, v2, v3);
        }

        public Scalar4i(int v0, int v1) {
            super((Pointer) null);
            allocate(v0, v1);
        }

        public Scalar4i(int v0) {
            super((Pointer) null);
            allocate(v0);
        }
    }

    @Name({"cv::Scalar_<double>"})
    public static class Scalar extends AbstractScalar {
        @ByVal
        public static native Scalar all(double d);

        private native void allocate();

        private native void allocate(double d);

        private native void allocate(double d, double d2);

        private native void allocate(double d, double d2, double d3, double d4);

        private native void allocateArray(long j);

        @ByVal
        public native Scalar conj();

        @Cast({"bool"})
        public native boolean isReal();

        @ByVal
        public native Scalar mul(@ByRef @Const Scalar scalar);

        @ByVal
        public native Scalar mul(@ByRef @Const Scalar scalar, double d);

        static {
            Loader.load();
        }

        public Scalar(Pointer p) {
            super(p);
        }

        public Scalar(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Scalar position(long position) {
            return (Scalar) super.position(position);
        }

        public Scalar() {
            super((Pointer) null);
            allocate();
        }

        public Scalar(double v0, double v1, double v2, double v3) {
            super((Pointer) null);
            allocate(v0, v1, v2, v3);
        }

        public Scalar(double v0, double v1) {
            super((Pointer) null);
            allocate(v0, v1);
        }

        public Scalar(double v0) {
            super((Pointer) null);
            allocate(v0);
        }
    }

    @Name({"cv::Size_<double>"})
    @NoOffset
    public static class Size2d extends DoublePointer {
        private native void allocate();

        private native void allocate(double d, double d2);

        private native void allocate(@ByRef @Const Point2d point2d);

        private native void allocate(@ByRef @Const Size2d size2d);

        private native void allocateArray(long j);

        public native double area();

        public native double height();

        public native Size2d height(double d);

        @ByRef
        @Name({"operator ="})
        public native Size2d put(@ByRef @Const Size2d size2d);

        public native double width();

        public native Size2d width(double d);

        static {
            Loader.load();
        }

        public Size2d(Pointer p) {
            super(p);
        }

        public Size2d(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Size2d position(long position) {
            return (Size2d) super.position(position);
        }

        public Size2d() {
            super((Pointer) null);
            allocate();
        }

        public Size2d(double _width, double _height) {
            super((Pointer) null);
            allocate(_width, _height);
        }

        public Size2d(@ByRef @Const Size2d sz) {
            super((Pointer) null);
            allocate(sz);
        }

        public Size2d(@ByRef @Const Point2d pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"cv::Size_<float>"})
    @NoOffset
    public static class Size2f extends FloatPointer {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocate(@ByRef @Const Point2f point2f);

        private native void allocate(@ByRef @Const Size2f size2f);

        private native void allocateArray(long j);

        public native float area();

        public native float height();

        public native Size2f height(float f);

        @ByRef
        @Name({"operator ="})
        public native Size2f put(@ByRef @Const Size2f size2f);

        public native float width();

        public native Size2f width(float f);

        static {
            Loader.load();
        }

        public Size2f(Pointer p) {
            super(p);
        }

        public Size2f(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Size2f position(long position) {
            return (Size2f) super.position(position);
        }

        public Size2f() {
            super((Pointer) null);
            allocate();
        }

        public Size2f(float _width, float _height) {
            super((Pointer) null);
            allocate(_width, _height);
        }

        public Size2f(@ByRef @Const Size2f sz) {
            super((Pointer) null);
            allocate(sz);
        }

        public Size2f(@ByRef @Const Point2f pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"cv::Size_<int>"})
    @NoOffset
    public static class Size extends IntPointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(@ByRef @Const Point point);

        private native void allocate(@ByRef @Const Size size);

        private native void allocateArray(long j);

        public native int area();

        public native int height();

        public native Size height(int i);

        @ByRef
        @Name({"operator ="})
        public native Size put(@ByRef @Const Size size);

        public native int width();

        public native Size width(int i);

        static {
            Loader.load();
        }

        public Size(Pointer p) {
            super(p);
        }

        public Size(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Size position(long position) {
            return (Size) super.position(position);
        }

        public Size() {
            super((Pointer) null);
            allocate();
        }

        public Size(int _width, int _height) {
            super((Pointer) null);
            allocate(_width, _height);
        }

        public Size(@ByRef @Const Size sz) {
            super((Pointer) null);
            allocate(sz);
        }

        public Size(@ByRef @Const Point pt) {
            super((Pointer) null);
            allocate(pt);
        }
    }

    @Name({"std::vector<cv::Size>"})
    public static class SizeVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native Size get(@Cast({"size_t"}) long j);

        public native SizeVector put(@Cast({"size_t"}) long j, Size size);

        @ByRef
        @Name({"operator="})
        public native SizeVector put(@ByRef SizeVector sizeVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public SizeVector(Pointer p) {
            super(p);
        }

        public SizeVector(Size... array) {
            this((long) array.length);
            put(array);
        }

        public SizeVector() {
            allocate();
        }

        public SizeVector(long n) {
            allocate(n);
        }

        public SizeVector put(Size... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class SparseMat extends Pointer {
        public static final int HASH_BIT = Integer.MIN_VALUE;
        public static final int HASH_SCALE = 1540483477;
        public static final int MAGIC_VAL = 1123876864;
        public static final int MAX_DIM = 32;

        @NoOffset
        public static class Hdr extends Pointer {
            private native void allocate(int i, @Const IntBuffer intBuffer, int i2);

            private native void allocate(int i, @Const IntPointer intPointer, int i2);

            private native void allocate(int i, @Const int[] iArr, int i2);

            public native void clear();

            public native int dims();

            public native Hdr dims(int i);

            @Cast({"size_t"})
            public native long freeList();

            public native Hdr freeList(long j);

            @Cast({"size_t*"})
            @StdVector
            public native SizeTPointer hashtab();

            public native Hdr hashtab(SizeTPointer sizeTPointer);

            @Cast({"size_t"})
            public native long nodeCount();

            public native Hdr nodeCount(long j);

            @Cast({"size_t"})
            public native long nodeSize();

            public native Hdr nodeSize(long j);

            @Cast({"uchar*"})
            @StdVector
            public native BytePointer pool();

            public native Hdr pool(BytePointer bytePointer);

            public native int refcount();

            public native Hdr refcount(int i);

            public native int size(int i);

            @MemberGetter
            public native IntPointer size();

            public native Hdr size(int i, int i2);

            public native int valueOffset();

            public native Hdr valueOffset(int i);

            static {
                Loader.load();
            }

            public Hdr(Pointer p) {
                super(p);
            }

            public Hdr(int _dims, @Const IntPointer _sizes, int _type) {
                super((Pointer) null);
                allocate(_dims, _sizes, _type);
            }

            public Hdr(int _dims, @Const IntBuffer _sizes, int _type) {
                super((Pointer) null);
                allocate(_dims, _sizes, _type);
            }

            public Hdr(int _dims, @Const int[] _sizes, int _type) {
                super((Pointer) null);
                allocate(_dims, _sizes, _type);
            }
        }

        public static class Node extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            @Cast({"size_t"})
            public native long hashval();

            public native Node hashval(long j);

            public native int idx(int i);

            @MemberGetter
            public native IntPointer idx();

            public native Node idx(int i, int i2);

            @Cast({"size_t"})
            public native long next();

            public native Node next(long j);

            static {
                Loader.load();
            }

            public Node() {
                super((Pointer) null);
                allocate();
            }

            public Node(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Node(Pointer p) {
                super(p);
            }

            public Node position(long position) {
                return (Node) super.position(position);
            }
        }

        private native void allocate();

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2);

        private native void allocate(int i, @Const IntPointer intPointer, int i2);

        private native void allocate(int i, @Const int[] iArr, int i2);

        private native void allocate(@ByRef @Const Mat mat);

        private native void allocate(@ByRef @Const SparseMat sparseMat);

        private native void allocateArray(long j);

        public native void addref();

        public native void assignTo(@ByRef SparseMat sparseMat);

        public native void assignTo(@ByRef SparseMat sparseMat, int i);

        @ByVal
        public native SparseMatIterator begin();

        public native int channels();

        public native void clear();

        @ByVal
        public native SparseMat clone();

        public native void convertTo(@ByRef Mat mat, int i);

        public native void convertTo(@ByRef Mat mat, int i, double d, double d2);

        public native void convertTo(@ByRef SparseMat sparseMat, int i);

        public native void convertTo(@ByRef SparseMat sparseMat, int i, double d);

        public native void copyTo(@ByRef Mat mat);

        public native void copyTo(@ByRef SparseMat sparseMat);

        public native void create(int i, @Const IntBuffer intBuffer, int i2);

        public native void create(int i, @Const IntPointer intPointer, int i2);

        public native void create(int i, @Const int[] iArr, int i2);

        public native int depth();

        public native int dims();

        @Cast({"size_t"})
        public native long elemSize();

        @Cast({"size_t"})
        public native long elemSize1();

        @ByVal
        public native SparseMatIterator end();

        public native void erase(int i, int i2);

        public native void erase(int i, int i2, int i3);

        public native void erase(int i, int i2, int i3, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        public native void erase(int i, int i2, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        public native void erase(@Const IntBuffer intBuffer);

        public native void erase(@Const IntBuffer intBuffer, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        public native void erase(@Const IntPointer intPointer);

        public native void erase(@Const IntPointer intPointer, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        public native void erase(@Const int[] iArr);

        public native void erase(@Const int[] iArr, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        public native int flags();

        public native SparseMat flags(int i);

        @Cast({"size_t"})
        public native long hash(int i);

        @Cast({"size_t"})
        public native long hash(int i, int i2);

        @Cast({"size_t"})
        public native long hash(int i, int i2, int i3);

        @Cast({"size_t"})
        public native long hash(@Const IntBuffer intBuffer);

        @Cast({"size_t"})
        public native long hash(@Const IntPointer intPointer);

        @Cast({"size_t"})
        public native long hash(@Const int[] iArr);

        public native Hdr hdr();

        public native SparseMat hdr(Hdr hdr);

        @Cast({"uchar*"})
        public native ByteBuffer newNode(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j);

        @Cast({"uchar*"})
        public native BytePointer newNode(@Const IntPointer intPointer, @Cast({"size_t"}) long j);

        @Cast({"uchar*"})
        public native byte[] newNode(@Const int[] iArr, @Cast({"size_t"}) long j);

        public native Node node(@Cast({"size_t"}) long j);

        @Cast({"size_t"})
        public native long nzcount();

        @Cast({"uchar*"})
        public native ByteBuffer ptr(@Const IntBuffer intBuffer, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native ByteBuffer ptr(@Const IntBuffer intBuffer, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, int i3, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, int i3, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, int i2, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native BytePointer ptr(int i, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"uchar*"})
        public native BytePointer ptr(@Const IntPointer intPointer, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native BytePointer ptr(@Const IntPointer intPointer, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"uchar*"})
        public native byte[] ptr(@Const int[] iArr, @Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native byte[] ptr(@Const int[] iArr, @Cast({"bool"}) boolean z, @Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @ByRef
        @Name({"operator ="})
        public native SparseMat put(@ByRef @Const Mat mat);

        @ByRef
        @Name({"operator ="})
        public native SparseMat put(@ByRef @Const SparseMat sparseMat);

        public native void release();

        public native void removeNode(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, @Cast({"size_t"}) long j3);

        public native void resizeHashTab(@Cast({"size_t"}) long j);

        public native int size(int i);

        @Const
        public native IntPointer size();

        public native int type();

        static {
            Loader.load();
        }

        public SparseMat(Pointer p) {
            super(p);
        }

        public SparseMat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SparseMat position(long position) {
            return (SparseMat) super.position(position);
        }

        public SparseMat() {
            super((Pointer) null);
            allocate();
        }

        public SparseMat(int dims, @Const IntPointer _sizes, int _type) {
            super((Pointer) null);
            allocate(dims, _sizes, _type);
        }

        public SparseMat(int dims, @Const IntBuffer _sizes, int _type) {
            super((Pointer) null);
            allocate(dims, _sizes, _type);
        }

        public SparseMat(int dims, @Const int[] _sizes, int _type) {
            super((Pointer) null);
            allocate(dims, _sizes, _type);
        }

        public SparseMat(@ByRef @Const SparseMat m) {
            super((Pointer) null);
            allocate(m);
        }

        public SparseMat(@ByRef @Const Mat m) {
            super((Pointer) null);
            allocate(m);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class SparseMatConstIterator extends Pointer {
        private native void allocate();

        private native void allocate(@Const SparseMat sparseMat);

        private native void allocate(@ByRef @Const SparseMatConstIterator sparseMatConstIterator);

        private native void allocateArray(long j);

        @Cast({"size_t"})
        public native long hashidx();

        public native SparseMatConstIterator hashidx(long j);

        @ByRef
        @Name({"operator ++"})
        public native SparseMatConstIterator increment();

        @ByVal
        @Name({"operator ++"})
        public native SparseMatConstIterator increment(int i);

        @MemberGetter
        @Const
        public native SparseMat m156m();

        @Const
        public native Node node();

        @Cast({"uchar*"})
        public native BytePointer ptr();

        public native SparseMatConstIterator ptr(BytePointer bytePointer);

        @ByRef
        @Name({"operator ="})
        public native SparseMatConstIterator put(@ByRef @Const SparseMatConstIterator sparseMatConstIterator);

        public native void seekEnd();

        static {
            Loader.load();
        }

        public SparseMatConstIterator(Pointer p) {
            super(p);
        }

        public SparseMatConstIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SparseMatConstIterator position(long position) {
            return (SparseMatConstIterator) super.position(position);
        }

        public SparseMatConstIterator() {
            super((Pointer) null);
            allocate();
        }

        public SparseMatConstIterator(@Const SparseMat _m) {
            super((Pointer) null);
            allocate(_m);
        }

        public SparseMatConstIterator(@ByRef @Const SparseMatConstIterator it) {
            super((Pointer) null);
            allocate(it);
        }
    }

    @Namespace("cv")
    public static class SparseMatIterator extends SparseMatConstIterator {
        private native void allocate();

        private native void allocate(SparseMat sparseMat);

        private native void allocate(@ByRef @Const SparseMatIterator sparseMatIterator);

        private native void allocateArray(long j);

        @ByRef
        @Name({"operator ++"})
        public native SparseMatIterator increment();

        @ByVal
        @Name({"operator ++"})
        public native SparseMatIterator increment(int i);

        public native Node node();

        @ByRef
        @Name({"operator ="})
        public native SparseMatIterator put(@ByRef @Const SparseMatIterator sparseMatIterator);

        static {
            Loader.load();
        }

        public SparseMatIterator(Pointer p) {
            super(p);
        }

        public SparseMatIterator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SparseMatIterator position(long position) {
            return (SparseMatIterator) super.position(position);
        }

        public SparseMatIterator() {
            super((Pointer) null);
            allocate();
        }

        public SparseMatIterator(SparseMat _m) {
            super((Pointer) null);
            allocate(_m);
        }

        public SparseMatIterator(@ByRef @Const SparseMatIterator it) {
            super((Pointer) null);
            allocate(it);
        }
    }

    @Namespace("cv::cuda")
    @Opaque
    public static class Stream extends Pointer {
        public Stream() {
            super((Pointer) null);
        }

        public Stream(Pointer p) {
            super(p);
        }
    }

    @Name({"std::vector<cv::String>"})
    public static class StringVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @Str
        @Index
        public native BytePointer get(@Cast({"size_t"}) long j);

        @Index
        @ValueSetter
        public native StringVector put(@Cast({"size_t"}) long j, @Str String str);

        public native StringVector put(@Cast({"size_t"}) long j, BytePointer bytePointer);

        @ByRef
        @Name({"operator="})
        public native StringVector put(@ByRef StringVector stringVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public StringVector(Pointer p) {
            super(p);
        }

        public StringVector(BytePointer... array) {
            this((long) array.length);
            put(array);
        }

        public StringVector(String... array) {
            this((long) array.length);
            put(array);
        }

        public StringVector() {
            allocate();
        }

        public StringVector(long n) {
            allocate(n);
        }

        public StringVector put(BytePointer... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }

        public StringVector put(String... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class TLSDataContainer extends Pointer {
        public native Pointer createDataInstance();

        public native void deleteDataInstance(Pointer pointer);

        public native Pointer getData();

        public native int key_();

        public native TLSDataContainer key_(int i);

        static {
            Loader.load();
        }

        public TLSDataContainer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class TermCriteria extends Pointer {
        public static final int COUNT = 1;
        public static final int EPS = 2;
        public static final int MAX_ITER = 1;

        private native void allocate();

        private native void allocate(int i, int i2, double d);

        private native void allocateArray(long j);

        public native double epsilon();

        public native TermCriteria epsilon(double d);

        public native int maxCount();

        public native TermCriteria maxCount(int i);

        public native int type();

        public native TermCriteria type(int i);

        static {
            Loader.load();
        }

        public TermCriteria(Pointer p) {
            super(p);
        }

        public TermCriteria(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TermCriteria position(long position) {
            return (TermCriteria) super.position(position);
        }

        public TermCriteria() {
            super((Pointer) null);
            allocate();
        }

        public TermCriteria(int type, int maxCount, double epsilon) {
            super((Pointer) null);
            allocate(type, maxCount, epsilon);
        }
    }

    @Namespace("cv::ogl")
    @Opaque
    public static class Texture2D extends Pointer {
        public Texture2D() {
            super((Pointer) null);
        }

        public Texture2D(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class UMat extends Pointer {
        public static final int AUTO_STEP = 0;
        public static final int CONTINUOUS_FLAG = 16384;
        public static final int DEPTH_MASK = 7;
        public static final int MAGIC_MASK = -65536;
        public static final int MAGIC_VAL = 1124007936;
        public static final int SUBMATRIX_FLAG = 32768;
        public static final int TYPE_MASK = 4095;

        private native void allocate();

        private native void allocate(@Cast({"cv::UMatUsageFlags"}) int i);

        private native void allocate(int i, int i2, int i3);

        private native void allocate(int i, int i2, int i3, @Cast({"cv::UMatUsageFlags"}) int i4);

        private native void allocate(int i, int i2, int i3, @ByRef @Const Scalar scalar);

        private native void allocate(int i, int i2, int i3, @ByRef @Const Scalar scalar, @Cast({"cv::UMatUsageFlags"}) int i4);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const IntBuffer intBuffer, int i2, @ByRef @Const Scalar scalar, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(int i, @Const IntPointer intPointer, int i2);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const IntPointer intPointer, int i2, @ByRef @Const Scalar scalar, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(int i, @Const int[] iArr, int i2);

        private native void allocate(int i, @Const int[] iArr, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(int i, @Const int[] iArr, int i2, @ByRef @Const Scalar scalar);

        private native void allocate(int i, @Const int[] iArr, int i2, @ByRef @Const Scalar scalar, @Cast({"cv::UMatUsageFlags"}) int i3);

        private native void allocate(@ByVal Size size, int i);

        private native void allocate(@ByVal Size size, int i, @Cast({"cv::UMatUsageFlags"}) int i2);

        private native void allocate(@ByVal Size size, int i, @ByRef @Const Scalar scalar);

        private native void allocate(@ByVal Size size, int i, @ByRef @Const Scalar scalar, @Cast({"cv::UMatUsageFlags"}) int i2);

        private native void allocate(@ByRef @Const UMat uMat);

        private native void allocate(@ByRef @Const UMat uMat, @ByRef @Const Range range);

        private native void allocate(@ByRef @Const UMat uMat, @ByRef @Const Range range, @ByRef(nullValue = "cv::Range::all()") @Const Range range2);

        private native void allocate(@ByRef @Const UMat uMat, @ByRef @Const Rect rect);

        private native void allocateArray(long j);

        @ByVal
        public static native UMat diag(@ByRef @Const UMat uMat);

        @ByVal
        public static native UMat eye(int i, int i2, int i3);

        @ByVal
        public static native UMat eye(@ByVal Size size, int i);

        public static native MatAllocator getStdAllocator();

        @ByVal
        public static native UMat ones(int i, int i2, int i3);

        @ByVal
        public static native UMat ones(@ByVal Size size, int i);

        @ByVal
        public static native UMat zeros(int i, int i2, int i3);

        @ByVal
        public static native UMat zeros(@ByVal Size size, int i);

        @Name({"deallocate"})
        public native void _deallocate();

        public native void addref();

        @ByRef
        public native UMat adjustROI(int i, int i2, int i3, int i4);

        public native MatAllocator allocator();

        public native UMat allocator(MatAllocator matAllocator);

        @ByVal
        @Name({"operator ()"})
        public native UMat apply(@Const Range range);

        @ByVal
        @Name({"operator ()"})
        public native UMat apply(@ByVal Range range, @ByVal Range range2);

        @ByVal
        @Name({"operator ()"})
        public native UMat apply(@ByRef @Const Rect rect);

        public native void assignTo(@ByRef UMat uMat);

        public native void assignTo(@ByRef UMat uMat, int i);

        public native int channels();

        public native int checkVector(int i);

        public native int checkVector(int i, int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native UMat clone();

        @ByVal
        public native UMat col(int i);

        @ByVal
        public native UMat colRange(int i, int i2);

        @ByVal
        public native UMat colRange(@ByRef @Const Range range);

        public native int cols();

        public native UMat cols(int i);

        public native void convertTo(@ByVal Mat mat, int i);

        public native void convertTo(@ByVal Mat mat, int i, double d, double d2);

        public native void convertTo(@ByVal UMat uMat, int i);

        public native void convertTo(@ByVal UMat uMat, int i, double d, double d2);

        public native void copySize(@ByRef @Const UMat uMat);

        public native void copyTo(@ByVal Mat mat);

        public native void copyTo(@ByVal Mat mat, @ByVal Mat mat2);

        public native void copyTo(@ByVal UMat uMat);

        public native void copyTo(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void create(int i, int i2, int i3);

        public native void create(int i, int i2, int i3, @Cast({"cv::UMatUsageFlags"}) int i4);

        public native void create(int i, @Const IntBuffer intBuffer, int i2);

        public native void create(int i, @Const IntBuffer intBuffer, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        public native void create(int i, @Const IntPointer intPointer, int i2);

        public native void create(int i, @Const IntPointer intPointer, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        public native void create(int i, @Const int[] iArr, int i2);

        public native void create(int i, @Const int[] iArr, int i2, @Cast({"cv::UMatUsageFlags"}) int i3);

        public native void create(@ByVal Size size, int i);

        public native void create(@ByVal Size size, int i, @Cast({"cv::UMatUsageFlags"}) int i2);

        public native int depth();

        @ByVal
        public native UMat diag();

        @ByVal
        public native UMat diag(int i);

        public native int dims();

        public native UMat dims(int i);

        public native double dot(@ByVal Mat mat);

        public native double dot(@ByVal UMat uMat);

        @Cast({"size_t"})
        public native long elemSize();

        @Cast({"size_t"})
        public native long elemSize1();

        @Cast({"bool"})
        public native boolean empty();

        public native int flags();

        public native UMat flags(int i);

        @ByVal
        public native Mat getMat(int i);

        public native Pointer handle(int i);

        @ByVal
        public native UMat inv();

        @ByVal
        public native UMat inv(int i);

        @Cast({"bool"})
        public native boolean isContinuous();

        @Cast({"bool"})
        public native boolean isSubmatrix();

        public native void locateROI(@ByRef Size size, @ByRef Point point);

        @ByVal
        public native UMat mul(@ByVal Mat mat);

        @ByVal
        public native UMat mul(@ByVal Mat mat, double d);

        @ByVal
        public native UMat mul(@ByVal UMat uMat);

        @ByVal
        public native UMat mul(@ByVal UMat uMat, double d);

        public native void ndoffset(@Cast({"size_t*"}) SizeTPointer sizeTPointer);

        @Cast({"size_t"})
        public native long offset();

        public native UMat offset(long j);

        @ByRef
        @Name({"operator ="})
        public native UMat put(@ByRef @Const Scalar scalar);

        @ByRef
        @Name({"operator ="})
        public native UMat put(@ByRef @Const UMat uMat);

        public native void release();

        @ByVal
        public native UMat reshape(int i);

        @ByVal
        public native UMat reshape(int i, int i2);

        @ByVal
        public native UMat reshape(int i, int i2, @Const IntBuffer intBuffer);

        @ByVal
        public native UMat reshape(int i, int i2, @Const IntPointer intPointer);

        @ByVal
        public native UMat reshape(int i, int i2, @Const int[] iArr);

        @ByVal
        public native UMat row(int i);

        @ByVal
        public native UMat rowRange(int i, int i2);

        @ByVal
        public native UMat rowRange(@ByRef @Const Range range);

        public native int rows();

        public native UMat rows(int i);

        @ByRef
        public native UMat setTo(@ByVal Mat mat);

        @ByRef
        public native UMat setTo(@ByVal Mat mat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

        @ByRef
        public native UMat setTo(@ByVal UMat uMat);

        @ByRef
        public native UMat setTo(@ByVal UMat uMat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

        @MemberGetter
        public native int size(int i);

        @ByVal
        public native Size size();

        @MemberGetter
        public native int step(int i);

        @MemberGetter
        public native long step();

        @Cast({"size_t"})
        public native long step1();

        @Cast({"size_t"})
        public native long step1(int i);

        @ByVal
        public native UMat m157t();

        @Cast({"size_t"})
        public native long total();

        public native int type();

        public native UMat m158u(UMatData uMatData);

        public native UMatData m159u();

        @Cast({"cv::UMatUsageFlags"})
        public native int usageFlags();

        public native UMat usageFlags(int i);

        static {
            Loader.load();
        }

        public UMat(Pointer p) {
            super(p);
        }

        public UMat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public UMat position(long position) {
            return (UMat) super.position(position);
        }

        public UMat(@Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(usageFlags);
        }

        public UMat() {
            super((Pointer) null);
            allocate();
        }

        public UMat(int rows, int cols, int type, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(rows, cols, type, usageFlags);
        }

        public UMat(int rows, int cols, int type) {
            super((Pointer) null);
            allocate(rows, cols, type);
        }

        public UMat(@ByVal Size size, int type, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(size, type, usageFlags);
        }

        public UMat(@ByVal Size size, int type) {
            super((Pointer) null);
            allocate(size, type);
        }

        public UMat(int rows, int cols, int type, @ByRef @Const Scalar s, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(rows, cols, type, s, usageFlags);
        }

        public UMat(int rows, int cols, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(rows, cols, type, s);
        }

        public UMat(@ByVal Size size, int type, @ByRef @Const Scalar s, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(size, type, s, usageFlags);
        }

        public UMat(@ByVal Size size, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(size, type, s);
        }

        public UMat(int ndims, @Const IntPointer sizes, int type, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, usageFlags);
        }

        public UMat(int ndims, @Const IntPointer sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public UMat(int ndims, @Const IntBuffer sizes, int type, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, usageFlags);
        }

        public UMat(int ndims, @Const IntBuffer sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public UMat(int ndims, @Const int[] sizes, int type, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, usageFlags);
        }

        public UMat(int ndims, @Const int[] sizes, int type) {
            super((Pointer) null);
            allocate(ndims, sizes, type);
        }

        public UMat(int ndims, @Const IntPointer sizes, int type, @ByRef @Const Scalar s, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s, usageFlags);
        }

        public UMat(int ndims, @Const IntPointer sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public UMat(int ndims, @Const IntBuffer sizes, int type, @ByRef @Const Scalar s, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s, usageFlags);
        }

        public UMat(int ndims, @Const IntBuffer sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public UMat(int ndims, @Const int[] sizes, int type, @ByRef @Const Scalar s, @Cast({"cv::UMatUsageFlags"}) int usageFlags) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s, usageFlags);
        }

        public UMat(int ndims, @Const int[] sizes, int type, @ByRef @Const Scalar s) {
            super((Pointer) null);
            allocate(ndims, sizes, type, s);
        }

        public UMat(@ByRef @Const UMat m) {
            super((Pointer) null);
            allocate(m);
        }

        public UMat(@ByRef @Const UMat m, @ByRef @Const Range rowRange, @ByRef(nullValue = "cv::Range::all()") @Const Range colRange) {
            super((Pointer) null);
            allocate(m, rowRange, colRange);
        }

        public UMat(@ByRef @Const UMat m, @ByRef @Const Range rowRange) {
            super((Pointer) null);
            allocate(m, rowRange);
        }

        public UMat(@ByRef @Const UMat m, @ByRef @Const Rect roi) {
            super((Pointer) null);
            allocate(m, roi);
        }
    }

    @Name({"std::vector<std::pair<cv::UMat,uchar> >"})
    public static class UMatBytePairVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native UMat first(@Cast({"size_t"}) long j);

        public native UMatBytePairVector first(@Cast({"size_t"}) long j, UMat uMat);

        @ByRef
        @Name({"operator="})
        public native UMatBytePairVector put(@ByRef UMatBytePairVector uMatBytePairVector);

        public native void resize(@Cast({"size_t"}) long j);

        @Index
        public native byte second(@Cast({"size_t"}) long j);

        public native UMatBytePairVector second(@Cast({"size_t"}) long j, byte b);

        public native long size();

        static {
            Loader.load();
        }

        public UMatBytePairVector(Pointer p) {
            super(p);
        }

        public UMatBytePairVector(UMat[] firstValue, byte[] secondValue) {
            this((long) Math.min(firstValue.length, secondValue.length));
            put(firstValue, secondValue);
        }

        public UMatBytePairVector() {
            allocate();
        }

        public UMatBytePairVector(long n) {
            allocate(n);
        }

        public UMatBytePairVector put(UMat[] firstValue, byte[] secondValue) {
            int i = 0;
            while (i < firstValue.length && i < secondValue.length) {
                first((long) i, firstValue[i]);
                second((long) i, secondValue[i]);
                i++;
            }
            return this;
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class UMatData extends Pointer {
        public static final int COPY_ON_MAP = 1;
        public static final int DEVICE_COPY_OBSOLETE = 4;
        public static final int DEVICE_MEM_MAPPED = 64;
        public static final int HOST_COPY_OBSOLETE = 2;
        public static final int TEMP_COPIED_UMAT = 24;
        public static final int TEMP_UMAT = 8;
        public static final int USER_ALLOCATED = 32;

        private native void allocate(@Const MatAllocator matAllocator);

        public native int allocatorFlags_();

        public native UMatData allocatorFlags_(int i);

        @Cast({"bool"})
        public native boolean copyOnMap();

        @MemberGetter
        @Const
        public native MatAllocator currAllocator();

        @Cast({"uchar*"})
        public native BytePointer data();

        public native UMatData data(BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean deviceCopyObsolete();

        @Cast({"bool"})
        public native boolean deviceMemMapped();

        public native int flags();

        public native UMatData flags(int i);

        public native Pointer handle();

        public native UMatData handle(Pointer pointer);

        @Cast({"bool"})
        public native boolean hostCopyObsolete();

        public native void lock();

        public native int mapcount();

        public native UMatData mapcount(int i);

        public native void markDeviceCopyObsolete(@Cast({"bool"}) boolean z);

        public native void markDeviceMemMapped(@Cast({"bool"}) boolean z);

        public native void markHostCopyObsolete(@Cast({"bool"}) boolean z);

        @Cast({"uchar*"})
        public native BytePointer origdata();

        public native UMatData origdata(BytePointer bytePointer);

        public native UMatData originalUMatData();

        public native UMatData originalUMatData(UMatData uMatData);

        @MemberGetter
        @Const
        public native MatAllocator prevAllocator();

        public native int refcount();

        public native UMatData refcount(int i);

        @Cast({"size_t"})
        public native long size();

        public native UMatData size(long j);

        @Cast({"bool"})
        public native boolean tempCopiedUMat();

        @Cast({"bool"})
        public native boolean tempUMat();

        public native void unlock();

        public native int urefcount();

        public native UMatData urefcount(int i);

        public native Pointer userdata();

        public native UMatData userdata(Pointer pointer);

        static {
            Loader.load();
        }

        public UMatData(Pointer p) {
            super(p);
        }

        public UMatData(@Const MatAllocator allocator) {
            super((Pointer) null);
            allocate(allocator);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class UMatDataAutoLock extends Pointer {
        private native void allocate(UMatData uMatData);

        public native UMatData m160u();

        public native UMatDataAutoLock m161u(UMatData uMatData);

        static {
            Loader.load();
        }

        public UMatDataAutoLock(Pointer p) {
            super(p);
        }

        public UMatDataAutoLock(UMatData u) {
            super((Pointer) null);
            allocate(u);
        }
    }

    @Name({"std::vector<cv::UMat>"})
    public static class UMatVector extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"size_t"}) long j);

        @ByRef
        @Index
        public native UMat get(@Cast({"size_t"}) long j);

        public native UMatVector put(@Cast({"size_t"}) long j, UMat uMat);

        @ByRef
        @Name({"operator="})
        public native UMatVector put(@ByRef UMatVector uMatVector);

        public native void resize(@Cast({"size_t"}) long j);

        public native long size();

        static {
            Loader.load();
        }

        public UMatVector(Pointer p) {
            super(p);
        }

        public UMatVector(UMat... array) {
            this((long) array.length);
            put(array);
        }

        public UMatVector() {
            allocate();
        }

        public UMatVector(long n) {
            allocate(n);
        }

        public UMatVector put(UMat... array) {
            if (size() != ((long) array.length)) {
                resize((long) array.length);
            }
            for (int i = 0; i < array.length; i++) {
                put((long) i, array[i]);
            }
            return this;
        }
    }

    @Namespace("cv::internal")
    @NoOffset
    public static class WriteStructContext extends Pointer {
        private native void allocate(@ByRef FileStorage fileStorage, @Str String str, int i);

        private native void allocate(@ByRef FileStorage fileStorage, @Str String str, int i, @Str String str2);

        private native void allocate(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, int i);

        private native void allocate(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, int i, @Str BytePointer bytePointer2);

        static {
            Loader.load();
        }

        public WriteStructContext(Pointer p) {
            super(p);
        }

        public WriteStructContext(@ByRef FileStorage _fs, @Str BytePointer name, int flags, @Str BytePointer typeName) {
            super((Pointer) null);
            allocate(_fs, name, flags, typeName);
        }

        public WriteStructContext(@ByRef FileStorage _fs, @Str BytePointer name, int flags) {
            super((Pointer) null);
            allocate(_fs, name, flags);
        }

        public WriteStructContext(@ByRef FileStorage _fs, @Str String name, int flags, @Str String typeName) {
            super((Pointer) null);
            allocate(_fs, name, flags, typeName);
        }

        public WriteStructContext(@ByRef FileStorage _fs, @Str String name, int flags) {
            super((Pointer) null);
            allocate(_fs, name, flags);
        }
    }

    public static native int CV_16SC(int i);

    public static native int CV_16UC(int i);

    public static native int CV_32FC(int i);

    public static native int CV_32SC(int i);

    public static native int CV_64FC(int i);

    public static native int CV_8SC(int i);

    public static native int CV_8UC(int i);

    public static native int CV_IS_CONT_MAT(int i);

    public static native int CV_IS_MAT_CONT(int i);

    public static native int CV_MAKETYPE(int i, int i2);

    public static native int CV_MAKE_TYPE(int i, int i2);

    public static native int CV_MAT_CN(int i);

    public static native int CV_MAT_DEPTH(int i);

    public static native int CV_MAT_TYPE(int i);

    @MemberGetter
    public static native int CV_SET_ELEM_FREE_FLAG();

    @MemberGetter
    public static native String CV_VERSION();

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, int i, DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(FloatBuffer floatBuffer, @Cast({"size_t"}) long j, int i, FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(DoublePointer doublePointer, @Cast({"size_t"}) long j, int i, DoublePointer doublePointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(FloatPointer floatPointer, @Cast({"size_t"}) long j, int i, FloatPointer floatPointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(double[] dArr, @Cast({"size_t"}) long j, int i, double[] dArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky(float[] fArr, @Cast({"size_t"}) long j, int i, float[] fArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky32f(FloatBuffer floatBuffer, @Cast({"size_t"}) long j, int i, FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky32f(FloatPointer floatPointer, @Cast({"size_t"}) long j, int i, FloatPointer floatPointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky32f(float[] fArr, @Cast({"size_t"}) long j, int i, float[] fArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky64f(DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, int i, DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky64f(DoublePointer doublePointer, @Cast({"size_t"}) long j, int i, DoublePointer doublePointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    @Cast({"bool"})
    public static native boolean Cholesky64f(double[] dArr, @Cast({"size_t"}) long j, int i, double[] dArr2, @Cast({"size_t"}) long j2, int i2);

    @MemberGetter
    public static native int IPL_IMAGE_MAGIC_VAL();

    @Namespace("cv::hal")
    public static native int LU(DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, int i, DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU(FloatBuffer floatBuffer, @Cast({"size_t"}) long j, int i, FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU(DoublePointer doublePointer, @Cast({"size_t"}) long j, int i, DoublePointer doublePointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU(FloatPointer floatPointer, @Cast({"size_t"}) long j, int i, FloatPointer floatPointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU(double[] dArr, @Cast({"size_t"}) long j, int i, double[] dArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU(float[] fArr, @Cast({"size_t"}) long j, int i, float[] fArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU32f(FloatBuffer floatBuffer, @Cast({"size_t"}) long j, int i, FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU32f(FloatPointer floatPointer, @Cast({"size_t"}) long j, int i, FloatPointer floatPointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU32f(float[] fArr, @Cast({"size_t"}) long j, int i, float[] fArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU64f(DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, int i, DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU64f(DoublePointer doublePointer, @Cast({"size_t"}) long j, int i, DoublePointer doublePointer2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv::hal")
    public static native int LU64f(double[] dArr, @Cast({"size_t"}) long j, int i, double[] dArr2, @Cast({"size_t"}) long j2, int i2);

    @Namespace("cv")
    public static native void LUT(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void LUT(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native double Mahalanobis(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native double Mahalanobis(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void PCABackProject(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void PCABackProject(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void PCACompute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void PCACompute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d);

    @Namespace("cv")
    public static native void PCACompute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    @Namespace("cv")
    public static native void PCACompute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void PCACompute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d);

    @Namespace("cv")
    public static native void PCACompute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    @Namespace("cv")
    public static native void PCAProject(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void PCAProject(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native double PSNR(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native double PSNR(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void SVBackSubst(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv")
    public static native void SVBackSubst(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

    @Namespace("cv")
    public static native void SVDecomp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void SVDecomp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, int i);

    @Namespace("cv")
    public static native void SVDecomp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void SVDecomp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, int i);

    @Namespace("std")
    @Cast({"uchar"})
    public static native byte abs(@Cast({"uchar"}) byte b);

    @Namespace("std")
    @Cast({"unsigned"})
    public static native int abs(@Cast({"unsigned"}) int i);

    @Namespace("cv")
    @ByVal
    public static native MatExpr abs(@ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    public static native MatExpr abs(@ByRef @Const MatExpr matExpr);

    @Namespace("std")
    @Cast({"ushort"})
    public static native short abs(@Cast({"ushort"}) short s);

    @Namespace("cv")
    public static native void absdiff(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void absdiff(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv::hal")
    public static native void absdiff16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void absdiff8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native String add(@Cast({"char"}) byte b, @Str String str);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native String add(@Str String str, @Cast({"char"}) byte b);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native String add(@Str String str, @Str String str2);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native BytePointer add(@Cast({"char"}) byte b, @Str BytePointer bytePointer);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native BytePointer add(@Str BytePointer bytePointer, @Cast({"char"}) byte b);

    @Namespace("cv")
    @Str
    @Name({"operator +"})
    public static native BytePointer add(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const Mat mat, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const MatExpr matExpr, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const MatExpr matExpr, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const Scalar scalar, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native MatExpr add(@ByRef @Const Scalar scalar, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native Range add(int i, @ByRef @Const Range range);

    @Namespace("cv")
    @ByVal
    @Name({"operator +"})
    public static native Range add(@ByRef @Const Range range, int i);

    @Namespace("cv")
    public static native void add(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void add(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4, int i);

    @Namespace("cv")
    public static native void add(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void add(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4, int i);

    @Namespace("cv::hal")
    public static native void add16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void add8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByRef
    @Name({"operator +="})
    public static native Mat addPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByRef
    @Name({"operator +="})
    public static native Mat addPut(@ByRef Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    public static native void addWeighted(@ByVal Mat mat, double d, @ByVal Mat mat2, double d2, double d3, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void addWeighted(@ByVal Mat mat, double d, @ByVal Mat mat2, double d2, double d3, @ByVal Mat mat3, int i);

    @Namespace("cv")
    public static native void addWeighted(@ByVal UMat uMat, double d, @ByVal UMat uMat2, double d2, double d3, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void addWeighted(@ByVal UMat uMat, double d, @ByVal UMat uMat2, double d2, double d3, @ByVal UMat uMat3, int i);

    @Namespace("cv::hal")
    public static native void addWeighted16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void addWeighted8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @Cast({"size_t"})
    public static native long alignSize(@Cast({"size_t"}) long j, int i);

    @Namespace("cv")
    @ByVal
    @Name({"operator &"})
    public static native MatExpr and(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator &"})
    public static native MatExpr and(@ByRef @Const Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator &"})
    public static native MatExpr and(@ByRef @Const Scalar scalar, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator &"})
    public static native Range and(@ByRef @Const Range range, @ByRef @Const Range range2);

    @Namespace("cv::hal")
    public static native void and8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void and8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void and8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByRef
    @Name({"operator &="})
    public static native Mat andPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByRef
    @Name({"operator &="})
    public static native Mat andPut(@ByRef Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByRef
    @Name({"operator &="})
    public static native Range andPut(@ByRef Range range, @ByRef @Const Range range2);

    @Namespace("cv")
    public static native void batchDistance(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void batchDistance(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, @ByVal Mat mat4, int i2, int i3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat5, int i4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void batchDistance(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void batchDistance(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, @ByVal UMat uMat4, int i2, int i3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat5, int i4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void bitwise_and(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void bitwise_and(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    public static native void bitwise_and(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void bitwise_and(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    public static native void bitwise_not(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void bitwise_not(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    public static native void bitwise_not(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void bitwise_not(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    public static native void bitwise_or(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void bitwise_or(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    public static native void bitwise_or(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void bitwise_or(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    public static native void bitwise_xor(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void bitwise_xor(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    public static native void bitwise_xor(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void bitwise_xor(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    public static native int borderInterpolate(int i, int i2, int i3);

    @Namespace("cv")
    public static native void calcCovarMatrix(@Const Mat mat, int i, @ByRef Mat mat2, @ByRef Mat mat3, int i2);

    @Namespace("cv")
    public static native void calcCovarMatrix(@Const Mat mat, int i, @ByRef Mat mat2, @ByRef Mat mat3, int i2, int i3);

    @Namespace("cv")
    public static native void calcCovarMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    @Namespace("cv")
    public static native void calcCovarMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2);

    @Namespace("cv")
    public static native void calcCovarMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    @Namespace("cv")
    public static native void calcCovarMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2);

    @Namespace("cv")
    public static native void cartToPolar(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void cartToPolar(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void cartToPolar(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void cartToPolar(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean checkHardwareSupport(int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean checkRange(@ByVal Mat mat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean checkRange(@ByVal Mat mat, @Cast({"bool"}) boolean z, Point point, double d, double d2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean checkRange(@ByVal UMat uMat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean checkRange(@ByVal UMat uMat, @Cast({"bool"}) boolean z, Point point, double d, double d2);

    @Namespace("cv")
    public static native void clearSeq(CvSeq cvSeq);

    @Namespace("cv::hal")
    public static native void cmp16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void cmp8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    public static native void compare(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    @Namespace("cv")
    public static native void compare(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    @Namespace("cv")
    public static native void completeSymm(@ByVal Mat mat);

    @Namespace("cv")
    public static native void completeSymm(@ByVal Mat mat, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void completeSymm(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void completeSymm(@ByVal UMat uMat, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void convertScaleAbs(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void convertScaleAbs(@ByVal Mat mat, @ByVal Mat mat2, double d, double d2);

    @Namespace("cv")
    public static native void convertScaleAbs(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void convertScaleAbs(@ByVal UMat uMat, @ByVal UMat uMat2, double d, double d2);

    @Namespace("cv")
    public static native void copyMakeBorder(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2, int i3, int i4, int i5);

    @Namespace("cv")
    public static native void copyMakeBorder(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2, int i3, int i4, int i5, @ByRef(nullValue = "cv::Scalar()") @Const Scalar scalar);

    @Namespace("cv")
    public static native void copyMakeBorder(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2, int i3, int i4, int i5);

    @Namespace("cv")
    public static native void copyMakeBorder(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2, int i3, int i4, int i5, @ByRef(nullValue = "cv::Scalar()") @Const Scalar scalar);

    @Namespace("cv")
    public static native int countNonZero(@ByVal Mat mat);

    @Namespace("cv")
    public static native int countNonZero(@ByVal UMat uMat);

    @Namespace("cv")
    public static native float cubeRoot(float f);

    public static native void cvAbsDiff(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvAbsDiffS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @ByVal CvScalar cvScalar);

    public static native void cvAdd(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvAdd(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvAddS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvAddS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvAddWeighted(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double d, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d2, double d3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native Pointer cvAlloc(@Cast({"size_t"}) long j);

    public static native void cvAnd(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvAnd(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvAndS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvAndS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    @ByVal
    public static native CvAttrList cvAttrList();

    @ByVal
    public static native CvAttrList cvAttrList(@ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer, CvAttrList cvAttrList);

    @ByVal
    public static native CvAttrList cvAttrList(@ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, CvAttrList cvAttrList);

    @ByVal
    public static native CvAttrList cvAttrList(@Cast({"const char**"}) PointerPointer pointerPointer, CvAttrList cvAttrList);

    @ByVal
    public static native CvAttrList cvAttrList(@ByPtrPtr @Cast({"const char**"}) byte[] bArr, CvAttrList cvAttrList);

    public static native String cvAttrValue(@Const CvAttrList cvAttrList, String str);

    @Cast({"const char*"})
    public static native BytePointer cvAttrValue(@Const CvAttrList cvAttrList, @Cast({"const char*"}) BytePointer bytePointer);

    @ByVal
    public static native CvScalar cvAvg(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @ByVal
    public static native CvScalar cvAvg(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvAvgSdv(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvScalar cvScalar, CvScalar cvScalar2);

    public static native void cvAvgSdv(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvScalar cvScalar, CvScalar cvScalar2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvBackProjectPCA(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvCalcCovarMatrix(@Cast({"const CvArr**"}) PointerPointer pointerPointer, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2);

    public static native void cvCalcCovarMatrix(@ByPtrPtr @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, int i2);

    public static native void cvCalcPCA(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    public static native void cvCartToPolar(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvCartToPolar(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    public static native float cvCbrt(float f);

    public static native int cvCeil(double d);

    public static native int cvCeil(float f);

    public static native int cvCeil(int i);

    public static native void cvChangeSeqBlock(Pointer pointer, int i);

    public static native int cvCheckArr(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvCheckArr(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, double d, double d2);

    public static native int cvCheckArray(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, double d, double d2);

    public static native int cvCheckHardwareSupport(int i);

    @ByVal
    public static native CvTermCriteria cvCheckTermCriteria(@ByVal CvTermCriteria cvTermCriteria, double d, int i);

    public static native void cvClearGraph(CvGraph cvGraph);

    public static native void cvClearMemStorage(CvMemStorage cvMemStorage);

    public static native void cvClearND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native void cvClearND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native void cvClearND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native void cvClearSeq(CvSeq cvSeq);

    public static native void cvClearSet(CvSet cvSet);

    public static native Pointer cvClone(@Const Pointer pointer);

    public static native CvGraph cvCloneGraph(@Const CvGraph cvGraph, CvMemStorage cvMemStorage);

    public static native IplImage cvCloneImage(@Const IplImage iplImage);

    public static native CvMat cvCloneMat(@Const CvMat cvMat);

    public static native CvMatND cvCloneMatND(@Const CvMatND cvMatND);

    public static native CvSeq cvCloneSeq(@Const CvSeq cvSeq);

    public static native CvSeq cvCloneSeq(@Const CvSeq cvSeq, CvMemStorage cvMemStorage);

    public static native CvSparseMat cvCloneSparseMat(@Const CvSparseMat cvSparseMat);

    public static native void cvCmp(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, int i);

    public static native void cvCmpS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double d, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native void cvCompleteSymm(CvMat cvMat);

    public static native void cvCompleteSymm(CvMat cvMat, int i);

    public static native void cvConvert(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvConvertScale(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvConvertScale(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2);

    public static native void cvConvertScaleAbs(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvConvertScaleAbs(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2);

    public static native void cvCopy(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvCopy(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native int cvCountNonZero(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native CvMemStorage cvCreateChildMemStorage(CvMemStorage cvMemStorage);

    public static native void cvCreateData(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native CvGraph cvCreateGraph(int i, int i2, int i3, int i4, CvMemStorage cvMemStorage);

    public static native CvGraphScanner cvCreateGraphScanner(CvGraph cvGraph);

    public static native CvGraphScanner cvCreateGraphScanner(CvGraph cvGraph, CvGraphVtx cvGraphVtx, int i);

    public static native IplImage cvCreateImage(@ByVal CvSize cvSize, int i, int i2);

    public static native IplImage cvCreateImageHeader(@ByVal CvSize cvSize, int i, int i2);

    public static native CvMat cvCreateMat(int i, int i2, int i3);

    public static native CvMat cvCreateMatHeader(int i, int i2, int i3);

    public static native CvMatND cvCreateMatND(int i, @Const IntBuffer intBuffer, int i2);

    public static native CvMatND cvCreateMatND(int i, @Const IntPointer intPointer, int i2);

    public static native CvMatND cvCreateMatND(int i, @Const int[] iArr, int i2);

    public static native CvMatND cvCreateMatNDHeader(int i, @Const IntBuffer intBuffer, int i2);

    public static native CvMatND cvCreateMatNDHeader(int i, @Const IntPointer intPointer, int i2);

    public static native CvMatND cvCreateMatNDHeader(int i, @Const int[] iArr, int i2);

    public static native CvMemStorage cvCreateMemStorage();

    public static native CvMemStorage cvCreateMemStorage(int i);

    public static native CvSeq cvCreateSeq(int i, @Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, CvMemStorage cvMemStorage);

    public static native void cvCreateSeqBlock(CvSeqWriter cvSeqWriter);

    public static native CvSet cvCreateSet(int i, int i2, int i3, CvMemStorage cvMemStorage);

    public static native CvSparseMat cvCreateSparseMat(int i, @Const IntBuffer intBuffer, int i2);

    public static native CvSparseMat cvCreateSparseMat(int i, @Const IntPointer intPointer, int i2);

    public static native CvSparseMat cvCreateSparseMat(int i, @Const int[] iArr, int i2);

    public static native CvSparseMat cvCreateSparseMat(@ByRef @Const SparseMat sparseMat);

    public static native void cvCrossProduct(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvCvtScale(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2);

    public static native void cvCvtScaleAbs(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2);

    public static native Pointer cvCvtSeqToArray(@Const CvSeq cvSeq, Pointer pointer);

    public static native Pointer cvCvtSeqToArray(@Const CvSeq cvSeq, Pointer pointer, @ByVal(nullValue = "CvSlice(CV_WHOLE_SEQ)") CvSlice cvSlice);

    public static native void cvDCT(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native void cvDFT(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native void cvDFT(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, int i2);

    public static native void cvDecRefData(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native double cvDet(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvDiv(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvDiv(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d);

    public static native double cvDotProduct(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvEigenVV(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvEigenVV(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d, int i, int i2);

    public static native CvSeq cvEndWriteSeq(CvSeqWriter cvSeqWriter);

    public static native void cvEndWriteStruct(CvFileStorage cvFileStorage);

    public static native void cvError(int i, String str, String str2, String str3, int i2);

    public static native void cvError(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2);

    public static native int cvErrorFromIppStatus(int i);

    @Cast({"const char*"})
    public static native BytePointer cvErrorStr(int i);

    public static native void cvExp(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvFFT(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, int i2);

    public static native float cvFastArctan(float f, float f2);

    public static native CvGraphEdge cvFindGraphEdge(@Const CvGraph cvGraph, int i, int i2);

    public static native CvGraphEdge cvFindGraphEdgeByPtr(@Const CvGraph cvGraph, @Const CvGraphVtx cvGraphVtx, @Const CvGraphVtx cvGraphVtx2);

    public static native CvTypeInfo cvFindType(String str);

    public static native CvTypeInfo cvFindType(@Cast({"const char*"}) BytePointer bytePointer);

    public static native CvTypeInfo cvFirstType();

    public static native void cvFlip(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvFlip(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native int cvFloor(double d);

    public static native int cvFloor(float f);

    public static native int cvFloor(int i);

    public static native void cvFlushSeqWriter(CvSeqWriter cvSeqWriter);

    public static native void cvFree_(Pointer pointer);

    public static native void cvGEMM(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvGEMM(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    @ByVal
    public static native CvScalar cvGet1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    @ByVal
    public static native CvScalar cvGet2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2);

    @ByVal
    public static native CvScalar cvGet3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3);

    public static native CvMat cvGetCol(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i);

    public static native CvMat cvGetCols(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i, int i2);

    public static native CvMat cvGetDiag(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat);

    public static native CvMat cvGetDiag(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i);

    public static native int cvGetDimSize(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    public static native int cvGetDims(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvGetDims(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntBuffer intBuffer);

    public static native int cvGetDims(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntPointer intPointer);

    public static native int cvGetDims(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int[] iArr);

    public static native int cvGetElemType(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvGetErrInfo(@ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer2, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer3, IntBuffer intBuffer);

    public static native int cvGetErrInfo(@ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer3, IntPointer intPointer);

    public static native int cvGetErrInfo(@Cast({"const char**"}) PointerPointer pointerPointer, @Cast({"const char**"}) PointerPointer pointerPointer2, @Cast({"const char**"}) PointerPointer pointerPointer3, IntPointer intPointer);

    public static native int cvGetErrInfo(@ByPtrPtr @Cast({"const char**"}) byte[] bArr, @ByPtrPtr @Cast({"const char**"}) byte[] bArr2, @ByPtrPtr @Cast({"const char**"}) byte[] bArr3, int[] iArr);

    public static native int cvGetErrMode();

    public static native int cvGetErrStatus();

    public static native CvFileNode cvGetFileNode(CvFileStorage cvFileStorage, CvFileNode cvFileNode, @Const CvStringHashNode cvStringHashNode);

    public static native CvFileNode cvGetFileNode(CvFileStorage cvFileStorage, CvFileNode cvFileNode, @Const CvStringHashNode cvStringHashNode, int i);

    public static native CvFileNode cvGetFileNodeByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str);

    public static native CvFileNode cvGetFileNodeByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"const char*"})
    public static native BytePointer cvGetFileNodeName(@Const CvFileNode cvFileNode);

    public static native CvStringHashNode cvGetHashedKey(CvFileStorage cvFileStorage, String str);

    public static native CvStringHashNode cvGetHashedKey(CvFileStorage cvFileStorage, String str, int i, int i2);

    public static native CvStringHashNode cvGetHashedKey(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer);

    public static native CvStringHashNode cvGetHashedKey(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, int i, int i2);

    public static native IplImage cvGetImage(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IplImage iplImage);

    public static native int cvGetImageCOI(@Const IplImage iplImage);

    @ByVal
    public static native CvRect cvGetImageROI(@Const IplImage iplImage);

    public static native CvMat cvGetMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat);

    public static native CvMat cvGetMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, IntBuffer intBuffer, int i);

    public static native CvMat cvGetMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, IntPointer intPointer, int i);

    public static native CvMat cvGetMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int[] iArr, int i);

    @ByVal
    public static native CvScalar cvGetND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    @ByVal
    public static native CvScalar cvGetND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    @ByVal
    public static native CvScalar cvGetND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native CvSparseNode cvGetNextSparseNode(CvSparseMatIterator cvSparseMatIterator);

    public static native int cvGetNumThreads();

    public static native int cvGetOptimalDFTSize(int i);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer, IntBuffer intBuffer, CvSize cvSize);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer, IntPointer intPointer, CvSize cvSize);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"uchar**"}) PointerPointer pointerPointer, IntPointer intPointer, CvSize cvSize);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr);

    public static native void cvGetRawData(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr, int[] iArr, CvSize cvSize);

    public static native double cvGetReal1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    public static native double cvGetReal2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2);

    public static native double cvGetReal3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3);

    public static native double cvGetRealND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native double cvGetRealND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native double cvGetRealND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native CvFileNode cvGetRootFileNode(@Const CvFileStorage cvFileStorage);

    public static native CvFileNode cvGetRootFileNode(@Const CvFileStorage cvFileStorage, int i);

    public static native CvMat cvGetRow(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i);

    public static native CvMat cvGetRows(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i, int i2);

    public static native CvMat cvGetRows(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i, int i2, int i3);

    @Cast({"schar*"})
    public static native BytePointer cvGetSeqElem(@Const CvSeq cvSeq, int i);

    public static native int cvGetSeqReaderPos(CvSeqReader cvSeqReader);

    public static native CvSetElem cvGetSetElem(@Const CvSet cvSet, int i);

    @ByVal
    public static native CvSize cvGetSize(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native CvMat cvGetSubArr(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, @ByVal CvRect cvRect);

    public static native CvMat cvGetSubRect(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, @ByVal CvRect cvRect);

    public static native int cvGetThreadNum();

    @Cast({"int64"})
    public static native long cvGetTickCount();

    public static native double cvGetTickFrequency();

    public static native int cvGraphAddEdge(CvGraph cvGraph, int i, int i2);

    public static native int cvGraphAddEdge(CvGraph cvGraph, int i, int i2, @Const CvGraphEdge cvGraphEdge, @Cast({"CvGraphEdge**"}) PointerPointer pointerPointer);

    public static native int cvGraphAddEdge(CvGraph cvGraph, int i, int i2, @Const CvGraphEdge cvGraphEdge, @ByPtrPtr CvGraphEdge cvGraphEdge2);

    public static native int cvGraphAddEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2);

    public static native int cvGraphAddEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2, @Const CvGraphEdge cvGraphEdge, @Cast({"CvGraphEdge**"}) PointerPointer pointerPointer);

    public static native int cvGraphAddEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2, @Const CvGraphEdge cvGraphEdge, @ByPtrPtr CvGraphEdge cvGraphEdge2);

    public static native int cvGraphAddVtx(CvGraph cvGraph);

    public static native int cvGraphAddVtx(CvGraph cvGraph, @Const CvGraphVtx cvGraphVtx, @Cast({"CvGraphVtx**"}) PointerPointer pointerPointer);

    public static native int cvGraphAddVtx(CvGraph cvGraph, @Const CvGraphVtx cvGraphVtx, @ByPtrPtr CvGraphVtx cvGraphVtx2);

    public static native CvGraphEdge cvGraphFindEdge(CvGraph cvGraph, int i, int i2);

    public static native CvGraphEdge cvGraphFindEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2);

    public static native void cvGraphRemoveEdge(CvGraph cvGraph, int i, int i2);

    public static native void cvGraphRemoveEdgeByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx, CvGraphVtx cvGraphVtx2);

    public static native int cvGraphRemoveVtx(CvGraph cvGraph, int i);

    public static native int cvGraphRemoveVtxByPtr(CvGraph cvGraph, CvGraphVtx cvGraphVtx);

    public static native int cvGraphVtxDegree(@Const CvGraph cvGraph, int i);

    public static native int cvGraphVtxDegreeByPtr(@Const CvGraph cvGraph, @Const CvGraphVtx cvGraphVtx);

    public static native int cvGuiBoxReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native int cvGuiBoxReport(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2, Pointer pointer);

    public static native void cvInRange(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvInRangeS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native int cvIncRefData(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native IplImage cvInitImageHeader(IplImage iplImage, @ByVal CvSize cvSize, int i, int i2);

    public static native IplImage cvInitImageHeader(IplImage iplImage, @ByVal CvSize cvSize, int i, int i2, int i3, int i4);

    public static native CvMat cvInitMatHeader(CvMat cvMat, int i, int i2, int i3);

    public static native CvMat cvInitMatHeader(CvMat cvMat, int i, int i2, int i3, Pointer pointer, int i4);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const IntBuffer intBuffer, int i2);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const IntBuffer intBuffer, int i2, Pointer pointer);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const IntPointer intPointer, int i2);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const IntPointer intPointer, int i2, Pointer pointer);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const int[] iArr, int i2);

    public static native CvMatND cvInitMatNDHeader(CvMatND cvMatND, int i, @Const int[] iArr, int i2, Pointer pointer);

    public static native int cvInitNArrayIterator(int i, @Cast({"CvArr**"}) PointerPointer pointerPointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMatND cvMatND, CvNArrayIterator cvNArrayIterator, int i2);

    public static native int cvInitNArrayIterator(int i, @ByPtrPtr opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, CvMatND cvMatND, CvNArrayIterator cvNArrayIterator);

    public static native int cvInitNArrayIterator(int i, @ByPtrPtr opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, CvMatND cvMatND, CvNArrayIterator cvNArrayIterator, int i2);

    public static native CvSparseNode cvInitSparseMatIterator(@Const CvSparseMat cvSparseMat, CvSparseMatIterator cvSparseMatIterator);

    public static native void cvInitTreeNodeIterator(CvTreeNodeIterator cvTreeNodeIterator, @Const Pointer pointer, int i);

    public static native void cvInsertNodeIntoTree(Pointer pointer, Pointer pointer2, Pointer pointer3);

    public static native void cvInv(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native double cvInvert(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native double cvInvert(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native int cvIplDepth(int i);

    public static native int cvIsInf(double d);

    public static native int cvIsInf(float f);

    public static native int cvIsNaN(double d);

    public static native int cvIsNaN(float f);

    public static native int cvKMeans2(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @ByVal CvTermCriteria cvTermCriteria);

    public static native int cvKMeans2(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @ByVal CvTermCriteria cvTermCriteria, int i2, @Cast({"CvRNG*"}) LongBuffer longBuffer, int i3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, DoubleBuffer doubleBuffer);

    public static native int cvKMeans2(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @ByVal CvTermCriteria cvTermCriteria, int i2, @Cast({"CvRNG*"}) LongPointer longPointer, int i3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, DoublePointer doublePointer);

    public static native int cvKMeans2(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @ByVal CvTermCriteria cvTermCriteria, int i2, @Cast({"CvRNG*"}) long[] jArr, int i3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double[] dArr);

    public static native void cvLUT(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native Pointer cvLoad(String str);

    public static native Pointer cvLoad(String str, CvMemStorage cvMemStorage, String str2, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer);

    public static native Pointer cvLoad(String str, CvMemStorage cvMemStorage, String str2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer);

    public static native Pointer cvLoad(String str, CvMemStorage cvMemStorage, String str2, @ByPtrPtr @Cast({"const char**"}) byte[] bArr);

    public static native Pointer cvLoad(@Cast({"const char*"}) BytePointer bytePointer);

    public static native Pointer cvLoad(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer2, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer);

    public static native Pointer cvLoad(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer3);

    public static native Pointer cvLoad(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char**"}) PointerPointer pointerPointer);

    public static native Pointer cvLoad(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer2, @ByPtrPtr @Cast({"const char**"}) byte[] bArr);

    public static native void cvLog(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native double cvMahalanobis(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native double cvMahalonobis(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native CvSeq cvMakeSeqHeaderForArray(int i, int i2, int i3, Pointer pointer, int i4, CvSeq cvSeq, CvSeqBlock cvSeqBlock);

    @ByVal
    public static native CvMat cvMat(int i, int i2, int i3);

    @ByVal
    public static native CvMat cvMat(int i, int i2, int i3, Pointer pointer);

    public static native void cvMatMul(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvMatMulAdd(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvMatMulAddEx(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    public static native void cvMatMulAddS(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, CvMat cvMat, CvMat cvMat2);

    public static native void cvMax(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvMaxS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double d, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native Pointer cvMemStorageAlloc(CvMemStorage cvMemStorage, @Cast({"size_t"}) long j);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, String str);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, String str, int i);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer);

    @ByVal
    public static native CvString cvMemStorageAllocString(CvMemStorage cvMemStorage, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvMerge(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr5);

    public static native void cvMin(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, @Cast({"CvPoint*"}) IntBuffer intBuffer, @Cast({"CvPoint*"}) IntBuffer intBuffer2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, DoublePointer doublePointer, DoublePointer doublePointer2);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, DoublePointer doublePointer, DoublePointer doublePointer2, CvPoint cvPoint, CvPoint cvPoint2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double[] dArr, double[] dArr2);

    public static native void cvMinMaxLoc(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double[] dArr, double[] dArr2, @Cast({"CvPoint*"}) int[] iArr, @Cast({"CvPoint*"}) int[] iArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvMinS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double d, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvMirror(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native void cvMixChannels(@Cast({"const CvArr**"}) PointerPointer pointerPointer, int i, @Cast({"CvArr**"}) PointerPointer pointerPointer2, int i2, @Const IntPointer intPointer, int i3);

    public static native void cvMixChannels(@ByPtrPtr @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByPtrPtr opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, @Const IntBuffer intBuffer, int i3);

    public static native void cvMixChannels(@ByPtrPtr @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByPtrPtr opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, @Const IntPointer intPointer, int i3);

    public static native void cvMixChannels(@ByPtrPtr @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByPtrPtr opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, @Const int[] iArr, int i3);

    public static native void cvMul(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvMul(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d);

    public static native void cvMulSpectrums(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, int i);

    public static native void cvMulTransposed(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native void cvMulTransposed(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d);

    public static native int cvNextGraphItem(CvGraphScanner cvGraphScanner);

    public static native int cvNextNArraySlice(CvNArrayIterator cvNArrayIterator);

    public static native Pointer cvNextTreeNode(CvTreeNodeIterator cvTreeNodeIterator);

    public static native double cvNorm(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native double cvNorm(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvNormalize(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvNormalize(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2, int i, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvNot(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native int cvNulDevReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native int cvNulDevReport(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2, Pointer pointer);

    public static native CvFileStorage cvOpenFileStorage(String str, CvMemStorage cvMemStorage, int i);

    public static native CvFileStorage cvOpenFileStorage(String str, CvMemStorage cvMemStorage, int i, String str2);

    public static native CvFileStorage cvOpenFileStorage(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, int i);

    public static native CvFileStorage cvOpenFileStorage(@Cast({"const char*"}) BytePointer bytePointer, CvMemStorage cvMemStorage, int i, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native void cvOr(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvOr(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvOrS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvOrS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvPerspectiveTransform(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const CvMat cvMat);

    @ByVal
    public static native CvPoint cvPoint(int i, int i2);

    @ByVal
    public static native CvPoint2D32f cvPoint2D32f(double d, double d2);

    @ByVal
    public static native CvPoint2D64f cvPoint2D64f(double d, double d2);

    @ByVal
    public static native CvPoint3D32f cvPoint3D32f(double d, double d2, double d3);

    @ByVal
    public static native CvPoint3D64f cvPoint3D64f(double d, double d2, double d3);

    @Cast({"CvPoint*"})
    @ByVal
    public static native IntBuffer cvPointFrom32f(@Cast({"CvPoint2D32f*"}) @ByVal FloatBuffer floatBuffer);

    @ByVal
    public static native CvPoint cvPointFrom32f(@ByVal CvPoint2D32f cvPoint2D32f);

    @Cast({"CvPoint*"})
    @ByVal
    public static native int[] cvPointFrom32f(@Cast({"CvPoint2D32f*"}) @ByVal float[] fArr);

    @Cast({"CvPoint2D32f*"})
    @ByVal
    public static native FloatBuffer cvPointTo32f(@Cast({"CvPoint*"}) @ByVal IntBuffer intBuffer);

    @ByVal
    public static native CvPoint2D32f cvPointTo32f(@ByVal CvPoint cvPoint);

    @Cast({"CvPoint2D32f*"})
    @ByVal
    public static native float[] cvPointTo32f(@Cast({"CvPoint*"}) @ByVal int[] iArr);

    public static native void cvPolarToCart(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvPolarToCart(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    public static native void cvPow(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d);

    public static native Pointer cvPrevTreeNode(CvTreeNodeIterator cvTreeNodeIterator);

    public static native void cvProjectPCA(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    @Cast({"uchar*"})
    public static native ByteBuffer cvPtr1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, IntBuffer intBuffer);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, IntPointer intPointer);

    @Cast({"uchar*"})
    public static native byte[] cvPtr1D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int[] iArr);

    @Cast({"uchar*"})
    public static native ByteBuffer cvPtr2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, IntBuffer intBuffer);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, IntPointer intPointer);

    @Cast({"uchar*"})
    public static native byte[] cvPtr2D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int[] iArr);

    @Cast({"uchar*"})
    public static native ByteBuffer cvPtr3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3, IntBuffer intBuffer);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3);

    @Cast({"uchar*"})
    public static native BytePointer cvPtr3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3, IntPointer intPointer);

    @Cast({"uchar*"})
    public static native byte[] cvPtr3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3, int[] iArr);

    @Cast({"uchar*"})
    public static native ByteBuffer cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    @Cast({"uchar*"})
    public static native ByteBuffer cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer, IntBuffer intBuffer2, int i, @Cast({"unsigned*"}) IntBuffer intBuffer3);

    @Cast({"uchar*"})
    public static native BytePointer cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    @Cast({"uchar*"})
    public static native BytePointer cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer, IntPointer intPointer2, int i, @Cast({"unsigned*"}) IntPointer intPointer3);

    @Cast({"uchar*"})
    public static native byte[] cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    @Cast({"uchar*"})
    public static native byte[] cvPtrND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr, int[] iArr2, int i, @Cast({"unsigned*"}) int[] iArr3);

    @Cast({"CvRNG"})
    public static native long cvRNG();

    @Cast({"CvRNG"})
    public static native long cvRNG(@Cast({"int64"}) long j);

    @ByVal
    public static native CvRect cvROIToRect(@ByVal IplROI iplROI);

    public static native void cvRandArr(@Cast({"CvRNG*"}) LongBuffer longBuffer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2);

    public static native void cvRandArr(@Cast({"CvRNG*"}) LongPointer longPointer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2);

    public static native void cvRandArr(@Cast({"CvRNG*"}) long[] jArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByVal CvScalar cvScalar, @ByVal CvScalar cvScalar2);

    @Cast({"unsigned"})
    public static native int cvRandInt(@Cast({"CvRNG*"}) LongBuffer longBuffer);

    @Cast({"unsigned"})
    public static native int cvRandInt(@Cast({"CvRNG*"}) LongPointer longPointer);

    @Cast({"unsigned"})
    public static native int cvRandInt(@Cast({"CvRNG*"}) long[] jArr);

    public static native double cvRandReal(@Cast({"CvRNG*"}) LongBuffer longBuffer);

    public static native double cvRandReal(@Cast({"CvRNG*"}) LongPointer longPointer);

    public static native double cvRandReal(@Cast({"CvRNG*"}) long[] jArr);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) LongBuffer longBuffer);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) LongBuffer longBuffer, double d);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) LongPointer longPointer);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) LongPointer longPointer, double d);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) long[] jArr);

    public static native void cvRandShuffle(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"CvRNG*"}) long[] jArr, double d);

    public static native opencv_core$CvArr cvRange(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, double d, double d2);

    public static native void cvRawDataToScalar(@Const Pointer pointer, int i, CvScalar cvScalar);

    public static native Pointer cvRead(CvFileStorage cvFileStorage, CvFileNode cvFileNode);

    public static native Pointer cvRead(CvFileStorage cvFileStorage, CvFileNode cvFileNode, CvAttrList cvAttrList);

    public static native Pointer cvReadByName(CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str);

    public static native Pointer cvReadByName(CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str, CvAttrList cvAttrList);

    public static native Pointer cvReadByName(CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    public static native Pointer cvReadByName(CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer, CvAttrList cvAttrList);

    public static native int cvReadInt(@Const CvFileNode cvFileNode);

    public static native int cvReadInt(@Const CvFileNode cvFileNode, int i);

    public static native int cvReadIntByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str);

    public static native int cvReadIntByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str, int i);

    public static native int cvReadIntByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvReadIntByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvReadRawData(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, Pointer pointer, String str);

    public static native void cvReadRawData(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, Pointer pointer, @Cast({"const char*"}) BytePointer bytePointer);

    public static native void cvReadRawDataSlice(@Const CvFileStorage cvFileStorage, CvSeqReader cvSeqReader, int i, Pointer pointer, String str);

    public static native void cvReadRawDataSlice(@Const CvFileStorage cvFileStorage, CvSeqReader cvSeqReader, int i, Pointer pointer, @Cast({"const char*"}) BytePointer bytePointer);

    public static native double cvReadReal(@Const CvFileNode cvFileNode);

    public static native double cvReadReal(@Const CvFileNode cvFileNode, double d);

    public static native double cvReadRealByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str);

    public static native double cvReadRealByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str, double d);

    public static native double cvReadRealByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    public static native double cvReadRealByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer, double d);

    public static native String cvReadString(@Const CvFileNode cvFileNode, String str);

    @Cast({"const char*"})
    public static native BytePointer cvReadString(@Const CvFileNode cvFileNode);

    @Cast({"const char*"})
    public static native BytePointer cvReadString(@Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    public static native String cvReadStringByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str);

    public static native String cvReadStringByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, String str, String str2);

    @Cast({"const char*"})
    public static native BytePointer cvReadStringByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"const char*"})
    public static native BytePointer cvReadStringByName(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @ByVal
    public static native CvScalar cvRealScalar(double d);

    @ByVal
    public static native CvRect cvRect(int i, int i2, int i3, int i4);

    @ByVal
    public static native IplROI cvRectToROI(@ByVal CvRect cvRect, int i);

    public static native CvErrorCallback cvRedirectError(CvErrorCallback cvErrorCallback);

    public static native CvErrorCallback cvRedirectError(CvErrorCallback cvErrorCallback, Pointer pointer, @ByPtrPtr @Cast({"void**"}) Pointer pointer2);

    public static native CvErrorCallback cvRedirectError(CvErrorCallback cvErrorCallback, Pointer pointer, @Cast({"void**"}) PointerPointer pointerPointer);

    public static native void cvReduce(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvReduce(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, int i2);

    public static native void cvRegisterType(@Const CvTypeInfo cvTypeInfo);

    public static native void cvRelease(@ByPtrPtr @Cast({"void**"}) Pointer pointer);

    public static native void cvRelease(@Cast({"void**"}) PointerPointer pointerPointer);

    public static native void cvReleaseData(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvReleaseFileStorage(@Cast({"CvFileStorage**"}) PointerPointer pointerPointer);

    public static native void cvReleaseFileStorage(@ByPtrPtr CvFileStorage cvFileStorage);

    public static native void cvReleaseGraphScanner(@Cast({"CvGraphScanner**"}) PointerPointer pointerPointer);

    public static native void cvReleaseGraphScanner(@ByPtrPtr CvGraphScanner cvGraphScanner);

    public static native void cvReleaseImage(@Cast({"IplImage**"}) PointerPointer pointerPointer);

    public static native void cvReleaseImage(@ByPtrPtr IplImage iplImage);

    public static native void cvReleaseImageHeader(@Cast({"IplImage**"}) PointerPointer pointerPointer);

    public static native void cvReleaseImageHeader(@ByPtrPtr IplImage iplImage);

    public static native void cvReleaseMat(@Cast({"CvMat**"}) PointerPointer pointerPointer);

    public static native void cvReleaseMat(@ByPtrPtr CvMat cvMat);

    public static native void cvReleaseMatND(@Cast({"CvMatND**"}) PointerPointer pointerPointer);

    public static native void cvReleaseMatND(@ByPtrPtr CvMatND cvMatND);

    public static native void cvReleaseMemStorage(@Cast({"CvMemStorage**"}) PointerPointer pointerPointer);

    public static native void cvReleaseMemStorage(@ByPtrPtr CvMemStorage cvMemStorage);

    public static native void cvReleaseSparseMat(@Cast({"CvSparseMat**"}) PointerPointer pointerPointer);

    public static native void cvReleaseSparseMat(@ByPtrPtr CvSparseMat cvSparseMat);

    public static native void cvRemoveNodeFromTree(Pointer pointer, Pointer pointer2);

    public static native void cvRepeat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvResetImageROI(IplImage iplImage);

    public static native CvMat cvReshape(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i);

    public static native CvMat cvReshape(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvMat cvMat, int i, int i2);

    public static native opencv_core$CvArr cvReshapeMatND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, int i3, IntBuffer intBuffer);

    public static native opencv_core$CvArr cvReshapeMatND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, int i3, IntPointer intPointer);

    public static native opencv_core$CvArr cvReshapeMatND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i2, int i3, int[] iArr);

    public static native void cvRestoreMemStoragePos(CvMemStorage cvMemStorage, CvMemStoragePos cvMemStoragePos);

    public static native int cvRound(double d);

    public static native int cvRound(float f);

    public static native int cvRound(int i);

    public static native void cvSVBkSb(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr5, int i);

    public static native void cvSVD(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvSVD(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, int i);

    public static native void cvSave(String str, @Const Pointer pointer);

    public static native void cvSave(String str, @Const Pointer pointer, String str2, String str3, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native void cvSave(@Cast({"const char*"}) BytePointer bytePointer, @Const Pointer pointer);

    public static native void cvSave(@Cast({"const char*"}) BytePointer bytePointer, @Const Pointer pointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native void cvSaveMemStoragePos(@Const CvMemStorage cvMemStorage, CvMemStoragePos cvMemStoragePos);

    @ByVal
    public static native CvScalar cvScalar(double d);

    @ByVal
    public static native CvScalar cvScalar(double d, double d2, double d3, double d4);

    @ByVal
    public static native CvScalar cvScalarAll(double d);

    public static native void cvScalarToRawData(@Const CvScalar cvScalar, Pointer pointer, int i);

    public static native void cvScalarToRawData(@Const CvScalar cvScalar, Pointer pointer, int i, int i2);

    public static native void cvScale(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, double d, double d2);

    public static native void cvScaleAdd(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native int cvSeqElemIdx(@Const CvSeq cvSeq, @Const Pointer pointer);

    public static native int cvSeqElemIdx(@Const CvSeq cvSeq, @Const Pointer pointer, @Cast({"CvSeqBlock**"}) PointerPointer pointerPointer);

    public static native int cvSeqElemIdx(@Const CvSeq cvSeq, @Const Pointer pointer, @ByPtrPtr CvSeqBlock cvSeqBlock);

    @Cast({"schar*"})
    public static native BytePointer cvSeqInsert(CvSeq cvSeq, int i);

    @Cast({"schar*"})
    public static native BytePointer cvSeqInsert(CvSeq cvSeq, int i, @Const Pointer pointer);

    public static native void cvSeqInsertSlice(CvSeq cvSeq, int i, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvSeqInvert(CvSeq cvSeq);

    public static native int cvSeqPartition(@Const CvSeq cvSeq, CvMemStorage cvMemStorage, @Cast({"CvSeq**"}) PointerPointer pointerPointer, CvCmpFunc cvCmpFunc, Pointer pointer);

    public static native int cvSeqPartition(@Const CvSeq cvSeq, CvMemStorage cvMemStorage, @ByPtrPtr CvSeq cvSeq2, CvCmpFunc cvCmpFunc, Pointer pointer);

    public static native void cvSeqPop(CvSeq cvSeq);

    public static native void cvSeqPop(CvSeq cvSeq, Pointer pointer);

    public static native void cvSeqPopFront(CvSeq cvSeq);

    public static native void cvSeqPopFront(CvSeq cvSeq, Pointer pointer);

    public static native void cvSeqPopMulti(CvSeq cvSeq, Pointer pointer, int i);

    public static native void cvSeqPopMulti(CvSeq cvSeq, Pointer pointer, int i, int i2);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPush(CvSeq cvSeq);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPush(CvSeq cvSeq, @Const Pointer pointer);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPushFront(CvSeq cvSeq);

    @Cast({"schar*"})
    public static native BytePointer cvSeqPushFront(CvSeq cvSeq, @Const Pointer pointer);

    public static native void cvSeqPushMulti(CvSeq cvSeq, @Const Pointer pointer, int i);

    public static native void cvSeqPushMulti(CvSeq cvSeq, @Const Pointer pointer, int i, int i2);

    public static native void cvSeqRemove(CvSeq cvSeq, int i);

    public static native void cvSeqRemoveSlice(CvSeq cvSeq, @ByVal CvSlice cvSlice);

    @Cast({"schar*"})
    public static native ByteBuffer cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, IntBuffer intBuffer);

    @Cast({"schar*"})
    public static native ByteBuffer cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, IntBuffer intBuffer, Pointer pointer2);

    @Cast({"schar*"})
    public static native BytePointer cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, IntPointer intPointer);

    @Cast({"schar*"})
    public static native BytePointer cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, IntPointer intPointer, Pointer pointer2);

    @Cast({"schar*"})
    public static native byte[] cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, int[] iArr);

    @Cast({"schar*"})
    public static native byte[] cvSeqSearch(CvSeq cvSeq, @Const Pointer pointer, CvCmpFunc cvCmpFunc, int i, int[] iArr, Pointer pointer2);

    public static native CvSeq cvSeqSlice(@Const CvSeq cvSeq, @ByVal CvSlice cvSlice);

    public static native CvSeq cvSeqSlice(@Const CvSeq cvSeq, @ByVal CvSlice cvSlice, CvMemStorage cvMemStorage, int i);

    public static native void cvSeqSort(CvSeq cvSeq, CvCmpFunc cvCmpFunc);

    public static native void cvSeqSort(CvSeq cvSeq, CvCmpFunc cvCmpFunc, Pointer pointer);

    public static native void cvSet(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar);

    public static native void cvSet(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvSet1D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, @ByVal CvScalar cvScalar);

    public static native void cvSet2D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, @ByVal CvScalar cvScalar);

    public static native void cvSet3D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3, @ByVal CvScalar cvScalar);

    public static native int cvSetAdd(CvSet cvSet);

    public static native int cvSetAdd(CvSet cvSet, CvSetElem cvSetElem, @Cast({"CvSetElem**"}) PointerPointer pointerPointer);

    public static native int cvSetAdd(CvSet cvSet, CvSetElem cvSetElem, @ByPtrPtr CvSetElem cvSetElem2);

    public static native void cvSetData(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, Pointer pointer, int i);

    public static native int cvSetErrMode(int i);

    public static native void cvSetErrStatus(int i);

    public static native void cvSetIPLAllocators(Cv_iplCreateImageHeader cv_iplCreateImageHeader, Cv_iplAllocateImageData cv_iplAllocateImageData, Cv_iplDeallocate cv_iplDeallocate, Cv_iplCreateROI cv_iplCreateROI, Cv_iplCloneImage cv_iplCloneImage);

    public static native void cvSetIdentity(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvSetIdentity(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal(nullValue = "CvScalar(cvRealScalar(1))") CvScalar cvScalar);

    public static native void cvSetImageCOI(IplImage iplImage, int i);

    public static native void cvSetImageROI(IplImage iplImage, @ByVal CvRect cvRect);

    public static native void cvSetND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer, @ByVal CvScalar cvScalar);

    public static native void cvSetND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer, @ByVal CvScalar cvScalar);

    public static native void cvSetND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr, @ByVal CvScalar cvScalar);

    public static native CvSetElem cvSetNew(CvSet cvSet);

    public static native void cvSetNumThreads();

    public static native void cvSetNumThreads(int i);

    public static native void cvSetReal1D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, double d);

    public static native void cvSetReal2D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, double d);

    public static native void cvSetReal3D(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i, int i2, int i3, double d);

    public static native void cvSetRealND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer, double d);

    public static native void cvSetRealND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer, double d);

    public static native void cvSetRealND(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr, double d);

    public static native void cvSetRemove(CvSet cvSet, int i);

    public static native void cvSetRemoveByPtr(CvSet cvSet, Pointer pointer);

    public static native void cvSetSeqBlockSize(CvSeq cvSeq, int i);

    public static native void cvSetSeqReaderPos(CvSeqReader cvSeqReader, int i);

    public static native void cvSetSeqReaderPos(CvSeqReader cvSeqReader, int i, int i2);

    public static native void cvSetZero(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @ByVal
    public static native CvSize cvSize(int i, int i2);

    @ByVal
    public static native CvSize2D32f cvSize2D32f(double d, double d2);

    @ByVal
    public static native CvSlice cvSlice(int i, int i2);

    public static native int cvSliceLength(@ByVal CvSlice cvSlice, @Const CvSeq cvSeq);

    public static native int cvSolve(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native int cvSolve(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, int i);

    public static native int cvSolveCubic(@Const CvMat cvMat, CvMat cvMat2);

    public static native void cvSolvePoly(@Const CvMat cvMat, CvMat cvMat2);

    public static native void cvSolvePoly(@Const CvMat cvMat, CvMat cvMat2, int i, int i2);

    public static native void cvSort(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvSort(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, int i);

    public static native void cvSplit(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr5);

    public static native void cvStartAppendToSeq(CvSeq cvSeq, CvSeqWriter cvSeqWriter);

    public static native void cvStartNextStream(CvFileStorage cvFileStorage);

    public static native void cvStartReadRawData(@Const CvFileStorage cvFileStorage, @Const CvFileNode cvFileNode, CvSeqReader cvSeqReader);

    public static native void cvStartReadSeq(@Const CvSeq cvSeq, CvSeqReader cvSeqReader);

    public static native void cvStartReadSeq(@Const CvSeq cvSeq, CvSeqReader cvSeqReader, int i);

    public static native void cvStartWriteSeq(int i, int i2, int i3, CvMemStorage cvMemStorage, CvSeqWriter cvSeqWriter);

    public static native void cvStartWriteStruct(CvFileStorage cvFileStorage, String str, int i);

    public static native void cvStartWriteStruct(CvFileStorage cvFileStorage, String str, int i, String str2, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native void cvStartWriteStruct(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvStartWriteStruct(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, int i, @Cast({"const char*"}) BytePointer bytePointer2, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native int cvStdErrReport(int i, String str, String str2, String str3, int i2, Pointer pointer);

    public static native int cvStdErrReport(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2, Pointer pointer);

    public static native void cvSub(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvSub(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvSubRS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvSubRS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvSubS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvSubS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    @ByVal
    public static native CvScalar cvSum(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvT(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    @ByVal
    public static native CvTermCriteria cvTermCriteria(int i, int i2, double d);

    @ByVal
    public static native CvScalar cvTrace(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvTransform(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const CvMat cvMat);

    public static native void cvTransform(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const CvMat cvMat, @Const CvMat cvMat2);

    public static native void cvTranspose(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native CvSeq cvTreeToNodeSeq(@Const Pointer pointer, int i, CvMemStorage cvMemStorage);

    public static native CvTypeInfo cvTypeOf(@Const Pointer pointer);

    public static native void cvUnregisterType(String str);

    public static native void cvUnregisterType(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvUseOptimized(int i);

    public static native void cvWrite(CvFileStorage cvFileStorage, String str, @Const Pointer pointer);

    public static native void cvWrite(CvFileStorage cvFileStorage, String str, @Const Pointer pointer, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native void cvWrite(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Const Pointer pointer);

    public static native void cvWrite(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Const Pointer pointer, @ByVal(nullValue = "CvAttrList(cvAttrList())") CvAttrList cvAttrList);

    public static native void cvWriteComment(CvFileStorage cvFileStorage, String str, int i);

    public static native void cvWriteComment(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvWriteFileNode(CvFileStorage cvFileStorage, String str, @Const CvFileNode cvFileNode, int i);

    public static native void cvWriteFileNode(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Const CvFileNode cvFileNode, int i);

    public static native void cvWriteInt(CvFileStorage cvFileStorage, String str, int i);

    public static native void cvWriteInt(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvWriteRawData(CvFileStorage cvFileStorage, @Const Pointer pointer, int i, String str);

    public static native void cvWriteRawData(CvFileStorage cvFileStorage, @Const Pointer pointer, int i, @Cast({"const char*"}) BytePointer bytePointer);

    public static native void cvWriteReal(CvFileStorage cvFileStorage, String str, double d);

    public static native void cvWriteReal(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, double d);

    public static native void cvWriteString(CvFileStorage cvFileStorage, String str, String str2);

    public static native void cvWriteString(CvFileStorage cvFileStorage, String str, String str2, int i);

    public static native void cvWriteString(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native void cvWriteString(CvFileStorage cvFileStorage, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    public static native void cvXor(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvXor(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4);

    public static native void cvXorS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvXorS(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvScalar cvScalar, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3);

    public static native void cvZero(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    public static native int cv_abs(@Cast({"uchar"}) byte b);

    @Namespace("cv")
    public static native int cv_abs(@Cast({"ushort"}) short s);

    @Namespace("cv")
    @ByVal
    public static native Mat cvarrToMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    @ByVal
    public static native Mat cvarrToMat(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, int i, @Cast({"cv::AutoBuffer<double>*"}) Pointer pointer);

    @Namespace("cv")
    @ByVal
    public static native Mat cvarrToMatND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    @ByVal
    public static native Mat cvarrToMatND(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"bool"}) boolean z, int i);

    public static native double cvmGet(@Const CvMat cvMat, int i, int i2);

    public static native void cvmSet(CvMat cvMat, int i, int i2, double d);

    @Namespace("cv")
    public static native void dct(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void dct(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void dct(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void dct(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native double determinant(@ByVal Mat mat);

    @Namespace("cv")
    public static native double determinant(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void dft(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void dft(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

    @Namespace("cv")
    public static native void dft(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void dft(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

    @Namespace("cv::hal")
    public static native void div16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void div8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(double d, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const Mat mat, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const MatExpr matExpr, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const MatExpr matExpr, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator /"})
    public static native MatExpr divide(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2);

    @Namespace("cv")
    public static native void divide(double d, @ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void divide(double d, @ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void divide(double d, @ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void divide(double d, @ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void divide(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void divide(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, int i);

    @Namespace("cv")
    public static native void divide(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void divide(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, int i);

    @Namespace("cv")
    @ByRef
    @Name({"operator /="})
    public static native Mat dividePut(@ByRef Mat mat, double d);

    @Namespace("cv")
    @ByRef
    @Name({"operator /="})
    public static native Mat dividePut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Name({"randu<double>"})
    public static native double doubleRand();

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean eigen(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean eigen(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean eigen(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean eigen(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    @ByVal
    @Name({"operator =="})
    public static native MatExpr equals(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator =="})
    public static native MatExpr equals(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator =="})
    public static native MatExpr equals(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator =="})
    public static native boolean equals(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator =="})
    public static native boolean equals(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator =="})
    public static native boolean equals(@ByRef @Const FileNodeIterator fileNodeIterator, @ByRef @Const FileNodeIterator fileNodeIterator2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator =="})
    public static native boolean equals(@ByRef @Const Range range, @ByRef @Const Range range2);

    @Namespace("cv")
    public static native void error(int i, @Str String str, String str2, String str3, int i2);

    @Namespace("cv")
    public static native void error(int i, @Str BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2);

    @Namespace("cv")
    public static native void errorNoReturn(int i, @Str String str, String str2, String str3, int i2);

    @Namespace("cv")
    public static native void errorNoReturn(int i, @Str BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, int i2);

    @Namespace("cv::hal")
    public static native void exp(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void exp(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void exp(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void exp(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv")
    public static native void exp(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void exp(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::hal")
    public static native void exp(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void exp(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void exp32f(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void exp32f(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native void exp32f(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void exp64f(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void exp64f(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void exp64f(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv")
    public static native void extractChannel(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void extractChannel(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void extractImageCOI(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal Mat mat);

    @Namespace("cv")
    public static native void extractImageCOI(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal Mat mat, int i);

    @Namespace("cv")
    public static native void extractImageCOI(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void extractImageCOI(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal UMat uMat, int i);

    @Namespace("cv")
    public static native float fastAtan2(float f, float f2);

    @Namespace("cv::hal")
    public static native void fastAtan2(@Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, FloatBuffer floatBuffer3, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv::hal")
    public static native void fastAtan2(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, FloatPointer floatPointer3, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv::hal")
    public static native void fastAtan2(@Const float[] fArr, @Const float[] fArr2, float[] fArr3, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void fastFree(Pointer pointer);

    @Namespace("cv")
    public static native Pointer fastMalloc(@Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void findNonZero(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void findNonZero(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void flip(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void flip(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    @Name({"randu<float>"})
    public static native float floatRand();

    @Namespace("cv")
    @Str
    public static native String format(String str);

    @Namespace("cv")
    @Str
    public static native BytePointer format(@Cast({"const char*"}) BytePointer bytePointer);

    @Namespace("cv")
    @Ptr
    public static native Formatted format(@ByVal Mat mat, int i);

    @Namespace("cv")
    @Ptr
    public static native Formatted format(@ByVal UMat uMat, int i);

    @Namespace("cv")
    public static native void gemm(@ByVal Mat mat, @ByVal Mat mat2, double d, @ByVal Mat mat3, double d2, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void gemm(@ByVal Mat mat, @ByVal Mat mat2, double d, @ByVal Mat mat3, double d2, @ByVal Mat mat4, int i);

    @Namespace("cv")
    public static native void gemm(@ByVal UMat uMat, @ByVal UMat uMat2, double d, @ByVal UMat uMat3, double d2, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void gemm(@ByVal UMat uMat, @ByVal UMat uMat2, double d, @ByVal UMat uMat3, double d2, @ByVal UMat uMat4, int i);

    @Namespace("cv")
    @Str
    public static native BytePointer getBuildInformation();

    @Namespace("cv")
    @Cast({"int64"})
    public static native long getCPUTickCount();

    @Namespace("cv")
    @Cast({"size_t"})
    public static native long getElemSize(int i);

    @Namespace("cv::ipp")
    @Str
    public static native BytePointer getIppErrorLocation();

    @Namespace("cv::ipp")
    public static native int getIppFeatures();

    @Namespace("cv::ipp")
    public static native int getIppStatus();

    @Namespace("cv")
    public static native int getNumThreads();

    @Namespace("cv")
    public static native int getNumberOfCPUs();

    @Namespace("cv")
    public static native int getOptimalDFTSize(int i);

    @Namespace("cv")
    @Cast({"schar*"})
    public static native BytePointer getSeqElem(@Const CvSeq cvSeq, int i);

    @Namespace("cv")
    public static native int getThreadNum();

    @Namespace("cv")
    @Cast({"int64"})
    public static native long getTickCount();

    @Namespace("cv")
    public static native double getTickFrequency();

    @Namespace("cv")
    public static native void glob(@Str String str, @ByRef StringVector stringVector);

    @Namespace("cv")
    public static native void glob(@Str String str, @ByRef StringVector stringVector, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void glob(@Str BytePointer bytePointer, @ByRef StringVector stringVector);

    @Namespace("cv")
    public static native void glob(@Str BytePointer bytePointer, @ByRef StringVector stringVector, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @ByVal
    @Name({"operator >"})
    public static native MatExpr greaterThan(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator >"})
    public static native MatExpr greaterThan(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator >"})
    public static native MatExpr greaterThan(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator >"})
    public static native boolean greaterThan(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator >"})
    public static native boolean greaterThan(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    @ByVal
    @Name({"operator >="})
    public static native MatExpr greaterThanEquals(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator >="})
    public static native MatExpr greaterThanEquals(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator >="})
    public static native MatExpr greaterThanEquals(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator >="})
    public static native boolean greaterThanEquals(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator >="})
    public static native boolean greaterThanEquals(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void hconcat(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void hconcat(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void hconcat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void hconcat(@ByVal MatVector matVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void hconcat(@ByVal MatVector matVector, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void hconcat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void hconcat(@ByVal UMatVector uMatVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void hconcat(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void idct(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void idct(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void idct(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void idct(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void idft(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void idft(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

    @Namespace("cv")
    public static native void idft(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void idft(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

    @Namespace("cv")
    public static native void inRange(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void inRange(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void insertChannel(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void insertChannel(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void insertImageCOI(@ByVal Mat mat, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    public static native void insertImageCOI(@ByVal Mat mat, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    @Namespace("cv")
    public static native void insertImageCOI(@ByVal UMat uMat, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    public static native void insertImageCOI(@ByVal UMat uMat, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int i);

    @Namespace("cv")
    @Name({"randu<int>"})
    public static native int intRand();

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(double d);

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(float f);

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(int i);

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Name({"saturate_cast<int>"})
    public static native int intSaturate(short s);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt32f(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt32f(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt32f(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt64f(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt64f(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void invSqrt64f(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv")
    public static native double invert(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native double invert(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native double invert(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native double invert(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native double kmeans(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal TermCriteria termCriteria, int i2, int i3);

    @Namespace("cv")
    public static native double kmeans(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal TermCriteria termCriteria, int i2, int i3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    public static native double kmeans(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal TermCriteria termCriteria, int i2, int i3);

    @Namespace("cv")
    public static native double kmeans(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal TermCriteria termCriteria, int i2, int i3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    @ByVal
    @Name({"operator <"})
    public static native MatExpr lessThan(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator <"})
    public static native MatExpr lessThan(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator <"})
    public static native MatExpr lessThan(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator <"})
    public static native boolean lessThan(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator <"})
    public static native boolean lessThan(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator <"})
    public static native boolean lessThan(@ByRef @Const FileNodeIterator fileNodeIterator, @ByRef @Const FileNodeIterator fileNodeIterator2);

    @Namespace("cv")
    @ByVal
    @Name({"operator <="})
    public static native MatExpr lessThanEquals(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator <="})
    public static native MatExpr lessThanEquals(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator <="})
    public static native MatExpr lessThanEquals(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator <="})
    public static native boolean lessThanEquals(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator <="})
    public static native boolean lessThanEquals(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv::hal")
    public static native void log(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void log(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void log(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void log(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv")
    public static native void log(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void log(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::hal")
    public static native void log(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void log(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void log32f(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void log32f(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native void log32f(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void log64f(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void log64f(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void log64f(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void magnitude(@Const DoubleBuffer doubleBuffer, @Const DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude(@Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, FloatBuffer floatBuffer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude(@Const DoublePointer doublePointer, @Const DoublePointer doublePointer2, DoublePointer doublePointer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, FloatPointer floatPointer3, int i);

    @Namespace("cv")
    public static native void magnitude(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void magnitude(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv::hal")
    public static native void magnitude(@Const double[] dArr, @Const double[] dArr2, double[] dArr3, int i);

    @Namespace("cv::hal")
    public static native void magnitude(@Const float[] fArr, @Const float[] fArr2, float[] fArr3, int i);

    @Namespace("cv::hal")
    public static native void magnitude32f(@Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, FloatBuffer floatBuffer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude32f(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, FloatPointer floatPointer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude32f(@Const float[] fArr, @Const float[] fArr2, float[] fArr3, int i);

    @Namespace("cv::hal")
    public static native void magnitude64f(@Const DoubleBuffer doubleBuffer, @Const DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude64f(@Const DoublePointer doublePointer, @Const DoublePointer doublePointer2, DoublePointer doublePointer3, int i);

    @Namespace("cv::hal")
    public static native void magnitude64f(@Const double[] dArr, @Const double[] dArr2, double[] dArr3, int i);

    @Namespace("cv")
    @ByVal
    public static native MatExpr max(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    public static native MatExpr max(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    public static native MatExpr max(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    public static native void max(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void max(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv::hal")
    public static native void max16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void max8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByVal
    public static native Scalar mean(@ByVal Mat mat);

    @Namespace("cv")
    @ByVal
    public static native Scalar mean(@ByVal Mat mat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    @ByVal
    public static native Scalar mean(@ByVal UMat uMat);

    @Namespace("cv")
    @ByVal
    public static native Scalar mean(@ByVal UMat uMat, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native void meanStdDev(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void meanStdDev(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    public static native void meanStdDev(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void meanStdDev(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    public static native void merge(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void merge(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void merge(@ByVal MatVector matVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void merge(@ByVal MatVector matVector, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void merge(@ByVal UMatVector uMatVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void merge(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

    @Namespace("cv::hal")
    public static native void merge16u(@ByPtrPtr @Cast({"const ushort**"}) ShortBuffer shortBuffer, @Cast({"ushort*"}) ShortBuffer shortBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge16u(@Cast({"const ushort**"}) PointerPointer pointerPointer, @Cast({"ushort*"}) ShortPointer shortPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge16u(@ByPtrPtr @Cast({"const ushort**"}) ShortPointer shortPointer, @Cast({"ushort*"}) ShortPointer shortPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge16u(@ByPtrPtr @Cast({"const ushort**"}) short[] sArr, @Cast({"ushort*"}) short[] sArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge32s(@ByPtrPtr @Const IntBuffer intBuffer, IntBuffer intBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge32s(@ByPtrPtr @Const IntPointer intPointer, IntPointer intPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge32s(@Cast({"const int**"}) PointerPointer pointerPointer, IntPointer intPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge32s(@ByPtrPtr @Const int[] iArr, int[] iArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge64s(@ByPtrPtr @Cast({"const int64**"}) LongBuffer longBuffer, @Cast({"int64*"}) LongBuffer longBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge64s(@ByPtrPtr @Cast({"const int64**"}) LongPointer longPointer, @Cast({"int64*"}) LongPointer longPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge64s(@Cast({"const int64**"}) PointerPointer pointerPointer, @Cast({"int64*"}) LongPointer longPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge64s(@ByPtrPtr @Cast({"const int64**"}) long[] jArr, @Cast({"int64*"}) long[] jArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge8u(@ByPtrPtr @Cast({"const uchar**"}) ByteBuffer byteBuffer, @Cast({"uchar*"}) ByteBuffer byteBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge8u(@ByPtrPtr @Cast({"const uchar**"}) BytePointer bytePointer, @Cast({"uchar*"}) BytePointer bytePointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge8u(@Cast({"const uchar**"}) PointerPointer pointerPointer, @Cast({"uchar*"}) BytePointer bytePointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void merge8u(@ByPtrPtr @Cast({"const uchar**"}) byte[] bArr, @Cast({"uchar*"}) byte[] bArr2, int i, int i2);

    @Namespace("cv")
    @ByVal
    public static native MatExpr min(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    public static native MatExpr min(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    public static native MatExpr min(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    public static native void min(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void min(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv::hal")
    public static native void min16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void min8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal Mat mat, DoubleBuffer doubleBuffer);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal Mat mat, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, IntBuffer intBuffer, IntBuffer intBuffer2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal Mat mat, DoublePointer doublePointer);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal Mat mat, DoublePointer doublePointer, DoublePointer doublePointer2, IntPointer intPointer, IntPointer intPointer2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal UMat uMat, DoublePointer doublePointer);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal UMat uMat, DoublePointer doublePointer, DoublePointer doublePointer2, IntPointer intPointer, IntPointer intPointer2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal UMat uMat, double[] dArr);

    @Namespace("cv")
    public static native void minMaxIdx(@ByVal UMat uMat, double[] dArr, double[] dArr2, int[] iArr, int[] iArr2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal Mat mat, DoubleBuffer doubleBuffer);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal Mat mat, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, Point point, Point point2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal Mat mat, DoublePointer doublePointer);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal Mat mat, DoublePointer doublePointer, DoublePointer doublePointer2, Point point, Point point2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, DoublePointer doublePointer, DoublePointer doublePointer2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, DoublePointer doublePointer, DoublePointer doublePointer2, IntPointer intPointer, IntPointer intPointer2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, double[] dArr, double[] dArr2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByRef @Const SparseMat sparseMat, double[] dArr, double[] dArr2, int[] iArr, int[] iArr2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal UMat uMat, DoublePointer doublePointer);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal UMat uMat, DoublePointer doublePointer, DoublePointer doublePointer2, Point point, Point point2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal UMat uMat, double[] dArr);

    @Namespace("cv")
    public static native void minMaxLoc(@ByVal UMat uMat, double[] dArr, double[] dArr2, Point point, Point point2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native void mixChannels(@Const Mat mat, @Cast({"size_t"}) long j, Mat mat2, @Cast({"size_t"}) long j2, @Const IntBuffer intBuffer, @Cast({"size_t"}) long j3);

    @Namespace("cv")
    public static native void mixChannels(@Const Mat mat, @Cast({"size_t"}) long j, Mat mat2, @Cast({"size_t"}) long j2, @Const IntPointer intPointer, @Cast({"size_t"}) long j3);

    @Namespace("cv")
    public static native void mixChannels(@Const Mat mat, @Cast({"size_t"}) long j, Mat mat2, @Cast({"size_t"}) long j2, @Const int[] iArr, @Cast({"size_t"}) long j3);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @Const IntBuffer intBuffer, @Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @StdVector IntPointer intPointer);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @Const IntPointer intPointer, @Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @StdVector int[] iArr);

    @Namespace("cv")
    public static native void mixChannels(@ByVal MatVector matVector, @ByVal MatVector matVector2, @Const int[] iArr, @Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @Const IntBuffer intBuffer, @Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @StdVector IntPointer intPointer);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @Const IntPointer intPointer, @Cast({"size_t"}) long j);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @StdVector int[] iArr);

    @Namespace("cv")
    public static native void mixChannels(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @Const int[] iArr, @Cast({"size_t"}) long j);

    @Namespace("cv::hal")
    public static native void mul16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void mul8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    public static native void mulSpectrums(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    @Namespace("cv")
    public static native void mulSpectrums(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void mulSpectrums(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    @Namespace("cv")
    public static native void mulSpectrums(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void mulTransposed(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void mulTransposed(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool"}) boolean z, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3, double d, int i);

    @Namespace("cv")
    public static native void mulTransposed(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void mulTransposed(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool"}) boolean z, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3, double d, int i);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(double d, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const Mat mat, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const MatExpr matExpr, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const MatExpr matExpr, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator *"})
    public static native MatExpr multiply(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2);

    @Namespace("cv")
    public static native void multiply(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void multiply(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, int i);

    @Namespace("cv")
    public static native void multiply(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void multiply(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, int i);

    @Namespace("cv")
    @ByRef
    @Name({"operator *="})
    public static native Mat multiplyPut(@ByRef Mat mat, double d);

    @Namespace("cv")
    @ByRef
    @Name({"operator *="})
    public static native Mat multiplyPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    public static native double norm(@ByVal Mat mat);

    @Namespace("cv")
    public static native double norm(@ByVal Mat mat, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native double norm(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native double norm(@ByVal Mat mat, @ByVal Mat mat2, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    public static native double norm(@ByRef @Const SparseMat sparseMat, int i);

    @Namespace("cv")
    public static native double norm(@ByVal UMat uMat);

    @Namespace("cv")
    public static native double norm(@ByVal UMat uMat, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    public static native double norm(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native double norm(@ByVal UMat uMat, @ByVal UMat uMat2, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) ByteBuffer byteBuffer, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) ByteBuffer byteBuffer, int i, int i2);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) BytePointer bytePointer, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) BytePointer bytePointer, int i, int i2);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"const uchar*"}) BytePointer bytePointer2, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"const uchar*"}) BytePointer bytePointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) byte[] bArr, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) byte[] bArr, int i, int i2);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) byte[] bArr, @Cast({"const uchar*"}) byte[] bArr2, int i);

    @Namespace("cv::hal")
    public static native int normHamming(@Cast({"const uchar*"}) byte[] bArr, @Cast({"const uchar*"}) byte[] bArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native float normL1_(@Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native float normL1_(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native float normL1_(@Const float[] fArr, @Const float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native int normL1_(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, int i);

    @Namespace("cv::hal")
    public static native int normL1_(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"const uchar*"}) BytePointer bytePointer2, int i);

    @Namespace("cv::hal")
    public static native int normL1_(@Cast({"const uchar*"}) byte[] bArr, @Cast({"const uchar*"}) byte[] bArr2, int i);

    @Namespace("cv::hal")
    public static native float normL2Sqr_(@Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native float normL2Sqr_(@Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native float normL2Sqr_(@Const float[] fArr, @Const float[] fArr2, int i);

    @Namespace("cv")
    public static native void normalize(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void normalize(@ByVal Mat mat, @ByVal Mat mat2, double d, double d2, int i, int i2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    public static native void normalize(@ByRef @Const SparseMat sparseMat, @ByRef SparseMat sparseMat2, double d, int i);

    @Namespace("cv")
    public static native void normalize(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void normalize(@ByVal UMat uMat, @ByVal UMat uMat2, double d, double d2, int i, int i2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    @ByVal
    @Name({"operator ~"})
    public static native MatExpr not(@ByRef @Const Mat mat);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator !"})
    public static native boolean not(@ByRef @Const Range range);

    @Namespace("cv::hal")
    public static native void not8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void not8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void not8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByVal
    @Name({"operator !="})
    public static native MatExpr notEquals(double d, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator !="})
    public static native MatExpr notEquals(@ByRef @Const Mat mat, double d);

    @Namespace("cv")
    @ByVal
    @Name({"operator !="})
    public static native MatExpr notEquals(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator !="})
    public static native boolean notEquals(@Str String str, @Str String str2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator !="})
    public static native boolean notEquals(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator !="})
    public static native boolean notEquals(@ByRef @Const FileNodeIterator fileNodeIterator, @ByRef @Const FileNodeIterator fileNodeIterator2);

    @Namespace("cv")
    @Cast({"bool"})
    @Name({"operator !="})
    public static native boolean notEquals(@ByRef @Const Range range, @ByRef @Const Range range2);

    @Namespace("cv")
    @ByVal
    @Name({"operator |"})
    public static native MatExpr or(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator |"})
    public static native MatExpr or(@ByRef @Const Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator |"})
    public static native MatExpr or(@ByRef @Const Scalar scalar, @ByRef @Const Mat mat);

    @Namespace("cv::hal")
    public static native void or8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void or8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void or8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByRef
    @Name({"operator |="})
    public static native Mat orPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByRef
    @Name({"operator |="})
    public static native Mat orPut(@ByRef Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    public static native void parallel_for_(@ByRef @Const Range range, @ByRef @Const ParallelLoopBody parallelLoopBody);

    @Namespace("cv")
    public static native void parallel_for_(@ByRef @Const Range range, @ByRef @Const ParallelLoopBody parallelLoopBody, double d);

    @Namespace("cv")
    public static native void patchNaNs(@ByVal Mat mat);

    @Namespace("cv")
    public static native void patchNaNs(@ByVal Mat mat, double d);

    @Namespace("cv")
    public static native void patchNaNs(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void patchNaNs(@ByVal UMat uMat, double d);

    @Namespace("cv")
    public static native void perspectiveTransform(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void perspectiveTransform(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void phase(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void phase(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void phase(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void phase(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void polarToCart(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void polarToCart(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void polarToCart(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void polarToCart(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void pow(@ByVal Mat mat, double d, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void pow(@ByVal UMat uMat, double d, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native int print(@Ptr Formatted formatted);

    @Namespace("cv")
    public static native int print(@Ptr Formatted formatted, @Cast({"FILE*"}) Pointer pointer);

    @Namespace("cv")
    public static native int print(@ByRef @Const Mat mat);

    @Namespace("cv")
    public static native int print(@ByRef @Const Mat mat, @Cast({"FILE*"}) Pointer pointer);

    @Namespace("cv")
    public static native int print(@ByRef @Const UMat uMat);

    @Namespace("cv")
    public static native int print(@ByRef @Const UMat uMat, @Cast({"FILE*"}) Pointer pointer);

    @Namespace("cv")
    public static native void randShuffle(@ByVal Mat mat);

    @Namespace("cv")
    public static native void randShuffle(@ByVal Mat mat, double d, RNG rng);

    @Namespace("cv")
    public static native void randShuffle(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void randShuffle(@ByVal UMat uMat, double d, RNG rng);

    @Namespace("cv")
    public static native void randn(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void randn(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void randu(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void randu(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @Str String str, @Str String str2);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"uchar*"}) ByteBuffer byteBuffer, @Cast({"uchar"}) byte b);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef DoubleBuffer doubleBuffer, double d);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef FloatBuffer floatBuffer, float f);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef IntBuffer intBuffer, int i);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"ushort*"}) ShortBuffer shortBuffer, @Cast({"ushort"}) short s);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"uchar*"}) BytePointer bytePointer, @Cast({"uchar"}) byte b);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef DoublePointer doublePointer, double d);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef FloatPointer floatPointer, float f);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef IntPointer intPointer, int i);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"ushort*"}) ShortPointer shortPointer, @Cast({"ushort"}) short s);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef DMatchVector dMatchVector);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef KeyPointVector keyPointVector);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef Mat mat);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef Mat mat, @ByRef(nullValue = "cv::Mat()") @Const Mat mat2);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef Range range, @ByRef @Const Range range2);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef SparseMat sparseMat);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef SparseMat sparseMat, @ByRef(nullValue = "cv::SparseMat()") @Const SparseMat sparseMat2);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"uchar*"}) byte[] bArr, @Cast({"uchar"}) byte b);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef double[] dArr, double d);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef float[] fArr, float f);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef int[] iArr, int i);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"ushort*"}) short[] sArr, @Cast({"ushort"}) short s);

    @Namespace("cv")
    public static native void read(@ByRef @Const FileNode fileNode, @ByRef @Cast({"bool*"}) boolean[] zArr, @Cast({"bool"}) boolean z);

    @Namespace("cv::hal")
    public static native void recip16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void recip8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    public static native ErrorCallback redirectError(ErrorCallback errorCallback);

    @Namespace("cv")
    public static native ErrorCallback redirectError(ErrorCallback errorCallback, Pointer pointer, @ByPtrPtr @Cast({"void**"}) Pointer pointer2);

    @Namespace("cv")
    public static native ErrorCallback redirectError(ErrorCallback errorCallback, Pointer pointer, @Cast({"void**"}) PointerPointer pointerPointer);

    @Namespace("cv")
    public static native void reduce(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

    @Namespace("cv")
    public static native void reduce(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2, int i3);

    @Namespace("cv")
    public static native void reduce(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

    @Namespace("cv")
    public static native void reduce(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2, int i3);

    @Namespace("cv")
    @ByVal
    public static native Mat repeat(@ByRef @Const Mat mat, int i, int i2);

    @Namespace("cv")
    public static native void repeat(@ByVal Mat mat, int i, int i2, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void repeat(@ByVal UMat uMat, int i, int i2, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void scaleAdd(@ByVal Mat mat, double d, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void scaleAdd(@ByVal UMat uMat, double d, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(double d);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(float f);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(int i);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Cast({"schar"})
    @Name({"saturate_cast<schar>"})
    public static native byte scharSaturateCast(short s);

    @Namespace("cv")
    public static native void seqInsertSlice(CvSeq cvSeq, int i, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Namespace("cv")
    public static native void seqPop(CvSeq cvSeq);

    @Namespace("cv")
    public static native void seqPop(CvSeq cvSeq, Pointer pointer);

    @Namespace("cv")
    public static native void seqPopFront(CvSeq cvSeq);

    @Namespace("cv")
    public static native void seqPopFront(CvSeq cvSeq, Pointer pointer);

    @Namespace("cv")
    @Cast({"schar*"})
    public static native BytePointer seqPush(CvSeq cvSeq);

    @Namespace("cv")
    @Cast({"schar*"})
    public static native BytePointer seqPush(CvSeq cvSeq, @Const Pointer pointer);

    @Namespace("cv")
    @Cast({"schar*"})
    public static native BytePointer seqPushFront(CvSeq cvSeq);

    @Namespace("cv")
    @Cast({"schar*"})
    public static native BytePointer seqPushFront(CvSeq cvSeq, @Const Pointer pointer);

    @Namespace("cv")
    public static native void seqRemove(CvSeq cvSeq, int i);

    @Namespace("cv")
    public static native void seqRemoveSlice(CvSeq cvSeq, @ByVal CvSlice cvSlice);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean setBreakOnError(@Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void setIdentity(@ByVal Mat mat);

    @Namespace("cv")
    public static native void setIdentity(@ByVal Mat mat, @ByRef(nullValue = "cv::Scalar(1)") @Const Scalar scalar);

    @Namespace("cv")
    public static native void setIdentity(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void setIdentity(@ByVal UMat uMat, @ByRef(nullValue = "cv::Scalar(1)") @Const Scalar scalar);

    @Namespace("cv::ipp")
    public static native void setIppStatus(int i);

    @Namespace("cv::ipp")
    public static native void setIppStatus(int i, String str, String str2, int i2);

    @Namespace("cv::ipp")
    public static native void setIppStatus(int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i2);

    @Namespace("cv")
    public static native void setNumThreads(int i);

    @Namespace("cv::ipp")
    public static native void setUseIPP(@Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void setUseOptimized(@Cast({"bool"}) boolean z);

    @Namespace("cv")
    @Str
    @Name({"operator <<"})
    public static native String shiftLeft(@Str String str, @Ptr Formatted formatted);

    @Namespace("cv")
    @Str
    @Name({"operator <<"})
    public static native String shiftLeft(@Str String str, @ByRef @Const Mat mat);

    @Namespace("cv")
    @Str
    @Name({"operator <<"})
    public static native BytePointer shiftLeft(@Str BytePointer bytePointer, @Ptr Formatted formatted);

    @Namespace("cv")
    @Str
    @Name({"operator <<"})
    public static native BytePointer shiftLeft(@Str BytePointer bytePointer, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByRef
    @Name({"operator <<"})
    public static native FileStorage shiftLeft(@ByRef FileStorage fileStorage, @Str String str);

    @Namespace("cv")
    @ByRef
    @Name({"operator <<"})
    public static native FileStorage shiftLeft(@ByRef FileStorage fileStorage, @Cast({"char*"}) ByteBuffer byteBuffer);

    @Namespace("cv")
    @ByRef
    @Name({"operator <<"})
    public static native FileStorage shiftLeft(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer);

    @Namespace("cv")
    @ByRef
    @Name({"operator <<"})
    public static native FileStorage shiftLeft(@ByRef FileStorage fileStorage, @Cast({"char*"}) byte[] bArr);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(double d);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(float f);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(int i);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Name({"saturate_cast<short>"})
    public static native short shortSaturateCast(short s);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solve(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solve(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solve(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solve(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    @Namespace("cv")
    public static native int solveCubic(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native int solveCubic(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native int solveLP(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef Mat mat3);

    @Namespace("cv")
    public static native double solvePoly(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native double solvePoly(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native double solvePoly(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native double solvePoly(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void sort(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void sort(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void sortIdx(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv")
    public static native void sortIdx(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    public static native void split(@ByRef @Const Mat mat, Mat mat2);

    @Namespace("cv")
    public static native void split(@ByVal Mat mat, @ByVal MatVector matVector);

    @Namespace("cv")
    public static native void split(@ByVal Mat mat, @ByVal UMatVector uMatVector);

    @Namespace("cv")
    public static native void split(@ByVal UMat uMat, @ByVal MatVector matVector);

    @Namespace("cv")
    public static native void split(@ByVal UMat uMat, @ByVal UMatVector uMatVector);

    @Namespace("cv::hal")
    public static native void split16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @ByPtrPtr @Cast({"ushort**"}) ShortBuffer shortBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"ushort**"}) PointerPointer pointerPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void split16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @ByPtrPtr @Cast({"ushort**"}) ShortPointer shortPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split16u(@Cast({"const ushort*"}) short[] sArr, @ByPtrPtr @Cast({"ushort**"}) short[] sArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split32s(@Const IntBuffer intBuffer, @ByPtrPtr IntBuffer intBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split32s(@Const IntPointer intPointer, @ByPtrPtr IntPointer intPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split32s(@Const IntPointer intPointer, @Cast({"int**"}) PointerPointer pointerPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void split32s(@Const int[] iArr, @ByPtrPtr int[] iArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split64s(@Cast({"const int64*"}) LongBuffer longBuffer, @ByPtrPtr @Cast({"int64**"}) LongBuffer longBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split64s(@Cast({"const int64*"}) LongPointer longPointer, @ByPtrPtr @Cast({"int64**"}) LongPointer longPointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split64s(@Cast({"const int64*"}) LongPointer longPointer, @Cast({"int64**"}) PointerPointer pointerPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void split64s(@Cast({"const int64*"}) long[] jArr, @ByPtrPtr @Cast({"int64**"}) long[] jArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @ByPtrPtr @Cast({"uchar**"}) ByteBuffer byteBuffer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split8u(@Cast({"const uchar*"}) BytePointer bytePointer, @ByPtrPtr @Cast({"uchar**"}) BytePointer bytePointer2, int i, int i2);

    @Namespace("cv::hal")
    public static native void split8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"uchar**"}) PointerPointer pointerPointer, int i, int i2);

    @Namespace("cv::hal")
    public static native void split8u(@Cast({"const uchar*"}) byte[] bArr, @ByPtrPtr @Cast({"uchar**"}) byte[] bArr2, int i, int i2);

    @Namespace("cv::hal")
    public static native void sqrt(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv")
    public static native void sqrt(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void sqrt(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::hal")
    public static native void sqrt(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void sqrt(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void sqrt32f(@Const FloatBuffer floatBuffer, FloatBuffer floatBuffer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt32f(@Const FloatPointer floatPointer, FloatPointer floatPointer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt32f(@Const float[] fArr, float[] fArr2, int i);

    @Namespace("cv::hal")
    public static native void sqrt64f(@Const DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt64f(@Const DoublePointer doublePointer, DoublePointer doublePointer2, int i);

    @Namespace("cv::hal")
    public static native void sqrt64f(@Const double[] dArr, double[] dArr2, int i);

    @Namespace("cv::hal")
    public static native void sub16s(@Const ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Const ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub16s(@Const ShortPointer shortPointer, @Cast({"size_t"}) long j, @Const ShortPointer shortPointer2, @Cast({"size_t"}) long j2, ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub16s(@Const short[] sArr, @Cast({"size_t"}) long j, @Const short[] sArr2, @Cast({"size_t"}) long j2, short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub16u(@Cast({"const ushort*"}) ShortBuffer shortBuffer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortBuffer shortBuffer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortBuffer shortBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub16u(@Cast({"const ushort*"}) ShortPointer shortPointer, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) ShortPointer shortPointer2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) ShortPointer shortPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub16u(@Cast({"const ushort*"}) short[] sArr, @Cast({"size_t"}) long j, @Cast({"const ushort*"}) short[] sArr2, @Cast({"size_t"}) long j2, @Cast({"ushort*"}) short[] sArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32f(@Const FloatBuffer floatBuffer, @Cast({"size_t"}) long j, @Const FloatBuffer floatBuffer2, @Cast({"size_t"}) long j2, FloatBuffer floatBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32f(@Const FloatPointer floatPointer, @Cast({"size_t"}) long j, @Const FloatPointer floatPointer2, @Cast({"size_t"}) long j2, FloatPointer floatPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32f(@Const float[] fArr, @Cast({"size_t"}) long j, @Const float[] fArr2, @Cast({"size_t"}) long j2, float[] fArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32s(@Const IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const IntBuffer intBuffer2, @Cast({"size_t"}) long j2, IntBuffer intBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32s(@Const IntPointer intPointer, @Cast({"size_t"}) long j, @Const IntPointer intPointer2, @Cast({"size_t"}) long j2, IntPointer intPointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub32s(@Const int[] iArr, @Cast({"size_t"}) long j, @Const int[] iArr2, @Cast({"size_t"}) long j2, int[] iArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub64f(@Const DoubleBuffer doubleBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer2, @Cast({"size_t"}) long j2, DoubleBuffer doubleBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub64f(@Const DoublePointer doublePointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer2, @Cast({"size_t"}) long j2, DoublePointer doublePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub64f(@Const double[] dArr, @Cast({"size_t"}) long j, @Const double[] dArr2, @Cast({"size_t"}) long j2, double[] dArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8s(@Cast({"const schar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8s(@Cast({"const schar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const schar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8s(@Cast({"const schar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const schar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"schar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void sub8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @Cast({"ptrdiff_t"})
    @Name({"operator -"})
    public static native long subtract(@ByRef @Const FileNodeIterator fileNodeIterator, @ByRef @Const FileNodeIterator fileNodeIterator2);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Mat mat, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const MatExpr matExpr, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const MatExpr matExpr, @ByRef @Const MatExpr matExpr2);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const MatExpr matExpr, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Scalar scalar, @ByRef @Const Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native MatExpr subtract(@ByRef @Const Scalar scalar, @ByRef @Const MatExpr matExpr);

    @Namespace("cv")
    @ByVal
    @Name({"operator -"})
    public static native Range subtract(@ByRef @Const Range range, int i);

    @Namespace("cv")
    public static native void subtract(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void subtract(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4, int i);

    @Namespace("cv")
    public static native void subtract(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void subtract(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4, int i);

    @Namespace("cv")
    @ByRef
    @Name({"operator -="})
    public static native Mat subtractPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByRef
    @Name({"operator -="})
    public static native Mat subtractPut(@ByRef Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"sum"})
    public static native Scalar sumElems(@ByVal Mat mat);

    @Namespace("cv")
    @ByVal
    @Name({"sum"})
    public static native Scalar sumElems(@ByVal UMat uMat);

    @Namespace("std")
    public static native void swap(@Str String str, @Str String str2);

    @Namespace("std")
    public static native void swap(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void swap(@ByRef Mat mat, @ByRef Mat mat2);

    @Namespace("cv")
    public static native void swap(@ByRef UMat uMat, @ByRef UMat uMat2);

    @Namespace("cv")
    @Str
    public static native String tempfile(String str);

    @Namespace("cv")
    @Str
    public static native BytePointer tempfile();

    @Namespace("cv")
    @Str
    public static native BytePointer tempfile(@Cast({"const char*"}) BytePointer bytePointer);

    @Namespace("cv")
    @ByRef
    public static native RNG theRNG();

    @Namespace("cv")
    @ByVal
    public static native Scalar trace(@ByVal Mat mat);

    @Namespace("cv")
    @ByVal
    public static native Scalar trace(@ByVal UMat uMat);

    @Namespace("cv")
    public static native void transform(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void transform(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void transpose(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void transpose(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(double d);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(float f);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(int i);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Cast({"uchar"})
    @Name({"saturate_cast<uchar>"})
    public static native byte ucharSaturateCast(short s);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(double d);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(float f);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(int i);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Cast({"unsigned"})
    @Name({"saturate_cast<unsigned>"})
    public static native int unsignedSaturateCast(short s);

    @Namespace("cv::ipp")
    @Cast({"bool"})
    public static native boolean useIPP();

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean useOptimized();

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(@Cast({"schar"}) byte b);

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(double d);

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(float f);

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(int i);

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(@Cast({"int64"}) long j);

    @Namespace("cv")
    @Cast({"ushort"})
    @Name({"saturate_cast<ushort>"})
    public static native short ushortSaturateCast(short s);

    @Namespace("cv")
    public static native void vconcat(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void vconcat(@Const Mat mat, @Cast({"size_t"}) long j, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void vconcat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void vconcat(@ByVal MatVector matVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void vconcat(@ByVal MatVector matVector, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void vconcat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void vconcat(@ByVal UMatVector uMatVector, @ByVal Mat mat);

    @Namespace("cv")
    public static native void vconcat(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, double d);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, float f);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, int i);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, double d);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, float f);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, int i);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @Str String str2);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @ByRef @Const DMatchVector dMatchVector);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @ByRef @Const KeyPointVector keyPointVector);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @ByRef @Const Mat mat);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @ByRef @Const Range range);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str String str, @ByRef @Const SparseMat sparseMat);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, double d);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, float f);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @ByRef @Const DMatchVector dMatchVector);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @ByRef @Const KeyPointVector keyPointVector);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @ByRef @Const Mat mat);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @ByRef @Const Range range);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer, @ByRef @Const SparseMat sparseMat);

    @Namespace("cv")
    public static native void write(@ByRef FileStorage fileStorage, @ByRef @Const Range range);

    @Namespace("cv")
    public static native void writeScalar(@ByRef FileStorage fileStorage, double d);

    @Namespace("cv")
    public static native void writeScalar(@ByRef FileStorage fileStorage, float f);

    @Namespace("cv")
    public static native void writeScalar(@ByRef FileStorage fileStorage, int i);

    @Namespace("cv")
    public static native void writeScalar(@ByRef FileStorage fileStorage, @Str String str);

    @Namespace("cv")
    public static native void writeScalar(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer);

    @Namespace("cv")
    @ByVal
    @Name({"operator ^"})
    public static native MatExpr xor(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByVal
    @Name({"operator ^"})
    public static native MatExpr xor(@ByRef @Const Mat mat, @ByRef @Const Scalar scalar);

    @Namespace("cv")
    @ByVal
    @Name({"operator ^"})
    public static native MatExpr xor(@ByRef @Const Scalar scalar, @ByRef @Const Mat mat);

    @Namespace("cv::hal")
    public static native void xor8u(@Cast({"const uchar*"}) ByteBuffer byteBuffer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) ByteBuffer byteBuffer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) ByteBuffer byteBuffer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void xor8u(@Cast({"const uchar*"}) BytePointer bytePointer, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) BytePointer bytePointer2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) BytePointer bytePointer3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv::hal")
    public static native void xor8u(@Cast({"const uchar*"}) byte[] bArr, @Cast({"size_t"}) long j, @Cast({"const uchar*"}) byte[] bArr2, @Cast({"size_t"}) long j2, @Cast({"uchar*"}) byte[] bArr3, @Cast({"size_t"}) long j3, int i, int i2, Pointer pointer);

    @Namespace("cv")
    @ByRef
    @Name({"operator ^="})
    public static native Mat xorPut(@ByRef Mat mat, @ByRef @Const Mat mat2);

    @Namespace("cv")
    @ByRef
    @Name({"operator ^="})
    public static native Mat xorPut(@ByRef Mat mat, @ByRef @Const Scalar scalar);

    static {
        Loader.load();
    }

    public static Mat noArray() {
        return null;
    }
}
