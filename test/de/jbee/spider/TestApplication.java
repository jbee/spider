package de.jbee.spider;

import de.jbee.spider.invoke.ContextAsParamInvoker;
import de.jbee.spider.invoke.DefaultInvoker;
import de.jbee.spider.invoke.CompositeInvoker;
import de.jbee.spider.test.A;
import de.jbee.spider.test.B;
import de.jbee.spider.test.C;
import de.jbee.spider.test.D;
import de.jbee.spider.test.E;
import de.jbee.spider.test.F;

public class TestApplication {

	public static void main( String[] args ) {

		CompositeInvoker invoker = new CompositeInvoker();
		invoker.append( ContextAsParamInvoker.getInstance() );
		invoker.append( DefaultInvoker.getInstance() );
		Context context = new Context( invoker );

		context.register( A.class );
		context.register( B.class );
		context.register( C.class );
		context.register( D.class );
		context.register( E.class );
		context.register( F.class );

		E e = context.yield( E.class );
		A a = context.yield( A.class );
		System.out.println( a );
		A a2 = context.yield( A.class );
		F f = context.yield( F.class );
	}
}
