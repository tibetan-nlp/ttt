package org.thdl.tib.dictionary ;

import org.thdl.tib.dictionary.TextBody ;
import org.thdl.tib.dictionary.DictionaryEntryDefinitions ;

public interface DictionaryEntry
{
  public TextBody getKeyword () ;
  public String getPhonetic () ;
  public DictionaryEntryDefinitions getDefinitions () ;
}

