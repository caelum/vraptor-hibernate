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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.environment.Environment;

/**
 * The goal of this class is only to test if Hibernate Session starts properly. No more tests are made.
 */
public class PluginTest {

	private Environment environment;

	private ConfigurationCreator configurationCreator;
	private Configuration configuration;

	private ServiceRegistryCreator serviceRegistryCreator;
	private ServiceRegistry serviceRegistry;

	private SessionFactoryCreator sessionFactoryCreator;
	private SessionFactory sessionFactory;

	private SessionCreator sessionCreator;
	private Session session;

	@Before
	public void setup() {
		environment = mock(Environment.class);

		URL cfgLocation = getClass().getResource("/hibernate.cfg.xml");
		when(environment.getResource(anyString())).thenReturn(cfgLocation);
	}

	@Test
	public void testIfSessionisUp() {
		buildConfiguration();
		buildServiceRegistry();
		buildSessionFactory();
		buildSession();

		assertFalse(sessionFactory.isClosed());
		assertTrue(session.isOpen());

		destroyObjects();

		assertFalse(session.isOpen());
		assertTrue(sessionFactory.isClosed());
	}

	private void buildConfiguration() {
		configurationCreator = new ConfigurationCreator(environment);
		configurationCreator = spy(configurationCreator);

		configuration = configurationCreator.getInstance();
	}

	private void buildServiceRegistry() {
		serviceRegistryCreator = new ServiceRegistryCreator(configuration);
		serviceRegistry = serviceRegistryCreator.getInstance();
	}

	private void buildSessionFactory() {
		sessionFactoryCreator = new SessionFactoryCreator(configuration, serviceRegistry);
		sessionFactory = sessionFactoryCreator.getInstance();
	}

	private void buildSession() {
		sessionCreator = new SessionCreator(sessionFactory);
		session = sessionCreator.getInstance();
	}

	private void destroyObjects() {
		sessionCreator.destroy(session);
		sessionFactoryCreator.destroy(sessionFactory);
		serviceRegistryCreator.destroy(serviceRegistry);
	}
}
