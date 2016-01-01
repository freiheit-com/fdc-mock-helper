package com.freiheit.testing.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.mockito.Mockito;

/**
 * A collection of helper methods for mocking (classes).
 * 
 * @author Michael Bohn (michael.bohn@freiheit.com) (initial creation)
 */
public class MockHelper {

    private MockHelper() {
        //instance creation is futile
    }

    /**
     * Creates a new instance of the supplied class, using a public constructor.
     * All constructor dependencies are mocked, if there is no object supplied
     * in the vararg list.
     * 
     * If there is more than one constructor, the method chooses the longest
     * constructor (the one with the most arguments). If there are two or more
     * constructors with the same argument count, it is unspecified which one is
     * used.
     * 
     * Throws a {@link RuntimeException} if the creation of the instance via reflection fails.
     */
    @Nonnull
    public static <T> T newInstanceMockDependencies( @Nonnull final Class<T> clazz, @Nonnull final Object... mocks ) {

        if ( clazz.getDeclaredConstructors().length == 0 ) {
            throw new IllegalArgumentException( String.format( "class %s has no accessible constructor", clazz.getName() ) );
        }

        final Constructor<?> cons = findBiggestConstructor( clazz );
        cons.setAccessible( true );
        final List<Object> actualParams = new ArrayList<Object>( cons.getParameterTypes().length );
        final List<Object> usedSuppliedMocks = new LinkedList<Object>();

        for ( final Class<?> formalParam : cons.getParameterTypes() ) {
            final Object mock = findMock( formalParam, mocks );
            if ( mock != null ) {
                actualParams.add( mock );
                usedSuppliedMocks.add( mock );
            } else {
                actualParams.add( Mockito.mock( formalParam ) );
            }
        }

        if ( usedSuppliedMocks.size() != mocks.length ) {
            throw new IllegalArgumentException( "At least one supplied mock wasn't used, maybe the class dependencies changed?" );
        }

        try {
			return clazz.cast( cons.newInstance( actualParams.toArray() ) );
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
    }

    @CheckForNull
    private static Object findMock( @Nonnull final Class<?> formalParam, @Nonnull final Object... mocks ) {
        for ( final Object mock : mocks ) {
            if ( formalParam.isAssignableFrom( mock.getClass() ) ) {
                return mock;
            }
        }

        return null;
    }

    /**
     * Search for "biggest" constructor. Assumes that the class hat at least one
     * public constructor (otherwise an exception will be thrown).
     */
    @Nonnull
    private static Constructor<?> findBiggestConstructor( @Nonnull final Class<?> clazz ) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> biggest = constructors[0];
        for ( int i = 1; i < constructors.length; i++ ) {
            final Constructor<?> c = constructors[i];
            if ( c.getParameterTypes().length > biggest.getParameterTypes().length ) {
                biggest = c;
            }
        }
        return biggest;
    }
}
