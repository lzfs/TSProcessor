package utility;

import model.Frame;

import java.util.List;

public interface Visualizer<F extends Frame> {
    void visualize(int id, String outputPath, List<F> frames);
}
