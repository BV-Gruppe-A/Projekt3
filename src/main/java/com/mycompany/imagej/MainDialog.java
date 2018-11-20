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
	
	public MainDialog(ImagePlus image) {	
		ImageProcessor ip = image.getProcessor();
		Histogram hist = new Histogram(ip);
		
		gd = new GenericDialog("Histogram");
		gd.setModal(false);
		gd.setLayout(new GridLayout(1,2));
		FlowLayout flLayout = new FlowLayout();
		JPanel jp1 = new JPanel(flLayout);
		JPanel jp2 = new JPanel(flLayout);
		JPanel outer = new JPanel(new BorderLayout());
		
		JButton rauschen1 = new JButton("Gauß Rauschen");
		JButton rauschen2 = new JButton("Salt and Pepper Rauschen");
		
		rauschen1.addActionListener((e)->{
			hist.addGaussianNoise(ip, ip.getWidth(), ip.getHeight());
			gd.repaint();
			hist.update();
			updateResultTable(hist);});
		rauschen2.addActionListener((e)->{
			SaltAndPepper sp = new SaltAndPepper();
			sp.run(ip);
			gd.repaint();
			hist.update();
			updateResultTable(hist);});
		jp1.add(rauschen1);
		jp1.add(rauschen2);
		
		
		outer.add(jp1, BorderLayout.NORTH);
		outer.add(jp2, BorderLayout.CENTER);
		
		gd.addImage(image);
		gd.add(outer);
		
		rs = new ResultsTable();
		updateResultTable(hist);
		
		gd.showDialog();
	
		
		
	}
	
	void updateResultTable(Histogram hist) {
		
		rs.setValue("Mean",rowNumber,hist.getMean());
		rs.setValue("Min",rowNumber,hist.getMin());
		rs.setValue("Max",rowNumber,hist.getMax());
		rs.setValue("Var",rowNumber,hist.getVar());
		rs.setValue("Skewness",rowNumber,hist.getSkewness());
		rs.setValue("Kurtosis",rowNumber,hist.getKurtosis());
		rs.setValue("Entropy",rowNumber++,hist.getEntropy());
		
		rs.show("Results");
	}
	
}
