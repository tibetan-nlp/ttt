/**
 *
 *
 */

package org.thdl.tib.input ;

public class DictionarySettings
{
	private boolean dictionaryValid = false ;
	private boolean dictionaryEnabled = false ;
	private boolean dictionaryLocal = false ;
	private String dictionaryPath = "" ;

	public DictionarySettings ()
	{
		dictionaryValid = false ;
		dictionaryEnabled = false ;
		dictionaryLocal = false ;
		dictionaryPath = "" ;
	}

	public DictionarySettings ( boolean valid, boolean enabled, boolean local, String pathOrUrl )
	{
		dictionaryValid = valid ;
		dictionaryEnabled = enabled ;
		dictionaryLocal = local ;
		dictionaryPath = pathOrUrl ;
	}

	public DictionarySettings ( boolean enabled, boolean local, String pathOrUrl )
	{
		dictionaryValid = true ;
		dictionaryEnabled = enabled ;
		dictionaryLocal = local ;
		dictionaryPath = pathOrUrl ;
	}

    public static DictionarySettings cloneDs  ( DictionarySettings ds )
    {
        return new DictionarySettings ( ds.dictionaryValid, 
                                        ds.dictionaryEnabled,
                                        ds.dictionaryLocal,
                                        ds.dictionaryPath ) ;
    }

	public boolean equal ( DictionarySettings ds )
	{
		return ( ds.isEnabled () == this.isEnabled () &&
				 ds.isLocal () != this.isLocal () &&
				 ds.getPathOrUrl ().equals ( this.getPathOrUrl () ) ) ;
	}

	public boolean isValid ()
	{
		return dictionaryValid ;
	}

	public boolean isEnabled ()
	{
		return dictionaryEnabled ;
	}

	public boolean isLocal ()
	{
		return dictionaryLocal ;
	}

	public String getPathOrUrl ()
	{
		return dictionaryPath ;
	}

	public void set ( DictionarySettings ds )
	{
		dictionaryValid = ds.dictionaryValid ;
		dictionaryEnabled = ds.dictionaryEnabled ;
		dictionaryLocal = ds.dictionaryLocal ;
		dictionaryPath = ds.dictionaryPath ;
	}

	public void setValid ( boolean valid )
	{
		dictionaryValid = valid ;
	}

	public void setEnabled ( boolean enabled )
	{
		dictionaryEnabled = enabled ;
	}

	public void setLocal ( boolean local )
	{
		dictionaryLocal = local ;
	}

	public void setPathOrUrl ( String pathOrUrl )
	{
		dictionaryPath = pathOrUrl ;
	}
};
