package de.jbee.spider;

import junit.framework.TestCase;
import de.jbee.spider.invoke.ContextAsParamInvoker;
import de.jbee.spider.invoke.DefaultInvoker;
import de.jbee.spider.invoke.CompositeInvoker;
import de.jbee.spider.test.A;
import de.jbee.spider.test.B;
import de.jbee.spider.test.C;
import de.jbee.spider.test.D;
import de.jbee.spider.test.E;
import de.jbee.spider.test.Missing;

public class TestContext
		extends TestCase {

	private final static CompositeInvoker INVOKER = new CompositeInvoker();

	static {
		INVOKER.append( ContextAsParamInvoker.getInstance() );
		INVOKER.append( DefaultInvoker.getInstance() );
	}

	private Context context;

	@Override
	protected void setUp()
			throws Exception {
		super.setUp();
		context = new Context( INVOKER );
	}

	public void testRegisterClassOfT() {
		fail( "Not yet implemented" );
	}

	public void testRegisterClassOfTString() {
		fail( "Not yet implemented" );
	}

	public void testYieldClassOfT() {
		fail( "Not yet implemented" );
	}

	public void testYieldClassOfTString() {
		fail( "Not yet implemented" );
	}

	public void testYieldKnownTypeWithoutDependencies() {
		registerTypesToContext( A.class, B.class );

		assertTypeCanBeYieldWithoutAddingDependencies( A.class, B.class );
	}

	public void testYieldUnknownType() {
		registerTypesToContext( D.class, E.class );

		assertYieldOfTypeThrowsException( UnknownTypeException.class, A.class, B.class, C.class );
		assertNoDependencies();
	}

	public void testYieldTypeWithFailedDependency() {

		registerTypesToContext( C.class, D.class );

		assertYieldOfTypeThrowsException( UnknownTypeException.class, Missing.class, C.class );
		assertYieldOfTypeThrowsException( FailedDependencyException.class, D.class );
	}

	private <T extends SpiderException> void assertYieldOfTypeThrowsException(
			Class<T> exceptionType, Class<?>... types ) {
		for ( Class<?> type : types ) {
			try {
				context.yield( type );
				fail( "Missing expected exception for yield of type: " + type );
			} catch ( Exception e ) {
				if ( e.getClass() != exceptionType ) {
					assertSame( "Yield of type: " + type, exceptionType, e.getClass() );
				}
			}
		}
	}

	private void registerTypesToContext( Class<?>... types ) {
		for ( Class<?> type : types ) {
			context.register( type );
		}
	}

	private void assertTypeCanBeYieldWithoutAddingDependencies( Class<?>... types ) {
		for ( Class<?> type : types ) {
			assertNotNull( context.yield( type ) );
		}
		assertNoDependencies();
	}

	private void assertNoDependencies() {
		assertTrue( "Context has dependencies", context.getDependencies().isEmpty() );
	}

}
