package utility;

import model.ClusterImpl;
import model.Record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the writer interface.
 * It can be used to write a list of clusters to a specified file.
 * It starts each entry with the index of the cluster,
 * followed by all its components.
 * The clusters are separated from each other with a horizontal line.
 */
public class ClusterWriter implements Writer<ClusterImpl> {

    private final static Logger LOGGER = Logger.getLogger("ClusterWriterLogger");
    /**
     * The type of dataset. Just used to write this information to the output file.
     */
    private String datasetType;
    /**
     * The threshold for clustering. Just used to write this information to the output file.
     */
    private double threshold;
    /**
     * The list of attributes this dataset offers. Just used to write this information to the output file.
     */
    private List<String> usedAttributes;
    /**
     * The name of the distance function you want to use. Just used to write this information to the output file.
     */
    private String distanceFunction;

    /**
     * Whether if every second frame should be ignored to increase performance. Just used to write this information to the output file.
     */
    private boolean skipFrames;

    public ClusterWriter(String datasetType, double threshold, List<String> usedAttributes, String distanceFunction, boolean skipFrames) {
        this.datasetType = datasetType;
        this.threshold = threshold;
        this.usedAttributes = usedAttributes;
        this.distanceFunction = distanceFunction;
        this.skipFrames = skipFrames;
    }

    /**
     * This method writes the clusters to the file.
     *
     * @param path         the destination path of the clusters text-file.
     * @param clusterImpls the list of the clusters you want to write.
     */
    @Override
    public void write(String path, List<ClusterImpl> clusterImpls) {
        try {
            File output = new File(path + "clusters.txt");
            output.createNewFile();
            FileWriter writer = new FileWriter(output);
            writer.write("configuration: \n");
            writer.write("datasetType: " + datasetType + "\n" +
                         "threshold: " + threshold + "\n" + "usedAttributes: ");
            for (String attribute : usedAttributes) {
                writer.write(attribute + " ");
            }
            writer.write("\n");
            writer.write("distanceFunction: " + distanceFunction + "\n" + "skipFrames: " + skipFrames);
            writer.write("\n\n");

            for (ClusterImpl clusterImpl : clusterImpls) {
                writer.write(clusterImpl.getId() + "\n");
                for (Record record : clusterImpl.getComponents()) {
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
