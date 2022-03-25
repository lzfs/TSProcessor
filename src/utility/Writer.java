package utility;

import model.Cluster;

import java.util.List;

/**
 * This interface should be implemented by all classes that write clusters to a file.
 */
public interface Writer<C extends Cluster> {
    /**
     * This method writes a list of clusters to a file at the given location.
     *
     * @param path     the location you want to write to.
     * @param clusters the list of clusters you want to write to a file.
     */
    void write(String path, List<C> clusters);
}
