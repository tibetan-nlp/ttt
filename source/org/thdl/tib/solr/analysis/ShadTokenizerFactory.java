package org.thdl.tib.solr.analysis;

import org.apache.lucene.analysis.util.TokenizerFactory;
import java.io.Reader;

public class ShadTokenizerFactory extends TokenizerFactory {
	@Override
	public void init(Map<String,String> args) {
		super.init(args);
		assureMatchVersion();
	}
	
    public ShadTokenizer create(Reader input) {
        return new ShadTokenizer(input);
    }
}

