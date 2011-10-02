package de.jbee.spider;

public class CircularDependencyException
		extends SpiderException {

	public String trackedPath;

	public CircularDependencyException( Class<?> clashType ) {
		super( "Found circular dependency for " + clashType );
		trackedPath = clashType.getSimpleName();
	}

	@Override
	public void trace( Class<?> type ) {
		trackedPath = type.getSimpleName() + " -> " + trackedPath;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " : " + trackedPath;
	}

}
