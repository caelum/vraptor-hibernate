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
    protected void create() {
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
