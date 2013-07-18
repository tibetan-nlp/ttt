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
        remap.put("adv.dir",            "adv.dir");
        remap.put("adv.intense",        "adv.intense");
        remap.put("adv.proclausal",     "adv.proclausal");
        remap.put("adv.temp",           "adv.temp");

        remap.put("case.abl",           "case");
        remap.put("case.agn",           "case");
        remap.put("case.all",           "case");
        remap.put("case.ass",           "case");
        remap.put("case.comp",          "case");
        remap.put("case.ela",           "case");
        remap.put("case.gen",           "case");
        remap.put("case.loc",           "case");
        remap.put("case.term",          "case");
        
        /*
        remap.put("case.abl",           "case.abl");
        remap.put("case.agn",           "case.agn");
        remap.put("case.all",           "case.all");
        remap.put("case.ass",           "case.ass");
        remap.put("case.comp",          "case.comp");
        remap.put("case.ela",           "case.ela");
        remap.put("case.gen",           "case.gen");
        remap.put("case.loc",           "case.loc");
        remap.put("case.term",          "case.term");
        */
        
        remap.put("cl.focus",           "cl.focus");
        remap.put("cl.lta",             "cl.lta");
        remap.put("cl.quot",            "cl.quot");
        remap.put("cl.tsam",            "cl.tsam");
        
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
        
        /*
        remap.put("cv.abl",             "cv.abl");
        remap.put("cv.agn",             "cv.agn");
        remap.put("cv.all",             "cv.all");
        remap.put("cv.are",             "cv.are");
        remap.put("cv.ela",             "cv.ela");
        remap.put("cv.fin",             "cv.fin");
        remap.put("cv.gen",             "cv.gen");
        remap.put("cv.imp",             "cv.imp");
        remap.put("cv.impf",            "cv.impf");
        remap.put("cv.loc",             "cv.loc");
        remap.put("cv.ques",            "cv.ques");
        remap.put("cv.sem",             "cv.sem");
        remap.put("cv.term",            "cv.term");
        */
        
        remap.put("d.dem",              "d.dem");
        remap.put("d.det",              "d.det");
        remap.put("d.emph",             "d.emph");
        remap.put("d.indef",            "d.indef");
        remap.put("d.plural",           "d.plural");
        
        //remap.put("dunno","dunno");
        
        remap.put("n.count",            "n");
        remap.put("n.mass",             "n");
        remap.put("n.prop",             "n");
        remap.put("n.rel",              "n");
        
        remap.put("n.v.aux",            "n.v.aux");
        remap.put("n.v.cop",            "n.v.cop");
        remap.put("n.v.fut",            "n.v");
        remap.put("n.v.fut.n.v.past",   "n.v");
        remap.put("n.v.fut.n.v.pres",   "n.v");
        remap.put("n.v.invar",          "n.v");
        remap.put("n.v.neg",            "n.v.neg");
        remap.put("n.v.past",           "n.v");
        remap.put("n.v.past.n.v.pres",  "n.v");
        remap.put("n.v.pres",           "n.v");
        remap.put("n.v.redup",          "n.v.redup");
        
        remap.put("neg",                "neg");
        
        remap.put("num.card",           "num.card");
        remap.put("num.ord",            "num.ord");
        
        remap.put("p.indef",            "p.indef");
        remap.put("p.interrog",         "p.interrog");
        remap.put("p.pers",             "p.pers");
        remap.put("p.refl",             "p.refl");
        
        remap.put("punc",               "punc");
        
        remap.put("v.aux",              "v.aux");
        remap.put("v.cop",              "v.cop");
        remap.put("v.cop.neg",          "v.cop.neg");
        remap.put("v.fut",              "v");
        remap.put("v.fut.v.past",       "v");
        remap.put("v.fut.v.pres",       "v");
        remap.put("v.imp",              "v");
        remap.put("v.invar",            "v");
        remap.put("v.neg",              "v.neg");
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
