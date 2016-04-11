/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computervision.chapter14.ex141;

import java.io.File;
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

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {

    public void run() throws IOException {
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("lbpcascade_frontalface.xml").getPath().substring(1));
        if (faceDetector.empty()) {
            System.out.println("...");
        }
        Mat image = Imgcodecs.imread("c:/open/images/equipe.jpeg");
  //      Mat image = Imgcodecs.imread(getClass().getResource("equipe.jpeg").getPath().substring(1));
        
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, 
                    new Point(rect.x, rect.y), 
                    new Point(rect.x + rect.width, rect.y + rect.height), 
                    new Scalar(0, 255, 0)
            );
        }

        // Save the visualized detection.
        String filename = "c:/open/images/equipeFaceDetection.jpg";
        File file = new File(filename);        
        
        System.out.println(String.format("Writing %s", filename));
        
        Imgcodecs.imwrite(file.getPath(), image);
        
    }
}

public class FaceDetectorOpenCV {

    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            new DetectFaceDemo().run();
        } catch (IOException ex) {
            Logger.getLogger(FaceDetectorOpenCV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
