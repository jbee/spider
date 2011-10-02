package de.jbee.spider.invoke;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import de.jbee.spider.Context;
import de.jbee.spider.Impl;

public class DefaultInvoker
		implements Invoker {

	private static Invoker instance;

	private DefaultInvoker() {
		// hide
	}

	public static Invoker getInstance() {
		if ( instance == null ) {
			instance = new DefaultInvoker();
		}
		return instance;
	}

	@Override
	public <T> T invokeConstuctor( Class<T> type, Context context )
			throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		@SuppressWarnings ( "unchecked" )
		Constructor<T> c = (Constructor<T>) type.getConstructors()[0];
		Annotation[][] parameterAnnotations = c.getParameterAnnotations();
		Class<?>[] parameterTypes = c.getParameterTypes();
		if ( parameterTypes.length == 0 )
			return type.newInstance();
		Object[] arguments = new Object[parameterTypes.length];
		int i = 0;
		for ( Class<?> paramType : parameterTypes ) {
			Impl impl = implAnnotation( parameterAnnotations[i] );
			arguments[i++] = impl == null
				? context.yield( paramType )
				: context.yield( paramType, impl.value() );
		}
		return c.newInstance( arguments );
	}

	@Override
	public boolean isConstructable( Class<?> type ) {
		for ( Constructor<?> c : type.getConstructors() ) {
			if ( Modifier.isPublic( c.getModifiers() ) )
				return true;
		}
		return false;
	}

	private Impl implAnnotation( Annotation[] annotations ) {
		if ( annotations == null || annotations.length == 0 )
			return null;
		for ( Annotation a : annotations ) {
			if ( a.annotationType().equals( Impl.class ) )
				return (Impl) a;
		}
		return null;
	}

}
