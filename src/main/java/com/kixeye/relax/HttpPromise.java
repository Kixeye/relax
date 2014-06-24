package com.kixeye.relax;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a promise.
 * 
 * @author ebahtijaragic
 *
 * @param <T>
 */
public class HttpPromise<T> {
	private static final Logger logger = LoggerFactory.getLogger(HttpPromise.class);
	
	private AtomicReference<Value> value = new AtomicReference<>(null);
	
	private AtomicBoolean isComplete = new AtomicBoolean(false);
	
	private ConcurrentLinkedQueue<HttpPromiseListener<T>> listeners = new ConcurrentLinkedQueue<>();
	
	private ExecutorService executor = null;
	
	private class Value {
		private T value;
		private Exception exception;
		
		/**
		 * @param value
		 */
		public Value(T value) {
			this.value = value;
		}

		/**
		 * @param exception
		 */
		public Value(T value, Exception exception) {
			this.value = value;
			this.exception = exception;
		}
	}
	
	/**
	 * 
	 */
	public HttpPromise() {
	}
	
	/**
	 * @param executor
	 */
	public HttpPromise(ExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 */
	public void addListener(HttpPromiseListener<T> listener) {
		listeners.add(listener);

		if (value.get() != null) {
			processListeners();
		}
	}
	
	/**
	 * Gets the current value.
	 * 
	 * @return
	 * @throws Throwable 
	 */
	public T get() throws Exception {
		Value currentValue = value.get();
		
		if (currentValue == null) {
			return null;
		} else {
			if (currentValue.exception != null) {
				throw currentValue.exception;
			}
			
			return currentValue.value;
		}
	}
	
	/**
	 * Waits until this promise completes.
	 * 
	 * @param time
	 * @param timeUnits
	 * @return
	 * @throws InterruptedException 
	 */
	public HttpPromise<T> waitForComplete(long time, TimeUnit timeUnits) throws InterruptedException {
		long timeMillis = timeUnits.toMillis(time);
		long startTime = System.currentTimeMillis();

		synchronized (value) {
			while ((System.currentTimeMillis() - startTime) < timeMillis && !isComplete()) {
				value.wait(timeMillis - (System.currentTimeMillis() - startTime));
			}
		}
		
		return this;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value
	 */
	public HttpPromise<T> set(T value) {
		if (this.value.compareAndSet(null, new Value(value))) {
			isComplete.set(true);
			
			synchronized (this.value) {
				this.value.notifyAll();
			}
			
			processListeners();
		}
		
		return this;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value
	 */
	public HttpPromise<T> setError(Exception error) {
		if (this.value.compareAndSet(null, new Value(null, error))) {
			isComplete.set(true);
			
			this.value.notify();
			
			processListeners();
		}
		
		return this;
	}
	
	/**
	 * Returns true if this promise is complete.
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return isComplete.get();
	}
	
	/**
	 * Processes the listeners.
	 */
	private void processListeners() {
		while (!listeners.isEmpty()){ 
			try {
				HttpPromiseListener<T> listener = listeners.poll();
				
				if (executor != null) {
					executor.submit(new HttpPromiseTask<T>(listener, this));
				} else {
					listener.handle(this);
				}
			} catch (Exception e) {
				logger.error("Unexpected error processing listener", e);
			}
		}
	}
	
	/**
	 * A listener for promise events.
	 * 
	 * @author ebahtijaragic
	 *
	 * @param <T>
	 */
	public static interface HttpPromiseListener<T> {
		/**
		 * Handles a promise update.
		 * 
		 * @param promise
		 */
		public void handle(HttpPromise<T> promise);
	}
	
	/**
	 * A task that executes the listener.
	 * 
	 * @author ebahtijaragic
	 *
	 * @param <T>
	 */
	public static class HttpPromiseTask<T> implements Runnable {
		private final HttpPromiseListener<T> listener;
		private final HttpPromise<T> promise;
		
		/**
		 * @param listener
		 * @param promise
		 */
		public HttpPromiseTask(HttpPromiseListener<T> listener,
				HttpPromise<T> promise) {
			this.listener = listener;
			this.promise = promise;
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			listener.handle(promise);
		}
	}
}
