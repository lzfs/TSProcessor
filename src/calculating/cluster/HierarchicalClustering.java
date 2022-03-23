package calculating.cluster;

import calculating.metric.DTW;
import model.Cluster;
import model.KinectRecord;

import java.util.ArrayList;
import java.util.List;

public class HierarchicalClustering implements ClusterAlgorithm {
    private static int id = 1;
    private List<KinectRecord> initialRecords;
    private double threshold;
    private List<Cluster> clusters = new ArrayList<>();

    public HierarchicalClustering(List<KinectRecord> initialRecords, double threshold) {
        this.initialRecords = initialRecords;
        this.threshold = threshold;
    }

    @Override
    public List<Cluster> cluster() {
        List<Cluster> result = new ArrayList<>();
        DTW dtw = new DTW();
        for (KinectRecord record : this.initialRecords) {
            this.clusters.add(this.recordToCluster(record));
        }
        double currentMinimumCost;
        double cost;
        int mergeCandidate1;
        int mergeCandidate2;

        do {
            // reset for next loop iteration
            currentMinimumCost = dtw.calculateCost(this.clusters.get(0).getMedianFrames(), this.clusters.get(1).getMedianFrames());
            cost = dtw.calculateCost(this.clusters.get(0).getMedianFrames(), this.clusters.get(1).getMedianFrames());
            mergeCandidate1 = 0;
            mergeCandidate2 = 1;
            for (Cluster cluster1 : this.clusters) {
                for (Cluster cluster2 : this.clusters) {
                    if (cluster1 != cluster2 && cluster1.isConsider() && cluster2.isConsider()) {
                        cost = dtw.calculateCost(cluster1.getMedianFrames(), cluster2.getMedianFrames());
                        if (cost <= currentMinimumCost) {
                            currentMinimumCost = cost;
                            mergeCandidate1 = this.clusters.indexOf(cluster1);
                            mergeCandidate2 = this.clusters.indexOf(cluster2);
                        }
                    }
                }
            }
            // merges cluster2 into cluster1 and updates the cluster list. Consider of cluster2 is set to false.
            this.clusters.get(mergeCandidate1).mergeWithCluster(this.clusters.get(mergeCandidate2));
            System.out.println(currentMinimumCost);
        } while (cost < threshold);
        for (Cluster cluster : this.clusters) {
            if (cluster.isConsider()) {
                result.add(cluster);
            }
        }
        return result;
    }

    public Cluster recordToCluster(KinectRecord record) {
        this.id += 1;
        return new Cluster(id, record);
    }

    public void addCluster(Cluster cluster) {
        this.clusters.add(cluster);
    }

    public Cluster createCluster(KinectRecord record1, KinectRecord record2) {
        return null;
    }
}
