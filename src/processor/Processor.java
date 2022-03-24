package processor;

import calculating.cluster.KinectHierarchicalClustering;
import model.KinectCluster;
import model.KinectRecord;
import utility.KinectClusterWriter;
import utility.ConfigReader;
import utility.KinectDataReader;
import utility.KinectVisualizer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Processor {
    private static String inputPath;
    private static String outputPath;
    private static String seperator;
    private static String datasetType;
    private static double threshold;
    private static List<String> attributes;
    private static List<String> usedAttributes;
    private static String distanceFunction;
    private static List<KinectRecord> data;
    private static List<KinectCluster> kinectClusters;

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.read(args[0]);
        inputPath = properties.getProperty("inputPath");
        outputPath = properties.getProperty("outputPath");
        seperator = properties.getProperty("seperator");
        datasetType = properties.getProperty("datasetType");
        threshold = Double.parseDouble(properties.getProperty("threshold"));
        attributes = Arrays.asList(properties.getProperty("attributes").split(","));
        usedAttributes = Arrays.asList(properties.getProperty("usedAttributes").split(","));
        distanceFunction = properties.getProperty("distanceFunction");


        if (datasetType.equals("kinect")) {
            KinectDataReader kinectDataReader = new KinectDataReader("D:", seperator, attributes);
            data = kinectDataReader.read(inputPath);

            KinectHierarchicalClustering clustering = new KinectHierarchicalClustering(data, threshold, attributes, usedAttributes, distanceFunction);
            kinectClusters = clustering.cluster();

            KinectClusterWriter writer = new KinectClusterWriter();
            writer.write(outputPath, kinectClusters);

            KinectVisualizer kinectVisualizer = new KinectVisualizer();
            for (KinectCluster kinectCluster : kinectClusters) {
                kinectVisualizer.visualize(kinectCluster.getId(), outputPath, kinectCluster.getMedianFrames());
            }
        }
    }
}
