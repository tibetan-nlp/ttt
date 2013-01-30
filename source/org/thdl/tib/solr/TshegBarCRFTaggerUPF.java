package org.thdl.tib.solr;

import org.chasen.crfpp.Tagger;

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

public class TshegBarCRFTaggerUPF extends FieldMutatingUpdateProcessorFactory {
    private static final String MODEL_FILE_PARAM = "modelFile";
	private static final String TAG_DELIMITER_PARAM = "tagDelimiter";
	private static final String DELIMIT_OUTPUT_PARAM = "delimitOutput";
    
    private static final String MODEL_FILE_DEFAULT = "";
    private static final String TAG_DELIMITER_DEFAULT = "|";
	private static final String DELIMIT_OUTPUT_DEFAULT = " ";
    
    private String modelFile, tagDelimiter, delimitOutput;

	@SuppressWarnings("unchecked")
	@Override
	public void init(NamedList args) {
	    Object modelFileParam = args.remove(MODEL_FILE_PARAM);
	    if (null == modelFileParam || !(modelFileParam instanceof String)) {
	        modelFile = MODEL_FILE_DEFAULT;
	    }
	    else {
	        modelFile = (String)modelFileParam;
	    }
	    
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
  public FieldMutatingUpdateProcessor.FieldNameSelector 
    getDefaultSelector(final SolrCore core) {

    return FieldMutatingUpdateProcessor.SELECT_NO_FIELDS;

  }

  @Override
  public UpdateRequestProcessor getInstance(SolrQueryRequest req,
                                            SolrQueryResponse rsp,
                                            UpdateRequestProcessor next) {
    return new FieldValueMutatingUpdateProcessor(getSelector(), next) {
      protected Object mutateValue(final Object src) {
        if (src instanceof CharSequence) {
          String[] syllables = ((CharSequence)src).toString().split("\\s+");
          
          Tagger tagger = new Tagger("-m " + modelFile + " -v 3 -n2");
          tagger.clear();
          for (int i=0; i<syllables.length; i++) {
              tagger.add(syllables[i].replace('|','\t'));
          }
          if (!tagger.parse())
              return src;

          List<String> tags = new LinkedList<String>();
          
          for (int i = 0; i < tagger.size(); ++i) {
              String tag = "";
              for (int j = 0; j < tagger.xsize(); ++j) {
                  tag = tag + tagger.x(i, j) + "|";
              }
              tag = tag + tagger.y2(i);
              tags.add(tag);
          }
          
          return StringUtils.join(tags.iterator(), " ");
        }
        else 
        {
        	return src;
        }
      }
    };
  }    
  
  static {
      try {
          System.out.println(System.getProperty("java.library.path"));
          System.load("/opt/solr/4/lib/libCRFPP.jnilib");
      } catch (UnsatisfiedLinkError e) {
          System.err.println("Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
          System.exit(1);
      }
  }

}
