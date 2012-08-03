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

import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

/**
 * Creates a Hibernate {@link Configuration}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
@Component
@ApplicationScoped
public class ConfigurationCreator
    implements ComponentFactory<Configuration> {

    private Configuration cfg;
    private Environment env;

    public ConfigurationCreator(Environment env) {
        this.env = env;
    }

    /**
     * Create a new instance for {@link Configuration}, and after call the
     * {@link ConfigurationCreator#configureExtras()} method, that you can override to configure extra guys.
     * This method uses vraptor-environment that allow you to get different resources for each environment you
     * needs.
     */
    @PostConstruct
    public void create() {
        cfg = new Configuration().configure(env.getResource("/hibernate.cfg.xml"));
        configureExtras();
    }

    /**
     * This method can override if you want to configure more things.
     */
    public void configureExtras() {

    }

    public Configuration getInstance() {
        return cfg;
    }
}
