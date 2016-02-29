package computervision.chapter2.ex26;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;
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
 * @author hamilton.matos
 */
public class MainWindow26 extends javax.swing.JFrame {

    List<String> images = new ArrayList<>();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Creates new form MainWindow26
     */
    public MainWindow26() {
        initComponents();

    }

    public ImageIcon createImageIconFor(Mat img) {
        Imgproc.resize(img, img, new Size(320, 240));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        ImageIcon result = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);

            result = new ImageIcon(bufImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Computer Vision - Exercise 2.6");

        jScrollPane1.setBorder(null);

        jPanel1.setLayout(new java.awt.GridLayout(2, 4, 20, 20));
        jScrollPane1.setViewportView(jPanel1);

        jButton1.setText("carregar imagens");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 530, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Carregar imagens ...");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File[] selectedFiles = chooser.getSelectedFiles();
            for (File selectedFile : selectedFiles) {
                images.add(selectedFile.getName());
            }

            calculateRGBAverage(images);

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow26.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow26.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow26.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow26.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow26().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    private void calculateRGBAverage(List<String> images) {
        for (String img : images) {
            Mat rgba = Imgcodecs.imread(img);

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
            double[] averageResults = new double[3];

            mColorsRGB = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};

            DecimalFormat df = new DecimalFormat("#.00");

            for (int c = 0; c < 3; c++) {
                Imgproc.calcHist(image, mChannels[c], mMat0, hist, mHistSize, mRanges);

                System.out.println(hist.dump());
                Core.MinMaxLocResult minMaxLoc = Core.minMaxLoc(hist);

                Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
                hist.get(0, 0, mBuff);
                for (int h = 0; h < mHistSizeNum; h++) {
                    mP1.x = mP2.x = offset + (c * (mHistSizeNum + 10) + h) * thikness;
                    mP1.y = (int) (sizeRgba.height - 1);
                    mP2.y = mP1.y - 2 - (int) mBuff[h];
                    Imgproc.line(rgba, mP1, mP2, mColorsRGB[c], thikness);
                }

                double result = (minMaxLoc.maxVal - minMaxLoc.minVal) / 256;
                System.out.println("max------" + minMaxLoc.maxVal);
                System.out.println("min------" + minMaxLoc.minVal);
                System.out.println("avg------" + df.format(result));
                averageResults[c] = result;
            }

            ImageJPanel imageJPanel = new ImageJPanel();
            TitledBorder border = (TitledBorder) imageJPanel.getBorder();
            border.setTitle(img);
            Icon icon = createImageIconFor(rgba);
            imageJPanel.getjLabelImage().setIcon(icon);
            imageJPanel.getjLabelB().setText(String.valueOf(df.format(averageResults[0])));
            imageJPanel.getjLabelG().setText(String.valueOf(df.format(averageResults[1])));
            imageJPanel.getjLabelR().setText(String.valueOf(df.format(averageResults[2])));

            jPanel1.add(imageJPanel);

            revalidate();
         
        }
    }
}