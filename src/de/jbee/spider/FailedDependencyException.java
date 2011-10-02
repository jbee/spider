package de.jbee.spider;

public class FailedDependencyException
		extends SpiderException {

	public FailedDependencyException( Class<?> clashType, Class<?> failedDependency ) {
		super( "Failed to instantiate " + clashType + " due failed dependency " + failedDependency );
	}

}
