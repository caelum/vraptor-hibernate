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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Creates a Hibernate {@link Session}, once per request.
 * 
 * @author Otávio Scherer Garcia
 */
@RequestScoped
public class SessionCreator {

    private SessionFactory factory;
    private Session session;

    @Deprecated
	//CDI eyes only
	public SessionCreator() {
	}
    
    @Inject
    public SessionCreator(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     * Open a {@link Session}.
     */
    @PostConstruct
    public void create() {
        session = factory.openSession();
    }

    /**
     * Close a {@link Session} if it's open.
     */
    @PreDestroy
    public void destroy() {
        if (session.isOpen()) {
            session.close();
        }
    }
    
    @Produces
    public Session getInstance() {
        return session;
    }

}