package com.reuterwall.akka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("RandomOrgSystem");
		final ActorRef randomOrgBuffer = system.actorOf(new Props(
				RandomOrgBuffer.class), "buffer");

		RandomOrgRandom random = new RandomOrgRandom(randomOrgBuffer);
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			numbers.add(i);
		}

		Collections.shuffle(numbers, random);
		for (Integer i : numbers) {
			System.out.print(i + ", ");
		}
		System.out.println();

		system.shutdown();
	}
}
