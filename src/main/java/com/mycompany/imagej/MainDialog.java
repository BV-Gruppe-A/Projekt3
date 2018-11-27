package com.mycompany.imagej;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.filter.SaltAndPepper;
import ij.process.ImageProcessor;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Panel;
import javax.swing.*;

public class MainDialog {
	ResultsTable rs;
	GenericDialog gd;
	int rowNumber = 0;
	
	// Constructor, which fills the dialog with elements
	public MainDialog(ImagePlus image) {	
		ImageProcessor ip = image.getProcessor();
		Histogram hist = new Histogram(ip);
		
		gd = new GenericDialog("Add Noise to Grey Image");
		gd.setModalityType(Dialog.ModalityType.MODELESS);		
		gd.hideCancelButton();
		gd.setOKLabel("Close");
		
		Panel pnlRauschButtons = new Panel();		
		JButton btnGaussNoise = new JButton("Gaussian Noise");
		JButton btnSaltPeppNoise = new JButton("Salt and Pepper Noise");
		JButton btnOpenPicture = new JButton("Open Picture");

		btnGaussNoise.addActionListener((e)->{
			hist.addGaussianNoise(ip, ip.getWidth(), ip.getHeight());
			gd.repaint();
			hist.update();
			updateResultTable(hist);
		});
			
		btnSaltPeppNoise.addActionListener((e)->{
			SaltAndPepper sp = new SaltAndPepper();
			sp.run(ip);
			gd.repaint();
			hist.update();
			updateResultTable(hist);
		});
		
		btnOpenPicture.addActionListener((e)->{
			ImagePlus greyPicture = new ImagePlus("Grey Image",ip);
			greyPicture.show();
		});
		
		pnlRauschButtons.add(btnGaussNoise);
		pnlRauschButtons.add(btnSaltPeppNoise);
		pnlRauschButtons.add(btnOpenPicture);

		gd.addImage(image);
		gd.addPanel(pnlRauschButtons, GridBagConstraints.SOUTH, new Insets(5, 0, 0, 0));
		
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
