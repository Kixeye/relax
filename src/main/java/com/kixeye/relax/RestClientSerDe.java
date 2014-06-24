package com.kixeye.relax;

/*
 * #%L
 * KIXMPP Parent
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
 * A Serializer/Deserializer.
 * 
 * @author ebahtijaragic
 */
public interface RestClientSerDe {
	/**
	 * Serializes an object into a byte array.
	 * 
	 * @param mimeType
	 * @param obj
	 * @return serialized object
	 * @throws IOException
	 */
	public byte[] serialize(String mimeType, Object obj) throws IOException;
	
	/**
	 * Deserializes data into an object.
	 * 
	 * @param mimeType
	 * @param data
	 * @param offset
	 * @param length
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T deserialize(String mimeType, byte[] data, int offset, int length, Class<T> clazz) throws IOException;
}
