package org.soas.solr.analysis;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class PermuteShingleFilterFactory extends TokenFilterFactory {
    
    public PermuteShingleFilterFactory(Map<String,String> args) {
        super(args);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }
  
    @Override
    public PermuteShingleTokenFilter create(TokenStream input) {
        return new PermuteShingleTokenFilter(input);
    }
}
