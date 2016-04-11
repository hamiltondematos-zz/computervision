/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computervision.chapter14.ex141;

import static computervision.chapter5.ex55.Exercise55.displayImage;
import static computervision.chapter5.ex55.Exercise55.mat2BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class DetectFaceDemo {

    public void run() throws IOException {

        Mat image = Imgcodecs.imread("images/equipe.jpeg");

        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("lbpcascade_frontalface.xml").getPath().substring(1));
        if (faceDetector.empty()) {
            System.out.println("No faces found.");
        }

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 255, 30)
            );
        }

        displayImage(mat2BufferedImage(image), "Detected faces");
    }
}

public class FaceDetectorOpenCV {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try {
            new DetectFaceDemo().run();
        } catch (IOException ex) {
            Logger.getLogger(FaceDetectorOpenCV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
