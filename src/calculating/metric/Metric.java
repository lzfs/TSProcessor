package calculating.metric;

import model.Record;

public interface Metric<R extends Record> {
    double calculateCost(R record1, R record2);
}
