package com.ds3161.reactor.rabbit.test.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import static com.ds3161.reactor.rabbit.test.Constants.QUEUE;

@Slf4j
@Configuration
public class MessagingConfig {

	@Bean
	Sender sender(Mono<Connection> connectionMono) {
		return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
	}

	@Bean
	Receiver receiver(Mono<Connection> connectionMono) {
		return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
	}

	@Bean
	Flux<Delivery> deliveryFlux(Receiver receiver, @Value("${spring.application.name}") String consumerName) {
		// TODO handle manual ack when processed
		return receiver.consumeAutoAck(QUEUE, new ConsumeOptions().consumerTag(consumerName));
	}

}
