package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;

@Namespace("cv::flann")
public class opencv_flann$HierarchicalClusteringIndexParams extends opencv_flann$IndexParams {
    private native void allocate();

    private native void allocate(int i, @Cast({"cvflann::flann_centers_init_t"}) int i2, int i3, int i4);

    private native void allocateArray(long j);

    static {
        Loader.load();
    }

    public opencv_flann$HierarchicalClusteringIndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$HierarchicalClusteringIndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$HierarchicalClusteringIndexParams position(long position) {
        return (opencv_flann$HierarchicalClusteringIndexParams) super.position(position);
    }

    public opencv_flann$HierarchicalClusteringIndexParams(int branching, @Cast({"cvflann::flann_centers_init_t"}) int centers_init, int trees, int leaf_size) {
        super((Pointer) null);
        allocate(branching, centers_init, trees, leaf_size);
    }

    public opencv_flann$HierarchicalClusteringIndexParams() {
        super((Pointer) null);
        allocate();
    }
}
