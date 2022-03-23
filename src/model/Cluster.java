package model;

import calculating.metric.DTW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cluster {
    private int id = 1;
    private List<KinectRecord> components = new ArrayList<>();
    private List<KinectFrame> medianFrames;
    private boolean consider;
    private DTW dtw;

    public Cluster(int id, KinectRecord component) {
        this.id = id;
        this.components.add(component);
        this.medianFrames = component.getFrames();
        this.consider = true;
        this.dtw = new DTW();
        component.setConsider(false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<KinectRecord> getComponents() {
        return components;
    }

    public void setComponents(List<KinectRecord> components) {
        this.components = components;
    }

    public List<KinectFrame> getMedianFrames() {
        return medianFrames;
    }

    public void setMedianFrames(List<KinectFrame> medianFrames) {
        this.medianFrames = medianFrames;
    }

    public boolean isConsider() {
        return consider;
    }

    public void setConsider(boolean consider) {
        this.consider = consider;
    }

    public void mergeWithCluster(Cluster cluster) {
        this.components = Stream.of(this.components, cluster.components)
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList());
        cluster.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, cluster);
    }

    public void mergeWithRecord(KinectRecord record) {
        this.components.add(record);
        record.setConsider(false);
        this.medianFrames = this.dtw.calculateMedianFrames(this.medianFrames, record);
    }

    public int compareTo(Cluster otherCluster) {
        return Integer.compare(this.id, otherCluster.getId());
        // (this.id < otherCluster.getId()) ? -1 : ((this.id == otherCluster.getId()) ? 0 : 1);
    }
}
