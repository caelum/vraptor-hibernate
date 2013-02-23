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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Container;

public class PluginTest {

    private ConfigurationCreator configurationCreator;
    private Configuration configuration;

    private ServiceRegistryCreator serviceRegistryCreator;
    private ServiceRegistry serviceRegistry;

    private SessionFactoryCreator sessionFactoryCreator;
    private SessionFactory sessionFactory;

    private SessionCreator sessionCreator;
    private Session session;

    private Container container;
    private Environment env;

    @Test
    public void testWithoutEnvironment() {
        container = mock(Container.class);
        env = mock(Environment.class);

        buildConfigurationWithoutEnvironment();
        buildServiceRegistry();
        buildSessionFactory();
        buildSession();

        assertFalse(sessionFactory.isClosed());
        assertTrue(session.isOpen());

        destroyObjects();

        assertFalse(session.isOpen());
        assertTrue(sessionFactory.isClosed());
    }

    @Test
    public void testWithEnvironment() {
        container = mock(Container.class);
        env = mock(Environment.class);

        buildConfigurationWithEnvironment();
        buildServiceRegistry();
        buildSessionFactory();
        buildSession();

        assertFalse(sessionFactory.isClosed());
        assertTrue(session.isOpen());

        destroyObjects();

        assertFalse(session.isOpen());
        assertTrue(sessionFactory.isClosed());
    }

    private void buildConfigurationWithoutEnvironment() {
        when(container.canProvide(Environment.class)).thenReturn(false);

        configurationCreator = new ConfigurationCreator(container);
        configurationCreator.create();
        configuration = configurationCreator.getInstance();
    }

    private void buildConfigurationWithEnvironment() {
        when(container.canProvide(Environment.class)).thenReturn(true);
        when(container.instanceFor(Environment.class)).thenReturn(env);
        
        URL hibcfg = getClass().getResource("/hibernate.cfg.xml");
        stub(env.getResource(anyString())).toReturn(hibcfg);

        configurationCreator = new ConfigurationCreator(container);
        configurationCreator.create();
        configuration = configurationCreator.getInstance();
    }

    private void buildServiceRegistry() {
        serviceRegistryCreator = new ServiceRegistryCreator(configuration);
        serviceRegistryCreator.create();
        serviceRegistry = serviceRegistryCreator.getInstance();
    }

    private void buildSessionFactory() {
        sessionFactoryCreator = new SessionFactoryCreator(configuration, serviceRegistry);
        sessionFactoryCreator.create();
        sessionFactory = sessionFactoryCreator.getInstance();
    }

    private void buildSession() {
        sessionCreator = new SessionCreator(sessionFactory);
        sessionCreator.create();
        session = sessionCreator.getInstance();
    }

    private void destroyObjects() {
        sessionCreator.destroy();
        sessionFactoryCreator.destroy();
        serviceRegistryCreator.destroy();
    }

}
