package utility;

import model.KinectCluster;
import model.Record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KinectClusterWriter implements Writer<KinectCluster> {

    private final static Logger LOGGER = Logger.getLogger("KinectClusterWriterLogger");

    @Override
    public void write(String path, List<KinectCluster> kinectClusters) {
        try {
            File output = new File(path + "clusters.txt");
            output.createNewFile();
            FileWriter writer = new FileWriter(output);

            for (KinectCluster kinectCluster : kinectClusters) {
                writer.write(kinectCluster.getId() + "\n");
                for (Record record : kinectCluster.getComponents()) {
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
