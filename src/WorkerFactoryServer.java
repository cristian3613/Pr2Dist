import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class WorkerFactoryServer implements WorkerFactory {

	Registry registro;

	// Devuelve un vector de hasta n referencias a objetos Worker.
	public ArrayList<Worker> dameWorkers(int n) throws RemoteException {
			/*
			 * Se recuperan los nombres asociados al registro
			 */
			String[] nombresRegistro = registro.list();
			ArrayList <WorkerServer> workers = new ArrayList<Worker>();
			int numWorkers = 0;
			for (String nombre : nombresRegistro) {
				if (nombre.startsWith("Worker")) {
					workers.add(registro.lookup(nombre));
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
				System.out.println("Se ejecuta el WorkerServer en puerto 1099");
			}
			else	System.out.println("Se ejecuta en " + host);
			WorkerServer obj = new WorkerServer(host);
			Worker stub = (Worker) UnicastRemoteObject.exportObject(obj, 0);
			/*
			 * Si host tiene valor null, el registro se busca en localhost:1099
			 * Si es distinto de null, el registro se busca en esa maquina
			 */
			registro = LocateRegistry.getRegistry(host);
			/*
			 * Se asocia un nombre al objeto remoto creado
			 */
			registro.bind("WorkerFactory ", stub);
			System.out.println("WorkerFactory ");
			System.err.println("Server ready");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en WorkerServer");
		}
	}
}