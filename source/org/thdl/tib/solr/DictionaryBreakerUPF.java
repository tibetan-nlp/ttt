package org.thdl.tib.solr;

import org.thdl.tib.scanner.*;

import java.io.*;
import java.net.URL;

import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class DictionaryBreakerUPF extends FieldMutatingUpdateProcessorFactory {
	private TibetanScanner scanner;
	public static final String wyliePunc = "/;|!:^@#$%=,<>(){}[]";
	
  @Override
  public FieldMutatingUpdateProcessor.FieldNameSelector 
    getDefaultSelector(final SolrCore core) {

    return FieldMutatingUpdateProcessor.SELECT_NO_FIELDS;

  }
  
  protected String wylieToUnicodeTidy(String wylie) {
      String unicode = BasicTibetanTranscriptionConverter.wylieToUnicode(wylie);
      if (unicode.startsWith("[#ERROR")) {
          return wylie;
      }
      else {
          return unicode;
      }
  }
  
  @Override
  public UpdateRequestProcessor getInstance(SolrQueryRequest req,
                                            SolrQueryResponse rsp,
                                            UpdateRequestProcessor next) {
    return new FieldValueMutatingUpdateProcessor(getSelector(), next) {
      protected Object mutateValue(final Object src) {
        if (src instanceof CharSequence) {
          CharSequence s = (CharSequence)src;
          
          String wylie = BasicTibetanTranscriptionConverter.unicodeToWylie(s.toString());

		  try {
			  URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
			  File file = new File(location.getPath());
			  String prefix = file.getParent();
			  prefix = (prefix == null) ? "": prefix + File.separator;
			  scanner = new LocalTibetanScanner(prefix + "thl-dicts/thl");
			  scanner.getDictionaryDescriptions();
			  scanner.scanBody(wylie);
			  Token[] tokens = scanner.getTokenArray();
			  if (tokens == null) {
			      return src;
			  }
			  else {
                  List<String> tokenized = new LinkedList<String>();
                  if (tokens.length > 0) {
                      int i=0;
                      for (; i<tokens.length-1; i++) {
                          String tokStr = tokens[i].getRawValue();
                          if (wyliePunc.indexOf(tokStr.charAt(tokStr.length()-1)) != -1) {
                              tokenized.add(wylieToUnicodeTidy(wylie.substring(tokens[i].getOffset(), tokens[i].getOffset() + tokStr.length())));
                          }
                          else 
                          {
                              tokenized.add(wylieToUnicodeTidy(wylie.substring(tokens[i].getOffset(), tokens[i+1].getOffset())));
                          }
                      }
                      int end = tokens[i].getOffset() + tokens[i].getRawValue().length();
                      if (end > wylie.length()) end = wylie.length();
                      tokenized.add(wylieToUnicodeTidy(wylie.substring(tokens[i].getOffset(), end)));
                  }
                  scanner.clearTokens();
                  scanner.finishUp();
                  tokens = scanner.getTokenArray();
                      if (tokens != null) {
                      for (int i=0; i<tokens.length; i++) {
                          int start = tokens[i].getOffset();
                          int length = tokens[i].getRawValue().length();
                          tokenized.add(wylieToUnicodeTidy(wylie.substring(start, start+length)));
                      }
                      scanner.clearTokens();
                  }
                  return StringUtils.join(tokenized.iterator(), " ");
              }
		  } 
		  catch (Exception e) {
		      final Writer result = new StringWriter();
		      final PrintWriter printWriter = new PrintWriter(result);
		      e.printStackTrace(printWriter);
		      return result.toString();
		  }
        }
        
        return src;
      }
    };
  }
}
