package de.jbee.spider;

public class InstantiationException
		extends SpiderException {

	public InstantiationException( Class<?> type, Throwable cause ) {
		super( "Failed to instantiate of type " + type, cause );
	}

}
