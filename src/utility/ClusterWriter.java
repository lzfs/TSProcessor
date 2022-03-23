package utility;

import model.Cluster;
import model.KinectRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterWriter implements Writer {

    private final static Logger LOGGER = Logger.getLogger("ClusterWriterLogger");

    @Override
    public void write(String path, List<Cluster> clusters) {
        try {
            File output = new File(path + "clusters.txt");
            output.createNewFile();
            FileWriter writer = new FileWriter(output);

            for (Cluster cluster : clusters) {
                writer.write(cluster.getId() + "\n");
                for (KinectRecord record : cluster.getComponents()) {
                    writer.write(record.getName() + "\n");
                }
                writer.write("----------------" + "\n");
            }
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex) {
            LOGGER.log(Level.INFO, "file not found");
        }
        catch (IOException ex) {
            LOGGER.log(Level.INFO, "io exception");
        }
    }
}
