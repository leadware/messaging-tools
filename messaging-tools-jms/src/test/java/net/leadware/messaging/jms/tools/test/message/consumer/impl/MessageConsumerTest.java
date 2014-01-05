package net.leadware.messaging.jms.tools.test.message.consumer.impl;

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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import net.leadware.messaging.jms.tools.JmsTemplate;
import net.leadware.messaging.jms.tools.JmsTemplate.AsyncReceive;
import net.leadware.messaging.jms.tools.constant.JmsConstant;
import net.leadware.messaging.jms.tools.constant.JmsPropertyContainer;
import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.ApplicationMessageType;
import net.leadware.messaging.jms.tools.message.configuration.Configuration;
import net.leadware.messaging.jms.tools.message.consumer.impl.DefaultMessageConsumer;
import net.leadware.messaging.jms.tools.message.consumer.listener.ApplicationMessageListener;
import net.leadware.messaging.jms.tools.message.publisher.impl.DefaultMessagePublisher;
import net.leadware.messaging.jms.tools.test.message.impl.DefaultTestMessage;

import org.apache.log4j.Logger;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Classe de test du consumer Applicatif par defaut 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 d�c. 2013 - 16:35:26
 */
public class MessageConsumerTest {

	/**
	 * Un logger
	 */
	private static Logger log = Logger.getLogger(MessageConsumerTest.class);
	
	/**
	 * Instance embarquee du serveur JMS HornetQ
	 */
	private static EmbeddedJMS embeddedJms;
	
	/**
	 * Methode a executer avant toutes les methodes de test
	 * @throws java.lang.Exception	Exception potentielle
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		
		// Demarrage du serveur JMS HornetQ
		embeddedJms = new EmbeddedJMS();
		
		// Demarrage
		embeddedJms.start();
		
		// Un log
		log.debug("JMS Server started...");
	}

	/**
	 * Methode a executer apres toutes les methodes de test
	 * @throws java.lang.Exception	Exception potentielle
	 */
	@AfterClass
	public static void tearDownClass() throws Exception {
		
		// Arret
		embeddedJms.stop();
		
		// Liberation
		embeddedJms = null;
		
		// Un log
		log.debug("JMS Server stopped...");
	}
	
	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.message.consumer.impl.DefaultMessageConsumer#receive(javax.jms.Destination, long)}.
	 * @throws Exception Exception potentielle
	 */
	//@Test
	public void testReceiveSync() throws Exception {
		
		// message a envoyer
		final String message = "ApplicationMessage To Send";
		
		// ApplicationMessage recu par le le consomateur
		final StringBuffer receivedMessage = new StringBuffer();
		
		// Obtention de la configuration
		Configuration configuration = new Configuration();
		
		// Lookup de la fabrique de connexion
		ConnectionFactory connectionFactory = (ConnectionFactory) embeddedJms.lookup(configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
		
		// Verifions que lq fabrique n'est pas nulle
		assertNotNull(connectionFactory);
		
		// Obtention de la destination
		final Topic destination = (Topic) embeddedJms.lookup("topic/ip-administration");
		
		// Verifions que lq destination n'est pas nulle
		assertNotNull(destination);
		
		// Instanciation d'un publisher Applicatif
		DefaultMessagePublisher defaultIPMessagePublisher = new DefaultMessagePublisher(connectionFactory);
		
		// Consomateur
		final DefaultMessageConsumer defaultIPMessageConsumer = new DefaultMessageConsumer(connectionFactory);
		
		// Un Runnable d'ecoute infini
		Thread infiniteReceiveThread = new Thread() {
			
			@Override
			public void run() {
				
				// Conteneur de Proprietes
				JmsPropertyContainer<String> propertyContainer = JmsPropertyContainer.<String>newInstance();
				
				// Ajout des ppt
				propertyContainer.add(JmsConstant.APP_JMS_MESSAGE_TYPE, ApplicationMessageType.DEFAULT_MESSAGE_TYPE.name());
				
				// Selecteur
				String messageSelector = JmsConstant.buildMessageSelector(propertyContainer.getProperties());
				
				// Reception
				ApplicationMessage<? extends Serializable> ipMessage = defaultIPMessageConsumer.receive(destination, JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT, messageSelector);
				
				// Ajout du payload
				receivedMessage.append(ipMessage.getPayLoad());
			}
		};
		
		// Demarrage du Thread
		infiniteReceiveThread.start();

		// Attente
		Thread.sleep(3000);
		
		// Envoie du message
		defaultIPMessagePublisher.publish(new DefaultTestMessage(message), destination);
		
		// Attente
		Thread.sleep(3000);
		
		// Verification du message reçu par le consomateur
		assertTrue(message.equals(receivedMessage.toString()));
	}

	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.message.consumer.impl.DefaultMessageConsumer#listen(javax.jms.Destination, net.leadware.messaging.jms.tools.message.consumer.listener.ApplicationMessageListener)}.
	 * @throws Exception Exception potentielle
	 */
	@Test
	public void testListenAsync() throws Exception {

		// message a envoyer
		final String message = "ApplicationMessage To Send";
		
		// ApplicationMessage recu par le le consomateur
		final StringBuffer receivedMessage = new StringBuffer();
		
		// Obtention de la configuration
		Configuration configuration = new Configuration();
		
		// Lookup de la fabrique de connexion
		ConnectionFactory connectionFactory = (ConnectionFactory) embeddedJms.lookup(configuration.getProperty(Configuration.CONNECTION_FACTORY_NAME));
		
		// Verifions que lq fabrique n'est pas nulle
		assertNotNull(connectionFactory);
		
		// Obtention de la destination
		final Topic destination = (Topic) embeddedJms.lookup("topic/ip-administration");
		
		// Verifions que lq destination n'est pas nulle
		assertNotNull(destination);
		
		// Instanciation d'un publisher Applicatif
		DefaultMessagePublisher defaultIPMessagePublisher = new DefaultMessagePublisher(connectionFactory);
		
		// Consomateur
		final DefaultMessageConsumer defaultIPMessageConsumer = new DefaultMessageConsumer(connectionFactory);

		// Conteneur de Proprietes
		JmsPropertyContainer<String> propertyContainer = JmsPropertyContainer.<String>newInstance();
		
		// Ajout des ppt
		propertyContainer.add(JmsConstant.APP_JMS_MESSAGE_TYPE, ApplicationMessageType.DEFAULT_MESSAGE_TYPE.name());
		
		// Selecteur
		String messageSelector = JmsConstant.buildMessageSelector(propertyContainer.getProperties());
		
		// Ajout du listener
		AsyncReceive asyncReceive = defaultIPMessageConsumer.listen(destination, new ApplicationMessageListener() {
			
			@Override
			public void onMessage(ApplicationMessage<? extends Serializable> message) {
				
				// Ajout du payload
				receivedMessage.append(message.getPayLoad());
			}
		}, messageSelector);

		// Attente
		Thread.sleep(3000);
		
		// Envoie du message
		defaultIPMessagePublisher.publish(new DefaultTestMessage(message), destination);
		
		// Attente
		Thread.sleep(3000);
		
		// Arret du listening
		asyncReceive.stopListen();
		
		// Verification du message reçu par le consomateur
		assertTrue("ApplicationMessage send is not receive: SEND [" + message + "], RECEIVE [" + receivedMessage.toString() + "]", message.equals(receivedMessage.toString()));
	}

}
