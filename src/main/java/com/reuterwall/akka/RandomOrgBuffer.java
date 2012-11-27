package com.reuterwall.akka;

import java.util.LinkedList;
import java.util.Queue;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.reuterwall.akka.RandomOrgClient.RandomOrgRequest;

public class RandomOrgBuffer extends UntypedActor {
	private int batchSize = 50;
	boolean waitingForResponse = false;
	Queue<Integer> buffer = new LinkedList<Integer>();
	Queue<ActorRef> backlog = new LinkedList<ActorRef>();
	ActorRef randomOrgClient;

	@Override
	public void preStart() {
		randomOrgClient = getContext().actorOf(
				new Props(RandomOrgClient.class), "client");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof RandomRequest) {
			preFetchIfAlmostEmpty();
			if (buffer.isEmpty()) {
				backlog.add(getSender());
			} else {
				getSender().tell(buffer.remove());
			}
		} else if (message instanceof RandomOrgResponse) {
			buffer.addAll(((RandomOrgResponse) message)
					.getRandomNumbers());
			waitingForResponse = false;
			while (!backlog.isEmpty() && !buffer.isEmpty()) {
				backlog.remove().tell(buffer.remove());
			}
			preFetchIfAlmostEmpty();
		} else {
			unhandled(message);
		}
	}

	private void preFetchIfAlmostEmpty() {
		if (buffer.size() <= batchSize / 4 && !waitingForResponse) {
			randomOrgClient.tell(new RandomOrgRequest(batchSize), getSelf());
			waitingForResponse = true;
		}
	}

	static class RandomRequest {
	}

	static class RandomOrgResponse {
		private Queue<Integer> randomNumbers;

		public RandomOrgResponse(Queue<Integer> randomNumbers) {
			this.randomNumbers = randomNumbers;
		}

		public Queue<Integer> getRandomNumbers() {
			return randomNumbers;
		}
	}
}