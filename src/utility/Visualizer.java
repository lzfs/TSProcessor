package utility;

import model.Frame;

import java.util.List;

/**
 * This interface should be implemented by all classes that visualize time-series data.
 */
public interface Visualizer<F extends Frame> {
    /**
     * This method visualizes a list of frames.
     * @param id the id of the record. This will be the file name of the visualization.
     * @param outputPath the path of the directory you want to save the visualization in.
     * @param frames the list of frames you want to visualize.
     */
    void visualize(int id, String outputPath, List<F> frames);
}
