package br.com.caelum.vraptor.hibernate;

import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

public interface HibernateInterceptor {

	void intercept(SimpleInterceptorStack stack);
	
}
