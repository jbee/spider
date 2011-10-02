/**
 * 
 */
package de.jbee.spider;

import java.util.IdentityHashMap;

public class Dependencies
		extends IdentityHashMap<Singleton<?>, Integer> {

	Dependencies() {
		super();
	}

	void inc( Singleton<?> key ) {
		Integer count = get( key );
		put( key, count == null
			? 1
			: count + 1 );
	}

	void dec( Singleton<?> key ) {
		Integer count = get( key );
		if ( count != null ) {
			if ( count == 1 ) {
				remove( key );
			} else {
				put( key, count - 1 );
			}
		}
	}

	boolean dependsOn( Singleton<?> key ) {
		return containsKey( key );
	}

	Singleton<?> firstFailedDepencency() {
		for ( Singleton<?> dependency : this.keySet() ) {
			if ( dependency.isFailedToInstantiate() )
				return dependency;
		}
		return null;
	}
}