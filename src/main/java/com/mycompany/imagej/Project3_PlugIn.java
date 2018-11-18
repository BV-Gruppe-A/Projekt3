package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.util.Random;


public class Project3_PlugIn implements PlugInFilter {
    
	final int BLACK = 0;
	final int WHITE = 255;
	
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

        int whichMethod = (int)IJ.getNumber("Which of the three Methods should be used? (Input: 1-3)", 1);
        
        switch(whichMethod) {
        case 1:
        	// Possiblity 1: Converts the image to a byteProcessor (= no colors)
            // seen on pages 318 & 325 in the book 
            ByteProcessor bp = ip.convertToByteProcessor();
            ImagePlus greyPicture = new ImagePlus("Grey Image", bp);
            greyPicture.show();
            
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
            break;
            
        case 3:
            // Possiblity 3: Calculating the resulting grey value for every pixel
            // based on the formula from the Übung (14.11.18)
            for (int u = 0; u < M; u++) {
                for (int v = 0; v < N; v++) {
                	int color = ip.getPixel(u, v);            	
                	int new_p = calculateIntensity(getRGBValues(color));            	
            		ip.putPixel(u, v, new_p);
            	}
            }            
            break;
            
        default:
        	IJ.showMessage("Error", "Wrong Input!\nPlease try again.");            	
        }
    }

    // splits the color-value into the 3 resulting colors (RGB)
    int[] getRGBValues(int color) {
    	int red = (color & RED_MASK) >> 16;
    	int green = (color & GREEN_MASK) >> 8;
    	int blue = color & BLUE_MASK;
    	
    	int[] rgb = {red, green, blue};
    	return rgb;
    }
    
    // calculates the grey value as a weighted sum of the rgb Values
    int calculateGreyscale(int[] rgbValues) {
     int grey =  (int)(WEIGHT_RED * rgbValues[0] + WEIGHT_GREEN * rgbValues[1] + WEIGHT_BLUE * rgbValues[2]); 
     return ((grey & 0xff) << 16) | ((grey & 0xff) << 8) | grey & 0xff;
    }
    
    // calculates the grey value as the length of the vector in the RGB color space
    // TODO value needs to be scaled
    int calculateIntensity(int[] rgbValues) {
    	int redSquared = (int) Math.pow(rgbValues[0], 2);
    	int greenSquared = (int) Math.pow(rgbValues[1], 2);
    	int blueSquared = (int) Math.pow(rgbValues[2], 2);
    	
    	int grey = (int) Math.round(Math.sqrt(redSquared + greenSquared + blueSquared));
    	return ((grey & 0xff) << 16) | ((grey & 0xff) << 8) | grey & 0xff;
    }
    
    double[] normHistogram(int[] histogram, int M, int N) {
    	double [] normedHist = new double[histogram.length];
    	for(int i=0;i<histogram.length;i++) {
    		normedHist[i] = histogram[i]/(double)(N*M);
    	}
    	return normedHist;
    }
    
    double mean(double[] nHist) {
    	double mean = 0;
    	for(int i=0; i < nHist.length; i++) {
    		mean+=i*nHist[i];
    	}
    	return mean;
    }

    double variance(double[] nHist, double mean) {
    	double var = 0;
    	for(int i=0; i < nHist.length; i++) {
    		var+=Math.pow(i-mean, 2)*nHist[i];
    	}
    	return var;
    }
  
    double skewness(double[] nHist, double mean, double var) {
    	double skew = 0;
    	for(int i=0; i < nHist.length; i++) {
    		skew+=Math.pow(i-mean,3)*nHist[i];
    	}
    	skew=Math.pow(Math.sqrt(var),3)*skew;
    	return skew;
    }
    
    double kurtosis(double[] nHist, double mean, double var) {
    	double kur = 0;
    	for(int i=0; i < nHist.length; i++) {
    		mean+=Math.pow(i-mean, 4)*nHist[i];
    	}
    	kur = -3 + Math.pow(var, 2) * kur;
    	return kur;
    }


	double entropy(double[] nHist) {
		double entropy = 0;
		for(int i=0; i < nHist.length; i++) {
    		entropy+=nHist[i]*Math.log10(nHist[i]);
    	}
		entropy = -1 * entropy;
		return entropy;
	}
	
	void addGaussianNoise (FloatProcessor I) 
		 Random rnd = new Random();
		 for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				float val = I.getf(u, v);
				float noise = (float) (rnd.nextGaussian() * 10);
				I.setf(u, v, val + noise);
			}
		 }
	}

}