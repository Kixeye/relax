package com.kixeye.relax;

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
