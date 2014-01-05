package net.leadware.messaging.jms.tools.message.creator.impl;

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
import javax.jms.ObjectMessage;
import javax.jms.Session;

import net.leadware.messaging.jms.tools.constant.JmsConstant;
import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.creator.MessageCreator;


/**
 * Classe representant un createur de message JMS 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:50:17
 */
public class DefaultMessageCreator implements MessageCreator {
	
	/**
	 * ApplicationMessage a creer
	 */
	private ApplicationMessage<? extends Serializable> message;
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param message	message Applicatif
	 */
	public DefaultMessageCreator(ApplicationMessage<? extends Serializable> message) {
		
		// Sauvegarde du message
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.message.creator.MessageCreator#createMessage(javax.jms.Session)
	 */
	@Override
	public Message createMessage(Session session) throws JMSException {
		
		// ApplicationMessage Objet
		ObjectMessage objectMessage = session.createObjectMessage(message);

		// On positionne la propriete de message Applicatif
		objectMessage.setStringProperty(JmsConstant.APP_JMS_MESSAGE_ID_PROPERTY, JmsConstant.APP_JMS_MESSAGE_ID_VALUE);
		
		// On positionne la propriete de message Applicatif
		objectMessage.setStringProperty(JmsConstant.APP_JMS_MESSAGE_TYPE, message.getType().name());
		
		// On retourne le message
		return objectMessage;
	}

}
