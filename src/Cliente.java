import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Cliente {

	public static void main(String[] args) throws RemoteException, NotBoundException {
		String host = (args.length < 2) ? null : args[1];
		Registry registry = LocateRegistry.getRegistry(host);
        Worker stub = (Worker) registry.lookup("Worker 1");
        ArrayList<Integer> response = stub.encuentraPrimos(1, 15);
	}

}
