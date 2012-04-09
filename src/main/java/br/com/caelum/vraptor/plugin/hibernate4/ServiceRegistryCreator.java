package br.com.caelum.vraptor.plugin.hibernate4;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

/**
 * Create a Hibernate {@link ServiceRegistry}, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
@Component
@ApplicationScoped
public class ServiceRegistryCreator
    implements ComponentFactory<ServiceRegistry> {

    private final Configuration cfg;
    private ServiceRegistry serviceRegistry;

    public ServiceRegistryCreator(Configuration cfg) {
        this.cfg = cfg;
    }

    /**
     * Builds a {@link ServiceRegistry}.
     */
    @PostConstruct
    public void create() {
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
        serviceRegistry = builder.applySettings(cfg.getProperties()).buildServiceRegistry();
    }

    /**
     * Destroy the {@link ServiceRegistry} when application is shutting down.
     */
    @PreDestroy
    public void destroy() {
        ServiceRegistryBuilder.destroy(serviceRegistry);
    }

    public ServiceRegistry getInstance() {
        return serviceRegistry;
    }
}
