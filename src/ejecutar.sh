java -classpath bin -Djava.rmi.server.codebase=file:bin/ LanzarWorker &
java -classpath bin -Djava.rmi.server.codebase=file:bin/ LanzarFactory &
java -classpath bin Cliente 1 25 1
