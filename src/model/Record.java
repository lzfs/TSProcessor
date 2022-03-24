package model;

import java.util.List;

public interface Record<F extends Frame> {
    void addFrame(F frame);
    List<F> getFrames();
    void setFrames(List<F> frames);
    String getName();
    boolean isConsider();
    void setConsider(boolean consider);
}
