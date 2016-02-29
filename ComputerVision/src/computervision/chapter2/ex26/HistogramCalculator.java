package computervision.chapter2.ex26;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author hsmilton
 */
public class HistogramCalculator {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

//        Mat m = Imgcodecs.imread("histogram.jpg");
//         List<Mat> image = new ArrayList<Mat>();
//	image.add(m);
// 
//	MatOfInt channels = new MatOfInt(2);
// 
//	//Mat mask = Mat.zeros(3, 3, CvType.CV_8UC1);
// 
//	Mat hist = new Mat();
// 
//	MatOfInt histSize = new MatOfInt(256);
// 
//	float range[] = {0, 256};
//	MatOfFloat ranges = new MatOfFloat(range);
// 
//	Imgproc.calcHist(image, channels, new Mat(), hist, histSize, ranges);
//        
//        System.out.println(hist.dump());
        Mat rgba = Imgcodecs.imread("test200.jpg");
        List<Mat> image = new ArrayList<Mat>();
        image.add(rgba);

        Size sizeRgba = rgba.size();
        Mat hist = new Mat();
        int mHistSizeNum = 25;
        int thikness = (int) (sizeRgba.width / (mHistSizeNum + 10) / 5);
        if (thikness > 5) {
            thikness = 5;
        }
        int offset = (int) ((sizeRgba.width - (5 * mHistSizeNum + 4 * 10) * thikness) / 2);

        MatOfFloat mRanges = new MatOfFloat(0f, 256f);

        MatOfInt mHistSize = new MatOfInt(mHistSizeNum);
        Mat mMat0 = new Mat();
        MatOfInt[] mChannels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};

        float mBuff[];
        mBuff = new float[mHistSizeNum];
        Point mP1 = new Point();
        Point mP2 = new Point();
        Scalar mColorsRGB[];
        mColorsRGB = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};
        for (int c = 0; c < 3; c++) {
            Imgproc.calcHist(image, mChannels[c], mMat0, hist, mHistSize, mRanges);
            System.out.println(hist.dump());
            Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
            hist.get(0, 0, mBuff);
            for (int h = 0; h < mHistSizeNum; h++) {
                mP1.x = mP2.x = offset + (c * (mHistSizeNum + 10) + h) * thikness;
                mP1.y = (int) (sizeRgba.height - 1);
                mP2.y = mP1.y - 2 - (int) mBuff[h];
                Imgproc.line(rgba, mP1, mP2, mColorsRGB[c], thikness);
            }
        }

        showResult(rgba);
    }

    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Mat getColorHistogram(String imgLocation) {
        try {
            Mat img = new Mat();
            img = Imgcodecs.imread(imgLocation);
            List<Mat> imagesList = new ArrayList<>();
            imagesList.add(img);
            //Mat tempChannel=new Mat(new Size(3,1),org.opencv.core.CvType.
            int histSizeArray[] = {256, 256, 256};
            int channelArray[] = {0, 1, 2};
            MatOfInt channels = new MatOfInt(channelArray);
            //mChannels = new MatOfInt[] { new MatOfInt(0), new MatOfInt(1), new MatOfInt(2) };
            Mat hist = new Mat();
            MatOfInt histSize = new MatOfInt(256);
        //histSize.fromArray(histSizeArray);
            //histSize = new MatOfInt(256);
            float hrangesArray[] = {0.0f, 255.0f};
            MatOfFloat ranges = new MatOfFloat(0.0f, 255.0f);
            org.opencv.imgproc.Imgproc.calcHist(imagesList, channels, new Mat(), hist, histSize, ranges);
            return hist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
