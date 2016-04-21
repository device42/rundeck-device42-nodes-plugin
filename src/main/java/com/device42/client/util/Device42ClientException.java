package com.device42.client.util;

/**
 * The exception used to indicate problems with REST API queries
 */
public class Device42ClientException extends RuntimeException {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -3104582443334015826L;

	/**
	 * Create empty exception
	 */
	public Device42ClientException() {
		super();
	}

	/**
	 * Create exception with message
	 * 
	 * @param message
	 *            The message to transfer
	 */
	public Device42ClientException(String message) {
		super(message);
	}

	/**
	 * Create the exception with message and nested exception
	 * 
	 * @param message
	 *            The message to transfer
	 * @param cause
	 *            Nested exception
	 */
	public Device42ClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
