import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface WorkerFactory extends Remote {
// Devuelve un vector con los primos entre min y max.
java.util.ArrayList<Integer> encuentraPrimos(int min, int max)
throws RemoteException;
}