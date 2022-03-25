package calculating.cluster;

import model.Cluster;
import model.Record;

import java.util.List;

/**
 * This interface should be implemented by all clusterAlgorithm classes.
 *
 * @param <C>the type of clusters this clustering algorithm will be working with.
 * @param <R>    the type of records this clustering algorithm will be working with.
 */
public interface ClusterAlgorithm<C extends Cluster, R extends Record> {
    /**
     * This method can be used to cluster a set of records.
     *
     * @return the list of found clusters.
     */
    List<C> cluster();

    /**
     * This method can transform a record into a cluster.
     * This is necessary because all clusters are initialized with one record at the beginning.
     *
     * @param record the record you want to transform.
     * @return the newly created cluster.
     */
    C recordToCluster(R record);
}
