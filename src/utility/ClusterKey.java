package utility;

import model.KinectCluster;

public class ClusterKey implements Comparable<ClusterKey> {
    private KinectCluster kinectCluster1;
    private KinectCluster kinectCluster2;

    public ClusterKey(KinectCluster kinectCluster1, KinectCluster kinectCluster2) {
        this.kinectCluster1 = kinectCluster1;
        this.kinectCluster2 = kinectCluster2;
    }

    public KinectCluster getCluster1() {
        return kinectCluster1;
    }

    public KinectCluster getCluster2() {
        return kinectCluster2;
    }

    @Override
    public int hashCode() {
        return kinectCluster1.hashCode() + 31 * kinectCluster2.hashCode();
    }

    @Override
    public int compareTo(ClusterKey o) {
        if (o == this) return 0;
        int i = kinectCluster1.compareTo(o.kinectCluster1);
        if (i != 0) return i;
        return kinectCluster2.compareTo(o.kinectCluster2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof ClusterKey)) return false;
        ClusterKey key = ClusterKey.class.cast(o);
        return kinectCluster1.equals(key.kinectCluster1) && kinectCluster2.equals(key.kinectCluster2);
    }
}

