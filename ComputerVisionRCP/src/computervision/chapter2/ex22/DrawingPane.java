package computervision.chapter2.ex22;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author hamilton.matos
 */
public class DrawingPane extends JComponent {

    private List<Polygon> polygons = new ArrayList<>();

    private Point startDrag;
    private Point endDrag;

    private Polygon polygonSelected = null;
    int counter = 0;

    private double matrix01 = 0;
    private double matrix10 = 0;
    private double matrix00 = 1;
    private double matrix11 = 1;
    private double matrix02 = 0;
    private double matrix12 = 0;
    private double matrix20 = 0;
    private double matrix21 = 0;
    private double matrix22 = 1;

    private Point clickedPoint;
    private int w;
    private int y;

    private String transformationTypeSelected;
    private JPopupMenu menuLoadSave;
    private JPopupMenu menuSelectTransformation;

    private MainWindowTopComponent mainJFrame;

    public DrawingPane(MainWindowTopComponent m) {
        this();
        this.mainJFrame = m;
    }

    public DrawingPane() {

        menuSelectTransformation = new JPopupMenu();
        JMenuItem translationItem = new JMenuItem("Translate");
        translationItem.addActionListener((ActionEvent e) -> {
            this.transformationTypeSelected = "translation";
        });
        menuSelectTransformation.add(translationItem);

        menuLoadSave = new JPopupMenu();
        JMenuItem load = new JMenuItem("Load shapes...");
        load.addActionListener((ActionEvent e) -> {
            this.polygons = PolygonSerializer.loadSavedPolygons();
            repaint();
        });
        menuLoadSave.add(load);
        JMenuItem save = new JMenuItem("Save shapes...");
        save.addActionListener((ActionEvent e) -> {
            PolygonSerializer.savePolygons(this.polygons);
        });
        menuLoadSave.add(save);

        //this.add(menu);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (null != polygonSelected && e.isPopupTrigger()) {
                    menuSelectTransformation.show(e.getComponent(), e.getX(), e.getY());
                    return;
                } else if (e.isPopupTrigger()) {
                    menuLoadSave.show(e.getComponent(), e.getX(), e.getY());
                    return;
                }

//                shapeSelected = null;
                polygonSelected = null;

                w = (int) (getSize().getWidth() / 2);
                y = (int) (getSize().getHeight() / 2);
                clickedPoint = new Point(e.getX(), e.getY());

//                for (Polygon shape : polygons) {
//                    if (shape.contains(new Point(e.getX() - w, y - e.getY()))) {
                polygons.stream().filter((shape) -> (shape.contains(new Point(e.getX() - w, y - e.getY())))).map((shape) -> {

                    System.out.println("Shape selected: " + polygons.indexOf(shape)
                            + ". x: " + shape.getBounds2D().getX() + ". y: " + shape.getBounds2D().getY());
                    return shape;
                }).forEach((shape) -> {

                    polygonSelected = shape;

                });

                startDrag = clickedPoint;
                endDrag = startDrag;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (null != polygonSelected && e.isPopupTrigger()) {
                    menuSelectTransformation.show(e.getComponent(), e.getX(), e.getY());
                    return;
                } else if (e.isPopupTrigger()) {
                    menuLoadSave.show(e.getComponent(), e.getX(), e.getY());
                    return;
                }

                if (null == polygonSelected) {
                    int w = (int) (getSize().getWidth() / 2);
                    int y = (int) (getSize().getHeight() / 2);
                    Rectangle2D r = makeRectangle(
                            startDrag.x - w,
                            y - startDrag.y,
                            e.getX() - w,
                            y - e.getY()
                    );

                    polygons.add(
                            new Polygon(
                                    new int[]{
                                        (startDrag.x - w),
                                        (e.getX() - w),
                                        (e.getX() - w),
                                        (startDrag.x - w)
                                    },
                                    new int[]{
                                        y - startDrag.y,
                                        y - startDrag.y,
                                        y - e.getY(),
                                        y - e.getY()
                                    },
                                    4));

                    //shapes.add(r);
                    startDrag = null;
                    endDrag = null;
                    repaint();
                }

            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                //if (null == shapeSelected) {
                if (null == polygonSelected) {
                    endDrag = new Point(
                            e.getX(),
                            e.getY()
                    );
                } else {

                    for (int i = 0; i < polygonSelected.npoints; i++) {
                        int clickTolerance = 10;
                        int xpoint = polygonSelected.xpoints[i];
                        int ypoint = polygonSelected.ypoints[i];
                        if (xpoint + clickTolerance > (clickedPoint.x - w) && xpoint - clickTolerance < (clickedPoint.x - w)
                                && ypoint + clickTolerance > (y - clickedPoint.y) && ypoint - clickTolerance < (y - clickedPoint.y)) {

                            if (transformationTypeSelected.equals("translation")) {
                                if (e.getX() - w < (clickedPoint.x - w)) {
                                    translateXDown();
                                } else {
                                    translateXUp();
                                }
                                if (e.getY() - y < (y - clickedPoint.y)) {
                                    translateYDown();
                                } else {
                                    translateYUp();
                                }
                            }

                        }
                    }

                }

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point movingPoint = new Point(
                        e.getX(),
                        e.getY()
                );

                // TODO trocar mouse pointer
//                polygons.stream().forEach((polygon) -> {
//                    for (int x : polygon.xpoints) {
//                        if (x == (int) movingPoint.getX()) {
//                            //setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
//                            System.out.println("ta na quina................................");
//                        } else {
//                            // setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//                        }
//                    }
//                });
            }

        });

    }

    private void paintBackground(Graphics2D g2) {
        double width = getSize().getWidth();

        g2.setPaint(Color.LIGHT_GRAY);
        for (int i = (int) (-1 * (width / 2)); i < getSize().width; i += 10) {
            Shape line = new Line2D.Float(i,
                    -1 * (int) getSize().getHeight(),
                    i, getSize().height / 2);
            g2.draw(line);
        }

        for (int i = -1 * getSize().height; i < getSize().height; i += 10) {
            Shape line = new Line2D.Float(
                    -1 * (int) getSize().getHeight(),
                    i,
                    getSize().width,
                    i);
            g2.draw(line);
        }

    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        AffineTransform originalTransform = g2.getTransform();
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.scale(1, -1);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2);
        Color[] colors = {Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.RED, Color.BLUE, Color.PINK, Color.GREEN, Color.ORANGE, Color.GRAY, Color.WHITE};
        int colorIndex = 0;

        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

        for (Polygon p : polygons) {
            g2.setPaint(Color.BLACK);
            g2.drawPolygon(p);
            g2.setPaint(colors[(colorIndex++) % 10]);
            g2.fill(p);
        }

        if (null != polygonSelected) {
            createPolygonMouseHandlers(g2);
        }

        if (startDrag != null && endDrag != null) {

            g2.setPaint(Color.LIGHT_GRAY);

            int w = (int) (getSize().getWidth() / 2);
            int y = (int) (getSize().getHeight() / 2);

            Shape r = makeRectangle(
                    startDrag.x - w,
                    y - startDrag.y,
                    endDrag.x - w,
                    y - endDrag.y
            );
            g2.draw(r);

        }

        g2.setTransform(originalTransform);
    }

    private void createPolygonMouseHandlers(Graphics2D g2) {
        float dash1[] = {13.0f};
        g2.setStroke(new BasicStroke(6f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, dash1, 0.0f));
        g2.setPaint(Color.green);
        g2.drawPolygon(polygonSelected);
        g2.fill(polygonSelected);
        
        g2.setStroke(new BasicStroke(5));
        g2.setPaint(Color.black);
        g2.drawOval(polygonSelected.xpoints[0] - 3,
                polygonSelected.ypoints[0] - 8,
                10, 10);
        g2.setPaint(Color.green);
        g2.fillOval(polygonSelected.xpoints[0] - 3,
                polygonSelected.ypoints[0] - 8,
                10, 10);
        
        g2.setPaint(Color.black);
        g2.drawOval(polygonSelected.xpoints[1] - 8,
                polygonSelected.ypoints[1] - 8,
                10, 10);
        g2.setPaint(Color.green);
        g2.fillOval(polygonSelected.xpoints[1] - 8,
                polygonSelected.ypoints[1] - 8,
                10, 10);
        
        g2.setPaint(Color.black);
        g2.drawOval(polygonSelected.xpoints[2] - 8,
                polygonSelected.ypoints[2] - 2,
                10, 10);
        g2.setPaint(Color.green);
        g2.fillOval(polygonSelected.xpoints[2] - 8,
                polygonSelected.ypoints[2] - 2,
                10, 10);
        
        g2.setPaint(Color.black);
        g2.drawOval(polygonSelected.xpoints[3] - 3,
                polygonSelected.ypoints[3] - 2,
                10, 10);
        g2.setPaint(Color.green);
        g2.fillOval(polygonSelected.xpoints[3] - 3,
                polygonSelected.ypoints[3] - 2,
                10, 10);
    }

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public void recalculatePosition() {

        if (null == polygonSelected) {
            return;
        }

        int w = (int) (getSize().getWidth() / 2);
        int y = (int) (getSize().getHeight() / 2);

//        double ax = shapeSelected.getX();
//        double bx = shapeSelected.getX() + shapeSelected.getWidth();
//        double cx = shapeSelected.getX() + shapeSelected.getWidth();
//        double dx = shapeSelected.getX();
//        double ay = shapeSelected.getY();
//        double by = shapeSelected.getY();
//        double cy = shapeSelected.getY() + shapeSelected.getHeight();
//        double dy = shapeSelected.getY() + shapeSelected.getHeight();
        double ax = polygonSelected.getBounds2D().getX();
        double bx = polygonSelected.getBounds2D().getX() + polygonSelected.getBounds2D().getWidth();
        double cx = polygonSelected.getBounds2D().getX() + polygonSelected.getBounds2D().getWidth();
        double dx = polygonSelected.getBounds2D().getX();
        double ay = polygonSelected.getBounds2D().getY();
        double by = polygonSelected.getBounds2D().getY();
        double cy = polygonSelected.getBounds2D().getY() + polygonSelected.getBounds2D().getHeight();
        double dy = polygonSelected.getBounds2D().getY() + polygonSelected.getBounds2D().getHeight();

        Mat result = multiplyMatrices(new double[]{
            //            0,
            //            100,
            //            100,
            //            0,
            //            0,
            //            0,
            //            100,
            //            100
            ax,
            bx,
            cx,
            dx,
            ay,
            by,
            cy,
            dy, 1, 1, 1, 1
        }, new double[]{
            this.matrix00, this.matrix01, this.matrix02,
            this.matrix10, this.matrix11, this.matrix12,
            this.matrix20, this.matrix21, this.matrix22

//            1,
//            0,
//            0,
//            1
        });

        double axR = result.get(0, 0)[0];
        double ayR = result.get(1, 0)[0];
        double bxR = result.get(0, 1)[0];
        double byR = result.get(1, 1)[0];
        double cxR = result.get(0, 2)[0];
        double cyR = result.get(1, 2)[0];
        double dxR = result.get(0, 3)[0];
        double dyR = result.get(1, 3)[0];

        double xResult = axR;
        double yResult = ayR;
        double resultWidth = Math.abs(cxR - axR);
        double resultHeight = Math.abs(dyR - byR);

        System.out.println("Calculando sobre: "
                + "[" + axR + "  " + ayR + "]"
                + "[" + bxR + "  " + byR + "]"
                + "[" + cxR + "  " + cyR + "]"
                + "[" + dxR + "  " + dyR + "]"
        );

        System.out.println("Resultado: "
                + "[" + xResult + "  " + yResult + "]"
                + "[" + (xResult + resultWidth) + "  " + yResult + "]"
                + "[" + (xResult + resultWidth) + "  " + (yResult + resultHeight) + "]"
                + "[" + xResult + "  " + (yResult + resultHeight) + "]"
        );

        if (null != this.mainJFrame) {
            this.mainJFrame.updateResultValues(axR, ayR, bxR, byR, cxR, cyR, dxR, dyR);
        }

        System.err.println(result.dump());

        this.polygonSelected.invalidate();
        this.polygonSelected.xpoints = new int[]{
            (int) axR,
            (int) bxR,
            (int) cxR,
            (int) dxR
        };
        this.polygonSelected.ypoints = new int[]{
            (int) ayR,
            (int) byR,
            (int) cyR,
            (int) dyR
        };

        repaint();

        resetMatrixValues();
    }

    private void resetMatrixValues() {
        setMatrix00(1);
        setMatrix01(0);
        setMatrix02(0);
        setMatrix10(0);
        setMatrix11(1);
        setMatrix12(0);
        setMatrix20(0);
        setMatrix21(0);
        setMatrix22(1);
    }

    public Mat multiplyMatrices(double[] matrixArray, double[] multiplicationMatrix) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat matrixA = new Mat(3, 4, CvType.CV_32F);
        matrixA.put(0, 0, matrixArray);
        Mat matrixB = new Mat(3, 3, CvType.CV_32F);
        matrixB.put(0, 0, multiplicationMatrix);
        Mat result = new Mat();
        Core.gemm(matrixB, matrixA, 1, new Mat(), 0, result);

        return result;
    }

    public double getMatrix01() {
        return matrix01;
    }

    public void setMatrix01(double matrix01) {
        this.matrix01 = matrix01;
    }

    public double getMatrix10() {
        return matrix10;
    }

    public void setMatrix10(double matrix10) {
        this.matrix10 = matrix10;
    }

    public double getMatrix00() {
        return matrix00;
    }

    public void setMatrix00(double matrix00) {
        this.matrix00 = matrix00;
    }

    public double getMatrix11() {
        return matrix11;
    }

    public void setMatrix11(double matrix11) {
        this.matrix11 = matrix11;
    }

    public void rotateClockwise(double i) {
        setMatrix00(Math.cos(i));
        setMatrix01(Math.sin(i) * -1);
        setMatrix10(Math.sin(i));
        setMatrix11(Math.cos(i));
        recalculatePosition();
    }

    public void reflectionX() {
        setMatrix00(1);
        setMatrix01(0);
        setMatrix10(0);
        setMatrix11(-1);
        recalculatePosition();
    }

    public void reflectionY() {
        setMatrix00(-1);
        setMatrix01(0);
        setMatrix10(0);
        setMatrix11(1);
        recalculatePosition();
    }

    public void scaleUp() {
        double r = this.getMatrix00() * 1.01;
        double r2 = this.getMatrix11() * 1.01;
        setMatrix00(r);
        setMatrix01(0);
        setMatrix10(0);
        setMatrix11(r2);
        recalculatePosition();

    }

    public void scaleDown() {
        double r = this.getMatrix00() / 1.01;
        double r2 = this.getMatrix11() / 1.01;
        setMatrix00(r);
        setMatrix01(0);
        setMatrix10(0);
        setMatrix11(r2);
        recalculatePosition();

    }

    public void translateXDown() {
        double v = getMatrix02() - 0.1;
        setMatrix02(v);
        recalculatePosition();
        setMatrix02(0);
    }

    public void translateXUp() {
        double v = getMatrix02() + 0.1;
        setMatrix02(v);
        recalculatePosition();
        setMatrix02(0);
    }

    public void translateYDown() {
        double v = getMatrix12() - 10;
        setMatrix12(v);
        recalculatePosition();
        setMatrix12(0);
    }

    public void translateYUp() {
        double v = getMatrix12() + 1;
        setMatrix12(v);
        recalculatePosition();
        setMatrix12(0);
    }

    public void reset() {
        setMatrix00(1);
        setMatrix01(0);
        setMatrix10(0);
        setMatrix11(1);
        recalculatePosition();
    }

    public void shearXPlus() {
        setMatrix01(getMatrix01() + 0.025);
        recalculatePosition();
    }

    public void shearXMinus() {
        setMatrix01(getMatrix01() - 0.025);
        recalculatePosition();
    }

    public void shearYPlus() {
        setMatrix10(getMatrix10() - 0.025);
        recalculatePosition();
    }

    public void shearYMinus() {
        setMatrix10(getMatrix10() + 0.025);
        recalculatePosition();
    }

    public double getMatrix02() {
        return matrix02;
    }

    public void setMatrix02(double matrix02) {
        this.matrix02 = matrix02;
    }

    public double getMatrix12() {
        return matrix12;
    }

    public void setMatrix12(double matrix12) {
        this.matrix12 = matrix12;
    }

    
    public void euclidean() {
        double angle = 0.5;
        double h = 0;
        double k = 0;
        setMatrix00(Math.cos(angle));
        setMatrix01(-1 * Math.sin(angle));
        setMatrix02(-1 * h);
        setMatrix10(Math.sin(angle));
        setMatrix11(Math.cos(angle));
        setMatrix12(-1 * k);
        setMatrix20(0);
        setMatrix21(0);
        setMatrix22(1);
        recalculatePosition();
    }

    public double getMatrix20() {
        return matrix20;
    }

    public void setMatrix20(double matrix20) {
        this.matrix20 = matrix20;
    }

    public double getMatrix21() {
        return matrix21;
    }

    public void setMatrix21(double matrix21) {
        this.matrix21 = matrix21;
    }

    public double getMatrix22() {
        return matrix22;
    }

    public void setMatrix22(double matrix22) {
        this.matrix22 = matrix22;
    }

    public void similarityUp() {
        this.similarityUp(1.05, 0.05, 1.05, 1.05);
    }

    public void similarityUp(double a, double b, double tx, double ty) {

        setMatrix00(a);
        setMatrix01(-1 * b);
        setMatrix02(tx);
        setMatrix10(b);
        setMatrix11(a);
        setMatrix12(ty);
        setMatrix20(0);
        setMatrix21(0);
        setMatrix22(1);
        recalculatePosition();
    }

    public void affine(double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22) {
        setMatrix20(a20);
        setMatrix21(a21);
        setMatrix22(a22);
        this.affine(a00, a01, a02, a10, a11, a12);
    }

    public void affine(double a00, double a01, double a02, double a10, double a11, double a12) {
        setMatrix00(a00);
        setMatrix01(a01);
        setMatrix02(a02);
        setMatrix10(a10);
        setMatrix11(a11);
        setMatrix12(a12);
//        setMatrix20(0);
//        setMatrix21(0);
//        setMatrix22(1);
        recalculatePosition();
    }

    public void setMainJFrame(MainWindowTopComponent m) {
        this.mainJFrame = m;
    }

    public void setMainTopComponent(MainWindowTopComponent topComponent) {
       this.mainJFrame = topComponent;
    }

}
