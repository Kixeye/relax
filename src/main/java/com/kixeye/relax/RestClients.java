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

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import com.squareup.okhttp.OkHttpClient;

/**
 * Builds rest clients.
 * 
 * @author ebahtijaragic
 */
public final class RestClients {
	private RestClients() {}
	
	/**
	 * Creates a new RestClient without a uri prefix.
	 * 
	 * @return
	 */
	public static RestClientBuilder create(RestClientSerDe serDe) {
		return new RestClientBuilder(serDe);
	}
	
	/**
	 * Creates a new RestClient with a uri prefix.
	 * 
	 * @param uriPrefix
	 * @return
	 */
	public static RestClientBuilder create(String uriPrefix, RestClientSerDe serDe) {
		return new RestClientBuilder(uriPrefix, serDe);
	}
	
	/**
	 * A builder of Rest Clients.
	 * 
	 * @author ebahtijaragic
	 */
	public static final class RestClientBuilder {
		private final String uriPrefix;
		private final RestClientSerDe serDe;
		
		private int connectionTimeoutValue = 5;
		private TimeUnit connectionTimeoutTimeUnit = TimeUnit.SECONDS;

		private int readTimeoutValue = 5;
		private TimeUnit readTimeoutTimeUnit = TimeUnit.SECONDS;
		
		private SSLContext sslContext;
		private String userAgentName;

		/**
		 * @param uriPrefix
		 */
		protected RestClientBuilder(String uriPrefix, RestClientSerDe serDe) {
			this.uriPrefix = uriPrefix;
			this.serDe = serDe;
		}
		
		/**
		 * 
		 */
		protected RestClientBuilder(RestClientSerDe serDe) {
			this.uriPrefix = null;
			this.serDe = serDe;
		}
		
		/**
		 * With a ssl context.
		 * 
		 * @param sslContext
		 * @return
		 */
		public RestClientBuilder withSSLContext(SSLContext sslContext) {
			this.sslContext = sslContext;
			
			return this;
		}
		
		/**
		 * Sets the read timeout.
		 * 
		 * @param timeout
		 * @param timeUnit
		 * @return
		 */
		public RestClientBuilder withReadTimeout(int timeout, TimeUnit timeUnit) {
			this.readTimeoutValue = timeout;
			this.readTimeoutTimeUnit = timeUnit;
			
			return this;
		}
		
		/**
		 * Sets the connection timeout.
		 * 
		 * @param timeout
		 * @param timeUnit
		 * @return
		 */
		public RestClientBuilder withConnectionTimeout(int timeout, TimeUnit timeUnit) {
			this.connectionTimeoutValue = timeout;
			this.connectionTimeoutTimeUnit = timeUnit;
			
			return this;
		}
		
		/**
		 * Builds the RestClient.
		 * 
		 * @return
		 */
		public RestClient build() {
			AsyncRestClient client = new AsyncRestClient();
			client.setUriPrefix(uriPrefix);
			
			OkHttpClient httpClient = new OkHttpClient();
			httpClient.setReadTimeout(readTimeoutValue, readTimeoutTimeUnit);
			httpClient.setConnectTimeout(connectionTimeoutValue, connectionTimeoutTimeUnit);
			
			if (sslContext != null) {
				httpClient.setSslSocketFactory(sslContext.getSocketFactory());
			}
			
			client.setHttpClient(httpClient);
			client.setSerDe(serDe);
			client.setUserAgentName(userAgentName);
			
			return client;
		}
	}
}
