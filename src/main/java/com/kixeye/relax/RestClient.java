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

import java.io.Closeable;
import java.io.IOException;

/**
 * A REST Client.
 * 
 * @author ebahtijaragic
 */
public interface RestClient extends Closeable {
	/**
	 * Performs an HTTP get.
	 * 
	 * @param path
	 * @param responseType
	 * @param pathVariables
	 * @return
	 * @throws IOException
	 */
	public abstract <O> HttpPromise<HttpResponse<O>> get(String path,
			Class<O> responseType, Object... pathVariables) throws IOException;

	/**
	 * Performs a HTTP post.
	 * 
	 * @param path
	 * @param contentTypeHeader
	 * @param acceptHeader
	 * @param requestObject
	 * @param responseType
	 * @param pathVariables
	 * @return
	 * @throws IOException
	 */
	public abstract <I, O> HttpPromise<HttpResponse<O>> post(String path,
			String contentTypeHeader, String acceptHeader, I requestObject,
			Class<O> responseType, Object... pathVariables) throws IOException;

	/**
	 * Performs a HTTP put.
	 * 
	 * @param path
	 * @param contentTypeHeader
	 * @param acceptHeader
	 * @param requestObject
	 * @param pathVariables
	 * @return
	 * @throws IOException
	 */
	public abstract <I> HttpPromise<HttpResponse<Void>> put(String path,
			String contentTypeHeader, String acceptHeader, I requestObject,
			Object... pathVariables) throws IOException;

	/**
	 * Performs a HTTP patch.
	 * 
	 * @param path
	 * @param contentTypeHeader
	 * @param acceptHeader
	 * @param requestObject
	 * @param pathVariables
	 * @return
	 * @throws IOException
	 */
	public abstract <I> HttpPromise<HttpResponse<Void>> patch(String path,
			String contentTypeHeader, String acceptHeader, I requestObject,
			Object... pathVariables) throws IOException;

	/**
	 * Performs a HTTP delete.
	 * 
	 * @param path
	 * @param pathVariables
	 * @return
	 * @throws IOException
	 */
	public abstract <I> HttpPromise<HttpResponse<Void>> delete(String path,
			Object... pathVariables) throws IOException;

}