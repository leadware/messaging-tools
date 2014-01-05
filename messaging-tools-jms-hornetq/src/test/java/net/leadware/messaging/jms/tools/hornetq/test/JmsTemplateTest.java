package net.leadware.messaging.jms.tools.hornetq.test;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import net.leadware.messaging.jms.tools.JmsTemplate;
import net.leadware.messaging.jms.tools.JmsTemplate.AsyncReceive;
import net.leadware.messaging.jms.tools.hornetq.factory.HornetQNettyConnectionFactoryClientBuilder;
import net.leadware.messaging.jms.tools.message.creator.MessageCreator;

import org.apache.log4j.Logger;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Classe de test du template jms
 * @author <a href="mailto:jetune@yahoo.fr">Jean-Jacques ETUNE NGI</a>
 * @since 31 déc. 2012 - 00:54:28
 */
public class JmsTemplateTest {

	/**
	 * Un logger
	 */
	private static Logger log = Logger.getLogger(JmsTemplateTest.class);
	
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
	 * Test method for {@link net.leadware.messaging.jms.tools.JmsTemplate#send(javax.jms.Destination, net.leadware.messaging.jms.tools.message.creator.MessageCreator)}.
	 * @throws Exception Exception potentielle
	 */
	@Test
	public void testSendReceiveSynch() throws Exception {
		
		// message a envoyer
		final String message = "ApplicationMessage To Send";

		// ApplicationMessage recu
		final StringBuffer receivedMessage = new StringBuffer();
		
		// Constructeur de Fabrique
		HornetQNettyConnectionFactoryClientBuilder builder = new HornetQNettyConnectionFactoryClientBuilder();
		
		// Positionnement du Host
		builder.setJmsServerHost("localhost");

		// Positionnement du Port
		builder.setJmsServerPort("1099");
		
		// Lookup de la fabrique de connexion
		ConnectionFactory connectionFactory = builder.build();

		// Verifions que lq fabrique n'est pas nulle
		assertNotNull(connectionFactory);
		
		// Obtention de la destination
		final Destination destination = (Destination) embeddedJms.lookup("queue/ip-archiving");
		
		// Verifions que lq destination n'est pas nulle
		assertNotNull(destination);
		
		// Instanciation d'un template JMS
		final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		// Positionnement du timeout de reception
		jmsTemplate.setReceiveTimeout(0);
		
		// Thread de reception
		Thread receiveThread = new Thread() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void run() {
				
				// reception du message
				ObjectMessage jmsMessage = (ObjectMessage) jmsTemplate.receive(destination);
				
				// Verification de la non nullite du message JMS
				assertNotNull(jmsMessage);
				
				// Objet contenu dans le message JMS
				Object payload = null;
				
				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					payload = jmsMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}
				
				// Affichage du message
				log.debug("ApplicationMessage Payload: " + payload);

				// Positionnement du message recu
				receivedMessage.append(payload);
			}
		};
		
		// Demarrage du Thread
		receiveThread.start();
		
		// Envoi du message
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				// Creation du message
				return session.createObjectMessage(message);
			}
		});
		
		// Attente
		Thread.sleep(3000);

		// Verifions que le message recu par le Listener est bien celui envoye par le template
		assertTrue(message.equals(receivedMessage.toString()));
	}
	
	/**
	 * Test method for {@link net.leadware.messaging.jms.tools.JmsTemplate#listen(javax.jms.Destination, javax.jms.MessageListener)}.
	 * @throws Exception Exception potentielle
	 */
	@Test
	public void testReceiveAsync() throws Exception {
		
		// message a envoyer
		final String message = "ApplicationMessage To Send";
		
		// ApplicationMessage recu
		final StringBuffer receivedMessage = new StringBuffer();

		// Constructeur de Fabrique
		HornetQNettyConnectionFactoryClientBuilder builder = new HornetQNettyConnectionFactoryClientBuilder();
		
		// Positionnement du Host
		builder.setJmsServerHost("localhost");

		// Positionnement du Port
		builder.setJmsServerPort("1099");
		
		// Lookup de la fabrique de connexion
		ConnectionFactory connectionFactory = builder.build();
		
		// Verifions que lq fabrique n'est pas nulle
		assertNotNull(connectionFactory);
		
		// Obtention de la destination
		final Destination destination = (Destination) embeddedJms.lookup("queue/ip-archiving");
		
		// Verifions que lq destination n'est pas nulle
		assertNotNull(destination);
		
		// Instanciation d'un template JMS
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		
		// Ecoute asynchrone de messages
		AsyncReceive asyncReceive = jmsTemplate.listen(destination, new MessageListener() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void onMessage(Message jmsMessage) {
				
				// On caste en ObjectMessage
				ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
				
				// Verification de la non nullite du message JMS
				assertNotNull(objectMessage);
				
				// Objet contenu dans le message JMS
				Object payload = null;

				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					payload = objectMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}
				
				// Affichage du message
				log.debug("ApplicationMessage Payload: " + payload);
				
				// Positionnement du message recu
				receivedMessage.append(payload);
			}
		});
		
		// Envoi du message
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				// Creation du message
				return session.createObjectMessage(message);
			}
		});

		// Attente
		Thread.sleep(3000);
		
		// Liberation
		asyncReceive.stopListen();
		
		// Verifions que le message recu par le Listener est bien celui envoye par le template
		assertTrue(message.equals(receivedMessage.toString()));
	}
	
	@Test
	public void testReceiveSelected() throws Exception {
		
		// Liste contenant les messages reçus par le process avec selecteur
		final List<String> listWithSelector = new ArrayList<String>();

		// Liste contenant les messages reçus par le process sans selecteur
		final List<String> listWithoutSelector = new ArrayList<String>();
		
		// message 1 a envoyer
		final String message1 = "ApplicationMessage To Send 1";

		// message 2 a envoyer
		final String message2 = "ApplicationMessage To Send 2";

		// message 2 a envoyer
		final String message3 = "ApplicationMessage To Send 3";
		
		// Nom d'une propriete du message
		final String messagePropertyName1 = "type";
		
		// Valeur de la propriete du message
		final String messagePropertyValue1 = "type1";
		
		// Selecteur de message
		final String messageSelector1 = "(" + messagePropertyName1 + "='" + messagePropertyValue1 + "')";

		// Nom d'une propriete du message
		final String messagePropertyName2 = "type";
		
		// Valeur de la propriete du message
		final String messagePropertyValue2 = "type2";
		
		// Selecteur de message
		final String messageSelector2 = "(" + messagePropertyName2 + "='" + messagePropertyValue2 + "')";

		// Constructeur de Fabrique
		HornetQNettyConnectionFactoryClientBuilder builder = new HornetQNettyConnectionFactoryClientBuilder();
		
		// Positionnement du Host
		builder.setJmsServerHost("localhost");

		// Positionnement du Port
		builder.setJmsServerPort("1099");
		
		// Lookup de la fabrique de connexion
		ConnectionFactory connectionFactory = builder.build();

		// Verifions que lq fabrique n'est pas nulle
		assertNotNull(connectionFactory);
		
		// Obtention de la destination
		final Destination destination = (Destination) embeddedJms.lookup("queue/ip-archiving");
		
		// Verifions que lq destination n'est pas nulle
		assertNotNull(destination);
		
		// Instanciation d'un template JMS
		final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);

		// Ecoute asynchrone de messages
		AsyncReceive asyncReceiveSelector1 = jmsTemplate.listen(destination, new MessageListener() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void onMessage(Message jmsMessage) {
				
				// On caste en ObjectMessage
				ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
				
				// Verification de la non nullite du message JMS
				assertNotNull(objectMessage);
				
				// Objet contenu dans le message JMS
				Object payload = null;

				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					payload = objectMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}

				// Affichage du message
				log.debug("#Selector[" + messageSelector1 + "]: " + payload);
				
				// Ajout du message
				listWithSelector.add(payload.toString());
			}
		}, messageSelector1);
		
		// Ecoute asynchrone de messages sans selecteur
		AsyncReceive asyncReceiveSelector2 = jmsTemplate.listen(destination, new MessageListener() {

			/**
			 * Un logger
			 */
			private Logger log = Logger.getLogger(getClass());
			
			@Override
			public void onMessage(Message jmsMessage) {
				
				// On caste en ObjectMessage
				ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
				
				// Verification de la non nullite du message JMS
				assertNotNull(objectMessage);
				
				// Objet contenu dans le message JMS
				Object payload = null;

				// Chargement du message contenu dans le message JMS
				try {
					
					// Extraction du payload
					payload = objectMessage.getObject();
					
				} catch (JMSException e) {
					
					// Erreur
					fail("Erreur lors de l'extraction du contenu dans le message");
				}

				// Affichage du message
				log.debug("#Selector[" + messageSelector2 + "]: " + payload);
				
				// Ajout du message
				listWithoutSelector.add(payload.toString());
			}
		}, messageSelector2);
		
		// Envoi du message 1
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				// ApplicationMessage Objet
				ObjectMessage objectMessage = session.createObjectMessage(message1);
				
				// Ajout de la ppt
				objectMessage.setStringProperty(messagePropertyName1, messagePropertyValue1);
				
				// Creation du message
				return objectMessage;
			}
		});

		// Envoi du message 2
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				// ApplicationMessage Objet
				ObjectMessage objectMessage = session.createObjectMessage(message2);
				
				// Ajout de la ppt
				objectMessage.setStringProperty(messagePropertyName2, messagePropertyValue2);
				
				// Creation du message
				return objectMessage;
			}
		});

		// Envoi du message 3
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				// ApplicationMessage Objet
				ObjectMessage objectMessage = session.createObjectMessage(message3);
				
				// Ajout de la ppt
				objectMessage.setStringProperty(messagePropertyName2, messagePropertyValue2);
				
				// Creation du message
				return objectMessage;
			}
		});
		
		// Attente
		Thread.sleep(5000);
		
		// Arret des processus d'ecoute
		asyncReceiveSelector2.stopListen();
		asyncReceiveSelector1.stopListen();
		
		// Verification
		assertEquals(1, listWithSelector.size());
		assertEquals(2, listWithoutSelector.size());
	}

}
