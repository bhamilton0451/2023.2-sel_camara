package jantarFilosofos;

public class TarefaDois {
	public static void main(String[] args) {
		RunnableThread rt = new RunnableThread();
		
		for (int i = 0; i < RunnableThread.talheres.length; i++) {
			RunnableThread.talheres[i] = 0;
		}
		
		for(int L = 0; L < jantaram.length; L++) {
			jantaram[L] = false;
		}
		
		for(int L = 0; L < quant.length; L++) {
			quant[L] = 0;
		}
		
		for(int L = 0; L < thread.length; L++) {
			thread[L] = new Thread(rt);
			thread[L].setName( String.valueOf(L) );
			thread[L].start();
		}
		
		greenLight = true;
	}
	
	static Thread[] thread = new Thread[RunnableThread.quantidade];
	static boolean[] jantaram = new boolean[RunnableThread.quantidade];
	static int[] quant = new int[RunnableThread.quantidade];
	static boolean greenLight = false;
	static int acoes = 0;
	static int loops = 0;

	public static void janta() {
		int cont = 0;
		
		for(int i = 0; i < jantaram.length; i++){
			if (jantaram[i] == true){
				cont = cont + 1;
			}
			
			if (cont == 5){
				status();   
			}
		}
	}

	public static void acao(int talher, int index) {
		janta();
		
		try {
			if (podeComer(talher) == true) {
				
				int filosofo = Integer.parseInt(currentThreadName());
				
				jantaram[filosofo] = true;
				quant[filosofo] = quant[filosofo] + 1;
				
				RunnableThread.setStatusFilosofo(StatusFilosofo.Comendo);
				
				System.out.println(
						RunnableThread.getStatusFilosofo() + " - " + currentThreadName() + " | talheres usados " + talher + " e " + index
				);
				
				Thread.sleep(200);
				
				RunnableThread.talheres[talher] = 0;
				RunnableThread.talheres[index] = 0;

				finalizou();
			} else {
				
				RunnableThread.setStatusFilosofo(StatusFilosofo.Pensando);
				System.out.println(RunnableThread.getStatusFilosofo() + " - " + currentThreadName());
				Thread.sleep(150);
			}

		} catch (InterruptedException iex) {
			//System.out.println("Exception: " + iex.getMessage());
		}
	}

	public static void finalizou() throws InterruptedException {
		RunnableThread.setStatusFilosofo(StatusFilosofo.Terminou);
		System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
		Thread.sleep(200);
	}

	public static int filaCircular(int talher) {
		acoes++;
		int index;
		
		if (talher == 0) {
			index = 4;
		} else {
			index = talher - 1;
		}

		return index;
	}

	public static boolean podeComer(int talher) throws InterruptedException {
		RunnableThread.semaphore.acquire();
		
		int index = filaCircular(talher);
		if (RunnableThread.talheres[talher] == 0 && RunnableThread.talheres[index] == 0){
			
			RunnableThread.talheres[talher] = 1;
			RunnableThread.talheres[index] = 1;
			RunnableThread.semaphore.release();
			
			return true;
		}
		RunnableThread.semaphore.release();
		return false;
	}
	
	public static void status() {
		try{
			if(RunnableThread.semaphore.tryAcquire()){				
				loops = loops+1;
		
				System.out.println("Todos jantaram! ");
				System.out.println("Acoes realizadas: " + RunnableThread.GetConta());
				
				for(int L = 0; L < quant.length; L++) {
					System.out.println("Filosofo " + String.valueOf(L) + " comeu: " + quant[L] + " vezes.");
					quant[L] = 0;
				}
				
				for (int i = 0; i < jantaram.length; i++) {
					jantaram[i] = false;
				}
				
				RunnableThread.ContaZero();
				
				System.out.println();
				System.out.println();
				
				RunnableThread.semaphore.release();
				endAllThreads();
			}
		} catch (Exception a) {}
	}
	
	private static String currentThreadName() {
		return Thread.currentThread().getName();
	}
	
	private static void endAllThreads() {
		for(int L = 0; L < thread.length; L++) {
			if(thread[L] != Thread.currentThread()){
				thread[L].interrupt();
			}
		}
		Thread.currentThread().interrupt();
	}
}

enum StatusFilosofo {
	Comendo,
	Pensando,
	Terminou,
	Iniciou,
}

//Test