package com.mycompany.imagej;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.filter.SaltAndPepper;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainDialog {
	ResultsTable rs;
	GenericDialog gd;
	int rowNumber = 0;
	
	// Constructor, which fills the dialog with elements
	public MainDialog(ImagePlus image) {	
		ImageProcessor ip = image.getProcessor();
		Histogram hist = new Histogram(ip);
		
		gd = new GenericDialog("Histogram");
		gd.setModal(false);
		gd.setLayout(new BorderLayout());
		
		ButtonGroup bgRauschButtons = new ButtonGroup();
		
		JButton btnGaussRausch = new JButton("Gauß Rauschen");
		JButton btnSaltPeppRausch = new JButton("Salt and Pepper Rauschen");
		
		btnGaussRausch.addActionListener((e)->{
			hist.addGaussianNoise(ip, ip.getWidth(), ip.getHeight());
			gd.repaint();
			hist.update();
			updateResultTable(hist);
		});
			
		btnSaltPeppRausch.addActionListener((e)->{
			SaltAndPepper sp = new SaltAndPepper();
			sp.run(ip);
			gd.repaint();
			hist.update();
			updateResultTable(hist);
		});
		
		bgRauschButtons.add(btnGaussRausch);
		bgRauschButtons.add(btnSaltPeppRausch);
		
		gd.addImage(image);
		gd.add (bgRauschButtons, BorderLayout.SOUTH);
		
		rs = new ResultsTable();
		updateResultTable(hist);
		
		gd.showDialog();		
	}
	
	// updates the result table which shows the moments, min & max
	private void updateResultTable(Histogram hist) {		
		rs.setValue("Mean", rowNumber, hist.getMean());
		rs.setValue("Min", rowNumber, hist.getMin());
		rs.setValue("Max", rowNumber, hist.getMax());
		rs.setValue("Var", rowNumber, hist.getVar());
		rs.setValue("Skewness", rowNumber, hist.getSkewness());
		rs.setValue("Kurtosis", rowNumber, hist.getKurtosis());
		rs.setValue("Entropy", rowNumber++, hist.getEntropy());
		
		rs.show("Results");
	}
	
}
