package computervision.chapter4.ex412;

import boofcv.abst.feature.detect.line.DetectLineHoughPolar;
import boofcv.factory.feature.detect.line.ConfigHoughPolar;
import boofcv.factory.feature.detect.line.FactoryDetectLineAlgs;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.feature.ImageLinePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageGray;
import georegression.struct.line.LineParametric2D_F32;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;

public class Exercise412 {

    private static final float edgeThreshold = 85;
    private static final int maxLines = 10;
    private static ListDisplayPanel listPanel = new ListDisplayPanel();

    public static void main(String[] args) {
        BufferedImage original = UtilImageIO.loadImage("images/shining-hedge.jpg");

        detectLines(original, GrayU8.class, GrayS16.class);

        ShowImages.showWindow(listPanel, "Detected Lines", true);

    }

    public static <T extends ImageGray, D extends ImageGray>
            void detectLines(BufferedImage image,
                    Class<T> imageType,
                    Class<D> derivType) {

        T input = ConvertBufferedImage.convertFromSingle(image, null, imageType);

        DetectLineHoughPolar<T, D> detector = FactoryDetectLineAlgs.houghPolar(
                new ConfigHoughPolar(3, 30, 2, Math.PI / 180, edgeThreshold, maxLines), imageType, derivType);

        List<LineParametric2D_F32> found = detector.detect(input);

        ImageLinePanel gui = new ImageLinePanel();
        gui.setBackground(image);
        gui.setLines(found);
        gui.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

        listPanel.addItem(gui, null);
    }

    public static void displayImage(Image img2) {
        ImageIcon icon = new ImageIcon(img2);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static BufferedImage mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }

    private static void display(Mat mat) {
        displayImage(mat2BufferedImage(mat));
    }
}
