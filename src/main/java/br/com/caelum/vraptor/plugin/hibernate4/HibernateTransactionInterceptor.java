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
package br.com.caelum.vraptor.plugin.hibernate4;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor4.AroundCall;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.interceptor.SimpleInterceptorStack;

/**
 * An example of Hibernate Transaction management on VRaptor
 * 
 * @author Lucas Cavalcanti
 */
@Intercepts
public class HibernateTransactionInterceptor {

    private Session session;
    private Validator validator;

    @Deprecated	//CDI eyes only
	public HibernateTransactionInterceptor() {}
    
    @Inject
    public HibernateTransactionInterceptor(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
    }

    @AroundCall
    public void intercept(SimpleInterceptorStack stack) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            stack.next();
            if (!validator.hasErrors() && transaction.isActive()) {
                transaction.commit();
            }
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}