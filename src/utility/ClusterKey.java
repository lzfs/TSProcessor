package utility;

import model.Cluster;

public class ClusterKey implements Comparable<ClusterKey> {
    private Cluster cluster1;
    private Cluster cluster2;

    public ClusterKey(Cluster cluster1, Cluster cluster2) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
    }

    public Cluster getCluster1() {
        return cluster1;
    }

    public Cluster getCluster2() {
        return cluster2;
    }

    @Override
    public int hashCode() {
        return cluster1.hashCode() + 31 * cluster2.hashCode();
    }

    @Override
    public int compareTo(ClusterKey o) {
        if (o == this) return 0;
        int i = cluster1.compareTo(o.cluster1);
        if (i != 0) return i;
        return cluster2.compareTo(o.cluster2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof ClusterKey)) return false;
        ClusterKey key = ClusterKey.class.cast(o);
        return cluster1.equals(key.cluster1) && cluster2.equals(key.cluster2);
    }
}

