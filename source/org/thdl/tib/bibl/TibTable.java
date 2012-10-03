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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
* This element is a wrapper element for a JTable that sets up two kinds of tables for editing a Tibbibl--one for
* displaying various editions and one for displaying alternative readings.
*
* @author Than Garson, Tibetan and Himalayan Digital Library
*/

public class TibTable extends Observable implements TibConstants, ActionListener
{
	// Attributes
	private JTable theTable;
	private TibTableModel ttm;
	private org.jdom.Element app, pagination;
	private IDFactory idf;
	private Hashtable pageHash;
	private int type;
	private boolean editable;
	private int mainrow;
	private TibTableCellRenderer ttcr;


	// Constants
	public static int EDS_CONSULTED = 0;
	public static int APP			= 1;

	// Header Constants
	private static String ED_NAME_HEAD = "Edition Name";
	private static String ED_HEAD	= "Edition";
	private static String VOL_HEAD	= "Volume";
	private static String TEXT_HEAD	= "Text No.";
	private static String PAGE_HEAD	= "Pagination";
	private static String READING	= "Reading";
	private static String ID_HEAD	= "Id. No.";
	private static String IS_MAIN	= "Main Reading";

	public static String MAINED		= "Set Main Edition";
	public static String REMOVE_RDG = "Remove Reading";
	public static String SUB			= "Done";
	public static String CANC	 		= "Cancel";

	private static String[] DEFAULT_HEADS = {ED_NAME_HEAD, ID_HEAD,VOL_HEAD,PAGE_HEAD};
	private static String[] ED_CON_HEADS = {ED_HEAD,TEXT_HEAD,VOL_HEAD,PAGE_HEAD};
	private static String[] APP_HEADS	= {ED_HEAD,VOL_HEAD,PAGE_HEAD,READING,IS_MAIN};

	// Accessor methods
	public void setTable(JTable jt)
	{
		theTable = jt;
		ttcr = new TibTableCellRenderer();
		ttcr.setMainRow(getMainRow());
		String st = new String();
		jt.setDefaultRenderer(st.getClass(),ttcr);
	}

	public JTable getTable()
	{
		return theTable;
	}

	public void setTibTableModel(TibTableModel ttm)
	{
		this.ttm = ttm;
	}

	public TibTableModel getTibTableModel()
	{
		return ttm;
	}

	public void setApp(org.jdom.Element ap)
	{
		app = ap;
	}

	public org.jdom.Element getApp()
	{
		return app;
	}

	public void setIDFactory(IDFactory idf)
	{
		if(idf == null) {System.out.println("IDF is null in TibTable!");}
		this.idf = idf;
	}

	public IDFactory getIDFactory()
	{
		return idf;
	}

	public void setPagination(org.jdom.Element page)
	{
		pagination = page;
		pageHash = new Hashtable();
		Iterator it = page.getChildren(NUM).iterator();
		while(it.hasNext())
		{
			org.jdom.Element numChild = (org.jdom.Element)it.next();
			String ed = numChild.getAttributeValue(CORRESP);
			String pageNums = TibDoc.cleanString(numChild.getText());
			pageHash.put(ed,pageNums);
		}
	}

	public org.jdom.Element getPagination()
	{
		return pagination;
	}

	public Hashtable getPageHash()
	{
		return pageHash;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setMainRow()
	{
		int oldrow = getMainRow();
		int row = theTable.getSelectedRow();
		String firstCell = (String)theTable.getValueAt(row,0);
		if(firstCell == null || firstCell.equals("")) {return;}
		setMainRow(row);
		ttcr.setMainRow(row);
		ttm.setValueAt(new Boolean(false),oldrow,4);
		ttm.setValueAt(new Boolean(true),row,4);
		theTable.repaint();
	}

	public void setMainRow(int mr)
	{
			mainrow = mr;
	}

	public int getMainRow()
	{
		return mainrow;
	}

	public String[] getHeaders()
	{
		if(type == EDS_CONSULTED) {
			return ED_CON_HEADS;
		} else if(type == APP) {
			return APP_HEADS;
		}
		return DEFAULT_HEADS;
	}

	// Helper methods
	/**
	*<p>
	* This is an "overriding" of the setTable(JTable jt) accessor, which simply sets the table variable
	* to the given parameter. This method on the other hand sets the table
	* depending on the {@link #type} of TibTable this is. If this TibTable is constructed
	* solely with an {@link IDFactory}, then it is an {@link #EDS_CONSULTED} type. If it is
	* constructed with an Apparatus element, IDFactory, and a Pagination element, then it
	* is an {@link #APP}, or apparatus, type. This method will construct the table model
	* accordingly.
	* <p>
	*/
	public void setTable()
	{
		if(type == EDS_CONSULTED) {

			Vector eds = idf.getEditionsConsulted();
			Object[][] tdata = new Object[eds.size()][ED_CON_HEADS.length];
			int e = 0;
			Iterator it = eds.iterator();
			while(it.hasNext()) {
				String ed = (String)it.next();
				if(ed.equals(NG)) {continue;}
				tdata[e][0] = ed;
				tdata[e][1] = idf.getTextNum(ed);
				tdata[e][2] = idf.getVolNum(ed);
				tdata[e][3] = idf.getPagination(ed);
				for(int n=0; n<4; n++) {
					if(tdata[e][n] == null) {tdata[e][n] = new String("");}
				}
				e++;
			}
			setTibTableModel(new TibTableModel(tdata,ED_CON_HEADS));
			setTable(new JTable(getTibTableModel()));

		} else if (type == APP) {
			doAppTable();
		}

	}

	public void doAppTable()
	{
		Vector eds = new Vector(idf.getEditionsConsulted());
		java.util.List readings = getApp().getChildren();
		Object[][] tdata = new Object[eds.size()+1][APP_HEADS.length];
		for(int r=0;r<eds.size()+1;r++) {
			for(int c = 0;c<4; c++) {
				tdata[r][c] = new String();
			}
			tdata[r][4] = new Boolean(false);
		}
		int c = 0;
		Iterator it = readings.iterator();
		boolean isMain;
		while(it.hasNext()) {
			isMain = false;
			org.jdom.Element child = (org.jdom.Element)it.next();
			if(child.getName().equals(LM)) { setMainRow(c); isMain = true;}
			String ed = child.getAttributeValue(WIT);
			if(ed == null) {
				System.out.println(" A null witness!\n" + TiblEdit.outputString(child));
				continue;
			}
			eds.remove(ed);
			String volNum, pageNum, reading;
			if(idf.hasEdition(ed)) {
				volNum = idf.getVolNum(ed);
				pageNum = (String)pageHash.get(ed);
			} else {
				volNum = new String();
				pageNum = new String();
			}
			reading = TibDoc.cleanString(child.getText());
			if (reading == null) {reading = new String();}
			tdata[c][0] = ed;
			tdata[c][1] = volNum;
			tdata[c][2] = pageNum;
			tdata[c][3] = reading;
			tdata[c++][4] = new Boolean(isMain);
		}
		it = eds.iterator();
		while(it.hasNext()) {
			String ed = (String)it.next();
			if(ed.trim().equals("Tk1") || ed.trim().equals("Tb1") || ed.trim().equals(NG)) {continue;}
			String volNum, pageNum, reading;
			if(idf.hasEdition(ed)) {
				if(idf.getTextNum(ed).indexOf("ot found")>-1) {continue;}
				volNum = idf.getVolNum(ed);
				pageNum = (String)pageHash.get(ed);
			} else {
				volNum = new String();
				pageNum = new String();
			}
			tdata[c][0] = ed;
			tdata[c][1] = volNum;
			tdata[c++][2] = pageNum;
		}

		setTibTableModel(new TibTableModel(tdata,APP_HEADS));
		setTable(new JTable(getTibTableModel()));
	}

	public JSplitPane getAppPanel()
	{
		JScrollPane tablePane;
		JPanel buttonPanel;
		JButton mainEdButton, removeButton, submitButton, cancelButton;

		JSplitPane fullPane;

		tablePane = new JScrollPane(getTable());

		buttonPanel = new JPanel(new GridLayout(4,1));
		buttonPanel.setSize(new Dimension(200,250));

		mainEdButton = new JButton(MAINED);
		buttonPanel.add(getButtonPanel(mainEdButton));
		mainEdButton.addActionListener(this);

		removeButton = new JButton(REMOVE_RDG);
		buttonPanel.add(getButtonPanel(removeButton));
		removeButton.addActionListener(this);

		submitButton = new JButton(SUB);
		buttonPanel.add(getButtonPanel(submitButton));
		submitButton.addActionListener(this);

		cancelButton = new JButton(CANC);
		buttonPanel.add(getButtonPanel(cancelButton));
		cancelButton.addActionListener(this);

		buttonPanel.setBackground(Color.white);
		fullPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tablePane,buttonPanel);

		return fullPane;
	}

	public JPanel getButtonPanel(JButton jb)
	{
		jb.setPreferredSize(new Dimension(150,20));
		JPanel butPanel = new JPanel();
		butPanel.setBackground(Color.white);
		butPanel.add(jb);
		return butPanel;
	}

	public void setEditable(boolean torf)
	{
		editable = torf;
		ttm.setEditable(torf);
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void doSubmit()
	{
		Hashtable args = new Hashtable();
		args.put(AP,app);
		args.put(PAGIN,pagination);
		args.put(TABLE,ttm);
		args.put(TYPE,APP_SUBMIT);
		setChanged();
		notifyObservers(args);
	}

	public void doRemove()
	{
		int row = theTable.getSelectedRow();
		String sigla = theTable.getValueAt(row,0).toString();
		boolean isMain = false;
		try {
			isMain = ((Boolean)theTable.getValueAt(row,4)).booleanValue();
		} catch (ClassCastException cce) {
			System.out.println("Fifth column is not a boolean! (" + row + ":" + sigla +")");
		}
		if(isMain) {
			JOptionPane.showMessageDialog(null,"You cannot remove the main reading!",
											   "Unable to Remove Reading",
											   JOptionPane.ERROR_MESSAGE);
		} else {
			deleteRow(row);
		}
	}

	public void doCancel()
	{
		Hashtable args = new Hashtable();
		args.put(TYPE,CANCEL);
		setChanged();
		notifyObservers(args);
	}

	public void deleteRow(int row)
	{
		Object[][] data = ttm.getData();
		Object[] colHeads = ttm.getColumnNames();

		if(row>=data.length) {return;}

		Object[][] newData = new Object[data.length-1][colHeads.length];
		int n = -1;

		for(int r=0; r<data.length;r++)
		{
			if(r == row) {continue;}
			n++;
			newData[n] = data[r];
		}

		setTibTableModel(new TibTableModel(newData,colHeads));
		theTable.setModel(getTibTableModel());

		if(getMainRow()>row) {
			setMainRow(getMainRow()-1);
			ttcr.setMainRow(getMainRow());
		}

		theTable.repaint();
	}

	// Constructors

	public TibTable()
	{
		setType(-1);
	}

	public TibTable(Object[][] in_data, int type)
	{
		setType(type);
		TibTableModel ttm = new TibTableModel(in_data, getHeaders());
		setTibTableModel(ttm);
		setTable(new JTable(ttm));
	}

	public TibTable(IDFactory idf)
	{
		setType(EDS_CONSULTED);
		setIDFactory(idf);
		setTable();
	}

	public TibTable(org.jdom.Element app, IDFactory idf, org.jdom.Element page)
	{
		setType(APP);
		setApp(app);
		setIDFactory(idf);
		setPagination(page);
		setTable();
		setEditable(true);
	}
	// Listener Methods
	public void actionPerformed(ActionEvent ae)
	{
		String actionType = ae.getActionCommand();
		if(actionType.equals(MAINED)) {
			setMainRow();
		} else if (actionType.equals(REMOVE_RDG)) {
			doRemove();
		} else if (actionType.equals(SUB)) {
			doSubmit();
		} else if (actionType.equals(CANC)) {
			doCancel();
		}
	}


	// Table model as inner class
	public class TibTableModel extends AbstractTableModel
	{
	    	Object[] columnNames;
			Object[][] data;

			boolean editable;
			boolean isEditions;

		public void setEditable(boolean torf)
		{
			editable = torf;
		}

	    public String getColumnName(int col)
	    {
			if(col<0 || col>=getColumnCount()) {return new String();}
	        return columnNames[col].toString();
	    }

	    public Object[] getColumnNames()
	    {
			return columnNames;
		}

		public Object[][] getData()
		{
			return data;
		}

	    public int getRowCount() { return data.length; }

	    public int getMainRow() {
			for(int r=0;r<getRowCount();r++)
				if(isMainRow(r)) {return r;}
			return -1;
		}

		public boolean isMainRow(int r) {
			int lastCol = getColumnCount()-1;
			String colName = getColumnName(lastCol);
			if(colName != null && colName.equals(IS_MAIN)) {
				return ((Boolean)getValueAt(r,lastCol)).booleanValue();
			}
			return false;
		}

	    public int getColumnCount() { return columnNames.length; }

		public Class getColumnClass(int col) {
			if(col<0 || col>getColumnCount()) {return null;}
			Object val = getValueAt(0, col);
			if(val == null) {val = new String();}
			return val.getClass();}

	    public Object getValueAt(int row, int col)
	    {
			int rowCount = getRowCount();
			int colCount = getColumnCount();
			if(row<0 || row>rowCount || col<0 || col>colCount) {return "";}
	        return data[row][col];
	    }

	    public boolean isCellEditable(int row, int col)
		{
			if(getColumnName(col).equals(IS_MAIN)) {return false;}
			return editable;
		}

	    public void setValueAt(Object value, int row, int col)
	    {
			data[row][col] = value;
	    }

	    public void setValueAt(int row, int col, String value)
	    {
			value = TibDoc.cleanString(value);
			data[row][col] = value;
		}

		public String getStringAt(int row, int col)
		{
			Object datum = getValueAt(row,col);
			if(datum instanceof String) {
				return (String)datum;
			}
			return null;
		}

		// Helper methods
		public int findColumn(String colName)
		{
			for(int c=0; c<getColumnCount(); c++)
			{
				String name = getColumnName(c);
				if (name.equals(colName)) {return c;}
			}
			return -1;
		}

		public Vector getWitnesses()
		{
			Vector witnesses = new Vector();
			for(int r=0; r<getRowCount(); r++) {
				if(isValidRow(r)) {witnesses.add(getStringAt(r,0));}
			}
			return witnesses;
		}

		public boolean isValidRow(int r) {
			String sigla = getStringAt(r,0);
			if(sigla != null && !sigla.equals("") && !sigla.equals(" ")) {
				String read = getStringAt(r,3);
				if(read != null && !read.equals("") && !read.equals(" ")) {
					return true;
				}
			}
			return false;
		}


		// Contructor for TibTableModel
		public TibTableModel(Object[][] d, Object[] cn)
		{
			if(d[0].length != cn.length) {System.out.println("Number of column headers does not match number of columns in TibTableModel!");}
			editable = false;
			data = d;
			columnNames = cn;
		}

		public TibTableModel(Object[][] d, Object[] cn, boolean editable)
		{
			this.editable = editable;
			data = d;
			columnNames = cn;
		}
	}

	public class TibTableCellRenderer extends DefaultTableCellRenderer
	{
		int mainrow;

		public void setMainRow(int mr)
		{
			mainrow = mr;
		}

		public int getMainRow()
		{
			return mainrow;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
        {
			Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			if(row == mainrow) {
				Font oldFont, newFont;
				oldFont = c.getFont();
				newFont = oldFont.deriveFont(Font.BOLD);
				c.setFont(newFont);
			}
			return c;
		}

		public TibTableCellRenderer()
		{
			super();
			mainrow = -1;
		}
	}

}

