package model;

/**
 * This interface should be implemented by all record classes.
 *
 * @param <R> the type of record this frames belongs to.
 */
public interface Frame<R extends Record> {
    R getRecord();

    String getValue(String key);
}
