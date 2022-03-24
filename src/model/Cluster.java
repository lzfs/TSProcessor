package model;

import java.util.List;

public interface Cluster< C extends Cluster, F extends Frame, R extends Record> {
    int getId();
    List<R> getComponents();
    List<F> getMedianFrames();
    boolean isConsider();
    void setConsider(boolean consider);
    void mergeWithCluster(C Cluster);
    void mergeWithRecord(R record);
    int compareTo(C otherCluster);
}
