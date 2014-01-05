package net.leadware.messaging.jms.tools.message.consumer.listener;

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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.leadware.messaging.jms.tools.message.ApplicationMessage;

import org.apache.log4j.Logger;


/**
 * Classe representant un listener JMS 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:40:48
 */
public class JMSListener implements MessageListener {

	/**
	 * Un logger
	 */
	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * Listener de message Applicatif
	 */
	private ApplicationMessageListener messageListener;
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param messageListener	Listener de message
	 */
	public JMSListener(ApplicationMessageListener messageListener) {
		
		// Initialisation du listener
		this.messageListener = messageListener;
	}
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		
		// Un log
		log.debug("#onMessage");
		
		// Si le message est null
		if(message == null) {

			// Un log
			log.debug("#onMessage - ApplicationMessage null");
			
			// On sort
			return;
		}
		
		// Si le message n'est pas un ObjectMessage
		if(!(message instanceof ObjectMessage)) {
			
			// Un log
			log.debug("#onMessage - Le Message n'est pas du type traite par le listener");
			
			// On sort
			return;
		}
		
		// On recupere l'objet
		Object extractedObject = null;
		
		try {
			
			// On recupere l'objet
			extractedObject = ((ObjectMessage)message).getObject();
			
		} catch (JMSException e) {

			// Un log
			log.debug("#onMessage - Erreur lors de l'estraction de l'objet contenu dans le message objet");
			
			// On sort
			return;
		}

		// Si l'objet extrait est null
		if(extractedObject == null) {

			// Un log
			log.debug("#onMessage - L'objet contenu dans le message JMS est null");
			
			// On sort
			return;
		}
		
		// Si l'objet n'est pas un message Applicatif
		if(!(extractedObject instanceof ApplicationMessage)) {

			// Un log
			log.debug("#onMessage - Le Message n'est pas du type traite par le listener");
			
			// On sort
			return;
		}
		
		// On caste
		ApplicationMessage<? extends Serializable> ipMessage = (ApplicationMessage<?>) extractedObject;

		// Un log
		log.debug("#onMessage - Traitement du message par le listener");
		
		// Traitement du message
		messageListener.onMessage(ipMessage);
	}

}
