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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a Hibernate {@link ServiceRegistry}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
public class ServiceRegistryCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistryCreator.class);
	private Configuration cfg;

	/**
	 * @deprecated CDI eyes only
	 */
	public ServiceRegistryCreator() {
	}

	@Inject
	public ServiceRegistryCreator(Configuration cfg) {
		this.cfg = cfg;
	}

	@Produces
	@ApplicationScoped
	public ServiceRegistry getInstance() {
		LOGGER.debug("creating a service registry");
		return new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
	}

	public void destroy(@Disposes ServiceRegistry serviceRegistry) {
		LOGGER.debug("destroying service registry");
		StandardServiceRegistryBuilder.destroy(serviceRegistry);
	}
}
