package calculating.cluster;

import calculating.metric.KinectDtw;
import model.KinectCluster;
import model.KinectRecord;
import utility.ClusterKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KinectHierarchicalClustering implements ClusterAlgorithm<KinectCluster, KinectRecord> {
    private static int id = 1;
    private List<KinectRecord> initialRecords;
    private double threshold;
    private List<KinectCluster> kinectClusters = new ArrayList<>();
    private Map<ClusterKey, Double> calculatedCost = new HashMap<>();
    private final double minConst = 1.0E-3;

    public KinectHierarchicalClustering(List<KinectRecord> initialRecords, double threshold) {
        this.initialRecords = initialRecords;
        this.threshold = threshold;
    }

    @Override
    public List<KinectCluster> cluster() {
        List<KinectCluster> result = new ArrayList<>();
        KinectDtw kinectDtw = new KinectDtw();
        for (KinectRecord record : this.initialRecords) {
            this.kinectClusters.add(this.recordToCluster(record));
        }
        double currentMinimumCost;
        double cost;
        int mergeCandidate1;
        int mergeCandidate2;

        do {
            // reset for next loop iteration
            currentMinimumCost = kinectDtw.calculateCost(this.kinectClusters.get(0).getMedianFrames(), this.kinectClusters.get(1).getMedianFrames());
            cost = kinectDtw.calculateCost(this.kinectClusters.get(0).getMedianFrames(), this.kinectClusters.get(1).getMedianFrames());
            mergeCandidate1 = 0;
            mergeCandidate2 = 1;
            for (KinectCluster kinectCluster1 : this.kinectClusters) {
                for (KinectCluster kinectCluster2 : this.kinectClusters) {
                    if (kinectCluster1 != kinectCluster2 && kinectCluster1.isConsider() && kinectCluster2.isConsider()) {
                        if (this.calculatedCost.containsKey(new ClusterKey(kinectCluster1, kinectCluster2))) {
                            cost = this.calculatedCost.get(new ClusterKey(kinectCluster1, kinectCluster2));
                        }
                        else {
                            cost = kinectDtw.calculateCost(kinectCluster1.getMedianFrames(), kinectCluster2.getMedianFrames());
                            this.calculatedCost.put(new ClusterKey(kinectCluster1, kinectCluster2), cost);
                        }
                        if (cost <= currentMinimumCost) {
                            currentMinimumCost = cost;
                            mergeCandidate1 = this.kinectClusters.indexOf(kinectCluster1);
                            mergeCandidate2 = this.kinectClusters.indexOf(kinectCluster2);
                        }
                    }
                }
            }
            if (currentMinimumCost < threshold && currentMinimumCost > this.minConst) {
                // merges cluster2 into cluster1 and updates the cluster list. Consider of cluster2 is set to false.
                this.kinectClusters.get(mergeCandidate1).mergeWithCluster(this.kinectClusters.get(mergeCandidate2));

                Map<ClusterKey, Double> calculatedCostCopy = new HashMap<>();
                calculatedCostCopy.putAll(calculatedCost);

                for (Map.Entry<ClusterKey, Double> entry : calculatedCostCopy.entrySet()) {

                    // ignore mergeCandidate2 because cluster2 consider is set to false
                    if (entry.getKey().getCluster1().getId() == mergeCandidate1) {
                        for (KinectCluster kinectCluster : kinectClusters) {
                            calculatedCost.remove(new ClusterKey(this.kinectClusters.get(mergeCandidate1), kinectCluster));
                        }
                    }
                }

                System.out.println(currentMinimumCost);
            }
        } while (currentMinimumCost < threshold && currentMinimumCost > this.minConst);
        for (KinectCluster kinectCluster : this.kinectClusters) {
            if (kinectCluster.isConsider()) {
                result.add(kinectCluster);
            }
        }
        return result;
    }

    @Override
    public KinectCluster recordToCluster(KinectRecord record) {
        this.id += 1;
        return new KinectCluster(id, record);
    }
}
