/**
 * 
 */
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe representant un conteneur de proprietes 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:22:52
 */
public class JmsPropertyContainer<T> {
	
	/**
	 * Map ds proprietes
	 */
	private Map<String, T> properties = new HashMap<String, T>();
	
	/**
	 * Constructeur par defaut
	 */
	protected JmsPropertyContainer() {}
	
	/**
	 * Obtention de l'instance
	 * @return	Instance de travail
	 */
	public static <T> JmsPropertyContainer<T> newInstance() {
		
		// On retourne l'instance
		return new JmsPropertyContainer<T>();
	}

	/**
	 * Methode d'ajour d'une propriete
	 * @param property	Propriete a ajouter
	 * @return	Conteneur de proprietes
	 */
	public JmsPropertyContainer<T> add(String propertyName, T propertyValue) {
		
		// Si le Nom de la propriete est null
		if(propertyName == null || propertyName.trim().length() == 0) return this;
		
		// Ajout de la ppt
		properties.put(propertyName.trim(), propertyValue);
		
		// On retourne le conteneur
		return this;
	}

	/**
	 * Methode d'obtention de l'Ensemble des proprietes
	 * @return Ensemble des proprietes
	 */
	public Map<String, T> getProperties() {
		
		// On retourne la MAP non modifiable
		return Collections.unmodifiableMap(properties);
	}
	
	/**
	 * Methode d'obtention de la taille du conteneur
	 * @return	Taille du conteneur
	 */
	public int size() {
		
		// On retourne la taille de la collection
		return this.properties.size();
	}
	
	/**
	 * Methode de vidage du conteneur
	 */
	public void clear() {
		
		// Si la collection est non nulle
		if(properties != null) properties.clear();
	}
}
