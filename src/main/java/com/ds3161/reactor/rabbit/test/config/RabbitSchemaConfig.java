package com.ds3161.reactor.rabbit.test.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.ds3161.reactor.rabbit.test.Constants.EXCHANGE;
import static com.ds3161.reactor.rabbit.test.Constants.QUEUE;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RabbitSchemaConfig {

	private final AmqpAdmin amqpAdmin;

	@PostConstruct
	public void init() {
		// initialize your rabbit schema here. Much like spring cloud streams it is best
		// to make it additive to prevent interruption of any in flight apps
		// Note: This can also be done through the sender bean from reactor rabbitmq
		// (instead of spring amqp) as well, however if an app is only a consumer, it
		// can't really create itself as the queue. Would rather manage bindings in a way
		// that works for both producer apps and consumer apps
		final Exchange exchange = ExchangeBuilder.topicExchange(EXCHANGE).build();
		final Queue queue = QueueBuilder.durable(QUEUE).build();
		amqpAdmin.declareExchange(exchange);
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("#").noargs());

		// TODO DLQ config
	}

}
