package org.thdl.tib.dictionary ;

import java.lang.* ;
import java.util.regex.Pattern ;
import java.util.regex.Matcher ;
import java.util.Vector ;
import java.util.Enumeration ;

public class StandardPronounciationEngine
{
  public StandardPronounciationEngine () throws Exception
  {
    setRules () ;
    lastWordOriginal = "" ;
  }

  Rule [] rules ;

  protected int stateFlag ;
  protected int returnFlag ;
  protected String lastWordOriginal ;

  class Rule
  {
    /**
     * conditions
     */
    public static final int STARTS_WITH = 0 ;
    public static final int ENDS_WITH = 1 ;
    public static final int ENDS_WITH_FOLLOWS_VOWEL = 2 ;
    public static final int ENDS_WITH_FOLLOWS_CONSONANT = 3 ;
    public static final int STARTS_WITH_BORDERS_VOWEL = 4 ;
    public static final int STARTS_WITH_BORDERS_CONSONANT = 5 ;
    public static final int EQUALS = 6 ;
    public static final int CONTAINS = 7 ;
    public static final int STARTS_WITH_BORDERS_CONSONANTS = 8 ;

    /**
     * actions
     */
    public static final int REPLACE_MATCH = 0 ;
    public static final int SET_FLAG = 1 ;
    public static final int REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL = 2 ;
    public static final int REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT = 3 ;
    public static final int REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT = 4 ;
    public static final int SET_RETURN_FLAG = 5 ;

    /**
     * flags
     */
    public static final int FLAG_ENDS_WITH_VOWEL = 1 ;
    public static final int FLAG_ENDS_WITH_CONSONANT_VOICED = 2 ;
    public static final int FLAG_ENDS_WITH_CONSONANT_VOICELESS = 4 ;

    /**
     * return flags
     */
    public static final int DO_NOTHING = 0 ;
    public static final int DROP_SUFFIX_AND_NASALIZE = 1 ;

    private int condition ;
    private String condArg ;
    private int action ;
    private Object actionArg ;

    private Pattern pattern ;

    private static final String vowelSet = "aeiou" ;
    private static final String consonantSet = "bBcCdDfgGhjkKlmnNpPrsStTvwXzZ" ;

    /**
     * constructior
     */
    Rule ( int condition, String condArg, int action, Object actionArg ) throws Exception
    {
      this.condition = condition ;
      this.action = action ;
      this.condArg = condArg ;
      this.actionArg = actionArg ;

      String patStr = "" ;

      switch ( condition )
      {
      case CONTAINS :
        patStr = condArg ;
        break ;
      case EQUALS :
        patStr = "^" + condArg + "$" ;
        break ;
      case STARTS_WITH :
        patStr = "^" + condArg ;
        break ;
      case ENDS_WITH :
        patStr = condArg + "$" ;
        break ;
      case ENDS_WITH_FOLLOWS_VOWEL :
        patStr = "([" + vowelSet + "]{1,2})" + condArg + "$" ;
        if ( REPLACE_MATCH == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT == action )
          this.actionArg = "$1" + (String)actionArg ;
        break ;
      case ENDS_WITH_FOLLOWS_CONSONANT :
        patStr = "([" + consonantSet + "])" + condArg + "$" ;
        if ( REPLACE_MATCH == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT == action )
          this.actionArg = "$1" + (String)actionArg ;
        break ;
      case STARTS_WITH_BORDERS_VOWEL :
        patStr = "^" + condArg + "([" + vowelSet + "]{1,2})" ;
        if ( REPLACE_MATCH == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT == action )
          this.actionArg = (String)actionArg + "$1" ;
        break ;
      case STARTS_WITH_BORDERS_CONSONANT:
        patStr = "^" + condArg + "(([" + consonantSet + "]))" ;
        if ( REPLACE_MATCH == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT == action )
          this.actionArg = (String)actionArg + "$1" ;
        break ;
      case STARTS_WITH_BORDERS_CONSONANTS:
        patStr = "^" + condArg + "([" + consonantSet + "]{2})" ;
        if ( REPLACE_MATCH == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL == action ||
             REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT == action )
          this.actionArg = (String)actionArg + "$1" ;
        break ;
      default:
        throw new Exception ( "Invalid condition for a rule." ) ;
      }

      pattern = Pattern.compile ( patStr ) ;
    }

    /**
     * property access
     */
    public Object getActionArg ()
    {
      return actionArg ;
    }

    public String getConditionArg ()
    {
      return condArg ;
    }

    public int getCondition ()
    {
      return condition ;
    }

    public int getAction ()
    {
      return action ;
    }

    public Pattern getPattern ()
    {
      return pattern ;
    }
  }

  /**
   * setRules
   */
  protected void setRules () throws Exception
  {
    //
    // based on http://www.thdl.org/xml/showEssay.php?xml=/collections/langling/THDL_phonetics.xml&l=d1e671
    //
    Rule [] thdlRules =
    {
      //
      // 6. When ba and bo appear as the final syllable of a word, they are transcribed as "wa" and "wo," respectively.
      // This also includes ba'i ( > wé, about which see rule 16 below) and bar ( > war) as final syllables, although the latter is
      // more evident in literary forms
      //
      new Rule ( Rule.EQUALS, "ba", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL, "wa" ),
      new Rule ( Rule.EQUALS, "ba", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT, "wa" ),
      new Rule ( Rule.EQUALS, "ba", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT, "pa" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL, "wo" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT, "wo" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT, "po" ),
      new Rule ( Rule.EQUALS, "bar", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL, "war" ),
      new Rule ( Rule.EQUALS, "bar", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT, "war" ),
      new Rule ( Rule.EQUALS, "bar", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT, "par" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL, "wo" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT, "wo" ),
      new Rule ( Rule.EQUALS, "bo", Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT, "po" ),

      //
      // 7. The consonant clusters py, phy and by are transcribed respectively as "ch," "ch," and "j."
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "py", Rule.REPLACE_MATCH, "c" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "Py", Rule.REPLACE_MATCH, "c" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "by", Rule.REPLACE_MATCH, "j" ) ,

      //
      // 8. The consonant cluster my is transcribed as "ny."
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "my", Rule.REPLACE_MATCH, "ny" ) ,

      //
      // 13. When the second syllable of a word begins with the prefix achung ('), nasalization occurs
      // A. An 'n' is inserted after the first syllable, and the suffix of the first syllable (if there is one) is elided
      // B. If the root letter of the second syllable is pha or ba, an 'm' is inserted after the first syllable,
      // and the suffix of the first syllable (if there is one) is elided
      new Rule ( Rule.STARTS_WITH, "'", Rule.SET_RETURN_FLAG, new Integer ( Rule.DROP_SUFFIX_AND_NASALIZE ) ),

      //
      // dirty workaround
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANTS, "g", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANTS, "d", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANTS, "b", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANTS, "m", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANTS, "'", Rule.REPLACE_MATCH, "" ),

      //
      // 9. Consonant clusters with r subscripts (which are pronounced as retroflexes) are transcribed with an "r."
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "kr", Rule.REPLACE_MATCH, "tr" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "Kr", Rule.REPLACE_MATCH, "tr" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "gr", Rule.REPLACE_MATCH, "dr" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "nr", Rule.REPLACE_MATCH, "n" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "pr", Rule.REPLACE_MATCH, "tr" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "Pr", Rule.REPLACE_MATCH, "tr" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "br", Rule.REPLACE_MATCH, "dr" ) ,

      //
      // For all other consonant clusters in which the r subscript is not pronounced,
      // such as mr, sr, and so forth, THDL Simplified Phonetics simply drops the "r"
      // in accordance with the general principle
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "mr", Rule.REPLACE_MATCH, "m" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "Sr", Rule.REPLACE_MATCH, "S" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "sr", Rule.REPLACE_MATCH, "s" ) ,

      //
      // 10. Consonant clusters containing a subscript la are transcribed as "l"
      // with the exception of zl, which is transcribed as "d."
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "kl", Rule.REPLACE_MATCH, "l" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "gl", Rule.REPLACE_MATCH, "l" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "bl", Rule.REPLACE_MATCH, "l" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "rl", Rule.REPLACE_MATCH, "l" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "zl", Rule.REPLACE_MATCH, "d" ) ,
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "sl", Rule.REPLACE_MATCH, "l" ) ,

      //
      // 11. Consonant clusters with an l superscript and h root letter are rendered "lh."
      //
      new Rule ( Rule.STARTS_WITH, "lh", Rule.REPLACE_MATCH, "hl" ),

      //
      // 12. Consonant clusters with a d prefix and b root letter undergo transformations in the following way,
      // depending on whether the consonant cluster includes the subscripts y or r:
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "db", Rule.REPLACE_MATCH, "w" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "dby", Rule.REPLACE_MATCH, "y" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_VOWEL, "dbr", Rule.REPLACE_MATCH, "r" ),

      // !!!!! TODO !!!!!
      // Note: there are some exceptions to the form the nasalization takes:
      // skyabs 'gro > kyamdro
      // rten 'brel > temdrel
      // lam 'bras > lamdré

      //
      // http://www.thdl.org/xml/showEssay.php?xml=/collections/langling/THDL_phonetics.xml&l=d1e294
      // The THDL Simplified Phonetic system, in contrast to Wylie, drops all Tibetan letters not
      // pronounced in a given syllable. This includes the superscribed consonants r, l, and s ;
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "r", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "l", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "s", Rule.REPLACE_MATCH, "" ),

      //
      // .... the prefixes g, d, b, m, and ' ;
      //
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "g", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "d", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "b", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "m", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.STARTS_WITH_BORDERS_CONSONANT, "'", Rule.REPLACE_MATCH, "" ),

      // the suffixes d , ' , and s ; ( THEY ARE HANDLED ELSEWHERE )

      //
      // .... and the post-suffixes s and d.
      //
      new Rule ( Rule.ENDS_WITH_FOLLOWS_CONSONANT, "s", Rule.REPLACE_MATCH, "" ),
      new Rule ( Rule.ENDS_WITH_FOLLOWS_CONSONANT, "d", Rule.REPLACE_MATCH, "" ),

      //
      // 15. When two of the same vowels are connected by an achung, they are transcribed by dropping the achung and
      // combining the two vowels into one
      //
      new Rule ( Rule.CONTAINS, "a'a", Rule.REPLACE_MATCH, "a" ),
      new Rule ( Rule.CONTAINS, "e'e", Rule.REPLACE_MATCH, "e" ),
      new Rule ( Rule.CONTAINS, "i'i", Rule.REPLACE_MATCH, "i" ),
      new Rule ( Rule.CONTAINS, "o'o", Rule.REPLACE_MATCH, "o" ),
      new Rule ( Rule.CONTAINS, "u'u", Rule.REPLACE_MATCH, "u" ),

      new Rule ( Rule.ENDS_WITH, "as", Rule.REPLACE_MATCH, "é" ) ,
      new Rule ( Rule.ENDS_WITH, "ad", Rule.REPLACE_MATCH, "é" ) ,
      new Rule ( Rule.ENDS_WITH, "an", Rule.REPLACE_MATCH, "én" ) ,
      new Rule ( Rule.ENDS_WITH, "al", Rule.REPLACE_MATCH, "él" ) ,
      new Rule ( Rule.ENDS_WITH, "os", Rule.REPLACE_MATCH, "ö" ) ,
      new Rule ( Rule.ENDS_WITH, "od", Rule.REPLACE_MATCH, "ö" ) ,
      new Rule ( Rule.ENDS_WITH, "on", Rule.REPLACE_MATCH, "ön" ) ,
      new Rule ( Rule.ENDS_WITH, "ol", Rule.REPLACE_MATCH, "öl" ) ,
      new Rule ( Rule.ENDS_WITH, "u'", Rule.REPLACE_MATCH, "u" ) ,
      new Rule ( Rule.ENDS_WITH, "us", Rule.REPLACE_MATCH, "ü" ) ,
      new Rule ( Rule.ENDS_WITH, "ud", Rule.REPLACE_MATCH, "ü" ) ,
      new Rule ( Rule.ENDS_WITH, "un", Rule.REPLACE_MATCH, "ün" ) ,
      new Rule ( Rule.ENDS_WITH, "ul", Rule.REPLACE_MATCH, "ül" ) ,
      new Rule ( Rule.ENDS_WITH, "es", Rule.REPLACE_MATCH, "e" ) ,
      new Rule ( Rule.ENDS_WITH, "ed", Rule.REPLACE_MATCH, "e" ) ,
      new Rule ( Rule.ENDS_WITH, "en", Rule.REPLACE_MATCH, "en" ) ,
      new Rule ( Rule.ENDS_WITH, "el", Rule.REPLACE_MATCH, "el" ) ,
      new Rule ( Rule.ENDS_WITH, "is", Rule.REPLACE_MATCH, "i" ) ,
      new Rule ( Rule.ENDS_WITH, "id", Rule.REPLACE_MATCH, "i" ) ,

      //
      //	5. The suffixes g and b are devoiced and rendered "k" and "p," respectively,
      //  since this most closely approximates actual pronunciation.
      //
      new Rule ( Rule.ENDS_WITH_FOLLOWS_VOWEL, "g", Rule.REPLACE_MATCH, "k" ),
      new Rule ( Rule.ENDS_WITH_FOLLOWS_VOWEL, "b", Rule.REPLACE_MATCH, "p" ),

      // these rule must be *last*
      new Rule ( Rule.ENDS_WITH, "[aeiou]", Rule.SET_FLAG, new Integer(Rule.FLAG_ENDS_WITH_VOWEL) ),
      new Rule ( Rule.ENDS_WITH, "(b|d|g|l|m|n|N|r)", Rule.SET_FLAG, new Integer ( Rule.FLAG_ENDS_WITH_CONSONANT_VOICED ) ),
      new Rule ( Rule.ENDS_WITH, "(k|p|s|t)", Rule.SET_FLAG, new Integer ( Rule.FLAG_ENDS_WITH_CONSONANT_VOICELESS ) ),

      new Rule ( Rule.ENDS_WITH, "a'i", Rule.REPLACE_MATCH, "e" ) ,
      new Rule ( Rule.ENDS_WITH, "e'i", Rule.REPLACE_MATCH, "e" ) ,
      new Rule ( Rule.ENDS_WITH, "o'i", Rule.REPLACE_MATCH, "ö" ) ,
      new Rule ( Rule.ENDS_WITH, "u'i", Rule.REPLACE_MATCH, "ü" ),

      //
      // 14. Multiple vowels that have discrete sounds and are connected by an achung (') are transcribed by dropping the achung
      // (at this point all achungs have been removed by previous rules)
      //
      new Rule ( Rule.CONTAINS, "'", Rule.REPLACE_MATCH, "" ),

      //new Rule ( Rule.CONTAINS, "X", Rule.REPLACE_MATCH, "ts" ),
      //new Rule ( Rule.CONTAINS, "T", Rule.REPLACE_MATCH, "t" ),
      //new Rule ( Rule.CONTAINS, "P", Rule.REPLACE_MATCH, "p" ),

    } ;

    rules = thdlRules ;
  }

  /**
   * applyRule
   */
  protected String applyRule ( Rule rule, String in  ) throws Exception
  {
    switch ( rule.getAction () )
    {
      //
      // Rule.REPLACE_MATCH - if text defined in condArg found, replace it with actionArg unconditionally
      //
    case Rule.REPLACE_MATCH :
    {
      Matcher matcher = rule.getPattern ().matcher ( in ) ;
      return matcher.replaceFirst ( (String)rule.getActionArg () ) ;
    }

    //
    // Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL - if text defined in condArg found,
    // AND last syllable ends with a vowel - replace it with actionArg
    //
    case Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOWEL :
      if ( 0 != ( stateFlag & Rule.FLAG_ENDS_WITH_VOWEL ) )
      {
        Matcher matcher = rule.getPattern ().matcher ( in ) ;
        return matcher.replaceFirst ( (String)rule.getActionArg () ) ;
      }
      break ;

      //
      // Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT - if text defined in condArg found,
      // AND last syllable ends with a voiced consonant - replace it with actionArg
      //
    case Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICED_CONSONANT :
      if ( 0 != ( stateFlag & Rule.FLAG_ENDS_WITH_CONSONANT_VOICED ) )
      {
        Matcher matcher = rule.getPattern ().matcher ( in ) ;
        return matcher.replaceFirst ( (String)rule.getActionArg () ) ;
      }
      break ;

      //
      // Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT - if text defined in condArg found,
      // AND last syllable ends with a voiceless consonant - replace it with actionArg
      //
    case Rule.REPLACE_IF_LAST_SYLLABLE_ENDED_WITH_VOICELESS_CONSONANT :
      if ( 0 != ( stateFlag & Rule.FLAG_ENDS_WITH_CONSONANT_VOICELESS ) )
      {
        Matcher matcher = rule.getPattern ().matcher ( in ) ;
        return matcher.replaceFirst ( (String)rule.getActionArg () ) ;
      }
      break ;

      //
      // Rule.SET_FLAG - set the flag
      //
    case Rule.SET_FLAG :
      if ( rule.getPattern ().matcher ( in ).find () )
      {
        stateFlag = ((Integer) rule.getActionArg ()).intValue () ;
      }
      break ;

      //
      // Rule.SET_RETURN_FLAG - set the return flag
      //
    case Rule.SET_RETURN_FLAG :
      if ( rule.getPattern ().matcher ( in ).find () )
      {
        returnFlag = ((Integer) rule.getActionArg ()).intValue () ;
      }
      break ;

      //
      // shouldn't ever happen
      //
    default:
      throw new Exception ( "Invalid action." ) ;
    }

    return in ;
  }

  /**
   * processWord
   */
  protected String processWord ( String in ) throws Exception
  {
    returnFlag = Rule.DO_NOTHING ;
    lastWordOriginal = in ;

    //
    // run the word through all rules
    //
    for ( int i = 0; i < rules.length; i++ )
    {
      in = applyRule ( rules [i], in ) ;
    }

    return in ;
  }

  private static boolean isVowel ( char c )
  {
    return ( -1 != "aeiouéöü".indexOf ( c ) ) ;
  }

  private static String dropSuffix ( String in )
  {
    while ( !isVowel ( in.charAt ( in.length () - 1 ) ) )
      in = in.substring ( 0, in.length () - 1 ) ;

    return in ;
  }

  private String nasalize ( String in, String followingSyllable )
  {
    if ( followingSyllable.startsWith ( "'by" ) || followingSyllable.startsWith ( "'br" ) )
      in = in + "n" ;
    else if ( followingSyllable.startsWith ( "'b" ) || lastWordOriginal.startsWith ( "'ph" ) )
      in = in + "m" ;
    else
      in = in + "n" ;

    return in ;
  }

  public String processWylie ( String in ) throws Exception
  {
    return process ( eliminateDigraphs ( in ) ) ;
  }

  public static String eliminateDigraphs ( String in )
  {
    //
    // replace the roman digraphs
    //
    in = in.replaceAll ( "bh", "B" ) ;
    in = in.replaceAll ( "ch", "C" ) ;
    in = in.replaceAll ( "dz", "D" ) ;
    in = in.replaceAll ( "kh", "K" ) ;
    in = in.replaceAll ( "ng", "N" ) ;
    in = in.replaceAll ( "ph", "P" ) ;
    in = in.replaceAll ( "sh", "S" ) ;
    in = in.replaceAll ( "th", "T" ) ;
    in = in.replaceAll ( "ts", "X" ) ;
    in = in.replaceAll ( "tsh", "Q" ) ;
    in = in.replaceAll ( "zh", "Z" ) ;

    return in ;
  }

  /**
   * process
   */
  public String process ( String in ) throws Exception
  {
    stateFlag = 0 ;

    String out = "" ;

    in = in.trim () ;
    String [] matchWords = in.split ( "[^a-z'A-Z]+" ) ;

    for ( int i = 0; i < matchWords.length; i++ )
    {
      String newWord = processWord ( matchWords [i] ) ;
      if ( i > 0 && Rule.DROP_SUFFIX_AND_NASALIZE == returnFlag )
      {
        out = dropSuffix ( out ) ;
        out = nasalize ( out, newWord ) ;
      }

      if ( out.length () > 0 )
        out += " " ;

      out += newWord ;
    }

    return out ;
  }
}