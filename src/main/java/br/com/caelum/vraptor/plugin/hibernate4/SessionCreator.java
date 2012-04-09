package br.com.caelum.vraptor.plugin.hibernate4;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;

/**
 * Creates a Hibernate {@link Session}, once per request.
 * 
 * @author Otávio Scherer Garcia
 */
@Component
@RequestScoped
public class SessionCreator
    implements ComponentFactory<Session> {

    private final SessionFactory factory;
    private Session session;

    public SessionCreator(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     * Open a {@link Session}.
     */
    @PostConstruct
    public void create() {
        session = factory.openSession();
    }

    /**
     * Close a {@link Session} if it's open.
     */
    @PreDestroy
    public void destroy() {
        if (session.isOpen()) {
            session.close();
        }
    }

    public Session getInstance() {
        return session;
    }

}