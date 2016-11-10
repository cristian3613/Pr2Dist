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

	public static void ejecutar(int min, int max, int n, String host) throws RemoteException,
	NotBoundException {
		try {
			//Se guardan los rangos iniciales y el numero de servidores
			int minRango = min;
			int maxRango = max;
			int nPedidos = n;
			//Se obtiene el registro RMI almacenado en host
			Registry registry = LocateRegistry.getRegistry(host);
			//Se recupera el servidor de asignacion
			WorkerFactory servAsig = (WorkerFactory) registry.lookup("Factory");
			//Se adquieren los servidores de calculo pedidos
			ArrayList<Worker> workers = servAsig.dameWorkers(n);
			//Se crea el objeto que gestionara la concurrencia
			Monitor monitor = new Monitor(workers);
			final int numWorkers = workers.size();
			int numsAnalizar = max - min;
			int manda = numsAnalizar / numWorkers;
			int limite = manda / numWorkers;
			int intervalo = min + manda - 1;
			if (intervalo > max) intervalo = max;
			Worker work;
			Gestor gestor;
			Thread thread;

			while (intervalo < max) {
				work = monitor.daWorker();
				System.out.println("Intervalo "+min+ " y "+intervalo);
				gestor = new Gestor(work, min, intervalo , monitor);
				thread = new Thread(gestor);
				thread.start();
				min = intervalo + 1;
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
			System.out.println("Esperando a que acaben todos servidores de calcular...");
			/* 
			 * BLOQUEADO hasta que no acaben 
			 */ 
			while(monitor.tamanyo() < numWorkers)	Thread.sleep(1);
			Collections.sort(primos);
			System.out.println("\nPRIMOS CALCULADOS Y ORDENADOS DEL RANGO ["
					+ minRango + ", " + maxRango + "] utilizando " + nPedidos +
					" SERVIDORES" + " de calculo:\n");
			/*
			 * Se imprimen los primos obtenidos
			 */
			System.out.println(primos);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR en Main");
		}
	}

}
