package calculating.metric;

import model.KinectCluster;
import model.KinectFrame;
import model.KinectRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KinectDtw implements Metric<KinectRecord> {
    private List<String> usedAttributes;
    private String distanceFunction;

    public KinectDtw(List<String> usedAttributes, String distanceFunction) {
        this.usedAttributes = usedAttributes;
        this.distanceFunction = distanceFunction;
    }

    @Override
    public double calculateCost(KinectRecord record1, KinectRecord record2) {
        double cost = 0;
        for (String attribute : this.usedAttributes) {
            double[][] dtwMatrix = buildCostMatrix(record1.getFrames(), record2.getFrames(), attribute);
            cost += calculatePathCost(dtwMatrix);
        }

        return cost / this.usedAttributes.size();
    }

    public double calculateCost(List<KinectFrame> frames1, List<KinectFrame> frames2) {
        double cost = 0;
        for (String attribute : this.usedAttributes) {
            double[][] dtwMatrix = buildCostMatrix(frames1, frames2, attribute);
            cost += calculatePathCost(dtwMatrix);
        }

        return cost / this.usedAttributes.size();
    }

    public double calculatePathCost(double[][] dtwMatrix) {
        int n = dtwMatrix.length - 1;
        int m = dtwMatrix[n - 1].length - 1;
        int pathCounter = 1;
        double cost = dtwMatrix[n][m];
        while (n != 0 && m != 0) {
            double minTmp = Math.min(dtwMatrix[n - 1][m], dtwMatrix[n][m - 1]);
            double lastMin = Math.min(minTmp, dtwMatrix[n - 1][m - 1]);
            cost += lastMin;
            if (lastMin == dtwMatrix[n - 1][m - 1]) {
                n = n - 1;
                m = m - 1;
            }
            else if (lastMin == dtwMatrix[n - 1][m]) {
                n = n - 1;
            }
            else if (lastMin == dtwMatrix[n][m - 1]) {
                m = m - 1;
            }
            pathCounter += 1;
        }
        return cost / pathCounter;
    }

    private double[][] buildCostMatrix(List<KinectFrame> frames1, List<KinectFrame> frames2, String attribute) {
        double[] s = new double[frames1.size()];
        double[] t = new double[frames2.size()];

        int counter = 0;
        for (KinectFrame frame : frames1) {
            s[counter] = Double.parseDouble(frame.getValue(attribute));
            counter += 1;
        }
        counter = 0;
        for (KinectFrame frame : frames2) {
            t[counter] = Double.parseDouble(frame.getValue(attribute));
            counter += 1;
        }

        int n = s.length;
        int m = t.length;
        // filled with zeros by default
        double[][] dtwMatrix = new double[n + 1][m + 1];

        for (int i = 0; i < dtwMatrix.length; i++) {
            for (int j = 0; j < dtwMatrix[0].length; j++) {
                dtwMatrix[i][j] = Double.POSITIVE_INFINITY; // use as infinity
            }
        }
        dtwMatrix[0][0] = 0;

        for (int i = 1; i < dtwMatrix.length; i++) {
            for (int j = 1; j < dtwMatrix[0].length; j++) {
                /*
                you can use any distance function here
                just add another else-if case with your function and put in into the config-file
                 */
                double cost;
                if (this.distanceFunction.equals("other")) {
                    cost = 0; // cost = someOtherFunction(s[i - 1], t[j - 1]);
                } else {
                    cost = Math.abs(s[i - 1] - t[j - 1]);// this is the default distance function
                }
                double minTmp = Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]);
                double lastMin = Math.min(minTmp, dtwMatrix[i - 1][j - 1]);
                dtwMatrix[i][j] = cost + lastMin;
            }
        }
        return dtwMatrix;
    }

    public List<KinectFrame> calculateMedian(double[][] dtwMatrix, List<KinectFrame> frames1, List<KinectFrame> frames2) {
        List<KinectFrame> medianFrames = new ArrayList<>();
        int n = dtwMatrix.length - 1;
        int m = dtwMatrix[n - 1].length - 1;
        while (n > 0 && m > 0) {
            double minTmp = Math.min(dtwMatrix[n - 1][m], dtwMatrix[n][m - 1]);
            double lastMin = Math.min(minTmp, dtwMatrix[n - 1][m - 1]);
            if (lastMin == dtwMatrix[n - 1][m - 1]) {
                n = n - 1;
                m = m - 1;
            }
            else if (lastMin == dtwMatrix[n - 1][m]) {
                n = n - 1;
            }
            else if (lastMin == dtwMatrix[n][m - 1]) {
                m = m - 1;
            }

            // default
            KinectFrame frame1 = frames1.get(0);
            KinectFrame frame2 = frames2.get(0);
            if (n - 1 >= 0) {
                frame1 = frames1.get(n - 1);
            }
            if (m - 1 >= 0) {
                frame2 = frames2.get(m - 1);
            }

            medianFrames.add(new KinectFrame(frame1.getValue("timestamp") + ";" + frame2.getValue("timestamp"),
                                             frame1.getValue("kinectId") + ";" + frame2.getValue("kinectId"),
                                             frame1.getValue("recordId") + ";" + frame2.getValue("recordId"),
                                             String.valueOf((Double.parseDouble(frame1.getValue("x")) + Double.parseDouble(frame2.getValue("x"))) / 2),
                                             String.valueOf((Double.parseDouble(frame1.getValue("z")) + Double.parseDouble(frame2.getValue("z"))) / 2),
                                             frame1.getValue("engaged") + ";" + frame2.getValue("engaged"),
                                             frame1.getRecord()));
            // TODO should not just contain record from frame1
        }
        Collections.reverse(medianFrames);
        return medianFrames;
    }

    public List<KinectFrame> calculateMedianFrames(List<KinectFrame> medianFrames, KinectRecord record) {
        return calculateMedianFrames(medianFrames, record.getFrames());
    }

    public List<KinectFrame> calculateMedianFrames(List<KinectFrame> medianFrames, KinectCluster kinectCluster) {
        return calculateMedianFrames(medianFrames, kinectCluster.getMedianFrames());
    }

    private List<KinectFrame> calculateMedianFrames(List<KinectFrame> frames1, List<KinectFrame> frames2) {
        double[][] dtwMatrix = buildCostMatrix(frames1, frames2, this.usedAttributes.get(0)); // there has to be at least one used attribute

        // use only attribute 0 to match by commenting out these for-loops
        for (int i = 1; i < this.usedAttributes.size(); i++) {
            double[][] tmp = buildCostMatrix(frames1, frames2, this.usedAttributes.get(i));

            for (int r = 0; r < dtwMatrix.length - 1; r++) {
                for (int c = 0; c < dtwMatrix[r].length - 1; c++) {
                    dtwMatrix[r][c] = (dtwMatrix[r][c] + tmp[r][c]) / 2;
                }
            }
        }

        List<KinectFrame> median = calculateMedian(dtwMatrix, frames1, frames2);

        return median;
    }
}
