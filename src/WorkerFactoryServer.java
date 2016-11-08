import java.rmi.NotBoundException;
import java.rmi.Remote;
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
		ArrayList<Worker> workers = new ArrayList<Worker>();
		/*
		 * Se recuperan los nombres asociados al registro
		 */
		String[] nombresRegistro = registro.list();
		int numWorkers = 0;
		for (String nombre : nombresRegistro) {
			if (nombre.startsWith("Worker")) {
				try {
					workers.add((Worker) registro.lookup(nombre));
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				numWorkers++;
			}

		}

		if (n <= numWorkers) {	//Devolver workers solicitados
			return workers;
		}
		else{	//Numero de workers solicitados demasiado grande
			return new ArrayList<Worker> ();
		}
	}

	public static void ejecutar(String host) {
		try {
			if (host == null) {
				System.out.println("Se ejecuta el FactoryServer en puerto 1099");
			}
			else	System.out.println("Se ejecuta en " + host);
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
			System.err.println("Server ready");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en FactoryServer");
		}
	}
}