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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Create a Hibernate {@link ServiceRegistry}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
@ApplicationScoped
public class ServiceRegistryCreator {

	private Configuration cfg;

	@Deprecated
	// CDI eyes only
	public ServiceRegistryCreator() {
	}

	@Inject
	public ServiceRegistryCreator(Configuration cfg) {
		this.cfg = cfg;
	}

	public void destroy(@Disposes ServiceRegistry serviceRegistry) {
		ServiceRegistryBuilder.destroy(serviceRegistry);
	}

	@Produces
	public ServiceRegistry getInstance() {
		ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
		return builder.applySettings(cfg.getProperties()).buildServiceRegistry();
	}
}
