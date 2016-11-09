/**
 * 
 */

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;

public class Cliente {

	static ArrayList<Integer> primos = new ArrayList<Integer>(); //EXCLUSION MUTUA
	static ArrayList<Integer> candidatos = new ArrayList<Integer>(); //EXCLUSION MUTUA

	public static void main(String[] args) throws RemoteException,
			NotBoundException {
		try {
			/*
			 * Se leen parametros pasados
			 */
			int min = Integer.parseInt(args[0]);
			int max = Integer.parseInt(args[1]);
			int n = Integer.parseInt(args[2]);
			
			if (min < 3) primos.add(2);
			//INICIALIZACION LISTA
			for (int i=3; i <= (max - min)/2 ; i += 2){
				candidatos.add(i);
			}
				

			int minRango = min;
			int maxRango = max;
			int nPedidos = n;

			String host = (args.length < 4) ? null : args[3];
			Registry registry = LocateRegistry.getRegistry(host);

			WorkerFactory servAsig = (WorkerFactory) registry.lookup("Factory");

			ArrayList<Worker> workers = servAsig.dameWorkers(n);
			
			Monitor monitor = new Monitor(workers);
			int numWorkers = workers.size();
			int numsAnalizar = max - min + 1;
			//int divisor = (numWorkers * 2);
			int manda = numsAnalizar / numWorkers ; // MAL REPARTO
			//int sobra = numsAnalizar % divisor;
			int limite = manda / numWorkers;
			int intervalo = min + manda;
			if (intervalo > max) intervalo = max;
			Worker work;
			Gestor gestor;
			Thread thread;
			
			while (intervalo < max){
				work = monitor.daWorker();
				System.out.println("Probando "+ min+ "y "+ intervalo);
				gestor = new Gestor(work, min, intervalo , monitor);
				thread = new Thread(gestor);
				thread.start();
				min = intervalo;
				manda = manda - (manda / numWorkers);
				if (manda < limite) manda = limite;
				intervalo += manda;
				if (intervalo >= max) {
					intervalo = max;
					work = monitor.daWorker();
					gestor = new Gestor(work, min, intervalo , monitor);
					thread = new Thread(gestor);
					thread.start();
					break;
				}
				
			}
			while(monitor.tamanyo() != numWorkers) { /*BLOQUEADO hasta que no acaben*/ }
			Collections.sort(primos);
			 System.out.println("\nPRIMOS CALCULADOS Y ORDENADOS DEL RANGO ["
			  + minRango + ", " + maxRango + "] utilizando " + nPedidos +
			  " SERVIDORES" + " de calculo:\n");
			 System.out.println(primos);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR");
		}
	}

}
