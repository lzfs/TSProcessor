package calculating.metric;

import model.KinectRecord;

public interface Metric {

    double calculateCost(KinectRecord record1, KinectRecord record2);
}
