package com.github.hbobenicio.javaexamples.hellofutures;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CompletableFutureExample {
	
	public void run(int n) {
		System.out.println("Sequencial...");
		for (int i = 0; i < n; i++) {
			slowTask(i);
		}
		System.out.println("Sequencial finished!");
		
		System.out.println("Async...");
		var executorService = Executors.newCachedThreadPool();
		var futures = new ArrayList<CompletableFuture<Void>>(n);
		for (int i = 0; i < n; i++) {
			futures.add(asyncSlowTask(executorService, i));
		}
		
		var futuresCompleted = futures.stream()
			.map(CompletableFuture::join)
			.collect(Collectors.counting());
		
		executorService.shutdown();
		
		System.out.println("Async finished! Tarefas assincronas finalizadas: " + futuresCompleted.toString());
	}
	
	private void slowTask(int taskID) {
		System.out.printf("Iniciando slowTask %d...\n", taskID);
		long count = 0;
		for (long x = 0; x < 1000000000; x++) {
			count++;
		}
		System.out.printf("slowTask %d finalizada! count = %d\n", taskID, count);
	}
	
	private CompletableFuture<Void> asyncSlowTask(ExecutorService executorService, int taskId) {
		var future = new CompletableFuture<Void>();
		
		executorService.submit(() -> {
			slowTask(taskId);
			future.complete(null);
		});
		
		return future;
	}

}
