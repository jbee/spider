package de.jbee.spider.invoke;

import java.lang.reflect.InvocationTargetException;

import de.jbee.spider.Context;

public interface Invoker {

	public <T> T invokeConstuctor( Class<T> type, Context context )
			throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException;

	public boolean isConstructable( Class<?> type );
}
