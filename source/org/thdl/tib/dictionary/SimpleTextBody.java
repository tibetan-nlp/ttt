package org.thdl.tib.dictionary ;

import org.thdl.tib.dictionary.TextBody ;

public class SimpleTextBody implements TextBody
{
  static final int UNDEFINED_TYPE = 0 ;
  static final int WYLIE_TYPE = 1 ;
  static final int UNICODE_TYPE = 2 ;

  protected int basicType = UNDEFINED_TYPE  ;

  String unicode ;
  String wylie ;

  SimpleTextBody ()
  {
    unicode = "" ;
    wylie = "" ;
  }

  public static TextBody fromWylie ( String in )
  {
    SimpleTextBody stb = new SimpleTextBody () ;
    stb.setWylie ( in ) ;

    return stb ;
  }

  public static TextBody fromUnicode ( String in )
  {
    SimpleTextBody stb = new SimpleTextBody () ;
    stb.setUnicode ( in ) ;

    return stb ;
  }

  public void setWylie ( String in )
  {
    wylie = in ;
    basicType = WYLIE_TYPE ;
  }

  public void setUnicode ( String in )
  {
    unicode = in ;
    basicType = UNICODE_TYPE ;
  }

  public String getRomanizedWylie ()
  {
    String ret = getWylie () ;

    ret = ret.replaceAll ( "[\\/\\_\\*]", " " ) ;

    return ret ;
  }

  public String getUnicode ()
  {
    if ( UNICODE_TYPE == basicType )
    {
      return unicode ;
    }
    else if ( WYLIE_TYPE == basicType )
    {
      return wylieToUnicode ( unicode ) ;
    }
    else
    {
      return "" ;
    }
  }

  public String getWylie ()
  {
    if ( WYLIE_TYPE == basicType )
    {
      return wylie ;
    }
    else if ( UNICODE_TYPE == basicType )
    {
      return unicodeToWylie ( unicode ) ;
    }
    else
    {
      return "" ;
    }
  }

  protected static String unicodeToWylie ( String in )
  {
    return "<INVALID>" ;
  }

  protected static String wylieToUnicode ( String in )
  {
    return "<INVALID>" ;
  }

};

