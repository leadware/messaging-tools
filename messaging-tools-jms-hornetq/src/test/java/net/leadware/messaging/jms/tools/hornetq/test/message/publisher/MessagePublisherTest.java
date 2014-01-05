package net.leadware.messaging.jms.tools.hornetq.test.message.publisher;

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
import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import net.leadware.messaging.jms.tools.JmsTemplate;
import net.leadware.messaging.jms.tools.JmsTemplate.AsyncReceive;
import net.leadware.messaging.jms.tools.hornetq.factory.HornetQNettyConnectionFactoryClientBuilder;
import net.leadware.messaging.jms.tools.hornetq.test.message.impl.DefaultMessage;
import net.leadware.messaging.jms.tools.message.configuration.Configuration;
import net.leadware.messaging.jms.tools.message.publisher.impl.DefaultMessagePublisher;

import org.apache.log4j.Logger;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Classe de test du publisher par defaut 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 16:40:06
 */
public class MessagePublisherTest {
	
	/**
	 * Un logger
	 */
	private static Logger log = Logger.getLogger(MessagePublisherTest.class);
	
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
	 * Test method for {@link net.leadware.messaging.jms.tools.message.publisher.impl.DefaultMessagePublisher#publish(net.leadware.messaging.jms.tools.message.ApplicationMessage, javax.jms.Destination)}.
	 */
	@Test
	public void testPublishMessageDestination() throws Exception {
		
		// message a envoyer
		final String message = "ApplicationMessage To Send";
		
		// ApplicationMessage recu par le listener 1
		final StringBuffer receivedMessage1 = new StringBuffer();

		// ApplicationMessage recu par le listener 2
		final StringBuffer receivedMessage2 = new StringBuffer();
		
		// Positionnement de l'etat de construction du transport JMS Client
		System.getProperties().put(Configuration.JMS_TRANSPORT_ON_CLIENT, "true");
		
		// Positionnement du Host Serveur
		System.getProperties().put(Configuration.JMS_SERVER_HOST, "localhost");

		// Positionnement du Port Serveur
		System.getProperties().put(Configuration.JMS_SERVER_PORT, "1099");

		// Positionnement de la classe de construction des Connections
		System.getProperties().put(Configuration.JMS_CNX_FACT_BUILDER_CLASS, 
									HornetQNettyConnectionFactoryClientBuilder.class.getName());
		
		// Configuration
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
		DefaultMessagePublisher defaultIPMessagePublisher = new DefaultMessagePublisher(configuration);

		// Instanciation d'un template JMS
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		// Un log
		log.debug("#testPublishMessageDestination - Creation du listener 1");
		
		// Ecoute asynchrone de messages sur la destination d'envoie
		AsyncReceive asyncReceive_1 = jmsTemplate.listen(destination, new MessageListener() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void onMessage(Message jmsMessage) {

				// Un log
				log.debug("#onMessage - Listener 1");
				
				// On caste en ObjectMessage
				ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
				
				// Verification de la non nullite du message JMS
				assertNotNull(objectMessage);
				
				// Objet contenu dans le message JMS
				net.leadware.messaging.jms.tools.message.ApplicationMessage<? extends Serializable> ipMessage = null;

				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					ipMessage = (net.leadware.messaging.jms.tools.message.ApplicationMessage<?>) objectMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}
				
				// Obtention du payload
				Serializable payload = ipMessage.getPayLoad();
				
				// Affichage du message
				log.debug("#Listener - 1 : ApplicationMessage Payload: " + payload);
				
				// Ajout du contenu
				receivedMessage1.append(payload);
			}
		});

		// Un log
		log.debug("#testPublishMessageDestination - Creation du listener 2");
		
		// Ecoute asynchrone de messages sur la destination d'envoie
		AsyncReceive asyncReceive_2 = jmsTemplate.listen(destination, new MessageListener() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void onMessage(Message jmsMessage) {
				
				// Un log
				log.debug("#onMessage - Listener 2");
				
				// On caste en ObjectMessage
				ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
				
				// Verification de la non nullite du message JMS
				assertNotNull(objectMessage);
				
				// Objet contenu dans le message JMS
				net.leadware.messaging.jms.tools.message.ApplicationMessage<? extends Serializable> ipMessage = null;

				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					ipMessage = (net.leadware.messaging.jms.tools.message.ApplicationMessage<?>) objectMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}
				
				// Obtention du payload
				Serializable payload = ipMessage.getPayLoad();
				
				// Affichage du message
				log.debug("#Listener - 2 : ApplicationMessage Payload: " + payload);

				// Ajout du contenu
				receivedMessage2.append(payload);
			}
		});

		// Attente
		Thread.sleep(3000);
		
		// Envoie du message
		defaultIPMessagePublisher.publish(new DefaultMessage(message), destination);
		
		// Attente
		Thread.sleep(3000);
		
		// Liberation
		asyncReceive_1.stopListen();
		asyncReceive_2.stopListen();
		
		// Verification des message recu par le listener 1
		assertTrue(message.equals(receivedMessage1.toString()));

		// Verification des message recu par le listener 2
		assertTrue(message.equals(receivedMessage2.toString()));
	}

}
