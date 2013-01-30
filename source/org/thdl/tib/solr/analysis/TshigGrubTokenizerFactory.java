package org.thdl.tib.solr.analysis;

import org.apache.lucene.analysis.util.TokenizerFactory;
import java.io.Reader;
import java.util.Map;

public class TshigGrubTokenizerFactory extends TokenizerFactory {
	@Override
	public void init(Map<String,String> args) {
		super.init(args);
		assureMatchVersion();
	}
	
    public TshigGrubTokenizer create(Reader input) {
        return new TshigGrubTokenizer(luceneMatchVersion,input);
    }
}

