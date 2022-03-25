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
    /**
     * The location of the dataset.
     */
    private static String inputPath;
    /**
     * The location you want to save the results to.
     */
    private static String outputPath;
    /**
     * The separator used in the dataset to separate values.
     * e.g. "#".
     */
    private static String separator;
    /**
     * The type of dataset.
     * e.g. "kinect".
     */
    private static String datasetType;
    /**
     * The threshold for clustering.
     * If the cost is greater than this threshold the clustering will be terminated.
     */
    private static double threshold;
    /**
     * The list of attributes this dataset offers.
     */
    private static List<String> attributes;
    /**
     * The subset of attributes you want to consider for the cost calculation.
     */
    private static List<String> usedAttributes;
    /**
     * This attribute can be used to flip the visualization
     * if the x-values are mirror inverted in this dataset.
     */
    private static boolean flipVisualization;
    /**
     * The name of the parameter this dataset uses for the bodyId.
     */
    private static String bodyIdParamName;
    /**
     * The name of the distance function you want to use.
     * You can implement new ones in {@link calculating.metric.Dtw} or any other implementation you might have.
     */
    private static String distanceFunction;
    /**
     * This list contains all the records that where initially found in the data.
     */
    private static List<RecordImpl> data;
    /**
     * This list contains all the found clusters.
     */
    private static List<ClusterImpl> clusterImpls;

    public static void main(String[] args) {
        // read the config attributes
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.read(args[0]);
        inputPath = properties.getProperty("inputPath");
        outputPath = properties.getProperty("outputPath");
        separator = properties.getProperty("separator");
        datasetType = properties.getProperty("datasetType");
        threshold = Double.parseDouble(properties.getProperty("threshold"));
        attributes = Arrays.asList(properties.getProperty("attributes").split(","));
        usedAttributes = Arrays.asList(properties.getProperty("usedAttributes").split(","));
        flipVisualization = Boolean.parseBoolean(properties.getProperty("flipVisualization"));
        bodyIdParamName = properties.getProperty("bodyIdParamName");
        distanceFunction = properties.getProperty("distanceFunction");

        if (datasetType.equals("kinect")) {
            DataReader dataReader = new DataReader("D:", separator, attributes);
            data = dataReader.read(inputPath);

            HierarchicalClustering clustering = new HierarchicalClustering(data, threshold, attributes, usedAttributes, distanceFunction);
            clusterImpls = clustering.cluster();

            ClusterWriter writer = new ClusterWriter();
            writer.write(outputPath, clusterImpls);

            VisualizerImpl visualizerImpl = new VisualizerImpl(flipVisualization, bodyIdParamName);
            for (ClusterImpl clusterImpl : clusterImpls) {
                visualizerImpl.visualize(clusterImpl.getId(), outputPath, clusterImpl.getMedianFrames());
            }
        }
    }
}