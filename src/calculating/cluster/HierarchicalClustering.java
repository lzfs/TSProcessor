package calculating.cluster;

import calculating.metric.Dtw;
import model.ClusterImpl;
import model.RecordImpl;
import utility.ClusterKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the clusterAlgorithm interface.
 * It can be used to cluster a set of records by using the hierarchical clustering algorithm.
 * An explanation of this algorithm can be found in my bachelors thesis.
 */
public class HierarchicalClustering implements ClusterAlgorithm<ClusterImpl, RecordImpl> {
    /**
     * This id will be used to initialize new clusters.
     * If a new cluster gets initialized it will get the current value of this id.
     */
    private static int id = 1;
    /**
     * The list of records you initially pass to the algorithm.
     * It starts the clustering process with these records.
     */
    private List<RecordImpl> initialRecords;
    /**
     * The threshold for clustering.
     * If the cost is greater than this threshold the clustering will be terminated.
     */
    private double threshold;
    /**
     * The subset of attributes you want to consider for the cost calculation.
     */
    private List<String> usedAttributes;
    /**
     * The list of attributes this dataset offers.
     */
    private List<String> attributes;
    /**
     * The list of found clusters.
     * New clusters will be added to this list.
     */
    private List<ClusterImpl> clusterImpls = new ArrayList<>();
    /**
     * This map holds the cost values that have already been calculated.
     * This increases performance in further loop iterations,
     * because not all cost values have to be calculated again.
     */
    private Map<ClusterKey, Double> calculatedCost = new HashMap<>();
    /**
     * Lower bound constant.
     */
    private final double minConst = 1.0E-3;
    /**
     * The used metric is stored in the here.
     * The hierarchical clustering will use the implementation of the dynamic time warping algorithm here.
     */
    private Dtw dtw;

    public HierarchicalClustering(List<RecordImpl> initialRecords, double threshold, List<String> attributes, List<String> usedAttributes, String distanceFunction) {
        this.initialRecords = initialRecords;
        this.threshold = threshold;
        this.usedAttributes = usedAttributes;
        this.attributes = attributes;
        this.dtw = new Dtw(attributes, usedAttributes, distanceFunction);
    }

    /**
     * This method will cluster the passed records until the threshold is exceeded
     * or until no more merge operations can be performed.
     *
     * @return
     */
    @Override
    public List<ClusterImpl> cluster() {
        List<ClusterImpl> result = new ArrayList<>();
        // initialize each record as a cluster and add it to the clusters list
        for (RecordImpl record : this.initialRecords) {
            this.clusterImpls.add(this.recordToCluster(record));
        }
        double currentMinimumCost;
        double cost;
        int mergeCandidate1;
        int mergeCandidate2;

        // do at least once
        do {
            // reset for next loop iteration
            currentMinimumCost = this.dtw.calculateCost(this.clusterImpls.get(0).getMedianFrames(), this.clusterImpls.get(1).getMedianFrames());
            cost = this.dtw.calculateCost(this.clusterImpls.get(0).getMedianFrames(), this.clusterImpls.get(1).getMedianFrames());
            mergeCandidate1 = 0;
            mergeCandidate2 = 1;
            for (ClusterImpl clusterImpl1 : this.clusterImpls) {
                for (ClusterImpl clusterImpl2 : this.clusterImpls) {
                    if (clusterImpl1 != clusterImpl2 && clusterImpl1.isConsider() && clusterImpl2.isConsider()) {
                        if (this.calculatedCost.containsKey(new ClusterKey(clusterImpl1, clusterImpl2))) {
                            cost = this.calculatedCost.get(new ClusterKey(clusterImpl1, clusterImpl2));
                        }
                        else {
                            cost = this.dtw.calculateCost(clusterImpl1.getMedianFrames(), clusterImpl2.getMedianFrames());
                            this.calculatedCost.put(new ClusterKey(clusterImpl1, clusterImpl2), cost);
                        }
                        if (cost <= currentMinimumCost) {
                            // if you found a cost that is smaller than the current minimum cost this will be your new cost
                            currentMinimumCost = cost;
                            mergeCandidate1 = this.clusterImpls.indexOf(clusterImpl1);
                            mergeCandidate2 = this.clusterImpls.indexOf(clusterImpl2);
                        }
                    }
                }
            }
            // if the minimum cost is still below the threshold we can continue clustering
            if (currentMinimumCost < threshold && currentMinimumCost > this.minConst) {
                /* combining mergeCandidate1 and mergeCandidate2 has the lowest found cost
                these two should therefore be merged together
                merge cluster2 into cluster1 and update the cluster list
                consider of cluster2 is set to false */
                this.clusterImpls.get(mergeCandidate1).mergeWithCluster(this.clusterImpls.get(mergeCandidate2));

                // create copy of the map to avoid concurrentModificationException
                Map<ClusterKey, Double> calculatedCostCopy = new HashMap<>();
                calculatedCostCopy.putAll(calculatedCost);

                // remove all calculated costs from the map that contain cluster1 because it changed
                // and therefore all cost values with this cluster have to be calculated again
                for (Map.Entry<ClusterKey, Double> entry : calculatedCostCopy.entrySet()) {
                    // ignore mergeCandidate2 because the consider value of cluster2 is set to false
                    if (entry.getKey().getCluster1().getId() == mergeCandidate1) {
                        for (ClusterImpl clusterImpl : clusterImpls) {
                            calculatedCost.remove(new ClusterKey(this.clusterImpls.get(mergeCandidate1), clusterImpl));
                        }
                    }
                }
            }
        } while (currentMinimumCost < threshold && currentMinimumCost > this.minConst);
        // clean up the list of found clusters by removing all clusters that shouldn't be considered anymore
        for (ClusterImpl clusterImpl : this.clusterImpls) {
            if (clusterImpl.isConsider()) {
                result.add(clusterImpl);
            }
        }
        return result;
    }

    /**
     * This method transforms a record into a cluster.
     *
     * @param record the record you want to transform.
     * @return the newly created cluster.
     */
    @Override
    public ClusterImpl recordToCluster(RecordImpl record) {
        id += 1;
        return new ClusterImpl(id, record, this.dtw);
    }
}
