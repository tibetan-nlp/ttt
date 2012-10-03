package org.thdl.tib.input ;

import javax.swing.* ;
import javax.swing.text.* ;
import javax.swing.text.StyleConstants.FontConstants ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Rectangle ;
import java.awt.Font ;
import java.awt.Color ;
import java.awt.Component ;
import java.awt.event.KeyEvent ;
import java.awt.event.KeyAdapter ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.event.ListSelectionListener ;
import javax.swing.event.ListSelectionEvent ;
import java.util.concurrent.locks.ReentrantLock ;
import java.text.StringCharacterIterator ;
import org.thdl.tib.dictionary.TextBody ;
import org.thdl.tib.dictionary.DictionaryEntry ;
import org.thdl.tib.dictionary.DictionaryEntries ;
import org.thdl.tib.dictionary.DictionaryEntryDefinition ;
import org.thdl.tib.dictionary.DictionaryEntryDefinitions ;
import org.thdl.tib.dictionary.Phonetics ;
import java.util.Iterator ;

class DictionaryFrame extends JFrame implements ListSelectionListener
{
	//
	// the fields to display ..
	//
	protected JTextPane original ;			// .. the original text - romanized and tibetan
	protected JTextArea pronounciation ;	// .. the pronounciation
	protected DefaultListModel listModel ;	//
	protected JList entryList ;				// .. list of dictionary keywords
	protected JTextPane description ;		// .. sequence of dictionary entries
    protected JScrollPane descriptionPane ;	//
	protected JButton closeButton ;

	protected Font fontSerif ;
	protected Font fontSansSerif ;

    protected ReentrantLock listLock ;

	//
	// entryList contains MyListElement-s instead of just String-s
	// (we want a number to be associated with each element to store 
    // the position within description)
	//
	class MyListElement
	{
		MyListElement ( String theStr, int theData )
		{
			str = theStr ;
			data = theData ;
		}

		public void setIntData ( int theData )
		{
			data = theData ;
		}

		public int getIntData ()
		{
			return data ;
		}

		public String toString ()
		{
			return str ;
		}

		protected String str ;
		protected int data ;
	};

	DictionaryFrame ()
	{
		super ( "Dictionary" ) ;
		init ( null ) ;
	}

	DictionaryFrame ( Component parent )
	{
		super ( "Dictionary" ) ;
		init ( parent ) ;
	}

	void init ( Component parent )
	{	
        listLock = new ReentrantLock () ;

		fontSerif = new Font ( "serif", Font.PLAIN, 12 ) ;
		fontSansSerif = new Font ( "sansserif", Font.PLAIN, 12 ) ;

		//
		// layout
		//
		GridBagLayout gridbag = new GridBagLayout () ;
		GridBagConstraints c = new GridBagConstraints () ;

		getContentPane ().setLayout ( gridbag ) ;

		//
		// children
		//
		original = new JTextPane () ;
		pronounciation = new JTextArea ( "" ) ;

		listModel = new DefaultListModel () ;
		
		entryList = new JList ( listModel ) ;
		entryList.addListSelectionListener ( this ) ;

		description = new JTextPane () ;
		descriptionPane = new JScrollPane ( description, 
											JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
											JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) ;

        closeButton = new JButton ( "Close" ) ;
        closeButton.setMnemonic ( 'c' ) ;
        closeButton.addActionListener ( new ActionListener () 
                { public void actionPerformed ( ActionEvent e ) { closeWindow () ;  } } ) ;

		original.setEditable ( false ) ;
		pronounciation.setEditable ( false ) ;
		description.setEditable ( false ) ;

		//
		// add them
		//
		c.insets = new Insets ( 1, 1, 1, 1 ) ;
		c.anchor = GridBagConstraints.NORTHWEST ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( original, c ) ;
		getContentPane ().add ( original ) ;

		c.anchor = GridBagConstraints.NORTHWEST ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( pronounciation, c ) ;
		getContentPane ().add ( pronounciation ) ;

		c.weighty = 1.0 ;			
		c.fill = GridBagConstraints.BOTH ;
		c.gridheight = 4 ;
		c.weightx = 0.0 ;
		c.gridwidth = 1 ;

		gridbag.setConstraints ( entryList, c ) ;
		getContentPane ().add ( entryList ) ;

		c.fill = GridBagConstraints.BOTH ;
		c.gridheight = 4 ;
		c.gridwidth = GridBagConstraints.REMAINDER ;
		c.weightx = 3.0 ;

		gridbag.setConstraints ( descriptionPane, c ) ;
		getContentPane ().add ( descriptionPane ) ;

		c.anchor = GridBagConstraints.CENTER ;
		c.weightx = 1.0 ;			
		c.weighty = 0.0 ;			
		c.gridx = GridBagConstraints.RELATIVE ;
		c.gridy = GridBagConstraints.RELATIVE ;
		c.gridheight = 1 ;
		c.gridwidth = GridBagConstraints.RELATIVE ;
		c.fill = GridBagConstraints.HORIZONTAL ;

		gridbag.setConstraints ( closeButton, c ) ;
		getContentPane ().add ( closeButton ) ;

		//
		// set fonts
		//
		original.setFont ( fontSansSerif ) ;
		pronounciation.setFont ( fontSansSerif ) ;
		entryList.setFont ( fontSansSerif ) ;
		description.setFont ( fontSansSerif ) ;

		//
		// we need F12 to toggle the visible state of the dictionary window
		//
		KeyAdapter keyAdapter = new KeyAdapter () 
								{
									public void keyPressed ( KeyEvent ev )
									{
										if ( KeyEvent.VK_F12 == ev.getKeyCode () ||
                                             KeyEvent.VK_ESCAPE == ev.getKeyCode () )
											toggleVisible () ;
									}
								};
		original.addKeyListener ( keyAdapter ) ;
		pronounciation.addKeyListener ( keyAdapter ) ;
		entryList.addKeyListener ( keyAdapter ) ;
		description.addKeyListener ( keyAdapter ) ;

		//
		// finish
		//		
		pack () ;

		//
		// place the window on top of the parent frame, moving it slightly
		//
		if ( parent != null )
			setLocation ( parent.getLocation ().x, parent.getLocation ().y ) ;

		setSize ( 500, 500 ) ;		
	}

	/**
	 * toggle the visibility of this window
	 */
	void toggleVisible ()
	{
		setVisible ( !isVisible () ) ;
	}

    /**
     * close the window
     */
    void closeWindow ()
    {
        setVisible ( false ) ;
    }

	/**
	 * clear all fields
	 */
	public void reset ()
	{
		//
		// clear all fields
		//
        listLock.lock () ;
        try
        {
		    original.setText ( "" ) ;
		    pronounciation.setText ( "" ) ;

		    description.setText ( "" ) ;
			listModel.clear () ;		
        }
        finally
        {
            listLock.unlock () ;
        }
	}

	/**
	 * isEmpty
     * 
     * @return true if the dict frame does not display any data
	 */
	public boolean isEmpty ()
	{
		return ( original.getText ().length () > 0 ? false : true ) ;
	}

	/**
	 * set the original
	 * 
	 * @param text - has the Wylie roman text
	 * @param realDoc - ref to the actual document being edited
	 * @param startPos 
     * @param endPos - location of the text in the actual document being edited
	 */
	private void setOriginal ( String text, DefaultStyledDocument realDoc, int startPos, int endPos )
	{
		AbstractDocument doc = (AbstractDocument)original.getDocument () ;

		SimpleAttributeSet as = new SimpleAttributeSet () ;
		as.addAttribute ( FontConstants.Family, "serif" ) ;
		try
		{
			doc.insertString ( doc.getLength (), text + "  ", as ) ;
		}
		catch ( BadLocationException e )
		{
			// this should never happen
		}

		SimpleAttributeSet as1 = new SimpleAttributeSet () ;
		
		for ( int pos = startPos; pos <= endPos; pos++ )
		{
			Element el = realDoc.getCharacterElement ( pos ) ;
			
			try
			{
				doc.insertString ( doc.getLength (), realDoc.getText ( pos, 1 ), el.getAttributes () ) ;
			}
			catch ( BadLocationException e )
			{
				// this should never happen
			}
		}
	}

	/**
	 * set the pronounciation field
	 */
	private void setPronounciation ( String text )
	{
		//
		// set the pronounciation field
		//
		pronounciation.setText ( text ) ;
	}
	
	/**
	 * add a dictionary entry
     *
     * @param text - description as retrieved form TibetanScanner derived objs
	 */
	private void addDescription ( String key, String desc )
	{
		//
		// add the keyword to entryList. we assume the keyword is anything in the
		// dictionary entry that precedes the first dash
		//

		desc = handleSanskrit ( desc ) ;

		listLock.lock () ;

		try
		{        
			Document doc = description.getDocument () ;

			try
			{
				int len = doc.getLength() ;
				if ( len > 0 )
				{
					doc.insertString ( len, "\n\n", null ) ;
					len = doc.getLength () ;
				}

				String keyword = key ;
				String entry = desc ;

				//
				// add keyword to the list box
				//
				listModel.addElement ( new MyListElement ( keyword,	len ) ) ;

				SimpleAttributeSet attrSet = new SimpleAttributeSet () ;
				StyleConstants.setBold ( attrSet, true ) ;
				StyleConstants.setUnderline ( attrSet, true ) ;				

				//
				// add entry to the list box
				//
				doc.insertString ( len, keyword + "\n" , attrSet ) ;
				len = doc.getLength () ;

				StyleConstants.setBold ( attrSet, true ) ;
				StyleConstants.setUnderline ( attrSet, false ) ;
				doc.insertString ( len, entry, null ) ;
			}
			catch ( Exception e )
			{
			}
		}
		finally
		{
			listLock.unlock () ;
		}
	}
	/**
	 * called when entryList selection changes
	 */
	public void valueChanged ( ListSelectionEvent e ) 
	{
		//
		// get the position of the dictionary entry identified
		// by the selected entryList item.
		//

        listLock.lock () ;

        try
        {
            MyListElement elem = ((MyListElement)entryList.getSelectedValue ()) ;
            if ( null != elem )
            { 
		        int pos = elem.getIntData () ;
        
		        try
		        {
			        //
			        // make sure the part of description associated with the item
			        // is visible;  TODO this doesn't work quite well
			        //
			        Rectangle rect = description.modelToView ( pos ) ;
			        rect.setSize ( (int)rect.getWidth (), descriptionPane.getHeight () - 10 ) ;
			        description.scrollRectToVisible ( rect ) ;
		        }
		        catch ( BadLocationException ble )
		        {
			        //
			        // this should never happen
			        //
			        System.err.println ( "DictionaryFrame.valueChanged .. Incorrect argument passed to modelToView." ) ;
                }
            }
		}	
        finally
        {
            listLock.unlock () ;
        }        
	}

    /**
     * setData
     *
     */
    public void setData ( DictionaryEntries de, DefaultStyledDocument doc, int startPos, int endPos )
    {
        // TODO

		String originalText = "" ;
		String pronounciationText = "" ;

		Iterator it = de.iterator () ;

		while ( it.hasNext () )
		{
			DictionaryEntry entry = (DictionaryEntry)it.next () ;

			if ( originalText.length () > 0 )
				originalText += " " ;
			if ( pronounciationText.length () > 0 )
				pronounciationText += " " ;

			originalText += entry.getKeyword ().getWylie () ;
			pronounciationText += Phonetics.standardToLocalized ( Phonetics.THDL_ENGLISH, entry.getPhonetic () ) ;

			String desc = entry.getKeyword () + "\n" ;

			String defString = "" ;
			DictionaryEntryDefinitions defs = entry.getDefinitions () ;
			Iterator defIt = defs.iterator () ;
			while ( defIt.hasNext () )
			{
				DictionaryEntryDefinition def = (DictionaryEntryDefinition) defIt.next () ;
				defString += def.toString () ;
				defString += "\n" ;
			}

			addDescription ( entry.getKeyword ().getWylie (), defString ) ;
		}

		setOriginal ( originalText, doc, startPos, endPos ) ;
		setPronounciation ( pronounciationText ) ;
	}

    /**
     * gotoList
     * force the focus to the entry list box
     *
     */
    public void gotoList ()
    {
        entryList.requestFocusInWindow () ;
        entryList.setSelectedIndex ( 0 ) ;
    }

    /**
     * handleSanskrit
     * converts the dictionary entry text so that romanized Sanskrit leters are
     * displayed correctly (long a vs A etc) 
     * 
     * @param inText - text as retrieved from TibetanScanner
     * @return converted text
     */
    protected String handleSanskrit ( String inText )
    {                 
        String outText = "" ;
        StringCharacterIterator it = new StringCharacterIterator ( inText ) ;

        //
        // we have three states
        //
        final int DEFAULT = 0 ;
        final int INBRACES = 1 ;
        final int INWORD = 2 ;

        //
        // state is the current state
        // word is the recently collected piece of text
        // wasBraces is true if we are just after a {XX} sequence was encountered
        //
        int state = DEFAULT ;
        String word = "" ;
        boolean wasBraces = false ;
            
        //
        // loop through the entire text, find whatever is likely to be a Sanskrit word
        //
        // the decision whether a word could be Sanskrit is based on two criteria:
        // 1. if it appears immediately after a {XX} sequence it's assumed to be Sanskrit
        // 2. otherwise if it contains a sequence of <lowercase><uppercase>
        //    in which case uppercase is likely to be a letter that needs conversion
        //    
        for ( char c = it.first (); c != StringCharacterIterator.DONE; c = it.next () )
        {
            switch ( state )
            {
                case DEFAULT:
                    if ( Character.isLetter (c) )
                    {
                        word = String.valueOf (c) ;
                        state = INWORD ;
                    }
                    else if ( '{' == c )
                    {
                        wasBraces = false ;
                        state = INBRACES ;
                    }
                    else
                    {
                        wasBraces = false ;
                        outText += c ;
                    }
                    break ;
                    
                case INBRACES:
                    if ( '}' != c )
                    {
                        word += c ;
                    }
                    else
                    {
                        outText += "{" + word + "}" ;
                        word = "" ;
                        wasBraces = true ;
                        state = DEFAULT ;
                    }
                    break ;
                    
                case INWORD:
                    if ( Character.isLetter (c) )
                    {
                        word += c ;
                    }
                    else
                    {
                        //
                        // looks like a Sanskrit word, let's convert it
                        //                        
                        outText += processSanskrit ( word, wasBraces ) + c ;
                        word = "" ;                        
                        state = DEFAULT ;
                    }
                    break ;
            }
        }
        
        
        return outText ;
    }

    /**
     * isLikelyToBeSanskrit
     *
     * the criterion is :
     * the word contains a sequence of <lowercase><uppercase>
     *  
     * @param word
     * @return true is word is likely to be Sanskrit
     * 
     */
    protected boolean isLikelyToBeSanskrit ( String word )
    {
        if ( word.matches ( ".*[a-z][A-Z].*" ) )
            return true ;

        return false ;
    }

    /**
     * processSanskrit
     *
     * @param word
     * @param forceSanskrit
     * @return converted word
     */
    protected String processSanskrit ( String word, boolean forceSanskrit  )
    {
        //
        //
        //
        if ( forceSanskrit || isLikelyToBeSanskrit ( word ) )
        {
            word = word.replaceAll ( "A", "a\u0305" ) ;
            word = word.replaceAll ( "I", "i\u0305" ) ;
            word = word.replaceAll ( "U", "u\u0305" ) ;
            word = word.replaceAll ( "S", "s\u0323" ) ;
            word = word.replaceAll ( "D", "d\u0323" ) ;
            word = word.replaceAll ( "T", "t\u0323" ) ;
            word = word.replaceAll ( "M", "m\u0307" ) ;
            word = word.replaceAll ( "N", "n\u0307" ) ;
            word = word.replaceAll ( "H", "h\u0323" ) ;
            word = word.replaceAll ( "R", "r\u0323" ) ;
            word = word.replaceAll ( "L", "l\u0323" ) ;
            word = word.replaceAll ( "z", "s\u0301" ) ;
            word = word.replaceAll ( "G", "s\u0303" ) ;
        }

        return word ;
    }
    
	/**
	 * unit test
	 */
	public static void main ( String [] args )
	{
		new DictionaryFrame () ;
	}
}
