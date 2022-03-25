package utility;

import model.FrameImpl;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Visualizer interface.
 * It can be used to visualize the running path of one person or multiple people in a record.
 * It is displayed as viewed from a bird's eye view.
 */
public class VisualizerImpl extends JComponent implements Visualizer<FrameImpl> {
    /**
     * Some datasets deliver the x-values mirror inverted.
     * With this attribute you can mirror them so that they are displayed normal.
     */
    private boolean flipVisualization = false;
    /**
     * The identifier of the bodyId in this dataset.
     */
    private String bodyIdParamName = "none";

    public VisualizerImpl(boolean flipVisualization, String bodyIdParamName) {
        this.flipVisualization = flipVisualization;
        this.bodyIdParamName = bodyIdParamName;
    }

    /**
     * This method visualizes a time-series data by using the x- and z-values.
     *
     * @param id         the id of the record. This will be the file name of the visualization.
     * @param outputPath the path of the directory you want to save the visualization in.
     * @param frames     the list of frames you want to visualize.
     */
    @Override
    public void visualize(int id, String outputPath, List<FrameImpl> frames) {
        BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image.createGraphics();

        List<String> bodyIds = new ArrayList<>();
        if (this.bodyIdParamName.equals("none")) {
            bodyIds.add("none"); // if there is no specifier for the bodyId -> treat it like there is only one person
        }
        else {
            // get all the bodyIds to display different running paths for different people
            for (FrameImpl frame : frames) {
                bodyIds.add(frame.getValue(this.bodyIdParamName));
            }
        }

        // fine stroke
        graphic.setStroke(new BasicStroke(0));
        if (flipVisualization) {
            // move the running path into the center of the image
            graphic.translate(130, 275);
        }
        else {
            // move the running path into the center of the image
            graphic.translate(110, 275);
        }
        // make the running path appear bigger
        graphic.scale(40, 40);

        // print the running path of each person
        for (String bodyId : bodyIds) {
            FrameImpl oldFrame = frames.get(0);
            for (FrameImpl frame : frames) {
                double x1 = Double.parseDouble(oldFrame.getValue("x"));
                double z1 = -Double.parseDouble(oldFrame.getValue("z"));
                double x2 = Double.parseDouble(frame.getValue("x"));
                double z2 = -Double.parseDouble(frame.getValue("z"));

                if (flipVisualization) {
                    x1 = -x1;
                    x2 = -x2;
                }

                // either there is just one person then there is nothing to consider
                // but if there are multiple people you have to print the running paths individually so that they don't get connected
                if (bodyId.equals("none") || (oldFrame.getValue(bodyIdParamName).equals(bodyId) && frame.getValue(bodyIdParamName).equals(bodyId))) {
                    Point2D point1 = new Point2D.Double(x1, z1);
                    Point2D point2 = new Point2D.Double(x2, z2);
                    Line2D line = new Line2D.Double(point1, point2);
                    graphic.draw(line);
                }
                oldFrame = frame;
            }
        }

        this.paint(graphic);
        graphic.dispose();
        try {
            ImageIO.write(image, "png", new File("D:" + outputPath + id + ".png"));
        }
        catch (IOException e) {}
    }
}
