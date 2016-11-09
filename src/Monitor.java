import java.util.ArrayList;

public class Monitor {
	
	private ArrayList<Worker> workers = new ArrayList<Worker>();
	
	public Monitor(ArrayList<Worker> workers) {
		this.workers = workers;
	}

	public synchronized Worker daWorker() {
		Worker work = null;
		try {
			while(workers.isEmpty())	wait();
			//Se coge el primer servidor de la lista
			work = workers.get(0);
			//Se elimina de la lista
			workers.remove(0);
			return work;
		}
		catch(Exception e) {
			e.printStackTrace();
			return work;
		}
	}
	
	/**
	 * Se adjunta un nuevo servidor de calculo a la lista tras acabar
	 * de calcular los primos del subintervalo dado
	 * @param worker
	 */
	public synchronized void addWorker(Worker worker) {
		workers.add(worker);
		notifyAll();
	}
	
	public int tamanyo() {
		return workers.size();
	}
	
}
