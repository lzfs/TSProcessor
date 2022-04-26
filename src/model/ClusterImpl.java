package model;

import calculating.metric.Dtw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements the Cluster interface.
 * It represents a cluster with its id, all its components and the median frames of all these components.
 */
public class ClusterImpl implements Cluster<ClusterImpl, FrameImpl, RecordImpl> {
    /**
     * The id of this cluster.
     */
    private int id = 1;
    /**
     * The list of all records this cluster contains.
     * New ones will be added after a merge operation.
     */
    private List<RecordImpl> components = new ArrayList<>();
    /**
     * The median frames of this cluster.
     * A median value for each attribute for each frame of each component calculated and stored here.
     * These median frames are also used to visualize clusters.
     */
    private List<FrameImpl> medianFrames;
    /**
     * This attributes shows if this cluster should be considered in upcoming clustering steps.
     */
    private boolean consider;
    /**
     * The used metric is stored in the cluster,
     * because it will also be used to calculate the median frames.
     */
    private Dtw dtw;

    public ClusterImpl(int id, RecordImpl component, Dtw dtw) {
        this.id = id;
        this.components.add(component);
        this.medianFrames = component.getFrames();
        this.consider = true;
        this.dtw = dtw;
        component.setConsider(false);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<RecordImpl> getComponents() {
        return components;
    }

    @Override
    public List<FrameImpl> getMedianFrames() {
        return medianFrames;
    }

    @Override
    public boolean isConsider() {
        return consider;
    }

    @Override
    public void setConsider(boolean consider) {
        this.consider = consider;
    }

    /**
     * This method merges another cluster into this one.
     * The consider value of the other cluster will be set to false.
     *
     * @param clusterImpl the cluster you want to merge into this one.
     */
    @Override
    public void mergeWithCluster(ClusterImpl clusterImpl) {
        this.components = Stream.of(this.components, clusterImpl.components)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList());
        clusterImpl.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, clusterImpl);
    }

    /**
     * This method merges another record into this one.
     * The consider value of the other record will be set to false.
     *
     * @param recordImpl the record you want to merge into this one.
     */
    @Override
    public void mergeWithRecord(RecordImpl recordImpl) {
        this.components.add(recordImpl);
        recordImpl.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, recordImpl);
    }

    /**
     * This method can be used to compare two clustersImpls.
     *
     * @param otherClusterImpl the cluster you want to compare this one to.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(ClusterImpl otherClusterImpl) {
        return Integer.compare(this.id, otherClusterImpl.getId());
    }
}
