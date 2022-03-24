package model;

import calculating.metric.Dtw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterImpl implements Cluster<ClusterImpl, FrameImpl, RecordImpl> {
    private int id = 1;
    private List<RecordImpl> components = new ArrayList<>();
    private List<FrameImpl> medianFrames;
    private boolean consider;
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

    @Override
    public void mergeWithCluster(ClusterImpl clusterImpl) {
        this.components = Stream.of(this.components, clusterImpl.components)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList());
        clusterImpl.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, clusterImpl);
    }

    @Override
    public void mergeWithRecord(RecordImpl record) {
        this.components.add(record);
        record.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, record);
    }

    @Override
    public int compareTo(ClusterImpl otherClusterImpl) {
        return Integer.compare(this.id, otherClusterImpl.getId());
        // (this.id < otherCluster.getId()) ? -1 : ((this.id == otherCluster.getId()) ? 0 : 1);
    }
}
