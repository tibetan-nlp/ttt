package org.thdl.tib.dictionary ;

import java.util.Vector ;
import org.thdl.tib.scanner.Definitions ;
import org.thdl.tib.scanner.DictionarySource ;
import org.thdl.tib.scanner.ByteDictionarySource ;
import org.thdl.tib.scanner.FileSyllableListTree ;
import org.thdl.tib.dictionary.SimpleDictionaryEntryDefinition ;

class SimpleDictionaryEntryDefinitions extends Vector implements DictionaryEntryDefinitions
{
  public static SimpleDictionaryEntryDefinitions fromDefinitions ( Definitions defs )
  {
    SimpleDictionaryEntryDefinitions sded = new SimpleDictionaryEntryDefinitions () ;
    sded.populate ( defs ) ;

    return sded ;
  }

  protected void populate ( Definitions defs )
  {
    DictionarySource source = defs.getDictionarySource () ;
    String [] defArr = defs.def ;

    int i,j;

    if (FileSyllableListTree.versionNumber==2)
    {
      this.add ( new SimpleDictionaryEntryDefinition ( "(" + source.getTag(0) + ") " + defArr[0] ) ) ;
      for (i=1; i<defArr.length; i++)
        this.add ( new SimpleDictionaryEntryDefinition ( "(" + source.getTag(i) + ") " + defArr[i] ) ) ;
    }
    else
    {
      ByteDictionarySource sourceb = (ByteDictionarySource) source;
      j=0;
      while (sourceb.isEmpty(j)) j++;

      this.add ( new SimpleDictionaryEntryDefinition ( "(" + sourceb.getTag(j) + ") " + defArr[0] ) ) ;
      for (i=1; i<defArr.length; i++)
      {
        j++;
        while (sourceb.isEmpty(j)) j++;

        this.add ( new SimpleDictionaryEntryDefinition ( "(" + sourceb.getTag(j) + ") " + defArr[i] ) ) ;
      }
    }
  }
}
