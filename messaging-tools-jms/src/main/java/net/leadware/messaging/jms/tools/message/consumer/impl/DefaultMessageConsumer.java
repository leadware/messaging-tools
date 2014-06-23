package net.leadware.messaging.jms.tools.message.consumer.impl;

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
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.Context;

import net.leadware.messaging.jms.tools.JmsTemplate;
import net.leadware.messaging.jms.tools.JmsTemplate.AsyncReceive;
import net.leadware.messaging.jms.tools.constant.JmsConstant;
import net.leadware.messaging.jms.tools.factory.ConnectionFactoryBuilderHelper;
import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.configuration.Configuration;
import net.leadware.messaging.jms.tools.message.consumer.MessageConsumer;
import net.leadware.messaging.jms.tools.message.consumer.listener.ApplicationMessageListener;
import net.leadware.messaging.jms.tools.message.consumer.listener.JMSListener;

import org.apache.log4j.Logger;


/**
 * Classe representant l'implementation par defaut du consommateur de message JMS Applicatif
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 d�c. 2013 - 15:37:33
 */
public class DefaultMessageConsumer implements MessageConsumer {

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
	public DefaultMessageConsumer() {
		
		// Instanciation du template
		jmsTemplate = new JmsTemplate();
	}
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param connectionFactory	Fabrique de connection
	 */
	public DefaultMessageConsumer(ConnectionFactory connectionFactory) {

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
	public DefaultMessageConsumer(Configuration configuration) {
		
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
	public DefaultMessageConsumer(Configuration configuration, Context context) {
		
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

	@Override
	public ApplicationMessage<? extends Serializable> receive(Destination destination, String messageSelector) {
		
		// On retourne le ApplicationMessage
		return receive(destination, JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT, messageSelector);
	}

	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#receive(javax.jms.Destination)
	 */
	@Override
	public ApplicationMessage<? extends Serializable> receive(Destination destination) {
		
		// Appel Sous-jacent
		return receive(destination, JmsConstant.buildDefaultMessageSelector());
	}

	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#receive(java.lang.String)
	 */
	@Override
	public ApplicationMessage<? extends Serializable> receive(String destination) {
		
		// Reception a partir de la destination
		return receive(lookup(destination));
	}

	@Override
	public ApplicationMessage<? extends Serializable> receive(Destination destination, long timeout, String messageSelector) {

		// On positionne le timeout
		jmsTemplate.setReceiveTimeout(timeout);
		
		// Reception du message
		Message jmsMessage = jmsTemplate.receive(destination, messageSelector);
		
		// ApplicationMessage Applicatif
		ApplicationMessage<? extends Serializable> ipMessage = null;
		
		try {

			// Obtention du message Applicatif
			ipMessage = (ApplicationMessage<?>) ((ObjectMessage)jmsMessage).getObject();
			
		} catch (JMSException e) {
			
			// Un log
			log.debug("#receive - Erreur lors de l'extraction de l'objet contenu dans le message JMS [" + e.getMessage() + "]");
			
			// On retlance
			throw new RuntimeException("#receive - Erreur lors de l'extraction de l'objet contenu dans le message JMS", e);
		}
		
		// On retourne le message Applicatif
		return ipMessage;
	}
	
	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#receive(javax.jms.Destination, int)
	 */
	@Override
	public ApplicationMessage<? extends Serializable> receive(Destination destination, long timeout) {
		
		// On retourne le message
		return receive(destination, timeout, JmsConstant.buildDefaultMessageSelector());
	}

	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#receive(java.lang.String, int)
	 */
	@Override
	public ApplicationMessage<? extends Serializable> receive(String destination, long timeout) {
		
		// Appel sous-jacent
		return receive(lookup(destination), timeout);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#listen(javax.jms.Destination, com.oci.ip.tools.jms.message.consumer.listener.IPMessageListener, java.lang.String)
	 */
	@Override
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener, String messageSelector) {
		
		// Ecoute
		return jmsTemplate.listen(destination, new JMSListener(messageListener), messageSelector);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#listen(javax.jms.Destination, com.oci.ip.tools.jms.message.consumer.listener.IPMessageListener)
	 */
	@Override
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener) {
		
		// Ecoute
		return listen(destination, messageListener, JmsConstant.buildDefaultMessageSelector());
	}

	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#listen(java.lang.String, com.oci.ip.tools.jms.message.consumer.listener.IPMessageListener)
	 */
	@Override
	public AsyncReceive listen(String destination, ApplicationMessageListener messageListener) {
		
		// Appel Sous-jacent
		return listen(lookup(destination), messageListener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#listen(javax.jms.Destination, com.oci.ip.tools.jms.message.consumer.listener.IPMessageListener, java.util.Map)
	 */
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener, Map<String, String> selectorProperties) {

		// Construction du selecteur
		String messageSelector = JmsConstant.buildMessageSelector(selectorProperties);
		
		// Ecoute
		return listen(destination, messageListener, messageSelector);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.consumer.IPMessageConsumer#listen(java.lang.String, com.oci.ip.tools.jms.message.consumer.listener.IPMessageListener, java.util.Map)
	 */
	@Override
	public AsyncReceive listen(String destination, ApplicationMessageListener messageListener, Map<String, String> selectorProperties) {
		
		// Ecoute
		return listen(lookup(destination), messageListener, selectorProperties);
	}
	
	/**
	 * Methode de lookup de la destination
	 * @param name	Nom JNDI de la destination
	 * @return
	 */
	private Destination lookup(String name) {

		// La destination JMS
		Destination destination = null;
		
		try {
			
			// Obtention de la destination
			destination = (Destination) context.lookup(name);
			
		} catch (Exception e) {
			
			// Pile d'erreur
			e.printStackTrace();
			
			// Un log
			log.debug("#lookup - Impossible de charger la destination JMS [" + e.getMessage() + "]");
			
			// On retlance
			throw new RuntimeException("#lookup - Impossible de charger la destination JMS", e);
		}
		
		// On retourne la destination
		return destination;
	}
	
}
