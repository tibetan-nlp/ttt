package org.soas.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class MagicPrefixFilterFactory extends TokenFilterFactory {
  
  public  MagicPrefixFilterFactory(Map<String,String> args) {
    super(args);
    /*pattern = getPattern(args, "pattern");
    preserveOriginal = args.containsKey("preserve_original") ? Boolean.parseBoolean(args.get("preserve_original")) : true;
    */
  }
  @Override
  public MagicPrefixTokenFilter create(TokenStream input) {
    return new MagicPrefixTokenFilter(input, "#", "\\s+");
  }
}
