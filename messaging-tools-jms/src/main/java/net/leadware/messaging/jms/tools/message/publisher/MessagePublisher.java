package net.leadware.messaging.jms.tools.message.publisher;

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

import javax.jms.Destination;

import net.leadware.messaging.jms.tools.message.ApplicationMessage;


/**
 * Classe representant l'interface du producteur de message Applicatif
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:56:45
 */
public interface MessagePublisher {
	
	/**
	 * Methode d'envoie d'un message
	 * @param message	ApplicationMessage
	 */
	public void publish(ApplicationMessage<? extends Serializable> message, String destination);

	/**
	 * Methode d'envoie d'un message
	 * @param message	ApplicationMessage
	 * @param destination Destination du message
	 */
	public void publish(ApplicationMessage<? extends Serializable> message, Destination destination);
}
