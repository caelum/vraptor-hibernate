## vraptor-hibernate

A vraptor hibernate plugin that adds support to hibernate on specific environments.

# installing

Copy the jar file to your app or use:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-hibernate</artifactId>
			<version>1.0.0</version>
			<scope>compile</scope>
		</dependency>
		
# using

Just got through VRaptor docs

# Creating a custom hibernate.cfg.xml per environment

You can configure the current environment by setting the environment property on web.xml:

Given, for example, "production", the loaded file is now "/production/hibernate.cfg.xml" on your classpath.

	<context-param>
		<param-name>br.com.caelum.vraptor.environment</param-name>
		<param-value>production</param-value>
	</context-param>

# help

Get help from vraptor developers and the community at vraptor's mailing list.
