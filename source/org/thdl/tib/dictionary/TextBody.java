package org.thdl.tib.dictionary ;

import java.lang.String ;

public interface TextBody
{
  /**
   * setWylie
   *
   * populate TextBody based on romanized Wylie input string
   */
  void setWylie ( String in ) ;

  /**
   * setUnicode
   *
   * populate TextBody based on Unicode input string
   */
  void setUnicode ( String in ) ;

  /**
   * getRomanizedWylie
   *
   * populate TextBody based on romanized Wylie input string
   */
  public String getRomanizedWylie () ;

  /**
   * getWylie
   *
   * populate TextBody based on Wylie input string
   */
  public String getWylie () ;

  /**
   * getUnicode
   *
   * populate TextBody based on Unicode input string
   */
  public String getUnicode () ;
}

