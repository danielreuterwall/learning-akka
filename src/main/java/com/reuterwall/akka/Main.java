package com.reuterwall.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.reuterwall.akka.RandomOrgBuffer.RandomRequest;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("RandomOrgSystem");
		final ActorRef randomOrgBuffer = system.actorOf(new Props(
				RandomOrgBuffer.class), "buffer");

		randomOrgBuffer.tell(new RandomRequest(10));
		system.shutdown();
	}
}
