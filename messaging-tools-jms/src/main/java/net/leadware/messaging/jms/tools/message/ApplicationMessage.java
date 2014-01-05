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
import java.util.Date;

/**
 * Classe representant un message JMS Applicatif
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 d�c. 2013 - 15:25:44
 */
public abstract class ApplicationMessage<T extends Serializable> implements Serializable, Comparable<ApplicationMessage<T>> {

	/**
	 * ID Genere par Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant unique du message
	 */
	private String messageId = null;
	
	/**
	 * Date de creation du message
	 */
	private Date dateCreation = new Date();
	
	/**
	 * Type du message
	 */
	protected ApplicationMessageType type = ApplicationMessageType.DEFAULT_MESSAGE_TYPE;

    /**
     * Niveau de gravite du message
     */
    protected ApplicationMessageLevel messageLevel = ApplicationMessageLevel.INFO;

	/**
	 * Payload du message
	 */
	protected T payLoad = null;
	
	/**
	 * Methode d'obtention du champ "payLoad"
	 * @return Champ "payLoad"
	 */
	public T getPayLoad() {
	
		// Renvoi de la valeur du champ
		return payLoad;
	}

	/**
	 * Methode de mise a jour du champ "payLoad"
	 * @param payLoad Nouvelle valeur du champ "payLoad"
	 */
	public void setPayLoad(T payLoad) {
	
		// Mise a jour de la valeur du champ
		this.payLoad = payLoad;
	}
	
	/**
	 * Methode d'obtention du champ "messageId"
	 * @return champ "messageId"
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Methode de mise à jour du champ "messageId"
	 * @param champ "messageId"
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Methode d'obtention du champ "dateCreation"
	 * @return Champ "dateCreation"
	 */
	public Date getDateCreation() {
	
		// Renvoi de la valeur du champ
		return dateCreation;
	}

	/**
	 * Méthode d'obtention du champ "type"
	 * @return the type
	 */
	public ApplicationMessageType getType() {
		return type;
	}

    /**
	 * Méthode d'obtention du champ "messageLevel"
	 * @return the messageLevel
	 */
    public ApplicationMessageLevel getMessageLevel() {
        return messageLevel;
    }

    /**
	 * Methode de mise a jour du champ "messageLevel"
	 * @param messageLevel Nouvelle valeur du champ "messageLevel"
	 */
    public void setMessageLevel(ApplicationMessageLevel messageLevel) {
        this.messageLevel = messageLevel;

        // Si le niveau est null
        if(this.messageLevel == null) this.messageLevel = ApplicationMessageLevel.INFO;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */       
    @SuppressWarnings("rawtypes")
	@Override
    public boolean equals(Object parameter) {
    	
    	// Si le parametre est null
        if(parameter == null) return false;

        // Si le parametre n'est pas de l'instance
        if(!(parameter instanceof ApplicationMessage)) return false;

        // On caste
        ApplicationMessage casted = (ApplicationMessage) parameter;

        // Si le code du parametre est vide
        if(casted.messageId == null || casted.messageId.trim().length() == 0) return false;

        // Si le code du parametre en cours est vide
        if(messageId == null || messageId.trim().length() == 0) return false;

        // On retourne la comparaison des codes
        return messageId.equals(casted.messageId);
    }    
    
    /*
      * (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
    @Override
    public int compareTo(ApplicationMessage<T> parameter) {
    	
    	// Si le parametre est null
        if(parameter == null) return 1;
    
        // Si le parametre n'as pas de date
        if(parameter.dateCreation == null) return 1;
        
        // Si l'objet en cours n'a pas de date
        if(dateCreation == null) return -1;
        
        // Comparaison des dates
        return dateCreation.compareTo(parameter.dateCreation);
    }
}
