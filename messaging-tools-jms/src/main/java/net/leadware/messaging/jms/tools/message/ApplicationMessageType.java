/**
 * 
 */
package net.leadware.messaging.jms.tools.message;

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

/**
 * Interface representant le type de message 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 23 juin 2014 - 03:59:20
 */
public interface ApplicationMessageType extends Serializable {
	
	/**
	 * Type de message par defaut
	 */
	public static final ApplicationMessageType DEFAULT_MESSAGE_TYPE = new ApplicationMessageType() {
		
		/**
		 * ID Genere par eclipse
		 */
		private static final long serialVersionUID = 1L;
		
		/*
		 * (non-Javadoc)
		 * @see net.leadware.messaging.jms.tools.message.ApplicationMessageType#name()
		 */
		@Override
		public String name() {
			
			// On retourne la valeur par defaut
			return "APP_DEFAULT_MESSAGE_TYPE";
		}
	};
	
	/**
	 * Methode permettant de retourner le nom de la propriete
	 * @return	Nom de la propriete
	 */
	public String name();
}
