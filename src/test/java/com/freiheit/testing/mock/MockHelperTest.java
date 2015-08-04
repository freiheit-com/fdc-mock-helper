package com.freiheit.testing.mock;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.freiheit.testing.mock.MockHelper;

/**
 * Tests for {@link MockHelper}.
 * 
 * @author Michael Bohn (initial creation)
 */
public class MockHelperTest {
    /**
     * see test name.
     */
    @Test
    public void shouldCreateInstanceWithBiggestConstructor() throws Exception {
        //given
        final Class<TestClass> classOfInstance = TestClass.class;
        //when
        final TestClass instance = MockHelper.newInstanceMockDependencies( classOfInstance );
        //then
        assertThat( instance._constructedVia, is( "oneArgConstructor" ) );
    }

    /**
     * see test name.
     */
    @Test
    public void shouldUseSuppliedInstanceForDependencyInsteadOfMockingIt() throws Exception {
        //given
        final Class<TestClass> classOfInstance = TestClass.class;
        final DependencyClassA depClassA = new DependencyClassA();
        //when
        final TestClass instance = MockHelper.newInstanceMockDependencies( classOfInstance, depClassA );
        //then
        assertThat( instance._depClassA, is( depClassA ) );
    }

    /**
     * see test name.
     */
    @Test( expected = IllegalArgumentException.class )
    public void shouldThrowExceptionIfNoConstructorAccessible() throws Exception {
        //when
        MockHelper.newInstanceMockDependencies( AInterface.class );
        //then
        //throws Exception!
    }

    /**
     * This can happen, if the test class is refactored and the argument
     * removed. Instead of silently ignoring the argument, the method throws an
     * exception.
     */
    @Test( expected = IllegalArgumentException.class )
    public void shouldThrowExceptionIfASuppliedArgumentWasNotUsed() throws Exception {
        //given
        final Class<TestClass> classOfInstance = TestClass.class;
        final DependencyClassA depClassA = new DependencyClassA();
        //when
        MockHelper.newInstanceMockDependencies( classOfInstance, depClassA, "ImNotUsed" );
        //then
        //throws Exception!
    }

    //Test classes

    private class TestClass {

        public final String _constructedVia;
        public final DependencyClassA _depClassA;

        public TestClass() {
            _constructedVia = "zeroArgConstructor";
            _depClassA = null;
        }

        public TestClass( final DependencyClassA depA ) {
            _constructedVia = "oneArgConstructor";
            _depClassA = depA;
        }
    }

    private class DependencyClassA {
    }

    private interface AInterface {
    }
}
