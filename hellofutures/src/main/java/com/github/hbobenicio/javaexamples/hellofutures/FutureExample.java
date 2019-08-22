package com.github.hbobenicio.javaexamples.hellofutures;

import java.util.ArrayList;
import java.util.List;
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
		System.out.printf("Agendando %d tarefas...\n", taskCount);

		long start = System.currentTimeMillis();

		List<Future<Boolean>> futures = new ArrayList<>();
		for (int i = 0; i < taskCount; i++) {
			var future = asyncSlowTask(i);
			futures.add(future);
		}
		System.out.println("Todas as tarefas foram agendadas!");
		
		// Informa ao serviço executor que todas as tarefas já foram agendadas e não iremos agendar mais.
		this.executor.shutdown();

		// Bloqueia a thread corrente, considerando um certo timeout, até que todas as tarefas sejam
		// concluídas. Caso o timeout ocorra, o método retornará.
		boolean allTasksTerminated = this.executor.awaitTermination(2, TimeUnit.SECONDS);

		long finish = System.currentTimeMillis();
		long elapsedTime = finish - start;

		if (allTasksTerminated) {
			System.out.println("Todas as tarefas terminaram normalmente dentro do limite");
			System.out.printf("Tempo gasto: %d ms\n", elapsedTime);
		} else {
			System.out.println("Timeout!");

			var tasksNaoIniciadas = this.executor.shutdownNow();
			System.out.printf("%d tarefas não foram iniciadas e, portanto, foram canceladas.\n", tasksNaoIniciadas.size());
		}
	}
}
