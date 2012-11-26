package com.reuterwall.akka;

import static akka.pattern.Patterns.ask;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.util.Timeout;

import com.reuterwall.akka.RandomOrgBuffer.RandomRequest;

public class RandomOrgRandom extends Random {

	private ActorRef randomOrgBuffer;
	private final Timeout timeout = new Timeout(1, TimeUnit.MINUTES);

	public RandomOrgRandom(ActorRef randomOrgBuffer) {
		this.randomOrgBuffer = randomOrgBuffer;
	}

	@Override
	protected int next(int bits) {
		if (bits <= 16) {
			return random16Bits() & ((1 << bits) - 1);
		} else {
			return (next(bits - 16) << 16) + random16Bits();
		}
	}

	private int random16Bits() {
		Future<Object> future = ask(randomOrgBuffer, new RandomRequest(),
				timeout);
		try {
			return ((Integer) Await.result(future, timeout.duration()))
					.intValue();
		} catch (Exception e) {
			// Not necessary to address in this sample application
			throw new RuntimeException(e);
		}
	}
}
