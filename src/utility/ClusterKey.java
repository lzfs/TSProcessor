package utility;

import model.ClusterImpl;

/**
 * This class can be used as a key for a map.
 * It consists of two clusters and implements all necessary methods for the comparison.
 */
public class ClusterKey implements Comparable<ClusterKey> {
    /**
     * The first cluster of the key.
     */
    private ClusterImpl clusterImpl1;
    /**
     * The second cluster of the key.
     */
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

    /**
     * This method is necessary to compare two clusterKeys.
     * It will use {@link model.Cluster#compareTo(model.Cluster)} to compare them.
     *
     * @param o the other clusterKey you want to compare this one to.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
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
        return clusterImpl1.equals(key.clusterImpl1) && clusterImpl2.equals(key.clusterImpl2)
               || clusterImpl1.equals(key.clusterImpl2) && clusterImpl2.equals(key.clusterImpl1);
    }
}

