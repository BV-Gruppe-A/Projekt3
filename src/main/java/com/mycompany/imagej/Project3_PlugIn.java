package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.util.Random;


public class Project3_PlugIn implements PlugInFilter {
    
	final int BLACK = 0;
	final int WHITE = 255;
	

    
    @Override    
    public int setup(String args, ImagePlus im) {  
    	// this plugin accepts 8-bit grayscale images
        return DOES_8G; 
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();
        //TO DO do project
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