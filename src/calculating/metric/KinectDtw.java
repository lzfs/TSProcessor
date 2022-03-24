package calculating.metric;

import model.KinectCluster;
import model.KinectFrame;
import model.KinectRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KinectDtw implements Metric<KinectRecord> {

    @Override
    public double calculateCost(KinectRecord record1, KinectRecord record2) {
        double[][] dtwMatrix1 = buildCostMatrix(record1, record2, 0);
        double[][] dtwMatrix2 = buildCostMatrix(record1, record2, 1);
        double costPath1 = calculatePathCost(dtwMatrix1);
        double costPath2 = calculatePathCost(dtwMatrix2);

        return (costPath1 + costPath2) / 2;
    }

    public double calculateCost(List<KinectFrame> frames1, List<KinectFrame> frames2) {
        double[][] dtwMatrix1 = buildCostMatrix(frames1, frames2, 0);
        double[][] dtwMatrix2 = buildCostMatrix(frames1, frames2, 1);
        double costPath1 = calculatePathCost(dtwMatrix1);
        double costPath2 = calculatePathCost(dtwMatrix2);

        return (costPath1 + costPath2) / 2;
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

    private double[][] buildCostMatrix(KinectRecord record1, KinectRecord record2, int attribute) {
        List<KinectFrame> frames1 = record1.getFrames();
        List<KinectFrame> frames2 = record2.getFrames();
        double[] s = new double[frames1.size()];
        double[] t = new double[frames2.size()];

        if (attribute == 0) {
            int counter = 0;
            for (KinectFrame frame : frames1) {
                s[counter] = frame.getX();
                counter += 1;
            }
            counter = 0;
            for (KinectFrame frame : frames2) {
                t[counter] = frame.getX();
                counter += 1;
            }
        }
        else if (attribute == 1) {
            int counter = 0;
            for (KinectFrame frame : frames1) {
                s[counter] = frame.getZ();
                counter += 1;
            }
            counter = 0;
            for (KinectFrame frame : frames2) {
                t[counter] = frame.getZ();
                counter += 1;
            }
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
                double cost = Math.abs(s[i - 1] - t[j - 1]);
                double minTmp = Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]);
                double lastMin = Math.min(minTmp, dtwMatrix[i - 1][j - 1]);
                dtwMatrix[i][j] = cost + lastMin;
            }
        }
        return dtwMatrix;
    }

    private double[][] buildCostMatrix(List<KinectFrame> frames1, List<KinectFrame> frames2, int attribute) {
        double[] s = new double[frames1.size()];
        double[] t = new double[frames2.size()];

        if (attribute == 0) {
            int counter = 0;
            for (KinectFrame frame : frames1) {
                s[counter] = frame.getX();
                counter += 1;
            }
            counter = 0;
            for (KinectFrame frame : frames2) {
                t[counter] = frame.getX();
                counter += 1;
            }
        }
        else if (attribute == 1) {
            int counter = 0;
            for (KinectFrame frame : frames1) {
                s[counter] = frame.getZ();
                counter += 1;
            }
            counter = 0;
            for (KinectFrame frame : frames2) {
                t[counter] = frame.getZ();
                counter += 1;
            }
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
                double cost = Math.abs(s[i - 1] - t[j - 1]);
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

            medianFrames.add(new KinectFrame(frame1.getTimestamp() + ";" + frame2.getTimestamp(),
                                             frame1.getKinectId() + ";" + frame2.getKinectId(),
                                             frame1.getRecordId() + ";" + frame2.getRecordId(),
                                             String.valueOf((frame1.getX() + frame2.getX()) / 2),
                                             String.valueOf((frame1.getZ() + frame2.getZ()) / 2),
                                             frame1.getEngaged() + ";" + frame2.getEngaged(),
                                             frame1.getRecord()));
            // TODO should not just contain record from frame1
        }
        Collections.reverse(medianFrames);
        return medianFrames;
    }

    // need for more sophisticated approach. at the moment it calculates median bases either on x or y not on both.
    public List<KinectFrame> calculateMedianFrames(List<KinectFrame> medianFrames, KinectRecord record) {
        List<KinectFrame> frames1 = medianFrames;
        List<KinectFrame> frames2 = record.getFrames();
        // List<KinectFrame> resultFrames = new ArrayList<>();

        double[][] dtwMatrix1 = buildCostMatrix(frames1, frames2, 0);
        // double[][] dtwMatrix2 = buildCostMatrix(frames1, frames2, 1);

        List<KinectFrame> median1 = calculateMedian(dtwMatrix1, frames1, frames2);
        // List<KinectFrame> median2 = calculateMedian(dtwMatrix2, frames1, frames2);

        // for (KinectFrame f: median1) {
        //     resultFrames.add(median1 + median2);
        // }

        return median1;
    }

    public List<KinectFrame> calculateMedianFrames(List<KinectFrame> medianFrames, KinectCluster kinectCluster) {
        List<KinectFrame> frames1 = medianFrames;
        List<KinectFrame> frames2 = kinectCluster.getMedianFrames();

        double[][] dtwMatrix1 = buildCostMatrix(frames1, frames2, 0);

        List<KinectFrame> median1 = calculateMedian(dtwMatrix1, frames1, frames2);

        return median1;
    }
}
