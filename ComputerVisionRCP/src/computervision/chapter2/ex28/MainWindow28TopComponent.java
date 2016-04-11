/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computervision.chapter2.ex28;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.netbeans.api.settings.ConvertAsProperties;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//computervision.chapter2.ex28//MainWindow28//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MainWindow28TopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "computervision.chapter2.ex28.MainWindow28TopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MainWindow28Action",
        preferredID = "MainWindow28TopComponent"
)
@Messages({
    "CTL_MainWindow28Action=Exercise 2.8",
    "CTL_MainWindow28TopComponent=MainWindow28 Window",
    "HINT_MainWindow28TopComponent=This is a MainWindow28 window"
})
public final class MainWindow28TopComponent extends TopComponent {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public MainWindow28TopComponent() {
        initComponents();
        setName(Bundle.CTL_MainWindow28TopComponent());
        setToolTipText(Bundle.HINT_MainWindow28TopComponent());
    }

    @Override
    public boolean isShowing() {
        if (!super.isShowing() && null != myThread && null != webSource) {
            myThread.runnable = false;
            jButtonPause.setEnabled(false);
            jButtonStart.setEnabled(true);

            webSource.release();

            jPanelVideo.repaint();

            getGraphics().dispose();
        }

        return super.isShowing(); //To change body of generated methods, choose Tools | Templates.
    }

    private DaemonThread myThread = null;
    private int count = 0;
    private VideoCapture webSource = null;
    private Mat frameMatrix = new Mat();
    private Mat convertedMatrix = new Mat();
    private Mat destMatrix = new Mat();
    private MatOfByte matrixOfByte = new MatOfByte();
    private MatOfRect faceDetections = new MatOfRect();

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frameMatrix);
                            Graphics g = jPanelVideo.getGraphics();

                            Imgproc.cvtColor(frameMatrix, convertedMatrix, Imgproc.COLOR_BGR2YCrCb);
                            Core.inRange(convertedMatrix,
                                    new Scalar(0, 133, 77),
                                    new Scalar(255, 173, 127),
                                    destMatrix);

                            List<MatOfPoint> contours = new ArrayList<>();
                            Imgproc.findContours(destMatrix, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                            for (int i = 0; i < contours.size(); i++) {
                                double area = Imgproc.contourArea(contours.get(i));
                                if (area > 1000) {
                                    Imgproc.drawContours(frameMatrix, contours, i, new Scalar(140, 255, 22), 4);
                                }
                            }

                            Imgcodecs.imencode(".bmp", frameMatrix, matrixOfByte);
                            Image im = ImageIO.read(new ByteArrayInputStream(matrixOfByte.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            if (g.drawImage(buff, 0, 0, jPanelVideo.getWidth(),
                                    jPanelVideo.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (runnable == false) {
                                    System.out.println("Paused ..... ");
                                    this.wait();
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error");
                        }
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelVideo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonPause = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();

        setDisplayName(org.openide.util.NbBundle.getMessage(MainWindow28TopComponent.class, "MainWindow28TopComponent.displayName")); // NOI18N

        jPanelVideo.setBackground(new java.awt.Color(255, 255, 255));
        jPanelVideo.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MainWindow28TopComponent.class, "MainWindow28TopComponent.jPanelVideo.border.title"))); // NOI18N

        javax.swing.GroupLayout jPanelVideoLayout = new javax.swing.GroupLayout(jPanelVideo);
        jPanelVideo.setLayout(jPanelVideoLayout);
        jPanelVideoLayout.setHorizontalGroup(
            jPanelVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelVideoLayout.setVerticalGroup(
            jPanelVideoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jButtonPause, org.openide.util.NbBundle.getMessage(MainWindow28TopComponent.class, "MainWindow28TopComponent.jButtonPause.text")); // NOI18N
        jButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPauseActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPause);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonStart, org.openide.util.NbBundle.getMessage(MainWindow28TopComponent.class, "MainWindow28TopComponent.jButtonStart.text")); // NOI18N
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonStart);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addComponent(jPanelVideo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPauseActionPerformed
        myThread.runnable = false;
        jButtonPause.setEnabled(false);
        jButtonStart.setEnabled(true);

        webSource.release();

    }//GEN-LAST:event_jButtonPauseActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed

        webSource = new VideoCapture(0);
        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
        jButtonStart.setEnabled(false);
        jButtonPause.setEnabled(true);

    }//GEN-LAST:event_jButtonStartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPause;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelVideo;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
