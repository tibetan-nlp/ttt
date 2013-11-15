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

import java.util.Collection;
import java.util.Iterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.update.AddUpdateCommand;

public class UnambiguousTagCountUPF extends UpdateRequestProcessorFactory {
    private static final String TAG_PARAM = "datatags";
    private String tagFieldName;

	public static final String DELIMITER = " ";
	public static final String POS_PARAM = "pos";
	public static final String TOKEN_COUNT_PARAM = "tokenCount";
	public static final String UNAMBIG_COUNT_PARAM = "unambigCount";
  
	private String posFieldName = null;
	private String tokenCountFieldName = null;
	private String unambigCountFieldName = null;
  
	public void init(NamedList args) {
		Object o = args.remove(POS_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    posFieldName = o.toString();
		}
		
		o = args.remove(TOKEN_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tokenCountFieldName = o.toString();
		}
		
		o = args.remove(UNAMBIG_COUNT_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    unambigCountFieldName = o.toString();
		}
		
		o = args.remove(TAG_PARAM);
		if (null == o || !(o instanceof String)) {
		    //error
		}
		else {
		    tagFieldName = o.toString();
		}
	}
	    
	@Override
	public final UpdateRequestProcessor getInstance(SolrQueryRequest req, SolrQueryResponse rsp, UpdateRequestProcessor next) 
    {
		return new UpdateRequestProcessor(next) {
			@Override
			public void processAdd(AddUpdateCommand cmd) throws IOException {

				final SolrInputDocument doc = cmd.getSolrInputDocument();
				
				Pattern oneTag = Pattern.compile("\\[?([^\\]]+)\\]?");

				Collection c = doc.getFieldValues(tagFieldName);
				if (c != null) {
				    Iterator it = c.iterator();
	        
				    while (it.hasNext()) {
				        String next = (String)it.next();
                        
				        if (doc.containsKey(posFieldName + "_" + next)) {
				            int token_count = 0;
				            int unambig_count = 0;
				            
                            String posFieldValue = (String)doc.getFieldValue(posFieldName + "_" + next);
                            String[] pos = posFieldValue.split("\\s+");

				            for (int i=0; i<pos.length; i++) {
				                String tags = pos[i].substring(pos[i].indexOf('|')+1);
				                Matcher m = oneTag.matcher(tags);
                                if (m.matches()) {
                                    unambig_count += 1;
                                }
                            }
                            
                            token_count += pos.length;
                            
                            SolrInputField tokenCountField = new SolrInputField(tokenCountFieldName + "_" + next);
                            tokenCountField.addValue(new Integer(token_count), 1.0f);
                            doc.put(tokenCountFieldName + "_" + next, tokenCountField);
                                
                            SolrInputField unambigCountField = new SolrInputField(unambigCountFieldName + "_" + next);
                            unambigCountField.addValue(new Integer(unambig_count), 1.0f);
                            doc.put(unambigCountFieldName + "_" + next, unambigCountField);
                                
				        }
                    }
                }
				
				super.processAdd(cmd);
			}
		};
	}
}
