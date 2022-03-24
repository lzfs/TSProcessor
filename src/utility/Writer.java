package utility;

import model.Cluster;

import java.util.List;

public interface Writer<C extends Cluster> {
    void write(String path, List<C> clusters);
}
