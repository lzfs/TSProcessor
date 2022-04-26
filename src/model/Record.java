package model;

import java.util.List;

/**
 * This interface should be implemented by all record classes.
 *
 * @param <F> the type of frames the record contains.
 */
public interface Record<F extends Frame> {
    /**
     * This method adds a frame to the frame list of the record.
     *
     * @param frame the frame you want to add.
     */
    void addFrame(F frame);

    List<F> getFrames();

    void setFrames(List<F> frames);

    String getName();

    /**
     * @return if this records should be considered in upcoming clustering steps.
     */
    boolean isConsider();

    void setConsider(boolean consider);
}
