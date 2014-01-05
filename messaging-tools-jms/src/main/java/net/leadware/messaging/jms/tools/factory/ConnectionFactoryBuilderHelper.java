/**
 * 
 */
package net.leadware.messaging.jms.tools.factory;

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

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;


import net.leadware.messaging.jms.tools.message.configuration.Configuration;

import org.apache.log4j.Logger;


/**
 * Classe representant la classe d'aide au chargement du constructeur de fabrique de connexion JMS client 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:24:45
 */
public class ConnectionFactoryBuilderHelper {

	/**
	 * Un logger
	 */
	private static Logger log = Logger.getLogger(ConnectionFactoryBuilderHelper.class);
	
	/**
	 * Methode permettant de construire le contexte jndi
	 * @param configuration	Configuration JNDI
	 * @return	Contexte jndi
	 */
	public static Context buildContext(Configuration configuration) {

		// Verification de la configuration
		assert configuration != null : "Veuillez renseigner la configuration";
		
		// Contexte
		Context context = null;
		
		// Proprietes JNDI

		// Proprietes JNDI
		Properties jndiProperties = new Properties();
		
		// Ajout de la factory
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, 
						   configuration.getProperty(Configuration.INITIAL_CONTEXT_FACTORY, "").trim());

		// Ajout de la package
		jndiProperties.put(Context.URL_PKG_PREFIXES, 
						   configuration.getProperty(Configuration.URL_PKG_PREFIXES, "").trim());
		
		// Ajout de la package
		jndiProperties.put(Context.PROVIDER_URL, 
						   configuration.getProperty(Configuration.PROVIDER_URL, "").trim());
		
		try {

			// Initialisation du contexte
			context = new InitialContext(jndiProperties);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("ConnectionFactoryBuilderHelper#buildContext - Erreur lors de l'initialisation du contexte initial [" + e.getMessage() + "]");
			
			// On retlance
			throw new RuntimeException("ConnectionFactoryBuilderHelper#buildContext - Erreur lors de l'initialisation du contexte initial", e);
		}
		
		// On retourne le contexte
		return context;
	}
	
	/**
	 * Methode permettant de construire la Fabrique de connexion
	 * @param configuration	Configuration JMS
	 * @param context Contexte JNDI
	 * @return	Fabrique de connexion
	 */
	public static ConnectionFactory buildConnectionfactory(Configuration configuration, Context context) {
		
		// Verification de la configuration
		assert configuration != null : "Veuillez renseigner la configuration";
		
		// Fabrique de connection
		ConnectionFactory connectionFactory = null;
		
		// Obtention de l'√©tat de construction du transport client
		String jmsTransportOnClient = configuration.getProperty(Configuration.JMS_TRANSPORT_ON_CLIENT, "true").trim();
		
		// Si c'est OK
		if(jmsTransportOnClient.equalsIgnoreCase("true")) {
			
        	// Obtention de l'adresse du serveur JMS
        	String jmsServerHost = configuration.getProperty(Configuration.JMS_SERVER_HOST, "localhost").trim();

        	// Obtention du port du serveur JMS
        	String jmsServerPort = configuration.getProperty(Configuration.JMS_SERVER_PORT, "5445").trim();

        	// Obtention de l'adresse du serveur JMS
        	String jmsClientTransportBuilderClass = configuration.getProperty(Configuration.JMS_CNX_FACT_BUILDER_CLASS, "").trim();
        	
        	// Chargement de la classe
        	ConnectionFactoryClientBuilder builder = ConnectionFactoryBuilderHelper.load(jmsClientTransportBuilderClass);
        	
        	// Construction de la classe
        	builder.setJmsServerHost(jmsServerHost);
        	builder.setJmsServerPort(jmsServerPort);
        	
        	// Construction de la Fabrique
        	connectionFactory = builder.build();
        	
		} else {
			
			try {

				// Chargement de la fabrique de connection
				connectionFactory = (ConnectionFactory) context.lookup(configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
				
			} catch (Exception e) {
				
				// Un log
				log.debug("#ctor - Erreur lors du chargement de la fabrique de connexion JMS [" + e.getMessage() + "]");
				
				// On retlance
				throw new RuntimeException("#ctor - Erreur lors du chargement de la fabrique de connexion JMS", e);
			}
		}
		
		// On retourne la fabrique
		return connectionFactory;
	}
	
	/**
	 * Methode permettant de contstruire une instance du constructeur de Fabdirque de connexion JMS Cliente
	 * @param className	Nom de la classe a charger
	 * @return	Instance de la classe chargee
	 */
	public static ConnectionFactoryClientBuilder load(String className) {
		
		// Si le nom de la classe est vide
		if(className == null || className.trim().isEmpty())
			throw new RuntimeException("clientconnectionfactorybuilderloader.load.invalid.classname");
		
		// Classe chargee
		Class<?> loadedClass = null;
		
		try {
			
			// Tentative de chargement de la classe
			loadedClass = Class.forName(className.trim());
			
		} catch (ClassNotFoundException e) {
			
			// On relance
			throw new RuntimeException("clientconnectionfactorybuilderloader.load.error.loadclass",e);
		}
		
		// Si la classe n'est pas de l'instance cible
		if(!ConnectionFactoryClientBuilder.class.isAssignableFrom(loadedClass))
			throw new RuntimeException("clientconnectionfactorybuilderloader.load.invalid.classtype");
		
		// On caste
		Class<? extends ConnectionFactoryClientBuilder> connectionFactoryBuilderClass = loadedClass.asSubclass(ConnectionFactoryClientBuilder.class);

		// Instance
		ConnectionFactoryClientBuilder builder = null;
		
		try {

			// Instanciation
			builder = connectionFactoryBuilderClass.newInstance();
			
		} catch (IllegalAccessException e) {
			
			// On relance
			throw new RuntimeException("clientconnectionfactorybuilderloader.load.error.instanceclass",e);
			
		} catch (InstantiationException e) {
			
			// On relance
			throw new RuntimeException("clientconnectionfactorybuilderloader.load.error.instanceclass",e);
			
		}
		
		// On retourne l'instance
		return builder;
	}
}
