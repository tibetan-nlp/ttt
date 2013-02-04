/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thdl.tib.solr;

import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import java.io.IOException;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

/**
 * A non-duplicate processor. Removes duplicates in the specified fields.
 * 
 * <pre class="prettyprint" >
 * &lt;updateRequestProcessorChain name="uniq-fields"&gt;
 *   &lt;processor class="org.apache.solr.update.processor.PageTransitionUpdateProcessorFactory"&gt;
 *     &lt;lst name="fields"&gt;
 *       &lt;str&gt;uniq&lt;/str&gt;
 *       &lt;str&gt;uniq2&lt;/str&gt;
 *       &lt;str&gt;uniq3&lt;/str&gt;
 *     &lt;/lst&gt;      
 *   &lt;/processor&gt;
 *   &lt;processor class="solr.RunUpdateProcessorFactory" /&gt;
 * &lt;/updateRequestProcessorChain&gt;</pre>
 * 
 */
public class PageTransitionUPF extends UpdateRequestProcessorFactory {

	public static final String DELIMITER = " ";
	public static final String TRANSITION_PARAM = "trans";
	public static final String PAGE_PARAM = "page";
	public static final String DEST_PARAM = "dest";
  
	private String transFieldName = null;
	private String pageFieldName = null;
	private String destFieldName = null;
  
	public void init(NamedList args) {
		Object o = args.remove(TRANSITION_PARAM);
		transFieldName = o.toString();
		
		o = args.remove(PAGE_PARAM);
		pageFieldName = o.toString();
		
		o = args.remove(DEST_PARAM);
		destFieldName = o.toString();
	}


	@Override
	public final UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) 
    {
		return new UpdateRequestProcessor(next) {
			@Override
			public void processAdd(AddUpdateCommand cmd) throws IOException {

				final SolrInputDocument doc = cmd.getSolrInputDocument();

				// preserve initial values and boost (if any)
        		SolrInputField destField = doc.containsKey(destFieldName) ? doc.getField(destFieldName) : new SolrInputField(destFieldName); 

        		if (doc.containsKey(pageFieldName)) {
                    String page = (String)doc.getField(pageFieldName).getValue();
                    
                    if (doc.containsKey(transFieldName)) {
                        String trans = (String)doc.getField(transFieldName).getValue();
                
                        boolean matched = false;
                        int i=1;
                        for (; i<page.length()+1; i++) {
                            if (trans.endsWith(page.substring(0,i))) {
                                matched = true;
                                break;
                            }
                        }
                        //trans = trans.concat(DELIMITER);
                        String joined = matched ? trans.concat(page.substring(i)) : trans.concat(page);
                        
                        destField.addValue(joined, 1.0f);
                    }
                    else {
                        destField.addValue(page, 1.0f);
                    }
    
                    doc.put(destFieldName, destField);
                }
                
				super.processAdd(cmd);
			}
		};
	}
}
