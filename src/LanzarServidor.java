import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class LanzarServidor {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		String host = (args.length < 2) ? null : args[1];
		//if (args[0] == "-c") {
			WorkerServer.ejecutar(host);
		//}
	}

}
