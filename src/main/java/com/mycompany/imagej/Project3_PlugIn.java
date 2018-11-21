package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Project3_PlugIn implements PlugInFilter {
    
	final int BLACK = 0;
	final int WHITE = 255;
	 
	// maximal vector length in the RGB color space if red = green = blue = 255
	final double MAX_VECTOR_LENGTH = Math.sqrt(3 * Math.pow(WHITE, 2));
	
	// masks to get the 8-Bit color value from the whole 32-Bit int
	final int RED_MASK = 0x00FF0000;
	final int GREEN_MASK = 0x0000FF00;
	final int BLUE_MASK = 0x000000FF;
	
	// the weights are based on the values descriped in the book on page 324
	final double WEIGHT_RED = 0.299;
	final double WEIGHT_GREEN = 0.587;
	final double WEIGHT_BLUE = 0.114;
    
    @Override    
    public int setup(String args, ImagePlus im) {  
    	// this plugin accepts RGB images
        return DOES_RGB; 
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        
        ImagePlus greyPicture;
        ByteProcessor bp;
        
        int whichMethod = (int)IJ.getNumber("Which of the two Methods should be used? (Input: 1-2)", 1);
        
        switch(whichMethod) {
        case 1:
            // Possiblity 1: Calculating the resulting grey value for every pixel
            // based on the formula from the Übung (14.11.18)
            for (int u = 0; u < M; u++) {
                for (int v = 0; v < N; v++) {
                	int color = ip.getPixel(u, v);            	
                	int new_p = calculateIntensity(getRGBValues(color));               	
            		ip.putPixel(u, v, new_p);            	
                }
            }   
            bp = ip.convertToByteProcessor(false);
            
            
            break;
        case 2:
        	// Possiblity 2: Calculating the resulting grey value for every pixel
            // based on the formula in the book (p.324)
            for (int u = 0; u < M; u++) {
                for (int v = 0; v < N; v++) {
                	int color = ip.getPixel(u, v);            	
                	int new_p = calculateGreyscale(getRGBValues(color));            	
            		ip.putPixel(u, v, new_p);            		
                }
            }  
            bp = ip.convertToByteProcessor(false);
            
            break;
            
        default:
        	IJ.showMessage("Error", "Wrong Input!\nImageJ will convert the picture for you :)"); 
			
        	// Possiblity 3: Converts the image to a byteProcessor (= no colors)
            // seen on pages 318 & 325 in the book 
        	bp = ip.convertToByteProcessor(true);
        }
        
        greyPicture = new ImagePlus("Grey Image", bp);
        // greyPicture.show();
        
        MainDialog main = new MainDialog(greyPicture);
    }

    // splits the color-value into the 3 resulting colors (RGB)
    private int[] getRGBValues(int color) {
    	int red = (color & RED_MASK) >> 16;
    	int green = (color & GREEN_MASK) >> 8;
    	int blue = color & BLUE_MASK;
    	
    	int[] rgb = {red, green, blue};
    	return rgb;
    }
    
    // calculates the grey value as a weighted sum of the rgb Values
    private int calculateGreyscale(int[] rgbValues) {
		double sumAsDecimal = WEIGHT_RED * rgbValues[0] + WEIGHT_GREEN * rgbValues[1] + WEIGHT_BLUE * rgbValues[2];
		int grey =  (int) Math.round(sumAsDecimal); 
		return makeGreyToRGBValue(grey);
    }
    
    // calculates the grey value as the length of the vector in the RGB color space
    private int calculateIntensity(int[] rgbValues) {
    	double redSquared =  Math.pow(rgbValues[0], 2);
    	double greenSquared =  Math.pow(rgbValues[1], 2);
    	double blueSquared =  Math.pow(rgbValues[2], 2);
    	
    	double intensity =  Math.sqrt(redSquared + greenSquared + blueSquared);
    	int grey = (int) Math.round((intensity * (double) WHITE) / MAX_VECTOR_LENGTH);
    	
    	return makeGreyToRGBValue(grey);
    }
    
    // saves the grey value as a valid rgb value by setting r = g = b = grey
    private int makeGreyToRGBValue(int grey) {
    	return ((grey & 0xff) << 16) | ((grey & 0xff) << 8) | grey & 0xff;
    }
    
}