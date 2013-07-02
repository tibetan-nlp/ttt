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

package org.soas.solr.update.processor;

import org.apache.solr.update.processor.UpdateRequestProcessorFactory;
import org.apache.solr.update.processor.UpdateRequestProcessor;

import java.io.IOException;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

public class AppendTagUPF extends UpdateRequestProcessorFactory {
	public static final String DELIMITER = " ";
	public static final String SOURCE_PARAM = "source";
	public static final String POS_PARAM = "pos";
  
	private String posFieldName = null;
	private String sourceFieldName = null;
  
	public void init(NamedList args) {
		Object o = args.remove(POS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    posFieldName = o.toString();
		}
		
		o = args.remove(SOURCE_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    sourceFieldName = o.toString();
		}
	}
	    
	@Override
	public final UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) 
    {
		return new UpdateRequestProcessor(next) {
			@Override
			public void processAdd(AddUpdateCommand cmd) throws IOException {

				final SolrInputDocument doc = cmd.getSolrInputDocument();
	    
				if (doc.containsKey(sourceFieldName) && doc.containsKey(posFieldName)) {
				    String sourceFieldValue = (String)doc.getFieldValue(sourceFieldName);
				    String posFieldValue = (String)doc.getFieldValue(posFieldName);
				    String[] source = sourceFieldValue.split("\\s+");
				    String[] pos = posFieldValue.split("\\s+");
				    
				    if (source.length == pos.length) {
				        StringBuffer sb = new StringBuffer();
				        for (int i=0; i< source.length; i++) {
				            sb.append(source[i]);
				            sb.append(pos[i].substring(pos[i].indexOf('|')));
				            sb.append(' ');
				        }
				        doc.setField(sourceFieldName, sb.toString(), 1.0f);
				    }
                }
				
				super.processAdd(cmd);
			}
		};
	}
}
