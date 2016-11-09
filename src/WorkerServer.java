import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WorkerServer implements Worker {

	private String direccion;

	public WorkerServer(String dir) {
		direccion = (dir == null) ? "" : dir;
	}

	public String getDireccion() {	return direccion;	}

	/**
	 * Devuelve un vector con los primos entre min y max
	 */
	public ArrayList<Integer> encuentraPrimos(int min, int max, 
			ArrayList<Integer> primosCandidatos)
			throws RemoteException {
		ArrayList<Integer> listaPrimos = new ArrayList<Integer>();
		if ((min % 2 )== 0) min++;
		for (int i = min; i <= max; i+=2) {
			if (esPrimo(i, primosCandidatos)) {
				listaPrimos.add(i);
			}
		}
		System.out.println("Numeros primos entre " + min + " y " + max +
				" calculados");
		return listaPrimos;
	}

	/**
	 * Devuelve true si el numero pasado como parametro es primo
	 * 
	 * Code extracted from: http://www.crunchify.com
	 */
	private boolean esPrimo(int num, ArrayList<Integer> primosCandidatos) {
		//El bucle empieza desde el 2 (primer primo)
		for (int candidato : primosCandidatos) {
			if (num%candidato == 0)	return false;
			if (candidato > num/2) break;
		}
		return true;
	}
	
	/**
	 * Devuelve un vector con los primos entre min y max
	 */
	public ArrayList<Integer> encuentraPrimos2(int min, int max)
			throws RemoteException {
		ArrayList<Integer> listaPrimos = new ArrayList<Integer>();
		for (int i = min; i <= max; i++) {
			if (esPrimo2(i)) {
				listaPrimos.add(i);
			}
		}
		System.out.println("Numeros primos entre " + min + " y " + max +
				" calculados");
		return listaPrimos;
	}

	/**
	 * Devuelve true si el numero pasado como parametro es primo
	 * 
	 * Code extracted from: http://www.crunchify.com
	 */
	private boolean esPrimo2(int num) {
		//El bucle empieza desde el 2 (primer primo)
		for (int i = 2; i <= num/2; i++) {
			if (num%i == 0)	return false;
		}
		return true;
	}
	
	/**
	 * Arranca el servidor Worker
	 * @param host
	 */
	public static void ejecutar(String host) {
		try {
			if (host == null) {
				System.out.println("Se ejecuta el WorkerServer en puerto 1099");
			}
			else	System.out.println("Se ejecuta en " + host);
			WorkerServer obj = new WorkerServer(host);
			Worker stub = (Worker) UnicastRemoteObject.exportObject(obj, 0);
			/*
			 * Si host tiene valor null, el registro se busca en localhost:1099
			 * Si es distinto de null, el registro se busca en esa maquina
			 */
			Registry registro = LocateRegistry.getRegistry(host);
			/*
			 * Se recuperan los nombres asociados al registro
			 */
			String[] nombresRegistro = registro.list();
			int numWorkers = 1;
			for (String nombre : nombresRegistro) {
				if (nombre.startsWith("Worker"))	numWorkers++;
			}
			/*
			 * Se asocia un nombre al objeto remoto creado
			 */
			registro.bind("Worker " + numWorkers, stub);
			System.out.println("Worker " + numWorkers);
			System.err.println("Server ready");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en WorkerServer");
		}
	}

}
