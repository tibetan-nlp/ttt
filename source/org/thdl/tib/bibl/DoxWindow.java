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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
* This class displays a separate window with information concerning a texts doxographical category and it
* allows the user to set the doxographical categorization for a text. It changes options as the category is
* specificed and allows the user to view the categories in either English or Tibetan.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class DoxWindow extends JFrame
{
	private TiblEdit controller;

	// Static Type Definitions

	static int MAIN = 0;
	static int SUB  = 1;
	static int SUBSUB = 2;
	static String DOXDELIM = ":";
	static String IDDELIM = ".";


	// The Data on the DoxCats.
	String[] mainDox = {"a ti yo ga","a nu yo ga","ma h\u0101 yo ga","sna tshogs"};
	String[] mainEng = {"Atiyoga","Anuyoga","Mah\u0101yoga","Miscellaneous"};

	String[][] subDox = new String[4][6];

	// ati subs
	String[] ati = {"sems sde","klong sde","man ngag sde","spyi ti","yang ti","sna tshogs"};
	// anu subs =
	String[] anu = {"rtsa ba'i rgyud bzhi","mtha' drug gsal bar byed pa'i rgyud drug","dkon rgyud","sna tshogs"};
	// Maha subs
	String[] maha = {"rgyud sde","sgrub sde bka' brgyad","sna tshogs"};


	String[][][] subSubDox = new String[4][6][];

	// Atiyoga sub subs
	String[] semsSde = {"sems sde bco brgyad","kun byed skor","sna tshogs"};
	String[] manNgagSde = {"phyi nang skor","gsang skor","snying thig","sna tshogs"};
	String[] yangTi = {"bram ze'i skor","pad ma'i skor","sna tshogs"};

	// Mahayoga sub subs
	String[] rgyudSde = { "rtsa bar gyur sgyu 'phrul sde brgyad",
						  "bshad pa dang cha mthun gyi rgyud tantra sde bco brgyad",
						  "sna tshogs"};

	String[] sgrubSde = { "sgrub sde",
	                      "bka' brgyad",
	                      "sna tshogs"};

	String[][][][] subSubSubDox = new String[4][6][3][];
	// Mahayoga sub sub subs
	String[] rgyud18 = { "sku",
						 "gsung",
						 "thugs",
						 "yon tan",
						 "phrin las",
						 "spyi" };

	String[] sgrubSdeSub = { "bla ma dgongs pa 'dus pa",
							 "bde gshegs 'dus pa",
							 "sna tshogs" };

	String[] bkaBrgyad = { "'jam dpal sku'i skor",
					       "pad ma gsung gi rgyud",
					       "yang dag thugs kyi rgyud",
					       "bdud rtsi yon tan gyi rgyud",
					       "phrin las phur pa'i skor",
					       "ma mo rbod gtong skor",
					       "bstan srung mchod bstod",
					       "drag sngags skor",
					       "sna tshogs" };
	// English of categories
	// Sub categories
	String[][] subEng = new String[4][6];

	String[] atiEng = {"Mind Series",
					    "Space Series",
					    "Experiential Precept Series",
					    "Crown Pith",
					    "Ultra Pith",
					    "Miscellaneous"};

	String[] anuEng = { "The Four Root S\u016Btras",
						"The Six Tantras Clarifying the Limits",
						"The Twelve Rare Tantras",
						"Miscellaneous"};

	String[] mahaEng = { "Tantra Series",
					     "Practice Series of the Eight Proclamation Deities",
					     "Micellanesou"};

	// SubSub Categories
	String[][][] subSubEng = new String[4][6][];
	// Atiyoga
	String[] semsSdeEng = {"The Eighteen Mind Series Texts",
	                       "The All Creating Cycle",
	                       "Miscellaneous"};

	String[] manNgagSdeEng = {"External and internal cycles",
	                          "Esoteric Cycles",
	                          "Seminal Heart",
	                          "Miscellaneous"};

	String[] yangTiEng = {"Brahmin cycles",
	                      "Padma-related",
	                      "Miscellaneous"};

	// None for Anuyoga

	// Mahayoga
	String[] rgyudSdeEng = {"The Eightfold Set of Root Magical Emanation Tantras",
						    "The Eighteenfold Set of Explanatory Tantras",
						    "Miscellaneous"};

	String[] sgrubSdeEng = {"The Practice Series",
							"The Eight Proclamation Deities",
							"Miscellaneous"};

	// Sub sub sub categories
	String[][][][] subSubSubEng = new String [4][6][3][];

	String[] rgyud18Eng = {"Enlightened Body",
						   "Enlightened Speech",
						   "Enlightened Mind",
						   "Enlightened Qualities",
						   "Enlightened Activities",
						   "General"};

	String[] sgrubSdeSubEng = {"Summary of the Highest Intention",
							   "Consortium of Sugatas",
							   "Miscellaneous"};

	String[] bkaBrgyadEng = {"The Ma\u00F1jushr\u012B Cycle on Enlightened Form",
						     "The Lotus Tantras on Enlightened Communication",
						     "The Real Tantras on Enlightened Mind",
						     "The Nectar Tantras on Enlightened Qualities",
						     "The Sacred Dagger Cycle on Enlightened Activities",
						     "The Cycle on Invoking the Fierce Ma-mo Deities",
						     "Offerings and Praises to Protect the Teachings",
						     "The Cycle on Fierce Mantras",
						     "Miscellaneous"};


	// The Components of the Dox screen
	JFrame frame;
	JPanel contentPanel;

	static String DOX_INSTRUCTIONS = "Use this window to choose the doxographical category for this text. " +
									 "The categories can be viewed in either Tibetan or English, but both " +
									 "forms will be entered into the text's markup. The categories available " +
									 "will change as choices are made higher in the hierarchy. The numbers " +
									 "assigned to each category will be used to create the text's ID number. " +
									 "(The last section of the ID or the unique ID number will temporarily be " +
									 "assigned by the program until that category is filled out and ordered)";

	TCombo mainDoxBox;
	TCombo subDoxBox;
	TCombo subSubDoxBox;
	TCombo subSubSubDoxBox;
	JTextField numberInCategory;

	JRadioButton tib, eng;

	JButton submit,cancel;

	JLabel fileName;

// The init method
	public void init()
	{
		// Initializing the data ...
		// The 3 main divisions, plus misc, are defined above as mainDox

		// 3 main categories' subdivisiongs
		subDox[0] = ati;
		subDox[1] = anu;
		subDox[2] = maha;
		subDox[3] = null;

		// Ati sub subs
		subSubDox[0][0] = semsSde;
		subSubDox[0][2] = manNgagSde;
		subSubDox[0][4] = yangTi;

		//no Anu sub subs

		// Maha sub subs
		subSubDox[2][0] = rgyudSde;
		subSubDox[2][1] = sgrubSde;

		// Maha sub sub subs
		// rgyud sde - 18 tantras
		subSubSubDox[2][0][1] = rgyud18;

		//sgrub sde - practice series
		subSubSubDox[2][1][0] = sgrubSdeSub;

		// sgrub sde - 8 proclamation deities
		subSubSubDox[2][1][1] = bkaBrgyad;

		// English
		subEng[0] = atiEng;
		subEng[1] = anuEng;
		subEng[2] = mahaEng;
		subEng[3] = null;

		subSubEng[0][0] = semsSdeEng;
		subSubEng[0][2] = manNgagSdeEng;
		subSubEng[0][4] = yangTiEng;

		subSubEng[2][0] = rgyudSdeEng;
		subSubEng[2][1] = sgrubSdeEng;

		subSubSubEng[2][0][1] = rgyud18Eng;
		subSubSubEng[2][1][0] = sgrubSdeSubEng;
		subSubSubEng[2][1][1] = bkaBrgyadEng;

		// Setting up the Swing components
		mainDoxBox = new TCombo(mainDox);

		subDoxBox = new TCombo(subDox[0],mainDoxBox);

		subSubDoxBox = new TCombo(subSubDox[0][0],subDoxBox);

		subSubSubDoxBox = new TCombo(subSubDoxBox);

		mainDoxBox.addActionListener(new DoxEar(MAIN,subDoxBox));
		subDoxBox.addActionListener(new DoxEar(SUB,subSubDoxBox));
		subSubDoxBox.addActionListener(new DoxEar(SUBSUB,subSubSubDoxBox));

		// The Frame and panels
		getContentPane().setLayout(new BorderLayout());

		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(10,10,10,10));

		JTextArea instructions = new JTextArea(DOX_INSTRUCTIONS);
		instructions.setEditable(false);
		instructions.setFont(new Font("Arial",Font.PLAIN,12));
		instructions.setBorder(new EmptyBorder(5,10,5,5));
		instructions.setBackground(contentPanel.getBackground());
		instructions.setLineWrap(true);
		instructions.setWrapStyleWord(true);
		contentPanel.add(instructions);

		fileName = new JLabel(" ");

		contentPanel.add(fileName);

		JPanel doxList = new JPanel();
		doxList.setLayout(new GridLayout(4,1));
		//doxList.setBorder(new EmptyBorder(25,25,25,25));


		JPanel doxPanel = new JPanel(new BorderLayout());
		doxPanel.setBorder(new EmptyBorder(10,10,10,10));
		doxPanel.add(mainDoxBox,BorderLayout.CENTER);
		doxList.add(doxPanel);

		doxPanel = new JPanel(new BorderLayout());
		doxPanel.setBorder(new EmptyBorder(10,10,10,10));
		doxPanel.add(subDoxBox,BorderLayout.CENTER);
		doxList.add(doxPanel);

		doxPanel = new JPanel(new BorderLayout());
		doxPanel.setBorder(new EmptyBorder(10,10,10,10));
		doxPanel.add(subSubDoxBox,BorderLayout.CENTER);
		doxList.add(doxPanel);

		doxPanel = new JPanel(new BorderLayout());
		doxPanel.setBorder(new EmptyBorder(10,10,10,10));
		doxPanel.add(subSubSubDoxBox,BorderLayout.CENTER);
		doxList.add(doxPanel);

		/*doxPanel = new JPanel();
		doxPanel.setBorder(new EmptyBorder(10,10,10,10));
		JLabel label = new JLabel("Number in category: ");
		doxPanel.add(label);
		numberInCategory = new JTextField();
		doxPanel.add(numberInCategory);
		doxList.add(doxPanel);*/

		doxList.setSize(450,125);

		contentPanel.add(doxList);

		tib = new JRadioButton("Tibetan");
		eng = new JRadioButton("English");
		ButtonGroup group = new ButtonGroup();
		group.add(tib);
		group.add(eng);
		group.setSelected(tib.getModel(),true);

		LangChanger langListener = new LangChanger();

		tib.addActionListener(langListener);
		eng.addActionListener(langListener);

		JPanel controlPanel = new JPanel();//new GridLayout(2,5)
		//controlPanel.setBorder(new EmptyBorder(0,10,10,10));
		controlPanel.add(new JLabel());
		controlPanel.add(tib);
		controlPanel.add(new JLabel());
		controlPanel.add(eng);
		controlPanel.add(new JLabel());

		controlPanel.add(new JLabel());

		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
			{
				submit();
			}
		});
		controlPanel.add(submit);

		controlPanel.add(new JLabel());

		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
			{
				close();
			}
		});

		controlPanel.add(cancel);
		controlPanel.add(new JLabel());

		contentPanel.add(controlPanel);


		getContentPane().add(contentPanel,BorderLayout.CENTER);

		show();

		instructions.setSize(contentPanel.getSize().width-20,200);

	}

	public void setCategories(String masterID)
	{
		StringTokenizer stoke = new StringTokenizer(masterID,IDDELIM);
		try {
			String doxStr = stoke.nextToken();
			if(doxStr == null) {return;}
			doxStr = doxStr.substring(doxStr.length()-1);
			int index = -1;
			try {
				index = Integer.parseInt(doxStr)-1;
			} catch (NumberFormatException nfe) {return;}
			if(index>-1) {
				mainDoxBox.setSelectedIndex(index);
			} else {return;}

			doxStr = stoke.nextToken();
			if(doxStr == null) {return;}
			doxStr.trim();
			index = -1;
			try {
				index = Integer.parseInt(doxStr)-1;
			} catch (NumberFormatException nfe) {return;}
			if(index>-1) {
				subDoxBox.setSelectedIndex(index);
			} else {return;}

			doxStr = stoke.nextToken();
			if(doxStr == null) {return;}
			doxStr.trim();
			index = -1;
			try {
				index = Integer.parseInt(doxStr)-1;
			} catch (NumberFormatException nfe) {return;}
			if(index>-1) {
				subSubDoxBox.setSelectedIndex(index);
			} else {return;}

			doxStr = stoke.nextToken();
			if(doxStr == null) {return;}
			doxStr.trim();
			index = -1;
			try {
				index = Integer.parseInt(doxStr)-1;
			} catch (NumberFormatException nfe) {return;}
			if(index>-1) {
				subSubSubDoxBox.setSelectedIndex(index);
			} else {return;}
		} catch (NoSuchElementException nsee) {return;}
		catch(IllegalArgumentException iae) {return;}
	}

	public void setFileName()
	{
		if(controller == null) {return;}
		java.io.File currFile = controller.getCurrentFile();
		fileName.setText("File name: " + currFile.getName());
	}

	// Accessor Methods
	public String getDoxString(String lang)
	{
		String out = new String();

		if(lang.equals("eng")) {
			eng.doClick();
		} else {
			tib.doClick();
		}

		out += (String)mainDoxBox.getSelectedItem() + DOXDELIM;
		out += (String)subDoxBox.getSelectedItem();
		if(subSubDoxBox.isVisible()) {
			out += DOXDELIM + (String)subSubDoxBox.getSelectedItem();
			if(subSubSubDoxBox.isVisible()) {
				out += DOXDELIM + (String)subSubSubDoxBox.getSelectedItem();
			}
		}

		return out;
	}

	public String getID()
	{
		String out = new String("Ng");
		out += Integer.toString(mainDoxBox.getSelectedIndex()+1) + IDDELIM;
		out += Integer.toString(subDoxBox.getSelectedIndex()+1);
		if(subSubDoxBox.isVisible()) {
			out += IDDELIM + Integer.toString(subSubDoxBox.getSelectedIndex()+1);
			if(subSubSubDoxBox.isVisible()) {
				out += IDDELIM + Integer.toString(subSubSubDoxBox.getSelectedIndex()+1);
			}
		}

		return out;
	}


	// Helper Methods
	public void submit()
	{
		hide();
		//JOptionPane.showMessageDialog(this,"Id is: " + getID(), "Master ID", JOptionPane.INFORMATION_MESSAGE);
		controller.enterDox(this);
		close();
	}


	public void close()
	{
		hide();
		dispose();
	}

	public DoxWindow(String title)
	{
		super(title);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //********* CHANGE IN ACTUAL IMPLEMENTATION
		setBounds(100,100,500,400); // ***************
		this.controller = controller;
		init();
	}
	public DoxWindow(String title, String masterID, TiblEdit controller)
	{
		this(title);
		this.controller = controller;
		setCategories(masterID);
		setFileName();
	}

	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel(
				    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage() + "\n" +
				e.getClass().getName());
		}

		DoxWindow dw = new DoxWindow("Testing the Dox Window");

	}

	public class TCombo extends JComboBox
	{
		TCombo parent;
		String lang;
		Dimension size = new Dimension(450,25);

		// fix the size of the combo box
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		public Dimension getPreferredSize() {
			return size;
		}
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		public void setSize(Dimension d) {
		}
		public void setSize(int h, int w) {
		}

		public void setVisible(boolean b)
		{
			super.setVisible(b);
			try {
				if(b == false) {
					setSelectedIndex(0);
				}
			} catch (IllegalArgumentException iae) {}
		}

		public TCombo getTComboParent()
		{
			return parent;
		}

		public boolean hasParent()
		{
			if(parent == null) {return false;}
			return true;
		}

		public String getLang()
		{
			return lang;
		}

		public void setLang(String lang)
		{
			this.lang = lang;
		}

		public TCombo(String[] labels, TCombo parent)
		{
			super(labels);
			this.parent = parent;
			setSize(size);
			setLang("tib");
		}

		public TCombo(String[] labels)
		{
			super(labels);
			parent = null;
			setSize(size);
			setLang("tib");
		}

		public TCombo(TCombo parent)
		{
			super();
			this.parent = parent;
			setSize(size);
			setVisible(false);
			setLang("tib");
		}
	}

	public class DoxEar implements ActionListener
	{
		int type;
		TCombo child;
		String[] data;


		public DoxEar(int type, TCombo child)
		{
			this.type = type;
			this.child = child;
		}


		public void actionPerformed(ActionEvent ae)
		{
			TCombo parent = (TCombo)ae.getSource();
			String lang = parent.getLang();
			int parentIndex = parent.getSelectedIndex();
			data = null;
			try {
				if(lang.equals("tib")) {
					if(type==MAIN) {
						data = subDox[parentIndex];
					} else if(type == SUB) {
						TCombo grampa = parent.getTComboParent();
						int grampsIndex = grampa.getSelectedIndex();
						data = subSubDox[grampsIndex][parentIndex];
					} else if(type == SUBSUB) {
						TCombo grampa = parent.getTComboParent();
						int grampsIndex = grampa.getSelectedIndex();
						TCombo ancestor = grampa.getTComboParent();
						int ancestorIndex = ancestor.getSelectedIndex();
						data = subSubSubDox[ancestorIndex][grampsIndex][parentIndex];
					}
				} else if(lang.equals("eng")) {

					if(type==MAIN) {
						data = subEng[parentIndex];
					} else if(type == SUB) {
						TCombo grampa = parent.getTComboParent();
						int grampsIndex = grampa.getSelectedIndex();
						data = subSubEng[grampsIndex][parentIndex];
					} else if(type == SUBSUB) {
						TCombo grampa = parent.getTComboParent();
						int grampsIndex = grampa.getSelectedIndex();
						TCombo ancestor = grampa.getTComboParent();
						int ancestorIndex = ancestor.getSelectedIndex();
						data = subSubSubEng[ancestorIndex][grampsIndex][parentIndex];
					}
				}

				if(data != null) {
					DefaultComboBoxModel dcomboMod = new DefaultComboBoxModel(data);
					child.setModel(dcomboMod);
					child.setVisible(true);
					child.repaint();
					child.setSelectedIndex(0);
				} else {
					child.setVisible(false);
					child.repaint();
				}
			} catch (ArrayIndexOutOfBoundsException aiob) {
			} catch (NullPointerException npe) {
			}

		}
	}

	public class LangChanger implements ActionListener
	{

		public void actionPerformed(ActionEvent ae)
		{
			int main,sub,subsb,subsbsb;
			String command = ae.getActionCommand();
			try {
				if(command.equals("Tibetan")) {

					main = mainDoxBox.getSelectedIndex();
					sub = subDoxBox.getSelectedIndex();
					subsb = subSubDoxBox.getSelectedIndex();
					subsbsb = subSubSubDoxBox.getSelectedIndex();

					mainDoxBox.setLang("tib");
					mainDoxBox.setModel(new DefaultComboBoxModel(mainDox));
					subDoxBox.setLang("tib");
					subSubDoxBox.setLang("tib");
					subSubSubDoxBox.setLang("tib");

					mainDoxBox.setSelectedIndex(main);
					subDoxBox.setSelectedIndex(sub);
					subSubDoxBox.setSelectedIndex(subsb);
					subSubSubDoxBox.setSelectedIndex(subsbsb);


				} else if(command.equals("English")) {
					main = mainDoxBox.getSelectedIndex();
					sub = subDoxBox.getSelectedIndex();
					subsb = subSubDoxBox.getSelectedIndex();
					subsbsb = subSubSubDoxBox.getSelectedIndex();


					subSubSubDoxBox.setLang("eng");
					subSubDoxBox.setLang("eng");
					subDoxBox.setLang("eng");
					mainDoxBox.setLang("eng");
					mainDoxBox.setModel(new DefaultComboBoxModel(mainEng));

					mainDoxBox.setSelectedIndex(main);
					subDoxBox.setSelectedIndex(sub);
					subSubDoxBox.setSelectedIndex(subsb);
					subSubSubDoxBox.setSelectedIndex(subsbsb);

				}
			} catch (NullPointerException npe) {
				System.out.println("Null pointer exception caught!");
			}
		}

	}


}
