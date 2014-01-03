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

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.hibernate.cfg.Configuration;

/**
 * Creates a Hibernate {@link Configuration}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
@ApplicationScoped
public class ConfigurationCreator{

    private Configuration cfg;

    /**
     * Create a new instance for {@link Configuration}, and after call the
     * {@link ConfigurationCreator#configureExtras()} method, that you can override to configure extra guys.
     * If vraptor-environment is available on classpath, this method will use then to locate hibernate cfg
     * file.
     */
    @PostConstruct
    public void create() {
        cfg = new Configuration().configure(getHibernateCfgLocation());
    }

    protected URL getHibernateCfgLocation() {
        return getClass().getResource("/hibernate.cfg.xml");
    }

    @Produces
    public Configuration getInstance() {
        return cfg;
    }
}
