/***
 * Copyright (c) 2011 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.caelum.vraptor.hibernate;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;

/**
 * Decorator to {@link HibernateTransactionInterceptor}
 * 
 * @author Denilson Telaroli
 */

@Decorator
public class TransactionDecorator implements HibernateInterceptor {

	private final HibernateInterceptor delegate;
	private final ControllerMethod method;
	
	/**
	 * @deprecated CDI eyes only.
	 */
	protected TransactionDecorator() {
		this(null, null);
	}

	@Inject
	public TransactionDecorator(@Delegate @Any HibernateInterceptor delegate, ControllerMethod method) {
		this.delegate = delegate;
		this.method = method;
	}
	
	@Override
	public void intercept(SimpleInterceptorStack stack) {
		if(isTransactional()) {
			delegate.intercept(stack);
			return;
		}
		stack.next();
	}

	private boolean isTransactional() {
		return method.containsAnnotation(Transactional.class);
	}
	
}
