package com.ds3161.reactor.rabbit.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MessageHandler {

	// AtomicInteger atomicCount = new AtomicInteger();
	//
	// @Bean
	// public Consumer<Flux<Message<Map<String, Object>>>> receiveReactive() {
	// return messages -> messages.doOnNext(message -> {
	// log.info("Received message: {}", message);
	// int count = atomicCount.getAndIncrement();
	// if (count == 5 || (count > 5 && count % 5 == 0)) {
	// nackMessage(message,false);
	// }
	// else {
	// nackMessage(message,true);
	// }
	// }).subscribe();
	// }
	//
	// @Bean
	// public Consumer<Message<Map<String, Object>>> receiveNonReactive() {
	// return message -> {
	// log.info("Received message: {}", message);
	// throw new RuntimeException("Sumting wong");
	// };
	//
	// }
	//
	// private static void ackMessage(Message<Map<String, Object>> message) {
	// try {
	// log.info("acking message: {}", message);
	// final Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL,
	// Channel.class);
	// if (nonNull(channel)) {
	// channel.basicAck(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class),
	// false);
	// }
	// }
	// catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	// private static void nackMessage(Message<Map<String, Object>> message, boolean
	// requeue) {
	// try {
	// log.info("Nack message (requeue:{}) : {}", requeue,message);
	//
	// final Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL,
	// Channel.class);
	// if (nonNull(channel)) {
	// channel.basicReject(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class),
	// requeue);
	// }
	// }
	// catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }

}