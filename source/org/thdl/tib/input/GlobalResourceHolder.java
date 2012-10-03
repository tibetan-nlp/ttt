/**
 *
 *
 */

package org.thdl.tib.input ;

import org.thdl.tib.scanner.TibetanScanner ;
import org.thdl.tib.scanner.LocalTibetanScanner ;
import org.thdl.tib.scanner.RemoteTibetanScanner ;
import org.thdl.tib.scanner.Word ;

import org.thdl.tib.input.SettingsServiceProvider ;
import org.thdl.tib.input.DictionaryLoadState ;
import org.thdl.tib.input.DictionarySettings ;
import org.thdl.tib.dictionary.DictionaryInterface ;
import org.thdl.tib.dictionary.ScannerBasedDictionary ;

import java.awt.GraphicsEnvironment ;
import java.awt.Font ;
import java.net.URL ;
import java.net.URLConnection ;
import java.io.InputStream ;
import java.util.HashMap ;
import java.util.Observer ;
import java.util.Observable ;

public class GlobalResourceHolder
{
    protected static final int NUM_FONTS = 10 ;
    protected static final String FONT_NAME_PREFIX = "TibetanMachineWeb" ;
    
	private static boolean flagIsUsingLocalFonts = false ; 
	private static Font baseTMWFont [] = null ;
	private static Font derivedTMWFont [] = null ;
	private static HashMap nameToNumber = null ;
	private static SettingsServiceProvider settingsServiceProvider ;

	private static DictionarySettings dictionarySettings = null ;
    
    private static int dictionaryLoadState = DictionaryLoadState.UNDEFINED ;
    private static Object dictionaryLoadLock = null ;

    private static DictionaryInterface dictionary = null ;

	static class Listener implements Observer
	{
		public static final int DICTIONARY_SETTINGS = 1 ;

		/**
         * update
         * 
         */
        public void update ( Observable o, Object arg )
        {
            onSettingsChange ( arg ) ;
        }
        
		/**
		 * onSettingsChange
		 * 
		 */
		public void onSettingsChange ( Object arg )
		{
			if ( null == GlobalResourceHolder.settingsServiceProvider )
				return ;
			if ( arg instanceof DictionarySettings )
			{			
				//
				// compare with the current settings
				//
				DictionarySettings newSettings = 
                    DictionarySettings.cloneDs ( GlobalResourceHolder.
                    settingsServiceProvider.getDictionarySettings () ) ;

				if ( ! newSettings.equal ( GlobalResourceHolder.dictionarySettings ) )
				{
					GlobalResourceHolder.dictionarySettings.set ( newSettings ) ;
                    GlobalResourceHolder.dictionary = GlobalResourceHolder.loadDictionary () ;
				}
			}
		}
	} 

	static Listener listenerObj = null ;
    
	static
    {
		listenerObj = new Listener () ;		

		//
		// we have no service provider until the client class is born
		//
		settingsServiceProvider = null ;

        //
        // init the font array
        //
        //
		baseTMWFont = new Font [ NUM_FONTS ] ;
		derivedTMWFont = new Font [ NUM_FONTS ] ;
        
		//
        // if TibetanMachineWeb fonts are not installed
        // load the fonts from the .jar file and set
        // the flag
        //
		if ( ! isTMWInstalled () )
            setLocalFonts () ;

        //
        // initialize the dictionary stuff
        //
		dictionarySettings = new DictionarySettings () ;
        dictionaryLoadLock = new Object () ;
    }

	/**
	 */
	public static Listener getListener ()
	{
		return listenerObj ;
	}

    /**
     *
     */
    public static int getDictionaryLoadState ()
    {
        int state ;
        synchronized ( dictionaryLoadLock )
        {
            state = dictionaryLoadState ;
        }

        return state ;
    }

    public static void waitForDictionary ()
    {
        synchronized ( dictionaryLoadLock )
        {
            
        }
    }

    /**
     *
     *
     */
    public static DictionaryInterface getDictionary ()
    {
        return dictionary ;
    }

    /**
     * loadDictionary
     *
     */
    protected static DictionaryInterface loadDictionary ()
    {
        DictionaryInterface d = null ;

		if ( ! dictionarySettings.isEnabled () )
			return null ;

		dictionarySettings.setValid ( false ) ;

        synchronized ( dictionaryLoadLock )
        {
            dictionaryLoadState = DictionaryLoadState.UNDEFINED ;

		    try
		    {
			    if ( dictionarySettings.isLocal () )
			    {
				    d = new ScannerBasedDictionary ( new LocalTibetanScanner ( dictionarySettings.getPathOrUrl () ) ) ;			
				    dictionarySettings.setValid ( true ) ;
			    }
			    else
			    {
				    d = new ScannerBasedDictionary ( new RemoteTibetanScanner ( dictionarySettings.getPathOrUrl () ) ) ;
				    dictionarySettings.setValid ( true ) ;
			    }

                dictionaryLoadState = DictionaryLoadState.READY ;
		    }
		    catch ( Exception e )
		    {
			    System.err.println ( "TibetanScanner.loadDictionaryScanner () --> " + e.toString () ) ;
                dictionaryLoadState = DictionaryLoadState.ERROR ;
		    }
        }		

		return d ;
    }

    /*
     * isTMWInstalled ()
     * 
     * returns true if TibetanMachineWeb fonts are 
     * installed on the system.
     */
    protected static boolean isTMWInstalled ()
    {
        String [] familyNames = 
            GraphicsEnvironment.getLocalGraphicsEnvironment ().getAvailableFontFamilyNames () ;
 
        int mask = 0 ;
        
        for ( int i = 0; i < familyNames.length; i++ )
        {
            String family = familyNames [i] ;
            
            if ( family.startsWith ( FONT_NAME_PREFIX ) )
            {
                int fontNum = 0 ;

                if ( family == FONT_NAME_PREFIX )
                {
                    fontNum = 1 ;
                }
                else
                {
                    fontNum = Character.digit ( family.charAt ( family.length () - 1 ), 10 ) ;

                    if ( fontNum < 0 || fontNum > NUM_FONTS )
                    {
                        fontNum = 0 ;
                    }                    
                }

                if ( fontNum > 0 )
                {
                    mask |= ( 1 << (fontNum-1) ) ;
                    if ( ( ( 1 << NUM_FONTS ) - 1 ) == mask )
                        return true ;
                }
            }
        }
 
        return false ;
    }

    /*
     * setLocalFonts ()
     * 
     * loads the fonts from Fonts directory in
     * the .jar file, if the dir exists
     * 
     */
    protected static void setLocalFonts ()
    {
		nameToNumber = new HashMap () ;

        for ( int i = 0; i < NUM_FONTS; i++ )
        {
            String suffix = ((i>0)?String.valueOf (i):"") ;
            URL url = 
                GlobalResourceHolder.class.getResource ( "/Fonts/TibetanMachineWeb/timwn" + suffix + ".ttf" ) ;

            if ( null != url )
            {
                try
                {
                    URLConnection conn = url.openConnection () ;
                    InputStream is = conn.getInputStream () ;
                    baseTMWFont [i] = Font.createFont ( Font.TRUETYPE_FONT, is ) ;
                    is.close () ;
                }
                catch ( Exception e )
                {
                    System.err.println ( "Failed to load fonts from JAR !" ) ;
                    flagIsUsingLocalFonts = false ;
                    return ;
                }
            }    

			//
			// we use a hash map to translate font names into their numbers (indices)
			//
			nameToNumber.put ( FONT_NAME_PREFIX + ( ( 0 == i ) ? "" : String.valueOf (i) ), new Integer ( i ) ) ;
        }   

        flagIsUsingLocalFonts = true ;		
    }

	/*
	 * setFontSize
	 * 
	 * notifies the GlobalResource about the most probably used font size
	 * 
	 */
	protected static void setFontSize ( int fontSize )
	{
		if ( !flagIsUsingLocalFonts )
			return ;

		if ( null == derivedTMWFont [0] || fontSize != derivedTMWFont [0].getSize () )
		{
			for ( int i = 0; i < NUM_FONTS; i++ )
			{
				derivedTMWFont [i] = baseTMWFont [i].deriveFont ( Font.PLAIN, fontSize ) ;
			}
		}
	}

	/*
	 * getFont ( fontName, size )
	 * 
	 */
	public static Font getFont ( String fontName, int fontSize )
	{
		Integer intObj = (Integer)nameToNumber.get ( fontName ) ;

		if ( intObj != null )
		{
			int fontNum = intObj.intValue () ;
			return getFont ( fontNum, fontSize ) ;
		}

		return null ;
	}

	/*
	 * getFont ( fontNum, size )
	 * 
	 */
	public static Font getFont ( int fontNum, int fontSize )
	{		
		Font font = null ;

		if ( flagIsUsingLocalFonts )
		{
			if ( null == derivedTMWFont [fontNum] || fontSize != derivedTMWFont [fontNum].getSize () )
			{
				font = derivedTMWFont [fontNum] = baseTMWFont [fontNum].deriveFont ( Font.PLAIN, fontSize ) ;
			}
		}

		return font ;
	}

    /*
     * isUsingLocalFonts ()
     *
     * returns true if the app should use the fonts from the package .jar file
     * (==> fonts are not installed on the system)
     * 
     */
    public static boolean isUsingLocalFonts ()
    {
        return flagIsUsingLocalFonts ;
    }

	/**
	 * 
	 */
	public static void setSettingsServiceProvider ( SettingsServiceProvider theSettingsServiceProvider )
	{
		settingsServiceProvider = theSettingsServiceProvider ;
        theSettingsServiceProvider.getObservable ().addObserver ( listenerObj ) ;
	}
    
	/**
	 *
	 */
	public static void removeSettingsServiceProvider 
        ( SettingsServiceProvider theSettingsServiceProvider )
	{
		settingsServiceProvider = null ;
        theSettingsServiceProvider.getObservable ().deleteObserver ( listenerObj ) ;
	}

	/**
	 * 
	 */
	public static DictionarySettings getDictionarySettings ()
	{
		return dictionarySettings ;
	}    
};
