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

public class VisualizerImpl extends JComponent implements Visualizer<FrameImpl> {
    private boolean flipVisualization = false;
    private String bodyIdParamName = "none";

    public VisualizerImpl(boolean flipVisualization, String bodyIdParamName) {
        this.flipVisualization = flipVisualization;
        this.bodyIdParamName = bodyIdParamName;
    }

    @Override
    public void visualize(int id, String outputPath, List<FrameImpl> frames) {
        BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image.createGraphics();

        List<String> bodyIds = new ArrayList<>();
        if (this.bodyIdParamName.equals("none")) {
            bodyIds.add("none"); // there is no specifier for the bodyId -> treat it like there is only one person
        }
        else {
            for (FrameImpl frame : frames) {
                bodyIds.add(frame.getValue(this.bodyIdParamName));
            }
        }

        graphic.setStroke(new BasicStroke(0));
        if (flipVisualization) {
            graphic.translate(130, 275);
        }
        else {
            graphic.translate(110, 275);
        }
        graphic.scale(40, 40);

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
