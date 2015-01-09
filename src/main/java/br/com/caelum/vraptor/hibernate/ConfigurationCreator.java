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

import br.com.caelum.vraptor.environment.Environment;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.net.URL;


/**
 * Creates a Hibernate {@link Configuration}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
public class ConfigurationCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCreator.class);

	private final Environment environment;
	
	/**
	 * @deprecated cdi eyes only
	 */	
	protected ConfigurationCreator() {
		this(null);	
	}

	@Inject
	public ConfigurationCreator(Environment environment) {
		this.environment = environment;	
	}

	protected URL getHibernateCfgLocation() {
		return environment.getResource("/hibernate.cfg.xml");
	}

	@Produces
	@ApplicationScoped
	public Configuration getInstance() {
		Configuration configuration = new Configuration();

		extraConfigurations(configuration);

		URL location = getHibernateCfgLocation();
		LOGGER.debug("building configuration using {} file", location);
		return configuration.configure(location);
	}

	/**
	 * Override this method in a specialize class (using CDI's @Specializes) to set custom configurations.
	 *
	 * @param configuration
	 */
	protected void extraConfigurations(Configuration configuration) {
	}
}
