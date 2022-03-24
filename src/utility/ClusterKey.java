package utility;

import model.ClusterImpl;

public class ClusterKey implements Comparable<ClusterKey> {
    private ClusterImpl clusterImpl1;
    private ClusterImpl clusterImpl2;

    public ClusterKey(ClusterImpl clusterImpl1, ClusterImpl clusterImpl2) {
        this.clusterImpl1 = clusterImpl1;
        this.clusterImpl2 = clusterImpl2;
    }

    public ClusterImpl getCluster1() {
        return clusterImpl1;
    }

    public ClusterImpl getCluster2() {
        return clusterImpl2;
    }

    @Override
    public int hashCode() {
        return clusterImpl1.hashCode() + 31 * clusterImpl2.hashCode();
    }

    @Override
    public int compareTo(ClusterKey o) {
        if (o == this) return 0;
        int i = clusterImpl1.compareTo(o.clusterImpl1);
        if (i != 0) return i;
        return clusterImpl2.compareTo(o.clusterImpl2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof ClusterKey)) return false;
        ClusterKey key = ClusterKey.class.cast(o);
        return clusterImpl1.equals(key.clusterImpl1) && clusterImpl2.equals(key.clusterImpl2);
    }
}

