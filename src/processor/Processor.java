package processor;

import calculating.cluster.HierarchicalClustering;
import model.ClusterImpl;
import model.RecordImpl;
import utility.ClusterWriter;
import utility.ConfigReader;
import utility.DataReader;
import utility.VisualizerImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This is the main class of this project.
 * Start the {@link #main(String[])} method to evaluate a dataset.
 * Pass the path of the config-file as the start argument.
 * The processor will read and save all these attributes accordingly.
 * After that it will start the processing by executing the following tasks:
 * 1. Read all the data.
 * 2. Cluster the data.
 * 3. Write the found clusters to a file.
 * 4. Visualize the clusters.
 */
public class Processor {
    /*
     * The config-file should contain the following attributes.
     * inputPath: The location of the dataset.
     * outputPath: The location you want to save the results to.
     * Note: Make sure to specify prefixes containing ":" as a start and ignore it in the config-file.
     * separator: The separator used in the dataset to separate values. e.g.: "#".
     * datasetType: The type of dataset. e.g.: "kinect".
     * threshold: The threshold for clustering.
     * If the cost is greater than this threshold the clustering will be terminated.
     * attributes: The list of attributes this dataset offers.
     * usedAttributes: The subset of attributes you want to consider for the cost calculation.
     * flipVisualization: This attribute can be used to flip the visualization
     * if the x-values are mirror inverted in this dataset.
     * attributeForBodyIdentification: The name of the parameter this dataset uses for the identification of different bodies.
     * distanceFunction: The name of the distance function you want to use.
     * skipFrames: Whether if every third frame should be ignored to increase performance (this may lead to worse results).
     * You can implement new ones in {@link calculating.metric.Dtw} or any other implementation you might have.
     */

    /**
     * This is the main method of this project.
     * Call it to start clustering.
     *
     * @param args args[0] should contain the path of the config-file.
     *             args[1] should contain the prefix for the paths if necessary. e.g.: "D:" (colons cannot be used in config-file).
     */
    public static void main(String[] args) {
        // read the config attributes
        ConfigReader configReader = new ConfigReader();
        if (args[0] == null) {
            throw new IllegalArgumentException("Please specify the path of the config-file as the first start argument.");
        }
        Properties properties = configReader.read(args[0]);
        String prefix = "";
        if (args[1] != null) {
            prefix = args[1];
        }
        // The location of the dataset.
        String inputPath = properties.getProperty("inputPath");

        // The location you want to save the results to.
        String outputPath = properties.getProperty("outputPath");

        // The separator used in the dataset to separate values.
        // e.g. "#".
        String separator = properties.getProperty("separator");

        // The type of dataset.
        // e.g. "kinect".
        String datasetType = properties.getProperty("datasetType");

        // The threshold for clustering.
        // If the cost is greater than this threshold the clustering will be terminated.
        double threshold = Double.parseDouble(properties.getProperty("threshold"));

        // The list of attributes this dataset offers.
        List<String> attributes = Arrays.asList(properties.getProperty("attributes").split(","));

        // The subset of attributes you want to consider for the cost calculation.
        List<String> usedAttributes = Arrays.asList(properties.getProperty("usedAttributes").split(","));

        // This attribute can be used to flip the visualization
        // if the x-values are mirror inverted in this dataset.

        boolean flipVisualization = Boolean.parseBoolean(properties.getProperty("flipVisualization"));

        // The name of the parameter this dataset uses for the identification of different bodies.
        String attributeForBodyIdentification = properties.getProperty("attributeForBodyIdentification");

        // The name of the distance function you want to use.
        // You can implement new ones in {@link calculating.metric.Dtw} or any other implementation you might have.
        String distanceFunction = properties.getProperty("distanceFunction");

        // Whether if every third frame should be ignored to increase performance.
        boolean skipFrames = Boolean.parseBoolean(properties.getProperty("skipFrames"));

        if (datasetType.equals("kinect")) {
            DataReader dataReader = new DataReader(prefix, separator, attributes, skipFrames);

            // This list contains all the records that where initially found in the data.
            List<RecordImpl> data = dataReader.read(inputPath);

            HierarchicalClustering clustering = new HierarchicalClustering(data, threshold, attributes, usedAttributes, distanceFunction);

            // This list contains all the found clusters.
            List<ClusterImpl> clusterImpls = clustering.cluster();

            ClusterWriter writer = new ClusterWriter(datasetType, threshold, usedAttributes, distanceFunction, skipFrames);
            writer.write(outputPath, clusterImpls);

            VisualizerImpl visualizerImpl = new VisualizerImpl(flipVisualization, attributeForBodyIdentification, prefix);
            for (ClusterImpl clusterImpl : clusterImpls) {
                // use this visualize method if you want the visualization of the median of the cluster instead of the visualization for each record
                // visualizerImpl.visualize(clusterImpl.getId(), outputPath, clusterImpl.getMedianFrames());
                visualizerImpl.visualize(clusterImpl.getId(), outputPath, clusterImpl);
            }
        }
    }
}