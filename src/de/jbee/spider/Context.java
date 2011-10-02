package de.jbee.spider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import de.jbee.spider.invoke.Invoker;

public class Context {

	public Context( Invoker invoker ) {
		this.invoker = invoker;
	}

	static final class Instances
			extends IdentityHashMap<Class<?>, Map<String, Singleton<?>>> {

		<T> void add( Class<?> type, Singleton<T> instance, String impl ) {
			Map<String, Singleton<?>> impls = get( type );
			if ( impls == null ) {
				impls = new HashMap<String, Singleton<?>>();
			}
			impls.put( impl, instance );
			put( type, impls );
		}

		Singleton<?> get( Class<?> type, String impl ) {
			Map<String, Singleton<?>> impls = get( type );
			return impls == null
				? null
				: impls.get( impl );
		}
	}

	private final Instances instances = new Instances();
	private final Dependencies dependencies = new Dependencies();
	private final Invoker invoker;

	public <T> void register( Class<T> type ) {
		register( type, "" );
	}

	public Dependencies getDependencies() {
		return dependencies;
	}

	public <T> void register( Class<T> type, String impl ) {
		Singleton<T> s = new Singleton<T>( type );
		for ( Class<?> interfaceType : type.getInterfaces() ) {
			instances.add( interfaceType, s, impl );
		}
		instances.add( type, s, impl );
	}

	public <T> T yield( Class<T> type )
			throws SpiderException {
		return yield( type, "" );
	}

	public <T> T yield( Class<T> type, String impl )
			throws SpiderException {
		@SuppressWarnings ( "unchecked" )
		Singleton<T> s = (Singleton<T>) instances.get( type, impl );
		if ( s == null )
			throw new UnknownTypeException( type );
		if ( s.isFailedToInstantiate() )
			throw new FailedDependencyException( type, type );
		return s.getInstance( this );
	}

	public <T> T invoke( Class<T> type )
			throws SpiderException {
		try {
			return invoker.invokeConstuctor( type, this );
		} catch ( InvocationTargetException e ) {
			rethrowSpanException( type, e.getTargetException() );
		} catch ( SpiderException e ) {
			rethrowSpanException( type, e );
		} catch ( Exception e ) {
			rethrowSpanException( type, e );
		}
		return null;
	}

	private void rethrowSpanException( Class<?> type, Throwable e ) {
		if ( e instanceof SpiderException ) {
			throw (SpiderException) e;
		}
		throw new InstantiationException( type, e );
	}
}