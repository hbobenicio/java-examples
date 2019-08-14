package com.github.hbobenicio.javaexamples.hellofutures;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		final var futureExample = new FutureExample();
		futureExample.run(2);
		
		final var completableFutureExample = new CompletableFutureExample();
		completableFutureExample.run(2);
	}

}
