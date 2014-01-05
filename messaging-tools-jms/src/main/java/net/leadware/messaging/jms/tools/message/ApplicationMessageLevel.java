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

/**
 * Classe representant le niveau de gravite d'un message JMS Applicatif
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI</a>
 * @since 8 déc. 2013 - 15:30:08
 */
public enum ApplicationMessageLevel {
	
    /**
     * ApplicationMessage d'information
     */
    INFO,

    /**
     * ApplicationMessage de warning
     */
    WARNING,

    /**
     * ApplicationMessage d'erreur
     */
    ERROR,

    /**
     * ApplicationMessage d'erreur fatale
     */
    FATAL
}
