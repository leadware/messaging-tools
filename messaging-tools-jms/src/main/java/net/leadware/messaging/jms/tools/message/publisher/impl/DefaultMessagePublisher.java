package net.leadware.messaging.jms.tools.message.publisher.impl;

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

import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;

import net.leadware.messaging.jms.tools.JmsTemplate;
import net.leadware.messaging.jms.tools.factory.ConnectionFactoryBuilderHelper;
import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.configuration.Configuration;
import net.leadware.messaging.jms.tools.message.creator.impl.DefaultMessageCreator;
import net.leadware.messaging.jms.tools.message.publisher.MessagePublisher;

import org.apache.log4j.Logger;


/**
 * Classe representant le producteur de message Applicatif par defaut 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 d�c. 2013 - 15:57:23
 */
public class DefaultMessagePublisher implements MessagePublisher {
	
	/**
	 * Un logger
	 */
	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * Template JMS
	 */
	private JmsTemplate jmsTemplate;
	
	/**
	 * Contexte JNDI
	 */
	private Context context;

	/**
	 * Constructeur par defaut
	 */
	public DefaultMessagePublisher() {
		
		// Instanciation du template
		jmsTemplate = new JmsTemplate();
	}
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param connectionFactory	Fabrique de connection
	 */
	public DefaultMessagePublisher(ConnectionFactory connectionFactory) {

		// Verification de la fabrique de connection
		assert connectionFactory != null : "Veuillez renseigner la fabrique de connection";
		
		// Construction du Template JMS
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param configuration Configuration
	 */
	@Deprecated
	public DefaultMessagePublisher(Configuration configuration) {

		// Initialisation du contexte
		this.context = ConnectionFactoryBuilderHelper.buildContext(configuration);
		
		// Fabrique de connexion
		ConnectionFactory connectionFactory = ConnectionFactoryBuilderHelper.buildConnectionfactory(configuration, this.context);
		
		// Construction du template JMS
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}

	/**
	 * Constructeur avec initialisation des parametres
	 * @param configuration Configuration
	 * @param context Contexte jndi
	 */
	public DefaultMessagePublisher(Configuration configuration, Context context) {
		
		// Initialisation du contexte
		this.context = context;
		
		// Fabrique de connexion
		ConnectionFactory connectionFactory = ConnectionFactoryBuilderHelper.buildConnectionfactory(configuration, this.context);
		
		// Construction du template JMS
		this.jmsTemplate = new JmsTemplate(connectionFactory);
	}
	
	/**
	 * Methode permettant de modifier la valeur du champ "userName"
	 * @param userName Nouvelle valeur du champ "userName"
	 */
	public void setUserName(String userName) {
	
		// Mise a jour du template
		this.jmsTemplate.setUserName(userName);
	}
	
	/**
	 * Methode permettant de modifier la valeur du champ "password"
	 * @param password Nouvelle valeur du champ "password"
	 */
	public void setPassword(String password) {
	
		// Mise a jour du template
		this.jmsTemplate.setPassword(password);
	}

	/**
	 * Methode permettant de modifier la valeur du champ "securityEnabled"
	 * @param securityEnabled Nouvelle valeur du champ "securityEnabled"
	 */
	public void setSecurityEnabled(boolean securityEnabled) {
	
		// Mise a jour du template
		this.jmsTemplate.setSecurityEnabled(securityEnabled);
	}
	
	/**
	 * Methode permettant de modifier la valeur du champ "connectionFactory"
	 * @param connectionFactory Nouvelle valeur du champ "connectionFactory"
	 */
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
	
		// Mise a jour du template
		this.jmsTemplate.setConnectionFactory(connectionFactory);
	}

	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.publisher.IPMessagePublisher#publish(com.oci.ip.tools.jms.message.IPMessage, java.lang.String)
	 */
	@Override
	public void publish(ApplicationMessage<? extends Serializable> message, String destination) {
		
		// On verifie que la destination n'est pas nulle
		assert message != null : "DefaultMessagePublisher#publish - Veuillez renseigner le message Applicatif";
		
		// On verifie que la destination n'est pas nulle
		assert jmsTemplate != null : "DefaultMessagePublisher#publish - Veuillez renseigner le template JMS";

		// On verifie que le contexte jndi n'est pas null
		assert context != null : "DefaultMessagePublisher#publish - Veuillez renseigner le contexte JNDI";
		
		// La destination JMS
		Destination jmsDestination = null;
		
		try {
			
			// Obtention de la destination
			jmsDestination = (Destination) context.lookup(destination);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#publish - Impossible de charger la destination JMS [" + e.getMessage() + "]");
			
		}

		// Envoie du message
		jmsTemplate.send(jmsDestination, getMessageCreator(message));
	}
	
	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.publisher.IPMessagePublisher#publish(com.oci.ip.tools.jms.message.IPMessage, javax.jms.Destination)
	 */
	@Override
	public void publish(ApplicationMessage<? extends Serializable> message, Destination destination) {
		
		// On verifie que la destination n'est pas nulle
		assert message != null : "DefaultMessagePublisher#publish - Veuillez renseigner le message Applicatif";
		
		// On verifie que la destination n'est pas nulle
		assert jmsTemplate != null : "DefaultMessagePublisher#publish - Veuillez renseigner le template JMS";
		
		// Envoie du message
		jmsTemplate.send(destination, getMessageCreator(message));
	}
	
	/**
	 * Methode d'obtention du Createur de message JMS a partir du message Applicatif
	 * @param message	ApplicationMessage Applicatif
	 * @return	Createur
	 */
	protected DefaultMessageCreator getMessageCreator(ApplicationMessage<? extends Serializable> message) {
		
		// Instanciation du Createur de message
		DefaultMessageCreator messageCreator = new DefaultMessageCreator(message);
		
		// On retourne le message
		return messageCreator;
	}

	/**
	 * Methode d'obtention du champ "jmsTemplate"
	 * @return Champ "jmsTemplate"
	 */
	public JmsTemplate getJmsTemplate() {
	
		// Renvoi de la valeur du champ
		return jmsTemplate;
	}

	/**
	 * Methode de mise a jour du champ "jmsTemplate"
	 * @param jmsTemplate Nouvelle valeur du champ "jmsTemplate"
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
	
		// Mise a jour de la valeur du champ
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * Methode d'obtention du champ "context"
	 * @return Champ "context"
	 */
	public Context getContext() {
	
		// Renvoi de la valeur du champ
		return context;
	}

	/**
	 * Methode de mise a jour du champ "context"
	 * @param context Nouvelle valeur du champ "context"
	 */
	public void setContext(Context context) {
	
		// Mise a jour de la valeur du champ
		this.context = context;
	}
}
