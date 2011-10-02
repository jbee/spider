package de.jbee.spider.invoke;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import de.jbee.spider.Context;

public class CompositeInvoker
		implements Invoker {

	private final LinkedList<Invoker> invokerSequence = new LinkedList<Invoker>();

	public void append( Invoker invoker ) {
		invokerSequence.add( invoker );
	}

	@Override
	public <T> T invokeConstuctor( Class<T> type, Context context )
			throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		for ( Invoker i : invokerSequence ) {
			if ( i.isConstructable( type ) )
				return i.invokeConstuctor( type, context );
		}
		throw new InstantiationException( "No invoker is able to construct a " + type );
	}

	@Override
	public boolean isConstructable( Class<?> type ) {
		for ( Invoker i : invokerSequence ) {
			if ( i.isConstructable( type ) )
				return true;
		}
		return false;
	}
}
