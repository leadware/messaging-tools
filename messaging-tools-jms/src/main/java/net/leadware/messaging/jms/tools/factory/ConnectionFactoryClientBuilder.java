/**
 * 
 */
package net.leadware.messaging.jms.tools.factory;

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

import javax.jms.ConnectionFactory;

/**
 * Interface de l'utilitaire d'initialisation de la fabrique de connexion JMS au niveau Client 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:25:16
 */
public interface ConnectionFactoryClientBuilder {

	/**
	 * Methode de mise a jour de l'Adresse du serveur JMS
	 * @param jmsServerHost Nouvelle valeur de l'Adresse du serveur JMS
	 */
	public void setJmsServerHost(String jmsServerHost);
	
	/**
	 * Methode de mise a jour du Port du serveur JMS
	 * @param jmsServerPort Nouvelle valeur du Port du serveur JMS
	 */
	public void setJmsServerPort(String jmsServerPort);

	/**
	 * Methode de construction de la fabrique de connexion JMS
	 * @return	Fabrique de connexion JMS
	 */
	public ConnectionFactory build();
}
