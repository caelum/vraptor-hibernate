package br.com.caelum.vraptor.plugin.hibernate4;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

/**
 * Creates a {@link SessionFactory} object, once when application starts.
 * 
 * @author Otávio Scherer Garcia
 */
@Component
@ApplicationScoped
public class SessionFactoryCreator
    implements ComponentFactory<SessionFactory> {

    private final Configuration cfg;
    private final ServiceRegistry serviceRegistry;
    private SessionFactory sessionFactory;

    public SessionFactoryCreator(Configuration cfg, ServiceRegistry serviceRegistry) {
        this.cfg = cfg;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Build a {@link SessionFactory}.
     */
    @PostConstruct
    protected void create() {
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
    }

    /**
     * Closes {@link SessionFactory} if it's not closed.
     */
    @PreDestroy
    protected void destroy() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    public SessionFactory getInstance() {
        return sessionFactory;
    }
}