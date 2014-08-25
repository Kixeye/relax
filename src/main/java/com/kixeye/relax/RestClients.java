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

import javax.net.ssl.SSLContext;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;

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
		
		private ConnectionReuseStrategy connectionReuseStrategy;
		private RequestConfig requestConfig;
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
		 * With a request config.
		 * 
		 * @param requestConfig
		 * @return
		 */
		public RestClientBuilder withConnectionReuseStrategy(ConnectionReuseStrategy connectionReuseStrategy) {
			this.connectionReuseStrategy = connectionReuseStrategy;
			
			return this;
		}
		
		/**
		 * With a request config.
		 * 
		 * @param requestConfig
		 * @return
		 */
		public RestClientBuilder withRequestConfig(RequestConfig requestConfig) {
			this.requestConfig = requestConfig;
			
			return this;
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
		 * With a user agent name.
		 * 
		 * @param userAgentName
		 * @return
		 */
		public RestClientBuilder withUserAgentName(String userAgentName) {
			this.userAgentName = userAgentName;
			
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
			
			HttpAsyncClientBuilder builder = HttpAsyncClients.custom();
			if (requestConfig != null) {
				builder.setDefaultRequestConfig(requestConfig);
			}
			if (userAgentName != null) {
				builder.setUserAgent(userAgentName);
			}
			if (sslContext != null) {
				builder.setSSLContext(sslContext);
			}
			if (connectionReuseStrategy != null) {
				builder.setConnectionReuseStrategy(connectionReuseStrategy);
			}
			
			CloseableHttpAsyncClient httpClient = builder.build();
			httpClient.start();
			
			client.setHttpClient(httpClient, false);
			client.setSerDe(serDe);
			
			return client;
		}
	}
}
