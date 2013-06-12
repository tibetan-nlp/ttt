package org.thdl.tib.crf;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.thdl.tib.solr.util.TshegBarUtils;

public class SyllableFinalUPF extends FieldMutatingUpdateProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(SyllableFinalUPF.class);
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private String tagDelimiter, delimitOutput;
    
    
	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object tagDelimiterParam = args.remove(TAG_DELIMITER_PARAM);
	    if (null == tagDelimiterParam || !(tagDelimiterParam instanceof String)) {
	        tagDelimiter = TAG_DELIMITER_DEFAULT;
	    }
	    else {
	        tagDelimiter = (String)tagDelimiterParam;
	    }
	    
	    Object delimitOutputParam = args.remove(DELIMIT_OUTPUT_PARAM);
	    if (null == delimitOutputParam || !(delimitOutputParam instanceof String)) {
	        delimitOutput = DELIMIT_OUTPUT_DEFAULT;
	    }
	    else {
	        delimitOutput = (String)delimitOutputParam;
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
                    String[] in = s.toString().split("\\s+");
                    List<String> out = new LinkedList<String>();
                    for (int j=0; j<in.length; j++) {
                        int k = in[j].indexOf('|'); //should really be tagDelimiter
                        String rest = (k == -1) ? "" : in[j].substring(k);
                        String word = (k == -1) ? in[j] : in[j].substring(0,k);
                        int tsheg = TshegBarUtils.isTshegBar(word.charAt(word.length()-1)) ? 1 : 0;
                        String last = word.substring(word.length()-1-tsheg, word.length()-tsheg);
                        out.add(word + tagDelimiter + last + tagDelimiter + Integer.toString(tsheg) + rest);
                    }
                    
                    return StringUtils.join(out.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
