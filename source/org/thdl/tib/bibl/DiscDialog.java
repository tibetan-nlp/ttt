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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyleContext.NamedStyle;

/**
* This extension of JDialog is used for entering in discussion concerning a specific title in a Tibbibl document.
* There are two types of discussion: brief (no paragraphs) and long (with paragraphs). It provides the user
* with a {@link DiacriticPanel} so that diacritics can be easily entered into the discussion. It also provides
* means to format the text as either bold and/or italic.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/
public class DiscDialog extends JDialog implements TibConstants, ActionListener
{

	// Attributes
	TibFrame parent;
	TibDoc doc;
	org.jdom.Element textDisc;

	JComboBox styleBox, typeBox, discBox;
	JPanel content, tempPanel, filler;
	DiscTextPane jta;
	JLabel headLabel, header;
	Action boldAction,italicAction;

	Color bgColor = Color.gray;
	Dimension fillerSize = new Dimension(50,20);
	Dimension dialogSize = new Dimension(700,550);
	Dimension contentSize = new Dimension(300,400);
	Dimension maxLine = new Dimension(600,50);
	int marg = 8;
	int sectionSelection;
	int returnType;

	Hashtable actions;

	public static final int CANCEL = -1;
	public static final int ENTER = 1;
    public static String NORMSTY   = "Normal";
    public static String STRONG = "Strong";
    public static String WEAK   = "Weak";
    public static String TITLE  = "Title";
    public static String TIBSTY = "Tibetan";
    public static String LIST   = "List";
    public static String CITATION = "Citation";
    public static String LIST_MARK = "- ";

	String[] titleTypes = {BRIEF,FULL};

	// Methods
	public void init()
	{
		// Setting general parameters
		setSize(dialogSize);
		parent = (TibFrame)getOwner();
		returnType = CANCEL;

		// Content panel & header
		content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(marg,marg,marg,marg));
		tempPanel = new JPanel();
		tempPanel.setBorder(new EmptyBorder(0,0,marg,0));
		headLabel = new JLabel();
		headLabel.setFont(BOLD_FONT);
		headLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		tempPanel.add(headLabel);
		header = new JLabel();
		header.setFont(DEFAULT_FONT);
		header.setHorizontalAlignment(SwingConstants.LEFT);
		header.setMaximumSize(maxLine);
		tempPanel.add(header);
		//tempPanel.setMaximumSize(maxLine);
		content.add(tempPanel);

		// menu & tool bars
		tempPanel = getTempPanel();
		JMenuBar jmbar = new JMenuBar();
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		jmbar.add(tools);
		tempPanel.add(jmbar);
		tempPanel.setMaximumSize(maxLine);
		content.add(tempPanel);

		tempPanel = getTempPanel();
		tempPanel.setBorder(new CompoundBorder(new EmptyBorder(marg,0,0,0), LineBorder.createBlackLineBorder()));

		// The Text pane
		jta = new DiscTextPane(); //setTextPane();
		tempPanel.add(getScrollPane(jta));
		jta.addKeyListener(new DiscKeyAdapter());
		tempPanel.setPreferredSize(contentSize);
		content.add(tempPanel);
		setKeys(jta);

		// The tool bar
		// Style/Mark up combobox
		JLabel lab = new JLabel("Mark-up: ");
		lab.setFont(BOLD_FONT);
		tools.add(lab);
		styleBox = new JComboBox(jta.getStyleTypes(titleTypes[0]));
		styleBox.setMaximumSize(new Dimension(60,30));
		styleBox.setRenderer(new StyleBoxRenderer());
		tools.add(styleBox);
		styleBox.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
			String sname = (String)styleBox.getSelectedItem();
			if(sname != null) {jta.setStyle(sname);}
			jta.grabFocus();
		}});

		tools.add(new JLabel(" "));

		// The Discussion type comboBox
		lab = new JLabel(" Type: ");
		lab.setFont(BOLD_FONT);
		tools.add(lab);

		typeBox = new JComboBox(titleTypes);
		typeBox.setBorder(BorderFactory.createLoweredBevelBorder());
		typeBox.setBackground(Color.white);
		typeBox.setMaximumSize(new Dimension(60,30));
		tools.add(typeBox);
		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				styleBox.setModel(new DefaultComboBoxModel(jta.getStyleTypes(titleTypes[typeBox.getSelectedIndex()])));
				jta.grabFocus();
			}
		});
		tools.add(new JLabel(" "));

		// The Section Combo Box
		lab = new JLabel(" Section: ");
		lab.setFont(BOLD_FONT);
		tools.add(lab);
		discBox.setMaximumSize(new Dimension(150,30));
		tools.add(discBox);
		tools.setMaximumSize(maxLine);

		// The Button Panel
		tempPanel = getTempPanel();
		tempPanel.setBorder(new EmptyBorder(10,0,0,0));
		tempPanel.add(getFiller());

		JButton enter = new JButton("Enter");
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				returnType = ENTER;
				java.util.List results = jta.makeXML();
				ComboBoxElement cbe = (ComboBoxElement)discBox.getSelectedItem();
				org.jdom.Element tibl = cbe.getElement();
				cbe.setElement(writeDiscussion(tibl,results));
				setVisible(false);
			}
		});
		enter.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(3,8,3,8)));
		tempPanel.add(enter);
		tempPanel.add(getFiller());
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				returnType = CANCEL;
				setVisible(false);
			}
		});
		cancel.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(), new EmptyBorder(3,8,3,8)));
		tempPanel.add(cancel);
		tempPanel.add(getFiller());
		tempPanel.setMaximumSize(maxLine);
		content.add(tempPanel);


		DiacriticPanel diaPanel = new DiacriticPanel(jta);
		JScrollPane contentSP = getScrollPane(content);
		JSplitPane jsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,getScrollPane(diaPanel),contentSP);
		jsplit.setContinuousLayout(true);
		setContentPane(jsplit);
		setLocation(10,10);
		jta.setContent(((ComboBoxElement)discBox.getSelectedItem()).getElement());
		jta.addMouseListener(new MouseStyleListener());
		pack();
		setSize(dialogSize);
		jsplit.setDividerLocation(0.25D);
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                jta.requestFocus();
            }
        });
		sectionSelection = discBox.getSelectedIndex();
	}

	public void setTibDoc(TibDoc td)
	{
		org.jdom.Element sections, temp;
		sections = null;
		Vector discTibls = new Vector();
		doc = td;
		discTibls.add(new ComboBoxElement(doc.getRoot()));
		sections = td.findElement(SECTIONS);
		if(sections != null) {
			java.util.List tibAnals = sections.getChildren(TIBANAL);
			discTibls.addAll(processSections(tibAnals));
		}
		discBox = new JComboBox(discTibls);
		discBox.addActionListener(this);

	}

	public Vector processSections(java.util.List tibAnals)
	{
		Vector totalSections = new Vector();
		for(Iterator it = tibAnals.iterator(); it.hasNext();) {
			org.jdom.Element tibAn = (org.jdom.Element)it.next();
			java.util.List children = tibAn.getChildren(TIBL);
			if(children != null) {
				for(Iterator childIt = children.iterator(); childIt.hasNext();) {
					org.jdom.Element child = (org.jdom.Element)childIt.next();
					totalSections.add(new ComboBoxElement(child));
					if(child.getChildren(TIBANAL) != null) {
						totalSections.addAll(processSections(child.getChildren(TIBANAL)));
					}
				}
			}
		}

		return totalSections;
	}

	public TibDoc getTibDoc()
	{
		return doc;
	}

	private JPanel getTempPanel()
	{
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		//jp.setBackground(bgColor);
		return jp;
	}

	private JPanel getFiller()
	{
		JPanel filler = new JPanel();
		filler.setPreferredSize(fillerSize);
		return filler;
	}

	private JScrollPane getScrollPane(Component comp)
	{
		JScrollPane jsp = new JScrollPane(comp,
										  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return jsp;
	}

	public void setHeader(String txt)
	{
		if(txt.length()>50) {
			int ind = txt.indexOf(" ",50);
			if(ind == -1) {ind=txt.length();}
			txt = txt.substring(0,ind) + " ...";
		}
		header.setText(txt);
		repaint();
	}

	public void setTitleText(String title)
	{
		headLabel.setText("Discussion on Title: ");
		setHeader(title);
	}

	public JTextPane setTextPane()
	{
		JTextPane workTP = new JTextPane();
		workTP.setFont(DEFAULT_FONT);
		return workTP;
	}

	public void setKeys(JTextPane workTP)
	{

		Keymap kmap = workTP.addKeymap("dialogKeyMap",workTP.getKeymap());
		boldAction = new StyledEditorKit.BoldAction();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B,
											   Event.CTRL_MASK);
		kmap.addActionForKeyStroke(key,boldAction);
		boldAction.putValue(Action.NAME," Bold (Ctrl+B) ");

		italicAction = new StyledEditorKit.ItalicAction();
		key = KeyStroke.getKeyStroke(KeyEvent.VK_I,
									 Event.CTRL_MASK);
		kmap.addActionForKeyStroke(key, italicAction);
		italicAction.putValue(Action.NAME," Italic (Ctrl+I) ");
		workTP.setKeymap(kmap);
	}

	private void createActionTable(JTextComponent textComponent) {
		actions = new Hashtable();
		Action[] actionsArray = textComponent.getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put(a.getValue(Action.NAME), a);
			//System.out.println(a.getValue(Action.NAME));
		}
	}

	private Action getActionByName(String name) {
		return (Action)(actions.get(name));
	}

	public void setDiscussionText(org.jdom.Element tdecl)
	{
		StyledDocument doc = jta.getStyledDocument();
		Style norm = doc.getLogicalStyle(0);
		Style boldSt = doc.addStyle(BOLD,norm);
		boldSt.addAttribute(StyleConstants.FontConstants.Bold,new Boolean(true));
		Style italSt = doc.addStyle(ITALIC,norm);
		italSt.addAttribute(StyleConstants.FontConstants.Italic,new Boolean(true));
		org.jdom.Element disc = TiblEdit.locateDisc(tdecl);
		if(disc.getAttributeValue(TYPE) == null) { disc.setAttribute(TYPE,BRIEF); }
		String discType = disc.getAttributeValue(TYPE);
		try {
			if(discType.equalsIgnoreCase(BRIEF)) {
				typeBox.setSelectedIndex(0);
				for(Iterator it = disc.getContent().iterator();it.hasNext();) {
					Object child = it.next();
					//System.out.println("Item in Discussion: " + child.toString());
					if(child instanceof org.jdom.Text) {
						String txt = ((org.jdom.Text)child).getText();
						doc.insertString(doc.getLength(),txt,norm);
					} else if (child instanceof String) {
						String txt = (String)child;
						doc.insertString(doc.getLength(),txt,norm);
					} else if (child instanceof org.jdom.Element) {
						org.jdom.Element hiEl = (org.jdom.Element)child;
						String txt = hiEl.getText();
						String rend = hiEl.getAttributeValue(REND);
						if(rend == null) {
							doc.insertString(doc.getLength(),txt,norm);
						} else if(rend.equalsIgnoreCase(BOLD)) {
							doc.insertString(doc.getLength(),txt,boldSt);
						} else if(rend.equalsIgnoreCase(ITALIC)) {
							doc.insertString(doc.getLength(),txt,italSt);
						} else {
							System.out.println("Unknown rend type in setting discussion text (DiscDialog: 250)!");
						}
					}
				}
			} else {
				typeBox.setSelectedIndex(1);
				org.jdom.Element[] paras = TibDoc.toElementArray(disc.getChildren(P));
				for(int n=0; n<paras.length; n++) {
					for(Iterator it = paras[n].getContent().iterator();it.hasNext();) {
						Object child = it.next();
						if(child instanceof org.jdom.Text) {
							String txt = ((org.jdom.Text)child).getText();
							doc.insertString(doc.getLength(),txt,norm);
						} else if (child instanceof String) {
							String txt = (String)child;
							doc.insertString(doc.getLength(),txt,norm);
						} else if (child instanceof org.jdom.Element) {
							org.jdom.Element hiEl = (org.jdom.Element)child;
							String txt = hiEl.getText();
							String rend = hiEl.getAttributeValue(REND);
							if(rend == null) {
								doc.insertString(doc.getLength(),txt,norm);
							} else if(rend.equalsIgnoreCase(BOLD)) {
								doc.insertString(doc.getLength(),txt,boldSt);
							} else if(rend.equalsIgnoreCase(ITALIC)) {
								doc.insertString(doc.getLength(),txt,italSt);
							} else {
								System.out.println("Unknown rend type in setting discussion text (DiscDialog: 270)!");
							}
						}
					}
					doc.insertString(doc.getLength(),"\n\n",norm);
				}
			}
		} catch (BadLocationException ble) {
			System.out.println("A bad location was encountered in setting discussion dialog text!");
			ble.printStackTrace();
		}
	}


	// Interactive Methods
	public void showDiscussionDialog()
	{
		setVisible(true);
	}

	public org.jdom.Element writeDiscussion(org.jdom.Element tibl, java.util.List results)
	{
		if(results == null || results.size() == 0) { return tibl;}
		while(results.indexOf(null)>-1) {results.remove(null);}
		org.jdom.Element intelDecl, discElem, physDecl;
		intelDecl = tibl.getChild(INTDECL);
		Object item = null;
		if(intelDecl == null) {
			intelDecl = new org.jdom.Element(INTDECL);
			java.util.List tiblContent = tibl.getContent();
			for(Iterator it=tiblContent.iterator(); it.hasNext();) {
				item = it.next();
				if(item instanceof org.jdom.Element) {
					org.jdom.Element elem = (org.jdom.Element)item;
					if(elem.getName().equals(PHYSDEC)) { break; }
				}
			}
			int index = tiblContent.indexOf(item) + 1;
			if(index >= tiblContent.size()) {
				tiblContent.add(intelDecl);
			} else {
				tiblContent.add(index, intelDecl);
			}
		}
		discElem = null;
		item = null;
		for(Iterator it = intelDecl.getChildren(DISC).iterator(); it.hasNext();) {
			item = it.next();
			if(item instanceof org.jdom.Element) {
				org.jdom.Element child = (org.jdom.Element)item;
				if(child.getAttributeValue(TYPE).equals(SUMMARY)) {
					discElem = child;
					discElem.setContent(results);
					discElem.setAttribute(RESP,parent.getController().getEditorsInitials());
					discElem.setAttribute(REND,((String)typeBox.getSelectedItem()).toLowerCase());
					return tibl;
				}
			}
		}
		discElem = getDiscussion(parent.getController().getEditorsInitials());
		discElem.setContent(results);
		if(intelDecl.getChildren().size()<1) {
			org.jdom.Element dox = new org.jdom.Element(DOX);
			dox.setAttribute(TYPE,RELAT);
			org.jdom.Element tibLevel = TibDoc.getAncestor(TIBL,intelDecl);
			String level = (tibLevel == null?"section":tibLevel.getAttributeValue(LVL));
			dox.setText("NGB " + level + " proper.");
			intelDecl.addContent(dox);
		}
        intelDecl.addContent(discElem);
        return tibl;
	}

	public org.jdom.Element getDiscussion(String editor)
	{
		System.out.println("Getting discussion for " + editor);
		org.jdom.Element discElement;
		discElement = new org.jdom.Element(DISC);
		discElement.setAttribute(RESP,editor);
		discElement.setAttribute(TYPE,SUMMARY);
		StyledDocument doc = jta.getStyledDocument();
		String discType = getDiscussionType();
		if (discType.equalsIgnoreCase(BRIEF))
		{
			discElement.setAttribute(REND,BRIEF);
		} else {
			discElement.setAttribute(REND,FULL);
		}
		//System.out.println("The returned text is: \n" + TiblEdit.outputString(discElement));
		return discElement;
	}

	public String getDiscussionType()
	{
		return (String)typeBox.getSelectedItem();
	}

	public void setDiscussionType(String type) {
		if(type.equalsIgnoreCase(FULL)) {
			typeBox.setSelectedIndex(1);
		} else {
			typeBox.setSelectedIndex(0);
		}
	}

	public String getTextString()
	{
		String retText = new String();
		StyledDocument sd = jta.getStyledDocument();
		try {
			retText = sd.getText(0,sd.getLength());
		} catch (BadLocationException ble) {
		}
		return retText;
	}

	public int getReturnValue()
	{
		return returnType;
	}

	// Implementations
	public void actionPerformed(ActionEvent ae)
	{

		if(ae.getSource().equals(discBox)) {
			JComboBox source = (JComboBox)ae.getSource();
			ComboBoxElement cbe = (ComboBoxElement)source.getItemAt(sectionSelection);
			org.jdom.Element tibl = cbe.getElement();
			cbe.setElement(writeDiscussion(tibl,jta.makeXML()));
			cbe = (ComboBoxElement)source.getSelectedItem();
			sectionSelection = source.getSelectedIndex();
			jta.setContent(cbe.getElement());
			jta.grabFocus();
		}
	}


	// Constructors

	public DiscDialog(TibFrame owner, TibDoc doc)
	{
		super(owner,DISC_DIA_TITLE,true);
		setTibDoc(doc);
		init();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void closingWindow(WindowEvent we) {
				returnType = 0;
			}
		});
		//setDiscussionText();
		//setVisible(true);
	}

	public class DiscKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent ke) {
			String sname = (String)styleBox.getSelectedItem();
			if(sname != null) {jta.setCharacterAttributes(jta.getStyle(sname),true);}
		}

		public void keyReleased(KeyEvent ke) {
			if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
			   StyledDocument sd = jta.getStyledDocument();
			   int len = jta.getCaretPosition();

			   try {
				   if(typeBox.getSelectedIndex() == 0) {
						sd.remove(len-1,1);
				   } else {
					   sd.insertString(len,"\n",null);
				   }
			   } catch (BadLocationException ble) {
				   System.out.println("Bad location in trying to add extra newline in DiscDialog!");
			   }
			   jta.doDiscEnter(styleBox);

		   } else if(ke.getModifiers() == KeyEvent.ALT_MASK) {
			   //System.out.println("In switch keycode is: " + ke.getKeyCode() + " ["+ke.getKeyChar() + "]");
			   switch(ke.getKeyCode()) {
				   case KeyEvent.VK_C: styleBox.setSelectedIndex(5); break;  // if 'Alt-c' is pressed, choose citation
				   case KeyEvent.VK_I: styleBox.setSelectedIndex(4); break; // if 'Alt-i' is pressed, choose title
				   case KeyEvent.VK_L: styleBox.setSelectedIndex(6); break; // if 'Alt-l' is pressed, choose list
				   case KeyEvent.VK_S: styleBox.setSelectedIndex(1); break; // if 'Alt-s' is pressed, choose strong
				   case KeyEvent.VK_T: styleBox.setSelectedIndex(3); break; // if 'Alt-t' is pressed, choose title
				   case KeyEvent.VK_W: styleBox.setSelectedIndex(2); break; // if 'Alt-w' is pressed, choose weak
				   case KeyEvent.VK_N: styleBox.setSelectedIndex(0); break; // 'Alt-n' means normal
				   case KeyEvent.VK_B: typeBox.setSelectedIndex(0); break; // 'Alt-b' means a brief discussion.
				   case KeyEvent.VK_F: typeBox.setSelectedIndex(1); break;
			   }
		   }
	   }
   }

   public class MouseStyleListener extends MouseAdapter
   {
	   public void mouseReleased(MouseEvent me) {
		   int dot = jta.getCaretPosition();
		   AttributeSet as = jta.getStyledDocument().getCharacterElement(dot).getAttributes();
		   String caretStyle = as.getAttribute(AttributeSet.NameAttribute).toString();
		   if(caretStyle != null) {
			   styleBox.getModel().setSelectedItem(caretStyle);
		   } else {
			   System.out.println("Caret style is null! at " + dot);
		   }
	   }
   }

   public class ComboBoxElement
   {
	   org.jdom.Element theElement, intelDecl, disc;

	   public void setElement(org.jdom.Element el)
	   {
		   theElement = el;
	   }

	   public org.jdom.Element getElement()
	   {
		   return theElement;
	   }

	   public void setDiscussion(java.util.List content)
	   {
		   if(content != null) {disc.setContent(content);}
	   }

	   public org.jdom.Element getDiscussion()
	   {
	   	   return disc;
	   }

	   public String toString()
	   {
		   String level = theElement.getAttributeValue(LVL);
		   if(level != null && level.equalsIgnoreCase(TXT)) {
			   return "Whole Text";
		   }
		   org.jdom.Element head = theElement.getChild(HEAD);
		   if(head == null) {return "Unnamed Section!";}
		   return TibDoc.cleanString(head.getText());
	   }

	   public ComboBoxElement(org.jdom.Element el)
	   {
		   setElement(el);
	   }
   }

   public class DiscTextPane extends JTextPane
   {
	   Object[][] styleTable = {
			   {NORMSTY,StyleConstants.FontConstants.FontFamily,BASE_FONT_NAME,
			            StyleConstants.FontConstants.Bold,new Boolean(false),
			            StyleConstants.FontConstants.Italic,new Boolean(false),
			            StyleConstants.CharacterConstants.Underline,new Boolean(false)},

			   {STRONG, StyleConstants.FontConstants.Bold,new Boolean(true),
			            StyleConstants.FontConstants.Italic,new Boolean(false),
			            StyleConstants.CharacterConstants.Underline,new Boolean(false)},

			   {WEAK,   StyleConstants.FontConstants.Italic,new Boolean(true),
			            StyleConstants.FontConstants.Bold,new Boolean(false),
			            StyleConstants.CharacterConstants.Underline,new Boolean(false)},

			   {TIBSTY, StyleConstants.FontConstants.Italic,new Boolean(true),
			            StyleConstants.FontConstants.Bold,new Boolean(false),
			            StyleConstants.CharacterConstants.Underline,new Boolean(false),
			            StyleConstants.ColorConstants.Foreground,Color.blue},

			   {TITLE,  StyleConstants.CharacterConstants.Underline,new Boolean(true),
			            StyleConstants.FontConstants.Bold,new Boolean(false),
			            StyleConstants.FontConstants.Italic,new Boolean(false)},

			   {CITATION, StyleConstants.FontConstants.Italic,new Boolean(false),
			              StyleConstants.FontConstants.Bold,new Boolean(false),
			              StyleConstants.CharacterConstants.Underline,new Boolean(false),
			              StyleConstants.ColorConstants.Foreground,Color.red},

			   {LIST, StyleConstants.ParagraphConstants.LeftIndent,new Float(12.0F),
			          StyleConstants.FontConstants.Italic, new Boolean(false),
			          StyleConstants.FontConstants.Bold,new Boolean(false),
			          StyleConstants.CharacterConstants.Underline,new Boolean(false)}};

	   NamedStyle normal = null;

	   //Vector styleChoices = new Vector();

	   String NEW_PARA = "NewPara";

	   public void init()
	   {
           for(int r = 0; r<styleTable.length; r++)
           {
			   Object sname = styleTable[r][0];
			   NamedStyle newStyle = (NamedStyle)addStyle((String)sname,normal);
			   for(int c = 1; c<styleTable[r].length; c=c+2)
			   {
				   newStyle.addAttribute(styleTable[r][c],styleTable[r][c+1]);
			   }
			   if(r==0) {normal = newStyle;}
		   }
		   normal.addAttribute(StyleConstants.ParagraphConstants.LeftIndent, new Float(0.0F));
		   setStyle(normal.getName());
	   }

	   public void setStyle(String styleName)
	   {
			StyledDocument sd = getStyledDocument();
			Style newStyle = getStyle(styleName);
			if(newStyle == null) {System.out.println("New Style is null~!"); return;}
			if(styleName.equals(LIST)) {
				int caretPos = getCaretPosition();
				int offset = sd.getParagraphElement(caretPos).getStartOffset();
				Style presSty = sd.getLogicalStyle(caretPos);
				if(presSty == null || !presSty.getName().equals(LIST)) {
					int paraStart = sd.getParagraphElement(caretPos).getStartOffset();
					try {
						if(paraStart < 0) { return; }
						if(paraStart > sd.getLength()) { paraStart = sd.getLength(); }
						String paraBeg = (paraStart==sd.getLength()?new String():sd.getText(paraStart,LIST_MARK.length()));
						if(!paraBeg.equals(LIST_MARK)) {
							sd.insertString(paraStart,LIST_MARK,newStyle);
						}
					} catch (BadLocationException ble) {
						System.out.println("Bad location in inserting list mark at DiscDialog 692");
					}
				}
				setParagraphAttributes(newStyle,true);
				setCharacterAttributes(newStyle,true);

			} else if(styleName.equals(NORMSTY)) {
				int offset = sd.getParagraphElement(getCaretPosition()).getStartOffset();
				try {
					String prefix = sd.getText(offset,LIST_MARK.length());
					if(prefix.equals(LIST_MARK)) { sd.remove(offset,LIST_MARK.length()); }
				} catch (BadLocationException ble) {
					// It checks for a prefix of dash space in case it's a list
					// if not it throw this exception which just needs to be caught.
					// probably a bad way of doing it... should be reworked.
				}
				setParagraphAttributes(newStyle,true);
				setCharacterAttributes(newStyle,true);
			} else {
				setCharacterAttributes(newStyle,true);
			}
			// Stylechoices holds arrays which tell offset and style name for all style changes.
			//styleChoices.add(new String[] {Integer.toString(sd.getLength()),styleName});
			//System.out.println("added line for " + styleName + " to stylechoices!");
	   }

	   public String[] getStyleTypes(String type)
	   {
		   String[] outTable;
		   if(type.equals(titleTypes[0])) {
			   outTable = new String[styleTable.length-1];
		   } else {
			   outTable = new String[styleTable.length];
		   }
		   for(int r=0;r<styleTable.length;r++) {
			   if(r==6 && type.equals(titleTypes[0])) {break;}
			   String styName = (String)styleTable[r][0];
			   outTable[r] = styName;

		   }
		   return outTable;
	   }

	   public void doDiscEnter(JComboBox styleList)
	   {
			StyledDocument sd = getStyledDocument();
			String sty = (String)styleBox.getSelectedItem();
			Float indent = (Float)getParagraphAttributes().getAttribute(StyleConstants.ParagraphConstants.LeftIndent);

			if((sty != null && sty.equals(LIST)) || (indent != null && Float.compare(indent.floatValue(),12.0F) == 0)) {
				Style list = sd.getStyle(LIST);
				setParagraphAttributes(list,true);
				setCharacterAttributes(list,true);
				try {
					sd.insertString(sd.getLength(),LIST_MARK,list);
				} catch (BadLocationException ble) {
					System.out.println("Caught Bad location in inserting LIST_MARK: TiblEdit 760");
				}
				styleList.setSelectedIndex(6);
			} else {
				setParagraphAttributes(normal, true);
				setCharacterAttributes(normal, true);
				styleList.setSelectedIndex(0);
			}
	   }

	   public void setContent(org.jdom.Element tibl) {
		   setText("");
		   org.jdom.Element disc = null;
		   org.jdom.Element intelDecl = tibl.getChild(INTDECL);
		   if(intelDecl == null) {System.out.println("No intel decl!"); return;}
		   for(Iterator it = intelDecl.getChildren(DISC).iterator(); it.hasNext();) {
			   disc = (org.jdom.Element)it.next();
			   if(disc.getAttributeValue(TYPE).equalsIgnoreCase(SUMMARY)) {
				   break;
			   }
			   disc = null;
		   }
		   if(disc != null) {
			   String rend = disc.getAttributeValue(REND);
			   setDiscussionType(rend);
			   try {
				   setStyledText(disc);
			   } catch (BadLocationException ble) {
				   System.out.println("Bad location in writing out discussion into TextPane!");
				   System.out.println("Message: " + ble.getMessage());
			   }
		   }
	   }

	   public void setStyledText(org.jdom.Element elem) throws BadLocationException
	   {
		   StyledDocument sd = getStyledDocument();
		   String elemName = elem.getName();
		   if(elemName.equals(DISC)) {
			   java.util.List contents = elem.getContent();
			   for(Iterator it = contents.iterator();it.hasNext();) {
				   Object item = it.next();
				   if(item instanceof org.jdom.Text) {
						sd.insertString(sd.getLength(),((org.jdom.Text)item).getText(),sd.getStyle(NORMSTY));
				   } else {
					   setStyledText((org.jdom.Element)item);
				   }
			   }

		   } else if(elemName.equals(P)) {
			   java.util.List contents = elem.getContent();
			   if(contents == null || contents.size()==0) {System.out.println("A paragraph with nothing!"); return;}
			   for(Iterator it = contents.iterator();it.hasNext();) {
				   Object item = it.next();
				   if(item instanceof org.jdom.Text) {
						sd.insertString(sd.getLength(),((org.jdom.Text)item).getText(),sd.getStyle(NORMSTY));
				   } else {
					   setStyledText((org.jdom.Element)item);
				   }
			   }
			   if(getDiscussionType().equalsIgnoreCase(FULL))
				   sd.insertString(sd.getLength(),"\n\n",sd.getStyle(NORMSTY));

		   } else if(elemName.equals(TibConstants.LIST)) {
			   java.util.List contents = elem.getContent();
			   for(Iterator it = contents.iterator();it.hasNext();) {
				   org.jdom.Element item = (org.jdom.Element)it.next();
				   setStyledText(item);
			   }
		   } else if(elemName.equals(ITEM)) {
			   sd.insertString(sd.getLength(),LIST_MARK,sd.getStyle(LIST));
			   java.util.List contents = elem.getContent();
			   for(Iterator it = contents.iterator();it.hasNext();) {
				   Object item = it.next();
				   if(item instanceof org.jdom.Text) {
						sd.insertString(sd.getLength(),((org.jdom.Text)item).getText(),sd.getStyle(LIST));
				   } else {
					   setStyledText((org.jdom.Element)item);
				   }
			   }
			   int paraPos = sd.getLength()-1;
			   sd.insertString(sd.getLength(),"\n\n",sd.getStyle(LIST));
			   int paraStart = sd.getParagraphElement(paraPos).getStartOffset();
			   int paraEnd = sd.getParagraphElement(paraPos).getEndOffset();
			   sd.setParagraphAttributes(paraStart,paraEnd-paraStart+1,sd.getStyle(LIST),true);
		   } else if(elemName.equals(HI)) {
			   String rend = elem.getAttributeValue(REND);
			   String lang = elem.getAttributeValue(LANG);
			   String style = NORMSTY;
			   if(lang != null && lang.equals(TIB)) {
				   style = TIBSTY;
			   } else if (rend.equalsIgnoreCase(STRONG)) {
				   style = STRONG;
			   } else if (rend.equalsIgnoreCase(WEAK)) {
				   style = WEAK;
			   }
			   sd.insertString(sd.getLength(),elem.getText(),sd.getStyle(style));
		   } else if(elemName.equals(QUOTE)) {
			   sd.insertString(sd.getLength(),elem.getText(),sd.getStyle(CITATION));
		   } else if(elemName.equals(TibConstants.TITLE)) {
			   sd.insertString(sd.getLength(),elem.getText(),sd.getStyle(TITLE));
		   }
	   }

	   public java.util.List makeXML()
	   {
		   // new attempt
		   StyledDocument sd = getStyledDocument();
		   Object nameVal = null;
		   Object oldStyle = null;
		   String currSty = null;
		   String oldSty = null;
		   StringBuffer textRun = new StringBuffer();

		   org.jdom.Element p = null;
		   org.jdom.Element list = null;
		   org.jdom.Element openElem = null;

		   Vector returnList = new Vector();

		   boolean newPara = true;

		   for(int c=0; c<sd.getLength();c++) {
			   String ch = null;
			   try {
				   ch = sd.getText(c,1);
			   } catch(BadLocationException ble) {
				   System.out.println("Bad location: " + c);
			   }
			   AttributeSet as = sd.getCharacterElement(c).getAttributes();
			   nameVal = as.getAttribute(AttributeSet.NameAttribute);
			   currSty = (nameVal==null?null:nameVal.toString());
			   oldSty = (oldStyle==null?null:oldStyle.toString());

			   if (newPara) {
				   if(openElem != null) {
					   openElem.addContent(textRun.toString());
				   } else if(p!=null) {
					   p.addContent(textRun.toString());
				   }
				   textRun = new StringBuffer();

				   if(currSty.equals(LIST)) {
					   if(list == null) {
						   list = new org.jdom.Element(TibConstants.LIST);
						   list.setAttribute(TYPE,"bulleted");
					   } else {

						   openElem.addContent(textRun.toString());
						   textRun = new StringBuffer();
					   }
					   openElem = getDiscElement(LIST);
					   list = list.addContent(openElem);
				   } else {
					   if(list == null && p != null && (p.getContent().size()>1 || !p.getText().equals("")) ) {
						   returnList.add(p); }
					   if(list != null) { returnList.add(list); list = null;}
					   p = new org.jdom.Element(P);
				   }
			   }

			   if(ch.charAt(0) == (char)Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR) {
				   newPara = true;
				   if(list != null) {c++;}
				   continue;
			   } else {
				   newPara = false;
			   }

			   if(currSty != null && !currSty.equals(oldSty)) {
				   if(list == null) {
					   if(openElem == null) {
						   p.addContent(textRun.toString());
					   } else {
						   openElem.addContent(textRun.toString());
					   }
					   openElem = getDiscElement(currSty);
					   if(openElem != null) { p.addContent(openElem); }
				   } else {
					   openElem.addContent(textRun.toString());
					   if(!currSty.equals(LIST)) {
						   org.jdom.Element tempItem = openElem;
						   openElem = getDiscElement(currSty);
						   if(openElem != null) {
							   tempItem.addContent(openElem);
						   } else if(tempItem != null) {
							   openElem = tempItem;
						   }
					   } else if(!openElem.getName().equals(ITEM)) {
						   openElem = openElem.getParent();
					   }
				   }
				   textRun = new StringBuffer();
			   }



			   // for all
			   textRun.append(ch);
			   if(textRun.indexOf(LIST_MARK) == 0) {
				   textRun = textRun.delete(0,LIST_MARK.length());
			   }
			   oldStyle = nameVal;

		   } // End of for character loop
		   if(textRun != null) {
			   if(openElem != null) {
				   openElem.addContent(textRun.toString());
			   } else if(p!= null) {
				   p.addContent(textRun.toString());
			   }
		   }
		   if(list != null) {returnList.add(list); } else {returnList.add(p);}
		   return returnList;

	   }



	   private org.jdom.Element getDiscElement(String styleType)
	   {
		   org.jdom.Element retElem = null;
		   if(styleType.equals(STRONG)) {
			   retElem = new org.jdom.Element(HI);
			   retElem.setAttribute(REND,STRONG.toLowerCase());
		   } else if(styleType.equals(WEAK)) {
			   retElem = new org.jdom.Element(HI);
			   retElem.setAttribute(REND,WEAK.toLowerCase());
		   } else if(styleType.equals(TIBSTY)) {
			   retElem = new org.jdom.Element(HI);
			   retElem.setAttribute(LANG,TIB);
		   } else if(styleType.equals(TITLE)) {
			   retElem = new org.jdom.Element(TibConstants.TITLE);
		   } else if(styleType.equals(CITATION)) {
			   retElem = new org.jdom.Element(QUOTE);
		   } else if(styleType.equals(LIST)) {
			   retElem = new org.jdom.Element(ITEM);
		   }
		   return retElem;
	   }

	   private java.util.List processParagraph(StyledDocument sd, int start, int endPos)
	   {
		   org.jdom.Element currentEl = null;
		   org.jdom.Text textRun = new org.jdom.Text(null);
		   Vector returnList = new Vector();
		   NamedStyle lastAs = null;
		   for(int c=start;c<= endPos;c++) {
			   NamedStyle as = (NamedStyle)sd.getCharacterElement(c).getAttributes();
			   if(as == null) {continue;}
			   String styName = as.getName();
			   returnList.add(styName);
			   lastAs = as;
		   }
		   return returnList;
	   }

	   public DiscTextPane()
	   {
		   super();
		   init();
	   }

   }


public class StyleBoxRenderer extends JLabel
                       implements ListCellRenderer {
    public StyleBoxRenderer() {
        setOpaque(true);
    }
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
		String item = value.toString();
        setText(item);
		this.setFont(this.getFont().deriveFont(Font.PLAIN));
		this.setForeground(Color.black);
		this.setHorizontalAlignment(SwingConstants.LEFT);

        if(item.equals("Tibetan")) {
			this.setForeground(Color.blue);

        } else if(item.equals("Citation")) {
			this.setForeground(Color.red);

        } else if(item.equals("Strong")) {
			this.setFont(this.getFont().deriveFont(Font.BOLD));

        } else if(item.equals("Weak")) {
			this.setFont(this.getFont().deriveFont(Font.ITALIC));

		} else if(item.equals("Title")) {
			this.setText("<html><body><u>Title</u></body></html>");  // The only way to get a label underlined!!

		} else if(item.equals("List")) {
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setText("- List");

        }

        return this;
    }
}

}
