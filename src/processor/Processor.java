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

public class Processor {
    private static String inputPath;
    private static String outputPath;
    private static String separator;
    private static String datasetType;
    private static double threshold;
    private static List<String> attributes;
    private static List<String> usedAttributes;
    private static String distanceFunction;
    private static List<RecordImpl> data;
    private static List<ClusterImpl> clusterImpls;

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.read(args[0]);
        inputPath = properties.getProperty("inputPath");
        outputPath = properties.getProperty("outputPath");
        separator = properties.getProperty("separator");
        datasetType = properties.getProperty("datasetType");
        threshold = Double.parseDouble(properties.getProperty("threshold"));
        attributes = Arrays.asList(properties.getProperty("attributes").split(","));
        usedAttributes = Arrays.asList(properties.getProperty("usedAttributes").split(","));
        distanceFunction = properties.getProperty("distanceFunction");


        if (datasetType.equals("kinect")) {
            DataReader dataReader = new DataReader("D:", separator, attributes);
            data = dataReader.read(inputPath);

            HierarchicalClustering clustering = new HierarchicalClustering(data, threshold, attributes, usedAttributes, distanceFunction);
            clusterImpls = clustering.cluster();

            ClusterWriter writer = new ClusterWriter();
            writer.write(outputPath, clusterImpls);

            VisualizerImpl visualizerImpl = new VisualizerImpl();
            for (ClusterImpl clusterImpl : clusterImpls) {
                visualizerImpl.visualize(clusterImpl.getId(), outputPath, clusterImpl.getMedianFrames());
            }
        }
    }
}
