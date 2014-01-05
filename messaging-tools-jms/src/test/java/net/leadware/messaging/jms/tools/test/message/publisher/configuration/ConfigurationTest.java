package net.leadware.messaging.jms.tools.test.message.publisher.configuration;

/*
 * #%L
 * messaging-tools
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2014 Leadware
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.assertEquals;

import java.util.Properties;


import net.leadware.messaging.jms.tools.message.configuration.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de test de la classe de configuration 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 16:36:22
 */
public class ConfigurationTest {
	
	/**
	 * Configuration
	 */
	private Configuration configuration;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
		// Vidage
		clean(configuration);
	}

	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.message.configuration.Configuration#Configuration()}.
	 */
	@Test
	public void testConfiguration() {
		
		// Instanciation par le constructeur par defaut
		configuration = new Configuration();
		
		// Verifions la presence de certaines proprietes
		assertEquals("org.jnp.interfaces.NamingContextFactory", configuration.getProperty(Configuration.INITIAL_CONTEXT_FACTORY));
		assertEquals("/ConnectionFactory", configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
	}

	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.message.configuration.Configuration#Configuration(java.util.Properties)}.
	 */
	@Test
	public void testConfigurationProperties() {
		
		// Fabrique de context initial
		String initialContextFactory = "MyInitialContextFactory";
		
		// Propriete tierce
		String thirdPartyProperty = "test.redefine.property";
		
		// Proprietes
		Properties properties = new Properties();
		
		// Ajout des proprietes
		properties.put(Configuration.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		properties.put("jms.tools.test.property", thirdPartyProperty);
		
		// Instanciation de la configuration
		configuration = new Configuration(properties);
		
		// verification des proprietes
		assertEquals(initialContextFactory, configuration.getProperty(Configuration.INITIAL_CONTEXT_FACTORY));
		assertEquals("/ConnectionFactory", configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
	}

	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.message.configuration.Configuration#Configuration(java.lang.String)}.
	 */
	@Test
	public void testConfigurationString() {
		
		// Instanciation par le constructeur par defaut
		configuration = new Configuration("configuration/another-jndi.properties");
		
		// Verifions la presence de certaines proprietes
		assertEquals("another", configuration.getProperty(Configuration.INITIAL_CONTEXT_FACTORY));
		assertEquals("jndi/ConnectionFactory", configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
	}
	
	/**
	 * Methode de nettoyage de la configuration
	 * @param configuration Configuration a nettoyer
	 */
	private void clean(Configuration configuration) {
		
		// Si la configuration est nulle
		if(configuration == null) return;
		
		// Nettoyage
		configuration.clear();
	}
}
