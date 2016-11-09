import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Worker extends Remote {
	//Devuelve un vector con los primos entre min y max
	ArrayList<Integer> encuentraPrimos(int min, int max,
			ArrayList<Integer> primosCandidatos)	throws RemoteException;
	ArrayList<Integer> encuentraPrimos2(int min, int max)
			throws RemoteException;
}
