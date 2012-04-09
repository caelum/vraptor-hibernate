package br.com.caelum.vraptor.plugin.hibernate4;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * An example of Hibernate Transaction management on VRaptor
 * 
 * @author Lucas Cavalcanti
 */
@Intercepts
public class HibernateTransactionInterceptor
    implements Interceptor {

    private final Session session;
    private final Validator validator;

    public HibernateTransactionInterceptor(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
    }

    public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            stack.next(method, instance);
            if (!validator.hasErrors()) {
                transaction.commit();
            }
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public boolean accepts(ResourceMethod method) {
        return true; // Will intercept all requests
    }
}