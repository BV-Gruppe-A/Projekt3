package com.mycompany.imagej;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.SaltAndPepper;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MainDialog {

	GenericDialog gd;
	
	public MainDialog(ImagePlus image) {	
		ImageProcessor ip = image.getProcessor();
		Histogram hist = new Histogram(ip);
		
		gd = new GenericDialog("Histogram");
		gd.setLayout(new GridLayout(1,2));
		FlowLayout flLayout = new FlowLayout();
		JPanel jp1 = new JPanel(flLayout);
		JPanel jp2 = new JPanel(flLayout);
		JPanel outer = new JPanel(new BorderLayout());
		
		JButton rauschen1 = new JButton("Gauß Rauschen");
		JButton rauschen2 = new JButton("Salt and Pepper Rauschen");
		
		//rauschen1.addActionListener((e)->{hist.addGaussianNoise(new FloatProcessor(), ip.getWidth(), ip.getHeight());});
		rauschen2.addActionListener((e)->{
			SaltAndPepper sp = new SaltAndPepper();
			sp.add(ip,1);
			gd.repaint();});
		jp1.add(rauschen1);
		jp1.add(rauschen2);
		
		String [] spaltenNamen = {"Mittelwert","Minimum","Maximum","Varianz","Schiefe","Wölbung","Entropie"};
		DefaultTableModel tableModel = new DefaultTableModel(1, spaltenNamen.length);
		tableModel.setColumnIdentifiers(spaltenNamen);
		tableModel.setValueAt(hist.getMean(), 0, 0);
		tableModel.setValueAt(hist.getMin(), 0, 1);
		tableModel.setValueAt(hist.getMax(), 0, 2);
		tableModel.setValueAt(hist.getVar(), 0, 3);
		tableModel.setValueAt(hist.getSkewness(), 0, 4);
		tableModel.setValueAt(hist.getKurtosis(), 0, 5);
		tableModel.setValueAt(hist.getEntropy(), 0, 6);
		
		JTable table = new JTable(tableModel);
		
		jp2.add(new JScrollPane(table));
		
		outer.add(jp1, BorderLayout.NORTH);
		outer.add(jp2, BorderLayout.CENTER);
		
		gd.addImage(image);
		gd.add(outer);
		
		gd.showDialog();
	
	}
	
}
