package de.jbee.spider.invoke;

import java.lang.reflect.InvocationTargetException;

import de.jbee.spider.Context;

public class ContextAsParamInvoker
		implements Invoker {

	private static Invoker instance;

	private ContextAsParamInvoker() {
		// hide
	}

	public static Invoker getInstance() {
		if ( instance == null ) {
			instance = new ContextAsParamInvoker();
		}
		return instance;
	}

	@Override
	public <T> T invokeConstuctor( Class<T> type, Context context )
			throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		return type.getConstructor( Context.class ).newInstance( context );
	}

	@Override
	public boolean isConstructable( Class<?> type ) {
		try {
			type.getConstructor( Context.class );
			return true;
		} catch ( Exception e ) {
			return false;
		}
	}

}
