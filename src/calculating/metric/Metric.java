package calculating.metric;

import model.Frame;
import model.Record;

import java.util.List;

/**
 * This interface should be implemented by all metric classes.
 *
 * @param <R> the type of records this metric will be working with.
 */
public interface Metric<R extends Record, F extends Frame> {
    /**
     * This method calculated the cost between two records.
     *
     * @param record1 the first record.
     * @param record2 the second record.
     * @return the calculated cost of these records.
     */
    double calculateCost(R record1, R record2);

    /**
     * This method calculated the cost between two frame lists.
     *
     * @param frames1 the first frames list.
     * @param frames2 the second frames list.
     * @return the calculated cost of these lists.
     */
    double calculateCost(List<F> frames1, List<F> frames2);
}
