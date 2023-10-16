package jantarFilosofos;

import java.util.concurrent.Semaphore;

class RunnableThread implements Runnable {
	
	static int conta = 0;

	public static int quantidade = 5;
	public static int[] talheres = new int[quantidade];
	public static Semaphore semaphore = new Semaphore(1);
	private static StatusFilosofo statusFilosofo;

	@Override
	public void run() {
		conta++;
		
		while(TarefaDois.loops < 1) {
			int filosofo = Integer.parseInt(currentThreadName());
			RunnableThread.setStatusFilosofo(StatusFilosofo.Iniciou);
			
			System.out.println(
					RunnableThread.getStatusFilosofo() + " - " + currentThreadName()
			);
			
			int index = TarefaDois.filaCircular((filosofo));
			
			TarefaDois.acao(filosofo, index);
		}
	}
	
	
	
	public static int GetConta(){
		return conta;
	}
	
	public static void ContaZero(){
		conta = 0;	
	}
	
	private String currentThreadName(){
		return Thread.currentThread().getName();
	}
	
	public static void setStatusFilosofo(StatusFilosofo stat){
		statusFilosofo = stat;
	}

	public static StatusFilosofo getStatusFilosofo(){
		conta++;
		return statusFilosofo;
	}
	
}