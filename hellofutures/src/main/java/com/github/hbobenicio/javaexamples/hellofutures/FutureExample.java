package com.github.hbobenicio.javaexamples.hellofutures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureExample {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
//	private ExecutorService executor = Executors.newFixedThreadPool(5);
//	private ExecutorService executor = Executors.newWorkStealingPool(5);

	private void slowTask(int taskID) {
		System.out.printf("Iniciando slowTask %d...\n", taskID);
		long count = 0;
		for (long x = 0; x < 1000000000; x++) {
			count++;
		}
		System.out.printf("slowTask %d finalizada! count = %d\n", taskID, count);
	}

	private Future<Boolean> asyncSlowTask(int taskID) {
		return executor.submit(() -> {
			slowTask(taskID);
			return true;
		});
	}

	public void run(int taskCount) throws InterruptedException {
		System.out.println("Agendando tarefas...");
		long start = System.currentTimeMillis();
		for (int i = 0; i < taskCount; i++) {
			asyncSlowTask(i);
		}
		System.out.println("Todas as tasks foram agendadas!");
		
		this.executor.shutdown();
		boolean terminated = this.executor.awaitTermination(10, TimeUnit.SECONDS);
		long finish = System.currentTimeMillis();
		long elapsedTime = finish - start;
		if (terminated) {
			System.out.println("Todas as tarefas terminaram normalmente dentro do limite");
			System.out.printf("Tempo gasto: %d ms\n", elapsedTime);
		} else {
			System.out.println("Timeout");
			var tasksNaoIniciadas = this.executor.shutdownNow();
			System.out.printf("%d tarefas não foram iniciadas e, portanto, foram canceladas.\n", tasksNaoIniciadas.size());
		}
	}
}
