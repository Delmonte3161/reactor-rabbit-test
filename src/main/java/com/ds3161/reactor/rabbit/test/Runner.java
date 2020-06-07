package com.ds3161.reactor.rabbit.test;

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

	@Override
	public void run(String... args) throws Exception {
		int messageCount = 10;
		CountDownLatch latch = new CountDownLatch(messageCount);

		// A sample receiver
		// TODO get at the headers?
		deliveryFlux.doOnNext(m -> {
			log.info("Received message {}", new String(m.getBody()));
			latch.countDown();
		}).subscribe();

		// A sample sender
		// TODO forward headers/add headers
		log.info("Sending messages...");
		sender.send(
				Flux.range(1, messageCount).map(i -> new OutboundMessage(EXCHANGE, "", ("Message_" + i).getBytes())))
				.subscribe();
		latchCompleted.set(latch.await(5, TimeUnit.SECONDS));
	}

	@PreDestroy
	public void close() throws Exception {
		// I think this is only here to ensure the connection closes gracefully. But it
		// does extend closable so it should anyway?
		connectionMono.doOnNext(x -> log.info("Closing connection to rabbit...")).block().close();
	}

}