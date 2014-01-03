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

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.validator.Validator;

/**
 * Intercepts all requests starting a transaction. If any error occurs, the interceptor rollback transaction.
 * 
 * @author Lucas Cavalcanti
 */
@Intercepts
public class HibernateTransactionInterceptor {

	private Session session;
	private Validator validator;
	private MutableResponse response;

	/**
	 * @deprecated CDI eyes only
	 */
	public HibernateTransactionInterceptor() {
	}

	@Inject
	public HibernateTransactionInterceptor(Session session, Validator validator, MutableResponse response) {
		this.session = session;
		this.validator = validator;
		this.response = response;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {
		addRedirectListener();

		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			stack.next();
			commit(transaction);
		} finally {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	private void commit(Transaction transaction) {
		if (!validator.hasErrors() && transaction.isActive()) {
			transaction.commit();
		}
	}

	/**
	 * We force the commit before the redirect, this way we can abort the redirect if a database error occurs.
	 */
	private void addRedirectListener() {
		response.addRedirectListener(new MutableResponse.RedirectListener() {
			@Override
			public void beforeRedirect() {
				commit(session.getTransaction());
			}
		});
	}
}