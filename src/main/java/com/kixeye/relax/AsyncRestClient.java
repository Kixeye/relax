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

import com.kixeye.relax.util.UrlUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * A REST Async HTTP Client.
 * 
 * @author ebahtijaragic
 */
public class AsyncRestClient implements RestClient {
	private OkHttpClient httpClient;

	private RestClientSerDe serDe;
	
	private String uriPrefix = "";

	private String userAgentName;
	
	/**
	 * Creates an {@link AsyncRestClient}
	 */
	protected AsyncRestClient() {
		
	}
	
	/**
	 * Sets the uri prefix.
	 * 
	 * @param uriPrefix
	 */
	protected void setUriPrefix(String uriPrefix) {
		if (uriPrefix == null) {
			uriPrefix = "";
		}
		
		this.uriPrefix = uriPrefix;
	}
	
	/**
	 * Sets the serDe.
	 * 
	 * @param serDe
	 */
	protected void setSerDe(RestClientSerDe serDe) {
		this.serDe = serDe;
	}
	
	/**
	 * Sets the http client.
	 * 
	 * @param httpClient
	 * @param isHttpClientReused
	 */
	protected void setHttpClient(OkHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/**
	 * Clsoes the client.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void close() throws IOException {
		// nothing to do
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#get(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <O> HttpPromise<HttpResponse<O>> get(final String path, String acceptHeader, final Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<HttpResponse<O>> promise = new HttpPromise<>();
		
		Request.Builder requestBuilder = new Request.Builder().get().url(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		if (userAgentName != null) {
			requestBuilder = requestBuilder.addHeader("User-Agent", userAgentName);
		}
		
		if (acceptHeader != null) {
			requestBuilder = requestBuilder.addHeader("Accept", acceptHeader);
		}
		
		httpClient.newCall(requestBuilder.build()).enqueue(new AsyncRestClientResponseCallback<>(responseType, promise));
		
		return promise;
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#post(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <I, O> HttpPromise<HttpResponse<O>> post(String path, String contentTypeHeader, String acceptHeader, I requestObject, final Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<HttpResponse<O>> promise = new HttpPromise<>();
		
		RequestBody body = RequestBody.create(contentTypeHeader == null ? null : MediaType.parse(contentTypeHeader), serDe.serialize(contentTypeHeader, requestObject));
		
		Request.Builder requestBuilder = new Request.Builder().post(body).url(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		if (userAgentName != null) {
			requestBuilder = requestBuilder.addHeader("User-Agent", userAgentName);
		}
		
		if (contentTypeHeader != null) {
			requestBuilder = requestBuilder.addHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			requestBuilder = requestBuilder.addHeader("Accept", acceptHeader);
		}
		
		httpClient.newCall(requestBuilder.build()).enqueue(new AsyncRestClientResponseCallback<>(responseType, promise));
		
		return promise;
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#put(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Object)
	 */
	@Override
	public <I, O> HttpPromise<HttpResponse<O>> put(String path, String contentTypeHeader, String acceptHeader, I requestObject, Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<HttpResponse<O>> promise = new HttpPromise<>();
		
		RequestBody body = RequestBody.create(contentTypeHeader == null ? null : MediaType.parse(contentTypeHeader), serDe.serialize(contentTypeHeader, requestObject));
		
		Request.Builder requestBuilder = new Request.Builder().put(body).url(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		if (userAgentName != null) {
			requestBuilder = requestBuilder.addHeader("User-Agent", userAgentName);
		}
		
		if (contentTypeHeader != null) {
			requestBuilder = requestBuilder.addHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			requestBuilder = requestBuilder.addHeader("Accept", acceptHeader);
		}
		
		httpClient.newCall(requestBuilder.build()).enqueue(new AsyncRestClientResponseCallback<>(responseType, promise));
		
		return promise;
	}

	/**
	 * @see com.kixeye.relax.RestClient#patch(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Object)
	 */
	@Override
	public <I, O> HttpPromise<HttpResponse<O>> patch(String path, String contentTypeHeader, String acceptHeader, I requestObject, Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<HttpResponse<O>> promise = new HttpPromise<>();
		
		RequestBody body = RequestBody.create(contentTypeHeader == null ? null : MediaType.parse(contentTypeHeader), serDe.serialize(contentTypeHeader, requestObject));
		
		Request.Builder requestBuilder = new Request.Builder().patch(body).url(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		if (userAgentName != null) {
			requestBuilder = requestBuilder.addHeader("User-Agent", userAgentName);
		}
		
		if (contentTypeHeader != null) {
			requestBuilder = requestBuilder.addHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			requestBuilder = requestBuilder.addHeader("Accept", acceptHeader);
		}
		
		httpClient.newCall(requestBuilder.build()).enqueue(new AsyncRestClientResponseCallback<>(responseType, promise));
		
		return promise;
	}

	/**
	 * @see com.kixeye.relax.RestClient#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public <I> HttpPromise<HttpResponse<Void>> delete(String path, Object... pathVariables) throws IOException {
		HttpPromise<HttpResponse<Void>> promise = new HttpPromise<>();
		
		Request.Builder requestBuilder = new Request.Builder().delete().url(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		if (userAgentName != null) {
			requestBuilder = requestBuilder.addHeader("User-Agent", userAgentName);
		}
		
		httpClient.newCall(requestBuilder.build()).enqueue(new AsyncRestClientResponseCallback<>(null, promise));
		
		return promise;
	}
	
	/**
	 * Sets the user agent name.
	 * 
	 * @param userAgentName
	 */
	public void setUserAgentName(String userAgentName) {
		this.userAgentName = userAgentName;
	}
	
	/**
	 * A response callback that forwards the response to the promise.
	 * 
	 * @author ebahtijaragic
	 */
	private class AsyncRestClientResponseCallback<R> implements Callback {
		private final Class<R> responseType;
		
		private HttpPromise<HttpResponse<R>> promise;
		
		/**
		 * @param responseType
		 * @param HttpPromise<HttpResponse<T>> promise
		 */
		protected AsyncRestClientResponseCallback(Class<R> responseType, HttpPromise<HttpResponse<R>> promise) {
			this.responseType = responseType;
			this.promise = promise;
		}

		public void onFailure(Request request, IOException e) {
			promise.setError(e);
		}

		public void onResponse(Response response) throws IOException {
			try {
				promise.set(new HttpResponse<>(response, serDe, responseType));
			} catch (Exception e) {
				promise.setError(e);
			}
		}
	}
}
