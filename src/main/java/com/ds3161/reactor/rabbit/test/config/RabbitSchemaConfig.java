package com.ds3161.reactor.rabbit.test.config;

import com.rabbitmq.client.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.ds3161.reactor.rabbit.test.Constants.EXCHANGE;
import static com.ds3161.reactor.rabbit.test.Constants.QUEUE;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RabbitSchemaConfig {

	private final AmqpAdmin amqpAdmin;

	@PostConstruct
	public void init() {
		// ititialize your rabbit schema here. Much like spring cloud streams it is best
		// to make it addative to prevent interruption of any in flight apps
		final Exchange exchange = ExchangeBuilder.topicExchange(EXCHANGE).build();
		final Queue queue = QueueBuilder.durable(QUEUE).build();
		amqpAdmin.declareExchange(exchange);
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("#").noargs());
	}

}
