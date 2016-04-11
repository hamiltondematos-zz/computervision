package computervision.chapter4.ex42;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author hsmilton
 */
public class Exercise42 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat read(String file) {
        return imread(file, 1);
    }

    public static void main(String[] args) {

        Mat img = imread("images/lena2.jpg", 1);
        Mat dst = new Mat();
        Imgproc.cvtColor(img, dst, Imgproc.COLOR_BGR2GRAY);

        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.FAST);
        printFeatureDetector(featureDetector, dst, img, "FAST");

        featureDetector = FeatureDetector.create(FeatureDetector.ORB);
        printFeatureDetector(featureDetector, dst, img, "ORB");

        featureDetector = FeatureDetector.create(FeatureDetector.BRISK);
        printFeatureDetector(featureDetector, dst, img, "BRISK");

        featureDetector = FeatureDetector.create(FeatureDetector.MSER);
        printFeatureDetector(featureDetector, dst, img, "MSER");

        featureDetector = FeatureDetector.create(FeatureDetector.GFTT);
        printFeatureDetector(featureDetector, dst, img, "GFTT");

        featureDetector = FeatureDetector.create(FeatureDetector.HARRIS);
        printFeatureDetector(featureDetector, dst, img, "HARRIS");

        featureDetector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        printFeatureDetector(featureDetector, dst, img, "SIMPLEBLOB");

    }

    private static void printFeatureDetector(FeatureDetector featureDetector, Mat dst, Mat img, String title) {
        MatOfKeyPoint kp = new MatOfKeyPoint();
        featureDetector.detect(dst, kp);
        Features2d.drawKeypoints(dst, kp, img);
        Imgcodecs.imwrite("keypoints.jpg", img);
        displayImage(mat2BufferedImage(img), title);
    }

    public static void displayImage(Image img2, String title) {
        ImageIcon icon = new ImageIcon(img2);
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void displayImage(Image img2) {
        displayImage(img2, "");
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
}
