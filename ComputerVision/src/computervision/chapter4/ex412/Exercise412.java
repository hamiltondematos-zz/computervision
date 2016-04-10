package computervision.chapter4.ex412;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_STANDARD;
import static org.opencv.imgproc.Imgproc.medianBlur;

/**
 *
 * @author hsmilton
 */
public class Exercise412 {

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

//    src – Source 8-bit or floating-point, 1-channel or 3-channel image.
//dst – Destination image of the same size and type as src .
//d – Diameter of each pixel neighborhood that is used during filtering. If it is non-positive, it is computed from sigmaSpace .
//sigmaColor – Filter sigma in the color space. A larger value of the parameter means that farther colors within the pixel neighborhood (see sigmaSpace ) will be mixed together, resulting in larger areas of semi-equal color.
//sigmaSpace – Filter sigma in the coordinate space. A larger value of the parameter means that farther pixels will influence each other as long as their colors are close enough (see sigmaColor ). When d>0 , it specifies the neighborhood size regardless of sigmaSpace . Otherwise, d is proportional to sigmaSpace .
    public static BufferedImage bilateralFilterCV (String src, int diameter, int sigmaColor, int sigmaSpace) {
        Mat dst = new Mat();
        Imgproc.bilateralFilter (imread(src, 1), dst, diameter, sigmaColor, sigmaSpace);
        return mat2BufferedImage(dst);

    }

    public static void main(String[] args) {

        
        
        Mat img = imread("lena.png", 1);
        Mat dst = new Mat();
        Mat cdst = new Mat();

        Imgproc.Canny(img, dst, 50, 200);
        
        Mat lines = new Mat();
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150);
        
         for( int i = 0; i < lines.rows(); i++ )
    {
//        float rho = lines[i][0], theta = lines[i][1];
//        Point pt1, pt2;
//        double a = cos(theta), b = sin(theta);
//        double x0 = a*rho, y0 = b*rho;
//        pt1.x = cvRound(x0 + 1000*(-b));
//        pt1.y = cvRound(y0 + 1000*(a));
//        pt2.x = cvRound(x0 - 1000*(-b));
//        pt2.y = cvRound(y0 - 1000*(a));
//        line( cdst, pt1, pt2, Scalar(0,0,255), 3, CV_AA);
    }
        
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
// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
// Fastest code
// The output can be assigned either to a BufferedImage or to an Image

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
