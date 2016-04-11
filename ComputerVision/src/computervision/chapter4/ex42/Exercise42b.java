/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computervision.chapter4.ex42;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.abst.feature.describe.ConfigBrief;
import boofcv.abst.feature.describe.DescribeRegionPoint;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.alg.feature.detect.interest.GeneralFeatureDetector;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.examples.features.ExampleAssociatePoints;
import boofcv.factory.feature.associate.FactoryAssociation;
import boofcv.factory.feature.describe.FactoryDescribeRegionPoint;
import boofcv.factory.feature.detdesc.FactoryDetectDescribe;
import boofcv.factory.feature.detect.interest.FactoryDetectPoint;
import boofcv.factory.feature.detect.interest.FactoryInterestPoint;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.ImageGray;
import java.awt.image.BufferedImage;

/**
 *
 * @author hamilton.matos
 */
public class Exercise42b {
    

    public static void main(String args[]) {

        Class imageType = GrayF32.class;

        DetectDescribePoint detDesc = createFromPremade(imageType);

        ScoreAssociation scorer = FactoryAssociation.defaultScore(detDesc.getDescriptionType());
        AssociateDescription associate = FactoryAssociation.greedy(scorer, Double.MAX_VALUE, true);

        ExampleAssociatePoints app = new ExampleAssociatePoints(detDesc, associate, imageType);

        BufferedImage imageA = UtilImageIO.loadImage("images/associate.jpg");
        BufferedImage imageB = UtilImageIO.loadImage("images/associateB.jpg");
        
        app.associate(imageA, imageB, "Harris");
    }

    /**
     * For some features, there are pre-made implementations of
     * DetectDescribePoint. This has only been done in situations where there
     * was a performance advantage or that it was a very common combination.
     */
    public static <T extends ImageGray, TD extends TupleDesc>
            DetectDescribePoint<T, TD> createFromPremade(Class<T> imageType) {
        return (DetectDescribePoint) FactoryDetectDescribe.surfStable(
                new ConfigFastHessian(1, 2, 200, 1, 9, 4, 4), null, null, imageType);
//		return (DetectDescribePoint)FactoryDetectDescribe.sift(new ConfigCompleteSift(-1,5,300));
    }

    /**
     * Any arbitrary implementation of InterestPointDetector, OrientationImage,
     * DescribeRegionPoint can be combined into DetectDescribePoint. The syntax
     * is more complex, but the end result is more flexible. This should only be
     * done if there isn't a pre-made DetectDescribePoint.
     */
    public static <T extends ImageGray, TD extends TupleDesc>
            DetectDescribePoint<T, TD> createFromComponents(Class<T> imageType) {
        // create a corner detector
        Class derivType = GImageDerivativeOps.getDerivativeType(imageType);
        GeneralFeatureDetector corner = FactoryDetectPoint.createHarris(
                new ConfigGeneralDetector(1000, 5, 1), false, derivType);
        InterestPointDetector detector = FactoryInterestPoint.wrapPoint(corner, 1, imageType, derivType);

        // describe points using BRIEF
        DescribeRegionPoint describe = FactoryDescribeRegionPoint.brief(new ConfigBrief(true), imageType);

        // Combine together.
        // NOTE: orientation will not be estimated
        return FactoryDetectDescribe.fuseTogether(detector, null, describe);
    }
}

