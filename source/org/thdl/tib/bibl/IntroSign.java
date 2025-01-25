/*
The contents of this file are subject to the THDL Open Community License
Version 1.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License on the THDL web site 
(http://www.thdl.org/).

Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the 
License for the specific terms governing rights and limitations under the 
License. 

The Initial Developer of this software is the Tibetan and Himalayan Digital
Library (THDL). Portions created by the THDL are Copyright 2001-2003 THDL.
All Rights Reserved. 

Contributor(s): ______________________________________.
*/

package org.thdl.tib.bibl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
* This class displays the initial splash screen upon opening the program.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class IntroSign extends JFrame implements Runnable
{
	// Attributes
	JPanel cp, outPanel;
	String topline, title, org, byline;
	Toolkit xKit;
	Dimension wndSize, signSize, frameSize;
	Color bgColor = Color.getColor("silver");
	Color fgColor = Color.getColor("purple");
	Font headFont = new Font("Times",Font.BOLD,14);
	Font labelFont = new Font("Times",Font.PLAIN,12);
	String HOME 	= System.getProperties().getProperty("user.dir");
	String imageURL = new String("bin//knot.gif");

	private void init()
	{
		xKit = getToolkit();
		wndSize = xKit.getScreenSize();
		frameSize = new Dimension(300,250);
		setSize(frameSize);
		setLocation((wndSize.width-300)/2, (wndSize.height-250)/2);

		setBackground(bgColor);
		setUndecorated(true);

		ImageIcon knot = new ImageIcon(imageURL);
		title = "TiblEdit: XML Editor for Tibbibl Mark Up";
		byline = "Written by Than Garson, UVa";
		org = "©2002, Tibetan & Himalayan Digital Library";

		cp = new JPanel();
		cp.setPreferredSize(frameSize);
		cp.setBackground(bgColor);
		cp.setForeground(fgColor);
		Border outside = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black,1),
							BorderFactory.createBevelBorder(BevelBorder.RAISED));
		cp.setBorder(BorderFactory.createCompoundBorder(outside,BorderFactory.createEmptyBorder(10,5,5,5)));
		cp.setLayout(new GridLayout(0,1));
		cp.add(doLabel(knot));
		cp.add(doLabel(title));
		cp.add(doLabel(byline));
		cp.add(doLabel(org));
		getContentPane().add(cp,BorderLayout.CENTER);
		pack();
	}

	private JLabel doLabel(String in)
	{
		JLabel lab = new JLabel(in, JLabel.CENTER);
		lab.setForeground(fgColor);
		if(in.equals(title)) {
			lab.setFont(headFont);
		} else {
	 		lab.setFont(labelFont);
		}
		return lab;
	}

	private JLabel doLabel(ImageIcon im)
	{
		JLabel lab = new JLabel(" ",im,JLabel.CENTER);
		return lab;
	}

	public void close()
	{
		setVisible(false);
		dispose();
	}

	public IntroSign() {
		init();
		//addMouseListener(new IntroMouseListener(this));
	}

	public void run()
	{
		setVisible(true);
		for(int s=0;s<2000;s++)
		{
			toFront();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e){setVisible(false); dispose();}
		}
		setVisible(false);
		close();
		dispose();
	}

	public static void main(String[] args)
	{

	}

}
