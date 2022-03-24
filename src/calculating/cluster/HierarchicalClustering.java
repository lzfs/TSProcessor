package calculating.cluster;

import calculating.metric.Dtw;
import model.ClusterImpl;
import model.RecordImpl;
import utility.ClusterKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HierarchicalClustering implements ClusterAlgorithm<ClusterImpl, RecordImpl> {
    private static int id = 1;
    private List<RecordImpl> initialRecords;
    private double threshold;
    private List<String> usedAttributes;
    private List<String> attributes;
    private List<ClusterImpl> clusterImpls = new ArrayList<>();
    private Map<ClusterKey, Double> calculatedCost = new HashMap<>();
    private final double minConst = 1.0E-3;
    private Dtw dtw;

    public HierarchicalClustering(List<RecordImpl> initialRecords, double threshold, List<String> attributes, List<String> usedAttributes, String distanceFunction) {
        this.initialRecords = initialRecords;
        this.threshold = threshold;
        this.usedAttributes = usedAttributes;
        this.attributes = attributes;
        this.dtw = new Dtw(attributes, usedAttributes, distanceFunction);
    }

    @Override
    public List<ClusterImpl> cluster() {
        List<ClusterImpl> result = new ArrayList<>();
        for (RecordImpl record : this.initialRecords) {
            this.clusterImpls.add(this.recordToCluster(record));
        }
        double currentMinimumCost;
        double cost;
        int mergeCandidate1;
        int mergeCandidate2;

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
                            currentMinimumCost = cost;
                            mergeCandidate1 = this.clusterImpls.indexOf(clusterImpl1);
                            mergeCandidate2 = this.clusterImpls.indexOf(clusterImpl2);
                        }
                    }
                }
            }
            if (currentMinimumCost < threshold && currentMinimumCost > this.minConst) {
                // merges cluster2 into cluster1 and updates the cluster list. Consider of cluster2 is set to false.
                this.clusterImpls.get(mergeCandidate1).mergeWithCluster(this.clusterImpls.get(mergeCandidate2));

                Map<ClusterKey, Double> calculatedCostCopy = new HashMap<>();
                calculatedCostCopy.putAll(calculatedCost);

                for (Map.Entry<ClusterKey, Double> entry : calculatedCostCopy.entrySet()) {

                    // ignore mergeCandidate2 because cluster2 consider is set to false
                    if (entry.getKey().getCluster1().getId() == mergeCandidate1) {
                        for (ClusterImpl clusterImpl : clusterImpls) {
                            calculatedCost.remove(new ClusterKey(this.clusterImpls.get(mergeCandidate1), clusterImpl));
                        }
                    }
                }

                System.out.println(currentMinimumCost);
            }
        } while (currentMinimumCost < threshold && currentMinimumCost > this.minConst);
        for (ClusterImpl clusterImpl : this.clusterImpls) {
            if (clusterImpl.isConsider()) {
                result.add(clusterImpl);
            }
        }
        return result;
    }

    @Override
    public ClusterImpl recordToCluster(RecordImpl record) {
        id += 1;
        return new ClusterImpl(id, record, this.dtw);
    }
}
