package com.ds3161.reactor.rabbit.test.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Configuration
public class RabbitConfig {

	@Bean()
	Mono<Connection> connectionMono(RabbitProperties rabbitProperties) {
		// TODO ensure we get this working with java cf env. Likely with a cloud profile.
		// TODO May work as-is though as long as java cf env is populating the
		// TODO RabbitProperties. Should ensure we are setting eveything we need
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(rabbitProperties.getHost());
		connectionFactory.setPort(rabbitProperties.getPort());
		connectionFactory.setUsername(rabbitProperties.getUsername());
		connectionFactory.setPassword(rabbitProperties.getPassword());
		if (nonNull(rabbitProperties.getVirtualHost())) {
			connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
		}
		return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
	}

}
