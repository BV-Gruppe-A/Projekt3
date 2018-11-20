package com.mycompany.imagej;

import java.util.Random;

import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class Histogram {

	private int N;
	private int M;
	private int[] hist;
	private double[] nHist;
	private double mean;
	private double var;
	private double skewness;
	private double kurtosis;
	private double entropy;
	private ImageProcessor ip;
	private int min;
	private int max;
	
	public Histogram(ImageProcessor ip) {
		this.ip = ip; 
		M = ip.getWidth();
	    N = ip.getHeight();
	    this.update(); 
	    
	}
	
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
	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	private int calcMin(int[] hist) {
		int i=0;
		while(hist[i] == 0) {
			i++;
		}
		return i;
	}
	
	private int calcMax(int[] hist) {
		int i=255;
		while(hist[i] == 0) {
			i--;
		}
		return i;
	}
	
    private double[] normHistogram(int[] histogram, int M, int N) {
    	double [] normedHist = new double[histogram.length];
    	for(int i=0;i<histogram.length;i++) {
    		normedHist[i] = histogram[i]/(double)(N*M);
    	}
    	return normedHist;
    }
    
    private double mean(double[] nHist) {
    	double mean = 0;
    	for(int i=0; i < nHist.length; i++) {
    		mean+=i*nHist[i];
    	}
    	return mean;
    }

    private double variance(double[] nHist, double mean) {
    	double var = 0;
    	for(int i=0; i < nHist.length; i++) {
    		var+=Math.pow(i-mean, 2)*nHist[i];
    	}
    	return var;
    }
  
    private double skewness(double[] nHist, double mean, double var) {
    	double skew = 0;
    	for(int i=0; i < nHist.length; i++) {
    		skew+=Math.pow(i-mean,3)*nHist[i];
    	}
    	skew=Math.pow(Math.sqrt(var),3)*skew;
    	return skew;
    }
    
    private double kurtosis(double[] nHist, double mean, double var) {
    	double kur = 0;
    	for(int i=0; i < nHist.length; i++) {
    		kur+=Math.pow(i-mean, 4)*nHist[i];
    	}
    	kur = -3 + Math.pow(var, 2) * kur;
    	return kur;
    }


	private double entropy(double[] nHist) {
		double ent = 0;
		for(int i=0; i < nHist.length; i++) {
			if(nHist[i] != 0) {
				ent+=nHist[i]*Math.log10(nHist[i])/Math.log10(2);
			}
		}
		ent = -ent;
		return ent;
	}
	
	
	public void addGaussianNoise (FloatProcessor I, int N, int M) { 
		 Random rnd = new Random();
		 for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				float val = I.getf(u, v);
				float noise = (float) (rnd.nextGaussian() * 10);
				I.setf(u, v, val + noise);
			}
		 }
	}
	private int[] calculateHistogram(ImageProcessor ip, int N, int M) {
		//calculate the histogram of a 8 bit gray value image
		//compare page 49
		int[] histogram = new int[256];
		for (int v = 0; v < N; v++) {
			for (int u = 0; u < M; u++) {
				int i = (ip.getPixel(u,v) & 0x0000FF);
				histogram[i]++;
			}
		 }
		return histogram;
	}
	
}
