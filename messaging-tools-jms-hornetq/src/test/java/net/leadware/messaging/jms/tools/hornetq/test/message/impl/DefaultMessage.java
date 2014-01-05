/**
 * 
 */
package net.leadware.messaging.jms.tools.hornetq.test.message.impl;

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

import net.leadware.messaging.jms.tools.message.ApplicationMessage;
import net.leadware.messaging.jms.tools.message.ApplicationMessageType;

/**
 * Classe representant un message Applicatif de type Default 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 16:39:16
 */
public class DefaultMessage extends ApplicationMessage<String> {

	/**
	 * ID Genere par Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param payload	Payload du message
	 */
	public DefaultMessage(String payload) {
		
		// Positionnement du type
		this.type = ApplicationMessageType.DEFAULT_MESSAGE_TYPE;
		
		// Initialisation du payload
		this.payLoad = payload;
	}
}
