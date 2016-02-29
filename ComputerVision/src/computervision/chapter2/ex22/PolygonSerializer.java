/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computervision.chapter2.ex22;

import java.awt.Polygon;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author hamilton.matos
 */
public class PolygonSerializer {

    public static List<Polygon> loadSavedPolygons() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load polygons ...");
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            FileInputStream fileInput = null;
            try {
                fileInput = new FileInputStream(chooser.getSelectedFile());
                try (ObjectInputStream in = new ObjectInputStream(fileInput)) {
                    return (List<Polygon>) in.readObject();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(DrawingPane.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Not able to load polygons.");

            } finally {
                try {
                    fileInput.close();
                } catch (IOException ex) {
                    Logger.getLogger(DrawingPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

    public static void savePolygons(List<Polygon> polygons) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save polygons ...");
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(chooser.getSelectedFile());
                try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                    out.writeObject(polygons);
                }
                System.out.println("Saving polygons...");
            } catch (Exception ex) {
                Logger.getLogger(DrawingPane.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileOut.close();
                } catch (IOException ex) {
                    Logger.getLogger(DrawingPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
