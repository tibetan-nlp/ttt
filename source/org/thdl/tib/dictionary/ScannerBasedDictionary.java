package org.thdl.tib.dictionary ;

import org.thdl.tib.scanner.TibetanScanner ;
import org.thdl.tib.scanner.LocalTibetanScanner ;
import org.thdl.tib.scanner.RemoteTibetanScanner ;
import org.thdl.tib.scanner.Word ;
import org.thdl.tib.dictionary.DictionaryEntries ;
import org.thdl.tib.dictionary.TextBody ;
import org.thdl.tib.dictionary.SimpleDictionaryEntry ;
import org.thdl.tib.dictionary.SimpleDictionaryEntries ;

public class ScannerBasedDictionary implements DictionaryInterface
{
  TibetanScanner scanner ;

  public ScannerBasedDictionary ( TibetanScanner ts )
  {
    scanner = ts ;
  }

  public DictionaryEntries lookup ( TextBody tb )
  {
    DictionaryEntries entries = new SimpleDictionaryEntries () ;
    //
    // TibetanScanner expects romanized wylie for lookup
    //
    String input = tb.getRomanizedWylie () ;

    scanner.scanBody ( input ) ;
    scanner.finishUp () ;
    Word [] words = scanner.getWordArray () ;
    for ( int i = 0; i < words.length; i++ )
    {
      SimpleDictionaryEntry entry = SimpleDictionaryEntry.fromWord ( words [i] ) ;
      entries.add ( entry ) ;
    }

    scanner.clearTokens () ;

    return entries ;
  }
}
