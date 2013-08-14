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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HibernateTransactionInterceptorTest {

    @Mock private Session session;
    @Mock private SimpleInterceptorStack stack;
    @Mock private ControllerMethod method;
    @Mock private Transaction transaction;
    @Mock private Validator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldStartAndCommitTransaction() throws Exception {
        HibernateTransactionInterceptor interceptor = new HibernateTransactionInterceptor(session, validator);

        when(session.beginTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);

        interceptor.intercept(stack);

        InOrder callOrder = inOrder(session, transaction, stack);
        callOrder.verify(session).beginTransaction();
        callOrder.verify(stack).next();
        callOrder.verify(transaction).commit();
    }

    @Test
    public void shouldRollbackTransactionIfStillActiveWhenExecutionFinishes() throws Exception {
        HibernateTransactionInterceptor interceptor = new HibernateTransactionInterceptor(session, validator);

        when(session.beginTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException()).when(stack).next();
        try{
        	interceptor.intercept(stack);
        }catch(Exception e){
        	//nothing
       	}
        verify(transaction,never()).commit();
        verify(transaction).rollback();
    }

}