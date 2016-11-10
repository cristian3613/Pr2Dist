import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * adknafknfsgffg
 * @author victorlafajasanz y cristian
 *
 */
public class Main {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		if (args.length != 0) {
			String host;
			//Se guarda el primer parametro
			String queEjecuto = args[0];
			/*
			 * Ejecuta un servidor de calculo (WorkerServer)
			 */
			if (queEjecuto.equals("-c")) {
				host = (args.length < 2) ? null : args[1];
				System.setProperty("java.rmi.server.hostname", host);
				System.out.println("\nSe va a lanzar un servidor de calculo...");
				WorkerServer.ejecutar(host);
			}
			/*
			 * Ejecuta un servidor de asignacion (WorkerFactory)
			 */
			else if (queEjecuto.equals("-a")) {
				host = (args.length < 2) ? null : args[1];
				System.setProperty("java.rmi.server.hostname", host);
				System.out.println("\nSe va a lanzar el servidor de asignacion...");
				WorkerFactoryServer.ejecutar(host);
			}
			/*
			 * Ejecuta un cliente (Cliente)
			 */
			else if (queEjecuto.equals("-u")) {
				host = (args.length < 5) ? null : args[4];
				System.setProperty("java.rmi.server.hostname", host);
				int min = Integer.parseInt(args[1]);
				int max = Integer.parseInt(args[2]);
				int n = Integer.parseInt(args[3]);
				Cliente.ejecutar(min, max, n, host);
			}
			else {
				System.err.println("Parametro introducido no valido");
				System.exit(1);
			}
		}
		else {
			System.err.println("El numero de parametros no es valido");
			System.exit(1);
		}
	}

}
