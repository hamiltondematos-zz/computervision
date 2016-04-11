package computervision.chapter3.ex38;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.bilateralFilter;
import static org.opencv.imgproc.Imgproc.medianBlur;


/**
 *
 * @author hsmilton
 */
public class Exercise328 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat read(String file) {
        return imread(file, 1);
    }

    public static BufferedImage blur(String src, int kernelSize) {
        Mat dst = new Mat();
        medianBlur(imread(src, 1), dst, kernelSize);
        return mat2BufferedImage(dst);

    }

    public static BufferedImage bilateralFilterCV(String src, int diameter, int sigmaColor, int sigmaSpace) {
        Mat dst = new Mat();
        bilateralFilter(imread(src, 1), dst, diameter, sigmaColor, sigmaSpace);
        return mat2BufferedImage(dst);

    }

    public static void main(String[] args) {

        Mat src = imread("lena.png", 1);
        Mat dst = new Mat();

        // Apply median filter
        // src – input 1-, 3-, or 4-channel image; when ksize is 3 or 5, the image depth should be CV_8U, CV_16U, or CV_32F, for larger aperture sizes, it can only be CV_8U.
        //dst – destination array of the same size and type as src.
        //ksize – aperture linear size; it must be odd and greater than 1, for example: 3, 5, 7 ...
        medianBlur(src, dst, 5);

        displayImage(mat2BufferedImage(src));
        displayImage(mat2BufferedImage(dst));

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
}
