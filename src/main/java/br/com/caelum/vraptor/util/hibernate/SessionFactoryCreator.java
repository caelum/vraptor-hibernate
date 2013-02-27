/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.caelum.vraptor.util.hibernate;

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.Container;

/**
 * Creates a SessionFactory from default resource /hibernate.cfg.xml, using
 * AnnotationConfiguration, and provides it to container.
 * 
 * @author Lucas Cavalcanti
 * @author Guilherme Silveira
 */
@Component
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {

	private SessionFactory factory;
	private final Container container;

	public SessionFactoryCreator(Container container) {
		this.container = container;
	}

	@PostConstruct
	public void create() {
		Configuration configuration = new AnnotationConfiguration();
		configuration = configuration.configure(getHibernateCfgLocation());
		factory = configuration.buildSessionFactory();
	}

	public SessionFactory getInstance() {
		return factory;
	}

	@PreDestroy
	public void destroy() {
		factory.close();
	}
	
	private URL getHibernateCfgLocation() {
		if (isEnvironmentAvailable()) {
			Environment env = container.instanceFor(Environment.class);
			return env.getResource(getHibernateCfgName());
		}

		return getClass().getResource(getHibernateCfgName());
	}
	
	protected String getHibernateCfgName() {
		return "/hibernate.cfg.xml";
	}
	
	private boolean isEnvironmentAvailable() {
		try {
			Class.forName("br.com.caelum.vraptor.environment.Environment");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
