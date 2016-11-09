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

	static ArrayList<Integer> primos = new ArrayList<Integer>();

	public static void main(String[] args) throws RemoteException, NotBoundException {
		try {
			/*
			 * Se leen parametros pasados
			 */
			int min = Integer.parseInt(args[0]);
			int max = Integer.parseInt(args[1]);
			int n = Integer.parseInt(args[2]);
			
			int minRango = min;
			int maxRango = max;
			int nPedidos = n;
			
			String host = (args.length < 4) ? null : args[3];
			Registry registry = LocateRegistry.getRegistry(host);
			
			WorkerFactory servAsig = (WorkerFactory) registry.lookup("Factory");

			ArrayList<Worker> servsCalcPedidos = servAsig.dameWorkers(n);
			
			Monitor monitor = new Monitor(servsCalcPedidos);
			
			int servsPedidos = servsCalcPedidos.size();
			int numsAnalizar = max-min+1;
			int manda = numsAnalizar/servsPedidos;
			int sobra = numsAnalizar%servsPedidos;
			Worker work;
			Gestor gestor;
			Thread thread;
			while(min<max) {
				work = monitor.daWorker();
				if (sobra > 0) {
					gestor = new Gestor(work, min, min+sobra, monitor);
					thread = new Thread(gestor);
					thread.start();
					System.out.println("Rango: ["+min+"-"+(min+sobra)+"], Sobra = " + sobra);
					min = min+sobra+1;
					sobra = 0;
				}
				else {
					gestor = new Gestor(work, min, min+manda-1, monitor);
					thread = new Thread(gestor);
					thread.start();
					System.out.println("Rango: ["+min+"-"+(min+manda-1)+"], Sobra = " + sobra);
					min = min+manda;
				}
			}
			while(monitor.tamanyo() != servsPedidos) { /*BLOQUEADO hasta que no acaben*/ }
			Collections.sort(primos);
			System.out.println("\nPRIMOS CALCULADOS Y ORDENADOS DEL RANGO ["
					+minRango+", "+maxRango+"] utilizando "+nPedidos+" SERVIDORES"
							+ " de calculo:\n");
			System.out.println(primos);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("ERROR");
		}
	}

}
