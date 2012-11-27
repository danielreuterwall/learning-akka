package com.reuterwall.akka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import akka.actor.UntypedActor;

import com.reuterwall.akka.RandomOrgBuffer.RandomOrgResponse;

public class RandomOrgClient extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof RandomOrgRequest) {
			int batchSize = ((RandomOrgRequest) message).getBatchSize();
			URL url = new URL("https://www.random.org/integers/?num="
					+ batchSize
					+ "&min=0&max=65535&col=1&base=10&format=plain&rnd=new");
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			Queue<Integer> randomNumbers = new LinkedList<Integer>();
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				randomNumbers.add(new Integer(line));
			}
			reader.close();

			getSender().tell(new RandomOrgResponse(randomNumbers), getSelf());
		} else {
			unhandled(message);
		}
	}

	static class RandomOrgRequest {
		private int batchSize;

		public RandomOrgRequest(int batchSize) {
			this.batchSize = batchSize;
		}

		public int getBatchSize() {
			return batchSize;
		}
	}
}
