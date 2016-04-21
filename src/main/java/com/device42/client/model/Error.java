package com.device42.client.model;

/**
 * Representation of the error that is coming from the Device42 instance.
 */
public class Error {
	/**
	 * The error message
	 */
	private final String message;
	/**
	 * The error code
	 */
	private final int code;

	/**
	 * Create the new error with specific message and code
	 * 
	 * @param message
	 *            The error message
	 * @param code
	 *            The error code
	 */
	public Error(String message, int code) {
		this.message = message;
		this.code = code;
	}

	/**
	 * Get the error code
	 * 
	 * @return The error code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Get the error message
	 * 
	 * @return The error message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Error [message=" + message + ", code=" + code + "]";
	}
}
