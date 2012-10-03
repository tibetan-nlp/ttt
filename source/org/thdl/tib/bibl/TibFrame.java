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

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.EditorKit;
import javax.swing.text.Style;

	/**
	* <p>
	* TibFrame is the view for the {@link TiblEdit} program. TiblEdit is the controller.
	* {@link TibDoc}, or {@link TiblEdit#tibbibl} in its instantiation, is the data model.
	* TibFrame mainly utilizes a {@link TextPane} (an extension of JTextPane) as its main
	* view window, but also allows split screens with a {@link DiacriticPanel} on the left and/or
	* a {@link TibTable} at the bottom.
	* </p>
	*
	* @author Than Garson, Tibetan and Himalayan Digital Library
	*/

public class TibFrame extends JFrame implements CaretListener, TibConstants
{
	// Attributes
	private TiblEdit controller;

	// Frame Attributes
	private JMenu fileMenu, editMenu, insertMenu, provMenu, viewMenu;
	// File Menu items
	private JMenuItem openItem, closeItem, saveItem, saveAsItem, exportItem, exitItem;

	// Edit Menu items
	protected JMenuItem cutItem, copyItem, pasteItem, editTransItem, editNormItem,
	          removeAppItem, removeTitleItem, removeEditionItem;

	// Insert menu items
	private JMenuItem critTitleItem, edTitleItem, variantItem, insertEdItem, insertDiscItem;

	// View Menu items
	private JCheckBoxMenuItem diacItem, edConsultItem;
	private JMenuItem masterIDItem, userIDItem, aboutItem;
	private JMenuItem[] recentFileItems;
	//private JMenuItem authorItem, audienceItem, redactorItem, translatorItem, concealerItem, revealerItem;

	private JPanel contentPanel;
	private JComponent rightSp, leftSp, bottomSp, topSp;
	private JSplitPane horizontalSplit, verticalSplit;
	private TextPane tp;
	private DiacriticPanel dp;
	private TibTable tibTable;
	Vector styleElements;

	// Initialization
	/**
	* <p>
	* This is the initialization method for the TibFrame. The TibFrame is the view for
	* this program, for which a {@link TiblEdit} object is the controller and
	* a {@link TibDoc} is the data model. This method establishes a file menu,
	* an edit menu, an view menu, and an insert menu. Its content pane is set to a
	* JScrollPane with a {@link TextPane} as its view.
	* </p>
	*/
	protected void init()
	{
		Toolkit xKit = getToolkit();
		Dimension wndSize = xKit.getScreenSize();
		setBounds(wndSize.width/16, wndSize.height/16,
						  7*wndSize.width/8, 7*wndSize.height/8);
		addWindowListener(new XWindowHandler());
		addComponentListener(new XComponentHandler());

		// TextPane content pane
		tp = new TextPane(controller);
		EditorKit ekit = tp.getEditorKit();
		Action[] actions = ekit.getActions();

		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File Menu
		fileMenu = new JMenu(FILE);
		openItem = fileMenu.add(new FileAction(OPENFILE,this));
		openItem.setAccelerator(KeyStroke.getKeyStroke('O',Event.CTRL_MASK));
		openItem.setFont(MENU_FONT);
		openItem.setEnabled(true);

		closeItem = fileMenu.add(new FileAction(CLOSEFILE,this));
		closeItem.setAccelerator(KeyStroke.getKeyStroke('W',Event.CTRL_MASK));
		closeItem.setFont(MENU_FONT);
		closeItem.setEnabled(false);

		saveItem = fileMenu.add(new FileAction(SAVE,this));
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S',Event.CTRL_MASK));
		saveItem.setFont(MENU_FONT);
		saveItem.setEnabled(false);

		fileMenu.addSeparator();

		recentFileItems = new JMenuItem[RECENT_FILE_SIZE];
		for(int n=0;n<recentFileItems.length;n++)
		{
			recentFileItems[n] = new JMenuItem();
			fileMenu.add(recentFileItems[n]);
			recentFileItems[n].setVisible(false);
		}

		recentFileItems[0].setText(" ");
		recentFileItems[0].setVisible(true);

		fileMenu.addSeparator();
		exitItem = fileMenu.add(new FileAction(EXIT, this));
		exitItem.setAccelerator(KeyStroke.getKeyStroke('Q',Event.CTRL_MASK));
		exitItem.setFont(MENU_FONT);
		exitItem.setEnabled(true);

		menuBar.add(fileMenu);

		// Edit Menu
		editMenu = new JMenu(EDIT);

		 // 65 - paste; 26 - copy; 14 -cut

		copyItem = editMenu.add(actions[26]);
		copyItem.setAccelerator(KeyStroke.getKeyStroke('C',Event.CTRL_MASK));
		copyItem.setText("Copy");
		copyItem.setFont(MENU_FONT);
		copyItem.setEnabled(false);

		cutItem = editMenu.add(actions[14]);
		cutItem.setAccelerator(KeyStroke.getKeyStroke('X',Event.CTRL_MASK));
		cutItem.setText("Cut");
		cutItem.setFont(MENU_FONT);
		cutItem.setEnabled(false);

		pasteItem = editMenu.add(actions[65]);
		pasteItem.setAccelerator(KeyStroke.getKeyStroke('V',Event.CTRL_MASK));
		pasteItem.setText("Paste");
		pasteItem.setFont(MENU_FONT);
		pasteItem.setEnabled(false);

		editMenu.addSeparator();
		editTransItem = editMenu.add(new EditAction(TRANS_EDIT,this));
		editTransItem.setAccelerator(KeyStroke.getKeyStroke('T',Event.CTRL_MASK));
		editTransItem.setFont(MENU_FONT);
		editTransItem.setEnabled(false);

		editNormItem = editMenu.add(new EditAction(NORM_EDIT,this));
		editNormItem.setFont(MENU_FONT);
		editNormItem.setAccelerator(KeyStroke.getKeyStroke('N',Event.CTRL_MASK));
		editNormItem.setEnabled(false);

		removeAppItem = editMenu.add(new EditAction(REMOVE_APP,this));
		removeAppItem.setFont(MENU_FONT);
		removeAppItem.setAccelerator(KeyStroke.getKeyStroke('X',Event.CTRL_MASK+Event.SHIFT_MASK));
		removeAppItem.setEnabled(false);

		removeTitleItem = editMenu.add(new EditAction(REMOVE_TITLE,this));
		removeTitleItem.setFont(MENU_FONT);
		removeTitleItem.setAccelerator(KeyStroke.getKeyStroke('T',Event.CTRL_MASK+Event.SHIFT_MASK));
		removeTitleItem.setEnabled(false);

        removeEditionItem = editMenu.add(new EditAction(REMOVE_EDITION,this));
		removeEditionItem.setFont(MENU_FONT);
		removeEditionItem.setAccelerator(KeyStroke.getKeyStroke('R',Event.CTRL_MASK+Event.SHIFT_MASK));
		removeEditionItem.setEnabled(false);

		menuBar.add(editMenu);

		// Insert menu
		insertMenu = new JMenu(INSERT);

		insertEdItem = insertMenu.add(new InsertAction(ED_INFO,this));
		insertEdItem.setFont(MENU_FONT);
		insertEdItem.setAccelerator(KeyStroke.getKeyStroke('I',Event.CTRL_MASK));
		insertEdItem.setEnabled(false);

		critTitleItem = insertMenu.add(new InsertAction(CRIT_TITLE,this));
		critTitleItem.setFont(MENU_FONT);
		critTitleItem.setAccelerator(KeyStroke.getKeyStroke('C',Event.CTRL_MASK+Event.SHIFT_MASK));
		critTitleItem.setEnabled(false);

		edTitleItem = insertMenu.add(new InsertAction(ED_TITLE,this));
		edTitleItem.setFont(MENU_FONT);
		edTitleItem.setAccelerator(KeyStroke.getKeyStroke('E',Event.CTRL_MASK+Event.SHIFT_MASK));
		edTitleItem.setEnabled(false);

		insertDiscItem = insertMenu.add(new InsertAction(TITLE_DISC,this));
		insertDiscItem.setFont(MENU_FONT);
		insertDiscItem.setAccelerator(KeyStroke.getKeyStroke('D',Event.CTRL_MASK+Event.SHIFT_MASK));
		insertDiscItem.setEnabled(false);

		insertMenu.addSeparator();
		variantItem = insertMenu.add(new InsertAction(INSERT_APP,this));
		variantItem.setFont(MENU_FONT);
		variantItem.setAccelerator(KeyStroke.getKeyStroke('V',Event.CTRL_MASK+Event.SHIFT_MASK));
		variantItem.setEnabled(false);

		menuBar.add(insertMenu);

		// View menu
		viewMenu = new JMenu(VIEW);

		masterIDItem = viewMenu.add(new ViewAction(MASTER_ID_VIEW,this));
		masterIDItem.setFont(MENU_FONT);
		masterIDItem.setAccelerator(KeyStroke.getKeyStroke('M',Event.CTRL_MASK));
		masterIDItem.setEnabled(false);

		viewMenu.addSeparator();

		edConsultItem = new JCheckBoxMenuItem(EDCON,false);
		edConsultItem.setFont(MENU_FONT);
		edConsultItem.setAccelerator(KeyStroke.getKeyStroke('E',Event.CTRL_MASK));
		edConsultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				boolean on = ((JCheckBoxMenuItem)ae.getSource()).isSelected();
				if(on) {
					showEditions();
				} else {
					hideEditions();
				}
			}
		});
		edConsultItem.setEnabled(false);
		viewMenu.add(edConsultItem);

		diacItem = new JCheckBoxMenuItem(DIAC,false);
		diacItem.setFont(MENU_FONT);
		diacItem.setAccelerator(KeyStroke.getKeyStroke('D',Event.CTRL_MASK));
		diacItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				boolean on = ((JCheckBoxMenuItem)ae.getSource()).isSelected();
				if(on) {
					showDiacritics();
				} else {
					hideDiacritics();
				}
			}
		});
		viewMenu.add(diacItem);

		userIDItem = viewMenu.add(new ViewAction(USER_ID_VIEW,this));
		userIDItem.setFont(MENU_FONT);
		userIDItem.setEnabled(true);

		viewMenu.addSeparator();

		aboutItem = viewMenu.add(new ViewAction(ABOUT,this));
		aboutItem.setFont(MENU_FONT);
		aboutItem.setEnabled(true);

		menuBar.add(viewMenu);

		//Content frame


		topSp = new JScrollPane(tp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(topSp);
		setVisible(true);
		tp.setEditable(false);
	}

	// Accessors
	/**
	* <p>
	* This sets the {@link #controller} variable to the controller which is the
	* instantiation of the main {@link TiblEdit} program.
	* </p>
	*
	* @param jt TiblEdit - the controller of the program.
	*/
	public void setController (TiblEdit jt)
	{
		controller = jt;
	}

	/**
	* <p>
	* This method returns the controller for this view.
	* </p>
	*
	* @return TiblEdit - the controller.
	*/
	public TiblEdit getController()
	{
		return controller;
	}

	/**
	* <p>
	* This method sets the list of recent files in the file menu.
	* </p>
	*
	* @param recent Vector - A Vector of the recent files from the controller.
	*/
	public void setRecentFiles(Vector recent)
	{
		int n = 0;
		for(Iterator it=recent.iterator();it.hasNext();)
		{
			final File file = (File)it.next();
			if(n == RECENT_FILE_SIZE) {break;}
			JMenuItem fileItem = recentFileItems[n++];
			fileItem.setText(file.getName());
			ActionListener al[] = fileItem.getActionListeners();
			if(al != null && al.length >0) {fileItem.removeActionListener(al[0]);}
			fileItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae)
				{
					controller.openFile(file);
				}
			});
			fileItem.setVisible(true);
			fileItem.setEnabled(true);
		}

		for(;n<RECENT_FILE_SIZE;n++)
		{
			recentFileItems[n].setVisible(false);
		}
	}


	/**
	* <p>
	* This returns the {@link TextPane} that is the main view for tibbibl text information.
	* The TextPane is set in this objects {@link #init} method and is set within a
	* JScrollPane.
	* </p>
	*
	* @return TextPane the text pane that is the main content view of the program.
	*/
	public TextPane getTextPane()
	{
		return tp;
	}

	/**
	* <p>
	* This returns the position of the caret within the {@link TextPane} that is
	* the main text view for the program.
	* </p>
	*
	* @return int - the position of the caret.
	*/
	public int getCaretPosition()
	{
		return tp.getCaret().getDot();
	}

	// Helper methods
	/**
	* <p>
	* This method is called by the controller when a file is opened. It serves to
	* enable or disable the appropriate menu commands. Open is disabled, and
	* close, save, view titles, add crit title, add ed title, and view editions consulted
	* are turned on.
	* </p>
	*/
	public void fileOpened()
	{
		setTitle(PROG_NAME+controller.getCurrentFile().getName());
		openItem.setEnabled(false);
		closeItem.setEnabled(true);
		saveItem.setEnabled(true);
		critTitleItem.setEnabled(true);
		edTitleItem.setEnabled(true);
		edConsultItem.setEnabled(true);
		copyItem.setEnabled(true);
		cutItem.setEnabled(true);
		pasteItem.setEnabled(true);
		editTransItem.setEnabled(true);
		editNormItem.setEnabled(true);
		masterIDItem.setEnabled(true);
		insertEdItem.setEnabled(true);
		removeEditionItem.setEnabled(true);
		insertDiscItem.setEnabled(true);
		for(int n=0;n<RECENT_FILE_SIZE;n++)
			recentFileItems[n].setEnabled(false);
	}

	/**
	* <p>
	* This method is called by the controller when a file is closed. It serves to
	* enable or disable the appropriate menu commands. Open is enabled and
	* close, save, insert crit title, insert ed title, view titles, and view editions
	* consulted are all disabled. It also calls {@link TextPane#reset}, which resets
	* the styled document for the textpane view, calls {@link #hideEditions} to hide
	* any table that is displaying at the bottom of the screen, and then redisplays itself
	* with {@link #show}.
	* </p>
	*/
	public void fileClosed()
	{
		setTitle(DEFAULT_HEADER);
		openItem.setEnabled(true);
		closeItem.setEnabled(false);
		saveItem.setEnabled(false);
		critTitleItem.setEnabled(false);
		edTitleItem.setEnabled(false);
		edConsultItem.setEnabled(false);
		copyItem.setEnabled(false);
		cutItem.setEnabled(false);
		pasteItem.setEnabled(false);
		editTransItem.setEnabled(false);
		editNormItem.setEnabled(false);
		masterIDItem.setEnabled(false);
		insertEdItem.setEnabled(false);
		removeEditionItem.setEnabled(false);
		insertDiscItem.setEnabled(false);

		tp.reset();
		hideEditions();
		setRecentFiles(controller.getRecent());
		setTitle(DEFAULT_HEADER);
		show();
	}

	/**
	* <p>
	* This method is called (maybe) when the cursor is on a title. It enables the add
	* critical title menu option. (May be deprecated?)
	* </p>
	*/
	public void titleSelected()
	{
		critTitleItem.setEnabled(true);
	}

	/**
	* <p>
	* This method is called (maybe) when cursor is on something other than a title. It disables the add
	* critical title menu option. (May be deprecated?)
	* </p>
	*/
	public void titleNotSelected()
	{
		critTitleItem.setEnabled(false);
	}

	/**
	* <p>
	* This method displays a {@link TibTable} as the bottom half of a split screen with the {@link TextPane}
	* This particular table displays the editions consulted for the formation of the master record
	* by displaying the information in the text's tibiddecl element. To do so it uses {@link TibTable#TibTable(IDFactory) TibTable constructor}
	* that takes an {@link IDFactory} retrieved from the {@link TibDoc}. It then calls {@link #showTable(TibTable) TibTable}
	* method.
	* </p>
	*/
	public void showEditions()
	{
		if(controller.getTibDoc() == null) {return;}
		tibTable = new TibTable(controller.getTibDoc().getIDFactory());
		showTable(tibTable);
	}
	/**
	* <p>
	* This method hides <b>any</b> table that is displaying at the bottom of the {@link TextPane}. The first
	* table that used this was the edition's consulted table, but it will close any table including an apparatus
	* table that is displaying at the bottom of the screen.
	* </p>
	*/

	public void hideEditions()
	{
		topSp = new JScrollPane(tp);
		if(horizontalSplit == null) {
			setContentPane(topSp);
			show();

		} else {
			int horizLoc = horizontalSplit.getDividerLocation();
			horizontalSplit.setRightComponent(topSp);
			show();
			horizontalSplit.setDividerLocation(horizLoc);
		}
		show();
		verticalSplit = null;
	}

	/**
	* <p>
	* This method creates a split screen with a {@link DiacriticPanel#DiacriticPanel(TibFrame) DiacriticPanel}
	* on the left and the main {@link TextPane} on the right.
	* </p>
	*/
	public void showDiacritics()
	{
		dp = new DiacriticPanel(this);
		rightSp = new JScrollPane(dp);
		JComponent comp;
		if(verticalSplit == null) {
			leftSp = new JScrollPane(tp);
			comp = leftSp;
		} else {
			comp = verticalSplit;
		}
		horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,rightSp,comp);
		setContentPane(horizontalSplit);
		show();
		double loc = 0.1D;
		horizontalSplit.setDividerLocation(loc);
	}

	/**
	* <p>
	* This method hides the {@link DiacriticPanel} that is displayed on the left of
	* the split screen.
	* </p>
	*/
	public void hideDiacritics()
	{
		JComponent comp;
		if(verticalSplit == null) {
			comp = tp;
		} else {
			comp = verticalSplit;
		}
		rightSp = new JScrollPane(comp);
		setContentPane(rightSp);
		show();
		horizontalSplit = null;
	}

	/**
	* <p>
	* This method displays a {@link TibTable} underneath the {@link TextPane}. It takes a
	* {@link TibTable} as its parameter and depending on the value of its {@link TibTable#getType() type}
	* inserts either an JTable with the editions consulted or it retrieves an {@link TibTable#getAppPanel() specific
	* JPanel} that has a table with an apparatus' info and control buttons.
	* </p>
	*
	* @param tt TibTable - the TibTable object that contains the tabular information to be displayed.
	*/
	public void showTable(TibTable tt)
	{
		JSplitPane tableSplit = new JSplitPane();
		if(tt.getType() == TibTable.APP) {
			edConsultItem.setSelected(false);
			tt.addObserver(controller);
			tableSplit = tt.getAppPanel();
			bottomSp = tableSplit;
		} else {
			bottomSp = new JScrollPane(tt.getTable());
		}
		topSp = new JScrollPane(tp);
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,topSp,bottomSp);
		if(horizontalSplit == null) {
			setContentPane(verticalSplit);
			show();
		} else {
			int horizLoc = horizontalSplit.getDividerLocation();
			horizontalSplit.setRightComponent(verticalSplit);
			show();
			horizontalSplit.setDividerLocation(horizLoc);
		}
		double loc = 0.80D;
		verticalSplit.setDividerLocation(loc);
		tableSplit.setDividerLocation(loc);
	}

	/**
	* <p>
	* This method is called if the insert variant option is chosen from the insert menu. Variant readings
	* are displayed in the {@link TextPane} with a yellow background and are determined by being marked up
	* within an app element. When inserting a new variant, the app element was not originally there so the
	* selected area does not have a yellow background. This method sets the background of the selected area
	* to yellow until the insertion is complete and the TextPane can be redisplayed.
	* </p>
	*/
	public void displayNewApp()
	{
		int start = tp.getSelectionStart();
		int len = tp.getSelectedText().length();
		Style variant = tp.getStyle(TextPane.VARIANT);
		tp.getStyledDocument().setCharacterAttributes(start,len,variant,false);
	}

	/**
	* <p>
	* This method displays all the variations of text titles found in a {@link TibDoc}. It takes the
	* TibDoc's {@link TitleFactory} and uses its {@link TitleFactory#getAllTitles() getAllTitles} method
	* to retrive a vector of {@link ElementStyle ElementStyles}. It also adds a text header at the top
	* and depending on the {@link TiblEdit#mode mode} of the controller (i.e., whether it is inserting a
	* new title or translation, etc.) adds appropriate prompts. It then calls the {@link TextPane#setTextPane(Vector) TextPane's setTextPane(Vector)}
	* method with the vector of StyleElements and this displays the information. That setTextPane method
	* returns an {@link ElementList} which is a list of org.jdom.Elements with their associated positions in
	* the TextPane (start and end) so that when the caret is positioned somewhere in the TextPane the controller
	* can find the associated element and make the appropriate options available.
	* </p>
	*
	* @param tf TitleFactory - The TibDocs TitleFactory object for retrieving title information
	*
	* @return ElementList - The ElementList returned by the TextPane once its document is set.
	*
	*/
	public ElementList showTitles(TitleFactory tf)
	{
		int presCaretPos = tp.getCaretPosition();
		if(presCaretPos < 0) {presCaretPos = 0;}

		styleElements = new Vector();

		// Do the Text's Header Element as the documents general header
		String[] headStrings = tf.getHeadStrings();

		if(headStrings == null) {

			styleElements.add(new ElementStyle("No Head Element Found\n\n",TextPane.TEXT_HEAD,NO_ELEM));

		} else {
			styleElements.add(new ElementStyle(headStrings[0]+" ",TextPane.TEXT_HEAD,NO_ELEM));

			if(headStrings.length>1) {
				for(int n=1;n<headStrings.length;n++) {
					styleElements.add(new ElementStyle(headStrings[n]+"\n",TextPane.TEXT_HEAD_ITALIC,NO_ELEM));
				}
				styleElements.add(new ElementStyle("\n",TextPane.REG,NO_ELEM));
			}
		}

		styleElements.addAll(tf.getAllTitles());

		// Check to see if enterting translation

		org.jdom.Element child, selEl;
		String trans = new String();
		selEl = controller.getSelectedElement();

		// Get index of selected Element, adding 1 because label for insertion goes after.
		int index = findElementStyleIndex(selEl)+1;



		if(controller.getMode() == ENTER_TRANS) {

			styleElements.add(index++,new ElementStyle(ENTER_TRANS_PHRASE,TextPane.BOLD,NO_ELEM));
			child = selEl.getChild(FOREIGN);
			if(child == null) {
				child = new org.jdom.Element(FOREIGN);
				child.setAttribute(LANG,ENG);
				selEl.addContent(child);
			}
			trans = " " + TibDoc.cleanString(child.getText())+"\n";
			styleElements.add(index,new ElementStyle(trans,TextPane.RED,child));

		} else if(controller.getMode() == NEW_TITLE) {

			styleElements.add(index++,new ElementStyle(ENTER_TITLE_PHRASE,TextPane.BOLD,NO_ELEM));
			styleElements.add(index++,new ElementStyle(ENTER_PLACE,TextPane.RED,selEl));

		} else if(controller.getMode() == ENTER_NORMALIZED) {

			index -= 1;
			if(index<0) {index = 0;}
			styleElements.add(index,new ElementStyle(ENTER_NORMALIZED_PHRASE,TextPane.BOLD,NO_ELEM));
			index += 4;
			styleElements.add(index, new ElementStyle(ENTER_NORM_TRANS_PHRASE,TextPane.BOLD,NO_ELEM));

		} else if(controller.getMode() == DO_TRANS) {
			if(index == 0) {
				index = findElementStyleIndex(selEl.getParent())+2;
				styleElements.add(index++,new ElementStyle(ENTER_TRANS_PHRASE,TextPane.BOLD,NO_ELEM));
				styleElements.add(index, new ElementStyle("  \n",TextPane.RED,selEl));

			} else {
				index -= 1;
				styleElements.add(index,new ElementStyle(ENTER_TRANS_PHRASE,TextPane.BOLD,NO_ELEM));
			}

		}
		// Convert Vectors to String arrays and send to TextPane for displaying
		// Return the resulting element list
		tp.removeMouseListener(controller);
		tp.reset();
		ElementList el = tp.setTextPane(styleElements);

		if(controller.getMode()== NORM) {
			//tp.setCaretPosition(0);
			tp.addMouseListener(controller);
			tp.setEditable(false);
			tp.addCaretListener(this);
		} else {
			tp.removeCaretListener(this);
			tp.addKeyListener(controller);
			tp.setEditable(true);
		}

		/*try {
			tp.setCaretPosition(presCaretPos);
		} catch (IllegalArgumentException ia) {}
		tp.getCaret().setVisible(true);*/

		return el;
	}

	public void chooseEdition()
	{
		hideEditions();
		tibTable = new TibTable(controller.getTibDoc().getIDFactory());
		showTable(tibTable);
		JOptionPane.showMessageDialog(this,DEL_EDS_MESS,DEL_EDS_TITLE,JOptionPane.INFORMATION_MESSAGE);
		tibTable.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() == 2) {
					JTable theTable = tibTable.getTable();
					int selRow = theTable.getSelectedRow();
					String sigla = (String)theTable.getValueAt(selRow,0);
					IDFactory idf = controller.getTibDoc().getIDFactory();
					org.jdom.Element tibid = idf.findEdTibid(sigla);
					controller.deleteEdition(tibid);
				}
			}
		});
	}

	/**
	* <p>
	* This method is used by {@link #showTitles(TitleFactory)} to insert prompts for information
	* at the correct location. The showTitles method creates a vector of {@link ElementStyle}. Once
	* this is created, if the {@link TiblEdit#mode} is to insert a title or translation. This method
	* is called with the selected element. It searches the vector for that element and returns its
	* index in the vector.
	* </p>
	*
	* @param el org.jdom.Element - the selected element.
	*
	* @return int - that element's index in the ElementStyle Vector.
	*/
	private int findElementStyleIndex(org.jdom.Element el)
	{
		if(styleElements == null) {
			System.out.println("No styleElements list to search in findElementStyleIndex of TibFrame");
			return -1;
		}
		int index = -1;
		Iterator it = styleElements.iterator();
		while(it.hasNext())
		{
			Object item = it.next();
			//System.out.println("It is: " + item.getClass().getName());
			ElementStyle es = (ElementStyle)item;
			if(es.getElement().equals(el)) { index = styleElements.indexOf(es);}
		}
		if(index == -1) {
			//System.out.println("Element was not found: " + TiblEdit.outputString(el));
		}
		return index;
	}

	public DiscDialog getDiscussionDialog(TibDoc tibl)
	{
		DiscDialog dd = new DiscDialog(this, tibl);
		return dd;
	}

	// Constructor
	/**
	* <p>
	* This constructor simply takes a title as the corresponding JFrame contructor.
	* </p>
	*
	* @param title String - the frame's title.
	*/
	public TibFrame(String title)
	{
		super(title);
		init();
	}

	/**
	* <p>
	* This constructor takes both a title and a {@link TiblEdit} object that is its controller.
	* It first calls JFrame's title constructor, then {@link #setController(TiblEdit) the setController} method
	* and then the {@link #init()} method.
	* </p>
	*/
	public TibFrame(String title, TiblEdit app)
	{
		super(title);
		setController(app);
		init();
	}

	// Main for testing

	public static void main(String[] args)
	{
	}

	// Listeners
	/**
	* <p>
	* This implementation of the CaretListener interface calls the controller's
	* {@link TiblEdit#checkCaretPosition(String) checkCaretPosition} method with the type of {@link TibConstants#AP AP}
	* to see if an appartatus element is insertable. If so, then it turns on the insert variant reading
	* menu item. If not, that item is turned off.
	* </p>
	*
	* @param ce CaretEvent - the required parameter of this abstract method. It is not used here.
	*/
	public void caretUpdate(CaretEvent ce)
	{
		boolean canInsertAp = controller.checkCaretPosition(AP_CHECK);
		if(canInsertAp) {
			variantItem.setEnabled(true);
		} else {
			variantItem.setEnabled(false);
		}

		if(controller.checkCaretPosition(ED_TITLE_REM)) {
			removeTitleItem.setEnabled(true);
		} else {
			removeTitleItem.setEnabled(false);
		}
	}

	// SubClasses Event Handlers
	/**
	* <p>
	* The inner class, XWindowHandler, extends the WindowAdapter and is used for the closing of the frame.
	* It is added to the TibFrame in its {@link #init} method.
	* </p>
	*/

	/**
	* <p>
	* When the TibFrame is closed, the program ends. The window is disposed. The controller's
	* {@link TiblEdit#exit() exit()} method is called, and the system is exited.
	* </p>
	*/
	public class XWindowHandler extends WindowAdapter
			implements TibConstants
	{

		/** See class comment.
		 *
		 * @param e WindowEvent - the required parameter for this abstract method.
		 */
		public void windowClosing(WindowEvent e)
		{
			e.getWindow().dispose();
			controller.exit();
		}
	}
	public class XComponentHandler extends ComponentAdapter
	{

		public void ComponentResized(ComponentEvent ce)
		{
			show();
		}
	}


}
