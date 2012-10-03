package org.thdl.tib.dictionary ;

import org.thdl.tib.dictionary.StandardPronounciationEngine ;

public class Phonetics
{
  public static final String THDL_ENGLISH = "THDL_ENGLISH" ;
  public static boolean valid = false ;

  static StandardPronounciationEngine pronounciationEngine = null ;

  static
  {
    try
    {
      pronounciationEngine = new StandardPronounciationEngine () ;
      valid = true ;
    }
    catch ( Exception e )
    {
      valid = false ;
    }
  }

  public static boolean isValid ()
  {
    return valid ;
  }

  public static String standardToLocalized ( String locale, String in )
  {
    if ( locale.equals ( THDL_ENGLISH ) )
    {
      //
      // put back the roman digraphs
      //
      in = in.replaceAll ( "B", "bh" ) ;
      in = in.replaceAll ( "D", "dz" ) ;
      in = in.replaceAll ( "K", "kh" ) ;
      in = in.replaceAll ( "N", "ng" ) ;
      in = in.replaceAll ( "P", "p" ) ;
      in = in.replaceAll ( "S", "sh" ) ;
      in = in.replaceAll ( "T", "t" ) ;
      in = in.replaceAll ( "X", "ts" ) ;
      in = in.replaceAll ( "Q", "ts" ) ;
      in = in.replaceAll ( "Z", "sh" ) ;
      in = in.replaceAll ( "c", "ch" ) ;
      in = in.replaceAll ( "C", "ch" ) ;
    }
    else if ( locale.equals ( "POLISH" ) )
    {
      //
      // put back the roman digraphs
      //
      in = in.replaceAll ( "ny", "ni" ) ;
      in = in.replaceAll ( "w", "\u0142" ) ;
      in = in.replaceAll ( "B", "bh" ) ;
      in = in.replaceAll ( "C", "cz'" ) ;
      in = in.replaceAll ( "D", "dz" ) ;
      in = in.replaceAll ( "j", "dzi" ) ;
      in = in.replaceAll ( "K", "k'" ) ;
      in = in.replaceAll ( "N", "ng" ) ;
      in = in.replaceAll ( "P", "p'" ) ;
      in = in.replaceAll ( "S", "sz" ) ;
      in = in.replaceAll ( "T", "t'" ) ;
      in = in.replaceAll ( "X", "c" ) ;
      in = in.replaceAll ( "Q", "ts'" ) ;
      in = in.replaceAll ( "y", "j" ) ;
      in = in.replaceAll ( "Z", "sz" ) ;
      in = in.replaceAll ( "c", "cz" ) ;
    }
    else if ( locale.equals ( "CZECH" ) || locale.equals ( "SLOVAK" ) )
    {
      //
      // put back the roman digraphs
      //
      in = in.replaceAll ( "ny", "\u0148" ) ;
      in = in.replaceAll ( "w", "v" ) ;
      in = in.replaceAll ( "B", "bh" ) ;
      in = in.replaceAll ( "C", "\u010d'" ) ;
      in = in.replaceAll ( "D", "dz" ) ;
      in = in.replaceAll ( "j", "d\u017e" ) ;
      in = in.replaceAll ( "K", "k'" ) ;
      in = in.replaceAll ( "N", "ng" ) ;
      in = in.replaceAll ( "P", "p'" ) ;
      in = in.replaceAll ( "S", "\u0161" ) ;
      in = in.replaceAll ( "T", "t'" ) ;
      in = in.replaceAll ( "X", "c" ) ;
      in = in.replaceAll ( "Q", "ts'" ) ;
      in = in.replaceAll ( "y", "j" ) ;
      in = in.replaceAll ( "Z", "\u0161" ) ;
      in = in.replaceAll ( "c", "\u010d'" ) ;
    }

    return in ;
  }

  public static String wylieToStandardPhonetic ( String wylie )
  {
    try
    {
      return pronounciationEngine.processWylie ( wylie ) ;
    }
    catch ( Exception e )
    {
      return "<INVALID>" ;
    }
  }
}
