package de.jbee.spider;

public class SpiderException
		extends RuntimeException {

	public SpiderException( Class<?> type, Exception cause ) {
		super( "Failed to create " + type.getCanonicalName(), cause );
	}

	public SpiderException( String message, Throwable cause ) {
		super( message, cause );
	}

	public SpiderException( String message ) {
		super( message );
	}

	public void trace( Class<?> type ) {
		// override for special use
	}
}
