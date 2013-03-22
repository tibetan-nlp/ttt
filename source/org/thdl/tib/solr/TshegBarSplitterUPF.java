package org.thdl.tib.solr;

import org.thdl.tib.solr.util.TshegBarUtils;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class TshegBarSplitterUPF extends FieldMutatingUpdateProcessorFactory {
	private static final String OUTPUT_DELIMITER_PARAM = "outputDelimiter";
	private static final String OUTPUT_DELIMITER_DEFAULT = " ";
    private String outputDelimiter;

	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object outputDelimiterParam = args.remove(OUTPUT_DELIMITER_PARAM);
	    if (null == outputDelimiterParam || !(outputDelimiterParam instanceof String)) {
	        outputDelimiter = OUTPUT_DELIMITER_DEFAULT;
	    }
	    else {
	        outputDelimiter = (String)outputDelimiterParam;
	    }
        super.init(args);
    }
  
    @Override
    public FieldMutatingUpdateProcessor.FieldNameSelector getDefaultSelector(final SolrCore core) {
        return FieldMutatingUpdateProcessor.SELECT_NO_FIELDS;
    }
  
    @Override
    public UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) {
        return new FieldValueMutatingUpdateProcessor(getSelector(), next) {
            protected Object mutateValue(final Object src) {
                if (src instanceof CharSequence) {
                    CharSequence s = (CharSequence)src;
                    String[] tokens = s.toString().split("\\s+");
                    List<String> syllables = new LinkedList<String>();
                    
                    for (int j=0; j<tokens.length; j++) {
                        int start = 0;
                        for (int i=0; i<tokens[j].length(); i++) {
                            if (TshegBarUtils.isTshegBar(tokens[j].charAt(i))) { //first tsheg bar part of previous syllable
                                syllables.add(tokens[j].subSequence(start, i+1).toString());
                                start = i+1;
                            }
                            else if (TshegBarUtils.isPunctuation(tokens[j].charAt(i)) || i == tokens[j].length()-1) { //all other punctuation gets own syllable
                                if (start != i) {
                                    syllables.add(tokens[j].subSequence(start, i).toString());
                                }
                                syllables.add(tokens[j].substring(i, i+1));
                                start = i+1;
                            }
                            else if (i == tokens[j].length()-1) {
                                syllables.add(tokens[j].subSequence(start, i+1).toString());
                                start = i+1;
                            }
                        }
                    }
                    return StringUtils.join(syllables.iterator(), outputDelimiter);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
