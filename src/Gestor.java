import java.util.ArrayList;

public class Gestor implements Runnable {

	private Worker worker;
	private int min;
	private int max;
	private Monitor monitor;

	public Gestor(Worker worker, int min, int max, Monitor monitor){
		this.worker = worker;
		this.min = min;
		this.max = max;
		this.monitor = monitor;
	}

	public void run(){
		try{
			ArrayList<Integer> primosSubrango = worker.encuentraPrimos(min, max);
			/*
			 * Se anyade a la lista final de primos, los primos calculados del
			 * subrango dado
			 */
			synchronized(Cliente.primos) {
				Cliente.primos.addAll(primosSubrango);
			}
			monitor.addWorker(worker);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
