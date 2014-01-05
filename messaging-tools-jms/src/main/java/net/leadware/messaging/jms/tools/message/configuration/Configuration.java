package net.leadware.messaging.jms.tools.message.configuration;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Classe representant la configuration d'un producteur/consumateur JMS 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:34:12
 */
public class Configuration {

	/**
	 * Un logger
	 */
	protected Logger log = Logger.getLogger(getClass());

	/**
	 * Propriete contenant le scheme de l'URL du fournisseur JNDI
	 */
	public static final String PROVIDER_URL_SCHEME = "application.jms.java.naming.provider.url.scheme";
	
	/**
	 * Propriete contenant le port de l'URL du fournisseur JNDI
	 */
	public static final String PROVIDER_URL_PORT = "application.jms.java.naming.provider.url.port";
	
	/**
	 * Nom de la propriete Initial context factory
	 */
	public static final String INITIAL_CONTEXT_FACTORY = "application.jms.java.naming.factory.initial";
	
	/**
	 * Nom de la propriete URL
	 */
	public static final String PROVIDER_URL = "application.jms.java.naming.provider.url";
	
	/**
	 * Nom de la propriete url Packages
	 */
	public static final String URL_PKG_PREFIXES = "application.jms.java.naming.factory.url.pkgs";
	
	/**
	 * Nom de la propriete contenant la Fabrique de connection initiale
	 */
	public static final String CONNECTION_FACTORY_NAME = "application.jms.connection.factory";
	
	/**
	 * Nom de la propriete JMS Transport On Client
	 */
	public static final String JMS_TRANSPORT_ON_CLIENT = "application.jms.transport.on.client";
	
	/**
	 * Nom de la propriete Jms Server Host
	 */
	public static final String JMS_SERVER_HOST = "application.jms.server.host";
	
	/**
	 * Nom de la propriete Jms Server Port
	 */
	public static final String JMS_SERVER_PORT = "application.jms.server.port";

	/**
	 * Nom de la propriete JMS Transport On Client
	 */
	public static final String JMS_CNX_FACT_BUILDER_CLASS = "application.jms.client.transport.builder.class";
	
	
	/**
	 * Ensemble des proprietes
	 */
	protected Properties properties = new Properties();
	
	/**
	 * Etat de recuperation de la fabrique de connection depuis le serveur
	 */
	protected boolean jndiConnectionFactory = true;
	
	/**
	 * Constructeur par defaut
	 */
	public Configuration() {
		
		// Ajout du la Fabrique de contexte
		properties.put(INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		
		// Ajout de l'URL du fornisseur
		properties.put(PROVIDER_URL, "jnp://localhost:1099");
		
		// Ajout du nom des packages de base
		properties.put(URL_PKG_PREFIXES, "org.jboss.naming.client");
		
		// Ajout du nom de la Fabrique de connection
		properties.put(CONNECTION_FACTORY_NAME, "/ConnectionFactory"); 
		
		// Initialisation
		initProperties(loadFromSystemproperties());
		
		try {
			
			// Initialisation
			initProperties(loadFromFile("jndi.properties"));
			
		} catch (Exception e) {}
	}
	
	/**
	 * Methode permettant d'initialiser les proprietes de configuration a partir d'un objet
	 * @param contextProperties	Objet source
	 */
	protected void initProperties(Properties contextProperties) {
		
		// Ajout des proprietes
		if(contextProperties != null) {
			
			// Ajout
			this.properties.putAll(contextProperties);
		}
		
	}
	
	/**
	 * Constructeur avec initialisation des proprietes
	 * @param properties	Ensemble des proprietes
	 */
	public Configuration(Properties properties) {
		
		// Constructeur par defaut
		this();
		
		// Ajout des proprietes
		if(properties != null) this.properties.putAll(properties);
	}
	
	/**
	 * Constructeur avec initialisation des proprietes
	 * @param classPathConfigurationFile	Fichier de proprietes (dans le classpath)
	 */
	public Configuration(String classPathConfigurationFile) {

		// Constructeur par defaut
		this();
		
		// Chargement a partir du fichier
		Properties loadedProperties = loadFromFile(classPathConfigurationFile);
		
		// Ajout des proprietes
		if(loadedProperties != null) this.properties.putAll(loadedProperties);
	}
	
	/**
	 * Methode de chargement des proprietes systemes
	 * @return Ensemble des proprietes systemes
	 */
	private Properties loadFromSystemproperties() {
		
		// On retourne les proprietes systeme
		return System.getProperties();
	}
	
	/**
	 * Methode de chargement des proprietes a partir d'un fichier du classpath
	 * @param classPathConfigurationFile	Fichier de configuration du classpath
	 * @return	Ensemble des Proprietes
	 */
	private Properties loadFromFile(String classPathConfigurationFile) {

		// Verifions que le nom du fichier n'est pas null
		assert (classPathConfigurationFile != null && !classPathConfigurationFile.trim().isEmpty()) : "Veuillez renseigner le nom du fichier de proprietes";
		
		// Chargement du fichier
		URL url = getClass().getClassLoader().getResource(classPathConfigurationFile.trim());
		
		// Si l'URL est nulle
		if(url == null) throw new RuntimeException("le Fichier __var__ n'existe pas dans le classpath".replaceFirst("__var__", classPathConfigurationFile.trim()));
		
		// Stream
		InputStream inputStream = null;
		
		try {

			// Obtention du Stream
			inputStream = url.openStream();
			
		} catch (IOException e) {
			
			// Un log
			log.debug("#ctor - Erreur lors de l'ouverture du stream sur le fichier de configuration [" + e.getMessage() + "]");
			
			// On retlance
			throw new RuntimeException("#ctor - Erreur lors de l'ouverture du stream sur le fichier de configuration", e);
		}
		
		// Construction d'un Reader
		Reader reader = new InputStreamReader(inputStream);
		
		// Proprietes
		Properties loadedProperties = new Properties();
		
		try {
			
			// Chargement des proprietes
			loadedProperties.load(reader);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#ctor - Erreur lors du chargement des proprietes du fichier de configuration [" + e.getMessage() + "]");
			
			// On retlance
			throw new RuntimeException("#ctor - Erreur lors du chargement des proprietes du fichier de configuration", e);
		}
		
		// on retourne les proprietes
		return loadedProperties;
	}
	
	/**
	 * Methode d'obtention des proprietes
	 * @return	Ensemble des proprietes
	 */
	public Properties getProperties() {
		return this.properties;
	}
	
	/**
	 * Methode d'obtention de la valeur d'une propriete
	 * @param propertyName	Nom de la propriete
	 * @param defaultValue	Valeur par defaut de la propriete
	 * @return	Valeur de la propriete
	 */
	public String getProperty(String propertyName, String defaultValue) {
		
		// Si le nom de la propriete est null
		if(propertyName == null || propertyName.trim().isEmpty()) return defaultValue.trim();
		
		// Obtention de la propriete
		return this.properties.getProperty(propertyName.trim(), defaultValue.trim());
	}

	/**
	 * Methode d'obtention de la valeur d'une propriete
	 * @param propertyName	Nom de la propriete
	 * @return	Valeur de la propriete
	 */
	public String getProperty(String propertyName) {
		
		// Si le nom de la propriete est null
		if(propertyName == null || propertyName.trim().isEmpty()) return null;
		
		// Obtention de la propriete
		return this.properties.getProperty(propertyName.trim());
	}

	/**
	 * Methode permettant d'ajouter une propriete dans la configuration
	 * @param propertyName	Nom de la propriete
	 * @param value	Caleur de la propriete
	 */
	public void put(String propertyName, String value) {
		
		// Si le nom de la propriete est null
		if(propertyName == null || propertyName.trim().isEmpty()) return;
		
		// Si la valeur est nulle
		if(value == null || value.trim().isEmpty()) value = "";
		
		// Mise en place de la propriete
		this.properties.put(propertyName.trim(), value.trim());
	}

	/**
	 * Methode de vidage de la configuration
	 */
	public void clear() {
		
		// On vide
		this.properties.clear();
	}
	
	/**
	 * Methode de suppression d'une propriete
	 * @param propertyName	Nom de la propriete a supprimer
	 */
	public void remove(String propertyName) {
		
		// Si le nom de la propriete est null
		if(propertyName == null || propertyName.trim().isEmpty()) return;
		
		// Suppression
		properties.remove(propertyName.trim());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		// Buffer
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("JMS CONFIGURATION");
		buffer.append('\n');
		
		// Map Triee
		TreeMap<?, ?> tMap = new TreeMap<Object, Object>(properties);
		
		// Ensembles des cles
		Set<?> keys = tMap.keySet();
		
		// Parcours
		for (Object key : keys) {
			
			// Ajout dans le buffer
			buffer.append("========> " + key + " : " + tMap.get(key));
			buffer.append('\n');
		}
		
		// On retourne le contenu du buffer
		return buffer.toString();
	}
}
