/*
 * Copyright (c) 2011-2016, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package computervision.chapter7.ex72;

import static boofcv.examples.stereo.ExampleFundamentalMatrix.computeMatches;
import static boofcv.examples.stereo.ExampleFundamentalMatrix.robustFundamental;
import static boofcv.examples.stereo.ExampleFundamentalMatrix.simpleFundamental;
import boofcv.gui.feature.AssociationPanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.geo.AssociatedPair;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.ejml.data.DenseMatrix64F;


public class Exercise72 {

    public static void main(String args[]) {

        BufferedImage imageLeft = UtilImageIO.loadImage("images/left4.jpg");
        BufferedImage imageRight = UtilImageIO.loadImage("images/right4.jpg");

        List<AssociatedPair> matches = computeMatches(imageLeft, imageRight);

        // Fundamental matrix
        DenseMatrix64F F;

        List<AssociatedPair> inliers = new ArrayList<>();

        F = robustFundamental(matches, inliers);
        System.out.println("Robust");
        F.print();

        F = simpleFundamental(matches);
        System.out.println("Simple");
        F.print();

        AssociationPanel panel = new AssociationPanel(20);
        panel.setAssociation(inliers);
        panel.setImages(imageLeft, imageRight);

        ShowImages.showWindow(panel, "Inlier Pairs");
    }


}
