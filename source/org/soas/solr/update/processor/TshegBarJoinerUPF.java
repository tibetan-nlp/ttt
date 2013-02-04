package org.thdl.tib.solr;

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

public class TshegBarJoinerUPF extends FieldMutatingUpdateProcessorFactory {
    private static final String SPLIT_INPUT_PARAM = "splitInput";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
    
    private static final String SPLIT_INPUT_DEFAULT = "\\s+";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    
    private String splitInput, delimitOutput;

	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object splitInputParam = args.remove(SPLIT_INPUT_PARAM);
	    if (null == splitInputParam || !(splitInputParam instanceof String)) {
	        splitInput = SPLIT_INPUT_DEFAULT;
	    }
	    else {
	        splitInput = (String)splitInputParam;
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
                    String[] tokens = s.toString().split(splitInput);
                    List<String> words = new LinkedList<String>();
                    List<String> syls = new LinkedList<String>();
                    
                    for (int j=0; j<tokens.length; j++) {
                        String[] item = tokens[j].split("\\|");
                        if (item[1].equals("S")) {
                            words.add(item[0]);
                        }
                        else {
                            syls.add(item[0]);
                            if (item[1].equals("E")) {
                                words.add(StringUtils.join(syls.iterator(), ""));
                                syls.clear();
                            }
                        }
                    }
                    return StringUtils.join(words.iterator(), delimitOutput);
                }
                else 
                {
                    return src;
                }
            }
        };
    }
}
