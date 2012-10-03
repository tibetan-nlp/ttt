package org.thdl.tib.dictionary ;

import org.thdl.tib.dictionary.DictionaryEntry ;
import org.thdl.tib.dictionary.SimpleDictionaryEntryDefinitions ;
import org.thdl.tib.dictionary.Phonetics ;
import org.thdl.tib.scanner.Word ;

public class SimpleDictionaryEntry implements DictionaryEntry
{
  TextBody keyWord ;
  DictionaryEntryDefinitions definitions ;

  static boolean useDashes = true ;

  public static SimpleDictionaryEntry fromWord ( Word word )
  {
    SimpleDictionaryEntry sde = new SimpleDictionaryEntry () ;

    sde.definitions = SimpleDictionaryEntryDefinitions.fromDefinitions ( word.getDefs () ) ;
    sde.keyWord = SimpleTextBody.fromWylie ( word.getWylie () ) ;
    //sde.spaceInfo = word.getSpaceInfo or something like that

    return sde ;
  }

  boolean hasSpaceBeforeSyllable ( int syllableIndex )
  {
    //
    // TODO
    //
    return false ;
  }

  String joinSyllables ( String text )
  {
    String [] syllables = text.split ( " " ) ;

    String out = "" ;
    for ( int i = 0; i < syllables.length; i++ )
    {
      if ( i > 0 )
      {
        if ( hasSpaceBeforeSyllable ( i ) )
          out += " " ;
        else if ( useDashes )
          out += "-" ;
      }



      out += syllables [i] ;
    }

    return out ;
  }

  public TextBody getKeyword ()
  {
    return SimpleTextBody.fromWylie ( joinSyllables ( keyWord.getWylie () ) ) ;
  }

  public String getPhonetic ()
  {
    //
    // if phonetics specified in the dictionary - use it
    //
    // otherwise, generate one (currently the only option)
    return joinSyllables ( Phonetics.wylieToStandardPhonetic ( keyWord.getWylie () ) ) ;
  }

  public DictionaryEntryDefinitions getDefinitions ()
  {
    return definitions ;
  }
}

