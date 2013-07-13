package org.soas.solr.update.processor;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

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

public class PruneMinorUPF extends FieldMutatingUpdateProcessorFactory {
    private final static Logger log = LoggerFactory.getLogger(PruneMinorUPF.class);
    
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    private String tagDelimiter, delimitOutput;

    private static final Map<String,String> remap;
    
    static {
        remap = new HashMap<String,String>();
        //remap.put("adj","adj");
        remap.put("adv.dir",            "adv");
        remap.put("adv.intense",        "adv");
        remap.put("adv.proclausal",     "adv");
        remap.put("adv.temp",           "adv");
        
        remap.put("case.abl",           "case");
        remap.put("case.agn",           "case");
        remap.put("case.all",           "case");
        remap.put("case.ass",           "case");
        remap.put("case.comp",          "case");
        remap.put("case.ela",           "case");
        remap.put("case.gen",           "case");
        remap.put("case.loc",           "case");
        remap.put("case.term",          "case");
        
        remap.put("cl.focus",           "cl");
        remap.put("cl.lta",             "cl");
        remap.put("cl.quot",            "cl");
        remap.put("cl.tsam",            "cl");
        
        remap.put("cv.abl",             "cv");
        remap.put("cv.agn",             "cv");
        remap.put("cv.all",             "cv");
        remap.put("cv.are",             "cv");
        remap.put("cv.ela",             "cv");
        remap.put("cv.fin",             "cv");
        remap.put("cv.gen",             "cv");
        remap.put("cv.imp",             "cv");
        remap.put("cv.impf",            "cv");
        remap.put("cv.loc",             "cv");
        remap.put("cv.ques",            "cv");
        remap.put("cv.sem",             "cv");
        remap.put("cv.term",            "cv");
        
        remap.put("d.dem",              "d");
        remap.put("d.det",              "d");
        remap.put("d.emph",             "d");
        remap.put("d.indef",            "d");
        remap.put("d.plural",           "d");
        
        //remap.put("dunno","dunno");
        
        remap.put("n.count",            "n");
        remap.put("n.mass",             "n");
        remap.put("n.prop",             "n");
        remap.put("n.rel",              "n");
        
        remap.put("n.v.aux",            "n.v");
        remap.put("n.v.cop",            "n.v.cop");
        remap.put("n.v.fut",            "n.v");
        remap.put("n.v.fut.n.v.past",   "n.v");
        remap.put("n.v.fut.n.v.pres",   "n.v");
        remap.put("n.v.invar",          "n.v");
        remap.put("n.v.neg",            "n.v.neg");
        remap.put("n.v.past",           "n.v");
        remap.put("n.v.past.n.v.pres",  "n.v");
        remap.put("n.v.pres",           "n.v");
        remap.put("n.v.redup",          "n.v");
        
        remap.put("neg",                "neg");
        
        remap.put("num.card",           "num");
        remap.put("num.ord",            "num");
        
        remap.put("p.indef",            "p");
        remap.put("p.interrog",         "p");
        remap.put("p.pers",             "p");
        remap.put("p.refl",             "p");
        
        remap.put("punc",               "punc");
        
        remap.put("v.aux",              "v");
        remap.put("v.cop",              "v.cop");
        remap.put("v.cop.neg",          "v.cop.neg");
        remap.put("v.fut",              "v");
        remap.put("v.fut.v.past",       "v");
        remap.put("v.fut.v.pres",       "v");
        remap.put("v.imp",              "v");
        remap.put("v.invar",            "v");
        remap.put("v.neg",              "v");
        remap.put("v.past",             "v");
        remap.put("v.past.v.pres",      "v");
        remap.put("v.pres",             "v");
    }
    
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
                    String[] tokens = s.toString().split("\\s+");
                    List<String> words = new LinkedList<String>();
                    Pattern pattern = Pattern.compile("[\\[\\]]+");
                    
                    for (int j=0; j<tokens.length; j++) {
                        int n = tokens[j].indexOf(tagDelimiter);
                        
                        String tagString = tokens[j].substring(n+1);
                        if (tagString.charAt(0) != '[') {
                            String mappedTag = remap.containsKey(tagString) ? remap.get(tagString) : tagString;
                            words.add(tokens[j].substring(0, n) + tagDelimiter + mappedTag);
                            //int dot = tagString.indexOf('.');
                            //words.add(tokens[j].substring(0, n) + tagDelimiter + (dot == -1 ? tagString : tagString.substring(0, dot)));
                        }
                        else {
                            String[] tags = pattern.split(tagString.substring(1));
                            Set<String> major = new TreeSet<String>();
                            for (int k=0; k<tags.length; k++) {
                                major.add(remap.containsKey(tags[k]) ? remap.get(tags[k]) : tags[k]);
                                //int dot = tags[k].indexOf('.');
                                //major.add(dot == -1 ? tags[k] : tags[k].substring(0, dot));
                            }
                            words.add(tokens[j].substring(0, n) + tagDelimiter + "[" + StringUtils.join(major.iterator(), "][") + "]");
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
