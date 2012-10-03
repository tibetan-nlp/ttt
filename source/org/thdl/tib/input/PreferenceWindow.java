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

package org.thdl.tib.input;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.beans.PropertyChangeListener ;
import java.beans.PropertyChangeEvent ;
import java.io.File;

import org.thdl.tib.input.DictionaryLoadState ;

/** Shows a standard dialog window to set the preferences
    for the tibetan and roman script used
*/
public class PreferenceWindow implements ActionListener, ItemListener
{
    private JDialog dialog;
	private JComboBox tibetanFontSizes;
	private JComboBox romanFontSizes;
	private JComboBox romanFontFamilies;
	private JCheckBox dictionaryEnabled ;
	private ButtonGroup dictionaryType ;
	private JRadioButton dictionaryLocal ;
	private JRadioButton dictionaryRemote ;
	private JLabel dictionaryPathLabel ;
	private JTextField dictionaryPath ;
	private JButton dictionaryBrowse ;
    private JOptionPane pane ;

	boolean valDictionaryEnabled ;
	boolean valDictionaryLocal ;
	String valDictionaryPath ;

	private DuffPane dp;
    
    public PreferenceWindow(Component parent, DuffPane dp)
    {
        this.dp = dp;
   		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = genv.getAvailableFontFamilyNames();

        DictionarySettings ds = dp.getDictionarySettings () ;
		valDictionaryEnabled = ds.isEnabled () ;
		valDictionaryLocal = ds.isLocal () ;
		valDictionaryPath = ds.getPathOrUrl () ;


		JPanel tibetanPanel;

		tibetanPanel = new JPanel();
		tibetanPanel.setBorder(BorderFactory.createTitledBorder("Set Tibetan Font Size"));
		tibetanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		tibetanFontSizes.setMaximumSize(tibetanFontSizes.getPreferredSize());
		tibetanFontSizes.setSelectedItem(String.valueOf(dp.getTibetanFontSize()));
		tibetanFontSizes.setEditable(true);
		tibetanPanel.add(tibetanFontSizes);

		JPanel romanPanel;

		romanPanel = new JPanel();
		romanPanel.setBorder(BorderFactory.createTitledBorder("Set non-Tibetan Font and Size"));
		romanFontFamilies = new JComboBox(fontNames);
		romanFontFamilies.setMaximumSize(romanFontFamilies.getPreferredSize());
		romanFontFamilies.setSelectedItem(dp.getRomanFontFamily());
		romanFontFamilies.setEditable(true);
		romanFontSizes = new JComboBox(new String[] {"8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","48","72"});
		romanFontSizes.setMaximumSize(romanFontSizes.getPreferredSize());
		romanFontSizes.setSelectedItem(String.valueOf(dp.getRomanFontSize()));
		romanFontSizes.setEditable(true);
		romanPanel.setLayout(new GridLayout(1,2));
		romanPanel.add(romanFontFamilies);
		romanPanel.add(romanFontSizes);

		JPanel dictionaryPanel ;
		dictionaryPanel = new JPanel () ;
		dictionaryPanel.setBorder ( BorderFactory.createTitledBorder ( "Dictionary" ) ) ;
		dictionaryEnabled = new JCheckBox ( "Enable Dictionary Support" ) ;
		dictionaryEnabled.setMnemonic ( KeyEvent.VK_E ) ;
		dictionaryEnabled.setActionCommand ( "SetDictionaryEnable" ) ;
		dictionaryEnabled.addItemListener ( this ) ;
		dictionaryLocal = new JRadioButton ( "Local") ;
		dictionaryLocal.setActionCommand ( "SetDictionaryLocal" ) ;
		dictionaryLocal.setMnemonic ( KeyEvent.VK_L ) ;
		dictionaryLocal.addActionListener ( this ) ;
		dictionaryRemote = new JRadioButton ( "Remote") ;
		dictionaryRemote.setMnemonic ( KeyEvent.VK_R ) ;		
		dictionaryRemote.setActionCommand ( "SetDictionaryRemote" ) ;
		dictionaryRemote.addActionListener ( this ) ;
		dictionaryPathLabel = new JLabel ( "Path" ) ;
		dictionaryPath = new JTextField () ;
		dictionaryBrowse = new JButton ( "Browse" ) ;				
		dictionaryBrowse.setMnemonic ( KeyEvent.VK_B ) ;
		dictionaryBrowse.setActionCommand ( "BrowseDictionaryPath" ) ;
		dictionaryBrowse.addActionListener ( this ) ;
		dictionaryPanel.setLayout ( new GridBagLayout () ) ;
		GridBagConstraints gc = new GridBagConstraints () ;
		gc.anchor = GridBagConstraints.NORTHWEST ;
		gc.fill = GridBagConstraints.BOTH ;
		gc.weightx = 1.0 ;
		gc.weighty = 1.0 ;
		gc.gridwidth = GridBagConstraints.REMAINDER ;
		gc.insets = new Insets ( 1, 1, 1, 1 ) ;
		dictionaryPanel.add ( dictionaryEnabled, gc ) ;
		gc.gridwidth = GridBagConstraints.RELATIVE ;
		dictionaryPanel.add ( dictionaryLocal, gc ) ;
		gc.weightx = 0.0 ;
		gc.gridwidth = GridBagConstraints.REMAINDER ;
		dictionaryPanel.add ( dictionaryRemote, gc ) ;
		gc.weightx = 1.0 ;
		gc.gridwidth = GridBagConstraints.REMAINDER ;
		dictionaryPanel.add ( dictionaryPathLabel, gc ) ;
		gc.gridwidth = GridBagConstraints.RELATIVE ;
		gc.weightx = 3.0 ;
		dictionaryPanel.add ( dictionaryPath, gc ) ;
		gc.weightx = 0.0 ;
		gc.gridwidth = GridBagConstraints.REMAINDER ;
		dictionaryPanel.add ( dictionaryBrowse, gc ) ;		

		dictionaryType = new ButtonGroup () ;
		dictionaryType.add ( dictionaryLocal ) ;
		dictionaryType.add ( dictionaryRemote ) ;

		JPanel preferencesPanel = new JPanel();
		preferencesPanel.setLayout(new BoxLayout(preferencesPanel,BoxLayout.Y_AXIS));
		preferencesPanel.add(tibetanPanel);
		preferencesPanel.add(romanPanel);
		preferencesPanel.add(dictionaryPanel);

		pane = new JOptionPane(preferencesPanel);
		updateDictionaryGui () ;
		dialog = pane.createDialog(parent, "Preferences");

		dialog.setDefaultCloseOperation ( JDialog.DO_NOTHING_ON_CLOSE ) ;

		pane.addPropertyChangeListener ( new PropertyChangeListener ()
			{
				public void propertyChange ( PropertyChangeEvent e )
				{
					if ( /*dialog.isVisible () &&*/ e.getSource () == pane )
					{
						if ( pane.getValue () != null && 							 
							 e.getPropertyName ().equals ( JOptionPane.VALUE_PROPERTY ) )
						{							
							if ( pane.getValue ().toString ().equals ( JOptionPane.UNINITIALIZED_VALUE.toString () ) )
								return ;

							int val = ((Integer)pane.getValue ()).intValue () ;

							if ( val == JOptionPane.OK_OPTION )
							{
								if ( validateInput () )
                                {

									dialog.setVisible ( false ) ;
                                }
                                else
                                {
                                    dialog.setVisible ( true ) ;
                                }
							}
							else if ( val == JOptionPane.CANCEL_OPTION )
							{	
								dialog.setVisible ( false ) ;
							}
						}
					}
				}
			} ) ;
    }
    
    private static int stringToInt(String s)
    {
        int num;
 		try 
 		{
			num = Integer.parseInt(s);
		}
		catch (NumberFormatException ne) {
            num = -1;
		}
		return num;
    }
    
    public int getTibetanFontSize()
    {
		return stringToInt(tibetanFontSizes.getSelectedItem().toString());
    }
    
    public int getRomanFontSize()
    {
        return stringToInt(romanFontSizes.getSelectedItem().toString());
    }
    
    public String getRomanFont()
    {
        return romanFontFamilies.getSelectedItem().toString();
    }

	public void setDictionaryEnabled ( boolean enabled )
	{
		valDictionaryEnabled = enabled ;
	}

	public boolean getDictionaryEnabled ()
	{
		return valDictionaryEnabled ;
	}

	public void setDictionaryLocal ( boolean local )
	{
		valDictionaryLocal = local ;
	}

	public boolean getDictionaryLocal ()
	{
		return valDictionaryLocal ;
	}

	public void setDictionaryPath ( String path )
	{
		valDictionaryPath = path ;
	}

	public String getDictionaryPath ()
	{
		return valDictionaryPath ;
	}

	private void readDictionaryGui ()
	{
		valDictionaryEnabled = dictionaryEnabled.isSelected () ;
		valDictionaryLocal = dictionaryLocal.isSelected () ;
		valDictionaryPath = dictionaryPath.getText () ;
	}

	private void updateDictionaryGui ()
	{
		if ( ! valDictionaryEnabled )
		{
			dictionaryLocal.setEnabled ( false ) ;
			dictionaryRemote.setEnabled ( false ) ;
			dictionaryPathLabel.setEnabled ( false ) ;
			dictionaryPath.setEnabled ( false ) ;
			dictionaryBrowse.setEnabled ( false ) ;
		}
		else
		{
			dictionaryLocal.setEnabled ( true ) ;
			dictionaryRemote.setEnabled ( true ) ;

			if ( valDictionaryLocal )			
			{
				dictionaryPathLabel.setEnabled ( true ) ;
				dictionaryPathLabel.setText ( "Path" ) ;
				dictionaryPath.setEnabled ( true ) ;
				dictionaryBrowse.setEnabled ( true ) ;				
			}
			else
			{
				dictionaryPathLabel.setEnabled ( true ) ;
				dictionaryPathLabel.setText ( "URL" ) ;
				dictionaryPath.setEnabled ( true ) ;
				dictionaryBrowse.setEnabled ( false ) ;				
			}
		}

		if ( valDictionaryEnabled != dictionaryEnabled.isSelected () )
		{
			dictionaryEnabled.setSelected ( valDictionaryEnabled ) ;
		}

		if ( valDictionaryLocal != dictionaryLocal.isSelected () )
		{
			dictionaryLocal.setSelected ( valDictionaryLocal ) ;
			dictionaryRemote.setSelected ( !valDictionaryLocal ) ;
		}

		dictionaryPath.setText ( valDictionaryPath ) ;
	}

	private void guiSetDictionaryEnabled ( boolean enabled )
	{
		setDictionaryEnabled ( enabled ) ;
		updateDictionaryGui () ;
	}

	public void actionPerformed ( ActionEvent e )
	{
		if ( e.getActionCommand () == "SetDictionaryLocal" )
		{
			setDictionaryLocal ( true ) ;
			updateDictionaryGui () ;
		}
		else if ( e.getActionCommand() == "SetDictionaryRemote" ) 
		{
			setDictionaryLocal ( false ) ;
			updateDictionaryGui () ;
		}
		else if ( e.getActionCommand() == "BrowseDictionaryPath" )
		{
            browseDictionaryPath () ;			
		}
	}

	public void itemStateChanged ( ItemEvent e )
	{
		Object source = e.getItemSelectable () ;

		if ( dictionaryEnabled == source )
		{			
			guiSetDictionaryEnabled ( ( ItemEvent.SELECTED == e.getStateChange () ) ? true : false ) ;
		}
	}

    /** This returns only when the user has closed the dialog */
    public void show()
    {
        DictionarySettings ds = dp.getDictionarySettings () ;
		valDictionaryEnabled = ds.isEnabled () ;
		valDictionaryLocal = ds.isLocal () ;
		valDictionaryPath = ds.getPathOrUrl () ;
		updateDictionaryGui () ;
        
        pane.setOptionType ( JOptionPane.OK_CANCEL_OPTION ) ;

		dialog.show();
    }

	private final String LOCAL_DICTIONARY_EXTENSION = ".dic" ;

    protected void browseDictionaryPath ()
    {
        //
        // TODO - JFileChooser takes long to instantiate, 
        // put instance in GlobalResourceHolder and 
        // init it in a separate thread (same with dictionary)
        //
        JFileChooser fc = new JFileChooser ()
        {
			public String getName ( File f )
			{
				if ( f.isDirectory () )
					return super.getName ( f ) ;

				String fileName = f.getName () ;
				return fileName.substring ( 0, fileName.lastIndexOf ( LOCAL_DICTIONARY_EXTENSION ) ) ;
			}
        } ;

		//
		// a simple file filter that will make sure the remaining two files (other than the FILE.dic)
		// are present.
		//
		FileFilter ff = new FileFilter ()
		{
			public boolean accept ( File f )
			{
				//
				// a directory is always good
				//
				if ( f.isDirectory () )
					return true ;

				//
				// the extension must be .dic
				//
				if ( ! f.getAbsolutePath ().endsWith ( LOCAL_DICTIONARY_EXTENSION ) )
					return false ;

				//
				// we know the .dic file exists, we need to make sure the accompanying .wrd and .def
				// exists too
				//
				String fileNameDic = f.getAbsolutePath () ;
				String fileNameDef = fileNameDic.replace ( LOCAL_DICTIONARY_EXTENSION, ".def" ) ;
				String fileNameWrd = fileNameDic.replace ( LOCAL_DICTIONARY_EXTENSION, ".wrd" ) ;
				File fileDef = new File ( fileNameDef ) ;
				File fileWrd = new File ( fileNameWrd ) ;

				if ( ! fileDef.exists () )
					return false ;

				if ( ! fileWrd.exists () )
					return false ;

				return true ;
			}

			public String getDescription ()
			{
				return "Dictionary Files (*" + LOCAL_DICTIONARY_EXTENSION + ")" ;
			}
		};

		fc.setAcceptAllFileFilterUsed ( false ) ;
		fc.setFileFilter ( ff ) ;

        fc.setCurrentDirectory ( new File ( valDictionaryPath ).getParentFile () ) ;
        if ( JFileChooser.APPROVE_OPTION == fc.showOpenDialog ( pane ) )
        {
			String newDictionaryPath = fc.getSelectedFile ().toString () ;
			valDictionaryPath = newDictionaryPath.substring ( 0, newDictionaryPath.lastIndexOf ( LOCAL_DICTIONARY_EXTENSION ) ) ; ;

			updateDictionaryGui () ;
        }
    }

	/**
	 * validateInput
	 */
	protected boolean validateInput ()
	{   		
		int size;
		String font;

		readDictionaryGui () ;
		valDictionaryEnabled = getDictionaryEnabled () ;
		valDictionaryLocal = getDictionaryLocal () ;
		valDictionaryPath = getDictionaryPath () ;
			
		size = getTibetanFontSize();
		if (size>0) dp.setByUserTibetanFontSize(size);
		
		size = getRomanFontSize();
		if (size==-1) size = dp.getRomanFontSize();
		font = getRomanFont();
		dp.setByUserRomanAttributeSet(font, size);

		dp.setByUserDictionarySettings ( valDictionaryEnabled, valDictionaryLocal, valDictionaryPath ) ;

		//
		// wait for dictionary status to be either READY or ERROR LOADING
		//
		dp.waitForDictionary () ;
		int dictState = dp.getDictionaryLoadState () ;
                
		if ( DictionaryLoadState.READY == dictState )
		{
			return true ;
		}
		else if ( DictionaryLoadState.ERROR == dictState )
		{
			if ( ask ( "Cannot load dictionary with the current settings\n" +
				"Choose Yes to disable the dictionary, or No to correct the settings." ) )
			{
				dp.setByUserDictionarySettings ( false, 
					valDictionaryLocal, 
					valDictionaryPath ) ;
				return true ;
			}
		}
		else
		{
			System.err.println ( "Dictionary load state incorrect." ) ;
		}             

		return false ;
	}


    /**
     * ask
	 * 
	 * shows a confirmation dialog
     */
    protected boolean ask ( String question )
    {
        return (JOptionPane.YES_OPTION == 
                JOptionPane.showConfirmDialog ( pane, question, "Question", JOptionPane.YES_NO_OPTION ) ) ;
    }
}
