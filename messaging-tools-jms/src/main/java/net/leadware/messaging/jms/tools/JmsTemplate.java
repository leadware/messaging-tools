package net.leadware.messaging.jms.tools;

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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import net.leadware.messaging.jms.tools.message.creator.MessageCreator;

import org.apache.log4j.Logger;


/**
 * Classe representant le template d'envoie/reception de message via JMS 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 dec. 2013 - 15:22:14
 */
public class JmsTemplate {
	
	/**
	 * Un logger
	 */
	private Logger log = Logger.getLogger(getClass());
	
	/**
	 * Constante de reception sans attente
	 */
	public static final long RECEIVE_TIMEOUT_NO_WAIT = -1;
	
	/**
	 * Constante de reception en attente bloquante et infinie
	 */
	public static final long RECEIVE_TIMEOUT_INDEFINITE_WAIT = 0;
	
	/**
	 * Fabrique de connection
	 */
	private ConnectionFactory connectionFactory = null;
	
	/**
	 * Etat transactionnel de la session
	 */
	private boolean sessionTransacted = false;
	
	/**
	 * Mode de validation de la session
	 */
	private int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	
	/**
	 * Etat d'activation des ID de messages
	 */
	private boolean messageIdEnabled = true;
	
	/**
	 * Etat d'activation des timestamps de messages
	 */
	private boolean messageTimestampEnabled = true;
	
	/**
	 * Mode de livraison du message
	 */
	private int deliveryMode = Message.DEFAULT_DELIVERY_MODE;
	
	/**
	 * Priorite du message
	 */
	private int priority = Message.DEFAULT_PRIORITY;
	
	/**
	 * Duree de vie du message
	 */
	private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
	
	/**
	 * Timeout de reception
	 */
	private long receiveTimeout = RECEIVE_TIMEOUT_INDEFINITE_WAIT;
	
	/**
	 * Nom d'utilisateur
	 */
	private String userName;
	
	/**
	 * Mot de passe
	 */
	private String password;
	
	/**
	 * Etat de securite active
	 */
	private boolean securityEnabled = false;
	
	/**
	 * Constructeur par defaut
	 */
	public JmsTemplate() {}
	
	/**
	 * Constructeur avec initialisation de parametres
	 * @param connectionFactory	Fabrique de connection 
	 */
	public JmsTemplate(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	/**
	 * Methode d'obtention du champ "connectionFactory"
	 * @return Champ "connectionFactory"
	 */
	public ConnectionFactory getConnectionFactory() {
	
		// Renvoi de la valeur du champ
		return connectionFactory;
	}

	/**
	 * Methode de mise a jour du champ "connectionFactory"
	 * @param connectionFactory Nouvelle valeur du champ "connectionFactory"
	 */
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		
		// On verifie que la fabrique n'est pas nulle
		assert connectionFactory != null : "Jmstemplate#setConnectionFactory - Veuillez renseigner la Fabrique de connection";
		
		// Mise a jour de la valeur du champ
		this.connectionFactory = connectionFactory;
	}
	
	/**
	 * Methode d'obtention du champ "sessionTransacted"
	 * @return Champ "sessionTransacted"
	 */
	public boolean isSessionTransacted() {
	
		// Renvoi de la valeur du champ
		return sessionTransacted;
	}

	/**
	 * Methode de mise a jour du champ "sessionTransacted"
	 * @param sessionTransacted Nouvelle valeur du champ "sessionTransacted"
	 */
	public void setSessionTransacted(boolean sessionTransacted) {
	
		// Mise a jour de la valeur du champ
		this.sessionTransacted = sessionTransacted;
	}

	/**
	 * Methode d'obtention du champ "acknowledgeMode"
	 * @return Champ "acknowledgeMode"
	 */
	public int getAcknowledgeMode() {
	
		// Renvoi de la valeur du champ
		return acknowledgeMode;
	}

	/**
	 * Methode de mise a jour du champ "acknowledgeMode"
	 * @param acknowledgeMode Nouvelle valeur du champ "acknowledgeMode"
	 */
	public void setAcknowledgeMode(int acknowledgeMode) {
	
		// Mise a jour de la valeur du champ
		this.acknowledgeMode = acknowledgeMode;
	}
	
	/**
	 * Methode d'obtention du champ "messageIdEnabled"
	 * @return Champ "messageIdEnabled"
	 */
	public boolean isMessageIdEnabled() {
	
		// Renvoi de la valeur du champ
		return messageIdEnabled;
	}

	/**
	 * Methode de mise a jour du champ "messageIdEnabled"
	 * @param messageIdEnabled Nouvelle valeur du champ "messageIdEnabled"
	 */
	public void setMessageIdEnabled(boolean messageIdEnabled) {
	
		// Mise a jour de la valeur du champ
		this.messageIdEnabled = messageIdEnabled;
	}

	/**
	 * Methode d'obtention du champ "messageTimestampEnabled"
	 * @return Champ "messageTimestampEnabled"
	 */
	public boolean isMessageTimestampEnabled() {
	
		// Renvoi de la valeur du champ
		return messageTimestampEnabled;
	}

	/**
	 * Methode de mise a jour du champ "messageTimestampEnabled"
	 * @param messageTimestampEnabled Nouvelle valeur du champ "messageTimestampEnabled"
	 */
	public void setMessageTimestampEnabled(boolean messageTimestampEnabled) {
	
		// Mise a jour de la valeur du champ
		this.messageTimestampEnabled = messageTimestampEnabled;
	}
	
	/**
	 * Methode d'obtention du champ "deliveryMode"
	 * @return Champ "deliveryMode"
	 */
	public int getDeliveryMode() {
	
		// Renvoi de la valeur du champ
		return deliveryMode;
	}

	/**
	 * Methode de mise a jour du champ "deliveryMode"
	 * @param deliveryMode Nouvelle valeur du champ "deliveryMode"
	 */
	public void setDeliveryMode(int deliveryMode) {
	
		// Mise a jour de la valeur du champ
		this.deliveryMode = deliveryMode;
	}

	/**
	 * Methode d'obtention du champ "priority"
	 * @return Champ "priority"
	 */
	public int getPriority() {
	
		// Renvoi de la valeur du champ
		return priority;
	}

	/**
	 * Methode de mise a jour du champ "priority"
	 * @param priority Nouvelle valeur du champ "priority"
	 */
	public void setPriority(int priority) {
	
		// Mise a jour de la valeur du champ
		this.priority = priority;
	}

	/**
	 * Methode d'obtention du champ "timeToLive"
	 * @return Champ "timeToLive"
	 */
	public long getTimeToLive() {
	
		// Renvoi de la valeur du champ
		return timeToLive;
	}

	/**
	 * Methode de mise a jour du champ "timeToLive"
	 * @param timeToLive Nouvelle valeur du champ "timeToLive"
	 */
	public void setTimeToLive(long timeToLive) {
	
		// Mise a jour de la valeur du champ
		this.timeToLive = timeToLive;
	}
	
	/**
	 * Methode d'obtention du champ "receiveTimeout"
	 * @return Champ "receiveTimeout"
	 */
	public long getReceiveTimeout() {
	
		// Renvoi de la valeur du champ
		return receiveTimeout;
	}

	/**
	 * Methode de mise a jour du champ "receiveTimeout"
	 * @param receiveTimeout Nouvelle valeur du champ "receiveTimeout"
	 */
	public void setReceiveTimeout(long receiveTimeout) {
	
		// Mise a jour de la valeur du champ
		this.receiveTimeout = receiveTimeout;
	}
	
	/**
	 * Methode permettant d'obtenir la valeur du champ "userName"
	 * @return valeur du champ "userName"
	 */
	public String getUserName() {
	
		// On retourne la valeur du champ
		return userName;
	}

	/**
	 * Methode permettant de modifier la valeur du champ "userName"
	 * @param userName Nouvelle valeur du champ "userName"
	 */
	public void setUserName(String userName) {
	
		// Mise a jour de la valeur du champ
		this.userName = userName;
	}

	/**
	 * Methode permettant d'obtenir la valeur du champ "password"
	 * @return valeur du champ "password"
	 */
	public String getPassword() {
	
		// On retourne la valeur du champ
		return password;
	}

	/**
	 * Methode permettant de modifier la valeur du champ "password"
	 * @param password Nouvelle valeur du champ "password"
	 */
	public void setPassword(String password) {
	
		// Mise a jour de la valeur du champ
		this.password = password;
	}

	/**
	 * Methode permettant d'obtenir la valeur du champ "securityEnabled"
	 * @return valeur du champ "securityEnabled"
	 */
	public boolean isSecurityEnabled() {
	
		// On retourne la valeur du champ
		return securityEnabled;
	}

	/**
	 * Methode permettant de modifier la valeur du champ "securityEnabled"
	 * @param securityEnabled Nouvelle valeur du champ "securityEnabled"
	 */
	public void setSecurityEnabled(boolean securityEnabled) {
	
		// Mise a jour de la valeur du champ
		this.securityEnabled = securityEnabled;
	}

	/**
	 * Methode de creation d'une connection
	 * @return	Connection creee
	 * @throws JMSException Exception potentielle
	 */
	public Connection createConnection() throws JMSException {
		
		// Si la securite est activee
		if(securityEnabled) return connectionFactory.createConnection(userName, password);
		
		// Sinon
		return connectionFactory.createConnection();
	}
	
	/**
	 * Methode de creation d'une session
	 * @param connection	Connection sur laquelle on cree la session
	 * @return	Session creee
	 * @throws JMSException Eception potentielle
	 */
	public Session createSession(Connection connection) throws JMSException {

		// On verifie que la connection n'est pas nulle
		assert connection != null : "JmsTemplate#createSession - Veuillez renseigner la connection";
		
		// On retourne la session
		return connection.createSession(isSessionTransacted(), getAcknowledgeMode());
	}
	
	/**
	 * Methode d'envoie d'un message sur une destination donnee
	 * @param destination	Destination d'envoie
	 * @param messageCreator	Createur de message
	 */
	public void send(Destination destination, MessageCreator messageCreator) {
		
		// Un log
		log.debug("#send");
		
		// On verifie que la destination n'est pas nulle
		assert destination != null : "JmsTemplate#send - Veuillez renseigner la destination";
		
		// On verifie que le createur de message n'est pas nul
		assert messageCreator != null : "JmsTemplate#send - Veuillez renseigner le createur de message";
		
		// Connection JMS
		Connection connection = null;
		
		try {

			// Un log
			log.debug("#send - Creation d'une connection JMS");
			
			// Creation d'une connection
			connection = createConnection();
			
		} catch (JMSException e) {
			
			// Un log
			log.debug("#send - Erreur lors de l'envoie du message : Impossible de creer une connection JMS [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(null, null, null, connection);
			
			// On retlance
			throw new RuntimeException("#send - Erreur lors de l'envoie du message : Impossible de creer une connection JMS", e);
		}

		//  Session JMS
		Session session = null;
		
		try {

			// Un log
			log.debug("#send - Creation d'une session JMS");
			
			// Creation d'une session
			session = createSession(connection);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#send - Erreur lors de l'envoie du message : Impossible de creer une session JMS [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(null, null, session, connection);
			
			// On retlance
			throw new RuntimeException("#send - Erreur lors de l'envoie du message : Impossible de creer une session JMS", e);
		}
		
		// Producteur JMS
		MessageProducer messageProducer = null;
		
		// Un log
		log.debug("#send - Creation d'un producteur JMS");
		
		try {

			// Creation du producteur
			messageProducer = createProducer(session, destination);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#send - Erreur lors de l'envoie du message : Impossible de creer un producteur JMS [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(messageProducer, null, session, connection);
			
			// On retlance
			throw new RuntimeException("#send - Erreur lors de l'envoie du message : Impossible de creer un producteur JMS", e);
		}
		
		// Un log
		log.debug("#send - Envoie du message JMS");
		
		try {
			
			// Envoie
			messageProducer.send(messageCreator.createMessage(session), deliveryMode, priority, timeToLive);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#send - Erreur lors de l'envoie du message [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(messageProducer, null, session, connection);
			
			// On retlance
			throw new RuntimeException("#send - Erreur lors de l'envoie du message ", e);
		}
		
		// Liberation des ressources
		releaseResources(messageProducer, null, session, connection);
	}

	/**
	 * Methode de reception d'un message
	 * @param destination	Destination de reception
	 * @param messageSelector Selecteur de message
	 * @return	message recu
	 */
	public Message receive(Destination destination, String messageSelector) {
		
		// Un log
		log.debug("#receive");
		
		// On verifie que la destination n'est pas nulle
		assert destination != null : "JmsTemplate#receive - Veuillez renseigner la destination";

		// Connection JMS
		Connection connection = null;
		
		try {

			// Un log
			log.debug("#receive - Creation d'une connection JMS");
			
			// Creation d'une connection
			connection = createConnection();
			
		} catch (JMSException e) {
			
			// Un log
			log.debug("#receive - Erreur lors de la reception du message : Impossible de creer une connection JMS [" + e.getMessage() + "]");

			// Liberation des ressources
			releaseResources(null, null, null, connection);
			
			// On retlance
			throw new RuntimeException("#receive - Erreur lors de la reception du message : Impossible de creer une connection JMS", e);
		}

		try {

			// Un log
			log.debug("#receive - Demarrage de la connection JMS");

			// Demarrage de la connection
			connection.start();
			
		} catch (JMSException e) {
			
			// Un log
			log.debug("#receive - Erreur lors de la reception du message : Impossible de demarrer la connection JMS [" + e.getMessage() + "]");

			// Liberation des ressources
			releaseResources(null, null, null, connection);
			
			// On retlance
			throw new RuntimeException("#receive - Erreur lors de la reception du message : Impossible de demarrer une connection JMS", e);
		}
		
		//  Session JMS
		Session session = null;
		
		try {

			// Un log
			log.debug("#receive - Creation d'une session JMS");
			
			// Creation d'une session
			session = createSession(connection);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#receive - Erreur lors de la reception du message : Impossible de creer une session JMS [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(null, null, session, connection);
			
			// On retlance
			throw new RuntimeException("#receive - Erreur lors de la reception du message : Impossible de creer une session JMS", e);
		}

		// Consomateur JMS
		MessageConsumer messageConsumer = null;
		
		// Un log
		log.debug("#receive - Creation d'un consommateur JMS");
		
		try {

			// Creation du consommateur
			messageConsumer = createMessageConsumer(session, destination, messageSelector);
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#receive - Erreur lors de la reception du message : Impossible de creer un consommateur JMS [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(null, messageConsumer, session, connection);
			
			// On retlance
			throw new RuntimeException("#send - Erreur lors de l'envoie du message : Impossible de creer un producteur JMS", e);
		}
		
		// Un log
		log.debug("#receive - Reception du message JMS");
		
		// ApplicationMessage
		Message message = null;
		
		try {
			
			// Si il n'ya pas d'attente
			if(this.receiveTimeout == RECEIVE_TIMEOUT_NO_WAIT) {
				
				// reception sans attente
				message = messageConsumer.receiveNoWait();
				
			}
			
			// Si il y a une attente non infinie
			else if(this.receiveTimeout > 0) {
				
				// reception avec attente
				message = messageConsumer.receive(this.receiveTimeout);
				
			}
			
			// Si c'est une attente infinie
			else {
				
				// reception sans attente
				message = messageConsumer.receive();
				
			}
			
		} catch (Exception e) {
			
			// Un log
			log.debug("#send - Erreur lors de la reception du message [" + e.getMessage() + "]");
			
			// Liberation des ressources
			releaseResources(null, messageConsumer, session, connection);
			
			// On relance
			throw new RuntimeException("#send - Erreur lors de la reception du message ", e);
		}
		
		// Liberation des ressources
		releaseResources(null, messageConsumer, session, connection);
		
		// On retourne le message
		return message;
	}
	
	/**
	 * Methode de reception d'un message
	 * @param destination	Destination de reception
	 * @return	message recu
	 */
	public Message receive(Destination destination) {
		
		// Appel sous-jacent
		return receive(destination, null);
	}
	
	/**
	 * Methode de reception asynchrone d'un message
	 * @param destination destination d'ecoute
	 * @param messageListener	Listener
	 */
	public AsyncReceive listen(Destination destination, MessageListener messageListener) {
		
		// Appel sous-jacent
		return listen(destination, messageListener, null);
	}

	/**
	 * Methode de reception asynchrone d'un message
	 * @param destination destination d'ecoute
	 * @param messageListener	Listener
	 * @param messageSelector Selecteur de message
	 */
	public AsyncReceive listen(Destination destination, MessageListener messageListener, String messageSelector) {

		// Un log
		log.debug("#listen");
		
		// On verifie que la destination n'est pas nulle
		assert destination != null : "JmsTemplate#listen - Veuillez renseigner la destination";

		// On verifie que le listener n'est pas null
		assert messageListener != null : "JmsTemplate#listen - Veuillez renseigner le listener";
		
		// Ecouteur asynchrone
		AsyncReceive asyncReceive = new AsyncReceive(destination, messageListener, messageSelector);
		
		// Demarrage
		asyncReceive.start();
		
		// On retourne le receveur asynchrone
		return asyncReceive;
	}
	
	/**
	 * Methode permettant de creer un producteur sur une destination donnee et pour une session donnee
	 * @param session	Session
	 * @param destination	destination
	 * @return	Producteur de message
	 * @throws JMSException Exception potentielle
	 */
	private MessageProducer createProducer(Session session, Destination destination) throws JMSException {
		
		// Un log
		log.debug("#createProducer");
		
		// Creation du producteur
		MessageProducer messageProducer = session.createProducer(destination);

		// Un log
		log.debug("#createProducer - Positionnement d l'etat d'activation des IDs de messages");
		
		// Positionnement de l'etat d'activation des ID de messages
		messageProducer.setDisableMessageID(!isMessageIdEnabled());
		
		// Un log
		log.debug("#createProducer - Positionnement d l'etat d'activation des timestamps de messages");
		
		// Positionnement de l'etat d'activation des timestamps de message
		messageProducer.setDisableMessageTimestamp(!isMessageTimestampEnabled());
		
		// On retourne le producteur
		return messageProducer;
	}
	
	/**
	 * Methode de creation d'un consommateur JMS
	 * @param session	Session en cours
	 * @param destination	Destination d'ecoute
	 * @param messageSelector Selecteur de message
	 * @return	Consomateur
	 * @throws JMSException	Exception potentielle
	 */
	private MessageConsumer createMessageConsumer(Session session, Destination destination, String messageSelector) throws JMSException  {

		// Un log
		log.debug("#createMessageConsumer");

		// Consomateur
		MessageConsumer messageConsumer = null;
		
		// Si le selecteur est renseigne
		if(messageSelector != null && !messageSelector.trim().isEmpty()) {

			// Un log
			log.debug("#createMessageConsumer - Creation d'un consommateur avec selecteur de message");
			
			// Creation du consomateur
			messageConsumer = session.createConsumer(destination, messageSelector.trim());
			
		} else {

			// Un log
			log.debug("#createMessageConsumer - Creation d'un consommateur sans selecteur de message");
			
			// Creation du consomateur
			messageConsumer = session.createConsumer(destination);
			
		}
		
		// On retourne le consomateur
		return messageConsumer;
	}
	
	/**
	 * Methode de fermeture d'un Producteur
	 * @param messageProducer	Producteur a fermer
	 */
	private synchronized void releaseResources(MessageProducer messageProducer, MessageConsumer messageConsumer, Session session, Connection connection) {
		
		try {

			// Un log
			log.debug(Thread.currentThread().getId() + "#releaseResources - Consommateur de message");
			
			// Si le consommateur est null
			if(messageConsumer == null) {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Le consommateur est null");
				
			} else {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Femeture du consommateur");
				
				// Annulation du listener
				messageConsumer.setMessageListener(null);
				
				// Fermerture
				messageConsumer.close();
				
			}
			
		} catch (Exception e) {

			// Un log
			log.debug("#releaseResources - Impossible de fermer le consommateur JMS [" + e.getMessage() + "]");
		}
		
		try {

			// Un log
			log.debug(Thread.currentThread().getId() + "#releaseResources - Producteur de message");
			
			// Si le producteur est null
			if(messageProducer == null) {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Le producteur est null");
				
			} else {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Femeture du producteur");
				
				// Fermerture
				messageProducer.close();
				
			}

		} catch (Exception e) {

			// Un log
			log.debug("#releaseResources - Impossible de fermer le producteur JMS [" + e.getMessage() + "]");
		}
		
		try {

			// Un log
			log.debug(Thread.currentThread().getId() + "#releaseResources - Session JMS");
			
			// Si le producteur est null
			if(session == null) {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - La session est nulle");
				
			} else {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Femeture de la session");
				
				// Fermerture
				session.close();
				
			}

		} catch (JMSException e) {

			// Un log
			log.debug("#releaseResources - Impossible de fermer la session JMS [" + e.getMessage() + "]");
		}

		try {

			// Un log
			log.debug(Thread.currentThread().getId() + "#releaseResources - Connection JMS");
			
			// Si le producteur est null
			if(connection == null) {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - La connection est nulle");
				
			} else {

				// Un log
				log.debug(Thread.currentThread().getId() + "#releaseResources - Femeture de la connection");
				
				// Fermerture
				connection.close();
				
			}

		} catch (JMSException e) {

			// Un log
			log.debug("#releaseResources - Impossible de fermer la connection JMS [" + e.getMessage() + "]");
		}
	}
	
	/**
	 * Classe de reception asynchrone de message
	 * @author <a href="mailto:jetune@yahoo.fr">Jean-Jacques ETUNE NGI</a>
	 * @since 31 dec. 2012 - 22:02:08
	 */
	public class AsyncReceive extends Thread {

		/**
		 * Un logger
		 */
		private Logger log = Logger.getLogger(getClass());
		
		/**
		 * Connection JMS
		 */
		private Connection connection;
		
		/**
		 * Session JMS
		 */
		private Session session;
		
		/**
		 * Destination d'ecoute
		 */
		private Destination destination;

		/**
		 * Consommateur de message
		 */
		private MessageConsumer messageConsumer;
		
		/**
		 * Ecouteur asynchrone
		 */
		private MessageListener messageListener;
		
		/**
		 * Selecteur de message
		 */
		private String messageSelector = null;
		
		/**
		 * Constructeur avec initialisation de parametre
		 * @param connectionFactory	Fabrique de connection JMS
		 * @param destination Destination d'ecoute
		 * @param messageListener Ecouteur asynchrone
		 */
		public AsyncReceive(Destination destination, MessageListener messageListener) {
			
			// Initialisation de la destination d'ecoute
			this.destination = destination;
			
			// Initialisation de l'ecouteur
			this.messageListener = messageListener;
		}

		/**
		 * Constructeur avec initialisation de parametre
		 * @param destination Destination d'ecoute
		 * @param messageListener Ecouteur asynchrone
		 * @param messageSelector Selecteur de message
		 */
		public AsyncReceive(Destination destination, MessageListener messageListener, String messageSelector) {
			
			// Initialisation de la destination d'ecoute
			this.destination = destination;
			
			// Initialisation de l'ecouteur
			this.messageListener = messageListener;
			
			// Positionnement du selecteur de message
			this.messageSelector = messageSelector;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
			// Un log
			log.debug("#run");
			
			try {

				// Un log
				log.debug("#run - Creation d'une connection JMS");
				
				// Creation d'une connection
				connection = createConnection();
				
			} catch (JMSException e) {
				
				// Un log
				log.debug("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer une connection JMS [" + e.getMessage() + "]");

				// Liberation des ressources
				releaseResources(null, null, null, connection);
				
				// On retlance
				throw new RuntimeException("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer une connection JMS", e);
			}

			try {

				// Un log
				log.debug("#run - Demarrage de la connection JMS");

				// Demarrage de la connection
				connection.start();
				
			} catch (JMSException e) {
				
				// Un log
				log.debug("#run - Erreur lors de l'ecoute asynchrone : Impossible de demarrer la connection JMS [" + e.getMessage() + "]");

				// Liberation des ressources
				releaseResources(null, null, null, connection);
				
				// On retlance
				throw new RuntimeException("#run - Erreur lors de l'ecoute asynchrone : Impossible de demarrer une connection JMS", e);
			}
			
			try {

				// Un log
				log.debug("#run - Creation d'une session JMS");
				
				// Creation d'une session
				session = createSession(connection);
				
			} catch (Exception e) {
				
				// Un log
				log.debug("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer une session JMS [" + e.getMessage() + "]");
				
				// Liberation des ressources
				releaseResources(null, null, session, connection);
				
				// On retlance
				throw new RuntimeException("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer une session JMS", e);
			}
			
			// Un log
			log.debug("#run - Creation d'un consommateur JMS");
			
			try {

				// Creation du consommateur
				messageConsumer = createMessageConsumer(session, destination, messageSelector);
				
			} catch (Exception e) {
				
				// Un log
				log.debug("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer un consommateur JMS [" + e.getMessage() + "]");
				
				// Liberation des ressources
				releaseResources(null, messageConsumer, session, connection);
				
				// On retlance
				throw new RuntimeException("#run - Erreur lors de l'ecoute asynchrone : Impossible de creer un producteur JMS", e);
			}

			try {

				// Un log
				log.debug("#run - Positionnement du listener");
				
				// Positionnement du listener
				messageConsumer.setMessageListener(messageListener);
				
			} catch (Exception e) {
				
				// Un log
				log.debug("#run - Erreur lors de l'ecoute asynchrone : Impossible de positionner le listener JMS [" + e.getMessage() + "]");
				
				// Liberation des ressources
				releaseResources(null, messageConsumer, session, connection);
				
				// On retlance
				throw new RuntimeException("#run - Erreur lors de l'ecoute asynchrone : Impossible de positionner le listener JMS", e);
			}
		}
		
		/**
		 * Methode de liberation des resources
		 */
		public void stopListen() {
			
			// Liberation des ressources
			releaseResources(null, messageConsumer, session, connection);
		}
	}

}
