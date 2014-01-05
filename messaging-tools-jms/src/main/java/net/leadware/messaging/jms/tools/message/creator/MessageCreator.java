package net.leadware.messaging.jms.tools.message.creator;

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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Classe representant un forgeur de message JMS
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:52:51
 */
public interface MessageCreator {
	
	/**	
	 * Methode de creation d'un message a partir d'une session
	 * @param session	Session en cours
	 * @return	ApplicationMessage creee
	 * @throws JMSException Exception potentielle
	 */
	public Message createMessage(Session session) throws JMSException;
}
