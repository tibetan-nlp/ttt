package org.thdl.tib.dictionary ;

import org.thdl.tib.dictionary.DictionaryEntryDefinition ;

class SimpleDictionaryEntryDefinition implements DictionaryEntryDefinition
{
  String body ;

  public SimpleDictionaryEntryDefinition ( String theBody )
  {
    body = theBody ;
  }

  public String toString ()
  {
    return body ;
  }
}
