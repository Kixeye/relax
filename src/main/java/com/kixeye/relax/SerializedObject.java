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

/**
 * Represents a serialized object.
 * 
 * @author ebahtijaragic
 */
public class SerializedObject<O> {
	private final RestClientSerDe serDe;
	private final String contentType;
	private final byte[] data;
	private final Class<O> objectType;
	
	/**
	 * @param serDe
	 * @param contentType
	 * @param data
	 * @param objectType
	 */
	protected SerializedObject(RestClientSerDe serDe, String contentType, byte[] data, Class<O> objectType) {
		this.serDe = serDe;
		this.contentType = contentType;
		this.data = data;
		this.objectType = objectType;
	}

	/**
	 * Deserializes the serialized object.
	 * 
	 * @return
	 * @throws IOException
	 */
	public O deserialize() throws IOException {
		return serDe.deserialize(contentType, data, 0, data.length, objectType);
	}
}
