package com.ds3161.reactor.rabbit.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ds3161.reactor.rabbit.test.Constants.EXCHANGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

	private final Mono<Connection> connectionMono;

	private final Sender sender;

	private final Flux<Delivery> deliveryFlux;

	private final AtomicBoolean latchCompleted = new AtomicBoolean(false);

	private final static ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public void run(String... args) throws Exception {
		// A sample receiver
		deliveryFlux //
				.doOnNext(delivery -> log.info("Delivery envelope:{} properties:{} headers:{}", delivery.getEnvelope(),
						delivery.getProperties(), delivery.getProperties().getHeaders()))
				.map(delivery -> unmarshal(delivery.getBody())) //
				.doOnNext(body -> log.info("body:{}", body)) //
				.subscribe();

		// A sample sender
		log.info("Sending messages...");
		sender.send(Flux.range(1, 10)
				.map(i -> buildMessage(Collections.singletonMap("message", "message-value-" + i), EXCHANGE)))
				.subscribe();
	}

	private OutboundMessage buildMessage(Map<String, Object> payload, String destination) {
		// create (or forward) headers
		Map<String, Object> headers = new HashMap<>();
		headers.put("header1", "header1-value");
		headers.put("header2", "header2-value");

		// add headers to basic properties
		AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(headers).build();

		// return new Outbound message
		return new OutboundMessage(destination, "", properties, marshal(payload));
	}

	@PreDestroy
	public void close() throws Exception {
		// I think this is only here to ensure the connection closes gracefully. But it
		// does extend closable so it should anyway?
		// I think you can also just close the sender and receiver objects
		connectionMono.doOnNext(x -> log.info("Closing connection to rabbit...")).block().close();
	}

	private byte[] marshal(Map<String, Object> data) {
		try {
			return MAPPER.writeValueAsBytes(data);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Object> unmarshal(byte[] data) {
		try {
			Map<String, Object> body = MAPPER.readValue(data, Map.class);
			return body;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}