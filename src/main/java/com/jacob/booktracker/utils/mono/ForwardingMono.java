package com.jacob.booktracker.utils.mono;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

public abstract class ForwardingMono<T> extends Mono<T> {

	abstract Mono<T> getMono();

	@Override
	public void subscribe(CoreSubscriber<? super T> actual) {
		getMono().subscribe(actual);
	}
}
