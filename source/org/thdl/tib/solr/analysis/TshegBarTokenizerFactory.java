package org.thdl.tib.solr.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;

import java.io.Reader;
import java.util.Map;

public class TshegBarTokenizerFactory extends TokenizerFactory {
	
  @Override
  public void init(Map<String,String> args) {
    super.init(args);
    assureMatchVersion();
  }

  public Tokenizer create(Reader input) {
    TshegBarTokenizer tokenizer = new TshegBarTokenizer(luceneMatchVersion, input);
    return tokenizer;
  }
}
