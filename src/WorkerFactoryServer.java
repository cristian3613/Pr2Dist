import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WorkerFactoryServer implements WorkerFactory {

	static Registry registro;
	private String direccion;

	public WorkerFactoryServer(String dir) {
		direccion = (dir == null) ? "" : dir;
	}

	public String getDireccion() {	return direccion;	}

	// Devuelve un vector de hasta n referencias a objetos Worker.
	public ArrayList<Worker> dameWorkers(int n) throws RemoteException {
		//Se crea la lista vacia que almacenara los workers solicitados
		ArrayList<Worker> workers = new ArrayList<Worker>();
		/*
		 * Se recuperan los nombres asociados al registro
		 */
		String[] nombresRegistro = registro.list();
		int numWorkers = 0;
		if (n > 0) {
			for (String nombre : nombresRegistro) {
				if (nombre.startsWith("Worker")) {
					try {
						//Se anyade worker al vector
						workers.add((Worker) registro.lookup(nombre));
					} catch (NotBoundException e) {
						e.printStackTrace();
					}
					numWorkers++;
					System.out.println("\nSe te ha dado Worker " + numWorkers);
					if (numWorkers == n)	break;
				}
			}
			//Devolver workers solicitados
			if (n == numWorkers) {
				System.out.println("\nWorkers solicitados dados.");
				return workers;
			}
			//Numero de workers solicitados demasiado grande
			else {
				System.err.println("\nNumero de workers solicitados demasiado grande.");
				return new ArrayList<Worker> ();
			}
		}
		//Cuando piden un numero de workers <=0
		else {
			System.err.println("\nNumero de workers solicitados demasiado pequenyo.");
			return new ArrayList<Worker> ();
		}
	}

	public static void ejecutar(String host) {
		try {
			if (host == null) {
				System.out.println("El registro RMI se encuentra en port 1099");
			}
			else	System.out.println("El registro RMI se encuentra en " + host);
			WorkerFactoryServer obj = new WorkerFactoryServer(host);
			WorkerFactory stub = (WorkerFactory) UnicastRemoteObject.exportObject(obj, 0);
			/*
			 * Si host tiene valor null, el registro se busca en localhost:1099
			 * Si es distinto de null, el registro se busca en esa maquina
			 */
			registro = LocateRegistry.getRegistry(host);
			/*
			 * Se asocia un nombre al objeto remoto creado
			 */
			registro.bind("Factory", stub);
			System.out.println("Factory");
			System.err.println("FactoryServer ready");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en FactoryServer");
		}
	}
}