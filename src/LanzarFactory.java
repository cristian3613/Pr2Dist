
public class LanzarFactory {

	public static void main(String[] args) {
		String host = (args.length < 2) ? null : args[1];
		//if (args[0] == "-a") {
			WorkerFactoryServer.ejecutar(host);
		//}
	}

}
