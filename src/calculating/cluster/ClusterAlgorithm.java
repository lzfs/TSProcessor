package calculating.cluster;

import model.Cluster;
import model.Record;

import java.util.List;

public interface ClusterAlgorithm<C extends Cluster, R extends Record> {
    List<C> cluster();
    C recordToCluster(R record);
}
