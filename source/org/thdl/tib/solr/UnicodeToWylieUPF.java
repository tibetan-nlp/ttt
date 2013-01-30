package org.thdl.tib.solr;

import org.thdl.tib.scanner.BasicTibetanTranscriptionConverter;

import org.apache.solr.update.processor.FieldMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldValueMutatingUpdateProcessor;
import org.apache.solr.update.processor.FieldMutatingUpdateProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class UnicodeToWylieUPF extends FieldMutatingUpdateProcessorFactory {
	
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
          CharSequence s = (CharSequence)src;
          return BasicTibetanTranscriptionConverter.unicodeToWylie(s.toString());
        }
        else 
        {
        	return src;
        }
      }
    };
  }
}
