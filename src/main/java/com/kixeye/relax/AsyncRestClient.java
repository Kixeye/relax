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
import java.util.concurrent.CancellationException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;

import com.kixeye.relax.util.UrlUtils;

/**
 * A REST Async HTTP Client.
 * 
 * @author ebahtijaragic
 */
public class AsyncRestClient implements RestClient {
	private boolean isHttpClientReused = false;
	
	private CloseableHttpAsyncClient httpClient;

	private RestClientSerDe serDe;
	
	private String uriPrefix = "";
	
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
	protected void setHttpClient(CloseableHttpAsyncClient httpClient, boolean isHttpClientReused) {
		this.httpClient = httpClient;
		this.isHttpClientReused = isHttpClientReused;
	}
	
	/**
	 * Clsoes the client.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public void close() throws IOException {
		if (!isHttpClientReused) {
			httpClient.close();
		}
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#get(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <O> HttpPromise<SerializedObject<O>> get(final String path, final Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<SerializedObject<O>> promise = new HttpPromise<>();
		SerializedObjectHttpPromiseDataSetter<O> dataSetter = new SerializedObjectHttpPromiseDataSetter<>();

		HttpGet request = new HttpGet(UrlUtils.expand(uriPrefix + path, pathVariables));

		httpClient.execute(request, new AsyncRestClientResponseCallback<>(promise, responseType, dataSetter));
		
		return promise;
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#post(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <I, O> HttpPromise<SerializedObject<O>> post(String path, String contentTypeHeader, String acceptHeader, I requestObject, final Class<O> responseType, Object... pathVariables) throws IOException {
		HttpPromise<SerializedObject<O>> promise = new HttpPromise<>();
		SerializedObjectHttpPromiseDataSetter<O> dataSetter = new SerializedObjectHttpPromiseDataSetter<>();
		
		HttpPost request = new HttpPost(UrlUtils.expand(uriPrefix + path, pathVariables));
		if (requestObject != null) {
			request.setEntity(new ByteArrayEntity(serDe.serialize(contentTypeHeader, requestObject)));
		}
		
		if (contentTypeHeader != null) {
			request.setHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			request.setHeader("Accept", acceptHeader);
		}

		httpClient.execute(request, new AsyncRestClientResponseCallback<>(promise, responseType, dataSetter));
		
		return promise;
	}
	
	/**
	 * @see com.kixeye.relax.RestClient#put(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Object)
	 */
	@Override
	public <I> HttpPromise<Void> put(String path, String contentTypeHeader, String acceptHeader, I requestObject, Object... pathVariables) throws IOException {
		HttpPromise<Void> promise = new HttpPromise<>();
		VoidHttpPromiseDataSetter dataSetter = new VoidHttpPromiseDataSetter();
		
		HttpPost request = new HttpPost(UrlUtils.expand(uriPrefix + path, pathVariables));
		if (requestObject != null) {
			request.setEntity(new ByteArrayEntity(serDe.serialize(contentTypeHeader, requestObject)));
		}
		
		if (contentTypeHeader != null) {
			request.setHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			request.setHeader("Accept", acceptHeader);
		}

		httpClient.execute(request, new AsyncRestClientResponseCallback<>(promise, null, dataSetter));
		
		return promise;
	}

	/**
	 * @see com.kixeye.relax.RestClient#patch(java.lang.String, java.lang.String, java.lang.String, I, java.lang.Object)
	 */
	@Override
	public <I> HttpPromise<Void> patch(String path, String contentTypeHeader, String acceptHeader, I requestObject, Object... pathVariables) throws IOException {
		HttpPromise<Void> promise = new HttpPromise<>();
		VoidHttpPromiseDataSetter dataSetter = new VoidHttpPromiseDataSetter();
		
		HttpPatch request = new HttpPatch(UrlUtils.expand(uriPrefix + path, pathVariables));
		if (requestObject != null) {
			request.setEntity(new ByteArrayEntity(serDe.serialize(contentTypeHeader, requestObject)));
		}
		
		if (contentTypeHeader != null) {
			request.setHeader("Content-Type", contentTypeHeader);
		}
		
		if (acceptHeader != null) {
			request.setHeader("Accept", acceptHeader);
		}

		httpClient.execute(request, new AsyncRestClientResponseCallback<>(promise, null, dataSetter));
		
		return promise;
	}

	/**
	 * @see com.kixeye.relax.RestClient#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public <I> HttpPromise<Void> delete(String path, Object... pathVariables) throws IOException {
		HttpPromise<Void> promise = new HttpPromise<>();
		VoidHttpPromiseDataSetter dataSetter = new VoidHttpPromiseDataSetter();
		
		HttpDelete request = new HttpDelete(UrlUtils.expand(uriPrefix + path, pathVariables));
		
		httpClient.execute(request, new AsyncRestClientResponseCallback<>(promise, null, dataSetter));
		
		return promise;
	}
	
	/**
	 * Returns true if we're active.
	 * 
	 * @return
	 */
	public boolean isActive() {
		return httpClient.isRunning();
	}
	
	/**
	 * A response callback that forwards the response to the promise.
	 * 
	 * @author ebahtijaragic
	 *
	 * @param <T>
	 */
	private class AsyncRestClientResponseCallback<T, R> implements FutureCallback<HttpResponse> {
		private final HttpPromise<T> promise;
		private final Class<R> responseType;
		
		private HttpPromiseDataSetter<T, R> dataSetter;
		
		/**
		 * @param promise
		 * @param responseType
		 */
		protected AsyncRestClientResponseCallback(HttpPromise<T> promise, Class<R> responseType, HttpPromiseDataSetter<T, R> dataSetter) {
			this.promise = promise;
			this.responseType = responseType;
			this.dataSetter = dataSetter;
		}

		public void failed(Exception ex) {
			promise.setError(ex);
		}
		
		public void completed(HttpResponse result) {
			try {
				HttpEntity entity = result.getEntity();
				byte[] data = null;
				
				if (entity != null) {
					data = EntityUtils.toByteArray(entity);
					EntityUtils.consumeQuietly(entity);
				}
				
				Header contentTypeHeader = result.getFirstHeader("Content-Type");
				
				dataSetter.setData(promise, contentTypeHeader != null ? contentTypeHeader.getValue() : null, data, responseType);
			} catch (Exception e) {
				failed(e);
			}
		}
		
		public void cancelled() {
			promise.setError(new CancellationException());
		}
	}
	
	/**
	 * Sets promise as null.
	 * 
	 * @author ebahtijaragic
	 *
	 */
	private class VoidHttpPromiseDataSetter implements HttpPromiseDataSetter<Void, Void> {
		public void setData(HttpPromise<Void> promise, String mimeType, byte[] data, Class<Void> responseType) {
			promise.set(null);
		}
	}
	
	/**
	 * Sets promise data for a serialized object http promise.
	 * 
	 * @author ebahtijaragic
	 *
	 * @param <T>
	 */
	private class SerializedObjectHttpPromiseDataSetter<T> implements HttpPromiseDataSetter<SerializedObject<T>, T> {
		public void setData(HttpPromise<SerializedObject<T>> promise, String mimeType, byte[] data, Class<T> responseType) {
			promise.set(new SerializedObject<>(serDe, mimeType, data, responseType));
		}
	}
	
	/**
	 * Sets promise data.
	 * 
	 * @author ebahtijaragic
	 *
	 * @param <T>
	 * @param <R>
	 */
	private interface HttpPromiseDataSetter<T, R> {
		public void setData(HttpPromise<T> promise, String mimeType, byte[] data, Class<R> responseType);
	}
}
