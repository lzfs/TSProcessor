package calculating.metric;

import model.Record;

/**
 * This interface should be implemented by all metric classes.
 *
 * @param <R> the type of records this metric will be working with.
 */
public interface Metric<R extends Record> {
    /**
     * This method calculated the cost between two records.
     *
     * @param record1 the first record.
     * @param record2 the second record.
     * @return the calculated cost of these records.
     */
    double calculateCost(R record1, R record2);
}
