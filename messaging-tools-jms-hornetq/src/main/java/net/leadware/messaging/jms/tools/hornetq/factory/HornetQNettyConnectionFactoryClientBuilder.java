/**
 * 
 */
package net.leadware.messaging.jms.tools.hornetq.factory;

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

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import net.leadware.messaging.jms.tools.factory.ConnectionFactoryClientBuilder;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;


/**
 * Classe repr√©sentant l'implementation HornetQ du constructeur de fabrique 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 16:42:38
 */
public class HornetQNettyConnectionFactoryClientBuilder implements
		ConnectionFactoryClientBuilder {
	
	/**
	 * Adresse du serveur JMS
	 */
	private String jmsServerHost = "localhost";
	
	/**
	 * Port du serveur JMS
	 */
	private String jmsServerPort = "5455";
	
	/**
	 * Methode d'obtention du champ "jmsServerHost"
	 * @return the jmsServerHost
	 */
	public String getJmsServerHost() {
		return jmsServerHost;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.factory.ConnectionFactoryClientBuilder#setJmsServerHost(java.lang.String)
	 */
	public void setJmsServerHost(String jmsServerHost) {
		this.jmsServerHost = jmsServerHost;

		// Si le host est null
		if(jmsServerHost == null || jmsServerHost.trim().isEmpty()) this.jmsServerHost = "localhost";
	}

	/**
	 * Methode d'obtention du champ "jmsServerPort"
	 * @return the jmsServerPort
	 */
	public String getJmsServerPort() {
		return jmsServerPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.oci.ip.tools.jms.factory.ConnectionFactoryClientBuilder#setJmsServerPort(java.lang.String)
	 */
	public void setJmsServerPort(String jmsServerPort) {
		this.jmsServerPort = jmsServerPort;
		
		// Si le port est null
		if(jmsServerPort == null || jmsServerPort.trim().isEmpty()) this.jmsServerPort = "5455";
	}

	/* (non-Javadoc)
	 * @see com.oci.ip.tools.jms.factory.ConnectionFactoryClientBuilder#build()
	 */
	@Override
	public ConnectionFactory build() {
		
		// MAP des parametres
		Map<String, Object> connectionParams = new HashMap<String, Object>();
		
		// Positionnement du Port
		connectionParams.put(TransportConstants.PORT_PROP_NAME, Integer.parseInt(jmsServerPort));

		// Positionnement du Host
		connectionParams.put(TransportConstants.HOST_PROP_NAME, jmsServerHost);
		
		// Configuration du Transport
		TransportConfiguration transportConfiguration = 
				new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams);
		
		// Fabrique de connexion
		ConnectionFactory connectionFactory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
		
		// On retourne la fabrique
		return connectionFactory;
	}

}
