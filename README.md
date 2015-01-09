# VRaptor Hibernate Plugin

![Build status](https://secure.travis-ci.org/caelum/vraptor-hibernate.png)

VRaptor Hibernate Plugin provides support to use with Hibernate 4.

# How to install?

You only need to copy the jar to your classpath. VRaptor will register plugin when 
your application starts without any configurations. Downloads are available in 
downloads area or in Maven Repository:

	<dependency>
	  <groupId>br.com.caelum.vraptor</groupId>
	  <artifactId>vraptor-hibernate</artifactId>
	  <version>4.0.0.Final</version> <!-- or the latest version -->
	</dependency>

# Transactional Control

The default behavior is that each request will have a transaction available.

If you want, you can enable a decorator to change this behavior. 

When enabled, it will open transactions only for methods with `@Transactional` annotation. 

To do that you just need to add the follow content into your project's `beans.xml`:

```xml
<decorators>
    <class>br.com.caelum.vraptor.hibernate.TransactionDecorator</class>
</decorators>
```
# Extra Configurations

If you want to add some custom configuration in `org.hibernate.cfg.Configuration`,
extend the `br.com.caelum.vraptor.hibernate.ConfigurationCreator` class and override
the `protected void extraConfigurations(Configuration configuration)` method.
You need to annotate your extended class with `@Specializes` annotation.

```java
@Specializes
public class MyConfigurationCreator extends ConfigurationCreator {
	@Override
	protected void extraConfigurations(Configuration configuration) {
		configuration.setInterceptor(new MyHibernateInterceptor());
	}
}
```