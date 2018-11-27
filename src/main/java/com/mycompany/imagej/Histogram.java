package com.mycompany.imagej;

import java.util.Random;
import ij.process.ImageProcessor;

public class Histogram {
	// Constant Values
	final int BLACK = 0;
	final int WHITE = 255;
	
	final int GREY_MASK = 0x0000FF;
	final int SIGMA = 10;

	// Class Attributes
	private ImageProcessor ip;
	private int N;
	private int M;
	
	private int[] hist;
	private double[] nHist;
	
	private int min;
	private int max;
	
	private double mean;
	private double var;
	private double skewness;
	private double kurtosis;
	private double entropy;
	
	public Histogram(ImageProcessor ip) {
		this.ip = ip; 
		M = ip.getWidth();
	    N = ip.getHeight();
		
	    this.update(); 	    
	}
	
	// Getter
	
	public double getMean() {
		return mean;
	}

	public double getVar() {
		return var;
	}

	public double getSkewness() {
		return skewness;
	}

	public double getKurtosis() {
		return kurtosis;
	}

	public double getEntropy() {
		return entropy;
	}	
		
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	// updates all moments, min, max and the internal histogram
	public void update() {
		hist = calculateHistogram(ip,N,M);
		nHist = normHistogram(hist, M, N);
		mean = mean(nHist);
		var = variance(nHist, mean);
		skewness = skewness(nHist, mean, var);
		kurtosis = kurtosis(nHist, mean, var);
		entropy = entropy(nHist);
		min = calcMin(hist);
		max = calcMax(hist);
	}
	
	//calculate the histogram of a 8 bit gray value image
	//compare page 49
	private int[] calculateHistogram(ImageProcessor ip, int N, int M) {		
		int[] histogram = new int[256];
		for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				int i = (ip.getPixel(u, v) & GREY_MASK);
				histogram[i]++;
			}
		 }
		return histogram;
	}	
		
	// norms the Histogram
    private double[] normHistogram(int[] histogram, int M, int N) {
    	double [] normedHist = new double[histogram.length];
    	for(int i = 0; i < histogram.length; i++) {
    		normedHist[i] = histogram[i] / (double)(N * M);
    	}
    	return normedHist;
    }

	// calculates the minimal Value
	private int calcMin(int[] hist) {
		int i = BLACK;
		while(hist[i] == 0) {
			i++;
		}
		return i;
	}
	
	// calculates the maximal Value
	private int calcMax(int[] hist) {
		int i = WHITE;
		while(hist[i] == 0) {
			i--;
		}
		return i;
	}
    
	// calculates the mean Value (first Moment)
    private double mean(double[] nHist) {
    	double mean = 0;
    	for(int i = 0; i < nHist.length; i++) {
    		mean += i * nHist[i];
    	}
    	return mean;
    }

	// calculates the variance (second Moment)
    private double variance(double[] nHist, double mean) {
    	double var = 0;
    	for(int i = 0; i < nHist.length; i++) {
    		var += Math.pow(i - mean, 2) * nHist[i];
    	}
    	return var;
    }
  
    // calculates the skewness (third Moment)
    private double skewness(double[] nHist, double mean, double var) {
    	double skew = 0;
    	for(int i = 0; i < nHist.length; i++) {
    		skew += Math.pow(i - mean,3) * nHist[i];
    	}
    	skew = (1 / (Math.pow(Math.sqrt(var), 3))) * skew;
    	return skew;
    }
    
	// calculates the kurtosis (fourth Moment)
    private double kurtosis(double[] nHist, double mean, double var) {
    	double kur = 0;
    	for(int i = 0; i < nHist.length; i++) {
    		kur += Math.pow(i - mean, 4) * nHist[i];
    	}
    	kur = -3 + (1/(Math.pow(var, 2))) * kur;
    	return kur;
    }

	// calculates the entropy
	private double entropy(double[] nHist) {
		double ent = 0;
		for(int i =  0; i < nHist.length; i++) {
			if(nHist[i] != 0) {
				ent += nHist[i] * Math.log10(nHist[i]) / Math.log10(2);
			}
		}
		ent *= -1;
		return ent;
	}
	
	// adds gausian Noise to the image
	public void addGaussianNoise (ImageProcessor I, int N, int M) { 
		 Random rnd = new Random();
		 for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				float val = I.getf(u, v);
				float noise = (float) (rnd.nextGaussian() * SIGMA);
				I.setf(u, v, val + noise);
			}
		 }
	}	
}
