package org.thdl.tib.solr;

import org.thdl.tib.solr.util.TshegBarUtils;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class TshegBarTagMergerUPF extends FieldMutatingUpdateProcessorFactory {
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private static final String IF_NEXT_PARAM = "ifNext";
    private String tagDelimiter, delimitOutput;
    private Set<String> ifNext;

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
	    
	    Object ifNextParam = args.remove(IF_NEXT_PARAM);
	    if (null == ifNextParam || !(ifNextParam instanceof String)) {
	        //
	    }
	    else {
	        String[] nexts = ((String)ifNextParam).split(",\\s+");
	        ifNext = new HashSet<String>(Arrays.asList(nexts));
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
                    
                    int j = 0;
                    for (; j<tokens.length-1; j++) {
                        if (ifNext.contains(tokens[j+1]) && TshegBarUtils.isOpenEnded(tokens[j].charAt(tokens[j].length()-1))) {
                            String[] a = tokens[j].split("\\|");
                            String[] b = tokens[j+1].split("\\|");
                            syllables.add(a[0] + b[0] + "|" + a[1] + b[1]);
                            j++;
                        }
                        else {
                            syllables.add(tokens[j]);
                        }
                    }
                    if (j == tokens.length-1) {
                        syllables.add(tokens[tokens.length-1]);
                    }
                    
                    return StringUtils.join(syllables.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
