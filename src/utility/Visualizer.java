package utility;

import model.KinectFrame;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Visualizer extends JComponent {

    public void test(int id, String outputPath, List<KinectFrame> frames) {
        BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = image.createGraphics();

        graphic.setStroke(new BasicStroke(0));
        graphic.translate(130, 275);
        graphic.scale(20, 20);

        KinectFrame oldFrame = frames.get(0);
        for (KinectFrame frame : frames) {
            double x1 = -this.translate(oldFrame.getX(), 50);
            double z1 = -this.translate(oldFrame.getZ(), 50);
            double x2 = -this.translate(frame.getX(), 50);
            double z2 = -this.translate(frame.getZ(), 50);
            Point2D point1 = new Point2D.Double(x1, z1);
            Point2D point2 = new Point2D.Double(x2, z2);
            Line2D line = new Line2D.Double(point1, point2);
            graphic.draw(line);
            oldFrame = frame;
        }

        this.paint(graphic);
        graphic.dispose();
        try {
            ImageIO.write(image, "png", new File("D:" + outputPath + id + ".png"));
        }
        catch (IOException e) {}
    }

    private double translate(double value, int maxValue) {
        if (maxValue != 0) {
            return (value * 100) / maxValue;
        }
        else {
            return 0;
        }
    }
}
