/**
 * 
 */
package com.sfsn.backend.messaging.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sfsn.backend.messaging.config.ConfigConstants;
import com.sfsn.backend.messaging.config.MessagingConfiguration;

/**
 * @author Copyright (c) 1996, 2014, Oracle and/or its affiliates. All rights reserved.
 * @version %I%, %G%
 *
 */
@Configuration
public class SafeSeniorConfiguration {
	private final Log log = LogFactory.getLog(getClass().getSimpleName());
	
	public final String queueName = MessagingConfiguration.getConfigValue(ConfigConstants.JMS_QUEUE);
	public final String routingKey = queueName;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		log.info("connectionFactory is called.");
		log.info("queueName="+ queueName);
		log.info("routingKey="+ routingKey);
		String address = MessagingConfiguration.getConfigValue(ConfigConstants.JMS_SERVER);
		log.info("jms.address="+ address);
		String username = MessagingConfiguration.getConfigValue(ConfigConstants.JMS_USERNAME);
		log.info("jms.username="+ username);
		String password = MessagingConfiguration.getConfigValue(ConfigConstants.JMS_PASSWORD);
		log.info("jms.password="+ password);
		String virtualHost = MessagingConfiguration.getConfigValue(ConfigConstants.JMS_VIRTUAL_HOST);
		log.info("jms.virtualHost="+ virtualHost);
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(address);
		connectionFactory.setVirtualHost(virtualHost);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.addConnectionListener(new ConnectionListener() {
			@Override
			public void onClose(Connection arg0) {
				log.warn("connection.connectionFactory is closed.");
				// Send an email to the monitoring team.
			}

			@Override
			public void onCreate(Connection arg0) {
				log.info("connection.connectionFactory is created.");
			}
		});
		return connectionFactory;
	}
	

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		//The routing key is set to the name of the queue by the broker for the default exchange.
		template.setRoutingKey(routingKey);
		//Where we will synchronously receive messages from
		template.setQueue(this.queueName);
		return template;
	}

	@Bean
	// Every queue is bound to the default direct exchange
	public Queue getWorkQueue() {
		return new Queue(this.queueName);
	}
}
