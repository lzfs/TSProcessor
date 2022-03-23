package processor;

import calculating.cluster.HierarchicalClustering;
import calculating.metric.DTW;
import model.Cluster;
import model.KinectFrame;
import model.KinectRecord;
import model.Record;
import utility.ClusterWriter;
import utility.ConfigReader;
import utility.TSReader;
import utility.Visualizer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Processor {
    private static String inputPath;
    private static String outputPath;
    private static String datasetType;
    private static double threshold;
    private static List<String> usedAttributes;
    private static List<KinectRecord> data;
    private static List<Cluster> clusters;

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.read(args[0]);
        inputPath = properties.getProperty("inputPath");
        outputPath = properties.getProperty("outputPath");
        datasetType = properties.getProperty("datasetType");
        threshold = Double.parseDouble(properties.getProperty("threshold"));
        usedAttributes = Arrays.asList(properties.getProperty("usedAttributes").split(","));

        TSReader tsReader = new TSReader();
        data = tsReader.read(inputPath);

        HierarchicalClustering clustering = new HierarchicalClustering(data, threshold);
        clusters = clustering.cluster();

        ClusterWriter writer = new ClusterWriter();
        writer.write(outputPath, clusters);

        Visualizer visualizer = new Visualizer();
        for (Cluster cluster : clusters) {
            visualizer.test(cluster.getId(), outputPath, cluster.getMedianFrames());
        }
    }
}
