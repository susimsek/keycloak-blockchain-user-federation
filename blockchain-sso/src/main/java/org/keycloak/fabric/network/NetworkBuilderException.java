package org.keycloak.fabric.network;

public class NetworkBuilderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3303490998239593821L;

	public NetworkBuilderException() {
		super();
	}

	public NetworkBuilderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NetworkBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkBuilderException(String message) {
		super(message);
	}

	public NetworkBuilderException(Throwable cause) {
		super(cause);
	}

}
