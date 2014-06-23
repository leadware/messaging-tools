package net.leadware.messaging.jms.tools.message.consumer;

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

import javax.jms.Destination;

import net.leadware.messaging.jms.tools.JmsTemplate.AsyncReceive;
import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.consumer.listener.ApplicationMessageListener;


/**
 * Classe l'interface du consomateur de message JMS Applicatif
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 d�c. 2013 - 15:37:00
 */
public interface MessageConsumer {
	
	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Destination de reception
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(Destination destination);
	
	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Nom JNDI de la Destination de reception
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(String destination);

	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Destination de reception
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(Destination destination, String messageSelector);
	
	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Destination de reception
	 * @param timeout
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(Destination destination, long timeout);

	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Destination de reception
	 * @param timeout
	 * @param messageSelector Selecteur de message
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(Destination destination, long timeout, String messageSelector);
	
	/**
	 * Methode de reception synchrone de message sur une destination donnee
	 * @param destination	Nom JNDI de la Destination de reception
	 * @param timeout
	 * @return	ApplicationMessage Applicatif recu
	 */
	public ApplicationMessage<? extends Serializable> receive(String destination, long timeout);

	/**
	 * Methode de reception asynchrone d'un message Applicatif
	 * @param destination	Destination d'ecoute
	 * @param messageListener	Listener de message Applicatif
	 * @param messageSelector Selecteur de message
	 * @return Thread de reception asynchrone
	 */
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener, String messageSelector);
	
	/**
	 * Methode de reception asynchrone d'un message Applicatif
	 * @param destination	Destination d'ecoute
	 * @param messageListener	Listener de message Applicatif
	 * @return Thread de reception asynchrone
	 */
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener);
	
	/**
	 * Methode de reception asynchrone d'un message Applicatif
	 * @param destination	Nom JNDI de la Destination d'ecoute
	 * @param messageListener	Listener de message Applicatif
	 * @return Thread de reception asynchrone
	 */
	public AsyncReceive listen(String destination, ApplicationMessageListener messageListener);

	/**
	 * Methode de reception asynchrone d'un message Applicatif
	 * @param destination	Destination d'ecoute
	 * @param messageListener	Listener de message Applicatif
	 * @param selectorProperties Map des proprietes de selection
	 * @return Thread de reception asynchrone
	 */
	public AsyncReceive listen(Destination destination, ApplicationMessageListener messageListener, Map<String, String> selectorProperties);
	
	/**
	 * Methode de reception asynchrone d'un message Applicatif
	 * @param destination	Nom JNDI de la Destination d'ecoute
	 * @param messageListener	Listener de message Applicatif
	 * @param selectorProperties Map des proprietes de selection
	 * @return Thread de reception asynchrone
	 */
	public AsyncReceive listen(String destination, ApplicationMessageListener messageListener, Map<String, String> selectorProperties);
	
	/**
	 * Methode de mise a jour du nom d'utilisateur 
	 * @param userName	Nom d'utilisateur
	 */
	public void setUserName(String userName);

	/**
	 * Methode de mise a jour du nom d'utilisateur 
	 * @param userName	Nom d'utilisateur
	 */
	public void setPassword(String password);
	
	/**
	 * Methode permettant de modifier la valeur du champ "securityEnabled"
	 * @param securityEnabled Nouvelle valeur du champ "securityEnabled"
	 */
	public void setSecurityEnabled(boolean securityEnabled);
}
