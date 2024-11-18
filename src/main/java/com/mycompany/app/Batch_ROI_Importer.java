package com.mycompany.app;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Batch_ROI_Importer implements PlugIn {
    
    public void run(String arg) {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.error("No image open", "Please open an image first before running this plugin");
            return;
        }
        OpenDialog od = new OpenDialog("Choose ROI Coordinates File", "", "");
        String directory = od.getDirectory();
        String fileName = od.getFileName();
        if (fileName == null) {
            return; 
        }
        
        String filePath = directory + fileName;
        try {
            importRois(filePath, imp);
        } catch (IOException e) {
            IJ.error("Error reading file", e.getMessage());
        }
    }
    
    private void importRois(String filePath, ImagePlus imp) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<Point> points = new ArrayList<>();
        String currentRoiName = "ROI";
        RoiManager rm = RoiManager.getInstance();
        
        if (rm == null) {
            rm = new RoiManager();
        }
        
        int roiCounter = 0;
        String line;
        
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                if (line.startsWith("#")) {
                    if (!points.isEmpty()) {
                        addRoiToManager(points, currentRoiName, rm, imp);
                        roiCounter++;
                        points.clear();
                    }
                    currentRoiName = line.substring(1).trim();
                    if (currentRoiName.isEmpty()) {
                        currentRoiName = "ROI_" + (roiCounter + 1);
                    }
                } else {
                    String[] coords = line.split("\\s+");
                    if (coords.length >= 2) {
                        try {
                            int x = Integer.parseInt(coords[0].trim());
                            int y = Integer.parseInt(coords[1].trim());
                            points.add(new Point(x, y));
                        } catch (NumberFormatException e) {
                            IJ.log("Warning: Skipping invalid coordinate line: " + line);
                        }
                    }
                }
            }
            
            if (!points.isEmpty()) {
                addRoiToManager(points, currentRoiName, rm, imp);
                roiCounter++;
            }
            
            if (roiCounter > 0) {
                IJ.showStatus(roiCounter + " ROIs imported successfully");
                rm.runCommand("Show All"); 
            } else {
                IJ.showMessage("No valid ROIs found in the file");
            }
            
        } finally {
            br.close();
        }
    }
    
    private void addRoiToManager(List<Point> points, String name, RoiManager rm, ImagePlus imp) {
        if (points.size() < 3) {
            IJ.log("Warning: Skipping ROI '" + name + "' - requires at least 3 points");
            return;
        }
        
        int n = points.size();
        int[] xPoints = new int[n];
        int[] yPoints = new int[n];
        
        for (int i = 0; i < n; i++) {
            Point p = points.get(i);
            xPoints[i] = p.x;
            yPoints[i] = p.y;
        }
        
        Roi roi = new PolygonRoi(xPoints, yPoints, n, Roi.POLYGON);
        roi.setName(name);
        
        Rectangle bounds = roi.getBounds();
        if (bounds.x < 0 || bounds.y < 0 || 
            bounds.x + bounds.width > imp.getWidth() || 
            bounds.y + bounds.height > imp.getHeight()) {
            IJ.log("Warning: ROI '" + name + "' extends beyond image boundaries");
        }
        
        rm.addRoi(roi);
    }
}