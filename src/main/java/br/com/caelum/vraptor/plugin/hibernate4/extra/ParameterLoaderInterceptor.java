/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package br.com.caelum.vraptor.plugin.hibernate4.extra;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Arrays.asList;
import static com.google.common.base.Predicates.instanceOf;


import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.type.Type;

import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.view.FlashScope;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Interceptor that loads given entity from the database.
 *
 * @author Lucas Cavalcanti
 * @author Cecilia Fernandes
 * @author Otávio Scherer Garcia
 * @since vraptor 3.4.0
 *
 */
@Intercepts(before=ParametersInstantiatorInterceptor.class)
public class ParameterLoaderInterceptor implements Interceptor{

    private Session session;
    private HttpServletRequest request;
    private ParameterNameProvider provider;
    private Result result;
    private Converters converters;
    private Localization localization;
    private FlashScope flash;
	private ControllerMethod method;

    @Deprecated //CDI eyes only
	public ParameterLoaderInterceptor() {
	}
    
    @Inject
    public ParameterLoaderInterceptor(Session session, HttpServletRequest request, ParameterNameProvider provider,
            Result result, Converters converters, Localization localization, FlashScope flash) {
        this.session = session;
        this.request = request;
        this.provider = provider;
        this.result = result;
        this.converters = converters;
        this.localization = localization;
        this.flash = flash;
    }
    
    public boolean accepts(ControllerMethod method) {
        return any(asList(method.getMethod().getParameterAnnotations()), hasAnnotation(Load.class));
    }

	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object controllerInstance) throws InterceptionException{
    	
        Annotation[][] annotations = method.getMethod().getParameterAnnotations();

        final String[] names = provider.parameterNamesFor(method.getMethod());
        final Class<?>[] types = method.getMethod().getParameterTypes();
        final Object[] args = flash.consumeParameters(method);

        for (int i = 0; i < names.length; i++) {
            if (hasLoadAnnotation(annotations[i])) {
                Object loaded = load(names[i], types[i]);

                // TODO extract to method, so users can override behaviour
                if (loaded == null) {
                    result.notFound();
                    return;
                }

                if (args != null) {
                    args[i] = loaded;
                } else {
                    request.setAttribute(names[i], loaded);
                }
            }
        }

        flash.includeParameters(method, args);
        stack.next(method, controllerInstance);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object load(String name, Class type) {
        String idProperty = session.getSessionFactory().getClassMetadata(type).getIdentifierPropertyName();
        checkArgument(idProperty != null, "Entity %s must have an id property for @Load.", type.getSimpleName());

        String parameter = request.getParameter(name + "." + idProperty);
        if (parameter == null) {
            return null;
        }

        Type idType = session.getSessionFactory().getClassMetadata(type).getIdentifierType();
        Converter<?> converter = converters.to(idType.getReturnedClass());
        checkArgument(converter != null, "Entity %s id type %s must have a converter", 
                type.getSimpleName(), idType);

        Serializable id = (Serializable) converter.convert(parameter, type, localization.getBundle());
        return session.get(type, id);
    }

    private boolean hasLoadAnnotation(Annotation[] annotations) {
        return !isEmpty(Iterables.filter(asList(annotations), Load.class));
    }
    
    private Predicate<? super Annotation[]> hasAnnotation(final Class<?> annotation) {
    	return new Predicate<Annotation[]>() {
            public boolean apply(Annotation[] param) {
                return any(asList(param), instanceOf(annotation));
            }
        };
	}
}
