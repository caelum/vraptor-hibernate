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
package br.com.caelum.vraptor.hibernate.extra;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.type.Type;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.converter.Converter;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.events.ControllerFound;
import br.com.caelum.vraptor.http.Parameter;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.view.FlashScope;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Observer that loads given entity from the database.
 * 
 * @author Lucas Cavalcanti
 * @author Cecilia Fernandes
 * @author Otávio Scherer Garcia
 * @since vraptor 3.4.0
 */
public class ParameterLoader {

	private Session session;
	private HttpServletRequest request;
	private ParameterNameProvider provider;
	private Result result;
	private Converters converters;
	private FlashScope flash;

	/**
	 * @deprecated CDI eyes only
	 */
	public ParameterLoader() {
	}

	@Inject
	public ParameterLoader(Session session, HttpServletRequest request, ParameterNameProvider provider, Result result,
			Converters converters, FlashScope flash) {
		this.session = session;
		this.request = request;
		this.provider = provider;
		this.result = result;
		this.converters = converters;
		this.flash = flash;
	}

	public boolean containsLoadAnnotation(ControllerMethod method) {
		return any(asList(method.getMethod().getParameterAnnotations()), hasAnnotation(Load.class));
	}

	public void load(@Observes ControllerFound event) {
		ControllerMethod method = event.getMethod();

		if (containsLoadAnnotation(method)) {
			Annotation[][] annotations = method.getMethod().getParameterAnnotations();

			final Parameter[] parameters = provider.parametersFor(method.getMethod());
			final Class<?>[] types = method.getMethod().getParameterTypes();
			final Object[] args = flash.consumeParameters(method);

			for (int i = 0; i < parameters.length; i++) {
				if (hasLoadAnnotation(annotations[i])) {
					String name = parameters[i].getName();
					Object loaded = load(name, types[i]);

					// TODO extract to method, so users can override behavior
					if (loaded == null) {
						result.notFound();
						return;
					}

					if (args != null) {
						args[i] = loaded;
					} else {
						request.setAttribute(name, loaded);
					}
				}
			}

			flash.includeParameters(method, args);
		}
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
		checkArgument(converter != null, "Entity %s id type %s must have a converter", type.getSimpleName(), idType);

		Serializable id = (Serializable) converter.convert(parameter, type);
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
