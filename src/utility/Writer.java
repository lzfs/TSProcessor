package utility;

import model.Cluster;

import java.util.List;

public interface Writer {

    void write(String path, List<Cluster> clusters);
}
