package calculating.metric;

import model.ClusterImpl;
import model.Frame;
import model.FrameImpl;
import model.RecordImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the metric interface.
 * It can be used to calculate the cost between two records by using the dynamic time warping algorithm.
 * An explanation of this algorithm can be found in my bachelors thesis.
 */
public class Dtw implements Metric<RecordImpl, FrameImpl> {
    /**
     * The list of attributes this dataset offers.
     */
    private List<String> attributes;
    /**
     * The subset of attributes you want to consider for the cost calculation.
     */
    private List<String> usedAttributes;
    /**
     * The name of the distance function you want to use.
     * You can implement new ones in {@link calculating.metric.Dtw} or any other implementation you might have.
     */
    private String distanceFunction;

    public Dtw(List<String> attributes, List<String> usedAttributes, String distanceFunction) {
        this.attributes = attributes;
        this.usedAttributes = usedAttributes;
        this.distanceFunction = distanceFunction;
    }

    /**
     * This function calculated the cost of two records.
     * It calls the {@link #calculateCost(java.util.List, java.util.List)} method.
     *
     * @param record1 the first record.
     * @param record2 the second record.
     * @return the cost of the two records.
     */
    @Override
    public double calculateCost(RecordImpl record1, RecordImpl record2) {
        return calculateCost(record1.getFrames(), record2.getFrames());
    }

    /**
     * This function calculated the cost of two lists of frames.
     *
     * @param frames1 the first frame list.
     * @param frames2 the second frame list.
     * @return the cost of the two frame lists.
     */
    @Override
    public double calculateCost(List<FrameImpl> frames1, List<FrameImpl> frames2) {
        // start with cost 0 and add the cost of each value to it
        double cost = 0;
        // calculate cost for all attributes
        for (String attribute : this.usedAttributes) {
            // build the cost matrix for this value
            double[][] dtwMatrix = buildCostMatrix(frames1, frames2, attribute);
            // calculate and add the cost of this attribute
            cost += calculatePathCost(dtwMatrix);
        }
        return cost / this.usedAttributes.size();
    }

    /**
     * Go through the cost matrix and find the best path using the dynamic time warping algorithm.
     *
     * @param dtwMatrix the matrix to go through.
     * @return the lowest found cost.
     */
    public double calculatePathCost(double[][] dtwMatrix) {
        int n = dtwMatrix.length - 1;
        int m = dtwMatrix[n - 1].length - 1;
        int pathCounter = 1;
        double cost = dtwMatrix[n][m];
        while (n != 0 && m != 0) {
            // dynamic time warping algorithm
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

    /**
     * Calculates the cost matrix of two frame list for a given attribute.
     *
     * @param frames1   the first list of frames.
     * @param frames2   the second list of frames.
     * @param attribute the attribute you want to build the cost matrix for.
     * @return
     */
    private double[][] buildCostMatrix(List<FrameImpl> frames1, List<FrameImpl> frames2, String attribute) {
        double[] s = new double[frames1.size()];
        double[] t = new double[frames2.size()];

        int counter = 0;
        for (FrameImpl frame : frames1) {
            s[counter] = Double.parseDouble(frame.getValue(attribute));
            counter += 1;
        }
        counter = 0;
        for (FrameImpl frame : frames2) {
            t[counter] = Double.parseDouble(frame.getValue(attribute));
            counter += 1;
        }

        int n = s.length;
        int m = t.length;
        // filled with zeros by default
        double[][] dtwMatrix = new double[n + 1][m + 1];

        for (int i = 0; i < dtwMatrix.length; i++) {
            for (int j = 0; j < dtwMatrix[0].length; j++) {
                // filled with infinity by definition of dtw
                dtwMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        // filled with 0 by definition of dtw
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
                }
                else {
                    cost = Math.abs(s[i - 1] - t[j - 1]);// this is the default distance function
                }
                // find the lowest of the three values and put the sum of this value and the cost in the matrix cell
                double minTmp = Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]);
                double lastMin = Math.min(minTmp, dtwMatrix[i - 1][j - 1]);
                dtwMatrix[i][j] = cost + lastMin;
            }
        }
        return dtwMatrix;
    }

    /**
     * This method calculates the median values of the frames for a specified matrix.
     * It used dtw to match these to lists before calculating the median.
     *
     * @param dtwMatrix The cost matrix for this attribute.
     * @param frames1   the first list of frames.
     * @param frames2   the second list of frames.
     * @return a list of frames that contain the median values of frames1 and frames2 combined.
     */
    public List<FrameImpl> calculateMedian(double[][] dtwMatrix, List<FrameImpl> frames1, List<FrameImpl> frames2) {
        List<FrameImpl> medianFrames = new ArrayList<>();
        int n = dtwMatrix.length - 1;
        int m = dtwMatrix[n - 1].length - 1;
        // go through matrix with dtw
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

            // default frames
            FrameImpl frame1 = frames1.get(0);
            FrameImpl frame2 = frames2.get(0);
            if (n - 1 >= 0) {
                frame1 = frames1.get(n - 1);
            }
            if (m - 1 >= 0) {
                frame2 = frames2.get(m - 1);
            }

            // create a map with the attributes for the new median frame
            Map<String, String> attributesMap = new HashMap<>();
            for (String attribute : this.attributes) {
                if (!usedAttributes.contains(attribute)) {
                    // in case of a string value: concatenate these values and separate them with a semicolon
                    attributesMap.put(attribute, frame1.getValue(attribute) + ";" + frame2.getValue(attribute));
                }
                else {
                    // in case of a numeric value: add these values and divide them by two
                    attributesMap.put(attribute, String.valueOf((Double.parseDouble(frame1.getValue(attribute)) + Double.parseDouble(frame2.getValue(attribute))) / 2));
                }
            }

            // create a new frame and add all these attributes to it and add it to the medianFrames list
            // note that it only mentions the record of frame1 as the parent record
            medianFrames.add(new FrameImpl(attributesMap, frame1.getRecord()));
        }
        // reverse the list because they are added in the opposite way
        Collections.reverse(medianFrames);
        return medianFrames;
    }

    /**
     * This is a convenience function that calls {@link #calculateMedianFrames(java.util.List, java.util.List)}.
     */
    public List<FrameImpl> calculateMedianFrames(List<FrameImpl> medianFrames, RecordImpl record) {
        return calculateMedianFrames(medianFrames, record.getFrames());
    }

    /**
     * This is a convenience function that calls {@link #calculateMedianFrames(java.util.List, java.util.List)}.
     */
    public List<FrameImpl> calculateMedianFrames(List<FrameImpl> medianFrames, ClusterImpl clusterImpl) {
        return calculateMedianFrames(medianFrames, clusterImpl.getMedianFrames());
    }

    /**
     * This function calculates the median frames.
     * It calls {@link #calculateMedian(double[][], java.util.List, java.util.List)} with the dtwMatrix made up from all attributes.
     *
     * @param frames1 the first list of frames.
     * @param frames2 the second list of frames.
     * @return a list of frames that contain the median values of frames1 and frames2 combined if using all attributes to match them.
     */
    private List<FrameImpl> calculateMedianFrames(List<FrameImpl> frames1, List<FrameImpl> frames2) {
        // there has to be at least one used attribute
        // build the cost matrix for this attribute
        double[][] dtwMatrix = buildCostMatrix(frames1, frames2, this.usedAttributes.get(0));

        // if you only want to use attribute 0 to match just comment out these for-loops
        for (int i = 1; i < this.usedAttributes.size(); i++) {
            // build the cost matrix for this attribute
            double[][] tmp = buildCostMatrix(frames1, frames2, this.usedAttributes.get(i));

            for (int r = 0; r < dtwMatrix.length - 1; r++) {
                for (int c = 0; c < dtwMatrix[r].length - 1; c++) {
                    dtwMatrix[r][c] = (dtwMatrix[r][c] + tmp[r][c]) / 2;
                }
            }
        }
        // this dtwMatrix is the combined matrix of all attributes
        // calling this function with this matrix will calculate the median based on all considered attributes
        List<FrameImpl> median = calculateMedian(dtwMatrix, frames1, frames2);
        return median;
    }
}
