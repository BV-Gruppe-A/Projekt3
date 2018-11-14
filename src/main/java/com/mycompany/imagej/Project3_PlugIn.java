package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Project3_PlugIn implements PlugInFilter {
    
	final int BLACK = 0;
	final int WHITE = 255;
	
    final int COLOR_MIN_DEFAULT = BLACK;
    final int COLOR_MAX_DEFAULT = WHITE;
    final int PERCENTAGE_DEFAULT = 1;
    
    int color_min, color_max;
	int percentage;
	
	int[] histogram;
    
    @Override    
    public int setup(String args, ImagePlus im) {  
    	// this plugin accepts 8-bit grayscale images
        return DOES_8G; 
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        //TODO do project
    }
}
