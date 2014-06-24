package com.kixeye.relax;

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
