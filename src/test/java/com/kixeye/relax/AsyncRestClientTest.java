package com.kixeye.relax;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests the {@link AsyncRestClient}
 * 
 * @author ebahtijaragic
 */
public class AsyncRestClientTest {
	private static final Logger logger = LoggerFactory.getLogger(AsyncRestClientTest.class);
	
	private Connection connection = null;
	private int port = -1;
	private Container testContainer = new Container() {
		@Override
		public void handle(Request request, Response response) {
			ObjectMapper mapper = new ObjectMapper();
			
			switch (request.getMethod()) {
				case "POST":
					try {
						TestObject requestObject = mapper.readValue(request.getInputStream(), TestObject.class);
						
						response.getByteChannel().write(ByteBuffer.wrap(new ObjectMapper().writeValueAsBytes(
								new TestObject(requestObject.testString, requestObject.testInt))));
					} catch (Exception e) {
						logger.error("Unexpected exception", e);
					}
				case "GET":
					try {
						response.getByteChannel().write(ByteBuffer.wrap(new ObjectMapper().writeValueAsBytes(
								new TestObject("testString", port))));
					} catch (Exception e) {
						logger.error("Unexpected exception", e);
					}
			}
			
			try {
				response.close();
			} catch (IOException e) {
				logger.error("Unexpected exception", e);
			}
		}
	};
	
	@Before
	public void setUp() throws Exception {
		Server server = new ContainerServer(testContainer);
		connection = new SocketConnection(server);
		
		ServerSocket socketServer = new ServerSocket(0);
		port = socketServer.getLocalPort();
		socketServer.close();
		
		connection.connect(new InetSocketAddress(port));
	}
	
	@After
	public void tearDown() throws Exception {
		connection.close();
	}
	
	@Test
	public void testGet() throws Exception {
		RestClientSerDe serDe = new RestClientSerDe() {
			@Override
			public byte[] serialize(String mimeType, Object obj) throws IOException {
				return new ObjectMapper().writeValueAsBytes(obj);
			}
			
			@Override
			public <T> T deserialize(String mimeType, byte[] data, int offset,
					int length, Class<T> clazz) throws IOException {
				return new ObjectMapper().readValue(data, offset, length, clazz);
			}
		};
		
		try (AsyncRestClient client = new AsyncRestClient(5000, 5000, serDe)) {
			TestObject testObject = client.get("http://localhost:" + port + "/test", TestObject.class).waitForComplete(8, TimeUnit.SECONDS).get().deserialize();
			
			Assert.assertEquals("testString", testObject.getTestString());
			Assert.assertEquals(port, testObject.getTestInt());
		}
	}
	
	@Test
	public void testPost() throws Exception {
		RestClientSerDe serDe = new RestClientSerDe() {
			@Override
			public byte[] serialize(String mimeType, Object obj) throws IOException {
				return new ObjectMapper().writeValueAsBytes(obj);
			}
			
			@Override
			public <T> T deserialize(String mimeType, byte[] data, int offset,
					int length, Class<T> clazz) throws IOException {
				return new ObjectMapper().readValue(data, offset, length, clazz);
			}
		};
		
		try (AsyncRestClient client = new AsyncRestClient(5000, 5000, serDe)) {
			TestObject testObject = client
					.post("http://localhost:" + port + "/test", null, null, new TestObject("testString", port), TestObject.class)
					.waitForComplete(8, TimeUnit.SECONDS).get().deserialize();
			
			Assert.assertEquals("testString", testObject.getTestString());
			Assert.assertEquals(port, testObject.getTestInt());
		}
	}
	
	@Test
	public void testPut() throws Exception {
		RestClientSerDe serDe = new RestClientSerDe() {
			@Override
			public byte[] serialize(String mimeType, Object obj) throws IOException {
				return new ObjectMapper().writeValueAsBytes(obj);
			}
			
			@Override
			public <T> T deserialize(String mimeType, byte[] data, int offset,
					int length, Class<T> clazz) throws IOException {
				return new ObjectMapper().readValue(data, offset, length, clazz);
			}
		};
		
		try (AsyncRestClient client = new AsyncRestClient(5000, 5000, serDe)) {
			boolean isComplete = client.put("http://localhost:" + port + "/test", null, null, new TestObject("testString", port))
					.waitForComplete(8, TimeUnit.SECONDS).isComplete();
			
			Assert.assertTrue(isComplete);
		}
	}
	
	public static class TestObject {
		private String testString;
		private int testInt;
		
		/**
		 * 
		 */
		public TestObject() {
		}

		/**
		 * @param testString
		 * @param testInt
		 */
		public TestObject(String testString, int testInt) {
			this.testString = testString;
			this.testInt = testInt;
		}

		/**
		 * @return the testString
		 */
		public String getTestString() {
			return testString;
		}

		/**
		 * @param testString the testString to set
		 */
		public void setTestString(String testString) {
			this.testString = testString;
		}

		/**
		 * @return the testInt
		 */
		public int getTestInt() {
			return testInt;
		}

		/**
		 * @param testInt the testInt to set
		 */
		public void setTestInt(int testInt) {
			this.testInt = testInt;
		}
	}
}
