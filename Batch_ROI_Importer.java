import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.*;
import ij.io.*;
import ij.plugin.frame.RoiManager;  // Added this import
import java.awt.*;
import java.io.*;
import java.util.*;

public class Batch_ROI_Importer implements PlugIn {
    
    public void run(String arg) {
        // Version check
        if (IJ.versionLessThan("1.54g")) {
            IJ.error("This plugin requires ImageJ 1.54g or later");
            return;
        }

        // Get current image
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.error("No image open");
            return;
        }
        
        // Open file dialog
        OpenDialog od = new OpenDialog("Choose ROI file", "");
        String directory = od.getDirectory();
        String fileName = od.getFileName();
        if (fileName == null) return;
        
        String filePath = directory + fileName;
        
        try {
            importRois(filePath, imp);
        } catch (IOException e) {
            IJ.error("Error reading file", e.getMessage());
        }
    }
    
    private void importRois(String filePath, ImagePlus imp) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        Vector<Point> points = new Vector<Point>();
        String currentRoiName = "";
        RoiManager rm = RoiManager.getInstance();
        
        if (rm == null) {
            rm = new RoiManager();
        }
        
        int roiCounter = 0;
        
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("#")) {
                    // Save previous ROI if exists
                    if (!points.isEmpty()) {
                        addRoiToManager(points, currentRoiName, rm, imp);
                        roiCounter++;
                        points.clear();
                    }
                    currentRoiName = line.substring(1);
                } else {
                    // Parse coordinates
                    String[] coords = line.split("\t");
                    if (coords.length == 2) {
                        try {
                            int x = Integer.parseInt(coords[0].trim());
                            int y = Integer.parseInt(coords[1].trim());
                            points.add(new Point(x, y));
                        } catch (NumberFormatException e) {
                            IJ.log("Skipping invalid coordinate: " + line);
                        }
                    }
                }
            }
            
            // Add last ROI if exists
            if (!points.isEmpty()) {
                addRoiToManager(points, currentRoiName, rm, imp);
                roiCounter++;
            }
            
            IJ.showStatus(roiCounter + " ROIs imported");
            
        } finally {
            br.close();
        }
    }
    
    private void addRoiToManager(Vector<Point> points, String name, RoiManager rm, ImagePlus imp) {
        if (points.size() < 3) return;
        
        int n = points.size();
        int[] xPoints = new int[n];
        int[] yPoints = new int[n];
        
        for (int i = 0; i < n; i++) {
            Point p = points.elementAt(i);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
        
        Roi roi = new PolygonRoi(xPoints, yPoints, n, Roi.POLYGON);
        roi.setName(name);
        
        // Check if ROI is within image bounds
        Rectangle bounds = roi.getBounds();
        if (bounds.x + bounds.width > imp.getWidth() || 
            bounds.y + bounds.height > imp.getHeight()) {
            IJ.log("Warning: ROI " + name + " extends beyond image boundaries");
        }
        
        rm.addRoi(roi);
    }
}