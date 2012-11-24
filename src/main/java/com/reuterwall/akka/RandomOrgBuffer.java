package com.reuterwall.akka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import akka.actor.UntypedActor;

public class RandomOrgBuffer extends UntypedActor {
	Queue<Integer> buffer = new LinkedList<Integer>();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof RandomRequest) {
			if (buffer.isEmpty()) {
				buffer.addAll(fetchRandomNumbers(50));
			}
			for (int count = ((RandomRequest) message).howMany(); count > 0; count--) {
				System.out.println(buffer.remove());
			}
		} else {
			unhandled(message);
		}
	}

	private Queue<Integer> fetchRandomNumbers(Integer count) throws IOException {
		URL url = new URL("https://www.random.org/integers/?num=" + count
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

		return randomNumbers;
	}

	static class RandomRequest {
		private int howMany;

		public RandomRequest(int howMany) {
			this.howMany = howMany;
		}

		public int howMany() {
			return howMany;
		}
	}
}