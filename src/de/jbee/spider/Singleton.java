/**
 * 
 */
package de.jbee.spider;

final class Singleton<T> {

	private final Class<T> type;

	private T instance;
	private boolean failedToInstantiate = false;

	Singleton( Class<T> type ) {
		this.type = type;
	}

	T getInstance( Context context ) {
		if ( isInstantiationUntried() ) {
			instantiateWith( context );
		}
		return instance;
	}

	private void instantiateWith( Context context ) {
		Dependencies dependencies = context.getDependencies();
		ensureNoCircularDependency( dependencies );
		ensureNoFailedDependency( dependencies );
		dependencies.inc( this );
		try {
			instance = context.invoke( type );
			System.out.println( "ok \t" + type );
		} catch ( SpiderException e ) {
			failedToInstantiate = true;
			e.trace( type );
			throw e;
		} finally {
			dependencies.dec( this );
		}
	}

	private boolean isInstantiationUntried() {
		return !isInstantiated() && !isFailedToInstantiate();
	}

	private void ensureNoCircularDependency( Dependencies dependencies ) {
		if ( dependencies.dependsOn( this ) ) {
			failedToInstantiate = true;
			throw new CircularDependencyException( type );
		}
	}

	private void ensureNoFailedDependency( Dependencies dependencies ) {
		if ( !dependencies.isEmpty() ) {
			Singleton<?> failedDependency = dependencies.firstFailedDepencency();
			if ( failedDependency != null ) {
				failedToInstantiate = true;
				throw new FailedDependencyException( type, failedDependency.type );
			}
		}
	}

	public boolean isFailedToInstantiate() {
		return failedToInstantiate;
	}

	public boolean isInstantiated() {
		return instance != null;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof Singleton<?> ) {
			return ( (Singleton<?>) obj ).type == type;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {
		if ( instance != null ) {
			return type.toString();
		}
		return failedToInstantiate
			? "(" + type + ")"
			: "[" + type + "]";
	}
}