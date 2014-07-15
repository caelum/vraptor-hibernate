package br.com.caelum.vraptor.hibernate;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

public class TransactionDecoratorTest {

	TransactionDecorator decorator;
	@Mock private ControllerMethod method;
	@Mock private HibernateInterceptor interceptor;
	@Mock private SimpleInterceptorStack stack;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		decorator = new TransactionDecorator(interceptor, method);
	}

	@Test
	public void shouldIntercept() {
		when(method.containsAnnotation(Transactional.class)).thenReturn(true);
		
		decorator.intercept(stack);
		
		verify(stack, never()).next();
	}
	
	@Test
	public void shouldNotIntercept() {
		when(method.containsAnnotation(Transactional.class)).thenReturn(false);
		
		decorator.intercept(stack);
		
		verify(stack).next();
	}

}