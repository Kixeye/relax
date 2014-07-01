package com.kixeye.relax;

/*
 * #%L
 * Relax
 * %%
 * Copyright (C) 2014 KIXEYE, Inc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * HTTP response.
 * 
 * @author ebahtijaragic
 *
 * @param <T>
 */
public class HttpResponse<T> {
	private final int statusCode;
	private final Map<String, List<String>> headers = new HashMap<>();
	private final SerializedObject<T> body;
	
	/**
	 * @param statusCode
	 * @param headers
	 * @param body
	 */
	protected HttpResponse(int statusCode, Map<String, List<String>> headers, SerializedObject<T> body) {
		this.statusCode = statusCode;
		this.body = body;
		
		if (headers != null) {
			for (Entry<String, List<String>> headerEntry : headers.entrySet()) {
				this.headers.put(headerEntry.getKey(), new ArrayList<>(headerEntry.getValue()));
			}
		}
	}
	
	/**
	 * @param result
	 * @param serDe
	 * @param objectType
	 * @throws IOException
	 */
	protected HttpResponse(org.apache.http.HttpResponse result, RestClientSerDe serDe, Class<T> objectType) throws IOException {
		this.statusCode = result.getStatusLine().getStatusCode();
		
		Header[] headers = result.getAllHeaders();
		if (headers != null) {
			for (Header header : headers) {
				List<String> headerValues = this.headers.get(header.getName());
				if (headerValues == null) {
					headerValues = new ArrayList<>();
					this.headers.put(header.getName(), headerValues);
				}
				headerValues.add(header.getValue());
			}
		}
		
		HttpEntity entity = result.getEntity();
		byte[] data = null;
		
		if (entity != null) {
			data = EntityUtils.toByteArray(entity);
			EntityUtils.consumeQuietly(entity);
		}
		
		Header contentTypeHeader = result.getFirstHeader("Content-Type");
		
		if (!Void.class.equals(objectType)) {
			this.body = new SerializedObject<>(serDe, contentTypeHeader != null ? contentTypeHeader.getValue() : null, data, objectType);
		} else {
			this.body = null;
		}
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the headers
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	/**
	 * @return the body
	 */
	public SerializedObject<T> getBody() {
		return body;
	}
}
