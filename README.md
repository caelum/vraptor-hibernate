# VRaptor Hibernate 4 Plugin

VRaptor Hibernate 4 Plugin provides support to use with Hibernate 4. 

# Why other plugin?

The current plugin embedded into VRaptor doesn't work with Hibernate 4 because 
SessionFactory creation are diferent in this version. 

# How to install?

You only need to copy the jar to your classpath. VRaptor will register plugin when 
your application starts without any configurations. Downloads are available in 
downloads area or in Maven Repository:

<dependency>
  <groupId>br.com.caelum.vraptor</groupId>
	<artifactId>vraptor-plugin-hibernate4</artifactId>
	<version>1.0.0</version>
</dependency>