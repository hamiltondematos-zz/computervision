package computervision.chapter2.ex26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

import org.opencv.imgproc.Imgproc;

public class Histogram {

    public static final int BINS = 32;
    public static final float MIN_VALUE = 0.0f;
    public static final float MAX_VALUE = 255.0f;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    /* generate a 32-bar histogram for each color channel */
    public double[] extract(String path) {

        System.out.println("Path: " + path);
        Mat image = Imgcodecs.imread(path);

        return extract(image);
    }

    public double[] extract(Mat image) {
        Mat hsvImage = new Mat(image.width(), image.height(), image.type());
        Mat histHue = new Mat();
        Mat histSaturation = new Mat();

        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        List<Mat> channels = new ArrayList<Mat>();
        Core.split(hsvImage, channels);

        //Histogram for hue
        Imgproc.calcHist(Arrays.asList(new Mat[]{channels.get(0)}), new MatOfInt(0),
                new Mat(), histHue, new MatOfInt(BINS), new MatOfFloat(MIN_VALUE, MAX_VALUE));

        //Histogram for saturation
        Imgproc.calcHist(Arrays.asList(new Mat[]{channels.get(1)}), new MatOfInt(0),
                new Mat(), histSaturation, new MatOfInt(BINS), new MatOfFloat(MIN_VALUE, MAX_VALUE));

        double sum = Core.sumElems(histHue).val[0];
        double[] values = new double[histHue.height() + histSaturation.height()];
        int k = 0;
        for (int i = 0; i < histHue.height(); ++i) {
            values[k++] = histHue.get(i, 0)[0] / sum;
        }
        sum = Core.sumElems(histSaturation).val[0];
        for (int i = 0; i < histSaturation.height(); ++i) {
            values[k++] = histSaturation.get(i, 0)[0] / sum;
        }
        return values;
    }
}
