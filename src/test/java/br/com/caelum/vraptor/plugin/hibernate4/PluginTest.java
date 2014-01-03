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
import static org.mockito.Mockito.spy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;

public class PluginTest {

    private ConfigurationCreator configurationCreator;
    private Configuration configuration;

    private ServiceRegistryCreator serviceRegistryCreator;
    private ServiceRegistry serviceRegistry;

    private SessionFactoryCreator sessionFactoryCreator;
    private SessionFactory sessionFactory;

    private SessionCreator sessionCreator;
    private Session session;

    @Test
    public void testWithoutEnvironment() {
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

    private void buildConfigurationWithoutEnvironment() {
        configurationCreator = new ConfigurationCreator();
        configurationCreator = spy(configurationCreator);
        
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
