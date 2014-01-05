package net.leadware.messaging.jms.tools.constant;

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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.leadware.messaging.jms.tools.message.ApplicationMessageType;



/**
 * Classe representant l'insterface definissant les constantes JMS 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:22:32
 */
public class JmsConstant {

	/**
	 * Constante contenant la valeur du type de message Applicatif
	 */
	public static final String APP_JMS_MESSAGE_ID_PROPERTY = "APP_JMS_MESSAGE_ID";

	/**
	 * Constante contenant la valeur de selection de message Applicatif
	 */
	public static final String APP_JMS_MESSAGE_ID_VALUE = "true";
	
	/**
	 * Constante contenant la valeur du type de message Applicatif
	 */
	public static final String APP_JMS_MESSAGE_TYPE = "APP_MESSAGE_TYPE";
	
	/**
	 * Methode de construction du selecteur de message
	 * @param messageType	Type de message
	 * @return Selecteur de message
	 */
	public static String buildDefaultMessageSelector() {
		
		// Buffer
		StringBuilder builder = new StringBuilder();
		
		// Ajout
		builder.append("(")
			   .append(APP_JMS_MESSAGE_ID_PROPERTY)
			   .append("=")
			   .append("'" + APP_JMS_MESSAGE_ID_VALUE + "'")
			   .append(")");
		
		// On retourne le selecteur
		return builder.toString();
	}
	
	/**
	 * Methode de construction du selecteur de messages
	 * @param values	Map des Parametre de selection
	 * @return
	 */
	public static String buildMessageSelector(Map<String, String> values) {

		// Buffer
		StringBuilder builder = new StringBuilder(buildDefaultMessageSelector());
		
		// Si la Map est vide
		if(values == null) return builder.toString();
		
		// Ensemble des Cles
		Set<Entry<String, String>> entries = values.entrySet();
		
		// Parcours
		for (Entry<String, String> entry : entries) {

			// Ajout
			builder.append(" AND ")
				   .append("(")
				   .append(entry.getKey())
				   .append("=")
				   .append("'" + entry.getValue() + "'")
				   .append(")");
		}
		
		// On retourne la chaine
		return builder.toString();
	}

	/**
	 * Methode de construction du selecteur de messages
	 * @param messageType	Type de message
	 * @return Selecteur
	 */
	public static String buildMessageSelector(ApplicationMessageType messageType) {

		// Buffer
		StringBuilder builder = new StringBuilder(buildDefaultMessageSelector());
		
		// Si la Map est vide
		if(messageType == null) return builder.toString();

		// Ajout
		builder.append(" AND ")
			   .append("(")
			   .append(APP_JMS_MESSAGE_TYPE)
			   .append("=")
			   .append("'" + messageType.name() + "'")
			   .append(")");
			
		// On retourne la chaine
		return builder.toString();
	}
	
	
}
