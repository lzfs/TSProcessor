package model;

import java.util.List;

/**
 * This interface should be implemented by all cluster classes.
 *
 * @param <C> the type of clusters to use.
 * @param <F> the type of frames of the record.
 * @param <R> the type of records to use.
 */
public interface Cluster<C extends Cluster, F extends Frame, R extends Record> {
    int getId();

    List<R> getComponents();

    List<F> getMedianFrames();

    boolean isConsider();

    void setConsider(boolean consider);

    /**
     * This method merges another cluster with this cluster.
     *
     * @param cluster the cluster you want to merge with.
     */
    void mergeWithCluster(C cluster);

    /**
     * This method merges a record with this cluster.
     *
     * @param record the record you want to merge with.
     */
    void mergeWithRecord(R record);

    /**
     * This method can be used to compare two clusters.
     *
     * @param otherCluster the cluster you want to compare this one to.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    int compareTo(C otherCluster);
}
