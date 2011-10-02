package de.jbee.spider;

public class UnknownTypeException
		extends SpiderException {

	public UnknownTypeException( Class<?> type ) {
		super( "No implementation for " + type + " has been registered." );
	}

}
