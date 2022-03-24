package model;

import calculating.metric.KinectDtw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KinectCluster implements Cluster<KinectCluster, KinectFrame, KinectRecord> {
    private int id = 1;
    private List<KinectRecord> components = new ArrayList<>();
    private List<KinectFrame> medianFrames;
    private boolean consider;
    private KinectDtw kinectDtw;

    public KinectCluster(int id, KinectRecord component, KinectDtw kinectDtw) {
        this.id = id;
        this.components.add(component);
        this.medianFrames = component.getFrames();
        this.consider = true;
        this.kinectDtw = kinectDtw;
        component.setConsider(false);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<KinectRecord> getComponents() {
        return components;
    }

    @Override
    public List<KinectFrame> getMedianFrames() {
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
    public void mergeWithCluster(KinectCluster kinectCluster) {
        this.components = Stream.of(this.components, kinectCluster.components)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList());
        kinectCluster.setConsider(false);
        this.medianFrames = this.kinectDtw.calculateMedianFrames(this.medianFrames, kinectCluster);
    }

    @Override
    public void mergeWithRecord(KinectRecord record) {
        this.components.add(record);
        record.setConsider(false);
        this.medianFrames = this.kinectDtw.calculateMedianFrames(this.medianFrames, record);
    }

    @Override
    public int compareTo(KinectCluster otherKinectCluster) {
        return Integer.compare(this.id, otherKinectCluster.getId());
        // (this.id < otherCluster.getId()) ? -1 : ((this.id == otherCluster.getId()) ? 0 : 1);
    }
}
